// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.impl;

import fr.centralesupelec.edf.riseclipse.iec61850.scl.SclPackage;
import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.impl.EPackageRegistryImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.Diagnostician;
import org.eclipse.emf.ecore.util.EObjectValidator;
import org.eclipse.ocl.pivot.utilities.OCL;
import org.eclipse.ocl.pivot.validation.ComposedEValidator;
import org.lfenergy.compas.scl.extensions.model.SclFileType;
import org.lfenergy.compas.scl.validator.SclValidator;
import org.lfenergy.compas.scl.validator.collector.OclFileCollector;
import org.lfenergy.compas.scl.validator.collector.OclFileUtil;
import org.lfenergy.compas.scl.validator.exception.SclValidatorException;
import org.lfenergy.compas.scl.validator.model.ValidationError;
import org.lfenergy.compas.scl.validator.resource.SclModelLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static org.lfenergy.compas.scl.validator.exception.SclValidatorErrorCode.OCL_MODEL_PACKAGE_NOT_FOUND;

@ApplicationScoped
public class SclRiseClipseValidator implements SclValidator {
    private static final Logger LOGGER = LoggerFactory.getLogger(SclRiseClipseValidator.class);

    private final List<URI> oclFiles = new ArrayList<>();

    @Inject
    public SclRiseClipseValidator(OclFileCollector oclFileCollector) {
        this.oclFiles.addAll(oclFileCollector.getOclFiles());

        // Check if the SclPackage can be initialized.
        var sclPck = SclPackage.eINSTANCE;
        if (sclPck == null) {
            throw new SclValidatorException(OCL_MODEL_PACKAGE_NOT_FOUND, "SCL package not found");
        }
    }

    @Override
    public List<ValidationError> validate(SclFileType type, String sclData) {
        // Create an EPackage.Registry for just the EXTLibraryPackage
        var registry = new EPackageRegistryImpl();
        registry.put(SclPackage.eNS_URI, SclPackage.eINSTANCE);

        // Create an OCL that creates a ResourceSet using the minimal EPackage.Registry
        var ocl = OCL.newInstance(registry);

        var validator = ComposedEValidator.install(SclPackage.eINSTANCE);
        if (!oclFiles.isEmpty()) {
            RiseClipseOCLValidator riseClipseOclValidator = new RiseClipseOCLValidator(ocl);
            oclFiles.stream()
                    .filter(uri -> OclFileUtil.includeOnType(uri, type))
                    .forEach(riseClipseOclValidator::addOCLDocument);
            riseClipseOclValidator.prepareValidator(validator);
        }

        var sclLoader = new SclModelLoader(ocl);
        var resource = sclLoader.load(sclData);

        var validationErrors = new ArrayList<ValidationError>();
        // The resource should have only one root element, an SCL object.
        // If there are other objects, it means that something is wrong in the SCL file
        // And it is useless to try to validate them.
        if (resource != null) {
            LOGGER.info("Validating SCL Data for type '{}'.", type);
            var diagnostician = new CompasDiagnostician();
            var diagnostic = diagnostician.validate(resource);

            for (Diagnostic childDiagnostic : diagnostic.getChildren()) {
                var validationError = new ValidationError();
                validationErrors.add(validationError);

                String message = childDiagnostic.getMessage();
                validationError.setMessage(message);
                LOGGER.debug("SCL Validation Error '{}'", message);
            }
        }

        return validationErrors;
    }

    private static class CompasDiagnostician extends Diagnostician {
        public BasicDiagnostic createDefaultDiagnostic(Resource resource) {
            return new BasicDiagnostic(EObjectValidator.DIAGNOSTIC_SOURCE, 0, "", new Object[]{resource});
        }

        public Diagnostic validate(Resource resource) {
            BasicDiagnostic diagnostics = createDefaultDiagnostic(resource);
            for (EObject eObject : resource.getContents()) {
                validate(eObject, diagnostics);
            }
            return diagnostics;
        }
    }

}

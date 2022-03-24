// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.impl;

import fr.centralesupelec.edf.riseclipse.iec61850.scl.SclPackage;
import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.impl.EValidatorRegistryImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.Diagnostician;
import org.eclipse.emf.ecore.util.EObjectValidator;
import org.eclipse.ocl.pivot.validation.ComposedEValidator;
import org.lfenergy.compas.scl.extensions.model.SclFileType;
import org.lfenergy.compas.scl.validator.SclValidator;
import org.lfenergy.compas.scl.validator.collector.OclFileCollector;
import org.lfenergy.compas.scl.validator.model.ValidationError;
import org.lfenergy.compas.scl.validator.util.OclUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.lfenergy.compas.scl.validator.util.MessageUtil.cleanupMessage;

public class SclRiseClipseValidator implements SclValidator {
    private static final Logger LOGGER = LoggerFactory.getLogger(SclRiseClipseValidator.class);

    private final List<URI> oclFiles = new ArrayList<>();
    private final Path tempDirectory;

    public SclRiseClipseValidator(OclFileCollector oclFileCollector, Path tempDirectory) {
        this.oclFiles.addAll(oclFileCollector.getOclFiles());
        this.tempDirectory = tempDirectory;

        // Initialize the OCL Libraries
        OclUtil.setupOcl();
    }

    @Override
    public List<ValidationError> validate(SclFileType type, String sclData) {
        // List with Validation Error Results if there are any.
        var validationErrors = new ArrayList<ValidationError>();

        XSDValidator.prepare(validationErrors, "/Users/rob/Code/CoMPAS/compas-scl-validator/scl2007b/target/xsd/SCL2007B/SCL.xsd");
        XSDValidator.validate(sclData);

        if (!validationErrors.isEmpty()) return validationErrors;

        // Create the validator and prepare it with the OCL Files.
        var validatorRegistry = new EValidatorRegistryImpl();
        var validator = new ComposedEValidator(null);
        validatorRegistry.put(SclPackage.eINSTANCE, validator);

        OclFileLoader oclFileLoader = new OclFileLoader(tempDirectory, oclFiles);
        try {
            // Load all the OCL Files, adding them to the OCL Instance.
            LOGGER.info("Loading OCL Files for type '{}'.", type);
            oclFileLoader.loadOCLDocuments(type);
            oclFileLoader.prepareValidator(validator);

            // Load the SCL File as Resource ready to be processed.
            LOGGER.info("Loading SCL Data for type '{}'.", type);
            var sclLoader = new SclModelLoader();
            var resource = sclLoader.load(sclData);

            LOGGER.info("Validating SCL Data for type '{}'.", type);
            var diagnostician = new CompasDiagnostician(validatorRegistry);
            var diagnostic = diagnostician.validate(resource);
            processDiagnostic(diagnostic, validationErrors);
        } finally {
            oclFileLoader.cleanup();
        }

        return validationErrors;
    }

    private void processDiagnostic(Diagnostic diagnostic, List<ValidationError> validationErrors) {
        // If there are children in the diagnostic there are validation errors to be processed.
        for (Diagnostic childDiagnostic : diagnostic.getChildren()) {
            var validationError = new ValidationError();
            validationErrors.add(validationError);

            String message = cleanupMessage(childDiagnostic.getMessage());
            validationError.setMessage(message);
            LOGGER.debug("SCL Validation Error '{}'", message);

            // Also process the children of the children.
            processDiagnostic(childDiagnostic, validationErrors);
        }
    }

    /**
     * Simple extension of the Diagnostician to make working with the Resource easier.
     */
    private static class CompasDiagnostician extends Diagnostician {
        public CompasDiagnostician(Registry eValidatorRegistry) {
            super(eValidatorRegistry);
        }

        /**
         * Create a basic diagnostic instance from the resource.
         *
         * @param resource The Resource to be processed.
         * @return The Diagnostic to which the results are added.
         */
        public BasicDiagnostic createDefaultDiagnostic(Resource resource) {
            return new BasicDiagnostic(EObjectValidator.DIAGNOSTIC_SOURCE, 0, "", new Object[]{resource});
        }

        /**
         * Validate the passed Resource.
         *
         * @param resource The Resource to be validated.
         * @return The Diagnostic containing the results of the validation.
         */
        public Diagnostic validate(Resource resource) {
            BasicDiagnostic diagnostics = createDefaultDiagnostic(resource);
            for (EObject eObject : resource.getContents()) {
                super.validate(eObject, diagnostics);
            }
            return diagnostics;
        }
    }
}

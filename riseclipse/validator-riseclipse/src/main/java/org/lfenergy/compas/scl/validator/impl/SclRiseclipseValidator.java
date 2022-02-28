// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.impl;

import fr.centralesupelec.edf.riseclipse.iec61850.scl.SCL;
import fr.centralesupelec.edf.riseclipse.iec61850.scl.SclPackage;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.Diagnostician;
import org.eclipse.ocl.pivot.validation.ComposedEValidator;
import org.lfenergy.compas.scl.extensions.model.SclFileType;
import org.lfenergy.compas.scl.validator.SclValidator;
import org.lfenergy.compas.scl.validator.exception.SclValidatorException;
import org.lfenergy.compas.scl.validator.model.ValidationError;
import org.lfenergy.compas.scl.validator.resource.OclFileCollector;
import org.lfenergy.compas.scl.validator.resource.SclModelLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.*;

@ApplicationScoped
public class SclRiseclipseValidator implements SclValidator {
    private static final Logger LOGGER = LoggerFactory.getLogger(SclRiseclipseValidator.class);

    private final List<URI> oclFiles = new ArrayList<>();
    private final SclModelLoader sclLoader;

    @Inject
    public SclRiseclipseValidator(OclFileCollector oclFileCollector) {
        this.oclFiles.addAll(oclFileCollector.getDefaultOclFiles());
        this.sclLoader = new SclModelLoader();

        SclPackage sclPck = SclPackage.eINSTANCE;
        if (sclPck == null) {
            throw new SclValidatorException("", "SCL package not found");
        }
    }

    @Override
    public List<ValidationError> validate(SclFileType type, String sclData) {
        ComposedEValidator validator = ComposedEValidator.install(SclPackage.eINSTANCE);
        if (!oclFiles.isEmpty()) {
            OCLValidator oclValidator = new OCLValidator(SclPackage.eINSTANCE);

            for (int i = 0; i < oclFiles.size(); ++i) {
                oclValidator.addOCLDocument(oclFiles.get(i));
            }
            oclValidator.prepareValidator(validator);
        }

        Resource resource = sclLoader.load(sclData);
        if (resource != null) {
            LOGGER.info("Validating SCL Data.");
            if (resource.getContents().isEmpty())
                return Collections.emptyList();

            Map<Object, Object> context = new HashMap<Object, Object>();
//            EValidator.SubstitutionLabelProvider substitutionLabelProvider = new EValidator.SubstitutionLabelProvider() {
//
//                @Override
//                public String getValueLabel(EDataType eDataType, Object value ) {
//                    return Diagnostician.INSTANCE.getValueLabel( eDataType, value );
//                }
//
//                @Override
//                public String getObjectLabel( EObject eObject ) {
//                    // plugin.properties files are not included in a fat jar when it is created
//                    // with Export… → Java → Runnable JAR file, leading to IllegalArgumentException.
//                    // If a string is missing, this is MissingResourceException
//                    // A NPE may also happen if eObject has no label provider (not an object of our metamodels)
//                    try {
//                        IItemLabelProvider labelProvider = ( IItemLabelProvider ) adapter.adapt( eObject,
//                                IItemLabelProvider.class );
//                        return labelProvider.getText( eObject );
//                    }
//                    catch( NullPointerException | IllegalArgumentException | MissingResourceException ex ) {
//                        return eObject.eClass().getName();
//                    }
//                }
//
//                @Override
//                public String getFeatureLabel( EStructuralFeature eStructuralFeature ) {
//                    return Diagnostician.INSTANCE.getFeatureLabel( eStructuralFeature );
//                }
//            };
//            context.put( EValidator.SubstitutionLabelProvider.class, substitutionLabelProvider );

            // The resource should have only one root element, an SCL object.
            // If there are other objects, it means that something is wrong in the SCL file
            // and it is useless to try to validate them.
            if (resource.getContents().get(0) instanceof SCL) {
                Diagnostic diagnostic = Diagnostician.INSTANCE.validate(resource.getContents().get(0), context);

                for (Diagnostic childDiagnostic : diagnostic.getChildren()) {
                    List<?> data = childDiagnostic.getData();
                    EObject object = (EObject) data.get(0);
                    String message = childDiagnostic.getMessage();
                    if ((data.size() > 1) && (data.get(1) instanceof EAttribute) && (!childDiagnostic.getChildren().isEmpty())) {
                        EAttribute attribute = (EAttribute) data.get(1);
                        if (attribute == null) continue;
                        message = "Attribute " + attribute.getName() + " : "
                                + childDiagnostic.getChildren().get(0).getMessage();
                    }
                    LOGGER.info("SCL Validation Error '{}'", message);
                }
            }
        }


        return Collections.emptyList();
    }
}

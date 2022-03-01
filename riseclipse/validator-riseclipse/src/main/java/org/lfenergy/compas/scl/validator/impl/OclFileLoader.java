// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.impl;

import fr.centralesupelec.edf.riseclipse.iec61850.scl.SclPackage;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource.Diagnostic;
import org.eclipse.ocl.pivot.resource.CSResource;
import org.eclipse.ocl.pivot.utilities.OCL;
import org.eclipse.ocl.pivot.validation.ComposedEValidator;
import org.eclipse.ocl.xtext.completeocl.validation.CompleteOCLEObjectValidator;
import org.lfenergy.compas.scl.validator.exception.SclValidatorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import static org.lfenergy.compas.scl.validator.exception.SclValidatorErrorCode.*;

public class OclFileLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(OclFileLoader.class);

    private final Path oclTempFile;
    private final OCL ocl;

    public OclFileLoader(OCL ocl) {
        this.ocl = ocl;

        // *.ocl Complete OCL documents support required
        org.eclipse.ocl.xtext.completeocl.CompleteOCLStandaloneSetup.doSetup();
        // *.oclstdlib OCL Standard Library support required
        org.eclipse.ocl.xtext.oclstdlib.OCLstdlibStandaloneSetup.doSetup();

        try {
            oclTempFile = Files.createTempFile("allConstraints", ".ocl");
        } catch (IOException exp) {
            throw new SclValidatorException(CREATE_OCL_TEMP_FILES_FAILED, "Unable to create temporary file", exp);
        }
    }

    public void addOCLDocument(URI oclUri) {
        if (oclUri == null) {
            throw new SclValidatorException(NO_URI_PASSED, "Unable to create URI for temporary file");
        }

        // We want to check the validity of OCL files
        // So, we have to do it now, before concatenating it to oclTempFile
        CSResource oclResource;
        try {
            oclResource = ocl.getCSResource(oclUri);

            if (!oclResource.getErrors().isEmpty()) {
                StringBuilder message = new StringBuilder("Syntax error in '" + oclUri + "':\n");
                EList<Diagnostic> errors = oclResource.getErrors();
                for (Diagnostic error : errors) {
                    message.append("Error: ").append(error.getMessage()).append("\n");
                }
                LOGGER.error(message.toString());
            } else {
                try {
                    BufferedWriter o = Files.newBufferedWriter(oclTempFile, StandardOpenOption.APPEND);
                    o.write("import '" + oclUri + "'\n");
                    o.close();
                } catch (IOException exp) {
                    throw new SclValidatorException(WRITE_TO_OCL_TEMP_FILES_FAILED, "Unable to write temporary OCL file", exp);
                }
            }
        } catch (IOException exp) {
            LOGGER.error("Unable to read OCL file '{}'", oclUri, exp);
        }
    }

    public void prepareValidator(ComposedEValidator validator) {
        URI uri = URI.createFileURI(oclTempFile.toFile().getAbsolutePath());
        if (uri == null) {
            throw new SclValidatorException(PREPARE_OCL_TEMP_FILES_FAILED, "Unable to create URI for temporary file");
        }
        CompleteOCLEObjectValidator oclValidator = new CompleteOCLEObjectValidator(SclPackage.eINSTANCE, uri);
        validator.addChild(oclValidator);
    }

    public void cleanup() {
        oclTempFile.toFile().delete();
    }
}

// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.impl;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
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

public class OCLValidator {
    private static final Logger LOGGER = LoggerFactory.getLogger(OCLValidator.class);

    private EPackage modelPackage;
    private Path oclTempFile;
    private OCL ocl;

    public OCLValidator(EPackage modelPackage) {
        this.modelPackage = modelPackage;
        // *.ocl Complete OCL documents support required
        org.eclipse.ocl.xtext.completeocl.CompleteOCLStandaloneSetup.doSetup();
        // *.oclstdlib OCL Standard Library support required
        org.eclipse.ocl.xtext.oclstdlib.OCLstdlibStandaloneSetup.doSetup();

        try {
            oclTempFile = Files.createTempFile("allConstraints", ".ocl");
        } catch (IOException exp) {
            throw new SclValidatorException("", "Unable to create temporary file", exp);
        }

        ocl = OCL.newInstance(OCL.NO_PROJECTS);
    }

    public boolean addOCLDocument(URI oclUri) {
        if (oclUri == null) {
            throw new SclValidatorException("", "Unable to create URI for temporary file");
        }

        // We want to check the validity of OCL files
        // So, we have to do it now, before concatenating it to oclTempFile
        CSResource oclResource = null;
        try {
            LOGGER.debug("Loading OCL File '{}'.", oclUri);
            oclResource = ocl.getCSResource(oclUri);
        } catch (IOException exp) {
            throw new SclValidatorException("", "Unable to read OCL file", exp);
        }
        if (!oclResource.getErrors().isEmpty()) {
            LOGGER.error("syntax error in " + oclUri + " (it will be ignored):");
            EList<Diagnostic> errors = oclResource.getErrors();
            for (Diagnostic error : errors) {
                LOGGER.error("  " + error.getMessage());
            }
            return false;
        }

        try {
            BufferedWriter o = Files.newBufferedWriter(oclTempFile, StandardOpenOption.APPEND);
            o.write("import \'file:" + oclUri + "\'\n");
            o.close();
        } catch (IOException exp) {
            throw new SclValidatorException("", "Unable to write temporary OCL file", exp);
        }
        return true;
    }

    public void prepareValidator(ComposedEValidator validator) {
        URI uri = URI.createFileURI(oclTempFile.toFile().getAbsolutePath());
        if (uri == null) {
            throw new SclValidatorException("", "Unable to create URI for temporary file");
        }
        CompleteOCLEObjectValidator oclValidator = new CompleteOCLEObjectValidator(modelPackage, uri);
        validator.addChild(oclValidator);
    }
}

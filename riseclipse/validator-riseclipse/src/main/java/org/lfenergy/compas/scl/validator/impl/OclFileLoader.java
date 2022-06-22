// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.impl;

import fr.centralesupelec.edf.riseclipse.iec61850.scl.SclPackage;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.impl.EPackageRegistryImpl;
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
import java.util.List;

import static org.lfenergy.compas.scl.validator.exception.SclValidatorErrorCode.*;

public class OclFileLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(OclFileLoader.class);

    private final List<URI> oclFiles;
    private final Path oclTempFile;
    private final OCL ocl;

    public OclFileLoader(Path tempDirectoryPath, List<URI> oclFiles) {
        this.oclFiles = oclFiles;

        // Create an EPackage.Registry for the SclPackage.
        var registry = new EPackageRegistryImpl();
        registry.put(SclPackage.eNS_URI, SclPackage.eINSTANCE);
        // Create an OCL that creates a ResourceSet using the minimal EPackage.Registry
        this.ocl = OCL.newInstance(registry);

        // First make sure the directory for temporary file exists.
        var tempDirectory = tempDirectoryPath.toFile();
        if (!tempDirectory.exists() && !tempDirectory.mkdirs()) {
            throw new SclValidatorException(CREATE_OCL_TEMP_DIR_FAILED, "Unable to create temporary directory");
        }

        try {
            oclTempFile = Files.createTempFile(tempDirectoryPath, "allConstraints", ".ocl");
        } catch (IOException exp) {
            throw new SclValidatorException(CREATE_OCL_TEMP_FILES_FAILED, "Unable to create temporary file", exp);
        }
    }

    public void loadOCLDocuments() {
        oclFiles.stream()
                .forEach(this::addOCLDocument);
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
                logErrorMessage(oclUri, oclResource.getErrors());
            } else {
                appendToTempFile(oclUri);
            }
        } catch (IOException exp) {
            LOGGER.error("Unable to read OCL file '{}'", oclUri, exp);
        }
    }

    private void appendToTempFile(URI oclUri) {
        try {
            BufferedWriter o = Files.newBufferedWriter(oclTempFile, StandardOpenOption.APPEND);
            o.write("import '" + oclUri + "'\n");
            o.close();
        } catch (IOException exp) {
            throw new SclValidatorException(WRITE_TO_OCL_TEMP_FILES_FAILED, "Unable to write temporary OCL file", exp);
        }
    }

    private void logErrorMessage(URI oclUri, List<Diagnostic> errors) {
        StringBuilder message = new StringBuilder("Syntax error in '" + oclUri + "':\n");
        for (Diagnostic error : errors) {
            message.append("Error: ").append(error.getMessage()).append("\n");
        }
        if (LOGGER.isErrorEnabled()) {
            LOGGER.error(message.toString());
        }
    }

    public void prepareValidator(ComposedEValidator validator) {
        URI uri = URI.createFileURI(oclTempFile.toFile().getAbsolutePath());
        CompleteOCLEObjectValidator oclValidator = new CompleteOCLEObjectValidator(SclPackage.eINSTANCE, uri);
        validator.addChild(oclValidator);
    }

    public void cleanup() {
        ocl.dispose();

        try {
            if (!Files.deleteIfExists(oclTempFile)) {
                LOGGER.warn("Unable to remove temporary file '{}'.", oclTempFile);
            }
        } catch (IOException exp) {
            LOGGER.warn("Unable to remove temporary file '{}'.", oclTempFile, exp);
        }
    }
}

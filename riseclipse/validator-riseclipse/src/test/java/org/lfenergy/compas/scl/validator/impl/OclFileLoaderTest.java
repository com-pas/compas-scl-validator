// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.impl;

import org.eclipse.emf.common.util.URI;
import org.eclipse.ocl.pivot.validation.ComposedEValidator;
import org.eclipse.ocl.xtext.completeocl.validation.CompleteOCLEObjectValidator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.lfenergy.compas.scl.validator.exception.SclValidatorException;
import org.lfenergy.compas.scl.validator.util.OclUtil;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.lfenergy.compas.scl.validator.exception.SclValidatorErrorCode.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OclFileLoaderTest {
    private OclFileLoader loader;
    private Path tempFile;

    @BeforeEach
    void setup() throws IOException {
        // Initialize the OCL Libraries
        OclUtil.setupOcl();

        var tempDirectory = "./target/data/temp";
        var tempDirectoryPath = Path.of(tempDirectory);
        var oclFile = findOCL("example.ocl");

        loader = new OclFileLoader(tempDirectoryPath, List.of(oclFile));
        tempFile = Files.walk(tempDirectoryPath)
                .filter(path -> path.toString().contains(File.separator + "allConstraints"))
                .findFirst()
                .orElseThrow();
    }

    @Test
    void constructor_WhenCalledWithUnCreatableDirectory_ThenExceptionThrown() {
        var unCreatablePath = Path.of("/some-directory");
        if (isWindows()) {
            unCreatablePath = Path.of("C:/Windows/some-directory");
        }

        List<URI> fileList = List.of();
        var directoryPath = unCreatablePath;
        var exception = assertThrows(SclValidatorException.class, () -> new OclFileLoader(directoryPath, fileList));

        assertEquals(CREATE_OCL_TEMP_DIR_FAILED, exception.getErrorCode());
    }

    @Test
    void constructor_WhenCalledWithUnCreatableTempFile_ThenExceptionThrown() {
        var unWritablePath = Path.of("/");
        if (isWindows()) {
            unWritablePath = Path.of("C:/Windows");
        }

        List<URI> fileList = List.of();
        var directoryPath = unWritablePath;
        var exception = assertThrows(SclValidatorException.class, () -> new OclFileLoader(directoryPath, fileList));

        assertEquals(CREATE_OCL_TEMP_FILES_FAILED, exception.getErrorCode());
    }

    private boolean isWindows() {
        return System.getProperty("os.name").contains("win");
    }

    @Test
    void loadOCLDocuments_WhenCalled_ThenFilesFromListAreLoaded() throws IOException {
        loader.loadOCLDocuments();

        assertEquals(1, Files.lines(tempFile).count());
    }

    @Test
    void addOCLDocument_WhenCalledWithNull_ThenExceptionThrown() {
        var exception = assertThrows(SclValidatorException.class,
                () -> loader.addOCLDocument(null));

        assertEquals(NO_URI_PASSED, exception.getErrorCode());
    }

    @Test
    void addOCLDocument_WhenCalledWithValidOcl_ThenOclFileIsAddedToTempFile() throws IOException {
        var oclFile = findOCL("example.ocl");

        loader.addOCLDocument(oclFile);

        assertEquals(1, Files.lines(tempFile).count());
    }

    @Test
    void addOCLDocument_WhenCalledWithInvalidOcl_ThenOclFileIsNotAddedToTempFile() throws IOException {
        var oclFile = findOCL("invalid.ocl");

        loader.addOCLDocument(oclFile);

        assertEquals(0, Files.lines(tempFile).count());
    }

    @Test
    void prepareValidator_whenCalledWithValidator_ThenValidatorIsAdded() {
        var validator = mock(ComposedEValidator.class);
        loader.prepareValidator(validator);

        verify(validator).addChild(any(CompleteOCLEObjectValidator.class));
    }

    @AfterEach
    void cleanup() {
        loader.cleanup();
    }

    private URI findOCL(String filename) {
        var url = getClass().getResource("/ocl-testfiles/" + filename);
        if (url != null) {
            return URI.createFileURI(url.getPath());
        }
        throw new NullPointerException("File /ocl/" + filename + " not found!");
    }
}
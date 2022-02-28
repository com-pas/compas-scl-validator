// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.collector;

import org.eclipse.emf.common.util.URI;
import org.lfenergy.compas.scl.validator.exception.SclValidatorException;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.lfenergy.compas.scl.validator.exception.SclValidatorErrorCode.LOADING_CUSTOM_OCL_FILES_FAILED;
import static org.lfenergy.compas.scl.validator.exception.SclValidatorErrorCode.LOADING_OCL_FILES_FAILED;

/**
 * Abstract class to support retrieving default files from the ClassPath or from a Directory.
 */
public abstract class AbstractFileCollector implements OclFileCollector {
    /**
     * Search for all files (with extension ocl) in the directory 'ocl' on the classpath.
     *
     * @return The List of Default OCL Files as URI that Compas uses to validate with RiseClipse.
     * @throws SclValidatorException Thrown when there is some I/O or URI Syntax Error.
     */
    protected List<URI> getDefaultOclFilesFromClasspath() {
        Function<Path, Boolean> filter = path -> path.toString().endsWith(".ocl");

        try {
            var resource = getClass().getResource("/ocl");
            if (resource != null) {
                var uri = resource.toURI();
                // The directory is in a JAR, search will be different.
                if (uri.getScheme().equals("jar")) {
                    try (FileSystem fileSystem = FileSystems.newFileSystem(uri, Collections.emptyMap())) {
                        var oclDirectoryPath = fileSystem.getPath("/ocl");
                        return Files.walk(oclDirectoryPath)
                                .filter(filter::apply)
                                .map(Path::toString)
                                .map(URI::createFileURI)
                                .collect(Collectors.toList());
                    }
                } else {
                    var oclDirectoryPath = Paths.get(uri);
                    return Files.walk(oclDirectoryPath)
                            .filter(filter::apply)
                            .map(Path::toFile)
                            .filter(File::isFile)
                            .map(file -> URI.createFileURI(file.getAbsolutePath()))
                            .collect(Collectors.toList());
                }
            }
            return Collections.emptyList();
        } catch (URISyntaxException | IOException exp) {
            throw new SclValidatorException(LOADING_OCL_FILES_FAILED, "Error loading OCL Files", exp);
        }
    }

    /**
     * Search (recursively) for all files in the directory passed. The filter can be used to filter files from the List.
     *
     * @param directoryName The directory in which top search for files (recursively).
     * @param filter        The filter used to filter the list of file, use '(path) -> true' to return them all.
     * @return The list of Files as URI found.
     */
    protected List<URI> getFilesFromDirectory(String directoryName, Function<Path, Boolean> filter) {
        try {
            File directory = new File(directoryName);
            if (directory.exists() && directory.isDirectory()) {
                var oclDirectoryPath = Paths.get(directory.toURI());
                return Files.walk(oclDirectoryPath)
                        .filter(filter::apply)
                        .map(Path::toFile)
                        .filter(File::isFile)
                        .map(file -> URI.createFileURI(file.getAbsolutePath()))
                        .collect(Collectors.toList());
            }
            return Collections.emptyList();
        } catch (IOException exp) {
            throw new SclValidatorException(LOADING_CUSTOM_OCL_FILES_FAILED, "Error loading Custom OCL Files", exp);
        }
    }
}

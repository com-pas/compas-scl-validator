// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.resource;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractFileCollector {
    protected List<URI> getFiles(String directory, FilterPath filter) throws IOException, URISyntaxException {
        var resource = getClass().getResource(directory);
        if (resource != null) {
            var uri = resource.toURI();
            if (uri.getScheme().equals("jar")) {
                try (FileSystem fileSystem = FileSystems.newFileSystem(uri, Collections.emptyMap())) {
                    var oclDirectoryPath = fileSystem.getPath(directory);
                    return Files.walk(oclDirectoryPath)
                            .filter(filter::filter)
                            .map(Path::toUri)
                            .collect(Collectors.toList());
                }
            } else {
                var oclDirectoryPath = Paths.get(uri);
                return Files.walk(oclDirectoryPath)
                        .filter(filter::filter)
                        .map(Path::toFile)
                        .filter(File::isFile)
                        .map(File::toURI)
                        .collect(Collectors.toList());
            }
        }
        return Collections.emptyList();
    }

    protected interface FilterPath {
        default boolean filter(Path path) {
            return true;
        }
    }
}

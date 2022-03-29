// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.collector;

import org.eclipse.emf.common.util.URI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * OCL File collector which combines the default OCL Files from the Classpath with all files found in a custom
 * directory configured.
 */
public class CompasOclFileCollector extends AbstractFileCollector {
    private static final Logger LOGGER = LoggerFactory.getLogger(CompasOclFileCollector.class);

    private final String oclCustomDirectory;

    public CompasOclFileCollector(String oclCustomDirectory) {
        this.oclCustomDirectory = oclCustomDirectory;
    }

    @Override
    public List<URI> getOclFiles() {
        LOGGER.debug("Searching for OCL Files in classpath.");
        var oclFiles = new ArrayList<>(getDefaultOclFilesFromClasspath());

        if (oclCustomDirectory != null) {
            LOGGER.debug("Searching for OCL Files in custom directory '{}'.", oclCustomDirectory);
            oclFiles.addAll(getFilesFromDirectory(oclCustomDirectory, path -> path.toString().endsWith(".ocl")));
        }

        // logListOfFiles(oclFiles);
        return oclFiles;
    }

    /**
     * Log the list of files found during startup.
     *
     * @param oclFiles The List of Files found.
     */
    protected void logListOfFiles(ArrayList<URI> oclFiles) {
        if (oclFiles != null && !oclFiles.isEmpty()) {
            oclFiles.forEach(oclFile -> LOGGER.info("Found OCL File '{}'", oclFile));
        } else {
            LOGGER.warn("No OCL Files found!");
        }
    }
}

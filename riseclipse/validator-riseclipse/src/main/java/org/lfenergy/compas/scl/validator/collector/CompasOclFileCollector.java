// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.collector;

import org.eclipse.emf.common.util.URI;

import java.util.ArrayList;
import java.util.List;

/**
 * OCL File collector which combines the default OCL Files from the Classpath with all files found in a custom
 * directory configured.
 */
public class CompasOclFileCollector extends AbstractFileCollector {
    private final String oclCustomDirectory;

    public CompasOclFileCollector(String oclCustomDirectory) {
        this.oclCustomDirectory = oclCustomDirectory;
    }

    @Override
    public List<URI> getOclFiles() {
        var oclFiles = new ArrayList<URI>();
        oclFiles.addAll(getDefaultOclFilesFromClasspath());

        if (oclCustomDirectory != null) {
            oclFiles.addAll(getFilesFromDirectory(oclCustomDirectory, (path) -> path.toString().endsWith(".ocl")));
        }
        return oclFiles;
    }
}

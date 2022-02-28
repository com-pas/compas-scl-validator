// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.collector;

import org.eclipse.emf.common.util.URI;
import org.lfenergy.compas.scl.extensions.model.SclFileType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class OclFileUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(OclFileUtil.class);

    OclFileUtil() {
        throw new UnsupportedOperationException("OclFileUtil class");
    }

    public static boolean includeOnType(URI uri, SclFileType type) {
        var fullPath = uri.path();
        // OCL Files that are not in the directory 'FileSpecifics' will always be included.
        // In the directory 'FileSpecifics' only the OCL Files in the directory 'Common' and for the directory for the
        // requested SCL File, for instance 'CID', will be included.
        var include =
                fullPath.contains(File.separator + "FileSpecifics" + File.separator + type + File.separator)
                        || fullPath.contains(File.separator + "FileSpecifics" + File.separator + "Common" + File.separator)
                        || !fullPath.contains(File.separator + "FileSpecifics" + File.separator);
        LOGGER.debug("Full Path '{}' will be included: {}", fullPath, include);
        return include;
    }
}

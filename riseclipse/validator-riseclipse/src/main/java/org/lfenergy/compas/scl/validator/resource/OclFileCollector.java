// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.resource;

import org.eclipse.emf.common.util.URI;
import org.lfenergy.compas.scl.validator.exception.SclValidatorException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.List;

import static org.lfenergy.compas.scl.validator.exception.SclValidatorErrorCode.LOADING_OCL_FILES_FAILED;

public class OclFileCollector extends AbstractFileCollector {
    public List<URI> getDefaultOclFiles() {
        try {
            return getFiles("/ocl", new FilterOclExtension());
        } catch (URISyntaxException | IOException exp) {
            throw new SclValidatorException(LOADING_OCL_FILES_FAILED, "Error loading OCL Files", exp);
        }
    }

    private static class FilterOclExtension implements FilterPath {
        @Override
        public boolean filter(Path path) {
            return path.toString().endsWith(".ocl");
        }
    }
}

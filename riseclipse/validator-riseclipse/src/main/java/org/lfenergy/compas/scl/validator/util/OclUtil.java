// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.util;

import fr.centralesupelec.edf.riseclipse.iec61850.scl.SclPackage;
import org.lfenergy.compas.scl.validator.exception.SclValidatorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.lfenergy.compas.scl.validator.exception.SclValidatorErrorCode.OCL_MODEL_PACKAGE_NOT_FOUND;

public class OclUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(OclUtil.class);

    private static final String FILE_SPECIFICS_DIR_NAME = "FileSpecifics";
    private static final String COMMON_DIR_NAME = "Common";

    OclUtil() {
        throw new UnsupportedOperationException("OclFileUtil class");
    }

    public static void setupOcl() {
        // *.ocl Complete OCL documents support required
        org.eclipse.ocl.xtext.completeocl.CompleteOCLStandaloneSetup.doSetup();
        // *.oclstdlib OCL Standard Library support required
        org.eclipse.ocl.xtext.oclstdlib.OCLstdlibStandaloneSetup.doSetup();

        // Check if the SclPackage can be initialized.
        var sclPck = SclPackage.eINSTANCE;
        if (sclPck == null) {
            throw new SclValidatorException(OCL_MODEL_PACKAGE_NOT_FOUND, "SCL package not found");
        }
    }
}

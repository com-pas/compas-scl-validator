// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.exception;

public class SclValidatorErrorCode {
    SclValidatorErrorCode() {
        throw new UnsupportedOperationException("SclValidatorErrorCode class");
    }

    public static final String NO_SCL_ELEMENT_FOUND_ERROR_CODE = "SVS-0001";
    public static final String LOADING_SCL_FILE_ERROR_CODE = "SVS-0002";
    public static final String LOADING_XSD_FILE_ERROR_CODE = "SVS-0003";
    public static final String CREATE_XPATH_ELEMENT_ERROR_CODE = "SVS-0004";

    public static final String LOADING_OCL_FILES_FAILED = "SVS-1001";
    public static final String LOADING_CUSTOM_OCL_FILES_FAILED = "SVS-1002";

    public static final String CREATE_OCL_TEMP_FILES_FAILED = "SVS-2001";
    public static final String CREATE_OCL_TEMP_DIR_FAILED = "SVS-2002";
    public static final String WRITE_TO_OCL_TEMP_FILES_FAILED = "SVS-2003";
    public static final String OCL_MODEL_PACKAGE_NOT_FOUND = "SVS-2005";
    public static final String NO_URI_PASSED = "SVS-2006";
    public static final String RESOURCE_RESOLVER_FAILED = "SVS-2007";

    public static final String CALCULATING_CHECKSUM_FAILED = "SVS-3001";
    public static final String DETERMINING_ID_FAILED = "SVS-3002";
    public static final String LOADING_NSDOC_FILE_FAILED = "SVS-3003";
    public static final String NSDOC_FILE_NOT_FOUND = "SVS-3004";
}

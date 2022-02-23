// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.exception;

public class SclValidatorErrorCode {
    SclValidatorErrorCode() {
        throw new UnsupportedOperationException("SclValidatorErrorCode class");
    }

    public static final String NO_SCL_ELEMENT_FOUND_ERROR_CODE = "SVS-0001";
    public static final String LOADING_OCL_FILES_FAILED = "SVS-0002";
}

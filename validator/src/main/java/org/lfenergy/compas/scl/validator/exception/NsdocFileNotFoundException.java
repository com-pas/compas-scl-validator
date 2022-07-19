// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.exception;

import org.lfenergy.compas.core.commons.exception.CompasException;

import static org.lfenergy.compas.scl.validator.exception.SclValidatorErrorCode.NSDOC_FILE_NOT_FOUND;

public class NsdocFileNotFoundException extends CompasException {
    public NsdocFileNotFoundException(String message) {
        super(NSDOC_FILE_NOT_FOUND, message);
    }
}

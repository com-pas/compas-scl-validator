// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.exception;

import org.lfenergy.compas.core.commons.exception.CompasException;

public class SclValidatorException extends CompasException {
    public SclValidatorException(String errorCode, String message) {
        super(errorCode, message);
    }
}

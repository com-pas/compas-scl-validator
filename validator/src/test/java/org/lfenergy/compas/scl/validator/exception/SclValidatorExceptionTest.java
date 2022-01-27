// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.exception;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SclValidatorExceptionTest {
    @Test
    void constructor_WhenCalledWithOnlyMessage_ThenMessageCanBeRetrieved() {
        var expectedMessage = "The message";
        var exception = new SclValidatorException(SclValidatorErrorCode.NO_SCL_ELEMENT_FOUND_ERROR_CODE, expectedMessage);

        Assertions.assertEquals(SclValidatorErrorCode.NO_SCL_ELEMENT_FOUND_ERROR_CODE, exception.getErrorCode());
        Assertions.assertEquals(expectedMessage, exception.getMessage());
    }
}
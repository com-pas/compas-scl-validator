// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.exception;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class NsdocFileNotFoundExceptionTest {
    @Test
    void constructor_WhenCalledWithOnlyMessage_ThenMessageCanBeRetrieved() {
        var expectedMessage = "The message";
        var exception = new NsdocFileNotFoundException(expectedMessage);

        Assertions.assertEquals(SclValidatorErrorCode.NSDOC_FILE_NOT_FOUND, exception.getErrorCode());
        Assertions.assertEquals(expectedMessage, exception.getMessage());
    }
}
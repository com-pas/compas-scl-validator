// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.exception;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SclValidatorErrorCodeTest {
    @Test
    void constructor_WhenConstructorCalled_ThenShouldThrowExceptionCauseForbidden() {
        Assertions.assertThrows(UnsupportedOperationException.class, SclValidatorErrorCode::new);
    }

}
// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.lfenergy.compas.scl.validator.util.MessageUtil.createValidationError;

class MessageUtilTest {
    @Test
    void constructor_WhenConstructorCalled_ThenShouldThrowExceptionCauseForbidden() {
        assertThrows(UnsupportedOperationException.class, MessageUtil::new);
    }

    @Test
    void createValidationError_WhenCalledNullPassed_ThenEmptyOptionalReturned() {
        var result = createValidationError(null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void createValidationError_WhenCalledBlankStringPassed_ThenEmptyOptionalReturned() {
        var result = createValidationError("");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void createValidationError_WhenCalledWithoutParts_ThenSameMessageReturned() {
        var message = "Just some message";

        var result = createValidationError(message);

        assertNotNull(result);
        assertFalse(result.isEmpty());

        var validationError = result.get();
        assertNull(validationError.getRuleName());
        assertNull(validationError.getLinenumber());
        assertNull(validationError.getColumnNumber());
        assertEquals(message, validationError.getMessage());
    }

    @Test
    void createValidationError_WhenCalledWithTooManyParts_ThenSameMessageReturned() {
        var message = "ERROR;Part1;Part2;Part3;Part4;Just some message";

        var result = createValidationError(message);

        assertNotNull(result);
        assertFalse(result.isEmpty());

        var validationError = result.get();
        assertNull(validationError.getRuleName());
        assertNull(validationError.getLinenumber());
        assertNull(validationError.getColumnNumber());
        assertEquals(message, validationError.getMessage());
    }

    @Test
    void createValidationError_WhenCalledWithCorrectMessage_ThenConvertValidationErrorReturned() {
        var message = "AnyLN (lnType=LN2) does not refer an existing LNodeType in DataTypeTemplates section";
        var ruleName = "OCL/SemanticConstraints/AnyLN_RefersToLNodeType";
        var linenumber = 9;

        var result = createValidationError("ERROR;" + ruleName + ";scl-file.scd;" + linenumber + ";" + message);

        assertNotNull(result);
        assertFalse(result.isEmpty());

        var validationError = result.get();
        assertEquals(ruleName, validationError.getRuleName());
        assertEquals(linenumber, validationError.getLinenumber());
        assertNull(validationError.getColumnNumber());
        assertEquals(message, validationError.getMessage());
    }

    @Test
    void createValidationError_WhenCalledWithInvalidLinenumber_ThenNegativeLinenumberReturned() {
        var message = "AnyLN (lnType=LN2) does not refer an existing LNodeType in DataTypeTemplates section";
        var ruleName = "OCL/SemanticConstraints/AnyLN_RefersToLNodeType";

        var result = createValidationError("ERROR;" + ruleName + ";scl-file.scd;INVALID_LINENUMBER;" + message);

        assertNotNull(result);
        assertFalse(result.isEmpty());

        var validationError = result.get();
        assertEquals(ruleName, validationError.getRuleName());
        assertEquals(-1, validationError.getLinenumber());
        assertNull(validationError.getColumnNumber());
        assertEquals(message, validationError.getMessage());
    }
}

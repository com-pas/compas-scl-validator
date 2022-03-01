// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.impl;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.lfenergy.compas.scl.validator.impl.MessageUtil.cleanupMessage;

class MessageUtilTest {
    @Test
    void constructor_WhenConstructorCalled_ThenShouldThrowExceptionCauseForbidden() {
        assertThrows(UnsupportedOperationException.class, MessageUtil::new);
    }

    @Test
    void cleanupMessage_WhenCalledWithNullMessage_ThenNullIsReturned() {
        var result = cleanupMessage(null);

        assertNull(result);
    }

    @Test
    void cleanupMessage_WhenCalledWithAlreadyCleanMessage_ThenSameMessageIsReturned() {
        var expectedMessage = "Some validation message";

        var result = cleanupMessage(expectedMessage);

        assertEquals(expectedMessage, result);
    }

    @Test
    void cleanupMessage_WhenCalledWithMessageThatStartWithError_ThenCleanedMessageIsReturned() {
        var expectedMessage = "Some validation message";

        var result = cleanupMessage("ERROR:" + expectedMessage);

        assertEquals(expectedMessage, result);
    }
}

// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.rest.v1.websocket;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lfenergy.compas.scl.validator.exception.SclValidatorException;

import javax.xml.bind.UnmarshalException;

import static org.junit.jupiter.api.Assertions.*;
import static org.lfenergy.compas.scl.validator.SclValidatorConstants.SCL_VALIDATOR_SERVICE_V1_NS_URI;
import static org.lfenergy.compas.scl.validator.exception.SclValidatorErrorCode.WEBSOCKET_DECODER_ERROR_CODE;

class SclValidateResponseDecoderTest {
    private SclValidateResponseDecoder decoder;

    @BeforeEach
    void init() {
        decoder = new SclValidateResponseDecoder();
        decoder.init(null);
    }

    @Test
    void willDecode_WhenCalledWithString_ThenTrueReturned() {
        assertTrue(decoder.willDecode(""));
        assertTrue(decoder.willDecode("Some text"));
    }

    @Test
    void willDecode_WhenCalledWithNull_ThenFalseReturned() {
        assertFalse(decoder.willDecode(null));
    }

    @Test
    void decode_WhenCalledWithCorrectRequestXML_ThenStringConvertedToObject() {
        var validationMessage = "Some validation error";
        var ruleName = "Rule Name 1";
        var lineNumber = 15;
        var columnNumber = 34;

        var message = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + "<svs:SclValidateResponse xmlns:svs=\"" + SCL_VALIDATOR_SERVICE_V1_NS_URI + "\">"
                + "<svs:ValidationErrors>"
                + "<svs:Message>" + validationMessage + "</svs:Message>"
                + "<svs:RuleName>" + ruleName + "</svs:RuleName>"
                + "<svs:LineNumber>" + lineNumber + "</svs:LineNumber>"
                + "<svs:ColumnNumber>" + columnNumber + "</svs:ColumnNumber>"
                + "</svs:ValidationErrors>"
                + "</svs:SclValidateResponse>";

        var result = decoder.decode(message);

        assertNotNull(result);
        assertEquals(1, result.getValidationErrorList().size());

        var validationError = result.getValidationErrorList().get(0);
        assertEquals(validationMessage, validationError.getMessage());
        assertEquals(ruleName, validationError.getRuleName());
        assertEquals(lineNumber, validationError.getLineNumber());
        assertEquals(columnNumber, validationError.getColumnNumber());
    }

    @Test
    void decode_WhenCalledWithWrongXML_ThenExceptionThrown() {
        var message = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + "<svs:SclValidateRequest xmlns:svs=\"" + SCL_VALIDATOR_SERVICE_V1_NS_URI + "\">"
                + "</svs:SclValidateRequest>";

        var exception = assertThrows(SclValidatorException.class, () -> decoder.decode(message));
        assertEquals(WEBSOCKET_DECODER_ERROR_CODE, exception.getErrorCode());
        assertEquals(UnmarshalException.class, exception.getCause().getClass());
    }

    @Test
    void decode_WhenCalledWithInCorrectMessage_ThenExceptionThrown() {
        var message = "Incorrect Meesage";

        var exception = assertThrows(SclValidatorException.class, () -> decoder.decode(message));
        assertEquals(WEBSOCKET_DECODER_ERROR_CODE, exception.getErrorCode());
        assertEquals(UnmarshalException.class, exception.getCause().getClass());
    }

    @AfterEach
    void destroy() {
        decoder.destroy();
    }
}

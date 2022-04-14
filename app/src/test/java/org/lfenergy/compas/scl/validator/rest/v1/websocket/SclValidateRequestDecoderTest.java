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

class SclValidateRequestDecoderTest {
    private SclValidateRequestDecoder decoder;

    @BeforeEach
    void init() {
        decoder = new SclValidateRequestDecoder();
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
        var sclData = "Some SCL Data";
        var message = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + "<svs:SclValidateRequest xmlns:svs=\"" + SCL_VALIDATOR_SERVICE_V1_NS_URI + "\">"
                + "<svs:SclData><![CDATA[" + sclData + "]]></svs:SclData>"
                + "</svs:SclValidateRequest>";

        var result = decoder.decode(message);

        assertNotNull(result);
        assertEquals(sclData, result.getSclData());
    }

    @Test
    void decode_WhenCalledWithWrongXML_ThenExceptionThrown() {
        var message = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + "<svs:SclValidateResponse xmlns:svs=\"" + SCL_VALIDATOR_SERVICE_V1_NS_URI + "\">"
                + "</svs:SclValidateResponse>";

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

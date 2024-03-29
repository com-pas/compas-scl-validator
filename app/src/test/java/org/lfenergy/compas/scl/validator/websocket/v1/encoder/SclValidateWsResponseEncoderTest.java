// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.websocket.v1.encoder;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lfenergy.compas.scl.validator.model.ValidationError;
import org.lfenergy.compas.scl.validator.websocket.v1.model.SclValidateWsResponse;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.lfenergy.compas.scl.validator.SclValidatorConstants.SCL_VALIDATOR_SERVICE_V1_NS_URI;

class SclValidateWsResponseEncoderTest {
    private SclValidateWsResponseEncoder encoder;

    @BeforeEach
    void init() {
        encoder = new SclValidateWsResponseEncoder();
        encoder.init(null);
    }

    @Test
    void encode_WhenCalledWithRequest_ThenRequestConvertedToString() {
        var validationMessage = "Some Validation Message";
        var ruleName = "Rule Name 1";
        var lineNumber = 15;
        var columnNumber = 34;

        var request = new SclValidateWsResponse();
        request.setValidationErrorList(new ArrayList<>());
        var validationError = new ValidationError();
        validationError.setMessage(validationMessage);
        validationError.setRuleName(ruleName);
        validationError.setLineNumber(lineNumber);
        validationError.setColumnNumber(columnNumber);
        request.getValidationErrorList().add(validationError);

        var result = encoder.encode(request);

        var expectedResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
                "<svs:SclValidateWsResponse xmlns:svs=\"" + SCL_VALIDATOR_SERVICE_V1_NS_URI + "\">" +
                "<svs:ValidationErrors>" +
                "<svs:Message>" + validationMessage + "</svs:Message>" +
                "<svs:RuleName>" + ruleName + "</svs:RuleName>" +
                "<svs:LineNumber>" + lineNumber + "</svs:LineNumber>" +
                "<svs:ColumnNumber>" + columnNumber + "</svs:ColumnNumber>" +
                "</svs:ValidationErrors>" +
                "</svs:SclValidateWsResponse>";
        assertNotNull(result);
        assertEquals(expectedResult, result);
    }

    @AfterEach
    void destroy() {
        encoder.destroy();
    }
}

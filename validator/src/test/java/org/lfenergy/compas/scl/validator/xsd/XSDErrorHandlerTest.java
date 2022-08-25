// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.xsd;

import org.junit.jupiter.api.Test;
import org.lfenergy.compas.scl.validator.model.ValidationError;
import org.xml.sax.SAXParseException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.lfenergy.compas.scl.validator.xsd.XSDErrorHandler.DEFAULT_PREFIX;
import static org.lfenergy.compas.scl.validator.xsd.XSDErrorHandler.DEFAULT_RULE_NAME;

class XSDErrorHandlerTest {
    private List<ValidationError> errorList = new ArrayList<>();
    private XSDErrorHandler handler = new XSDErrorHandler(errorList);

    @Test
    void warning_WhenCalled_ThenNewValidationErrorAddedToList() {
        var message = "Some Warning Message";
        var linenumber = 3;
        var columnNumber = 15;

        handler.warning(new SAXParseException(message, "", "", linenumber, columnNumber));

        assertEquals(1, errorList.size());
        assertValidationError(errorList.get(0), message, linenumber, columnNumber);
    }

    @Test
    void error_WhenCalled_ThenNewValidationErrorAddedToList() {
        var message = "Some Error Message";
        var linenumber = 5;
        var columnNumber = 25;

        handler.error(new SAXParseException(message, "", "", linenumber, columnNumber));

        assertEquals(1, errorList.size());
        assertValidationError(errorList.get(0), message, linenumber, columnNumber);
    }

    @Test
    void fatalError_WhenCalled_ThenNewValidationErrorAddedToList() {
        var message = "Some Error Message";
        var linenumber = 5;
        var columnNumber = 25;

        handler.fatalError(new SAXParseException(message, "", "", linenumber, columnNumber));

        assertEquals(1, errorList.size());
        assertValidationError(errorList.get(0), message, linenumber, columnNumber);
    }

    private void assertValidationError(ValidationError validationError, String message, int linenumber, int columnNumber) {
        assertEquals(message, validationError.getMessage());
        assertEquals(linenumber, validationError.getLinenumber());
        assertEquals(columnNumber, validationError.getColumnNumber());
    }

    @Test
    void getRuleName_WhenXSDMessageIsNull_ThenDefaultXSDRuleNameReturned() {
        var ruleName = handler.getRuleName(null);

        assertEquals(DEFAULT_RULE_NAME, ruleName);
    }

    @Test
    void getRuleName_WhenXSDMessageIsBlank_ThenDefaultXSDRuleNameReturned() {
        var ruleName = handler.getRuleName("");

        assertEquals(DEFAULT_RULE_NAME, ruleName);
    }

    @Test
    void getRuleName_WhenXSDMessageContainsNoRule_ThenDefaultXSDRuleNameReturned() {
        var xsdMessage = "Duplicate match in scope for field \"depth\"";

        var ruleName = handler.getRuleName(xsdMessage);

        assertEquals(DEFAULT_RULE_NAME, ruleName);
    }

    @Test
    void getRuleName_WhenXSDMessageContainsRuleWithSpaces_ThenDefaultXSDRuleNameReturned() {
        var xsdMessage = "SOME SPACES RULE: Duplicate match in scope for field \"depth\"";

        var ruleName = handler.getRuleName(xsdMessage);

        assertEquals(DEFAULT_RULE_NAME, ruleName);
    }

    @Test
    void getRuleName_WhenXSDMessageContainsRule_ThenRuleNameReturned() {
        var expectedRuleName = "cvc-complex-type.2.1";
        var xsdMessage = expectedRuleName + ": Element 'depth' must have no character or element information " +
                "item [children], because the type's content type is empty.";

        var ruleName = handler.getRuleName(xsdMessage);

        assertEquals(DEFAULT_PREFIX + expectedRuleName, ruleName);
    }

    @Test
    void getMessage_WhenXSDMessageIsNull_ThenNullReturned() {
        var message = handler.getMessage(null);

        assertNull(message);
    }

    @Test
    void getMessage_WhenXSDMessageIsBlank_ThenBlankMessageReturned() {
        var xsdMessage = " ";

        var message = handler.getMessage(xsdMessage);

        assertEquals(xsdMessage, message);
    }

    @Test
    void getMessage_WhenXSDMessageContainsNoRule_ThenOriginalMessageReturned() {
        var xsdMessage = "Duplicate match in scope for field \"depth\"";

        var message = handler.getMessage(xsdMessage);

        assertEquals(xsdMessage, message);
    }

    @Test
    void getMessage_WhenXSDMessageContainsRuleWithSpaces_ThenOriginalMessageReturned() {
        var xsdMessage = "SOME SPACES RULE: Duplicate match in scope for field \"depth\"";

        var message = handler.getMessage(xsdMessage);

        assertEquals(xsdMessage, message);
    }

    @Test
    void getMessage_WhenXSDMessageContainsRule_ThenRuleNameIsStrippedFromMessage() {
        var ruleName = "cvc-complex-type.2.1";
        var expectedMessage = "Element 'depth' must have no character or element information item [children], " +
                "because the type's content type is empty.";

        var message = handler.getMessage(ruleName + ": " + expectedMessage);

        assertEquals(expectedMessage, message);
    }
}
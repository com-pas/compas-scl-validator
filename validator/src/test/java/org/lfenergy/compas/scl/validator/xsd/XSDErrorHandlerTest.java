// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.xsd;

import org.apache.xerces.impl.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.lfenergy.compas.scl.validator.exception.SclValidatorException;
import org.lfenergy.compas.scl.validator.model.ValidationError;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.Validator;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.lfenergy.compas.scl.validator.exception.SclValidatorErrorCode.CREATE_XPATH_ELEMENT_ERROR_CODE;
import static org.lfenergy.compas.scl.validator.xsd.XSDErrorHandler.DEFAULT_PREFIX;
import static org.lfenergy.compas.scl.validator.xsd.XSDErrorHandler.DEFAULT_RULE_NAME;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class XSDErrorHandlerTest {
    @Mock
    private Validator validator;

    private List<ValidationError> errorList = new ArrayList<>();
    private Document doc;

    private XSDErrorHandler handler;

    @BeforeEach
    void setup() throws IOException, ParserConfigurationException, SAXException {
        handler = new XSDErrorHandler(validator, errorList);

        try (var inputStream = getClass()
                .getResourceAsStream("/scl/validation/example-without-xsd-validation-errors.scd")) {
            assertNotNull(inputStream);

            var dbf = DocumentBuilderFactory.newInstance();
            doc = dbf.newDocumentBuilder().parse(inputStream);
        }
    }

    @Test
    void warning_WhenCalled_ThenNewValidationErrorAddedToList() throws SAXException {
        var message = "Some warning message";

        when(validator.getProperty(Constants.XERCES_PROPERTY_PREFIX + Constants.CURRENT_ELEMENT_NODE_PROPERTY))
                .thenReturn(doc.getDocumentElement());

        handler.warning(new SAXParseException(message, null));

        assertValidationError(errorList, message);
    }

    @Test
    void error_WhenCalled_ThenNewValidationErrorAddedToList() throws SAXException {
        var message = "Some error message";

        when(validator.getProperty(Constants.XERCES_PROPERTY_PREFIX + Constants.CURRENT_ELEMENT_NODE_PROPERTY))
                .thenReturn(doc.getDocumentElement());

        handler.error(new SAXParseException(message, null));

        assertValidationError(errorList, message);
    }

    @Test
    void fatalError_WhenCalled_ThenNewValidationErrorAddedToList() throws SAXException {
        var message = "Some fatal error message";

        when(validator.getProperty(Constants.XERCES_PROPERTY_PREFIX + Constants.CURRENT_ELEMENT_NODE_PROPERTY))
                .thenReturn(doc.getDocumentElement());

        handler.fatalError(new SAXParseException(message, null));

        assertValidationError(errorList, message);
    }

    private void assertValidationError(List<ValidationError> errorList, String message) throws SAXException {
        assertEquals(1, errorList.size());
        assertEquals(message, errorList.get(0).getMessage());
        assertEquals("/SCL", errorList.get(0).getXpath());
        assertNull(errorList.get(0).getLineNumber());
        assertNull(errorList.get(0).getColumnNumber());

        verify(validator).getProperty(Constants.XERCES_PROPERTY_PREFIX + Constants.CURRENT_ELEMENT_NODE_PROPERTY);
    }

    @Test
    void getRuleName_WhenXSDMessageIsNull_ThenDefaultXSDRuleNameReturned() {
        executeTest_getRuleName_WhereResultIsDefaultRuleName(null);
    }

    @Test
    void getRuleName_WhenXSDMessageIsBlank_ThenDefaultXSDRuleNameReturned() {
        executeTest_getRuleName_WhereResultIsDefaultRuleName("");
    }

    @Test
    void getRuleName_WhenXSDMessageContainsNoRule_ThenDefaultXSDRuleNameReturned() {
        executeTest_getRuleName_WhereResultIsDefaultRuleName(
                "Duplicate match in scope for field \"depth\"");
    }

    @Test
    void getRuleName_WhenXSDMessageContainsRuleWithSpaces_ThenDefaultXSDRuleNameReturned() {
        executeTest_getRuleName_WhereResultIsDefaultRuleName(
                "SOME SPACES RULE: Duplicate match in scope for field \"depth\"");
    }

    private void executeTest_getRuleName_WhereResultIsDefaultRuleName(String xsdMessage) {
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
        executeTest_GetMessage_WhereInputIsResult(null);
    }

    @Test
    void getMessage_WhenXSDMessageIsBlank_ThenBlankMessageReturned() {
        executeTest_GetMessage_WhereInputIsResult(" ");
    }

    @Test
    void getMessage_WhenXSDMessageContainsNoRule_ThenOriginalMessageReturned() {
        executeTest_GetMessage_WhereInputIsResult("Duplicate match in scope for field \"depth\"");
    }

    @Test
    void getMessage_WhenXSDMessageContainsRuleWithSpaces_ThenOriginalMessageReturned() {
        executeTest_GetMessage_WhereInputIsResult("SOME SPACES RULE: Duplicate match in scope for field \"depth\"");
    }

    private void executeTest_GetMessage_WhereInputIsResult(String xsdMessage) {
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

    @Test
    void getXPath_WhenPassingNull_ThenNullReturned() {
        assertNull(handler.getXPath(null));
    }

    @Test
    void getXPath_WhenPassingSCLElement_ThenExpectedXPathReturned() {
        assertEquals("/SCL", handler.getXPath(doc.getDocumentElement()));
    }

    @Test
    void getXPath_WhenPassingChildElement_ThenExpectedXPathReturned() {
        var firstIed = getFirstChild(doc.getDocumentElement(), "IED");

        assertEquals("/SCL/IED[1]", handler.getXPath(firstIed));
    }

    @Test
    void getIndex_WhenPassingParentAndChildElement_ThenExpectedIndexReturned() {
        var scl = doc.getDocumentElement();

        var firstIed = getFirstChild(scl, "IED");
        assertEquals(1, handler.getIndex(scl, firstIed));

        var firstDataTypeTemplates = getFirstChild(scl, "DataTypeTemplates");
        assertEquals(1, handler.getIndex(scl, firstDataTypeTemplates));
    }

    @Test
    void getIndex_WhenPassingParentAndSecondChildElement_ThenExpectedIndexReturned() {
        var scl = doc.getDocumentElement();
        var dataTypeTemplates = getFirstChild(scl, "DataTypeTemplates");

        Node foundChild = null;
        var children = dataTypeTemplates.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            var child = children.item(i);
            if ("LNodeType".equals(child.getNodeName())) {
                if (foundChild == null) {
                    // First child found, so assign it and continue.
                    foundChild = child;
                    continue;
                }
                // This will be the second child.
                foundChild = child;
                break;
            }
        }
        assertEquals(2, handler.getIndex(dataTypeTemplates, foundChild));
    }

    @Test
    void getIndex_WhenPassingInvalidChild_ThenExceptionIsThrown() {
        var scl = doc.getDocumentElement();
        var dataTypeTemplates = getFirstChild(scl, "DataTypeTemplates");
        var lNodeType = getFirstChild(dataTypeTemplates, "LNodeType");

        var exception = assertThrows(SclValidatorException.class, () -> handler.getIndex(scl, lNodeType));
        assertEquals(CREATE_XPATH_ELEMENT_ERROR_CODE, exception.getErrorCode());
    }

    private Node getFirstChild(Node parent, String tagName) {
        Node firstChild = null;
        var children = parent.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            var child = children.item(i);
            if (tagName.equals(child.getNodeName())) {
                firstChild = child;
                break;
            }
        }
        return firstChild;
    }
}
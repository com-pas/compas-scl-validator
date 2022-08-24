package org.lfenergy.compas.scl.validator.xsd;

import org.junit.jupiter.api.Test;
import org.lfenergy.compas.scl.validator.model.ValidationError;
import org.xml.sax.SAXParseException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
}
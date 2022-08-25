// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.xsd;

import org.lfenergy.compas.scl.validator.model.ValidationError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

import java.util.List;

public class XSDErrorHandler implements ErrorHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(XSDErrorHandler.class);

    public static final String DEFAULT_PREFIX = "XSD/";
    public static final String DEFAULT_RULE_NAME = DEFAULT_PREFIX + "general";

    private List<ValidationError> errorList;

    public XSDErrorHandler(List<ValidationError> errorList) {
        this.errorList = errorList;
    }

    @Override
    public void warning(SAXParseException exception) {
        var validationError = createValidationError(exception);
        errorList.add(validationError);

        LOGGER.debug("XSD Validation - warning: '{}' (Line number {}, Column number {})",
                validationError.getMessage(),
                validationError.getLineNumber(),
                validationError.getColumnNumber());
    }

    @Override
    public void error(SAXParseException exception) {
        var validationError = createValidationError(exception);
        errorList.add(validationError);

        LOGGER.debug("XSD Validation - error: '{}' (Line number {}, Column number {})",
                validationError.getMessage(),
                validationError.getLineNumber(),
                validationError.getColumnNumber());
    }

    @Override
    public void fatalError(SAXParseException exception) {
        var validationError = createValidationError(exception);
        errorList.add(validationError);

        LOGGER.debug("XSD Validation - fatal error, stopping: '{}' (Line number {}, Column number {})",
                validationError.getMessage(),
                validationError.getLineNumber(),
                validationError.getColumnNumber());
    }

    private ValidationError createValidationError(SAXParseException exception) {
        var validationError = new ValidationError();
        var xsdMessage = exception.getMessage();
        validationError.setMessage(getMessage(xsdMessage));
        validationError.setRuleName(getRuleName(xsdMessage));
        validationError.setLineNumber(exception.getLineNumber());
        validationError.setColumnNumber(exception.getColumnNumber());
        return validationError;
    }

    String getRuleName(String xsdMessage) {
        var ruleName = DEFAULT_RULE_NAME;
        if (xsdMessage != null && !xsdMessage.isBlank()) {
            int endIndex = xsdMessage.indexOf(':');
            if (endIndex > 0) {
                var tmpRuleName = xsdMessage.substring(0, endIndex);
                if (!tmpRuleName.contains(" ")) {
                    ruleName = "XSD/" + tmpRuleName;
                }
            }
        }
        return ruleName;
    }

    String getMessage(String xsdMessage) {
        var message = xsdMessage;
        if (message != null && !message.isBlank()) {
            int endIndex = message.indexOf(':');
            if (endIndex > 0) {
                var tmpRuleName = xsdMessage.substring(0, endIndex);
                if (!tmpRuleName.contains(" ")) {
                    message = message.substring(endIndex + 1).trim();
                }
            }
        }
        return message;
    }
}

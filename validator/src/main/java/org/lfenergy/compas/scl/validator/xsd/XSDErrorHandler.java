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

    private List<ValidationError> errorList;

    public XSDErrorHandler(List<ValidationError> errorList) {
        this.errorList = errorList;
    }

    @Override
    public void warning(SAXParseException exception) {
        var validationError = createValidationError(exception);
        errorList.add(validationError);

        LOGGER.debug("XSD Validation - warning: '{}' (Linenumber {}, Column number {})",
                validationError.getMessage(),
                validationError.getLinenumber(),
                validationError.getColumnNumber());
    }

    @Override
    public void error(SAXParseException exception) {
        var validationError = createValidationError(exception);
        errorList.add(validationError);

        LOGGER.debug("XSD Validation - error: '{}' (Linenumber {}, Column number {})",
                validationError.getMessage(),
                validationError.getLinenumber(),
                validationError.getColumnNumber());
    }

    @Override
    public void fatalError(SAXParseException exception) {
        var validationError = createValidationError(exception);
        errorList.add(validationError);

        LOGGER.debug("XSD Validation - fatal error, stopping: '{}' (Linenumber {}, Column number {})",
                validationError.getMessage(),
                validationError.getLinenumber(),
                validationError.getColumnNumber());
    }

    private ValidationError createValidationError(SAXParseException exception) {
        var validationError = new ValidationError();
        validationError.setMessage(exception.getMessage());
        validationError.setRuleName("XSD validation");
        validationError.setLinenumber(exception.getLineNumber());
        validationError.setColumnNumber(exception.getColumnNumber());
        return validationError;
    }
}

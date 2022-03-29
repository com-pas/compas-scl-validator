// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.xsd;

import java.io.*;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.lfenergy.compas.scl.validator.exception.SclValidatorException;
import org.lfenergy.compas.scl.validator.model.ValidationError;
import org.lfenergy.compas.scl.validator.xsd.resourceresolver.ResourceResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import static org.lfenergy.compas.scl.validator.exception.SclValidatorErrorCode.LOADING_XSD_FILE_ERROR_CODE;

public class XSDValidator {
    private static final Logger LOGGER = LoggerFactory.getLogger(XSDValidator.class);

    private final Validator validator;

    private final List<ValidationError> errorList;
    private final String sclData;

    public XSDValidator(List<ValidationError> errorList, String sclData) {
        this.errorList = errorList;
        this.sclData = sclData;

        var sclVersion = getSclVersion(sclData);

        try {
            var factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            factory.setResourceResolver(new ResourceResolver(sclVersion));
            var schema = factory.newSchema(
                    new StreamSource(getClass().getClassLoader().getResourceAsStream("xsd/SCL" + sclVersion + "/SCL.xsd")));
            validator = schema.newValidator();
        }
        catch(SAXException exception) {
            LOGGER.error("[XSD validation] SAXException: {}", exception.getMessage());
            throw new SclValidatorException(LOADING_XSD_FILE_ERROR_CODE, exception.getMessage());
        }

        validator.setErrorHandler(new ErrorHandler() {
            @Override
            public void warning(SAXParseException exception) {
                var validationError = addNewValidationError();
                var validationMessage = getXsdValidationMessage(exception);

                validationError.setMessage(validationMessage);
                LOGGER.warn(validationMessage);
            }

            @Override
            public void error(SAXParseException exception) {
                var validationError = addNewValidationError();
                var validationMessage = getXsdValidationMessage(exception);

                validationError.setMessage(validationMessage);
                LOGGER.error(validationMessage);
            }

            @Override
            public void fatalError(SAXParseException exception) {
                var validationError = addNewValidationError();
                var validationMessage = getXsdValidationMessage(exception);

                validationError.setMessage(validationMessage);
                LOGGER.error(validationMessage);
                LOGGER.error("[XSD validation] fatal error for schema validation, stopping");
            }
        } );
    }

    public void validate() {
        try {
            SAXSource source = new SAXSource(new InputSource(new StringReader(sclData)));
            validator.validate(source);
        }
        catch(IOException exception) {
            LOGGER.error("[XSD validation] IOException: {}", exception.getMessage());
        }
        catch(SAXException exception) {
            LOGGER.error("[XSD validation] SAXException: {}", exception.getMessage());
        }
    }

    private ValidationError addNewValidationError() {
        var validationError = new ValidationError();
        errorList.add(validationError);
        return validationError;
    }

    private String getSclVersion(String sclData) {
        var sclInfo = new SclInfo(sclData);

        var version = sclInfo.getVersion();
        var revision = sclInfo.getRevision();
        var release = sclInfo.getRelease();

        var sclVersion = "";
        if (version != null) sclVersion += version;
        if (revision != null) sclVersion += revision;
        if (release != null) sclVersion += release;

        return sclVersion;
    }

    private String getXsdValidationMessage(SAXParseException exception) {
        return "[XSD validation] (line: " + exception.getLineNumber() + ", column: "
                + exception.getColumnNumber() + "): " + exception.getMessage();
    }
}

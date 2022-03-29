// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.xsd;

import java.io.*;
import java.util.ArrayList;

import javax.xml.XMLConstants;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.lfenergy.compas.scl.validator.model.ValidationError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class XSDValidator {
    private static final Logger LOGGER = LoggerFactory.getLogger(XSDValidator.class);

    private Validator xsdValidator;

    private final ArrayList<ValidationError> errorList;

    public XSDValidator(ArrayList<ValidationError> errorList, String sclData) {
        this.errorList = errorList;

        var sclVersion = getSclVersion(sclData);

        try {
            var factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            factory.setResourceResolver(new SclResourceResolver(sclVersion));
            var schema = factory.newSchema(
                    new StreamSource(getClass().getClassLoader().getResourceAsStream("xsd/SCL" + sclVersion + "/SCL.xsd")));
            xsdValidator = schema.newValidator();
        }
        catch(SAXException exception) {
            LOGGER.error("[XSD validation] SAXException: " + exception.getMessage());
            return;
        }

        xsdValidator.setErrorHandler(new ErrorHandler() {
            @Override
            public void warning(SAXParseException exception) {
                var validationError = addNewValidationError();
                validationError.setMessage("[XSD validation] (line: " + exception.getLineNumber() + ", column: "
                        + exception.getColumnNumber() + "): " + exception.getMessage());
                LOGGER.warn("[XSD validation] (line: " + exception.getLineNumber() + ", column: "
                        + exception.getColumnNumber() + "): " + exception.getMessage());
            }

            @Override
            public void error(SAXParseException exception) {
                var validationError = addNewValidationError();
                validationError.setMessage("[XSD validation] (line: " + exception.getLineNumber() + ", column: "
                        + exception.getColumnNumber() + "): " + exception.getMessage());
                LOGGER.error("[XSD validation] (line: " + exception.getLineNumber() + ", column: "
                        + exception.getColumnNumber() + "): " + exception.getMessage());
            }

            @Override
            public void fatalError(SAXParseException exception) {
                var validationError = addNewValidationError();
                validationError.setMessage("[XSD validation] (line: " + exception.getLineNumber() + ", column: "
                        + exception.getColumnNumber() + "): " + exception.getMessage());
                LOGGER.error("[XSD validation] (line: " + exception.getLineNumber() + ", column: "
                        + exception.getColumnNumber() + "): " + exception.getMessage());
                LOGGER.error("[XSD validation] fatal error for schema validation, stopping");
            }
        } );
    }

    public void validate(String sclData) {
        try {
            SAXSource source = new SAXSource(new InputSource(new StringReader(sclData)));
            xsdValidator.validate(source);
        }
        catch(IOException exception) {
            LOGGER.error( "[XSD validation] IOException: " + exception.getMessage() );
        }
        catch(SAXException exception) {
            LOGGER.error( "[XSD validation] SAXException: " + exception.getMessage() );
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
}

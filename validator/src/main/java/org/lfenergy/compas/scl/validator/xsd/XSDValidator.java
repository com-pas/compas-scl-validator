// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.xsd;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.lfenergy.compas.scl.validator.exception.SclValidatorException;
import org.lfenergy.compas.scl.validator.model.ValidationError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import static org.lfenergy.compas.scl.validator.exception.SclValidatorErrorCode.CONVERTING_SCL_FILE_ERROR_CODE;

public class XSDValidator {
    private static final Logger LOGGER = LoggerFactory.getLogger(XSDValidator.class);

    private Validator xsdValidator;

    private final ArrayList<ValidationError> errorList;

    public XSDValidator(ArrayList<ValidationError> errorList) {
        this.errorList = errorList;
    }

    private void prepare(String sclData) {
        SchemaFactory factory = SchemaFactory.newInstance( XMLConstants.W3C_XML_SCHEMA_NS_URI );

        Source schemaFile = new StreamSource(new File(getXsdPath(sclData)));
        Schema schema;
        try {
            schema = factory.newSchema(schemaFile);
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
            prepare(sclData);
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

    private String getXsdPath(String sclData) {
        var sclElementAttributes = convertToDocument(sclData)
                .getElementsByTagName("SCL").item(0).getAttributes();

        var version = getAttributeValue(sclElementAttributes, "version");
        var revision = getAttributeValue(sclElementAttributes, "revision");
        var release = getAttributeValue(sclElementAttributes, "release");

        return "/Users/rob/Code/CoMPAS/compas-scl-validator/validator/target/xsd/SCL2007B4/SCL.xsd";
    }

    private Document convertToDocument(String value) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            return builder.parse(new InputSource(new StringReader(value)));
        } catch (Exception e) {
            throw new SclValidatorException(CONVERTING_SCL_FILE_ERROR_CODE, "Unable to convert SCL file to document", e);
        }
    }

    private String getAttributeValue(NamedNodeMap attributes, String attributeName) {
        if (attributes.getNamedItem(attributeName) != null) {
            return attributes.getNamedItem(attributeName).getNodeValue();
        }
        return null;
    }
}

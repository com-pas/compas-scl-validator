// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.xsd;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
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

    public XSDValidator(ArrayList<ValidationError> errorList) {
        this.errorList = errorList;
    }

    public void prepare(String xsdFile) {
        SchemaFactory factory = SchemaFactory.newInstance( XMLConstants.W3C_XML_SCHEMA_NS_URI );

        Source schemaFile = new StreamSource(new File(xsdFile) );
        Schema schema;
        try {
            schema = factory.newSchema( schemaFile );
            xsdValidator = schema.newValidator();
        }
        catch( SAXException e ) {
            LOGGER.error( "[XSD validation] SAXException: " + e.getMessage() );
            return;
        }

        xsdValidator.setErrorHandler( new ErrorHandler() {
            @Override
            public void warning( SAXParseException exception ) {
                var validationError = addNewValidationError();
                validationError.setMessage("[XSD validation] (line: " + exception.getLineNumber() + ", column: "
                        + exception.getColumnNumber() + "): " + exception.getMessage());
                LOGGER.warn( "[XSD validation] (line: " + exception.getLineNumber() + ", column: "
                        + exception.getColumnNumber() + "): " + exception.getMessage() );
            }

            @Override
            public void error( SAXParseException exception ) {
                var validationError = addNewValidationError();
                validationError.setMessage("[XSD validation] (line: " + exception.getLineNumber() + ", column: "
                        + exception.getColumnNumber() + "): " + exception.getMessage());
                LOGGER.error( "[XSD validation] (line: " + exception.getLineNumber() + ", column: "
                        + exception.getColumnNumber() + "): " + exception.getMessage() );
            }

            @Override
            public void fatalError( SAXParseException exception ) {
                var validationError = addNewValidationError();
                validationError.setMessage("[XSD validation] (line: " + exception.getLineNumber() + ", column: "
                        + exception.getColumnNumber() + "): " + exception.getMessage());
                LOGGER.error( "[XSD validation] (line: " + exception.getLineNumber() + ", column: "
                        + exception.getColumnNumber() + "): " + exception.getMessage() );
                LOGGER.error( "[XSD validation] fatal error for schema validation, stopping" );
            }
        } );

    }

    public void validate(String sclFile ) {
        try {
            SAXSource source = new SAXSource(new InputSource(new StringReader(sclFile)));
            xsdValidator.validate( source );
        }
        catch( IOException e ) {
            LOGGER.error( "[XSD validation] IOException: " + e.getMessage() );
        }
        catch( SAXException e ) {
            LOGGER.error( "[XSD validation] SAXException: " + e.getMessage() );
        }
    }

    private ValidationError addNewValidationError() {
        var validationError = new ValidationError();
        errorList.add(validationError);
        return validationError;
    }
}

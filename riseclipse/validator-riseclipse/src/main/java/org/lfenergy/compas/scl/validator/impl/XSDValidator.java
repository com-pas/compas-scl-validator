// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.impl;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
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

import static org.lfenergy.compas.scl.validator.util.MessageUtil.cleanupMessage;

public class XSDValidator {
    private static final Logger LOGGER = LoggerFactory.getLogger(XSDValidator.class);

    private static Validator xsdValidator;

    public static void prepare( String xsdFile ) {
        SchemaFactory factory = SchemaFactory.newInstance( XMLConstants.W3C_XML_SCHEMA_NS_URI );

        Source schemaFile = new StreamSource( new File( xsdFile ) );
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
                LOGGER.warn( "[XSD validation] (line: " + exception.getLineNumber() + ", column: "
                        + exception.getColumnNumber() + "): " + exception.getMessage() );
            }

            @Override
            public void error( SAXParseException exception ) {
                LOGGER.error( "[XSD validation] (line: " + exception.getLineNumber() + ", column: "
                        + exception.getColumnNumber() + "): " + exception.getMessage() );
            }

            @Override
            public void fatalError( SAXParseException exception ) {
                LOGGER.error( "[XSD validation] (line: " + exception.getLineNumber() + ", column: "
                        + exception.getColumnNumber() + "): " + exception.getMessage() );
                LOGGER.error( "[XSD validation] fatal error for schema validation, stopping" );
                return;
            }
        } );

    }

    public static void validate(ArrayList<ValidationError> errors, String sclFile ) {
        xsdValidator.reset();

        try {
            SAXSource source = new SAXSource( new InputSource( new StringReader(sclFile) ) );
            xsdValidator.validate( source );
        }
        catch( IOException e ) {
            LOGGER.error( "[XSD validation] IOException: " + e.getMessage() );
        }
        catch( SAXException e ) {
            var validationError = new ValidationError();
            errors.add(validationError);
            validationError.setMessage("[XSD validation] SAXException: " + e.getMessage());
            LOGGER.error( "[XSD validation] SAXException: " + e.getMessage() );
        }
    }

    // From https://stackoverflow.com/questions/5353783/why-org-apache-xerces-parsers-saxparser-does-not-skip-bom-in-utf8-encoded-xml

    private static char[] UTF32BE = { 0x0000, 0xFEFF };
    private static char[] UTF32LE = { 0xFFFE, 0x0000 };
    private static char[] UTF16BE = { 0xFEFF         };
    private static char[] UTF16LE = { 0xFFFE         };
    private static char[] UTF8    = { 0xEFBB, 0xBF   };

    private static boolean removeBOM( Reader reader, char[] bom ) throws IOException {
        int bomLength = bom.length;
        reader.mark( bomLength );
        char[] possibleBOM = new char[bomLength];
        reader.read( possibleBOM );
        for( int x = 0; x < bomLength; x++ ) {
            if( ( int ) bom[x] != ( int ) possibleBOM[x] ) {
                reader.reset();
                return false;
            }
        }
        return true;
    }

    private static void removeBOM( Reader reader ) throws IOException {
        if( removeBOM( reader, UTF32BE )) {
            return;
        }
        if( removeBOM( reader, UTF32LE )) {
            return;
        }
        if( removeBOM( reader, UTF16BE )) {
            return;
        }
        if( removeBOM( reader, UTF16LE )) {
            return;
        }
        if( removeBOM( reader, UTF8 )) {
            return;
        }
    }
}

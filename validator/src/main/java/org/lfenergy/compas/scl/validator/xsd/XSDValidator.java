// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.xsd;

import org.lfenergy.compas.scl.validator.exception.SclValidatorException;
import org.lfenergy.compas.scl.validator.model.ValidationError;
import org.lfenergy.compas.scl.validator.xsd.resourceresolver.ResourceResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import static org.lfenergy.compas.scl.validator.exception.SclValidatorErrorCode.LOADING_XSD_FILE_ERROR_CODE;

public class XSDValidator {
    private static final Logger LOGGER = LoggerFactory.getLogger(XSDValidator.class);

    private final Validator validator;
    private final String sclData;

    public XSDValidator(List<ValidationError> errorList, String sclData) {
        this.sclData = sclData;

        var info = new SclInfo(sclData);
        var sclVersion = info.getSclVersion();

        try {
            var sclXsd = new StreamSource(getClass().getClassLoader().getResourceAsStream("xsd/SCL" + sclVersion + "/SCL.xsd"));
            var compasXsd = new StreamSource(getClass().getClassLoader().getResourceAsStream("xsd/SCL_CoMPAS.xsd"));

            var factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            factory.setResourceResolver(new ResourceResolver(sclVersion));
            var schema = factory.newSchema(new Source[]{sclXsd, compasXsd});
            validator = schema.newValidator();
            validator.setErrorHandler(new XSDErrorHandler(errorList));
        } catch (SAXException exception) {
            throw new SclValidatorException(LOADING_XSD_FILE_ERROR_CODE, exception.getMessage());
        }
    }

    public void validate() {
        try {
            SAXSource source = new SAXSource(new InputSource(new StringReader(sclData)));
            validator.validate(source);
        } catch (IOException | SAXException exception) {
            LOGGER.error("[XSD validation] Exception: {}", exception.getMessage());
        }
    }
}

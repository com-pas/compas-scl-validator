// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.xsd;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.xerces.jaxp.DocumentBuilderFactoryImpl;
import org.lfenergy.compas.scl.validator.exception.SclValidatorException;
import org.lfenergy.compas.scl.validator.model.ValidationError;
import org.lfenergy.compas.scl.validator.xsd.resourceresolver.ResourceResolver;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.lfenergy.compas.scl.validator.exception.SclValidatorErrorCode.LOADING_SCL_FILE_ERROR_CODE;
import static org.lfenergy.compas.scl.validator.exception.SclValidatorErrorCode.LOADING_XSD_FILE_ERROR_CODE;

public class XSDValidator {
    private static final Logger LOGGER = LogManager.getLogger(XSDValidator.class);

    private final Validator validator;
    private final Document doc;

    public XSDValidator(List<ValidationError> errorList, String sclData) {
        try {
            // Load the SCL XML as Document with Xerces. This way Xerces is able to return the Element which is
            // related to the Validation Error that occurred. The Element will be converted to a generic XPath string.
            var dbf = DocumentBuilderFactory.newInstance(
                    DocumentBuilderFactoryImpl.class.getName(),
                    XSDValidator.class.getClassLoader());
            dbf.setNamespaceAware(true);

            this.doc = dbf.newDocumentBuilder().parse(
                    new ByteArrayInputStream(sclData.getBytes(StandardCharsets.UTF_8)));
        } catch (SAXException | IOException | ParserConfigurationException exception) {
            throw new SclValidatorException(LOADING_SCL_FILE_ERROR_CODE, exception.getMessage());
        }

        // Use the Document to retrieve the SCL Schema Version
        var info = new SclInfo(this.doc);
        var sclVersion = info.getSclVersion();

        try {
            // Retrieve both SCL and CoMPAS XSD Files to be used for XSD Validation.
            var sclXsd = new StreamSource(getClass().getClassLoader().getResourceAsStream("xsd/SCL" + sclVersion + "/SCL.xsd"));
            var compasXsd = new StreamSource(getClass().getClassLoader().getResourceAsStream("xsd/SCL_CoMPAS.xsd"));

            var factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            factory.setResourceResolver(new ResourceResolver(sclVersion));
            var schema = factory.newSchema(new Source[]{sclXsd, compasXsd});

            validator = schema.newValidator();
            validator.setErrorHandler(new XSDErrorHandler(validator, errorList));
        } catch (SAXException exception) {
            throw new SclValidatorException(LOADING_XSD_FILE_ERROR_CODE, exception.getMessage());
        }
    }

    public void validate() {
        try {
            var source = new DOMSource(doc.getDocumentElement());
            validator.validate(source);
        } catch (IOException | SAXException exception) {
            LOGGER.error("[XSD validation] Exception: {}", exception.getMessage());
        }
    }
}

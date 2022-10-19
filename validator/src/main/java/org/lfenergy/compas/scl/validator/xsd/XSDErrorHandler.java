// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.xsd;

import org.apache.xerces.impl.Constants;
import org.lfenergy.compas.scl.validator.exception.SclValidatorException;
import org.lfenergy.compas.scl.validator.model.ValidationError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.validation.Validator;
import java.util.List;

import static org.lfenergy.compas.scl.validator.exception.SclValidatorErrorCode.CREATE_XPATH_ELEMENT_ERROR_CODE;

public class XSDErrorHandler implements ErrorHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(XSDErrorHandler.class);

    public static final String DEFAULT_PREFIX = "XSD/";
    public static final String DEFAULT_RULE_NAME = DEFAULT_PREFIX + "general";

    private final Validator validator;
    private final List<ValidationError> errorList;

    public XSDErrorHandler(Validator validator, List<ValidationError> errorList) {
        this.validator = validator;
        this.errorList = errorList;
    }

    @Override
    public void warning(SAXParseException exception) throws SAXException {
        var validationError = createValidationError(exception);
        errorList.add(validationError);

        LOGGER.debug("XSD Validation - warning: '{}' (XPath {})",
                validationError.getMessage(),
                validationError.getXPath());
    }

    @Override
    public void error(SAXParseException exception) throws SAXException {
        var validationError = createValidationError(exception);
        errorList.add(validationError);

        LOGGER.debug("XSD Validation - error: '{}' (XPath {})",
                validationError.getMessage(),
                validationError.getXPath());
    }

    @Override
    public void fatalError(SAXParseException exception) throws SAXException {
        var validationError = createValidationError(exception);
        errorList.add(validationError);

        LOGGER.debug("XSD Validation - fatal error, stopping: '{}' (XPath {})",
                validationError.getMessage(),
                validationError.getXPath());
    }

    private ValidationError createValidationError(SAXParseException exception) throws SAXException {
        var validationError = new ValidationError();
        var xsdMessage = exception.getMessage();
        validationError.setMessage(getMessage(xsdMessage));
        validationError.setRuleName(getRuleName(xsdMessage));
        validationError.setXPath(getXPath(getCurrentNode()));
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

    String getXPath(Node node) {
        if (node != null) {
            var parent = node.getParentNode();
            if (parent != null && parent != node.getOwnerDocument()) {
                return getXPath(parent) + "/" + node.getNodeName() + "[" + getIndex(parent, node) + "]";
            }
            return "/" + node.getNodeName();
        }
        return null;
    }

    int getIndex(Node parent, Node child) {
        var children = parent.getChildNodes();
        var index = 0;
        for (int i = 0; i < children.getLength(); i++) {
            var listItem = children.item(i);
            if (listItem.getNodeName().equals(child.getNodeName())) {
                index++;
                if (listItem == child) {
                    return index;
                }
            }
        }
        throw new SclValidatorException(CREATE_XPATH_ELEMENT_ERROR_CODE, "Error determining index of child element");
    }

    private Node getCurrentNode() throws SAXException {
        // Get prop "http://apache.org/xml/properties/dom/current-element-node"
        // See https://xerces.apache.org/xerces2-j/properties.html#dom.current-element-node
        return (Node) validator.getProperty(Constants.XERCES_PROPERTY_PREFIX + Constants.CURRENT_ELEMENT_NODE_PROPERTY);
    }
}

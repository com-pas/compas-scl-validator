// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.model;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;

import static org.lfenergy.compas.scl.validator.SclValidatorConstants.SCL_VALIDATOR_SERVICE_V1_NS_URI;

@Schema(description = "Single validation error containing the message.")
@XmlAccessorType(XmlAccessType.FIELD)
public class ValidationError {
    @Schema(description = "The message of the validation error that occurred.",
            example = "Terminal (name=T1) (line 27) does not refer an existing ConnectivityNode")
    @XmlElement(name = "Message",
            namespace = SCL_VALIDATOR_SERVICE_V1_NS_URI,
            required = true)
    private String message;

    @Schema(description = "The name of the rule in RiseClipse that created the validation error",
            example = "SemanticConstraints")
    @XmlElement(name = "RuleName",
            namespace = SCL_VALIDATOR_SERVICE_V1_NS_URI)
    private String ruleName;

    @Schema(description = "The line number in the SCL file where the validation error occurred",
            example = "9")
    @XmlElement(name = "LineNumber",
            namespace = SCL_VALIDATOR_SERVICE_V1_NS_URI)
    private Integer lineNumber;

    @Schema(description = "The column number on the line number in the SCL file where the validation error occurred",
            example = "14")
    @XmlElement(name = "ColumnNumber",
            namespace = SCL_VALIDATOR_SERVICE_V1_NS_URI)
    private Integer columnNumber;

    @Schema(description = "The XPath expression to find the element where the validation error occurred",
            example = "/SCL/Substation[1]/VoltageLevel[1]/Bay[5]")
    @XmlElement(name = "XPath",
            namespace = SCL_VALIDATOR_SERVICE_V1_NS_URI)
    private String xpath;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public Integer getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(Integer lineNumber) {
        this.lineNumber = lineNumber;
    }

    public Integer getColumnNumber() {
        return columnNumber;
    }

    public void setColumnNumber(Integer columnNumber) {
        this.columnNumber = columnNumber;
    }

    public String getXpath() {
        return xpath;
    }

    public void setXpath(String xPath) {
        this.xpath = xPath;
    }
}

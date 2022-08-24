// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.model;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

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

    @Schema(description = "The linenumber in the SCL file where the validation error occurred",
            example = "9")
    @XmlElement(name = "Linenumber",
            namespace = SCL_VALIDATOR_SERVICE_V1_NS_URI)
    private long linenumber;

    @Schema(description = "The column number on the linenumber in the SCL file where the validation error occurred",
            example = "14")
    @XmlElement(name = "ColumnNumber",
            namespace = SCL_VALIDATOR_SERVICE_V1_NS_URI)
    private long columnNumber;

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

    public long getLinenumber() {
        return linenumber;
    }

    public void setLinenumber(long linenumber) {
        this.linenumber = linenumber;
    }

    public long getColumnNumber() {
        return columnNumber;
    }

    public void setColumnNumber(long columnNumber) {
        this.columnNumber = columnNumber;
    }
}

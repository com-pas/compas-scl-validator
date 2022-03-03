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
            example = "ERROR:[SemanticConstraints] Terminal (name=T1) (line 27) does not refer an existing ConnectivityNode")
    @XmlElement(name = "Message",
            namespace = SCL_VALIDATOR_SERVICE_V1_NS_URI,
            required = true)
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

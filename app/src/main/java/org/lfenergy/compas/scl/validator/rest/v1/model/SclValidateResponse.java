// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.rest.v1.model;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.lfenergy.compas.scl.validator.model.ValidationError;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

import static org.lfenergy.compas.scl.validator.SclValidatorConstants.SCL_VALIDATOR_SERVICE_V1_NS_URI;

@Schema(description = "The Validation Response returned")
@XmlRootElement(name = "SclValidateResponse", namespace = SCL_VALIDATOR_SERVICE_V1_NS_URI)
@XmlAccessorType(XmlAccessType.FIELD)
public class SclValidateResponse {
    @Schema(description = "The list with Validation Errors, if any.")
    @XmlElement(name = "ValidationErrors", namespace = SCL_VALIDATOR_SERVICE_V1_NS_URI)
    protected List<ValidationError> validationErrorList;

    public List<ValidationError> getValidationErrorList() {
        return validationErrorList;
    }

    public void setValidationErrorList(List<ValidationError> validationErrorList) {
        this.validationErrorList = validationErrorList;
    }
}

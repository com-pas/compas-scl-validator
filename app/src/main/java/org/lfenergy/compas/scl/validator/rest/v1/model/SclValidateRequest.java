// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0

package org.lfenergy.compas.scl.validator.rest.v1.model;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import static org.lfenergy.compas.scl.validator.SclValidatorConstants.SCL_VALIDATOR_SERVICE_V1_NS_URI;

@Schema(description = "The Validation Request retrieved")
@XmlRootElement(name = "SclValidateRequest", namespace = SCL_VALIDATOR_SERVICE_V1_NS_URI)
@XmlAccessorType(XmlAccessType.FIELD)
public class SclValidateRequest {
    @Schema(description = "The SCL Data to be validated")
    @NotBlank
    @XmlElement(name = "SclData", namespace = SCL_VALIDATOR_SERVICE_V1_NS_URI)
    protected String sclData;

    public String getSclData() {
        return sclData;
    }

    public void setSclData(String sclData) {
        this.sclData = sclData;
    }
}

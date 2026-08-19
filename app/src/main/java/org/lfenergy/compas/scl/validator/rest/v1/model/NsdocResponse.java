// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.rest.v1.model;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import static org.lfenergy.compas.scl.validator.SclValidatorConstants.SCL_VALIDATOR_SERVICE_V1_NS_URI;

@Schema(description = "The response with NSDoc File.")
@XmlRootElement(name = "NsdocResponse", namespace = SCL_VALIDATOR_SERVICE_V1_NS_URI)
@XmlAccessorType(XmlAccessType.FIELD)
public class NsdocResponse {
    @Schema(description = "The NSDoc File as String")
    @XmlElement(name = "NsdocFile", namespace = SCL_VALIDATOR_SERVICE_V1_NS_URI)
    private String nsdocFile;

    public String getNsdocFile() {
        return nsdocFile;
    }

    public void setNsdocFile(String nsdocFile) {
        this.nsdocFile = nsdocFile;
    }
}

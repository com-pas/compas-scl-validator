// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.rest.v1.model;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.lfenergy.compas.scl.validator.model.NsdocFile;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collection;

import static org.lfenergy.compas.scl.validator.SclValidatorConstants.SCL_VALIDATOR_SERVICE_V1_NS_URI;

@Schema(description = "The response with the list of known NSDoc Files.")
@XmlRootElement(name = "NsdocListResponse", namespace = SCL_VALIDATOR_SERVICE_V1_NS_URI)
@XmlAccessorType(XmlAccessType.FIELD)
public class NsdocListResponse {
    @Schema(description = "The list of known NSDoc Files")
    @XmlElement(name = "NsdocFile", namespace = SCL_VALIDATOR_SERVICE_V1_NS_URI)
    private Collection<NsdocFile> nsdocFiles;

    public Collection<NsdocFile> getNsdocFiles() {
        return nsdocFiles;
    }

    public void setNsdocFiles(Collection<NsdocFile> nsdocFiles) {
        this.nsdocFiles = nsdocFiles;
    }
}

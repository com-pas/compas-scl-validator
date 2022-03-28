// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.model;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import javax.validation.constraints.NotBlank;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.UUID;

import static org.lfenergy.compas.scl.validator.SclValidatorConstants.SCL_VALIDATOR_SERVICE_V1_NS_URI;

@Schema(description = "Information about a single NSDoc file that is known by the service.")
@XmlAccessorType(XmlAccessType.FIELD)
public class NsdocFile {
    @Schema(description = "The technical id of this NSDoc File.",
            example = "123e4567-e89b-12d3-a456-426614174000")
    @NotBlank
    @XmlElement(name = "Id",
            namespace = SCL_VALIDATOR_SERVICE_V1_NS_URI,
            required = true)
    private UUID id;

    @Schema(description = "The id of the NSDoc File.",
            example = "IEC 61850-7-3")
    @NotBlank
    @XmlElement(name = "NsDocId",
            namespace = SCL_VALIDATOR_SERVICE_V1_NS_URI,
            required = true)
    private String nsDocId;

    @Schema(description = "The name of the NSDoc File.",
            example = "OfficialFile73.nsdoc")
    @NotBlank
    @XmlElement(name = "Filename",
            namespace = SCL_VALIDATOR_SERVICE_V1_NS_URI,
            required = true)
    private String filename;

    @Schema(description = "The checksum of the NSDoc File.")
    @NotBlank
    @XmlElement(name = "Checksum",
            namespace = SCL_VALIDATOR_SERVICE_V1_NS_URI,
            required = true)
    private String checksum;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNsDocId() {
        return nsDocId;
    }

    public void setNsDocId(String nsDocId) {
        this.nsDocId = nsDocId;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }
}

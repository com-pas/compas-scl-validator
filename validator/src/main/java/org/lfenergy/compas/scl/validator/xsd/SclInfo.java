// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.xsd;

import org.w3c.dom.Document;

public class SclInfo {
    private String version;
    private String revision;
    private String release;

    public SclInfo(Document doc) {
        var sclElement = doc.getDocumentElement();
        version = sclElement.getAttribute("version");
        revision = sclElement.getAttribute("revision");
        release = sclElement.getAttribute("release");
    }

    public String getSclVersion() {
        var sclVersion = "";
        if (version != null) sclVersion += version;
        if (revision != null) sclVersion += revision;
        if (release != null) sclVersion += release;

        return sclVersion;
    }
}

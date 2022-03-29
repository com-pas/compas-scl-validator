// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.xsd.resourceresolver;

import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;

import java.io.InputStream;

public class ResourceResolver implements LSResourceResolver {
    private final String sclVersion;

    public ResourceResolver(String sclVersion) {
        this.sclVersion = sclVersion;
    }

    public LSInput resolveResource(String type, String namespaceURI,
                                   String publicId, String systemId, String baseURI) {
        InputStream resourceAsStream = this.getClass().getClassLoader()
                .getResourceAsStream("xsd/SCL" + sclVersion + "/" + systemId);
        return new ResourceInput(publicId, systemId, resourceAsStream);
    }
}
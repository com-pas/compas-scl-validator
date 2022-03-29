// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.xsd;

import org.lfenergy.compas.scl.validator.exception.SclValidatorException;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

import static org.lfenergy.compas.scl.validator.exception.SclValidatorErrorCode.RESOURCE_RESOLVER_FAILED;

public class SclResourceResolver implements LSResourceResolver {
    private final String sclVersion;

    SclResourceResolver(String sclVersion) {
        this.sclVersion = sclVersion;
    }

    public LSInput resolveResource(String type, String namespaceURI,
                                   String publicId, String systemId, String baseURI) {
        InputStream resourceAsStream = this.getClass().getClassLoader()
                .getResourceAsStream("xsd/SCL" + sclVersion + "/" + systemId);
        return new ResourceInput(publicId, systemId, resourceAsStream);
    }

    private static class ResourceInput implements LSInput {
        private String publicId;
        private String systemId;
        private final BufferedInputStream inputStream;

        public ResourceInput(String publicId, String sysId, InputStream input) {
            this.publicId = publicId;
            this.systemId = sysId;
            this.inputStream = new BufferedInputStream(input);
        }

        public String getPublicId() {
            return publicId;
        }

        @Override
        public void setPublicId(String publicId) {
            this.publicId = publicId;
        }

        @Override
        public String getBaseURI() {
            return null;
        }

        @Override
        public InputStream getByteStream() {
            return null;
        }

        @Override
        public boolean getCertifiedText() {
            return false;
        }

        @Override
        public Reader getCharacterStream() {
            return null;
        }

        @Override
        public String getEncoding() {
            return null;
        }

        @Override
        public String getStringData() {
            synchronized (inputStream) {
                try {
                    return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                } catch (IOException e) {
                    throw new SclValidatorException(RESOURCE_RESOLVER_FAILED,
                            "StringData of ResourceResolver cannot be retrieved");
                }
            }
        }

        @Override
        public void setBaseURI(String baseURI) {
            // Needs override, but nothing to implement.
        }

        @Override
        public void setByteStream(InputStream byteStream) {
            // Needs override, but nothing to implement.
        }

        @Override
        public void setCertifiedText(boolean certifiedText) {
            // Needs override, but nothing to implement.
        }

        @Override
        public void setCharacterStream(Reader characterStream) {
            // Needs override, but nothing to implement.
        }

        @Override
        public void setEncoding(String encoding) {
            // Needs override, but nothing to implement.
        }

        @Override
        public void setStringData(String stringData) {
            // Needs override, but nothing to implement.
        }

        @Override
        public String getSystemId() {
            return systemId;
        }

        @Override
        public void setSystemId(String systemId) {
            this.systemId = systemId;
        }
    }
}
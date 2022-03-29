// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.xsd;

import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

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
                    byte[] input = new byte[inputStream.available()];
                    inputStream.read(input);
                    String contents = new String(input);
                    return contents;
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("Exception " + e);
                    return null;
                }
            }
        }

        @Override
        public void setBaseURI(String baseURI) {
        }

        @Override
        public void setByteStream(InputStream byteStream) {
        }

        @Override
        public void setCertifiedText(boolean certifiedText) {
        }

        @Override
        public void setCharacterStream(Reader characterStream) {
        }

        @Override
        public void setEncoding(String encoding) {
        }

        @Override
        public void setStringData(String stringData) {
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
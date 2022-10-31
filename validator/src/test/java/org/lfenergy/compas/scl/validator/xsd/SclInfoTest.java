// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.xsd;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SclInfoTest {
    @Test
    void getSclVersion_WhenCalledWithValidSclFile_ThenSclVersionFromFileReturned()
            throws IOException, ParserConfigurationException, SAXException {
        var document = getDocument("/scl/example.scd");
        var sclInfo = new SclInfo(document);

        assertNotNull(sclInfo.getSclVersion());
        assertEquals("2007B4", sclInfo.getSclVersion());
    }

    @Test
    void getSclVersion_WhenCalledWithSclFileWithMissingVersion_ThenSclVersionFromFileReturned()
            throws IOException, ParserConfigurationException, SAXException {
        var document = getDocument("/scl/validation/example-with-missing-version.scd");
        var sclInfo = new SclInfo(document);

        assertNotNull(sclInfo.getSclVersion());
        assertEquals("", sclInfo.getSclVersion());
    }

    private Document getDocument(String filename) throws IOException, ParserConfigurationException, SAXException {
        try (var inputStream = getClass().getResourceAsStream(filename)) {
            assertNotNull(inputStream);

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            return dbf.newDocumentBuilder().parse(inputStream);
        }
    }
}

// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.util;

import org.junit.jupiter.api.Test;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.lfenergy.compas.scl.validator.util.StaxUtil.getAttributeValue;
import static org.lfenergy.compas.scl.validator.util.StaxUtil.isElement;

class StaxUtilTest {
    @Test
    void constructor_WhenConstructorCalled_ThenShouldThrowExceptionCauseForbidden() {
        assertThrows(UnsupportedOperationException.class, StaxUtil::new);
    }

    @Test
    void isElement_WhenCalledWithExpectedElementName_ThenFirstElementHasCorrectElementName() throws XMLStreamException, IOException {
        var element = getFirstElement();
        if (element != null) {
            var result = isElement(element, "SCL");
            assertTrue(result, "No SCL Element Found");
        } else {
            fail("XML File couldn't be read.");
        }
    }

    @Test
    void isElement_WhenCalledWithUnknownElementName_ThenFirstElementHasIncorrectElementName() throws XMLStreamException, IOException {
        var element = getFirstElement();
        if (element != null) {
            var result = isElement(element, "Unknown");
            assertFalse(result, "Unknown Element shouldn't be Found");
        } else {
            fail("XML File couldn't be read.");
        }
    }

    @Test
    void getAttributeValue_WhenCalledWithKnownAttribute_ThenAttributeValueReturned() throws XMLStreamException, IOException {
        var element = getFirstElement();
        if (element != null) {
            var result = getAttributeValue(element, "revision");
            assertEquals("B", result);
        } else {
            fail("XML File couldn't be read.");
        }
    }

    @Test
    void getAttributeValue_WhenCalledWithUnknownAttribute_ThenNullReturned() throws XMLStreamException, IOException {
        var element = getFirstElement();
        if (element != null) {
            var result = getAttributeValue(element, "unknown");
            assertNull(result);
        } else {
            fail("XML File couldn't be read.");
        }
    }

    private StartElement getFirstElement() throws XMLStreamException, IOException {
        try (var inputStream = getClass().getResourceAsStream("/scl/example.scd")) {
            var xmlInputFactory = XMLInputFactory.newInstance();
            var reader = xmlInputFactory.createXMLEventReader(inputStream);

            while (reader.hasNext()) {
                var nextEvent = reader.nextEvent();
                if (nextEvent.isStartElement()) {
                    return nextEvent.asStartElement();
                }
            }
            return null;
        }
    }
}

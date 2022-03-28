// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.common;

import org.lfenergy.compas.scl.validator.exception.SclValidatorException;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.lfenergy.compas.scl.validator.exception.SclValidatorErrorCode.DETERMINING_ID_FAILED;

public class NsdocInfo {
    private static final String NSDOC_ELEMENT_NAME = "NSDoc";

    private String id = null;

    public NsdocInfo(File file) {
        try (var fis = new FileInputStream(file)) {
            XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
            XMLEventReader reader = xmlInputFactory.createXMLEventReader(fis);

            while (id == null && reader.hasNext()) {
                XMLEvent nextEvent = reader.nextEvent();
                if (nextEvent.isStartElement()) {
                    StartElement startElement = nextEvent.asStartElement();
                    if (NSDOC_ELEMENT_NAME.equals(startElement.getName().getLocalPart())) {
                        Attribute attribute = startElement.getAttributeByName(new QName("id"));
                        if (attribute != null) {
                            id = attribute.getValue();
                        }
                    }
                }
            }
        } catch (IOException | XMLStreamException exp) {
            throw new SclValidatorException(DETERMINING_ID_FAILED, "Error loading NSDoc File " + file.getName() + "'.", exp);
        }

        if (id == null) {
            throw new SclValidatorException(DETERMINING_ID_FAILED, "No ID found in NSDoc File '" + file.getName() + "'.");
        }
    }

    public String getId() {
        return id;
    }
}

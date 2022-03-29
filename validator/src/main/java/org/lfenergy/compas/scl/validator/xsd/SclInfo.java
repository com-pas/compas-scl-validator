// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.xsd;

import org.lfenergy.compas.scl.validator.exception.SclValidatorException;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.lfenergy.compas.scl.validator.exception.SclValidatorErrorCode.LOADING_SCL_FILE_ERROR_CODE;
import static org.lfenergy.compas.scl.validator.util.StaxUtil.getAttributeValue;
import static org.lfenergy.compas.scl.validator.util.StaxUtil.isElement;

public class SclInfo {
    private static final String SCL_ELEMENT_NAME = "SCL";

    private String version = null;
    private String revision = null;
    private String release = null;

    public SclInfo(String sclData) {
        try (var fis = new ByteArrayInputStream(sclData.getBytes(StandardCharsets.UTF_8))) {
            var xmlInputFactory = getXMLInputFactory();
            var reader = xmlInputFactory.createXMLEventReader(fis);

            while (reader.hasNext()) {
                processEvent(reader.nextEvent());
            }
        } catch (IOException | XMLStreamException exp) {
            throw new SclValidatorException(LOADING_SCL_FILE_ERROR_CODE, "Error loading SCL File", exp);
        }
    }

    private void processEvent(XMLEvent nextEvent) {
        if (nextEvent.isStartElement()) {
            processStartElement(nextEvent.asStartElement());
        }
    }

    private void processStartElement(StartElement element) {
        if (isElement(element, SCL_ELEMENT_NAME)) {
            version = getAttributeValue(element, "version");
            revision = getAttributeValue(element, "revision");
            release = getAttributeValue(element, "release");
        }
    }

    public String getVersion() {
        return version;
    }

    public String getRevision() {
        return revision;
    }

    public String getRelease() {
        return release;
    }

    private XMLInputFactory getXMLInputFactory() {
        var xmlInputFactory = XMLInputFactory.newInstance();
        xmlInputFactory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.FALSE);
        return xmlInputFactory;
    }
}

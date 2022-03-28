// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.common;

import org.lfenergy.compas.scl.validator.exception.SclValidatorException;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.lfenergy.compas.scl.validator.exception.SclValidatorErrorCode.DETERMINING_ID_FAILED;
import static org.lfenergy.compas.scl.validator.exception.SclValidatorErrorCode.LOADING_NSDOC_FILE_FAILED;
import static org.lfenergy.compas.scl.validator.util.StaxUtil.getAttributeValue;
import static org.lfenergy.compas.scl.validator.util.StaxUtil.isElement;

public class NsdocInfo {
    private static final String NSDOC_ELEMENT_NAME = "NSDoc";

    private String id = null;

    public NsdocInfo(File file) {
        try (var fis = new FileInputStream(file)) {
            var factory = XMLInputFactory.newInstance();
            // Completely disable external entities declarations:
            factory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.FALSE);

            var reader = factory.createXMLEventReader(fis);

            while (id == null && reader.hasNext()) {
                processEvent(reader.nextEvent());
            }
        } catch (IOException | XMLStreamException exp) {
            throw new SclValidatorException(LOADING_NSDOC_FILE_FAILED, "Error loading NSDoc File " + file.getName() + "'.", exp);
        }

        if (id == null) {
            throw new SclValidatorException(DETERMINING_ID_FAILED, "No ID found in NSDoc File '" + file.getName() + "'.");
        }
    }

    private void processEvent(XMLEvent nextEvent) {
        if (nextEvent.isStartElement()) {
            processStartElement(nextEvent.asStartElement());
        }
    }

    private void processStartElement(StartElement element) {
        if (isElement(element, NSDOC_ELEMENT_NAME)) {
            id = getAttributeValue(element, "id");
        }
    }

    public String getId() {
        return id;
    }
}

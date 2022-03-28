// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.util;

import javax.xml.namespace.QName;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;

public class StaxUtil {
    StaxUtil() {
        throw new UnsupportedOperationException("StaxUtil class");
    }

    public static boolean isElement(StartElement element, String elementName) {
        return elementName.equals(element.getName().getLocalPart());
    }

    public static String getAttributeValue(StartElement element, String attributeName) {
        Attribute attribute = element.getAttributeByName(new QName(attributeName));
        if (attribute != null) {
            return attribute.getValue();
        }
        return null;
    }
}

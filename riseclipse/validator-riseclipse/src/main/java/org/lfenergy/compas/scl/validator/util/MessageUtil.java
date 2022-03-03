// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.util;

public class MessageUtil {
    MessageUtil() {
        throw new UnsupportedOperationException("MessageUtil class");
    }

    public static String cleanupMessage(String message) {
        String cleanedMessage = message;
        if (cleanedMessage != null
                && cleanedMessage.toUpperCase().startsWith("ERROR:")) {
            cleanedMessage = cleanedMessage.substring(6);
        }
        return cleanedMessage;
    }
}

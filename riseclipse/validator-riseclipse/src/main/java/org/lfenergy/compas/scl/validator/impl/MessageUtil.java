// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lfenergy.compas.scl.validator.model.ValidationError;

import java.util.Optional;

public class MessageUtil {
    private static final Logger LOGGER = LogManager.getLogger(MessageUtil.class);

    MessageUtil() {
        throw new UnsupportedOperationException("MessageUtil class");
    }

    public static Optional<ValidationError> createValidationError(String message) {
        if (message == null || message.isBlank()) {
            return Optional.empty();
        }

        var validationError = new ValidationError();
        var messageParts = message.split(";");
        if (messageParts.length == 5) {
            // The expected number of parts is found, the message and rule are set as-is, the line number
            // is converted to a Integer value,
            validationError.setRuleName(messageParts[1]);
            validationError.setMessage(messageParts[4]);

            try {
                validationError.setLineNumber(Integer.parseInt(messageParts[3]));
            } catch (NumberFormatException exp) {
                validationError.setLineNumber(-1);
                LOGGER.debug("Invalid line number '{}' found", messageParts[3], exp);
            }
        } else if (messageParts.length == 2) {
            // It seems like an old message that starts with 'ERROR;', so only set the second part as Message
            validationError.setMessage(messageParts[1]);
        } else {
            // If we can split it correctly, just put the message as-is to the response.
            validationError.setMessage(message);
        }

        return Optional.of(validationError);
    }
}

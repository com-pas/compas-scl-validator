// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.xsd;

import org.lfenergy.compas.scl.validator.model.ValidationError;

import java.util.ArrayList;
import java.util.List;

public class SclXsdValidator {

    public List<ValidationError> validate(String sclData) {
        var validationErrors = new ArrayList<ValidationError>();

        var xsdValidator = new XSDValidator(validationErrors, sclData);
        xsdValidator.validate();

        return validationErrors;
    }
}

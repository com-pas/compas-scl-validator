// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.service;

import org.lfenergy.compas.scl.extensions.model.SclFileType;
import org.lfenergy.compas.scl.validator.SclValidator;
import org.lfenergy.compas.scl.validator.model.ValidationError;
import org.lfenergy.compas.scl.validator.xsd.SclXsdValidator;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;

@ApplicationScoped
public class SclValidatorService {
    private final SclValidator validator;
    private final SclXsdValidator xsdValidator;

    @Inject
    public SclValidatorService(SclValidator validator, SclXsdValidator xsdValidator) {
        this.validator = validator;
        this.xsdValidator = xsdValidator;
    }

    public List<ValidationError> validate(SclFileType type, String sclData) {
        var errors = xsdValidator.validate(sclData);
        if (errors.isEmpty()) {
            errors = validator.validate(type, sclData);
        }
        return errors;
    }
}

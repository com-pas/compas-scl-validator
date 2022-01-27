// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.service;

import org.lfenergy.compas.scl.extensions.model.SclFileType;
import org.lfenergy.compas.scl.validator.SclValidator;
import org.lfenergy.compas.scl.validator.model.ValidationError;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;

@ApplicationScoped
public class SclValidatorService {
    private final SclValidator validator;

    @Inject
    public SclValidatorService(SclValidator validator) {
        this.validator = validator;
    }

    public List<ValidationError> validate(SclFileType type, String sclData) {
        return validator.validate(type, sclData);
    }
}

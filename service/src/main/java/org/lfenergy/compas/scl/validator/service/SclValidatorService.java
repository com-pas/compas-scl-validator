// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.service;

import org.lfenergy.compas.scl.extensions.model.SclFileType;
import org.lfenergy.compas.scl.validator.SclValidator;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class SclValidatorService {
    private final SclValidator validator;

    @Inject
    public SclValidatorService(SclValidator validator) {
        this.validator = validator;
    }

    public String validate(SclFileType type, String sclData) {
        return validator.validate(type, sclData);
    }
}

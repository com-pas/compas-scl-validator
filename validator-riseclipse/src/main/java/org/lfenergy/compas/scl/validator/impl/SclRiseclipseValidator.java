// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.impl;

import org.lfenergy.compas.scl.extensions.model.SclFileType;
import org.lfenergy.compas.scl.validator.SclValidator;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SclRiseclipseValidator implements SclValidator {
    @Override
    public String validate(SclFileType type, String sclData) {
        return sclData;
    }
}

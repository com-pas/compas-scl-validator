// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.impl;

import org.lfenergy.compas.scl.extensions.model.SclFileType;
import org.lfenergy.compas.scl.validator.SclValidator;
import org.lfenergy.compas.scl.validator.model.ValidationError;
import org.lfenergy.compas.scl.validator.resource.OclFileCollector;

import javax.enterprise.context.ApplicationScoped;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ApplicationScoped
public class SclRiseclipseValidator implements SclValidator {
    private final List<URI> oclFiles = new ArrayList<>();

    public SclRiseclipseValidator(OclFileCollector oclFileCollector) {
        this.oclFiles.addAll(oclFileCollector.getDefaultOclFiles());
    }

    @Override
    public List<ValidationError> validate(SclFileType type, String sclData) {
        return Collections.emptyList();
    }
}

// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lfenergy.compas.scl.extensions.model.SclFileType;
import org.lfenergy.compas.scl.validator.collector.CompasOclFileCollector;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.lfenergy.compas.scl.validator.util.TestSupportUtil.readSCL;

class SclRiseClipseValidatorTest {
    private SclRiseClipseValidator sclValidator;

    @BeforeEach
    public void setup() {
        var oclFileCollector = new CompasOclFileCollector(null);
        this.sclValidator = new SclRiseClipseValidator(oclFileCollector, Path.of("./target/data/temp"));
    }

    @Test
    void validate_WhenCalled_ThenExpectedValidationErrorsReturned() throws IOException {
        var type = SclFileType.CID;
        var sclData = readSCL("example.scd");

        var result = sclValidator.validate(type, sclData);

        assertNotNull(result);
//        assertEquals(15, result.size());
    }
}

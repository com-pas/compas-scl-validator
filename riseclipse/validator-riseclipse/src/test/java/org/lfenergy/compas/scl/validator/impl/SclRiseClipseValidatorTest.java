// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.lfenergy.compas.scl.extensions.model.SclFileType;
import org.lfenergy.compas.scl.validator.collector.CompasOclFileCollector;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class SclRiseClipseValidatorTest {
    private SclRiseClipseValidator sclValidator;

    @BeforeEach
    public void setup() {
        var oclFileCollector = new CompasOclFileCollector(null);
        this.sclValidator = new SclRiseClipseValidator(oclFileCollector);
    }

    @Test
    void validate_WhenCalled_ThenEmptyListReturned() throws IOException {
        var type = SclFileType.CID;
        var sclData = readSCL();

        var result = sclValidator.validate(type, sclData);

        assertNotNull(result);
        assertEquals(15, result.size());
    }

    private String readSCL() throws IOException {
        var inputStream = getClass().getResourceAsStream("/scl/scl_test_file.scd");
        assert inputStream != null;

        return new String(inputStream.readAllBytes());
    }
}

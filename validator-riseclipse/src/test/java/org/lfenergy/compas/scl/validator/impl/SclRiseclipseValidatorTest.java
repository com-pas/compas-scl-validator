// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.lfenergy.compas.scl.extensions.model.SclFileType;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class SclRiseclipseValidatorTest {
    private SclRiseclipseValidator sclValidator = new SclRiseclipseValidator();

    @Test
    void validate_WhenCalled_ThenEmptyListReturned() {
        var type = SclFileType.CID;
        var sclData = "Some String";

        var result = sclValidator.validate(type, sclData);

        assertNotNull(result);
        assertEquals(0, result.size());
    }
}
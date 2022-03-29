// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.lfenergy.compas.scl.extensions.model.SclFileType;
import org.lfenergy.compas.scl.validator.SclValidator;
import org.lfenergy.compas.scl.validator.model.ValidationError;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SclValidatorServiceTest {

    private SclValidatorService sclValidatorService;

    @Mock
    private SclValidator sclValidator;

    @BeforeEach
    void beforeEach() {
        sclValidatorService = new SclValidatorService(sclValidator);
    }

    @Test
    void validate_WhenCalled_ThenExpectedListReturned() {
        var type = SclFileType.CID;
        var sclData = "Some String";

        when(sclValidator.validate(type, sclData)).thenReturn(List.of(new ValidationError()));

        var result = sclValidatorService.validate(type, sclData);

        assertNotNull(result);
        assertEquals(1, result.size());

        verify(sclValidator, times(1)).validate(type, sclData);
    }
}
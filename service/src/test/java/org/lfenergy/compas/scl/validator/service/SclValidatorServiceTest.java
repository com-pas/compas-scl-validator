// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.lfenergy.compas.scl.extensions.model.SclFileType;
import org.lfenergy.compas.scl.validator.SclValidator;
import org.lfenergy.compas.scl.validator.xsd.SclXsdValidator;
import org.lfenergy.compas.scl.validator.model.ValidationError;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SclValidatorServiceTest {

    @InjectMocks
    private SclValidatorService sclValidatorService;

    @Mock
    private SclValidator sclValidator;

    @Mock
    private SclXsdValidator xsdValidator;

    @Test
    void validate_WhenCalled_ThenExpectedListReturned() {
        var type = SclFileType.CID;
        var sclData = "Some String";

        when(sclValidator.validate(type, sclData)).thenReturn(List.of(new ValidationError()));
        when(xsdValidator.validate(sclData)).thenReturn(Collections.emptyList());

        var result = sclValidatorService.validate(type, sclData);

        assertNotNull(result);
        assertEquals(1, result.size());

        verify(sclValidator, times(1)).validate(type, sclData);
        verify(xsdValidator, times(1)).validate(sclData);
    }

    @Test
    void validate_WhenXsdErrorsAreReturned_ThenSclValidatorIsNeverCalled() {
        var type = SclFileType.CID;
        var sclData = "Some String";

        when(xsdValidator.validate(sclData)).thenReturn(List.of(new ValidationError()));

        var result = sclValidatorService.validate(type, sclData);

        assertNotNull(result);
        assertEquals(1, result.size());

        verify(xsdValidator, times(1)).validate(sclData);
        verify(sclValidator, never()).validate(type, sclData);
    }
}
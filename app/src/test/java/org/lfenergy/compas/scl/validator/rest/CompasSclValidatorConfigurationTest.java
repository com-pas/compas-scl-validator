// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.rest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompasSclValidatorConfigurationTest {
    @Mock
    private ValidatorProperties validatorProperties;

    @Test
    void createElementConverter_WhenCalled_ThenObjectReturned() {
        assertNotNull(new CompasSclValidatorConfiguration().createElementConverter());
    }

    @Test
    void createOclFileCollector_WhenCalled_ThenObjectReturned() {
        when(validatorProperties.oclCustomDirectory()).thenReturn("/somedirectory");

        assertNotNull(new CompasSclValidatorConfiguration().createOclFileCollector(validatorProperties));

        verify(validatorProperties, times(1)).oclCustomDirectory();
    }
}
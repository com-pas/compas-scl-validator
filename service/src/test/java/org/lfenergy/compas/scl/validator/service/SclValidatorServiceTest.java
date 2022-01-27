// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.lfenergy.compas.scl.validator.SclValidator;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SclValidatorServiceTest {

    private SclValidatorService sclValidatorService;

    @Mock
    private SclValidator sclValidator;

    @BeforeEach
    void beforeEach() {
        sclValidatorService = new SclValidatorService(sclValidator);
    }

}
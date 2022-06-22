// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lfenergy.compas.scl.extensions.model.SclFileType;
import org.lfenergy.compas.scl.validator.exception.SclValidatorException;
import org.lfenergy.compas.scl.validator.util.OclUtil;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.lfenergy.compas.scl.validator.exception.SclValidatorErrorCode.LOADING_SCL_FILE_ERROR_CODE;
import static org.lfenergy.compas.scl.validator.util.TestSupportUtil.readSCL;

class SclModelLoaderTest {
    private SclModelLoader loader;

    @BeforeEach
    void setup() {
        // Initialize the OCL Libraries
        OclUtil.setupOcl();

        loader = new SclModelLoader();
    }

    @Test
    void load_WhenCalledWithValidSCXML_ThenResourceLoaded() throws IOException {
        var sclData = readSCL("example.scd");

        var result = loader.load(sclData, SclFileType.SCD);

        assertNotNull(result);
    }

    @Test
    void load_WhenCalledWithInvalidSCXML_ThenExceptionThrown() throws IOException {
        var sclData = readSCL("invalid.scd");

        var exception = assertThrows(SclValidatorException.class,
                () -> loader.load(sclData, SclFileType.SCD));

        assertEquals(LOADING_SCL_FILE_ERROR_CODE, exception.getErrorCode());
    }
}
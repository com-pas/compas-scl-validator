// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.common;

import org.junit.jupiter.api.Test;
import org.lfenergy.compas.scl.validator.exception.SclValidatorException;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;
import static org.lfenergy.compas.scl.validator.exception.SclValidatorErrorCode.DETERMINING_ID_FAILED;

class NsdocInfoTest {
    @Test
    void getId_WhenCalledWithValidNsDocFile_ThenIdFromFileReturned() {
        var nsdocFile = new File(getClass().getResource("/nsdoc/testFile74.nsdoc").getFile());

        var nsdocInfo = new NsdocInfo(nsdocFile);

        assertNotNull(nsdocInfo.getId());
        assertEquals("IEC 61850-7-4", nsdocInfo.getId());
    }

    @Test
    void getId_WhenCalledWithInvalidNsDocFile_ThenExceptionThrownDuringConstruction() {
        var nsdocFile = new File(getClass().getResource("/nsdoc/invalid.nsdoc").getFile());

        var exception = assertThrows(SclValidatorException.class, () -> new NsdocInfo(nsdocFile));
        assertNotNull(DETERMINING_ID_FAILED, exception.getErrorCode());
    }
}
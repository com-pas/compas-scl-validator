// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.xsd;

import org.junit.jupiter.api.Test;
import org.lfenergy.compas.scl.validator.exception.SclValidatorException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;
import static org.lfenergy.compas.scl.validator.exception.SclValidatorErrorCode.LOADING_SCL_FILE_ERROR_CODE;

class SclInfoTest {
    @Test
    void getSclInfo_WhenCalledWithValidSclFile_ThenSclInfoFromFileReturned() throws IOException {
        var scdFile = new File(getClass().getResource("/scl/example.scd").getFile());
        var sclInfo = new SclInfo(Files.readString(scdFile.toPath()));

        assertNotNull(sclInfo.getVersion());
        assertNotNull(sclInfo.getRelease());
        assertNotNull(sclInfo.getRevision());
        assertEquals("2007", sclInfo.getVersion());
        assertEquals("4", sclInfo.getRelease());
        assertEquals("B", sclInfo.getRevision());
    }

    @Test
    void getSclInfo_WhenCalledWithInvalidSclFile_ThenExceptionThrownDuringConstruction() throws IOException {
        var scdFile = new File(getClass().getResource("/scl/invalid.scd").getFile());
        
        var path = scdFile.toPath();
        var content = Files.readString(path);

        var exception = assertThrows(SclValidatorException.class, () -> new SclInfo(content));
        assertNotNull(LOADING_SCL_FILE_ERROR_CODE, exception.getErrorCode());
    }
}

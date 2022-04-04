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
    void constructor_WhenCalledWithInvalidSclFile_ThenExceptionThrownDuringConstruction() throws IOException {
        var scdFile = new File(getClass().getResource("/scl/invalid.scd").getFile());

        var path = scdFile.toPath();
        var content = Files.readString(path);

        var exception = assertThrows(SclValidatorException.class, () -> new SclInfo(content));
        assertEquals(LOADING_SCL_FILE_ERROR_CODE, exception.getErrorCode());
    }

    @Test
    void getSclVersion_WhenCalledWithValidSclFile_ThenSclVersionFromFileReturned() throws IOException {
        var scdFile = new File(getClass().getResource("/scl/example.scd").getFile());
        var sclInfo = new SclInfo(Files.readString(scdFile.toPath()));

        assertNotNull(sclInfo.getSclVersion());
        assertEquals("2007B4", sclInfo.getSclVersion());
    }

    @Test
    void getSclVersion_WhenCalledWithSclFileWithMissingVersion_ThenSclVersionFromFileReturned() throws IOException {
        var scdFile = new File(getClass().getResource("/scl/validation/example-with-missing-version.scd").getFile());
        var sclInfo = new SclInfo(Files.readString(scdFile.toPath()));

        assertNotNull(sclInfo.getSclVersion());
        assertEquals("", sclInfo.getSclVersion());
    }
}

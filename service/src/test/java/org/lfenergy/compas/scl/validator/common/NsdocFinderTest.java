// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.common;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lfenergy.compas.scl.validator.model.NsdocFile;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class NsdocFinderTest {
    private NsdocFinder finder;

    @BeforeEach
    void setup() {
        finder = new NsdocFinder("./src/test/data/nsdoc");
    }

    @Test
    void getNsdocFiles_WhenCalledWithConfiguredDirectory_ThenExpectCorrectNumberOfEntriesReturned() {
        var files = finder.getNsdocFiles();

        assertNotNull(files);
        // 2 Files expected, because 1 file is invalid.
        assertEquals(2, files.size());

        var fileList = new ArrayList<>(files);
        assertEquals("IEC 61850-7-3", fileList.get(0).getNsdocId());
        assertEquals("IEC 61850-7-4", fileList.get(1).getNsdocId());
    }

    @Test
    void getNsdocFiles_WhenCalledWithNonExistingDirectory_ThenNoEntriesReturned() {
        finder = new NsdocFinder("./src/test/data/non-existing");
        var files = finder.getNsdocFiles();

        assertNotNull(files);
        assertEquals(0, files.size());
    }

    @Test
    void getNsdocFiles_WhenCalledWithFileAndNotDirectory_ThenNoEntriesReturned() {
        finder = new NsdocFinder("./src/test/data/nsdoc/invalid.doc");
        var files = finder.getNsdocFiles();

        assertNotNull(files);
        assertEquals(0, files.size());
    }

    @Test
    void getNsdocFile_WhenUseTheIDsToRetrieveEntries_ThenContentIsReturned() {
        var files = finder.getNsdocFiles();
        var content = files.stream()
                .map(NsdocFile::getId)
                .map(id -> finder.getNsdocFile(id))
                .findFirst()
                .orElse(null);

        assertNotNull(content);
    }
}

// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.collector;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CompasOclFileCollectorTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(CompasOclFileCollectorTest.class);

    @Test
    void getDefaultOclFiles_WhenCalledWithoutCustomDirectory_ThenListReturned() {
        var collector = new CompasOclFileCollector(null);

        var result = collector.getOclFiles();

        assertNotNull(result);
        assertEquals(226, result.size());
    }

    @Test
    void getDefaultOclFiles_WhenCalledWithCustomDirectory_ThenListReturned() {
        var collector = new CompasOclFileCollector("./src/test/data/ocl");

        var result = collector.getOclFiles();

        assertNotNull(result);
        assertEquals(227, result.size());
    }
}
// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.resource;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class OclFileCollectorTest {
    private final OclFileCollector collector = new OclFileCollector();

    @Test
    void getDefaultOclFiles_WhenCalled_ThenListReturned() {
        var result = collector.getDefaultOclFiles();

        assertNotNull(result);
        assertEquals(226, result.size());
    }
}
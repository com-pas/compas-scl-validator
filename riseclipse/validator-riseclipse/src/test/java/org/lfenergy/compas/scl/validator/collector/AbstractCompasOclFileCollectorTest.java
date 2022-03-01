// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.collector;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public abstract class AbstractCompasOclFileCollectorTest {
    protected void executeAndValidateOclFileCollector(OclFileCollector collector, int expectedFiles) {
        var result = collector.getOclFiles();

        assertNotNull(result);
        assertEquals(expectedFiles, result.size());
    }
}

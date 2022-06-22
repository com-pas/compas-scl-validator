// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.collector;

import org.junit.jupiter.api.Test;

class CompasOclFileCollectorTest extends AbstractCompasOclFileCollectorTest {
    @Test
    void getDefaultOclFiles_WhenCalledWithoutCustomDirectory_ThenListReturned() {
        assertValidateOclFileCollector(new CompasOclFileCollector(null), 212);
    }

    @Test
    void getDefaultOclFiles_WhenCalledWithCustomDirectory_ThenListReturned() {
        assertValidateOclFileCollector(new CompasOclFileCollector("./src/test/data/ocl"), 213);
    }
}

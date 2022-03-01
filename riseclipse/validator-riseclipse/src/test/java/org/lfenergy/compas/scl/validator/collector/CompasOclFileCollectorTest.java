// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.collector;

import org.junit.jupiter.api.Test;

class CompasOclFileCollectorTest extends AbstractCompasOclFileCollectorTest {
    @Test
    void getDefaultOclFiles_WhenCalledWithoutCustomDirectory_ThenListReturned() {
        executeAndValidateOclFileCollector(new CompasOclFileCollector(null), 226);
    }

    @Test
    void getDefaultOclFiles_WhenCalledWithCustomDirectory_ThenListReturned() {
        executeAndValidateOclFileCollector(new CompasOclFileCollector("./src/test/data/ocl"), 227);
    }
}

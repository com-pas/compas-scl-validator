// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.rest;

import org.junit.jupiter.api.Test;
import org.lfenergy.compas.scl.validator.collector.AbstractCompasOclFileCollectorTest;
import org.lfenergy.compas.scl.validator.collector.CompasOclFileCollector;

/**
 * This test is added in this project to check if loading the OCL Files from a JAR File is also working.
 * Loading works in a different way from the module itself and from another module through a JAR File.
 */
class CompasOclFileCollectorFromJarTest extends AbstractCompasOclFileCollectorTest {
    @Test
    void getDefaultOclFiles_WhenCalledWithoutCustomDirectory_ThenListReturned() {
        executeAndValidateOclFileCollector(new CompasOclFileCollector(null), 226);
    }

    @Test
    void getDefaultOclFiles_WhenCalledWithCustomDirectory_ThenListReturned() {
        executeAndValidateOclFileCollector(new CompasOclFileCollector("./src/test/data/ocl"), 227);
    }
}

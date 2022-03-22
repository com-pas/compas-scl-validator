// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.util;

import org.eclipse.emf.common.util.URI;
import org.junit.jupiter.api.Test;
import org.lfenergy.compas.scl.extensions.model.SclFileType;
import org.lfenergy.compas.scl.validator.collector.CompasOclFileCollector;
import org.lfenergy.compas.scl.validator.collector.OclFileCollector;

import static org.junit.jupiter.api.Assertions.*;

class OclUtilTest {
    private OclFileCollector collector = new CompasOclFileCollector(null);

    @Test
    void constructor_WhenConstructorCalled_ThenShouldThrowExceptionCauseForbidden() {
        assertThrows(UnsupportedOperationException.class, OclUtil::new);
    }

    @Test
    void includeOnType_WhenCalledWithExpectedSclFileTypeUri_ThenReturnTrue() {
        assertTrue(executeTest("/ocl/FileSpecifics/CID/ExtRef.ocl"));
    }

    @Test
    void includeOnType_WhenCalledWithUnexpectedSclFileTypeUri_ThenReturnFalse() {
        assertFalse(executeTest("/ocl/FileSpecifics/ICD/DOType.ocl"));
    }

    @Test
    void includeOnType_WhenCalledWithCommonUri_ThenReturnTrue() {
        assertTrue(executeTest("/ocl/FileSpecifics/Common/ReportControl.ocl"));
    }

    @Test
    void includeOnType_WhenCalledWithNoFileSpecificUri_ThenReturnTrue() {
        assertTrue(executeTest("/ocl/SemanticConstraints/Server.ocl"));
    }

    private boolean executeTest(String oclFileName) {
        var uri = getResource(oclFileName);
        return OclUtil.includeOnType(uri, SclFileType.CID);
    }

    private URI getResource(String oclFileName) {
        return collector.getOclFiles()
                .stream()
                .filter(uri -> uri.toFileString().endsWith(oclFileName))
                .findFirst()
                .orElseThrow();
    }
}

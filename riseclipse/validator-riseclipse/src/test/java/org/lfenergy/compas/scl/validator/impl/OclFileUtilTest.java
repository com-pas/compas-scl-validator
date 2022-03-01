// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.impl;

import org.eclipse.emf.common.util.URI;
import org.junit.jupiter.api.Test;
import org.lfenergy.compas.scl.extensions.model.SclFileType;
import org.lfenergy.compas.scl.validator.collector.CompasOclFileCollector;
import org.lfenergy.compas.scl.validator.collector.OclFileCollector;

import static org.junit.jupiter.api.Assertions.*;

class OclFileUtilTest {
    private OclFileCollector collector = new CompasOclFileCollector(null);

    @Test
    void constructor_WhenConstructorCalled_ThenShouldThrowExceptionCauseForbidden() {
        assertThrows(UnsupportedOperationException.class, OclFileUtil::new);
    }

    @Test
    void includeOnType_WhenCalledWithExpectedSclFileTypeUri_ThenReturnTrue() {
        var oclFileName = "/ocl/FileSpecifics/CID/ExtRef.ocl";
        var uri = getResource(oclFileName);

        var result = OclFileUtil.includeOnType(uri, SclFileType.CID);

        assertTrue(result);
    }

    @Test
    void includeOnType_WhenCalledWithUnexpectedSclFileTypeUri_ThenReturnFalse() {
        var oclFileName = "/ocl/FileSpecifics/ICD/DOType.ocl";
        var uri = getResource(oclFileName);

        var result = OclFileUtil.includeOnType(uri, SclFileType.CID);

        assertFalse(result);
    }

    @Test
    void includeOnType_WhenCalledWithCommonUri_ThenReturnTrue() {
        var oclFileName = "/ocl/FileSpecifics/Common/ReportControl.ocl";
        var uri = getResource(oclFileName);

        var result = OclFileUtil.includeOnType(uri, SclFileType.CID);

        assertTrue(result);

    }

    @Test
    void includeOnType_WhenCalledWithNoFileSpecificUri_ThenReturnTrue() {
        var oclFileName = "/ocl/SemanticConstraints/Server.ocl";
        var uri = getResource(oclFileName);

        var result = OclFileUtil.includeOnType(uri, SclFileType.CID);

        assertTrue(result);
    }

    private URI getResource(String oclFileName) {
        return collector.getOclFiles()
                .stream()
                .filter(uri -> uri.toFileString().endsWith(oclFileName))
                .findFirst()
                .orElseThrow();
    }
}
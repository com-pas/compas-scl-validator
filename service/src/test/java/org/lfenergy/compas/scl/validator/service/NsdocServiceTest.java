// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.lfenergy.compas.scl.validator.common.NsdocFinder;
import org.lfenergy.compas.scl.validator.model.NsdocFile;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NsdocServiceTest {

    private NsdocService nsdocService;

    @Mock
    private NsdocFinder nsdocFinder;

    @BeforeEach
    void beforeEach() {
        nsdocService = new NsdocService(nsdocFinder);
    }

    @Test
    void list_WhenCalled_ThenEmptyListReturned() {
        when(nsdocFinder.getNsdocFiles()).thenReturn(List.of(new NsdocFile()));

        var result = nsdocService.list();

        assertNotNull(result);
        assertEquals(1, result.size());

        verify(nsdocFinder, times(1)).getNsdocFiles();
    }

    @Test
    void get_WhenCalled_ThenContentReturned() {
        String id = "IEC 61850-7-3";
        String content = "<some><content/></some>";

        when(nsdocFinder.getNsdocFile(id)).thenReturn(content);

        var result = nsdocService.get(id);

        assertNotNull(result);
        assertEquals(content, result);

        verify(nsdocFinder, times(1)).getNsdocFile(id);
    }
}
// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.xsd.resourceresolver;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class ResourceInputTest {
    @Test
    void allGetters_WhenCalled_ThenCorrectValueIsReturned() throws IOException {
        var firstInputStream = this.getClass().getResourceAsStream("/scl/example.scd");
        var secondInputStream = this.getClass().getResourceAsStream("/scl/example.scd");

        var input = new ResourceInput("1", "2", firstInputStream);

        assertEquals("1", input.getPublicId());
        assertEquals("2", input.getSystemId());
        assertNull(input.getBaseURI());
        assertNull(input.getByteStream());
        assertFalse(input.getCertifiedText());
        assertNull(input.getCharacterStream());
        assertNull(input.getEncoding());

        var expectedStringData = new String(secondInputStream.readAllBytes(), StandardCharsets.UTF_8);
        assertEquals(expectedStringData.hashCode(), input.getStringData().hashCode());
    }
}

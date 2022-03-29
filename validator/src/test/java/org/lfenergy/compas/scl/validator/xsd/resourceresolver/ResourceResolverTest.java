// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.xsd.resourceresolver;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ResourceResolverTest {
    @Test
    void resolveResource_WhenCalled_ThenACorrectResourceInputIsReturned() {
        var resolver = new ResourceResolver("1");

        var input = resolver.resolveResource("type", "namespace", "1", "2", "base");

        assertEquals("1", input.getPublicId());
        assertEquals("2", input.getSystemId());
    }
}

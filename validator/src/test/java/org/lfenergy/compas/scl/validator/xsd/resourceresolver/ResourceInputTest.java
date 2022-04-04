// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.xsd.resourceresolver;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lfenergy.compas.scl.validator.exception.SclValidatorException;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.lfenergy.compas.scl.validator.exception.SclValidatorErrorCode.RESOURCE_RESOLVER_FAILED;

class ResourceInputTest {

    private ResourceInput resourceInput;

    @BeforeEach
    void beforeEach() {
        var inputStream = this.getClass().getResourceAsStream("/scl/example.scd");
        resourceInput = new ResourceInput("1", "2", inputStream);
    }

    @Test
    void allGetters_WhenCalled_ThenCorrectValueIsReturned() throws IOException {
        var secondInputStream = this.getClass().getResourceAsStream("/scl/example.scd");

        assertEquals("1", resourceInput.getPublicId());
        assertEquals("2", resourceInput.getSystemId());
        assertNull(resourceInput.getBaseURI());
        assertNull(resourceInput.getByteStream());
        assertFalse(resourceInput.getCertifiedText());
        assertNull(resourceInput.getCharacterStream());
        assertNull(resourceInput.getEncoding());

        var expectedStringData = new String(secondInputStream.readAllBytes(), StandardCharsets.UTF_8);
        assertEquals(expectedStringData.hashCode(), resourceInput.getStringData().hashCode());
    }

    @Test
    void getStringData_WhenCalledWithInvalidInputStream_ThenExceptionIsThrown() {
        var input = new ResourceInput("1", "2", null);

        var exception = assertThrows(SclValidatorException.class, input::getStringData);
        assertEquals(RESOURCE_RESOLVER_FAILED, exception.getErrorCode());
    }

    @Test
    void setPublicId_WhenCalled_ThenCorrectValueIsSet() {
        resourceInput.setPublicId("10");
        assertEquals("10", resourceInput.getPublicId());
    }

    @Test
    void setSystemId_WhenCalled_ThenCorrectValueIsSet() {
        resourceInput.setSystemId("10");
        assertEquals("10", resourceInput.getSystemId());
    }

    @Test
    void notUsedSetters_WhenCalled_NoExceptionsAreThrown() {
        try {
            resourceInput.setBaseURI("baseURI");
            resourceInput.setByteStream(null);
            resourceInput.setCertifiedText(true);
            resourceInput.setCharacterStream(null);
            resourceInput.setEncoding("encoding");
            resourceInput.setStringData("stringData");
        } catch (Exception e) {
            fail("All setters without a body should be called without exceptions thrown");
        }
    }
}

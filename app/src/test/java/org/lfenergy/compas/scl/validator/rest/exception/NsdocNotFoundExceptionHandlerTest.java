// SPDX-FileCopyrightText: 2021 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.rest.exception;

import org.junit.jupiter.api.Test;
import org.lfenergy.compas.core.jaxrs.model.ErrorResponse;
import org.lfenergy.compas.scl.validator.exception.NsdocFileNotFoundException;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class NsdocNotFoundExceptionHandlerTest {
    @Test
    void toResponse_WhenCalledWithException_ThenNotFoundReturnedWithBody() {
        var exception = new NsdocFileNotFoundException("Some message that will only be logged");
        var handler = new NsdocNotFoundExceptionHandler();

        var result = handler.toResponse(exception);
        assertEquals(NOT_FOUND.getStatusCode(), result.getStatus());
        var errorMessage = ((ErrorResponse) result.getEntity()).getErrorMessages().get(0);
        assertEquals(exception.getErrorCode(), errorMessage.getCode());
        assertEquals(exception.getMessage(), errorMessage.getMessage());
        assertNull(errorMessage.getProperty());
    }
}

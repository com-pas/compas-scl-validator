// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.rest.v1;

import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.quarkus.test.security.TestSecurity;
import org.junit.jupiter.api.Test;
import org.lfenergy.compas.core.commons.model.ErrorMessage;
import org.lfenergy.compas.core.commons.model.ErrorResponse;
import org.lfenergy.compas.core.websocket.ErrorResponseDecoder;
import org.lfenergy.compas.scl.extensions.model.SclFileType;
import org.lfenergy.compas.scl.validator.model.ValidationError;
import org.lfenergy.compas.scl.validator.rest.v1.model.SclValidateRequest;
import org.lfenergy.compas.scl.validator.rest.v1.model.SclValidateResponse;
import org.lfenergy.compas.scl.validator.rest.v1.websocket.SclValidateRequestEncoder;
import org.lfenergy.compas.scl.validator.rest.v1.websocket.SclValidateResponseDecoder;
import org.lfenergy.compas.scl.validator.service.SclValidatorService;
import org.lfenergy.compas.scl.validator.util.TestSupportUtil;

import javax.websocket.ClientEndpoint;
import javax.websocket.ContainerProvider;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import java.net.URI;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.lfenergy.compas.core.commons.exception.CompasErrorCode.WEBSOCKET_DECODER_ERROR_CODE;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@QuarkusTest
@TestSecurity(user = "test-user")
class SclValidatorServerEndpointTest {
    private static final LinkedBlockingDeque<ValidationError> validationErrors = new LinkedBlockingDeque<>();
    private static final LinkedBlockingDeque<ErrorMessage> errorQueue = new LinkedBlockingDeque<>();

    @InjectMock
    private SclValidatorService sclValidatorService;

    @TestHTTPResource("/validate-ws/v1/SCD")
    private URI uri;

    @Test
    void validate_WhenCalled_ThenExpectedResponseIsRetrieved() throws Exception {
        var encoder = new SclValidateRequestEncoder();
        var sclFileTye = SclFileType.SCD;
        var request = new SclValidateRequest();
        request.setSclData(TestSupportUtil.readSCL("scl-1.scd"));

        when(sclValidatorService.validate(sclFileTye, request.getSclData()))
                .thenReturn(List.of(new ValidationError()));

        try (Session session = ContainerProvider.getWebSocketContainer().connectToServer(Client.class, uri)) {
            session.getAsyncRemote().sendText(encoder.encode(request));

            assertNotNull(validationErrors.poll(10, TimeUnit.SECONDS));
            assertEquals(0, validationErrors.size());
            verify(sclValidatorService).validate(sclFileTye, request.getSclData());
        }
    }

    @Test
    void validate_WhenCalledWithInvalidText_ThenExceptionThrown() throws Exception {
        try (Session session = ContainerProvider.getWebSocketContainer().connectToServer(ErrorClient.class, uri)) {
            session.getAsyncRemote().sendText("Invalid Message");

            var errorMessage = errorQueue.poll(10, TimeUnit.SECONDS);
            assertEquals(WEBSOCKET_DECODER_ERROR_CODE, errorMessage.getCode());
            assertEquals(0, errorQueue.size());
        }
    }

    @ClientEndpoint(decoders = SclValidateResponseDecoder.class)
    private static class Client {
        @OnMessage
        public void onMessage(SclValidateResponse response) {
            validationErrors.addAll(response.getValidationErrorList());
        }
    }

    @ClientEndpoint(decoders = ErrorResponseDecoder.class)
    static class ErrorClient {
        @OnMessage
        public void onMessage(ErrorResponse response) {
            if (response.getErrorMessages().size() > 0) {
                errorQueue.addAll(response.getErrorMessages());
            }
        }
    }
}

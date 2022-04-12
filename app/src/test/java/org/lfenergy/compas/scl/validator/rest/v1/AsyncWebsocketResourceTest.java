// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.rest.v1;

import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.quarkus.test.security.TestSecurity;
import org.junit.jupiter.api.Test;
import org.lfenergy.compas.scl.extensions.model.SclFileType;
import org.lfenergy.compas.scl.validator.model.ValidationError;
import org.lfenergy.compas.scl.validator.rest.v1.model.SclValidateRequest;
import org.lfenergy.compas.scl.validator.rest.v1.model.SclValidateResponse;
import org.lfenergy.compas.scl.validator.rest.v1.websocket.AsyncWebsocketRequestEncoder;
import org.lfenergy.compas.scl.validator.rest.v1.websocket.AsyncWebsocketResponseDecoder;
import org.lfenergy.compas.scl.validator.service.SclValidatorService;

import javax.websocket.ClientEndpoint;
import javax.websocket.ContainerProvider;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@QuarkusTest
@TestSecurity(user = "test-user")
class AsyncWebsocketResourceTest {
    private static final LinkedBlockingDeque<ValidationError> validationErrors = new LinkedBlockingDeque<>();

    @InjectMock
    private SclValidatorService sclValidatorService;

    @TestHTTPResource("/compas-scl-validator/validate-ws/v1/SCD")
    private URI uri;

    @Test
    public void updateSCL_WhenCalled_ThenExpectedResponseIsRetrieved() throws Exception {
        var encoder = new AsyncWebsocketRequestEncoder();
        var sclFileTye = SclFileType.SCD;
        var request = new SclValidateRequest();
        request.setSclData(readFile());

        when(sclValidatorService.validate(sclFileTye, request.getSclData()))
                .thenReturn(List.of(new ValidationError()));

        try (Session session = ContainerProvider.getWebSocketContainer().connectToServer(Client.class, uri)) {
            session.getAsyncRemote().sendText(encoder.encode(request));

            assertNotNull(validationErrors.poll(10, TimeUnit.SECONDS));
            assertEquals(0, validationErrors.size());
            verify(sclValidatorService, times(1)).validate(sclFileTye, request.getSclData());
        }
    }

    @ClientEndpoint(decoders = AsyncWebsocketResponseDecoder.class)
    public static class Client {
        @OnMessage
        public void onMessage(SclValidateResponse response) {
            validationErrors.addAll(response.getValidationErrorList());
        }
    }

    private String readFile() throws IOException {
        var resource = requireNonNull(getClass().getResource("/scl/scl-1.scd"));
        var path = Paths.get(resource.getPath());
        return String.join("\n", Files.readAllLines(path));
    }
}

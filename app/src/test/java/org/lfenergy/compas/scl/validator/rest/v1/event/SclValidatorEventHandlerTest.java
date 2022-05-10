// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.rest.v1.event;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.lfenergy.compas.scl.extensions.model.SclFileType;
import org.lfenergy.compas.scl.validator.model.ValidationError;
import org.lfenergy.compas.scl.validator.rest.v1.model.SclValidateResponse;
import org.lfenergy.compas.scl.validator.service.SclValidatorService;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SclValidatorEventHandlerTest {
    @Mock
    private SclValidatorService service;

    @InjectMocks
    private SclValidatorEventHandler eventHandler;

    @Test
    void validateWebsocketsEvent_WhenCalled_ThenExpectedCallsAreMade() throws IOException {
        var veList = new ArrayList<ValidationError>();
        var type = SclFileType.CID;
        var sclData = "Some SCL Data";

        var session = Mockito.mock(Session.class);
        var async = Mockito.mock(RemoteEndpoint.Async.class);
        when(session.getAsyncRemote()).thenReturn(async);

        var request = Mockito.mock(SclValidatorEventRequest.class);
        when(request.getSession()).thenReturn(session);
        when(request.getType()).thenReturn(type);
        when(request.getSclData()).thenReturn(sclData);

        when(service.validate(type, sclData)).thenReturn(veList);

        eventHandler.validateWebsocketsEvent(request);

        verify(session, times(1)).getAsyncRemote();
        ArgumentCaptor<SclValidateResponse> captor = ArgumentCaptor.forClass(SclValidateResponse.class);
        verify(async, times(1)).sendObject(captor.capture());
        var response = captor.getValue();
        assertEquals(veList, response.getValidationErrorList());

        verify(service, times(1)).validate(type, sclData);
        verify(request, times(1)).getSession();
        verify(request, times(1)).getType();
        verify(request, times(1)).getSclData();
    }
}

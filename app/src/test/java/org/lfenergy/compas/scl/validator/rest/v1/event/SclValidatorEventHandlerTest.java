// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.rest.v1.event;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.lfenergy.compas.core.commons.exception.CompasException;
import org.lfenergy.compas.core.commons.model.ErrorResponse;
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
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.lfenergy.compas.core.commons.exception.CompasErrorCode.WEBSOCKET_GENERAL_ERROR_CODE;
import static org.lfenergy.compas.scl.validator.exception.SclValidatorErrorCode.LOADING_OCL_FILES_FAILED;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SclValidatorEventHandlerTest {
    @Mock
    private SclValidatorService service;

    @InjectMocks
    private SclValidatorEventHandler eventHandler;

    @Test
    void validateWebsocketsEvent_WhenCalled_ThenExpectedCallsAreMade() {
        var veList = new ArrayList<ValidationError>();
        var type = SclFileType.CID;
        var sclData = "Some SCL Data";

        var session = Mockito.mock(Session.class);
        var async = Mockito.mock(RemoteEndpoint.Async.class);
        when(session.getAsyncRemote()).thenReturn(async);
        var request = new SclValidatorEventRequest(session, type, sclData);

        when(service.validate(type, sclData)).thenReturn(veList);

        eventHandler.validateWebsocketsEvent(request);

        verify(session).getAsyncRemote();
        ArgumentCaptor<SclValidateResponse> captor = ArgumentCaptor.forClass(SclValidateResponse.class);
        verify(async).sendObject(captor.capture());
        var response = captor.getValue();
        assertEquals(veList, response.getValidationErrorList());

        verify(service).validate(type, sclData);
    }

    @Test
    void validateWebsocketsEvent_WhenCalledAndCompasExceptionThrownByService_ThenErrorResponseReturned() {
        var type = SclFileType.CID;
        var sclData = "Some SCL Data";
        var errorMessage = "Some Error";

        var session = Mockito.mock(Session.class);
        var async = Mockito.mock(RemoteEndpoint.Async.class);
        when(session.getAsyncRemote()).thenReturn(async);
        var request = new SclValidatorEventRequest(session, type, sclData);

        when(service.validate(type, sclData))
                .thenThrow(new CompasException(LOADING_OCL_FILES_FAILED, errorMessage));

        eventHandler.validateWebsocketsEvent(request);

        verify(session).getAsyncRemote();
        ArgumentCaptor<ErrorResponse> captor = ArgumentCaptor.forClass(ErrorResponse.class);
        verify(async).sendObject(captor.capture());
        var response = captor.getValue();
        assertEquals(1, response.getErrorMessages().size());
        var message = response.getErrorMessages().get(0);
        assertEquals(LOADING_OCL_FILES_FAILED, message.getCode());
        assertEquals(errorMessage, message.getMessage());

        verify(service).validate(type, sclData);
    }

    @Test
    void validateWebsocketsEvent_WhenCalledAndRuntimeExceptionThrownByService_ThenErrorResponseReturned() {
        var type = SclFileType.CID;
        var sclData = "Some SCL Data";
        var errorMessage = "Some Error";

        var session = Mockito.mock(Session.class);
        var async = Mockito.mock(RemoteEndpoint.Async.class);
        when(session.getAsyncRemote()).thenReturn(async);
        var request = new SclValidatorEventRequest(session, type, sclData);

        when(service.validate(type, sclData)).thenThrow(new RuntimeException(errorMessage));

        eventHandler.validateWebsocketsEvent(request);

        verify(session).getAsyncRemote();
        ArgumentCaptor<ErrorResponse> captor = ArgumentCaptor.forClass(ErrorResponse.class);
        verify(async).sendObject(captor.capture());
        var response = captor.getValue();
        assertEquals(1, response.getErrorMessages().size());
        var message = response.getErrorMessages().get(0);
        assertEquals(WEBSOCKET_GENERAL_ERROR_CODE, message.getCode());
        assertEquals(errorMessage, message.getMessage());

        verify(service).validate(type, sclData);
    }
}

// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.rest.v1.event;

import io.quarkus.vertx.ConsumeEvent;
import org.lfenergy.compas.scl.validator.rest.v1.model.SclValidateResponse;
import org.lfenergy.compas.scl.validator.service.SclValidatorService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;

@ApplicationScoped
public class AsyncWebsocketEventHandler {
    private final SclValidatorService sclValidatorService;

    @Inject
    public AsyncWebsocketEventHandler(SclValidatorService sclValidatorService) {
        this.sclValidatorService = sclValidatorService;
    }

    @ConsumeEvent(value = "validate-ws", blocking = true)
    public void validateEvent(AsyncWebsocketEventRequest request) throws IOException {
        var response = new SclValidateResponse();
        response.setValidationErrorList(sclValidatorService.validate(request.getType(), request.getSclData()));

        var session = request.getSession();
        session.getAsyncRemote().sendObject(response);
        session.close();
    }
}

// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.websocket.event;

import io.quarkus.vertx.ConsumeEvent;
import org.lfenergy.compas.core.websocket.WebsocketHandler;
import org.lfenergy.compas.scl.validator.service.SclValidatorService;
import org.lfenergy.compas.scl.validator.websocket.event.model.SclValidatorEventRequest;
import org.lfenergy.compas.scl.validator.websocket.v1.model.SclValidateWsResponse;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * Event Handler used to execute the validation asynchronized.
 */
@ApplicationScoped
public class SclValidatorEventHandler {
    private final SclValidatorService sclValidatorService;

    @Inject
    public SclValidatorEventHandler(SclValidatorService sclValidatorService) {
        this.sclValidatorService = sclValidatorService;
    }

    @ConsumeEvent(value = "validate-ws", blocking = true)
    public void validateWebsocketsEvent(SclValidatorEventRequest request) {
        new WebsocketHandler<SclValidateWsResponse>().execute(request.getSession(), () -> {
            var response = new SclValidateWsResponse();
            response.setValidationErrorList(sclValidatorService.validate(request.getType(), request.getSclData()));
            return response;
        });
    }
}

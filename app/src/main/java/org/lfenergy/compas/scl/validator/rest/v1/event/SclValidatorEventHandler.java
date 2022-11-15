// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.rest.v1.event;

import io.quarkus.vertx.ConsumeEvent;
import org.lfenergy.compas.core.websocket.WebsocketHandler;
import org.lfenergy.compas.scl.validator.rest.v1.model.SclValidateResponse;
import org.lfenergy.compas.scl.validator.service.SclValidatorService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

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
        new WebsocketHandler<SclValidateResponse>().execute(request.getSession(), () -> {
            var response = new SclValidateResponse();
            response.setValidationErrorList(sclValidatorService.validate(request.getType(), request.getSclData()));
            return response;
        });
    }
}

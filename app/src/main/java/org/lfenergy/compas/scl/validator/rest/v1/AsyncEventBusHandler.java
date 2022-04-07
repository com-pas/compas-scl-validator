// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.rest.v1;

import io.quarkus.vertx.ConsumeEvent;
import org.lfenergy.compas.scl.extensions.model.SclFileType;
import org.lfenergy.compas.scl.validator.rest.v1.model.SclValidateResponse;
import org.lfenergy.compas.scl.validator.service.SclValidatorService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class AsyncEventBusHandler {
    private final SclValidatorService sclValidatorService;

    @Inject
    public AsyncEventBusHandler(SclValidatorService sclValidatorService) {
        this.sclValidatorService = sclValidatorService;
    }

    @ConsumeEvent("validate")
    public SclValidateResponse validateEvent(ValidateEventRequest request) {
        var response = new SclValidateResponse();
        response.setValidationErrorList(sclValidatorService.validate(request.getType(), request.getSclData()));
        return response;
    }

    public static class ValidateEventRequest {
        private final SclFileType type;
        private final String sclData;

        public ValidateEventRequest(SclFileType type, String sclData) {
            this.type = type;
            this.sclData = sclData;
        }

        public SclFileType getType() {
            return type;
        }

        public String getSclData() {
            return sclData;
        }
    }
}

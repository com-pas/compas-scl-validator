// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.rest.v1;

import io.quarkus.security.Authenticated;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.eventbus.EventBus;
import io.vertx.mutiny.core.eventbus.Message;
import org.lfenergy.compas.scl.extensions.model.SclFileType;
import org.lfenergy.compas.scl.validator.exception.SclValidatorException;
import org.lfenergy.compas.scl.validator.rest.v1.model.SclValidateRequest;
import org.lfenergy.compas.scl.validator.rest.v1.model.SclValidateResponse;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.time.Duration;

import static org.lfenergy.compas.scl.validator.exception.SclValidatorErrorCode.TIMEOUT_ERROR_CODE;
import static org.lfenergy.compas.scl.validator.rest.SclResourceConstants.TYPE_PATH_PARAM;

@Authenticated
@RequestScoped
@Path("/eventbus/v1/{" + TYPE_PATH_PARAM + "}")
public class AsyncEventBusResource {
    private final EventBus bus;

    @Inject
    public AsyncEventBusResource(EventBus bus) {
        this.bus = bus;
    }

    @POST
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    public Uni<SclValidateResponse> validateSCL(@PathParam(TYPE_PATH_PARAM) SclFileType type,
                                                @Valid SclValidateRequest request) {
        return bus.<SclValidateResponse>request("validate", new AsyncEventBusHandler.ValidateEventRequest(type, request.getSclData()))
                .ifNoItem().after(Duration.ofMinutes(5))
                .failWith(new SclValidatorException(TIMEOUT_ERROR_CODE, "Timeout error validating SCL File."))
                .onItem().transform(Message::body);
    }
}
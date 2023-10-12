// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.rest.v1;

import io.quarkus.security.Authenticated;
import io.smallrye.mutiny.Uni;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lfenergy.compas.scl.extensions.model.SclFileType;
import org.lfenergy.compas.scl.validator.rest.v1.model.SclValidateRequest;
import org.lfenergy.compas.scl.validator.rest.v1.model.SclValidateResponse;
import org.lfenergy.compas.scl.validator.service.SclValidatorService;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import static org.lfenergy.compas.scl.validator.rest.SclResourceConstants.TYPE_PATH_PARAM;

@Authenticated
@RequestScoped
@Path("/validate/v1/{" + TYPE_PATH_PARAM + "}")
public class SclValidatorResource {
    private static final Logger LOGGER = LogManager.getLogger(SclValidatorResource.class);

    private final SclValidatorService sclValidatorService;

    @Inject
    public SclValidatorResource(SclValidatorService sclValidatorService) {
        this.sclValidatorService = sclValidatorService;
    }

    @POST
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    public Uni<SclValidateResponse> validateSCL(@PathParam(TYPE_PATH_PARAM) SclFileType type,
                                                @Valid SclValidateRequest request) {
        LOGGER.info("Validating SCL File for type {}.", type);
        var response = new SclValidateResponse();
        response.setValidationErrorList(sclValidatorService.validate(type, request.getSclData()));
        return Uni.createFrom().item(response);
    }
}

// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.rest.v1;

import io.quarkus.security.Authenticated;
import org.lfenergy.compas.scl.extensions.model.SclFileType;
import org.lfenergy.compas.scl.validator.rest.v1.model.SclValidateRequest;
import org.lfenergy.compas.scl.validator.rest.v1.model.SclValidateResponse;
import org.lfenergy.compas.scl.validator.service.SclValidatorService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import static org.lfenergy.compas.scl.validator.rest.SclResourceConstants.TYPE_PATH_PARAM;

@Authenticated
@RequestScoped
@Path("/validate/v1/{" + TYPE_PATH_PARAM + "}")
public class SclValidatorResource {
    private final SclValidatorService sclValidatorService;

    @Inject
    public SclValidatorResource(SclValidatorService compasCimMappingService) {
        this.sclValidatorService = compasCimMappingService;
    }

    @POST
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    public SclValidateResponse validateSCL(@PathParam(TYPE_PATH_PARAM) SclFileType type,
                                           @Valid SclValidateRequest request) {
        var response = new SclValidateResponse();
        response.setValidationErrorList(sclValidatorService.validate(type, request.getSclData()));
        return response;
    }
}
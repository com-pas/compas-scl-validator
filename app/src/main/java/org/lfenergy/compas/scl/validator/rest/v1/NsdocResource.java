// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.rest.v1;

import io.quarkus.security.Authenticated;
import io.smallrye.mutiny.Uni;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lfenergy.compas.scl.validator.rest.v1.model.NsdocListResponse;
import org.lfenergy.compas.scl.validator.rest.v1.model.NsdocResponse;
import org.lfenergy.compas.scl.validator.service.NsdocService;

import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.UUID;

import static org.lfenergy.compas.scl.validator.rest.SclResourceConstants.ID_PARAM;

@Authenticated
@RequestScoped
@Path("/nsdoc/v1")
public class NsdocResource {
    private static final Logger LOGGER = LogManager.getLogger(NsdocResource.class);

    private final NsdocService nsdocService;

    public NsdocResource(NsdocService nsdocService) {
        this.nsdocService = nsdocService;
    }

    @GET
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    public Uni<NsdocListResponse> list() {
        LOGGER.info("Retrieving list of NSDoc Files");
        var response = new NsdocListResponse();
        response.setNsdocFiles(nsdocService.list());
        return Uni.createFrom().item(response);
    }

    @GET
    @Path("{" + ID_PARAM + "}")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    public Uni<NsdocResponse> get(@PathParam(ID_PARAM) UUID id) {
        LOGGER.info("Retrieving NS Doc File '{}'", id);
        var response = new NsdocResponse();
        response.setNsdocFile(nsdocService.get(id));
        return Uni.createFrom().item(response);
    }
}
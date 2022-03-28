// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.rest.v1;

import io.quarkus.security.Authenticated;
import org.lfenergy.compas.scl.validator.rest.v1.model.NsdocGetRequest;
import org.lfenergy.compas.scl.validator.rest.v1.model.NsdocListResponse;
import org.lfenergy.compas.scl.validator.service.NsdocService;

import javax.enterprise.context.RequestScoped;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Authenticated
@RequestScoped
@Path("/nsdoc/v1")
public class NsdocResource {
    private final NsdocService nsdocService;

    public NsdocResource(NsdocService nsdocService) {
        this.nsdocService = nsdocService;
    }

    @GET
    @Path("list")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    public NsdocListResponse list() {
        NsdocListResponse response = new NsdocListResponse();
        response.setNsdocFiles(nsdocService.list());
        return response;
    }

    @GET
    @Path("get")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    public String get(@Valid NsdocGetRequest request) {
        return nsdocService.get(request.getId());
    }
}
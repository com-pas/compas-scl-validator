// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.rest.v1;

import io.quarkus.security.Authenticated;
import io.vertx.mutiny.core.eventbus.EventBus;
import org.lfenergy.compas.scl.extensions.model.SclFileType;
import org.lfenergy.compas.scl.validator.rest.v1.event.AsyncWebsocketEventRequest;
import org.lfenergy.compas.scl.validator.rest.v1.model.SclValidateRequest;
import org.lfenergy.compas.scl.validator.rest.v1.websocket.AsyncWebsocketRequestDecoder;
import org.lfenergy.compas.scl.validator.rest.v1.websocket.AsyncWebsocketResponseEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

import static org.lfenergy.compas.scl.validator.rest.SclResourceConstants.TYPE_PATH_PARAM;

@Authenticated
@ApplicationScoped
@ServerEndpoint(value = "/validate-ws/v1/{" + TYPE_PATH_PARAM + "}",
        decoders = AsyncWebsocketRequestDecoder.class,
        encoders = AsyncWebsocketResponseEncoder.class)
public class AsyncWebsocketResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncWebsocketResource.class);

    private final EventBus bus;

    @Inject
    public AsyncWebsocketResource(EventBus bus) {
        this.bus = bus;
    }

    @OnError
    public void onError(Session session, @PathParam(TYPE_PATH_PARAM) String type, Throwable throwable) throws IOException {
        LOGGER.info("Error with session {} for type {}.", session.getId(), type, throwable);
        session.close();
    }

    @OnMessage
    public void validateSCL(Session session, SclValidateRequest request, @PathParam(TYPE_PATH_PARAM) String type) {
        LOGGER.info("Message from session {} for type {}.", session.getId(), type);
        bus.send("validate-ws", new AsyncWebsocketEventRequest(
                session, SclFileType.valueOf(type), request.getSclData()));
    }
}
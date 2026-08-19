// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.websocket.v1;

import io.quarkus.security.Authenticated;
import io.vertx.mutiny.core.eventbus.EventBus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lfenergy.compas.core.websocket.ErrorResponseEncoder;
import org.lfenergy.compas.scl.extensions.model.SclFileType;
import org.lfenergy.compas.scl.validator.websocket.event.model.SclValidatorEventRequest;
import org.lfenergy.compas.scl.validator.websocket.v1.decoder.SclValidateWsRequestDecoder;
import org.lfenergy.compas.scl.validator.websocket.v1.encoder.SclValidateWsResponseEncoder;
import org.lfenergy.compas.scl.validator.websocket.v1.model.SclValidateWsRequest;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

import static org.lfenergy.compas.core.websocket.WebsocketSupport.handleException;
import static org.lfenergy.compas.scl.validator.rest.SclResourceConstants.TYPE_PATH_PARAM;

@Authenticated
@ApplicationScoped
@ServerEndpoint(value = "/validate-ws/v1/{" + TYPE_PATH_PARAM + "}",
        decoders = {SclValidateWsRequestDecoder.class},
        encoders = {SclValidateWsResponseEncoder.class, ErrorResponseEncoder.class})
public class SclValidatorServerEndpoint {
    private static final Logger LOGGER = LogManager.getLogger(SclValidatorServerEndpoint.class);

    private final EventBus eventBus;

    @Inject
    public SclValidatorServerEndpoint(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam(TYPE_PATH_PARAM) String type) {
        LOGGER.debug("Starting session {} for type {}.", session.getId(), type);
    }

    @OnMessage
    public void onMessage(Session session,
                          @Valid SclValidateWsRequest request,
                          @PathParam(TYPE_PATH_PARAM) String type) {
        LOGGER.info("Message from session {} for type {}.", session.getId(), type);
        eventBus.send("validate-ws", new SclValidatorEventRequest(
                session, SclFileType.valueOf(type), request.getSclData()));
    }

    @OnError
    public void onError(Session session, @PathParam(TYPE_PATH_PARAM) String type, Throwable throwable) {
        LOGGER.warn("Error with session {} for type {}.", session.getId(), type, throwable);
        handleException(session, throwable);
    }

    @OnClose
    public void onClose(Session session, @PathParam(TYPE_PATH_PARAM) String type) {
        LOGGER.debug("Closing session {} for type {}.", session.getId(), type);
    }
}

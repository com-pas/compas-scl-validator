// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.rest.v1;

import io.quarkus.security.Authenticated;
import io.vertx.mutiny.core.eventbus.EventBus;
import org.lfenergy.compas.scl.extensions.model.SclFileType;
import org.lfenergy.compas.scl.validator.rest.v1.event.SclValidatorEventRequest;
import org.lfenergy.compas.scl.validator.rest.v1.model.SclValidateRequest;
import org.lfenergy.compas.scl.validator.rest.v1.websocket.SclValidateRequestDecoder;
import org.lfenergy.compas.scl.validator.rest.v1.websocket.SclValidateRequestEncoder;
import org.lfenergy.compas.scl.validator.rest.v1.websocket.SclValidateResponseDecoder;
import org.lfenergy.compas.scl.validator.rest.v1.websocket.SclValidateResponseEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

import static org.lfenergy.compas.scl.validator.rest.SclResourceConstants.TYPE_PATH_PARAM;

@Authenticated
@ApplicationScoped
@ServerEndpoint(value = "/compas-scl-validator/validate-ws/v1/{" + TYPE_PATH_PARAM + "}",
        decoders = {SclValidateRequestDecoder.class, SclValidateResponseDecoder.class},
        encoders = {SclValidateRequestEncoder.class, SclValidateResponseEncoder.class})
public class SclValidatorServerEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(SclValidatorServerEndpoint.class);

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
    public void onMessage(Session session, SclValidateRequest request, @PathParam(TYPE_PATH_PARAM) String type) {
        LOGGER.info("Message from session {} for type {}.", session.getId(), type);
        eventBus.send("validate-ws", new SclValidatorEventRequest(
                session, SclFileType.valueOf(type), request.getSclData()));
    }

    @OnError
    public void onError(Session session, @PathParam(TYPE_PATH_PARAM) String type, Throwable throwable) throws IOException {
        LOGGER.warn("Error with session {} for type {}.", session.getId(), type, throwable);
    }

    @OnClose
    public void onClose(Session session, @PathParam(TYPE_PATH_PARAM) String type) {
        LOGGER.debug("Closing session {} for type {}.", session.getId(), type);
    }
}

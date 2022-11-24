// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.websocket.v1.decoder;

import org.lfenergy.compas.core.websocket.AbstractDecoder;
import org.lfenergy.compas.core.websocket.WebsocketSupport;
import org.lfenergy.compas.scl.validator.websocket.v1.model.SclValidateWsRequest;

public class SclValidateWsRequestDecoder extends AbstractDecoder<SclValidateWsRequest> {
    @Override
    public boolean willDecode(String message) {
        return (message != null);
    }

    @Override
    public SclValidateWsRequest decode(String message) {
        return WebsocketSupport.decode(message, SclValidateWsRequest.class);
    }
}

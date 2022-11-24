// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.websocket.v1.encoder;

import org.lfenergy.compas.core.websocket.AbstractEncoder;
import org.lfenergy.compas.core.websocket.WebsocketSupport;
import org.lfenergy.compas.scl.validator.websocket.v1.model.SclValidateWsRequest;

public class SclValidateWsRequestEncoder extends AbstractEncoder<SclValidateWsRequest> {
    @Override
    public String encode(SclValidateWsRequest jaxbObject) {
        return WebsocketSupport.encode(jaxbObject, SclValidateWsRequest.class);
    }
}

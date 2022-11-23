// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.websocket.v1.encoder;

import org.lfenergy.compas.core.websocket.AbstractEncoder;
import org.lfenergy.compas.core.websocket.WebsocketSupport;
import org.lfenergy.compas.scl.validator.websocket.v1.model.SclValidateWsResponse;

public class SclValidateWsResponseEncoder extends AbstractEncoder<SclValidateWsResponse> {
    @Override
    public String encode(SclValidateWsResponse jaxbObject) {
        return WebsocketSupport.encode(jaxbObject, SclValidateWsResponse.class);
    }
}

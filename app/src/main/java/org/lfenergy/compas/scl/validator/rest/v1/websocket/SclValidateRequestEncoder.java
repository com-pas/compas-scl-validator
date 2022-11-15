// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.rest.v1.websocket;

import org.lfenergy.compas.core.websocket.AbstractEncoder;
import org.lfenergy.compas.core.websocket.WebsocketSupport;
import org.lfenergy.compas.scl.validator.rest.v1.model.SclValidateRequest;

public class SclValidateRequestEncoder extends AbstractEncoder<SclValidateRequest> {
    @Override
    public String encode(SclValidateRequest jaxbObject) {
        return WebsocketSupport.encode(jaxbObject, SclValidateRequest.class);
    }
}

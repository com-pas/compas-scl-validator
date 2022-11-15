// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.rest.v1.websocket;

import org.lfenergy.compas.core.websocket.AbstractEncoder;
import org.lfenergy.compas.core.websocket.WebsocketSupport;
import org.lfenergy.compas.scl.validator.rest.v1.model.SclValidateResponse;

public class SclValidateResponseEncoder extends AbstractEncoder<SclValidateResponse> {
    @Override
    public String encode(SclValidateResponse jaxbObject) {
        return WebsocketSupport.encode(jaxbObject, SclValidateResponse.class);
    }
}

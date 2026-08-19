// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.websocket.event.model;

import org.lfenergy.compas.scl.extensions.model.SclFileType;

import jakarta.websocket.Session;

public class SclValidatorEventRequest {
    private Session session;
    private final SclFileType type;
    private final String sclData;

    public SclValidatorEventRequest(Session session, SclFileType type, String sclData) {
        this.session = session;
        this.type = type;
        this.sclData = sclData;
    }

    public Session getSession() {
        return session;
    }

    public SclFileType getType() {
        return type;
    }

    public String getSclData() {
        return sclData;
    }
}

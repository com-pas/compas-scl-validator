// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.rest.v1.websocket;

import org.lfenergy.compas.scl.validator.rest.v1.model.SclValidateResponse;

import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;

public class AsyncWebsocketResponseEncoder implements Encoder.Text<SclValidateResponse> {
    @Override
    public String encode(SclValidateResponse response) {
        StringWriter st = null;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(SclValidateResponse.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            st = new StringWriter();
            marshaller.marshal(response, st);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return st.toString();
    }

    @Override
    public void init(EndpointConfig endpointConfig) {
        // do nothing.
    }

    @Override
    public void destroy() {
        // do nothing.
    }
}
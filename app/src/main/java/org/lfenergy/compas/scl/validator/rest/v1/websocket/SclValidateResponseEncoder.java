// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.rest.v1.websocket;

import org.lfenergy.compas.scl.validator.exception.SclValidatorException;
import org.lfenergy.compas.scl.validator.rest.v1.model.SclValidateResponse;

import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;
import javax.xml.bind.JAXBContext;
import java.io.StringWriter;

import static org.lfenergy.compas.scl.validator.exception.SclValidatorErrorCode.WEBSOCKET_ENCODER_ERROR_CODE;

public class SclValidateResponseEncoder implements Encoder.Text<SclValidateResponse> {
    @Override
    public void init(EndpointConfig endpointConfig) {
        // do nothing.
    }

    @Override
    public String encode(SclValidateResponse response) {
        try {
            var jaxbContext = JAXBContext.newInstance(SclValidateResponse.class);
            var marshaller = jaxbContext.createMarshaller();

            var st = new StringWriter();
            marshaller.marshal(response, st);
            return st.toString();
        } catch (Exception exp) {
            throw new SclValidatorException(WEBSOCKET_ENCODER_ERROR_CODE,
                    "Error marshalling SCL Validate Response from Websockets.",
                    exp);
        }
    }

    @Override
    public void destroy() {
        // do nothing.
    }
}
// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.rest.v1.websocket;

import org.lfenergy.compas.scl.validator.exception.SclValidatorException;
import org.lfenergy.compas.scl.validator.rest.v1.model.SclValidateRequest;

import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;
import javax.xml.bind.JAXBContext;
import java.io.StringWriter;

import static org.lfenergy.compas.scl.validator.exception.SclValidatorErrorCode.WEBSOCKET_ENCODER_ERROR_CODE;

public class SclValidateRequestEncoder implements Encoder.Text<SclValidateRequest> {
    @Override
    public void init(EndpointConfig endpointConfig) {
        // do nothing.
    }

    @Override
    public String encode(SclValidateRequest request) {
        try {
            var jaxbContext = JAXBContext.newInstance(SclValidateRequest.class);
            var marshaller = jaxbContext.createMarshaller();

            var st = new StringWriter();
            marshaller.marshal(request, st);
            return st.toString();
        } catch (Exception exp) {
            throw new SclValidatorException(WEBSOCKET_ENCODER_ERROR_CODE,
                    "Error marshalling SCL Validate Request from Websockets.",
                    exp);
        }
    }

    @Override
    public void destroy() {
        // do nothing.
    }
}

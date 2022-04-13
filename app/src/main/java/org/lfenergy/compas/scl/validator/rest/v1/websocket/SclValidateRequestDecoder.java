// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.rest.v1.websocket;

import org.lfenergy.compas.scl.validator.exception.SclValidatorException;
import org.lfenergy.compas.scl.validator.rest.v1.model.SclValidateRequest;

import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;
import javax.xml.bind.JAXBContext;
import java.io.StringReader;

import static org.lfenergy.compas.scl.validator.exception.SclValidatorErrorCode.WEBSOCKET_DECODER_ERROR_CODE;

public class SclValidateRequestDecoder implements Decoder.Text<SclValidateRequest> {
    @Override
    public void init(EndpointConfig endpointConfig) {
        // do nothing.
    }

    @Override
    public boolean willDecode(String message) {
        return (message != null);
    }

    @Override
    public SclValidateRequest decode(String message) {
        try {
            var jaxbContext = JAXBContext.newInstance(SclValidateRequest.class);
            var unmarshaller = jaxbContext.createUnmarshaller();
            var reader = new StringReader(message);
            return (SclValidateRequest) unmarshaller.unmarshal(reader);
        } catch (Exception exp) {
            throw new SclValidatorException(WEBSOCKET_DECODER_ERROR_CODE,
                    "Error unmarshalling SCL Validate Request from Websockets.",
                    exp);
        }
    }

    @Override
    public void destroy() {
        // do nothing.
    }
}
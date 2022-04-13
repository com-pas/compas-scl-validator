package org.lfenergy.compas.scl.validator.rest.v1.websocket;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lfenergy.compas.scl.validator.exception.SclValidatorException;
import org.lfenergy.compas.scl.validator.rest.v1.model.SclValidateRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.lfenergy.compas.scl.validator.SclValidatorConstants.SCL_VALIDATOR_SERVICE_V1_NS_URI;
import static org.lfenergy.compas.scl.validator.exception.SclValidatorErrorCode.WEBSOCKET_ENCODER_ERROR_CODE;

class SclValidateRequestEncoderTest {
    private SclValidateRequestEncoder encoder;

    @BeforeEach
    void init() {
        encoder = new SclValidateRequestEncoder();
        encoder.init(null);
    }

    @Test
    void encode_WhenCalledWithRequest_ThenRequestConvertedToString() {
        var sclData = "Some SCL Data";
        var request = new SclValidateRequest();
        request.setSclData(sclData);

        var result = encoder.encode(request);

        var expectedResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
                + "<svs:SclValidateRequest xmlns:svs=\"" + SCL_VALIDATOR_SERVICE_V1_NS_URI + "\">"
                + "<svs:SclData>" + sclData + "</svs:SclData>"
                + "</svs:SclValidateRequest>";
        assertNotNull(result);
        assertEquals(expectedResult, result);
    }

    @Test
    void encode_WhenCalledWithNull_ThenExceptionThrown() {
        var exception = assertThrows(SclValidatorException.class, () -> encoder.encode(null));
        assertEquals(WEBSOCKET_ENCODER_ERROR_CODE, exception.getErrorCode());
        assertEquals(IllegalArgumentException.class, exception.getCause().getClass());
    }

    @AfterEach
    void destroy() {
        encoder.destroy();
    }
}
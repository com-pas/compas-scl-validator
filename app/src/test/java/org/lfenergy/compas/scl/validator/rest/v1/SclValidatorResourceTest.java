// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.rest.v1;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.lfenergy.compas.scl.extensions.model.SclFileType;
import org.lfenergy.compas.scl.validator.model.ValidationError;
import org.lfenergy.compas.scl.validator.rest.v1.model.SclValidateRequest;
import org.lfenergy.compas.scl.validator.service.SclValidatorService;
import org.lfenergy.compas.scl.validator.util.TestSupportUtil;

import java.io.IOException;
import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.path.xml.config.XmlPathConfig.xmlPathConfig;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.lfenergy.compas.scl.validator.SclValidatorConstants.SCL_VALIDATOR_SERVICE_V1_NS_URI;
import static org.lfenergy.compas.scl.validator.rest.SclResourceConstants.TYPE_PATH_PARAM;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@QuarkusTest
@TestHTTPEndpoint(SclValidatorResource.class)
@TestSecurity(user = "test-user")
class SclValidatorResourceTest {
    @InjectMock
    private SclValidatorService sclValidatorService;

    @Test
    void validate_WhenCalled_ThenExpectedResponseIsRetrieved() throws IOException {
        var sclFileTye = SclFileType.CID;
        var request = new SclValidateRequest();
        request.setSclData(TestSupportUtil.readSCL("scl-1.scd"));

        when(sclValidatorService.validate(sclFileTye, request.getSclData()))
                .thenReturn(List.of(new ValidationError()));

        var response = given()
                .pathParam(TYPE_PATH_PARAM, sclFileTye)
                .contentType(ContentType.XML)
                .body(request)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .response();

        var xmlPath = response.xmlPath()
                .using(xmlPathConfig().declaredNamespace("svs", SCL_VALIDATOR_SERVICE_V1_NS_URI));
        var errors = xmlPath.getList("svs:SclValidateResponse.svs:ValidationErrors");
        assertNotNull(errors);
        assertEquals(1, errors.size());
        verify(sclValidatorService).validate(sclFileTye, request.getSclData());
    }
}

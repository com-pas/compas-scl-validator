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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.path.xml.config.XmlPathConfig.xmlPathConfig;
import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.lfenergy.compas.scl.validator.SclValidatorConstants.SCL_VALIDATOR_SERVICE_V1_NS_URI;
import static org.lfenergy.compas.scl.validator.rest.SclResourceConstants.TYPE_PATH_PARAM;
import static org.mockito.Mockito.*;

@QuarkusTest
@TestHTTPEndpoint(SclValidatorResource.class)
@TestSecurity(user = "test-user")
class SclValidatorResourceTest {
    @InjectMock
    private SclValidatorService sclValidatorService;

    @Test
    void updateSCL_WhenCalled_ThenExpectedResponseIsRetrieved() throws IOException {
        var sclFileTye = SclFileType.CID;
        var request = new SclValidateRequest();
        request.setSclData(readFile());

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
                .using(xmlPathConfig().declaredNamespace("saa", SCL_VALIDATOR_SERVICE_V1_NS_URI));
        var errors = xmlPath.getList("saa:SclValidateResponse.ValidationErrors");
        assertNotNull(errors);
        assertEquals(1, errors.size());
        verify(sclValidatorService, times(1)).validate(sclFileTye, request.getSclData());
    }

    private String readFile() throws IOException {
        var resource = requireNonNull(getClass().getResource("/scl/scl-1.scd"));
        var path = Paths.get(resource.getPath());
        return String.join("\n", Files.readAllLines(path));
    }
}
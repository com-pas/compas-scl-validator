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
import org.lfenergy.compas.scl.validator.exception.NsdocFileNotFoundException;
import org.lfenergy.compas.scl.validator.exception.SclValidatorErrorCode;
import org.lfenergy.compas.scl.validator.model.NsdocFile;
import org.lfenergy.compas.scl.validator.service.NsdocService;

import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static io.restassured.path.xml.config.XmlPathConfig.xmlPathConfig;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.lfenergy.compas.core.commons.CommonConstants.COMPAS_COMMONS_V1_NS_URI;
import static org.lfenergy.compas.scl.validator.SclValidatorConstants.SCL_VALIDATOR_SERVICE_V1_NS_URI;
import static org.lfenergy.compas.scl.validator.rest.SclResourceConstants.ID_PARAM;
import static org.mockito.Mockito.*;

@QuarkusTest
@TestHTTPEndpoint(NsdocResource.class)
@TestSecurity(user = "test-user")
class NsdocResourceTest {
    @InjectMock
    private NsdocService nsdocService;

    @Test
    void list_WhenCalled_ThenListReturned() {
        when(nsdocService.list())
                .thenReturn(List.of(new NsdocFile()));

        var response = given()
                .contentType(ContentType.XML)
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .response();

        var xmlPath = response.xmlPath()
                .using(xmlPathConfig().declaredNamespace("svs", SCL_VALIDATOR_SERVICE_V1_NS_URI));
        var files = xmlPath.getList("svs:NsdocListResponse.svs:NsdocFile");
        assertNotNull(files);
        assertEquals(1, files.size());
        verify(nsdocService, times(1)).list();
    }

    @Test
    void get_WhenCalled_ThenContentReturned() {
        UUID id = UUID.randomUUID();
        String result = "<some><xml></xml></some>";

        when(nsdocService.get(id))
                .thenReturn(result);

        var response = given()
                .pathParam(ID_PARAM, id)
                .contentType(ContentType.XML)
                .when()
                .get("/{" + ID_PARAM + "}")
                .then()
                .statusCode(200)
                .extract()
                .response();

        var xmlPath = response.xmlPath()
                .using(xmlPathConfig().declaredNamespace("svs", SCL_VALIDATOR_SERVICE_V1_NS_URI));
        var nsdocFile = xmlPath.getString("svs:NsdocResponse.svs:NsdocFile");
        assertNotNull(nsdocFile);
        assertEquals(result, nsdocFile);
        verify(nsdocService, times(1)).get(id);
    }

    @Test
    void get_WhenCalledWithUnknownFile_Then404Returned() {
        UUID id = UUID.randomUUID();

        when(nsdocService.get(id))
                .thenThrow(new NsdocFileNotFoundException("Some Message"));

        var response = given()
                .pathParam(ID_PARAM, id)
                .contentType(ContentType.XML)
                .when()
                .get("/{" + ID_PARAM + "}")
                .then()
                .statusCode(404)
                .extract()
                .response();

        var xmlPath = response.xmlPath()
                .using(xmlPathConfig().declaredNamespace("commons", COMPAS_COMMONS_V1_NS_URI));
        var messages = xmlPath.getList("commons:ErrorResponse.commons:ErrorMessage");
        assertNotNull(messages);
        assertEquals(1, messages.size());

        var code = xmlPath.getString("commons:ErrorResponse.commons:ErrorMessage.commons:Code");
        assertEquals(SclValidatorErrorCode.NSDOC_FILE_NOT_FOUND, code);

        verify(nsdocService, times(1)).get(id);
    }
}

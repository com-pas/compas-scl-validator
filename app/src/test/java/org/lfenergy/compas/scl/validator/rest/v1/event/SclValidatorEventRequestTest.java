// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.rest.v1.event;

import com.openpojo.reflection.impl.PojoClassFactory;
import com.openpojo.validation.ValidatorBuilder;
import com.openpojo.validation.rule.impl.GetterMustExistRule;
import com.openpojo.validation.test.impl.GetterTester;
import org.junit.jupiter.api.Test;
import org.lfenergy.compas.scl.extensions.model.SclFileType;
import org.mockito.Mockito;

import javax.websocket.Session;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class SclValidatorEventRequestTest {
    @Test
    void constructor_WhenCalledWith2Arguments_ThenValuesSet() {
        var type = SclFileType.CID;
        var sclData = "Some SCL Data";

        var result = new SclValidatorEventRequest(type, sclData);

        assertNull(result.getSession());
        assertEquals(type, result.getType());
        assertEquals(sclData, result.getSclData());
    }

    @Test
    void constructor_WhenCalledWith3Arguments_ThenValuesSet() {
        var session = Mockito.mock(Session.class);
        var type = SclFileType.CID;
        var sclData = "Some SCL Data";

        var result = new SclValidatorEventRequest(session, type, sclData);

        assertEquals(session, result.getSession());
        assertEquals(type, result.getType());
        assertEquals(sclData, result.getSclData());
    }

    @Test
    void validateSettersAndGetters() {
        var personPojo = PojoClassFactory.getPojoClass(SclValidatorEventRequest.class);
        var validator = ValidatorBuilder.create()
                // Lets make sure that we have a getter for every field defined.
                .with(new GetterMustExistRule())
                // Lets also validate that they are behaving as expected
                .with(new GetterTester())
                .build();

        // Start the Test
        validator.validate(personPojo);
    }
}

// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.xsd;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SclXsdValidatorTest {

    private SclXsdValidator validator;

    @BeforeEach
    public void setup() {
        this.validator = new SclXsdValidator();
    }

    @Test
    void validate_WhenCalled_ThenExpectedValidationErrorsReturned() throws IOException {
        var scdFile = new File(getClass()
                .getResource("/scl/validation/example-with-xsd-validation-errors.scd").getFile());

        var path = scdFile.toPath();
        var sclData = Files.readString(path);

        var result = validator.validate(sclData);

        assertNotNull(result);
        assertEquals(4, result.size());
    }
}

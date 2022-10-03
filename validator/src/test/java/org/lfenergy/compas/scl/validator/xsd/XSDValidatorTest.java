// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.xsd;

import org.junit.jupiter.api.Test;
import org.lfenergy.compas.scl.validator.exception.SclValidatorException;
import org.lfenergy.compas.scl.validator.model.ValidationError;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.lfenergy.compas.scl.validator.exception.SclValidatorErrorCode.LOADING_XSD_FILE_ERROR_CODE;

class XSDValidatorTest {
    @Test
    void validate_WhenCalledWithSclDataWithoutXsdValidationErrors_ThenNoErrorsAreRetrieved() throws IOException {
        var errorList = new ArrayList<ValidationError>();
        try (var inputStream = getClass()
                .getResourceAsStream("/scl/validation/example-without-xsd-validation-errors.scd")) {
            var data = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            new XSDValidator(errorList, data).validate();

            assertEquals(0, errorList.size());
        }
    }

    @Test
    void validate_WhenCalledWithSclDataWithXsdValidationErrors_ThenErrorsAreRetrieved() throws IOException {
        var errorList = new ArrayList<ValidationError>();
        try (var inputStream = getClass()
                .getResourceAsStream("/scl/validation/example-with-xsd-validation-errors.scd")) {
            var data = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            new XSDValidator(errorList, data).validate();

            assertEquals(4, errorList.size());

            var error = errorList.get(2);
            assertEquals("Attribute 'name' must appear on element 'BDA'.", error.getMessage());
            assertEquals("XSD/cvc-complex-type.4", error.getRuleName());
            assertEquals(66, error.getLineNumber());
            assertEquals(45, error.getColumnNumber());
        }
    }

    @Test
    void validate_WhenCalledWithSclDataWithCompasXsdValidationErrors_ThenErrorsAreRetrieved() throws IOException {
        var errorList = new ArrayList<ValidationError>();
        try (var inputStream = getClass()
                .getResourceAsStream("/scl/validation/example-with-compas-validation-errors.scd")) {
            var data = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            new XSDValidator(errorList, data).validate();

            assertEquals(4, errorList.size());

            var error = errorList.get(0);
            assertEquals("Value 'INVALID' is not facet-valid with respect to enumeration '[SSD, IID, ICD, SCD, CID, " +
                    "SED, ISD, STD]'. It must be a value from the enumeration.", error.getMessage());
            assertEquals("XSD/cvc-enumeration-valid", error.getRuleName());
            assertEquals(10, error.getLineNumber());
            assertEquals(57, error.getColumnNumber());

            error = errorList.get(2);
            assertEquals("Value 'Invalid Label' is not facet-valid with respect to pattern '[A-Za-z][0-9A-Za-z_-]*' " +
                    "for type 'tCompasLabel'.", error.getMessage());
            assertEquals("XSD/cvc-pattern-valid", error.getRuleName());
            assertEquals(12, error.getLineNumber());
            assertEquals(55, error.getColumnNumber());
        }
    }

    @Test
    void validate_WhenCalledWithSclDataContainingInvalidVersion_ThenExceptionIsThrown() throws IOException {
        var errorList = new ArrayList<ValidationError>();
        try (var inputStream = getClass()
                .getResourceAsStream("/scl/validation/example-with-wrong-version.scd")) {
            var data = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

            var exception = assertThrows(SclValidatorException.class, () -> new XSDValidator(errorList, data));
            assertEquals(LOADING_XSD_FILE_ERROR_CODE, exception.getErrorCode());
        }
    }

    @Test
    void validate_WhenCalledWithSclDataContainingMissingVersion_ThenExceptionIsThrown() throws IOException {
        var errorList = new ArrayList<ValidationError>();
        try (var inputStream = getClass()
                .getResourceAsStream("/scl/validation/example-with-missing-version.scd")) {
            var data = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

            var exception = assertThrows(SclValidatorException.class, () -> new XSDValidator(errorList, data));
            assertEquals(LOADING_XSD_FILE_ERROR_CODE, exception.getErrorCode());
        }
    }
}

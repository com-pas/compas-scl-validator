// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator;

import org.lfenergy.compas.scl.extensions.model.SclFileType;

public interface SclValidator {
    String validate(SclFileType type, String sclData);
}

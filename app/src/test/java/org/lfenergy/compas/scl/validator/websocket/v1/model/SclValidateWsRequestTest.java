// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.websocket.v1.model;

import org.lfenergy.compas.scl.validator.model.AbstractPojoTester;

class SclValidateWsRequestTest extends AbstractPojoTester {
    @Override
    protected Class<?> getClassToBeTested() {
        return SclValidateWsRequest.class;
    }
}

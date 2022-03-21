// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.util;

import java.io.IOException;

public final class TestSupportUtil {
    TestSupportUtil() {
        throw new UnsupportedOperationException("FileTestUtil class");
    }

    public static String readSCL(String filename) throws IOException {
        var inputStream = TestSupportUtil.class.getResourceAsStream("/scl/" + filename);
        assert inputStream != null;

        return new String(inputStream.readAllBytes());
    }
}

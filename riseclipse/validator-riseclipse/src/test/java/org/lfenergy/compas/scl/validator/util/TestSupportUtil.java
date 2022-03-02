// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.util;

import fr.centralesupelec.edf.riseclipse.iec61850.scl.SclPackage;
import org.eclipse.emf.ecore.impl.EPackageRegistryImpl;
import org.eclipse.ocl.pivot.utilities.OCL;

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

    public static OCL createSclOcl() {
        // Create an EPackage.Registry for the SclPackage.
        var registry = new EPackageRegistryImpl();
        registry.put(SclPackage.eNS_URI, SclPackage.eINSTANCE);
        // Create an OCL that creates a ResourceSet using the minimal EPackage.Registry
        return OCL.newInstance(registry);
    }
}

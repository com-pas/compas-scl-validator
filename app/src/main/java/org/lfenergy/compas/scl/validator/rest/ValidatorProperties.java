// SPDX-FileCopyrightText: 2021 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.rest;

import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithName;

import java.nio.file.Path;

@ConfigMapping(prefix = "compas.validator")
public interface ValidatorProperties {
    @WithName("ocl.custom.directory")
    String oclCustomDirectory();

    @WithName("nsdoc.directory")
    String nsdocDirectory();

    @WithName("temp.directory")
    Path tempDirectory();
}

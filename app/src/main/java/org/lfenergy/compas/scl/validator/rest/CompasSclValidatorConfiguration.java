// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.rest;

import org.lfenergy.compas.core.commons.ElementConverter;
import org.lfenergy.compas.scl.validator.collector.CompasOclFileCollector;
import org.lfenergy.compas.scl.validator.collector.OclFileCollector;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

/**
 * Create Beans from other dependencies that are used in the application.
 */
public class CompasSclValidatorConfiguration {
    @Produces
    @ApplicationScoped
    public ElementConverter createElementConverter() {
        return new ElementConverter();
    }

    @Produces
    @ApplicationScoped
    public OclFileCollector createOclFileCollector(ValidatorProperties properties) {
        return new CompasOclFileCollector(properties.oclCustomDirectory());
    }
}

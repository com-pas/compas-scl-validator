// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.rest;

import io.quarkus.runtime.Startup;
import org.lfenergy.compas.core.commons.ElementConverter;
import org.lfenergy.compas.scl.validator.xsd.SclXsdValidator;
import org.lfenergy.compas.scl.validator.collector.CompasOclFileCollector;
import org.lfenergy.compas.scl.validator.collector.OclFileCollector;
import org.lfenergy.compas.scl.validator.common.NsdocFinder;
import org.lfenergy.compas.scl.validator.impl.SclRiseClipseValidator;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

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

    @Produces
    @ApplicationScoped
    public SclRiseClipseValidator createSclRiseClipseValidator(OclFileCollector oclFileCollector,
                                                               ValidatorProperties properties) {
        return new SclRiseClipseValidator(oclFileCollector, properties.tempDirectory());
    }

    @Produces
    @ApplicationScoped
    public SclXsdValidator createSclXsdValidator() {
        return new SclXsdValidator();
    }

    @Startup
    @ApplicationScoped
    public NsdocFinder createNsdocFinder(ValidatorProperties properties) {
        return new NsdocFinder(properties.nsdocDirectory());
    }
}

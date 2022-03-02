// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.rest;

import io.quarkus.runtime.annotations.RegisterForReflection;
import org.lfenergy.compas.core.commons.ElementConverter;
import org.lfenergy.compas.scl.validator.collector.CompasOclFileCollector;
import org.lfenergy.compas.scl.validator.collector.OclFileCollector;
import org.lfenergy.compas.scl.validator.impl.SclRiseClipseValidator;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

/**
 * Create Beans from other dependencies that are used in the application.
 */
@RegisterForReflection(targets = {
        org.lfenergy.compas.core.jaxrs.model.ErrorResponse.class,
        org.lfenergy.compas.core.jaxrs.model.ErrorMessage.class,
        org.eclipse.ocl.pivot.internal.resource.OCLASResourceFactory.class,
        org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl.class,
        org.eclipse.xtext.resource.IResourceFactory.class,
        org.eclipse.xtext.resource.XtextResourceFactory.class,
        org.eclipse.xtext.resource.XtextResource.class,
        java.lang.Boolean.class, java.lang.Byte.class,
        java.lang.Double.class, java.lang.Float.class,
        java.lang.Integer.class, java.lang.Long.class,
        java.lang.Short.class}, ignoreNested = false)
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
}

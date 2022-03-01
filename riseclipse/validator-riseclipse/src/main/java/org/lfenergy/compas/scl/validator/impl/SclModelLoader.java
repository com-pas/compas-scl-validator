// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.impl;

import fr.centralesupelec.edf.riseclipse.iec61850.scl.util.SclResourceFactoryImpl;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.ocl.pivot.utilities.OCL;
import org.lfenergy.compas.scl.validator.exception.SclValidatorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.UUID;

public class SclModelLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(SclModelLoader.class);

    private final ResourceSet resourceSet;
    private final HashMap<String, Boolean> options;

    public SclModelLoader(OCL ocl) {
        this.resourceSet = ocl.getResourceSet();

        // Register the appropriate resource factory to handle all file  extensions.
        this.resourceSet.getResourceFactoryRegistry()
                .getExtensionToFactoryMap()
                .put(Resource.Factory.Registry.DEFAULT_EXTENSION, new SclResourceFactoryImpl());

        this.options = new HashMap<>();
        this.options.put(XMLResource.OPTION_DEFER_IDREF_RESOLUTION, true);
    }

    public Resource load(String sclData) {
        LOGGER.debug("Loading SCL Data in RiseClipse.");
        try {
            UUID uuid = UUID.randomUUID();
            Resource resource = resourceSet.createResource(URI.createURI(uuid.toString()));
            resource.load(new ByteArrayInputStream(sclData.getBytes(StandardCharsets.UTF_8)), options);
            return resource;
        } catch (Exception exp) {
            throw new SclValidatorException("", "Problem loading SCL Data", exp);
        }
    }
}

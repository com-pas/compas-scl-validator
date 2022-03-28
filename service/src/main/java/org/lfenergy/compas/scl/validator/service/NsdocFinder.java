// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.service;

import org.apache.commons.codec.digest.DigestUtils;
import org.lfenergy.compas.scl.validator.exception.NsdocFileNotFoundException;
import org.lfenergy.compas.scl.validator.exception.SclValidatorException;
import org.lfenergy.compas.scl.validator.model.NsdocFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.lfenergy.compas.scl.validator.exception.SclValidatorErrorCode.*;

public class NsdocFinder {
    private static final Logger LOGGER = LoggerFactory.getLogger(NsdocFinder.class);

    private static final String NSDOC_ELEMENT_NAME = "NSDoc";

    private final String nsdocDirectory;
    private final Map<String, NsdocFile> nsdocFiles;

    public NsdocFinder(String nsdocDirectory) {
        this.nsdocDirectory = nsdocDirectory;
        this.nsdocFiles = getFilesFromDirectory(nsdocDirectory);
    }

    private Map<String, NsdocFile> getFilesFromDirectory(String directoryName) {
        File directory = new File(directoryName);
        if (directory.exists() && directory.isDirectory()) {
            var files = directory.listFiles();
            if (files != null) {
                return Arrays.stream(files)
                        .map(this::convertToNsdocFile)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toMap(NsdocFile::getId, Function.identity()));
            }
        }
        return Collections.emptyMap();
    }

    private NsdocFile convertToNsdocFile(File file) {
        try {
            var nsdocFile = new NsdocFile();
            nsdocFile.setId(getId(file));
            nsdocFile.setFilename(file.getName());
            nsdocFile.setChecksum(calculateChecksum(file));
            return nsdocFile;
        } catch (Exception exp) {
            LOGGER.warn("Error loading NSDoc File '" + file.getName() + "'. Skipping file.", exp);
            return null;
        }
    }

    private String getId(File file) {
        try (var fis = new FileInputStream(file)) {
            XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
            XMLEventReader reader = xmlInputFactory.createXMLEventReader(fis);

            String id = null;
            while (id == null && reader.hasNext()) {
                XMLEvent nextEvent = reader.nextEvent();
                if (nextEvent.isStartElement()) {
                    StartElement startElement = nextEvent.asStartElement();
                    if (NSDOC_ELEMENT_NAME.equals(startElement.getName().getLocalPart())) {
                        Attribute attribute = startElement.getAttributeByName(new QName("id"));
                        if (attribute != null) {
                            id = attribute.getValue();
                        }
                    }
                }
            }
            if (id == null) {
                throw new SclValidatorException(DETERMINING_ID_FAILED, "No ID found in NSDoc File '" + file.getName() + "'.");
            }
            return id;
        } catch (IOException | XMLStreamException exp) {
            throw new SclValidatorException(DETERMINING_ID_FAILED, "Error loading NSDoc File " + file.getName() + "'.", exp);
        }
    }

    private String calculateChecksum(File file) {
        try (var fis = new FileInputStream(file)) {
            return DigestUtils.sha256Hex(fis);
        } catch (IOException exp) {
            throw new SclValidatorException(CALCULATING_CHECKSUM_FAILED, "Error calculating checksum for NSDoc File " +
                    file.getName() + "'.", exp);
        }
    }

    public Collection<NsdocFile> getNsdocFiles() {
        return nsdocFiles.values()
                .stream()
                .sorted(Comparator.comparing(NsdocFile::getId))
                .collect(Collectors.toList());
    }

    public String getNsdocFile(String id) {
        try {
            if (nsdocFiles.containsKey(id)) {
                var nsdocFile = nsdocFiles.get(id);
                return Files.readString(new File(this.nsdocDirectory, nsdocFile.getFilename()).toPath());
            }
            throw new NsdocFileNotFoundException("NSDoc File with ID '" + id + "' not found.");
        } catch (IOException exp) {
            throw new SclValidatorException(LOADING_NSDOC_FILE_FAILED, "Error loading NSDoc File with ID '" + id + "'", exp);
        }
    }
}

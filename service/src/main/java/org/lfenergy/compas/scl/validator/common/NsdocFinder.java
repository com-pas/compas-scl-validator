// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.common;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lfenergy.compas.scl.validator.exception.NsdocFileNotFoundException;
import org.lfenergy.compas.scl.validator.exception.SclValidatorException;
import org.lfenergy.compas.scl.validator.model.NsdocFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.lfenergy.compas.scl.validator.exception.SclValidatorErrorCode.CALCULATING_CHECKSUM_FAILED;
import static org.lfenergy.compas.scl.validator.exception.SclValidatorErrorCode.LOADING_NSDOC_FILE_FAILED;

public class NsdocFinder {
    private static final Logger LOGGER = LogManager.getLogger(NsdocFinder.class);

    private final String nsdocDirectory;
    private final Map<UUID, NsdocFile> nsdocFiles;

    public NsdocFinder(String nsdocDirectory) {
        this.nsdocDirectory = nsdocDirectory;
        this.nsdocFiles = getFilesFromDirectory(nsdocDirectory);
    }

    private Map<UUID, NsdocFile> getFilesFromDirectory(String directoryName) {
        File directory = new File(directoryName);
        if (directory.exists() && directory.isDirectory()) {
            var files = directory.listFiles();
            return Arrays.stream(files)
                    .filter(File::isFile)
                    .filter(file -> file.getName().endsWith(".nsdoc"))
                    .map(this::convertToNsdocFile)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toMap(NsdocFile::getId, Function.identity()));
        }
        return Collections.emptyMap();
    }

    private NsdocFile convertToNsdocFile(File file) {
        try {
            var nsdocInfo = new NsdocInfo(file);
            var nsdocFile = new NsdocFile();
            nsdocFile.setId(UUID.randomUUID());
            nsdocFile.setNsdocId(nsdocInfo.getId());
            nsdocFile.setFilename(file.getName());
            nsdocFile.setChecksum(calculateChecksum(file));
            return nsdocFile;
        } catch (Exception exp) {
            LOGGER.warn("Error loading NSDoc File '{}'. Skipping file.", file.getName(), exp);
            return null;
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
                .sorted(Comparator.comparing(NsdocFile::getNsdocId))
                .toList();
    }

    public String getNsdocFile(UUID id) {
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

// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.service;

import org.lfenergy.compas.scl.validator.common.NsdocFinder;
import org.lfenergy.compas.scl.validator.model.NsdocFile;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Collection;

@ApplicationScoped
public class NsdocService {
    private final NsdocFinder nsdocFinder;

    @Inject
    public NsdocService(NsdocFinder nsdocFinder) {
        this.nsdocFinder = nsdocFinder;
    }

    public Collection<NsdocFile> list() {
        return nsdocFinder.getNsdocFiles();
    }

    public String get(String id) {
        return nsdocFinder.getNsdocFile(id);
    }
}

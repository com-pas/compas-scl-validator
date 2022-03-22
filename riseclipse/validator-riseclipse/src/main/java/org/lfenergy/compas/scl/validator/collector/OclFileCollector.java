// SPDX-FileCopyrightText: 2022 Alliander N.V.
//
// SPDX-License-Identifier: Apache-2.0
package org.lfenergy.compas.scl.validator.collector;

import org.eclipse.emf.common.util.URI;

import java.util.List;

/**
 * Interface used to collect OCL Files for use in the SCL Validator.
 */
public interface OclFileCollector {
    List<URI> getOclFiles();
}

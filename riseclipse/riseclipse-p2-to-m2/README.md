<!--
SPDX-FileCopyrightText: 2022 Alliander N.V.

SPDX-License-Identifier: Apache-2.0
-->

This project is used to create a maven repository from Eclipse Dependencies. This is needed for building the executable
jar to run in a Docker Container.

The project is a copy of the p2_to_m2 project
from [RiseClipse Developer](https://github.com/riseclipse/riseclipse-developer) of the
directory `fr.centralesupelec.edf.riseclipse.developer.p2_to_m2`.

The file `src/main/resources/riseclipse.aggr` is also a copy of the file from RiseClipse and can be found
[here](https://github.com/riseclipse/riseclipse-developer/blob/master/fr.centralesupelec.edf.riseclipse.developer.p2_to_m2/src/main/resources/riseclipse.aggr)
. This file needs to be updated manually.

**Remark RiseClipse**: The `src/main/resources/riseclipse.aggr` file should be edited with
the [CBI/aggregator](https://wiki.eclipse.org/CBI/aggregator) tool.

**Remark CoMPAS**: Because we only need this small part of the RiseClipse Developer GIT Repository and mainly 2 files
(pom.xml (partially) and riseclipse.aggr) it didn't seem usefully to make a Git Submodule from it.
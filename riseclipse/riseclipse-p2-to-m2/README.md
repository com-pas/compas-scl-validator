Copyright (c) 2021 CentraleSupélec & EDF.

This program is free software: 

>  you can redistribute it and/or modify it under the terms of the GNU Lesser 
  General Public License as published by the Free Software Foundation, either 
  version 3 of the License, or (at your option) any later version. This program 
  is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
  without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR 
  PURPOSE. See the GNU General Lesser Public License for more details. You 
  should have received a copy of the GNU General Lesser Public License along 
  with this program. If not, see <http://www.gnu.org/licenses/lgpl-3.0.html>.

This file is part of the RiseClipse tool.
 
### Contributors:
  * Computer Science Department, CentraleSupélec
  * EDF R&D
### Contacts:
  * dominique.marcadet@centralesupelec.fr
  * aurelie.dehouck-neveu@edf.fr
### Web site:
  * <https://riseclipse.github.io>

****

This project is used to create maven artifacts from Eclipse plugins. This is needed 
for building fat jars which run outside Eclipse.


This project is based on work by German Vega [(see this message)](https://www.eclipse.org/forums/index.php?t=msg&th=1097672&goto=1826425&#msg_1826425).

The following is an extract of the `pom.xml` file present in the archive :
>This file is part of VASCO Model Transformation - Platform Eclipse Modeling Framework 4.15 Maven repository

>Please visit http://vasco.imag.fr for further information

>Authors : German Vega , Yves ledru , Akram Idani 

>> Laboratoire d'Informatique de Grenoble, Team VASCO

> Copyright (C) 2016 - 2020 University of Grenoble Alpes

>  This program is free software: 

>  	you can redistribute it and/or modify it under the terms of the GNU Lesser 
  	General Public License as published by the Free Software Foundation, either 
  	version 3 of the License, or (at your option) any later version. This program 
  	is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
  	without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR 
  	PURPOSE. See the GNU General Lesser Public License for more details. You 
  	should have received a copy of the GNU General Lesser Public License along 
  	with this program. If not, see <http://www.gnu.org/licenses/lgpl-3.0.html>.

****

The `src/main/resources/riseclipse.aggr` file should be edited with the 
[CBI/aggregator](https://wiki.eclipse.org/CBI/aggregator) tool.

Changes in this file should usually also be done in the target platform 
(project `fr.centralesupelec.edf.riseclipse.developer.eclipse`).

<!--
SPDX-FileCopyrightText: 2022 Alliander N.V.

SPDX-License-Identifier: Apache-2.0
-->

# Development

## Git

If the project is already cloned and a submodule is added use the following commands, first `git submodule init` and
next `git submodule update`.

More about Git Submodules can be found [here](https://git-scm.com/book/en/v2/Git-Tools-Submodules).

Tip: The URL to the submodules are configured in the file `.gitmodules`, but these are using the SSH URL. There is a way
described in the URL above that the URL can be overwritten locally with an HTTPS URL of the GIT Repository.

```
Since the URL in the .gitmodules file is what other people will first try to clone/fetch from, make sure to use a URL 
that they can access if possible. For example, if you use a different URL to push to than others would to pull from, 
use the one that others have access to. You can overwrite this value locally with git config 
submodule."riseclipse/riseclipse-developer".url HTTPS_URL for your own use. When applicable, a relative URL can be 
helpful.
```

## IntelliJ

Importing the project is a bit harder for the SCL Validator then normal. It's caused because of the submodules that are
needed from RiseClipse. These projects are Eclipse projects using Eclipse Tycho to build and Eclipse project structure.

A way to make everything work in IntelliJ is importing the project in the following way.

- First step is to just import everything like it are Maven projects;
- Next step is to re-import the RiseClipse Submodule as Eclipse;
    - In IntelliJ select "File" -> "New" -> "Module from Existing Sources...";
    - Select one of the RiseClipse Submodules, for instance "riseclipse-metamodel-scl2003";
    - Next select "Eclipse" by "Import module from External Model";
    - Follow the rest of the wizard, only to remember to select all subprojects that are available in the directory;

Now the module should be correctly imported in IntelliJ to be used. Check the Module Settings of one of the subprojects
to check if the directory "src" is a Java Source Directory, for instance the module
"riseclipse/riseclipse-metamodel-scl2003/fr.centralesupelec.edf.riseclipse.iec61850.scl.utilities".

## Eclipse

Example about how to use Eclipse OCL was found
[here](https://help.eclipse.org/latest/index.jsp?topic=%2Forg.eclipse.ocl.doc%2Fhelp%2FPivotStandalone.html).

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell script
./mvnw package io.quarkus:quarkus-maven-plugin::dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.

## Packaging and running the application

The application can be packaged using:

```shell script
./mvnw package
```

It produces the `quarkus-run.jar` file in the `app/target/quarkus-app/` directory. Be aware that it’s not an _über-jar_
as the dependencies are copied into the `app/target/quarkus-app/lib/` directory.

If you want to build an _über-jar_, execute the following command:

```shell script
./mvnw package -Dquarkus.package.type=uber-jar
```

The application is now runnable using `java -jar app/target/quarkus-app/quarkus-run.jar`.


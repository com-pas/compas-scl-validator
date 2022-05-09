<!--
SPDX-FileCopyrightText: 2022 Alliander N.V.

SPDX-License-Identifier: Apache-2.0
-->

# Development

## Git

If the project is already cloned and a submodule is added use the following commands to retrieve the files from the
RiseClipse Repository:

- `git submodule init`
- `git submodule update`

More about Git Submodules can be found [here](https://git-scm.com/book/en/v2/Git-Tools-Submodules).

**Remark**: The URLs to the submodules are configured in the file `.gitmodules`, but these are using the SSH URLs. There
is a way described [here](https://git-scm.com/book/en/v2/Git-Tools-Submodules) that the URL can be overwritten locally
with an HTTPS URL of the GIT Repository.

Because of the subdirectory where the submodules are in, this doesn't work exactly that way. Use the following commands
to update the URLs locally to HTTPS.

```
git config submodule.riseclipse/riseclipse-ocl-constraints-scl2003.url https://github.com/riseclipse/riseclipse-ocl-constraints-scl2003.git

git submodule init
git submodule update
```

### Git update Submodules

A submodule in Git is fixed to a specific commit of the remote repository. To update the reference to the latest commit
in the remote repository execute the following command from the root directory of the project.

```
git submodule update --remote
```

Next commit and push the changes to the Git Repository of CoMPAS. The build now uses the latest commit of the main
branch.

## Eclipse

### Eclipse Dependencies

To make everything build and run Eclipse Dependencies are needed, but unlike the RiseClipse Dependencies these can't be
found in the Maven Central Repository, but in an Eclipse P2 Repository. In RiseClipse there is a p2-to-m2 project that
creates a local Maven Repository from the needed dependencies from the Eclipse P2 Repository. A CoMPAS Version of this
maven module is created here, see [README.md](riseclipse/riseclipse-p2-to-m2/README.md) for more info.

### Eclipse OCL

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


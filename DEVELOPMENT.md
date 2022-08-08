<!--
SPDX-FileCopyrightText: 2022 Alliander N.V.

SPDX-License-Identifier: Apache-2.0
-->

# Development for CoMPAS SCL Validator


---
**Note**
Mac M1 chip users, see [below](#mac-m1)

---


Since version 1.2.x the JAR Files of RiseClipse are distributed through Maven Central Repository. The JAR Files can be
retrieved from there and don't need to be build locally anymore. Only the OCL Files for the SCL validation still need to
be downloaded from RiseClipse GIT Repository. This is still done using Git Submodules.

To clone the project or update the project this means that the Git commands are sometimes a little different. To clone
the project use the following command `git clone --recurse-submodules git@github.com:com-pas/compas-scl-validator.git`.
This will also clone the submodules.

Tip: The URL to the submodules are configured in the file `.gitmodules`, but these are using the SSH URL. There is a way
described [here](https://git-scm.com/book/en/v2/Git-Tools-Submodules) to overwrite the URL locally with an HTTPS URL of
the GIT Repository.

Check the [Development](DEVELOPMENT.md) page for more detail information how to develop in this GIT repository.

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

## Building the application

You can use Maven to build the application and see if all tests are working using:

```shell script
./mvnw clean verify
```

This should normally be enough to also run the application, but there were cases that we need to build using:

```shell script
./mvnw clean install
```

This to make the local modules available for the app module to run the application.

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell script
./mvnw package io.quarkus:quarkus-maven-plugin::dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.

### Application depends on a running KeyCloak instance for dev mode

There is a KeyCloak instance need to be running on port 8089 by default in dev mode.
See [Security](README.md#security) for default values, if custom keycloak is used.

There is a preconfigured keycloak instance available in
the [CoMPAS Deployment Repository](https://github.com/com-pas/compas-deployment). This repository can be cloned and
when going to this directory the following command can be executed to create a local Docker Image with configuration.

```shell
cd <CoMPAS Deployment Repository Directory>/compas/keycloak
docker build -t compas_keycloak . 
```

There is now a Docker Image `compas_keycloak` created that can be started using the following command

```shell
docker run --rm --name compas_keycloak \
   -p 8089:8080 
   -d compas_keycloak:latest
```

## Testing the application

The application is tested with unit and integration tests, but you can also manually test the application using for
instance Postman. And there is also a way to test this service with the CoMPAS OpenSCD Frontend application.

### Postman

To manually test the application there is a Postman collection in the directory `postman` that can be imported
and used to execute REST XML Calls.

To make the call work we also need to import an environment and authorisation collection. These files can be found
in [CoMPAS Deployment Repository](https://github.com/com-pas/compas-deployment) in the directory `postman`
(`auth.collection.json` and `local.environment.json`).

In the authorisation collection there are called for the 3 users known within the Demo KeyCloak instance.
If one of these calls are executed there is a variable `bearer` filled.

Now one of the SCL Auto Alignment calls can be executed, the variable `bearer` is added to the header of the request.
After the call is executed the result should be shown in Postman.

### CoMPAS OpenSCD Frontend application

To test the SCL Validator with the CoMPAS OpenSCD application just run the application in dev mode, including the
KeyCloak instance. For further instruction how to start the CoMPAS OpenSCD application and use this locally see
the file `DEVELOPMENT.md` in [CoMPAS OpenSCD application](https://github.com/com-pas/compas-open-scd).

## Docker Images

### Creating a Docker image with native executable

The CoMPAS SCL Validator currently isn't build as Native executable, GraalVM has a lot of problems to strip the
Eclipse libraries and make them work as Native executable.

### Creating a Docker image with JVM executable

The release action is creating a Docker Image which runs the application using a JVM. You can create a Docker Image
with JVM executable using:

```shell script
./mvnw package -Pjvm-image
```

## Mac M1

The Mac M1 chip is ARM based. In order to make this project work, you need to install the x86 version of your IDE.
You also need Rosetta2 to virtualize your os. In the x86 version of your IDE, you also need to install the x86 Java version.
You can use SDK-Man to easily switch between java versions.

While calling maven scripts, you need to add the os.arch flag.
```shell script
./mvnw clean compile -Dos.arch=x86_64
```

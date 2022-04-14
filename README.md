<!--
SPDX-FileCopyrightText: 2022 Alliander N.V.

SPDX-License-Identifier: Apache-2.0
-->

[![Maven Build Github Action Status](<https://img.shields.io/github/workflow/status/com-pas/compas-scl-validator/Maven%20Build?logo=GitHub>)](https://github.com/com-pas/compas-scl-validator/actions?query=workflow%3A%22Maven+Build%22)
[![REUSE status](https://api.reuse.software/badge/github.com/com-pas/compas-scl-validator)](https://api.reuse.software/info/github.com/com-pas/compas-scl-validator)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=com-pas_compas-scl-validator&metric=alert_status)](https://sonarcloud.io/dashboard?id=com-pas_compas-scl-validator)
[![LFX Security Status](https://img.shields.io/badge/dynamic/json?color=orange&label=LFX%20Security%20Tool&query=issues%5B%3F%28%40%5B%27repository-name%27%5D%20%3D%3D%20%27compas-scl-validator%27%29%5D%5B%27high-open-issues%27%5D&suffix=%20High%20open%20issues&url=https%3A%2F%2Fapi.security.lfx.linuxfoundation.org%2Fv1%2Fproject%2Fe8b6fdf9-2686-44c5-bbaa-6965d04ad3e1%2Fissues)](https://security.lfx.linuxfoundation.org/#/e8b6fdf9-2686-44c5-bbaa-6965d04ad3e1/issues)
[![Slack](https://raw.githubusercontent.com/com-pas/compas-architecture/master/public/LFEnergy-slack.svg)](http://lfenergy.slack.com/)

# compas-scl-validator

Service to validate SCL Files.

## Rest and Websockets versions

The validation can be done using both Rest and Websockets as transportation technic.

| Method    | URL                                                                   |
|-----------|-----------------------------------------------------------------------|
| Rest      | http(s)://**server-address**/compas-scl-validator/validate/v1/{type}  |
| Websocket | ws(s)://**server-address**/compas-scl-validator/validate-ws/v1/{type} |

In CoMPAS OpenSCD there is a switch in the setting to indicate if websockets needs to be used. The logic will
automatically determine the URL to be used.

## Development

For the RiseClipse implementation of the validator parts of the RiseClipse project are being used. Currently, these
parts aren't distributed to any Maven Repository, so the Git Repositories need to be included. This is done using Git
Submodules.

To clone the project or update the project this means that the Git commands are sometimes a little different. To clone
the project use the following command `git clone --recurse-submodules git@github.com:com-pas/compas-scl-validator.git`.
This will also clone the submodules.

Tip: The URL to the submodules are configured in the file `.gitmodules`, but these are using the SSH URL. There is a way
described in the URL above that the URL can be overwritten locally with an HTTPS URL of the GIT Repository.

Check the [Development](DEVELOPMENT.md) page for more detail information how to work with this repository, because of
the mixture with RiseClipse.

## Custom OCL Files

There is a way to add custom OCL Files to the validator, for instance to force company specific rules. In the Docker
Image there is a volume `/data/ocl` which can be used to add these files, see our compas-deployment project for an
example how to.

In this directory, you can use subdirectories like `SemanticConstraints` as RiseClipse is doing. And there is a special
filter that when you create a directory `FileSpecifics`. In this directory you can create for instance a directory `CID`
to put constraints specific for an SCL File Type. Known types are `SSD`, `IID`, `ICD`, `SCD`, `CID`, `SED`, `ISD`,
`STD`.

For instance,

```
data
└── ocl
    ├── FileSpecifics
    │         └── CID
    │             └── Busbar.ocl
    └── SemanticConstraints
        └── Busbar.ocl
```

If you are using the validator are library (using JAR Files) there is a property to configure the directory, see
[Common Environment variables](#common-environment-variables)

## NSDoc Files

Because NSDoc File can't be distributed in an OpenSource Project these need to be added during deployment. In the Docker
Image there is a volume `/data/nsdoc` which can be used to add these files, see our compas-deployment project for an
example how to.

Only direct files found in this directory will be processed. Invalid NSDoc Files will be ignored. A directory can look
like this for instance,

```
data
└── nsdoc
    ├── IEC_61850-7-2_2007B3-en.nsdoc
    ├── IEC_61850-7-3_2007B3-en.nsdoc
    └── IEC_61850-7-4_2007B3-en.nsdoc
    
```

If you are using the validator are library (using JAR Files) there is a property to configure the directory, see
[Common Environment variables](#common-environment-variables)

## Common Environment variables

Below environment variable(s) can be used to configure the validator.

| Environment variable                  | Java Property                         | Description                                                 | Example     |
|---------------------------------------|---------------------------------------|-------------------------------------------------------------|-------------|
| COMPAS_VALIDATOR_OCL_CUSTOM_DIRECTORY | compas.validator.ocl.custom.directory | Reference to a directory to load custom OCL Files           | /data/ocl   |
| COMPAS_VALIDATOR_NSDOC_DIRECTORY      | compas.validator.nsdoc.directory      | Reference to a directory where the NSDoc Files can be found | /data/nsdoc | 

## Security

To use most of the endpoints the users needs to be authenticated using JWT in the authorization header. There are 4
environment variables that can be set in the container to configure the validation/processing of the JWT.

| Environment variable | Java Property                    | Description                                        | Example                                                                |
|----------------------|----------------------------------|----------------------------------------------------|------------------------------------------------------------------------|
| JWT_VERIFY_KEY       | smallrye.jwt.verify.key.location | Location of certificates to verify the JWT.        | http://localhost:8089/auth/realms/compas/protocol/openid-connect/certs |
| JWT_VERIFY_ISSUER    | mp.jwt.verify.issuer             | The issuer of the JWT.                             | http://localhost:8089/auth/realms/compas                               |
| JWT_VERIFY_CLIENT_ID | mp.jwt.verify.audiences          | The Client ID that should be in the "aud" claim.   | scl-validator                                                          |
| JWT_GROUPS_PATH      | smallrye.jwt.path.groups         | The JSON Path where to find the roles of the user. | resource_access/scl-validator/roles                                    |

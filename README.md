<!--
SPDX-FileCopyrightText: 2022 Alliander N.V.

SPDX-License-Identifier: Apache-2.0
-->

[![Maven Build Github Action Status](<https://img.shields.io/github/workflow/status/com-pas/compas-scl-validator/Build%20Project?logo=GitHub>)](https://github.com/com-pas/compas-scl-validator/actions?query=workflow%3A%22Build+Project%22)
[![REUSE status](https://api.reuse.software/badge/github.com/com-pas/compas-scl-validator)](https://api.reuse.software/info/github.com/com-pas/compas-scl-validator)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=com-pas_compas-scl-validator&metric=alert_status)](https://sonarcloud.io/dashboard?id=com-pas_compas-scl-validator)
[![CII Best Practices](https://bestpractices.coreinfrastructure.org/projects/5925/badge)](https://bestpractices.coreinfrastructure.org/projects/5925)
[![Slack](https://raw.githubusercontent.com/com-pas/compas-architecture/master/public/LFEnergy-slack.svg)](http://lfenergy.slack.com/)

# CoMPAS SCL Validator

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

Information about how to run and develop for this project check [Development](DEVELOPMENT.md).

## Custom OCL Files

There is a way to add custom OCL Files to the validator, for instance to force company specific rules. In the Docker
Image there is a volume `/data/ocl` which can be used to add these files, see our compas-deployment project for an
example how to.

In this directory, you can use subdirectories like `SemanticConstraints` as RiseClipse is doing. And there is a special
directory `FileSpecifics`. In this directory mostly file specific rules are added. There are functions that can be used
in the OCL Rules to determine the type of file, like `isInICDFile()`.

For instance,

```
data
└── ocl
    ├── FileSpecifics
    │   └── DOType.ocl
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

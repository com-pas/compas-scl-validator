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

## Common Environment variables

Below environment variable(s) can be used to configure the validator.

| Environment variable                  | Java Property                         | Description                                       | Example   |
|---------------------------------------|---------------------------------------|---------------------------------------------------|-----------|
| COMPAS_VALIDATOR_OCL_CUSTOM_DIRECTORY | compas.validator.ocl.custom.directory | Reference to a directory to load custom OCL Files | /data/ocl |

## Security

To use most of the endpoints the users needs to be authenticated using JWT in the authorization header. There are 4
environment variables that can be set in the container to configure the validation/processing of the JWT.

| Environment variable | Java Property                    | Description                                        | Example                                                                |
|----------------------|----------------------------------|----------------------------------------------------|------------------------------------------------------------------------|
| JWT_VERIFY_KEY       | smallrye.jwt.verify.key.location | Location of certificates to verify the JWT.        | http://localhost:8089/auth/realms/compas/protocol/openid-connect/certs |
| JWT_VERIFY_ISSUER    | mp.jwt.verify.issuer             | The issuer of the JWT.                             | http://localhost:8089/auth/realms/compas                               |
| JWT_VERIFY_CLIENT_ID | mp.jwt.verify.audiences          | The Client ID that should be in the "aud" claim.   | scl-validator                                                          |
| JWT_GROUPS_PATH      | smallrye.jwt.path.groups         | The JSON Path where to find the roles of the user. | resource_access/scl-validator/roles                                    |

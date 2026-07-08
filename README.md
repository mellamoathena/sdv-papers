# sdv-papers

[![Java CI with Maven](https://github.com/mellamoathena/sdv-papers/actions/workflows/maven.yml/badge.svg)](https://github.com/mellamoathena/sdv-papers/actions/workflows/maven.yml)
[![Coverage Status](https://coveralls.io/repos/github/mellamoathena/sdv-papers/badge.svg?branch=main)](https://coveralls.io/github/mellamoathena/sdv-papers?branch=main)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=mellamoathena_sdv-papers&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=mellamoathena_sdv-papers)

An Automated Software Testing exam project: a Java/Maven/MongoDB application with a Swing GUI, managing Authors and Papers with a many-to-many relationship, developed with TDD and a full test pyramid (unit, integration, end-to-end).

## Build

Requires Java 8 and Docker (for the integration and end-to-end tests, which use Testcontainers).

```
./mvnw verify
```
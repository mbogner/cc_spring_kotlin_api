# {{cookiecutter.project_name}}

API implemented with Spring Boot based on OpenAPI.

## Release

For example this prepares a release of 1.0.0 and a development version of 1.0.1-SNAPSHOT.
Do NOT run it with the sample versions. This needs to be updated on every run.

```shell
./gradlew release -Prelease.useAutomaticVersion=true -Prelease.releaseVersion=1.0.0 -Prelease.newVersion=1.0.1-SNAPSHOT
```
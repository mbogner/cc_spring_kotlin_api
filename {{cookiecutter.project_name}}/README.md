# {{cookiecutter.project_name}}

{{cookiecutter.description}}

## Release

All releases have to be done from main branch.

The following example releases version 1.0.0 and prepares a development version of 1.0.1-SNAPSHOT.

```shell
./gradlew release -Prelease.useAutomaticVersion=true \
  -Prelease.releaseVersion=1.0.0 \
  -Prelease.newVersion=1.0.1-SNAPSHOT
```

You can skip the version definitions to release the snapshot and automatically increment.

```shell
./gradlew release -Prelease.useAutomaticVersion=true
```

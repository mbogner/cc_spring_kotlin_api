#!/usr/bin/env bash
BASENAME=$(basename "$PWD")
cookiecutter --no-input --config-file test.yml "$PWD/$BASENAME.zip" || exit 1

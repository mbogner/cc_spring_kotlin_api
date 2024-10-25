#!/usr/bin/env bash
cookiecutter --no-input --config-file test.yml $PWD/$(basename $PWD).zip

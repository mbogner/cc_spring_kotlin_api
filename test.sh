#!/usr/bin/env bash
rm -rf test &&
./cookie_package.sh &&
./cookie_gen.sh &&
idea test
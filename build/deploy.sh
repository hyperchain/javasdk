#!/usr/bin/env bash

# make sure settings.xml has oss config and gpg key is ready local
cd .. && mvn deploy -Dmaven.test.skip=true -P release
#!/bin/bash
set -e
set -x
# Need to build the maven plugin first
mvn clean install -pl :postroom-typed-code-generator
mvn clean verify

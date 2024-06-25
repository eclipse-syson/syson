#!/bin/bash

set -x
set -e
# version : 0.1.0

SCRIPT=$(realpath "$0")
SCRIPTPATH=$(dirname "$SCRIPT")

# Generate HTML / PDF
echo "Antora build"


docker build ${SCRIPTPATH} -t syson_doc_generator:local

docker run -v $SCRIPTPATH/..:/usr/app:z --security-opt label=disable syson_doc_generator:local

#!/bin/sh
docker kill syson-postgres
docker run -p 5433:5432 --name syson-postgres -e POSTGRES_USER=dbuser -e POSTGRES_PASSWORD=dbpwd -e POSTGRES_DB=syson-db -d postgres
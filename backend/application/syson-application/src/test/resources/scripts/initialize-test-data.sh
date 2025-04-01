#!/bin/sh

export PGPASSWORD=dbpwd

isTestContainerRunning=$(docker ps -q -f "status=running" -f "name=syson-test-postgres" | wc -l)

if [[ $isTestContainerRunning -ne 1 ]]; then
    echo "Container syson-test-postgres is not running, please start it and run this script again"
    exit 1
fi

echo "Cleaning syson-db & importing ./syson-test-database.sql"
# Remove winpty if you are not calling the script from git bash
psql -U dbuser -d syson-db --host "localhost" --port "5433" -f ./cleanup.sql -f ./syson-test-database.sql
echo "Done"

exit 0
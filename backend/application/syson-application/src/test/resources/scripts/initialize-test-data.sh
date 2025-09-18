#!/bin/sh

export PGPASSWORD=dbpwd

isTestContainerRunning=$(docker ps -q -f "status=running" -f "name=syson-test-postgres" | wc -l)

if [ -z "$1" ]; then
    echo "Error: missing first parameter holding the name of the targeted file to load data from (*.sql)."
    exit 1
fi


if [[ $isTestContainerRunning -ne 1 ]]; then
    echo "Container syson-test-postgres is not running, please start it and run this script again"
    exit 1
fi

echo "Cleaning syson-db & importing ./${1}"

# Use the following command if you are not calling the script from git bash
psql -U dbuser -d syson-db --host "localhost" --port "5433" -f ./cleanup.sql -f ./${1}

# Use the following command if you are calling the script from git bash
#winpty psql -U dbuser -d syson-db --host "localhost" --port "5433" -f ./cleanup.sql -f ./${1}

echo "Done"

exit 0
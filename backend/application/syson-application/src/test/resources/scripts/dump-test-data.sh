#!/bin/sh

export PGPASSWORD=dbpwd

isTestContainerRunning=$(docker ps -q -f "status=running" -f "name=syson-test-postgres" | wc -l)

if [[ $isTestContainerRunning -ne 1 ]]; then
    echo "Container syson-test-postgres is not running, please start it and run this script again"
    exit 1
fi

echo "Extracting test data from syson-db"
# Remove winpty if you are not calling the script from git bash
winpty pg_dump --host localhost --port 5433 --file "./syson-test-database.sql" --username dbuser --format=p --encoding UTF8 --data-only --inserts --table public.document --table public.project --table public.representation_metadata --table public.representation_content --table public.semantic_data --table public.semantic_data_domain --table public.image --table public.nature --table public.project_image syson-db
echo "Done"
echo "Cleaning test data"
sed -i "/^SELECT pg_catalog.set_config('search_path', '', false);/d" ./syson-test-database.sql
echo "Done"

exit 0
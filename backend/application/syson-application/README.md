# SysON Application

## Developer testing environment

SysON contains tests that can be executed out of the box. The instructions below detail how to configure your development environment to contribute/modify tests and their baseline models.

### Prerequisites

The scripts used in the instructions below rely on the `pg_dump` and `psql` executables. We recommand to install [pgAdmin](https://www.pgadmin.org/download/) to get them as well as additional tools to manipulate Postgres databases.

The scripts below have been tested with Git Bash on Windows. They rely on `winpty` to communicate with Windows console programs. Please adapt the scripts if you are using a different shell/operating system.

### Setup the testing environment

Integration tests are initialized with SQL script file. Each script contains the data required for one or more test.
It generally contains only one project.
Those files contain models and diagrams that are used as baselines to run the tests on.

The steps below show how to generate this file:

1. Create a new docker container named `syson-test-postgres`

```bash
docker run -p 5433:5432 --name syson-test-postgres \
                             -e POSTGRES_USER=dbuser \
                             -e POSTGRES_PASSWORD=dbpwd \
                             -e POSTGRES_DB=syson-db \
                             -d postgres:12
```

> [!NOTE]
> It is good practice to have a dedicated container to manage the test models. The database is regularly cleaned and updated in the development process, and it shouldn't contain sensible information.

2. Start SysON backend & frontend

This creates the database tables required by the tests. The SysON frontend can be used later to update the test models.

3. Run the initialization script

```bash
cd src/test/resources/scripts
./initialize-test-data.sh <$SQLScriptName>
```

4. Navigate to `localhost:5173` and update your test models using SysON
5. Run the export script

```bash
cd src/test/resources/scripts
./dump-test-data.sh <$SQLScriptName>
```

The produced SQL file will replace the existing one.

> [!TIP]
> Follow from step 3 if you already have a `syson-test-postgres` container.

### Good practices

When creating a new project, creates the associated XXXTestProjectData Java class.
Follow the pattern used in the existing files to stores required information such as:

* Editing Context identifier
* Graphical Element identifiers
* Semantic identifiers
* Name of the initialization script


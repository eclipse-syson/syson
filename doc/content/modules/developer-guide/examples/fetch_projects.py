# These examples are adapted from the SysML v2 API Cookbook, available at
# https://github.com/Systems-Modeling/SysML-v2-API-Cookbook, maintained by the
# Object Management Group (OMG).
# The original cookbook is designed for use with Jupyter Lab.
# These examples have been adapted to run as standalone Python scripts, making
# them suitable for use in various environments, including SysON.
# They showcase practical usage scenarios and may include additional functionality
# or modifications tailored to specific needs.

import requests # <1>
from init_api import init_sysmlv2_api

def fetch_projects(host): # <2>
    projects_url = f"{host}/projects" # <3>
    response = requests.get(projects_url) # <4>
    if response.status_code == 200: # <5>
        projects = response.json()
        for project in projects:
            print(f"Project Name: {project['name']}, ID: {project['@id']}")
    else:
        print(f"Error fetching projects: {response.status_code} - {response.text}")

if __name__ == "__main__":
    host = init_sysmlv2_api()
    # Get the projects
    fetch_projects(host)
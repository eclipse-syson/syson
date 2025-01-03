# These examples are adapted from the SysML v2 API Cookbook, available at
# https://github.com/Systems-Modeling/SysML-v2-API-Cookbook, maintained by the
# Object Management Group (OMG).
# The original cookbook is designed for use with Jupyter Lab.
# These examples have been adapted to run as standalone Python scripts, making
# them suitable for use in various environments, including SysON.
# They showcase practical usage scenarios and may include additional functionality
# or modifications tailored to specific needs.

import requests # <1>
from init_api import parse_arguments
from init_api import init_sysmlv2_api
from fetch_commits import get_last_commit_id

def fetch_elements(host, project_id):  # <2>
    commit_id = get_last_commit_id(host, project_id)
    # API endpoint to fetch elements of the commit
    element_get_url = f"{host}/projects/{project_id}/commits/{commit_id}/elements" # <3>
    # Send GET request to retrieve elements
    response = requests.get(element_get_url)  # <4>
    if response.status_code == 200:  # <5>
        elements = response.json()
        for element in elements:
            print(f"Element ID: {element['@id']}, Name: {element['name']}")
        return elements
    else:
        print(f"Error fetching elements: {response.status_code} - {response.text}")
        return None

if __name__ == "__main__":
    args = parse_arguments()
    host = init_sysmlv2_api()
    project_id = args.project_id
    fetch_elements(host, project_id)
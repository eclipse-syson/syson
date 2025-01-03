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

def fetch_tags(host, project_id):  # <2>
    tags_url = f"{host}/projects/{project_id}/tags"  # <3>
    response = requests.get(tags_url)  # <4>
    if response.status_code == 200:  # <5>
        tags = response.json()
        for tag in tags:
            print(f"Tag ID: {tag['@id']}")
    else:
        print(f"Error fetching tags: {response.status_code} - {response.text}")

if __name__ == "__main__":
    args = parse_arguments()
    host = init_sysmlv2_api()
    project_id = args.project_id
    fetch_tags(host, project_id)

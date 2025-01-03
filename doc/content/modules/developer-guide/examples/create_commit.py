# These examples are adapted from the SysML v2 API Cookbook, available at
# https://github.com/Systems-Modeling/SysML-v2-API-Cookbook, maintained by the
# Object Management Group (OMG).
# The original cookbook is designed for use with Jupyter Lab.
# These examples have been adapted to run as standalone Python scripts, making
# them suitable for use in various environments, including SysON.
# They showcase practical usage scenarios and may include additional functionality
# or modifications tailored to specific needs.

import requests # <1>
import json
from init_api import parse_arguments
from init_api import init_sysmlv2_api
from fetch_elements import fetch_elements

def create_commit(host, project_id, commit_body): # <2>
    # API endpoint to create a commit
    commit_post_url = f"{host}/projects/{project_id}/commits" # <3>

    # Send POST request to create a commit
    response = requests.post(  # <4>
        commit_post_url,
        headers={"Content-Type": "application/json"},
        data=json.dumps(commit_body)
    )

    commit_id = ""

    # Check if the commit creation was successful
    if response.status_code == 200: # <5>
        response_json = response.json()
        commit_id = response_json.get('@id', "")
        return commit_id
    else:
        print(f"Problem creating a new commit in project ID: {project_id}")
        print(response)
        return None

def create_elements(host, project_id):
    # Create 1st Commit on the main (default) branch that creates 2 elements
    commit_body = {
        "@type": "Commit",
        "change": [
            {
                "@type": "DataVersion",
                "payload": {
                    "@type": "PartDefinition",
                    "name": "Spacecraft System"
                }
            },
            {
                "@type": "DataVersion",
                "payload": {
                    "@type": "PartDefinition",
                    "name": "Payload System"
                }
            }
        ]
    }

    commit_id = create_commit(host, project_id, commit_body)

    # Get elements
    response = fetch_elements(host, project_id)
    if response.status_code == 200:
        elements = response.json()

        # Filter and retrieve IDs of specific elements
        payload_system_element = next((e for e in elements if e['name'] == "Payload System"), None)
        payload_system_element_id = payload_system_element['@id'] if payload_system_element else "Not Found"

        spacecraft_system_element = next((e for e in elements if e['name'] == "Spacecraft System"), None)
        spacecraft_system_element_id = spacecraft_system_element['@id'] if spacecraft_system_element else "Not Found"

        # Display the fetched elements
        print({"Payload System ID": payload_system_element_id, "Spacecraft System ID": spacecraft_system_element_id})
    else:
        print(f"Problem fetching elements for project {project_id} at commit {commit1_id}.")
        print(response)
    return commit_id

if __name__ == "__main__":
    args = parse_arguments()
    host = init_sysmlv2_api()
    project_id = args.project_id

    # Get elements
    response = fetch_elements(host, project_id)

    # Create elements
    commit_id = create_elements(host, project_id)

    # Get elements
    response = fetch_elements(host, project_id)
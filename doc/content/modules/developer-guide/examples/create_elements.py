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

def create_elements(host, project_id): # <2>
    # Define the commit body to create two elements: Spacecraft System and Payload System
    commit_body = { # <3>
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

    # API endpoint to create a commit
    commit_post_url = f"{host}/projects/{project_id}/commits"

    # Send POST request to create a commit
    commit_post_response = requests.post(  # <4>
        commit_post_url,
        headers={"Content-Type": "application/json"},
        data=json.dumps(commit_body)
    )

    commit1_id = ""

    # Check if the commit creation was successful
    if commit_post_response.status_code == 200: # <5>
        commit_response_json = commit_post_response.json()
        print(commit_response_json)
        commit1_id = commit_response_json.get('@id', "")
    else:
        print(f"Problem creating a new commit in Spacecraft project {project_id}")
        print(commit_post_response)
        return

    # API endpoint to fetch elements of the commit
    element_get_url = f"{host}/projects/{project_id}/commits/{commit1_id}/elements"

    # Send GET request to retrieve elements
    element_get_response = requests.get(element_get_url)

    if element_get_response.status_code == 200:
        elements = element_get_response.json()

        # Filter and retrieve IDs of specific elements
        payload_system_element = next((e for e in elements if e['name'] == "Payload System"), None)
        payload_system_element_id = payload_system_element['@id'] if payload_system_element else "Not Found"

        spacecraft_system_element = next((e for e in elements if e['name'] == "Spacecraft System"), None)
        spacecraft_system_element_id = spacecraft_system_element['@id'] if spacecraft_system_element else "Not Found"

        # Display the fetched elements
        print({"Payload System ID": payload_system_element_id, "Spacecraft System ID": spacecraft_system_element_id})
    else:
        print(f"Problem fetching elements for project {project_id} at commit {commit1_id}.")
        print(element_get_response)

if __name__ == "__main__":
    host = init_sysmlv2_api()
    project_id = "<your-project-id>"  # Replace with your project ID
    create_elements(host, project_id)

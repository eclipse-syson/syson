# These examples are adapted from the SysML v2 API Cookbook, available at
# https://github.com/Systems-Modeling/SysML-v2-API-Cookbook, maintained by the
# Object Management Group (OMG).
# The original cookbook is designed for use with Jupyter Lab.
# These examples have been adapted to run as standalone Python scripts, making
# them suitable for use in various environments, including SysON.
# They showcase practical usage scenarios and may include additional functionality
# or modifications tailored to specific needs.

from init_api import init_sysmlv2_api # <1>
from fetch_elements import fetch_elements
from fetch_commits import get_last_commit_id
from create_commit import create_commit

def update_element(host, project_id, element_id, element_new_name):
    commit_id = get_last_commit_id(host, project_id)
    commit_body = { # <3>
        "@type": "Commit",
        "change": [
            {
                "@type": "DataVersion",
                "payload": {
                    "@type": "PartDefinition",
                    "name":element_new_name,  # <2>
                    "identifier":element_id
            },
                "identity": {
                    "@id": element_id
                }
            }
        ],
        "previousCommit": {
            "@id": commit_id
        }
    }

    commit_id = create_commit(host, project_id, commit_body) # <4>

if __name__ == "__main__":
    host = init_sysmlv2_api()
    project_id = "<your-project-id>"  # Replace with your project ID
    element_id ="<your-element-id>"  # Replace with your element ID

    # Get elements
    response = fetch_elements(host, project_id)

    # Update an existing element
    update_element(host, project_id, element_id, "Updated element")

    # Get elements
    response = fetch_elements(host, project_id)
# These examples are adapted from the SysML v2 API Cookbook, available at
# https://github.com/Systems-Modeling/SysML-v2-API-Cookbook, maintained by the
# Object Management Group (OMG).
# The original cookbook is designed for use with Jupyter Lab.
# These examples have been adapted to run as standalone Python scripts, making
# them suitable for use in various environments, including SysON.
# They showcase practical usage scenarios and may include additional functionality
# or modifications tailored to specific needs.

from init_api import parse_arguments # <1>
from init_api import init_sysmlv2_api
from fetch_elements import fetch_elements
from create_commit import create_commit

def delete_element(host, project_id, commit_id, element_id):
    commit_body = { # <3>
        "@type": "Commit",
        "change": [
            {
                "@type": "DataVersion",
                "payload": None,  # <2>
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
    args = parse_arguments()
    host = init_sysmlv2_api()
    project_id = args.project_id
    element_id = args.element_id

    # Get elements
    response = fetch_elements(host, project_id)

    # Delete an element
    delete_element(host, project_id, element_id)

    # Get elements
    response = fetch_elements(host, project_id)
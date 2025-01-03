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
from fetch_branches  import fetch_branches
from fetch_commits import get_last_commit_id
import json
from datetime import datetime

def create_branch(host, project_id, branch_name): # <2>
    commit_id = get_last_commit_id(host, project_id)

    # Define the body to create a branch
    body = {
        "@type": "Branch",
        "name": branch_name,
        "head": {
            "@id": commit_id
        }
    }

    # API endpoint to create a commit
    url = f"{host}/projects/{project_id}/branches" # <3>

    # Send POST request to create a branch
    response = requests.post(  # <4>
        url,
        headers={"Content-Type": "application/json"},
        data=json.dumps(body)
    )

    branch_id = ""

    # Check if the branch creation was successful
    if response.status_code == 200: # <5>
        response_json = response.json()
        branch_id = response_json.get('@id', "Unknown ID")
        print("Branch created successfully:")
        print(f"Branch ID: {branch_id}")
    else:
        print(f"Problem creating a new branch in project {project_id}")
        print(response)
        return

if __name__ == "__main__":
    args = parse_arguments()
    host = init_sysmlv2_api()
    project_id = args.project_id

    # Fetch and display the list of branches currently available for a project
    print("Fetching the list of branches currently available on a project:")
    fetch_branches(host, project_id)

    # Create a new branch with a unique name by appending a timestamp
    timestamp = datetime.now().strftime("%Y-%m-%d %H:%M:%S")
    branch_name = f"New Branch - {timestamp}"
    create_branch(host, project_id, branch_name)

    # Fetch and display the updated list of branches after creating the new branch
    print("Fetching the updated list of branches after creating a new branch:")
    fetch_branches(host, project_id)
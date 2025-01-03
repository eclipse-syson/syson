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
from fetch_projects import fetch_projects
from datetime import datetime

def create_project(host, project_name): # <2>
    # Define project data as query parameters
    project_params = {
        "name": project_name
    }

    # API endpoint to create a project
    url = f"{host}/projects"  # <3>

    # Send POST request with query parameters
    response = requests.post(url, params=project_params)  # <4>

    # Check if the project creation was successful
    if response.status_code == 201:  # <5>
        response_json = response.json()
        print("Project created successfully:")
        print(f"Project ID: {response_json.get("@id", "Unknown ID")}")
        print(f"Project Name: {response_json.get('name', 'Unknown Name')}")
    else:
        print(f"Error creating project: {response.status_code} - {response.text}")

if __name__ == "__main__":
    host = init_sysmlv2_api()

    # Fetch and display the list of projects currently available on the server
    print("Fetching the list of projects currently available on the server:")
    fetch_projects(host)

    # Create a new project with a unique name by appending a timestamp
    timestamp = datetime.now().strftime("%Y-%m-%d %H:%M:%S")
    project_name = f"New Project - {timestamp}"
    create_project(host, project_name)

    # Fetch and display the updated list of projects after creating the new project
    print("Fetching the updated list of projects after creating a new project:")
    fetch_projects(host)
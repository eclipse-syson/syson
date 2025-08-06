###############################################################################
# Copyright (c) 2025 Obeo.
# This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v2.0
# which accompanies this distribution, and is available at
# https://www.eclipse.org/legal/epl-2.0/
#
# SPDX-License-Identifier: EPL-2.0
#
# Contributors:
#     Obeo - initial API and implementation
###############################################################################

import requests # <1>
import uuid
import argparse
from pathlib import Path

headers = {
    "Content-Type": "application/json"
}

GRAPHQL_ENDPOINT =  "/api/graphql"

# GraphQL query to fetch editing context and stereotypes
fetch_editing_context_query = """
query FetchEditingContext($projectId: ID!) {
  viewer {
    project(projectId: $projectId) {
      currentEditingContext {
        id
        stereotypes {
          edges {
            node {
              id
              label
            }
          }
        }
      }
    }
  }
}
"""

# GraphQL mutation for importing SysML v2 content
import_sysml_mutation = """
mutation InsertTextualSysMLv2($input: InsertTextualSysMLv2Input!) {
  insertTextualSysMLv2(input: $input) {
    __typename
    ... on SuccessPayload {
      id
    }
    ... on ErrorPayload {
      message
    }
  }
}
"""

# Function to fetch the editing context
def fetch_editing_context(url, project_id): # <2>
    graphql_url = f"{url}{GRAPHQL_ENDPOINT}"
    variables = {"projectId": project_id}
    response = requests.post(
        graphql_url,
        json={"query": fetch_editing_context_query, "variables": variables},
        headers=headers
    )

    if response.status_code != 200:
        print(f"Error fetching editing context: {response.status_code} - {response.text}")
        return None

    data = response.json()
    viewer = data.get("data", {}).get("viewer", {})
    project = viewer.get("project", {})
    editing_context = project.get("currentEditingContext", {})
    return editing_context

# Function to import SysML v2 content into a project
def import_sysml_to_project(url, file_path, editing_context_id, object_id): # <3>
    graphql_url = f"{url}{GRAPHQL_ENDPOINT}"
    try:
        # Read the SysML v2 file
        with open(file_path, "r") as file:
            textual_content = file.read()

        # Generate a unique operation ID
        operation_id = str(uuid.uuid4())

        # Prepare mutation variables
        variables = {
            "input": {
                "id": operation_id,
                "editingContextId": editing_context_id,
                "objectId": object_id,
                "textualContent": textual_content
            }
        }

        # Send the mutation request # <4>
        response = requests.post(
            graphql_url,
            json={"query": import_sysml_mutation, "variables": variables},
            headers= headers
        )

        if response.status_code != 200:
            print(f"Error importing SysML v2: {response.status_code} - {response.text}")
            return False

        data = response.json()
        result = data.get("data", {}).get("insertTextualSysMLv2", {})

        if result.get("__typename") == "SuccessPayload":
            return True
        elif result.get("__typename") == "ErrorPayload":
            print(f"Error: {result.get('message')}")
            return False
        else:
            print("Unexpected response:", data)
            return False

    except Exception as e:
        print(f"An error occurred while importing SysML v2: {e}")
        return False

if __name__ == "__main__":
    # Set up argument parsing
    parser = argparse.ArgumentParser(description="SysON new objects from text script")

    parser.add_argument(
        "url",
        nargs="?",  # Makes the argument optional
        default="http://localhost:8080",  # Default value
        help="The API base URL (e.g., http://yourSysonServerURL). Defaults to http://localhost:8080"
    )

    parser.add_argument(
        "file_path",
        type=Path,  # Ensure it's a valid path
        help="File path containing the SysML textual file to import (absolute path required)"
    )

    parser.add_argument(
        "project_id",
        type=str,
        help="The UUID of the project to import the SysML file into"
    )

    parser.add_argument(
        "namespace_elementId",
        type=str,
        help="The elementId of the SysMLv2 element under which the content of the SysMLv2 file will be imported"
    )

    args = parser.parse_args()

    # Get all function parameters # <5>
    url = args.url
    file_path = args.file_path
    # Fetch editing context
    editing_context = fetch_editing_context(url, args.project_id)
    if editing_context:
        editing_context_id = editing_context["id"]
    else:
        print("Editing context not found.")
        exit()
    namespace_id = args.namespace_elementId

    # Import SysML file into the project, under the specified Namespace element
    if import_sysml_to_project(url, file_path, editing_context_id, namespace_id): # <6>
        print(f"SysML v2 file imported successfully: {file_path}") # <7>
    else:
        print(f"Failed to import SysML v2 file: {file_path}") # <7>
###############################################################################
# Copyright (c) 2026 Obeo.
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

import argparse
from pathlib import Path

import requests  # <1>


GRAPHQL_ENDPOINT = "/api/graphql"
DOWNLOAD_ENDPOINT = "/api/editingcontexts/{editing_context_id}/documents/{document_id}"


fetch_editing_context_query = """
query FetchEditingContext($projectId: ID!) {
  viewer {
    project(projectId: $projectId) {
      currentEditingContext {
        id
      }
    }
  }
}
"""


def get_graphql_url(url):
    return f"{url.rstrip('/')}{GRAPHQL_ENDPOINT}"


def get_download_url(url, editing_context_id, document_id):
    return f"{url.rstrip('/')}{DOWNLOAD_ENDPOINT.format(editing_context_id=editing_context_id, document_id=document_id)}"


def print_graphql_errors(data):
    errors = data.get("errors", [])
    for error in errors:
        print(f"GraphQL error: {error.get('message', error)}")


def fetch_editing_context_id(url, project_id):  # <2>
    response = requests.post(
        get_graphql_url(url),
        json={
            "query": fetch_editing_context_query,
            "variables": {"projectId": project_id},
        },
    )

    if response.status_code != 200:
        print(f"Error fetching editing context: {response.status_code} - {response.text}")
        return None

    data = response.json()
    if data.get("errors"):
        print_graphql_errors(data)
        return None

    project = data.get("data", {}).get("viewer", {}).get("project")
    if not project:
        print(f"Project not found: {project_id}")
        return None

    editing_context = project.get("currentEditingContext")
    if not editing_context:
        print(f"Editing context not found for project: {project_id}")
        return None

    return editing_context.get("id")


def download_sysml_file(url, project_id, document_id, output_path):  # <3>
    editing_context_id = fetch_editing_context_id(url, project_id)  # <6>
    if not editing_context_id:
        return False

    response = requests.get(
        get_download_url(url, editing_context_id, document_id),  # <4>
        headers={"Accept": "text/html"},
        stream=True,  # <5>
    )

    if response.status_code != 200:
        print(f"Error downloading SysML file: {response.status_code} - {response.text}")
        return False

    output_path.parent.mkdir(parents=True, exist_ok=True)
    with open(output_path, "wb") as file:
        for chunk in response.iter_content(chunk_size=8192):
            if chunk:
                file.write(chunk)

    print(f"SysML file downloaded successfully: {output_path}")
    return True


def parse_arguments():
    parser = argparse.ArgumentParser(description="Download a SysML textual file from a SysON project")
    parser.add_argument(
        "arguments",
        nargs="+",
        help="Either: project-id document-id output-path, or: url project-id document-id output-path",
    )
    args = parser.parse_args()

    if len(args.arguments) == 3:
        args.url = "http://localhost:8080"
        args.project_id = args.arguments[0]
        args.document_id = args.arguments[1]
        args.output_path = Path(args.arguments[2])
    elif len(args.arguments) == 4:
        args.url = args.arguments[0]
        args.project_id = args.arguments[1]
        args.document_id = args.arguments[2]
        args.output_path = Path(args.arguments[3])
    else:
        parser.error("expected either: project-id document-id output-path, or: url project-id document-id output-path")

    return args


if __name__ == "__main__":
    args = parse_arguments()
    output_path = args.output_path.expanduser().resolve()

    if not download_sysml_file(args.url, args.project_id, args.document_id, output_path):  # <7>
        exit(1)

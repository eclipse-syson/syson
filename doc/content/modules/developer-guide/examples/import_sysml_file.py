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
import json
import uuid
from pathlib import Path

import requests  # <1>


GRAPHQL_ENDPOINT = "/api/graphql"
GRAPHQL_UPLOAD_ENDPOINT = "/api/graphql/upload"


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


upload_document_mutation = """
mutation UploadDocument($input: UploadDocumentInput!) {
  uploadDocument(input: $input) {
    __typename
    ... on UploadDocumentSuccessPayload {
      id
      report
    }
    ... on ErrorPayload {
      messages {
        body
        level
      }
    }
  }
}
"""


def build_headers(token):  # <2>
    headers = {}
    if token:
        headers["Authorization"] = f"Bearer {token}"
    return headers


def get_graphql_url(url):
    return f"{url.rstrip('/')}{GRAPHQL_ENDPOINT}"


def get_graphql_upload_url(url):
    return f"{url.rstrip('/')}{GRAPHQL_UPLOAD_ENDPOINT}"


def print_graphql_errors(data):
    errors = data.get("errors", [])
    for error in errors:
        print(f"GraphQL error: {error.get('message', error)}")


def fetch_editing_context_id(url, project_id, token):  # <3>
    response = requests.post(
        get_graphql_url(url),
        json={
            "query": fetch_editing_context_query,
            "variables": {"projectId": project_id},
        },
        headers=build_headers(token),
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


def print_messages(messages):
    for message in messages:
        print(f"{message.get('level', 'INFO')}: {message.get('body', '')}")


def import_sysml_file(url, file_path, editing_context_id, read_only, token):  # <4>
    operation_id = str(uuid.uuid4())
    operations = {
        "query": upload_document_mutation,
        "variables": {
            "input": {
                "id": operation_id,
                "editingContextId": editing_context_id,
                "file": None,
                "readOnly": read_only,
            }
        },
    }
    file_map = {
        "0": "variables.file"
    }

    with open(file_path, "rb") as file:
        response = requests.post(  # <5>
            get_graphql_upload_url(url),
            data={
                "operations": json.dumps(operations),
                "map": json.dumps(file_map),
            },
            files={
                "0": (file_path.name, file, "text/plain"),
            },
            headers=build_headers(token),
        )

    if response.status_code not in (200, 201):
        print(f"Error importing SysML file: {response.status_code} - {response.text}")
        return False

    data = response.json()
    if data.get("errors"):
        print_graphql_errors(data)
        return False

    payload = data.get("data", {}).get("uploadDocument", {})
    if payload.get("__typename") == "UploadDocumentSuccessPayload":
        print(f"SysML file imported successfully: {file_path}")
        report = payload.get("report")
        if report:
            print("Import report:")
            print(report)
        return True

    if payload.get("__typename") == "ErrorPayload":
        print_messages(payload.get("messages", []))
        return False

    print(f"Unexpected response: {data}")
    return False


def parse_arguments():
    parser = argparse.ArgumentParser(description="Import a SysML textual file into a SysON project")
    parser.add_argument(
        "arguments",
        nargs="+",
        help="Either: file-path project-id, or: url file-path project-id",
    )
    parser.add_argument(
        "--token",
        type=str,
        help="Bearer token used to authenticate on the SysON server",
    )
    parser.add_argument(
        "--read-only",
        action="store_true",
        help="Import the uploaded document as read-only",
    )
    args = parser.parse_args()

    if len(args.arguments) == 2:
        args.url = "http://localhost:8080"
        args.file_path = Path(args.arguments[0])
        args.project_id = args.arguments[1]
    elif len(args.arguments) == 3:
        args.url = args.arguments[0]
        args.file_path = Path(args.arguments[1])
        args.project_id = args.arguments[2]
    else:
        parser.error("expected either: file-path project-id, or: url file-path project-id")

    return args


if __name__ == "__main__":
    args = parse_arguments()
    file_path = args.file_path.expanduser().resolve()

    if not file_path.is_file():
        print(f"File not found: {file_path}")
        exit(1)

    editing_context_id = fetch_editing_context_id(args.url, args.project_id, args.token)  # <6>
    if not editing_context_id:
        exit(1)

    if not import_sysml_file(args.url, file_path, editing_context_id, args.read_only, args.token):  # <7>
        exit(1)

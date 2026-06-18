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
import io
import json
import zipfile

import requests  # <1>


PROJECT_DOWNLOAD_ENDPOINT = "/api/projects/{project_id}"


def get_project_download_url(url, project_id):
    return f"{url.rstrip('/')}{PROJECT_DOWNLOAD_ENDPOINT.format(project_id=project_id)}"


def download_project_archive(url, project_id):  # <2>
    response = requests.get(
        get_project_download_url(url, project_id),  # <3>
        stream=True,
    )

    if response.status_code != 200:
        print(f"Error downloading project archive: {response.status_code} - {response.text}")
        return None

    archive_content = io.BytesIO()
    for chunk in response.iter_content(chunk_size=8192):
        if chunk:
            archive_content.write(chunk)

    archive_content.seek(0)
    with zipfile.ZipFile(archive_content) as archive:
        manifest_name = None
        for entry_name in archive.namelist():
            if entry_name.endswith("/manifest.json"):
                manifest_name = entry_name
                break

        if not manifest_name:
            print("manifest.json not found in the project archive")
            return None

        with archive.open(manifest_name) as manifest_file:
            manifest = json.load(io.TextIOWrapper(manifest_file, encoding="utf-8"))  # <4>

    document_ids_to_name = manifest.get("documentIdsToName", {})
    if not document_ids_to_name:
        print("No documents found in the project manifest")
        return []

    documents = sorted(document_ids_to_name.items(), key=lambda item: item[1].lower())
    return documents


def parse_arguments():
    parser = argparse.ArgumentParser(description="Retrieve the documents of a SysON project")
    parser.add_argument(
        "arguments",
        nargs="+",
        help="Either: project-id, or: url project-id",
    )
    args = parser.parse_args()

    if len(args.arguments) == 1:
        args.url = "http://localhost:8080"
        args.project_id = args.arguments[0]
    elif len(args.arguments) == 2:
        args.url = args.arguments[0]
        args.project_id = args.arguments[1]
    else:
        parser.error("expected either: project-id, or: url project-id")

    return args


if __name__ == "__main__":
    args = parse_arguments()
    documents = download_project_archive(args.url, args.project_id)

    if documents is None:
        raise SystemExit(1)

    for document_id, document_name in documents:  # <5>
        print(f"Document Name: {document_name}, Document ID: {document_id}")

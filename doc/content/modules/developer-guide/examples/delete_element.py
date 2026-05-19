# These examples are adapted from the SysML v2 API Cookbook, available at
# https://github.com/Systems-Modeling/SysML-v2-API-Cookbook, maintained by the
# Object Management Group (OMG).
# The original cookbook is designed for use with Jupyter Lab.
# These examples have been adapted to run as standalone Python scripts, making
# them suitable for use in various environments, including SysON.
# They showcase practical usage scenarios and may include additional functionality
# or modifications tailored to specific needs.

import requests  # <1>
from init_api import parse_arguments
from init_api import init_sysmlv2_api
from fetch_commits import get_last_commit_id


def fetch_element(host, project_id, commit_id, element_id):  # <2>
    element_url = f"{host}/projects/{project_id}/commits/{commit_id}/elements/{element_id}"  # <3>
    response = requests.get(element_url)  # <4>
    if response.status_code == 200:
        return response.json()

    print(f"Unable to fetch element: {response.status_code} - {response.text}")
    return None


def delete_element(host, project_id, element_id):  # <5>
    commit_id = get_last_commit_id(host, project_id)
    if not commit_id:
        print("Deletion aborted: unable to determine the latest commit.")
        return False

    element = fetch_element(host, project_id, commit_id, element_id)
    if not element:
        print(f"Deletion aborted: element '{element_id}' was not found.")
        return False

    print(
        f"Deleting element '{element.get('name') or 'N/A'}' "
        f"(id = {element['@id']}, type = {element['@type']})"
    )

    commit_url = f"{host}/projects/{project_id}/commits"  # <6>
    commit_body = {
        "@type": "Commit",
        "change": [
            {
                "@type": "DataVersion",
                "identity": {
                    "@id": element_id,
                    "@type": "DataIdentity",
                },
            }
        ],
    }  # <7>

    response = requests.post(commit_url, json=commit_body)  # <8>
    if response.status_code != 201:
        print(f"Error deleting element: {response.status_code} - {response.text}")
        return False

    print("Delete commit created successfully.")

    new_commit_id = get_last_commit_id(host, project_id)  # <9>
    if not new_commit_id:
        print("Deletion verification skipped: unable to determine the latest commit.")
        return True

    verification_url = f"{host}/projects/{project_id}/commits/{new_commit_id}/elements/{element_id}"
    verification_response = requests.get(verification_url)  # <10>
    if verification_response.status_code == 404:
        print(f"Element '{element_id}' deleted successfully.")
        return True

    print(
        "Deletion verification failed: "
        f"the element is still accessible ({verification_response.status_code})."
    )
    return False


if __name__ == "__main__":
    args = parse_arguments()
    host = init_sysmlv2_api()
    project_id = args.project_id
    element_id = args.element_id

    if not element_id:
        raise SystemExit("Usage: python delete_element.py <project-id> <element-id>")

    delete_element(host, project_id, element_id)

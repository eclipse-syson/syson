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

def fetch_commits(host, project_id):  # <2>
    commits_url = f"{host}/projects/{project_id}/commits"  # <3>
    response = requests.get(commits_url)  # <4>
    if response.status_code == 200:  # <5>
        commits = response.json()
        for commit in commits:
            print(f"Commit ID: {commit['@id']}")
        return commits
    else:
        print(f"Error fetching commits: {response.status_code} - {response.text}")
        return None

#  Retrieves the latest commit for a given project.
def get_last_commit_id(host, project_id):
    commits = fetch_commits(host, project_id)
    if commits:
        last_commit = commits[-1] if commits else None
        if last_commit:
            last_commit_id = last_commit['@id']
            print(f"Last Commit ID: {last_commit_id}")
        return last_commit_id
    else:
        print("No commits available.")
        return None

if __name__ == "__main__":
    args = parse_arguments()
    host = init_sysmlv2_api()
    project_id = args.project_id
    fetch_commits(host, project_id)

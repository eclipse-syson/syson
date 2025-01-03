# These examples are adapted from the SysML v2 API Cookbook, available at
# https://github.com/Systems-Modeling/SysML-v2-API-Cookbook, maintained by the
# Object Management Group (OMG).
# The original cookbook is designed for use with Jupyter Lab.
# These examples have been modified to function as standalone Python scripts,
# making them applicable in various environments, including SysON.
# They demonstrate practical usage scenarios and may include additional
# functionality or adjustments tailored to specific needs.

import requests # <1>
from init_api import init_sysmlv2_api

# Fetches all PartUsage elements in a given project and displays their names and IDs in a DataFrame.
def get_part_usage(host, project_id, part_usage_name):  # <2>
    query_input = {
        '@type':'Query',
        'select': ['name','@id','@type','owner'],
        'where': {
            '@type': 'CompositeConstraint',
            'operator': 'and',
            'constraint': [
            {
                '@type': 'PrimitiveConstraint',
                'inverse': False,
                'operator': '=',
                'property': 'name',
                'value': part_usage_name
            },
            {
                '@type': 'PrimitiveConstraint',
                'inverse': False,
                'operator': '=',
                'property': '@type',
                'value': 'PartUsage'
            }
        ]
        }
    }

    query_url = f"{host}/projects/{project_id}/query-results"  # <3>
    response = requests.post(query_url, json=query_input)  # <4>

    if response.status_code == 200:  # <5>
        query_response_json = response.json()
        print(query_response_json)
        element_id = query_response_json[0]['@id']
        print(f"Element ID: {element_id}")

    else:
        print(f"Failed to fetch PartUsage elements. Status code: {response.status_code}")
        print(f"Response: {response.text}")

if __name__ == "__main__":
    host = init_sysmlv2_api()
    project_id = "<your-project-id>"  # Replace with your actual project ID
    part_usage_name = "<your-part-usage-name"   # Replace with your actual part usage name
    get_part_usage(host, project_id, part_usage_name)
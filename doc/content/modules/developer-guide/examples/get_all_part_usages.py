# These examples are adapted from the SysML v2 API Cookbook, available at
# https://github.com/Systems-Modeling/SysML-v2-API-Cookbook, maintained by the
# Object Management Group (OMG).
# The original cookbook is designed for use with Jupyter Lab.
# These examples have been modified to function as standalone Python scripts,
# making them applicable in various environments, including SysON.
# They demonstrate practical usage scenarios and may include additional
# functionality or adjustments tailored to specific needs.

import requests # <1>
from init_api import parse_arguments
from init_api import init_sysmlv2_api
import pandas as pd

# Fetches all PartUsage elements in a given project and displays their names and IDs in a DataFrame.
def get_all_part_usages(host, project_id):  # <2>
    query_input = {
        '@type': 'Query',
        'select': ['name', '@id', '@type', 'owner'],
        'where': {
            '@type': 'CompositeConstraint',
            'operator': 'and',
            'constraint': [
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
        df = pd.DataFrame({'Part Usage Name': [], 'Part Usage ID': []})

        for part_usage in query_response_json:
            df = pd.concat(
                [df, pd.DataFrame({
                    'Part Usage Name': [part_usage.get('name', 'N/A')],
                    'Part Usage ID': [part_usage['@id']]
                })],
                ignore_index=True
            )
        print(df)
    else:
        print(f"Failed to fetch PartUsage elements. Status code: {response.status_code}")
        print(f"Response: {response.text}")

if __name__ == "__main__":
    args = parse_arguments()
    host = init_sysmlv2_api()
    project_id = args.project_id

    get_all_part_usages(host, project_id)
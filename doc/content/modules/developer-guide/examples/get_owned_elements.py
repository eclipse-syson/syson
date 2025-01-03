# These examples are adapted from the SysML v2 API Cookbook, available at
# https://github.com/Systems-Modeling/SysML-v2-API-Cookbook, maintained by the
# Object Management Group (OMG).
# The original cookbook is designed for use with Jupyter Lab.
# These examples have been adapted to run as standalone Python scripts, making
# them suitable for use in various environments, including SysON.
# They showcase practical usage scenarios and may include additional functionality
# or modifications tailored to specific needs.

import requests
from init_api import parse_arguments
from init_api import init_sysmlv2_api
from fetch_commits import get_last_commit_id

# Function to fetch an element and print its name and type
def get_element(host, project_id, commit_id, element_id, indent):
     # Fetch the element in the given commit of the given project
    element_url = f"{host}/projects/{project_id}/commits/{commit_id}/elements/{element_id}"  # <3>
    response = requests.get(element_url)  # <1>

    if response.status_code == 200:
        element_data = response.json()
        element_name_to_print = element_data['name'] if element_data['name'] else 'N/A'
        element_id = element_data ['@id']
        element_type = element_data ['@type']
        print(f"{indent} - {element_name_to_print} (id = {element_id} , type = {element_type})") # <2>
        return element_data
    else:
        return None

# Fetches immediate owned elements for a given element
def get_owned_elements_immediate(host, project_id, commit_id, element_id, indent):
    element_data = get_element(host, project_id, commit_id, element_id, indent)
    if element_data:
        owned_elements = element_data['ownedElement']
        if len(owned_elements) > 0:
            for owned_element in owned_elements:
                get_element(host, project_id, commit_id, owned_element['@id'], indent + '  ')
    else:
        print(f"Unable to fetch element with id '{element_id}' in commit '{commit_id}' of project '{project_id}'")

# Fetches owned elements recursively for a given element
def get_owned_elements(host, project_id, commit_id, element_id, indent):
    element_data = get_element(host, project_id, commit_id, element_id, indent)
    if element_data: # <3>
        owned_elements = element_data['ownedElement']
        if len(owned_elements) > 0:
            for owned_element in owned_elements:
                get_owned_elements(host, project_id, commit_id, owned_element['@id'], indent+' ')
    else:
        print(f"Unable to fetch element with id '{element_id}' in commit '{commit_id}' of project '{project_id}'")  # <4>

if __name__ == "__main__":
    args = parse_arguments()
    host = init_sysmlv2_api()
    project_id = args.project_id
    commit_id = get_last_commit_id(host, project_id)
    element_id = args.element_id

    #Get owned elements (immediate) for the given element in the given commit of the given project
    print("Immediate Owned Elements:")
    get_owned_elements_immediate(host, project_id, commit_id, element_id, '')

    # Get owned elements (recursive) for the given element in the given commit of the given project
    print("\nRecursive Owned Elements:")
    get_owned_elements(host, project_id, commit_id, element_id, '')
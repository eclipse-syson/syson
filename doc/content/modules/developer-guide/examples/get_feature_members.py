# These examples are adapted from the SysML v2 API Cookbook, available at
# https://github.com/Systems-Modeling/SysML-v2-API-Cookbook, maintained by the
# Object Management Group (OMG).
# The original cookbook is designed for use with Jupyter Lab.
# These examples have been adapted to run as standalone Python scripts, making
# them suitable for use in various environments, including SysON.
# They showcase practical usage scenarios and may include additional functionality
# or modifications tailored to specific needs.

from init_api import parse_arguments
from init_api import init_sysmlv2_api
from fetch_commits import get_last_commit_id
from get_owned_elements import get_element

def get_member_features(host, project_id, commit_id, element_id, member_type, indent):
    # Fetch the element
    element_data = get_element(host, project_id, commit_id, element_id, indent)
    if element_data:
        element_type = element_data ['@type']

        if element_type == member_type:
            print(element_data)
            # Feature memberships
            element_features = element_data['ownedFeature']
            if len(element_features) > 0:
                for feature in element_features:
                    get_member_features(host, project_id, commit_id, feature['@id'], member_type, indent + "  ")

if __name__ == "__main__":
    args = parse_arguments()
    host = init_sysmlv2_api()
    project_id = args.project_id
    commit_id = get_last_commit_id(host, project_id)
    element_id = args.element_id

    # Get Parts Tree
    print("Parts Tree:")
    get_member_features(host, project_id, commit_id, element_id, 'PartUsage', " ")

    # Get Behavior Tree
    print("Behaviors Tree:")
    get_member_features(host, project_id, commit_id, element_id, 'ActionUsage', " ")

    # Get Requirements Tree
    print("Requirements Tree:")
    get_member_features(host, project_id, commit_id, element_id, 'RequirementUsage', " ")
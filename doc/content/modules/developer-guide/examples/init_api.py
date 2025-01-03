# These examples are adapted from the SysML v2 API Cookbook, available at
# https://github.com/Systems-Modeling/SysML-v2-API-Cookbook, maintained by the
# Object Management Group (OMG).
# The original cookbook is designed for use with Jupyter Lab.
# These examples have been adapted to run as standalone Python scripts, making
# them suitable for use in various environments, including SysON.
# They showcase practical usage scenarios and may include additional functionality
# or modifications tailored to specific needs.
import argparse

def init_sysmlv2_api():
    host =  "http://localhost:8080/api/rest" # Replace with your actual API host URL # <1>
    return host

def parse_arguments():
    # Parse command-line arguments
    parser = argparse.ArgumentParser()
    parser.add_argument(
        "project_id",
        type=str,
        help="The project ID.",
    )
    parser.add_argument(
        "element_id",
        type=str,
        nargs="?",  # This makes element_id optional
        help="The element ID (optional).",
    )
    return parser.parse_args()
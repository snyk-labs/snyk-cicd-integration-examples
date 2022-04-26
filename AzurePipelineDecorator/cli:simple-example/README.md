# This decorator uses Snyk CLI to run the injected task

## Pre-requisites

1. Create a [publisher account](https://docs.microsoft.com/en-us/azure/devops/extend/get-started/node?view=azure-devops#create-a-publisher)
2. Should have access to Snyk API Token


## Files to update

- Update the YAML file with input details - 
    - Add a variable SNYK_TOKEN with the value of your API token for your pipelines
    - Update the org_name and optionally add other CLI params and flags based on your requirements. 
- Update the vss-extension.json - To include your publisher id, version and optionally -  name of the extension and description etc.

Follow the extension creation, publishing, installation and testing steps as listed in the README of parent dir AzurePipelineDecorator
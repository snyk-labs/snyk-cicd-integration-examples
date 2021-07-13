# This decorator uses Snyk Plugin to run the injected task

## Pre-requisites

1. Create a [publisher account](https://docs.microsoft.com/en-us/azure/devops/extend/get-started/node?view=azure-devops#create-a-publisher)
2. Create [Azure service connection with Snyk API Key](https://support.snyk.io/hc/en-us/articles/360004127677-Azure-Pipelines-integration)


## Files to update

- Update the YAML file with input details - 
    - serviceConnectionEndpoint -  name of the service connection created under project settings
    - testType: with 'app' or 'container' based on the tests you are running
    - severityThreshold: low, medium, high based on the threshold you want to set 
    - failOnIssues: false or true
    - projectName: 'PROVIDE_PROJ_NAME_HARDCODED_OR_VARIABLE'
    - organization: 'PROVIDE_ORG_NAME'
    - additionalArguments: '--all-projects'
- Update the vss-extension.json - To include your publisher id, version and optionally -  name of the extension, description, etc.

Follow the extension creation, publishing, installation and testing steps as listed in the README of parent dir AzurePipelineDecorator
# Azure Pipleline CI/CD Examples using Decorators

Azure pipeline decorators let you add steps at the beginning and at the end of every job. 

Note : Decorators in this repo work only with Azure DevOps 

Check the official documentation [here](https://docs.microsoft.com/en-us/azure/devops/extend/develop/add-pipeline-decorator?view=azure-devops) and [here](https://docs.microsoft.com/en-us/azure/devops/extend/develop/pipeline-decorator-context?view=azure-devops) for more information about Azure Pipelines decorators.

## Introduction 

This project provides you guidelines on how to inject Snyk test and monitor checks post build execution of all the pipelines in the organization. 

With a traditional pipeline approach a user would normally be required to add the Snyk task for every pipeline to check for any vulnerabilities. This process relies a lot on developers to remember to add this step/task in all the new and existing pipelines. 

Using Pipeline decorators (private extensions) Snyk checks can be scaled across the organizations. 

## About this repo structure
This project showcases two ways of injecting Snyk tasks 

1. Using Snyk task plugin (under directory "extension")
2. Using Snyk CLI (under directory "cli/simple-example")

## Getting Started

Each folder contains a standalone decorator extension with Snyk. 

Pick the implementation you want to use and follow the instructions below. 

### Pre-requisites
1. Must have a Snyk Account and snyk API token 
    - If you are using the CLI example then you would need to add the SNYK_TOKEN as a variable for your pipeline
    - If you are using the extension example then that would require a [service connection](https://support.snyk.io/hc/en-us/articles/360004127677-Azure-Pipelines-integration#UUID-de2c0973-46ee-29ea-742e-9c1acc91d13e) with Snyk. 
2. Install TFX CLI ```npm install -g tfx-cli```
3. Create a [publisher account](https://docs.microsoft.com/en-us/azure/devops/extend/get-started/node?view=azure-devops#create-a-publisher)

### Update the code 

Based on the implementation of choice, you would need to update two files with relevant information: 

- Update the YAML file - To include authentication, project and org name
- Update the vss-extension.json - To include your publisher id, version and optionally -  name of extension, description 

### Build and package the extensio

1. First install node.js dependencies

```npm install```

2. Then create the actual extension. 
Note: To run this step you should be in the directory with file vss-extension.json

```tfx extension create``` 

If you updated the YAML file and need to create revised versions of the extension then use the following command to auto-update the version#
``` tfx extension create --rev-version```

### Publish the extension 
When you have the extension file, head to [the marketplace management portal](https://marketplace.visualstudio.com/manage) and add the new extension.

You will also need to share the extension to your organization.

Note: Only private extensions can contain Decorators, so be sure not to make it public.

For more info take a look at: https://docs.microsoft.com/en-us/azure/devops/extend/get-started/node?view=azure-devops#package-and-publish-your-extension

### Install the extension 

Once shared with your Azure DevOps Organization, you can head to the Manage Extensions tab under Organization Settings, go to Shared and finally install your Decorator Extension. (org settings > Extensions > shared > install your plugin)

For more info refer: https://docs.microsoft.com/en-us/azure/devops/extend/get-started/node?view=azure-devops#install-your-extension

### Test and run your pipeline

After the plugin has been installed run any of the pipelines under the organization and you should see one of the jobs as "Decorator injected task..."

![image](https://user-images.githubusercontent.com/32653970/116176786-bedd6e80-a6e0-11eb-8f49-892158b6a9c6.png)



# snyk-cicd-integration-examples

The purpose of this repo is to share examples of CI/CD integrations with Snyk.

There are typically 4 ways to deploy Snyk in a CI/CD pipeline:

1. Using a native plugin from the marketplace
1. Installing Snyk CLI using the npm i -g snyk command in the pipeline (CLI-like)
1. Installing the Snyk CLI binary 
1. Installing the Snyk CLI container image

The various ways are explained in details on the Snyk [support page](https://support.snyk.io/hc/en-us/articles/360018245398-Snyk-CI-CD-Integration-good-practices-).

We are continuously trying to update this repo with useful templates.

If you have suggestions for improvement feel free to share them with us!

## Approach

The CI/CD examples are meant to showcase the Snyk integration and therefore are **not** complete build pipelines. Applications will not be compiled, tested or packaged unless it is needed to showcase Snyk features.

Snyk has various ways to output results during the CI/CD process.

1. Failing the build using a exit code if security vulnerabilities are detected
1. Output of HTML reports using `snyk-to-html` and potentially ignoring the return code
1. Output of SARIF artifacts for Git repositories with integrated CI/CD systems so that pull requests can be decorated (Github, Bitbucket etc..)

Whenever possible, this repository should showcase all possible outputs.

## Naming & Contents

The top level folders resemble the CI/CD system in use, e.g. `Github Actions`, `Azure Pipelines`, and can contain sub folders if needed.

For the files themselves, they are meant to be "self-describing" and "self-contained". This means:

- They are named based on the `system`, `type of installation`, `language/package manager` and optionally classifiers on the output. For example, a file `GitHubActions-npm install-nodeJS.yml` describes
  - a Github Actions file 
  - installation of `snyk` via `npm install`
  - specific instructions for `nodeJS` projects
- A file named `GitHubActions-npm install-nodeJS-sarif.yml` could build on the file above but output specific sarif instructions for GH Actions to pick up.

The `language/package manager` part can be renamed to `generic`, if no language or package specific instructions are needed!

As the audience for these files are users new to Snyk, the contents of the file need to be concise & helpful. The top of the file should contain helpful links to the documentation of the CI/CD system itself and a note on what the filename in the end _should_ be. In the GH Actions example, the filename really should be `.github/workflows/snyk.yml`, for example.

Refer to the below table for example values.
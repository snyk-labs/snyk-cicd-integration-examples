// This example utilizes the pipeline plugin:
// https://www.jenkins.io/solutions/pipeline/

// Please read this file in its entirety and ensure that you've replaced the installations, tokens, URLs, etc. match your organization's

pipeline {
    agent any
    // Install the Jenkins tools you need for your project / environment
    tools {
        nodejs 'nodeInstallationName' // Refers to a global tool configuration for nodejs called 'nodeInstallationName' 
    }

    stages {
        stage('Initialize & Cleanup Workspace') {
            steps {
               echo 'Initialize & Cleanup Workspace'
               sh 'ls -la'
               sh 'rm -rf *'
               sh 'rm -rf .git'
               sh 'rm -rf .gitignore'
               sh 'ls -la'
            }
        }

        stage('Git Clone') {
            steps {
                // Clone the repo that is going to be built
                git url: 'https://github.com/snyk-labs/nodejs-goof'
                sh 'ls -la'
            }
        }

        stage('Build') {
            steps {
                // Install dependencies and run the build.
                // snyk-filter requires node-jq and snyk-filter to be installed
                sh 'npm install -g node-jq'
                sh 'npm install -g snyk-filter'
                // browserify is a dependency for the project I'm building in this walkthrough. 
                sh 'npm install browserify'
                // this is the node command for starting the build
                sh 'npm run build'
            }
        }

        stage('Snyk Test using plugin') {
            // Run snyk test to check for vulnerabilities and fail the build if any are found
            steps {
                snykSecurity(
                    snykInstallation: 'snykInstallationName', // Refers to the global tool configuration for Snyk called 'snykInstallationName'
                    snykTokenId: 'snykTokenId', // Refers to the ID of the Snyk API Token Credential.
                    monitorProjectOnBuild: false, // snyk-filter is not supported with monitor, so this should be set to false.
                    failOnIssues: 'false', // if the build fails in the snykSecurity step, snyk-filter will not run, which is why failOnIssues is set to false.
                    additionalArguments: '--json-file-output=vuln.json' //this outputs the results into a JSON named vuln.json in the workspace directory
                    // place other applicable parameters here (see here: https://plugins.jenkins.io/snyk-security-scanner/#plugin-content-pipeline-projects)
                )
                // Run snyk-filter, using the vuln.json created in the snykSecurity step and the location of the filter which in this example is in the same repo as the project
                sh 'snyk-filter -i vuln.json -f snyk-filter/exploitable_cvss_9.yml'
            }
        }
    }
}

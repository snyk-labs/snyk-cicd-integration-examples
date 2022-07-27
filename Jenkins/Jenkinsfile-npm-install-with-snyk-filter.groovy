// This example utilizes the pipeline plugin:
// https://www.jenkins.io/solutions/pipeline/

// Please read this file in its entirety and ensure that you've replaced the installations, tokens, URLs, etc. match your organization's

pipeline {
    agent any

    tools {
        nodejs 'nodeInstallationName'
    }
    stages {
        stage('Git Clone') {
            steps {
                git url: 'https://github.com/snyk-labs/nodejs-goof' 
            }
        }

        stage('Install Snyk-Filter') {
            steps {
                // snyk-filter requires node-jq and snyk-filter to be installed
                sh 'npm install -g node-jq snyk-filter'
            }
        }
        
        stage('Build') {
            steps {
                // Add your build instructions in this stage
            }
        }

        stage('Snyk Test using plugin') {
            // Run snyk test to check for vulnerabilities and fail the build if any are found
            steps {
                // Run snyk test, output results as json and then run snyk-filter using that json and the location of the filter.
                snykSecurity( 
                    snykInstallation: 'snykInstallationName', 
                    snykTokenId: 'snykTokenId', 
                    monitorProjectOnBuild: false, // snyk-filter is not supported with monitor, so this should be set to false.
                    failOnIssues: 'false', // if the build fails in the snykSecurity step, snyk-filter will not run, which is why failOnIssues is set to false.
                    additionalArguments: '--json-file-output=all-vulnerabilities.json'
                )
                sh 'snyk-filter -i vuln.json -f snyk-filter/exploitable_cvss_9.yml'
            }
        }
    }
}

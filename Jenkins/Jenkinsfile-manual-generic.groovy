// Jenkinsfile solution based on the pipeline plugin:
// https://www.jenkins.io/solutions/pipeline/

// Results are output in SARIF format & processed using the Warnings NG plugin:
// https://plugins.jenkins.io/warnings-ng/

// Please read the contents of this file carefully & ensure the URLs, tokens etc match your organisations needs.


def snykCliBaseName(){
    if (isUnix()) {
        def uname = sh script: 'uname', returnStdout: true
        if (uname.startsWith("Darwin")) {
            return "snyk-macos"
        } else {
            return "snyk-linux"
        }
    } else {
        return "snyk-win.exe"
    }
}

pipeline {
    agent any

    stages {
        stage('git clone') {
            steps {
                git url: 'https://github.com/sebsnyk/juice-shop.git'
            }
        }

        // Install the Snyk CLI by downloading a binary. For more information, check:
        // https://docs.snyk.io/snyk-cli/install-the-snyk-cli
        stage('Install snyk CLI') {
            steps {
                script {
                    def basename = snykCliBaseName()

                    if (isUnix()) {
                        sh("curl -O -s -L https://static.snyk.io/cli/latest/$basename")
                        sh("curl -O -s -L https://static.snyk.io/cli/latest/${basename}.sha256")
                        sh("shasum -c ${basename}.sha256")
                        sh("chmod +x $basename && mv $basename ./snyk")
                    } else {
                        throw "Not implemented."
                    }
                }
            }
        }

        // This OPTIONAL step will configure the Snyk CLI to connect to the EU instance of Snyk.
        // stage('Configure Snyk for EU data center') {
        //     steps {
        //         sh './snyk config set use-base64-encoding=true'
        //         sh './snyk config set endpoint='https://app.eu.snyk.io/api'
        //     }
        // }
        
        // Authorize the Snyk CLI
        stage('Authorize Snyk CLI') {
            steps {
                withCredentials([string(credentialsId: 'SNYK_TOKEN', variable: 'SNYK_TOKEN')]) {
                    sh './snyk auth ${SNYK_TOKEN}'
                }
            }
        }

        stage('Build App') {
            steps {
                // Replace this with your build instructions, as necessary.
                sh 'echo no-op'
            }
        }

        stage('Snyk') {
            parallel {
                stage('Snyk Open Source') {
                    steps {
                        catchError(buildResult: 'SUCCESS', stageResult: 'FAILURE') {
                            sh './snyk test --sarif-file-output=results-open-source.sarif'
                        }
                        recordIssues tool: sarif(name: 'Snyk Open Source', id: 'snyk-open-source', pattern: 'results-open-source.sarif')
                    }
                }
                stage('Snyk Code') {
                    steps {
                        catchError(buildResult: 'SUCCESS', stageResult: 'FAILURE') {
                            sh './snyk code test --sarif-file-output=results-code.sarif'
                        }
                        recordIssues  tool: sarif(name: 'Snyk Code', id: 'snyk-code', pattern: 'results-code.sarif')
                    }
                }
                stage('Snyk Container') {
                    steps {
                        catchError(buildResult: 'SUCCESS', stageResult: 'FAILURE') {
                            sh './snyk container test sebsnyk/juice-shop --file=Dockerfile --sarif-file-output=results-container.sarif'
                        }
                        recordIssues tool: sarif(name: 'Snyk Container', id: 'snyk-container', pattern: 'results-container.sarif')
                    }
                }
                stage('Snyk IaC') {
                    steps {
                        catchError(buildResult: 'SUCCESS', stageResult: 'FAILURE') {
                            sh './snyk iac test --sarif-file-output=results-iac.sarif'
                        }
                        recordIssues tool: sarif(name: 'Snyk IaC', id: 'snyk-iac', pattern: 'results-iac.sarif')
                    }
                }
            }
        }
    }
} 
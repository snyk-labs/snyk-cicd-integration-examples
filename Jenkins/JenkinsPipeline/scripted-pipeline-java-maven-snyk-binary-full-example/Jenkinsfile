node {

    def mavenHome = tool 'maven-3.6.0' // Refers to a global tool configuration for Maven called 'maven-3.6.0'
    env.PATH="${env.PATH}:$mavenHome/bin"

    stage('Initialize & Cleanup Workspace') {
       echo 'Initialize & Cleanup Workspace'
       sh 'ls -la'
       sh 'rm -rf *'
       sh 'rm -rf .git'
       sh 'rm -rf .gitignore'
       sh 'ls -la'
    }

    stage('Git Clone') {
        git url: 'https://github.com/jeff-snyk-demo/testproject-java-maven.git'
        sh 'ls -la'
    }

    stage('Test Build Requirements') {
        sh 'java -version'
        sh 'mvn -v'
    }

    // Not required if you install the Snyk CLI on your Agent
    stage('Download Latest Snyk CLI') {
        latest_version = sh(script: 'curl -Is "https://github.com/snyk/snyk/releases/latest" | grep "^location" | sed s#.*tag/##g', returnStdout: true)
        latest_version = latest_version.trim()
        echo "Latest Snyk CLI Version: ${latest_version}"

        snyk_cli_dl_linux="https://github.com/snyk/snyk/releases/download/${latest_version}/snyk-linux"
        echo "Download URL: ${snyk_cli_dl_linux}"

        sh """
            curl -Lo ./snyk "${snyk_cli_dl_linux}"
            chmod +x snyk
            ls -la
            ./snyk -v
        """
    }

    // Authorize the Snyk CLI
    withCredentials([string(credentialsId: 'SNYK_TOKEN', variable: 'SNYK_TOKEN_VAR')]) {
        stage('Authorize Snyk CLI') {
            sh './snyk auth ${SNYK_TOKEN_VAR}'
        }
    }

    stage('Build') {
        sh 'mvn package'
    }

    // Run snyk test to check for vulnerabilities and fail the build if any are found
    // Consider using --severity-threshold=<low|medium|high> for more granularity (see snyk help for more info).
    stage('Snyk Test using Snyk CLI') {
        sh './snyk test'
    }

    // Capture the dependency tree for ongoing monitoring in Snyk.
    // This is typically done after deployment to some environment (ex staging, test, production, etc).
    stage('Snyk Monitor using Snyk CLI') {
        // Use your own Snyk Organization with --org=<your-org>
        sh './snyk monitor --org=demo-applications'
    }

}

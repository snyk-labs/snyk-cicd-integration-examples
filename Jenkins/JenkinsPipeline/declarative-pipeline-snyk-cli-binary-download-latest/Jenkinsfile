pipeline {
    agent any

    stages {

        // Not required if you just install the Snyk CLI on your Agent
        stage('Download Latest Snyk CLI') {
            steps {
                sh '''
                    latest_version=$(curl -Is "https://github.com/snyk/cli/releases/latest" | grep "^location" | sed s#.*tag/##g | tr -d "\r")
                    echo "Latest Snyk CLI Version: ${latest_version}"

                    snyk_cli_dl_linux="https://github.com/snyk/cli/releases/download/${latest_version}/snyk-linux"
                    echo "Download URL: ${snyk_cli_dl_linux}"

                    curl -Lo ./snyk "${snyk_cli_dl_linux}"
                    chmod +x snyk
                    ls -la
                    ./snyk -v
                '''
            }
        }
    }
}

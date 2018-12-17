node {

    // Not required if you install the Snyk CLI on your Agent
    stage('Download Latest Snyk CLI') {
        latest_version = sh(script: 'curl -Is "https://github.com/snyk/snyk/releases/latest" | grep "Location" | sed s#.*tag/##g', returnStdout: true)
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
}

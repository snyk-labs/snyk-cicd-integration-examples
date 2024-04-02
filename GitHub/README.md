# Snyk Github Actions
Using Snyk Effectively  on Github

## Using Snyk Actions
All of these workflow use [Snyk Actions](https://github.com/snyk/actions) to execute the desired use cases.

In order to use the Snyk Action, you will need to have a Snyk API token. You can sign up for a [free account](www.snyk.io/login) and save your [API token](https://github.com/snyk/actions#getting-your-snyk-token) as a [secret](https://docs.github.com/en/actions/security-guides/encrypted-secrets) in your Github repository.

### Inline display of SARIF data
Github supports the inline display of scan results.
During the Github Actions-run, the results get pushed to Github Security.

![](gh-actions-pipeline-npm-nodejs-sarif.png)

On an issue-card you can immediately review the issue:
![](gh-actions-pipeline-npm-nodejs-sarif_issue_card.png)

See [GH-actions-pipeline-npm-nodejs-sarif.yml](GH-actions-pipeline-npm-nodejs-sarif.yml) for instructions how to get this output and build-workflow.

### Open Source Delta Check

This workflow lets you block pipelines only if new vulnerabilities are introduced. It uses the [Snyk Delta](https://github.com/snyk-tech-services/snyk-delta) tool to do the comparison with an already existing monitored projects to show results.

```yaml
jobs:
  security:
    runs-on: ubuntu-latest
    
    steps:
      - uses: actions/checkout@master
      - name: Use Node.js
        uses: actions/setup-node@v1
        with:
          node-version: 18.4
      - name: Installing snyk-delta and dependencies
        run: npm i -g snyk-delta
      - uses: snyk/actions/setup@master
      - name: Snyk Test
        run: snyk test --json --print-deps | snyk-delta
        env:
          SNYK_TOKEN: ${{ secrets.SNYK_TOKEN }}
```

### Code Scanning Alerts for Snyk Code (SAST)

This workflow tests your application for SAST vulnerabities and then presents them in the Secuirty tab of Github. It provides in-line details of where the vulnerability is found and provides details and guidance to fix it.

```yaml
jobs:
  snyk:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - uses: snyk/actions/setup@master
      - name: Snyk Code Test
        continue-on-error: true
        run: snyk code test --sarif > snyk_sarif
        env:
          SNYK_TOKEN: ${{ secrets.SNYK_TOKEN }}
      - name: Upload results to Github Code Scanning
        uses: github/codeql-action/upload-sarif@v1
        with:
          sarif_file: snyk_sarif
```

### Container Monitor Results

This workflow lets you inspect your image for vulnerabilities, and creates a project on your Snyk Account with the available base image remediation recommendations.

```yaml
jobs:
  security:
    runs-on: ubuntu-latest
    steps:
    - uses: actions/checkout@v2
    - name: Build the Docker image
      run: docker build . --file Dockerfile --tag my-vuln-image:latest
    - name: Monitor Docker Vulnerabilities
      uses: snyk/actions/docker@master
      env:
        SNYK_TOKEN: ${{ secrets.SNYK_TOKEN }}
      with:
        image: my-vuln-image:latest
        command: monitor
```

### Code Scanning IaC Results

This workflow tests your infrastructure as code files for misconfigurations and populates them in the Secuirty Tab of Github. It requires the path to the configuration file that you would like to test. For example `deployment.yaml` for a Kubernetes deployment manifest or `main.tf` for a Terraform configuration file.

```yaml
name: Snyk Infrastructure as Code Check
jobs:
  snyk:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - name: Run Snyk to check configuration files for security issues
        continue-on-error: true
        uses: snyk/actions/iac@master
        env:
          SNYK_TOKEN: ${{ secrets.SNYK_TOKEN }}
        with:
          file: deployment.yaml
      - name: Upload result to GitHub Code Scanning
        uses: github/codeql-action/upload-sarif@v1
        with:
          sarif_file: snyk.sarif
          name: Infrastructure as Code Snyk Results
```

### Connect to different Snyk data centers

If your data residency is different to the standard US instance of Snyk, then `SNYK_API` can be used.
Make sure to configure this variable according to the [documentation](https://docs.snyk.io/more-info/data-residency-at-snyk#cli-and-ci-pipelines-urls) within the [secrets](https://docs.github.com/en/actions/security-guides/encrypted-secrets) of the repository.

```yaml
name: Snyk Open Source Scan (.NET)
jobs:
  snyk:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - name: Run Snyk to check for vulnerabilities
        uses: snyk/actions/dotnet@master
        env:
          SNYK_API: ${{ secrets.SNYK_API }}
          SNYK_TOKEN: ${{ secrets.SNYK_TOKEN }}
```
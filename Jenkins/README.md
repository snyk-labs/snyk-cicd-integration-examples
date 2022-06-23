# Jenkins Pipelines

Jenkins supports a declarative way to configure pipelines, using the https://www.jenkins.io/solutions/pipeline plugin.

Using this approach, Snyk can be enabled in a straightforward way.

The [example](./Jenkinsfile-npm-install-generic.groovy) listed in this folder will activate all of Snyk's products and output to the Jenkins status pages using the warnings-ng plugin.

The scripts may depend on various plugins. Please follow the information within the scripts to install them.

## Samples

Trend lines across all of Snyks products.

![](01-jenkins-overview.png)

High level overview for a product is available.

![](02-jenkins-code.png)

Snyk Code issues can be seen in detail, including their occurance within the source code file.

![](03-jenkins-code-detail.png)

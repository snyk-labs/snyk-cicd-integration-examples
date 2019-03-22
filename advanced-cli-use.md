# Snyk CLI Advanced Use

## Alternate ways of running the Snyk CLI
For npm projects, an alternate way of using the Snyk CLI is to add the `snyk` module to your package.json by running
```npm install snyk```
Then you can run the CLI using either (running a `snyk test` in these examples):
```
node node_modules/snyk/dist/cli/index.js test
```
or
```
./node_modules/.bin/snyk test
```

One advantage of doing this is that you'd always have the latest version of the Snyk CLI by running `npm install`.

## snyk test for all build files
Running `snyk test` in an application directory will test only the first build/manifest file found. For example, if you hava a Java + Maven project with a pom.xml file in the root and several modules, each with their own pom.xml, you would typically have to run `snyk test` for each one.

Say for example you have a Java + Maven project with three pom.xml files - one root and two modules:
```
./pom.xml
./module1/pom.xml
./module2/pom.xml
```

Then you can test all of them by running these three separate commands:
```
snyk test
snyk test module1
snyk test module2
```

You can also group them into a single command like this:
```
snyk test . module1 module2
```

In other words, you can specify each directory you want to test.

Another option would be to do something like this - which finds all the pom.xml files for you:
```
snyk test $(find . -name "pom.xml" -exec dirname {} \;)
```

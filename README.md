# Regex101 plugin for IntelliJ-based IDEs

This plugin adds an intention to open regular expressions on regex101.com

[![Plugin repository](https://img.shields.io/jetbrains/plugin/v/14461-open-regex101?label=version&style=flat-square)](https://plugins.jetbrains.com/plugin/14461-open-regex101)
[![Plugin downloads](https://img.shields.io/jetbrains/plugin/d/14461-open-regex101?style=flat-square)](https://plugins.jetbrains.com/plugin/14461-open-regex101)
[![Plugin stars](https://img.shields.io/jetbrains/plugin/r/stars/14461-open-regex101?style=flat-square)](https://plugins.jetbrains.com/plugin/14461-open-regex101)
[![Build status](https://img.shields.io/github/workflow/status/aesy/regex101-intellij/Continous%20Integration?style=flat-square)](https://github.com/aesy/regex101-intellij/actions)
[![Test coverage](https://img.shields.io/codecov/c/github/aesy/regex101-intellij?style=flat-square)](https://codecov.io/github/aesy/regex101-intellij)
[![MIT license](https://img.shields.io/github/license/aesy/regex101-intellij.svg?style=flat-square)](https://github.com/aesy/regex101-intellij/blob/master/LICENSE)

## Installation

Download and install the plugin through your IDE or through [the marketplace](https://plugins.jetbrains.com/plugin/14461-open-regex101):

![Marketplace](./img/installation.png)

## Usage

Show context actions on a regular expression and pick "Open RegExp on regex101.com": 

![Context actions](./img/usage.png)

## Development

#### Prerequisites

* [Gradle 5.2+](https://gradle.org/)
* [A Java 8+ Runtime](https://adoptopenjdk.net/)

#### Build

To compile and package the plugin, simply issue the following command:

```sh
$ ./gradlew buildPlugin
```

This will create a zip located in `build/distributions/`.

#### Test

Run the tests as you would in any other gradle project:

```sh
$ ./gradlew test
```

#### Run

To run the plugin from the command line, the following command can be used:

```sh
$ ./gradlew runIde
```

This will start IntelliJ Ultimate with all necessary plugins loaded. Logs are located at 
`build/idea-sandbox/system/log/idea.log`.

## Contribute
Use the [issue tracker](https://github.com/aesy/regex101-intellij/issues) to report bugs or make feature requests. 

## License
MIT, see [LICENSE](/LICENSE) file.

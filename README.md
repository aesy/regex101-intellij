# Regex101 plugin for IntelliJ-based IDEs

This plugin adds an intention to open regular expressions on regex101.com

[![Plugin repository](https://img.shields.io/jetbrains/plugin/v/14461-open-regex101?label=plugin%20repository&style=flat-square)](https://plugins.jetbrains.com/plugin/14461-open-regex101)
[![MIT license](https://img.shields.io/github/license/aesy/regex101-intellij.svg?style=flat-square)](https://github.com/aesy/regex101-intellij/blob/master/LICENSE)

## Development

#### Prerequisites

* [Gradle 5.2+](https://gradle.org/)
* [A Java 8+ Runtime](https://adoptopenjdk.net/)

#### Build

To compile and package the plugin, simply issue the following command:

$ `gradle buildPlugin`

This will create a zip located in `build/distributions/`.

#### Run

To run the plugin from the command line, the following command can be used:

$ `gradle runIde`

This will start IntelliJ Ultimate with all necessary plugins loaded. Logs are located at 
`build/idea-sandbox/system/log/idea.log`.

## Contribute
Use the [issue tracker](https://github.com/aesy/regex101-intellij/issues) to report bugs or make feature requests. 

## License
MIT, see [LICENSE](/LICENSE) file.

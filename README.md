# gradle-beaver-plugin

## Status

Beta

## Overview

A Gradle plugin for [beaver](http://beaver.sf.net), a LALR parser generator.

## Usage

First, install the plugin to your local repository using `gradle install`.
(I'll get this puppy up on Maven Central sometime soon.)

Then:

    apply plugin: 'java'
    apply plugin: 'beaver'

    buildscript {
        repositories {
            mavenLocal()
        }

        dependencies {
            classpath 'co.tomlee.gradle.plugins:gradle-beaver-plugin:0.0.1'
        }
    }

    dependencies {
        beaver 'net.sf.beaver:beaver-cc:0.9.11'
    }

Once everything's wired up, Gradle will look for your Beaver grammars in
`src/main/beaver/*.g`.

## License

Apache 2.0

## Support

Please log defects and feature requests using the issue tracker on [github](http://github.com/thomaslee/gradle-beaver-plugin/issues).

## About

gradle-beaver-plugin was written by [Tom Lee](http://tomlee.co).

Follow me on [Twitter](http://www.twitter.com/tglee) or
[LinkedIn](http://au.linkedin.com/pub/thomas-lee/2/386/629).



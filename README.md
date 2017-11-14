# Yildiz-Engine feature-mission-shared

This is the official repository of the feature mission shared library, part of the Yildiz-Engine project.
The feature mission library library contains the common code(client and server) to manage the missions.

## Features

* Missions.
* Sub missions.
* Rewards
* ...

## Requirements

To build this module, you will need a java 8 JDK and Maven 3.

## Coding Style and other information

Project website:
http://www.yildiz-games.be

Issue tracker:
https://yildiz.atlassian.net/browse/YFMS

Wiki:
https://yildiz.atlassian.net/wiki

Quality report:
https://sonarcloud.io/dashboard?id=be.yildiz-games%3Afeature-mission-shared

## License

All source code files are licensed under the permissive MIT license
(http://opensource.org/licenses/MIT) unless marked differently in a particular folder/file.

## Build instructions

Go to your root directory, where you POM file is located.

Then invoke maven

	mvn clean install

This will compile the source code, then run the unit tests, and finally build a jar file.

## Usage

In your maven project, add the dependency

```xml
<dependency>
    <groupId>be.yildiz-games</groupId>
    <artifactId>feature-mission-shared</artifactId>
    <version>LATEST</version>
</dependency>
```
Replace LATEST with the correct version.

## Contact
Owner of this repository: Grégory Van den Borre
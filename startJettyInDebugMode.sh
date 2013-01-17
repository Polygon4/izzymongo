#!/bin/sh
export MAVEN_OPTS='-Xdebug -Xrunjdwp:transport=dt_socket,address=5005,server=y,suspend=n'

mvn -Pjetty
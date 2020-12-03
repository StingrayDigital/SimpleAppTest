#!/bin/sh
./gradlew clean createJar
java -jar build/libs/SimpleAppTest-*.jar --start-bluetooth-daemon

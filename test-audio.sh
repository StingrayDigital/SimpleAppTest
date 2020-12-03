#!/bin/sh
./gradlew clean createJar
java -jar build/libs/SimpleAppTest-*.jar --play  $(pwd)/sample/sample.ogg

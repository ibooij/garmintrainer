#!/bin/sh

echo installing substance-lite 5.3 into local maven repository.
mvn install:install-file -DgroupId=org.jvnet -DartifactId=substance-lite -Dversion=5.3 -Dpackaging=jar -Dfile=extra_libs/substance-lite.jar


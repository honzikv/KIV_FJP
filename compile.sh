#!/bin/bash
mvn clean install
cp ".\target\FJPSem-1.0-SNAPSHOT-jar-with-dependencies.jar" "fjp-sem-runnable.jar"

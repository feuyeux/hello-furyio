#!/usr/bin/env bash
mvn clean package
mvn exec:java -Dexec.mainClass="org.feuyeux.io.HelloFory"

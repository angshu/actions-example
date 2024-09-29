#!/bin/bash
export JAVA_HOME=$(jenv javahome)
echo $JAVA_HOME
./mvnw clean install -DskipTests
docker build -t angshu/customer-actions .
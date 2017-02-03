#!/bin/sh

export JAVA_HOME=/apps/java/home/SAF
export PATH=$JAVA_HOME/bin:$PATH

MY_CLASSPATH=$HOME/javaSource/xml2pdf/
for file in  $(ls ./lib/*.jar);
do
  MY_CLASSPATH+=":$file"
done

export CLASSPATH=$MY_CLASSPATH:$CLASSPATH
echo $CLASSPATH

javac ConvertXMLtoPDF.java

java ConvertXMLtoPDF

 


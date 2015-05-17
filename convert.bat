@echo off

rem if you want a special java version
set JAVA_HOME=%ProgramFiles%\java\jre7

set path=.;%JAVA_HOME%\bin
	
java -jar hqm_convert.jar

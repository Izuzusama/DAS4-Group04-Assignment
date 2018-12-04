mkdir bin
javac -classpath ../lib/detect-object.jar:../lib/xchart-3.5.2.jar -d bin *.java
if %errorlevel% neq 0 exit /b %errorlevel%
copy manifest.txt bin
cd bin
jar cvfm server.jar manifest.txt *.class
cd ../
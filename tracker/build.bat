mkdir bin
javac -d bin *.java
if %errorlevel% neq 0 exit /b %errorlevel%
copy manifest.txt bin
cd bin
jar cvfm tracker.jar manifest.txt *.class
cd ../
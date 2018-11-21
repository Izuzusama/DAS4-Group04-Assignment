mkdir bin
javac -d bin *.java
copy manifest.txt bin
cd bin
jar cvfm server.jar manifest.txt *.class
cd ../
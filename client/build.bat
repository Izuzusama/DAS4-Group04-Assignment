mkdir bin
javac -d bin *.java
copy manifest.txt bin
cd bin
jar cvfm client.jar manifest.txt *.class
cd ../
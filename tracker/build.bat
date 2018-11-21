mkdir bin
javac -d bin *.java
copy manifest.txt bin
cd bin
jar cvfm tracker.jar manifest.txt *.class
cd ../
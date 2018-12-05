mkdir -p bin
javac -d bin *.java
cp manifest.txt bin
cd bin
jar cvfm tracker.jar manifest.txt *.class
cd ../
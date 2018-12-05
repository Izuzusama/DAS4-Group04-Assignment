mkdir -p bin
javac -classpath '../../lib/detect-object.jar:../../lib/xchart-3.5.2.jar' -d bin *.java
cp manifest.txt bin
cd bin
jar cvfm server.jar manifest.txt *.class
cd ../
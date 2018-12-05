set -e
mkdir -p bin
mkdir -p bin/tracker
mkdir -p bin/server
mkdir -p bin/client
cd src/tracker
./build.sh
cp bin/tracker.jar ../../bin/tracker/
cd ../
cd server
./build.sh
cp bin/server.jar ../../bin/server/
cd ../
cd client
./build.sh
cp bin/client.jar ../../bin/client/
cd ../../

# Copy Lib files
cp lib/*.jar bin/server/
cp tools/ffmpeg bin/server/
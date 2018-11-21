mkdir bin
cd tracker
build.bat
copy bin\tracker.jar ..\bin
cd ../
cd server
build.bat
copy bin\server.jar ..\bin
cd client
build.bat
copy bin\client.jar ..\bin
cd ..

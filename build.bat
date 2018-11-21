mkdir bin
cd tracker
call build.bat
copy bin\tracker.jar ..\bin
cd ../
cd server
call build.bat
copy bin\server.jar ..\bin
cd client
call build.bat
copy bin\client.jar ..\bin
cd ..

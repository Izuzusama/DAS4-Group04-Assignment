mkdir bin
cd tracker
call build.bat
if %errorlevel% neq 0 exit /b %errorlevel%
copy bin\tracker.jar ..\bin
cd ../
cd server
call build.bat
if %errorlevel% neq 0 exit /b %errorlevel%
copy bin\server.jar ..\bin
cd ../
cd client
call build.bat
if %errorlevel% neq 0 exit /b %errorlevel%
copy bin\client.jar ..\bin
cd ..

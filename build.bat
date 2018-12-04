mkdir bin
mkdir bin\tracker
mkdir bin\server
mkdir bin\client
cd src\tracker
call build.bat
if %errorlevel% neq 0 exit /b %errorlevel%
copy bin\tracker.jar ..\..\bin\tracker\
cd ..\
cd server
call build.bat
if %errorlevel% neq 0 exit /b %errorlevel%
copy bin\server.jar ..\..\bin\server\
cd ..\
cd client
call build.bat
if %errorlevel% neq 0 exit /b %errorlevel%
copy bin\client.jar ..\..\bin\client\
cd ..\..\

REM Copy Lib files
copy /Y lib\* bin\server\
copy /Y tools\ffmpeg.exe bin\server\
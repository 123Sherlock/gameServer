@echo off

::.proto文件路径，最后不要跟“\”符号
set PROTO_DIR=%~dp0
::Java文件生成路径，最后不要跟“\”符号
set JAVA_PATH=%~dp0\..\src\main\java
::protoc.exe路径
set PROTOC_PATH=%~dp0\protoc\bin\protoc.exe

::遍历所有.proto文件
for /f "delims=" %%i in ('dir /b "%PROTO_DIR%\*.proto"') do (
    echo %%i
    %PROTOC_PATH% --proto_path=%PROTO_DIR% --java_out=%JAVA_PATH% %%i
)

echo finish

pause

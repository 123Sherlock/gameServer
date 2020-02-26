@echo off

::.proto文件路径，最后不要跟“\”符号
set PROTO_DIR=E:\Document\Java\project\server01\proto
::Java文件生成路径，最后不要跟“\”符号
set JAVA_PATH=E:\Document\Java\project\server01\src\main\java
::protoc.exe路径
set PROTOC_PATH=E:\Document\Java\project\server01\proto\protoc\bin\protoc.exe

::遍历所有.proto文件
for /f "delims=" %%i in ('dir /b "%PROTO_DIR%\*.proto"') do (
    echo %%i
    %PROTOC_PATH% --proto_path=%PROTO_DIR% --java_out=%JAVA_PATH% %%i
)

echo finish

pause

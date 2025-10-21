@ECHO OFF
setlocal ENABLEDELAYEDEXPANSION

REM ----------------------------------------------------------------------------
REM Maven Wrapper Startup Script (Windows)
REM ----------------------------------------------------------------------------

set DIR=%~dp0
if "%DIR%"=="" set DIR=.

set WRAPPER_JAR=%DIR%\.mvn\wrapper\maven-wrapper.jar
set WRAPPER_PROPERTIES=%DIR%\.mvn\wrapper\maven-wrapper.properties
set WRAPPER_LAUNCHER=org.apache.maven.wrapper.MavenWrapperMain

REM Determine Java executable
if not "x%JAVA_HOME%"=="x" (
  set JAVA_EXE=%JAVA_HOME%\bin\java.exe
  if exist "%JAVA_EXE%" goto javaOk
)
set JAVA_EXE=java
:javaOk

REM Ensure wrapper JAR exists (download if missing)
if exist "%WRAPPER_JAR%" goto run

for /f "usebackq tokens=1,2 delims==" %%A in ("%WRAPPER_PROPERTIES%") do (
  if "%%A"=="wrapperUrl" set DOWNLOAD_URL=%%B
)
if "x%DOWNLOAD_URL%"=="x" set DOWNLOAD_URL=https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar

echo Downloading Maven Wrapper jar from %DOWNLOAD_URL%

powershell -NoLogo -NoProfile -Command "[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; $ProgressPreference='SilentlyContinue'; Invoke-WebRequest -Uri '%DOWNLOAD_URL%' -OutFile '%WRAPPER_JAR%'" >NUL 2>&1
if exist "%WRAPPER_JAR%" goto run

echo Failed to download Maven Wrapper jar from %DOWNLOAD_URL%
exit /b 1

:run
REM Resolve project base dir
set MAVEN_PROJECTBASEDIR=%DIR%

"%JAVA_EXE%" -Dmaven.multiModuleProjectDirectory="%MAVEN_PROJECTBASEDIR%" -cp "%WRAPPER_JAR%" %WRAPPER_LAUNCHER% %*
endlocal

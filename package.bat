@echo off
setlocal enabledelayedexpansion
title DevFlow - Build & Package WAR

echo ================================================================
echo             DevFlow WAR Build & Packaging Suite
echo ================================================================
echo.

set MAVEN_CMD=mvn
where mvn >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    if exist "C:\Program Files\Apache NetBeans\java\maven\bin\mvn.cmd" (
        set "MAVEN_CMD=C:\Program Files\Apache NetBeans\java\maven\bin\mvn.cmd"
    ) else if exist "C:\Program Files\apache-maven\bin\mvn.cmd" (
        set "MAVEN_CMD=C:\Program Files\apache-maven\bin\mvn.cmd"
    )
)

echo [*] Compiling and packaging DevFlow.war...
"!MAVEN_CMD!" clean package -DskipTests

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ================================================================
    echo [SUCCESS] Package created at: target\DevFlow.war
    echo You can deploy this WAR to any Apache Tomcat 9 / 10 server.
    echo ================================================================
) else (
    echo.
    echo [!] Build failed. Please check compiler logs above.
)

pause

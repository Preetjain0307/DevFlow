@echo off
setlocal enabledelayedexpansion
title DevFlow Web Application Server

echo ================================================================
echo           DEVFLOW: Developer Collaboration Platform
echo ================================================================
echo.

:: Detect Maven Executable
set MAVEN_CMD=mvn
where mvn >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    if exist "C:\Program Files\Apache NetBeans\java\maven\bin\mvn.cmd" (
        set "MAVEN_CMD=C:\Program Files\Apache NetBeans\java\maven\bin\mvn.cmd"
    ) else if exist "C:\Program Files\apache-maven\bin\mvn.cmd" (
        set "MAVEN_CMD=C:\Program Files\apache-maven\bin\mvn.cmd"
    ) else (
        echo [!] WARNING: Maven not found in PATH or standard directories.
        echo Please ensure Apache Maven is installed and added to your PATH.
    )
)

echo [*] Using Maven: !MAVEN_CMD!
echo [*] Platform URL: http://localhost:8080/DevFlow
echo [*] Universal 1-Click Demo: http://localhost:8080/DevFlow/demo-login?role=demo
echo.
echo [*] Starting Embedded Tomcat Server (Port 8080)...
echo ================================================================

set MAVEN_OPTS=-Xms256m -Xmx768m -XX:ReservedCodeCacheSize=128m
"!MAVEN_CMD!" tomcat7:run

pause

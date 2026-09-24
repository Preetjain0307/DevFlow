@echo off
setlocal enabledelayedexpansion
title DevFlow - Automated Database Setup

echo ================================================================
echo           DevFlow Database Initialization ^& Seeding
echo ================================================================
echo.

set DB_USER=root
set DB_NAME=devflow_db
set /p DB_PASS=Enter MySQL root password (press Enter for default 'root'): 
if "%DB_PASS%"=="" set DB_PASS=root

echo.
echo [*] Testing connection and importing schema.sql...
mysql -u %DB_USER% -p%DB_PASS% < database\schema.sql
if %ERRORLEVEL% NEQ 0 (
    echo [!] ERROR: Failed to execute database\schema.sql.
    echo Please ensure MySQL Server is running on port 3306 and password is correct.
    goto :end
)
echo [OK] Schema imported successfully (26 tables created).

echo.
echo [*] Importing comprehensive sample dataset (users, projects, tasks, bugs, meetings, proposals)...
mysql -u %DB_USER% -p%DB_PASS% < database\sample_data.sql
if %ERRORLEVEL% NEQ 0 (
    echo [!] ERROR: Failed to execute database\sample_data.sql.
    goto :end
)
echo [OK] Sample dataset seeded successfully!

echo.
echo ================================================================
echo        Database Setup Complete! DevFlow is Ready to Run!
echo ================================================================
echo Default Accounts:
echo  - Universal Demo User : demo (1-Click Instant Login)
echo  - System Administrator: admin / password123
echo  - Project Manager     : pm_sarah / password123
echo  - Lead Developer      : dev_alex / password123
echo  - QA Tester           : tester_mark / password123
echo  - Faculty Mentor      : faculty_dr_alan / password123
echo ================================================================
echo.

:end
pause

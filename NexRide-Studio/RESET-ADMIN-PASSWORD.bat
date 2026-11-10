@echo off
cd /d "%~dp0"
powershell.exe -NoProfile -ExecutionPolicy Bypass -File "%~dp0RESET-ADMIN-PASSWORD.ps1"
if errorlevel 1 pause

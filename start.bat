@echo off
echo ========================================================
echo    Starting CricPulse Cricket Score Management System   
echo ========================================================
echo.

cd /d "%~dp0backend"
echo [1/2] Starting Spring Boot Backend (Port 8080)...
start "CricPulse Backend" java -jar target\cricket-score-service-1.0.0.jar

cd /d "%~dp0frontend"
echo [2/2] Starting Vite Frontend Server (Port 5173)...
start "CricPulse Frontend" cmd /k "npm run dev"

echo.
echo ========================================================
echo  All services started!
echo  Opening http://localhost:5173 in browser...
echo ========================================================
timeout /t 3 /nobreak >nul
start http://localhost:5173

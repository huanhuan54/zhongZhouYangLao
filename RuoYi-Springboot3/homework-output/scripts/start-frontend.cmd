@echo off
set "ROOT=C:\Users\青峰不语\Desktop\黑马实训\zzyl-springboot\RuoYi-Springboot3"
set "NODE_HOME=F:\zzyl-env\tools\node-v20.17.0-win-x64"
set "PATH=%NODE_HOME%;%PATH%"
cd /d "%ROOT%\ruoyi-ui"
if not exist "%ROOT%\homework-output\logs" mkdir "%ROOT%\homework-output\logs"
"%NODE_HOME%\npm.cmd" run dev > "%ROOT%\homework-output\logs\frontend-dev.log" 2> "%ROOT%\homework-output\logs\frontend-dev.err.log"

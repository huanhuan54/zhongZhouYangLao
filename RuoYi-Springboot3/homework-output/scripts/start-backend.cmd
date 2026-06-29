@echo off
set "ROOT=C:\Users\青峰不语\Desktop\黑马实训\zzyl-springboot\RuoYi-Springboot3"
set "JAVA_HOME=F:\zzyl-env\tools\jdk-17.0.19+10"
cd /d "%ROOT%"
if not exist "%ROOT%\homework-output\logs" mkdir "%ROOT%\homework-output\logs"
"%JAVA_HOME%\bin\java.exe" -jar "%ROOT%\ruoyi-admin\target\ruoyi-admin.jar" > "%ROOT%\homework-output\logs\backend-start.log" 2> "%ROOT%\homework-output\logs\backend-start.err.log"

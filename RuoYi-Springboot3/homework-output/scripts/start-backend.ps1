$Root = (Resolve-Path (Join-Path $PSScriptRoot '..\..')).Path
$Java = 'F:\zzyl-env\tools\jdk-17.0.19+10\bin\java.exe'
$Jar = Join-Path $Root 'ruoyi-admin\target\ruoyi-admin.jar'
$LogDir = Join-Path $Root 'homework-output\logs'
New-Item -ItemType Directory -Force -Path $LogDir | Out-Null
Set-Location $Root
& $Java -jar $Jar *> (Join-Path $LogDir 'backend-start.log')

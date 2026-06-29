$Root = (Resolve-Path (Join-Path $PSScriptRoot '..\..')).Path
$NodeHome = 'F:\zzyl-env\tools\node-v20.17.0-win-x64'
$Npm = Join-Path $NodeHome 'npm.cmd'
$LogDir = Join-Path $Root 'homework-output\logs'
New-Item -ItemType Directory -Force -Path $LogDir | Out-Null
$env:Path = "$NodeHome;$env:Path"
Set-Location (Join-Path $Root 'ruoyi-ui')
& $Npm run dev *> (Join-Path $LogDir 'frontend-dev.log')

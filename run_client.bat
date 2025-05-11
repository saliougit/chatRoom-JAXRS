@echo off
echo Verification du serveur Tomcat...
powershell -Command "$success = $false; for ($i = 0; $i -lt 10; $i++) { $result = Test-NetConnection localhost -Port 8080 -WarningAction SilentlyContinue; if ($result.TcpTestSucceeded) { $success = $true; break; }; Write-Host 'Attente du serveur...'; Start-Sleep -s 2 }; if ($success) { exit 0 } else { exit 1 }"
if errorlevel 1 (
    echo Le serveur Tomcat n'est pas accessible
    exit /b 1
)

echo Verification des dependances...
if not exist "target\classes" (
    echo Les classes n'ont pas ete compilees. Execution de 'mvn compile'...
    call mvn compile
)

if not exist "target\dependency" (
    echo Les dependances ne sont pas presentes. Execution de 'mvn dependency:copy-dependencies'...
    call mvn dependency:copy-dependencies
)

echo Demarrage du client...
set "CLASSPATH=target\classes;target\dependency\*"
java -cp "%CLASSPATH%" client.ChatClientGUI

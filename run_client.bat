@echo off
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

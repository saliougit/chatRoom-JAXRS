@echo off
echo Verification de Maven...
where mvn >nul 2>&1
if %ERRORLEVEL% neq 0 (
    echo Maven n'est pas installe ou n'est pas dans le PATH
    exit /b 1
)

echo Compilation du projet...
call mvn clean package

echo Verification du WAR...
if not exist "target\chatroom-jaxrs.war" (
    echo Le fichier WAR n'a pas ete genere correctement
    exit /b 1
)

set "CATALINA_HOME=C:/Users/Dell 5540/tomcat"

echo Deploiement du WAR dans Tomcat...
copy /Y "target\chatroom-jaxrs.war" "%CATALINA_HOME%\webapps\chatroom.war"

echo Demarrage de Tomcat...
call "%CATALINA_HOME%\bin\startup.bat"

echo Tomcat demarre! Le serveur sera accessible a http://localhost:8080/chatroom
echo Pour arreter le serveur, utilisez "%CATALINA_HOME%\bin\shutdown.bat"

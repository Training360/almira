@echo off

REM A bat fájl feladata, a MySQL elindítása, ha kell
REM A séma létrehozása

chcp 65001 > nul

set MYSQL_HOME=C:\xampp\mysql\bin

tasklist | find "mysqld" > nul

REM echo Errorlevel erteke: %errorlevel%

REM Itt vizsgáljuk, hogy fut-e a MySQL

if %errorlevel% == 0 (
	echo MySQL már fut
) else (
	echo MySQL nem fut, indítsd el!
	exit
)

REM Itt hozzuk létre a sémát
echo Séma és felhasználók létrehozása
%MYSQL_HOME%\mysql -u root < create-schema.sql

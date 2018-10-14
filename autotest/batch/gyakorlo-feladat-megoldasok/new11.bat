@echo off
chcp 65001 > nul
if "%1" == "" (
echo Adj meg paramétert
	) else (
		echo A paraméter értéke: %1
)

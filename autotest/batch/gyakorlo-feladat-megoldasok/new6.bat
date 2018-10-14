@echo off
chcp 65001 > nul
if exist %1 (
start %1
	) else (
		echo Nem található
)


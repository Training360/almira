@echo off

REM Megpróbálja kiírni a paraméterként kapott fájlt
REM Ha nem sikerül, kiírja az a.txt fájlt
REM Ha nem sikerül, kiírja, hogy ez sem létezik

chcp 65001 > nul

set DEFAULT_FILE=a.txt

if exist %1 ( type %1
) else (
   echo Megpróbálom az alapértelmezett %DEFAULT_FILE% fájlt kiírni!
   if exist %DEFAULT_FILE% ( type %DEFAULT_FILE%
   ) else (
      echo Nem találtam fájlt!
   )
)
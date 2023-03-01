@echo off
REM This should clean and rebuild the program
REM This requires TASM.EXE and TLINK.EXE to be in classpath


cd build
del *.*
tasm ..\src\main.asm
tlink /t main.obj
main.com
cd ..

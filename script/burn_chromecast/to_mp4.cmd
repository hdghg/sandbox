@echo on

REM /b - plain out, /s - full paths /a-d files only /on sorted alpha asc
md mp4
for /f "tokens=*" %%a in ('
    dir /b
    /s
    /a-d
    /on
    *.mkv
') do (
    @echo "in: %%a out: mp4/%%~na subs: %%~na%%~xa"
    c:\soft\ffmpeg\bin\ffmpeg.exe -i "%%a" ^
        -vf subtitles="%%~na%%~xa":si=2 ^
        -profile:v baseline -level:v 4.1 ^
        -map 0:0 -map 0:2 "mp4/%%~na.mp4"
)
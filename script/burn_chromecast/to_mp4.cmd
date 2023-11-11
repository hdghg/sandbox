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
        -preset fast -c:a aac -b:a 192k -ac 2 ^
        -c:v libx264 ^
        -profile:v high -level:v 4.1 ^
        -pix_fmt yuv420p ^
        -map_metadata -1 ^
        -map 0:0 -map 0:2 "../dlna/bb1/%%~na.mp4"
)
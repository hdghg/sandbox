#!/bin/sh
./clean.sh
mkdir build
#yasm -f bin -o build/main.bin main.asm
nasm -f bin -o build/main.bin main.asm
dd if=/dev/zero of=build/floppy.img bs=1024 count=1440
dd if=build/main.bin of=build/floppy.img conv=notrunc
echo "Built artifact: build/floppy.img"


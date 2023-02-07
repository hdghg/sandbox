#!/bin/sh
./build.sh
qemu-system-i386 -fda build/floppy.img -boot a


#!/bin/sh
for i in `seq 1 1000`
do
  shuf -i 0-65536 -n1
done
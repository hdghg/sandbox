#!/bin/sh
printf ":: Insertion Sort ::\n"
for i in $(ls -1qd ../insertionsort/cases/*/)
do
  printf "Running test $i\n"

  ./insertionsort/insertionsort < $i/input.txt > sol.txt
  diff -Z sol.txt $i/output.txt > /dev/null
  if test $? -gt 0
  then
    echo Test Failed
    diff -Zy sol.txt $i/output.txt
  else
    echo Test success!
  fi

done

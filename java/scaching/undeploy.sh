#!/bin/bash

FILES="./*.id"
for id in $FILES;
do
  if [ -f "$id" ]
  then
    echo "Processing $id file..."
    docker stop $(cat $id)
    rm $id
  fi
done

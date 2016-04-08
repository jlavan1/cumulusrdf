#!/bin/bash

for x in *.owl; do
  sed -i -e 1,11d "$x"
done

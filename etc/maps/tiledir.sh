#!/bin/sh
for file in raster/*.png
do
	./tile.py "$file"
done

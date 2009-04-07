#!/usr/bin/python
import Image, sys, os

try:
	inputFile = sys.argv[1]
except IndexError:
	print "Usage: tile.py fileToSlice.png"
	sys.exit(2)

outputFolder = os.path.basename(inputFile) + "_tiles/"
outputFiletype = ".png"

tileWidth = 160
tileHeight = 120

im = Image.open(inputFile)

tilesNumX = im.size[0] / tileWidth
tilesNumY = im.size[1] / tileHeight
topLeftOffsetX = im.size[0] % tileWidth / 2
topLeftOffsetY = im.size[1] % tileHeight / 2

print "tiles x       = ", tilesNumX
print "tiles y       = ", tilesNumY
print "offset left   = ", topLeftOffsetX
print "offset right  = ", im.size[0] - tilesNumX * tileWidth - topLeftOffsetX
print "offset top    = ", topLeftOffsetY
print "offset bottom = ", im.size[1] - tilesNumY * tileHeight - topLeftOffsetY

if not os.path.exists(outputFolder):
                    os.makedirs(outputFolder)

for tileY in range(tilesNumY):
	for tileX in range(tilesNumX):
		offsetX = tileX * tileWidth + topLeftOffsetX
		offsetY = (tilesNumY - tileY - 1) * tileHeight + topLeftOffsetY
		box = (offsetX, offsetY, offsetX + tileWidth, offsetY + tileHeight)
		tile = im.crop(box)
		tileName = "%s%02d-%02d%s" % (outputFolder, tileY, tileX, outputFiletype)
		tile.save(tileName)


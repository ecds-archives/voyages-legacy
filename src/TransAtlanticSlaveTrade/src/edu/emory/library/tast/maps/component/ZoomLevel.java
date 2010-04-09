/*
Copyright 2010 Emory University
	
	    This file is part of Trans-Atlantic Slave Voyages.
	
	    Trans-Atlantic Slave Voyages is free software: you can redistribute it and/or modify
	    it under the terms of the GNU General Public License as published by
	    the Free Software Foundation, either version 3 of the License, or
	    (at your option) any later version.
	
	    Trans-Atlantic Slave Voyages is distributed in the hope that it will be useful,
	    but WITHOUT ANY WARRANTY; without even the implied warranty of
	    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	    GNU General Public License for more details.
	
	    You should have received a copy of the GNU General Public License
	    along with Trans-Atlantic Slave Voyages.  If not, see <http://www.gnu.org/licenses/>. 
*/
package edu.emory.library.tast.maps.component;

import java.io.Serializable;

public class ZoomLevel implements Serializable
{
	
	private static final long serialVersionUID = 5541108478267879512L;

	private int tileWidth;
	private int tileHeight;
	private double bottomLeftTileX;
	private double bottomLeftTileY;
	private int tilesNumX;
	private int tilesNumY;
	private double scale;
	private String tilesDir;
	
	public ZoomLevel(int tileWidth, int tileHeight, double bottomLeftTileX, double bottomLeftTileY, int tilesNumX, int tilesNumY, double scale, String tilesDir)
	{
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
		this.bottomLeftTileX = bottomLeftTileX;
		this.bottomLeftTileY = bottomLeftTileY;
		this.tilesNumX = tilesNumX;
		this.tilesNumY = tilesNumY;
		this.scale = scale;
		this.tilesDir = tilesDir;
	}

	public double getBottomLeftTileX()
	{
		return bottomLeftTileX;
	}
	
	public void setBottomLeftTile(double bottomLeftTileX, double bottomLeftTileY)
	{
		this.bottomLeftTileX = bottomLeftTileX;
		this.bottomLeftTileY = bottomLeftTileY;
	}

	public void setBottomLeftTileX(double bottomLeftTileX)
	{
		this.bottomLeftTileX = bottomLeftTileX;
	}
	
	public double getBottomLeftTileY()
	{
		return bottomLeftTileY;
	}
	
	public void setBottomLeftTileY(double bottomLeftTileY)
	{
		this.bottomLeftTileY = bottomLeftTileY;
	}
	
	public double getScale()
	{
		return scale;
	}
	
	public void setScale(double scale)
	{
		this.scale = scale;
	}
	
	public String getTilesDir()
	{
		return tilesDir;
	}
	
	public void setTilesDir(String tilesDir)
	{
		this.tilesDir = tilesDir;
	}
	
	public void setTilesNum(int tilesNumX, int tilesNumY)
	{
		this.tilesNumX = tilesNumX;
		this.tilesNumY = tilesNumY;
	}

	public int getTilesNumX()
	{
		return tilesNumX;
	}
	
	public void setTilesNumX(int tilesNumX)
	{
		this.tilesNumX = tilesNumX;
	}
	
	public int getTilesNumY()
	{
		return tilesNumY;
	}
	
	public void setTilesNumY(int tilesNumY)
	{
		this.tilesNumY = tilesNumY;
	}

	public void setTileSize(int tileWidth, int tileHeight)
	{
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
	}

	public int getTileHeight()
	{
		return tileHeight;
	}

	public void setTileHeight(int tileHeight)
	{
		this.tileHeight = tileHeight;
	}

	public int getTileWidth()
	{
		return tileWidth;
	}

	public void setTileWidth(int tileWidth)
	{
		this.tileWidth = tileWidth;
	}

}
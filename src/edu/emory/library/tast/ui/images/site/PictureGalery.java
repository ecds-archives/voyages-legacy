package edu.emory.library.tast.ui.images.site;

import java.util.Arrays;

import edu.emory.library.tast.dm.Image;

public class PictureGalery {
	private GaleryImage[] pictures;
	private GaleryImage visiblePicture;
	
	public PictureGalery(GaleryImage[] pictures) {
		this.pictures = pictures;
	}
	
	public boolean canMoveForward(int set, int number) {
		return set * number < this.pictures.length;
	}
	
	public boolean canMoveBackward(int set, int number) {
		return set > 1;
	}

	public GaleryImage[] getPictures(int set, int number) {
		int first = (set - 1) * number;
		int last = set * number;
		if (last > this.pictures.length) {
			last = this.pictures.length;
		}
		GaleryImage[] ret = new GaleryImage[last - first];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = this.pictures[first + i];
		}
		return ret;
	}

	public void setVisiblePicture(GaleryImage image) {
		this.visiblePicture = image;
	}
	
	public GaleryImage getVisiblePicture() {
		return this.visiblePicture;
	}

	public int getFirst(int set, int number) {
		return (set - 1) * number;
	}
	
	public int getNumberOfAll() {
		return this.pictures.length;
	}

	public int getLast(int set, int number) {
		int last = set * number;
		if (last < this.pictures.length) {			
			return last;
		} else {
			return this.pictures.length;
		}
	}
}

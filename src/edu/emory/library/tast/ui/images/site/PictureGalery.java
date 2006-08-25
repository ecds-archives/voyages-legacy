package edu.emory.library.tast.ui.images.site;

import java.util.Arrays;

import edu.emory.library.tast.dm.Image;

public class PictureGalery {
	private Image[] pictures;
	private int firstVisible;
	private Image visiblePicture;
	
	public PictureGalery(Image[] pictures) {
		this.pictures = pictures;
		this.firstVisible = 0;
	}
	
	public void moveForward(int number) {
		this.firstVisible += number;
	}
	
	public void moveBackward(int number) {
		this.firstVisible -= number;
		if (this.firstVisible < 0) {
			this.firstVisible = 0;
		}
	}
	
	public boolean canMoveForward(int number) {
		return this.firstVisible + number < this.pictures.length;
	}
	
	public boolean canMoveBackward(int number) {
		return this.firstVisible - number >= 0;
	}

	public Image[] getPictures(int number) {
		Image[] ret = new Image[number < this.pictures.length - this.firstVisible ? number : this.pictures.length - this.firstVisible];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = this.pictures[this.firstVisible + i];
		}
		return ret;
	}

	public void setPictures(Image[] pictures) {
		this.pictures = pictures;
	}

	public void setVisiblePicture(Image image) {
		this.visiblePicture = image;
	}
	
	public Image getVisiblePicture() {
		return this.visiblePicture;
	}
}

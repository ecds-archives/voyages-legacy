package edu.emory.library.tast.ui.images.site;

import edu.emory.library.tast.dm.Image;
import edu.emory.library.tast.ui.images.GalleryImage;

public class NewImagesBean {

	private GalleryImage[] galleryImages;
	
	public NewImagesBean() {
		Image[] images = Image.getImagesArray();
		galleryImages = new GalleryImage[images.length];
		for (int i = 0; i < galleryImages.length; i++) {
			galleryImages[i] = new GalleryImage(images[i].getFileName(), images[i].getTitle());
		}
	}
	
	
	public GalleryImage[] getImages() {
		return this.galleryImages;
	}
}

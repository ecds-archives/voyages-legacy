package edu.emory.library.tast.ui.images.site;

import java.util.Iterator;
import java.util.List;

import edu.emory.library.tast.dm.Category;
import edu.emory.library.tast.dm.Image;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.specific.SequenceAttribute;
import edu.emory.library.tast.ui.images.GalleryImage;
import edu.emory.library.tast.util.HibernateUtil;
import edu.emory.library.tast.util.query.Conditions;
import edu.emory.library.tast.util.query.QueryValue;

public class NewImagesBean {

	private GalleryImage[] galleryImages;
	
	public NewImagesBean() {
		
		Image[] images = Image.getImagesArray();
		galleryImages = new GalleryImage[images.length];
		for (int i = 0; i < galleryImages.length; i++) {
			galleryImages[i] = new GalleryImage(images[i].getId() + "", images[i].getFileName(), images[i].getTitle());
		}
	}
	
	
	private GalleryImage[] getSample(Category cat, int size) {
		Conditions cond = new Conditions();
		cond.addCondition(Image.getAttribute("category"), cat, Conditions.OP_EQUALS);
		QueryValue qValue = new QueryValue("Image", cond);
		qValue.setLimit(size);
		qValue.addPopulatedAttribute(Image.getAttribute("id"));
		qValue.addPopulatedAttribute(Image.getAttribute("fileName"));
		qValue.addPopulatedAttribute(Image.getAttribute("title"));
		Object[] images = (Object[])qValue.executeQuery();
		
		GalleryImage[] ret = new GalleryImage[size];
		for (int i = 0; i < images.length; i++) {
			Object[] row = (Object[])images[i];
			ret[i] = new GalleryImage(row[0].toString(), row[1].toString(), row[2].toString());
		}
		
		return ret;
	}
	
	public GalleryImage[] getImages() {
		return this.galleryImages;
	}
	
	public GalleryImage[] getSampleVessels() {
		return this.getSample(Category.loadById(HibernateUtil.getSession(), 1), 5);
	}
	
	public GalleryImage[] getSampleSlaves() {
		return this.getSample(Category.loadById(HibernateUtil.getSession(), 2), 5);
	}
	
	public GalleryImage[] getSampleSlavers() {
		return this.getSample(Category.loadById(HibernateUtil.getSession(), 3), 5);
	}
	
	public GalleryImage[] getSamplePorts() {
		return this.getSample(Category.loadById(HibernateUtil.getSession(), 4), 5);
	}
	
	public GalleryImage[] getSampleRegions() {
		return this.getSample(Category.loadById(HibernateUtil.getSession(), 5), 5);
	}
	
	public String seeVessels() {
		return null;
	}
}

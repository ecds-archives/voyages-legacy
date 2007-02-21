package edu.emory.library.tast.ui.images.site;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import edu.emory.library.tast.Languages;
import edu.emory.library.tast.dm.Category;
import edu.emory.library.tast.dm.Image;
import edu.emory.library.tast.ui.images.GalleryImage;
import edu.emory.library.tast.util.HibernateUtil;
import edu.emory.library.tast.util.StringUtils;
import edu.emory.library.tast.util.query.Conditions;
import edu.emory.library.tast.util.query.QueryValue;

public class NewImagesBean {

	private List categories = null;
	private GalleryImage[] galleryImages;
	private String imageLike;
	private String selectedCategory = "-1";
	private String from = "";
	private String to = "";
	private String imageId;
	
	public NewImagesBean() {
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
	
	public GalleryImage[] getQueryResponse() {
		return this.getGalleryImages();
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
		this.selectedCategory = "1";
		return "images-query";
	}

	public String seeSlaves() {
		this.selectedCategory = "2";
		return "images-query";
	}

	public String seeSlavers() {
		this.selectedCategory = "3";
		return "images-query";
	}
	
	public String seePorts() {
		this.selectedCategory = "4";
		return "images-query";
	}
	
	public String seeRegions() {
		this.selectedCategory = "5";
		return "images-query";
	}
	
	public String search() {
		if (this.selectedCategory.equals("-1")) {
			return null;
		}
		return "images-query";
	}
	
	public String detailRequested() {
		return "images-detail";
	}
	
	public GalleryImage[] getGalleryImages() {
		
		Conditions conds = new Conditions();
		if (this.imageLike != null && !this.imageLike.equals("")) {
			Conditions subCond = new Conditions(Conditions.JOIN_OR);
			subCond.addCondition(Image.getAttribute("title"), "%" + imageLike + "%", Conditions.OP_LIKE);
			subCond.addCondition(Image.getAttribute("description"), "%" + imageLike + "%", Conditions.OP_LIKE);
			conds.addCondition(subCond);
		}
		conds.addCondition(Image.getAttribute("category"), Category.loadById(HibernateUtil.getSession(), Long.parseLong(this.selectedCategory)), Conditions.OP_EQUALS);
		QueryValue qValue = new QueryValue("Image", conds);
		qValue.addPopulatedAttribute(Image.getAttribute("id"));
		qValue.addPopulatedAttribute(Image.getAttribute("fileName"));
		qValue.addPopulatedAttribute(Image.getAttribute("title"));
		qValue.addPopulatedAttribute(Image.getAttribute("date"));
		
		Object[] response = qValue.executeQuery();
		List galImagesList = new ArrayList();
		for (int i = 0; i < response.length; i++) {
			Object[] row = (Object[]) response[i];
			if (from.equals("") && to.equals("")) {
				galImagesList.add(new GalleryImage(row[0].toString(), row[1].toString(), row[2].toString()));
			} else if (row[3] != null && !row[3].equals("")) {
				boolean firstok = true;
				boolean secondok = true;
				int year = Integer.parseInt(row[3].toString());
				if (!from.equals("") && year < Integer.parseInt(from)) {
					firstok = false;
				}
				if (!to.equals("") && year > Integer.parseInt(to)) {
					secondok = false;
				}
				if (firstok && secondok) {
					galImagesList.add(new GalleryImage(row[0].toString(), row[1].toString(), row[2].toString()));
				}
			}
		}
		
		galleryImages = (GalleryImage[]) galImagesList.toArray(new GalleryImage[] {});
		
		return galleryImages;
	}


	public void setGalleryImages(GalleryImage[] galleryImages) {
		this.galleryImages = galleryImages;
	}


	public String getImageLike() {
		return imageLike;
	}


	public void setImageLike(String imageLike) {
		this.imageLike = imageLike;
	}


	public String getSelectedCategory() {
		return selectedCategory;
	}


	public void setSelectedCategory(String selectedCategory) {
		this.selectedCategory = selectedCategory;
	}
	
	public List getCategories() {
		if (categories == null) {
			categories = new ArrayList();
			categories.add(new SelectItem("-1", "Select category"));
			List cats = Category.loadAll(HibernateUtil.getSession(), "id");
			Iterator iter = cats.iterator();
			while (iter.hasNext()) {
				Category cat = (Category)iter.next();
				categories.add(new SelectItem(cat.getId() + "", cat.getName()));
			}
		}
		return categories;
	}


	public String getFrom() {
		return from;
	}


	public void setFrom(String from) {
		this.from = from;
	}


	public String getTo() {
		return to;
	}


	public void setTo(String to) {
		this.to = to;
	}


	public String getImageId() {
		return imageId;
	}


	public void setImageId(String imageId) {
		this.imageId = imageId;
	}
	
	public String getImageTitle()
	{
		Image img = Image.loadById(Integer.parseInt(this.imageId));
		return img.getTitle();
	}

	public String getImageDescription()
	{
		Image img = Image.loadById(Integer.parseInt(this.imageId));
		return img.getDescription();
	}
	
	public String getImageURL()
	{
		Image img = Image.loadById(Integer.parseInt(this.imageId));
		return "../images-database/" + img.getFileName();
	}

	public List getImageInfo()
	{
		
		List info = new ArrayList();
		
		Image img = Image.loadById(Integer.parseInt(this.imageId));
		
		if (!StringUtils.isNullOrEmpty(img.getDate()))
			info.add(new ImageInfo("Date:", img.getDate()));

		if (!StringUtils.isNullOrEmpty(img.getCreator()))
			info.add(new ImageInfo("Creator:", img.getCreator()));
			
		if (!StringUtils.isNullOrEmpty(img.getSource()))
			info.add(new ImageInfo("Source:", img.getSource()));

		if (!StringUtils.isNullOrEmpty(img.getLanguage()))
			info.add(new ImageInfo("Language:", img.getLanguageName()));

		return info;

	}

}
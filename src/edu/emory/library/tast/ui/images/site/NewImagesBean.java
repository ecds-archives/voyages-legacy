package edu.emory.library.tast.ui.images.site;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.emory.library.tast.TastResource;
import edu.emory.library.tast.dm.Image;
import edu.emory.library.tast.dm.ImageCategory;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.specific.FunctionAttribute;
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
	
	private GalleryImage[] getSample(ImageCategory cat, int size) {
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
	
	public GalleryImage[] getQueryResponse()
	{
		return this.getGalleryImages();
	}
	
	public String getQueryTitle()
	{
		
		StringBuffer title = new StringBuffer();
		
		String titleImages = TastResource.getText("images_query_showing");
		String titleContaining = TastResource.getText("images_query_title_containing");
		String titleFrom = TastResource.getText("images_query_title_from");
		String titleOriginated = TastResource.getText("images_query_title_originated");
		String titleBefore = TastResource.getText("images_query_title_before");
		String titleAfter = TastResource.getText("images_query_title_after");
		
		title.append(titleImages);
		
		if (imageLike != null)
		{
			String query = imageLike.trim();
			if (imageLike.length() != 0)
			{
				title.append(" ");
				title.append(titleContaining);
				title.append(" '");
				title.append(query);
				title.append("'");
			}
		}
		
		ImageCategory cat = ImageCategory.loadById(null, Integer.parseInt(selectedCategory));
		if (cat != null)
		{
			title.append(" ");
			title.append(titleFrom);
			title.append(" ");
			title.append(cat.getName());
		}
		
		int fromInt = 0;
		boolean fromOk;
		try
		{
			fromInt = Integer.parseInt(from);
			fromOk = true;
		}
		catch (NumberFormatException nfe)
		{
			fromOk = false;
		}
		
		int toInt = 0;
		boolean toOk;
		try
		{
			toInt = Integer.parseInt(to);
			toOk = true;
		}
		catch (NumberFormatException nfe)
		{
			toOk = false;
		}
		
		if (fromOk && toOk)
		{
			title.append(" ");
			title.append(titleOriginated);
			title.append(" ");
			title.append(fromInt);
			title.append(" - ");
			title.append(toInt);
		}
		else if (fromOk)
		{
			title.append(" ");
			title.append(titleOriginated);
			title.append(" ");
			title.append(titleAfter);
			title.append(" ");
			title.append(fromInt);
		}
		else if (toOk)
		{
			title.append(" ");
			title.append(titleOriginated);
			title.append(" ");
			title.append(titleBefore);
			title.append(" ");
			title.append(toInt);
		}

		return title.toString();

	}

	public GalleryImage[] getSampleVessels() {
		return this.getSample(ImageCategory.loadById(HibernateUtil.getSession(), 1), 5);
	}
	
	public GalleryImage[] getSampleSlaves() {
		return this.getSample(ImageCategory.loadById(HibernateUtil.getSession(), 2), 5);
	}
	
	public GalleryImage[] getSampleSlavers() {
		return this.getSample(ImageCategory.loadById(HibernateUtil.getSession(), 3), 5);
	}
	
	public GalleryImage[] getSamplePorts() {
		return this.getSample(ImageCategory.loadById(HibernateUtil.getSession(), 4), 5);
	}
	
	public GalleryImage[] getSampleRegions() {
		return this.getSample(ImageCategory.loadById(HibernateUtil.getSession(), 5), 5);
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
		return "images-query";
	}
	
	public String detailRequested() {
		return "images-detail";
	}
	
	public GalleryImage[] getGalleryImages()
	{
		
		Session sess = HibernateUtil.getSession();
		Transaction transaction = sess.beginTransaction();
		
		Conditions conds = new Conditions();
		
		String[] keywords = StringUtils.extractQueryKeywords(this.imageLike, true);
		if (keywords.length > 0)
		{
			Conditions keywordsSubCond = new Conditions(Conditions.JOIN_OR);
			Attribute titleUpperAttr = new FunctionAttribute("upper", new Attribute[] {Image.getAttribute("title")}); 
			Attribute descUpperAttr = new FunctionAttribute("upper", new Attribute[] {Image.getAttribute("description")}); 
			for (int i = 0; i < keywords.length; i++)
			{
				String keyword = "%" + keywords[i] + "%";
				keywordsSubCond.addCondition(titleUpperAttr, keyword, Conditions.OP_LIKE);
				keywordsSubCond.addCondition(descUpperAttr, keyword, Conditions.OP_LIKE);
			}
			conds.addCondition(keywordsSubCond);
		}
		
		ImageCategory cat = ImageCategory.loadById(sess, Long.parseLong(this.selectedCategory));
		if (cat != null) conds.addCondition(Image.getAttribute("category"), cat, Conditions.OP_EQUALS);
		
		QueryValue qValue = new QueryValue("Image", conds);
		qValue.addPopulatedAttribute(Image.getAttribute("id"));
		qValue.addPopulatedAttribute(Image.getAttribute("fileName"));
		qValue.addPopulatedAttribute(Image.getAttribute("title"));
		qValue.addPopulatedAttribute(Image.getAttribute("date"));
		
		Object[] response = qValue.executeQuery(sess);
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
		
		transaction.commit();
		sess.close();;
		
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
			List cats = ImageCategory.loadAll(HibernateUtil.getSession(), "id");
			Iterator iter = cats.iterator();
			while (iter.hasNext()) {
				ImageCategory cat = (ImageCategory)iter.next();
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
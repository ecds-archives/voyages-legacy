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
	//private String backAction = null;
	
	public NewImagesBean() {
	}
	
	private GalleryImage[] getSample(int catId, int size)
	{

		Session sess = HibernateUtil.getSession();
		Transaction transaction = sess.beginTransaction();
		
		ImageCategory cat = ImageCategory.loadById(sess, catId);
		
		Conditions cond = new Conditions();
		cond.addCondition(Image.getAttribute("category"), cat, Conditions.OP_EQUALS);
		QueryValue qValue = new QueryValue("Image", cond);
		
		qValue.setLimit(size);
		
		qValue.addPopulatedAttribute(Image.getAttribute("id"));
		qValue.addPopulatedAttribute(Image.getAttribute("fileName"));
		qValue.addPopulatedAttribute(Image.getAttribute("title"));
		
		qValue.setOrderBy(new Attribute[] {Image.getAttribute("order")});
		qValue.setOrder(QueryValue.ORDER_ASC);

		Object[] images = (Object[])qValue.executeQuery(sess);
		size = Math.min(size, images.length);
		GalleryImage[] ret = new GalleryImage[size];

		for (int i = 0; i < size; i++)
		{
			Object[] row = (Object[])images[i];
			GalleryImage img = new GalleryImage(row[0].toString(), row[1].toString(), row[2].toString());
			ret[i] = img;
		}
		
		transaction.commit();
		sess.close();

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
		return this.getSample(1, 5);
	}
	
	public GalleryImage[] getSampleSlaves() {
		return this.getSample(2, 5);
	}
	
	public GalleryImage[] getSampleSlavers() {
		return this.getSample(3, 5);
	}
	
	public GalleryImage[] getSamplePorts() {
		return this.getSample(4, 5);
	}
	
	public GalleryImage[] getSampleRegions() {
		return this.getSample(5, 5);
	}
	
	public GalleryImage[] getSamplePresentation() {
		return this.getSample(99, 5);
	}
	
	public String seeVessels() {
		this.selectedCategory = "1";
		//this.backAction = "images-main";
		return "images-query";
	}

	public String seeSlaves() {
		this.selectedCategory = "2";
		//this.backAction = "images-query";
		return "images-query";
	}

	public String seeSlavers() {
		this.selectedCategory = "3";
		//this.backAction = "images-query";
		return "images-query";
	}
	
	public String seePorts() {
		this.selectedCategory = "4";
		//this.backAction = "images-query";
		return "images-query";
	}
	
	public String seeRegions() {
		this.selectedCategory = "5";
		//this.backAction = "images-query";
		return "images-query";
	}
	
	public String seePresentation() {
		this.selectedCategory = "99";
		//this.backAction = "images-query";
		return "images-query";
	}

	public String search() {
		//this.backAction = "images-main";
		return "images-query";
	}
	
	public String detailRequested()
	{
		
		imageLike = "";
		selectedCategory = "";
		from = "";
		to = "";
		
		Session sess = HibernateUtil.getSession();
		Transaction transaction = sess.beginTransaction();
		
		Image img = Image.loadById(Integer.parseInt(imageId), sess);
		if (img != null)
		{
			ImageCategory cat = img.getCategory();
			if (cat != null)
			{
				selectedCategory = String.valueOf(cat.getId().intValue());
			}
		}
		
		transaction.commit();
		sess.close();
		
		getGalleryImages();
		
		return "images-detail";
		
	}
	
	public String next() {
		if (this.galleryImages != null) {
			for (int i = 0; i < this.galleryImages.length - 1; i++) {
				if (this.galleryImages[i].getId().equals(this.imageId)) {
					this.imageId = this.galleryImages[i + 1].getId();
					return null;
				}
			}
		}
		return null;
	}
	
	public String prev() {
		if (this.galleryImages != null) {
			for (int i = 1; i < this.galleryImages.length; i++) {
				if (this.galleryImages[i].getId().equals(this.imageId)) {
					this.imageId = this.galleryImages[i - 1].getId();
					return null;
				}
			}
		}
		return null;
	}
	
	public String back() {
		/*if ("images-query".equals(this.backAction)) {
			String ret = this.backAction;
			this.backAction = "images-main";
			return ret;
		}*/
		return "images-back";
	}
	
	public GalleryImage[] getGalleryImages()
	{
		
		Session sess = HibernateUtil.getSession();
		Transaction transaction = sess.beginTransaction();
		
		Conditions conds = new Conditions();
		
		String[] keywords = StringUtils.extractQueryKeywords(this.imageLike, true);
		if (keywords.length > 0)
		{
			Conditions keywordsSubCond = new Conditions(Conditions.JOIN_AND);
			Attribute titleUpperAttr = new FunctionAttribute("upper", new Attribute[] {Image.getAttribute("title")}); 
			Attribute descUpperAttr = new FunctionAttribute("upper", new Attribute[] {Image.getAttribute("description")}); 
			for (int i = 0; i < keywords.length; i++)
			{
				String keyword = "%" + keywords[i] + "%";
				Conditions keywordSubCond = new Conditions(Conditions.JOIN_OR);
				keywordSubCond.addCondition(titleUpperAttr, keyword, Conditions.OP_LIKE);
				keywordSubCond.addCondition(descUpperAttr, keyword, Conditions.OP_LIKE);
				keywordsSubCond.addCondition(keywordSubCond);
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
		
		qValue.setOrderBy(new Attribute[] {Image.getAttribute("order")});
		qValue.setOrder(QueryValue.ORDER_ASC);
		
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
			categories.add(new SelectItem("-1", TastResource.getText("images_all_categories")));
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

		if (!StringUtils.isNullOrEmpty(img.getLanguage(), true))
			info.add(new ImageInfo("Language:", img.getLanguageName()));

		return info;

	}

}
package edu.emory.library.tast.images.site;

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
import edu.emory.library.tast.images.GalleryImage;
import edu.emory.library.tast.util.HibernateUtil;
import edu.emory.library.tast.util.StringUtils;
import edu.emory.library.tast.util.query.Conditions;
import edu.emory.library.tast.util.query.QueryValue;

public class NewImagesBean {

	private static final int POPUP_EXTRA_HEIGHT = 50;
	private static final int POPUP_EXTRA_WIDTH = 30;
	private static final int DETAIL_IMAGE_WIDTH = 600;
	private List categories = null;
	private GalleryImage[] galleryImages;
	private String imageLike;
	private String selectedCategory = "-1";
	private Integer from = null;
	private Integer to = null;
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
		qValue.addPopulatedAttribute(Image.getAttribute("date"));
		
		qValue.setOrderBy(new Attribute[] {Image.getAttribute("date")});
		qValue.setOrder(QueryValue.ORDER_ASC);

		Object[] images = (Object[])qValue.executeQuery(sess);
		size = Math.min(size, images.length);
		GalleryImage[] ret = new GalleryImage[size];

		for (int i = 0; i < size; i++)
		{
			Object[] row = (Object[])images[i];
			ret[i] = new GalleryImage(
					row[0].toString(),
					(String)row[1],
					(String)row[2],
					((Integer)row[3]).intValue() != 0 ? "(" + row[3] + ")" : null);
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
		
		if (from != null && to != null)
		{
			title.append(" ");
			title.append(titleOriginated);
			title.append(" ");
			title.append(from);
			title.append(" - ");
			title.append(to);
		}
		else if (from != null)
		{
			title.append(" ");
			title.append(titleOriginated);
			title.append(" ");
			title.append(titleAfter);
			title.append(" ");
			title.append(from);
		}
		else if (to != null)
		{
			title.append(" ");
			title.append(titleOriginated);
			title.append(" ");
			title.append(titleBefore);
			title.append(" ");
			title.append(to);
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

	public GalleryImage[] getSampleManuscripts() {
		return this.getSample(6, 5);
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
	
	public String seeManuscripts() {
		this.selectedCategory = "6";
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
		from = null;
		to = null;
		
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
		
		if (from != null) conds.addCondition(Image.getAttribute("date"), from, Conditions.OP_GREATER_OR_EQUAL);
		if (to != null) conds.addCondition(Image.getAttribute("date"), to, Conditions.OP_SMALLER_OR_EQUAL);
		
		QueryValue qValue = new QueryValue("Image", conds);

		qValue.addPopulatedAttribute(Image.getAttribute("id"));
		qValue.addPopulatedAttribute(Image.getAttribute("fileName"));
		qValue.addPopulatedAttribute(Image.getAttribute("title"));
		qValue.addPopulatedAttribute(Image.getAttribute("date"));
		
		qValue.setOrderBy(new Attribute[] {Image.getAttribute("date")});
		qValue.setOrder(QueryValue.ORDER_ASC);
		
		Object[] response = qValue.executeQuery(sess);
		galleryImages = new GalleryImage[response.length];

		for (int i = 0; i < response.length; i++)
		{
			Object[] row = (Object[]) response[i];
			galleryImages[i] = new GalleryImage(
					row[0].toString(),
					(String)row[1],
					(String)row[2],
					((Integer)row[3]).intValue() != 0 ? "(" + row[3] + ")" : null);
		}
		
		transaction.commit();
		sess.close();;
		
		return galleryImages;
		
	}
	
	public String getExpandJavaScript()
	{
		
		Image img = Image.loadById(Integer.parseInt(this.imageId));
		
		StringBuffer js = new StringBuffer();
		js.append("window.open(");
		
		js.append("'images-detail-expanded.faces'");
		js.append(", ");

		js.append("'imagedetail'");
		js.append(", ");
		
		js.append("'");
		js.append("width=").append(img.getWidth() + POPUP_EXTRA_WIDTH);
		js.append(", ");
		js.append("height=").append(img.getHeight() + POPUP_EXTRA_HEIGHT);
		js.append(", ");
		js.append("menubar=no");
		js.append(", ");
		js.append("resizable=yes");
		js.append(", ");
		js.append("status=no");
		js.append(", ");
		js.append("toolbar=no");
		js.append("'");

		js.append(");");

		return js.toString();
		
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


	public Integer getFrom() {
		return from;
	}


	public void setFrom(Integer  from) {
		this.from = from;
	}


	public Integer getTo() {
		return to;
	}


	public void setTo(Integer to) {
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
		return "../servlet/thumbnail" +
				"?i=" + img.getFileName() +
				"&w=" + DETAIL_IMAGE_WIDTH +
				"&h=0";
	}
	
	public String getImageExpandedURL()
	{
		Image img = Image.loadById(Integer.parseInt(this.imageId));
		return "../images-database/" + img.getFileName();
	}

	public List getImageInfo()
	{
		
		List info = new ArrayList();
		
		Image img = Image.loadById(Integer.parseInt(this.imageId));
		
		if (img.getDate() != 0)
			info.add(new ImageInfo("Date:", String.valueOf(img.getDate())));

		if (!StringUtils.isNullOrEmpty(img.getCreator()))
			info.add(new ImageInfo("Creator:", img.getCreator()));
			
		if (!StringUtils.isNullOrEmpty(img.getSource()))
			info.add(new ImageInfo("Source:", img.getSource()));

		if (!StringUtils.isNullOrEmpty(img.getLanguage(), true))
			info.add(new ImageInfo("Language:", img.getLanguageName()));

		return info;

	}

}
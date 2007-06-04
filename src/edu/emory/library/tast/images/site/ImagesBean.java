package edu.emory.library.tast.images.site;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.component.UIData;
import javax.faces.model.SelectItem;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.emory.library.tast.TastResource;
import edu.emory.library.tast.common.voyage.VoyageDetailBean;
import edu.emory.library.tast.dm.Image;
import edu.emory.library.tast.dm.ImageCategory;
import edu.emory.library.tast.dm.Voyage;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.images.GalleryImage;
import edu.emory.library.tast.util.HibernateUtil;
import edu.emory.library.tast.util.StringUtils;
import edu.emory.library.tast.util.query.Conditions;
import edu.emory.library.tast.util.query.QueryValue;

public class ImagesBean
{

	private static final long ALL_CATEGORIES_ID = 0;

	private static final int DETAIL_THUMBS_COUNT = 8;
	private static final int POPUP_EXTRA_HEIGHT = 50;
	private static final int POPUP_EXTRA_WIDTH = 30;
	private static final int DETAIL_IMAGE_WIDTH = 600;

	private List categories = null;
	
	private GalleryImage[] galleryImages;
	private int selectedImageIndex;
	
	private String searchQueryTitle;
	private String searchQueryDescription;
	private long searchQueryCategory = ALL_CATEGORIES_ID;
	private Integer searchQueryFrom = null;
	private Integer searchQueryTo = null;
	private Integer searchVoyageId = null;
	
	private String imageId;
	private String imageTitle;
	private String imageDescription;
	private String imageUrl;
	private String imageExpandedURL;
	private List imageInfo;
	private List imageVoyagesInfo;
	
	private UIData linkedVoyagesTable;
	private VoyageDetailBean voyageBean;
	
	private void resetSearchParameters()
	{
		searchQueryTitle = "";
		searchQueryCategory = ALL_CATEGORIES_ID;
		searchQueryDescription = "";
		searchQueryFrom = null;
		searchQueryTo = null;
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
		
		qValue.setOrderBy(new Attribute[] {Image.getAttribute("date"), Image.getAttribute("id")});
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
	
	public String getQueryTitle()
	{
		
		StringBuffer title = new StringBuffer();
		
		String titleImages = TastResource.getText("images_query_showing");
		String titleContaining = TastResource.getText("images_query_title_title");
		String descriptionContaining = TastResource.getText("images_query_title_description");
		String titleFrom = TastResource.getText("images_query_title_from");
		String titleOriginated = TastResource.getText("images_query_title_originated");
		String titleBefore = TastResource.getText("images_query_title_before");
		String titleAfter = TastResource.getText("images_query_title_after");
		String titleVoyageId = TastResource.getText("images_query_title_voyage_id");
		
		title.append(titleImages);
		
		if (searchQueryTitle != null)
		{
			String query = searchQueryTitle.trim();
			if (searchQueryTitle.length() != 0)
			{
				title.append(" ");
				title.append(titleContaining);
				title.append(" '");
				title.append(query);
				title.append("'");
			}
		}
		
		if (searchQueryDescription != null)
		{
			String query = searchQueryDescription.trim();
			if (searchQueryDescription.length() != 0)
			{
				title.append(" ");
				title.append(descriptionContaining);
				title.append(" '");
				title.append(query);
				title.append("'");
			}
		}

		ImageCategory cat = ImageCategory.loadById(null, searchQueryCategory);
		if (cat != null)
		{
			title.append(" ");
			title.append(titleFrom);
			title.append(" ");
			title.append(cat.getName());
		}

		if (searchVoyageId != null)
		{
			title.append(" ");
			title.append(titleVoyageId);
			title.append(" '");
			title.append(searchVoyageId);
			title.append("'");
		}

		if (searchQueryFrom != null && searchQueryTo != null)
		{
			title.append(" ");
			title.append(titleOriginated);
			title.append(" ");
			title.append(searchQueryFrom);
			title.append(" - ");
			title.append(searchQueryTo);
		}
		else if (searchQueryFrom != null)
		{
			title.append(" ");
			title.append(titleOriginated);
			title.append(" ");
			title.append(titleAfter);
			title.append(" ");
			title.append(searchQueryFrom);
		}
		else if (searchQueryTo != null)
		{
			title.append(" ");
			title.append(titleOriginated);
			title.append(" ");
			title.append(titleBefore);
			title.append(" ");
			title.append(searchQueryTo);
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
		resetSearchParameters();
		this.searchQueryCategory = 1;
		loadGallery();
		return "images-query";
	}

	public String seeSlaves() {
		resetSearchParameters();
		this.searchQueryCategory = 2;
		loadGallery();
		return "images-query";
	}

	public String seeSlavers() {
		resetSearchParameters();
		this.searchQueryCategory = 3;
		loadGallery();
		return "images-query";
	}
	
	public String seePorts() {
		resetSearchParameters();
		this.searchQueryCategory = 4;
		loadGallery();
		return "images-query";
	}
	
	public String seeRegions() {
		resetSearchParameters();
		this.searchQueryCategory = 5;
		loadGallery();
		return "images-query";
	}
	
	public String seeManuscripts() {
		resetSearchParameters();
		this.searchQueryCategory = 6;
		loadGallery();
		return "images-query";
	}

	public String seePresentation() {
		resetSearchParameters();
		this.searchQueryCategory = 99;
		loadGallery();
		return "images-query";
	}

	public String search()
	{
		loadGallery();
		return "images-query";
	}
	
	public void openImageFromVoyageDetail(int voyageId, String imageId)
	{
		resetSearchParameters();
		this.imageId = imageId;
		this.searchVoyageId = new Integer(voyageId);
		loadGallery();
	}

	public String gotoDetailFromGallery()
	{
		loadDetail(false);
		return "images-detail";
	}
	
	public String gotoDetailFromDetail()
	{
		loadDetail(false);
		return "images-detail";
	}

	public String gotoDetailFromHomepage()
	{
		resetSearchParameters();
		loadDetail(true);
		loadGallery();
		return "images-detail";
	}
	
	public boolean isHasPrevImage()
	{
		return selectedImageIndex > 0;
	}
	
	public boolean isHasNextImage()
	{
		return selectedImageIndex < galleryImages.length - 1;
	}
	
	private String getImageGalleryUrl(int imageIndex)
	{
		return "/servlet/thumbnail" +
		"?i=" + galleryImages[imageIndex].getImageName() +
		"&w=" + "100" +
		"&h=" + "100";
	}
	
	public String getPrevThumbImageUrl()
	{
		return getImageGalleryUrl(selectedImageIndex-1);
	}

	public String getCurrThumbImageUrl()
	{
		return getImageGalleryUrl(selectedImageIndex);
	}

	public String getNextThumbImageUrl()
	{
		return getImageGalleryUrl(selectedImageIndex+1);
	}

	public String gotoPrev()
	{
		if (selectedImageIndex > 0)
		{
			selectedImageIndex--;
			imageId = galleryImages[selectedImageIndex].getId();
			loadDetail(false);
		}
		return null;
		/*
		if (this.galleryImages != null) {
			for (int i = 1; i < this.galleryImages.length; i++) {
				if (this.galleryImages[i].getId().equals(this.imageId)) {
					this.imageId = this.galleryImages[i - 1].getId();
					loadDetail(false);
					return null;
				}
			}
		}
		return null;
		*/
	}

	public String gotoNext()
	{
		if (selectedImageIndex < galleryImages.length - 1)
		{
			selectedImageIndex++;
			imageId = galleryImages[selectedImageIndex].getId();
			loadDetail(false);
		}
		return null;
		/*
		if (this.galleryImages != null) {
			for (int i = 0; i < this.galleryImages.length - 1; i++) {
				if (this.galleryImages[i].getId().equals(this.imageId)) {
					this.imageId = this.galleryImages[i + 1].getId();
					loadDetail(false);
					return null;
				}
			}
		}
		return null;
		*/
	}
	
	public String back()
	{
		return "images-back";
	}
	
	private void loadDetail(boolean setCategory)
	{

		Session sess = HibernateUtil.getSession();
		Transaction transaction = sess.beginTransaction();

		Image img = Image.loadById(Integer.parseInt(imageId), sess);

		if (img != null)
		{
			
			imageTitle = img.getTitle();
			imageDescription = img.getDescription();
			
			imageUrl = "../servlet/thumbnail" +
				"?i=" + img.getFileName() +
				"&w=" + DETAIL_IMAGE_WIDTH +
				"&h=0";
			
			imageExpandedURL = "../images-database/" +
				img.getFileName();
			
			if (setCategory)
			{
				ImageCategory cat = img.getCategory();
				if (cat != null) searchQueryCategory = cat.getId().intValue();
			}

			imageInfo = new ArrayList();

			if (img.getDate() != 0)
				imageInfo.add(new ImageDetailInfo("Date:", String.valueOf(img.getDate())));

			if (!StringUtils.isNullOrEmpty(img.getCreator()))
				imageInfo.add(new ImageDetailInfo("Creator:", img.getCreator()));

			if (!StringUtils.isNullOrEmpty(img.getSource()))
				imageInfo.add(new ImageDetailInfo("Source:", img.getSource()));

			if (!StringUtils.isNullOrEmpty(img.getLanguage(), true))
				imageInfo.add(new ImageDetailInfo("Language:", img.getLanguageName()));

			imageVoyagesInfo = new ArrayList();

			List voyages = Voyage.loadByVoyageIds(sess, img.getVoyageIds());

			if (voyages != null && voyages.size() > 0)
			{

				StringBuffer info = new StringBuffer();

				for (Iterator iter = voyages.iterator(); iter.hasNext();)
				{

					Voyage voyage = (Voyage) iter.next();

					info.setLength(0);
					if (StringUtils.isNullOrEmpty(voyage.getShipname()))
						info.append(TastResource.getText("images_shipname_unknown"));
					else
						info.append(voyage.getShipname());

					info.append(", ");
					if (voyage.getYearam() == null)
						info.append(TastResource.getText("images_year_unknown"));
					else
						info.append(voyage.getYearam());

					imageVoyagesInfo.add(new ImageLinkedVoyageInfo(
							voyage.getIid().longValue(),
							voyage.getVoyageid().intValue(),
							info.toString()));
				}

			}

		}
		
		transaction.commit();
		sess.close();

	}
	
	private void loadGallery()
	{
		
		Session sess = HibernateUtil.getSession();
		Transaction transaction = sess.beginTransaction();
		
		StringBuffer hqlWhere = new StringBuffer();
		int conditionsCount = 0;

		String[] keywordsTitle = StringUtils.extractQueryKeywords(this.searchQueryTitle, true);
		if (keywordsTitle.length > 0)
		{
			if (conditionsCount > 0) hqlWhere.append(" and ");
			hqlWhere.append("(");
			for (int i = 0; i < keywordsTitle.length; i++)
			{
				if (i > 0) hqlWhere.append(" and ");
				hqlWhere.append("remove_accents(upper(title)) like ");
				hqlWhere.append("remove_accents(upper(:title").append(i).append("))");
			}
			hqlWhere.append(")");
			conditionsCount++;
		}

		String[] keywordsDescripton = StringUtils.extractQueryKeywords(this.searchQueryDescription, true);
		if (keywordsDescripton.length > 0)
		{
			if (conditionsCount > 0) hqlWhere.append(" and ");
			hqlWhere.append("(");
			for (int i = 0; i < keywordsDescripton.length; i++)
			{
				if (i > 0) hqlWhere.append(" and ");
				hqlWhere.append("remove_accents(upper(description) like ");
				hqlWhere.append("remove_accents(upper(:description").append(i).append("))");
			}
			hqlWhere.append(")");
			conditionsCount++;
		}
		
		if (this.searchQueryCategory != ALL_CATEGORIES_ID)
		{
			if (conditionsCount > 0) hqlWhere.append(" and ");
			hqlWhere.append("category.id = :categoryId");
			conditionsCount++;
		}
		
		if (searchQueryFrom != null)
		{
			if (conditionsCount > 0) hqlWhere.append(" and ");
			hqlWhere.append("date >= :dateFrom");
			conditionsCount++;
		}
		
		if (searchQueryTo != null)
		{
			if (conditionsCount > 0) hqlWhere.append(" and ");
			hqlWhere.append("date <= :dateTo");
			conditionsCount++;
		}
		
		if (searchVoyageId != null)
		{
			if (conditionsCount > 0) hqlWhere.append(" and ");
			hqlWhere.append(":voyageId = some elements(voyageIds)");
			conditionsCount++;
		}
		
		StringBuffer hsql = new StringBuffer();
		hsql.append("select id, fileName, title, date from Image");
		if (hqlWhere.length() > 0)
		{
			hsql.append(" where ");
			hsql.append(hqlWhere);
		}
		hsql.append(" order by date asc, id asc");
		
		System.out.println(hsql.toString());
		
		Query query = sess.createQuery(hsql.toString());
		
		for (int i = 0; i < keywordsTitle.length; i++)
			query.setParameter("title" + i, "%" + keywordsTitle[i] + "%");
		
		for (int i = 0; i < keywordsDescripton.length; i++)
			query.setParameter("description" + i, "%" + keywordsDescripton[i] + "%");
		
		if (this.searchQueryCategory != ALL_CATEGORIES_ID)
			query.setParameter("categoryId", new Long(this.searchQueryCategory));
		
		if (searchQueryFrom != null)
			query.setParameter("dateFrom", searchQueryFrom);

		if (searchQueryTo != null)
			query.setParameter("dateTo", searchQueryTo);

		if (searchVoyageId != null)
			query.setParameter("voyageId", searchVoyageId);
		
		List response = query.list();
		galleryImages = new GalleryImage[response.size()];

		selectedImageIndex = -1;
		int imageIndex = 0;
		for (Iterator iter = response.iterator(); iter.hasNext();)
		{
			Object[] row = (Object[]) iter.next();
			String galleryImageId = row[0].toString();
			if (galleryImageId.equals(imageId)) selectedImageIndex = imageIndex;
			galleryImages[imageIndex++] = new GalleryImage(
					galleryImageId,
					(String)row[1],
					(String)row[2],
					((Integer)row[3]).intValue() != 0 ? "(" + row[3] + ")" : null);
		}
		
		transaction.commit();
		sess.close();
		
	}
	
	public String gotoVoyage()
	{
		
		ImageLinkedVoyageInfo voyageInfo = (ImageLinkedVoyageInfo) linkedVoyagesTable.getRowData();
		
		voyageBean.openVoyage(voyageInfo.getVoyageIid());
		voyageBean.setPreviousView("images-detail");
		
		return "voyage-detail";
		
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
	
	public List getCategories()
	{
		if (categories == null)
		{
			categories = new ArrayList();
			categories.add(new SelectItem(String.valueOf(ALL_CATEGORIES_ID), TastResource.getText("images_all_categories")));
			List cats = ImageCategory.loadAll(HibernateUtil.getSession(), "id");
			Iterator iter = cats.iterator();
			while (iter.hasNext())
			{
				ImageCategory cat = (ImageCategory)iter.next();
				categories.add(new SelectItem(cat.getId() + "", cat.getName()));
			}
		}
		return categories;
	}
	
	public GalleryImage[] getDetailThumbsImages()
	{
		
		int thumbsCount = Math.min(DETAIL_THUMBS_COUNT, galleryImages.length);

		int startIndex;
		int endIndex;
		
		if (galleryImages.length <= DETAIL_THUMBS_COUNT)
		{
			startIndex = 0;
			endIndex = galleryImages.length - 1;
		}
		else if (selectedImageIndex == 0)
		{
			startIndex = 0;
			endIndex = DETAIL_THUMBS_COUNT - 1;
		}
		else if (selectedImageIndex - 1 + DETAIL_THUMBS_COUNT - 1 >= galleryImages.length)
		{
			startIndex = galleryImages.length - DETAIL_THUMBS_COUNT; 
			endIndex = galleryImages.length - 1;
		}
		else
		{
			startIndex = selectedImageIndex - 1;
			endIndex = startIndex + DETAIL_THUMBS_COUNT - 1;
		}
		
		GalleryImage[] thumbs = new GalleryImage[thumbsCount];

		for (int i = startIndex; i <= endIndex; i++)
			thumbs[i - startIndex] = galleryImages[i];
		
		return thumbs;

	}

	public String getGalleryPositionIndicator()
	{
		return (selectedImageIndex + 1) + " / " + galleryImages.length;
	}

	public String getSearchQueryTitle()
	{
		return searchQueryTitle;
	}

	public void setSearchQueryTitle(String imageLike)
	{
		this.searchQueryTitle = imageLike;
	}

	public long getSearchQueryCategory()
	{
		return searchQueryCategory;
	}

	public void setSearchQueryCategory(long selectedCategory)
	{
		this.searchQueryCategory = selectedCategory;
	}

	public Integer getSearchQueryFrom()
	{
		return searchQueryFrom;
	}

	public void setSearchQueryFrom(Integer  from)
	{
		this.searchQueryFrom = from;
	}

	public Integer getSearchQueryTo()
	{
		return searchQueryTo;
	}

	public void setSearchQueryTo(Integer to)
	{
		this.searchQueryTo = to;
	}

	public String getImageId()
	{
		return imageId;
	}

	public void setImageId(String imageId)
	{
		if (galleryImages != null)
		{
			for (int i = 0; i < galleryImages.length; i++)
			{
				if (galleryImages[i].getId().equals(imageId))
				{
					selectedImageIndex = i;
					break;
				}
			}
		}
		this.imageId = imageId;
	}
	
	public String getImageTitle()
	{
		return imageTitle;
	}

	public String getImageDescription()
	{
		return imageDescription;
	}
	
	public String getImageURL()
	{
		return imageUrl;
	}
	
	public String getImageExpandedURL()
	{
		return imageExpandedURL;
	}

	public List getImageInfo()
	{
		return imageInfo;
	}
	
	public boolean isHasImageVoyages()
	{
		return imageVoyagesInfo != null && imageVoyagesInfo.size() != 0; 
	}

	public List getImageVoyages()
	{
		return imageVoyagesInfo;
	}

	public UIData getLinkedVoyagesTable()
	{
		return linkedVoyagesTable;
	}

	public void setLinkedVoyagesTable(UIData linkedVoyagesTable)
	{
		this.linkedVoyagesTable = linkedVoyagesTable;
	}

	public VoyageDetailBean getVoyageBean()
	{
		return voyageBean;
	}

	public void setVoyageBean(VoyageDetailBean voyageDetailBean)
	{
		this.voyageBean = voyageDetailBean;
	}

	public GalleryImage[] getGalleryImages()
	{
		return galleryImages;
	}

	public String getSearchQueryDescription()
	{
		return searchQueryDescription;
	}

	public void setSearchQueryDescription(String searchQueryOther)
	{
		this.searchQueryDescription = searchQueryOther;
	}

	public int getDetailThumbsCount()
	{
		return DETAIL_THUMBS_COUNT;
	}

	public Integer getSearchVoyageId()
	{
		return searchVoyageId;
	}

	public void setSearchVoyageId(Integer searchVoyageId)
	{
		this.searchVoyageId = searchVoyageId;
	}

}
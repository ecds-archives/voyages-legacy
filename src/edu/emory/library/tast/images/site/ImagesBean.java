package edu.emory.library.tast.images.site;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.component.UIData;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.emory.library.tast.TastResource;
import edu.emory.library.tast.common.MessageBarComponent;
import edu.emory.library.tast.common.QuerySummaryItem;
import edu.emory.library.tast.common.voyage.VoyageDetailBean;
import edu.emory.library.tast.dm.Configuration;
import edu.emory.library.tast.dm.Image;
import edu.emory.library.tast.dm.ImageCategory;
import edu.emory.library.tast.dm.Voyage;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.images.GalleryImage;
import edu.emory.library.tast.slaves.SlavesQuery;
import edu.emory.library.tast.util.HibernateUtil;
import edu.emory.library.tast.util.StringUtils;
import edu.emory.library.tast.util.query.Conditions;
import edu.emory.library.tast.util.query.QueryValue;

public class ImagesBean
{

	public static final long ALL_CATEGORIES_ID = 0;

	public static final int DETAIL_THUMBS_COUNT = 8;
	public static final int POPUP_EXTRA_HEIGHT = 50;
	public static final int POPUP_EXTRA_WIDTH = 30;
	public static final int DETAIL_IMAGE_WIDTH = 600;
	
	private List categories = null;
	
	private GalleryImage[] galleryImages;
	private int selectedImageIndex;
	
	private MessageBarComponent messageBar;
	
	private ImagesQuery currentQuery = new ImagesQuery();
	private ImagesQuery workingQuery = new ImagesQuery();
	// -------------------------------
	
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
		this.currentQuery = new ImagesQuery();
		this.workingQuery = new ImagesQuery();
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
		
		if (workingQuery.getSearchQueryTitle() != null)
		{
			String query = workingQuery.getSearchQueryTitle().trim();
			if (workingQuery.getSearchQueryTitle().length() != 0)
			{
				title.append(" ");
				title.append(titleContaining);
				title.append(" '");
				title.append(query);
				title.append("'");
			}
		}
		
		if (workingQuery.getSearchQueryDescription() != null)
		{
			String query = workingQuery.getSearchQueryDescription().trim();
			if (workingQuery.getSearchQueryDescription().length() != 0)
			{
				title.append(" ");
				title.append(descriptionContaining);
				title.append(" '");
				title.append(query);
				title.append("'");
			}
		}

		ImageCategory cat = ImageCategory.loadById(null, workingQuery.getSearchQueryCategory());
		if (cat != null)
		{
			title.append(" ");
			title.append(titleFrom);
			title.append(" ");
			title.append(cat.getName());
		}

		if (workingQuery.getSearchVoyageId() != null)
		{
			title.append(" ");
			title.append(titleVoyageId);
			title.append(" '");
			title.append(workingQuery.getSearchVoyageId());
			title.append("'");
		}

		if (workingQuery.getSearchQueryFrom() != null && workingQuery.getSearchQueryTo() != null)
		{
			title.append(" ");
			title.append(titleOriginated);
			title.append(" ");
			title.append(workingQuery.getSearchQueryFrom());
			title.append(" - ");
			title.append(workingQuery.getSearchQueryTo());
		}
		else if (workingQuery.getSearchQueryFrom() != null)
		{
			title.append(" ");
			title.append(titleOriginated);
			title.append(" ");
			title.append(titleAfter);
			title.append(" ");
			title.append(workingQuery.getSearchQueryFrom());
		}
		else if (workingQuery.getSearchQueryTo() != null)
		{
			title.append(" ");
			title.append(titleOriginated);
			title.append(" ");
			title.append(titleBefore);
			title.append(" ");
			title.append(workingQuery.getSearchQueryTo());
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
		workingQuery.setSearchQueryCategory(1);
		loadGallery();
		return "images-query";
	}

	public String seeSlaves() {
		resetSearchParameters();
		workingQuery.setSearchQueryCategory(2);
		loadGallery();
		return "images-query";
	}

	public String seeSlavers() {
		resetSearchParameters();
		workingQuery.setSearchQueryCategory(3);
		loadGallery();
		return "images-query";
	}
	
	public String seePorts() {
		resetSearchParameters();
		workingQuery.setSearchQueryCategory(4);
		loadGallery();
		return "images-query";
	}
	
	public String seeRegions() {
		resetSearchParameters();
		workingQuery.setSearchQueryCategory(5);
		loadGallery();
		return "images-query";
	}
	
	public String seeManuscripts() {
		resetSearchParameters();
		workingQuery.setSearchQueryCategory(6);
		loadGallery();
		return "images-query";
	}

	public String seePresentation() {
		resetSearchParameters();
		workingQuery.setSearchQueryCategory(99);
		loadGallery();
		return "images-query";
	}

	public String search()
	{
		if (this.currentQuery.equals(this.workingQuery)) {
			return null;
		}
		this.workingQuery = (ImagesQuery) this.currentQuery.clone();
		loadGallery();
		return "images-query";
	}
	
	public void openImageFromVoyageDetail(int voyageId, String imageId)
	{
		resetSearchParameters();
		this.imageId = imageId;
		workingQuery.setSearchVoyageId(new Integer(voyageId));
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
				if (cat != null) workingQuery.setSearchQueryCategory(cat.getId().intValue());
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

		String[] keywordsTitle = StringUtils.extractQueryKeywords(workingQuery.getSearchQueryTitle(), true);
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

		String[] keywordsDescripton = StringUtils.extractQueryKeywords(workingQuery.getSearchQueryDescription(), true);
		if (keywordsDescripton.length > 0)
		{
			if (conditionsCount > 0) hqlWhere.append(" and ");
			hqlWhere.append("(");
			for (int i = 0; i < keywordsDescripton.length; i++)
			{
				if (i > 0) hqlWhere.append(" and ");
				hqlWhere.append("remove_accents(upper(description)) like ");
				hqlWhere.append("remove_accents(upper(:description").append(i).append("))");
			}
			hqlWhere.append(")");
			conditionsCount++;
		}
		
		if (workingQuery.getSearchQueryCategory() != ALL_CATEGORIES_ID)
		{
			if (conditionsCount > 0) hqlWhere.append(" and ");
			hqlWhere.append("category.id = :categoryId");
			conditionsCount++;
		}
		
		if (workingQuery.getSearchQueryFrom() != null)
		{
			if (conditionsCount > 0) hqlWhere.append(" and ");
			hqlWhere.append("date >= :dateFrom");
			conditionsCount++;
		}
		
		if (workingQuery.getSearchQueryTo() != null)
		{
			if (conditionsCount > 0) hqlWhere.append(" and ");
			hqlWhere.append("date <= :dateTo");
			conditionsCount++;
		}
		
		if (workingQuery.getSearchPortId() != null)
		{
			if (conditionsCount > 0) hqlWhere.append(" and ");
			hqlWhere.append(":portId = some elements(ports)");
			conditionsCount++;
		}
		
		if (workingQuery.getSearchRegionId() != null)
		{
			if (conditionsCount > 0) hqlWhere.append(" and ");
			hqlWhere.append(":regionId = some elements(regions)");
			conditionsCount++;
		}
		
		if (workingQuery.getSearchVoyageId() != null)
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
		
		if (workingQuery.getSearchQueryCategory() != ALL_CATEGORIES_ID)
			query.setParameter("categoryId", new Long(workingQuery.getSearchQueryCategory()));
		
		if (workingQuery.getSearchQueryFrom() != null)
			query.setParameter("dateFrom", workingQuery.getSearchQueryFrom());

		if (workingQuery.getSearchQueryTo() != null)
			query.setParameter("dateTo", workingQuery.getSearchQueryTo());

		if (workingQuery.getSearchVoyageId() != null)
			query.setParameter("voyageId", workingQuery.getSearchVoyageId());
		
		if (workingQuery.getSearchPortId() != null)
			query.setParameter("portId", workingQuery.getSearchPortId());

		if (workingQuery.getSearchRegionId() != null)
			query.setParameter("regionId", workingQuery.getSearchRegionId());

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

	public int getDetailThumbsCount()
	{
		return DETAIL_THUMBS_COUNT;
	}

	public List getQuerySummary() {
		
		List query = new ArrayList();
	
		if (!StringUtils.isNullOrEmpty(this.workingQuery.getSearchQueryTitle())) {
			query.add(new QuerySummaryItem("Description containing", this.workingQuery.getSearchQueryTitle()));
		}

		if (!StringUtils.isNullOrEmpty(this.workingQuery.getSearchQueryDescription())) {
			query.add(new QuerySummaryItem("Description containing", this.workingQuery.getSearchQueryDescription()));
		}

		if (this.workingQuery.getSearchVoyageId() != null) {
			query.add(new QuerySummaryItem("Voyage ID", this.workingQuery.getSearchVoyageId().toString()));
		}

		if (this.workingQuery.getSearchQueryCategory() == ALL_CATEGORIES_ID) {
			query.add(new QuerySummaryItem("Category of images", "All categories"));
		} else {
			for (Iterator iter = categories.iterator(); iter.hasNext();) {
				SelectItem element = (SelectItem) iter.next();
				if (element.getValue().equals(String.valueOf(this.workingQuery.getSearchQueryCategory()))) {
					query.add(new QuerySummaryItem("Category of images", element.getLabel()));
					break;
				}
			}
		}

		if (this.workingQuery.getSearchQueryFrom() != null || this.workingQuery.getSearchQueryTo() != null) {
			String label = null;
			if (this.workingQuery.getSearchQueryFrom() == null) {
				label = "? - " + this.workingQuery.getSearchQueryTo();
			} else if (this.workingQuery.getSearchQueryTo() == null) {
				label = this.workingQuery.getSearchQueryFrom() + " - ?";
			} else {
				label = this.workingQuery.getSearchQueryFrom() + " - " + this.workingQuery.getSearchQueryTo();
			}
			query.add(new QuerySummaryItem("Date range", label));
		}
			
		return query;
	}

	public ImagesQuery getCurrentQuery() {
		return currentQuery;
	}

	public void setCurrentQuery(ImagesQuery currentQuery) {
		this.currentQuery = currentQuery;
	}
	
	public String permLink() {
		
		Configuration conf = new Configuration();
		conf.addEntry("permlinkSlaves", this.workingQuery);
		conf.save();

		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		messageBar.setMessage(request.getRequestURL() + "?permlink=" + conf.getId());
		messageBar.setRendered(true);
		
		return null;
	}
	
	public void restoreLink(Long configId) {
		Session session = HibernateUtil.getSession();
		Transaction t = session.beginTransaction();
		try {
			Configuration conf = Configuration.loadConfiguration(configId);
			if (conf == null)
				return;

			if (conf.getEntry("permlinkSlaves") != null) {
				ImagesQuery selection = (ImagesQuery) conf.getEntry("permlinkSlaves");
				this.currentQuery = (ImagesQuery) selection.clone();
				this.workingQuery = (ImagesQuery) selection.clone();
			}
		} finally {
			t.commit();
			session.close();
		}
	}

	public MessageBarComponent getMessageBar() {
		return messageBar;
	}

	public void setMessageBar(MessageBarComponent messageBar) {
		this.messageBar = messageBar;
	}

}
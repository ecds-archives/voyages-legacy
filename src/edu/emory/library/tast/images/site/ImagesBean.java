package edu.emory.library.tast.images.site;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.component.UIData;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.emory.library.tast.AppConfig;
import edu.emory.library.tast.TastResource;
import edu.emory.library.tast.common.SelectItem;
import edu.emory.library.tast.common.voyage.VoyageDetailBean;
import edu.emory.library.tast.dm.Image;
import edu.emory.library.tast.dm.ImageCategory;
import edu.emory.library.tast.dm.Voyage;
import edu.emory.library.tast.images.GalleryImage;
import edu.emory.library.tast.util.HibernateUtil;
import edu.emory.library.tast.util.StringUtils;

public class ImagesBean
{

	private static final int INDEX_GALLERY_SAMPLE = 4;

	public static final int DETAIL_THUMBS_COUNT = 8;
	public static final int POPUP_EXTRA_HEIGHT = 50;
	public static final int POPUP_EXTRA_WIDTH = 30;
	public static final int DETAIL_IMAGE_WIDTH = 440;
	
	private SelectItem[] categories = null;
	private String[] allCategoryIds = null;
	
	private GalleryImage[] galleryImages;
	private int selectedImageIndex;
	
	private ImagesQuery currentQuery;
	private ImagesQuery workingQuery;
	
	private String imageId;
	private String imageTitle;
	private String imageDescription;
	private String imageUrl;
	private String imageExpandedURL;
	private List imageInfo;
	private List imageVoyagesInfo;
	
	private UIData linkedVoyagesTable;
	private VoyageDetailBean voyageBean;
	
	public ImagesBean()
	{
		resetSearchParameters();
	}
	
	private void resetSearchParameters()
	{
		
		this.currentQuery = new ImagesQuery();
		this.workingQuery = new ImagesQuery();
		
		ensureCategoriesLoaded();
		this.currentQuery.setCategories(this.allCategoryIds);
		this.workingQuery.setCategories(this.allCategoryIds);
		
	}
	
	private GalleryImage[] getSample(int catId, int size)
	{

		Session sess = HibernateUtil.getSession();
		Transaction transaction = sess.beginTransaction();
		
		String hql =
			"select img.id, img.fileName, img.title, img.date " +
			"from Image img " +
			"where img.readyToGo = true and img.category.id = " + catId + " " +
			"order by img.date, img.id";
		
		List images = sess.createQuery(hql).setMaxResults(size).list();
		size = Math.min(size, images.size());
		GalleryImage[] ret = new GalleryImage[size];
		
		for (int i = 0; i < size; i++)
		{
			Object[] row = (Object[])images.get(i);
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
	
	public GalleryImage[] getSampleVessels()
	{
		return this.getSample(1, INDEX_GALLERY_SAMPLE);
	}
	
	public GalleryImage[] getSampleSlaves()
	{
		return this.getSample(2, INDEX_GALLERY_SAMPLE);
	}
	
	public GalleryImage[] getSampleSlavers()
	{
		return this.getSample(3, INDEX_GALLERY_SAMPLE);
	}
	
	public GalleryImage[] getSamplePorts()
	{
		return this.getSample(4, INDEX_GALLERY_SAMPLE);
	}
	
	public GalleryImage[] getSampleRegions()
	{
		return this.getSample(5, INDEX_GALLERY_SAMPLE);
	}

	public GalleryImage[] getSampleManuscripts()
	{
		return this.getSample(6, INDEX_GALLERY_SAMPLE);
	}

	public GalleryImage[] getSamplePresentation()
	{
		return this.getSample(99, INDEX_GALLERY_SAMPLE);
	}
	
	public String seeVessels()
	{
		resetSearchParameters();
		currentQuery.setCategory(1);
		search();
		return "images-query";
	}

	public String seeSlaves()
	{
		resetSearchParameters();
		currentQuery.setCategory(2);
		search();
		return "images-query";
	}

	public String seeSlavers()
	{
		resetSearchParameters();
		currentQuery.setCategory(3);
		search();
		return "images-query";
	}
	
	public String seePorts()
	{
		resetSearchParameters();
		currentQuery.setCategory(4);
		search();
		return "images-query";
	}
	
	public String seeRegions()
	{
		resetSearchParameters();
		currentQuery.setCategory(5);
		search();
		return "images-query";
	}
	
	public String seeManuscripts()
	{
		resetSearchParameters();
		currentQuery.setCategory(6);
		search();
		return "images-query";
	}

	public String seePresentation()
	{
		resetSearchParameters();
		currentQuery.setCategory(99);
		search();
		return "images-query";
	}

	public String search()
	{
		this.workingQuery = (ImagesQuery) this.currentQuery.clone();
		loadGallery();
		return "images-query";
	}
	
	public String startAgain()
	{
		resetSearchParameters();
		search();
		return "images";
	}

	public void openImageFromVoyageDetail(int voyageId, String imageId)
	{
		resetSearchParameters();
		this.imageId = imageId;
		// workingQuery.setSearchVoyageId(new Integer(voyageId));
		loadGallery();
	}

	public String gotoImageFromUrl(String externalId)
	{
		loadDetail(null, externalId, false);
		return "images-detail";
	}

	public String gotoDetailFromGallery()
	{
		loadDetail(imageId, null, false);
		return "images-detail";
	}
	
	public String gotoDetailFromDetail()
	{
		loadDetail(imageId, null, false);
		return "images-detail";
	}

	public String gotoDetailFromHomepage()
	{
		resetSearchParameters();
		loadDetail(imageId, null, true);
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
			loadDetail(imageId, null, false);
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
			loadDetail(imageId, null, false);
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
	
	private void loadDetail(String imageId, String imageExternalId, boolean setCategory)
	{

		String imagesBaseUrl = AppConfig.getConfiguration().getString(AppConfig.IMAGES_URL);
		imagesBaseUrl = StringUtils.trimEnd(imagesBaseUrl, '/');

		Session sess = HibernateUtil.getSession();
		Transaction transaction = sess.beginTransaction();
		
		Image img = imageId != null ?
				Image.loadById(Integer.parseInt(imageId), sess) :
					Image.loadByExternalId(imageExternalId, sess);

		if (img != null)
		{
			
			imageId = this.imageId = String.valueOf(img.getId()); 
			imageTitle = img.getTitle();
			imageDescription = img.getDescription();
			
			imageUrl = "../servlet/thumbnail" +
				"?i=" + img.getFileName() +
				"&w=" + DETAIL_IMAGE_WIDTH +
				"&h=0";
			
			imageExpandedURL = imagesBaseUrl + "/" +
				img.getFileName();
			
			if (setCategory)
			{
				ImageCategory cat = img.getCategory();
				if (cat != null) workingQuery.setCategory(cat.getId().intValue());
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

		String[] keywords = StringUtils.extractQueryKeywords(workingQuery.getKeyword(), true);
		if (keywords.length > 0)
		{
			if (conditionsCount > 0) hqlWhere.append(" and ");
			hqlWhere.append("(");
			for (int i = 0; i < keywords.length; i++)
			{
				if (i > 0) hqlWhere.append(" and ");
				hqlWhere.append("(");
				hqlWhere.append("remove_accents(upper(title)) like ");
				hqlWhere.append("remove_accents(upper(:keyword").append(i).append("))");
				hqlWhere.append(" or ");
				hqlWhere.append("remove_accents(upper(description)) like ");
				hqlWhere.append("remove_accents(upper(:keyword").append(i).append("))");
				hqlWhere.append(")");
			}
			hqlWhere.append(")");
			conditionsCount++;
		}

		String[] categories = workingQuery.getCategories();
		if (categories != null && categories.length != 0)
		{
			if (conditionsCount > 0) hqlWhere.append(" and ");
			hqlWhere.append("(");
			for (int i = 0; i < categories.length; i++)
			{
				if (i > 0) hqlWhere.append(" or ");
				hqlWhere.append("category.id = :categoryId").append(i);
			}
			hqlWhere.append(")");
			conditionsCount++;
		}
		
		if (workingQuery.getYearFrom() != null)
		{
			if (conditionsCount > 0) hqlWhere.append(" and ");
			hqlWhere.append("date >= :dateFrom");
			conditionsCount++;
		}
		
		if (workingQuery.getYearTo() != null)
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
		
//		if (workingQuery.getSearchVoyageId() != null)
//		{
//			if (conditionsCount > 0) hqlWhere.append(" and ");
//			hqlWhere.append(":voyageId = some elements(voyageIds)");
//			conditionsCount++;
//		}
		
		if (conditionsCount > 0) hqlWhere.append(" and ");
		hqlWhere.append("readyToGo = true");
		conditionsCount++;
		
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
		
		for (int i = 0; i < keywords.length; i++)
			query.setParameter("keyword" + i, "%" + keywords[i] + "%");
		
		if (categories != null)
			for (int i = 0; i < categories.length; i++)
				query.setParameter("categoryId" + i, new Long(categories[i]));
		
		if (workingQuery.getYearFrom() != null)
			query.setParameter("dateFrom", workingQuery.getYearFrom());

		if (workingQuery.getYearTo() != null)
			query.setParameter("dateTo", workingQuery.getYearTo());
	
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
		
		voyageBean.openVoyageByIid(voyageInfo.getVoyageIid());
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
		js.append(", ");
		js.append("scrollbars=yes");
		js.append("'");

		js.append(");");

		return js.toString();
		
	}
	
//	public List getCategories()
//	{
//		if (categories == null)
//		{
//			categories = new ArrayList();
//			categories.add(new SelectItem(String.valueOf(ALL_CATEGORIES_ID), TastResource.getText("images_all_categories")));
//			List cats = ImageCategory.loadAll(HibernateUtil.getSession(), "id");
//			Iterator iter = cats.iterator();
//			while (iter.hasNext())
//			{
//				ImageCategory cat = (ImageCategory)iter.next();
//				categories.add(new SelectItem(cat.getId() + "", cat.getName()));
//			}
//		}
//		return categories;
//	}
	
	private void ensureCategoriesLoaded()
	{
		
		if (categories != null && allCategoryIds != null)
			return;
		
		Session sess = HibernateUtil.getSession();
		Transaction transaction = sess.beginTransaction();
		
		String hql =
			"select img.category.id, img.category.name, count(img) " +
			"from Image img " +
			"where img.readyToGo = true " +
			"group by img.category.id, img.category.name " +
			"order by img.category.name";
		
		List cats = sess.createQuery(hql).list();
		
		categories = new SelectItem[cats.size()];
		allCategoryIds = new String[cats.size()];
		
		int catIdx = 0;
		Iterator iter = cats.iterator();
		while (iter.hasNext())
		{
			Object[] row = (Object[])iter.next();
			
			String catId = String.valueOf(row[0]);
			long imagesCount = ((Long)row[2]).longValue();
			
			String label = (String) row[1];
			if (imagesCount == 1)
				label += " " +
						"<span class=\"images-count\">" +
						"(1 image)" +
						"</span>";
			else
				label += " " +
						"<span class=\"images-count\">" +
						"(" + imagesCount + " images)" +
						"</span>";
			
			allCategoryIds[catIdx] = catId;
			categories[catIdx] = new SelectItem(label, catId);
			
			catIdx++;
		}
		
		transaction.commit();
		sess.close();

	}
	
	public SelectItem[] getCategories()
	{
		ensureCategoriesLoaded();
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
			endIndex = galleryImages.length  - 1;
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

	public ImagesQuery getCurrentQuery()
	{
		return currentQuery;
	}

	public void setCurrentQuery(ImagesQuery currentQuery)
	{
		this.currentQuery = currentQuery;
	}	
	
}
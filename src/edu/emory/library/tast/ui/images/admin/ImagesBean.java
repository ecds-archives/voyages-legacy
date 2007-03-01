package edu.emory.library.tast.ui.images.admin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.io.IOUtils;
import org.apache.myfaces.custom.fileupload.UploadedFile;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.DataException;

import edu.emory.library.tast.AppConfig;
import edu.emory.library.tast.Language;
import edu.emory.library.tast.Languages;
import edu.emory.library.tast.dm.Image;
import edu.emory.library.tast.dm.ImageCategory;
import edu.emory.library.tast.dm.Port;
import edu.emory.library.tast.dm.Region;
import edu.emory.library.tast.ui.LookupSources;
import edu.emory.library.tast.util.HibernateUtil;
import edu.emory.library.tast.util.StringUtils;
import edu.emory.library.tast.util.UidGenerator;

public class ImagesBean
{
	
	private static final String IMAGES_REGIONS_LOOKUP = "images-regions-lookup";
	private static final String IMAGES_PORTS_LOOKUP = "images-ports-lookup";
	private static final String IMAGES_PEOPLE_LOOKUP = "images-people-lookup";
	
	private static final String[] AUTH_STATUS_SHORT_LABELS = new String[] {
		"-",
		"No auth. needed",
		"Gift",
		"Personal col.",
		"Emory owned",
		"Applied for",
		"Granted",
		"Denided"};
	
	private static final String[] AUTH_STATUS_LONG_LABELS = new String[] {
		"-",
		"No authorization needed",
		"Gift",
		"Personal collection",
		"Emory owned",
		"Applied for authorization",
		"Authorization granted",
		"Authorization denided"};

	private static final String[] IMAGE_STATUS_LABELS = new String[] {
		"-",
		"Located",
		"Digitalized",
		"Processing",
		"Ready"};
	
	private Map allowedTypes = new HashMap();

	private String listStyle = "table";
	private String selectedImageId;
	private String thumbnailSize = "48x48";
	private long listCategoryId;
	
	private String searchFor = null;
	private String sortBy = "cat.name";
	
	private Image image;
	private long imageCategoryId;
	private UploadedFile uploadedImage;
	private boolean uploadBoxShown;
	private String[] selectedRegionsIds;
	private String[] selectedPortsIds;
	private String errorText;

	private int scrollPosX;
	private int scrollPosY;
	
	public ImagesBean()
	{
		allowedTypes.put("image/gif", "GIF");
		allowedTypes.put("image/jpeg", "JPG");
		allowedTypes.put("image/pjpeg", "JPG");
		allowedTypes.put("image/png", "PNG");
	}
	
	public String getScrollToJavaScript()
	{
		if (scrollPosX != 0 || scrollPosY != 0)
		{
			StringBuffer js = new StringBuffer();
			js.append("window.onload = function() {");
			js.append("window.scrollTo(").append(scrollPosX).append(", ").append(scrollPosY).append(");");
			js.append("}");
			clearScrollPosition();
			return js.toString();
		}
		else
		{
			return "";
		}
	}
	
	protected void saveScrollPosition()
	{

		Map params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String scrollPosXStr = (String) params.get("scrollPosX");
		String scrollPosYStr = (String) params.get("scrollPosY");
		
		try
		{
			scrollPosX = Integer.parseInt(scrollPosXStr);
			scrollPosY = Integer.parseInt(scrollPosYStr);
		}
		catch (NumberFormatException e)
		{
			clearScrollPosition();
		}

	}
	
	protected void clearScrollPosition()
	{
		scrollPosX = 0;
		scrollPosY = 0;
	}
	
	public List getAllImages()
	{
		
		// open db
		Session sess = HibernateUtil.getSession();
		Transaction transaction = sess.beginTransaction();

		// basic query
		Criteria crit = sess.createCriteria(Image.class);
		crit.createAlias("category", "cat");
		
		// category
		if (listCategoryId != 0)
		{
			ImageCategory cat = ImageCategory.loadById(sess, listCategoryId);
			crit.add(Restrictions.eq("category", cat));
		}

		// search for
		if (!StringUtils.isNullOrEmpty(searchFor))
		{
			String searchForLocal = "%" + searchFor.trim() + "%";
			crit.add(Restrictions.disjunction().
				add(Restrictions.ilike("title", searchForLocal)).
				add(Restrictions.ilike("description", searchForLocal)).
				add(Restrictions.ilike("comments", searchForLocal)).
				add(Restrictions.ilike("source", searchForLocal)).
				add(Restrictions.ilike("references", searchForLocal)));
		}

		// sort order
		crit.addOrder(Order.asc(sortBy));
		if (!"date".equals(sortBy)) crit.addOrder(Order.asc("date"));

		// load images from db and create a new list for UI
		List dbImages = crit.list();
		List uiImages = new ArrayList(dbImages.size());

		// thumnail size
		String [] thumbnailSizeArr = thumbnailSize.split("x");
		String w = thumbnailSizeArr[0];
		String h = thumbnailSizeArr[1];

		// move them to ui list
		for (Iterator iter = dbImages.iterator(); iter.hasNext();)
		{
			Image image = (Image) iter.next();

			int authStatus = image.getAuthorizationStatus();
			String authStatusLabel;
			if (authStatus < 0 || AUTH_STATUS_SHORT_LABELS.length <= authStatus)
				authStatusLabel = "?";
			else
				authStatusLabel = AUTH_STATUS_SHORT_LABELS[authStatus];

			int imgStatus = image.getImageStatus();
			String imgStatusLabel;
			if (imgStatus < 0 || IMAGE_STATUS_LABELS.length <= imgStatus)
				imgStatusLabel = "?";
			else
				imgStatusLabel = IMAGE_STATUS_LABELS[imgStatus];

			String subItems[] = new String[] {
					image.isReadyToGo() ? "yes" : "no", 
					authStatusLabel,
					imgStatusLabel,
					image.getCategory().getName(),
					String.valueOf(image.getDate()),
					StringUtils.coalesce(image.getSource(), "")};

			ImageListItem uiImage = new ImageListItem();
			uiImage.setId(String.valueOf(image.getId()));
			uiImage.setName(image.getTitle());
			uiImage.setUrl("servlet/thumbnail?i=" + image.getFileName() + "&w=" + w + "&h=" + h);
			uiImage.setSubItems(subItems);
			uiImages.add(uiImage);
		}
		
		// close db
		transaction.commit();
		sess.close();

		// return list of images
		return uiImages;

	}
	
	public String searchInList()
	{
		return null;
	}

	public String openImage()
	{
		
		// we come from list -> detail
		// so save the position in the list
		saveScrollPosition();
		
		// open db
		Session sess = HibernateUtil.getSession();
		Transaction transaction = sess.beginTransaction();
		
		// load image itself
		image = Image.loadById(Integer.parseInt(selectedImageId), sess);
		
		// image category has to be in its own variable
		imageCategoryId = image.getCategory().getId().intValue();

		// sort
		List regions = sess.createFilter(image.getRegions(), "order by this.name").list();		
		List ports = sess.createFilter(image.getPorts(), "order by this.name").list();		

		// selected regions
		int i = 0;
		selectedRegionsIds = new String[regions.size()];
		for (Iterator iter = regions.iterator(); iter.hasNext();)
		{
			Region dbRegion = (Region) iter.next();
			selectedRegionsIds[i++] = String.valueOf(dbRegion.getId());
		}
		
		// selected ports
		int j = 0;
		selectedPortsIds = new String[ports.size()];
		for (Iterator iter = ports.iterator(); iter.hasNext();)
		{
			Port dbPort = (Port) iter.next();
			selectedPortsIds[j++] = String.valueOf(dbPort.getId());
		}

		// close db
		transaction.commit();
		sess.close();
		
		// goto detail
		cleanAndPrepareDetailPage();
		return "detail";

	}
	
	public String newImage()
	{
		
		// we come from list -> detail
		// so save the position in the list
		saveScrollPosition();

		// create an empty image
		image = new Image();
		
		// clean the list of regions
		selectedRegionsIds = new String[0];

		// clean the list of ports
		selectedPortsIds = new String[0];

		// goto detail
		cleanAndPrepareDetailPage();
		return "detail";

	}
	
	private void cleanAndPrepareDetailPage()
	{
		registerLookupSources();
		uploadBoxShown = false;
		setErrorText(null);
	}
	
	private void registerLookupSources()
	{
		
		// register regions lookup source
		LookupSources.registerLookupSource(
				IMAGES_REGIONS_LOOKUP,
				new LookupSourceRegions());

		// register ports lookup source
		LookupSources.registerLookupSource(
				IMAGES_PORTS_LOOKUP,
				new LookupSourcePorts());

	}

	public String uploadNewImage()
	{
		
		try
		{

			if (uploadedImage == null)
				return null;
			
			// check type
			String extension = (String) allowedTypes.get(uploadedImage.getContentType()); 
			if (extension == null)
				return null;
			
			// new filename
			File file = null;
			String fileName = null;
			String imageDir = AppConfig.getConfiguration().getString(AppConfig.IMAGES_DIRECTORY);
			do
			{
				fileName = new UidGenerator().generate() + "." + extension;
				file = new File(imageDir, fileName);
			}
			while(file.exists());
			
			// copy
			FileOutputStream imgFileStream = new FileOutputStream(file);
			int size = IOUtils.copy(uploadedImage.getInputStream(), imgFileStream);
			imgFileStream.close();
			
			// get image info
			Iterator readerIter = ImageIO.getImageReadersByFormatName(extension);
			ImageReader rdr = (ImageReader) readerIter.next();
			if (rdr == null) return null;
			rdr.setInput(ImageIO.createImageInputStream(file), true);
			
			// get width and height
			int width = rdr.getWidth(0);
			int height = rdr.getHeight(0);
			
			// replace current image
			image.setWidth(width);
			image.setHeight(height);
			image.setSize(size);
			image.setMimeType(uploadedImage.getContentType());
			image.setFileName(fileName);
			
			// all ok
			uploadBoxShown = false;

		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
		
		return null;
	}
	
	private String checkTextField(String value, String fieldName, int maxLength, boolean canBeEmpty) throws SaveImageException
	{

		if (value == null)
			throw new SaveImageException("Please specify the field " + fieldName + ".");
			
		String valueLocal = value.trim();
		
		if (!canBeEmpty && valueLocal.length() == 0)
			throw new SaveImageException("Please specify the field " + fieldName + ".");
		
		if (valueLocal.length() > maxLength)
			throw new SaveImageException("The field " + valueLocal + " is too long (max allowed " + maxLength + " ).");
		
		return valueLocal;

	}

	public String saveImage()
	{
		
		Session sess = null;
		Transaction transaction = null;
		
		try
		{

			// open db
			sess = HibernateUtil.getSession();
			transaction = sess.beginTransaction();
			
			// links to regions
			Set imageRegions = new HashSet();
			image.setRegions(imageRegions);
			for (int i = 0; i < selectedRegionsIds.length; i++)
			{
				int regionId = Integer.parseInt(selectedRegionsIds[i]);
				Region dbRegion = Region.loadById(sess, regionId);
				imageRegions.add(dbRegion);
			}
			
			// links to ports
			Set imagePorts = new HashSet();
			image.setPorts(imagePorts);
			for (int i = 0; i < selectedPortsIds.length; i++)
			{
				int portId = Integer.parseInt(selectedPortsIds[i]);
				Port dbPort = Port.loadById(sess, portId);
				imagePorts.add(dbPort);
			}
	
			// we will use it often
			Configuration appConf = AppConfig.getConfiguration();
			
			// title
			String titleLocal = checkTextField(
					image.getTitle(), "title",
					appConf.getInt(AppConfig.IMAGES_TITLE_MAXLEN), false);

			// source
			String sourceLocal = checkTextField(
					image.getSource(), "source",
					appConf.getInt(AppConfig.IMAGES_SOURCE_MAXLEN), true);

			// creator
			String creatorLocal = checkTextField(
					image.getCreator(), "creator",
					appConf.getInt(AppConfig.IMAGES_CREATOR_MAXLEN), true);

			// creator
			String referencesLocal = checkTextField(
					image.getReferences(), "references",
					appConf.getInt(AppConfig.IMAGES_REFERENCES_MAXLEN), true);
			
			// creator
			String emoryLocationLocal = checkTextField(
					image.getEmoryLocation(), "Emory location",
					appConf.getInt(AppConfig.IMAGES_EMORYLOCATION_MAXLEN), true);

			// check date
//			String dateLocal = image.getDate().trim();
//			if (!StringUtils.isNullOrEmpty(dateLocal, true) && !Image.checkDate(dateLocal))
//				throw new SaveImageException("Date is invalid. Expected YYYY or YYYY-MM or YYYY-MM-DD.");
			
			// category
			ImageCategory cat = ImageCategory.loadById(sess, imageCategoryId);

			// save
			image.setCategory(cat);
			image.setTitle(titleLocal);
			image.setSource(sourceLocal);
			image.setCreator(creatorLocal);
			image.setReferences(referencesLocal);
			image.setEmoryLocation(emoryLocationLocal);
			sess.saveOrUpdate(image);

			// commit
			transaction.commit();
			sess.close();
			return "list";
		
		}
		catch (SaveImageException se)
		{
			if (transaction != null) transaction.rollback();
			if (sess != null) sess.close();
			setErrorText(se.getMessage());
			return null;
		}
		catch (DataException de)
		{
			if (transaction != null) transaction.rollback();
			if (sess != null) sess.close();
			setErrorText("Internal error accessing database. Sorry for the inconvenience.");
			return null;
		}

	}
	
	public String cancelEdit()
	{
		return "list";
	}
	
	public String deleteImage()
	{

		Session sess = HibernateUtil.getSession();
		Transaction transaction = sess.beginTransaction();
		
		sess.delete(image);

		transaction.commit();
		sess.close();
		
		return "list";
	}
	
	public String showUploadBox()
	{
		uploadBoxShown = true;
		return null;
	}
	
	public String hideUploadBox()
	{
		uploadBoxShown = false;
		return null;
	}
	
	public SelectItem[] getLanguages()
	{

		Language[] langs = Languages.getActive();
		
		SelectItem[] langSelItems = new SelectItem[langs.length + 1];
		langSelItems[0] = new SelectItem("", "-");
		
		for (int i = 0; i < langs.length; i++)
		{
			Language lang = langs[i];
			langSelItems[i+1] = new SelectItem(lang.getCode(), lang.getName());
		}
		return langSelItems;

	}
	
	private SelectItem[] loadCategories(boolean includeAll)
	{
		
		Session sess = HibernateUtil.getSession();;
		Transaction transaction = sess.beginTransaction();
		
		int extraItem = includeAll ? 1 : 0;
		
		List cats = ImageCategory.loadAll(sess);
		SelectItem[] catsSelItems = new SelectItem[cats.size() + extraItem];
		
		if (includeAll) catsSelItems[0] = new SelectItem("0", "all categories");
		
		int i = 0;
		for (Iterator iter = cats.iterator(); iter.hasNext();)
		{
			ImageCategory cat = (ImageCategory) iter.next();
			catsSelItems[i + extraItem] = new SelectItem(cat.getId().toString(), cat.getName());
			i++;
		}
		
		transaction.commit();
		sess.close();
		
		return catsSelItems;

	}

	public SelectItem[] getDetailCategories()
	{
		return loadCategories(false);
	}

	public SelectItem[] getListCategories()
	{
		return loadCategories(true);
	}
	
	public String getListStyle()
	{
		return listStyle;
	}

	public void setListStyle(String listStyle)
	{
		this.listStyle = listStyle;
	}

	public Image getImage()
	{
		return image;
	}
	
	public String getImageUrl()
	{
		if (image.getFileName() != null)
		{
			return "images-database/" + image.getFileName();
		}
		else
		{
			return "blank.png";
		}
	}

	public UploadedFile getUploadedImage()
	{
		return uploadedImage;
	}

	public void setUploadedImage(UploadedFile uploadedImage)
	{
		this.uploadedImage = uploadedImage;
	}
	
	public String getImageInfo()
	{
		if (image.getMimeType() == null)
			return "";
		else
			return image.getWidth() + "x" + image.getHeight() +
			" (" + image.getMimeType() + ")";
	}
	
	public SelectItem[] createListItemsByStringArray(String[] array)
	{
		SelectItem[] items = new SelectItem[array.length];
		for (int i = 0; i < array.length; i++)
		{
			items[i] = new SelectItem(String.valueOf(i), array[i]);
		}
		return items;
	}

	public SelectItem[] getImageStatusItems()
	{
		return createListItemsByStringArray(IMAGE_STATUS_LABELS);
	}

	public SelectItem[] getAuthorizationStatusItems()
	{
		return createListItemsByStringArray(AUTH_STATUS_LONG_LABELS);
	}

	public boolean isUploadBoxShown()
	{
		return uploadBoxShown;
	}

	public void setUploadBoxShown(boolean uploadBoxShown)
	{
		this.uploadBoxShown = uploadBoxShown;
	}
	
//	public int getListThumbnailWidth()
//	{
//		return AppConfig.getConfiguration().getInt(AppConfig.IMAGES_THUMBNAIL_WIDTH);
//	}
//
//	public int getListThumbnailHeight()
//	{
//		return AppConfig.getConfiguration().getInt(AppConfig.IMAGES_THUMBNAIL_HEIGHT);
//	}
	
	public String getRegionsLookupSourceId()
	{
		return IMAGES_REGIONS_LOOKUP;
	}

	public String getPortsLookupSourceId()
	{
		return IMAGES_PORTS_LOOKUP;
	}

	public String getPeopleLookupSourceId()
	{
		return IMAGES_PEOPLE_LOOKUP;
	}

	public String[] getSelectedPortsIds()
	{
		return selectedPortsIds;
	}

	public void setSelectedPortsIds(String[] selectedPortsIds)
	{
		this.selectedPortsIds = selectedPortsIds;
	}

	public String[] getSelectedRegionsIds()
	{
		return selectedRegionsIds;
	}

	public void setSelectedRegionsIds(String[] selectedRegionsIds)
	{
		this.selectedRegionsIds = selectedRegionsIds;
	}

	public String getErrorText()
	{
		return errorText;
	}

	public void setErrorText(String errorText)
	{
		this.errorText = errorText;
	}

	public boolean isError()
	{
		return !StringUtils.isNullOrEmpty(errorText);
	}

	public String getSelectedImageId()
	{
		return selectedImageId;
	}

	public void setSelectedImageId(String selectedImageId)
	{
		this.selectedImageId = selectedImageId;
	}

	public String getThumbnailSize()
	{
		return thumbnailSize;
	}

	public void setThumbnailSize(String thumbnailSize)
	{
		this.thumbnailSize = thumbnailSize;
	}

	public String getSearchFor()
	{
		return searchFor;
	}

	public void setSearchFor(String searchInListFor)
	{
		this.searchFor = searchInListFor;
	}

	public String getSortBy()
	{
		return sortBy;
	}

	public void setSortBy(String sortBy)
	{
		this.sortBy = sortBy;
	}

	public long getImageCategoryId()
	{
		return imageCategoryId;
	}

	public void setImageCategoryId(long imageCategoryId)
	{
		this.imageCategoryId = imageCategoryId;
	}

	public long getListCategoryId()
	{
		return listCategoryId;
	}

	public void setListCategoryId(long listCategoryId)
	{
		this.listCategoryId = listCategoryId;
	}

}
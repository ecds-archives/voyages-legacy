package edu.emory.library.tast.images.admin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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
import edu.emory.library.tast.common.LookupSources;
import edu.emory.library.tast.db.HibernateConn;
import edu.emory.library.tast.dm.Image;
import edu.emory.library.tast.dm.ImageCategory;
import edu.emory.library.tast.dm.Port;
import edu.emory.library.tast.dm.Region;
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
	
	private int editedImageId;
	private String imageExternalId;
	private String imageTitle;
	private String imageDescription;
	private String imageCreator;
	private String imageSource;
	private int imageDate;
	private String imageLanguage;
	private String imageComments;
	private String imageReferences;
	private boolean imageEmory;
	private String imageEmoryLocation;
	private int imageAuthorizationStatus;
	private int imageImageStatus;
	private boolean imageReadyToGo;
	private long imageCategoryId;
	private String imageVoyageIds;
	private int imageWidth;
	private int imageHeight;
	private int imageSize;
	private String imageFileName;
	private String imageMimeType;
	private String[] selectedRegionsIds;
	private String[] selectedPortsIds;

	private UploadedFile uploadedImage;
	private boolean uploadBoxShown;
	
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
		Session sess = HibernateConn.getSession();
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
		
		editedImageId = Integer.parseInt(selectedImageId);
		
		// we come from list -> detail
		// so save the position in the list
		saveScrollPosition();
		
		// open db
		Session sess = HibernateConn.getSession();
		Transaction transaction = sess.beginTransaction();
		
		// load image itself
		Image image = Image.loadById(editedImageId, sess);
		
		// load basic info to bean members
		imageTitle = image.getTitle();
		imageDescription = image.getDescription();
		imageExternalId = image.getExternalId();
		imageSource = image.getSource();
		imageDate = image.getDate();
		imageCreator = image.getCreator();
		imageLanguage = image.getLanguage();
		imageComments = image.getComments();
		imageReferences = image.getReferences();
		imageEmory = image.isEmory();
		imageEmoryLocation = image.getEmoryLocation();
		imageAuthorizationStatus = image.getAuthorizationStatus();
		imageImageStatus = image.getImageStatus();
		imageReadyToGo = image.isReadyToGo();
		imageCategoryId = image.getCategory().getId().intValue();
		
		// image properties
		imageWidth = image.getWidth();
		imageHeight = image.getHeight();
		imageSize = image.getSize();
		imageFileName = image.getFileName();
		imageMimeType = image.getMimeType();
		
		// linked voyage IDs
		imageVoyageIds = StringUtils.join("\n", image.getVoyageIds());

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
		
		editedImageId = 0;
		selectedImageId = null;
		
		// we come from list -> detail
		// so save the position in the list
		saveScrollPosition();
		
		// init values
		imageTitle = "";
		imageDescription = "";
		imageSource = "";
		imageDate = 0;
		imageCreator = "";
		imageLanguage = "en";
		imageComments = "";
		imageReferences = "";
		imageEmory = false;
		imageEmoryLocation = "";
		imageAuthorizationStatus = 0;
		imageImageStatus = 0;
		imageReadyToGo = false;
		imageCategoryId = 0;
		
		imageFileName = null;
		imageWidth = 0;
		imageHeight = 0;
		imageSize = 0;
		imageMimeType = null;

		// clean the list of regions
		selectedRegionsIds = new String[0];

		// clean the list of ports
		selectedPortsIds = new String[0];
		
		// linked voyage IDs
		imageVoyageIds = "";

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
			imgFileStream.flush();
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
			imageWidth = width;
			imageHeight = height;
			imageSize = size;
			imageMimeType = uploadedImage.getContentType();
			imageFileName = fileName;
			
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
			sess = HibernateConn.getSession();
			transaction = sess.beginTransaction();
			
			// load image
			Image image = null;
			if (selectedImageId != null) {
				image = Image.loadById(Integer.parseInt(selectedImageId), sess);
			} else {
				image = new Image();
			}
			
			// basic image metadata
			image.setTitle(imageTitle);
			image.setSource(imageSource);
			image.setCreator(imageCreator);
			image.setReferences(imageReferences);
			image.setEmoryLocation(imageEmoryLocation);
			image.setExternalId(imageExternalId);
			
			// we will use it often
			Configuration appConf = AppConfig.getConfiguration();
			
			// image properties
			image.setWidth(imageWidth);
			image.setHeight(imageHeight);
			image.setSize(imageSize);
			image.setFileName(imageFileName);
			image.setMimeType(imageMimeType);
			
			// title
			String titleLocal = checkTextField(
					image.getTitle(), "title",
					appConf.getInt(AppConfig.IMAGES_TITLE_MAXLEN), false);
			image.setTitle(titleLocal);
			
			// description
			image.setDescription(imageDescription);
			
			// category
			ImageCategory cat = ImageCategory.loadById(sess, imageCategoryId);
			image.setCategory(cat);

			// check source
			String sourceLocal = checkTextField(
					image.getSource(), "source",
					appConf.getInt(AppConfig.IMAGES_SOURCE_MAXLEN), true);
			image.setSource(sourceLocal);
			
			// date
			image.setDate(imageDate);
			
			// check creator
			String creatorLocal = checkTextField(
					image.getCreator(), "creator",
					appConf.getInt(AppConfig.IMAGES_CREATOR_MAXLEN), true);
			image.setCreator(creatorLocal);
			
			// language
			image.setLanguage(imageLanguage);
			
			// comments
			image.setComments(imageComments);
			
			// check references
			String referencesLocal = checkTextField(
					image.getReferences(), "references",
					appConf.getInt(AppConfig.IMAGES_REFERENCES_MAXLEN), true);
			image.setReferences(referencesLocal);
			
			// is at Emory
			image.setEmory(imageEmory);
			
			// check emory location
			String emoryLocationLocal = checkTextField(
					image.getEmoryLocation(), "Emory location",
					appConf.getInt(AppConfig.IMAGES_EMORYLOCATION_MAXLEN), true);
			image.setEmoryLocation(emoryLocationLocal);
			
			// authorization status
			image.setAuthorizationStatus(imageAuthorizationStatus);
			
			// image status
			image.setImageStatus(imageImageStatus);
			
			// image ready to go
			image.setReadyToGo(imageReadyToGo);

			// links to regions (not shown now)
			Set imageRegions = new HashSet();
			image.setRegions(imageRegions);
			for (int i = 0; i < selectedRegionsIds.length; i++)
			{
				int regionId = Integer.parseInt(selectedRegionsIds[i]);
				Region dbRegion = Region.loadById(sess, regionId);
				imageRegions.add(dbRegion);
			}
			
			// links to ports (not shown now)
			Set imagePorts = new HashSet();
			image.setPorts(imagePorts);
			for (int i = 0; i < selectedPortsIds.length; i++)
			{
				int portId = Integer.parseInt(selectedPortsIds[i]);
				Port dbPort = Port.loadById(sess, portId);
				imagePorts.add(dbPort);
			}
	
			// links to voyages
			Integer[] newVoyageIds;
			try
			{
				newVoyageIds = StringUtils.parseIntegerArray(StringUtils.splitByLinesAndRemoveEmpty(imageVoyageIds));
			}
			catch (NumberFormatException nfe)
			{
				throw new SaveImageException("All linked voyage IDs have to be integers.");
			}
			Set voyageIds = image.getVoyageIds();
			voyageIds.clear();
			Collections.addAll(voyageIds, newVoyageIds);
			
			// save
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
			setErrorText("Internal problem with database. Sorry for the inconvenience.");
			return null;
		}

	}
	
	public String cancelEdit()
	{
		return "list";
	}
	
	public String deleteImage()
	{

		Session sess = HibernateConn.getSession();
		Transaction transaction = sess.beginTransaction();
		
		Image image = Image.loadById(Integer.parseInt(selectedImageId));
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
		
		Session sess = HibernateConn.getSession();;
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

	public String getImageUrl()
	{
		if (imageFileName != null)
		{
			String baseUrl = AppConfig.getConfiguration().getString(AppConfig.IMAGES_URL);
			baseUrl = StringUtils.trimEnd(baseUrl, '/');
			return baseUrl + "/" + imageFileName;
		}
		else
		{
			return "blank.png";
		}
	}
	
	public String getImageVoyageIds()
	{
		return imageVoyageIds;
	}

	public void setImageVoyageIds(String imageVoyageIds)
	{
		this.imageVoyageIds = imageVoyageIds;
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
		if (imageMimeType == null)
			return "";
		else
			return imageWidth + "x" + imageHeight +
			" (" + imageMimeType + ")";
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

	public String getImageCreator()
	{
		return imageCreator;
	}

	public void setImageCreator(String imageCreator)
	{
		this.imageCreator = imageCreator;
	}

	public int getImageDate()
	{
		return imageDate;
	}

	public void setImageDate(int imageDate)
	{
		this.imageDate = imageDate;
	}

	public String getImageDescription()
	{
		return imageDescription;
	}

	public void setImageDescription(String imageDescription)
	{
		this.imageDescription = imageDescription;
	}

	public String getImageLanguage()
	{
		return imageLanguage;
	}

	public void setImageLanguage(String imageLanguage)
	{
		this.imageLanguage = imageLanguage;
	}

	public String getImageSource()
	{
		return imageSource;
	}

	public void setImageSource(String imageSource)
	{
		this.imageSource = imageSource;
	}

	public String getImageTitle()
	{
		return imageTitle;
	}

	public void setImageTitle(String imageTitle)
	{
		this.imageTitle = imageTitle;
	}

	public int getImageSize()
	{
		return imageSize;
	}

	public void setImageSize(int imageSize)
	{
		this.imageSize = imageSize;
	}

	public String getImageComments()
	{
		return imageComments;
	}

	public void setImageComments(String imageComments)
	{
		this.imageComments = imageComments;
	}

	public int getImageAuthorizationStatus()
	{
		return imageAuthorizationStatus;
	}

	public void setImageAuthorizationStatus(int imageAuthorizationStatus)
	{
		this.imageAuthorizationStatus = imageAuthorizationStatus;
	}

	public String getImageEmoryLocation()
	{
		return imageEmoryLocation;
	}

	public void setImageEmoryLocation(String imageEmoryLocation)
	{
		this.imageEmoryLocation = imageEmoryLocation;
	}

	public int getImageImageStatus()
	{
		return imageImageStatus;
	}

	public void setImageImageStatus(int imageImageStatus)
	{
		this.imageImageStatus = imageImageStatus;
	}

	public boolean isImageEmory()
	{
		return imageEmory;
	}

	public void setImageEmory(boolean imageIsEmory)
	{
		this.imageEmory = imageIsEmory;
	}

	public String getImageReferences()
	{
		return imageReferences;
	}

	public void setImageReferences(String imageReferences)
	{
		this.imageReferences = imageReferences;
	}

	public boolean isImageReadyToGo()
	{
		return imageReadyToGo;
	}

	public void setImageReadyToGo(boolean imageReadyToGo)
	{
		this.imageReadyToGo = imageReadyToGo;
	}

	public String getImageFileName()
	{
		return imageFileName;
	}

	public void setImageFileName(String imageFileName)
	{
		this.imageFileName = imageFileName;
	}

	public int getImageHeight()
	{
		return imageHeight;
	}

	public void setImageHeight(int imageHeight)
	{
		this.imageHeight = imageHeight;
	}

	public String getImageMimeType()
	{
		return imageMimeType;
	}

	public void setImageMimeType(String imageMimeType)
	{
		this.imageMimeType = imageMimeType;
	}

	public int getImageWidth()
	{
		return imageWidth;
	}

	public void setImageWidth(int imageWidth)
	{
		this.imageWidth = imageWidth;
	}

	public int getEditedImageId()
	{
		return editedImageId;
	}

	public void setEditedImageId(int editedImageId)
	{
		this.editedImageId = editedImageId;
	}

	public String getImageExternalId()
	{
		return imageExternalId;
	}

	public void setImageExternalId(String imageExternalId)
	{
		this.imageExternalId = imageExternalId;
	}

}
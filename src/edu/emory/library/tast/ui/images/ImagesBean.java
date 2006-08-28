package edu.emory.library.tast.ui.images;

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

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;

import org.apache.commons.io.IOUtils;
import org.apache.myfaces.custom.fileupload.UploadedFile;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.DataException;

import edu.emory.library.tast.AppConfig;
import edu.emory.library.tast.dm.Image;
import edu.emory.library.tast.dm.Person;
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
	
	private Map allowedTypes = new HashMap();

	private String listStyle = "table";
	private String selectedImageId;
	
	private Image image;
	private UploadedFile uploadedImage;
	private boolean uploadBoxShown;
	private String[] selectedRegionsIds;
	private String[] selectedPortsIds;
	private String[] selectedPeopleIds;
	private String errorText;
	
	public ImagesBean()
	{
		allowedTypes.put("image/gif", "GIF");
		allowedTypes.put("image/jpeg", "JPG");
		allowedTypes.put("image/png", "PNG");
	}
	
	public List getAllImages()
	{
		
		// open db
		Session sess = HibernateUtil.getSession();
		Transaction transaction = sess.beginTransaction();
		
		// load images from db and create a new list
		List dbImages = Image.getImagesList(sess);
		List uiImages = new ArrayList(dbImages.size());
		
		// move them to ui list
		for (Iterator iter = dbImages.iterator(); iter.hasNext();)
		{
			Image image = (Image) iter.next();
			ImageListItem uiImage = new ImageListItem();
			uiImage.setId(String.valueOf(image.getId()));
			uiImage.setName(image.getName());
			uiImage.setUrl("images/" + image.getThumbnailFileName());
			uiImage.setWidth(image.getWidth());
			uiImage.setHeight(image.getHeight());
			uiImages.add(uiImage);
		}
		
		// close db
		transaction.commit();
		sess.close();
		
		// return list of images
		return uiImages;
		
	}

	public String openImage()
	{
		
		// open db
		Session sess = HibernateUtil.getSession();
		Transaction transaction = sess.beginTransaction();
		
		// load image itself
		image = Image.loadById(Integer.parseInt(selectedImageId), sess);
		
		// selected regions
		int i = 0;
		selectedRegionsIds = new String[image.getRegions().size()];
		for (Iterator iter = image.getRegions().iterator(); iter.hasNext();)
		{
			Region dbRegion = (Region) iter.next();
			selectedRegionsIds[i++] = String.valueOf(dbRegion.getId());
		}
		
		// selected ports
		int j = 0;
		selectedPortsIds = new String[image.getPorts().size()];
		for (Iterator iter = image.getPorts().iterator(); iter.hasNext();)
		{
			Port dbPort = (Port) iter.next();
			selectedPortsIds[j++] = String.valueOf(dbPort.getId());
		}

		// selected people
		int k = 0;
		selectedPeopleIds = new String[image.getPeople().size()];
		for (Iterator iter = image.getPeople().iterator(); iter.hasNext();)
		{
			Person dbPerson = (Person) iter.next();
			selectedPeopleIds[k++] = String.valueOf(dbPerson.getId());
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
		
		// create an empty image
		image = new Image();
		
		// clean the list of regions
		selectedRegionsIds = new String[0];

		// clean the list of ports
		selectedPortsIds = new String[0];

		// clean the list of people
		selectedPeopleIds = new String[0];
		
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

		// register people lookup source
		LookupSources.registerLookupSource(
				IMAGES_PEOPLE_LOOKUP,
				new LookupSourcePeople());
		
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
			IOUtils.copy(uploadedImage.getInputStream(), imgFileStream);
			imgFileStream.close();
			
			// get image info
			Iterator readerIter = ImageIO.getImageReadersByMIMEType(uploadedImage.getContentType());
			ImageReader rdr = (ImageReader) readerIter.next();
			if (rdr == null) return null;
			rdr.setInput(ImageIO.createImageInputStream(file), true);
			
			// get width and height
			int width = rdr.getWidth(0);
			int height = rdr.getHeight(0);
			
			// replace current image
			image.setWidth(width);
			image.setHeight(height);
			image.setMimeType(uploadedImage.getContentType());
			image.setFileName(fileName);
			
			// all ok
			uploadBoxShown = false;

		}
		catch (IOException e)
		{
		}
		
		return null;
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
	
			// links to people
			Set imagePeople = new HashSet();
			image.setPeople(imagePeople);
			for (int i = 0; i < selectedPeopleIds.length; i++)
			{
				int personId = Integer.parseInt(selectedPeopleIds[i]);
				Person dbPerson = Person.loadById(sess, personId);
				imagePeople.add(dbPerson);
			}

			// links check name
			if (StringUtils.isNullOrEmpty(image.getName(), true))
				throw new SaveImageException("Please specify image name.");

			// save
			image.setName(image.getName().trim());
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
			return "images/" + image.getFileName();
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
		return image.getWidth() + "x" + image.getHeight() +
			" (" + image.getMimeType() + ")";
	}

	public boolean isUploadBoxShown()
	{
		return uploadBoxShown;
	}

	public void setUploadBoxShown(boolean uploadBoxShown)
	{
		this.uploadBoxShown = uploadBoxShown;
	}
	
	public int getListThumbnailWidth()
	{
		return AppConfig.getConfiguration().getInt(AppConfig.IMAGES_THUMBNAIL_WIDTH);
	}

	public int getListThumbnailHeight()
	{
		return AppConfig.getConfiguration().getInt(AppConfig.IMAGES_THUMBNAIL_HEIGHT);
	}
	
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

	public String[] getSelectedPeopleIds()
	{
		return selectedPeopleIds;
	}

	public void setSelectedPeopleIds(String[] selectedPeopleIds)
	{
		this.selectedPeopleIds = selectedPeopleIds;
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

}
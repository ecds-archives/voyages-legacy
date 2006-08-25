package edu.emory.library.tast.ui.images;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
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

public class ImagesBean
{
	
	private static final String IMAGES_REGIONS_LOOKUP = "images-regions-lookup";
	private static final String IMAGES_PORTS_LOOKUP = "images-ports-lookup";
	private static final String IMAGES_PEOPLE_LOOKUP = "images-people-lookup";
	
	private String listStyle = "table";
	private UploadedFile uploadedImage;
	private Image image;
	private boolean uploadBoxShown;
	private String[] selectedRegionsIds;
	private String[] selectedPortsIds;
	private String[] selectedPeopleIds;
	
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

	public void openImageListener(ImageSelectedEvent event)
	{
		
		// open db
		Session sess = HibernateUtil.getSession();
		Transaction transaction = sess.beginTransaction();
		
		// load image itself
		image = Image.loadById(Integer.parseInt(event.getImageId()), sess);
		
//		// regions/ports lists
//		List dbRegions = Region.getRegionsList(sess);
//		availRegions = new ArrayList();
//		availPorts = new ArrayList();
//		selectedRegions = new ArrayList();
//		selectedPorts = new ArrayList();
//		
//		// fill regions/ports lists
//		int i = 0, j = 0;
//		for (Iterator iter = dbRegions.iterator(); iter.hasNext();)
//		{
//			Region dbRegion = (Region) iter.next();
//			SelectItem uiRegion = new SelectItem(
//					dbRegion.getName(),
//					String.valueOf(dbRegion.getId()), i++);
//			
//			if (image.getRegions().contains(dbRegion))
//				selectedRegions.add(uiRegion);
//			else
//				availRegions.add(uiRegion);
//
//			for (Iterator iterator = dbRegion.getPorts().iterator(); iterator.hasNext();)
//			{
//				Port dbPort = (Port) iterator.next();
//				SelectItem uiPort = new SelectItem(
//						dbRegion.getName() + " / " + dbPort.getName(),
//						String.valueOf(dbPort.getId()), j++);
//				
//				if (image.getPorts().contains(dbPort))
//					selectedPorts.add(uiPort);
//				else
//					availPorts.add(uiPort);
//
//			}
//		}
//		
//		// load all people
//		List dbPeople = Person.getPeopleList(sess);
//		availPeople = new ArrayList(dbPeople.size());
//		selectedPeople = new ArrayList(dbPeople.size());
//
//		// fill people list
//		int k = 0;
//		for (Iterator iter = dbPeople.iterator(); iter.hasNext();)
//		{
//			Person dbPerson = (Person) iter.next();
//			SelectItem uiPerson = new SelectItem(
//					dbPerson.getLastName(),
//					String.valueOf(dbPerson.getId()), k++);
//			
//			if (image.getPeople().contains(dbPerson))
//				selectedPeople.add(uiPerson);
//			else
//				availPeople.add(uiPerson);
//
//		}
		
		// selected regions
		int i = 0;
		selectedRegionsIds = new String[image.getRegions().size()];
		for (Iterator iter = image.getRegions().iterator(); iter.hasNext();)
		{
			Region dbRegion = (Region) iter.next();
			selectedRegionsIds[i++] = String.valueOf(dbRegion.getId());
			System.out.println(dbRegion.getId());
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

		// close db
		transaction.commit();
		sess.close();
		
	}

	public String openImageAction()
	{
		return "detail";
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
	
	public String uploadNewImage()
	{
		
		if (uploadedImage == null)
			return null;
		
		try
		{
			
			// new filename
			String fileName = "xxx.png";
			
			// copy
			File file = new File(AppConfig.getConfiguration().getString(AppConfig.IMAGES_DIRECTORY), fileName);
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
		
		Session sess = HibernateUtil.getSession();
		Transaction transaction = sess.beginTransaction();

		Set imageRegions = new HashSet();
		image.setRegions(imageRegions);
		
		for (int i = 0; i < selectedRegionsIds.length; i++)
		{
			int regionId = Integer.parseInt(selectedRegionsIds[i]);
			Region dbRegion = Region.loadById(sess, regionId);
			imageRegions.add(dbRegion);
		}
		
		Set imagePorts = new HashSet();
		image.setPorts(imagePorts);

		for (int i = 0; i < selectedPortsIds.length; i++)
		{
			int portId = Integer.parseInt(selectedPortsIds[i]);
			Port dbPort = Port.loadById(sess, portId);
			imagePorts.add(dbPort);
		}

		Set imagePeople = new HashSet();
		image.setPeople(imagePeople);

		for (int i = 0; i < selectedPeopleIds.length; i++)
		{
			int personId = Integer.parseInt(selectedPeopleIds[i]);
			Person dbPerson = Person.loadById(sess, personId);
			imagePeople.add(dbPerson);
		}

		try
		{
			
//			String name = StringUtils.trimAndUnNull(attributeName, getMaxNameLength());
//			if (name.length() == 0)
//				throw new SaveException("Please specify attribute name.");
//			
//			String userLabel = StringUtils.trimAndUnNull(attributeUserLabel, getMaxUserLabelLength());
//			if (userLabel.length() == 0)
//				throw new SaveException("Please specify label.");
//	
//			String description = StringUtils.trimAndUnNull(attributeDescription, getMaxDescriptionLength());
//			if (description.length() > getMaxDescriptionLength())
//				throw new SaveException("Description is limited to " + getMaxDescriptionLength() + " characters.");
//
//			attribute.setName(name);
//			attribute.setUserLabel(userLabel);
//			attribute.setDescription(description);
//			attribute.setCategory(new Integer(attributeCategory));
//			attribute.setVisible(new Boolean(attributeVisible));
			
			sess.saveOrUpdate(image);
			
			transaction.commit();
			sess.close();
			return "list";
		
		}
//		catch (SaveException se)
//		{
//			transaction.rollback();
//			session.close();
//			setErrorText(se.getMessage());
//			return null;
//		}
		catch (DataException de)
		{
			transaction.rollback();
			sess.close();
			//setErrorText("Internal error accessing database. Sorry for the inconvenience.");
			return null;
		}

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
		return "images/" + image.getFileName();
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
		return image.getWidth() + "x" + image.getHeight();
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

//	public List getAvailablePeople()
//	{
//		return availPeople;
//	}
//
//	public void setAvailablePeople(List availPeople)
//	{
//		this.availPeople = availPeople;
//	}
//
//	public List getSelectedPeople()
//	{
//		return selectedPeople;
//	}
//
//	public void setSelectedPeople(List selectedPeople)
//	{
//		this.selectedPeople = selectedPeople;
//	}
//	
//	public String getPersonSearchString()
//	{
//		return personSearchString;
//	}
//
//	public void setPersonSearchString(String personSearchString)
//	{
//		this.personSearchString = personSearchString;
//	}
//
//	public String personSearch()
//	{
//		
//		// open db
//		Session sess = HibernateUtil.getSession();
//		Transaction transaction = sess.beginTransaction();
//		
//		// load all people
//		List dbPeople = Person.getPeopleList(personSearchString + "%");
//		filteredPeople = new ArrayList(dbPeople.size());
//
//		// fill people list
//		for (Iterator iter = dbPeople.iterator(); iter.hasNext();)
//		{
//			Person dbPerson = (Person) iter.next();
//			javax.faces.model.SelectItem item = new javax.faces.model.SelectItem();
//			item.setValue(String.valueOf(dbPerson.getId()));
//			item.setLabel(dbPerson.getLastName());
//			filteredPeople.add(item);
//		}
//		
//		// close db
//		transaction.commit();
//		sess.close();
//		
//		return null;
//	}
//
//	public List getFilteredPeople()
//	{
//		return filteredPeople;
//	}
//
//	public void setFilteredPeople(List filteredPeople)
//	{
//		this.filteredPeople = filteredPeople;
//	}
//	
//	public String addSelectedPersons()
//	{
//		selectedPeople.add(null);
//		return null;
//	}

}
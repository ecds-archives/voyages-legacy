package edu.emory.library.tast.ui.images;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
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
import edu.emory.library.tast.ui.SelectItem;
import edu.emory.library.tast.util.HibernateUtil;

public class ImagesBean
{
	
	private String listStyle = "table";
	private UploadedFile uploadedImage;
	private Image image;
	private boolean uploadBoxShown;
	private List availRegions;
	private List availPorts;
	private List availPeople;
	private List selectedRegions;
	private List selectedPorts;
	private List selectedPeople;
	
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
		
		// regions/ports lists
		List dbRegions = Region.getRegionsList(sess);
		availRegions = new ArrayList();
		availPorts = new ArrayList();
		selectedRegions = new ArrayList();
		selectedPorts = new ArrayList();
		
		// fill regions/ports lists
		int i = 0, j = 0;
		for (Iterator iter = dbRegions.iterator(); iter.hasNext();)
		{
			Region dbRegion = (Region) iter.next();
			SelectItem uiRegion = new SelectItem(
					dbRegion.getName(),
					String.valueOf(dbRegion.getId()), i++);
			
			if (image.getRegions().contains(dbRegion))
				selectedRegions.add(uiRegion);
			else
				availRegions.add(uiRegion);

			for (Iterator iterator = dbRegion.getPorts().iterator(); iterator.hasNext();)
			{
				Port dbPort = (Port) iterator.next();
				SelectItem uiPort = new SelectItem(
						dbRegion.getName() + " / " + dbPort.getName(),
						String.valueOf(dbPort.getId()), j++);
				
				if (image.getPorts().contains(dbPort))
					selectedPorts.add(uiPort);
				else
					availPorts.add(uiPort);

			}
		}
		
		// load all people
		List dbPeople = Person.getPeopleList(sess);
		availPeople = new ArrayList();
		selectedPeople = new ArrayList();

		// fill people list
		int k = 0;
		for (Iterator iter = dbPeople.iterator(); iter.hasNext();)
		{
			Person dbPerson = (Person) iter.next();
			SelectItem uiPerson = new SelectItem(
					dbPerson.getLastName(),
					String.valueOf(dbPerson.getId()), k++);
			
			if (image.getPeople().contains(dbPerson))
				selectedPeople.add(uiPerson);
			else
				availPeople.add(uiPerson);

		}
		
		// close db
		transaction.commit();
		sess.close();
		
	}

//	private void loadRegionsAndPortsToUI()
//	{
//		
//		List dbRegions = Region.getRegionsList();
//		
//		availRegions = new ArrayList();
//		availPorts = new ArrayList();
//		selectedRegions = new ArrayList();
//		selectedPorts = new ArrayList();
//		
//		int i = 0, j = 0;
//		for (Iterator iter = dbRegions.iterator(); iter.hasNext();)
//		{
//			Region region = (Region) iter.next();
//			SelectItem regionItem = new SelectItem(
//					region.getName(),
//					String.valueOf(region.getId()), i++);
//			
//			if (image.getRegions().contains(region))
//				selectedRegions.add(regionItem);
//			else
//				availRegions.add(regionItem);
//
//			for (Iterator iterator = region.getPorts().iterator(); iterator.hasNext();)
//			{
//				Port port = (Port) iterator.next();
//				SelectItem portItem = new SelectItem(
//						region.getName() + " / " + port.getName(),
//						String.valueOf(port.getId()), j++);
//				
//				if (image.getPorts().contains(port))
//					selectedPorts.add(portItem);
//				else
//					availPorts.add(portItem);
//
//			}
//		}
//
//	}

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
		
		Session session = HibernateUtil.getSession();
		Transaction transaction = session.beginTransaction();

		Set imageRegions = image.getRegions();
		imageRegions.clear();

		for (Iterator iter = selectedRegions.iterator(); iter.hasNext();)
		{
			SelectItem regionItem = (SelectItem) iter.next();
			Region region = Region.loadById(Integer.parseInt(regionItem.getValue()) , session);
			imageRegions.add(region);
		}
		
		Set imagePorts = image.getPorts();
		imagePorts.clear();

		for (Iterator iter = selectedPorts.iterator(); iter.hasNext();)
		{
			SelectItem portItem = (SelectItem) iter.next(); 
			Port port = Port.loadById(Integer.parseInt(portItem.getValue()) , session);
			imagePorts.add(port);
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
			
			session.saveOrUpdate(image);
			
			transaction.commit();
			session.close();
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
			session.close();
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
	
	public List getAvailableRegions()
	{
		return availRegions;
	}

	public void setAvailableRegions(List availRegions)
	{
		this.availRegions = availRegions;
	}

	public List getSelectedRegions()
	{
		return selectedRegions;
	}

	public void setSelectedRegions(List selectedRegions)
	{
		this.selectedRegions = selectedRegions;
	}

	public List getAvailablePorts()
	{
		return availPorts;
	}

	public void setAvailablePorts(List availPorts)
	{
		this.availPorts = availPorts;
	}

	public List getSelectedPorts()
	{
		return selectedPorts;
	}

	public void setSelectedPorts(List selectedPorts)
	{
		this.selectedPorts = selectedPorts;
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

	public List getAvailablePeople()
	{
		return availPeople;
	}

	public void setAvailablePeople(List availPeople)
	{
		this.availPeople = availPeople;
	}

	public List getSelectedPeople()
	{
		return selectedPeople;
	}

	public void setSelectedPeople(List selectedPeople)
	{
		this.selectedPeople = selectedPeople;
	}

}

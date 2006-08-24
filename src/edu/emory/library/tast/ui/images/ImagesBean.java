package edu.emory.library.tast.ui.images;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;

import org.apache.commons.io.IOUtils;
import org.apache.myfaces.custom.fileupload.UploadedFile;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.DataException;

import edu.emory.library.tast.AppConfig;
import edu.emory.library.tast.dm.Image;
import edu.emory.library.tast.util.HibernateUtil;

public class ImagesBean
{
	
	private String listStyle = "table";
	private UploadedFile uploadedImage;
	private Image image;
	private boolean uploadBoxShown;
	
	public List getAllImages()
	{
		return Image.getImagesList();
	}

	public String getListStyle()
	{
		return listStyle;
	}

	public void setListStyle(String listStyle)
	{
		this.listStyle = listStyle;
	}
	
	public void openImageListener(ImageSelectedEvent event)
	{
		image = Image.loadById(event.getImageId()); 
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
		
		Session session = HibernateUtil.getSession();
		Transaction transaction = session.beginTransaction();

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

}

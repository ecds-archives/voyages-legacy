package edu.emory.library.tast.ui.images;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.DataException;

import edu.emory.library.tast.dm.Image;
import edu.emory.library.tast.util.HibernateUtil;

public class ImagesBean
{
	
	private String listStyle = "table";
	private String uploadedImage;
	private Image image;
	
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

	public Image getImage()
	{
		return image;
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

	public String getUploadedImage()
	{
		return uploadedImage;
	}

	public void setUploadedImage(String uploadedImage)
	{
		this.uploadedImage = uploadedImage;
	}
	
}

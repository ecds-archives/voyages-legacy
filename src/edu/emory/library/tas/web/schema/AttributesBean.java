package edu.emory.library.tas.web.schema;

import javax.faces.component.UIParameter;
import javax.faces.event.ActionEvent;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.DataException;

import edu.emory.library.tas.Slave;
import edu.emory.library.tas.Voyage;
import edu.emory.library.tas.attrGroups.Attribute;
import edu.emory.library.tas.util.HibernateUtil;
import edu.emory.library.tas.util.StringUtils;

public class AttributesBean extends SchemaEditBeanBase
{
	
	private Long attributeId;
	private String attributeUserLabel;
	private String attributeName;
	private String attributeDescription;
	
	private class SaveException extends Exception
	{
		private static final long serialVersionUID = -1049756863527182663L;
		public SaveException(String msg)
		{
			super(msg);
		}
	}
	
	public Object[] getAttributes()
	{
		if (editingVoyages())
			return Voyage.getAttributes();
		else if (editingSlaves())
			return Slave.getAttributes();
		else
			return null;
	}
	
	public void editAttribute(ActionEvent event)
	{
		
		saveScrollPosition();
		setErrorText(null);
		
		UIParameter groupIdParam = (UIParameter) event.getComponent().findComponent("attributeId");
		if (groupIdParam == null) return;
		
		Attribute attribute = (Attribute) Attribute.loadById((Long) groupIdParam.getValue());
		attributeId = attribute.getId();
		setAttributeUserLabel(attribute.getUserLabel());
		setAttributeName(attribute.getName());
		setAttributeDescription(attribute.getDescription());
		
	}
	
	public String saveAttribute()
	{

		Session session = HibernateUtil.getSession();
		Transaction transaction = session.beginTransaction();

		Attribute attribute;
		if (attributeId != null)
			attribute = (Attribute) Attribute.loadById(attributeId, session);	
		else if (editingVoyages())
			attribute = Attribute.newForVoyages(session);	
		else 
			attribute = Attribute.newForSlaves(session);	

		try
		{
			
			String name = StringUtils.trimAndUnNull(attributeName, getMaxNameLength());
			if (name.length() == 0)
				throw new SaveException("Please specify attribute name.");
			
			String userLabel = StringUtils.trimAndUnNull(attributeUserLabel, getMaxUserLabelLength());
			if (userLabel.length() == 0)
				throw new SaveException("Please specify label.");
	
			String description = StringUtils.trimAndUnNull(attributeDescription, getMaxDescriptionLength());
			if (description.length() > getMaxDescriptionLength())
				throw new SaveException("Description is limited to " + getMaxDescriptionLength() + " characters.");

			attribute.setName(name);
			attribute.setUserLabel(userLabel);
			attribute.setDescription(description);
			
			session.saveOrUpdate(attribute);
			
			transaction.commit();
			session.close();
			clearEditResouces();
			return "back";
		
		}
		catch (SaveException se)
		{
			transaction.rollback();
			session.close();
			setErrorText(se.getMessage());
			return null;
		}
		catch (DataException de)
		{
			transaction.rollback();
			session.close();
			setErrorText("Internal error accessing database. Sorry for the inconvenience.");
			return null;
		}
		
	}
	
	public String cancelEdit()
	{
		clearEditResouces();
		return "back";
	}

	private void clearEditResouces()
	{
		attributeId = null;
		attributeName = null;
		attributeUserLabel = null;
		attributeDescription = null;
		setErrorText(null);
	}

	public String getAttributeUserLabel()
	{
		return attributeUserLabel;
	}

	public void setAttributeUserLabel(String selectedGroupUserLabel)
	{
		this.attributeUserLabel = selectedGroupUserLabel;
	}

	public String getAttributeName()
	{
		return attributeName;
	}

	public void setAttributeName(String selectedGroupUserName)
	{
		this.attributeName = selectedGroupUserName;
	}

	public String getAttributeDescription()
	{
		return attributeDescription;
	}

	public void setAttributeDescription(String attributeDescription)
	{
		this.attributeDescription = attributeDescription;
	}

}

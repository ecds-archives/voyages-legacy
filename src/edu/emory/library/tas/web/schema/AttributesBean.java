package edu.emory.library.tas.web.schema;

import java.util.Arrays;
import java.util.Iterator;

import javax.faces.component.UIParameter;
import javax.faces.event.ActionEvent;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.DataException;

import edu.emory.library.tas.Slave;
import edu.emory.library.tas.Voyage;
import edu.emory.library.tas.attrGroups.AbstractAttribute;
import edu.emory.library.tas.attrGroups.Attribute;
import edu.emory.library.tas.attrGroups.CompoundAttribute;
import edu.emory.library.tas.attrGroups.Group;
import edu.emory.library.tas.util.HibernateUtil;
import edu.emory.library.tas.util.StringUtils;

public class AttributesBean extends SchemaEditBeanBase
{
	
	private Long attributeId;
	private String attributeUserLabel;
	private String attributeName;
	private String attributeDescription;
	private int attributeCategory = AbstractAttribute.CATEGORY_BEGINNER;
	private boolean attributeVisible = true;
	
	private int listSortBy = AbstractAttributeForDisplay.SORT_BY_NAME;
	private boolean listSortAsc = true;
	
	private class SaveException extends Exception
	{
		private static final long serialVersionUID = -1049756863527182663L;
		public SaveException(String msg)
		{
			super(msg);
		}
	}
	
	public String sortByName()
	{
		if (listSortBy == AbstractAttributeForDisplay.SORT_BY_NAME) listSortAsc = !listSortAsc;
		listSortBy = AbstractAttributeForDisplay.SORT_BY_NAME;
		return null;
	}
	
	public String sortByLabel()
	{
		if (listSortBy == AbstractAttributeForDisplay.SORT_BY_LABEL) listSortAsc = !listSortAsc;
		listSortBy = AbstractAttributeForDisplay.SORT_BY_LABEL;
		return null;
	}

	public String sortByType()
	{
		if (listSortBy == AbstractAttributeForDisplay.SORT_BY_TYPE) listSortAsc = !listSortAsc;
		listSortBy = AbstractAttributeForDisplay.SORT_BY_TYPE;
		return null;
	}

	public String sortByVisibility()
	{
		if (listSortBy == AbstractAttributeForDisplay.SORT_BY_VISIBILITY) listSortAsc = !listSortAsc;
		listSortBy = AbstractAttributeForDisplay.SORT_BY_VISIBILITY;
		return null;
	}

	public String sortByCategory()
	{
		if (listSortBy == AbstractAttributeForDisplay.SORT_BY_CATEGORY) listSortAsc = !listSortAsc;
		listSortBy = AbstractAttributeForDisplay.SORT_BY_CATEGORY;
		return null;
	}

	public AttributeForDisplay[] getAttributes()
	{
		
		Object[] attributes = null;
		Object[] compoundAttributes = null;
		Object[] groups = null;
		
		if (editingVoyages())
		{
			attributes = Voyage.getAttributes();
			compoundAttributes = Voyage.getCoumpoundAttributes();
			groups = Voyage.getGroups();
		}
		else if (editingSlaves())
		{
			attributes = Slave.getAttributes();
			compoundAttributes = Slave.getCoumpoundAttributes();
			groups = Slave.getGroups();
		}
		else
		{
			return null;
		}

		AbstractAttribute.sortByUserLabelOrName(compoundAttributes);
		Group.sortByUserLabelOrName(groups);
		AttributeForDisplay[] attributesForDisplay = new AttributeForDisplay[attributes.length];

		StringBuffer htmlCompAttrs = new StringBuffer();
		StringBuffer htmlGroups = new StringBuffer();
		StringBuffer htmlProxiedGroups = new StringBuffer();

		for (int i = 0; i < attributes.length; i++)
		{
			Attribute attr = (Attribute) attributes[i];
			
			htmlCompAttrs.setLength(0);
			for (int j = 0; j < compoundAttributes.length; j++)
			{
				CompoundAttribute compAttr = (CompoundAttribute) compoundAttributes[j];
				if (compAttr.getAttributes().contains(attr))
				{
					htmlCompAttrs.append("<div>");
					htmlCompAttrs.append(compAttr.getUserLabelOrName());
					htmlCompAttrs.append("</div>");
				}
			}
			
			htmlProxiedGroups.setLength(0);
			for (int j = 0; j < groups.length; j++)
			{
				Group group = (Group) groups[j];
				for (Iterator iter = group.getCompoundAttributes().iterator(); iter.hasNext();)
				{
					CompoundAttribute compAttr = (CompoundAttribute) iter.next();
					if (compAttr.getAttributes().contains(attr))
					{
						htmlProxiedGroups.append("<div>");
						htmlProxiedGroups.append(group.getUserLabelOrName());
						htmlProxiedGroups.append(" (by ");
						htmlProxiedGroups.append(compAttr.getUserLabelOrName());
						htmlProxiedGroups.append(")</div>");
					}
				}
			}

			htmlGroups.setLength(0);
			for (int j = 0; j < groups.length; j++)
			{
				Group group = (Group) groups[j];
				if (group.getAttributes().contains(attr))
				{
					htmlGroups.append("<div>");
					htmlGroups.append(group.getUserLabelOrName());
					htmlGroups.append("</div>");
				}
			}

			attributesForDisplay[i] = new AttributeForDisplay(attr);
			attributesForDisplay[i].setCompoundAttributesHTML(htmlCompAttrs.toString());
			attributesForDisplay[i].setGroupsHTML(htmlGroups.toString());
			attributesForDisplay[i].setProxiedGroupsHTML(htmlProxiedGroups.toString());

		}
		
		Arrays.sort(attributesForDisplay, new AbstractAttributeComparator(listSortBy, listSortAsc));
		return attributesForDisplay;
		
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
		setAttributeCategory(attribute.getCategory().intValue());
		setAttributeVisible(attribute.isVisible());
		
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
			attribute.setCategory(new Integer(attributeCategory));
			attribute.setVisible(new Boolean(attributeVisible));
			
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

	public int getAttributeCategory()
	{
		return attributeCategory;
	}

	public void setAttributeCategory(int attributeCategory)
	{
		this.attributeCategory = attributeCategory;
	}

	public boolean isAttributeVisible()
	{
		return attributeVisible;
	}

	public void setAttributeVisible(boolean attributeVisible)
	{
		this.attributeVisible = attributeVisible;
	}

}

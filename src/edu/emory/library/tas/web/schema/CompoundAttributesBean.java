package edu.emory.library.tas.web.schema;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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

public class CompoundAttributesBean extends SchemaEditBeanBase
{

	private Long attributeId;
	private String attributeUserLabel;
	private String attributeName;
	private String attributeDescription;
	private List availableAttributes = new ArrayList();
	private List attributeAttributes = new ArrayList();
	private boolean questionReallyDelete = false;
	private int attributeCategory = AbstractAttribute.CATEGORY_BEGINNER;
	private boolean attributeVisible = true;
	
	private class SaveException extends Exception
	{
		private static final long serialVersionUID = 1272411262323327048L;
		public SaveException(String msg)
		{
			super(msg);
		}
	}
	
	public CompoundAttributeForDisplay[] getAttributes()
	{
		
		Object[] compoundAttributes = null;
		Object[] groups = null;
		
		if (editingVoyages())
		{
			compoundAttributes = Voyage.getCoumpoundAttributes();
			groups = Voyage.getGroups();
		}
		else if (editingSlaves())
		{
			compoundAttributes = Slave.getCoumpoundAttributes();
			groups = Slave.getGroups();
		}
		else
		{
			return null;
		}
		
		CompoundAttributeForDisplay[] compoundAttributesForDisplay =
			new CompoundAttributeForDisplay[compoundAttributes.length];

		StringBuffer htmlGroups = new StringBuffer();
		StringBuffer htmlAttrs = new StringBuffer();

		for (int i = 0; i < compoundAttributes.length; i++)
		{
			CompoundAttribute compAttr = (CompoundAttribute) compoundAttributes[i];
			
			htmlGroups.setLength(0);
			for (int j = 0; j < groups.length; j++)
			{
				Group group = (Group) groups[j];
				if (group.getCompoundAttributes().contains(compAttr))
				{
					htmlGroups.append("<div>");
					htmlGroups.append(group.getUserLabelOrName());
					htmlGroups.append("</div>");
				}
			}
			
			htmlAttrs.setLength(0);
			Attribute[] attrsSorted = compAttr.getAttributesSortedByUserLabel();
			for (int j = 0; j < attrsSorted.length; j++)
			{
				Attribute attr = attrsSorted[j];
				htmlAttrs.append("<div>");
				htmlAttrs.append(attr.getUserLabelOrName());
				htmlAttrs.append("</div>");
			}

			compoundAttributesForDisplay[i] = new CompoundAttributeForDisplay(compAttr);
			compoundAttributesForDisplay[i].setGroupsHTML(htmlGroups.toString());
			compoundAttributesForDisplay[i].setAttributesHTML(htmlAttrs.toString());

		}
		
		return compoundAttributesForDisplay;
		
	}
	
	private String makeAttributeLabel(AbstractAttribute attr)
	{
		if (attr.getUserLabel() == null || attr.getUserLabel().length() == 0)
		{
			return attr.getName() + ": " + 
			"[no label]" + " " +
			"(" + attr.getTypeDisplayName() + ")";
		}
		else
		{
			return attr.getName() + ": " +
			attr.getUserLabel() + " " + 
			"(" + attr.getTypeDisplayName() + ")";
		}
	}
	
	private void moveAttributesToUI(AbstractAttribute[] dbAll, Set dbSelected, List uiAvailable, List uiSelected)
	{
		
		AbstractAttribute.sortByName(dbAll);
		
		Set selectedIds = new HashSet();
		if (dbSelected != null)
		{
			for (Iterator iter = dbSelected.iterator(); iter.hasNext();)
			{
				AbstractAttribute attr = (AbstractAttribute) iter.next();
				selectedIds.add(attr.getId());
			}
		}
		
		uiSelected.clear();
		uiAvailable.clear();
		
		for (int i = 0; i < dbAll.length; i++)
		{
			AbstractAttribute attr = dbAll[i];
			SelectItem item = new SelectItem();
			item.setText(makeAttributeLabel(attr));
			item.setValue(attr.getId().toString());
			item.setOrderNumber(i);
			if (selectedIds.contains(dbAll[i].getId()))
			{
				uiSelected.add(item);
			}
			else
			{
				uiAvailable.add(item);
			}
		}
		
	}
	
	public String newAttribute()
	{
		
		saveScrollPosition();
		clearEditResouces();
		
		attributeId = null;
		
		moveAttributesToUI(
				editingVoyages() ? Voyage.getAttributes() : Slave.getAttributes(),
				null,
				availableAttributes,
				attributeAttributes);
		
		return "edit";
	}

	public void editAttribute(ActionEvent event)
	{
		
		saveScrollPosition();
		setErrorText(null);
		
		UIParameter groupIdParam = (UIParameter) event.getComponent().findComponent("attributeId");
		if (groupIdParam == null) return;
		
		CompoundAttribute attribute = (CompoundAttribute) CompoundAttribute.loadById((Long) groupIdParam.getValue());
		attributeId = attribute.getId();
		setAttributeUserLabel(attribute.getUserLabel());
		setAttributeName(attribute.getName());
		setAttributeDescription(attribute.getDescription());
		setAttributeCategory(attribute.getCategory().intValue());
		setAttributeVisible(attribute.isVisible());
		
		moveAttributesToUI(
				Voyage.getAttributes(),
				attribute.getAttributes(),
				availableAttributes,
				attributeAttributes);
		
	}
	
	private Set moveAttributesFromUI(Session session, List uiAttributes, boolean isCompundAttr) throws SaveException
	{
		
		boolean first = true;
		int type = 0;
		String dictionaryName = null;
		
		Set newAttributes = new HashSet();
		for (Iterator iter = uiAttributes.iterator(); iter.hasNext();)
		{
			SelectItem item = (SelectItem) iter.next();

			AbstractAttribute attr = null;
			if (isCompundAttr)
				attr = CompoundAttribute.loadById(new Long(item.getValue()), session);
			else
				attr = Attribute.loadById(new Long(item.getValue()), session);
				
			if (attr != null)
			{
				if (first)
				{
					type = attr.getType().intValue();
					dictionaryName = attr.getDictionary();
					first = false;
				}
				else
				{
					if (type != attr.getType().intValue() ||
							(type == AbstractAttribute.TYPE_DICT &&
									!dictionaryName.equals(attr.getDictionary())))
						throw new SaveException("The selected attributes are not of the same type.");
				}
				newAttributes.add(attr);
			}
		}
		
		return newAttributes;
		
	}
	
	public String saveAttribute()
	{
		
		questionReallyDelete = false;

		Session session = HibernateUtil.getSession();
		Transaction transaction = session.beginTransaction();
		
		CompoundAttribute attribute;
		if (attributeId != null)
			attribute = (CompoundAttribute) CompoundAttribute.loadById(attributeId, session);	
		else if (editingVoyages())
			attribute = CompoundAttribute.newForVoyages(session);	
		else 
			attribute = CompoundAttribute.newForSlaves(session);	

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
	
			Set attributes = moveAttributesFromUI(session, attributeAttributes, false);
			
			attribute.setName(name);
			attribute.setUserLabel(userLabel);
			attribute.setAttributes(attributes);
			attribute.setCategory(new Integer(attributeCategory));
			attribute.setVisible(new Boolean(attributeVisible));
			attribute.setDescription(description);
			if (attributes != null && attributes.size() > 0)
			{
				Attribute firstAttr = (Attribute) attributes.iterator().next(); 
				attribute.setType(firstAttr.getType());
				attribute.setDictionary(firstAttr.getDictionary());
				attribute.setLength(firstAttr.getLength());
			}
			else
			{
				attribute.setType(null);
				attribute.setDictionary(null);
				attribute.setLength(null);
			}
			
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
	
	private boolean deleteInternal(boolean hard)
	{
		
		Session session = HibernateUtil.getSession();
		Transaction transaction = session.beginTransaction();
		
		CompoundAttribute attribute = (CompoundAttribute) CompoundAttribute.loadById(attributeId, session);
		List containingGroups = attribute.loadContainingGroups(session);
		
		if (!hard)
		{
			if (containingGroups.size() > 0)
			{
				transaction.rollback();
				session.close();
				return false;
			}
		}
		else
		{
			if (containingGroups.size() > 0)
			{
				for (Iterator iter = containingGroups.iterator(); iter.hasNext();)
				{
					Group group = (Group) iter.next();
					group.getCompoundAttributes().remove(attribute);
					session.update(group);
				}
			}
		}
		
		session.delete(attribute);
		transaction.commit();
		session.close();
		
		return true;
		
	}
	
	public String deleteAttributeSoft()
	{
		if (!deleteInternal(false))
		{
			questionReallyDelete = true;
			return null;
		}
		else
		{
			clearEditResouces();
			return "back";
		}
	}
	
	public String deleteAttributeHard()
	{
		deleteInternal(true);
		clearEditResouces();
		return "back";
	}
	
	public String cancelEdit()
	{
		clearEditResouces();
		return "back";
	}

	private void clearEditResouces()
	{
		questionReallyDelete = false;
		availableAttributes.clear();
		attributeAttributes.clear();
		attributeId = null;
		attributeName = null;
		attributeUserLabel = null;
		attributeDescription = null;
		attributeVisible = true;
		attributeCategory = AbstractAttribute.CATEGORY_BEGINNER;
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

	public List getAvailableAttributes()
	{
		return availableAttributes;
	}

	public void setAvailableAttributes(List availableAttributes)
	{
		if (availableAttributes == null)
			this.availableAttributes.clear();
		else
			this.availableAttributes = availableAttributes;
	}

	public List getAttributeAttributes()
	{
		return attributeAttributes;
	}

	public void setAttributeAttributes(List groupAttributes)
	{
		if (groupAttributes == null)
			this.attributeAttributes.clear();
		else
			this.attributeAttributes = groupAttributes;
	}

	public String getAttributeDescription()
	{
		return attributeDescription;
	}

	public void setAttributeDescription(String attributeDescription)
	{
		this.attributeDescription = attributeDescription;
	}

	public boolean isNewAttribute()
	{
		return attributeId == null;
	}

	public boolean isQuestionReallyDelete()
	{
		return questionReallyDelete;
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

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

public class GroupsBean extends SchemaEditBeanBase
{
	
	private Long groupId;
	private String groupUserLabel;
	private String groupName;
	private String groupDescription;
	private List availableAttributes = new ArrayList();
	private List groupAttributes = new ArrayList();
	private List availableCompoundAttributes = new ArrayList();
	private List groupCompoundAttributes = new ArrayList();
	
	private class SaveException extends Exception
	{
		private static final long serialVersionUID = 6458860234621829596L;
		public SaveException(String msg)
		{
			super(msg);
		}
	}
	
	public GroupsBean()
	{
	}
	
	public Object[] getGroups()
	{
		if (editingVoyages())
			return Voyage.getGroups();
		else
			return Slave.getGroups();
	}
	
	private String makeAttributeLabel(AbstractAttribute attr)
	{
		if (attr.getUserLabel() == null || attr.getUserLabel().length() == 0)
		{
			return attr.getName() + ": [no label]";
		}
		else
		{
			return attr.getName() + ": " + attr.getUserLabel();
		}
	}
	
	private void moveAttributesToUI(AbstractAttribute[] allAbstrAttribs, AbstractAttribute[] groupAbstrAttribs, List uiSelected, List uiAvailable)
	{
		
		uiSelected.clear();
		Set selectedIds = new HashSet();
		if (groupAbstrAttribs != null)
		{
			AbstractAttribute.sortByName(groupAbstrAttribs);
			for (int i = 0; i < groupAbstrAttribs.length; i++)
			{
				AbstractAttribute attr = groupAbstrAttribs[i];
				SelectItem item = new SelectItem();
				item.setText(makeAttributeLabel(attr));
				item.setValue(attr.getId().toString());
				item.setOrderNumber(i);
				selectedIds.add(attr.getId());
				uiSelected.add(item);
			}
		}
		
		uiAvailable.clear();
		AbstractAttribute.sortByName(allAbstrAttribs);
		for (int i = 0; i < allAbstrAttribs.length; i++)
		{
			AbstractAttribute attr = allAbstrAttribs[i];
			if (!selectedIds.contains(allAbstrAttribs[i].getId()))
			{
				SelectItem item = new SelectItem();
				item.setText(makeAttributeLabel(attr));
				item.setValue(attr.getId().toString());
				item.setOrderNumber(i);
				uiAvailable.add(item);
			}
		}
		
	}
	
	public String newGroup()
	{
		
		clearEditResouces();
		
		groupId = null;
		
		moveAttributesToUI(
				editingVoyages() ? Voyage.getAttributes() : Slave.getAttributes(),
				null,
				groupAttributes,
				availableAttributes);
		
		moveAttributesToUI(
				editingVoyages() ? Voyage.getCoumpoundAttributes() : Slave.getCoumpoundAttributes(),
				null,
				groupCompoundAttributes,
				availableCompoundAttributes);
		
		return "edit";
	}

	public void editGroup(ActionEvent event)
	{
		
		setErrorText(null);
		
		UIParameter groupIdParam = (UIParameter) event.getComponent().findComponent("groupId");
		if (groupIdParam == null) return;
		
		Group group = Group.loadById((Long) groupIdParam.getValue());
		groupId = group.getId();
		setGroupUserLabel(group.getUserLabel());
		setGroupName(group.getName());
		setGroupDescription(group.getDescription());
		
		moveAttributesToUI(
				Voyage.getAttributes(),
				(AbstractAttribute[]) group.getAttributes().toArray(new AbstractAttribute[0]),
				groupAttributes,
				availableAttributes);
		
		moveAttributesToUI(
				Voyage.getCoumpoundAttributes(),
				(AbstractAttribute[]) group.getCompoundAttributes().toArray(new AbstractAttribute[0]),
				groupCompoundAttributes,
				availableCompoundAttributes);
		
	}
	
	private Set moveAttributesFromUI(Session session, List uiAttributes, boolean isCompundAttr)
	{
		
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
				newAttributes.add(attr);
		}
		
		return newAttributes;
		
	}
	
	public String saveGroup()
	{

		Session session = HibernateUtil.getSession();
		Transaction transaction = session.beginTransaction();
		
		Group group = null;
		if (groupId != null)
			group = Group.loadById(groupId, session);
		else if (editingVoyages())
			group = Group.newForVoyages(session);
		else
			group = Group.newForSlaves(session);

		try
		{
			
			String name = StringUtils.trimAndUnNull(groupName, getMaxNameLength());
			if (name.length() == 0)
				throw new SaveException("Please specify group name.");
			
			String userLabel = StringUtils.trimAndUnNull(groupUserLabel, getMaxUserLabelLength());
			if (userLabel.length() == 0)
				throw new SaveException("Please specify label.");
	
			String description = StringUtils.trimAndUnNull(groupDescription, getMaxDescriptionLength());
			if (description.length() > getMaxDescriptionLength())
				throw new SaveException("Description is limited to " + getMaxDescriptionLength() + " characters.");

			Set attributes = moveAttributesFromUI(session, groupAttributes, false);
			Set compoundAttributes = moveAttributesFromUI(session, groupCompoundAttributes, true);

			group.setName(name);
			group.setUserLabel(userLabel);
			group.setDescription(description);
			group.setAttributes(attributes);
			group.setCompoundAttributes(compoundAttributes);
			
			session.saveOrUpdate(group);
			
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
	
	public String deleteGroup()
	{
		
		Session session = HibernateUtil.getSession();
		Transaction transaction = session.beginTransaction();
		Group group = Group.loadById(groupId, session);
		session.delete(group);
		transaction.commit();
		session.close();
		
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
		availableAttributes.clear();
		groupAttributes.clear();
		availableCompoundAttributes.clear();
		groupCompoundAttributes.clear();
		groupId = null;
		groupName = null;
		groupUserLabel = null;
		groupDescription = null;
		setErrorText(null);
	}

	public String getGroupUserLabel()
	{
		return groupUserLabel;
	}

	public void setGroupUserLabel(String selectedGroupUserLabel)
	{
		this.groupUserLabel = selectedGroupUserLabel;
	}

	public String getGroupName()
	{
		return groupName;
	}

	public void setGroupName(String selectedGroupUserName)
	{
		this.groupName = selectedGroupUserName;
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

	public List getAvailableCompoundAttributes()
	{
		return availableCompoundAttributes;
	}

	public void setAvailableCompoundAttributes(List availableCompoundAttributes)
	{
		if (availableCompoundAttributes == null)
			this.availableCompoundAttributes.clear();
		else
			this.availableCompoundAttributes = availableCompoundAttributes;
	}

	public List getGroupAttributes()
	{
		return groupAttributes;
	}

	public void setGroupAttributes(List groupAttributes)
	{
		if (groupAttributes == null)
			this.groupAttributes.clear();
		else
			this.groupAttributes = groupAttributes;
	}

	public List getGroupCompoundAttributes()
	{
		return groupCompoundAttributes;
	}

	public void setGroupCompoundAttributes(List groupCompoundAttributes)
	{
		if (groupCompoundAttributes == null)
			this.groupCompoundAttributes.clear();
		else
			this.groupCompoundAttributes = groupCompoundAttributes;
	}

	public String getGroupDescription()
	{
		return groupDescription;
	}

	public void setGroupDescription(String groupDescription)
	{
		this.groupDescription = groupDescription;
	}

	public boolean isNewGroup()
	{
		return groupId == null;
	}
	
}
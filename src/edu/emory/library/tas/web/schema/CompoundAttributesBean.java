package edu.emory.library.tas.web.schema;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.faces.component.UIParameter;
import javax.faces.event.ActionEvent;

import org.hibernate.Session;

import edu.emory.library.tas.Slave;
import edu.emory.library.tas.Voyage;
import edu.emory.library.tas.attrGroups.AbstractAttribute;
import edu.emory.library.tas.attrGroups.Attribute;
import edu.emory.library.tas.attrGroups.CompoundAttribute;
import edu.emory.library.tas.util.HibernateConnector;
import edu.emory.library.tas.util.HibernateUtil;
import edu.emory.library.tas.util.StringUtils;

public class CompoundAttributesBean
{

	private EditMode editMode = EditMode.Voyages;
	private Long attributeId;
	private String attributeUserLabel;
	private String attributeName;
	private List availableAttributes = new ArrayList();
	private List attributeAttributes = new ArrayList();
	
	private String errorText;
	
	private class SaveException extends Exception
	{
		private static final long serialVersionUID = 1272411262323327048L;
		public SaveException(String msg)
		{
			super(msg);
		}
	}
	
	public String switchToVoyages()
	{
		editMode = EditMode.Voyages;
		return null;
	}
	
	public String switchToSlaves()
	{
		editMode = EditMode.Slaves;
		return null;
	}

	public Object[] getAttributes()
	{
		if (editMode.isVoyages())
			return Voyage.getCoumpoundAttributes();
		else if (editMode.isSlaves())
			return Slave.getCoumpoundAttributes();
		else
			return null;
	}
	
	private String makeAttributeLabel(AbstractAttribute attr)
	{
		if (attr.getUserLabel() == null || attr.getUserLabel().length() == 0)
		{
			return attr.getName() + ": " + 
			"[no label]" + " " +
			"(" + attr.getType() + ")";
		}
		else
		{
			return attr.getName() + ": " +
			attr.getUserLabel() + " " + 
			"(" + attr.getType() + ")";
		}
	}
	
	private void moveAttributesToUI(AbstractAttribute[] allAbstrAttribs, AbstractAttribute[] groupAbstrAttribs, List uiSelected, List uiAvailable)
	{
		
		AbstractAttribute.sortByName(allAbstrAttribs);
		AbstractAttribute.sortByName(groupAbstrAttribs);
		
		uiSelected.clear();
		Set selectedIds = new HashSet();
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
		
		uiAvailable.clear();
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
	
	public void editAttribute(ActionEvent event)
	{
		
		UIParameter groupIdParam = (UIParameter) event.getComponent().findComponent("attributeId");
		if (groupIdParam == null) return;
		
		CompoundAttribute attribute = (CompoundAttribute) CompoundAttribute.loadById((Long) groupIdParam.getValue());
		attributeId = attribute.getId();
		setAttributeUserLabel(attribute.getUserLabel());
		setAttributeName(attribute.getName());
		
		moveAttributesToUI(
				Voyage.getAttributes(),
				(AbstractAttribute[]) attribute.getAttributes().toArray(new AbstractAttribute[0]),
				attributeAttributes,
				availableAttributes);
		
	}
	
	private Set getNewAttributes(Session session, List uiAttributes, boolean isCompundAttr) throws SaveException
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

		Session session = HibernateUtil.getSession();
		CompoundAttribute attribute = (CompoundAttribute) CompoundAttribute.loadById(attributeId, session);

		try
		{
			
			String name = StringUtils.trimAndUnNull(attributeName);
			if (name.length() == 0)
				throw new SaveException("Please specify attribute name.");
			
			String userLabel = StringUtils.trimAndUnNull(attributeUserLabel);
			if (userLabel.length() == 0)
				throw new SaveException("Please specify label.");
	
			Set attributes = getNewAttributes(session, attributeAttributes, false);
			
			attribute.setName(name);
			attribute.setUserLabel(userLabel);
			attribute.setAttributes(attributes);
			HibernateConnector.getConnector().updateObject(attribute);
			
			session.close();
			errorText = null;
			return "back";
		
		}
		catch (SaveException se)
		{
			session.close();
			errorText = se.getMessage();
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
		availableAttributes.clear();
		attributeAttributes.clear();
		attributeId = null;
		attributeName = null;
		attributeUserLabel = null;
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

	public String getErrorText()
	{
		return errorText;
	}
	
}

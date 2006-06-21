package edu.emory.library.tas.web.schema;

import javax.faces.component.UIParameter;
import javax.faces.event.ActionEvent;

import edu.emory.library.tas.Voyage;
import edu.emory.library.tas.attrGroups.Group;
import edu.emory.library.tas.util.HibernateConnector;

public class GroupsBean
{
	
	private Long selectedGroupId;
	private String selectedGroupUserLabel;
	private String selectedGroupName;
	
	public Object[] getGroups()
	{
		return Voyage.getGroups();
	}
	
	public void editGroup(ActionEvent event)
	{
		
		UIParameter groupIdParam = (UIParameter) event.getComponent().findComponent("groupId");
		if (groupIdParam == null) return;
		
		Group selectedGroup = Group.loadById((Long) groupIdParam.getValue());
		
		setSelectedGroupId(selectedGroup.getId());
		setSelectedGroupUserLabel(selectedGroup.getUserLabel());
		setSelectedGroupName(selectedGroup.getName());
		
	}
	
	public String saveSelectedGroup()
	{
		
		Group selectedGroup = Group.loadById(selectedGroupId);
		selectedGroup.setUserLabel(selectedGroupUserLabel);
		selectedGroup.setName(selectedGroupName);
		
		HibernateConnector.getConnector().updateObject(selectedGroup);
		
		return "back";
	}

	public Long getSelectedGroupId()
	{
		return selectedGroupId;
	}

	public void setSelectedGroupId(Long selectedGroupId)
	{
		this.selectedGroupId = selectedGroupId;
	}

	public String getSelectedGroupUserLabel()
	{
		return selectedGroupUserLabel;
	}

	public void setSelectedGroupUserLabel(String selectedGroupUserLabel)
	{
		this.selectedGroupUserLabel = selectedGroupUserLabel;
	}

	public String getSelectedGroupName()
	{
		return selectedGroupName;
	}

	public void setSelectedGroupName(String selectedGroupUserName)
	{
		this.selectedGroupName = selectedGroupUserName;
	}
	
}
package edu.emory.library.tas.web.schema;

import javax.faces.component.UIParameter;
import javax.faces.event.ActionEvent;

import edu.emory.library.tas.Voyage;
import edu.emory.library.tas.attrGroups.Group;
import edu.emory.library.tas.util.HibernateUtil;

public class GroupListBean
{
	
	private Group selectedGroup;
	
	public Object[] getGroups()
	{
		return Voyage.getGroups();
	}
	
	public void editGroup(ActionEvent event)
	{
		
		UIParameter groupIdParam = (UIParameter) event.getComponent().findComponent("groupId");
		if (groupIdParam == null) return;
		selectedGroup = Group.loadById((Long) groupIdParam.getValue());
	}

	public String getCacheInfo()
	{
		return HibernateUtil.getSessionFactory().getStatistics().toString();
	}

}

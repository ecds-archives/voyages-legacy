package edu.emory.library.tas.web.schema;

import edu.emory.library.tas.util.query.QueryValue;

public class GroupListBean
{
	
	public Object[] getGroups()
	{
		QueryValue query = new QueryValue("Group");
		Object[] groups = (Object[]) query.executeQuery();
		if (groups.length == 0) return null;
		//return Voyage.getGroups();
		return groups;
	}

}

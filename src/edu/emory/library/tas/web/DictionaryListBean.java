package edu.emory.library.tas.web;

import edu.emory.library.tas.Dictionary;

public class DictionaryListBean
{

	private String attribute;
	private boolean attributeChanged = false;
	private Dictionary[] list = null;
	
	public String getAttribute()
	{
		return attribute;
	}
	
	public void setAttribute(String attribute)
	{
		if ((attribute == null && this.attribute != null) || !attribute.equals(this.attribute))
			attributeChanged = true;
		
		this.attribute = attribute;
	}
	
	public Dictionary[] getList()
	{
		if (attributeChanged || list == null)
			list = Dictionary.loadDictionary(attribute);
		
		return list;
	}
	
}

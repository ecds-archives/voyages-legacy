package edu.emory.library.tas.web;

import java.util.Map;

import javax.faces.context.FacesContext;

import edu.emory.library.tas.Dictionary;
import edu.emory.library.tas.attrGroups.AbstractAttribute;

public class DictionaryListBean
{

	private String attributeId;
	private String formName;
	private String hiddenFieldName;
	private String displayFieldName;
	private boolean paramsLoaded = false;
	private Dictionary[] dictionary = null;
	
	private void ensureLoadParamsFromRequest()
	{
		if (!paramsLoaded)
		{
			FacesContext context = FacesContext.getCurrentInstance();
			Map params = context.getExternalContext().getRequestParameterMap();
			attributeId = (String) params.get("attributeId");
			formName = (String) params.get("formName");
			hiddenFieldName = (String) params.get("hiddenFieldName");
			displayFieldName = (String) params.get("displayFieldName");
			paramsLoaded = true;
		}
	}
	
	public String getAttributeName()
	{
		ensureLoadParamsFromRequest();
		return attributeId;
	}

	public String getDisplayFieldName()
	{
		ensureLoadParamsFromRequest();
		return displayFieldName;
	}

	public String getFormName()
	{
		ensureLoadParamsFromRequest();
		return formName;
	}

	public String getHiddenFieldName()
	{
		ensureLoadParamsFromRequest();
		return hiddenFieldName;
	}

	public Dictionary[] getDictionary()
	{
		if (dictionary == null)
		{
			AbstractAttribute attribute = AbstractAttribute.loadById(new Long(attributeId));
			dictionary = Dictionary.loadDictionary(attribute.getDictionary());
		}
		return dictionary;
	}
	
}

package edu.emory.library.tas.web;

import java.util.Map;

import javax.faces.context.FacesContext;

import edu.emory.library.tas.Dictionary;
import edu.emory.library.tas.SchemaColumn;
import edu.emory.library.tas.Voyage;

public class DictionaryListBean
{

	private String attributeName;
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
			attributeName = (String) params.get("attributeName");
			formName = (String) params.get("formName");
			hiddenFieldName = (String) params.get("hiddenFieldName");
			displayFieldName = (String) params.get("displayFieldName");
			paramsLoaded = true;
		}
	}
	
	public String getAttributeName()
	{
		ensureLoadParamsFromRequest();
		return attributeName;
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
			SchemaColumn col = Voyage.getSchemaColumn(attributeName);
			dictionary = Dictionary.loadDictionary(col.getDictinaory());
		}
		return dictionary;
	}
	
}

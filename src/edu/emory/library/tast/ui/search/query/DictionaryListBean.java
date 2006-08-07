package edu.emory.library.tast.ui.search.query;

import java.util.Map;

import javax.faces.context.FacesContext;

import edu.emory.library.tast.dm.Dictionary;
import edu.emory.library.tast.dm.attributes.AbstractAttribute;

public class DictionaryListBean
{

	private String attributeId;
	private String formName;
	private String hiddenFieldName;
	private String displayFieldName;
	private String updateTotalFieldName;
	private String builderId;
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
			updateTotalFieldName = (String) params.get("updateTotalFieldName");
			builderId = (String) params.get("builderId");
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

	public String getUpdateTotalFieldName()
	{
		ensureLoadParamsFromRequest();
		return updateTotalFieldName;
	}
	
	public String getBuilderId()
	{
		ensureLoadParamsFromRequest();
		return builderId;
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

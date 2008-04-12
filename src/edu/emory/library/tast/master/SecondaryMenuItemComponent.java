package edu.emory.library.tast.master;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;

import edu.emory.library.tast.util.JsfUtils;

public class SecondaryMenuItemComponent extends UIComponentBase
{
	
	private boolean menuIdSet = false;
	private String menuId;

	private boolean labelSet = false;
	private String label;

	private boolean hrefSet = false;
	private String href;

	public String getFamily()
	{
		return null;
	}
	
	public Object saveState(FacesContext context)
	{
		Object[] values = new Object[4];
		values[0] = super.saveState(context);
		values[1] = getMenuId();
		values[2] = getLabel();
		values[3] = getHref();
		return values;
	}
	
	public void restoreState(FacesContext context, Object state)
	{
		Object[] values = (Object[]) state;
		super.restoreState(context, values[0]);
		menuId = (String) values[1];
		label = (String) values[2];
		href = (String) values[3];
	}

	public String getLabel()
	{
		return JsfUtils.getCompPropString(this, getFacesContext(),
				"label", labelSet, label);
	}

	public void setLabel(String label)
	{
		labelSet = true;
		this.label = label;
	}

	public String getMenuId()
	{
		return JsfUtils.getCompPropString(this, getFacesContext(),
				"menuId", menuIdSet, menuId);
	}

	public void setMenuId(String menuId)
	{
		menuIdSet = true;
		this.menuId = menuId;
	}

	public String getHref()
	{
		return JsfUtils.getCompPropString(this, getFacesContext(),
				"href", hrefSet, href);
	}

	public void setHref(String href)
	{
		hrefSet = true;
		this.href = href;
	}
	
}

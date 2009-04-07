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

	private boolean expandedSet = false;
	private boolean expanded = true;

	public String getFamily()
	{
		return null;
	}
	
	public Object saveState(FacesContext context)
	{
		return new Object[] {
			super.saveState(context),
			getMenuId(),
			getLabel(),
			getHref(),
			new Boolean(isExpanded())};
	}
	
	public void restoreState(FacesContext context, Object state)
	{
		int i = 0;
		Object[] values = (Object[]) state;
		super.restoreState(context, values[i++]);
		menuId = (String) values[i++];
		label = (String) values[i++];
		href = (String) values[i++];
		expanded = ((Boolean) values[i++]).booleanValue();
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

	public boolean isExpanded()
	{
		return JsfUtils.getCompPropBoolean(this, getFacesContext(),
				"expanded", expandedSet, expanded);
	}

	public void setExpanded(boolean expanded)
	{
		expandedSet = true;
		this.expanded = expanded;
	}
	
}

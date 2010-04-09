/*
Copyright 2010 Emory University
	
	    This file is part of Trans-Atlantic Slave Voyages.
	
	    Trans-Atlantic Slave Voyages is free software: you can redistribute it and/or modify
	    it under the terms of the GNU General Public License as published by
	    the Free Software Foundation, either version 3 of the License, or
	    (at your option) any later version.
	
	    Trans-Atlantic Slave Voyages is distributed in the hope that it will be useful,
	    but WITHOUT ANY WARRANTY; without even the implied warranty of
	    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	    GNU General Public License for more details.
	
	    You should have received a copy of the GNU General Public License
	    along with Trans-Atlantic Slave Voyages.  If not, see <http://www.gnu.org/licenses/>. 
*/
package edu.emory.library.tast.common;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;

import edu.emory.library.tast.util.JsfUtils;

/**
 * Panel which is used as a tab.
 * When one wants to use additional panel in tab set component,
 * all the components of the tab should be children of this panel tab.
 * Component tag name used in jsp files is panelTab.
 * This component appears as children of component panelTabSet.
 * For more details see PanelTabSetComponent.
 *
 */
public class PanelTabComponent extends UIComponentBase
{

	private boolean titleSet = false;
	private String title;
	
	private boolean sectionIdSet = false;
	private String sectionId;
	
	private boolean hrefSet = false;
	private String href;
	
	private boolean cssClassSet = false;
	private String cssClass;

	public String getFamily()
	{
		return null;
	}

	public Object saveState(FacesContext context)
	{
		Object[] values = new Object[5];
		values[0] = super.saveState(context);
		values[1] = title;
		values[2] = sectionId;
		values[3] = href;
		values[4] = cssClass;
		return values;
	}
	
	public void restoreState(FacesContext context, Object state)
	{
		Object[] values = (Object[]) state;
		super.restoreState(context, values[0]);
		title = (String) values[1];
		sectionId = (String) values[2];
		href = (String) values[3];
		cssClass = (String) values[4];
	}
	
	public String getSectionId()
	{
		return JsfUtils.getCompPropString(this, getFacesContext(),
				"sectionId", sectionIdSet, sectionId);
	}

	public void setSectionId(String sectionId)
	{
		this.sectionIdSet = true;
		this.sectionId = sectionId;
	}

	public String getTitle()
	{
		return JsfUtils.getCompPropString(this, getFacesContext(),
				"title", titleSet, title);
	}

	public void setTitle(String title)
	{
		this.titleSet = true;
		this.title = title;
	}

	public String getHref()
	{
		return JsfUtils.getCompPropString(this, getFacesContext(),
				"href", hrefSet, href);
	}

	public void setHref(String href)
	{
		this.hrefSet = true;
		this.href = href;
	}

	public String getCssClass()
	{
		return JsfUtils.getCompPropString(this, getFacesContext(),
				"cssClass", cssClassSet, cssClass);
	}

	public void setCssClass(String cssClass)
	{
		this.cssClassSet = true;
		this.cssClass = cssClass;
	}

}
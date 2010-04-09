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

/**
 * This component should not be used any more. Its functionality is provided by
 * {@link edu.emory.library.tast.common.SectionGroupComponent}.
 * 
 * @author Jan Zich
 */
public class TabComponent extends UIComponentBase
{
	
	private String text;
	private String tabId;
//	private boolean selected;
	
	public Object saveState(FacesContext context)
	{
		Object values[] = new Object[3];
		values[0] = super.saveState(context);
		values[1] = text;
		values[2] = tabId;
		//values[3] = new Boolean(selected);
		return values;
	}
	
	public void restoreState(FacesContext context, Object state)
	{
		Object values[] = (Object[]) state;
		super.restoreState(context, values[0]);
		text = (String) values[1];
		tabId = (String) values[2];
		//selected = ((Boolean) values[3]).booleanValue();
	}

	public String getFamily()
	{
		return null;
	}

	public String getTabId()
	{
		return tabId;
	}

	public void setTabId(String id)
	{
		this.tabId = id;
	}

	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
	}

//	public boolean isSelected()
//	{
//		return selected;
//	}
//
//	public void setSelected(boolean selected)
//	{
//		this.selected = selected;
//	}

}
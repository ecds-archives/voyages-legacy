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

import java.io.IOException;
import java.util.Map;

import javax.faces.component.UIComponentBase;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;

import edu.emory.library.tast.util.StringUtils;
import edu.emory.library.tast.util.JsfUtils;

public abstract class MenuComponent extends UIComponentBase
{
	
	private MenuItemSection[] items;
	private boolean itemsSet = false;
	private MethodBinding onMenuSelected;
	private String customSubmitFunction = null;

	public String getFamily()
	{
		return null;
	}
	
	public boolean getRendersChildren()
	{
		return true;
	}
	
	public Object saveState(FacesContext context)
	{
		Object[] values = new Object[3];
		values[0] = super.saveState(context);
		values[1] = saveAttachedState(context, onMenuSelected);
		values[2] = customSubmitFunction;
		return values;
	}
	
	public void restoreState(FacesContext context, Object state)
	{
		Object[] values = (Object[]) state;
		super.restoreState(context, values[0]);
		onMenuSelected = (MethodBinding) restoreAttachedState(context, values[1]);
		customSubmitFunction = (String) values[2]; 
	}
	
	protected String getSelectedMenuIdFieldName(FacesContext context)
	{
		return getClientId(context) + "_selected";
	}
	
	public void decode(FacesContext context)
	{
		Map params = context.getExternalContext().getRequestParameterMap();
		
		String submittedSelectedMenuId = (String) params.get(getSelectedMenuIdFieldName(context));
		if (!StringUtils.isNullOrEmpty(submittedSelectedMenuId))
			queueEvent(new MenuItemSelectedEvent(this, submittedSelectedMenuId));
		
	}
	
	public void broadcast(FacesEvent event) throws AbortProcessingException
	{
		super.broadcast(event);
		
		if (event instanceof MenuItemSelectedEvent)
			if (onMenuSelected != null)
				onMenuSelected.invoke(getFacesContext(), new Object[] {event});
		
	}
	
	protected static String generateSubmitJS(FacesContext context, UIForm form, String fieldNameForSelected, String menuId, String customSubmitFunction)
	{
		
		StringBuffer js = new StringBuffer();

		if (customSubmitFunction == null)
		{
			
			JsfUtils.appendSubmitJS(js, context, form, fieldNameForSelected, menuId);
			
		}
		else
		{
			
			js.append(customSubmitFunction).append("(");
			
			js.append("this ");
			js.append(", ");
			
			js.append("function() {");
			JsfUtils.appendSubmitJS(js, context, form, fieldNameForSelected, menuId);
			js.append("}");
			
			js.append(");");
			
		}
		
		return js.toString();

	}
	
	public void encodeChildren(FacesContext context) throws IOException
	{
	}
	
	public void encodeEnd(FacesContext context) throws IOException
	{
	}

	public MenuItemSection[] getItems()
	{
		if (itemsSet) return items;
		ValueBinding vb = getValueBinding("items");
		if (vb == null) return items;
		return (MenuItemSection[]) vb.getValue(getFacesContext());
	}

	public void setItems(MenuItemSection[] items)
	{
		itemsSet = true;
		this.items = items;
	}

	public MethodBinding getOnMenuSelected()
	{
		return onMenuSelected;
	}

	public void setOnMenuSelected(MethodBinding onMenuSelected)
	{
		this.onMenuSelected = onMenuSelected;
	}

	public String getCustomSubmitFunction()
	{
		return customSubmitFunction;
	}

	public void setCustomSubmitFunction(String customSubmitFunction)
	{
		this.customSubmitFunction = customSubmitFunction;
	}

}
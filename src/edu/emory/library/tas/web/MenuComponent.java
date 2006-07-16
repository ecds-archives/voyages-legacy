package edu.emory.library.tas.web;

import java.io.IOException;
import java.util.Map;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;

import edu.emory.library.tas.util.StringUtils;

public abstract class MenuComponent extends UIComponentBase
{
	
	private MenuItemMain[] items;
	private boolean itemsSet = false;
	private MethodBinding onMenuSelected;

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
		Object[] values = new Object[2];
		values[0] = super.saveState(context);
		values[1] = saveAttachedState(context, onMenuSelected);
		return values;
	}
	
	public void restoreState(FacesContext context, Object state)
	{
		Object[] values = (Object[]) state;
		super.restoreState(context, values[0]);
		onMenuSelected = (MethodBinding) restoreAttachedState(context, values[1]);
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
	
	public void encodeChildren(FacesContext context) throws IOException
	{
	}
	
	public void encodeEnd(FacesContext context) throws IOException
	{
	}

	public MenuItemMain[] getItems()
	{
		if (itemsSet) return items;
		ValueBinding vb = getValueBinding("items");
		if (vb == null) return items;
		return (MenuItemMain[]) vb.getValue(getFacesContext());
	}

	public void setItems(MenuItemMain[] items)
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

}
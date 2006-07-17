package edu.emory.library.tas.web;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;

/**
 * This component should not be used any more. Its functionality is provided by
 * {@link edu.emory.library.tas.web.SectionGroupComponent}.
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
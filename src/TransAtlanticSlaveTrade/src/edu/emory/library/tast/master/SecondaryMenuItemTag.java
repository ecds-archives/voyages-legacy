package edu.emory.library.tast.master;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;

public class SecondaryMenuItemTag extends UIComponentTag
{
	
	private String menuId;
	private String label;
	private String href;
	private String expanded;

	public String getComponentType()
	{
		return "SecondaryMenuItem";
	}
	
	protected void setProperties(UIComponent component)
	{
		
		Application app = FacesContext.getCurrentInstance().getApplication();
		SecondaryMenuItemComponent menu = (SecondaryMenuItemComponent) component;
		
		if (menuId != null && isValueReference(menuId))
		{
			ValueBinding vb = app.createValueBinding(menuId);
			menu.setValueBinding("menuId", vb);
		}
		else
		{
			menu.setMenuId(menuId);
		}

		if (label != null && isValueReference(label))
		{
			ValueBinding vb = app.createValueBinding(label);
			menu.setValueBinding("label", vb);
		}
		else
		{
			menu.setLabel(label);
		}
		
		if (href != null && isValueReference(href))
		{
			ValueBinding vb = app.createValueBinding(href);
			menu.setValueBinding("href", vb);
		}
		else
		{
			menu.setHref(href);
		}
		
		if (expanded != null && isValueReference(expanded))
		{
			ValueBinding vb = app.createValueBinding(expanded);
			menu.setValueBinding("expanded", vb);
		}
		else if (expanded != null)
		{
			menu.setExpanded(Boolean.parseBoolean(expanded));
		}

	}

	public String getRendererType()
	{
		return null;
	}

	public String getLabel()
	{
		return label;
	}

	public void setLabel(String label)
	{
		this.label = label;
	}

	public String getMenuId()
	{
		return menuId;
	}

	public void setMenuId(String menuId)
	{
		this.menuId = menuId;
	}

	public String getHref()
	{
		return href;
	}

	public void setHref(String href)
	{
		this.href = href;
	}

	public String getExpanded()
	{
		return expanded;
	}

	public void setExpanded(String expanded)
	{
		this.expanded = expanded;
	}

}

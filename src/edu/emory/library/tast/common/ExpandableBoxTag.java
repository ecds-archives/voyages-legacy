package edu.emory.library.tast.common;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;

/**
 * Tag for expandableBox.
 * The attributes which can be used:
 * boxId - id of box - used in expandableBoxSet (see ExpandableBoxSet)
 * text - text visible in top bar of expandable box
 * collapsed - default value for box (expanded/collapsed)
 *
 */
public class ExpandableBoxTag extends UIComponentTag
{
	
	private String text;
	private String collapsed;
	private String boxId;
	
	protected void setProperties(UIComponent component)
	{
		
		Application app = FacesContext.getCurrentInstance().getApplication();
		ExpandableBoxComponent box  = (ExpandableBoxComponent) component;
		
		if (text !=null && isValueReference(text))
		{
			ValueBinding vb = app.createValueBinding(text);
			component.setValueBinding("text", vb);
		}
		else
		{
			box.setText(text);
		}
		
		if (collapsed !=null && isValueReference(collapsed))
		{
			ValueBinding vb = app.createValueBinding(collapsed);
			component.setValueBinding("collapsed", vb);
		}
		else
		{
			box.setCollapsed(collapsed);
		}	
		
		if (boxId != null) {
			box.setBoxId(boxId);
		}
		
		super.setProperties(component);
		
	}

	public String getComponentType()
	{
		return "ExpandableBox";
	}

	public String getRendererType()
	{
		return null;
	}

	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
	}

	public String getCollapsed() {
		return collapsed;
	}

	public void setCollapsed(String collapsed) {
		this.collapsed = collapsed;
	}

	public String getBoxId() {
		return boxId;
	}

	public void setBoxId(String gid) {
		this.boxId = gid;
	}

}

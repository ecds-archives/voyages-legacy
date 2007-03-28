package edu.emory.library.tast.common;

import javax.faces.component.UIComponent;
import javax.faces.webapp.UIComponentTag;

/**
 * Tag for expandableBoxSet component.
 * The tag can have following attributes:
 * expandedId - id of expanded box (see ExpandedBoxTag)
 *
 */
public class ExpandableBoxSetTag extends UIComponentTag {

	public String expandedId;
	
	public String getComponentType() {
		return "ExpandableBoxSet";
	}
	
	protected void setProperties(UIComponent component) {
		
		ExpandableBoxSetComponent c = (ExpandableBoxSetComponent) component;
		
		if (expandedId != null) {
			c.setExpandedId(expandedId);
		}
		
	}

	public String getRendererType() {
		return null;
	}

	public String getExpandedId() {
		return expandedId;
	}

	public void setExpandedId(String expanded) {
		this.expandedId = expanded;
	}

}

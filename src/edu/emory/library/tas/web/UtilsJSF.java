package edu.emory.library.tas.web;

import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;

public class UtilsJSF
{
	
	public static UIForm getForm(UIComponent component, FacesContext context)
	{
        UIComponent parent = component.getParent();
        while (parent != null && !"javax.faces.Form".equals(parent.getFamily()))
        {
            parent = parent.getParent();
        }
        return (UIForm) parent;
	}


}

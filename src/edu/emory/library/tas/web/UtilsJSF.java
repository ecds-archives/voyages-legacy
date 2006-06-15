package edu.emory.library.tas.web;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

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
	
	public static void encodeHiddenInput(UIComponent component, ResponseWriter writer, String name, String value) throws IOException
	{
		writer.startElement("input", component);
		writer.writeAttribute("type", "hidden", null);
		writer.writeAttribute("name", name, null);
		if (value != null) writer.writeAttribute("value", value, null);
		writer.endElement("input");
	}
	
	public static String generateSubmitJavaScript(FacesContext context, UIForm form, String name, String value)
	{
		
		StringBuffer js = new StringBuffer();

		if (name != null && value != null)
		{
			js.append("document.");
			js.append("forms['").append(form.getClientId(context)).append("'].");
			js.append("elements['").append(name).append("'].value = ");
			js.append("'").append(value).append("';");
		}

		if (js.length() > 0) js.append(" ");
		js.append("document.");
		js.append("forms['").append(form.getClientId(context)).append("'].");
		js.append("submit();");
		
		if (js.length() > 0) js.append(" ");
		js.append("return false;");
		
		return js.toString();
	
	}


}

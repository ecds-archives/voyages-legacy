package edu.emory.library.tas.web;

import java.io.IOException;
import java.util.Map;

import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

public class MenuSliderComponent extends MenuComponent
{
	
	private String expandedMainMenuId = null;
	
	private String getExpandedMenuIdFieldName(FacesContext context)
	{
		return getClientId(context) + "_expanded";
	}

	public void decode(FacesContext context)
	{
		super.decode(context);
		
		Map params = context.getExternalContext().getRequestParameterMap();
		if (params.containsKey(getExpandedMenuIdFieldName(context)))
			expandedMainMenuId = (String) params.get(getExpandedMenuIdFieldName(context));
		
	}
	
	private void encodeSubmenu(MenuItem item, String mainMenuDivId, FacesContext context, ResponseWriter writer, UIForm form) throws IOException
	{
		boolean expanded = item.getId().equals(expandedMainMenuId);

		writer.startElement("div", this);
		writer.writeAttribute("id", mainMenuDivId, null);
		writer.writeAttribute("class", "menu-slider-submenu", null);
		if (!expanded) writer.writeAttribute("style", "display: none", null);
		
		MenuItem[] items = item.getSubmenu();
		for (int i = 0; i < items.length; i++)
		{
			MenuItem subItem = items[i];
			
			String onClick = UtilsJSF.generateSubmitJS(
					context, form,
					getSelectedMenuIdFieldName(context), subItem.getId());
			

			writer.startElement("div", this);
			writer.writeAttribute("class", "menu-slider-submenu-item", null);
			writer.writeAttribute("onclick", onClick, null);
			writer.write(subItem.getText());
			writer.endElement("div");
		}

		writer.endElement("div");
	}
	
	public void encodeBegin(FacesContext context) throws IOException
	{
		
		ResponseWriter writer = context.getResponseWriter();
		UIForm form = UtilsJSF.getForm(this, context);
		
		String expandedMenuIdFieldName = getExpandedMenuIdFieldName(context);
		String expandedElementIdFieldName = getClientId(context) + "_expanded_element_id";

		UtilsJSF.encodeHiddenInput(this, writer,
				getSelectedMenuIdFieldName(context));
		
		UtilsJSF.encodeHiddenInput(this, writer,
				expandedMenuIdFieldName, expandedMainMenuId);

		UtilsJSF.encodeHiddenInput(this, writer,
				expandedElementIdFieldName);

		MenuItemMain[] items = getItems();
		
		StringBuffer jsOnClick = new StringBuffer();
		for (int i = 0; i < items.length; i++)
		{
			MenuItemMain mainItem = items[i];
			String mainMenuDivId = getClientId(context) + "_" + i;
			
			jsOnClick.setLength(0);
			jsOnClick.append("var expandedMenuId = ");
			UtilsJSF.appendFormElementRefJS(jsOnClick, context, form, expandedMenuIdFieldName).append("; ");
			jsOnClick.append("var expandedElementId = ");
			UtilsJSF.appendFormElementRefJS(jsOnClick, context, form, expandedElementIdFieldName).append("; ");
			jsOnClick.append("if (expandedMenuId.value == '").append(mainItem.getId()).append("') {");
			{
				UtilsJSF.appendHideElement(jsOnClick, mainMenuDivId).append(" ");
				jsOnClick.append("expandedMenuId.value = '';");
				jsOnClick.append("expandedElementId.value = '';");
			}
			jsOnClick.append("} else {");
			{
				UtilsJSF.appendShowElement(jsOnClick, mainMenuDivId).append(" ");
				jsOnClick.append("if (expandedMenuId.value != '') ");
				jsOnClick.append("document.getElementById(expandedElementId.value).style.display = 'none'; ");
				jsOnClick.append("expandedMenuId.value = '").append(mainItem.getId()).append("';");
				jsOnClick.append("expandedElementId.value = '").append(mainMenuDivId).append("';");
			}
			jsOnClick.append("}");
			
			writer.startElement("div", this);
			writer.writeAttribute("class", "menu-slider-item-main", null);
			writer.writeAttribute("onclick", jsOnClick.toString(), null);
			writer.write(mainItem.getText());
			writer.endElement("div");

			encodeSubmenu(mainItem, mainMenuDivId, context, writer, form);
		
		}
		
	}
	
}
package edu.emory.library.tast.ui;

import java.io.IOException;
import java.util.Map;

import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import edu.emory.library.tast.util.JsfUtils;

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
	
	private void encodeSubmenu(MenuItem item, String mainMenuDivId, boolean expanded, FacesContext context, ResponseWriter writer, UIForm form, String customSubmitFunction) throws IOException
	{

		writer.startElement("div", this);
		writer.writeAttribute("id", mainMenuDivId, null);
		writer.writeAttribute("class", "menu-slider-submenu", null);
		if (!expanded) writer.writeAttribute("style", "display: none", null);
		
		MenuItem[] items = item.getSubmenu();
		for (int i = 0; i < items.length; i++)
		{
			MenuItem subItem = items[i];
			
			String onClick = MenuComponent.generateSubmitJS(context, form,
					getSelectedMenuIdFieldName(context),
					subItem.getId(),
					customSubmitFunction);

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
		UIForm form = JsfUtils.getForm(this, context);
		
		String expandedMenuIdFieldName = getExpandedMenuIdFieldName(context);
		String expandedMainIdFieldName = getClientId(context) + "_expanded_main_element_id";
		String expandedSubIdFieldName = getClientId(context) + "_expanded_sub_element_id";

		JsfUtils.encodeHiddenInput(this, writer,
				getSelectedMenuIdFieldName(context));
		
		JsfUtils.encodeHiddenInput(this, writer,
				expandedMenuIdFieldName, expandedMainMenuId);

		JsfUtils.encodeHiddenInput(this, writer,
				expandedMainIdFieldName);

		JsfUtils.encodeHiddenInput(this, writer,
				expandedSubIdFieldName);

		MenuItemSection[] items = getItems();
		
		StringBuffer jsOnClick = new StringBuffer();
		for (int i = 0; i < items.length; i++)
		{
			MenuItemSection mainItem = items[i];
			boolean expanded = mainItem.getId().equals(expandedMainMenuId);
			
			String mainMenuDivId = getClientId(context) + "_main_" + i;
			String subMenuDivId = getClientId(context) + "_sub_" + i;
			
			jsOnClick.setLength(0);
			JsfUtils.appendFormElementRefWithVarJS(jsOnClick, context, form, "expandedMenuId", expandedMenuIdFieldName).append(" ");
			JsfUtils.appendFormElementRefWithVarJS(jsOnClick, context, form, "expandedMainId", expandedMainIdFieldName).append(" ");
			JsfUtils.appendFormElementRefWithVarJS(jsOnClick, context, form, "expandedSubId", expandedSubIdFieldName).append(" ");
			JsfUtils.appendElementRefWithVarJS(jsOnClick, "subMenu", subMenuDivId).append(" ");
			JsfUtils.appendElementRefWithVarJS(jsOnClick, "mainMenu", mainMenuDivId).append(" ");
			jsOnClick.append("var prevMainMenu = document.getElementById(expandedMainId.value); ");
			jsOnClick.append("var prevSubMenu = document.getElementById(expandedSubId.value); ");
			jsOnClick.append("if (expandedMenuId.value == '").append(mainItem.getId()).append("') {");
			{
				jsOnClick.append("subMenu.style.display = 'none'; ");
				jsOnClick.append("mainMenu.className = 'menu-slider-item-main-collapsed'; ");
				// jsOnClick.append("if (Scriptaculous) Effect.SlideUp(subMenu); ");
				jsOnClick.append("expandedMenuId.value = ''; ");
				jsOnClick.append("expandedMainId.value = '';");
				jsOnClick.append("expandedSubId.value = '';");
			}
			jsOnClick.append("} else {");
			{
				jsOnClick.append("subMenu.style.display = 'block'; ");
				jsOnClick.append("mainMenu.className = 'menu-slider-item-main-expanded'; ");
				// jsOnClick.append("if (Scriptaculous) Effect.SlideDown(subMenu); ");
				jsOnClick.append("if (prevSubMenu) {");
				{
					jsOnClick.append("prevSubMenu.style.display = 'none'; ");
					jsOnClick.append("prevMainMenu.className = 'menu-slider-item-main-collapsed';");
					// jsOnClick.append("if (Scriptaculous) Effect.SlideUp(subMenu); ");
				}
				jsOnClick.append("} ");
				jsOnClick.append("expandedMenuId.value = '").append(mainItem.getId()).append("'; ");
				jsOnClick.append("expandedMainId.value = '").append(mainMenuDivId).append("'; ");
				jsOnClick.append("expandedSubId.value = '").append(subMenuDivId).append("';");
			}
			jsOnClick.append("}");
			
			String mainMenuClass = expanded ?
					"menu-slider-item-main-expanded" :
					"menu-slider-item-main-collapsed";

			writer.startElement("div", this);
			writer.writeAttribute("id", mainMenuDivId, null);
			writer.writeAttribute("class", mainMenuClass, null);
			writer.writeAttribute("onclick", jsOnClick.toString(), null);
			writer.write(mainItem.getText());
			writer.endElement("div");

			encodeSubmenu(mainItem, subMenuDivId, expanded, context, writer, form, getCustomSubmitFunction());
		
		}
		
	}
	
}
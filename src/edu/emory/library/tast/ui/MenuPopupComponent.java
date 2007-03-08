package edu.emory.library.tast.ui;

import java.io.IOException;

import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import edu.emory.library.tast.util.JsfUtils;

public class MenuPopupComponent extends MenuComponent
{

	private void encodeSubmenu(int n, MenuItem item, FacesContext context, ResponseWriter writer, UIForm form, String customSubmitFunction) throws IOException
	{
		writer.startElement("div", this);
		writer.writeAttribute("class", "menu-popup-submenu-frame", null);

		writer.startElement("div", this);
		writer.writeAttribute("class", "menu-popup-submenu", null);
		writer.writeAttribute("onmouseover", "AttributesMenu.cancelclose(" + n + ", this.parentNode);", null);
		writer.writeAttribute("onmouseout", "AttributesMenu.delayclose(" + n + ", this.parentNode);", null);
		
		MenuItem[] items = item.getSubmenu();
		for (int i = 0; i < items.length; i++)
		{
			MenuItem subItem = items[i];
			
			String onClick = MenuComponent.generateSubmitJS(context, form,
					getSelectedMenuIdFieldName(context),
					subItem.getId(),
					customSubmitFunction);
			
			writer.startElement("div", this);
			writer.writeAttribute("class", "menu-popup-submenu-item", null);
			writer.writeAttribute("onclick", onClick, null);
			writer.write(subItem.getText());
			writer.endElement("div");
		}

		writer.endElement("div");
		writer.endElement("div");
	}
	
	public void encodeBegin(FacesContext context) throws IOException
	{
		
		ResponseWriter writer = context.getResponseWriter();
		UIForm form = JsfUtils.getForm(this, context);
		
		JsfUtils.encodeHiddenInput(this, writer,
				getSelectedMenuIdFieldName(context));
		
		String customSubmitFunction =getCustomSubmitFunction();
		
		MenuItemSection[] items = getItems();
		
		for (int i = 0; i < items.length; i++)
		{
			MenuItemSection mainItem = items[i];
			
			writer.startElement("div", this);
			if (i == 0)
			{
				writer.writeAttribute("class", "menu-popup-item-main-first", null);
			}
			else if (i < items.length - 1)
			{
				writer.writeAttribute("class", "menu-popup-item-main", null);
			}
			else
			{
				writer.writeAttribute("class", "menu-popup-item-main-last", null);
			}
			writer.writeAttribute("onmouseover", "AttributesMenu.cancelclose(" + i + ", this.firstChild);", null);
			writer.writeAttribute("onmouseout", "AttributesMenu.delayclose(" + i + ", this.firstChild);", null);
			encodeSubmenu(i + items.length, mainItem, context, writer, form, customSubmitFunction);
			writer.write(mainItem.getText());
			writer.endElement("div");
		}
		
	}

}
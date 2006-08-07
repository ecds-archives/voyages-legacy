package edu.emory.library.tast.ui.search.query;

import java.io.IOException;

import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

public class MenuPopupComponent extends MenuComponent
{

	private void encodeSubmenu(MenuItem item, FacesContext context, ResponseWriter writer, UIForm form, String customSubmitFunction) throws IOException
	{
		writer.startElement("div", this);
		writer.writeAttribute("class", "menu-popup-submenu-frame", null);

		writer.startElement("div", this);
		writer.writeAttribute("class", "menu-popup-submenu", null);
		writer.writeAttribute("onmouseover", "this.parentNode.style.display = 'block';", null);
		writer.writeAttribute("onmouseout", "this.parentNode.style.display = 'none';", null);
		
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
		UIForm form = UtilsJSF.getForm(this, context);
		
		UtilsJSF.encodeHiddenInput(this, writer,
				getSelectedMenuIdFieldName(context));
		
		String customSubmitFunction =getCustomSubmitFunction();
		
		MenuItemMain[] items = getItems();
		
		for (int i = 0; i < items.length; i++)
		{
			MenuItemMain mainItem = items[i];
			
			writer.startElement("div", this);
			writer.writeAttribute("class", "menu-popup-item-main", null);
			writer.writeAttribute("onmouseover", "this.firstChild.style.display = 'block';", null);
			writer.writeAttribute("onmouseout", "this.firstChild.style.display = 'none';", null);
			encodeSubmenu(mainItem, context, writer, form, customSubmitFunction);
			writer.write(mainItem.getText());
			writer.endElement("div");
		}
		
	}

}
package edu.emory.library.tast.ui;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.ValueBinding;

import edu.emory.library.tast.util.JsfUtils;

/**
 * <p>
 * This component is a combination of tabs and an expandable box component. If
 * is contains children only of type {@link SectionComponent}, it renders a
 * header with tabs with one tab for each child. Otherwise, it only renders
 * single header text given by the title property.
 * </p>
 * <p>
 * There is a problem related to hiding and showing tabs. If a tab is hidden and
 * if it contains additional input-type components (like a textbox), the child
 * components do not receive any request values from their HTML controls since
 * they are not rendered. And so all hidden components then pass to bean
 * typically just <code>null</code> values instead of the last visible valied
 * values. So beans should text the incomming values for <code>null</code>s,
 * if they are using componends inside this one. But they should do it anyways.
 * </p>
 * <p>
 * Second problem with the hiding is that when a portion of a component tree is
 * not rendered, the generated sequence of client IDs gets shifted and therefore
 * some components can get each time different client IDs. So if a components
 * uses its client ID to name HTML controls of elements, it should get some
 * fixed ID.
 * </p>
 * 
 * @author Jan Zich
 * 
 */
public class SectionGroupComponent extends UIComponentBase
{
	
	private static final String EXPANEDED = "expanded";
	private static final String COLLAPSED = "collapsed";

	private boolean collapsed = false;
	private String title;
	private String backgroundStyle;
	private String tabsStyle;
	private String buttonsStyle;
	private String selectedSectionId;
	private boolean selectedSectionIdSet = false;

	public String getFamily()
	{
		return null;
	}
	
	public boolean getRendersChildren()
	{
		return true;
	}
	
	public Object saveState(FacesContext context)
	{
		Object[] values = new Object[6];
		values[0] = super.saveState(context);
		values[1] = title;
		values[2] = backgroundStyle;
		values[3] = tabsStyle;
		values[4] = buttonsStyle;
		values[5] = selectedSectionId;
		return values;
	}
	
	public void restoreState(FacesContext context, Object state)
	{
		Object[] values = (Object[]) state;
		super.restoreState(context, values[0]);
		title = (String) values[1];
		backgroundStyle = (String) values[2];
		tabsStyle = (String) values[3];
		buttonsStyle = (String) values[4];
		selectedSectionId = (String) values[5];
	}
	
	private String getStateHiddenFieldName(FacesContext context)
	{
		return getClientId(context) + "_state";
	}

	private String getSelectedTabHiddenFieldName(FacesContext context)
	{
		return getClientId(context) + "_selected_tab";
	}

	public void decode(FacesContext context)
	{
		
		Map params = context.getExternalContext().getRequestParameterMap();

		String stateStr = (String) params.get(getStateHiddenFieldName(context));
		collapsed = COLLAPSED.equals(stateStr); 
		
		String newSelectedTabId = (String) params.get(getSelectedTabHiddenFieldName(context));
		if (newSelectedTabId != null && newSelectedTabId.length() > 0)
		{
			if (!newSelectedTabId.equals(selectedSectionId))
			{
				queueEvent(new TabChangeEvent(this, newSelectedTabId));
				selectedSectionId = newSelectedTabId;
			}
		}
		
	}
	
	public void processUpdates(FacesContext context)
	{
		ValueBinding vb = getValueBinding("selectedSectionId");
		if (vb != null) vb.setValue(context, selectedSectionId);
		super.processUpdates(context);
	}
	
	private void encodeSingleTextTitle(FacesContext context, ResponseWriter writer) throws IOException
	{
		writer.startElement("div", this);
		writer.writeAttribute("class", "section-title-text", null);
		writer.write(title);
		writer.endElement("div");
	}

	private void encodeTabsTitle(FacesContext context, ResponseWriter writer, UIForm form, String selectedSectionId) throws IOException
	{
		
		writer.startElement("table", this);
		writer.writeAttribute("border", "0", null);
		writer.writeAttribute("cellspacing", "0", null);
		writer.writeAttribute("cellpadding", "0", null);
		writer.writeAttribute("align", "left", null);
		writer.startElement("tr", this);
		
		for (Iterator iter = getChildren().iterator(); iter.hasNext();)
		{
			SectionComponent sect = (SectionComponent) iter.next();
			
			boolean isSelected = 
				selectedSectionId.equals(sect.getSectionId());
			
			String tabClass =
				"section-title-tab " + 
				"section-title-tab-" + tabsStyle + (isSelected ? "-selected" : "");
			
			String jsOnClick = JsfUtils.generateSubmitJS(
					context, form,
					getSelectedTabHiddenFieldName(context), sect.getSectionId());
			
			writer.startElement("td", this);
			writer.writeAttribute("class", tabClass, null);
			writer.writeAttribute("onclick", jsOnClick, null);
			writer.write(sect.getTitle());
			writer.endElement("td");
		}
		
		writer.endElement("tr");
		writer.endElement("table");
		
	}
	
	private void encodeTitle(FacesContext context, ResponseWriter writer, UIForm form, String selectedSectionId, boolean hasTabs) throws IOException
	{
		if (hasTabs)
			encodeTabsTitle(context, writer, form, selectedSectionId);
		else
			encodeSingleTextTitle(context, writer);
	}

	private void encodeButtons(FacesContext context, ResponseWriter writer, UIForm form) throws IOException
	{
		
		String srcExpand = "icon-expand-" + buttonsStyle + ".png";
		String srcCollapse = "icon-collapse-" + buttonsStyle + ".png";
		
		String imgId = getClientId(context) + "_button";
		
		StringBuffer jsOnClick = new StringBuffer();
		
		jsOnClick.append("var box = ");
		JsfUtils.appendElementRefJS(jsOnClick, getClientId(context));
		jsOnClick.append("; ");
		
		jsOnClick.append("var state = ");
		JsfUtils.appendFormElementRefJS(jsOnClick, context, form, getStateHiddenFieldName(context));
		jsOnClick.append("; ");
		
		jsOnClick.append("var img = ");
		JsfUtils.appendElementRefJS(jsOnClick, imgId);
		jsOnClick.append("; ");

		jsOnClick.append("if (state.value == '").append(COLLAPSED).append("') {");
		jsOnClick.append("state.value = '").append(EXPANEDED).append("'; ");
		jsOnClick.append("box.style.display = 'block'; ");
		jsOnClick.append("img.src = '").append(srcCollapse).append("';");
		jsOnClick.append("} else {");
		jsOnClick.append("state.value = '").append(COLLAPSED).append("'; ");
		jsOnClick.append("box.style.display = 'none'; ");
		jsOnClick.append("img.src = '").append(srcExpand).append("';");
		jsOnClick.append("}");
		
		writer.startElement("img", this);
		writer.writeAttribute("id", imgId, null);
		writer.writeAttribute("src", collapsed ? srcExpand : srcCollapse, null);
		writer.writeAttribute("onclick", jsOnClick, null);
		writer.writeAttribute("class", "section-button", null);
		writer.writeAttribute("width", "12", null);
		writer.writeAttribute("height", "12", null);
		writer.writeAttribute("border", "0", null);
		writer.endElement("img");
		
	}
	
	public void encodeChildren(FacesContext context) throws IOException
	{
	
		ResponseWriter writer = context.getResponseWriter();
		UIForm form = JsfUtils.getForm(this, context);

		String selectedSectionId = getSelectedTabId();
		if (selectedSectionId == null) selectedSectionId = "";
		
		boolean hasTabs = true;
		for (Iterator iter = getChildren().iterator(); iter.hasNext();)
		{
			UIComponent comp = (UIComponent) iter.next();
			if (!(comp instanceof SectionComponent)) hasTabs = false;
		}
		
		JsfUtils.encodeHiddenInput(this, writer,
				getStateHiddenFieldName(context),
				collapsed ? COLLAPSED : EXPANEDED);
		
		JsfUtils.encodeHiddenInput(this, writer,
				getSelectedTabHiddenFieldName(context),
				selectedSectionId);

		String mainClass = "section-title";
		mainClass += " section-title-" + backgroundStyle;
		if (hasTabs) mainClass += " section-title-line-" + tabsStyle;
		
		writer.startElement("div", this);
		writer.writeAttribute("class", mainClass, null);

		writer.startElement("table", this);
		writer.writeAttribute("border", "0", null);
		writer.writeAttribute("cellspacing", "0", null);
		writer.writeAttribute("cellpadding", "0", null);
		writer.writeAttribute("class", "section-title", null);
		writer.startElement("tr", this);

		writer.startElement("td", this);
		writer.writeAttribute("class", "section-title-left", null);
		encodeTitle(context, writer, form, selectedSectionId, hasTabs);
		writer.endElement("td");
		
		writer.startElement("td", this);
		writer.writeAttribute("class", "section-title-buttons", null);
		encodeButtons(context, writer, form);
		writer.endElement("td");

		writer.endElement("tr");
		writer.endElement("table");

		writer.endElement("div");
		
		writer.startElement("div", this);
		writer.writeAttribute("id", getClientId(context), null);
		writer.writeAttribute("class", "section-body", null);
		if (collapsed) writer.writeAttribute("style", "display: none", null);
		if (hasTabs)
		{
			for (Iterator iter = getChildren().iterator(); iter.hasNext();)
			{
				SectionComponent sect = (SectionComponent) iter.next();
				if (selectedSectionId.equals(sect.getSectionId()))
				{
					sect.setRendered(true);
					JsfUtils.renderChild(context, sect);
				} else {
					sect.setRendered(false);
				}
			}
		}
		else
		{
			for (Iterator iter = getChildren().iterator(); iter.hasNext();)
			{
				UIComponent comp = (UIComponent) iter.next();
				JsfUtils.renderChild(context, comp);
			}
		}
		writer.endElement("div");

	}
	
	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getBackgroundStyle()
	{
		return backgroundStyle;
	}

	public void setBackgroundStyle(String backgroundStyle)
	{
		this.backgroundStyle = backgroundStyle;
	}

	public String getTabsStyle()
	{
		return tabsStyle;
	}

	public void setTabsStyle(String tabsStyle)
	{
		this.tabsStyle = tabsStyle;
	}

	public String getButtonsStyle()
	{
		return buttonsStyle;
	}

	public void setButtonsStyle(String buttonsStyle)
	{
		this.buttonsStyle = buttonsStyle;
	}

	public String getSelectedTabId()
	{
		if (selectedSectionIdSet) return selectedSectionId;
		ValueBinding vb = getValueBinding("selectedSectionId");
		if (vb == null) return selectedSectionId;
		return (String) vb.getValue(getFacesContext());
	}

	public void setSelectedSectionId(String selectedSectionId)
	{
		this.selectedSectionId = selectedSectionId;
	}

}

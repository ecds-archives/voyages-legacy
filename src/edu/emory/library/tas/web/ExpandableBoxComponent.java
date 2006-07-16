package edu.emory.library.tas.web;

import java.io.IOException;

import javax.faces.component.UIComponentBase;
import javax.faces.component.UIForm;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.ValueBinding;

public class ExpandableBoxComponent extends UIComponentBase
{
	
	private static final String EXPANEDED = "expanded";
	private static final String COLLAPSED = "collapsed";
	private static final String COLLAPSE_SYMBOL = "-";
	private static final String EXPANED_SYMBOL = "+";

	private boolean collapsed = false;

	private String text = "";
	private boolean textSet = false;

	public String getFamily()
	{
		return null;
	}
	
	public boolean getRendersChildren()
	{
		return false;
	}
	
	public Object saveState(FacesContext context)
	{
		Object[] values = new Object[2];
		values[0] = super.saveState(context);
		values[1] = text;
		return values;
	}
	
	public void restoreState(FacesContext context, Object state)
	{
		Object[] values = (Object[]) state;
		super.restoreState(context, values[0]);
		text = (String) values[1];
	}
	
	private String getStateHiddenFieldName(FacesContext context)
	{
		return getClientId(context) + "_state";
	}
	
	public void decode(FacesContext context)
	{
		
		ExternalContext externalContex = context.getExternalContext();

		String stateStr = (String) externalContex.getRequestParameterMap().get(
				getStateHiddenFieldName(context));
		
		collapsed = COLLAPSED.equals(stateStr); 
		
	}
	
	public void encodeBegin(FacesContext context) throws IOException
	{
		
		ResponseWriter writer = context.getResponseWriter();
		UIForm form = UtilsJSF.getForm(this, context);
		
		String tdWithSymbolId = getClientId(context) + "_symbol";
		
		UtilsJSF.encodeHiddenInput(this, writer,
				getStateHiddenFieldName(context),
				collapsed ? COLLAPSED : EXPANEDED);
		
		StringBuffer js = new StringBuffer();
		
		js.append("var box = ");
		UtilsJSF.appendElementRefJS(js, getClientId(context));
		js.append("; ");
		
		js.append("var state = ");
		UtilsJSF.appendFormElementRefJS(js, context, form, getStateHiddenFieldName(context));
		js.append("; ");

		js.append("var symbol = ");
		UtilsJSF.appendElementRefJS(js, tdWithSymbolId);
		js.append("; ");

		js.append("if (state.value == '").append(COLLAPSED).append("') {");
		js.append("state.value = '").append(EXPANEDED).append("'; ");
		js.append("box.style.display = 'block'; ");
		js.append("symbol.innerHTML = '").append(COLLAPSE_SYMBOL).append("';");
		js.append("} else {");
		js.append("state.value = '").append(COLLAPSED).append("'; ");
		js.append("box.style.display = 'none'; ");
		js.append("symbol.innerHTML = '").append(EXPANED_SYMBOL).append("';");
		js.append("}");
		
		writer.startElement("div", this);
		writer.writeAttribute("class", "side-panel-section", null);
		writer.writeAttribute("onclick", js.toString(), null);
		
		writer.startElement("table", this);
		writer.writeAttribute("border", "0", null);
		writer.writeAttribute("cellspacing", "0", null);
		writer.writeAttribute("cellpadding", "0", null);
		writer.startElement("tr", this);
		
		writer.startElement("td", this);
		writer.writeAttribute("class", "side-panel-section-text", null);
		writer.write(getText());
		writer.endElement("td");
		
		writer.startElement("td", this);
		writer.writeAttribute("class", "side-panel-section-symbol", null);
		writer.writeAttribute("id", tdWithSymbolId, null);
		writer.write(collapsed ?  EXPANED_SYMBOL : COLLAPSE_SYMBOL);
		writer.endElement("td");

		writer.endElement("tr");
		writer.endElement("table");
		writer.endElement("div");

		writer.startElement("div", this);
		writer.writeAttribute("id", getClientId(context), null);
		if (collapsed) writer.writeAttribute("style", "display: none;", null);
	
	}
	
	public void encodeEnd(FacesContext context) throws IOException
	{
		ResponseWriter writer = context.getResponseWriter();
		writer.endElement("div");
	}

	public String getText()
	{
		if (textSet) return text;
		ValueBinding vb = getValueBinding("text");
		if (vb == null) return text;
		return (String) vb.getValue(getFacesContext());
	}

	public void setText(String text)
	{
		textSet = true;
		this.text = text;
	}

}

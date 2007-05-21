package edu.emory.library.tast.util;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.ValueBinding;


public class JsfUtils
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
	
	public static void encodeHiddenInput(UIComponent component, ResponseWriter writer, String name) throws IOException
	{
		encodeHiddenInput(component, writer, name, null);
	}

	public static void encodeHiddenInput(UIComponent component, ResponseWriter writer, String name, String value) throws IOException
	{
		writer.startElement("input", component);
		writer.writeAttribute("type", "hidden", null);
		writer.writeAttribute("name", name, null);
		if (value != null) writer.writeAttribute("value", value, null);
		writer.endElement("input");
	}
	
	public static void encodeJavaScriptStart(UIComponent component, ResponseWriter writer) throws IOException
	{
		writer.startElement("script", component);
		writer.writeAttribute("type", "text/javascript", null);
		writer.writeAttribute("language", "javascript", null);
	}

	public static void encodeJavaScriptEnd(UIComponent component, ResponseWriter writer) throws IOException
	{
		writer.endElement("script");
	}
	
	public static void encodeJavaScriptBlock(UIComponent component, ResponseWriter writer, String js) throws IOException
	{
		JsfUtils.encodeJavaScriptStart(component, writer);
		writer.write(js);
		JsfUtils.encodeJavaScriptEnd(component, writer);
	}

	public static void encodeJavaScriptBlock(UIComponent component, ResponseWriter writer, StringBuffer js) throws IOException
	{
		encodeJavaScriptBlock(component, writer, js.toString());
	}

	public static StringBuffer appendSubmitJS(StringBuffer js, FacesContext context, UIForm form, String elementName, String value)
	{
		/*
		js.append("var inpt = document.createElement('input'); ");
		js.append("inpt.type = 'hidden'; ");
		js.append("inpt.name = '");
		js.append(elementName);
		js.append("'; ");

		js.append("document.forms['");
		js.append(form.getClientId(context));
		js.append("'].appendChild(inpt); ");
		*/
		
		js.append("if (typeof(saveScrolling) == 'function') ");
		js.append("saveScrolling('" + form.getClientId(context) + "'); ");
		
		if (elementName != null && value != null)
		{
			appendFormElementValJS(js, context, form, elementName);
			js.append(" = '").append(value).append("';");
		}

		if (js.length() > 0) js.append(" ");
		appendFormRefJS(js, context, form);
		js.append(".submit();");
		
		if (js.length() > 0) js.append(" ");
		js.append("return false;");

		return js;
	}

	public static String generateConditionalJS(FacesContext context, UIForm form, String elementName, String jsObjectAttributeName, String hiddenField, String trueValue, String falseValue) {
		StringBuffer js = new StringBuffer();
		if (elementName != null) {
			js.append("var obj = ");
			appendElementRefJS(js, elementName).append(";");
			js.append("if (obj.").append(jsObjectAttributeName).append(") {");
			appendFormElementValJS(js, context, form, hiddenField).append(" = ");
			js.append("'").append(trueValue).append("';");
//			js.append("alert('new val: ' + ");
//			appendFormElementValJS(js, context, form, hiddenField);
//			js.append(");");
			js.append("} else {");
			appendFormElementValJS(js, context, form, hiddenField).append(" = ");
			js.append("'").append(falseValue).append("';");
//			js.append("alert('new val: ' + ");
//			appendFormElementValJS(js, context, form, hiddenField);
//			js.append(");");
			js.append("};");
		}
		return js.toString();
	}
	
	public static String generateSubmitJS(FacesContext context, UIForm form, String elementName, String value)
	{
		StringBuffer js = new StringBuffer();
		appendSubmitJS(js, context, form, elementName, value);
		return js.toString();
	}
	
	public static StringBuffer appendFormRefJS(StringBuffer js, FacesContext context, UIForm form)
	{
		js.append("document.");
		js.append("forms['").append(form.getClientId(context)).append("']");
		return js;
	}

	public static StringBuffer appendFormElementRefJS(StringBuffer js, FacesContext context, UIForm form, String elementName)
	{
		appendFormRefJS(js, context, form);
		js.append(".elements['").append(elementName).append("']");
		return js;
	}

	public static StringBuffer appendFormElementRefWithVarJS(StringBuffer js, FacesContext context, UIForm form, String var, String elementName)
	{
		js.append("var ").append(var).append(" = ");
		appendFormElementRefJS(js, context, form, elementName);
		js.append(";");
		return js;
	}

	public static StringBuffer appendFormElementValJS(StringBuffer js, FacesContext context, UIForm form, String elementName)
	{
		appendFormElementRefJS(js, context, form, elementName);
		js.append(".value");
		return js;
	}
	
	public static StringBuffer appendElementRefJS(StringBuffer js, String elementId)
	{
		js.append("document.");
		js.append("getElementById('").append(elementId).append("')");
		return js;
	}
	
	public static StringBuffer appendElementRefWithVarJS(StringBuffer js, String var, String elementId)
	{
		js.append("var ").append(var).append(" = ");
		appendElementRefJS(js, elementId);
		js.append(";");
		return js;
	}

	public static StringBuffer appendSetElementStyle(StringBuffer js, String elementId, String style, String value)
	{
		appendElementRefJS(js, elementId);
		js.append(".style.").append(style);
		js.append(" = '").append(value).append("';");
		return js;
	}

	public static StringBuffer appendHideElement(StringBuffer js, String elementId)
	{
		return appendSetElementStyle(js, elementId, "display", "none");
	}

	public static StringBuffer appendShowElement(StringBuffer js, String elementId)
	{
		return appendSetElementStyle(js, elementId, "display", "");
	}

	public static StringBuffer appendToggleElement(StringBuffer js, String elementId)
	{
		js.append("{");
		js.append("var el = ");
		appendElementRefJS(js, elementId).append("; ");
		js.append("el.style.display = el.style.display == 'none' ? '' : 'none' ;");
		js.append("}");
		return js;
	}

	public static String getParam(String name)
	{
		return (String) FacesContext.getCurrentInstance().getExternalContext().
			getRequestParameterMap().get(name);	
	}
	
    public static void renderChildren(FacesContext context, UIComponent component) throws IOException
    {
        if (component.getChildCount() > 0)
        {
            for (Iterator it = component.getChildren().iterator(); it.hasNext(); )
            {
                UIComponent child = (UIComponent)it.next();
                renderChild(context, child);
            }
        }
    }

    public static void renderChild(FacesContext context, UIComponent child) throws IOException
    {
        if (!child.isRendered())
        	return;
        
        child.encodeBegin(context);
        if (child.getRendersChildren())
        {
            child.encodeChildren(context);
        }
        else
        {
            renderChildren(context, child);
        }
        child.encodeEnd(context);
    }
    
    public static double getParamDouble(Map params, String paramName)
    {
		String value = (String) params.get(paramName);
		if (!StringUtils.isNullOrEmpty(value))
			return Double.parseDouble(value);
		else
			throw new RuntimeException("missing param " + paramName);
    }
    
    public static double getParamDouble(FacesContext context, String paramName)
    {
    	return getParamDouble(
    			context.getExternalContext().getRequestParameterMap(),
    			paramName);
    }

    public static double getParamDouble(Map params, String paramName, double def)
    {
		String value = (String) params.get(paramName);
		if (!StringUtils.isNullOrEmpty(value))
			return Double.parseDouble(value);
		else
			return def;
    }
    
    public static double getParamDouble(FacesContext context, String paramName, double def)
    {
    	return getParamDouble(
    			context.getExternalContext().getRequestParameterMap(),
    			paramName, def);
    }

    public static int getParamInt(Map params, String paramName)
    {
		String value = (String) params.get(paramName);
		if (!StringUtils.isNullOrEmpty(value))
			return Integer.parseInt(value);
		else
			throw new RuntimeException("missing param " + paramName);
    }
    
    public static int getParamInt(FacesContext context, String paramName)
    {
    	return getParamInt(
    			context.getExternalContext().getRequestParameterMap(),
    			paramName);
    }

    public static int getParamInt(Map params, String paramName, int def)
    {
		String value = (String) params.get(paramName);
		if (!StringUtils.isNullOrEmpty(value))
			return Integer.parseInt(value);
		else
			return def;
    }
    
    public static int getParamInt(FacesContext context, String paramName, int def)
    {
    	return getParamInt(
    			context.getExternalContext().getRequestParameterMap(),
    			paramName, def);
    }

    public static boolean getParamBoolean(Map params, String paramName)
    {
		String value = (String) params.get(paramName);
		if (StringUtils.isNullOrEmpty(value))
			return false;
		else
			return "true".compareToIgnoreCase(value) == 0;
    }
    
    public static boolean getParamBoolean(FacesContext context, String paramName)
    {
    	return getParamBoolean(
    			context.getExternalContext().getRequestParameterMap(),
    			paramName);
    }

    public static boolean getParamBoolean(Map params, String paramName, boolean def)
    {
		String value = (String) params.get(paramName);
		if (StringUtils.isNullOrEmpty(value))
			return def;
		else
			return "true".compareToIgnoreCase(value) == 0;
    }
    
    public static boolean getParamBoolean(FacesContext context, String paramName, boolean def)
    {
    	return getParamBoolean(
    			context.getExternalContext().getRequestParameterMap(),
    			paramName, def);
    }
    
    public static String getParamString(Map params, String paramName)
    {
    	String value = (String) params.get(paramName);
    	//if (value == null) throw new RuntimeException("missing param " + paramName);
		return value;
    }
    
    public static String getParamString(FacesContext context, String paramName)
    {
    	return getParamString (
    			context.getExternalContext().getRequestParameterMap(),
    			paramName);
    }

    public static String getParamString(Map params, String paramName, String def)
    {
		String value = (String) params.get(paramName);
		if (value == null)
			return def;
		else
			return value;
    }
    
    public static String getParamString(FacesContext context, String paramName, String def)
    {
    	return getParamString (
    			context.getExternalContext().getRequestParameterMap(),
    			paramName, def);
    }

    public static String getParamStringReplaceEmpty(Map params, String paramName, String def)
    {
		String value = (String) params.get(paramName);
		if (!StringUtils.isNullOrEmpty(value))
			return value;
		else
			return def;
    }
    
    public static String getParamStringReplaceEmpty(FacesContext context, String paramName, String def)
    {
    	return getParamStringReplaceEmpty(
    			context.getExternalContext().getRequestParameterMap(),
    			paramName, def);
    }

    public static boolean isParamEqualTo(Map params, String paramName, String value)
    {
		String paramValue = (String) params.get(paramName);
		if (paramValue == null && value == null)
			return true;
		else if (paramValue != null)
			return paramValue.equals(value);
		else 
			return value.equals(paramName);
    }

    public static boolean isParamEqualTo(FacesContext context, String paramName, String value)
    {
    	return isParamEqualTo(
    			context.getExternalContext().getRequestParameterMap(),
    			paramName, value);
    }
    
    public static String escapeStringForJS(String str)
    {
    	if (str == null) return "";
    	str = str.replaceAll("'", "\\\\'");
    	str = str.replaceAll("\r\n", "<br>");
    	str = str.replaceAll("\n\r", "<br>");
    	str = str.replaceAll("\n", "<br>");
    	return str;
    }
    
    public static void encodeBlank(UIComponent component, ResponseWriter writer, int width, int height) throws IOException
    {
    	writer.startElement("img", component);
    	writer.writeAttribute("src", "blank.png", null);
    	writer.writeAttribute("width", String.valueOf(width), null);
    	writer.writeAttribute("height", String.valueOf(height), null);
    	writer.writeAttribute("border", "0", null);
    	writer.endElement("img");
    }
    
    public static void main(String[] args)
	{
		System.out.println(escapeStringForJS("'"));
	}
    
    public static int getComponentInt(UIComponent component, FacesContext context, boolean set, int setValue, String boundKey)
    {
        if (set) return setValue;
        ValueBinding vb = component.getValueBinding(boundKey);
        if (vb == null) return setValue;
        return ((Integer) vb.getValue(context)).intValue();
    }
    
    public static int getCompPropInt(UIComponent component, FacesContext context, String name, boolean set, int value)
    {
        if (set) return value;
        ValueBinding vb = component.getValueBinding(name);
        if (vb == null) return value;
        return ((Integer) vb.getValue(context)).intValue();
    }

    public static boolean getCompPropBoolean(UIComponent component, FacesContext context, String name, boolean set, boolean value)
    {
        if (set) return value;
        ValueBinding vb = component.getValueBinding(name);
        if (vb == null) return value;
        return ((Boolean) vb.getValue(context)).booleanValue();
    }

    public static double getCompPropDouble(UIComponent component, FacesContext context, String name, boolean set, double value)
    {
        if (set) return value;
        ValueBinding vb = component.getValueBinding(name);
        if (vb == null) return value;
        return ((Double) vb.getValue(context)).doubleValue();
    }

    public static String getCompPropString(UIComponent component, FacesContext context, String name, boolean set, String value)
    {
        if (set) return value;
        ValueBinding vb = component.getValueBinding(name);
        if (vb == null) return value;
        return (String) vb.getValue(context);
    }

    public static String[] getCompPropStringArray(UIComponent component, FacesContext context, String name, boolean set, String[] value)
    {
        if (set) return value;
        ValueBinding vb = component.getValueBinding(name);
        if (vb == null) return value;
        return (String[]) vb.getValue(context);
    }

    public static Object getCompPropObject(UIComponent component, FacesContext context, String name, boolean set, Object value)
    {
        if (set) return value;
        ValueBinding vb = component.getValueBinding(name);
        if (vb == null) return value;
        return vb.getValue(context);
    }
    
    public static void writeParamIfNotNull(ResponseWriter writer, String name, String value) throws IOException
    {
    	if (value != null) writer.writeAttribute(name, value, null);
    }
    
    public static void writeParamIfNotDefault(ResponseWriter writer, String name, int value, int def) throws IOException
    {
    	if (value != def) writer.writeAttribute(name, String.valueOf(value), null);
    }

	public static void navigateTo(String outcome)
	{
		navigateTo(null, outcome);
	}
	
	public static void navigateTo(String fromAction, String outcome)
	{
		FacesContext fc = FacesContext.getCurrentInstance(); 
		fc.getApplication().getNavigationHandler().handleNavigation(fc, fromAction, outcome);
	}

}
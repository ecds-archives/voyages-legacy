package edu.emory.library.tast.reditor;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.faces.component.UIComponentBase;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.ValueBinding;

import edu.emory.library.tast.util.JsfUtils;

public class EditorComponent extends UIComponentBase
{
	
	private boolean schemaSet;
	private Schema schema;
	
	private boolean valuesSet;
	private Values values;

	private FieldSchemaState[] schemaFromState;
	
	public String getFamily()
	{
		return null;
	}
	
	public Object saveState(FacesContext context)
	{
		Object[] values = new Object[2];
		values[0] = super.saveState(context);
		values[1] = saveAttachedState(context, schema.getSerializableState());
		return values;
	}
	
	public void restoreState(FacesContext context, Object state)
	{
		Object[] values = (Object[]) state;
		super.restoreState(context, values[0]);
		schemaFromState = (FieldSchemaState[]) restoreAttachedState(context, values[1]);
	}
	
	public void decode(FacesContext context)
	{
		values = new Values();
		for (int i = 0; i < schemaFromState.length; i++)
		{
			FieldValue fieldValue = FieldValue.createFieldValue(schemaFromState[i]);
			fieldValue.decode(this, context);
			values.addValue(fieldValue);
		}
	}
	
	public void processUpdates(FacesContext context)
	{
		ValueBinding vb = getValueBinding("values");
		if (vb != null) vb.setValue(context, values);
	}
	
	public void encodeBegin(FacesContext context) throws IOException
	{
		
		// standard stuff
		ResponseWriter writer = context.getResponseWriter();
		UIForm form = JsfUtils.getForm(this, context);
		
		// get schema and values from a bean
		schema = getSchema();
		values = getValues();
		if (values == null) values = new Values(); 
		
		// component id
		String mainId = getClientId(context);
		
		// JS registration
		StringBuffer regJS = new StringBuffer();
		regJS.append("RecordEditorGlobals.registerEditor(new RecordEditor(");
		regJS.append("'").append(mainId).append("', ");
		regJS.append("'").append(form.getClientId(context)).append("', ");
		
		// lists
		regJS.append("[");
		int j = 0;
		for (Iterator iter = schema.getLists().entrySet().iterator(); iter.hasNext();)
		{
			Entry listEntry = (Entry) iter.next();;
			String listName = (String) listEntry.getKey();
			ListItem[] items = (ListItem[]) listEntry.getValue();

			if (j > 0) regJS.append(", ");
			regJS.append("new RecordEditorList(");
			regJS.append("'").append(listName).append("', ");
			regJS.append("[");
			for (int i = 0; i < items.length; i++)
			{
				ListItem event = items[i];
				if (i > 0) regJS.append(", ");
				regJS.append("new ListItem(");
				regJS.append("'").append(event.getValue()).append("'");
				regJS.append("'").append(event.getParentValue()).append("'");
				regJS.append("'").append(JsfUtils.escapeStringForJS(event.getText())).append("'");
				regJS.append(")");
			}
			regJS.append("]");
			regJS.append(")");
			
			j++;

		}
		regJS.append("]");

		// end js registration
		regJS.append("));");

		// render JS
		JsfUtils.encodeJavaScriptBlock(this, writer, regJS);

		// start main table
		writer.startElement("table", this);
		writer.writeAttribute("border", "1", null);
		writer.writeAttribute("cellspacing", "0", null);
		writer.writeAttribute("cellpadding", "0", null);
		writer.writeAttribute("class", "message-bar", null);
		
		// for all fields in schema
		for (Iterator iter = schema.getFields().iterator(); iter.hasNext();)
		{

			// schema and value
			FieldSchema fieldSchema = (FieldSchema) iter.next();
			FieldValue fieldValue = values.getValueFor(fieldSchema.getName());
			if (fieldValue == null) fieldValue = FieldValue.createFieldValue(fieldSchema);
			
			writer.startElement("tr", this);

			// label
			writer.startElement("td", this);
			writer.write(fieldSchema.getLabel());
			writer.endElement("td");
			
			// value
			writer.startElement("td", this);
			fieldSchema.encode(this, form, context, schema, fieldValue);
			writer.endElement("td");

			writer.endElement("tr");
		
		}
		
		writer.endElement("table");

	}

	public void encodeChildren(FacesContext context) throws IOException
	{
	}

	public void encodeEnd(FacesContext context) throws IOException
	{
	}

	private Schema getSchema()
	{
		return (Schema) JsfUtils.getCompPropObject(this, getFacesContext(),
				"schema", schemaSet, schema);
	}

	public void setSchema(Schema schema)
	{
		schemaSet = true;
		this.schema = schema;
	}

	public Values getValues()
	{
		return (Values) JsfUtils.getCompPropObject(this, getFacesContext(),
				"values", valuesSet, values);
	}

	public void setValues(Values values)
	{
		valuesSet = true;
		this.values = values;
	}

}
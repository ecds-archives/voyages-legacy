package edu.emory.library.tast.common.grideditor;

import java.util.HashMap;
import java.util.Map;

import edu.emory.library.tast.common.grideditor.list.ListFieldType;
import edu.emory.library.tast.common.grideditor.list.ListItem;
import edu.emory.library.tast.common.grideditor.list.ListValue;
import edu.emory.library.tast.common.grideditor.textbox.TextboxFieldType;
import edu.emory.library.tast.common.grideditor.textbox.TextboxValue;

public class GridEditorTestBean
{

	private static final String FIELD_TYPE_STATES = "listOfStates";
	private static final String FIELD_TYPE_NAME = "name";
	private static final String FIELD_TYPE_PHONE = "phone";

	private Values values;
	
	public GridEditorTestBean()
	{
		values = new Values();
		values.setValue("new", "firstName", new TextboxValue("Bill"));
		values.setValue("new", "lastName", new TextboxValue("Gates"));
		values.setValue("new", "phone", new TextboxValue("555"));
		values.setValue("new", "state", new ListValue());
		values.setValue("old", "firstName", new TextboxValue("Jan"));
		values.setValue("old", "lastName", new TextboxValue("Zich"));
		values.setValue("old", "phone", new TextboxValue("111"));
		values.setValue("old", "state", new ListValue(new String[] {"DS", "GA"}));
	}
	
	public Row[] getRows()
	{
		return new Row[] {
				new Row(FIELD_TYPE_NAME, "firstName", "First name"),
				new Row(FIELD_TYPE_NAME, "lastName", "Last name"),
				new Row(FIELD_TYPE_PHONE, "phone", "Phone number"),
				new Row(FIELD_TYPE_STATES, "state", "State")
		};
	}

	public Column[] getColumns()
	{
		return new Column[] {
				new Column("old", "Old"),
				new Column("new", "New")
		};
	}
	
	public Map getFieldTypes()
	{
		
		ListItem[] states = new ListItem[] {

				new ListItem("DS", "Deep South", new ListItem[] {
						new ListItem("AL", "Alabama"),
						new ListItem("FL", "Florida"),
						new ListItem("GA", "Georgia"),
						new ListItem("LU", "Louisiana"),
						new ListItem("MS", "Mississippi")}),
						
				new ListItem("MS", "Mid-South", new ListItem[] {
						new ListItem("AL", "Kentucky"),
						new ListItem("FL", "North Carolina"),
						new ListItem("GA", "South Carolina"),
						new ListItem("LU", "Tennessee"),
						new ListItem("MS", "Virginia")}),
						
				new ListItem("PW", "Pacific West", new ListItem[] {
						new ListItem("AL", "Alaska"),
						new ListItem("FL", "California"),
						new ListItem("GA", "Hawaii")})
						
		};
		
		Map fieldTypes = new HashMap();
		
		fieldTypes.put(
				FIELD_TYPE_NAME,
				new TextboxFieldType(FIELD_TYPE_NAME));
		
		fieldTypes.put(
				FIELD_TYPE_PHONE,
				new TextboxFieldType(FIELD_TYPE_PHONE));
		
		fieldTypes.put(
				FIELD_TYPE_STATES,
				new ListFieldType(FIELD_TYPE_STATES, states));
		
		return fieldTypes;
		
	}

	public Values getValues()
	{
		return values;
	}

	public void setValues(Values values)
	{
		this.values = values;
	}

}
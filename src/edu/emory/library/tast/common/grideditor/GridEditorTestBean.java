package edu.emory.library.tast.common.grideditor;

import java.util.HashMap;
import java.util.Map;

import edu.emory.library.tast.common.grideditor.list.ListItem;
import edu.emory.library.tast.common.grideditor.list.ListRow;
import edu.emory.library.tast.common.grideditor.list.ListValue;
import edu.emory.library.tast.common.grideditor.textbox.TextboxRow;
import edu.emory.library.tast.common.grideditor.textbox.TextboxValue;

public class GridEditorTestBean
{
	
	private static final String LIST_OF_STATES = "listOfStates";

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
				new TextboxRow("firstName", "First name"),
				new TextboxRow("lastName", "Last name"),
				new TextboxRow("phone", "Phone number"),
				new ListRow("state", "Phone number", LIST_OF_STATES)
		};
	}

	public Column[] getColumns()
	{
		return new Column[] {
				new Column("old", "Old"),
				new Column("new", "New")
		};
	}
	
	public Map getExtensions()
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
		
		Map extensions = new HashMap();
		extensions.put(LIST_OF_STATES, new SharedListExtension(states));
		return extensions;
		
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
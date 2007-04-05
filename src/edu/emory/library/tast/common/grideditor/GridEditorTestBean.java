package edu.emory.library.tast.common.grideditor;

import edu.emory.library.tast.common.grideditor.textbox.TextboxAdapter;
import edu.emory.library.tast.common.grideditor.textbox.TextboxValue;

public class GridEditorTestBean
{
	
	private Values values;
	
	public GridEditorTestBean()
	{
		values = new Values();
		values.setValue("new", "firstName", new TextboxValue("Bill"));
		values.setValue("new", "lastName", new TextboxValue("Gates"));
		values.setValue("new", "phone", new TextboxValue("555"));
		values.setValue("old", "firstName", new TextboxValue("Jan"));
		values.setValue("old", "lastName", new TextboxValue("Zich"));
		values.setValue("old", "phone", new TextboxValue("111"));
	}
	
	public Row[] getRows()
	{
		return new Row[] {
				new Row(TextboxAdapter.TYPE, "firstName", "First name"),
				new Row(TextboxAdapter.TYPE, "lastName", "Last name"),
				new Row(TextboxAdapter.TYPE, "phone", "Phone number")
		};
	}

	public Column[] getColumns()
	{
		return new Column[] {
				new Column("old", "Old"),
				new Column("new", "New")
		};
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
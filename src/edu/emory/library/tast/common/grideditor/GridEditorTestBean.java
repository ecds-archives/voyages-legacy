package edu.emory.library.tast.common.grideditor;

import java.util.HashMap;
import java.util.Map;

import edu.emory.library.tast.common.grideditor.date.DateFieldType;
import edu.emory.library.tast.common.grideditor.date.DateValue;
import edu.emory.library.tast.common.grideditor.list.ListFieldType;
import edu.emory.library.tast.common.grideditor.list.ListItem;
import edu.emory.library.tast.common.grideditor.list.ListValue;
import edu.emory.library.tast.common.grideditor.textbox.TextareaFieldType;
import edu.emory.library.tast.common.grideditor.textbox.TextareaValue;
import edu.emory.library.tast.common.grideditor.textbox.TextboxFieldType;
import edu.emory.library.tast.common.grideditor.textbox.TextboxValue;
import edu.emory.library.tast.util.StringUtils;

public class GridEditorTestBean
{

	private static final String FIELD_TYPE_STATES = "listOfStates";
	private static final String FIELD_TYPE_NAME = "name";
	private static final String FIELD_TYPE_PHONE = "phone";
	private static final String FIELD_TYPE_DESC = "description";
	private static final String FIELD_TYPE_DATE = "date";

	private Values values;
	
	public void columnAction(ColumnActionEvent event) {
		System.out.println("column = " + event.getColumnName() + " action = " + event.getActionName());
	}
	
	public GridEditorTestBean()
	{
		
		values = new Values();
		
		TextboxValue firstNameReadOnly = new TextboxValue("Jan");
		firstNameReadOnly.setPastNotes(new String[] {"First note.", "Some other note. Some other note. Some other note."});
		
		values.setValue("old", "firstName", firstNameReadOnly);
		values.setValue("old", "lastName", new TextboxValue("Zich"));
		values.setValue("old", "phone", new TextboxValue("111"));
		values.setValue("old", "state", new ListValue(new String[] {"DS", "GA"}));
		values.setValue("old", "description", new TextareaValue("We need all the exposure we can get. Make it your mission to convert as many of your friends, family members and coworkers as possible. If you're a student, get it distributed at your college. Submit a story to  Slashdot and other news sites about the release. Make some noise on your blog. Mass distribution via the Internet is possible."));
		values.setValue("old", "dob", new DateValue("2000", "1", "1"));

		TextboxValue firstNameEdit = new TextboxValue("Bill");
		firstNameEdit.setPastNotes(new String[] {"Some other note. Some other note. Some other note. First note."});
		
		values.setValue("new", "firstName", firstNameEdit);
		values.setValue("new", "lastName", new TextboxValue("Gates"));
		values.setValue("new", "phone", new TextboxValue("555"));
		values.setValue("new", "state", new ListValue());
		values.setValue("new", "description", new TextareaValue("Removing Thunderbird 2 won't remove your email messages, extensions or other add-ons. This data is stored in your profile folder, which is located in one of the following locations depending on your operating system."));
		values.setValue("new", "dob", new DateValue("2001", "1", "1"));
		
	}
	
	public Row[] getRows()
	{
		
		Row firstNameRow = new Row(FIELD_TYPE_NAME, "firstName", "First name", null, "name");
		firstNameRow.setNoteEnabled(true);
		
		return new Row[] {
				firstNameRow,
				new Row(FIELD_TYPE_NAME, "lastName", "Last name", null, "name"),
				new Row(FIELD_TYPE_PHONE, "phone", "Phone number", null, "contact"),
				new Row(FIELD_TYPE_STATES, "state", "State", null, "contact"),
				new Row(FIELD_TYPE_DESC, "description", "Description", null, "other"),
				new Row(FIELD_TYPE_DATE, "dob", "Date of birth", null, "xyz")
		};
	}
	
	public RowGroup[] getRowGroups()
	{
		return new RowGroup[] {
				new RowGroup("name", "Name"),
				new RowGroup("contact", "Contact"),
				new RowGroup("other", "Other"),
		};
	}

	public Column[] getColumns()
	{
		
		Column oldColumn = new Column("old", "Old", true);
		oldColumn.setActions(new ColumnAction[] {
				new ColumnAction("action1", "Action 1"),
				new ColumnAction("action2", "Action 2")});
		
		Column newColumn = new Column("new", "New", false);
		
		return new Column[] {oldColumn, newColumn};
		
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
				new TextboxFieldType(FIELD_TYPE_NAME, "grid-editor-name"));
		
		fieldTypes.put(
				FIELD_TYPE_PHONE,
				new TextboxFieldType(FIELD_TYPE_PHONE, "grid-editor-phone"));
		
		fieldTypes.put(
				FIELD_TYPE_STATES,
				new ListFieldType(FIELD_TYPE_STATES, states));
		
		fieldTypes.put(
				FIELD_TYPE_DESC,
				new TextareaFieldType(FIELD_TYPE_DESC, "grid-editor-desc", 10));

		fieldTypes.put(
				FIELD_TYPE_DATE,
				new DateFieldType(FIELD_TYPE_DATE));

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
	
	public String testError()
	{
		
		TextboxValue firstNameVal = (TextboxValue) values.getValue("new", "firstName");
		TextboxValue lastNameVal = (TextboxValue) values.getValue("new", "lastName");
		
		if (StringUtils.isNullOrEmpty(firstNameVal.getText()))
			firstNameVal.setErrorMessage("First name cannot be empty.");
		
		if (StringUtils.isNullOrEmpty(lastNameVal.getText()))
			firstNameVal.setErrorMessage("Last name cannot be empty.");

		return null;

	}

}
package edu.emory.library.tas.web.schema;

public class SchemaEditBeanBase
{

	private static final int MAX_NAME_LENGTH = 40;
	private static final int MAX_USER_LABEL_LENGTH = 40;
	private static final int MAX_DESCRIPTION_LENGTH = 200;

	private Switcher switcher;
	private String errorText;

	public boolean editingVoyages()
	{
		return switcher.getEditMode().isVoyages();
	}

	public boolean editingSlaves()
	{
		return switcher.getEditMode().isSlaves();
	}

	public EditMode getEditMode()
	{
		return switcher.getEditMode();
	}

	public Switcher getSwitcher()
	{
		return switcher;
	}

	public void setSwitcher(Switcher switcher)
	{
		this.switcher = switcher;
	}

	public String getErrorText()
	{
		return errorText;
	}

	public void setErrorText(String errorText)
	{
		this.errorText = errorText;
	}
	
	public boolean isError()
	{
		return errorText != null && errorText.length() > 0 ;
	}
	
	public int getMaxNameLength()
	{
		return MAX_NAME_LENGTH;
	}

	public int getMaxUserLabelLength()
	{
		return MAX_USER_LABEL_LENGTH;
	}

	public int getMaxDescriptionLength()
	{
		return MAX_DESCRIPTION_LENGTH;
	}

}

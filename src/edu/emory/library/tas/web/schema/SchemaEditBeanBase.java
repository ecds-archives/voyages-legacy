package edu.emory.library.tas.web.schema;

public class SchemaEditBeanBase
{

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

}

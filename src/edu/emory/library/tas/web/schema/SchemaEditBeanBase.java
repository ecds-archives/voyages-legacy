package edu.emory.library.tas.web.schema;

public class SchemaEditBeanBase
{

	private Switcher switcher;

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

}

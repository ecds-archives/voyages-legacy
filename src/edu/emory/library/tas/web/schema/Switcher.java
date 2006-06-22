package edu.emory.library.tas.web.schema;

public class Switcher
{
	
	private EditMode editMode = EditMode.Voyages;
	
	public String gotoVoyagesGroups()
	{
		editMode = EditMode.Voyages;
		return "voyages-groups";
	}

	public String gotoVoyagesCompoundAttributes()
	{
		editMode = EditMode.Voyages;
		return "voyages-compound-attributes";
	}

	public String gotoVoyagesAttributes()
	{
		editMode = EditMode.Voyages;
		return "voyages-attributes";
	}

	public String gotoSlavesGroups()
	{
		editMode = EditMode.Slaves;
		return "slaves-groups";
	}

	public String gotoSlavesCompoundAttributes()
	{
		editMode = EditMode.Slaves;
		return "slaves-compound-attributes";
	}

	public String gotoSlavesAttributes()
	{
		editMode = EditMode.Slaves;
		return "slaves-attributes";
	}

	public EditMode getEditMode()
	{
		return editMode;
	}

	public void setEditMode(EditMode editMode)
	{
		this.editMode = editMode;
	}

}
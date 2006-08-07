package edu.emory.library.tast.ui.schema;

public class EditMode
{

	private static final int MODE_VOYAGES = 1;
	private static final int MODE_SLAVES = 2;
	
	public static final EditMode Voyages = new EditMode(MODE_VOYAGES); 
	public static final EditMode Slaves = new EditMode(MODE_SLAVES); 
	
	private int mode = 0;

	private EditMode(int mode)
	{
		this.mode = mode;
	}
	
	public boolean equals(Object obj)
	{
		if (obj != null && obj instanceof EditMode)
		{
			return ((EditMode)obj).mode == mode;
		}
		else
		{
			return false;
		}
	}
	
	public boolean isSlaves()
	{
		return mode == MODE_SLAVES;
	}
	
	public boolean isVoyages()
	{
		return mode == MODE_VOYAGES;
	}

}

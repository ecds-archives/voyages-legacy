package edu.emory.library.tast.common.grideditor.textbox;

import javax.faces.context.FacesContext;

import edu.emory.library.tast.common.grideditor.GridEditorComponent;
import edu.emory.library.tast.common.grideditor.Value;

public class TextboxIntegerAdapter extends TextboxAdapter
{
	
	public static final String TYPE = "textbox-integer";
	
	public Value decode(FacesContext context, String inputPrefix, GridEditorComponent gridEditor)
	{
		return new TextboxIntegerValue(getSubmittedValue(context, inputPrefix));
	}
	
}

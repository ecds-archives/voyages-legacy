package edu.emory.library.tast.common.grideditor.textbox;

import javax.faces.context.FacesContext;

import edu.emory.library.tast.common.grideditor.GridEditorComponent;
import edu.emory.library.tast.common.grideditor.Value;

public class TextboxDoubleAdapter extends TextboxAdapter
{
	
	public static final String TYPE = "textbox-double";
	
	public Value decode(FacesContext context, String inputPrefix, GridEditorComponent gridEditor)
	{
		return new TextboxDoubleValue(getSubmittedValue(context, inputPrefix));
	}
	
}
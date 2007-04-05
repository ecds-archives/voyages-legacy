package edu.emory.library.tast.common.grideditor;

import java.io.IOException;

import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;

abstract public class Adapter
{
	
	public abstract Value decode(
			FacesContext context,
			String inputPrefix,
			GridEditorComponent gridEditor);
	
	public abstract void encode(
			FacesContext context,
			GridEditorComponent gridEditor,
			UIForm form,
			String inputPrefix,
			Value value,
			boolean readOnly) throws IOException;

}

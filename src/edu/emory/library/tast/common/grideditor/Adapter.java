package edu.emory.library.tast.common.grideditor;

import java.io.IOException;
import java.util.Map;

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
			String clientGridId,
			UIForm form,
			Row row,
			Map extensions,
			String inputPrefix,
			Value value,
			boolean readOnly) throws IOException;
	
	public void encodeRegJS(
			FacesContext context,
			StringBuffer regJS,
			GridEditorComponent gridEditor,
			String inputPrefix,
			Value value,
			boolean readOnly) throws IOException
	{
		regJS.append("{}");
	}

}
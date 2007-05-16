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
			String clientGridId,
			UIForm form,
			Row row,
			Column column,
			FieldType fieldType,
			String inputPrefix,
			Value value,
			boolean readOnly,
			boolean invokeCompare) throws IOException;
	
	public void createValueJavaScript(
			FacesContext context,
			StringBuffer regJS,
			GridEditorComponent gridEditor,
			String inputPrefix,
			Row row,
			Column column,
			Value value,
			boolean readOnly) throws IOException
	{
		regJS.append("{}");
	}

	public void createFieldTypeJavaScript(
			FacesContext context,
			StringBuffer regJS,
			GridEditorComponent gridEditor,
			FieldType fieldType) throws IOException
	{
		regJS.append("{}");
	}

}
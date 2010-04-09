/*
Copyright 2010 Emory University
	
	    This file is part of Trans-Atlantic Slave Voyages.
	
	    Trans-Atlantic Slave Voyages is free software: you can redistribute it and/or modify
	    it under the terms of the GNU General Public License as published by
	    the Free Software Foundation, either version 3 of the License, or
	    (at your option) any later version.
	
	    Trans-Atlantic Slave Voyages is distributed in the hope that it will be useful,
	    but WITHOUT ANY WARRANTY; without even the implied warranty of
	    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	    GNU General Public License for more details.
	
	    You should have received a copy of the GNU General Public License
	    along with Trans-Atlantic Slave Voyages.  If not, see <http://www.gnu.org/licenses/>. 
*/
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
			String cellId,
			Value value, boolean readOnly) throws IOException
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
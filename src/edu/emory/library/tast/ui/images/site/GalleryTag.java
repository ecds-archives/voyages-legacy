package edu.emory.library.tast.ui.images.site;

import javax.faces.component.UIComponent;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;

public class GalleryTag extends UIComponentTag {

	private String pictures;
	
	private String rows;
	
	private String columns;
	
	private String thumbnailHeight;
	
	private String thumbnailWidth;
	
	public String getComponentType() {
		return "Gallery";
	}

	
	protected void setProperties(UIComponent component) {

		super.setProperties(component);

		//Setting of properties
		if (pictures != null) {
			if (isValueReference(pictures)) {
				ValueBinding vb = getFacesContext().getApplication()
						.createValueBinding(pictures);
				component.setValueBinding("pictures", vb);
			} else {
				component.getAttributes().put("pictures", pictures);
			}
		}
		
		if (rows != null) {
			if (isValueReference(rows)) {
				ValueBinding vb = getFacesContext().getApplication()
						.createValueBinding(rows);
				component.setValueBinding("rows", vb);
			} else {
				component.getAttributes().put("rows", rows);
			}
		}
		
		if (columns != null) {
			if (isValueReference(columns)) {
				ValueBinding vb = getFacesContext().getApplication()
						.createValueBinding(columns);
				component.setValueBinding("columns", vb);
			} else {
				component.getAttributes().put("columns", columns);
			}
		}
	}
	
	public String getRendererType() {
		// TODO Auto-generated method stub
		return null;
	}


	public String getPictures() {
		return pictures;
	}


	public void setPictures(String pictures) {
		this.pictures = pictures;
	}


	public String getRows() {
		return rows;
	}


	public void setRows(String rows) {
		this.rows = rows;
	}


	public String getColumns() {
		return columns;
	}


	public void setColumns(String columns) {
		this.columns = columns;
	}


	public String getThumbnailHeight() {
		return thumbnailHeight;
	}


	public void setThumbnailHeight(String thumbnailHeight) {
		this.thumbnailHeight = thumbnailHeight;
	}


	public String getThumbnailWidth() {
		return thumbnailWidth;
	}


	public void setThumbnailWidth(String thumbnailWidth) {
		this.thumbnailWidth = thumbnailWidth;
	}

}

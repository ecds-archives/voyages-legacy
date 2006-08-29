package edu.emory.library.tast.ui.images.site;

import javax.faces.component.UIComponent;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;

import org.apache.myfaces.el.MethodBindingImpl;

public class GalleryTag extends UIComponentTag {

	private String pictures;
	
	private String rows;
	
	private String columns;
	
	private String thumbnailHeight;
	
	private String thumbnailWidth;
	
	private String action;
	
	private String searchCondition;
	
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
		
		if (searchCondition != null) {
			if (isValueReference(searchCondition)) {
				ValueBinding vb = getFacesContext().getApplication()
						.createValueBinding(searchCondition);
				component.setValueBinding("searchCondition", vb);
			} else {
				component.getAttributes().put("searchCondition", searchCondition);
			}
		}
		
		if (action != null && component instanceof GalleryComponent) {
			GalleryComponent gallery = (GalleryComponent)component;
			gallery.setAction(new MethodBindingImpl(getFacesContext().getApplication(), action, new Class[] {}));
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


	public String getAction() {
		return action;
	}


	public void setAction(String action) {
		this.action = action;
	}


	public String getSearchCondition() {
		return searchCondition;
	}


	public void setSearchCondition(String searchCondition) {
		this.searchCondition = searchCondition;
	}

}

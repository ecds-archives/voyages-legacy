package edu.emory.library.tast.ui.images.admin;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;

public class ImageListTag extends UIComponentTag
{
	
	private String images;
	private String listStyle;
	private String selectedImageId;
	private String action;
	private String columns;

	public String getComponentType()
	{
		return "ImageList";
	}

	public String getRendererType()
	{
		return null;
	}
	
	protected void setProperties(UIComponent component)
	{
		
		ImageListComponent imageList = (ImageListComponent) component;
		Application app = FacesContext.getCurrentInstance().getApplication();
		
		if (images != null && isValueReference(images))
		{
			ValueBinding vb = app.createValueBinding(images);
			component.setValueBinding("images", vb);
		}

		if (listStyle != null && isValueReference(listStyle))
		{
			ValueBinding vb = app.createValueBinding(listStyle);
			component.setValueBinding("listStyle", vb);
		}
		else
		{
			imageList.setListStyle(ImageListStyle.parse(listStyle));
		}
		
		if (selectedImageId != null && isValueReference(selectedImageId))
		{
			ValueBinding vb = app.createValueBinding(selectedImageId);
			component.setValueBinding("selectedImageId", vb);
		}
		else
		{
			imageList.setSelectedImageId(selectedImageId);
		}
		
		if (action != null && isValueReference(action))
		{
			MethodBinding mb = app.createMethodBinding(action, null);
			imageList.setAction(mb);
		}

		if (columns != null && isValueReference(columns))
		{
			ValueBinding vb = app.createValueBinding(columns);
			component.setValueBinding("columns", vb);
		}
		else
		{
			if (columns != null)
			{
				String[] columnsStr = columns.split("\\s*,\\s*");
				ImageListColumn[] columns = new ImageListColumn[columnsStr.length];
				for (int i = 0; i < columnsStr.length; i++)
				{
					columns[i] = new ImageListColumn(columnsStr[i]);
				}
				imageList.setColumns(columns);
			}
		}

	}

	public String getListStyle()
	{
		return listStyle;
	}

	public void setListStyle(String listStyle)
	{
		this.listStyle = listStyle;
	}

	public String getImages()
	{
		return images;
	}

	public void setImages(String images)
	{
		this.images = images;
	}

	public String getAction()
	{
		return action;
	}

	public void setAction(String action)
	{
		this.action = action;
	}

	public String getSelectedImageId()
	{
		return selectedImageId;
	}

	public void setSelectedImageId(String selectedImageId)
	{
		this.selectedImageId = selectedImageId;
	}

	public String getColumns()
	{
		return columns;
	}

	public void setColumns(String columns)
	{
		this.columns = columns;
	}

}

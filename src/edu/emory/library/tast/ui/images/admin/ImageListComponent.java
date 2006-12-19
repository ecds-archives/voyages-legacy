package edu.emory.library.tast.ui.images.admin;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.component.UICommand;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.ValueBinding;
import javax.faces.event.ActionEvent;

import edu.emory.library.tast.util.JsfUtils;
import edu.emory.library.tast.util.StringUtils;

public class ImageListComponent extends UICommand
{
	
	private boolean imagesSet = false;
	private List images;
	
	private boolean columnsSet = false;
	private ImageListColumn[] columns;
	
	private boolean listStyleSet = false;
	private ImageListStyle listStyle = ImageListStyle.Table;
	
	private boolean selectedImageIdSet = false;
	private String selectedImageId;
	
	public String getRendererType()
	{
		return null;
	}
	
	public String getFamily()
	{
		return null;
	}
	
	public Object saveState(FacesContext context)
	{
		Object values[] = new Object[4];
		values[0] = super.saveState(context);
		values[1] = listStyle;
		values[2] = selectedImageId;
		values[3] = columns;
		return values;
	}
	
	public void restoreState(FacesContext context, Object state)
	{
		Object values[] = (Object[]) state;
		super.restoreState(context, values[0]);
		listStyle = (ImageListStyle) values[1];
		selectedImageId = (String) values[2];
		columns = (ImageListColumn[]) values[3];
	}
	
	private String getFieldNameForSelectedImageId(FacesContext context)
	{
		return getClientId(context) + "_selected_id";
	}
	
	public void decode(FacesContext context)
	{

		Map params = context.getExternalContext().getRequestParameterMap();
		
		String imageId = JsfUtils.getParamString(params, getFieldNameForSelectedImageId(context));
		if (!StringUtils.isNullOrEmpty(imageId))
		{
			selectedImageId = imageId;
			queueEvent(new ActionEvent(this));
		}

	}
	
	public void processUpdates(FacesContext context)
	{
        ValueBinding vb = getValueBinding("selectedImageId");
        if (vb != null) vb.setValue(context, selectedImageId);
	}
	
	private void encodeImageThumbnail(FacesContext context, ResponseWriter writer, ImageListItem image, String onClick) throws IOException
	{

		writer.startElement("a", this);
		writer.writeAttribute("href", "#", null);
		writer.writeAttribute("onclick", onClick, null);
		
		writer.startElement("img", this);
		writer.writeAttribute("class", "imagelist-thumbnail", null);
		writer.writeAttribute("src", image.getUrl(), null);
		writer.writeAttribute("border", "0", null);
//		writer.writeAttribute("width", String.valueOf(thumbnailWidth), null);
//		writer.writeAttribute("height", String.valueOf(thumbnailHeight), null);
		writer.endElement("img");
		
		writer.endElement("a");

	}
	
	private void encodeTableOrList(FacesContext context, ResponseWriter writer, UIForm form, List images, boolean displayThumbnails) throws IOException
	{

		writer.startElement("table", this);
		writer.writeAttribute("border", "0", null);
		writer.writeAttribute("cellspacing", "0", null);
		writer.writeAttribute("cellpadding", "0", null);
		writer.writeAttribute("class", "imagelist-table", null);
		
		if (columns != null)
		{
			writer.startElement("tr", this);
			
			writer.startElement("td", this);
			writer.writeAttribute("class", "imagelist-table-header", null);
			if (displayThumbnails) writer.writeAttribute("colspan", "2", null);
			writer.write("Image");
			writer.endElement("td");
			
			writer.endElement("td");
			for (int i = 0; i < columns.length; i++)
			{
				writer.startElement("td", this);
				writer.writeAttribute("class", "imagelist-table-header", null);
				writer.write(columns[i].getName());
				writer.endElement("td");
			}
			writer.endElement("tr");
		}
		
		for (Iterator iter = images.iterator(); iter.hasNext();)
		{
			ImageListItem image = (ImageListItem) iter.next();
			
			String onClick = JsfUtils.generateSubmitJS(
					context, form,
					getFieldNameForSelectedImageId(context),
					image.getId());
			
			writer.startElement("tr", this);

			if (displayThumbnails)
			{
				writer.startElement("td", this);
				encodeImageThumbnail(context, writer, image, onClick);
				writer.endElement("td");
			}

			writer.startElement("td", this);
			writer.startElement("a", this);
			writer.writeAttribute("href", "#", null);
			writer.writeAttribute("onclick", onClick, null);
			writer.write(image.getName());
			writer.endElement("a");
			writer.endElement("td");
			
			String[] subItems = image.getSubItems();
			for (int i = 0; i < subItems.length; i++)
			{
				writer.startElement("td", this);
				writer.write(subItems[i]);
				writer.endElement("td");
			}

			writer.endElement("tr");
			
		}
		
		writer.endElement("table");
		
	}
	
	private void encodeGallery(FacesContext context, ResponseWriter writer, UIForm form, List images) throws IOException
	{

		writer.startElement("div", this);
		writer.writeAttribute("class", "imagelist-gallery", null);
		
		for (Iterator iter = images.iterator(); iter.hasNext();)
		{
			ImageListItem image = (ImageListItem) iter.next();
			
			String onClick = JsfUtils.generateSubmitJS(
					context, form,
					getFieldNameForSelectedImageId(context), image.getId());

			writer.startElement("div", this);
			writer.writeAttribute("class", "imagelist-gallery-image", null);
			
			writer.startElement("div", this);
			writer.writeAttribute("class", "imagelist-gallery-thumbnail", null);
			encodeImageThumbnail(context, writer, image, onClick);
			writer.endElement("div");

			writer.startElement("div", this);
			writer.writeAttribute("class", "imagelist-gallery-name", null);
			writer.startElement("a", this);
			writer.writeAttribute("href", "#", null);
			writer.writeAttribute("onclick", onClick, null);
			writer.write(image.getName());
			writer.endElement("a");
			writer.endElement("div");
			
//			writer.startElement("div", this);
//			writer.writeAttribute("class", "imagelist-gallery-size", null);
//			writer.write(image.getWidth() + " x " + image.getHeight());
//			writer.endElement("div");

			writer.endElement("div");
			
		}
		
		writer.endElement("div");
	
	}

	public void encodeBegin(FacesContext context) throws IOException
	{
		
		// general stuff
		ResponseWriter writer = context.getResponseWriter();
		UIForm form = JsfUtils.getForm(this, context);
		
		// get data from a bean
		List images = getImages();
		listStyle = getListStyle();
		selectedImageId = getSelectedImageId();
		
		// a field for storing the selected image id
		JsfUtils.encodeHiddenInput(this, writer,
				getFieldNameForSelectedImageId(context));

		// render table
		if (listStyle.equals(ImageListStyle.Table))
			encodeTableOrList(context, writer, form,
					images, false);

 		// render list
		else if (listStyle.equals(ImageListStyle.List))
			encodeTableOrList(context, writer, form,
					images, true);

		// render gallery
		else if (listStyle.equals(ImageListStyle.Gallery))
			encodeGallery(context, writer, form,
					images);
		
	}
	
	public void encodeChildren(FacesContext context) throws IOException
	{
	}
	
	public void encodeEnd(FacesContext context) throws IOException
	{
	}

	public void setImages(List images)
	{
		imagesSet = true;
		this.images = images;
	}

	public List getImages()
	{
        if (imagesSet) return images;
        ValueBinding vb = getValueBinding("images");
        if (vb == null) return images;
        return (List) vb.getValue(getFacesContext());
	}

	public void setListStyle(ImageListStyle listStyle)
	{
		this.listStyle = listStyle;
	}

	public ImageListStyle getListStyle()
	{
        if (listStyleSet) return listStyle;
        ValueBinding vb = getValueBinding("listStyle");
        if (vb == null) return listStyle;
        Object listStyleLocalObj = vb.getValue(getFacesContext());
        if (listStyleLocalObj instanceof String)
        	return ImageListStyle.parse((String) listStyleLocalObj);
        else
        	return (ImageListStyle) listStyleLocalObj;
	}

	public void setSelectedImageId(String selectedImageId)
	{
		selectedImageIdSet = true;
		this.selectedImageId = selectedImageId;
	}

	public String getSelectedImageId()
	{
        if (selectedImageIdSet) return selectedImageId;
        ValueBinding vb = getValueBinding("selectedImageId");
        if (vb == null) return selectedImageId;
        return (String) vb.getValue(getFacesContext());
	}

	public void setColumns(ImageListColumn[] columns)
	{
		columnsSet = true;
		this.columns = columns;
	}

	public ImageListColumn[] getColumns()
	{
        if (columnsSet) return columns;
        ValueBinding vb = getValueBinding("columnsId");
        if (vb == null) return columns;
        return (ImageListColumn[]) vb.getValue(getFacesContext());
	}

}
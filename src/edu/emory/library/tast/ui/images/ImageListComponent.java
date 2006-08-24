package edu.emory.library.tast.ui.images;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.component.UICommand;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.FacesEvent;

import edu.emory.library.tast.AppConfig;
import edu.emory.library.tast.dm.Image;
import edu.emory.library.tast.util.JsfUtils;

public class ImageListComponent extends UICommand
{
	
	private MethodBinding onImageSelected;

	private boolean imagesSet = false;
	private List images;

	private boolean listStyleSet = false;
	private ImageListStyle listStyle = ImageListStyle.Table;

	public String getFamily()
	{
		return "edu.emory.library.tast.ImageList";
	}
	
	public Object saveState(FacesContext context)
	{
		Object values[] = new Object[3];
		values[0] = super.saveState(context);
		values[1] = saveAttachedState(context, onImageSelected);
		values[2] = listStyle;
		return values;
	}
	
	public void restoreState(FacesContext context, Object state)
	{
		Object values[] = (Object[]) state;
		super.restoreState(context, values[0]);
		onImageSelected = (MethodBinding) restoreAttachedState(context, values[1]);
		listStyle = (ImageListStyle) values[2];
	}
	
	
	private String getFieldNameForSelectedImage(FacesContext context)
	{
		return getClientId(context) + "_selected_id";
	}
	
	public void decode(FacesContext context)
	{
		Map params = context.getExternalContext().getRequestParameterMap();
		
		int imageId = JsfUtils.getParamInt(
				params, getFieldNameForSelectedImage(context), -1);
		
		if (imageId != -1)
		{
			queueEvent(new ImageSelectedEvent(this, imageId));
			queueEvent(new ActionEvent(this));
		}

	}
	
	public void broadcast(FacesEvent event) throws AbortProcessingException
	{
		
		super.broadcast(event);
		
		if (event instanceof ImageSelectedEvent)
			if (onImageSelected != null)
				onImageSelected.invoke(getFacesContext(), new Object[] {event});
		
	}
	
	private void encodeImageThumbnail(FacesContext context, ResponseWriter writer, Image image, String onClick, int thumbnailWidth, int thumbnailHeight) throws IOException
	{

		writer.startElement("a", this);
		writer.writeAttribute("href", "#", null);
		writer.writeAttribute("onclick", onClick, null);
		
		writer.startElement("img", this);
		writer.writeAttribute("class", "imagelist-thumbnail", null);
		writer.writeAttribute("src", "images/" + image.getThumbnailFileName(), null);
		writer.writeAttribute("border", "0", null);
		writer.writeAttribute("width", String.valueOf(thumbnailWidth), null);
		writer.writeAttribute("height", String.valueOf(thumbnailHeight), null);
		writer.endElement("img");
		
		writer.endElement("a");

	}
	
	private void encodeTableOrList(FacesContext context, ResponseWriter writer, UIForm form, List images, int thumbnailWidth, int thumbnailHeight, boolean displayThumbnails) throws IOException
	{

		writer.startElement("table", this);
		writer.writeAttribute("border", "0", null);
		writer.writeAttribute("cellspacing", "0", null);
		writer.writeAttribute("cellpadding", "0", null);
		writer.writeAttribute("class", "imagelist-table", null);
		
		for (Iterator iter = images.iterator(); iter.hasNext();)
		{
			Image image = (Image) iter.next();
			
			String onClick = JsfUtils.generateSubmitJS(
					context, form,
					getFieldNameForSelectedImage(context), String.valueOf(image.getId()));
			
			writer.startElement("tr", this);

			if (displayThumbnails)
			{
				writer.startElement("td", this);
				encodeImageThumbnail(context, writer, image, onClick, thumbnailWidth, thumbnailHeight);
				writer.endElement("td");
			}

			writer.startElement("td", this);
			writer.startElement("a", this);
			writer.writeAttribute("href", "#", null);
			writer.writeAttribute("onclick", onClick, null);
			writer.write(image.getName());
			writer.endElement("a");
			writer.endElement("td");
			
			writer.startElement("td", this);
			writer.write(image.getWidth() + " x " + image.getHeight());
			writer.endElement("td");

			writer.endElement("tr");
			
		}
		
		writer.endElement("table");
		
	}
	
	private void encodeGallery(FacesContext context, ResponseWriter writer, UIForm form, List images, int thumbnailWidth, int thumbnailHeight) throws IOException
	{

		writer.startElement("div", this);
		
		for (Iterator iter = images.iterator(); iter.hasNext();)
		{
			Image image = (Image) iter.next();
			
			String onClick = JsfUtils.generateSubmitJS(
					context, form,
					getFieldNameForSelectedImage(context), String.valueOf(image.getId()));

			writer.startElement("div", this);
			writer.writeAttribute("class", "imagelist-gallery-image", null);
			
			writer.startElement("div", this);
			writer.writeAttribute("class", "imagelist-gallery-thumbnail", null);
			encodeImageThumbnail(context, writer, image, onClick, thumbnailWidth, thumbnailHeight);
			writer.endElement("div");

			writer.startElement("div", this);
			writer.writeAttribute("class", "imagelist-gallery-name", null);
			writer.writeAttribute("href", "#", null);
			writer.writeAttribute("onclick", onClick, null);
			writer.write(image.getName());
			writer.endElement("div");
			
			writer.startElement("div", this);
			writer.writeAttribute("class", "imagelist-gallery-size", null);
			writer.write(image.getWidth() + " x " + image.getHeight());
			writer.endElement("div");

			writer.endElement("div");
			
		}
		
		writer.endElement("div");
	
	}

	public void encodeBegin(FacesContext context) throws IOException
	{
		
		// general stuff
		ResponseWriter writer = context.getResponseWriter();
		UIForm form = JsfUtils.getForm(this, context);
		
		// load once
		int thumbnailWidth = AppConfig.getConfiguration().getInt(AppConfig.IMAGES_THUMBNAIL_WIDTH);
		int thumbnailHeight = AppConfig.getConfiguration().getInt(AppConfig.IMAGES_THUMBNAIL_HEIGHT);

		// a field for storing the selected image id
		JsfUtils.encodeHiddenInput(this, writer, getFieldNameForSelectedImage(context));

		// get data from a bean
		List images = getImages();
		listStyle = getListStyle();
		System.out.println(listStyle);
		
 		// render table
		if (listStyle.equals(ImageListStyle.Table))
			encodeTableOrList(context, writer, form,
					images,
					thumbnailWidth, thumbnailHeight, false);

 		// render list
		else if (listStyle.equals(ImageListStyle.List))
			encodeTableOrList(context, writer, form,
					images,
					thumbnailWidth, thumbnailHeight, true);

		// render gallery
		else if (listStyle.equals(ImageListStyle.Gallery))
			encodeGallery(context, writer, form,
					images,
					thumbnailWidth, thumbnailHeight);
		
	}
	
	public void encodeChildren(FacesContext context) throws IOException
	{
	}
	
	public void encodeEnd(FacesContext context) throws IOException
	{
	}

	public void setImages(List images)
	{
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

	public MethodBinding getOnImageSelected()
	{
		return onImageSelected;
	}

	public void setOnImageSelected(MethodBinding onImageSelected)
	{
		this.onImageSelected = onImageSelected;
	}

}
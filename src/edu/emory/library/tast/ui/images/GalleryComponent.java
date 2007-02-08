package edu.emory.library.tast.ui.images;

import java.io.IOException;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import edu.emory.library.tast.util.JsfUtils;

public class GalleryComponent extends UIComponentBase
{
	
	private boolean thumbnailWidthSet = false;
	private int thumbnailWidth;
	
	private boolean thumbnailHeightSet = false;
	private int thumbnailHeight;
	
	private boolean columnsCountSet = false;
	private int columnsCount;

	private boolean imagesSet = false;
	private GalleryImage[] images;

	public String getFamily()
	{
		return null;
	}
	
	public Object saveState(FacesContext context)
	{
		Object values[] = new Object[4];
		values[0] = super.saveState(context);
		values[1] = new Integer(thumbnailWidth);
		values[2] = new Integer(thumbnailHeight);
		values[3] = new Integer(columnsCount);
		return values;
	}
	
	public void restoreState(FacesContext context, Object state)
	{
		Object[] values = (Object[]) state;
		super.restoreState(context, values[0]);
		thumbnailWidth = ((Integer) values[1]).intValue();
		thumbnailHeight = ((Integer) values[2]).intValue();
		columnsCount = ((Integer) values[3]).intValue();
	}
	
	public void encodeBegin(FacesContext context) throws IOException
	{

		ResponseWriter writer = context.getResponseWriter();
		
		images = getImages();
		thumbnailWidth = getThumbnailWidth();
		thumbnailHeight = getThumbnailHeight();
		columnsCount = getColumnsCount();
		
		String thumbnailWidthString = String.valueOf(thumbnailWidth);
		String thumbnailHeightString = String.valueOf(thumbnailHeight);
		
		writer.startElement("table", this);
		writer.writeAttribute("border", "0", null);
		writer.writeAttribute("cellspacing", "0", null);
		writer.writeAttribute("cellpadding", "0", null);
		writer.writeAttribute("class", "gallery-table", null);

		int column = 0;
		for (int i = 0; i < images.length; i++)
		{
			
			GalleryImage image = images[i];
			
			String url =
				context.getExternalContext().getRequestContextPath() +
				"/ThumbnailServlet" +
				"?i=" + image.getImageName() +
				"&w=" + thumbnailWidthString +
				"&h=" + thumbnailHeightString;
			
			if (column % columnsCount == 0)
			{
				if (column > 0) writer.endElement("tr");
				writer.startElement("tr", this);
				column = 0;
			}
			else
			{
				column++;
			}
			
			writer.startElement("td", this);
			writer.writeAttribute("class", "gallery-image", null);
			
			writer.startElement("div", this);
			writer.writeAttribute("class", "gallery-image", null);

			writer.startElement("img", this);
			writer.writeAttribute("src", url, null);
			writer.writeAttribute("width", thumbnailWidthString, null);
			writer.writeAttribute("height", thumbnailHeightString, null);
			writer.writeAttribute("border", "0", null);
			writer.endElement("img");
			
			writer.startElement("div", this);
			writer.writeAttribute("class", "gallery-image-frame", null);
			writer.endElement("div");

			writer.endElement("div");
			
			writer.startElement("div", this);
			writer.writeAttribute("class", "gallery-image-label", null);
			writer.write(image.getLabel());
			writer.endElement("div");
			
			if (image.hasDescription())
			{
				writer.startElement("div", this);
				writer.writeAttribute("class", "gallery-image-description", null);
				writer.write(image.getDescription());
				writer.endElement("div");
			}

			writer.endElement("td");
			
		}
		
		writer.endElement("tr");
		
		writer.endElement("table");

	}

	public GalleryImage[] getImages()
	{
		return (GalleryImage[]) JsfUtils.getCompPropObject(this, getFacesContext(),
				"images", imagesSet, images);
	}

	public void setImages(GalleryImage[] images)
	{
		imagesSet = true;
		this.images = images;
	}

	public int getThumbnailHeight()
	{
		return JsfUtils.getCompPropInt(this, getFacesContext(),
				"thumbnailHeight", thumbnailHeightSet, thumbnailHeight);
	}

	public void setThumbnailHeight(int thumbnailHeight)
	{
		thumbnailHeightSet = true;
		this.thumbnailHeight = thumbnailHeight;
	}

	public int getThumbnailWidth()
	{
		return JsfUtils.getCompPropInt(this, getFacesContext(),
				"thumbnailWidth", thumbnailWidthSet, thumbnailWidth);
	}

	public void setThumbnailWidth(int thumbnailWidth)
	{
		thumbnailWidthSet = true;
		this.thumbnailWidth = thumbnailWidth;
	}

	public int getColumnsCount()
	{
		return JsfUtils.getCompPropInt(this, getFacesContext(),
				"columnsCount", columnsCountSet, columnsCount);
	}

	public void setColumnsCount(int columns)
	{
		columnsCountSet = true;
		this.columnsCount = columns;
	}

}

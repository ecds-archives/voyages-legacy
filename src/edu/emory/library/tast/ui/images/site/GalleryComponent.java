package edu.emory.library.tast.ui.images.site;

import java.io.IOException;
import java.util.Map;

import javax.faces.component.UIComponentBase;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;

import org.apache.myfaces.el.MethodBindingImpl;

import edu.emory.library.tast.dm.Image;
import edu.emory.library.tast.ui.images.site.GalleryRequestBean.GalleryParams;
import edu.emory.library.tast.util.JsfUtils;
import edu.emory.library.tast.util.StringUtils;

public class GalleryComponent extends UIComponentBase
{

//	private static final String GALLERY_BACK_BUTTON = "gallery-back-button";
//	private static final String GALLERY_FORWARD_BUTTON = "gallery-forward-button";

	private static final int IMG_MAX_WIDTH = 600;
	private static final int IMG_MAX_HEIGHT = 480;
	
	private MethodBinding showEvent;
	
	public void restoreState(FacesContext arg0, Object arg1) {
		Object[] state = (Object[])arg1;
		super.restoreState(arg0, state[0]);
		this.showEvent = (MethodBinding)restoreAttachedState(arg0, state[1]);
	}

	public Object saveState(FacesContext arg0) {
		Object[] state = new Object[2];
		state[0] = super.saveState(arg0); 
		state[1] = saveAttachedState(arg0, showEvent);
		return state;
	}

	public void decode(FacesContext context) {
		Map params = context.getExternalContext().getRequestParameterMap();

		if (params.get(this.getHiddenFieldId(context)) != null) {
			String param = (String)params.get(this.getHiddenFieldId(context));
			queueEvent(new ShowVoyageEvent(this, new Integer(param)));
		}

	}
	
	public void broadcast(FacesEvent event) throws AbortProcessingException {
		super.broadcast(event);

		if (event instanceof ShowVoyageEvent && showEvent != null) {
			showEvent.invoke(getFacesContext(), new Object[] { event });
		}

	}

	public void encodeBegin(FacesContext context) throws IOException
	{

		UIForm form = JsfUtils.getForm(this, context);
		ResponseWriter writer = context.getResponseWriter();

		JsfUtils.encodeHiddenInput(this, writer, this.getHiddenFieldId(context));

		int rows = Integer.parseInt(this.getValueOrAttribute(context, "rows").toString());
		int cols = Integer.parseInt(this.getValueOrAttribute(context, "columns").toString());
		int thumbnailWidth = Integer.parseInt(this.getValueOrAttribute(context, "thumbnailWidth").toString());
		int thumbnailHeight = Integer.parseInt(this.getValueOrAttribute(context, "thumbnailHeight").toString());
		PictureGalery gallery = (PictureGalery) this.getBoundedValue(context, "pictures");

		GalleryRequestBean.GalleryParams params = (GalleryRequestBean.GalleryParams) this.getValueOrAttribute(context, "galleryParams");
		params.restoreState();
		
		StringBuffer js = new StringBuffer();
		
		if (gallery != null)
		{

			int set = Integer.parseInt(params.getVisibleSet());
			int pict = Integer.parseInt(params.getVisiblePicture());
			
			writer.startElement("div", this);
			writer.writeAttribute("class", "gallery-thumbnails", null);

			writer.startElement("table", this);
			writer.writeAttribute("border", "0", null);
			writer.writeAttribute("cellspacing", "0", null);
			writer.writeAttribute("cellpadding", "0", null);
			writer.writeAttribute("class", "gallery-thumbnails-table", null);
			writer.startElement("tr", this);
			
			writer.startElement("td", this);
			writer.writeAttribute("class", "gallery-arrow-prev", null);

			int prevPictIndex = (set - 1) * (rows * cols) + pict - 1; 
			if (prevPictIndex >= 0)
			{
				
				int prevSet = set;
				int prevPict = pict - 1;
				if (prevPict == -1)
				{
					prevPict = cols * rows - 1;
					prevSet--;
				}
				
				js.setLength(0);
				js.append("galleryp.faces");
				js.append("?").append(GalleryRequestBean.GALLERY_TYPE).append("=").append(params.getGalleryType());
				js.append("&").append(GalleryRequestBean.ID).append("=").append(params.getId());
				js.append("&").append(GalleryRequestBean.SET).append("=").append(prevSet);
				js.append("&").append(GalleryRequestBean.PICT).append("=").append(prevPict);
				
				writer.startElement("a", this);
				writer.writeAttribute("href", js.toString(), null);
				writer.startElement("img", this);
				writer.writeAttribute("border", "0", null);
				writer.writeAttribute("width", "16", null);
				writer.writeAttribute("height", "16", null);
				writer.writeAttribute("src", "gallery-arrow-prev-active.png", null);
				writer.endElement("img");
				writer.endElement("a");

			}
			else
			{
				
				writer.startElement("img", this);
				writer.writeAttribute("border", "0", null);
				writer.writeAttribute("width", "16", null);
				writer.writeAttribute("height", "16", null);
				writer.writeAttribute("src", "gallery-arrow-prev-blurred.png", null);
				writer.endElement("img");
				
			}
			
			writer.endElement("td");		

//			writer.startElement("td", this);
//			writer.startElement("table", this);
//			writer.writeAttribute("border", "1", null);
//			writer.writeAttribute("cellspacing", "0", null);
//			writer.writeAttribute("cellpadding", "0", null);
//			writer.writeAttribute("class", "gallery-table-thumbnails", null);
			GaleryImage[] picts = gallery.getPictures(set, rows * cols);
			for (int i = 0; i < rows * cols && i < picts.length; i++)
			{

//				if (i % rows == 0) {
//					writer.startElement("tr", this);
//				}

				Image image = picts[i].getImage();

				StringBuffer link = new StringBuffer();
				link.append("galleryp.faces?");
				link.append(GalleryRequestBean.GALLERY_TYPE).append("=");
				link.append(params.getGalleryType());
				link.append("&").append(GalleryRequestBean.ID).append("=");
				link.append(params.getId());
				link.append("&").append(GalleryRequestBean.SET).append("=");
				link.append(params.getVisibleSet());
				link.append("&").append(GalleryRequestBean.PICT).append("=");
				link.append(i);
				
				writer.startElement("td", this);
				if (params.getVisiblePicture() != null)
				{
					int n = Integer.parseInt(params.getVisiblePicture());
					if (n == i)
					{
						writer.writeAttribute("class", "gallery-thumbnail-selected", null);
					}
					else
					{
						writer.writeAttribute("class", "gallery-thumbnail", null);
					}
				}
				
				String thumbnailSrc = "servlet/thumbnail" + 
					"?i=" + image.getFileName() +
					"&w=" + thumbnailWidth +
					"&h=" + thumbnailHeight;

				writer.startElement("a", this);
				writer.writeAttribute("href", link.toString(), null);
				writer.startElement("img", this);
				writer.writeAttribute("border", "0", null);
				writer.writeAttribute("width", String.valueOf(thumbnailWidth), null);
				writer.writeAttribute("height", String .valueOf(thumbnailHeight), null);
				writer.writeAttribute("src", thumbnailSrc, null);
				writer.endElement("img");
				writer.endElement("a");
				
				writer.startElement("div", this);
				writer.writeAttribute("class", "gallery-thumbnail-title", null);
				writer.startElement("a", this);
				writer.writeAttribute("href", link.toString(), null);
				writer.write(image.getTitle());
				writer.endElement("a");
				writer.endElement("div");

				writer.endElement("td");
//				if ((i + 1) % rows == 0) {
//					writer.endElement("tr");
//				}
			}
//			writer.endElement("table");
//			writer.startElement("div", this);
//			writer.writeAttribute("align", "right;", null);
//			writer.write("Showing images from " + (pictures.getFirst(set, rows * cols) + 1)
//					+ " to " + (pictures.getLast(set, rows * cols)) + " out of "
//					+ pictures.getNumberOfAll());
//			writer.endElement("div");
//			writer.endElement("td");

			writer.startElement("td", this);
			writer.writeAttribute("class", "gallery-arrow-next", null);
			
			
			int nextPictIndex = (set - 1) * (rows * cols) + pict + 1; 
			if (nextPictIndex < gallery.getNumberOfAll())
			{
				
				int nextSet = set;
				int nextPict = pict + 1;
				if (nextPict == rows * cols)
				{
					nextSet++;
					nextPict = 0;
				}
				
				js.setLength(0);
				js.append("galleryp.faces");
				js.append("?").append(GalleryRequestBean.GALLERY_TYPE).append("=").append(params.getGalleryType());
				js.append("&").append(GalleryRequestBean.ID).append("=").append(params.getId());
				js.append("&").append(GalleryRequestBean.SET).append("=").append(nextSet);
				js.append("&").append(GalleryRequestBean.PICT).append("=").append(nextPict);
				
				writer.startElement("a", this);
				writer.writeAttribute("href", js.toString(), null);
				writer.startElement("img", this);
				writer.writeAttribute("border", "0", null);
				writer.writeAttribute("width", "16", null);
				writer.writeAttribute("height", "16", null);
				writer.writeAttribute("src", "gallery-arrow-next-active.png", null);
				writer.endElement("img");
				writer.endElement("a");

			}
			else
			{
				
				writer.startElement("img", this);
				writer.writeAttribute("border", "0", null);
				writer.writeAttribute("width", "16", null);
				writer.writeAttribute("height", "16", null);
				writer.writeAttribute("src", "gallery-arrow-next-blurred.png", null);
				writer.endElement("img");
				
			}
			
			writer.endElement("td");

			writer.endElement("tr");
			writer.endElement("table");

			writer.endElement("div");
			
			if (params.getVisiblePicture() != null)
			{

				int size = Integer.parseInt(params.getVisiblePicture());
				GaleryImage visibleImage = picts[size >= picts.length ? picts.length - 1 : size];
				
				int imageWidth = visibleImage.getImage().getWidth(); 
				int imageHeight = visibleImage.getImage().getHeight();
				
				if (IMG_MAX_HEIGHT * imageWidth >= IMG_MAX_WIDTH * imageHeight)
				{
					imageHeight = (int)(((double)IMG_MAX_WIDTH / (double)imageWidth) * imageHeight);
					imageWidth = IMG_MAX_WIDTH;
				}
				else
				{
					imageWidth = (int)(((double)IMG_MAX_HEIGHT / (double)imageHeight) * imageWidth);
					imageHeight = IMG_MAX_HEIGHT;
				}
				
				writer.startElement("div", this);
				writer.writeAttribute("class", "gallery-counter", null);
				writer.write(((set - 1) * (rows * cols) + pict + 1) + " / " + gallery.getNumberOfAll());
				writer.endElement("div");

				writer.startElement("div", this);
				writer.writeAttribute("class", "gallery-image", null);
				
				writer.startElement("table", this);
				writer.writeAttribute("border", "0", null);
				writer.writeAttribute("cellspacing", "0", null);
				writer.writeAttribute("cellpadding", "0", null);
				writer.writeAttribute("class", "gallery-image-table", null);
				writer.startElement("tr", this);
				
				writer.startElement("td", this);
				writer.writeAttribute("class", "gallery-image", null);
				
				writer.startElement("img", this);
				writer.writeAttribute("border", "0", null);
				writer.writeAttribute("width", String.valueOf(imageWidth), null);
				writer.writeAttribute("height", String.valueOf(imageHeight), null);
				writer.writeAttribute("src", "servlet/thumbnail" + 
					"?i=" + visibleImage.getImage().getFileName() +
					"&w=" + imageWidth +
					"&h=" + imageHeight, null);
				
				writer.endElement("img");
				writer.endElement("td");
				
				writer.startElement("td", this);
				writer.writeAttribute("class", "gallery-image-info", null);
				this.printImageInfoNew(context, form, writer, visibleImage, params);
				writer.endElement("td");

				writer.endElement("tr");
				writer.endElement("table");
				
				writer.endElement("div");

			}

		}
	}
	
	private void startInfoTableElement(ResponseWriter writer) throws IOException
	{
		writer.startElement("tr", this);
	}
	
	private void printInfoTableLabel(ResponseWriter writer, String label) throws IOException
	{
		writer.startElement("td", this);
		writer.writeAttribute("class", "gallery-image-info-table-label", null);
		writer.write(label);
		writer.endElement("td");
	}

	private void startInfoTableValue(ResponseWriter writer) throws IOException
	{
		writer.startElement("td", this);
		writer.writeAttribute("class", "gallery-image-info-table-value", null);
	}

	private void endInfoTableValue(ResponseWriter writer) throws IOException
	{
		writer.endElement("td");
	}

	private void endInfoTableElement(ResponseWriter writer) throws IOException
	{
		writer.endElement("tr");
	}

	private void printInfoTableElement(ResponseWriter writer, String label, String value) throws IOException
	{
		startInfoTableElement(writer);
		printInfoTableLabel(writer, label);
		startInfoTableValue(writer);
		writer.write(value);
		endInfoTableValue(writer);
		endInfoTableElement(writer);
	}

	private void printImageInfoNew(FacesContext context, UIForm form, ResponseWriter writer, GaleryImage visibleImage, GalleryParams params) throws IOException
	{
		
		writer.startElement("div", this);
		writer.writeAttribute("class", "gallery-image-title", null);
		writer.write(visibleImage.getImage().getTitle());
		if (!StringUtils.isNullOrEmpty(visibleImage.getImage().getDate()))
		{
			writer.write(" ");
			writer.startElement("span", this);
			writer.writeAttribute("class", "gallery-image-year", null);
			writer.write(" (");
			writer.write(visibleImage.getImage().getDate());
			writer.write(")");
			writer.endElement("span");
		}
		writer.endElement("div");
		
		writer.startElement("div", this);
		writer.writeAttribute("class", "gallery-image-description", null);
		writer.write(visibleImage.getImage().getDescription());
		writer.endElement("div");
		
		writer.startElement("table", this);
		writer.writeAttribute("border", "0", null);
		writer.writeAttribute("cellspacing", "0", null);
		writer.writeAttribute("cellpadding", "0", null);
		writer.writeAttribute("class", "gallery-image-info-table", null);

		String source = visibleImage.getImage().getSource(); 
		if (!StringUtils.isNullOrEmpty(source))
			printInfoTableElement(writer,
					"Source", source);

		String creator = visibleImage.getImage().getCreator(); 
		if (!StringUtils.isNullOrEmpty(creator))
			printInfoTableElement(writer,
					"Creator", creator);

		if (visibleImage.getImage().getVoyageid() != null)
		{
			
//			StringBuffer link = new StringBuffer();
//			link.append("galleryp.faces?").append("vid=");
//			link.append(visibleImage.getImage().getVoyageid());

			String jsClick = JsfUtils.generateSubmitJS(context, form,
					getHiddenFieldId(context),
					String.valueOf(visibleImage.getImage().getVoyageid()));

			startInfoTableElement(writer);
			printInfoTableLabel(writer, "Voyage");
			startInfoTableValue(writer);
			
			writer.write("ID = ");
			writer.write(String.valueOf(visibleImage.getImage().getVoyageid()));
			writer.write(", ");

			writer.startElement("a", this);
			writer.writeAttribute("href", "#", null);
			writer.writeAttribute("onclick", jsClick, null);
			writer.write("see details");
			writer.endElement("a");
			
			endInfoTableValue(writer);
			endInfoTableElement(writer);

		}

		writer.endElement("table");

	}
	
	public void encodeEnd(FacesContext context) throws IOException
	{
	}
	
	/*

	private void printImageInfo(FacesContext context, UIForm form,
			ResponseWriter writer, GaleryImage visibleImage, GalleryRequestBean.GalleryParams params) throws IOException {
		 
		
		writer.startElement("div", this);
		writer.writeAttribute("style", "width: "
				+ visibleImage.getImage().getWidth() + "px;", null);
		writer.writeAttribute("align", "left", null);
		writer.startElement("table", this);
		writer.writeAttribute("class", "gallery-info-table", null);
		writer.startElement("tr", this);
		writer.startElement("td", this);
		writer.write("Title:");
		writer.endElement("td");
		writer.startElement("td", this);
		if (visibleImage.getImage().getTitle() != null) {
			writer.write(visibleImage.getImage().getTitle());
		} else {
			writer.write("none");
		}
		writer.endElement("td");
		writer.endElement("tr");

		writer.startElement("tr", this);
		writer.startElement("td", this);
		writer.write("Description:");
		writer.endElement("td");
		writer.startElement("td", this);
		if (visibleImage.getImage().getDescription() != null) {
			writer.write(visibleImage.getImage().getDescription());
		} else {
			writer.write("none");
		}
		writer.endElement("td");
		writer.endElement("tr");
		
		
		writer.startElement("tr", this);
		writer.startElement("td", this);
		writer.write("Date:");
		writer.endElement("td");
		writer.startElement("td", this);
		if (visibleImage.getImage().getDate() != null) {
			writer.write(visibleImage.getImage().getDate());
		} else {
			writer.write("unknown");
		}
		writer.endElement("td");
		writer.endElement("tr");
		
		writer.startElement("tr", this);
		writer.startElement("td", this);
		writer.write("Source:");
		writer.endElement("td");
		writer.startElement("td", this);
		if (visibleImage.getImage().getSource() != null) {
			writer.write(visibleImage.getImage().getSource());
		} else {
			writer.write("unknown");
		}
		writer.endElement("td");
		writer.endElement("tr");
		
		writer.startElement("tr", this);
		writer.startElement("td", this);
		writer.write("Creator:");
		writer.endElement("td");
		writer.startElement("td", this);
		if (visibleImage.getImage().getCreator() != null) {
			writer.write(visibleImage.getImage().getCreator());
		} else {
			writer.write("unknown");
		}
		writer.endElement("td");
		writer.endElement("tr");
		
		
		writer.startElement("tr", this);
		writer.startElement("td", this);
		writer.write("Linked voyage:");
		writer.endElement("td");
		writer.startElement("td", this);	
		if (visibleImage.getImage().getVoyageid() != null) {
			StringBuffer link = new StringBuffer();
			link.append("galleryp.faces?").append("vid=");
			link.append(visibleImage.getImage().getVoyageid() + "");
			
			writer.startElement("a", this);
			writer.writeAttribute("href", "#", null);
			String jsClick = JsfUtils.generateSubmitJS(context, form,
					getHiddenFieldId(context), "" + visibleImage.getImage().getVoyageid());
			writer.writeAttribute("onclick", jsClick, null);
			writer.write(visibleImage.getImage().getVoyageid() + "");
			writer.endElement("a");
		} else {
			writer.write("none");
		}
		writer.endElement("td");
		writer.endElement("tr");
		
//		writer.write("Related people:");
//		writer.endElement("td");
//		writer.startElement("td", this);
//		Person[] persons = visibleImage.getPeople();
//		if (persons != null) {
//			for (int i = 0; i < persons.length; i++) {
//				Person person = persons[i];
//				writer.startElement("a", this);
//
//				StringBuffer link = new StringBuffer();
//				link.append("galleryp.faces?").append(GalleryRequestBean.GALLERY_TYPE);				
//				link.append("=").append("people");
//				link.append("&").append(GalleryRequestBean.ID);
//				link.append("=").append(person.getId());
//				link.append("&").append(GalleryRequestBean.SET);
//				link.append("=1");				
//				writer.writeAttribute("href", link, null);
//				if (person.getFirstName() != null) {
//					writer.write(person.getFirstName());
//				}
//				writer.write(" ");
//				if (person.getLastName() != null) {
//					writer.write(person.getLastName());
//				}
//				writer.endElement("a");
//				if (i + 1 < persons.length) {
//					writer.write("; ");
//				}
//			}
//		}
//		writer.endElement("td");
//		writer.endElement("tr");
//
//		writer.startElement("tr", this);
//		writer.startElement("td", this);
//		writer.write("Related ports:");
//		writer.endElement("td");
//		writer.startElement("td", this);
//		Port[] ports = visibleImage.getPorts();
//		if (ports != null) {
//			for (int i = 0; i < ports.length; i++) {
//				Port port = ports[i];
//				writer.startElement("a", this);
//				
//				StringBuffer link = new StringBuffer();
//				link.append("galleryp.faces?").append(GalleryRequestBean.GALLERY_TYPE);				
//				link.append("=").append("ports");
//				link.append("&").append(GalleryRequestBean.ID);
//				link.append("=").append(port.getId());
//				link.append("&").append(GalleryRequestBean.SET);
//				link.append("=1");
//				writer.writeAttribute("href", link, null);
//
//				writer.write(port.getName());
//				writer.endElement("a");
//				if (i + 1 < ports.length) {
//					writer.write("; ");
//				}
//			}
//		}
//		writer.endElement("td");
//		writer.endElement("tr");
//
//		writer.startElement("tr", this);
//		writer.startElement("td", this);
//		writer.write("Related regions:");
//		writer.endElement("td");
//		writer.startElement("td", this);
//		Region[] regions = visibleImage.getRegions();
//		if (regions != null) {
//			for (int i = 0; i < regions.length; i++) {
//				Region region = regions[i];
//				writer.startElement("a", this);
//				
//				StringBuffer link = new StringBuffer();
//				link.append("galleryp.faces?").append(GalleryRequestBean.GALLERY_TYPE);				
//				link.append("=").append("regions");
//				link.append("&").append(GalleryRequestBean.ID);
//				link.append("=").append(region.getId());
//				link.append("&").append(GalleryRequestBean.SET);
//				link.append("=1");
//				writer.writeAttribute("href", link, null);
//
//				writer.write(region.getName());
//				writer.endElement("a");
//				if (i + 1 < regions.length) {
//					writer.write("; ");
//				}
//			}
//		}
//		writer.endElement("td");
//		writer.endElement("tr");
		writer.endElement("table");
		writer.endElement("div");
	}

	private void encodeButton(FacesContext context, UIForm form,
			ResponseWriter writer, String button, String js) throws IOException {
		writer.startElement("td", this);
		writer.startElement("div", this);
		writer.writeAttribute("class", button, null);
		writer.writeAttribute("onclick", js, null);
		writer.endElement("div");
		writer.endElement("td");
	}

	public void encodeEnd(FacesContext context) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		writer.endElement("div");
	}
	*/

	private Object getBoundedValue(FacesContext context, String string) {
		ValueBinding vb = this.getValueBinding(string);
		if (vb != null && vb.getValue(context) != null) {
			return vb.getValue(context);
		}
		return null;
	}

	private Object getValueOrAttribute(FacesContext context, String string) {
		if (this.getAttributes().containsKey(string)) {
			return this.getAttributes().get(string);
		} else if (this.getBoundedValue(context, string) != null) {
			return this.getBoundedValue(context, string);
		} else {
			return "-1";
		}
	}

	private String getHiddenFieldId(FacesContext context) {
		return this.getId() + "_actionSensor";
	}

	public void setShowEventHandler(MethodBindingImpl impl) {
		this.showEvent = impl;
	}

	public String getFamily()
	{
		// TODO Auto-generated method stub
		return null;
	}
}

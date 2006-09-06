package edu.emory.library.tast.ui.images.site;

import java.io.IOException;
import java.util.Map;

import javax.faces.component.UICommand;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.ValueBinding;
import javax.faces.event.ActionEvent;
import javax.faces.event.FacesEvent;

import org.hibernate.mapping.ValueVisitor;

import edu.emory.library.tast.dm.Image;
import edu.emory.library.tast.dm.Person;
import edu.emory.library.tast.dm.Port;
import edu.emory.library.tast.dm.Region;
import edu.emory.library.tast.util.JsfUtils;

public class GalleryComponent extends UICommand {

	private static final String GALLERY_BACK_BUTTON = "gallery-back-button";

	private static final String GALLERY_FORWARD_BUTTON = "gallery-forward-button";

	public void decode(FacesContext context) {
		Map params = context.getExternalContext().getRequestParameterMap();

		String action = (String) params.get(getHiddenFieldId(context));
		if (action != null) {
			int rows = Integer.parseInt(this.getValueOrAttribute(context,
					"rows").toString());
			int cols = Integer.parseInt(this.getValueOrAttribute(context,
					"columns").toString());
			PictureGalery pictures = (PictureGalery) this.getBoundedValue(
					context, "pictures");
		
			ValueBinding searchCondition = (ValueBinding)this.getValueBinding("searchCondition");
			
			if (action.startsWith("search_")) {
				if (searchCondition != null) {
					searchCondition.setValue(context, action.substring("search_".length()));
					this.queueEvent(new ActionEvent(this));
				}
			} else if (pictures != null) {
				if (GALLERY_FORWARD_BUTTON.equals(action)) {
					if (pictures.canMoveForward(rows * cols)) {
						pictures.moveForward(rows * cols);
					}
				} else if (GALLERY_BACK_BUTTON.equals(action)) {
					if (pictures.canMoveBackward(rows * cols)) {
						pictures.moveBackward(rows * cols);
					}
				} else if (!action.trim().equals("")) {
					int visible = Integer.parseInt(action);

					GaleryImage[] picts = pictures.getPictures(rows * cols);
					pictures.setVisiblePicture(picts[visible]);
				}
			}
			
		}

	}

	public void encodeBegin(FacesContext context) throws IOException {
		
		UIForm form = JsfUtils.getForm(this, context);
		
		ResponseWriter writer = context.getResponseWriter();
		writer.startElement("div", this);
		writer.writeAttribute("align", "center", null);

		JsfUtils
				.encodeHiddenInput(this, writer, this.getHiddenFieldId(context));

		int rows = Integer.parseInt(this.getValueOrAttribute(context, "rows")
				.toString());
		int cols = Integer.parseInt(this
				.getValueOrAttribute(context, "columns").toString());
		int thumbnailWidth = Integer.parseInt(this.getValueOrAttribute(context,
				"thumbnailWidth").toString());
		int thumbnailHeight = Integer.parseInt(this.getValueOrAttribute(
				context, "thumbnailHeight").toString());

		PictureGalery pictures = (PictureGalery) this.getBoundedValue(context,
				"pictures");
		if (pictures != null) {

			writer.startElement("table", this);
			writer.writeAttribute("class", "gallery-table", null);
			writer.startElement("tr", this);
			this.encodeButton(context, form, writer, GALLERY_BACK_BUTTON);

			writer.startElement("td", this);
			writer.startElement("table", this);
			writer.writeAttribute("class", "gallery-table-thumbnails", null);
			GaleryImage[] picts = pictures.getPictures(rows * cols);
			for (int i = 0; i < rows * cols && i < picts.length; i++) {
				if (i % rows == 0) {
					writer.startElement("tr", this);
				}
				writer.startElement("td", this);
				Image image = picts[i].getImage();
				writer.startElement("a", this);
				writer.writeAttribute("href", "#", null);
				String js = JsfUtils.generateSubmitJS(context, JsfUtils
						.getForm(this, context),
						this.getHiddenFieldId(context), i + "");
				writer.writeAttribute("onclick", js, null);

				writer.startElement("img", this);
				if (thumbnailWidth != -1) {
					writer.writeAttribute("width", String
							.valueOf(thumbnailWidth), null);
				}
				if (thumbnailHeight != -1) {
					writer.writeAttribute("height", String
							.valueOf(thumbnailHeight), null);
				}
				writer.writeAttribute("src", "servlet/thumbnail?i="
						+ image.getFileName() + "&w=" + thumbnailWidth + "&h=" + thumbnailHeight, null);
				
				writer.writeAttribute("style", "cursor: pointer;", null);

				writer.write("<br/>");
				writer.write(image.getTitle());

				writer.endElement("td");
				if ((i + 1) % rows == 0) {
					writer.endElement("tr");
				}
			}
			writer.endElement("table");
			writer.startElement("div", this);
			writer.writeAttribute("align", "right;", null);
			writer.write("Showing images from " + (pictures.getFirst() + 1) + " to " + (pictures.getFirst() + rows*cols) + " out of "+ pictures.getNumberOfAll());
			writer.endElement("div");
			writer.endElement("td");

			this.encodeButton(context, form, writer, GALLERY_FORWARD_BUTTON);
			writer.endElement("tr");
			writer.endElement("table");

			GaleryImage visibleImage = pictures.getVisiblePicture();
			if (visibleImage != null) {
				writer.write(visibleImage.getImage().getTitle());
				writer.write("<br/>");
				writer.startElement("img", this);
				writer.writeAttribute("src", "images/"
						+ visibleImage.getImage().getFileName(), null);
				writer.endElement("img");
				this.printImageInfo(context, form, writer, visibleImage);
			}

		}
	}

	private void printImageInfo(FacesContext context, UIForm form, ResponseWriter writer,
			GaleryImage visibleImage) throws IOException {
		writer.startElement("div", this);
		writer.writeAttribute("style", "width: " + visibleImage.getImage().getWidth() + "px;", null);
		writer.writeAttribute("align", "left", null);
		writer.startElement("table", this);
		writer.writeAttribute("class", "gallery-info-table", null);
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
		writer.write("Related people:");
		writer.endElement("td");
		writer.startElement("td", this);
		Person[] persons = visibleImage.getPeople();
		if (persons != null) {
			for (int i = 0; i < persons.length; i++) {				
				Person person = persons[i];
				writer.startElement("a", this);
				
				String js = JsfUtils.generateSubmitJS(context, form,
						this.getHiddenFieldId(context), "search_person_" + person.getId());
				writer.writeAttribute("onclick", js, null);
				writer.writeAttribute("href", "#", null);
				if (person.getFirstName() != null) {
					writer.write(person.getFirstName());
				}
				writer.write(" ");
				if (person.getLastName() != null) {
					writer.write(person.getLastName());
				}
				writer.endElement("a");
				if (i + 1 < persons.length) {
					writer.write("; ");
				}
			}
		}
		writer.endElement("td");
		writer.endElement("tr");

		writer.startElement("tr", this);
		writer.startElement("td", this);
		writer.write("Related ports:");
		writer.endElement("td");
		writer.startElement("td", this);
		Port[] ports = visibleImage.getPorts();
		if (ports != null) {
			for (int i = 0; i < ports.length; i++) {
				Port port = ports[i];
				writer.startElement("a", this);
				writer.writeAttribute("href", "#", null);
				
				String js = JsfUtils.generateSubmitJS(context, form,
						this.getHiddenFieldId(context), "search_port_" + port.getId());
				writer.writeAttribute("onclick", js, null);
				
				writer.write(port.getName());
				writer.endElement("a");
				if (i + 1 < persons.length) {
					writer.write("; ");
				}
			}
		}
		writer.endElement("td");
		writer.endElement("tr");

		writer.startElement("tr", this);
		writer.startElement("td", this);
		writer.write("Related regions:");
		writer.endElement("td");
		writer.startElement("td", this);
		Region[] regions = visibleImage.getRegions();
		if (regions != null) {
			for (int i = 0; i < regions.length; i++) {
				Region region = regions[i];
				writer.startElement("a", this);
				writer.writeAttribute("href", "#", null);
				
				String js = JsfUtils.generateSubmitJS(context, form,
						this.getHiddenFieldId(context), "search_region_" + region.getId());
				writer.writeAttribute("onclick", js, null);
				
				writer.write(region.getName());
				writer.endElement("a");
				if (i + 1 < persons.length) {
					writer.write("; ");
				}
			}
		}
		writer.endElement("td");
		writer.endElement("tr");
		writer.endElement("table");
		writer.endElement("div");
	}

	private void encodeButton(FacesContext context, UIForm form, ResponseWriter writer,
			String button) throws IOException {
		writer.startElement("td", this);
		writer.startElement("div", this);
		writer.writeAttribute("class", button, null);
		String js = JsfUtils.generateSubmitJS(context, form, this.getHiddenFieldId(context), button);
		writer.writeAttribute("onclick", js, null);
		writer.endElement("div");
		writer.endElement("td");
	}

	public void encodeEnd(FacesContext context) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		writer.endElement("div");
	}

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
}

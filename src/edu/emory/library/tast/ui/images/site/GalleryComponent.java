package edu.emory.library.tast.ui.images.site;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.ValueBinding;

import edu.emory.library.tast.dm.Image;
import edu.emory.library.tast.dm.Person;
import edu.emory.library.tast.dm.Port;
import edu.emory.library.tast.dm.Region;
import edu.emory.library.tast.util.JsfUtils;

public class GalleryComponent extends UIComponentBase {

	private static final String GALLERY_BACK_BUTTON = "gallery-back-button";

	private static final String GALLERY_FORWARD_BUTTON = "gallery-forward-button";

	public String getFamily() {
		return null;
	}

	public void decode(FacesContext context) {
		Map params = context.getExternalContext().getRequestParameterMap();

		String action = (String) params.get(getHiddenFieldId(context));
		if (action != null) {
			int rows = Integer.parseInt(this.getValueOrAttribute(context,
					"rows").toString());
			int cols = Integer.parseInt(this.getValueOrAttribute(context,
					"columns").toString());
			PictureGalery pictures = (PictureGalery) this.getValueBinding(
					context, "pictures");

			if (pictures != null) {
				if (GALLERY_FORWARD_BUTTON.equals(action)) {
					if (pictures.canMoveForward(rows * cols)) {
						pictures.moveForward(rows * cols);
					}
				} else if (GALLERY_BACK_BUTTON.equals(action)) {
					if (pictures.canMoveBackward(rows * cols)) {
						pictures.moveBackward(rows * cols);
					}
				} else {
					int visible = Integer.parseInt(action);

					Image[] picts = pictures.getPictures(rows * cols);
					pictures.setVisiblePicture(picts[visible]);
				}
			}
		}

	}

	public void encodeBegin(FacesContext context) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		writer.startElement("div", this);

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

		PictureGalery pictures = (PictureGalery) this.getValueBinding(context,
				"pictures");
		if (pictures != null) {

			writer.startElement("table", this);
			writer.startElement("tr", this);
			writer.startElement("td", this);
			writer.writeAttribute("style", "text-align: center;", null);

			writer.startElement("table", this);
			writer.startElement("tr", this);
			this.encodeButton(context, writer, GALLERY_BACK_BUTTON);

			writer.startElement("td", this);
			writer.startElement("table", this);
			Image[] picts = pictures.getPictures(rows * cols);
			for (int i = 0; i < rows * cols && i < picts.length; i++) {
				if (i % rows == 0) {
					writer.startElement("tr", this);
				}
				writer.startElement("td", this);
				Image image = picts[i];
				writer.startElement("img", this);
				if (thumbnailWidth != -1) {
					writer.writeAttribute("width", String
							.valueOf(thumbnailWidth), null);
				}
				if (thumbnailHeight != -1) {
					writer.writeAttribute("height", String
							.valueOf(thumbnailHeight), null);
				}
				writer.writeAttribute("src", "../../images/"
						+ image.getThumbnailFileName(), null);
				String js = JsfUtils.generateSubmitJS(context, JsfUtils
						.getForm(this, context),
						this.getHiddenFieldId(context), i + "");
				writer.writeAttribute("onclick", js, null);
				writer.writeAttribute("style", "cursor: pointer;", null);
				writer.endElement("td");
				if ((i + 1) % rows == 0) {
					writer.endElement("tr");
				}
			}
			writer.endElement("table");
			writer.endElement("td");

			this.encodeButton(context, writer, GALLERY_FORWARD_BUTTON);
			writer.endElement("tr");
			writer.endElement("table");

			writer.endElement("td");
			writer.endElement("tr");
			writer.startElement("tr", this);
			writer.startElement("td", this);
			writer.writeAttribute("style", "text-align: center;", null);

			Image visibleImage = pictures.getVisiblePicture();
			if (visibleImage != null) {
				writer.startElement("img", this);
				writer.writeAttribute("src", "../../images/"
						+ visibleImage.getFileName(), null);
				writer.endElement("img");
				this.printImageInfo(context, writer, visibleImage);
			}

			writer.endElement("td");

			writer.endElement("tr");
			writer.endElement("table");
		}
	}

	private void printImageInfo(FacesContext context, ResponseWriter writer,
			Image visibleImage) throws IOException {
		writer.startElement("table", this);
		writer.startElement("tr", this);
		writer.startElement("td", this);
		writer.write("Description:");
		writer.endElement("td");
		writer.startElement("td", this);
		visibleImage.getDescription();
		writer.endElement("td");
		writer.endElement("tr");

		writer.startElement("tr", this);
		writer.startElement("td", this);
		writer.write("Connected people:");
		writer.endElement("td");
		writer.startElement("td", this);
		Set persons = visibleImage.getPeople();
		if (persons != null) {
			Iterator iter = persons.iterator();
			while (iter.hasNext()) {
				Person person = (Person) iter.next();
				writer.startElement("a", this);
				writer.writeAttribute("href", "#", null);
				writer.write(person.getFirstName());
				writer.write(" ");
				writer.write(person.getLastName());
				writer.endElement("a");
			}
		}
		writer.endElement("td");
		writer.endElement("tr");

		writer.startElement("tr", this);
		writer.startElement("td", this);
		writer.write("Connected ports:");
		writer.endElement("td");
		writer.startElement("td", this);
		Set ports = visibleImage.getPorts();
		if (ports != null) {
			Iterator iter = ports.iterator();
			while (iter.hasNext()) {
				Port port = (Port) iter.next();
				writer.startElement("a", this);
				writer.writeAttribute("href", "#", null);
				writer.write(port.getName());
				writer.endElement("a");
			}
		}
		writer.endElement("td");
		writer.endElement("tr");

		writer.startElement("tr", this);
		writer.startElement("td", this);
		writer.write("Connected regions:");
		writer.endElement("td");
		writer.startElement("td", this);
		Set regions = visibleImage.getRegions();
		if (regions != null) {
			Iterator iter = regions.iterator();
			while (iter.hasNext()) {
				Region region = (Region) iter.next();
				writer.startElement("a", this);
				writer.writeAttribute("href", "#", null);
				writer.write(region.getName());
				writer.endElement("a");
			}
		}
		writer.endElement("td");
		writer.endElement("tr");
	}

	private void encodeButton(FacesContext context, ResponseWriter writer,
			String button) throws IOException {
		writer.startElement("td", this);
		writer.startElement("div", this);
		writer.writeAttribute("class", button, null);
		String js = JsfUtils.generateSubmitJS(context, JsfUtils.getForm(this,
				context), this.getHiddenFieldId(context), button);
		writer.writeAttribute("onclick", js, null);
		writer.endElement("div");
		writer.endElement("td");
	}

	public void encodeEnd(FacesContext context) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		writer.endElement("div");
	}

	private Object getValueBinding(FacesContext context, String string) {
		ValueBinding vb = this.getValueBinding(string);
		if (vb != null && vb.getValue(context) != null) {
			return vb.getValue(context);
		}
		return null;
	}

	private Object getValueOrAttribute(FacesContext context, String string) {
		if (this.getAttributes().containsKey(string)) {
			return this.getAttributes().get(string);
		} else if (this.getValueBinding(context, string) != null) {
			return this.getValueBinding(context, string);
		} else {
			return "-1";
		}
	}

	private String getHiddenFieldId(FacesContext context) {
		return this.getId() + "_actionSensor";
	}
}

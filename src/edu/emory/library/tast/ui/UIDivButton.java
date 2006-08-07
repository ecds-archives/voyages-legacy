package edu.emory.library.tast.ui;

import java.io.IOException;
import java.util.Map;

import javax.faces.component.UIComponentBase;
import javax.faces.component.UIForm;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;

import edu.emory.library.tast.ui.search.query.UtilsJSF;
import edu.emory.library.tast.ui.search.table.ClickEvent;

public class UIDivButton extends UIOutput {

	private static final String PRESSED = "pressed";
	/**
	 * Show details binding.
	 */
	private MethodBinding action;

	/**
	 * Default constructor.
	 *
	 */
	public UIDivButton() {
		super();
	}

	/**
	 * Restore state overload.
	 */
	public void restoreState(FacesContext context, Object state) {
		Object[] values = (Object[]) state;
		super.restoreState(context, values[0]);
		action = (MethodBinding) restoreAttachedState(context, values[1]);
	}

	/**
	 * Save state overload.
	 */
	public Object saveState(FacesContext context) {
		Object[] values = new Object[2];
		values[0] = super.saveState(context);
		values[1] = saveAttachedState(context, action);
		return values;
	}

	/**
	 * Decode overload.
	 */
	public void decode(FacesContext context) {

		Map params = context.getExternalContext().getRequestParameterMap();

		String hidden = (String) params.get(getHiddenFieldName(context));
		if (hidden != null && hidden.equals(PRESSED)) {
			queueEvent(new ClickEvent(this));
		}

	}

	/**
	 * Encode begin overload.
	 */
	public void encodeBegin(FacesContext context) throws IOException {
		ResponseWriter writer = context.getResponseWriter();

		UtilsJSF.encodeHiddenInput(this, writer, getHiddenFieldName(context));
		
		writer.startElement("div", this);
		String id = (String)this.getAttributes().get("id");
		if (id != null) {
			writer.writeAttribute("id", id, null);
		}
		String style = (String)this.getAttributes().get("style");
		if (style != null) {
			writer.writeAttribute("style", style, null);
		}
		String styleClass = (String)this.getAttributes().get("styleClass");
		if (styleClass != null) {
			writer.writeAttribute("class", styleClass, null);
		}
		
		StringBuffer buffer = new StringBuffer();
		UIForm form = UtilsJSF.getForm(this, context);
		UtilsJSF.appendSubmitJS(buffer, context, form, getHiddenFieldName(context), PRESSED);
		writer.writeAttribute("onclick", buffer.toString(), null);
		
		
	}

	/**
	 * Encode end overload.
	 */
	public void encodeEnd(FacesContext context) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		writer.endElement("div");
	}
	

	private String getHiddenFieldName(FacesContext context) {
		return this.getClientId(context) + "_clicked";
	}
	
	public void broadcast(FacesEvent event) throws AbortProcessingException {
		super.broadcast(event);

		if (event instanceof ClickEvent && action != null) {
			action.invoke(getFacesContext(), new Object[] { event });
		}

	}

	public MethodBinding getAction() {
		return action;
	}

	public void setAction(MethodBinding action) {
		this.action = action;
	}
}

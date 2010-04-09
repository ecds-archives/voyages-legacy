/*
Copyright 2010 Emory University
	
	    This file is part of Trans-Atlantic Slave Voyages.
	
	    Trans-Atlantic Slave Voyages is free software: you can redistribute it and/or modify
	    it under the terms of the GNU General Public License as published by
	    the Free Software Foundation, either version 3 of the License, or
	    (at your option) any later version.
	
	    Trans-Atlantic Slave Voyages is distributed in the hope that it will be useful,
	    but WITHOUT ANY WARRANTY; without even the implied warranty of
	    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	    GNU General Public License for more details.
	
	    You should have received a copy of the GNU General Public License
	    along with Trans-Atlantic Slave Voyages.  If not, see <http://www.gnu.org/licenses/>. 
*/
package edu.emory.library.tast.common;

import java.io.IOException;
import java.util.Map;

import javax.faces.component.UIForm;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.MethodBinding;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;

import edu.emory.library.tast.common.listing.ClickEvent;
import edu.emory.library.tast.util.JsfUtils;

public class SimpleButtonComponent extends UIOutput {

	private static final String PRESSED = "pressed";
	/**
	 * Show details binding.
	 */
	private MethodBinding action;

	/**
	 * Default constructor.
	 *
	 */
	public SimpleButtonComponent() {
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

		JsfUtils.encodeHiddenInput(this, writer, getHiddenFieldName(context));
		
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
		UIForm form = JsfUtils.getForm(this, context);
		JsfUtils.appendSubmitJS(buffer, context, form, getHiddenFieldName(context), PRESSED);
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

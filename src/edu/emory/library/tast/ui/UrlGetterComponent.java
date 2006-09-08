package edu.emory.library.tast.ui;

import java.io.IOException;

import javax.faces.component.UICommand;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.servlet.http.HttpServletRequest;

public class UrlGetterComponent extends UICommand {

	public void decode(FacesContext context) {
		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!decode!!!!!!!!!!");
		String param = (String)this.getValue(context, "param");
		if (param != null) {
			HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
			String value = request.getParameter(param);
			if (value != null) {
				this.setValue(context, "value", value);
			}
		}
	}

	private Object getValue(FacesContext context, String name) {
		ValueBinding binding = this.getValueBinding(name);
		if (binding != null) {
			return binding.getValue(context);
		} else {
			return this.getAttributes().get(name);
		}
	}
	
	private void setValue(FacesContext context, String name, Object value) {
		ValueBinding binding = this.getValueBinding(name);
		if (binding != null) {
			binding.setValue(context, value);
		}
	}

	public void encodeBegin(FacesContext context) throws IOException {
		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!encodeBegin!!!!!!!!!!");
	}

	public void encodeEnd(FacesContext context) throws IOException {
		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!encodeEnd!!!!!!!!!!");
	}

	
}

package edu.emory.library.tast;

import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public class TastResource {
	private static ResourceBundle resource = null;
	
	private static synchronized void init() {
		if (resource == null) {
			resource = PropertyResourceBundle.getBundle("resources");
		}
	}
	
	public static String getText(String text) {
		if (resource == null) {
			init();
		}
		return resource.getString(text);
	}
	
	public static void main(String[] params) {
		System.out.println(getText("TEST"));
		System.out.println(getText("TEST2"));
	}
	
}

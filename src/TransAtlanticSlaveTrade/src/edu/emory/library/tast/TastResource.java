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

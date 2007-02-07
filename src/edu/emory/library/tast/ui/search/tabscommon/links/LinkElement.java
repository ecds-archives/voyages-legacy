package edu.emory.library.tast.ui.search.tabscommon.links;

import java.io.Serializable;

public class LinkElement implements Serializable {

	private int id;
	private String lablel;
	private String classStyle;
	private boolean clickable;
	private int firstVisible;
	private boolean selectedNumber = false;
	
	public LinkElement(int id, String label, boolean clickable, int firstVisible, String classStyle) {
		this.id = id;
		this.lablel = label;
		this.clickable = clickable;
		this.firstVisible = firstVisible;
		this.classStyle = classStyle;
	}
	
	public LinkElement(int id, String label, boolean clickable, boolean selectedNumber, int firstVisible, String classStyle) {
		this.id = id;
		this.lablel = label;
		this.clickable = clickable;
		this.firstVisible = firstVisible;
		this.selectedNumber = selectedNumber;
		this.classStyle = classStyle;
	}
	
	public int getId() {
		return this.id;
	}

	public String getLabel() {
		return this.lablel;
	}

	public boolean isClickable() {
		return this.clickable;
	}

	public int getFirstVisible() {
		return firstVisible;
	}
	
	public boolean isSelectedNumber() {
		return this.selectedNumber ;
	}

	public String getClassStyle() {
		return classStyle;
	}
}

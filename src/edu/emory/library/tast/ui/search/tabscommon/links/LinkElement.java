package edu.emory.library.tast.ui.search.tabscommon.links;

import java.io.Serializable;

public class LinkElement implements Serializable {

	private int id;
	private String lablel;
	private boolean clickable;
	private int firstVisible;
	
	public LinkElement(int id, String label, boolean clickable, int firstVisible) {
		this.id = id;
		this.lablel = label;
		this.clickable = clickable;
		this.firstVisible = firstVisible;
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
}

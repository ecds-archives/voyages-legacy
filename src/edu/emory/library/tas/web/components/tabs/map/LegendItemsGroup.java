package edu.emory.library.tas.web.components.tabs.map;

import java.util.ArrayList;
import java.util.List;

public class LegendItemsGroup {
	
	private String title;
	
	private List items = new ArrayList();
	
	public LegendItemsGroup(String title) {
		this.title = title;
	}
	
	public void addItemToGroup(LegendItem item) {
		this.items.add(item);
	}
	
	public LegendItem[] getItems() {
		return (LegendItem[])this.items.toArray(new LegendItem[] {});
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("Legend group:" ).append(this.title);
		LegendItem[] items = this.getItems();
		for (int i = 0; i < items.length; i++) {
			buffer.append("   ").append(items[i]);
		}
		return buffer.toString();
	}
}

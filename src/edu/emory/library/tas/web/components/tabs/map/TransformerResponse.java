package edu.emory.library.tas.web.components.tabs.map;

public class TransformerResponse {
	
	private AbstractMapItem[] items;
	
	private LegendItemsGroup[] legendItems;

	public TransformerResponse(AbstractMapItem[] items, LegendItemsGroup[] legend) {
		this.items = items;
		this.legendItems = legend;
	}
	
	public AbstractMapItem[] getItems() {
		return items;
	}

	public LegendItemsGroup[] getLegendItems() {
		return legendItems;
	}
	
}

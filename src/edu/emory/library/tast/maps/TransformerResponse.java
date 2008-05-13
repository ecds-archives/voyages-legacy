package edu.emory.library.tast.maps;

/**
 * Response class that is returned by AbstractDataTransformer.transform().
 * @author Pawel Jurczyk
 *
 */
public class TransformerResponse {
	
	/**
	 * Map items.
	 */
	private AbstractMapItem[] items;
	
	/**
	 * Map legend.
	 */
	private LegendItemsGroup[] legendItems;

	/**
	 * Constructor of object.
	 * @param items map items
	 * @param legend legend
	 */
	public TransformerResponse(AbstractMapItem[] items, LegendItemsGroup[] legend) {
		this.items = items;
		this.legendItems = legend;
	}
	
	/**
	 * Gets map items.
	 * @return
	 */
	public AbstractMapItem[] getItems() {
		return items;
	}

	/**
	 * Gets map legend.
	 * @return
	 */
	public LegendItemsGroup[] getLegendItems() {
		return legendItems;
	}
	
}

package edu.emory.library.tast.ui.maps;

import edu.emory.library.tast.dm.attributes.Attribute;

/**
 * Abstract class that should be used as superclass for any classes that will
 * transform results from database to map format.
 * The class contains AttributesMap field that is an information about attributes
 * in the data of transformFunction. This attrbiutes provides the information that
 * e.g. ((Object[])data[1])[0] is portdep Voyage attribute.
 * 
 * @author Pawel Jurczyk
 *
 */
public abstract class AbstractDataTransformer {
	
	/**
	 * Mapping of attributes to col/row in input data.
	 */
	private AttributesMap attributesMap;
	
	/**
	 * Default constructor.
	 * @param map mapping of data[i][j] to Attribute
	 */
	public AbstractDataTransformer(AttributesMap map) {
		this.attributesMap = map;
	}
	
	/**
	 * Function that should be implemented in subclasses. The function parses
	 * result from database and prepares response.
	 * @param data data from database
	 * @param min minimal value in data
	 * @param max maximal value in data
	 * @return TransformResponse object
	 */
	public abstract TransformerResponse transformData(Object[] data, double min, double max);
	
	/**
	 * Gets attribute that is in passed data in (i,j) position.
	 * @param i i position
	 * @param j j position
	 * @return
	 */
	protected Attribute getAttribute(int i, int j) {
		return attributesMap.getAttribute(i, j);
	}
}

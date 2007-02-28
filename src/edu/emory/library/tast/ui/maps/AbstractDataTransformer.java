package edu.emory.library.tast.ui.maps;

import edu.emory.library.tast.ui.search.tabscommon.VisibleAttributeInterface;

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
	public abstract TransformerResponse transformData(AbstractTransformerQueryHolder data);
	
	/**
	 * Gets attribute that is in passed data in (i,j) position.
	 * @param i i position
	 * @param j j position
	 * @return
	 */
	protected VisibleAttributeInterface getAttribute(int i, int j) {
		return attributesMap.getAttribute(i, j);
	}
	
	protected void round(double[] ranges) {
		
		int rounds[] = new int[ranges.length];
		//double range = ranges[ranges.length - 1] - ranges[0]; 
		
		for (int i = 0; i < ranges.length; i++) {
			int cRound = this.getRound(ranges[0], ranges[ranges.length - 1], ranges[i]);
			rounds[i] = cRound;
		}
		
		int reminder = 0;
		if (((int)(ranges[0]) % rounds[0]) != 0) {
			reminder = 1;
		}
		ranges[ranges.length - 1] = ((int)(ranges[ranges.length - 1] / rounds[ranges.length - 1]) + reminder) * rounds[ranges.length - 1];
		for (int i = 0; i < ranges.length- 1; i++) {
			ranges[i] = ((int)(ranges[i] / rounds[i])) * rounds[i];
		}
	}
	
	private int getRound(double min, double max, double number) {
		if (max - min < 10) {
			return 1;
		} else if (max - min < 501) {
			return 10;
		} else if (max - min < 5001) {
			return 100;
		} else if (max - min < 50001) {
			return 1000;
		} else {
			return 10000;
		}
	}
}

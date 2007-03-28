package edu.emory.library.tast.common.table.links;

import java.io.Serializable;

/**
 * This class represents links available in pager.
 * Pager is component which is visible below the table
 * and allows switching between result sets.
 *
 */
public class LinkElement implements Serializable {

	private static final long serialVersionUID = 1L;
	
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

	/**
	 * Label of link (1, 2, next, prev)
	 * @return
	 */
	public String getLabel() {
		return this.lablel;
	}

	/**
	 * True if one can click on link.
	 * @return
	 */
	public boolean isClickable() {
		return this.clickable;
	}

	/**
	 * Number of first visible row after clicking this link 
	 * @return
	 */
	public int getFirstVisible() {
		return firstVisible;
	}
	
	/**
	 * Tells whether this link is selected
	 * @return
	 */
	public boolean isSelectedNumber() {
		return this.selectedNumber ;
	}

	/**
	 * Class (css style) of this link
	 * @return
	 */
	public String getClassStyle() {
		return classStyle;
	}
}

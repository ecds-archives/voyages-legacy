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
package edu.emory.library.tast.database.tabscommon;

/**
 * Action that memores any parameters that can be used in performAction method.
 * This type of action is used in table bean to record operations performed
 * on GUI and gives a possibility of reverting them (cancel feature)
 *
 */
public abstract class MemorizedAction {
	
	/**
	 * Parameters used in performAction implementation. 
	 */
	protected Object[] params = null;
	
	/**
	 * Constructor.
	 * @param params parameters that will be used in performAction
	 */
	public MemorizedAction(Object[] params) {
		this.params = params;
	}
	
	/**
	 * Method that should perform specific actions using params provided in constructor.
	 *
	 */
	public abstract void performAction();
}

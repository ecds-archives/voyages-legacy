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
package edu.emory.library.tast.essays;

import javax.faces.component.UIParameter;

public class EssaysBean
{
	
	private UIParameter paramActiveMenuId;
	
	public String getActiveMenuId()
	{
		return paramActiveMenuId != null ?
			(String) paramActiveMenuId.getValue() :
				null;
	}

	public UIParameter getParamActiveMenuId()
	{
		return paramActiveMenuId;
	}

	public void setParamActiveMenuId(UIParameter paramActiveMenuId)
	{
		this.paramActiveMenuId = paramActiveMenuId;
	}

	public boolean isIntroExpanded()
	{
		String activeMenuId = getActiveMenuId();
		return activeMenuId != null && activeMenuId.startsWith("essays-intro");
	}

	public boolean isSeasonalityExpanded()
	{
		String activeMenuId = getActiveMenuId();
		return activeMenuId != null && activeMenuId.startsWith("essays-seasonality");
	}
	
	public boolean isSourcesExpanded()
	{
		String activeMenuId = getActiveMenuId();
		return activeMenuId != null && activeMenuId.startsWith("sources");
	}
	
	public boolean isMethodExpanded()
	{
		String activeMenuId = getActiveMenuId();
		return activeMenuId != null && activeMenuId.startsWith("methodology");
	}

}

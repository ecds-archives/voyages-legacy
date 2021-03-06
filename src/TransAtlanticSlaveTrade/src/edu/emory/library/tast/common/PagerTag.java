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
package edu.emory.library.tast.common;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;

public class PagerTag extends UIComponentTag
{
	
	private String firstRecord = "1";
	private String lastRecord;
	private String pageSize = "20";
	private String currentPage = "0";
	private String maxShownPages = "21";

	protected void setProperties(UIComponent component)
	{
		
		Application app = FacesContext.getCurrentInstance().getApplication();
		PagerComponent pager  = (PagerComponent) component;
		
		if (firstRecord != null && isValueReference(firstRecord))
		{
			ValueBinding vb = app.createValueBinding(firstRecord);
			component.setValueBinding("firstRecord", vb);
		}
		else
		{
			try
			{
				pager.setFirstRecord(Integer.parseInt(firstRecord));
			}
			catch (NumberFormatException nfe)
			{
			}
		}
		
		if (lastRecord != null && isValueReference(lastRecord))
		{
			ValueBinding vb = app.createValueBinding(lastRecord);
			component.setValueBinding("lastRecord", vb);
		}
		else
		{
			try
			{
				pager.setLastRecord(Integer.parseInt(lastRecord));
			}
			catch (NumberFormatException nfe)
			{
			}
		}

		if (pageSize != null && isValueReference(pageSize))
		{
			ValueBinding vb = app.createValueBinding(pageSize);
			component.setValueBinding("pageSize", vb);
		}
		else
		{
			try
			{
				pager.setPageSize(Integer.parseInt(pageSize));
			}
			catch (NumberFormatException nfe)
			{
			}
		}

		if (currentPage != null && isValueReference(currentPage))
		{
			ValueBinding vb = app.createValueBinding(currentPage);
			component.setValueBinding("currentPage", vb);
		}
		else
		{
			try
			{
				pager.setCurrentPage(Integer.parseInt(currentPage));
			}
			catch (NumberFormatException nfe)
			{
			}
		}

		if (maxShownPages != null && isValueReference(maxShownPages))
		{
			ValueBinding vb = app.createValueBinding(maxShownPages);
			component.setValueBinding("maxShownPages", vb);
		}
		else
		{
			try
			{
				pager.setMaxShownPages(Integer.parseInt(maxShownPages));
			}
			catch (NumberFormatException nfe)
			{
			}
		}
		
	}

	public String getComponentType()
	{
		return "Pager";
	}

	public String getRendererType()
	{
		return null;
	}

	public String getCurrentPage()
	{
		return currentPage;
	}

	public void setCurrentPage(String currentPage)
	{
		this.currentPage = currentPage;
	}

	public String getFirstRecord()
	{
		return firstRecord;
	}

	public void setFirstRecord(String firstRecord)
	{
		this.firstRecord = firstRecord;
	}

	public String getLastRecord()
	{
		return lastRecord;
	}

	public void setLastRecord(String lastRecord)
	{
		this.lastRecord = lastRecord;
	}

	public String getMaxShownPages()
	{
		return maxShownPages;
	}

	public void setMaxShownPages(String maxShownPages)
	{
		this.maxShownPages = maxShownPages;
	}

	public String getPageSize()
	{
		return pageSize;
	}

	public void setPageSize(String pageSize)
	{
		this.pageSize = pageSize;
	}

}

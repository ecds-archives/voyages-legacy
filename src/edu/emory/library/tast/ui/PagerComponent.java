package edu.emory.library.tast.ui;

import java.io.IOException;

import javax.faces.component.UIComponentBase;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import edu.emory.library.tast.util.JsfUtils;

public class PagerComponent extends UIComponentBase
{
	
	private boolean firstRecordSet = false;
	private int firstRecord = 0;
	
	private boolean lastRecordSet = false;
	private int lastRecord = 0;
	
	private boolean pageSizeSet = false;
	private int pageSize = 0;

	private boolean currentPageSet = false;
	private int currentPage = 0;
	
	private boolean maxShownPagesSet = false;
	private int maxShownPages = 0;

	public String getFamily()
	{
		return null;
	}
	
	public Object saveState(FacesContext context)
	{
		Object[] values = new Object[6];
		values[0] = super.saveState(context);
		values[1] = new Integer(pageSize);
		values[2] = new Integer(maxShownPages);
		values[3] = new Integer(firstRecord);
		values[4] = new Integer(lastRecord);
		values[5] = new Integer(currentPage);
		return values;
	}
	
	public void restoreState(FacesContext context, Object state)
	{
		Object[] values = (Object[]) state;
		super.restoreState(context, values[0]);
		pageSize = ((Integer) values[1]).intValue();
		maxShownPages = ((Integer) values[2]).intValue();
		firstRecord = ((Integer) values[3]).intValue();
		lastRecord = ((Integer) values[4]).intValue();
		currentPage = ((Integer) values[5]).intValue();
	}
	
	private String getHtmlCurrentPageNameInput(FacesContext context)
	{
		return getClientId(context);
	}
	
	public void encodeBegin(FacesContext context) throws IOException
	{
		
		ResponseWriter writer = context.getResponseWriter();
		UIForm form = JsfUtils.getForm(this, context);
		String currentPageField = getHtmlCurrentPageNameInput(context);
		
		firstRecord = getFirstRecord();
		lastRecord = getLastRecord();
		pageSize = getPageSize();
		currentPage = getCurrentPage();
		maxShownPages = getMaxShownPages();
		
		JsfUtils.encodeHiddenInput(this, writer,
				currentPageField);
		
		int totalCount = lastRecord - firstRecord + 1;
		if (totalCount <= 0) return;
		
		int totalPages = (totalCount - 1) / pageSize + 1;
		if (totalPages == 1) return;
		
		writer.startElement("table", this);
		writer.writeAttribute("border", "0", null);
		writer.writeAttribute("cellspacing", "0", null);
		writer.writeAttribute("cellpadding", "0", null);
		writer.writeAttribute("align", "left", null);
		writer.writeAttribute("class", "pager", null);
		writer.startElement("tr", this);
		
		if (currentPage > 1)
		{
			
			String onClick = JsfUtils.generateSubmitJS(context, form,
					currentPageField, String.valueOf(currentPage - 1)); 
			
			writer.startElement("td", this);
			writer.writeAttribute("class", "pager-previous-enabled", null);
			writer.writeAttribute("onclick", onClick, null);
			writer.write("Previous");
			writer.endElement("td");

		}
		else
		{
			
			writer.startElement("td", this);
			writer.writeAttribute("class", "pager-previous-disabled", null);
			writer.write("Previous");
			writer.endElement("td");

		}
		
		int extraToLeft, extraToRight;
		int fromPage, toPage;
		
		if (maxShownPages >= 1)
		{
			int extraPages = maxShownPages - 1;
			if (extraPages % 2 == 0)
			{
				extraToLeft = extraPages >> 1;
				extraToRight = extraPages >> 1;
			}
			else
			{
				extraToLeft = maxShownPages >> 1;
				extraToRight = extraPages + 1;
			}
			fromPage = currentPage - extraToLeft;
			toPage = currentPage + extraToRight;
			if (fromPage < 1)
			{
				toPage += 1 - fromPage;
				fromPage = 1;
			}
			else if (toPage > totalPages)
			{
				fromPage -= toPage - totalPages;
				toPage = totalPages;
			}
			if (fromPage < 1) fromPage = 1;
			if (toPage > totalPages) toPage = totalPages;
		}
		else
		{
			fromPage = 1;
			toPage = totalPages;
		}
		
		for (int page = fromPage; page <= toPage; page++)
		{
			
			if (page != currentPage)
			{

				String onClick = JsfUtils.generateSubmitJS(context, form,
						currentPageField, String.valueOf(page)); 
				
				writer.startElement("td", this);
				writer.writeAttribute("class", "pager-page", null);
				writer.writeAttribute("onclick", onClick, null);
				writer.write(String.valueOf(page));
				writer.endElement("td");

			}
			else
			{

				writer.startElement("td", this);
				writer.writeAttribute("class", "pager-page-current", null);
				writer.write(String.valueOf(page));
				writer.endElement("td");

			}

		}
		
		if (currentPage < totalPages)
		{
			
			String onClick = JsfUtils.generateSubmitJS(context, form,
					currentPageField, String.valueOf(currentPage + 1)); 
			
			writer.startElement("td", this);
			writer.writeAttribute("class", "pager-next-enabled", null);
			writer.writeAttribute("onclick", onClick, null);
			writer.write("Next");
			writer.endElement("td");

		}
		else
		{
			writer.startElement("td", this);
			writer.writeAttribute("class", "pager-next-disabled", null);
			writer.write("Next");
			writer.endElement("td");
		}
		
		writer.endElement("tr");
		writer.endElement("table");
		
	}

	public int getCurrentPage()
	{
		return JsfUtils.getCompPropInt(this, getFacesContext(),
				"currentPage", currentPageSet, currentPage);
	}

	public void setCurrentPage(int currentPage)
	{
		currentPageSet = true;
		this.currentPage = currentPage;
	}

	public int getFirstRecord()
	{
		return JsfUtils.getCompPropInt(this, getFacesContext(),
				"firstRecord", firstRecordSet, firstRecord);
	}

	public void setFirstRecord(int firstRecord)
	{
		firstRecordSet = true;
		this.firstRecord = firstRecord;
	}

	public int getLastRecord()
	{
		return JsfUtils.getCompPropInt(this, getFacesContext(),
				"lastRecord", lastRecordSet, lastRecord);
	}

	public void setLastRecord(int lastRecord)
	{
		lastRecordSet = true;
		this.lastRecord = lastRecord;
	}

	public int getMaxShownPages()
	{
		return JsfUtils.getCompPropInt(this, getFacesContext(),
				"maxShownPages", maxShownPagesSet, maxShownPages);
	}

	public void setMaxShownPages(int maxShownPages)
	{
		maxShownPagesSet = true;
		this.maxShownPages = maxShownPages;
	}

	public int getPageSize()
	{
		return JsfUtils.getCompPropInt(this, getFacesContext(),
				"pageSize", pageSizeSet, pageSize);
	}

	public void setPageSize(int pageSize)
	{
		pageSizeSet = true;
		this.pageSize = pageSize;
	}
	
	

}

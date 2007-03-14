package edu.emory.library.tast.maps.component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ZoomHistory implements Serializable
{
	
	private static final long serialVersionUID = 5731386622192110293L;
	
	private List items = (List) new ArrayList();
	private int position = -1;
	
	public void addItem(ZoomHistoryItem item)
	{
		items.add(item);
	}
	
	void addItem(int scale, double cx, double cy)
	{
		items.add(new ZoomHistoryItem(scale, cx, cy));
	}

	public List getItems()
	{
		return items;
	}

	public void setItems(List items)
	{
		if (items == null) return;
		this.items = items;
	}

	public int getPosition()
	{
		return position;
	}

	public void setPosition(int position)
	{
		this.position = position;
	}
	
	public boolean canGoBack()
	{
		return position > 0;
	}
	
	public boolean canGoForward()
	{
		return position+1 < items.size();
	}

	public String toString()
	{
		StringBuffer str = new StringBuffer();
		str.append(position);
		for (Iterator iter = items.iterator(); iter.hasNext();)
		{
			ZoomHistoryItem item = (ZoomHistoryItem) iter.next();
			if (str.length() > 0) str.append(" ");
			str.append(item.getScale()).append(" ");
			str.append(item.getCenterX()).append(" ");
			str.append(item.getCenterY());
		}
		return str.toString();
	}

	public static ZoomHistory parse(String str)
	{
		if (str == null)
			throw new RuntimeException("null zoom history");
		
		String[] values = str.trim().split("\\s+");
		
		if (values == null || values.length % 3 != 1)
			throw new RuntimeException("invalid number of arguments zoom history");
		
		ZoomHistory history = new ZoomHistory();
		try
		{
			
			history.setPosition(Integer.parseInt(values[0]));
			
			int n = (values.length - 1) / 3;
			for (int i = 0; i < n; i++)
			{
				ZoomHistoryItem item = new ZoomHistoryItem();
				item.setScale(Integer.parseInt(values[3*i+1]));
				item.setCenterX(Double.parseDouble(values[3*i+2]));
				item.setCenterY(Double.parseDouble(values[3*i+3]));
				history.addItem(item);
			}
		}
		catch (NumberFormatException nfe)
		{
			throw new RuntimeException("invalid zoom history " + str);
		}
		
		return history;
		
	}

}
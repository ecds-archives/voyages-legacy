package edu.emory.library.tast.common.grideditor;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import edu.emory.library.tast.common.grideditor.list.ListItem;
import edu.emory.library.tast.util.JsfUtils;

public class SharedListsExtension extends Extension
{
	
	private Map sharedLists = null;

	private void encodeSharedListRec(StringBuffer regJS, ListItem[] items)
	{
		regJS.append("[");
		for (int i = 0; i < items.length; i++)
		{
			ListItem item = items[i];
			if (i > 0) regJS.append(", ");
			regJS.append("new ListItem(");
			if (item.getValue() == null)
			{
				regJS.append("null, ");
			}
			else
			{
				regJS.append("'").append(item.getValue()).append("', ");
			}
			regJS.append("'").append(JsfUtils.escapeStringForJS(item.getText())).append("'");
			regJS.append(", ");
			encodeSharedListRec(regJS, item.getSubItems());
			regJS.append(")");
		}
		regJS.append("]");
		regJS.append(")");
	}

	private void encodeSharedList(StringBuffer regJS, String listName, ListItem[] items)
	{
		regJS.append("new RecordEditorList(");
		regJS.append("'").append(listName).append("', ");
		encodeSharedListRec(regJS, items);
		regJS.append(")");
	}

	public void encodeRegJS(StringBuffer regJS)
	{
		
		regJS.append("[");
		
		int j = 0;
		for (Iterator iter = sharedLists.entrySet().iterator(); iter.hasNext();)
		{

			Entry listEntry = (Entry) iter.next();;
			ListItem[] items = (ListItem[]) listEntry.getValue();
			String listName = (String) listEntry.getKey();

			if (j > 0) regJS.append(", ");
			encodeSharedList(regJS, listName, items);
			
		}
		
		regJS.append("]");

	}

	public Map getSharedLists()
	{
		return sharedLists;
	}

	public void setSharedLists(Map sharedLists)
	{
		this.sharedLists = sharedLists;
	}

}

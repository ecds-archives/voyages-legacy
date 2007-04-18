package edu.emory.library.tast.common.grideditor;

import edu.emory.library.tast.common.grideditor.list.ListItem;
import edu.emory.library.tast.util.JsfUtils;

public class SharedListExtension extends Extension
{
	
	private ListItem[] list = null;

	private void encodeSharedListRec(StringBuffer regJS, ListItem[] items)
	{
		regJS.append("[");
		for (int i = 0; i < items.length; i++)
		{
			ListItem item = items[i];
			if (i > 0) regJS.append(", ");
			regJS.append("new GridEditorListItem(");
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

	public void encodeRegJS(StringBuffer regJS)
	{
		encodeSharedListRec(regJS, list);
	}

	public ListItem[] getList()
	{
		return list;
	}

	public void setList(ListItem[] sharedLists)
	{
		this.list = sharedLists;
	}

}

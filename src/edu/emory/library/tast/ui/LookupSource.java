package edu.emory.library.tast.ui;

import java.util.List;

public interface LookupSource
{
	public boolean canReturnAllItems();
	public int getMaxLimit();
	public List allItems();
	public List getItemsByValues(String[] ids);
	public List search(String value);
}

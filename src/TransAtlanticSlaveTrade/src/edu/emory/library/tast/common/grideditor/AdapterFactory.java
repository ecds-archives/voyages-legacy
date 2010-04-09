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
package edu.emory.library.tast.common.grideditor;

import java.util.HashMap;
import java.util.Map;

import edu.emory.library.tast.common.grideditor.date.DateAdapter;
import edu.emory.library.tast.common.grideditor.list.ListAdapter;
import edu.emory.library.tast.common.grideditor.textbox.TextareaAdapter;
import edu.emory.library.tast.common.grideditor.textbox.TextboxAdapter;
import edu.emory.library.tast.common.grideditor.textbox.TextboxDoubleAdapter;
import edu.emory.library.tast.common.grideditor.textbox.TextboxFloatAdapter;
import edu.emory.library.tast.common.grideditor.textbox.TextboxIntegerAdapter;
import edu.emory.library.tast.common.grideditor.textbox.TextboxLongAdapter;

public class AdapterFactory
{
	
	private static Map adapters; 
	
	static
	{
		adapters = new HashMap();
		adapters.put(TextboxAdapter.TYPE, new TextboxAdapter());
		adapters.put(DateAdapter.TYPE, new DateAdapter());
		adapters.put(TextboxIntegerAdapter.TYPE, new TextboxIntegerAdapter());
		adapters.put(TextboxLongAdapter.TYPE, new TextboxLongAdapter());
		adapters.put(TextboxFloatAdapter.TYPE, new TextboxFloatAdapter());
		adapters.put(TextboxDoubleAdapter.TYPE, new TextboxDoubleAdapter());
		adapters.put(TextareaAdapter.TYPE, new TextareaAdapter());
		adapters.put(ListAdapter.TYPE, new ListAdapter());
	}
	
	public static Adapter getAdapter(String type)
	{
		Adapter adapater = (Adapter) adapters.get(type);
		if (adapater == null) throw new RuntimeException("invalid adapter type " + type);
		return adapater;
	}

}
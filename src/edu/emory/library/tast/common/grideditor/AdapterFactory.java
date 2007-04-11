package edu.emory.library.tast.common.grideditor;

import java.util.HashMap;
import java.util.Map;

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
		adapters.put(TextboxIntegerAdapter.TYPE, new TextboxIntegerAdapter());
		adapters.put(TextboxLongAdapter.TYPE, new TextboxLongAdapter());
		adapters.put(TextboxFloatAdapter.TYPE, new TextboxFloatAdapter());
		adapters.put(TextboxDoubleAdapter.TYPE, new TextboxDoubleAdapter());
	}
	
	public static Adapter getAdapter(String type)
	{
		Adapter adapater = (Adapter) adapters.get(type);
		if (adapater == null) new RuntimeException("invalid adapter type " + type);
		return adapater;
	}

}
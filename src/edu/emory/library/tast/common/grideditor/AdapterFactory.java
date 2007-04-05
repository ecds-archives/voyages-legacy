package edu.emory.library.tast.common.grideditor;

import java.util.HashMap;
import java.util.Map;

import edu.emory.library.tast.common.grideditor.textbox.TextboxAdapter;

public class AdapterFactory
{
	
	private static Map adapters; 
	
	static
	{
		adapters = new HashMap();
		adapters.put(TextboxAdapter.TYPE, new TextboxAdapter());
		adapters.put(TextboxAdapter.TYPE, new TextboxAdapter());
		adapters.put(TextboxAdapter.TYPE, new TextboxAdapter());
		adapters.put(TextboxAdapter.TYPE, new TextboxAdapter());
		adapters.put(TextboxAdapter.TYPE, new TextboxAdapter());
		adapters.put(TextboxAdapter.TYPE, new TextboxAdapter());
		adapters.put(TextboxAdapter.TYPE, new TextboxAdapter());
	}
	
	public static Adapter getAdapter(String type)
	{
		Adapter adapater = (Adapter) adapters.get(type);
		if (adapater == null) new RuntimeException("invalid adapater type " + type);
		return adapater;
	}

}
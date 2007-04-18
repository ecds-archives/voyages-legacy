package edu.emory.library.tast.common.grideditor;

import java.util.HashMap;
import java.util.Map;

import edu.emory.library.tast.common.grideditor.date.DateAdapter;
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
	}
	
	public static Adapter getAdapter(String type)
	{
		Adapter adapater = (Adapter) adapters.get(type);
		if (adapater == null) new RuntimeException("invalid adapter type " + type);
		return adapater;
	}

}
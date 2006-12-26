package edu.emory.library.tast.spss;

import edu.emory.library.tast.dm.attributes.Attribute;

public class LogRecordWriter
{
	
	private LogWriter lorWriter;
	private STSchemaVariable variable;
	private Attribute attribute;
	
	public LogRecordWriter(LogWriter lorWriter)
	{
		this.lorWriter = lorWriter;
	}
	
	public void setContext(STSchemaVariable variable, Attribute attribute)
	{
		this.variable = variable;
		this.attribute = attribute;
	}
	
	
	

}

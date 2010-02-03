package edu.emory.library.tast.common.grideditor.textbox;

import edu.emory.library.tast.common.grideditor.FieldType;

public abstract class AbstractTextFieldType extends FieldType
{
	
	private String cssClass = null;
	private String cssStyle = null;
	protected String inputSize = null;

	public AbstractTextFieldType(String name)
	{
		super(name);
	}

	public AbstractTextFieldType(String name, String cssClass)
	{
		super(name);
		this.cssClass = cssClass;
	}

	public AbstractTextFieldType(String name, String cssClass, String cssStyle)
	{
		super(name);
		this.cssClass = cssClass;
		this.cssStyle = cssStyle;
	}

	public String getInputSize() {
		return inputSize;
	}

	public void setInputSize(String inputSize) {
		this.inputSize = inputSize;
	}

	public String getCssClass()
	{
		return cssClass;
	}

	public void setCssClass(String cssClass)
	{
		this.cssClass = cssClass;
	}

	public String getCssStyle()
	{
		return cssStyle;
	}

	public void setCssStyle(String cssStyle)
	{
		this.cssStyle = cssStyle;
	}

}

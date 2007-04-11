package edu.emory.library.tast.common.grideditor;

import edu.emory.library.tast.util.StringUtils;

abstract public class Value
{
	
	private String errorMessage;

	public boolean isError()
	{
		return !StringUtils.isNullOrEmpty(errorMessage);
	}

	public String getErrorMessage()
	{
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage)
	{
		this.errorMessage = errorMessage;
	}
	
}

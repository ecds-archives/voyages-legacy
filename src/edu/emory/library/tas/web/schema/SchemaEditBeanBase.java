package edu.emory.library.tas.web.schema;

import java.util.Map;

import javax.faces.context.FacesContext;

public class SchemaEditBeanBase
{

	private static final int MAX_NAME_LENGTH = 40;
	private static final int MAX_USER_LABEL_LENGTH = 40;
	private static final int MAX_DESCRIPTION_LENGTH = 200;

	private Switcher switcher;
	private String errorText;
	
	private int scrollPosX = 0;
	private int scrollPosY = 0;

	public String getScrollToJavaScript()
	{
		if (scrollPosX != 0 || scrollPosY != 0)
		{
			StringBuffer js = new StringBuffer();
			js.append("window.onload = function() {");
			js.append("window.scrollTo(").append(scrollPosX).append(", ").append(scrollPosY).append(");");
			js.append("}");
			clearScrollPosition();
			return js.toString();
		}
		else
		{
			return "";
		}
	}
	
	protected void saveScrollPosition()
	{

		Map params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String scrollPosXStr = (String) params.get("scrollPosX");
		String scrollPosYStr = (String) params.get("scrollPosY");
		
		try
		{
			scrollPosX = Integer.parseInt(scrollPosXStr);
			scrollPosY = Integer.parseInt(scrollPosYStr);
		}
		catch (NumberFormatException e)
		{
			clearScrollPosition();
		}

	}
	
	protected void clearScrollPosition()
	{
		scrollPosX = 0;
		scrollPosY = 0;
	}

	public boolean editingVoyages()
	{
		return switcher.getEditMode().isVoyages();
	}

	public boolean editingSlaves()
	{
		return switcher.getEditMode().isSlaves();
	}

	public EditMode getEditMode()
	{
		return switcher.getEditMode();
	}

	public Switcher getSwitcher()
	{
		return switcher;
	}

	public void setSwitcher(Switcher switcher)
	{
		this.switcher = switcher;
	}

	public String getErrorText()
	{
		return errorText;
	}

	public void setErrorText(String errorText)
	{
		this.errorText = errorText;
	}
	
	public boolean isError()
	{
		return errorText != null && errorText.length() > 0 ;
	}
	
	public int getMaxNameLength()
	{
		return MAX_NAME_LENGTH;
	}

	public int getMaxUserLabelLength()
	{
		return MAX_USER_LABEL_LENGTH;
	}

	public int getMaxDescriptionLength()
	{
		return MAX_DESCRIPTION_LENGTH;
	}

}

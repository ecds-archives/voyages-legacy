package edu.emory.library.tast.ui;

public class MenuItem
{
	
	private String text;
	private String id;
	private MenuItem[] submenu;
	
	public String getId()
	{
		return id;
	}
	
	public void setId(String id)
	{
		this.id = id;
	}
	
	public String getText()
	{
		return text;
	}
	
	public void setText(String text)
	{
		this.text = text;
	}

	public MenuItem[] getSubmenu()
	{
		return submenu;
	}

	public void setSubmenu(MenuItem[] submenu)
	{
		this.submenu = submenu;
	}

}

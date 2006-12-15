package edu.emory.library.tast.estimates.table;

public class Label
{
	
	private String text;
	private Label[] breakdown;
	
	public Label(String text)
	{
		this.text = text;
		this.breakdown = null;
	}

	public Label[] getBreakdown()
	{
		return breakdown;
	}
	
	public void setBreakdown(Label[] breakdown)
	{
		this.breakdown = breakdown;
	}
	
	public boolean hasBreakdown()
	{
		return breakdown != null && breakdown.length != 0;
	}
	
	public String getText()
	{
		return text;
	}
	
	public void setText(String text)
	{
		this.text = text;
	}

}

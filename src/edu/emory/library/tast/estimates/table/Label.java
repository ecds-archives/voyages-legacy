package edu.emory.library.tast.estimates.table;


public class Label
{
	
	private String text;
	private Label[] breakdown;
	private int leavesCount;
	
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
	
	public int getLeavesCount()
	{
		return leavesCount;
	}

	public int calculateLeaves()
	{
		if (this.breakdown == null || this.breakdown.length == 0)
		{
			leavesCount = 1;
		}
		else
		{
			leavesCount = 0;
			for (int i = 0; i < breakdown.length; i++)
			{
				leavesCount += breakdown[i].calculateLeaves();
			}
		}
		return leavesCount;
	}

}

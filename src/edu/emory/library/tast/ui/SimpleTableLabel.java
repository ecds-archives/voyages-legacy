package edu.emory.library.tast.ui;

public class SimpleTableLabel
{
	
	private String label;
	private int span = 1;
	
	public SimpleTableLabel(String label, int span)
	{
		this.label = label;
		this.span = span;
	}

	public SimpleTableLabel(String label)
	{
		this.label = label;
		this.span = 1;
	}

	public String getLabel()
	{
		return label;
	}
	
	public void setLabel(String label)
	{
		this.label = label;
	}
	
	public int getSpan()
	{
		return span;
	}
	
	public void setSpan(int span)
	{
		this.span = span;
	}
	
	public static SimpleTableLabel[] createFromStringArray(String[] textLabels)
	{
		return createFromStringArray(textLabels, 1);
	}

	public static SimpleTableLabel[] createFromStringArray(String[] textLabels, int span)
	{
		SimpleTableLabel[] labels =
			new SimpleTableLabel[textLabels.length];
		
		for (int i = 0; i < textLabels.length; i++)
			labels[i] = new SimpleTableLabel(textLabels[i], span);
		
		return labels;
	}

}

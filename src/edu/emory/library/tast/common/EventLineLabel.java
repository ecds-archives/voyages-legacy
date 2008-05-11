package edu.emory.library.tast.common;

import java.text.MessageFormat;

import edu.emory.library.tast.util.MathUtils;

public class EventLineLabel
{
	
	private boolean major;
	private double value;
	private String label;
	
	public EventLineLabel(double value, String label, boolean major)
	{
		this.major = major;
		this.value = value;
		this.label = label;
	}

	public String getLabel()
	{
		return label;
	}
	
	public void setLabel(String label)
	{
		this.label = label;
	}
	
	public boolean isMajor()
	{
		return major;
	}
	
	public void setMajor(boolean major)
	{
		this.major = major;
	}
	
	public double getValue()
	{
		return value;
	}
	
	public void setValue(double value)
	{
		this.value = value;
	}
	
	static public EventLineLabel[] createPercentualLabels(MessageFormat fmt)
	{
		
		EventLineLabel[] labels = new EventLineLabel[11];
		
		for (int i = 0; i <= 10; i++)
			labels[i] = new EventLineLabel(
					(double)(i*10),
					fmt.format(new Object[] {new Integer(i*10)}),
					i == 5);
		
		return labels;
		
	}

	static public EventLineLabel[] createStandardLabels(double maxValue, MessageFormat fmt)
	{
		
		double majorSpacing;
		double minorSpacing;
		int majorSpaceEvery;
		
		double nextPow10 = MathUtils.firstGreaterOrEqualPow10(maxValue);
		
		if (maxValue / (nextPow10/10) >= 5)
		{
			majorSpacing = nextPow10 / 2;
			minorSpacing = majorSpacing / 5;
			majorSpaceEvery = 5;
		}
		else
		{
			majorSpacing = nextPow10 / 10;
			minorSpacing = majorSpacing / 2;
			majorSpaceEvery = 2;
		}

		int labelsCount = (int) (maxValue / minorSpacing) + 2;
		if (labelsCount < 0)
			return new EventLineLabel[0];

		EventLineLabel[] labels = new EventLineLabel[labelsCount];
		
		for (int i = 0; i < labelsCount; i++)
			labels[i] = new EventLineLabel(
				i * minorSpacing,
				fmt.format(new Object[] {new Double(i * minorSpacing)}),
				i % majorSpaceEvery == 0);
		
		return labels;

	}

}
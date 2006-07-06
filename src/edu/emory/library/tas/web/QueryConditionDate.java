package edu.emory.library.tas.web;

import java.util.Arrays;

import edu.emory.library.tas.attrGroups.AbstractAttribute;
import edu.emory.library.tas.util.query.Conditions;

public class QueryConditionDate extends QueryConditionRange
{
	
	public static final String[] MONTH_NAMES = {
		"Jan", "Feb", "Mar", "Apr", "May", "Jun",
		"Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };
	
	private String fromMonth;
	private String fromYear;
	private String toMonth;
	private String toYear;
	private String geMonth;
	private String geYear;
	private String leMonth;
	private String leYear;
	private String eqMonth;
	private String eqYear;
	private boolean[] selectedMonths = null;
	
	public QueryConditionDate(AbstractAttribute attribute)
	{
		super(attribute);
	}

	public QueryConditionDate(AbstractAttribute attribute, int type)
	{
		super(attribute);
		this.type = type;
	}

	public boolean addToConditions(Conditions conditions)
	{
		return false;
	}

	private void ensureMonths()
	{
		if (selectedMonths == null)
		{
			selectedMonths = new boolean[12];
			Arrays.fill(selectedMonths, true);
		}
	}
	
	public boolean isMonthSelected(int month)
	{
		ensureMonths();
		return selectedMonths[month];
	}
	
	public void setMonthStatus(int month, boolean selected)
	{
		ensureMonths();
		selectedMonths[month] = selected;
	}

	public void selectMonth(int month)
	{
		setMonthStatus(month, true);
	}

	public void deselectMonth(int month)
	{
		setMonthStatus(month, false);
	}

	public boolean[] getSelectedMonths()
	{
		return selectedMonths;
	}

	public void setSelectedMonths(boolean[] selectedMonths)
	{
		this.selectedMonths = selectedMonths;
	}
	
	public String getEqMonth()
	{
		return eqMonth;
	}

	public void setEqMonth(String eqMonth)
	{
		this.eqMonth = eqMonth;
	}

	public String getEqYear()
	{
		return eqYear;
	}

	public void setEqYear(String eqYear)
	{
		this.eqYear = eqYear;
	}

	public String getFromMonth()
	{
		return fromMonth;
	}

	public void setFromMonth(String fromMonth)
	{
		this.fromMonth = fromMonth;
	}

	public String getFromYear()
	{
		return fromYear;
	}

	public void setFromYear(String fromYear)
	{
		this.fromYear = fromYear;
	}

	public String getGeMonth()
	{
		return geMonth;
	}

	public void setGeMonth(String geMonth)
	{
		this.geMonth = geMonth;
	}

	public String getGeYear()
	{
		return geYear;
	}

	public void setGeYear(String geYear)
	{
		this.geYear = geYear;
	}

	public String getLeMonth()
	{
		return leMonth;
	}

	public void setLeMonth(String leMonth)
	{
		this.leMonth = leMonth;
	}

	public String getLeYear()
	{
		return leYear;
	}

	public void setLeYear(String leYear)
	{
		this.leYear = leYear;
	}

	public String getToMonth()
	{
		return toMonth;
	}

	public void setToMonth(String toMonth)
	{
		this.toMonth = toMonth;
	}

	public String getToYear()
	{
		return toYear;
	}

	public void setToYear(String toYear)
	{
		this.toYear = toYear;
	}

	private boolean compareTextFields(String val1, String val2)
	{
		return
			(val1 == null && val2 == null) ||
			(val1 != null && val1.equals(val2));
	}
	
	public boolean equals(Object obj)
	{
		if (!super.equals(obj) || !(obj instanceof QueryConditionDate))
			return false;

		QueryConditionDate queryConditionRange = (QueryConditionDate) obj;
		return
			type == queryConditionRange.getType() &&
			Arrays.equals(selectedMonths, queryConditionRange.getSelectedMonths()) &&
			compareTextFields(fromMonth, queryConditionRange.getFromMonth()) &&
			compareTextFields(fromYear, queryConditionRange.getFromYear()) &&
			compareTextFields(toMonth, queryConditionRange.getToMonth()) &&
			compareTextFields(toYear, queryConditionRange.getToYear()) &&
			compareTextFields(leMonth, queryConditionRange.getLeMonth()) &&
			compareTextFields(leYear, queryConditionRange.getLeYear()) &&
			compareTextFields(geMonth, queryConditionRange.getGeMonth()) &&
			compareTextFields(geYear, queryConditionRange.getGeYear()) &&
			compareTextFields(eqMonth, queryConditionRange.getEqMonth()) &&
			compareTextFields(eqYear, queryConditionRange.getEqYear());
	}

	protected Object clone()
	{
		QueryConditionDate newQueryCondition = new QueryConditionDate(getAttribute(), getType());
		
		newQueryCondition.setType(type);
		newQueryCondition.setFromMonth(fromMonth);
		newQueryCondition.setFromYear(fromYear);
		newQueryCondition.setToMonth(toMonth);
		newQueryCondition.setToYear(toYear);
		newQueryCondition.setLeMonth(leMonth);
		newQueryCondition.setLeYear(leYear);
		newQueryCondition.setGeMonth(geMonth);
		newQueryCondition.setGeYear(geYear);
		newQueryCondition.setEqMonth(eqMonth);
		newQueryCondition.setEqYear(eqYear);

		if (selectedMonths != null)
			for (int i = 0; i < 12; i++)
				newQueryCondition.setMonthStatus(i, selectedMonths[i]);
		
		return newQueryCondition;
	}

}

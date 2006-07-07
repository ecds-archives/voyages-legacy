package edu.emory.library.tas.web;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import edu.emory.library.tas.attrGroups.AbstractAttribute;
import edu.emory.library.tas.attrGroups.Attribute;
import edu.emory.library.tas.attrGroups.CompoundAttribute;
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
	private boolean noOfSelectedMonthsDetermined = false; 
	private int noOfSelectedMonths; 
	private String selectedMonthsAsString = null;
	private Integer[] selectedMonthsAsArray = null;
	
	public QueryConditionDate(AbstractAttribute attribute)
	{
		super(attribute);
	}

	public QueryConditionDate(AbstractAttribute attribute, int type)
	{
		super(attribute);
		this.type = type;
	}

	private Date parseDate(String monthStr, String yearStr, int roundDirection)
	{

		int month = 0;
		int year = 0;
	
		if (yearStr == null)
			return null;
		
		if (monthStr == null)
			monthStr = "";

		monthStr = monthStr.trim();
		yearStr = yearStr.trim();
		
		if (yearStr.length() == 0)
			return null;
		
		try { year = Integer.parseInt(yearStr); }
		catch (NumberFormatException nfe) { return null; }
		
		if (monthStr.length() > 0)
		{
			try { month = Integer.parseInt(monthStr); }
			catch (NumberFormatException nfe) { return null; }
		}
		else
		{
			if (roundDirection > 0)
				month = 12;
			else
				month = 1;
		}
		
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(year, month - 1, 1);
		
		if (roundDirection > 0)
		{
			cal.add(Calendar.MONTH, 1);
			cal.add(Calendar.DATE, -1);
		}

		return cal.getTime();
		
	}

	private void addSingleAttributeToConditions(Attribute attribute, Conditions conditions, Date fromDateQuery, Date toDateQuery)
	{
		switch (type)
		{
			case QueryConditionNumeric.TYPE_BETWEEN:
			case QueryConditionNumeric.TYPE_EQ:
				conditions.addCondition(attribute.getName(), fromDateQuery, Conditions.OP_GREATER_OR_EQUAL);
				conditions.addCondition(attribute.getName(), toDateQuery, Conditions.OP_SMALLER_OR_EQUAL);
				break;

			case QueryConditionNumeric.TYPE_LE:
				conditions.addCondition(attribute.getName(), toDateQuery, Conditions.OP_SMALLER_OR_EQUAL);
				break;
				
			case QueryConditionNumeric.TYPE_GE:
				conditions.addCondition(attribute.getName(), fromDateQuery, Conditions.OP_GREATER_OR_EQUAL);
				break;

		}
		
		if (!areAllMonthsSelected())
			conditions.addCondition(
					"date_part('month', " + attribute.getName() + ")",
					getSelectedMonthsAsArray(),
					Conditions.OP_IN);
		
	}
	
	public boolean addToConditions(Conditions conditions)
	{

		Date fromDateQuery = null;
		Date toDateQuery = null;

		switch (type)
		{

			case QueryConditionNumeric.TYPE_BETWEEN:
				fromDateQuery = parseDate(fromMonth, fromYear, -1);
				toDateQuery = parseDate(toMonth, toYear, 1);
				if (fromDateQuery == null || toDateQuery == null) 
				{
					setErrorFlag(true);
					return false;
				}
				break;

			case QueryConditionNumeric.TYPE_LE:
				toDateQuery = parseDate(leMonth, leYear, 1);
				if (toDateQuery == null)
				{
					setErrorFlag(true);
					return false;
				}
				break;
				
			case QueryConditionNumeric.TYPE_GE:
				fromDateQuery = parseDate(geMonth, geYear, -1);
				if (fromDateQuery == null)
				{
					setErrorFlag(true);
					return false;
				}
				break;

			case QueryConditionNumeric.TYPE_EQ:
				fromDateQuery = parseDate(eqMonth, eqYear, -1);
				toDateQuery = parseDate(eqMonth, eqYear, 1);
				if (fromDateQuery == null || toDateQuery == null) 
				{
					setErrorFlag(true);
					return false;
				}
				break;

		}
		
		if (isOnAttribute())
		{
			Attribute attr = (Attribute) getAttribute();
			addSingleAttributeToConditions(attr, conditions, fromDateQuery, toDateQuery);
		}
		else if (isOnCompoundAttribute())
		{
			CompoundAttribute compAttr = (CompoundAttribute) getAttribute();
			Conditions orCond = new Conditions(Conditions.JOIN_OR);
			conditions.addCondition(orCond);
			for (Iterator iterAttr = compAttr.getAttributes().iterator(); iterAttr.hasNext();)
			{
				Attribute attr = (Attribute) iterAttr.next();
				addSingleAttributeToConditions(attr, orCond, fromDateQuery, toDateQuery);
			}
		}
		
		return true;
		
	}
	
	private void resetPrecomputedMonthsInfo()
	{
		noOfSelectedMonthsDetermined = false;
		selectedMonthsAsString = null;
		selectedMonthsAsArray = null;
	}
	
	private void ensureMonths()
	{
		if (selectedMonths == null)
		{
			resetPrecomputedMonthsInfo();
			selectedMonths = new boolean[12];
			Arrays.fill(selectedMonths, true);
		}
	}
	
	public boolean isMonthSelected(int month)
	{
		ensureMonths();
		return selectedMonths[month];
	}
	
	public boolean areAllMonthsSelected()
	{
		return getNoOfSelectedMonths() == 12;
	}
	
	public boolean noMonthSelected()
	{
		return getNoOfSelectedMonths() == 0;
	}

	public int getNoOfSelectedMonths()
	{
		ensureMonths();
		if (!noOfSelectedMonthsDetermined)
		{
			noOfSelectedMonths = 0;
			for (int i = 0; i < 12; i++)
				if (selectedMonths[i])
					noOfSelectedMonths ++;
			noOfSelectedMonthsDetermined = true;
		}
		return noOfSelectedMonths;
	}

	public String getSelectedMonthsAsString()
	{
		if (selectedMonthsAsString == null)
		{
			StringBuffer selMonthsList = new StringBuffer();
			for (int i = 0; i < 12; i++)
			{
				if (isMonthSelected(i))
				{
					if (selMonthsList.length() > 0 ) selMonthsList.append(",");
					selMonthsList.append(i);
				}
			}
			selectedMonthsAsString = selMonthsList.toString(); 
		}
		return selectedMonthsAsString;
	}
	
	public Integer[] getSelectedMonthsAsArray()
	{
		if (selectedMonthsAsArray == null)
		{
			selectedMonthsAsArray = new Integer[getNoOfSelectedMonths()];
			for (int i = 0, j = 0; i < 12; i++)
			{
				if (isMonthSelected(i))
				{
					selectedMonthsAsArray[j++] = new Integer(i+1);
				}
			}
		}
		return selectedMonthsAsArray;
	}

	public void setMonthStatus(int month, boolean selected)
	{
		ensureMonths();
		if (selectedMonths[month] != selected)
		{
			resetPrecomputedMonthsInfo();
			selectedMonths[month] = selected;
		}
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
		resetPrecomputedMonthsInfo();
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

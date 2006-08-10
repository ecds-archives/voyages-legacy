package edu.emory.library.tast.ui.search.query;

import java.util.Arrays;

import edu.emory.library.tast.util.StringUtils;
import edu.emory.library.tast.util.query.Conditions;

public class QueryConditionDate extends QueryConditionRange
{
	
	private static final long serialVersionUID = -5784093870244195009L;

	public static final String[] MONTH_NAMES = {
		"Jan", "Feb", "Mar", "Apr", "May", "Jun",
		"Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };
	
	public static final String EMPTY_MONTH = "MM";
	public static final String EMPTY_YEAR = "YYYY";
	
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
	
	private transient boolean noOfSelectedMonthsDetermined = false; 
	private transient int noOfSelectedMonths; 
	private transient String selectedMonthsAsString = null;
	private transient Integer[] selectedMonthsAsArray = null;
	
	public QueryConditionDate(String searchableAttributeId)
	{
		super(searchableAttributeId);
		fromMonth = toMonth = geMonth = leMonth = eqMonth = EMPTY_MONTH;
		fromYear = toYear = geYear = leYear = eqYear = EMPTY_YEAR;
	}

	public QueryConditionDate(String searchableAttributeId, int type)
	{
		super(searchableAttributeId);
		this.type = type;
	}

	public boolean addToConditions(Conditions conditions, boolean markErrors)
	{

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
	
	private String formatDateForDisplay(String month, String year)
	{
		boolean hasMonth = StringUtils.isNullOrEmpty(month);
		boolean hasYear = StringUtils.isNullOrEmpty(year); 
		if (hasMonth && hasYear)
		{
			return month + "/" + year;
		}
		else if (hasYear)
		{
			return year;
		}
		else
		{
			return "";
		}
	}

	public String getFromForDisplay()
	{
		return formatDateForDisplay(fromMonth, fromYear);
	}

	public String getToForDisplay()
	{
		return formatDateForDisplay(toMonth, toYear);
	}

	public String getLeForDisplay()
	{
		return formatDateForDisplay(leMonth, leYear);
	}

	public String getGeForDisplay()
	{
		return formatDateForDisplay(geMonth, geYear);
	}

	public String getEqForDisplay()
	{
		return formatDateForDisplay(eqMonth, eqYear);
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
		QueryConditionDate newQueryCondition = new QueryConditionDate(getSearchableAttributeId(), getType());
		
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

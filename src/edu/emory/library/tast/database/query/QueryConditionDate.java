package edu.emory.library.tast.database.query;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import edu.emory.library.tast.TastResource;
import edu.emory.library.tast.database.query.searchables.SearchableAttributeSimpleDate;
import edu.emory.library.tast.util.StringUtils;

public class QueryConditionDate extends QueryConditionRange
{
	
	private static final long serialVersionUID = -5784093870244195009L;

	public static final String TYPE = "date";
	
	public static final String[] MONTH_NAMES = {
		TastResource.getText("components_search_jan"),
		TastResource.getText("components_search_feb"),
		TastResource.getText("components_search_mar"),
		TastResource.getText("components_search_apr"),
		TastResource.getText("components_search_may"),
		TastResource.getText("components_search_jun"),
		TastResource.getText("components_search_jul"),
		TastResource.getText("components_search_aug"),
		TastResource.getText("components_search_sep"),
		TastResource.getText("components_search_oct"),
		TastResource.getText("components_search_nov"),
		TastResource.getText("components_search_dec")};
	
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
	
	private boolean[] selectedMonths = new boolean[] {
			true, true, true, true, true, true, true, true, true, true, true, true};
	
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
		if (0 <= month && month < 12 && selectedMonths[month] != selected)
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
	
	public void selectedAllMonths()
	{
		selectedMonths = null;
		resetPrecomputedMonthsInfo();
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

	public void setEq(Date date)
	{
		
		Calendar cal = Calendar.getInstance();
		
		cal.clear();
		cal.setTime(date);
		eqMonth = String.valueOf(cal.get(Calendar.MONTH) + 1);
		eqYear = String.valueOf(cal.get(Calendar.YEAR));
		
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
	
	public void setFromTo(Date dateFrom, Date dateTo)
	{
		
		Calendar cal = Calendar.getInstance();

		cal.clear();
		cal.setTime(dateFrom);
		fromMonth = String.valueOf(cal.get(Calendar.MONTH) + 1);
		fromYear = String.valueOf(cal.get(Calendar.YEAR));

		cal.clear();
		cal.setTime(dateTo);
		toMonth = String.valueOf(cal.get(Calendar.MONTH) + 1);
		toYear = String.valueOf(cal.get(Calendar.YEAR));

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
	
	public void setGe(Date date)
	{
		
		Calendar cal = Calendar.getInstance();
		
		cal.clear();
		cal.setTime(date);
		geMonth = String.valueOf(cal.get(Calendar.MONTH) + 1);
		geYear = String.valueOf(cal.get(Calendar.YEAR));
		
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
	
	public void setLe(Date date)
	{
		
		Calendar cal = Calendar.getInstance();
		
		cal.clear();
		cal.setTime(date);
		leMonth = String.valueOf(cal.get(Calendar.MONTH));
		leYear = String.valueOf(cal.get(Calendar.YEAR));
		
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
		boolean hasMonth = !StringUtils.isNullOrEmpty(month);
		boolean hasYear = !StringUtils.isNullOrEmpty(year); 
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


	public UrlParam[] createUrlParamValue()
	{
		
		List params = new LinkedList();
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy.MM");
		String attrId = getSearchableAttributeId();
		
		switch (getType())
		{
			case QueryConditionRange.TYPE_BETWEEN:
				Date fromRange = SearchableAttributeSimpleDate.parseDate(fromMonth, fromYear, 0);
				Date toRange = SearchableAttributeSimpleDate.parseDate(toMonth, toYear, 0);
				if (fromRange == null || toRange == null) return null;
				params.add(new UrlParam(attrId + "From", fmt.format(fromRange)));
				params.add(new UrlParam(attrId + "To", fmt.format(toRange)));
				break;
			
			case QueryConditionRange.TYPE_EQ:
				Date eq = SearchableAttributeSimpleDate.parseDate(eqMonth, eqYear, 0);
				if (eq == null) return null;
				params.add(new UrlParam(attrId, fmt.format(eq)));
				break;
			
			case QueryConditionRange.TYPE_LE:
				Date le = SearchableAttributeSimpleDate.parseDate(leMonth, leYear, 0);
				if (le == null) return null;
				params.add(new UrlParam(attrId + "To", fmt.format(le)));
				break;
			
			case QueryConditionRange.TYPE_GE:
				Date ge = SearchableAttributeSimpleDate.parseDate(geMonth, geYear, 0);
				if (ge == null) return null;
				params.add(new UrlParam(attrId + "From", fmt.format(ge)));
				break;
			
			default:
				throw new RuntimeException("unexpected type");
		}
		
		if (!areAllMonthsSelected() && !noMonthSelected())
			for (int i = 0; i < 12; i++)
				if (selectedMonths[i])
					params.add(new UrlParam(attrId + "Month", MONTH_NAMES[i]));
		
		UrlParam paramsArr[] = new UrlParam[params.size()];
		params.toArray(paramsArr);
		
		return paramsArr;
		
	}
	
	public boolean equals(Object obj)
	{
		if (!super.equals(obj) || !(obj instanceof QueryConditionDate))
			return false;

		QueryConditionDate queryConditionRange = (QueryConditionDate) obj;
		return
			type == queryConditionRange.getType() &&
			Arrays.equals(selectedMonths, queryConditionRange.getSelectedMonths()) &&
			StringUtils.compareStrings(fromMonth, queryConditionRange.getFromMonth()) &&
			StringUtils.compareStrings(fromYear, queryConditionRange.getFromYear()) &&
			StringUtils.compareStrings(toMonth, queryConditionRange.getToMonth()) &&
			StringUtils.compareStrings(toYear, queryConditionRange.getToYear()) &&
			StringUtils.compareStrings(leMonth, queryConditionRange.getLeMonth()) &&
			StringUtils.compareStrings(leYear, queryConditionRange.getLeYear()) &&
			StringUtils.compareStrings(geMonth, queryConditionRange.getGeMonth()) &&
			StringUtils.compareStrings(geYear, queryConditionRange.getGeYear()) &&
			StringUtils.compareStrings(eqMonth, queryConditionRange.getEqMonth()) &&
			StringUtils.compareStrings(eqYear, queryConditionRange.getEqYear());
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

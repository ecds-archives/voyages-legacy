package edu.emory.library.tast.database.query;

import java.util.Arrays;

import org.w3c.dom.Node;

import edu.emory.library.tast.TastResource;
import edu.emory.library.tast.util.StringUtils;
import edu.emory.library.tast.util.XMLUtils;

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
	private boolean[] selectedMonths = new boolean[] {false,false,false,false,false,false,false,false,false, false,false, false};
	
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

	public String toXML() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<condition ");
		XMLUtils.appendAttribute(buffer, "type", TYPE);
		XMLUtils.appendAttribute(buffer, "attribute", this.getSearchableAttributeId());
		XMLUtils.appendAttribute(buffer, "fromMonth", fromMonth);
		XMLUtils.appendAttribute(buffer, "fromYear", fromYear);
		XMLUtils.appendAttribute(buffer, "toMonth", toMonth);
		XMLUtils.appendAttribute(buffer, "toYear", toYear);
		XMLUtils.appendAttribute(buffer, "leMonth", leMonth);
		XMLUtils.appendAttribute(buffer, "leYear", leYear);
		XMLUtils.appendAttribute(buffer, "geMonth", geMonth);
		XMLUtils.appendAttribute(buffer, "geYear", geYear);
		XMLUtils.appendAttribute(buffer, "eqMonth", eqMonth);
		XMLUtils.appendAttribute(buffer, "eqYear", eqYear);
		XMLUtils.appendAttribute(buffer, "querytype", new Integer(type));
		XMLUtils.appendAttribute(buffer, "selected", prepareList(selectedMonths));
		buffer.append("/>\n");
		return buffer.toString();
	}

	private static String prepareList(boolean []list) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < list.length; i++) {
			if (i != 0) {
				buffer.append(",");
			}
			buffer.append(list[i]);
		}
		return buffer.toString();
	}
	
	private static boolean[] parseListBack(String list) {
		String[] elements = list.split(",");
		boolean []ret = new boolean[elements.length];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = Boolean.parseBoolean(elements[i]);
		}
		return ret;
	}
	
	public static QueryCondition fromXML(Node node) {
		QueryConditionDate qc = new QueryConditionDate(XMLUtils.getXMLProperty(node, "attribute"));
		qc.fromMonth = XMLUtils.getXMLProperty(node, "fromMonth");
		qc.fromYear = XMLUtils.getXMLProperty(node, "fromYear");
		qc.toMonth = XMLUtils.getXMLProperty(node, "toMonth");
		qc.toYear = XMLUtils.getXMLProperty(node, "toYear");
		qc.leMonth = XMLUtils.getXMLProperty(node, "leMonth");
		qc.leYear = XMLUtils.getXMLProperty(node, "leYear");
		qc.geMonth = XMLUtils.getXMLProperty(node, "geMonth");
		qc.geYear = XMLUtils.getXMLProperty(node, "geYear");
		qc.eqMonth = XMLUtils.getXMLProperty(node, "eqMonth");
		qc.eqYear = XMLUtils.getXMLProperty(node, "eqYear");
		qc.type = Integer.parseInt(XMLUtils.getXMLProperty(node, "querytype"));
		qc.setSelectedMonths(parseListBack(XMLUtils.getXMLProperty(node, "selected")));
		return qc;
	}
}

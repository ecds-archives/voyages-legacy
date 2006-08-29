package edu.emory.library.tast.dm.attributes;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Map;

import org.w3c.dom.Node;

import edu.emory.library.tast.dm.attributes.exceptions.InvalidDateException;
import edu.emory.library.tast.dm.attributes.exceptions.InvalidNumberException;
import edu.emory.library.tast.dm.attributes.exceptions.InvalidNumberOfValuesException;
import edu.emory.library.tast.dm.attributes.exceptions.StringTooLongException;

public class DateAttribute extends Attribute {
	
	public static final String ATTR_TYPE_NAME = "Date";
	
	private String importDateDay;

	private String importDateMonth;

	private String importDateYear;
	
	public DateAttribute(String name, String objectType) {
		super(name, objectType);
	}
	
	public DateAttribute(Node xmlNode, String objectType) {
		super(xmlNode, objectType);
		
		this.importDateDay = this.parseAttribute(xmlNode, "importDateDay");
		this.importDateMonth = this.parseAttribute(xmlNode, "importDateMonth");
		this.importDateYear = this.parseAttribute(xmlNode, "importDateYear");

	}
	
	public String getImportDateDay() {
		return importDateDay;
	}

	public String getImportDateMonth() {
		return importDateMonth;
	}

	public String getImportDateYear() {
		return importDateYear;
	}
	
	public void setImportDateDay(String importDateDay) {
		this.importDateDay = importDateDay;
	}

	public void setImportDateMonth(String importDateMonth) {
		this.importDateMonth = importDateMonth;
	}

	public void setImportDateYear(String importDateYear) {
		this.importDateYear = importDateYear;
	}

	public Object parse(String[] values, int options) throws InvalidNumberOfValuesException, InvalidNumberException, InvalidDateException, StringTooLongException {
		
		String value;
		boolean separate = values.length == 3 && values[0] != null && values[1] != null && values[2] != null;
		boolean single = values.length == 1 && values[0] != null;

		if (!(separate || single))
			throw new InvalidNumberOfValuesException();

		if (separate) {

			String day = values[0].trim();
			String month = values[1].trim();
			String year = values[2].trim();

			if (day.length() == 0 || month.length() == 0
					|| year.length() == 0)
				return null;

			try {
				Calendar cal = Calendar.getInstance();
				cal.clear();
				cal.set(Integer.parseInt(year),
						Integer.parseInt(month) - 1, Integer.parseInt(day));
				// Timestamp tstamp = new Timestamp(Integer.parseInt(year),
				// Integer.parseInt(month),
				// Integer.parseInt(day),
				// 0,0,0,0);
				return cal.getTime();
			} catch (NumberFormatException nfe) {
				throw new InvalidDateException();
			}

		} else if (single) {

			value = values[0].trim();

			if (value.length() == 0)
				return null;

			try {
				DateFormat dateFormat = DateFormat.getDateInstance();
				return dateFormat.parse(value);
			} catch (ParseException e) {
				throw new InvalidDateException();
			}
		}
		return null;
	}

	public String getTypeDisplayName() {
		return "Date";
	}

	public boolean isOuterjoinable() {
		return false;
	}

	public String getHQLSelectPath(Map bindings) {
		if (!bindings.containsKey(this.getObjectType())) {
			return this.getName();
		}
		StringBuffer buffer = new StringBuffer();
		buffer.append(bindings.get(this.getObjectType()));
		buffer.append(".");
		buffer.append(this.getName());
		return buffer.toString();
	}

	public String getHQLWherePath(Map bindings) {
		return this.getHQLSelectPath(bindings);
	}

	public String getHQLParamName() {
		return this.getName();
	}

	public String getHQLOuterJoinPath(Map bindings) {
		return null;
	}

	public Object getValueToCondition(Object value) {
		return value;
	}
	
}

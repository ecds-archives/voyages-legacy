package edu.emory.library.tas.attrGroups;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import edu.emory.library.tas.Dictionary;
import edu.emory.library.tas.InvalidDateException;
import edu.emory.library.tas.InvalidNumberException;
import edu.emory.library.tas.InvalidNumberOfValuesException;
import edu.emory.library.tas.StringTooLongException;
import edu.emory.library.tas.util.HibernateUtil;

public abstract class AbstractAttribute implements Serializable, VisibleColumn {
	
	public final static int TYPE_INTEGER = 0;

	public final static int TYPE_LONG = 1;

	public final static int TYPE_FLOAT = 5;

	public final static int TYPE_STRING = 2;

	public final static int TYPE_DATE = 3;

	public final static int TYPE_DICT = 4;
	
	public final static int CATEGORY_BEGINNER = 0;

	public final static int CATEGORY_GENERAL = 1;

	private ObjectType objectType;

	private String name;

	private Long id;

	private String userLabel;

	private Integer type;

	private String dictionary;

	private String description;

	private Boolean visible;

	private Integer category;

	private Integer length = new Integer(-1);

	public AbstractAttribute() {
	}

	public AbstractAttribute(String name2, Integer type2, String dictionary2,
			String userLabel2, ObjectType objType, Integer length2) {
		this.name = name2;
		this.setType(type2);
		this.setLength(length);
		this.dictionary = dictionary2;
		this.userLabel = userLabel2;
		this.objectType = objType;
	}

	public Object parse(String value, int options) throws InvalidNumberOfValuesException, InvalidNumberException, InvalidDateException, StringTooLongException
	{
		return parse(new String[] { value }, options);
	}

	public Object parse(String value) throws InvalidNumberOfValuesException, InvalidNumberException, InvalidDateException, StringTooLongException
	{
		return parse(new String[] { value }, 0);
	}

	public Object parse(String[] values) throws InvalidNumberOfValuesException, InvalidNumberException, InvalidDateException, StringTooLongException
	{
		return parse(values, 0);
	}

	public Object parse(String[] values, int options) throws InvalidNumberOfValuesException, InvalidNumberException, InvalidDateException, StringTooLongException
	{

		String value;
		switch (getType().intValue()) {
		case TYPE_STRING:

			if (values.length != 1 || values[0] == null)
				throw new InvalidNumberOfValuesException();

			value = values[0].trim();
			if (value.length() == 0)
				return null;

			if (length.intValue() != -1 && value.length() > length.intValue())
				throw new StringTooLongException();

			return value;

		case TYPE_INTEGER:

			if (values.length != 1 || values[0] == null)
				throw new InvalidNumberOfValuesException();

			value = values[0].trim();
			if (value.length() == 0)
				return null;

			try {
				return new Integer(values[0]);
			} catch (NumberFormatException nfe) {
				throw new InvalidNumberException();
			}

		case TYPE_LONG:

			if (values.length != 1 || values[0] == null)
				throw new InvalidNumberOfValuesException();

			value = values[0].trim();
			if (value.length() == 0)
				return null;

			try {
				return new Long(values[0]);
			} catch (NumberFormatException nfe) {
				throw new InvalidNumberException();
			}

		case TYPE_FLOAT:

			if (values.length != 1 || values[0] == null)
				throw new InvalidNumberOfValuesException();

			value = values[0].trim();
			if (value.length() == 0)
				return null;

			try {
				return new Float(values[0]);
			} catch (NumberFormatException nfe) {
				throw new InvalidNumberException();
			}

		case TYPE_DATE:

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

		case TYPE_DICT:

			if (values.length != 1 || values[0] == null)
				throw new InvalidNumberOfValuesException();

			value = values[0].trim();
			if (value.length() == 0)
				return null;

			Integer remoteId = null;
			try {
				remoteId = new Integer(value);
			} catch (NumberFormatException nfe) {
				throw new InvalidNumberException();
			}

			Dictionary dicts[] = Dictionary.loadDictionary(getDictionary(),
					remoteId);
			if (dicts.length > 0) {
				return dicts[0];
			} else {
				Dictionary dict = Dictionary.createNew(getDictionary());
				dict.setRemoteId(remoteId);
				dict.setName(remoteId.toString());
				dict.save();
				return dict;
			}

		default:
			return null;

		}

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserLabel() {
		return userLabel;
	}

	public void setUserLabel(String userLabel) {
		this.userLabel = userLabel;
	}

	public ObjectType getObjectType() {
		return objectType;
	}

	public void setObjectType(ObjectType objectType) {
		this.objectType = objectType;
	}

	public String getName() {
		return name;
	}

	public void setName(String attrName) {
		this.name = attrName;
	}

	public String getUserLabelOrName()
	{
		if (userLabel != null && userLabel.length() > 0) return userLabel;
		if (name != null && name.length() > 0) return name;
		return "";
	}

	public Integer getCategory()
	{
		return category;
	}

	public void setCategory(Integer category)
	{
		this.category = category;
	}
	
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		if (type == null) {
			type = new Integer(-1);
		} else {
			this.type = type;
		}
	}

	public String getTypeDisplayName() {
		if (type == null)
			return "";
		switch (type.intValue()) {
		case TYPE_INTEGER:
			return "Integer";
		case TYPE_LONG:
			return "Integer";
		case TYPE_FLOAT:
			return "Decimal";
		case TYPE_STRING:
			return "Text";
		case TYPE_DATE:
			return "Date";
		case TYPE_DICT:
			return "List " + dictionary;
		default:
			return "";
		}
	}
	
	public String getDictionary() {
		return dictionary;
	}

	public void setDictionary(String dictionary) {
		this.dictionary = dictionary;
	}

	public boolean isDictinaory() {
		return dictionary != null;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		if (length != null)
			this.length = length;
	}

	public Boolean getVisible()
	{
		return visible;
	}

	public void setVisible(Boolean visible)
	{
		this.visible = visible;
	}
	
	public boolean isVisible()
	{
		return
			visible != null &&
			visible.booleanValue();
	}
	
	public boolean isVisibleByCategory(int category)
	{
		return
			isVisible() &&
			this.category != null &&
			this.category.intValue() <= category;
	}

	public String toString() {
		if (this.userLabel != null && !this.userLabel.equals("")) {
			return this.userLabel;
		} else {
			return this.name;
		}
	}

	public static AbstractAttribute loadById(Long id) {
		AbstractAttribute attribute = Attribute.loadById(id);
		if (attribute == null) {
			attribute = CompoundAttribute.loadById(id);
		}
		return attribute;
	}

	public static AbstractAttribute loadById(Long id, Session session) {
		AbstractAttribute attribute = Attribute.loadById(id, session);
		if (attribute == null) {
			attribute = CompoundAttribute.loadById(id, session);
		}
		return attribute;
	}

	public List loadContainingGroups(Session session) {
		String attributeType = this instanceof Attribute ? "attributes"
				: "compoundAttributes";
		Query query = session
				.createQuery("from Group g where :attr = some elements(g."
						+ attributeType + ")");
		query.setParameter("attr", this);
		return query.list();
	}

	public List loadContainingGroups() {
		Session session = HibernateUtil.getSession();
		List groups = loadContainingGroups(session);
		session.close();
		return groups;
	}

	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof AbstractAttribute))
			return false;
		AbstractAttribute theOther = (AbstractAttribute) obj;
		return (id == null && theOther.getId() == null)
				|| (id != null && id.equals(theOther.getId()));
	}

	public int hashCode() {
		if (id == null)
			return super.hashCode();
		return id.hashCode();
	}

	public static class UserLabelComparator implements Comparator {
		public int compare(Object o1, Object o2) {
			AbstractAttribute a1 = (AbstractAttribute) o1;
			AbstractAttribute a2 = (AbstractAttribute) o2;
			return a1.getUserLabel().compareToIgnoreCase(a2.getUserLabel());
		}
	}

	public static void sortByUserLabel(Object[] array) {
		Arrays.sort(array, new AbstractAttribute.UserLabelComparator());
	}

	public static void sortByUserLabel(List list) {
		Collections.sort(list, new AbstractAttribute.UserLabelComparator());
	}

	public static class NameComparator implements Comparator {
		public int compare(Object o1, Object o2) {
			AbstractAttribute a1 = (AbstractAttribute) o1;
			AbstractAttribute a2 = (AbstractAttribute) o2;
			return a1.getName().compareToIgnoreCase(a2.getName());
		}
	}

	public static void sortByName(Object[] array) {
		Arrays.sort(array, new AbstractAttribute.NameComparator());
	}

	public static void sortByName(List list) {
		Collections.sort(list, new AbstractAttribute.NameComparator());
	}

	public static class UserLabelOrNameComparator implements Comparator {
		public int compare(Object o1, Object o2) {
			AbstractAttribute a1 = (AbstractAttribute) o1;
			AbstractAttribute a2 = (AbstractAttribute) o2;
			return a1.getUserLabelOrName().compareToIgnoreCase(a2.getUserLabelOrName());
		}
	}

	public static void sortByUserLabelOrName(Object[] array) {
		Arrays.sort(array, new AbstractAttribute.UserLabelOrNameComparator());
	}

	public static void sortByUserLabelOrName(List list) {
		Collections.sort(list, new AbstractAttribute.UserLabelOrNameComparator());
	}

}
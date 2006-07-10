package edu.emory.library.tas;

import edu.emory.library.tas.attrGroups.Attribute;
import edu.emory.library.tas.attrGroups.CompoundAttribute;
import edu.emory.library.tas.attrGroups.Group;
import edu.emory.library.tas.attrGroups.ObjectType;
import edu.emory.library.tas.dicts.BodyPt;
import edu.emory.library.tas.dicts.Country;
import edu.emory.library.tas.dicts.MarkDegree;
import edu.emory.library.tas.dicts.MarkNumber;
import edu.emory.library.tas.dicts.MarkSize;
import edu.emory.library.tas.dicts.MarkType;
import edu.emory.library.tas.dicts.Nation;
import edu.emory.library.tas.dicts.OtherMark;
import edu.emory.library.tas.dicts.PlaceA;
import edu.emory.library.tas.dicts.PlaceB;
import edu.emory.library.tas.dicts.SexAge;
import edu.emory.library.tas.dicts.SpecialMark;
import edu.emory.library.tas.util.query.Conditions;
import edu.emory.library.tas.util.query.QueryValue;

/**
 * Slave object.
 * @author Pawel Jurczyk
 *
 */
public class Slave extends AbstractDescriptiveObject {
	
	private static final Object SLAVE = "Slave";
	/**
	 * Tells whether object has been updated or created.
	 */
	private int modified = 0;
	
	
	public static Group[] getGroups() {
		
		Conditions conditions = new Conditions();
		conditions.addCondition("typeName", SLAVE, Conditions.OP_EQUALS);
		QueryValue query = new QueryValue("ObjectType", conditions);
		query.setCacheable(true);
		Object[] types = (Object[]) query.executeQuery();
		ObjectType type = (ObjectType)types[0];
		
		conditions = new Conditions();
		conditions.addCondition("objectType", type, Conditions.OP_EQUALS);
		query = new QueryValue("Group", conditions);
		query.setOrder(QueryValue.ORDER_ASC);
		query.setOrderBy(new String[] {"name"});
		query.setCacheable(true);
		Object[] attributes = (Object[]) query.executeQuery();
		Group[] ret = null;
		if (attributes.length == 0) {
			ret = new Group[] {};
		} else {
			ret = new Group[attributes.length];
			for (int i = 0; i < attributes.length; i++) {
				ret[i] = (Group)attributes[i];
			}
		}		
		return ret;
	}
	
	public static Attribute[] getAttributes() {
		
		Conditions conditions = new Conditions();
		conditions.addCondition("typeName", SLAVE, Conditions.OP_EQUALS);
		QueryValue query = new QueryValue("ObjectType", conditions);
		query.setCacheable(true);
		Object[] types = (Object[]) query.executeQuery();
		ObjectType type = (ObjectType)types[0];
		
		conditions = new Conditions();
		conditions.addCondition("objectType", type, Conditions.OP_EQUALS);
		query = new QueryValue("Attribute", conditions);
		query.setOrder(QueryValue.ORDER_ASC);
		query.setOrderBy(new String[] {"name"});
		query.setCacheable(true);
		Object[] attributes = (Object[]) query.executeQuery();
		Attribute[] ret = null;
		if (attributes.length == 0) {
			ret = new Attribute[] {};
		} else {
			ret = new Attribute[attributes.length];
			for (int i = 0; i < attributes.length; i++) {
				ret[i] = (Attribute)attributes[i];
			}
		}		
		return ret;
	}
	
	public static Attribute getAttribute(String name) {
		
		Conditions conditions = new Conditions();
		conditions.addCondition("typeName", SLAVE, Conditions.OP_EQUALS);
		QueryValue query = new QueryValue("ObjectType", conditions);
		query.setCacheable(true);
		Object[] types = (Object[]) query.executeQuery();
		ObjectType type = (ObjectType)types[0];
		
		conditions = new Conditions();
		conditions.addCondition("objectType", type, Conditions.OP_EQUALS);
		conditions.addCondition("name", name, Conditions.OP_EQUALS);
		query = new QueryValue("Attribute", conditions);
		query.setCacheable(true);
		Object[] attributes = (Object[]) query.executeQuery();
		Attribute ret = null;
		if (attributes.length != 0) {
			ret = (Attribute)attributes[0];
		}
		return ret;
	}
	
	public static String[] getAllAttrNames() {
		
		Conditions conditions = new Conditions();
		conditions.addCondition("typeName", SLAVE, Conditions.OP_EQUALS);
		QueryValue query = new QueryValue("ObjectType", conditions);
		query.setCacheable(true);
		Object[] types = (Object[]) query.executeQuery();
		ObjectType type = (ObjectType)types[0];
		
		conditions = new Conditions();
		conditions.addCondition("objectType", type, Conditions.OP_EQUALS);
		query = new QueryValue("Attribute", conditions);
		query.setOrder(QueryValue.ORDER_ASC);
		query.setOrderBy(new String[] {"name"});
		query.setCacheable(true);
		Object[] attributes = (Object[]) query.executeQuery();
		String[] ret = null;
		if (attributes.length == 0) {
			ret = new String[] {};
		} else {
			ret = new String[attributes.length];
			for (int i = 0; i < attributes.length; i++) {
				ret[i] = ((Attribute)attributes[i]).getName();
			}
		}		
		return ret;
	}

	public static CompoundAttribute[] getCoumpoundAttributes() {
		
		Conditions conditions = new Conditions();
		conditions.addCondition("typeName", SLAVE, Conditions.OP_EQUALS);
		QueryValue query = new QueryValue("ObjectType", conditions);
		query.setCacheable(true);
		Object[] types = (Object[]) query.executeQuery();
		ObjectType type = (ObjectType)types[0];
		
		conditions = new Conditions();
		conditions.addCondition("objectType", type, Conditions.OP_EQUALS);
		query = new QueryValue("CompoundAttribute", conditions);
		query.setOrder(QueryValue.ORDER_ASC);
		query.setOrderBy(new String[] {"name"});
		query.setCacheable(true);
		Object[] attributes = (Object[]) query.executeQuery();
		CompoundAttribute[] ret = null;
		if (attributes.length == 0) {
			ret = new CompoundAttribute[] {};
		} else {
			ret = new CompoundAttribute[attributes.length];
			for (int i = 0; i < attributes.length; i++) {
				ret[i] = (CompoundAttribute)attributes[i];
			}
		}		
		return ret;
	}
	
//	/**
//	 * Gets Schema of given attribute.
//	 * @param p_attrName	attribute name
//	 * @return	user label of attribute
//	 */
//	public static SchemaColumn getSchemaColumn(String p_attrName) {
//		initTypes();
//		return (SchemaColumn)types.get(p_attrName);
//	}
//
//	/**
//	 * Gets a list of all valid attributes for object.
//	 * @return String[] with attribute names
//	 */
//	public static String[] getAllAttrNames() {
//		initTypes();
//		return (String[])types.keySet().toArray(new String[] {});
//	}
//	
//	/*AUTOGENERATED BLOCK*/
//	private static Map types = null;
//	public static synchronized void initTypes()
//	{
//		if (types != null) return; 
//		types = new HashMap();
//		
//		/** Static construction **/
//		types.put("iid", new SchemaColumn("iid", 1, null, -1, null, null, null, null, null, -1));
//		types.put("slaveId", new SchemaColumn("slaveId", 1, null, 0, "slaveid", null, null, null, "Slave ID", -1));
//		types.put("voyageId", new SchemaColumn("voyageId", 1, null, 0, "voyageid", null, null, null, "Voyage ID", -1));
//		types.put("sexage", new SchemaColumn("sexage", 4, "SexAge", 0, null, null, null, null, null, -1));
//		types.put("age", new SchemaColumn("age", 5, null, 0, null, null, null, null, null, -1));
//		types.put("feet", new SchemaColumn("feet", 0, null, 0, null, null, null, null, null, -1));
//		types.put("inch", new SchemaColumn("inch", 5, null, 0, null, null, null, null, null, -1));
//		types.put("ageinf", new SchemaColumn("ageinf", 0, null, 0, null, null, null, null, null, -1));
//		types.put("country", new SchemaColumn("country", 4, "Country", 0, null, null, null, null, null, -1));
//		types.put("name", new SchemaColumn("name", 2, null, 1, null, null, null, null, null, 25));
//		types.put("unclear", new SchemaColumn("unclear", 0, null, 0, null, null, null, null, null, -1));
//		types.put("nameimp", new SchemaColumn("nameimp", 2, null, 1, null, null, null, null, null, 25));
//		types.put("natimp", new SchemaColumn("natimp", 4, "Nation", 0, null, null, null, null, null, -1));
//		types.put("bodypt1", new SchemaColumn("bodypt1", 4, "BodyPt", 0, null, null, null, null, null, -1));
//		types.put("placea1", new SchemaColumn("placea1", 4, "PlaceA", 0, null, null, null, null, null, -1));
//		types.put("placeb1", new SchemaColumn("placeb1", 4, "PlaceB", 0, null, null, null, null, null, -1));
//		types.put("marksize", new SchemaColumn("marksize", 4, "MarkSize", 0, null, null, null, null, null, -1));
//		types.put("marktype", new SchemaColumn("marktype", 4, "MarkType", 0, null, null, null, null, null, -1));
//		types.put("marknumb", new SchemaColumn("marknumb", 4, "MarkNumber", 0, null, null, null, null, null, -1));
//		types.put("markdegr", new SchemaColumn("markdegr", 4, "MarkDegree", 0, null, null, null, null, null, -1));
//		types.put("othrmark", new SchemaColumn("othrmark", 4, "OtherMark", 0, null, null, null, null, null, -1));
//		types.put("markspec", new SchemaColumn("markspec", 4, "SpecialMark", 0, null, null, null, null, null, -1));
//		types.put("bodypt2", new SchemaColumn("bodypt2", 4, "BodyPt", 0, null, null, null, null, null, -1));
//		types.put("placea2", new SchemaColumn("placea2", 4, "PlaceA", 0, null, null, null, null, null, -1));
//		types.put("placeb2", new SchemaColumn("placeb2", 4, "PlaceB", 0, null, null, null, null, null, -1));
//		types.put("marksiz2", new SchemaColumn("marksiz2", 4, "MarkSize", 0, null, null, null, null, null, -1));
//		types.put("marktyp2", new SchemaColumn("marktyp2", 4, "MarkType", 0, null, null, null, null, null, -1));
//		types.put("marknum2", new SchemaColumn("marknum2", 4, "MarkNumber", 0, null, null, null, null, null, -1));
//		types.put("markdeg2", new SchemaColumn("markdeg2", 4, "MarkDegree", 0, null, null, null, null, null, -1));
//		types.put("bodypt3", new SchemaColumn("bodypt3", 4, "BodyPt", 0, null, null, null, null, null, -1));
//		types.put("placea3", new SchemaColumn("placea3", 4, "PlaceA", 0, null, null, null, null, null, -1));
//		types.put("placeb3", new SchemaColumn("placeb3", 4, "PlaceB", 0, null, null, null, null, null, -1));
//		types.put("marksiz3", new SchemaColumn("marksiz3", 4, "MarkSize", 0, null, null, null, null, null, -1));
//		types.put("marktyp3", new SchemaColumn("marktyp3", 4, "MarkType", 0, null, null, null, null, null, -1));
//		types.put("marknum3", new SchemaColumn("marknum3", 4, "MarkNumber", 0, null, null, null, null, null, -1));
//		types.put("markdeg3", new SchemaColumn("markdeg3", 4, "MarkDegree", 0, null, null, null, null, null, -1));
//		types.put("bodypt4", new SchemaColumn("bodypt4", 4, "BodyPt", 0, null, null, null, null, null, -1));
//		types.put("bodypt5", new SchemaColumn("bodypt5", 4, "BodyPt", 0, null, null, null, null, null, -1));
//		types.put("bodypt6", new SchemaColumn("bodypt6", 4, "BodyPt", 0, null, null, null, null, null, -1));
//		types.put("placea4", new SchemaColumn("placea4", 4, "PlaceA", 0, null, null, null, null, null, -1));
//		types.put("placea5", new SchemaColumn("placea5", 4, "PlaceA", 0, null, null, null, null, null, -1));
//		types.put("placea6", new SchemaColumn("placea6", 4, "PlaceA", 0, null, null, null, null, null, -1));
//		types.put("placeb4", new SchemaColumn("placeb4", 4, "PlaceB", 0, null, null, null, null, null, -1));
//		types.put("placeb5", new SchemaColumn("placeb5", 4, "PlaceB", 0, null, null, null, null, null, -1));
//		types.put("placeb6", new SchemaColumn("placeb6", 4, "PlaceB", 0, null, null, null, null, null, -1));
//		types.put("marksiz4", new SchemaColumn("marksiz4", 4, "MarkSize", 0, null, null, null, null, null, -1));
//		types.put("marksiz5", new SchemaColumn("marksiz5", 4, "MarkSize", 0, null, null, null, null, null, -1));
//		types.put("marksiz6", new SchemaColumn("marksiz6", 4, "MarkSize", 0, null, null, null, null, null, -1));
//		types.put("marktyp6", new SchemaColumn("marktyp6", 4, "MarkType", 0, null, null, null, null, null, -1));
//		types.put("marktyp5", new SchemaColumn("marktyp5", 4, "MarkType", 0, null, null, null, null, null, -1));
//		types.put("marktyp4", new SchemaColumn("marktyp4", 4, "MarkType", 0, null, null, null, null, null, -1));
//		types.put("marknum4", new SchemaColumn("marknum4", 4, "MarkNumber", 0, null, null, null, null, null, -1));
//		types.put("marknum5", new SchemaColumn("marknum5", 4, "MarkNumber", 0, null, null, null, null, null, -1));
//		types.put("marknum6", new SchemaColumn("marknum6", 4, "MarkNumber", 0, null, null, null, null, null, -1));
//		types.put("markdeg6", new SchemaColumn("markdeg6", 4, "MarkDegree", 0, null, null, null, null, null, -1));
//		types.put("markdeg5", new SchemaColumn("markdeg5", 4, "MarkDegree", 0, null, null, null, null, null, -1));
//		types.put("markdeg4", new SchemaColumn("markdeg4", 4, "MarkDegree", 0, null, null, null, null, null, -1));
//		types.put("majselpt", new SchemaColumn("majselpt", 0, null, 0, null, null, null, null, null, -1));
//		types.put("source", new SchemaColumn("source", 2, null, 1, null, null, null, null, null, 8));
//		types.put("majbuypt", new SchemaColumn("majbuypt", 0, null, 0, null, null, null, null, null, -1));
//		types.put("datarr34", new SchemaColumn("datarr34", 0, null, 0, null, null, null, null, null, -1));
//		types.put("regem1", new SchemaColumn("regem1", 0, null, 0, null, null, null, null, null, -1));
//	}
//	/*END AUTOGENERATED*/
	
	/**
	 * Basic constructor - object will have assigned new ID.
	 */
	public Slave() {		
	}
	
	/**
	 * Constructor that sets object ID.
	 * @param slaveId
	 */
	private Slave(Long slaveId) {
		this.setSlaveId(slaveId);
	}
	
	/**
	 * Creates new Slave with given ID.
	 * @param slaveId slave ID 
	 * @return  Slave object
	 */
	public static Slave createNew(Long slaveId) {
		return new Slave(slaveId);
	}

	/**
	 * Clones object. Copy is deep copy.
	 */
	public Object clone() {
		Slave n = new Slave();
		n.setModified(getModified());
		n.values = values;
		return n;
	}

	/**
	 * Gets modified flag.
	 * @return modified flag
	 */
	public int getModified() {
		return modified;
	}

	/**
	 * Sets modified flag
	 * @param modified new modified value
	 */
	public void setModified(int modified) {
		this.modified = modified;
	}
	
	/**
	 * Gets String representation of object.
	 */
	public String toString() {
		return "Slave " + this.values;
	}
	
	
	/*AUTOGENERATED BLOCK*/
	/** Getters/setters **/
	public void setIid(Long iid) {
		if ((iid == null && this.values.get("iid") != null) 
			|| (iid != null && !iid.equals(this.values.get("iid")))) {
			this.modified = UPDATED;
		}
		this.values.put("iid", iid);
	}
	public void setSlaveId(Long slaveId) {
		if ((slaveId == null && this.values.get("slaveId") != null) 
			|| (slaveId != null && !slaveId.equals(this.values.get("slaveId")))) {
			this.modified = UPDATED;
		}
		this.values.put("slaveId", slaveId);
	}
	public void setVoyageId(Long voyageId) {
		if ((voyageId == null && this.values.get("voyageId") != null) 
			|| (voyageId != null && !voyageId.equals(this.values.get("voyageId")))) {
			this.modified = UPDATED;
		}
		this.values.put("voyageId", voyageId);
	}
	public void setSexage(SexAge sexage) {
		if ((sexage == null && this.values.get("sexage") != null) 
			|| (sexage != null && !sexage.equals(this.values.get("sexage")))) {
			this.modified = UPDATED;
		}
		this.values.put("sexage", sexage);
	}
	public void setAge(Float age) {
		if ((age == null && this.values.get("age") != null) 
			|| (age != null && !age.equals(this.values.get("age")))) {
			this.modified = UPDATED;
		}
		this.values.put("age", age);
	}
	public void setFeet(Integer feet) {
		if ((feet == null && this.values.get("feet") != null) 
			|| (feet != null && !feet.equals(this.values.get("feet")))) {
			this.modified = UPDATED;
		}
		this.values.put("feet", feet);
	}
	public void setInch(Float inch) {
		if ((inch == null && this.values.get("inch") != null) 
			|| (inch != null && !inch.equals(this.values.get("inch")))) {
			this.modified = UPDATED;
		}
		this.values.put("inch", inch);
	}
	public void setAgeinf(Integer ageinf) {
		if ((ageinf == null && this.values.get("ageinf") != null) 
			|| (ageinf != null && !ageinf.equals(this.values.get("ageinf")))) {
			this.modified = UPDATED;
		}
		this.values.put("ageinf", ageinf);
	}
	public void setCountry(Country country) {
		if ((country == null && this.values.get("country") != null) 
			|| (country != null && !country.equals(this.values.get("country")))) {
			this.modified = UPDATED;
		}
		this.values.put("country", country);
	}
	public void setName(String name) {
		if ((name == null && this.values.get("name") != null) 
			|| (name != null && !name.equals(this.values.get("name")))) {
			this.modified = UPDATED;
		}
		this.values.put("name", name);
	}
	public void setUnclear(Integer unclear) {
		if ((unclear == null && this.values.get("unclear") != null) 
			|| (unclear != null && !unclear.equals(this.values.get("unclear")))) {
			this.modified = UPDATED;
		}
		this.values.put("unclear", unclear);
	}
	public void setNameimp(String nameimp) {
		if ((nameimp == null && this.values.get("nameimp") != null) 
			|| (nameimp != null && !nameimp.equals(this.values.get("nameimp")))) {
			this.modified = UPDATED;
		}
		this.values.put("nameimp", nameimp);
	}
	public void setNatimp(Nation natimp) {
		if ((natimp == null && this.values.get("natimp") != null) 
			|| (natimp != null && !natimp.equals(this.values.get("natimp")))) {
			this.modified = UPDATED;
		}
		this.values.put("natimp", natimp);
	}
	public void setBodypt1(BodyPt bodypt1) {
		if ((bodypt1 == null && this.values.get("bodypt1") != null) 
			|| (bodypt1 != null && !bodypt1.equals(this.values.get("bodypt1")))) {
			this.modified = UPDATED;
		}
		this.values.put("bodypt1", bodypt1);
	}
	public void setPlacea1(PlaceA placea1) {
		if ((placea1 == null && this.values.get("placea1") != null) 
			|| (placea1 != null && !placea1.equals(this.values.get("placea1")))) {
			this.modified = UPDATED;
		}
		this.values.put("placea1", placea1);
	}
	public void setPlaceb1(PlaceB placeb1) {
		if ((placeb1 == null && this.values.get("placeb1") != null) 
			|| (placeb1 != null && !placeb1.equals(this.values.get("placeb1")))) {
			this.modified = UPDATED;
		}
		this.values.put("placeb1", placeb1);
	}
	public void setMarksize(MarkSize marksize) {
		if ((marksize == null && this.values.get("marksize") != null) 
			|| (marksize != null && !marksize.equals(this.values.get("marksize")))) {
			this.modified = UPDATED;
		}
		this.values.put("marksize", marksize);
	}
	public void setMarktype(MarkType marktype) {
		if ((marktype == null && this.values.get("marktype") != null) 
			|| (marktype != null && !marktype.equals(this.values.get("marktype")))) {
			this.modified = UPDATED;
		}
		this.values.put("marktype", marktype);
	}
	public void setMarknumb(MarkNumber marknumb) {
		if ((marknumb == null && this.values.get("marknumb") != null) 
			|| (marknumb != null && !marknumb.equals(this.values.get("marknumb")))) {
			this.modified = UPDATED;
		}
		this.values.put("marknumb", marknumb);
	}
	public void setMarkdegr(MarkDegree markdegr) {
		if ((markdegr == null && this.values.get("markdegr") != null) 
			|| (markdegr != null && !markdegr.equals(this.values.get("markdegr")))) {
			this.modified = UPDATED;
		}
		this.values.put("markdegr", markdegr);
	}
	public void setOthrmark(OtherMark othrmark) {
		if ((othrmark == null && this.values.get("othrmark") != null) 
			|| (othrmark != null && !othrmark.equals(this.values.get("othrmark")))) {
			this.modified = UPDATED;
		}
		this.values.put("othrmark", othrmark);
	}
	public void setMarkspec(SpecialMark markspec) {
		if ((markspec == null && this.values.get("markspec") != null) 
			|| (markspec != null && !markspec.equals(this.values.get("markspec")))) {
			this.modified = UPDATED;
		}
		this.values.put("markspec", markspec);
	}
	public void setBodypt2(BodyPt bodypt2) {
		if ((bodypt2 == null && this.values.get("bodypt2") != null) 
			|| (bodypt2 != null && !bodypt2.equals(this.values.get("bodypt2")))) {
			this.modified = UPDATED;
		}
		this.values.put("bodypt2", bodypt2);
	}
	public void setPlacea2(PlaceA placea2) {
		if ((placea2 == null && this.values.get("placea2") != null) 
			|| (placea2 != null && !placea2.equals(this.values.get("placea2")))) {
			this.modified = UPDATED;
		}
		this.values.put("placea2", placea2);
	}
	public void setPlaceb2(PlaceB placeb2) {
		if ((placeb2 == null && this.values.get("placeb2") != null) 
			|| (placeb2 != null && !placeb2.equals(this.values.get("placeb2")))) {
			this.modified = UPDATED;
		}
		this.values.put("placeb2", placeb2);
	}
	public void setMarksiz2(MarkSize marksiz2) {
		if ((marksiz2 == null && this.values.get("marksiz2") != null) 
			|| (marksiz2 != null && !marksiz2.equals(this.values.get("marksiz2")))) {
			this.modified = UPDATED;
		}
		this.values.put("marksiz2", marksiz2);
	}
	public void setMarktyp2(MarkType marktyp2) {
		if ((marktyp2 == null && this.values.get("marktyp2") != null) 
			|| (marktyp2 != null && !marktyp2.equals(this.values.get("marktyp2")))) {
			this.modified = UPDATED;
		}
		this.values.put("marktyp2", marktyp2);
	}
	public void setMarknum2(MarkNumber marknum2) {
		if ((marknum2 == null && this.values.get("marknum2") != null) 
			|| (marknum2 != null && !marknum2.equals(this.values.get("marknum2")))) {
			this.modified = UPDATED;
		}
		this.values.put("marknum2", marknum2);
	}
	public void setMarkdeg2(MarkDegree markdeg2) {
		if ((markdeg2 == null && this.values.get("markdeg2") != null) 
			|| (markdeg2 != null && !markdeg2.equals(this.values.get("markdeg2")))) {
			this.modified = UPDATED;
		}
		this.values.put("markdeg2", markdeg2);
	}
	public void setBodypt3(BodyPt bodypt3) {
		if ((bodypt3 == null && this.values.get("bodypt3") != null) 
			|| (bodypt3 != null && !bodypt3.equals(this.values.get("bodypt3")))) {
			this.modified = UPDATED;
		}
		this.values.put("bodypt3", bodypt3);
	}
	public void setPlacea3(PlaceA placea3) {
		if ((placea3 == null && this.values.get("placea3") != null) 
			|| (placea3 != null && !placea3.equals(this.values.get("placea3")))) {
			this.modified = UPDATED;
		}
		this.values.put("placea3", placea3);
	}
	public void setPlaceb3(PlaceB placeb3) {
		if ((placeb3 == null && this.values.get("placeb3") != null) 
			|| (placeb3 != null && !placeb3.equals(this.values.get("placeb3")))) {
			this.modified = UPDATED;
		}
		this.values.put("placeb3", placeb3);
	}
	public void setMarksiz3(MarkSize marksiz3) {
		if ((marksiz3 == null && this.values.get("marksiz3") != null) 
			|| (marksiz3 != null && !marksiz3.equals(this.values.get("marksiz3")))) {
			this.modified = UPDATED;
		}
		this.values.put("marksiz3", marksiz3);
	}
	public void setMarktyp3(MarkType marktyp3) {
		if ((marktyp3 == null && this.values.get("marktyp3") != null) 
			|| (marktyp3 != null && !marktyp3.equals(this.values.get("marktyp3")))) {
			this.modified = UPDATED;
		}
		this.values.put("marktyp3", marktyp3);
	}
	public void setMarknum3(MarkNumber marknum3) {
		if ((marknum3 == null && this.values.get("marknum3") != null) 
			|| (marknum3 != null && !marknum3.equals(this.values.get("marknum3")))) {
			this.modified = UPDATED;
		}
		this.values.put("marknum3", marknum3);
	}
	public void setMarkdeg3(MarkDegree markdeg3) {
		if ((markdeg3 == null && this.values.get("markdeg3") != null) 
			|| (markdeg3 != null && !markdeg3.equals(this.values.get("markdeg3")))) {
			this.modified = UPDATED;
		}
		this.values.put("markdeg3", markdeg3);
	}
	public void setBodypt4(BodyPt bodypt4) {
		if ((bodypt4 == null && this.values.get("bodypt4") != null) 
			|| (bodypt4 != null && !bodypt4.equals(this.values.get("bodypt4")))) {
			this.modified = UPDATED;
		}
		this.values.put("bodypt4", bodypt4);
	}
	public void setBodypt5(BodyPt bodypt5) {
		if ((bodypt5 == null && this.values.get("bodypt5") != null) 
			|| (bodypt5 != null && !bodypt5.equals(this.values.get("bodypt5")))) {
			this.modified = UPDATED;
		}
		this.values.put("bodypt5", bodypt5);
	}
	public void setBodypt6(BodyPt bodypt6) {
		if ((bodypt6 == null && this.values.get("bodypt6") != null) 
			|| (bodypt6 != null && !bodypt6.equals(this.values.get("bodypt6")))) {
			this.modified = UPDATED;
		}
		this.values.put("bodypt6", bodypt6);
	}
	public void setPlacea4(PlaceA placea4) {
		if ((placea4 == null && this.values.get("placea4") != null) 
			|| (placea4 != null && !placea4.equals(this.values.get("placea4")))) {
			this.modified = UPDATED;
		}
		this.values.put("placea4", placea4);
	}
	public void setPlacea5(PlaceA placea5) {
		if ((placea5 == null && this.values.get("placea5") != null) 
			|| (placea5 != null && !placea5.equals(this.values.get("placea5")))) {
			this.modified = UPDATED;
		}
		this.values.put("placea5", placea5);
	}
	public void setPlacea6(PlaceA placea6) {
		if ((placea6 == null && this.values.get("placea6") != null) 
			|| (placea6 != null && !placea6.equals(this.values.get("placea6")))) {
			this.modified = UPDATED;
		}
		this.values.put("placea6", placea6);
	}
	public void setPlaceb4(PlaceB placeb4) {
		if ((placeb4 == null && this.values.get("placeb4") != null) 
			|| (placeb4 != null && !placeb4.equals(this.values.get("placeb4")))) {
			this.modified = UPDATED;
		}
		this.values.put("placeb4", placeb4);
	}
	public void setPlaceb5(PlaceB placeb5) {
		if ((placeb5 == null && this.values.get("placeb5") != null) 
			|| (placeb5 != null && !placeb5.equals(this.values.get("placeb5")))) {
			this.modified = UPDATED;
		}
		this.values.put("placeb5", placeb5);
	}
	public void setPlaceb6(PlaceB placeb6) {
		if ((placeb6 == null && this.values.get("placeb6") != null) 
			|| (placeb6 != null && !placeb6.equals(this.values.get("placeb6")))) {
			this.modified = UPDATED;
		}
		this.values.put("placeb6", placeb6);
	}
	public void setMarksiz4(MarkSize marksiz4) {
		if ((marksiz4 == null && this.values.get("marksiz4") != null) 
			|| (marksiz4 != null && !marksiz4.equals(this.values.get("marksiz4")))) {
			this.modified = UPDATED;
		}
		this.values.put("marksiz4", marksiz4);
	}
	public void setMarksiz5(MarkSize marksiz5) {
		if ((marksiz5 == null && this.values.get("marksiz5") != null) 
			|| (marksiz5 != null && !marksiz5.equals(this.values.get("marksiz5")))) {
			this.modified = UPDATED;
		}
		this.values.put("marksiz5", marksiz5);
	}
	public void setMarksiz6(MarkSize marksiz6) {
		if ((marksiz6 == null && this.values.get("marksiz6") != null) 
			|| (marksiz6 != null && !marksiz6.equals(this.values.get("marksiz6")))) {
			this.modified = UPDATED;
		}
		this.values.put("marksiz6", marksiz6);
	}
	public void setMarktyp6(MarkType marktyp6) {
		if ((marktyp6 == null && this.values.get("marktyp6") != null) 
			|| (marktyp6 != null && !marktyp6.equals(this.values.get("marktyp6")))) {
			this.modified = UPDATED;
		}
		this.values.put("marktyp6", marktyp6);
	}
	public void setMarktyp5(MarkType marktyp5) {
		if ((marktyp5 == null && this.values.get("marktyp5") != null) 
			|| (marktyp5 != null && !marktyp5.equals(this.values.get("marktyp5")))) {
			this.modified = UPDATED;
		}
		this.values.put("marktyp5", marktyp5);
	}
	public void setMarktyp4(MarkType marktyp4) {
		if ((marktyp4 == null && this.values.get("marktyp4") != null) 
			|| (marktyp4 != null && !marktyp4.equals(this.values.get("marktyp4")))) {
			this.modified = UPDATED;
		}
		this.values.put("marktyp4", marktyp4);
	}
	public void setMarknum4(MarkNumber marknum4) {
		if ((marknum4 == null && this.values.get("marknum4") != null) 
			|| (marknum4 != null && !marknum4.equals(this.values.get("marknum4")))) {
			this.modified = UPDATED;
		}
		this.values.put("marknum4", marknum4);
	}
	public void setMarknum5(MarkNumber marknum5) {
		if ((marknum5 == null && this.values.get("marknum5") != null) 
			|| (marknum5 != null && !marknum5.equals(this.values.get("marknum5")))) {
			this.modified = UPDATED;
		}
		this.values.put("marknum5", marknum5);
	}
	public void setMarknum6(MarkNumber marknum6) {
		if ((marknum6 == null && this.values.get("marknum6") != null) 
			|| (marknum6 != null && !marknum6.equals(this.values.get("marknum6")))) {
			this.modified = UPDATED;
		}
		this.values.put("marknum6", marknum6);
	}
	public void setMarkdeg6(MarkDegree markdeg6) {
		if ((markdeg6 == null && this.values.get("markdeg6") != null) 
			|| (markdeg6 != null && !markdeg6.equals(this.values.get("markdeg6")))) {
			this.modified = UPDATED;
		}
		this.values.put("markdeg6", markdeg6);
	}
	public void setMarkdeg5(MarkDegree markdeg5) {
		if ((markdeg5 == null && this.values.get("markdeg5") != null) 
			|| (markdeg5 != null && !markdeg5.equals(this.values.get("markdeg5")))) {
			this.modified = UPDATED;
		}
		this.values.put("markdeg5", markdeg5);
	}
	public void setMarkdeg4(MarkDegree markdeg4) {
		if ((markdeg4 == null && this.values.get("markdeg4") != null) 
			|| (markdeg4 != null && !markdeg4.equals(this.values.get("markdeg4")))) {
			this.modified = UPDATED;
		}
		this.values.put("markdeg4", markdeg4);
	}
	public void setMajselpt(Integer majselpt) {
		if ((majselpt == null && this.values.get("majselpt") != null) 
			|| (majselpt != null && !majselpt.equals(this.values.get("majselpt")))) {
			this.modified = UPDATED;
		}
		this.values.put("majselpt", majselpt);
	}
	public void setSource(String source) {
		if ((source == null && this.values.get("source") != null) 
			|| (source != null && !source.equals(this.values.get("source")))) {
			this.modified = UPDATED;
		}
		this.values.put("source", source);
	}
	public void setMajbuypt(Integer majbuypt) {
		if ((majbuypt == null && this.values.get("majbuypt") != null) 
			|| (majbuypt != null && !majbuypt.equals(this.values.get("majbuypt")))) {
			this.modified = UPDATED;
		}
		this.values.put("majbuypt", majbuypt);
	}
	public void setDatarr34(Integer datarr34) {
		if ((datarr34 == null && this.values.get("datarr34") != null) 
			|| (datarr34 != null && !datarr34.equals(this.values.get("datarr34")))) {
			this.modified = UPDATED;
		}
		this.values.put("datarr34", datarr34);
	}
	public void setRegem1(Integer regem1) {
		if ((regem1 == null && this.values.get("regem1") != null) 
			|| (regem1 != null && !regem1.equals(this.values.get("regem1")))) {
			this.modified = UPDATED;
		}
		this.values.put("regem1", regem1);
	}
	public Long getIid() {
		return (Long)this.values.get("iid");
	}
	public Long getSlaveId() {
		return (Long)this.values.get("slaveId");
	}
	public Long getVoyageId() {
		return (Long)this.values.get("voyageId");
	}
	public SexAge getSexage() {
		return (SexAge)this.values.get("sexage");
	}
	public Float getAge() {
		return (Float)this.values.get("age");
	}
	public Integer getFeet() {
		return (Integer)this.values.get("feet");
	}
	public Float getInch() {
		return (Float)this.values.get("inch");
	}
	public Integer getAgeinf() {
		return (Integer)this.values.get("ageinf");
	}
	public Country getCountry() {
		return (Country)this.values.get("country");
	}
	public String getName() {
		return (String)this.values.get("name");
	}
	public Integer getUnclear() {
		return (Integer)this.values.get("unclear");
	}
	public String getNameimp() {
		return (String)this.values.get("nameimp");
	}
	public Nation getNatimp() {
		return (Nation)this.values.get("natimp");
	}
	public BodyPt getBodypt1() {
		return (BodyPt)this.values.get("bodypt1");
	}
	public PlaceA getPlacea1() {
		return (PlaceA)this.values.get("placea1");
	}
	public PlaceB getPlaceb1() {
		return (PlaceB)this.values.get("placeb1");
	}
	public MarkSize getMarksize() {
		return (MarkSize)this.values.get("marksize");
	}
	public MarkType getMarktype() {
		return (MarkType)this.values.get("marktype");
	}
	public MarkNumber getMarknumb() {
		return (MarkNumber)this.values.get("marknumb");
	}
	public MarkDegree getMarkdegr() {
		return (MarkDegree)this.values.get("markdegr");
	}
	public OtherMark getOthrmark() {
		return (OtherMark)this.values.get("othrmark");
	}
	public SpecialMark getMarkspec() {
		return (SpecialMark)this.values.get("markspec");
	}
	public BodyPt getBodypt2() {
		return (BodyPt)this.values.get("bodypt2");
	}
	public PlaceA getPlacea2() {
		return (PlaceA)this.values.get("placea2");
	}
	public PlaceB getPlaceb2() {
		return (PlaceB)this.values.get("placeb2");
	}
	public MarkSize getMarksiz2() {
		return (MarkSize)this.values.get("marksiz2");
	}
	public MarkType getMarktyp2() {
		return (MarkType)this.values.get("marktyp2");
	}
	public MarkNumber getMarknum2() {
		return (MarkNumber)this.values.get("marknum2");
	}
	public MarkDegree getMarkdeg2() {
		return (MarkDegree)this.values.get("markdeg2");
	}
	public BodyPt getBodypt3() {
		return (BodyPt)this.values.get("bodypt3");
	}
	public PlaceA getPlacea3() {
		return (PlaceA)this.values.get("placea3");
	}
	public PlaceB getPlaceb3() {
		return (PlaceB)this.values.get("placeb3");
	}
	public MarkSize getMarksiz3() {
		return (MarkSize)this.values.get("marksiz3");
	}
	public MarkType getMarktyp3() {
		return (MarkType)this.values.get("marktyp3");
	}
	public MarkNumber getMarknum3() {
		return (MarkNumber)this.values.get("marknum3");
	}
	public MarkDegree getMarkdeg3() {
		return (MarkDegree)this.values.get("markdeg3");
	}
	public BodyPt getBodypt4() {
		return (BodyPt)this.values.get("bodypt4");
	}
	public BodyPt getBodypt5() {
		return (BodyPt)this.values.get("bodypt5");
	}
	public BodyPt getBodypt6() {
		return (BodyPt)this.values.get("bodypt6");
	}
	public PlaceA getPlacea4() {
		return (PlaceA)this.values.get("placea4");
	}
	public PlaceA getPlacea5() {
		return (PlaceA)this.values.get("placea5");
	}
	public PlaceA getPlacea6() {
		return (PlaceA)this.values.get("placea6");
	}
	public PlaceB getPlaceb4() {
		return (PlaceB)this.values.get("placeb4");
	}
	public PlaceB getPlaceb5() {
		return (PlaceB)this.values.get("placeb5");
	}
	public PlaceB getPlaceb6() {
		return (PlaceB)this.values.get("placeb6");
	}
	public MarkSize getMarksiz4() {
		return (MarkSize)this.values.get("marksiz4");
	}
	public MarkSize getMarksiz5() {
		return (MarkSize)this.values.get("marksiz5");
	}
	public MarkSize getMarksiz6() {
		return (MarkSize)this.values.get("marksiz6");
	}
	public MarkType getMarktyp6() {
		return (MarkType)this.values.get("marktyp6");
	}
	public MarkType getMarktyp5() {
		return (MarkType)this.values.get("marktyp5");
	}
	public MarkType getMarktyp4() {
		return (MarkType)this.values.get("marktyp4");
	}
	public MarkNumber getMarknum4() {
		return (MarkNumber)this.values.get("marknum4");
	}
	public MarkNumber getMarknum5() {
		return (MarkNumber)this.values.get("marknum5");
	}
	public MarkNumber getMarknum6() {
		return (MarkNumber)this.values.get("marknum6");
	}
	public MarkDegree getMarkdeg6() {
		return (MarkDegree)this.values.get("markdeg6");
	}
	public MarkDegree getMarkdeg5() {
		return (MarkDegree)this.values.get("markdeg5");
	}
	public MarkDegree getMarkdeg4() {
		return (MarkDegree)this.values.get("markdeg4");
	}
	public Integer getMajselpt() {
		return (Integer)this.values.get("majselpt");
	}
	public String getSource() {
		return (String)this.values.get("source");
	}
	public Integer getMajbuypt() {
		return (Integer)this.values.get("majbuypt");
	}
	public Integer getDatarr34() {
		return (Integer)this.values.get("datarr34");
	}
	public Integer getRegem1() {
		return (Integer)this.values.get("regem1");
	}
	/*END AUTOGENERATED*/
}

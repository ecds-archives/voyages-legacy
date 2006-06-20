package edu.emory.library.tas;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Session;

import edu.emory.library.tas.attrGroups.AbstractAttribute;
import edu.emory.library.tas.attrGroups.Attribute;
import edu.emory.library.tas.attrGroups.CompoundAttribute;
import edu.emory.library.tas.attrGroups.Group;
import edu.emory.library.tas.attrGroups.ObjectType;
import edu.emory.library.tas.dicts.Angola;
import edu.emory.library.tas.dicts.Carib;
import edu.emory.library.tas.dicts.DemBroadRegion;
import edu.emory.library.tas.dicts.DepDefinition;
import edu.emory.library.tas.dicts.DeparturePort;
import edu.emory.library.tas.dicts.DepartureRegion;
import edu.emory.library.tas.dicts.Editor;
import edu.emory.library.tas.dicts.EmbRegion;
import edu.emory.library.tas.dicts.Fate2;
import edu.emory.library.tas.dicts.Fate3;
import edu.emory.library.tas.dicts.Fate4;
import edu.emory.library.tas.dicts.Filter;
import edu.emory.library.tas.dicts.FirstDemPort;
import edu.emory.library.tas.dicts.Flag2;
import edu.emory.library.tas.dicts.Gc;
import edu.emory.library.tas.dicts.ImpPrincipalRegion;
import edu.emory.library.tas.dicts.ImputedNation;
import edu.emory.library.tas.dicts.IntDisembPort;
import edu.emory.library.tas.dicts.MajByIm;
import edu.emory.library.tas.dicts.Mjselrg2;
import edu.emory.library.tas.dicts.Natinim3;
import edu.emory.library.tas.dicts.Natinim4;
import edu.emory.library.tas.dicts.New;
import edu.emory.library.tas.dicts.Newamreg;
import edu.emory.library.tas.dicts.OldWorldPlace;
import edu.emory.library.tas.dicts.PrimaryLast;
import edu.emory.library.tas.dicts.PurchasePort;
import edu.emory.library.tas.dicts.RetPort;
import edu.emory.library.tas.dicts.Seafrica;
import edu.emory.library.tas.dicts.SecondDemPort;
import edu.emory.library.tas.dicts.ShipNationality;
import edu.emory.library.tas.dicts.Status;
import edu.emory.library.tas.dicts.Temp;
import edu.emory.library.tas.dicts.Temp2;
import edu.emory.library.tas.dicts.ThirdDemPort;
import edu.emory.library.tas.dicts.TonesType;
import edu.emory.library.tas.dicts.Us;
import edu.emory.library.tas.dicts.Userid;
import edu.emory.library.tas.dicts.VesselRig;
import edu.emory.library.tas.dicts.XmimpFlag;
import edu.emory.library.tas.dicts.Year100;
import edu.emory.library.tas.dicts.Year25;
import edu.emory.library.tas.dicts.Year5;
import edu.emory.library.tas.dicts.Yearches;
import edu.emory.library.tas.util.HibernateConnector;
import edu.emory.library.tas.util.HibernateUtil;
import edu.emory.library.tas.util.query.Conditions;
import edu.emory.library.tas.util.query.QueryValue;

public class Voyage extends AbstractDescriptiveObject {
	
	private static final String VOYAGE = "Voyage";

	/**
	 * Approved flag.
	 */
	private boolean approved = false;
	
	/**
	 * Field informing whether object's slaves were modified/unmodified.
	 */
	private int slavesModified = NOT_UPDATED;
	
	/**
	 * Creation flag. If true - object should be created in DB.
	 */
	private boolean created = false;
	
	/**
	 * Set of slaves.
	 */
	private Set slaves = new HashSet();
	
	public static Group[] getGroups() {
		
		Conditions conditions = new Conditions();
		conditions.addCondition("typeName", VOYAGE, Conditions.OP_EQUALS);
		QueryValue query = new QueryValue("ObjectType", conditions);
		Object[] types = (Object[]) query.executeQuery();
		ObjectType type = (ObjectType)types[0];
		
		conditions = new Conditions();
		conditions.addCondition("objectType", type, Conditions.OP_EQUALS);
		query = new QueryValue("Group", conditions);
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
		conditions.addCondition("typeName", VOYAGE, Conditions.OP_EQUALS);
		QueryValue query = new QueryValue("ObjectType", conditions);
		Object[] types = (Object[]) query.executeQuery();
		ObjectType type = (ObjectType)types[0];
		
		conditions = new Conditions();
		conditions.addCondition("objectType", type, Conditions.OP_EQUALS);
		query = new QueryValue("Attribute", conditions);
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
	
	public static String[] getAllAttrNames() {
		
		Conditions conditions = new Conditions();
		conditions.addCondition("typeName", VOYAGE, Conditions.OP_EQUALS);
		QueryValue query = new QueryValue("ObjectType", conditions);
		Object[] types = (Object[]) query.executeQuery();
		ObjectType type = (ObjectType)types[0];
		
		conditions = new Conditions();
		conditions.addCondition("objectType", type, Conditions.OP_EQUALS);
		query = new QueryValue("Attribute", conditions);
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
		conditions.addCondition("typeName", VOYAGE, Conditions.OP_EQUALS);
		QueryValue query = new QueryValue("ObjectType", conditions);
		Object[] types = (Object[]) query.executeQuery();
		ObjectType type = (ObjectType)types[0];
		
		conditions = new Conditions();
		conditions.addCondition("objectType", type, Conditions.OP_EQUALS);
		query = new QueryValue("CompoundAttribute", conditions);
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

//	/**
//	 * Gets a list of all valid attributes for object.
//	 * @return String[] with attribute names
//	 */
//	public static String[] getAllAttrNames() {
//		initTypes();
//		return (String[])types.keySet().toArray(new String[] {});
//	}
	
//	public static boolean isDictionary(String attrName) {
//		return ((SchemaColumn)types.get(attrName)).isDictinaory();
//	}
	
//	/*AUTOGENERATED BLOCK*/
//	private static Map types = null;
//	public static synchronized void initTypes()
//	{
//		if (types != null) return; 
//		types = new HashMap();
//
//		/** Static construction **/
//		types.put("voyageId", new SchemaColumn("voyageId", 1, null, 0, "voyageid", null, null, null, "Voyage ID", -1));
//		types.put("shipname", new SchemaColumn("shipname", 2, null, 1, null, null, null, null, "Ship name", 60));
////		types.put("revisionId", new SchemaColumn("revisionId", 1, null, -1, null, null, null, null, null, -1));
//		types.put("captaina", new SchemaColumn("captaina", 2, null, 1, null, null, null, null, null, 60));
//		types.put("captainb", new SchemaColumn("captainb", 2, null, 1, null, null, null, null, null, 40));
//		types.put("captainc", new SchemaColumn("captainc", 2, null, 1, null, null, null, null, null, 40));
//		types.put("datedep", new SchemaColumn("datedep", 3, null, 2, null, "datedepa", "datedepb", "datedepc", null, -1));
//		types.put("nppretra", new SchemaColumn("nppretra", 0, null, 0, null, null, null, null, null, -1));
//		types.put("d1slatr", new SchemaColumn("d1slatr", 3, null, 2, null, "d1slatra", "d1slatrb", "d1slatrc", null, -1));
//		types.put("npprior", new SchemaColumn("npprior", 0, null, 0, null, null, null, null, null, -1));
//		types.put("tslavesp", new SchemaColumn("tslavesp", 0, null, 0, null, null, null, null, null, -1));
//		types.put("tslavesd", new SchemaColumn("tslavesd", 0, null, 0, null, null, null, null, null, -1));
//		types.put("sladvoy", new SchemaColumn("sladvoy", 0, null, 0, null, null, null, null, null, -1));
//		types.put("slaarriv", new SchemaColumn("slaarriv", 0, null, 0, null, null, null, null, null, -1));
//		types.put("rrdata31", new SchemaColumn("rrdata31", 3, null, 2, null, "datarr32", "datarr33", "datarr34", null, -1));
//		types.put("slas32", new SchemaColumn("slas32", 0, null, 0, null, null, null, null, null, -1));
//		types.put("datarr32", new SchemaColumn("datarr32", 3, null, 2, null, "datarr36", "datarr37", "datarr34", null, -1));
//		types.put("slas36", new SchemaColumn("slas36", 0, null, 0, null, null, null, null, null, -1));
//		types.put("datarr33", new SchemaColumn("datarr33", 3, null, 2, null, "datarr39", "datarr40", "datarr34", null, -1));
//		types.put("slas39", new SchemaColumn("slas39", 0, null, 0, null, null, null, null, null, -1));
//		types.put("ddepamb", new SchemaColumn("ddepamb", 3, null, 2, null, "ddepam", "ddepamb", "ddepamc", null, -1));
//		types.put("datarr4", new SchemaColumn("datarr4", 3, null, 2, null, "datarr43", "datarr44", "datarr45", null, -1));
//		types.put("fate", new SchemaColumn("fate", 0, null, 0, null, null, null, null, null, -1));
//		types.put("sourcea", new SchemaColumn("sourcea", 2, null, 1, null, null, null, null, null, 60));
//		types.put("sourceb", new SchemaColumn("sourceb", 2, null, 1, null, null, null, null, null, 50));
//		types.put("sourcec", new SchemaColumn("sourcec", 2, null, 1, null, null, null, null, null, 50));
//		types.put("sourced", new SchemaColumn("sourced", 2, null, 1, null, null, null, null, null, 50));
//		types.put("sourcee", new SchemaColumn("sourcee", 2, null, 1, null, null, null, null, null, 50));
//		types.put("sourcef", new SchemaColumn("sourcef", 2, null, 1, null, null, null, null, null, 50));
//		types.put("sourceg", new SchemaColumn("sourceg", 2, null, 1, null, null, null, null, null, 50));
//		types.put("sourceh", new SchemaColumn("sourceh", 2, null, 1, null, null, null, null, null, 50));
//		types.put("sourcei", new SchemaColumn("sourcei", 2, null, 1, null, null, null, null, null, 50));
//		types.put("sourcej", new SchemaColumn("sourcej", 2, null, 1, null, null, null, null, null, 50));
//		types.put("sourcek", new SchemaColumn("sourcek", 2, null, 1, null, null, null, null, null, 50));
//		types.put("sourcel", new SchemaColumn("sourcel", 2, null, 1, null, null, null, null, null, 40));
//		types.put("sourcem", new SchemaColumn("sourcem", 2, null, 1, null, null, null, null, null, 40));
//		types.put("sourcen", new SchemaColumn("sourcen", 2, null, 1, null, null, null, null, null, 40));
//		types.put("sourceo", new SchemaColumn("sourceo", 2, null, 1, null, null, null, null, null, 40));
//		types.put("sourcep", new SchemaColumn("sourcep", 2, null, 1, null, null, null, null, null, 40));
//		types.put("sourceq", new SchemaColumn("sourceq", 2, null, 1, null, null, null, null, null, 40));
//		types.put("sourcer", new SchemaColumn("sourcer", 2, null, 1, null, null, null, null, null, 40));
//		types.put("slintend", new SchemaColumn("slintend", 0, null, 0, null, null, null, null, null, -1));
//		types.put("tonnage", new SchemaColumn("tonnage", 0, null, 0, null, null, null, null, null, -1));
//		types.put("crewdied", new SchemaColumn("crewdied", 0, null, 0, null, null, null, null, null, -1));
//		types.put("ncar13", new SchemaColumn("ncar13", 0, null, 0, null, null, null, null, null, -1));
//		types.put("ncar15", new SchemaColumn("ncar15", 0, null, 0, null, null, null, null, null, -1));
//		types.put("ncar17", new SchemaColumn("ncar17", 0, null, 0, null, null, null, null, null, -1));
//		types.put("sladamer", new SchemaColumn("sladamer", 0, null, 0, null, null, null, null, null, -1));
//		types.put("saild1", new SchemaColumn("saild1", 0, null, 0, null, null, null, null, null, -1));
//		types.put("saild2", new SchemaColumn("saild2", 0, null, 0, null, null, null, null, null, -1));
//		types.put("saild3", new SchemaColumn("saild3", 0, null, 0, null, null, null, null, null, -1));
//		types.put("saild4", new SchemaColumn("saild4", 0, null, 0, null, null, null, null, null, -1));
//		types.put("saild5", new SchemaColumn("saild5", 0, null, 0, null, null, null, null, null, -1));
//		types.put("ndesert", new SchemaColumn("ndesert", 0, null, 0, null, null, null, null, null, -1));
//		types.put("slinten2", new SchemaColumn("slinten2", 0, null, 0, null, null, null, null, null, -1));
//		types.put("guns", new SchemaColumn("guns", 0, null, 0, null, null, null, null, null, -1));
//		types.put("voyage", new SchemaColumn("voyage", 0, null, 0, null, null, null, null, null, -1));
//		types.put("crew1", new SchemaColumn("crew1", 0, null, 0, null, null, null, null, null, -1));
//		types.put("child2", new SchemaColumn("child2", 0, null, 0, null, null, null, null, null, -1));
//		types.put("child3", new SchemaColumn("child3", 0, null, 0, null, null, null, null, null, -1));
//		types.put("yrreg", new SchemaColumn("yrreg", 0, null, 0, null, null, null, null, null, -1));
//		types.put("crew3", new SchemaColumn("crew3", 0, null, 0, null, null, null, null, null, -1));
//		types.put("crew4", new SchemaColumn("crew4", 0, null, 0, null, null, null, null, null, -1));
//		types.put("crew5", new SchemaColumn("crew5", 0, null, 0, null, null, null, null, null, -1));
//		types.put("adult1", new SchemaColumn("adult1", 0, null, 0, null, null, null, null, null, -1));
//		types.put("child1", new SchemaColumn("child1", 0, null, 0, null, null, null, null, null, -1));
//		types.put("female1", new SchemaColumn("female1", 0, null, 0, null, null, null, null, null, -1));
//		types.put("male1", new SchemaColumn("male1", 0, null, 0, null, null, null, null, null, -1));
//		types.put("men1", new SchemaColumn("men1", 0, null, 0, null, null, null, null, null, -1));
//		types.put("women1", new SchemaColumn("women1", 0, null, 0, null, null, null, null, null, -1));
//		types.put("female2", new SchemaColumn("female2", 0, null, 0, null, null, null, null, null, -1));
//		types.put("male2", new SchemaColumn("male2", 0, null, 0, null, null, null, null, null, -1));
//		types.put("men2", new SchemaColumn("men2", 0, null, 0, null, null, null, null, null, -1));
//		types.put("women2", new SchemaColumn("women2", 0, null, 0, null, null, null, null, null, -1));
//		types.put("boy2", new SchemaColumn("boy2", 0, null, 0, null, null, null, null, null, -1));
//		types.put("girl2", new SchemaColumn("girl2", 0, null, 0, null, null, null, null, null, -1));
//		types.put("female3", new SchemaColumn("female3", 0, null, 0, null, null, null, null, null, -1));
//		types.put("male3", new SchemaColumn("male3", 0, null, 0, null, null, null, null, null, -1));
//		types.put("men3", new SchemaColumn("men3", 0, null, 0, null, null, null, null, null, -1));
//		types.put("women3", new SchemaColumn("women3", 0, null, 0, null, null, null, null, null, -1));
//		types.put("boy3", new SchemaColumn("boy3", 0, null, 0, null, null, null, null, null, -1));
//		types.put("girl3", new SchemaColumn("girl3", 0, null, 0, null, null, null, null, null, -1));
//		types.put("female4", new SchemaColumn("female4", 0, null, 0, null, null, null, null, null, -1));
//		types.put("male4", new SchemaColumn("male4", 0, null, 0, null, null, null, null, null, -1));
//		types.put("men4", new SchemaColumn("men4", 0, null, 0, null, null, null, null, null, -1));
//		types.put("women4", new SchemaColumn("women4", 0, null, 0, null, null, null, null, null, -1));
//		types.put("boy4", new SchemaColumn("boy4", 0, null, 0, null, null, null, null, null, -1));
//		types.put("girl4", new SchemaColumn("girl4", 0, null, 0, null, null, null, null, null, -1));
//		types.put("child4", new SchemaColumn("child4", 0, null, 0, null, null, null, null, null, -1));
//		types.put("female6", new SchemaColumn("female6", 0, null, 0, null, null, null, null, null, -1));
//		types.put("male6", new SchemaColumn("male6", 0, null, 0, null, null, null, null, null, -1));
//		types.put("men6", new SchemaColumn("men6", 0, null, 0, null, null, null, null, null, -1));
//		types.put("women6", new SchemaColumn("women6", 0, null, 0, null, null, null, null, null, -1));
//		types.put("boy6", new SchemaColumn("boy6", 0, null, 0, null, null, null, null, null, -1));
//		types.put("girl6", new SchemaColumn("girl6", 0, null, 0, null, null, null, null, null, -1));
//		types.put("child6", new SchemaColumn("child6", 0, null, 0, null, null, null, null, null, -1));
//		types.put("crew2", new SchemaColumn("crew2", 0, null, 0, null, null, null, null, null, -1));
//		types.put("infantm3", new SchemaColumn("infantm3", 0, null, 0, null, null, null, null, null, -1));
//		types.put("infantf3", new SchemaColumn("infantf3", 0, null, 0, null, null, null, null, null, -1));
//		types.put("sladafri", new SchemaColumn("sladafri", 0, null, 0, null, null, null, null, null, -1));
//		types.put("sladied5", new SchemaColumn("sladied5", 0, null, 0, null, null, null, null, null, -1));
//		types.put("sladied4", new SchemaColumn("sladied4", 0, null, 0, null, null, null, null, null, -1));
//		types.put("sladied2", new SchemaColumn("sladied2", 0, null, 0, null, null, null, null, null, -1));
//		types.put("sladied1", new SchemaColumn("sladied1", 0, null, 0, null, null, null, null, null, -1));
//		types.put("sladied3", new SchemaColumn("sladied3", 0, null, 0, null, null, null, null, null, -1));
//		types.put("sladied6", new SchemaColumn("sladied6", 0, null, 0, null, null, null, null, null, -1));
//		types.put("insurrec", new SchemaColumn("insurrec", 0, null, 0, null, null, null, null, null, -1));
//		types.put("adult3", new SchemaColumn("adult3", 0, null, 0, null, null, null, null, null, -1));
//		types.put("ownera", new SchemaColumn("ownera", 2, null, 1, null, null, null, null, null, 60));
//		types.put("ownerb", new SchemaColumn("ownerb", 2, null, 1, null, null, null, null, null, 40));
//		types.put("ownerc", new SchemaColumn("ownerc", 2, null, 1, null, null, null, null, null, 40));
//		types.put("ownerd", new SchemaColumn("ownerd", 2, null, 1, null, null, null, null, null, 40));
//		types.put("ownere", new SchemaColumn("ownere", 2, null, 1, null, null, null, null, null, 40));
//		types.put("ownerf", new SchemaColumn("ownerf", 2, null, 1, null, null, null, null, null, 40));
//		types.put("ownerg", new SchemaColumn("ownerg", 2, null, 1, null, null, null, null, null, 40));
//		types.put("ownerh", new SchemaColumn("ownerh", 2, null, 1, null, null, null, null, null, 40));
//		types.put("owneri", new SchemaColumn("owneri", 2, null, 1, null, null, null, null, null, 40));
//		types.put("ownerj", new SchemaColumn("ownerj", 2, null, 1, null, null, null, null, null, 40));
//		types.put("ownerk", new SchemaColumn("ownerk", 2, null, 1, null, null, null, null, null, 40));
//		types.put("ownerl", new SchemaColumn("ownerl", 2, null, 1, null, null, null, null, null, 40));
//		types.put("ownerm", new SchemaColumn("ownerm", 2, null, 1, null, null, null, null, null, 40));
//		types.put("ownern", new SchemaColumn("ownern", 2, null, 1, null, null, null, null, null, 40));
//		types.put("ownero", new SchemaColumn("ownero", 2, null, 1, null, null, null, null, null, 40));
//		types.put("ownerp", new SchemaColumn("ownerp", 2, null, 1, null, null, null, null, null, 40));
//		types.put("yearaf", new SchemaColumn("yearaf", 0, null, 0, null, null, null, null, null, -1));
//		types.put("yeardep", new SchemaColumn("yeardep", 0, null, 0, null, null, null, null, null, -1));
//		types.put("yearam", new SchemaColumn("yearam", 0, null, 0, null, null, null, null, null, -1));
//		types.put("tonmod", new SchemaColumn("tonmod", 5, null, 0, null, null, null, null, null, -1));
//		types.put("vymrtimp", new SchemaColumn("vymrtimp", 0, null, 0, null, null, null, null, null, -1));
//		types.put("tslmtimp", new SchemaColumn("tslmtimp", 0, null, 0, null, null, null, null, null, -1));
//		types.put("sla32imp", new SchemaColumn("sla32imp", 5, null, 0, null, null, null, null, null, -1));
//		types.put("sla36imp", new SchemaColumn("sla36imp", 5, null, 0, null, null, null, null, null, -1));
//		types.put("imprat", new SchemaColumn("imprat", 5, null, 0, null, null, null, null, null, -1));
//		types.put("regdis11", new SchemaColumn("regdis11", 0, null, 0, null, null, null, null, null, -1));
//		types.put("regdis21", new SchemaColumn("regdis21", 0, null, 0, null, null, null, null, null, -1));
//		types.put("sla39imp", new SchemaColumn("sla39imp", 0, null, 0, null, null, null, null, null, -1));
//		types.put("ncr15imp", new SchemaColumn("ncr15imp", 5, null, 0, null, null, null, null, null, -1));
//		types.put("ncr17imp", new SchemaColumn("ncr17imp", 0, null, 0, null, null, null, null, null, -1));
//		types.put("exprat", new SchemaColumn("exprat", 5, null, 0, null, null, null, null, null, -1));
//		types.put("male1imp", new SchemaColumn("male1imp", 0, null, 0, null, null, null, null, null, -1));
//		types.put("feml1imp", new SchemaColumn("feml1imp", 0, null, 0, null, null, null, null, null, -1));
//		types.put("chil1imp", new SchemaColumn("chil1imp", 0, null, 0, null, null, null, null, null, -1));
//		types.put("malrat1", new SchemaColumn("malrat1", 5, null, 0, null, null, null, null, null, -1));
//		types.put("chilrat1", new SchemaColumn("chilrat1", 5, null, 0, null, null, null, null, null, -1));
//		types.put("slavemx1", new SchemaColumn("slavemx1", 0, null, 0, null, null, null, null, null, -1));
//		types.put("slavema1", new SchemaColumn("slavema1", 0, null, 0, null, null, null, null, null, -1));
//		types.put("male3imp", new SchemaColumn("male3imp", 0, null, 0, null, null, null, null, null, -1));
//		types.put("feml3imp", new SchemaColumn("feml3imp", 0, null, 0, null, null, null, null, null, -1));
//		types.put("chil3imp", new SchemaColumn("chil3imp", 0, null, 0, null, null, null, null, null, -1));
//		types.put("chilrat3", new SchemaColumn("chilrat3", 5, null, 0, null, null, null, null, null, -1));
//		types.put("malrat3", new SchemaColumn("malrat3", 5, null, 0, null, null, null, null, null, -1));
//		types.put("slavemx3", new SchemaColumn("slavemx3", 0, null, 0, null, null, null, null, null, -1));
//		types.put("slavema3", new SchemaColumn("slavema3", 0, null, 0, null, null, null, null, null, -1));
//		types.put("adlt3imp", new SchemaColumn("adlt3imp", 0, null, 0, null, null, null, null, null, -1));
//		types.put("vymrtrat", new SchemaColumn("vymrtrat", 5, null, 0, null, null, null, null, null, -1));
//		types.put("slaximp", new SchemaColumn("slaximp", 5, null, 0, null, null, null, null, null, -1));
//		types.put("slamimp", new SchemaColumn("slamimp", 5, null, 0, null, null, null, null, null, -1));
//		types.put("voy2imp", new SchemaColumn("voy2imp", 0, null, 0, null, null, null, null, null, -1));
//		types.put("voy1imp", new SchemaColumn("voy1imp", 0, null, 0, null, null, null, null, null, -1));
//		types.put("malrat7", new SchemaColumn("malrat7", 5, null, 0, null, null, null, null, null, -1));
//		types.put("chilrat7", new SchemaColumn("chilrat7", 5, null, 0, null, null, null, null, null, -1));
//		types.put("sourcex", new SchemaColumn("sourcex", 2, null, 1, null, null, null, null, null, 254));
//		types.put("p", new SchemaColumn("p", 0, null, 0, null, null, null, null, null, -1));
//		types.put("men8", new SchemaColumn("men8", 0, null, 0, null, null, null, null, null, -1));
//		types.put("women8", new SchemaColumn("women8", 0, null, 0, null, null, null, null, null, -1));
//		types.put("boy8", new SchemaColumn("boy8", 0, null, 0, null, null, null, null, null, -1));
//		types.put("girl8", new SchemaColumn("girl8", 0, null, 0, null, null, null, null, null, -1));
//		types.put("behrflag", new SchemaColumn("behrflag", 0, null, 0, null, null, null, null, null, -1));
//		types.put("price", new SchemaColumn("price", 0, null, 0, null, null, null, null, null, -1));
//		types.put("uppguine", new SchemaColumn("uppguine", 5, null, 0, null, null, null, null, null, -1));
//		types.put("voyagimp", new SchemaColumn("voyagimp", 5, null, 0, null, null, null, null, null, -1));
//		types.put("timcoast", new SchemaColumn("timcoast", 5, null, 0, null, null, null, null, null, -1));
//		types.put("slavpday", new SchemaColumn("slavpday", 5, null, 0, null, null, null, null, null, -1));
//		types.put("rountrip", new SchemaColumn("rountrip", 5, null, 0, null, null, null, null, null, -1));
//		types.put("slavpvoy", new SchemaColumn("slavpvoy", 5, null, 0, null, null, null, null, null, -1));
//		types.put("slxp1000", new SchemaColumn("slxp1000", 5, null, 0, null, null, null, null, null, -1));
//		types.put("insurre2", new SchemaColumn("insurre2", 5, null, 0, null, null, null, null, null, -1));
//		types.put("timslave", new SchemaColumn("timslave", 5, null, 0, null, null, null, null, null, -1));
//		types.put("toncateg", new SchemaColumn("toncateg", 0, null, 0, null, null, null, null, null, -1));
//		types.put("evgreen", new SchemaColumn("evgreen", 0, null, 0, null, null, null, null, null, -1));
//		types.put("womrat1", new SchemaColumn("womrat1", 5, null, 0, null, null, null, null, null, -1));
//		types.put("womrat3", new SchemaColumn("womrat3", 5, null, 0, null, null, null, null, null, -1));
//		types.put("womrat7", new SchemaColumn("womrat7", 5, null, 0, null, null, null, null, null, -1));
//		types.put("menrat7", new SchemaColumn("menrat7", 5, null, 0, null, null, null, null, null, -1));
//		types.put("menrat3", new SchemaColumn("menrat3", 5, null, 0, null, null, null, null, null, -1));
//		types.put("menrat1", new SchemaColumn("menrat1", 5, null, 0, null, null, null, null, null, -1));
//		types.put("girlrat1", new SchemaColumn("girlrat1", 5, null, 0, null, null, null, null, null, -1));
//		types.put("girlrat3", new SchemaColumn("girlrat3", 5, null, 0, null, null, null, null, null, -1));
//		types.put("girlrat7", new SchemaColumn("girlrat7", 5, null, 0, null, null, null, null, null, -1));
//		types.put("boyrat7", new SchemaColumn("boyrat7", 5, null, 0, null, null, null, null, null, -1));
//		types.put("boyrat3", new SchemaColumn("boyrat3", 5, null, 0, null, null, null, null, null, -1));
//		types.put("boyrat1", new SchemaColumn("boyrat1", 5, null, 0, null, null, null, null, null, -1));
//		types.put("behmimp2", new SchemaColumn("behmimp2", 0, null, 0, null, null, null, null, null, -1));
//		types.put("behximp2", new SchemaColumn("behximp2", 5, null, 0, null, null, null, null, null, -1));
//		types.put("poundprice", new SchemaColumn("poundprice", 5, null, 0, "__price", null, null, null, null, -1));
//		types.put("termmth", new SchemaColumn("termmth", 0, null, 0, null, null, null, null, null, -1));
//		types.put("intrate", new SchemaColumn("intrate", 0, null, 0, null, null, null, null, null, -1));
//		types.put("cashpric", new SchemaColumn("cashpric", 5, null, 0, null, null, null, null, null, -1));
//		types.put("jamcaspr", new SchemaColumn("jamcaspr", 5, null, 0, null, null, null, null, null, -1));
//		types.put("locurmnl", new SchemaColumn("locurmnl", 5, null, 0, null, null, null, null, null, -1));
//		types.put("exchrate", new SchemaColumn("exchrate", 2, null, 1, null, null, null, null, null, 10));
//		types.put("frencpri", new SchemaColumn("frencpri", 5, null, 0, null, null, null, null, null, -1));
//		types.put("frnprinl", new SchemaColumn("frnprinl", 5, null, 0, null, null, null, null, null, -1));
//		types.put("nonpay", new SchemaColumn("nonpay", 5, null, 0, null, null, null, null, null, -1));
//		types.put("seneg", new SchemaColumn("seneg", 5, null, 0, null, null, null, null, null, -1));
//		types.put("sl", new SchemaColumn("sl", 5, null, 0, null, null, null, null, null, -1));
//		types.put("wwc", new SchemaColumn("wwc", 5, null, 0, null, null, null, null, null, -1));
//		types.put("slperton", new SchemaColumn("slperton", 5, null, 0, null, null, null, null, null, -1));
//		types.put("tonpercr", new SchemaColumn("tonpercr", 5, null, 0, null, null, null, null, null, -1));
//		types.put("portugus", new SchemaColumn("portugus", 5, null, 0, null, null, null, null, null, -1));
//		types.put("manol", new SchemaColumn("manol", 0, null, 0, null, null, null, null, null, -1));
//		types.put("portug2", new SchemaColumn("portug2", 5, null, 0, null, null, null, null, null, -1));
//		types.put("deptamer", new SchemaColumn("deptamer", 5, null, 0, null, null, null, null, null, -1));
//		types.put("girl5", new SchemaColumn("girl5", 0, null, 0, null, null, null, null, null, -1));
//		types.put("boy5", new SchemaColumn("boy5", 0, null, 0, null, null, null, null, null, -1));
//		types.put("men5", new SchemaColumn("men5", 0, null, 0, null, null, null, null, null, -1));
//		types.put("women5", new SchemaColumn("women5", 0, null, 0, null, null, null, null, null, -1));
//		types.put("child5", new SchemaColumn("child5", 0, null, 0, null, null, null, null, null, -1));
//		types.put("male5", new SchemaColumn("male5", 0, null, 0, null, null, null, null, null, -1));
//		types.put("female5", new SchemaColumn("female5", 0, null, 0, null, null, null, null, null, -1));
//		types.put("arrport2", new SchemaColumn("arrport2", 0, null, 0, null, null, null, null, null, -1));
//		types.put("nsp", new SchemaColumn("nsp", 0, null, 0, null, null, null, null, null, -1));
//		types.put("nna", new SchemaColumn("nna", 0, null, 0, null, null, null, null, null, -1));
//		types.put("infant3", new SchemaColumn("infant3", 0, null, 0, null, null, null, null, null, -1));
//		types.put("infants3", new SchemaColumn("infants3", 0, null, 0, null, null, null, null, null, -1));
//		types.put("infant1", new SchemaColumn("infant1", 0, null, 0, null, null, null, null, null, -1));
//		types.put("kleimerg", new SchemaColumn("kleimerg", 5, null, 0, null, null, null, null, null, -1));
//		types.put("klemerg2", new SchemaColumn("klemerg2", 5, null, 0, null, null, null, null, null, -1));
//		types.put("adult5", new SchemaColumn("adult5", 0, null, 0, null, null, null, null, null, -1));
//		types.put("bahijelm", new SchemaColumn("bahijelm", 5, null, 0, null, null, null, null, null, -1));
//		types.put("adult2", new SchemaColumn("adult2", 0, null, 0, null, null, null, null, null, -1));
//		types.put("adult4", new SchemaColumn("adult4", 0, null, 0, null, null, null, null, null, -1));
//		types.put("infant4", new SchemaColumn("infant4", 0, null, 0, null, null, null, null, null, -1));
//		types.put("voyagid2", new SchemaColumn("voyagid2", 5, null, 0, null, null, null, null, null, -1));
//		types.put("newmina", new SchemaColumn("newmina", 0, null, 0, null, null, null, null, null, -1));
//		types.put("crew", new SchemaColumn("crew", 0, null, 0, null, null, null, null, null, -1));
//		types.put("guns2", new SchemaColumn("guns2", 0, null, 0, null, null, null, null, null, -1));
//		types.put("men7", new SchemaColumn("men7", 0, null, 0, null, null, null, null, null, -1));
//		types.put("male7", new SchemaColumn("male7", 0, null, 0, null, null, null, null, null, -1));
//		types.put("female7", new SchemaColumn("female7", 0, null, 0, null, null, null, null, null, -1));
//		types.put("women7", new SchemaColumn("women7", 0, null, 0, null, null, null, null, null, -1));
//		types.put("adult7", new SchemaColumn("adult7", 0, null, 0, null, null, null, null, null, -1));
//		types.put("boy7", new SchemaColumn("boy7", 0, null, 0, null, null, null, null, null, -1));
//		types.put("girl7", new SchemaColumn("girl7", 0, null, 0, null, null, null, null, null, -1));
//		types.put("child7", new SchemaColumn("child7", 0, null, 0, null, null, null, null, null, -1));
//		types.put("voy3imp", new SchemaColumn("voy3imp", 0, null, 0, null, null, null, null, null, -1));
//		types.put("slavcapt", new SchemaColumn("slavcapt", 5, null, 0, null, null, null, null, null, -1));
//		types.put("majbycon", new SchemaColumn("majbycon", 5, null, 0, null, null, null, null, null, -1));
//		types.put("tabexpo", new SchemaColumn("tabexpo", 0, null, 0, null, null, null, null, null, -1));
//		types.put("rice", new SchemaColumn("rice", 5, null, 0, null, null, null, null, null, -1));
//		types.put("slavemx7", new SchemaColumn("slavemx7", 5, null, 0, null, null, null, null, null, -1));
//		types.put("spanishamer", new SchemaColumn("spanishamer", 5, null, 0, null, null, null, null, null, -1));
//		types.put("year25a", new SchemaColumn("year25a", 5, null, 0, null, null, null, null, null, -1));
//		types.put("menvymrtrat", new SchemaColumn("menvymrtrat", 5, null, 0, null, null, null, null, null, -1));
//		types.put("womenvymrtrat", new SchemaColumn("womenvymrtrat", 5, null, 0, null, null, null, null, null, -1));
//		types.put("boyvymrtrat", new SchemaColumn("boyvymrtrat", 5, null, 0, null, null, null, null, null, -1));
//		types.put("girlvymrtrat", new SchemaColumn("girlvymrtrat", 5, null, 0, null, null, null, null, null, -1));
//		types.put("cubaperiod", new SchemaColumn("cubaperiod", 5, null, 0, null, null, null, null, null, -1));
//		types.put("datepl", new SchemaColumn("datepl", 4, "DepDefinition", 0, null, null, null, null, null, -1));
//		types.put("embport", new SchemaColumn("embport", 4, "PurchasePort", 0, null, null, null, null, null, -1));
//		types.put("arrport", new SchemaColumn("arrport", 4, "IntDisembPort", 0, null, null, null, null, null, -1));
//		types.put("plac1tra", new SchemaColumn("plac1tra", 4, "PurchasePort", 0, null, null, null, null, null, -1));
//		types.put("destin", new SchemaColumn("destin", 4, "RetPort", 0, null, null, null, null, null, -1));
//		types.put("plac2tra", new SchemaColumn("plac2tra", 4, "PurchasePort", 0, null, null, null, null, null, -1));
//		types.put("plac3tra", new SchemaColumn("plac3tra", 4, "PurchasePort", 0, null, null, null, null, null, -1));
//		types.put("npafttra", new SchemaColumn("npafttra", 4, "OldWorldPlace", 0, null, null, null, null, null, -1));
//		types.put("sla1port", new SchemaColumn("sla1port", 4, "FirstDemPort", 0, null, null, null, null, null, -1));
//		types.put("adpsale1", new SchemaColumn("adpsale1", 4, "SecondDemPort", 0, null, null, null, null, null, -1));
//		types.put("adpsale2", new SchemaColumn("adpsale2", 4, "ThirdDemPort", 0, null, null, null, null, null, -1));
//		types.put("portret", new SchemaColumn("portret", 4, "RetPort", 0, null, null, null, null, null, -1));
//		types.put("tontype", new SchemaColumn("tontype", 4, "TonesType", 0, null, null, null, null, null, -1));
//		types.put("national", new SchemaColumn("national", 4, "ShipNationality", 0, null, null, null, null, null, -1));
//		types.put("embport2", new SchemaColumn("embport2", 4, "PurchasePort", 0, null, null, null, null, null, -1));
//		types.put("portdep", new SchemaColumn("portdep", 4, "DeparturePort", 0, null, null, null, null, null, -1));
//		types.put("rig", new SchemaColumn("rig", 4, "VesselRig", 0, null, null, null, null, null, -1));
//		types.put("placcons", new SchemaColumn("placcons", 4, "RetPort", 0, null, null, null, null, null, -1));
//		types.put("majselpt", new SchemaColumn("majselpt", 4, "FirstDemPort", 0, null, null, null, null, null, -1));
//		types.put("majbuypt", new SchemaColumn("majbuypt", 4, "PurchasePort", 0, null, null, null, null, null, -1));
//		types.put("natinimp", new SchemaColumn("natinimp", 4, "ImputedNation", 0, null, null, null, null, null, -1));
//		types.put("deptreg", new SchemaColumn("deptreg", 4, "DepartureRegion", 0, null, null, null, null, null, -1));
//		types.put("retrnreg", new SchemaColumn("retrnreg", 4, "DepartureRegion", 0, null, null, null, null, null, -1));
//		types.put("regisreg", new SchemaColumn("regisreg", 4, "DepartureRegion", 0, null, null, null, null, null, -1));
//		types.put("year100", new SchemaColumn("year100", 4, "Year100", 0, null, null, null, null, null, -1));
//		types.put("year5", new SchemaColumn("year5", 4, "Year5", 0, null, null, null, null, null, -1));
//		types.put("year25", new SchemaColumn("year25", 4, "Year25", 0, null, null, null, null, null, -1));
//		types.put("regem1", new SchemaColumn("regem1", 4, "EmbRegion", 0, null, null, null, null, null, -1));
//		types.put("regem2", new SchemaColumn("regem2", 4, "EmbRegion", 0, null, null, null, null, null, -1));
//		types.put("regem3", new SchemaColumn("regem3", 4, "EmbRegion", 0, null, null, null, null, null, -1));
//		types.put("embreg", new SchemaColumn("embreg", 4, "EmbRegion", 0, null, null, null, null, null, -1));
//		types.put("embreg2", new SchemaColumn("embreg2", 4, "EmbRegion", 0, null, null, null, null, null, -1));
//		types.put("majbuyrg", new SchemaColumn("majbuyrg", 4, "EmbRegion", 0, null, null, null, null, null, -1));
//		types.put("majbyimp", new SchemaColumn("majbyimp", 4, "EmbRegion", 0, null, null, null, null, null, -1));
//		types.put("regdis1", new SchemaColumn("regdis1", 4, "DepartureRegion", 0, null, null, null, null, null, -1));
//		types.put("regdis2", new SchemaColumn("regdis2", 4, "DepartureRegion", 0, null, null, null, null, null, -1));
//		types.put("regdis3", new SchemaColumn("regdis3", 4, "DepartureRegion", 0, null, null, null, null, null, -1));
//		types.put("regarrp", new SchemaColumn("regarrp", 4, "DepartureRegion", 0, null, null, null, null, null, -1));
//		types.put("majselrg", new SchemaColumn("majselrg", 4, "DepartureRegion", 0, null, null, null, null, null, -1));
//		types.put("fate2", new SchemaColumn("fate2", 4, "Fate2", 0, null, null, null, null, null, -1));
//		types.put("fate3", new SchemaColumn("fate3", 4, "Fate3", 0, null, null, null, null, null, -1));
//		types.put("fate4", new SchemaColumn("fate4", 4, "Fate4", 0, null, null, null, null, null, -1));
//		types.put("mjselimp", new SchemaColumn("mjselimp", 4, "ImpPrincipalRegion", 0, null, null, null, null, null, -1));
//		types.put("deptreg1", new SchemaColumn("deptreg1", 4, "DepartureRegion", 0, null, null, null, null, null, -1));
//		types.put("mjselrg1", new SchemaColumn("mjselrg1", 4, "DemBroadRegion", 0, null, null, null, null, null, -1));
//		types.put("constreg", new SchemaColumn("constreg", 4, "DepartureRegion", 0, null, null, null, null, null, -1));
//		types.put("status", new SchemaColumn("status", 4, "Status", 0, null, null, null, null, null, -1));
//		types.put("majbyim3", new SchemaColumn("majbyim3", 4, "MajByIm", 0, null, null, null, null, null, -1));
//		types.put("editor", new SchemaColumn("editor", 4, "Editor", 0, null, null, null, null, null, -1));
//		types.put("newf", new SchemaColumn("newf", 4, "New", 0, "new", null, null, null, null, -1));
//		types.put("filter_$", new SchemaColumn("filter_$", 4, "Filter", 0, "filter__", null, null, null, null, -1));
//		types.put("natinim3", new SchemaColumn("natinim3", 4, "Natinim3", 0, null, null, null, null, null, -1));
//		types.put("mjselrg2", new SchemaColumn("mjselrg2", 4, "Mjselrg2", 0, null, null, null, null, null, -1));
//		types.put("newamreg", new SchemaColumn("newamreg", 4, "Newamreg", 0, null, null, null, null, null, -1));
//		types.put("yearches", new SchemaColumn("yearches", 4, "Yearches", 0, null, null, null, null, null, -1));
//		types.put("gc", new SchemaColumn("gc", 4, "Gc", 0, null, null, null, null, null, -1));
//		types.put("angola", new SchemaColumn("angola", 4, "Angola", 0, null, null, null, null, null, -1));
//		types.put("seafrica", new SchemaColumn("seafrica", 4, "Seafrica", 0, null, null, null, null, null, -1));
//		types.put("regarrp2", new SchemaColumn("regarrp2", 4, "DepartureRegion", 0, null, null, null, null, null, -1));
//		types.put("userid", new SchemaColumn("userid", 4, "Userid", 0, null, null, null, null, null, -1));
//		types.put("xmimpflag", new SchemaColumn("xmimpflag", 4, "XmimpFlag", 0, null, null, null, null, null, -1));
//		types.put("temp2", new SchemaColumn("temp2", 4, "Temp2", 0, null, null, null, null, null, -1));
//		types.put("flag2", new SchemaColumn("flag2", 4, "Flag2", 0, null, null, null, null, null, -1));
//		types.put("primarylast", new SchemaColumn("primarylast", 4, "PrimaryLast", 0, null, null, null, null, null, -1));
//		types.put("primarylast1", new SchemaColumn("primarylast1", 4, "PrimaryLast", 0, null, null, null, null, null, -1));
//		types.put("carib", new SchemaColumn("carib", 4, "Carib", 0, null, null, null, null, null, -1));
//		types.put("natinim4", new SchemaColumn("natinim4", 4, "Natinim4", 0, null, null, null, null, null, -1));
//		types.put("us", new SchemaColumn("us", 4, "Us", 0, null, null, null, null, null, -1));
//		types.put("mjselim2", new SchemaColumn("mjselim2", 4, "DepartureRegion", 0, null, null, null, null, null, -1));
//		types.put("temp", new SchemaColumn("temp", 4, "Temp", 0, null, null, null, null, null, -1));
//	}
//	/*END AUTOGENERATED*/
	
	private Long iid;
	
	/**
	 * Creates new Voyage. Object will have new ID.
	 */
	public Voyage() {
		this.created = false;
	}
	
	/**
	 * Creates new Voyage.
	 * @param p_created
	 */
	private Voyage(boolean p_created) {
		this.created = p_created;
	}
	
	/**
	 * Creates voyage with given ID.
	 * @param voyageId desired voyage ID.
	 * @return	Created Voyage object
	 */
	public static Voyage createNew(Long voyageId) {
		Voyage voyage = new Voyage(true);
		voyage.setVoyageId(voyageId);
		return voyage;
	}

	/**
	 * 
	 * Loads voyage
	 * @param voyage voyage providing voyage ID
	 * @param option option
	 * @return loaded voyage
	 */
	private static Voyage loadInternal(Voyage voyage, int option) {
		Voyage localVoyage = null;
		
		Session session = HibernateUtil.getSession();
		
		//Load voyage from DB
		VoyageIndex[] voyageIndex = HibernateConnector.getConnector()
			.getVoyageIndexByVoyage(session, voyage, option);
		
		//Prepare result
		if (voyageIndex.length != 0) {
			localVoyage = voyageIndex[0].getVoyage();
			localVoyage.setSlaves(voyageIndex[0].getSlaves());
			localVoyage.setRevisionId(voyageIndex[0].getRevisionId());
			cleanObject(new Voyage[] {localVoyage});
		}
		session.close();
		
		return localVoyage;
	}
	
	private static void cleanObject(Voyage [] objs) {
		for (int i = 0; i < objs.length; i++) {
			Iterator iter = objs[i].getSlaves().iterator();
			while (iter.hasNext()) {
				((Slave)iter.next()).setModified(0);
			}
			objs[i].setModified(0);
		}
	}
	
	/**
	 * Loads Active (most recent active) voyage with given ID.
	 * @param voyageId	voyuage ID
	 * @return voyage object, null if there is no desired Voyage in DB
	 */
	public static Voyage loadActive(Long voyageId) {
		Voyage localVoyage = new Voyage();
		localVoyage.setVoyageId(voyageId);
		return loadInternal(localVoyage, 
				HibernateConnector.APPROVED & HibernateConnector.WITHOUT_HISTORY);
	}

	/**
	 * Loads most recent (not necessary active) voyage with given ID.
	 * @param voyageId	voyuage ID
	 * @return voyage object, null if there is no desired Voyage in DB
	 */
	public static Voyage loadMostRecent(Long voyageId) {
		Voyage localVoyage = new Voyage();
		localVoyage.setVoyageId(voyageId);
		return loadInternal(localVoyage, 
				HibernateConnector.APPROVED_AND_NOT_APPROVED & HibernateConnector.WITHOUT_HISTORY);
	}

	/**
	 * Loads most recent (not necessary active) voyage with given ID.
	 * @param voyageId	voyuage ID
	 * @return voyage object, null if there is no desired Voyage in DB
	 */
	public static Voyage[] loadMostRecent(Long voyageId, int p_packetSize) {
		Voyage localVoyage = new Voyage();
		localVoyage.setVoyageId(voyageId);
		
		Session session = HibernateUtil.getSession();
		//Load voyage from DB
		VoyageIndex[] voyageIndex = HibernateConnector.getConnector()
			.getVoyagesIndexSet(session, localVoyage, p_packetSize, HibernateConnector.APPROVED_AND_NOT_APPROVED & HibernateConnector.WITHOUT_HISTORY);
		
		Voyage[] ret = new Voyage[voyageIndex.length];
		//Prepare result
		for (int i = 0; i < voyageIndex.length; i++) {
			ret[i] = voyageIndex[i].getVoyage();
			ret[i].setSlaves(voyageIndex[i].getSlaves());
			ret[i].setRevisionId(voyageIndex[i].getRevisionId());
			
		}
		cleanObject(ret);
		session.close();
		return ret;
	}
	
	public static Voyage[] loadAllMostRecent(int p_firstResult, int p_fetchSize) {
		
		Session session = HibernateUtil.getSession();
		//Load voyage from DB
		VoyageIndex[] voyageIndex = HibernateConnector.getConnector()
			.getVoyagesIndexSet(session, p_firstResult, p_fetchSize, HibernateConnector.APPROVED_AND_NOT_APPROVED & HibernateConnector.WITHOUT_HISTORY);
		
		Voyage[] ret = new Voyage[voyageIndex.length];
		//Prepare result
		for (int i = 0; i < voyageIndex.length; i++) {
			ret[i] = voyageIndex[i].getVoyage();
			ret[i].setSlaves(voyageIndex[i].getSlaves());
			ret[i].setRevisionId(voyageIndex[i].getRevisionId());
			
		}
		cleanObject(ret);
		session.close();
		return ret;
	}

	/**
	 * Loads voyage with given ID and given revision ID.
	 * @param voyageId	voyuage ID
	 * @return voyage object, null if there is no desired Voyage in DB
	 */
	public static Voyage loadByRevision(Long voyageId, Long revisionId) {
		Voyage localVoyage = new Voyage();
		localVoyage.setVoyageId(voyageId);
		localVoyage.setRevisionId(revisionId);
		return loadInternal(localVoyage, 
				HibernateConnector.WITHOUT_HISTORY);
	}

	/**
	 * List all revis
	 * @param voyageId
	 * @param approved
	 * @return
	 */
	public static List loadAllRevisions(Long voyageId, int p_option) {
		int option = p_option | HibernateConnector.WITH_HISTORY;
		Voyage localVoyage = new Voyage();
		localVoyage.setVoyageId(voyageId);
		
		Session session = HibernateUtil.getSession();
		//Load info from DB
		VoyageIndex[] voyageIndex = HibernateConnector.getConnector()
			.getVoyageIndexByVoyage(session, localVoyage, option);
		List list = new ArrayList();
		//Prepare result
		for (int i = 0; i < voyageIndex.length; i++) {
			Voyage v = voyageIndex[i].getVoyage();
			v.setSlaves(voyageIndex[i].getSlaves());
			v.setRevisionId(voyageIndex[i].getRevisionId());
			list.add(v);
			cleanObject(new Voyage[] {v});
		}
		session.close();
		//Return result
		return list;
	}
	
	/**
	 * Saves voyage to DB.
	 *
	 */
	public void save() {
		//Prepare VoyageIndex
		VoyageIndex vIndex = new VoyageIndex();
		vIndex.setVoyage(this);
		vIndex.setVoyageId(this.getVoyageId());
		vIndex.setSlaves(this.getSlaves());
		vIndex.setRevisionDate(new Date(System.currentTimeMillis()));
		vIndex.setFlag(approved ? new Integer(1): new Integer(0));
		
		//Save to DB (or update...)
		if (this.created) {
			HibernateConnector.getConnector().createVoyage(vIndex);
		} else {
			HibernateConnector.getConnector().updateVoyage(vIndex);
		}
	}

	/**
	 * Gets deep copy of object.
	 */
	public Object clone() {
		//Copy voyage itself
		Voyage newVoyage = new Voyage();

		newVoyage.setRevisionId(getRevisionId());
		
		newVoyage.values = this.values;
		
		//Copy slaves
		Iterator iter = getSlaves().iterator();
		HashSet set = new HashSet();
		while (iter.hasNext()) {
			set.add(((Slave)iter.next()).clone());
		}
		newVoyage.setSlaves(set);
		newVoyage.setModified(getModified());
		
		//Return copy object
		return newVoyage;
	}
	
	/**
	 * Gets set of slaves.
	 * @return Set with Slave objects
	 */
	public Set getSlaves() {
		return this.slaves;
	}
	
	/**
	 * Sets set of slaves.
	 * @param slaves Set with Slave objects
	 */
	public void setSlaves(Set slaves) {
		this.slaves = slaves;
	}
	
	/**
	 * Removes slave from list.
	 * @param slave Slave to remove
	 */
	public void removeSlave(Slave slave) {
		this.slavesModified = UPDATED;
		this.slaves.remove(slave);
	}
	
	/**
	 * Adds slave to set of slaves.
	 * @param slave Slave to add
	 */
	public void addSlave(Slave slave) {
		this.slavesModified = UPDATED;
		this.slaves.add(slave);
	}
	
	/**
	 * Gets Slave with given slaveId.
	 * @param slaveId Slave id
	 * @return Slave object, null if voyage does not have such a slave
	 */
	public Slave getSlave(Long slaveId) {
		Iterator iter = this.slaves.iterator();
		while (iter.hasNext()) {
			Slave slave = (Slave)iter.next();
			if (slave.getSlaveId().equals(slaveId)) {
				return slave;
			}
		}
		return null;
	}
	
	/**
	 * Sets modified flag.
	 * @param p_modified
	 */
	public void setModified(int p_modified) {
		this.modified = p_modified;
	}
	
	/**
	 * Gets modified flag.
	 * @return
	 */
	public int getModified() {
		return this.modified;
	}
	
	public boolean wereSlavesModified() {
		return this.slavesModified == UPDATED;
	}
	
	public void setIid(Long iid) {
		this.iid = iid;
	}
	
	public Long getIid() {
		return this.iid;
	}
	
	public Long getRevisionId() {
		return (Long)this.values.get("revisionId");
	}
	
	public void setRevisionId(Long revisionId) {
		if ((revisionId == null && this.values.get("revisionId") != null) 
			|| (revisionId != null && !revisionId.equals(this.values.get("revisionId")))) {
			this.modified = UPDATED;
		}
		this.values.put("revisionId", revisionId);
	}
	
	/**
	 * Returns string representation of object.
	 */
	public String toString() {
		return "Voyage: " + values + " Slaves: " + getSlaves();
	}
	
	/*AUTOGENERATED BLOCK*/
	/** Getters/setters **/
	public void setVoyageId(Long voyageId) {
		if ((voyageId == null && this.values.get("voyageId") != null) 
			|| (voyageId != null && !voyageId.equals(this.values.get("voyageId")))) {
			this.modified = UPDATED;
		}
		this.values.put("voyageId", voyageId);
	}
	public void setShipname(String shipname) {
		if ((shipname == null && this.values.get("shipname") != null) 
			|| (shipname != null && !shipname.equals(this.values.get("shipname")))) {
			this.modified = UPDATED;
		}
		this.values.put("shipname", shipname);
	}
	public void setCaptaina(String captaina) {
		if ((captaina == null && this.values.get("captaina") != null) 
			|| (captaina != null && !captaina.equals(this.values.get("captaina")))) {
			this.modified = UPDATED;
		}
		this.values.put("captaina", captaina);
	}
	public void setCaptainb(String captainb) {
		if ((captainb == null && this.values.get("captainb") != null) 
			|| (captainb != null && !captainb.equals(this.values.get("captainb")))) {
			this.modified = UPDATED;
		}
		this.values.put("captainb", captainb);
	}
	public void setCaptainc(String captainc) {
		if ((captainc == null && this.values.get("captainc") != null) 
			|| (captainc != null && !captainc.equals(this.values.get("captainc")))) {
			this.modified = UPDATED;
		}
		this.values.put("captainc", captainc);
	}
	public void setDatedep(Date datedep) {
		if ((datedep == null && this.values.get("datedep") != null) 
			|| (datedep != null && !datedep.equals(this.values.get("datedep")))) {
			this.modified = UPDATED;
		}
		this.values.put("datedep", datedep);
	}
	public void setNppretra(Integer nppretra) {
		if ((nppretra == null && this.values.get("nppretra") != null) 
			|| (nppretra != null && !nppretra.equals(this.values.get("nppretra")))) {
			this.modified = UPDATED;
		}
		this.values.put("nppretra", nppretra);
	}
	public void setD1slatr(Date d1slatr) {
		if ((d1slatr == null && this.values.get("d1slatr") != null) 
			|| (d1slatr != null && !d1slatr.equals(this.values.get("d1slatr")))) {
			this.modified = UPDATED;
		}
		this.values.put("d1slatr", d1slatr);
	}
	public void setNpprior(Integer npprior) {
		if ((npprior == null && this.values.get("npprior") != null) 
			|| (npprior != null && !npprior.equals(this.values.get("npprior")))) {
			this.modified = UPDATED;
		}
		this.values.put("npprior", npprior);
	}
	public void setTslavesp(Integer tslavesp) {
		if ((tslavesp == null && this.values.get("tslavesp") != null) 
			|| (tslavesp != null && !tslavesp.equals(this.values.get("tslavesp")))) {
			this.modified = UPDATED;
		}
		this.values.put("tslavesp", tslavesp);
	}
	public void setTslavesd(Integer tslavesd) {
		if ((tslavesd == null && this.values.get("tslavesd") != null) 
			|| (tslavesd != null && !tslavesd.equals(this.values.get("tslavesd")))) {
			this.modified = UPDATED;
		}
		this.values.put("tslavesd", tslavesd);
	}
	public void setSladvoy(Integer sladvoy) {
		if ((sladvoy == null && this.values.get("sladvoy") != null) 
			|| (sladvoy != null && !sladvoy.equals(this.values.get("sladvoy")))) {
			this.modified = UPDATED;
		}
		this.values.put("sladvoy", sladvoy);
	}
	public void setSlaarriv(Integer slaarriv) {
		if ((slaarriv == null && this.values.get("slaarriv") != null) 
			|| (slaarriv != null && !slaarriv.equals(this.values.get("slaarriv")))) {
			this.modified = UPDATED;
		}
		this.values.put("slaarriv", slaarriv);
	}
	public void setRrdata31(Date rrdata31) {
		if ((rrdata31 == null && this.values.get("rrdata31") != null) 
			|| (rrdata31 != null && !rrdata31.equals(this.values.get("rrdata31")))) {
			this.modified = UPDATED;
		}
		this.values.put("rrdata31", rrdata31);
	}
	public void setSlas32(Integer slas32) {
		if ((slas32 == null && this.values.get("slas32") != null) 
			|| (slas32 != null && !slas32.equals(this.values.get("slas32")))) {
			this.modified = UPDATED;
		}
		this.values.put("slas32", slas32);
	}
	public void setDatarr32(Date datarr32) {
		if ((datarr32 == null && this.values.get("datarr32") != null) 
			|| (datarr32 != null && !datarr32.equals(this.values.get("datarr32")))) {
			this.modified = UPDATED;
		}
		this.values.put("datarr32", datarr32);
	}
	public void setSlas36(Integer slas36) {
		if ((slas36 == null && this.values.get("slas36") != null) 
			|| (slas36 != null && !slas36.equals(this.values.get("slas36")))) {
			this.modified = UPDATED;
		}
		this.values.put("slas36", slas36);
	}
	public void setDatarr33(Date datarr33) {
		if ((datarr33 == null && this.values.get("datarr33") != null) 
			|| (datarr33 != null && !datarr33.equals(this.values.get("datarr33")))) {
			this.modified = UPDATED;
		}
		this.values.put("datarr33", datarr33);
	}
	public void setSlas39(Integer slas39) {
		if ((slas39 == null && this.values.get("slas39") != null) 
			|| (slas39 != null && !slas39.equals(this.values.get("slas39")))) {
			this.modified = UPDATED;
		}
		this.values.put("slas39", slas39);
	}
	public void setDdepamb(Date ddepamb) {
		if ((ddepamb == null && this.values.get("ddepamb") != null) 
			|| (ddepamb != null && !ddepamb.equals(this.values.get("ddepamb")))) {
			this.modified = UPDATED;
		}
		this.values.put("ddepamb", ddepamb);
	}
	public void setDatarr4(Date datarr4) {
		if ((datarr4 == null && this.values.get("datarr4") != null) 
			|| (datarr4 != null && !datarr4.equals(this.values.get("datarr4")))) {
			this.modified = UPDATED;
		}
		this.values.put("datarr4", datarr4);
	}
	public void setFate(Integer fate) {
		if ((fate == null && this.values.get("fate") != null) 
			|| (fate != null && !fate.equals(this.values.get("fate")))) {
			this.modified = UPDATED;
		}
		this.values.put("fate", fate);
	}
	public void setSourcea(String sourcea) {
		if ((sourcea == null && this.values.get("sourcea") != null) 
			|| (sourcea != null && !sourcea.equals(this.values.get("sourcea")))) {
			this.modified = UPDATED;
		}
		this.values.put("sourcea", sourcea);
	}
	public void setSourceb(String sourceb) {
		if ((sourceb == null && this.values.get("sourceb") != null) 
			|| (sourceb != null && !sourceb.equals(this.values.get("sourceb")))) {
			this.modified = UPDATED;
		}
		this.values.put("sourceb", sourceb);
	}
	public void setSourcec(String sourcec) {
		if ((sourcec == null && this.values.get("sourcec") != null) 
			|| (sourcec != null && !sourcec.equals(this.values.get("sourcec")))) {
			this.modified = UPDATED;
		}
		this.values.put("sourcec", sourcec);
	}
	public void setSourced(String sourced) {
		if ((sourced == null && this.values.get("sourced") != null) 
			|| (sourced != null && !sourced.equals(this.values.get("sourced")))) {
			this.modified = UPDATED;
		}
		this.values.put("sourced", sourced);
	}
	public void setSourcee(String sourcee) {
		if ((sourcee == null && this.values.get("sourcee") != null) 
			|| (sourcee != null && !sourcee.equals(this.values.get("sourcee")))) {
			this.modified = UPDATED;
		}
		this.values.put("sourcee", sourcee);
	}
	public void setSourcef(String sourcef) {
		if ((sourcef == null && this.values.get("sourcef") != null) 
			|| (sourcef != null && !sourcef.equals(this.values.get("sourcef")))) {
			this.modified = UPDATED;
		}
		this.values.put("sourcef", sourcef);
	}
	public void setSourceg(String sourceg) {
		if ((sourceg == null && this.values.get("sourceg") != null) 
			|| (sourceg != null && !sourceg.equals(this.values.get("sourceg")))) {
			this.modified = UPDATED;
		}
		this.values.put("sourceg", sourceg);
	}
	public void setSourceh(String sourceh) {
		if ((sourceh == null && this.values.get("sourceh") != null) 
			|| (sourceh != null && !sourceh.equals(this.values.get("sourceh")))) {
			this.modified = UPDATED;
		}
		this.values.put("sourceh", sourceh);
	}
	public void setSourcei(String sourcei) {
		if ((sourcei == null && this.values.get("sourcei") != null) 
			|| (sourcei != null && !sourcei.equals(this.values.get("sourcei")))) {
			this.modified = UPDATED;
		}
		this.values.put("sourcei", sourcei);
	}
	public void setSourcej(String sourcej) {
		if ((sourcej == null && this.values.get("sourcej") != null) 
			|| (sourcej != null && !sourcej.equals(this.values.get("sourcej")))) {
			this.modified = UPDATED;
		}
		this.values.put("sourcej", sourcej);
	}
	public void setSourcek(String sourcek) {
		if ((sourcek == null && this.values.get("sourcek") != null) 
			|| (sourcek != null && !sourcek.equals(this.values.get("sourcek")))) {
			this.modified = UPDATED;
		}
		this.values.put("sourcek", sourcek);
	}
	public void setSourcel(String sourcel) {
		if ((sourcel == null && this.values.get("sourcel") != null) 
			|| (sourcel != null && !sourcel.equals(this.values.get("sourcel")))) {
			this.modified = UPDATED;
		}
		this.values.put("sourcel", sourcel);
	}
	public void setSourcem(String sourcem) {
		if ((sourcem == null && this.values.get("sourcem") != null) 
			|| (sourcem != null && !sourcem.equals(this.values.get("sourcem")))) {
			this.modified = UPDATED;
		}
		this.values.put("sourcem", sourcem);
	}
	public void setSourcen(String sourcen) {
		if ((sourcen == null && this.values.get("sourcen") != null) 
			|| (sourcen != null && !sourcen.equals(this.values.get("sourcen")))) {
			this.modified = UPDATED;
		}
		this.values.put("sourcen", sourcen);
	}
	public void setSourceo(String sourceo) {
		if ((sourceo == null && this.values.get("sourceo") != null) 
			|| (sourceo != null && !sourceo.equals(this.values.get("sourceo")))) {
			this.modified = UPDATED;
		}
		this.values.put("sourceo", sourceo);
	}
	public void setSourcep(String sourcep) {
		if ((sourcep == null && this.values.get("sourcep") != null) 
			|| (sourcep != null && !sourcep.equals(this.values.get("sourcep")))) {
			this.modified = UPDATED;
		}
		this.values.put("sourcep", sourcep);
	}
	public void setSourceq(String sourceq) {
		if ((sourceq == null && this.values.get("sourceq") != null) 
			|| (sourceq != null && !sourceq.equals(this.values.get("sourceq")))) {
			this.modified = UPDATED;
		}
		this.values.put("sourceq", sourceq);
	}
	public void setSourcer(String sourcer) {
		if ((sourcer == null && this.values.get("sourcer") != null) 
			|| (sourcer != null && !sourcer.equals(this.values.get("sourcer")))) {
			this.modified = UPDATED;
		}
		this.values.put("sourcer", sourcer);
	}
	public void setSlintend(Integer slintend) {
		if ((slintend == null && this.values.get("slintend") != null) 
			|| (slintend != null && !slintend.equals(this.values.get("slintend")))) {
			this.modified = UPDATED;
		}
		this.values.put("slintend", slintend);
	}
	public void setTonnage(Integer tonnage) {
		if ((tonnage == null && this.values.get("tonnage") != null) 
			|| (tonnage != null && !tonnage.equals(this.values.get("tonnage")))) {
			this.modified = UPDATED;
		}
		this.values.put("tonnage", tonnage);
	}
	public void setCrewdied(Integer crewdied) {
		if ((crewdied == null && this.values.get("crewdied") != null) 
			|| (crewdied != null && !crewdied.equals(this.values.get("crewdied")))) {
			this.modified = UPDATED;
		}
		this.values.put("crewdied", crewdied);
	}
	public void setNcar13(Integer ncar13) {
		if ((ncar13 == null && this.values.get("ncar13") != null) 
			|| (ncar13 != null && !ncar13.equals(this.values.get("ncar13")))) {
			this.modified = UPDATED;
		}
		this.values.put("ncar13", ncar13);
	}
	public void setNcar15(Integer ncar15) {
		if ((ncar15 == null && this.values.get("ncar15") != null) 
			|| (ncar15 != null && !ncar15.equals(this.values.get("ncar15")))) {
			this.modified = UPDATED;
		}
		this.values.put("ncar15", ncar15);
	}
	public void setNcar17(Integer ncar17) {
		if ((ncar17 == null && this.values.get("ncar17") != null) 
			|| (ncar17 != null && !ncar17.equals(this.values.get("ncar17")))) {
			this.modified = UPDATED;
		}
		this.values.put("ncar17", ncar17);
	}
	public void setSladamer(Integer sladamer) {
		if ((sladamer == null && this.values.get("sladamer") != null) 
			|| (sladamer != null && !sladamer.equals(this.values.get("sladamer")))) {
			this.modified = UPDATED;
		}
		this.values.put("sladamer", sladamer);
	}
	public void setSaild1(Integer saild1) {
		if ((saild1 == null && this.values.get("saild1") != null) 
			|| (saild1 != null && !saild1.equals(this.values.get("saild1")))) {
			this.modified = UPDATED;
		}
		this.values.put("saild1", saild1);
	}
	public void setSaild2(Integer saild2) {
		if ((saild2 == null && this.values.get("saild2") != null) 
			|| (saild2 != null && !saild2.equals(this.values.get("saild2")))) {
			this.modified = UPDATED;
		}
		this.values.put("saild2", saild2);
	}
	public void setSaild3(Integer saild3) {
		if ((saild3 == null && this.values.get("saild3") != null) 
			|| (saild3 != null && !saild3.equals(this.values.get("saild3")))) {
			this.modified = UPDATED;
		}
		this.values.put("saild3", saild3);
	}
	public void setSaild4(Integer saild4) {
		if ((saild4 == null && this.values.get("saild4") != null) 
			|| (saild4 != null && !saild4.equals(this.values.get("saild4")))) {
			this.modified = UPDATED;
		}
		this.values.put("saild4", saild4);
	}
	public void setSaild5(Integer saild5) {
		if ((saild5 == null && this.values.get("saild5") != null) 
			|| (saild5 != null && !saild5.equals(this.values.get("saild5")))) {
			this.modified = UPDATED;
		}
		this.values.put("saild5", saild5);
	}
	public void setNdesert(Integer ndesert) {
		if ((ndesert == null && this.values.get("ndesert") != null) 
			|| (ndesert != null && !ndesert.equals(this.values.get("ndesert")))) {
			this.modified = UPDATED;
		}
		this.values.put("ndesert", ndesert);
	}
	public void setSlinten2(Integer slinten2) {
		if ((slinten2 == null && this.values.get("slinten2") != null) 
			|| (slinten2 != null && !slinten2.equals(this.values.get("slinten2")))) {
			this.modified = UPDATED;
		}
		this.values.put("slinten2", slinten2);
	}
	public void setGuns(Integer guns) {
		if ((guns == null && this.values.get("guns") != null) 
			|| (guns != null && !guns.equals(this.values.get("guns")))) {
			this.modified = UPDATED;
		}
		this.values.put("guns", guns);
	}
	public void setVoyage(Integer voyage) {
		if ((voyage == null && this.values.get("voyage") != null) 
			|| (voyage != null && !voyage.equals(this.values.get("voyage")))) {
			this.modified = UPDATED;
		}
		this.values.put("voyage", voyage);
	}
	public void setCrew1(Integer crew1) {
		if ((crew1 == null && this.values.get("crew1") != null) 
			|| (crew1 != null && !crew1.equals(this.values.get("crew1")))) {
			this.modified = UPDATED;
		}
		this.values.put("crew1", crew1);
	}
	public void setChild2(Integer child2) {
		if ((child2 == null && this.values.get("child2") != null) 
			|| (child2 != null && !child2.equals(this.values.get("child2")))) {
			this.modified = UPDATED;
		}
		this.values.put("child2", child2);
	}
	public void setChild3(Integer child3) {
		if ((child3 == null && this.values.get("child3") != null) 
			|| (child3 != null && !child3.equals(this.values.get("child3")))) {
			this.modified = UPDATED;
		}
		this.values.put("child3", child3);
	}
	public void setYrreg(Integer yrreg) {
		if ((yrreg == null && this.values.get("yrreg") != null) 
			|| (yrreg != null && !yrreg.equals(this.values.get("yrreg")))) {
			this.modified = UPDATED;
		}
		this.values.put("yrreg", yrreg);
	}
	public void setCrew3(Integer crew3) {
		if ((crew3 == null && this.values.get("crew3") != null) 
			|| (crew3 != null && !crew3.equals(this.values.get("crew3")))) {
			this.modified = UPDATED;
		}
		this.values.put("crew3", crew3);
	}
	public void setCrew4(Integer crew4) {
		if ((crew4 == null && this.values.get("crew4") != null) 
			|| (crew4 != null && !crew4.equals(this.values.get("crew4")))) {
			this.modified = UPDATED;
		}
		this.values.put("crew4", crew4);
	}
	public void setCrew5(Integer crew5) {
		if ((crew5 == null && this.values.get("crew5") != null) 
			|| (crew5 != null && !crew5.equals(this.values.get("crew5")))) {
			this.modified = UPDATED;
		}
		this.values.put("crew5", crew5);
	}
	public void setAdult1(Integer adult1) {
		if ((adult1 == null && this.values.get("adult1") != null) 
			|| (adult1 != null && !adult1.equals(this.values.get("adult1")))) {
			this.modified = UPDATED;
		}
		this.values.put("adult1", adult1);
	}
	public void setChild1(Integer child1) {
		if ((child1 == null && this.values.get("child1") != null) 
			|| (child1 != null && !child1.equals(this.values.get("child1")))) {
			this.modified = UPDATED;
		}
		this.values.put("child1", child1);
	}
	public void setFemale1(Integer female1) {
		if ((female1 == null && this.values.get("female1") != null) 
			|| (female1 != null && !female1.equals(this.values.get("female1")))) {
			this.modified = UPDATED;
		}
		this.values.put("female1", female1);
	}
	public void setMale1(Integer male1) {
		if ((male1 == null && this.values.get("male1") != null) 
			|| (male1 != null && !male1.equals(this.values.get("male1")))) {
			this.modified = UPDATED;
		}
		this.values.put("male1", male1);
	}
	public void setMen1(Integer men1) {
		if ((men1 == null && this.values.get("men1") != null) 
			|| (men1 != null && !men1.equals(this.values.get("men1")))) {
			this.modified = UPDATED;
		}
		this.values.put("men1", men1);
	}
	public void setWomen1(Integer women1) {
		if ((women1 == null && this.values.get("women1") != null) 
			|| (women1 != null && !women1.equals(this.values.get("women1")))) {
			this.modified = UPDATED;
		}
		this.values.put("women1", women1);
	}
	public void setFemale2(Integer female2) {
		if ((female2 == null && this.values.get("female2") != null) 
			|| (female2 != null && !female2.equals(this.values.get("female2")))) {
			this.modified = UPDATED;
		}
		this.values.put("female2", female2);
	}
	public void setMale2(Integer male2) {
		if ((male2 == null && this.values.get("male2") != null) 
			|| (male2 != null && !male2.equals(this.values.get("male2")))) {
			this.modified = UPDATED;
		}
		this.values.put("male2", male2);
	}
	public void setMen2(Integer men2) {
		if ((men2 == null && this.values.get("men2") != null) 
			|| (men2 != null && !men2.equals(this.values.get("men2")))) {
			this.modified = UPDATED;
		}
		this.values.put("men2", men2);
	}
	public void setWomen2(Integer women2) {
		if ((women2 == null && this.values.get("women2") != null) 
			|| (women2 != null && !women2.equals(this.values.get("women2")))) {
			this.modified = UPDATED;
		}
		this.values.put("women2", women2);
	}
	public void setBoy2(Integer boy2) {
		if ((boy2 == null && this.values.get("boy2") != null) 
			|| (boy2 != null && !boy2.equals(this.values.get("boy2")))) {
			this.modified = UPDATED;
		}
		this.values.put("boy2", boy2);
	}
	public void setGirl2(Integer girl2) {
		if ((girl2 == null && this.values.get("girl2") != null) 
			|| (girl2 != null && !girl2.equals(this.values.get("girl2")))) {
			this.modified = UPDATED;
		}
		this.values.put("girl2", girl2);
	}
	public void setFemale3(Integer female3) {
		if ((female3 == null && this.values.get("female3") != null) 
			|| (female3 != null && !female3.equals(this.values.get("female3")))) {
			this.modified = UPDATED;
		}
		this.values.put("female3", female3);
	}
	public void setMale3(Integer male3) {
		if ((male3 == null && this.values.get("male3") != null) 
			|| (male3 != null && !male3.equals(this.values.get("male3")))) {
			this.modified = UPDATED;
		}
		this.values.put("male3", male3);
	}
	public void setMen3(Integer men3) {
		if ((men3 == null && this.values.get("men3") != null) 
			|| (men3 != null && !men3.equals(this.values.get("men3")))) {
			this.modified = UPDATED;
		}
		this.values.put("men3", men3);
	}
	public void setWomen3(Integer women3) {
		if ((women3 == null && this.values.get("women3") != null) 
			|| (women3 != null && !women3.equals(this.values.get("women3")))) {
			this.modified = UPDATED;
		}
		this.values.put("women3", women3);
	}
	public void setBoy3(Integer boy3) {
		if ((boy3 == null && this.values.get("boy3") != null) 
			|| (boy3 != null && !boy3.equals(this.values.get("boy3")))) {
			this.modified = UPDATED;
		}
		this.values.put("boy3", boy3);
	}
	public void setGirl3(Integer girl3) {
		if ((girl3 == null && this.values.get("girl3") != null) 
			|| (girl3 != null && !girl3.equals(this.values.get("girl3")))) {
			this.modified = UPDATED;
		}
		this.values.put("girl3", girl3);
	}
	public void setFemale4(Integer female4) {
		if ((female4 == null && this.values.get("female4") != null) 
			|| (female4 != null && !female4.equals(this.values.get("female4")))) {
			this.modified = UPDATED;
		}
		this.values.put("female4", female4);
	}
	public void setMale4(Integer male4) {
		if ((male4 == null && this.values.get("male4") != null) 
			|| (male4 != null && !male4.equals(this.values.get("male4")))) {
			this.modified = UPDATED;
		}
		this.values.put("male4", male4);
	}
	public void setMen4(Integer men4) {
		if ((men4 == null && this.values.get("men4") != null) 
			|| (men4 != null && !men4.equals(this.values.get("men4")))) {
			this.modified = UPDATED;
		}
		this.values.put("men4", men4);
	}
	public void setWomen4(Integer women4) {
		if ((women4 == null && this.values.get("women4") != null) 
			|| (women4 != null && !women4.equals(this.values.get("women4")))) {
			this.modified = UPDATED;
		}
		this.values.put("women4", women4);
	}
	public void setBoy4(Integer boy4) {
		if ((boy4 == null && this.values.get("boy4") != null) 
			|| (boy4 != null && !boy4.equals(this.values.get("boy4")))) {
			this.modified = UPDATED;
		}
		this.values.put("boy4", boy4);
	}
	public void setGirl4(Integer girl4) {
		if ((girl4 == null && this.values.get("girl4") != null) 
			|| (girl4 != null && !girl4.equals(this.values.get("girl4")))) {
			this.modified = UPDATED;
		}
		this.values.put("girl4", girl4);
	}
	public void setChild4(Integer child4) {
		if ((child4 == null && this.values.get("child4") != null) 
			|| (child4 != null && !child4.equals(this.values.get("child4")))) {
			this.modified = UPDATED;
		}
		this.values.put("child4", child4);
	}
	public void setFemale6(Integer female6) {
		if ((female6 == null && this.values.get("female6") != null) 
			|| (female6 != null && !female6.equals(this.values.get("female6")))) {
			this.modified = UPDATED;
		}
		this.values.put("female6", female6);
	}
	public void setMale6(Integer male6) {
		if ((male6 == null && this.values.get("male6") != null) 
			|| (male6 != null && !male6.equals(this.values.get("male6")))) {
			this.modified = UPDATED;
		}
		this.values.put("male6", male6);
	}
	public void setMen6(Integer men6) {
		if ((men6 == null && this.values.get("men6") != null) 
			|| (men6 != null && !men6.equals(this.values.get("men6")))) {
			this.modified = UPDATED;
		}
		this.values.put("men6", men6);
	}
	public void setWomen6(Integer women6) {
		if ((women6 == null && this.values.get("women6") != null) 
			|| (women6 != null && !women6.equals(this.values.get("women6")))) {
			this.modified = UPDATED;
		}
		this.values.put("women6", women6);
	}
	public void setBoy6(Integer boy6) {
		if ((boy6 == null && this.values.get("boy6") != null) 
			|| (boy6 != null && !boy6.equals(this.values.get("boy6")))) {
			this.modified = UPDATED;
		}
		this.values.put("boy6", boy6);
	}
	public void setGirl6(Integer girl6) {
		if ((girl6 == null && this.values.get("girl6") != null) 
			|| (girl6 != null && !girl6.equals(this.values.get("girl6")))) {
			this.modified = UPDATED;
		}
		this.values.put("girl6", girl6);
	}
	public void setChild6(Integer child6) {
		if ((child6 == null && this.values.get("child6") != null) 
			|| (child6 != null && !child6.equals(this.values.get("child6")))) {
			this.modified = UPDATED;
		}
		this.values.put("child6", child6);
	}
	public void setCrew2(Integer crew2) {
		if ((crew2 == null && this.values.get("crew2") != null) 
			|| (crew2 != null && !crew2.equals(this.values.get("crew2")))) {
			this.modified = UPDATED;
		}
		this.values.put("crew2", crew2);
	}
	public void setInfantm3(Integer infantm3) {
		if ((infantm3 == null && this.values.get("infantm3") != null) 
			|| (infantm3 != null && !infantm3.equals(this.values.get("infantm3")))) {
			this.modified = UPDATED;
		}
		this.values.put("infantm3", infantm3);
	}
	public void setInfantf3(Integer infantf3) {
		if ((infantf3 == null && this.values.get("infantf3") != null) 
			|| (infantf3 != null && !infantf3.equals(this.values.get("infantf3")))) {
			this.modified = UPDATED;
		}
		this.values.put("infantf3", infantf3);
	}
	public void setSladafri(Integer sladafri) {
		if ((sladafri == null && this.values.get("sladafri") != null) 
			|| (sladafri != null && !sladafri.equals(this.values.get("sladafri")))) {
			this.modified = UPDATED;
		}
		this.values.put("sladafri", sladafri);
	}
	public void setSladied5(Integer sladied5) {
		if ((sladied5 == null && this.values.get("sladied5") != null) 
			|| (sladied5 != null && !sladied5.equals(this.values.get("sladied5")))) {
			this.modified = UPDATED;
		}
		this.values.put("sladied5", sladied5);
	}
	public void setSladied4(Integer sladied4) {
		if ((sladied4 == null && this.values.get("sladied4") != null) 
			|| (sladied4 != null && !sladied4.equals(this.values.get("sladied4")))) {
			this.modified = UPDATED;
		}
		this.values.put("sladied4", sladied4);
	}
	public void setSladied2(Integer sladied2) {
		if ((sladied2 == null && this.values.get("sladied2") != null) 
			|| (sladied2 != null && !sladied2.equals(this.values.get("sladied2")))) {
			this.modified = UPDATED;
		}
		this.values.put("sladied2", sladied2);
	}
	public void setSladied1(Integer sladied1) {
		if ((sladied1 == null && this.values.get("sladied1") != null) 
			|| (sladied1 != null && !sladied1.equals(this.values.get("sladied1")))) {
			this.modified = UPDATED;
		}
		this.values.put("sladied1", sladied1);
	}
	public void setSladied3(Integer sladied3) {
		if ((sladied3 == null && this.values.get("sladied3") != null) 
			|| (sladied3 != null && !sladied3.equals(this.values.get("sladied3")))) {
			this.modified = UPDATED;
		}
		this.values.put("sladied3", sladied3);
	}
	public void setSladied6(Integer sladied6) {
		if ((sladied6 == null && this.values.get("sladied6") != null) 
			|| (sladied6 != null && !sladied6.equals(this.values.get("sladied6")))) {
			this.modified = UPDATED;
		}
		this.values.put("sladied6", sladied6);
	}
	public void setInsurrec(Integer insurrec) {
		if ((insurrec == null && this.values.get("insurrec") != null) 
			|| (insurrec != null && !insurrec.equals(this.values.get("insurrec")))) {
			this.modified = UPDATED;
		}
		this.values.put("insurrec", insurrec);
	}
	public void setAdult3(Integer adult3) {
		if ((adult3 == null && this.values.get("adult3") != null) 
			|| (adult3 != null && !adult3.equals(this.values.get("adult3")))) {
			this.modified = UPDATED;
		}
		this.values.put("adult3", adult3);
	}
	public void setOwnera(String ownera) {
		if ((ownera == null && this.values.get("ownera") != null) 
			|| (ownera != null && !ownera.equals(this.values.get("ownera")))) {
			this.modified = UPDATED;
		}
		this.values.put("ownera", ownera);
	}
	public void setOwnerb(String ownerb) {
		if ((ownerb == null && this.values.get("ownerb") != null) 
			|| (ownerb != null && !ownerb.equals(this.values.get("ownerb")))) {
			this.modified = UPDATED;
		}
		this.values.put("ownerb", ownerb);
	}
	public void setOwnerc(String ownerc) {
		if ((ownerc == null && this.values.get("ownerc") != null) 
			|| (ownerc != null && !ownerc.equals(this.values.get("ownerc")))) {
			this.modified = UPDATED;
		}
		this.values.put("ownerc", ownerc);
	}
	public void setOwnerd(String ownerd) {
		if ((ownerd == null && this.values.get("ownerd") != null) 
			|| (ownerd != null && !ownerd.equals(this.values.get("ownerd")))) {
			this.modified = UPDATED;
		}
		this.values.put("ownerd", ownerd);
	}
	public void setOwnere(String ownere) {
		if ((ownere == null && this.values.get("ownere") != null) 
			|| (ownere != null && !ownere.equals(this.values.get("ownere")))) {
			this.modified = UPDATED;
		}
		this.values.put("ownere", ownere);
	}
	public void setOwnerf(String ownerf) {
		if ((ownerf == null && this.values.get("ownerf") != null) 
			|| (ownerf != null && !ownerf.equals(this.values.get("ownerf")))) {
			this.modified = UPDATED;
		}
		this.values.put("ownerf", ownerf);
	}
	public void setOwnerg(String ownerg) {
		if ((ownerg == null && this.values.get("ownerg") != null) 
			|| (ownerg != null && !ownerg.equals(this.values.get("ownerg")))) {
			this.modified = UPDATED;
		}
		this.values.put("ownerg", ownerg);
	}
	public void setOwnerh(String ownerh) {
		if ((ownerh == null && this.values.get("ownerh") != null) 
			|| (ownerh != null && !ownerh.equals(this.values.get("ownerh")))) {
			this.modified = UPDATED;
		}
		this.values.put("ownerh", ownerh);
	}
	public void setOwneri(String owneri) {
		if ((owneri == null && this.values.get("owneri") != null) 
			|| (owneri != null && !owneri.equals(this.values.get("owneri")))) {
			this.modified = UPDATED;
		}
		this.values.put("owneri", owneri);
	}
	public void setOwnerj(String ownerj) {
		if ((ownerj == null && this.values.get("ownerj") != null) 
			|| (ownerj != null && !ownerj.equals(this.values.get("ownerj")))) {
			this.modified = UPDATED;
		}
		this.values.put("ownerj", ownerj);
	}
	public void setOwnerk(String ownerk) {
		if ((ownerk == null && this.values.get("ownerk") != null) 
			|| (ownerk != null && !ownerk.equals(this.values.get("ownerk")))) {
			this.modified = UPDATED;
		}
		this.values.put("ownerk", ownerk);
	}
	public void setOwnerl(String ownerl) {
		if ((ownerl == null && this.values.get("ownerl") != null) 
			|| (ownerl != null && !ownerl.equals(this.values.get("ownerl")))) {
			this.modified = UPDATED;
		}
		this.values.put("ownerl", ownerl);
	}
	public void setOwnerm(String ownerm) {
		if ((ownerm == null && this.values.get("ownerm") != null) 
			|| (ownerm != null && !ownerm.equals(this.values.get("ownerm")))) {
			this.modified = UPDATED;
		}
		this.values.put("ownerm", ownerm);
	}
	public void setOwnern(String ownern) {
		if ((ownern == null && this.values.get("ownern") != null) 
			|| (ownern != null && !ownern.equals(this.values.get("ownern")))) {
			this.modified = UPDATED;
		}
		this.values.put("ownern", ownern);
	}
	public void setOwnero(String ownero) {
		if ((ownero == null && this.values.get("ownero") != null) 
			|| (ownero != null && !ownero.equals(this.values.get("ownero")))) {
			this.modified = UPDATED;
		}
		this.values.put("ownero", ownero);
	}
	public void setOwnerp(String ownerp) {
		if ((ownerp == null && this.values.get("ownerp") != null) 
			|| (ownerp != null && !ownerp.equals(this.values.get("ownerp")))) {
			this.modified = UPDATED;
		}
		this.values.put("ownerp", ownerp);
	}
	public void setYearaf(Integer yearaf) {
		if ((yearaf == null && this.values.get("yearaf") != null) 
			|| (yearaf != null && !yearaf.equals(this.values.get("yearaf")))) {
			this.modified = UPDATED;
		}
		this.values.put("yearaf", yearaf);
	}
	public void setYeardep(Integer yeardep) {
		if ((yeardep == null && this.values.get("yeardep") != null) 
			|| (yeardep != null && !yeardep.equals(this.values.get("yeardep")))) {
			this.modified = UPDATED;
		}
		this.values.put("yeardep", yeardep);
	}
	public void setYearam(Integer yearam) {
		if ((yearam == null && this.values.get("yearam") != null) 
			|| (yearam != null && !yearam.equals(this.values.get("yearam")))) {
			this.modified = UPDATED;
		}
		this.values.put("yearam", yearam);
	}
	public void setTonmod(Float tonmod) {
		if ((tonmod == null && this.values.get("tonmod") != null) 
			|| (tonmod != null && !tonmod.equals(this.values.get("tonmod")))) {
			this.modified = UPDATED;
		}
		this.values.put("tonmod", tonmod);
	}
	public void setVymrtimp(Integer vymrtimp) {
		if ((vymrtimp == null && this.values.get("vymrtimp") != null) 
			|| (vymrtimp != null && !vymrtimp.equals(this.values.get("vymrtimp")))) {
			this.modified = UPDATED;
		}
		this.values.put("vymrtimp", vymrtimp);
	}
	public void setTslmtimp(Integer tslmtimp) {
		if ((tslmtimp == null && this.values.get("tslmtimp") != null) 
			|| (tslmtimp != null && !tslmtimp.equals(this.values.get("tslmtimp")))) {
			this.modified = UPDATED;
		}
		this.values.put("tslmtimp", tslmtimp);
	}
	public void setSla32imp(Float sla32imp) {
		if ((sla32imp == null && this.values.get("sla32imp") != null) 
			|| (sla32imp != null && !sla32imp.equals(this.values.get("sla32imp")))) {
			this.modified = UPDATED;
		}
		this.values.put("sla32imp", sla32imp);
	}
	public void setSla36imp(Float sla36imp) {
		if ((sla36imp == null && this.values.get("sla36imp") != null) 
			|| (sla36imp != null && !sla36imp.equals(this.values.get("sla36imp")))) {
			this.modified = UPDATED;
		}
		this.values.put("sla36imp", sla36imp);
	}
	public void setImprat(Float imprat) {
		if ((imprat == null && this.values.get("imprat") != null) 
			|| (imprat != null && !imprat.equals(this.values.get("imprat")))) {
			this.modified = UPDATED;
		}
		this.values.put("imprat", imprat);
	}
	public void setRegdis11(Integer regdis11) {
		if ((regdis11 == null && this.values.get("regdis11") != null) 
			|| (regdis11 != null && !regdis11.equals(this.values.get("regdis11")))) {
			this.modified = UPDATED;
		}
		this.values.put("regdis11", regdis11);
	}
	public void setRegdis21(Integer regdis21) {
		if ((regdis21 == null && this.values.get("regdis21") != null) 
			|| (regdis21 != null && !regdis21.equals(this.values.get("regdis21")))) {
			this.modified = UPDATED;
		}
		this.values.put("regdis21", regdis21);
	}
	public void setSla39imp(Integer sla39imp) {
		if ((sla39imp == null && this.values.get("sla39imp") != null) 
			|| (sla39imp != null && !sla39imp.equals(this.values.get("sla39imp")))) {
			this.modified = UPDATED;
		}
		this.values.put("sla39imp", sla39imp);
	}
	public void setNcr15imp(Float ncr15imp) {
		if ((ncr15imp == null && this.values.get("ncr15imp") != null) 
			|| (ncr15imp != null && !ncr15imp.equals(this.values.get("ncr15imp")))) {
			this.modified = UPDATED;
		}
		this.values.put("ncr15imp", ncr15imp);
	}
	public void setNcr17imp(Integer ncr17imp) {
		if ((ncr17imp == null && this.values.get("ncr17imp") != null) 
			|| (ncr17imp != null && !ncr17imp.equals(this.values.get("ncr17imp")))) {
			this.modified = UPDATED;
		}
		this.values.put("ncr17imp", ncr17imp);
	}
	public void setExprat(Float exprat) {
		if ((exprat == null && this.values.get("exprat") != null) 
			|| (exprat != null && !exprat.equals(this.values.get("exprat")))) {
			this.modified = UPDATED;
		}
		this.values.put("exprat", exprat);
	}
	public void setMale1imp(Integer male1imp) {
		if ((male1imp == null && this.values.get("male1imp") != null) 
			|| (male1imp != null && !male1imp.equals(this.values.get("male1imp")))) {
			this.modified = UPDATED;
		}
		this.values.put("male1imp", male1imp);
	}
	public void setFeml1imp(Integer feml1imp) {
		if ((feml1imp == null && this.values.get("feml1imp") != null) 
			|| (feml1imp != null && !feml1imp.equals(this.values.get("feml1imp")))) {
			this.modified = UPDATED;
		}
		this.values.put("feml1imp", feml1imp);
	}
	public void setChil1imp(Integer chil1imp) {
		if ((chil1imp == null && this.values.get("chil1imp") != null) 
			|| (chil1imp != null && !chil1imp.equals(this.values.get("chil1imp")))) {
			this.modified = UPDATED;
		}
		this.values.put("chil1imp", chil1imp);
	}
	public void setMalrat1(Float malrat1) {
		if ((malrat1 == null && this.values.get("malrat1") != null) 
			|| (malrat1 != null && !malrat1.equals(this.values.get("malrat1")))) {
			this.modified = UPDATED;
		}
		this.values.put("malrat1", malrat1);
	}
	public void setChilrat1(Float chilrat1) {
		if ((chilrat1 == null && this.values.get("chilrat1") != null) 
			|| (chilrat1 != null && !chilrat1.equals(this.values.get("chilrat1")))) {
			this.modified = UPDATED;
		}
		this.values.put("chilrat1", chilrat1);
	}
	public void setSlavemx1(Integer slavemx1) {
		if ((slavemx1 == null && this.values.get("slavemx1") != null) 
			|| (slavemx1 != null && !slavemx1.equals(this.values.get("slavemx1")))) {
			this.modified = UPDATED;
		}
		this.values.put("slavemx1", slavemx1);
	}
	public void setSlavema1(Integer slavema1) {
		if ((slavema1 == null && this.values.get("slavema1") != null) 
			|| (slavema1 != null && !slavema1.equals(this.values.get("slavema1")))) {
			this.modified = UPDATED;
		}
		this.values.put("slavema1", slavema1);
	}
	public void setMale3imp(Integer male3imp) {
		if ((male3imp == null && this.values.get("male3imp") != null) 
			|| (male3imp != null && !male3imp.equals(this.values.get("male3imp")))) {
			this.modified = UPDATED;
		}
		this.values.put("male3imp", male3imp);
	}
	public void setFeml3imp(Integer feml3imp) {
		if ((feml3imp == null && this.values.get("feml3imp") != null) 
			|| (feml3imp != null && !feml3imp.equals(this.values.get("feml3imp")))) {
			this.modified = UPDATED;
		}
		this.values.put("feml3imp", feml3imp);
	}
	public void setChil3imp(Integer chil3imp) {
		if ((chil3imp == null && this.values.get("chil3imp") != null) 
			|| (chil3imp != null && !chil3imp.equals(this.values.get("chil3imp")))) {
			this.modified = UPDATED;
		}
		this.values.put("chil3imp", chil3imp);
	}
	public void setChilrat3(Float chilrat3) {
		if ((chilrat3 == null && this.values.get("chilrat3") != null) 
			|| (chilrat3 != null && !chilrat3.equals(this.values.get("chilrat3")))) {
			this.modified = UPDATED;
		}
		this.values.put("chilrat3", chilrat3);
	}
	public void setMalrat3(Float malrat3) {
		if ((malrat3 == null && this.values.get("malrat3") != null) 
			|| (malrat3 != null && !malrat3.equals(this.values.get("malrat3")))) {
			this.modified = UPDATED;
		}
		this.values.put("malrat3", malrat3);
	}
	public void setSlavemx3(Integer slavemx3) {
		if ((slavemx3 == null && this.values.get("slavemx3") != null) 
			|| (slavemx3 != null && !slavemx3.equals(this.values.get("slavemx3")))) {
			this.modified = UPDATED;
		}
		this.values.put("slavemx3", slavemx3);
	}
	public void setSlavema3(Integer slavema3) {
		if ((slavema3 == null && this.values.get("slavema3") != null) 
			|| (slavema3 != null && !slavema3.equals(this.values.get("slavema3")))) {
			this.modified = UPDATED;
		}
		this.values.put("slavema3", slavema3);
	}
	public void setAdlt3imp(Integer adlt3imp) {
		if ((adlt3imp == null && this.values.get("adlt3imp") != null) 
			|| (adlt3imp != null && !adlt3imp.equals(this.values.get("adlt3imp")))) {
			this.modified = UPDATED;
		}
		this.values.put("adlt3imp", adlt3imp);
	}
	public void setVymrtrat(Float vymrtrat) {
		if ((vymrtrat == null && this.values.get("vymrtrat") != null) 
			|| (vymrtrat != null && !vymrtrat.equals(this.values.get("vymrtrat")))) {
			this.modified = UPDATED;
		}
		this.values.put("vymrtrat", vymrtrat);
	}
	public void setSlaximp(Float slaximp) {
		if ((slaximp == null && this.values.get("slaximp") != null) 
			|| (slaximp != null && !slaximp.equals(this.values.get("slaximp")))) {
			this.modified = UPDATED;
		}
		this.values.put("slaximp", slaximp);
	}
	public void setSlamimp(Float slamimp) {
		if ((slamimp == null && this.values.get("slamimp") != null) 
			|| (slamimp != null && !slamimp.equals(this.values.get("slamimp")))) {
			this.modified = UPDATED;
		}
		this.values.put("slamimp", slamimp);
	}
	public void setVoy2imp(Integer voy2imp) {
		if ((voy2imp == null && this.values.get("voy2imp") != null) 
			|| (voy2imp != null && !voy2imp.equals(this.values.get("voy2imp")))) {
			this.modified = UPDATED;
		}
		this.values.put("voy2imp", voy2imp);
	}
	public void setVoy1imp(Integer voy1imp) {
		if ((voy1imp == null && this.values.get("voy1imp") != null) 
			|| (voy1imp != null && !voy1imp.equals(this.values.get("voy1imp")))) {
			this.modified = UPDATED;
		}
		this.values.put("voy1imp", voy1imp);
	}
	public void setMalrat7(Float malrat7) {
		if ((malrat7 == null && this.values.get("malrat7") != null) 
			|| (malrat7 != null && !malrat7.equals(this.values.get("malrat7")))) {
			this.modified = UPDATED;
		}
		this.values.put("malrat7", malrat7);
	}
	public void setChilrat7(Float chilrat7) {
		if ((chilrat7 == null && this.values.get("chilrat7") != null) 
			|| (chilrat7 != null && !chilrat7.equals(this.values.get("chilrat7")))) {
			this.modified = UPDATED;
		}
		this.values.put("chilrat7", chilrat7);
	}
	public void setSourcex(String sourcex) {
		if ((sourcex == null && this.values.get("sourcex") != null) 
			|| (sourcex != null && !sourcex.equals(this.values.get("sourcex")))) {
			this.modified = UPDATED;
		}
		this.values.put("sourcex", sourcex);
	}
	public void setP(Integer p) {
		if ((p == null && this.values.get("p") != null) 
			|| (p != null && !p.equals(this.values.get("p")))) {
			this.modified = UPDATED;
		}
		this.values.put("p", p);
	}
	public void setMen8(Integer men8) {
		if ((men8 == null && this.values.get("men8") != null) 
			|| (men8 != null && !men8.equals(this.values.get("men8")))) {
			this.modified = UPDATED;
		}
		this.values.put("men8", men8);
	}
	public void setWomen8(Integer women8) {
		if ((women8 == null && this.values.get("women8") != null) 
			|| (women8 != null && !women8.equals(this.values.get("women8")))) {
			this.modified = UPDATED;
		}
		this.values.put("women8", women8);
	}
	public void setBoy8(Integer boy8) {
		if ((boy8 == null && this.values.get("boy8") != null) 
			|| (boy8 != null && !boy8.equals(this.values.get("boy8")))) {
			this.modified = UPDATED;
		}
		this.values.put("boy8", boy8);
	}
	public void setGirl8(Integer girl8) {
		if ((girl8 == null && this.values.get("girl8") != null) 
			|| (girl8 != null && !girl8.equals(this.values.get("girl8")))) {
			this.modified = UPDATED;
		}
		this.values.put("girl8", girl8);
	}
	public void setBehrflag(Integer behrflag) {
		if ((behrflag == null && this.values.get("behrflag") != null) 
			|| (behrflag != null && !behrflag.equals(this.values.get("behrflag")))) {
			this.modified = UPDATED;
		}
		this.values.put("behrflag", behrflag);
	}
	public void setPrice(Integer price) {
		if ((price == null && this.values.get("price") != null) 
			|| (price != null && !price.equals(this.values.get("price")))) {
			this.modified = UPDATED;
		}
		this.values.put("price", price);
	}
	public void setUppguine(Float uppguine) {
		if ((uppguine == null && this.values.get("uppguine") != null) 
			|| (uppguine != null && !uppguine.equals(this.values.get("uppguine")))) {
			this.modified = UPDATED;
		}
		this.values.put("uppguine", uppguine);
	}
	public void setVoyagimp(Float voyagimp) {
		if ((voyagimp == null && this.values.get("voyagimp") != null) 
			|| (voyagimp != null && !voyagimp.equals(this.values.get("voyagimp")))) {
			this.modified = UPDATED;
		}
		this.values.put("voyagimp", voyagimp);
	}
	public void setTimcoast(Float timcoast) {
		if ((timcoast == null && this.values.get("timcoast") != null) 
			|| (timcoast != null && !timcoast.equals(this.values.get("timcoast")))) {
			this.modified = UPDATED;
		}
		this.values.put("timcoast", timcoast);
	}
	public void setSlavpday(Float slavpday) {
		if ((slavpday == null && this.values.get("slavpday") != null) 
			|| (slavpday != null && !slavpday.equals(this.values.get("slavpday")))) {
			this.modified = UPDATED;
		}
		this.values.put("slavpday", slavpday);
	}
	public void setRountrip(Float rountrip) {
		if ((rountrip == null && this.values.get("rountrip") != null) 
			|| (rountrip != null && !rountrip.equals(this.values.get("rountrip")))) {
			this.modified = UPDATED;
		}
		this.values.put("rountrip", rountrip);
	}
	public void setSlavpvoy(Float slavpvoy) {
		if ((slavpvoy == null && this.values.get("slavpvoy") != null) 
			|| (slavpvoy != null && !slavpvoy.equals(this.values.get("slavpvoy")))) {
			this.modified = UPDATED;
		}
		this.values.put("slavpvoy", slavpvoy);
	}
	public void setSlxp1000(Float slxp1000) {
		if ((slxp1000 == null && this.values.get("slxp1000") != null) 
			|| (slxp1000 != null && !slxp1000.equals(this.values.get("slxp1000")))) {
			this.modified = UPDATED;
		}
		this.values.put("slxp1000", slxp1000);
	}
	public void setInsurre2(Float insurre2) {
		if ((insurre2 == null && this.values.get("insurre2") != null) 
			|| (insurre2 != null && !insurre2.equals(this.values.get("insurre2")))) {
			this.modified = UPDATED;
		}
		this.values.put("insurre2", insurre2);
	}
	public void setTimslave(Float timslave) {
		if ((timslave == null && this.values.get("timslave") != null) 
			|| (timslave != null && !timslave.equals(this.values.get("timslave")))) {
			this.modified = UPDATED;
		}
		this.values.put("timslave", timslave);
	}
	public void setToncateg(Integer toncateg) {
		if ((toncateg == null && this.values.get("toncateg") != null) 
			|| (toncateg != null && !toncateg.equals(this.values.get("toncateg")))) {
			this.modified = UPDATED;
		}
		this.values.put("toncateg", toncateg);
	}
	public void setEvgreen(Integer evgreen) {
		if ((evgreen == null && this.values.get("evgreen") != null) 
			|| (evgreen != null && !evgreen.equals(this.values.get("evgreen")))) {
			this.modified = UPDATED;
		}
		this.values.put("evgreen", evgreen);
	}
	public void setWomrat1(Float womrat1) {
		if ((womrat1 == null && this.values.get("womrat1") != null) 
			|| (womrat1 != null && !womrat1.equals(this.values.get("womrat1")))) {
			this.modified = UPDATED;
		}
		this.values.put("womrat1", womrat1);
	}
	public void setWomrat3(Float womrat3) {
		if ((womrat3 == null && this.values.get("womrat3") != null) 
			|| (womrat3 != null && !womrat3.equals(this.values.get("womrat3")))) {
			this.modified = UPDATED;
		}
		this.values.put("womrat3", womrat3);
	}
	public void setWomrat7(Float womrat7) {
		if ((womrat7 == null && this.values.get("womrat7") != null) 
			|| (womrat7 != null && !womrat7.equals(this.values.get("womrat7")))) {
			this.modified = UPDATED;
		}
		this.values.put("womrat7", womrat7);
	}
	public void setMenrat7(Float menrat7) {
		if ((menrat7 == null && this.values.get("menrat7") != null) 
			|| (menrat7 != null && !menrat7.equals(this.values.get("menrat7")))) {
			this.modified = UPDATED;
		}
		this.values.put("menrat7", menrat7);
	}
	public void setMenrat3(Float menrat3) {
		if ((menrat3 == null && this.values.get("menrat3") != null) 
			|| (menrat3 != null && !menrat3.equals(this.values.get("menrat3")))) {
			this.modified = UPDATED;
		}
		this.values.put("menrat3", menrat3);
	}
	public void setMenrat1(Float menrat1) {
		if ((menrat1 == null && this.values.get("menrat1") != null) 
			|| (menrat1 != null && !menrat1.equals(this.values.get("menrat1")))) {
			this.modified = UPDATED;
		}
		this.values.put("menrat1", menrat1);
	}
	public void setGirlrat1(Float girlrat1) {
		if ((girlrat1 == null && this.values.get("girlrat1") != null) 
			|| (girlrat1 != null && !girlrat1.equals(this.values.get("girlrat1")))) {
			this.modified = UPDATED;
		}
		this.values.put("girlrat1", girlrat1);
	}
	public void setGirlrat3(Float girlrat3) {
		if ((girlrat3 == null && this.values.get("girlrat3") != null) 
			|| (girlrat3 != null && !girlrat3.equals(this.values.get("girlrat3")))) {
			this.modified = UPDATED;
		}
		this.values.put("girlrat3", girlrat3);
	}
	public void setGirlrat7(Float girlrat7) {
		if ((girlrat7 == null && this.values.get("girlrat7") != null) 
			|| (girlrat7 != null && !girlrat7.equals(this.values.get("girlrat7")))) {
			this.modified = UPDATED;
		}
		this.values.put("girlrat7", girlrat7);
	}
	public void setBoyrat7(Float boyrat7) {
		if ((boyrat7 == null && this.values.get("boyrat7") != null) 
			|| (boyrat7 != null && !boyrat7.equals(this.values.get("boyrat7")))) {
			this.modified = UPDATED;
		}
		this.values.put("boyrat7", boyrat7);
	}
	public void setBoyrat3(Float boyrat3) {
		if ((boyrat3 == null && this.values.get("boyrat3") != null) 
			|| (boyrat3 != null && !boyrat3.equals(this.values.get("boyrat3")))) {
			this.modified = UPDATED;
		}
		this.values.put("boyrat3", boyrat3);
	}
	public void setBoyrat1(Float boyrat1) {
		if ((boyrat1 == null && this.values.get("boyrat1") != null) 
			|| (boyrat1 != null && !boyrat1.equals(this.values.get("boyrat1")))) {
			this.modified = UPDATED;
		}
		this.values.put("boyrat1", boyrat1);
	}
	public void setBehmimp2(Integer behmimp2) {
		if ((behmimp2 == null && this.values.get("behmimp2") != null) 
			|| (behmimp2 != null && !behmimp2.equals(this.values.get("behmimp2")))) {
			this.modified = UPDATED;
		}
		this.values.put("behmimp2", behmimp2);
	}
	public void setBehximp2(Float behximp2) {
		if ((behximp2 == null && this.values.get("behximp2") != null) 
			|| (behximp2 != null && !behximp2.equals(this.values.get("behximp2")))) {
			this.modified = UPDATED;
		}
		this.values.put("behximp2", behximp2);
	}
	public void setPoundprice(Float poundprice) {
		if ((poundprice == null && this.values.get("poundprice") != null) 
			|| (poundprice != null && !poundprice.equals(this.values.get("poundprice")))) {
			this.modified = UPDATED;
		}
		this.values.put("poundprice", poundprice);
	}
	public void setTermmth(Integer termmth) {
		if ((termmth == null && this.values.get("termmth") != null) 
			|| (termmth != null && !termmth.equals(this.values.get("termmth")))) {
			this.modified = UPDATED;
		}
		this.values.put("termmth", termmth);
	}
	public void setIntrate(Integer intrate) {
		if ((intrate == null && this.values.get("intrate") != null) 
			|| (intrate != null && !intrate.equals(this.values.get("intrate")))) {
			this.modified = UPDATED;
		}
		this.values.put("intrate", intrate);
	}
	public void setCashpric(Float cashpric) {
		if ((cashpric == null && this.values.get("cashpric") != null) 
			|| (cashpric != null && !cashpric.equals(this.values.get("cashpric")))) {
			this.modified = UPDATED;
		}
		this.values.put("cashpric", cashpric);
	}
	public void setJamcaspr(Float jamcaspr) {
		if ((jamcaspr == null && this.values.get("jamcaspr") != null) 
			|| (jamcaspr != null && !jamcaspr.equals(this.values.get("jamcaspr")))) {
			this.modified = UPDATED;
		}
		this.values.put("jamcaspr", jamcaspr);
	}
	public void setLocurmnl(Float locurmnl) {
		if ((locurmnl == null && this.values.get("locurmnl") != null) 
			|| (locurmnl != null && !locurmnl.equals(this.values.get("locurmnl")))) {
			this.modified = UPDATED;
		}
		this.values.put("locurmnl", locurmnl);
	}
	public void setExchrate(String exchrate) {
		if ((exchrate == null && this.values.get("exchrate") != null) 
			|| (exchrate != null && !exchrate.equals(this.values.get("exchrate")))) {
			this.modified = UPDATED;
		}
		this.values.put("exchrate", exchrate);
	}
	public void setFrencpri(Float frencpri) {
		if ((frencpri == null && this.values.get("frencpri") != null) 
			|| (frencpri != null && !frencpri.equals(this.values.get("frencpri")))) {
			this.modified = UPDATED;
		}
		this.values.put("frencpri", frencpri);
	}
	public void setFrnprinl(Float frnprinl) {
		if ((frnprinl == null && this.values.get("frnprinl") != null) 
			|| (frnprinl != null && !frnprinl.equals(this.values.get("frnprinl")))) {
			this.modified = UPDATED;
		}
		this.values.put("frnprinl", frnprinl);
	}
	public void setNonpay(Float nonpay) {
		if ((nonpay == null && this.values.get("nonpay") != null) 
			|| (nonpay != null && !nonpay.equals(this.values.get("nonpay")))) {
			this.modified = UPDATED;
		}
		this.values.put("nonpay", nonpay);
	}
	public void setSeneg(Float seneg) {
		if ((seneg == null && this.values.get("seneg") != null) 
			|| (seneg != null && !seneg.equals(this.values.get("seneg")))) {
			this.modified = UPDATED;
		}
		this.values.put("seneg", seneg);
	}
	public void setSl(Float sl) {
		if ((sl == null && this.values.get("sl") != null) 
			|| (sl != null && !sl.equals(this.values.get("sl")))) {
			this.modified = UPDATED;
		}
		this.values.put("sl", sl);
	}
	public void setWwc(Float wwc) {
		if ((wwc == null && this.values.get("wwc") != null) 
			|| (wwc != null && !wwc.equals(this.values.get("wwc")))) {
			this.modified = UPDATED;
		}
		this.values.put("wwc", wwc);
	}
	public void setSlperton(Float slperton) {
		if ((slperton == null && this.values.get("slperton") != null) 
			|| (slperton != null && !slperton.equals(this.values.get("slperton")))) {
			this.modified = UPDATED;
		}
		this.values.put("slperton", slperton);
	}
	public void setTonpercr(Float tonpercr) {
		if ((tonpercr == null && this.values.get("tonpercr") != null) 
			|| (tonpercr != null && !tonpercr.equals(this.values.get("tonpercr")))) {
			this.modified = UPDATED;
		}
		this.values.put("tonpercr", tonpercr);
	}
	public void setPortugus(Float portugus) {
		if ((portugus == null && this.values.get("portugus") != null) 
			|| (portugus != null && !portugus.equals(this.values.get("portugus")))) {
			this.modified = UPDATED;
		}
		this.values.put("portugus", portugus);
	}
	public void setManol(Integer manol) {
		if ((manol == null && this.values.get("manol") != null) 
			|| (manol != null && !manol.equals(this.values.get("manol")))) {
			this.modified = UPDATED;
		}
		this.values.put("manol", manol);
	}
	public void setPortug2(Float portug2) {
		if ((portug2 == null && this.values.get("portug2") != null) 
			|| (portug2 != null && !portug2.equals(this.values.get("portug2")))) {
			this.modified = UPDATED;
		}
		this.values.put("portug2", portug2);
	}
	public void setDeptamer(Float deptamer) {
		if ((deptamer == null && this.values.get("deptamer") != null) 
			|| (deptamer != null && !deptamer.equals(this.values.get("deptamer")))) {
			this.modified = UPDATED;
		}
		this.values.put("deptamer", deptamer);
	}
	public void setGirl5(Integer girl5) {
		if ((girl5 == null && this.values.get("girl5") != null) 
			|| (girl5 != null && !girl5.equals(this.values.get("girl5")))) {
			this.modified = UPDATED;
		}
		this.values.put("girl5", girl5);
	}
	public void setBoy5(Integer boy5) {
		if ((boy5 == null && this.values.get("boy5") != null) 
			|| (boy5 != null && !boy5.equals(this.values.get("boy5")))) {
			this.modified = UPDATED;
		}
		this.values.put("boy5", boy5);
	}
	public void setMen5(Integer men5) {
		if ((men5 == null && this.values.get("men5") != null) 
			|| (men5 != null && !men5.equals(this.values.get("men5")))) {
			this.modified = UPDATED;
		}
		this.values.put("men5", men5);
	}
	public void setWomen5(Integer women5) {
		if ((women5 == null && this.values.get("women5") != null) 
			|| (women5 != null && !women5.equals(this.values.get("women5")))) {
			this.modified = UPDATED;
		}
		this.values.put("women5", women5);
	}
	public void setChild5(Integer child5) {
		if ((child5 == null && this.values.get("child5") != null) 
			|| (child5 != null && !child5.equals(this.values.get("child5")))) {
			this.modified = UPDATED;
		}
		this.values.put("child5", child5);
	}
	public void setMale5(Integer male5) {
		if ((male5 == null && this.values.get("male5") != null) 
			|| (male5 != null && !male5.equals(this.values.get("male5")))) {
			this.modified = UPDATED;
		}
		this.values.put("male5", male5);
	}
	public void setFemale5(Integer female5) {
		if ((female5 == null && this.values.get("female5") != null) 
			|| (female5 != null && !female5.equals(this.values.get("female5")))) {
			this.modified = UPDATED;
		}
		this.values.put("female5", female5);
	}
	public void setArrport2(Integer arrport2) {
		if ((arrport2 == null && this.values.get("arrport2") != null) 
			|| (arrport2 != null && !arrport2.equals(this.values.get("arrport2")))) {
			this.modified = UPDATED;
		}
		this.values.put("arrport2", arrport2);
	}
	public void setNsp(Integer nsp) {
		if ((nsp == null && this.values.get("nsp") != null) 
			|| (nsp != null && !nsp.equals(this.values.get("nsp")))) {
			this.modified = UPDATED;
		}
		this.values.put("nsp", nsp);
	}
	public void setNna(Integer nna) {
		if ((nna == null && this.values.get("nna") != null) 
			|| (nna != null && !nna.equals(this.values.get("nna")))) {
			this.modified = UPDATED;
		}
		this.values.put("nna", nna);
	}
	public void setInfant3(Integer infant3) {
		if ((infant3 == null && this.values.get("infant3") != null) 
			|| (infant3 != null && !infant3.equals(this.values.get("infant3")))) {
			this.modified = UPDATED;
		}
		this.values.put("infant3", infant3);
	}
	public void setInfants3(Integer infants3) {
		if ((infants3 == null && this.values.get("infants3") != null) 
			|| (infants3 != null && !infants3.equals(this.values.get("infants3")))) {
			this.modified = UPDATED;
		}
		this.values.put("infants3", infants3);
	}
	public void setInfant1(Integer infant1) {
		if ((infant1 == null && this.values.get("infant1") != null) 
			|| (infant1 != null && !infant1.equals(this.values.get("infant1")))) {
			this.modified = UPDATED;
		}
		this.values.put("infant1", infant1);
	}
	public void setKleimerg(Float kleimerg) {
		if ((kleimerg == null && this.values.get("kleimerg") != null) 
			|| (kleimerg != null && !kleimerg.equals(this.values.get("kleimerg")))) {
			this.modified = UPDATED;
		}
		this.values.put("kleimerg", kleimerg);
	}
	public void setKlemerg2(Float klemerg2) {
		if ((klemerg2 == null && this.values.get("klemerg2") != null) 
			|| (klemerg2 != null && !klemerg2.equals(this.values.get("klemerg2")))) {
			this.modified = UPDATED;
		}
		this.values.put("klemerg2", klemerg2);
	}
	public void setAdult5(Integer adult5) {
		if ((adult5 == null && this.values.get("adult5") != null) 
			|| (adult5 != null && !adult5.equals(this.values.get("adult5")))) {
			this.modified = UPDATED;
		}
		this.values.put("adult5", adult5);
	}
	public void setBahijelm(Float bahijelm) {
		if ((bahijelm == null && this.values.get("bahijelm") != null) 
			|| (bahijelm != null && !bahijelm.equals(this.values.get("bahijelm")))) {
			this.modified = UPDATED;
		}
		this.values.put("bahijelm", bahijelm);
	}
	public void setAdult2(Integer adult2) {
		if ((adult2 == null && this.values.get("adult2") != null) 
			|| (adult2 != null && !adult2.equals(this.values.get("adult2")))) {
			this.modified = UPDATED;
		}
		this.values.put("adult2", adult2);
	}
	public void setAdult4(Integer adult4) {
		if ((adult4 == null && this.values.get("adult4") != null) 
			|| (adult4 != null && !adult4.equals(this.values.get("adult4")))) {
			this.modified = UPDATED;
		}
		this.values.put("adult4", adult4);
	}
	public void setInfant4(Integer infant4) {
		if ((infant4 == null && this.values.get("infant4") != null) 
			|| (infant4 != null && !infant4.equals(this.values.get("infant4")))) {
			this.modified = UPDATED;
		}
		this.values.put("infant4", infant4);
	}
	public void setVoyagid2(Float voyagid2) {
		if ((voyagid2 == null && this.values.get("voyagid2") != null) 
			|| (voyagid2 != null && !voyagid2.equals(this.values.get("voyagid2")))) {
			this.modified = UPDATED;
		}
		this.values.put("voyagid2", voyagid2);
	}
	public void setNewmina(Integer newmina) {
		if ((newmina == null && this.values.get("newmina") != null) 
			|| (newmina != null && !newmina.equals(this.values.get("newmina")))) {
			this.modified = UPDATED;
		}
		this.values.put("newmina", newmina);
	}
	public void setCrew(Integer crew) {
		if ((crew == null && this.values.get("crew") != null) 
			|| (crew != null && !crew.equals(this.values.get("crew")))) {
			this.modified = UPDATED;
		}
		this.values.put("crew", crew);
	}
	public void setGuns2(Integer guns2) {
		if ((guns2 == null && this.values.get("guns2") != null) 
			|| (guns2 != null && !guns2.equals(this.values.get("guns2")))) {
			this.modified = UPDATED;
		}
		this.values.put("guns2", guns2);
	}
	public void setMen7(Integer men7) {
		if ((men7 == null && this.values.get("men7") != null) 
			|| (men7 != null && !men7.equals(this.values.get("men7")))) {
			this.modified = UPDATED;
		}
		this.values.put("men7", men7);
	}
	public void setMale7(Integer male7) {
		if ((male7 == null && this.values.get("male7") != null) 
			|| (male7 != null && !male7.equals(this.values.get("male7")))) {
			this.modified = UPDATED;
		}
		this.values.put("male7", male7);
	}
	public void setFemale7(Integer female7) {
		if ((female7 == null && this.values.get("female7") != null) 
			|| (female7 != null && !female7.equals(this.values.get("female7")))) {
			this.modified = UPDATED;
		}
		this.values.put("female7", female7);
	}
	public void setWomen7(Integer women7) {
		if ((women7 == null && this.values.get("women7") != null) 
			|| (women7 != null && !women7.equals(this.values.get("women7")))) {
			this.modified = UPDATED;
		}
		this.values.put("women7", women7);
	}
	public void setAdult7(Integer adult7) {
		if ((adult7 == null && this.values.get("adult7") != null) 
			|| (adult7 != null && !adult7.equals(this.values.get("adult7")))) {
			this.modified = UPDATED;
		}
		this.values.put("adult7", adult7);
	}
	public void setBoy7(Integer boy7) {
		if ((boy7 == null && this.values.get("boy7") != null) 
			|| (boy7 != null && !boy7.equals(this.values.get("boy7")))) {
			this.modified = UPDATED;
		}
		this.values.put("boy7", boy7);
	}
	public void setGirl7(Integer girl7) {
		if ((girl7 == null && this.values.get("girl7") != null) 
			|| (girl7 != null && !girl7.equals(this.values.get("girl7")))) {
			this.modified = UPDATED;
		}
		this.values.put("girl7", girl7);
	}
	public void setChild7(Integer child7) {
		if ((child7 == null && this.values.get("child7") != null) 
			|| (child7 != null && !child7.equals(this.values.get("child7")))) {
			this.modified = UPDATED;
		}
		this.values.put("child7", child7);
	}
	public void setVoy3imp(Integer voy3imp) {
		if ((voy3imp == null && this.values.get("voy3imp") != null) 
			|| (voy3imp != null && !voy3imp.equals(this.values.get("voy3imp")))) {
			this.modified = UPDATED;
		}
		this.values.put("voy3imp", voy3imp);
	}
	public void setSlavcapt(Float slavcapt) {
		if ((slavcapt == null && this.values.get("slavcapt") != null) 
			|| (slavcapt != null && !slavcapt.equals(this.values.get("slavcapt")))) {
			this.modified = UPDATED;
		}
		this.values.put("slavcapt", slavcapt);
	}
	public void setMajbycon(Float majbycon) {
		if ((majbycon == null && this.values.get("majbycon") != null) 
			|| (majbycon != null && !majbycon.equals(this.values.get("majbycon")))) {
			this.modified = UPDATED;
		}
		this.values.put("majbycon", majbycon);
	}
	public void setTabexpo(Integer tabexpo) {
		if ((tabexpo == null && this.values.get("tabexpo") != null) 
			|| (tabexpo != null && !tabexpo.equals(this.values.get("tabexpo")))) {
			this.modified = UPDATED;
		}
		this.values.put("tabexpo", tabexpo);
	}
	public void setRice(Float rice) {
		if ((rice == null && this.values.get("rice") != null) 
			|| (rice != null && !rice.equals(this.values.get("rice")))) {
			this.modified = UPDATED;
		}
		this.values.put("rice", rice);
	}
	public void setSlavemx7(Float slavemx7) {
		if ((slavemx7 == null && this.values.get("slavemx7") != null) 
			|| (slavemx7 != null && !slavemx7.equals(this.values.get("slavemx7")))) {
			this.modified = UPDATED;
		}
		this.values.put("slavemx7", slavemx7);
	}
	public void setSpanishamer(Float spanishamer) {
		if ((spanishamer == null && this.values.get("spanishamer") != null) 
			|| (spanishamer != null && !spanishamer.equals(this.values.get("spanishamer")))) {
			this.modified = UPDATED;
		}
		this.values.put("spanishamer", spanishamer);
	}
	public void setYear25a(Float year25a) {
		if ((year25a == null && this.values.get("year25a") != null) 
			|| (year25a != null && !year25a.equals(this.values.get("year25a")))) {
			this.modified = UPDATED;
		}
		this.values.put("year25a", year25a);
	}
	public void setMenvymrtrat(Float menvymrtrat) {
		if ((menvymrtrat == null && this.values.get("menvymrtrat") != null) 
			|| (menvymrtrat != null && !menvymrtrat.equals(this.values.get("menvymrtrat")))) {
			this.modified = UPDATED;
		}
		this.values.put("menvymrtrat", menvymrtrat);
	}
	public void setWomenvymrtrat(Float womenvymrtrat) {
		if ((womenvymrtrat == null && this.values.get("womenvymrtrat") != null) 
			|| (womenvymrtrat != null && !womenvymrtrat.equals(this.values.get("womenvymrtrat")))) {
			this.modified = UPDATED;
		}
		this.values.put("womenvymrtrat", womenvymrtrat);
	}
	public void setBoyvymrtrat(Float boyvymrtrat) {
		if ((boyvymrtrat == null && this.values.get("boyvymrtrat") != null) 
			|| (boyvymrtrat != null && !boyvymrtrat.equals(this.values.get("boyvymrtrat")))) {
			this.modified = UPDATED;
		}
		this.values.put("boyvymrtrat", boyvymrtrat);
	}
	public void setGirlvymrtrat(Float girlvymrtrat) {
		if ((girlvymrtrat == null && this.values.get("girlvymrtrat") != null) 
			|| (girlvymrtrat != null && !girlvymrtrat.equals(this.values.get("girlvymrtrat")))) {
			this.modified = UPDATED;
		}
		this.values.put("girlvymrtrat", girlvymrtrat);
	}
	public void setCubaperiod(Float cubaperiod) {
		if ((cubaperiod == null && this.values.get("cubaperiod") != null) 
			|| (cubaperiod != null && !cubaperiod.equals(this.values.get("cubaperiod")))) {
			this.modified = UPDATED;
		}
		this.values.put("cubaperiod", cubaperiod);
	}
	public void setDatepl(DepDefinition datepl) {
		if ((datepl == null && this.values.get("datepl") != null) 
			|| (datepl != null && !datepl.equals(this.values.get("datepl")))) {
			this.modified = UPDATED;
		}
		this.values.put("datepl", datepl);
	}
	public void setEmbport(PurchasePort embport) {
		if ((embport == null && this.values.get("embport") != null) 
			|| (embport != null && !embport.equals(this.values.get("embport")))) {
			this.modified = UPDATED;
		}
		this.values.put("embport", embport);
	}
	public void setArrport(IntDisembPort arrport) {
		if ((arrport == null && this.values.get("arrport") != null) 
			|| (arrport != null && !arrport.equals(this.values.get("arrport")))) {
			this.modified = UPDATED;
		}
		this.values.put("arrport", arrport);
	}
	public void setPlac1tra(PurchasePort plac1tra) {
		if ((plac1tra == null && this.values.get("plac1tra") != null) 
			|| (plac1tra != null && !plac1tra.equals(this.values.get("plac1tra")))) {
			this.modified = UPDATED;
		}
		this.values.put("plac1tra", plac1tra);
	}
	public void setDestin(RetPort destin) {
		if ((destin == null && this.values.get("destin") != null) 
			|| (destin != null && !destin.equals(this.values.get("destin")))) {
			this.modified = UPDATED;
		}
		this.values.put("destin", destin);
	}
	public void setPlac2tra(PurchasePort plac2tra) {
		if ((plac2tra == null && this.values.get("plac2tra") != null) 
			|| (plac2tra != null && !plac2tra.equals(this.values.get("plac2tra")))) {
			this.modified = UPDATED;
		}
		this.values.put("plac2tra", plac2tra);
	}
	public void setPlac3tra(PurchasePort plac3tra) {
		if ((plac3tra == null && this.values.get("plac3tra") != null) 
			|| (plac3tra != null && !plac3tra.equals(this.values.get("plac3tra")))) {
			this.modified = UPDATED;
		}
		this.values.put("plac3tra", plac3tra);
	}
	public void setNpafttra(OldWorldPlace npafttra) {
		if ((npafttra == null && this.values.get("npafttra") != null) 
			|| (npafttra != null && !npafttra.equals(this.values.get("npafttra")))) {
			this.modified = UPDATED;
		}
		this.values.put("npafttra", npafttra);
	}
	public void setSla1port(FirstDemPort sla1port) {
		if ((sla1port == null && this.values.get("sla1port") != null) 
			|| (sla1port != null && !sla1port.equals(this.values.get("sla1port")))) {
			this.modified = UPDATED;
		}
		this.values.put("sla1port", sla1port);
	}
	public void setAdpsale1(SecondDemPort adpsale1) {
		if ((adpsale1 == null && this.values.get("adpsale1") != null) 
			|| (adpsale1 != null && !adpsale1.equals(this.values.get("adpsale1")))) {
			this.modified = UPDATED;
		}
		this.values.put("adpsale1", adpsale1);
	}
	public void setAdpsale2(ThirdDemPort adpsale2) {
		if ((adpsale2 == null && this.values.get("adpsale2") != null) 
			|| (adpsale2 != null && !adpsale2.equals(this.values.get("adpsale2")))) {
			this.modified = UPDATED;
		}
		this.values.put("adpsale2", adpsale2);
	}
	public void setPortret(RetPort portret) {
		if ((portret == null && this.values.get("portret") != null) 
			|| (portret != null && !portret.equals(this.values.get("portret")))) {
			this.modified = UPDATED;
		}
		this.values.put("portret", portret);
	}
	public void setTontype(TonesType tontype) {
		if ((tontype == null && this.values.get("tontype") != null) 
			|| (tontype != null && !tontype.equals(this.values.get("tontype")))) {
			this.modified = UPDATED;
		}
		this.values.put("tontype", tontype);
	}
	public void setNational(ShipNationality national) {
		if ((national == null && this.values.get("national") != null) 
			|| (national != null && !national.equals(this.values.get("national")))) {
			this.modified = UPDATED;
		}
		this.values.put("national", national);
	}
	public void setEmbport2(PurchasePort embport2) {
		if ((embport2 == null && this.values.get("embport2") != null) 
			|| (embport2 != null && !embport2.equals(this.values.get("embport2")))) {
			this.modified = UPDATED;
		}
		this.values.put("embport2", embport2);
	}
	public void setPortdep(DeparturePort portdep) {
		if ((portdep == null && this.values.get("portdep") != null) 
			|| (portdep != null && !portdep.equals(this.values.get("portdep")))) {
			this.modified = UPDATED;
		}
		this.values.put("portdep", portdep);
	}
	public void setRig(VesselRig rig) {
		if ((rig == null && this.values.get("rig") != null) 
			|| (rig != null && !rig.equals(this.values.get("rig")))) {
			this.modified = UPDATED;
		}
		this.values.put("rig", rig);
	}
	public void setPlaccons(RetPort placcons) {
		if ((placcons == null && this.values.get("placcons") != null) 
			|| (placcons != null && !placcons.equals(this.values.get("placcons")))) {
			this.modified = UPDATED;
		}
		this.values.put("placcons", placcons);
	}
	public void setMajselpt(FirstDemPort majselpt) {
		if ((majselpt == null && this.values.get("majselpt") != null) 
			|| (majselpt != null && !majselpt.equals(this.values.get("majselpt")))) {
			this.modified = UPDATED;
		}
		this.values.put("majselpt", majselpt);
	}
	public void setMajbuypt(PurchasePort majbuypt) {
		if ((majbuypt == null && this.values.get("majbuypt") != null) 
			|| (majbuypt != null && !majbuypt.equals(this.values.get("majbuypt")))) {
			this.modified = UPDATED;
		}
		this.values.put("majbuypt", majbuypt);
	}
	public void setNatinimp(ImputedNation natinimp) {
		if ((natinimp == null && this.values.get("natinimp") != null) 
			|| (natinimp != null && !natinimp.equals(this.values.get("natinimp")))) {
			this.modified = UPDATED;
		}
		this.values.put("natinimp", natinimp);
	}
	public void setDeptreg(DepartureRegion deptreg) {
		if ((deptreg == null && this.values.get("deptreg") != null) 
			|| (deptreg != null && !deptreg.equals(this.values.get("deptreg")))) {
			this.modified = UPDATED;
		}
		this.values.put("deptreg", deptreg);
	}
	public void setRetrnreg(DepartureRegion retrnreg) {
		if ((retrnreg == null && this.values.get("retrnreg") != null) 
			|| (retrnreg != null && !retrnreg.equals(this.values.get("retrnreg")))) {
			this.modified = UPDATED;
		}
		this.values.put("retrnreg", retrnreg);
	}
	public void setRegisreg(DepartureRegion regisreg) {
		if ((regisreg == null && this.values.get("regisreg") != null) 
			|| (regisreg != null && !regisreg.equals(this.values.get("regisreg")))) {
			this.modified = UPDATED;
		}
		this.values.put("regisreg", regisreg);
	}
	public void setYear100(Year100 year100) {
		if ((year100 == null && this.values.get("year100") != null) 
			|| (year100 != null && !year100.equals(this.values.get("year100")))) {
			this.modified = UPDATED;
		}
		this.values.put("year100", year100);
	}
	public void setYear5(Year5 year5) {
		if ((year5 == null && this.values.get("year5") != null) 
			|| (year5 != null && !year5.equals(this.values.get("year5")))) {
			this.modified = UPDATED;
		}
		this.values.put("year5", year5);
	}
	public void setYear25(Year25 year25) {
		if ((year25 == null && this.values.get("year25") != null) 
			|| (year25 != null && !year25.equals(this.values.get("year25")))) {
			this.modified = UPDATED;
		}
		this.values.put("year25", year25);
	}
	public void setRegem1(EmbRegion regem1) {
		if ((regem1 == null && this.values.get("regem1") != null) 
			|| (regem1 != null && !regem1.equals(this.values.get("regem1")))) {
			this.modified = UPDATED;
		}
		this.values.put("regem1", regem1);
	}
	public void setRegem2(EmbRegion regem2) {
		if ((regem2 == null && this.values.get("regem2") != null) 
			|| (regem2 != null && !regem2.equals(this.values.get("regem2")))) {
			this.modified = UPDATED;
		}
		this.values.put("regem2", regem2);
	}
	public void setRegem3(EmbRegion regem3) {
		if ((regem3 == null && this.values.get("regem3") != null) 
			|| (regem3 != null && !regem3.equals(this.values.get("regem3")))) {
			this.modified = UPDATED;
		}
		this.values.put("regem3", regem3);
	}
	public void setEmbreg(EmbRegion embreg) {
		if ((embreg == null && this.values.get("embreg") != null) 
			|| (embreg != null && !embreg.equals(this.values.get("embreg")))) {
			this.modified = UPDATED;
		}
		this.values.put("embreg", embreg);
	}
	public void setEmbreg2(EmbRegion embreg2) {
		if ((embreg2 == null && this.values.get("embreg2") != null) 
			|| (embreg2 != null && !embreg2.equals(this.values.get("embreg2")))) {
			this.modified = UPDATED;
		}
		this.values.put("embreg2", embreg2);
	}
	public void setMajbuyrg(EmbRegion majbuyrg) {
		if ((majbuyrg == null && this.values.get("majbuyrg") != null) 
			|| (majbuyrg != null && !majbuyrg.equals(this.values.get("majbuyrg")))) {
			this.modified = UPDATED;
		}
		this.values.put("majbuyrg", majbuyrg);
	}
	public void setMajbyimp(EmbRegion majbyimp) {
		if ((majbyimp == null && this.values.get("majbyimp") != null) 
			|| (majbyimp != null && !majbyimp.equals(this.values.get("majbyimp")))) {
			this.modified = UPDATED;
		}
		this.values.put("majbyimp", majbyimp);
	}
	public void setRegdis1(DepartureRegion regdis1) {
		if ((regdis1 == null && this.values.get("regdis1") != null) 
			|| (regdis1 != null && !regdis1.equals(this.values.get("regdis1")))) {
			this.modified = UPDATED;
		}
		this.values.put("regdis1", regdis1);
	}
	public void setRegdis2(DepartureRegion regdis2) {
		if ((regdis2 == null && this.values.get("regdis2") != null) 
			|| (regdis2 != null && !regdis2.equals(this.values.get("regdis2")))) {
			this.modified = UPDATED;
		}
		this.values.put("regdis2", regdis2);
	}
	public void setRegdis3(DepartureRegion regdis3) {
		if ((regdis3 == null && this.values.get("regdis3") != null) 
			|| (regdis3 != null && !regdis3.equals(this.values.get("regdis3")))) {
			this.modified = UPDATED;
		}
		this.values.put("regdis3", regdis3);
	}
	public void setRegarrp(DepartureRegion regarrp) {
		if ((regarrp == null && this.values.get("regarrp") != null) 
			|| (regarrp != null && !regarrp.equals(this.values.get("regarrp")))) {
			this.modified = UPDATED;
		}
		this.values.put("regarrp", regarrp);
	}
	public void setMajselrg(DepartureRegion majselrg) {
		if ((majselrg == null && this.values.get("majselrg") != null) 
			|| (majselrg != null && !majselrg.equals(this.values.get("majselrg")))) {
			this.modified = UPDATED;
		}
		this.values.put("majselrg", majselrg);
	}
	public void setFate2(Fate2 fate2) {
		if ((fate2 == null && this.values.get("fate2") != null) 
			|| (fate2 != null && !fate2.equals(this.values.get("fate2")))) {
			this.modified = UPDATED;
		}
		this.values.put("fate2", fate2);
	}
	public void setFate3(Fate3 fate3) {
		if ((fate3 == null && this.values.get("fate3") != null) 
			|| (fate3 != null && !fate3.equals(this.values.get("fate3")))) {
			this.modified = UPDATED;
		}
		this.values.put("fate3", fate3);
	}
	public void setFate4(Fate4 fate4) {
		if ((fate4 == null && this.values.get("fate4") != null) 
			|| (fate4 != null && !fate4.equals(this.values.get("fate4")))) {
			this.modified = UPDATED;
		}
		this.values.put("fate4", fate4);
	}
	public void setMjselimp(ImpPrincipalRegion mjselimp) {
		if ((mjselimp == null && this.values.get("mjselimp") != null) 
			|| (mjselimp != null && !mjselimp.equals(this.values.get("mjselimp")))) {
			this.modified = UPDATED;
		}
		this.values.put("mjselimp", mjselimp);
	}
	public void setDeptreg1(DepartureRegion deptreg1) {
		if ((deptreg1 == null && this.values.get("deptreg1") != null) 
			|| (deptreg1 != null && !deptreg1.equals(this.values.get("deptreg1")))) {
			this.modified = UPDATED;
		}
		this.values.put("deptreg1", deptreg1);
	}
	public void setMjselrg1(DemBroadRegion mjselrg1) {
		if ((mjselrg1 == null && this.values.get("mjselrg1") != null) 
			|| (mjselrg1 != null && !mjselrg1.equals(this.values.get("mjselrg1")))) {
			this.modified = UPDATED;
		}
		this.values.put("mjselrg1", mjselrg1);
	}
	public void setConstreg(DepartureRegion constreg) {
		if ((constreg == null && this.values.get("constreg") != null) 
			|| (constreg != null && !constreg.equals(this.values.get("constreg")))) {
			this.modified = UPDATED;
		}
		this.values.put("constreg", constreg);
	}
	public void setStatus(Status status) {
		if ((status == null && this.values.get("status") != null) 
			|| (status != null && !status.equals(this.values.get("status")))) {
			this.modified = UPDATED;
		}
		this.values.put("status", status);
	}
	public void setMajbyim3(MajByIm majbyim3) {
		if ((majbyim3 == null && this.values.get("majbyim3") != null) 
			|| (majbyim3 != null && !majbyim3.equals(this.values.get("majbyim3")))) {
			this.modified = UPDATED;
		}
		this.values.put("majbyim3", majbyim3);
	}
	public void setEditor(Editor editor) {
		if ((editor == null && this.values.get("editor") != null) 
			|| (editor != null && !editor.equals(this.values.get("editor")))) {
			this.modified = UPDATED;
		}
		this.values.put("editor", editor);
	}
	public void setNewf(New newf) {
		if ((newf == null && this.values.get("newf") != null) 
			|| (newf != null && !newf.equals(this.values.get("newf")))) {
			this.modified = UPDATED;
		}
		this.values.put("newf", newf);
	}
	public void setFilter_$(Filter filter_$) {
		if ((filter_$ == null && this.values.get("filter_$") != null) 
			|| (filter_$ != null && !filter_$.equals(this.values.get("filter_$")))) {
			this.modified = UPDATED;
		}
		this.values.put("filter_$", filter_$);
	}
	public void setNatinim3(Natinim3 natinim3) {
		if ((natinim3 == null && this.values.get("natinim3") != null) 
			|| (natinim3 != null && !natinim3.equals(this.values.get("natinim3")))) {
			this.modified = UPDATED;
		}
		this.values.put("natinim3", natinim3);
	}
	public void setMjselrg2(Mjselrg2 mjselrg2) {
		if ((mjselrg2 == null && this.values.get("mjselrg2") != null) 
			|| (mjselrg2 != null && !mjselrg2.equals(this.values.get("mjselrg2")))) {
			this.modified = UPDATED;
		}
		this.values.put("mjselrg2", mjselrg2);
	}
	public void setNewamreg(Newamreg newamreg) {
		if ((newamreg == null && this.values.get("newamreg") != null) 
			|| (newamreg != null && !newamreg.equals(this.values.get("newamreg")))) {
			this.modified = UPDATED;
		}
		this.values.put("newamreg", newamreg);
	}
	public void setYearches(Yearches yearches) {
		if ((yearches == null && this.values.get("yearches") != null) 
			|| (yearches != null && !yearches.equals(this.values.get("yearches")))) {
			this.modified = UPDATED;
		}
		this.values.put("yearches", yearches);
	}
	public void setGc(Gc gc) {
		if ((gc == null && this.values.get("gc") != null) 
			|| (gc != null && !gc.equals(this.values.get("gc")))) {
			this.modified = UPDATED;
		}
		this.values.put("gc", gc);
	}
	public void setAngola(Angola angola) {
		if ((angola == null && this.values.get("angola") != null) 
			|| (angola != null && !angola.equals(this.values.get("angola")))) {
			this.modified = UPDATED;
		}
		this.values.put("angola", angola);
	}
	public void setSeafrica(Seafrica seafrica) {
		if ((seafrica == null && this.values.get("seafrica") != null) 
			|| (seafrica != null && !seafrica.equals(this.values.get("seafrica")))) {
			this.modified = UPDATED;
		}
		this.values.put("seafrica", seafrica);
	}
	public void setRegarrp2(DepartureRegion regarrp2) {
		if ((regarrp2 == null && this.values.get("regarrp2") != null) 
			|| (regarrp2 != null && !regarrp2.equals(this.values.get("regarrp2")))) {
			this.modified = UPDATED;
		}
		this.values.put("regarrp2", regarrp2);
	}
	public void setUserid(Userid userid) {
		if ((userid == null && this.values.get("userid") != null) 
			|| (userid != null && !userid.equals(this.values.get("userid")))) {
			this.modified = UPDATED;
		}
		this.values.put("userid", userid);
	}
	public void setXmimpflag(XmimpFlag xmimpflag) {
		if ((xmimpflag == null && this.values.get("xmimpflag") != null) 
			|| (xmimpflag != null && !xmimpflag.equals(this.values.get("xmimpflag")))) {
			this.modified = UPDATED;
		}
		this.values.put("xmimpflag", xmimpflag);
	}
	public void setTemp2(Temp2 temp2) {
		if ((temp2 == null && this.values.get("temp2") != null) 
			|| (temp2 != null && !temp2.equals(this.values.get("temp2")))) {
			this.modified = UPDATED;
		}
		this.values.put("temp2", temp2);
	}
	public void setFlag2(Flag2 flag2) {
		if ((flag2 == null && this.values.get("flag2") != null) 
			|| (flag2 != null && !flag2.equals(this.values.get("flag2")))) {
			this.modified = UPDATED;
		}
		this.values.put("flag2", flag2);
	}
	public void setPrimarylast(PrimaryLast primarylast) {
		if ((primarylast == null && this.values.get("primarylast") != null) 
			|| (primarylast != null && !primarylast.equals(this.values.get("primarylast")))) {
			this.modified = UPDATED;
		}
		this.values.put("primarylast", primarylast);
	}
	public void setPrimarylast1(PrimaryLast primarylast1) {
		if ((primarylast1 == null && this.values.get("primarylast1") != null) 
			|| (primarylast1 != null && !primarylast1.equals(this.values.get("primarylast1")))) {
			this.modified = UPDATED;
		}
		this.values.put("primarylast1", primarylast1);
	}
	public void setCarib(Carib carib) {
		if ((carib == null && this.values.get("carib") != null) 
			|| (carib != null && !carib.equals(this.values.get("carib")))) {
			this.modified = UPDATED;
		}
		this.values.put("carib", carib);
	}
	public void setNatinim4(Natinim4 natinim4) {
		if ((natinim4 == null && this.values.get("natinim4") != null) 
			|| (natinim4 != null && !natinim4.equals(this.values.get("natinim4")))) {
			this.modified = UPDATED;
		}
		this.values.put("natinim4", natinim4);
	}
	public void setUs(Us us) {
		if ((us == null && this.values.get("us") != null) 
			|| (us != null && !us.equals(this.values.get("us")))) {
			this.modified = UPDATED;
		}
		this.values.put("us", us);
	}
	public void setMjselim2(DepartureRegion mjselim2) {
		if ((mjselim2 == null && this.values.get("mjselim2") != null) 
			|| (mjselim2 != null && !mjselim2.equals(this.values.get("mjselim2")))) {
			this.modified = UPDATED;
		}
		this.values.put("mjselim2", mjselim2);
	}
	public void setTemp(Temp temp) {
		if ((temp == null && this.values.get("temp") != null) 
			|| (temp != null && !temp.equals(this.values.get("temp")))) {
			this.modified = UPDATED;
		}
		this.values.put("temp", temp);
	}
	public Long getVoyageId() {
		return (Long)this.values.get("voyageId");
	}
	public String getShipname() {
		return (String)this.values.get("shipname");
	}
	public String getCaptaina() {
		return (String)this.values.get("captaina");
	}
	public String getCaptainb() {
		return (String)this.values.get("captainb");
	}
	public String getCaptainc() {
		return (String)this.values.get("captainc");
	}
	public Date getDatedep() {
		return (Date)this.values.get("datedep");
	}
	public Integer getNppretra() {
		return (Integer)this.values.get("nppretra");
	}
	public Date getD1slatr() {
		return (Date)this.values.get("d1slatr");
	}
	public Integer getNpprior() {
		return (Integer)this.values.get("npprior");
	}
	public Integer getTslavesp() {
		return (Integer)this.values.get("tslavesp");
	}
	public Integer getTslavesd() {
		return (Integer)this.values.get("tslavesd");
	}
	public Integer getSladvoy() {
		return (Integer)this.values.get("sladvoy");
	}
	public Integer getSlaarriv() {
		return (Integer)this.values.get("slaarriv");
	}
	public Date getRrdata31() {
		return (Date)this.values.get("rrdata31");
	}
	public Integer getSlas32() {
		return (Integer)this.values.get("slas32");
	}
	public Date getDatarr32() {
		return (Date)this.values.get("datarr32");
	}
	public Integer getSlas36() {
		return (Integer)this.values.get("slas36");
	}
	public Date getDatarr33() {
		return (Date)this.values.get("datarr33");
	}
	public Integer getSlas39() {
		return (Integer)this.values.get("slas39");
	}
	public Date getDdepamb() {
		return (Date)this.values.get("ddepamb");
	}
	public Date getDatarr4() {
		return (Date)this.values.get("datarr4");
	}
	public Integer getFate() {
		return (Integer)this.values.get("fate");
	}
	public String getSourcea() {
		return (String)this.values.get("sourcea");
	}
	public String getSourceb() {
		return (String)this.values.get("sourceb");
	}
	public String getSourcec() {
		return (String)this.values.get("sourcec");
	}
	public String getSourced() {
		return (String)this.values.get("sourced");
	}
	public String getSourcee() {
		return (String)this.values.get("sourcee");
	}
	public String getSourcef() {
		return (String)this.values.get("sourcef");
	}
	public String getSourceg() {
		return (String)this.values.get("sourceg");
	}
	public String getSourceh() {
		return (String)this.values.get("sourceh");
	}
	public String getSourcei() {
		return (String)this.values.get("sourcei");
	}
	public String getSourcej() {
		return (String)this.values.get("sourcej");
	}
	public String getSourcek() {
		return (String)this.values.get("sourcek");
	}
	public String getSourcel() {
		return (String)this.values.get("sourcel");
	}
	public String getSourcem() {
		return (String)this.values.get("sourcem");
	}
	public String getSourcen() {
		return (String)this.values.get("sourcen");
	}
	public String getSourceo() {
		return (String)this.values.get("sourceo");
	}
	public String getSourcep() {
		return (String)this.values.get("sourcep");
	}
	public String getSourceq() {
		return (String)this.values.get("sourceq");
	}
	public String getSourcer() {
		return (String)this.values.get("sourcer");
	}
	public Integer getSlintend() {
		return (Integer)this.values.get("slintend");
	}
	public Integer getTonnage() {
		return (Integer)this.values.get("tonnage");
	}
	public Integer getCrewdied() {
		return (Integer)this.values.get("crewdied");
	}
	public Integer getNcar13() {
		return (Integer)this.values.get("ncar13");
	}
	public Integer getNcar15() {
		return (Integer)this.values.get("ncar15");
	}
	public Integer getNcar17() {
		return (Integer)this.values.get("ncar17");
	}
	public Integer getSladamer() {
		return (Integer)this.values.get("sladamer");
	}
	public Integer getSaild1() {
		return (Integer)this.values.get("saild1");
	}
	public Integer getSaild2() {
		return (Integer)this.values.get("saild2");
	}
	public Integer getSaild3() {
		return (Integer)this.values.get("saild3");
	}
	public Integer getSaild4() {
		return (Integer)this.values.get("saild4");
	}
	public Integer getSaild5() {
		return (Integer)this.values.get("saild5");
	}
	public Integer getNdesert() {
		return (Integer)this.values.get("ndesert");
	}
	public Integer getSlinten2() {
		return (Integer)this.values.get("slinten2");
	}
	public Integer getGuns() {
		return (Integer)this.values.get("guns");
	}
	public Integer getVoyage() {
		return (Integer)this.values.get("voyage");
	}
	public Integer getCrew1() {
		return (Integer)this.values.get("crew1");
	}
	public Integer getChild2() {
		return (Integer)this.values.get("child2");
	}
	public Integer getChild3() {
		return (Integer)this.values.get("child3");
	}
	public Integer getYrreg() {
		return (Integer)this.values.get("yrreg");
	}
	public Integer getCrew3() {
		return (Integer)this.values.get("crew3");
	}
	public Integer getCrew4() {
		return (Integer)this.values.get("crew4");
	}
	public Integer getCrew5() {
		return (Integer)this.values.get("crew5");
	}
	public Integer getAdult1() {
		return (Integer)this.values.get("adult1");
	}
	public Integer getChild1() {
		return (Integer)this.values.get("child1");
	}
	public Integer getFemale1() {
		return (Integer)this.values.get("female1");
	}
	public Integer getMale1() {
		return (Integer)this.values.get("male1");
	}
	public Integer getMen1() {
		return (Integer)this.values.get("men1");
	}
	public Integer getWomen1() {
		return (Integer)this.values.get("women1");
	}
	public Integer getFemale2() {
		return (Integer)this.values.get("female2");
	}
	public Integer getMale2() {
		return (Integer)this.values.get("male2");
	}
	public Integer getMen2() {
		return (Integer)this.values.get("men2");
	}
	public Integer getWomen2() {
		return (Integer)this.values.get("women2");
	}
	public Integer getBoy2() {
		return (Integer)this.values.get("boy2");
	}
	public Integer getGirl2() {
		return (Integer)this.values.get("girl2");
	}
	public Integer getFemale3() {
		return (Integer)this.values.get("female3");
	}
	public Integer getMale3() {
		return (Integer)this.values.get("male3");
	}
	public Integer getMen3() {
		return (Integer)this.values.get("men3");
	}
	public Integer getWomen3() {
		return (Integer)this.values.get("women3");
	}
	public Integer getBoy3() {
		return (Integer)this.values.get("boy3");
	}
	public Integer getGirl3() {
		return (Integer)this.values.get("girl3");
	}
	public Integer getFemale4() {
		return (Integer)this.values.get("female4");
	}
	public Integer getMale4() {
		return (Integer)this.values.get("male4");
	}
	public Integer getMen4() {
		return (Integer)this.values.get("men4");
	}
	public Integer getWomen4() {
		return (Integer)this.values.get("women4");
	}
	public Integer getBoy4() {
		return (Integer)this.values.get("boy4");
	}
	public Integer getGirl4() {
		return (Integer)this.values.get("girl4");
	}
	public Integer getChild4() {
		return (Integer)this.values.get("child4");
	}
	public Integer getFemale6() {
		return (Integer)this.values.get("female6");
	}
	public Integer getMale6() {
		return (Integer)this.values.get("male6");
	}
	public Integer getMen6() {
		return (Integer)this.values.get("men6");
	}
	public Integer getWomen6() {
		return (Integer)this.values.get("women6");
	}
	public Integer getBoy6() {
		return (Integer)this.values.get("boy6");
	}
	public Integer getGirl6() {
		return (Integer)this.values.get("girl6");
	}
	public Integer getChild6() {
		return (Integer)this.values.get("child6");
	}
	public Integer getCrew2() {
		return (Integer)this.values.get("crew2");
	}
	public Integer getInfantm3() {
		return (Integer)this.values.get("infantm3");
	}
	public Integer getInfantf3() {
		return (Integer)this.values.get("infantf3");
	}
	public Integer getSladafri() {
		return (Integer)this.values.get("sladafri");
	}
	public Integer getSladied5() {
		return (Integer)this.values.get("sladied5");
	}
	public Integer getSladied4() {
		return (Integer)this.values.get("sladied4");
	}
	public Integer getSladied2() {
		return (Integer)this.values.get("sladied2");
	}
	public Integer getSladied1() {
		return (Integer)this.values.get("sladied1");
	}
	public Integer getSladied3() {
		return (Integer)this.values.get("sladied3");
	}
	public Integer getSladied6() {
		return (Integer)this.values.get("sladied6");
	}
	public Integer getInsurrec() {
		return (Integer)this.values.get("insurrec");
	}
	public Integer getAdult3() {
		return (Integer)this.values.get("adult3");
	}
	public String getOwnera() {
		return (String)this.values.get("ownera");
	}
	public String getOwnerb() {
		return (String)this.values.get("ownerb");
	}
	public String getOwnerc() {
		return (String)this.values.get("ownerc");
	}
	public String getOwnerd() {
		return (String)this.values.get("ownerd");
	}
	public String getOwnere() {
		return (String)this.values.get("ownere");
	}
	public String getOwnerf() {
		return (String)this.values.get("ownerf");
	}
	public String getOwnerg() {
		return (String)this.values.get("ownerg");
	}
	public String getOwnerh() {
		return (String)this.values.get("ownerh");
	}
	public String getOwneri() {
		return (String)this.values.get("owneri");
	}
	public String getOwnerj() {
		return (String)this.values.get("ownerj");
	}
	public String getOwnerk() {
		return (String)this.values.get("ownerk");
	}
	public String getOwnerl() {
		return (String)this.values.get("ownerl");
	}
	public String getOwnerm() {
		return (String)this.values.get("ownerm");
	}
	public String getOwnern() {
		return (String)this.values.get("ownern");
	}
	public String getOwnero() {
		return (String)this.values.get("ownero");
	}
	public String getOwnerp() {
		return (String)this.values.get("ownerp");
	}
	public Integer getYearaf() {
		return (Integer)this.values.get("yearaf");
	}
	public Integer getYeardep() {
		return (Integer)this.values.get("yeardep");
	}
	public Integer getYearam() {
		return (Integer)this.values.get("yearam");
	}
	public Float getTonmod() {
		return (Float)this.values.get("tonmod");
	}
	public Integer getVymrtimp() {
		return (Integer)this.values.get("vymrtimp");
	}
	public Integer getTslmtimp() {
		return (Integer)this.values.get("tslmtimp");
	}
	public Float getSla32imp() {
		return (Float)this.values.get("sla32imp");
	}
	public Float getSla36imp() {
		return (Float)this.values.get("sla36imp");
	}
	public Float getImprat() {
		return (Float)this.values.get("imprat");
	}
	public Integer getRegdis11() {
		return (Integer)this.values.get("regdis11");
	}
	public Integer getRegdis21() {
		return (Integer)this.values.get("regdis21");
	}
	public Integer getSla39imp() {
		return (Integer)this.values.get("sla39imp");
	}
	public Float getNcr15imp() {
		return (Float)this.values.get("ncr15imp");
	}
	public Integer getNcr17imp() {
		return (Integer)this.values.get("ncr17imp");
	}
	public Float getExprat() {
		return (Float)this.values.get("exprat");
	}
	public Integer getMale1imp() {
		return (Integer)this.values.get("male1imp");
	}
	public Integer getFeml1imp() {
		return (Integer)this.values.get("feml1imp");
	}
	public Integer getChil1imp() {
		return (Integer)this.values.get("chil1imp");
	}
	public Float getMalrat1() {
		return (Float)this.values.get("malrat1");
	}
	public Float getChilrat1() {
		return (Float)this.values.get("chilrat1");
	}
	public Integer getSlavemx1() {
		return (Integer)this.values.get("slavemx1");
	}
	public Integer getSlavema1() {
		return (Integer)this.values.get("slavema1");
	}
	public Integer getMale3imp() {
		return (Integer)this.values.get("male3imp");
	}
	public Integer getFeml3imp() {
		return (Integer)this.values.get("feml3imp");
	}
	public Integer getChil3imp() {
		return (Integer)this.values.get("chil3imp");
	}
	public Float getChilrat3() {
		return (Float)this.values.get("chilrat3");
	}
	public Float getMalrat3() {
		return (Float)this.values.get("malrat3");
	}
	public Integer getSlavemx3() {
		return (Integer)this.values.get("slavemx3");
	}
	public Integer getSlavema3() {
		return (Integer)this.values.get("slavema3");
	}
	public Integer getAdlt3imp() {
		return (Integer)this.values.get("adlt3imp");
	}
	public Float getVymrtrat() {
		return (Float)this.values.get("vymrtrat");
	}
	public Float getSlaximp() {
		return (Float)this.values.get("slaximp");
	}
	public Float getSlamimp() {
		return (Float)this.values.get("slamimp");
	}
	public Integer getVoy2imp() {
		return (Integer)this.values.get("voy2imp");
	}
	public Integer getVoy1imp() {
		return (Integer)this.values.get("voy1imp");
	}
	public Float getMalrat7() {
		return (Float)this.values.get("malrat7");
	}
	public Float getChilrat7() {
		return (Float)this.values.get("chilrat7");
	}
	public String getSourcex() {
		return (String)this.values.get("sourcex");
	}
	public Integer getP() {
		return (Integer)this.values.get("p");
	}
	public Integer getMen8() {
		return (Integer)this.values.get("men8");
	}
	public Integer getWomen8() {
		return (Integer)this.values.get("women8");
	}
	public Integer getBoy8() {
		return (Integer)this.values.get("boy8");
	}
	public Integer getGirl8() {
		return (Integer)this.values.get("girl8");
	}
	public Integer getBehrflag() {
		return (Integer)this.values.get("behrflag");
	}
	public Integer getPrice() {
		return (Integer)this.values.get("price");
	}
	public Float getUppguine() {
		return (Float)this.values.get("uppguine");
	}
	public Float getVoyagimp() {
		return (Float)this.values.get("voyagimp");
	}
	public Float getTimcoast() {
		return (Float)this.values.get("timcoast");
	}
	public Float getSlavpday() {
		return (Float)this.values.get("slavpday");
	}
	public Float getRountrip() {
		return (Float)this.values.get("rountrip");
	}
	public Float getSlavpvoy() {
		return (Float)this.values.get("slavpvoy");
	}
	public Float getSlxp1000() {
		return (Float)this.values.get("slxp1000");
	}
	public Float getInsurre2() {
		return (Float)this.values.get("insurre2");
	}
	public Float getTimslave() {
		return (Float)this.values.get("timslave");
	}
	public Integer getToncateg() {
		return (Integer)this.values.get("toncateg");
	}
	public Integer getEvgreen() {
		return (Integer)this.values.get("evgreen");
	}
	public Float getWomrat1() {
		return (Float)this.values.get("womrat1");
	}
	public Float getWomrat3() {
		return (Float)this.values.get("womrat3");
	}
	public Float getWomrat7() {
		return (Float)this.values.get("womrat7");
	}
	public Float getMenrat7() {
		return (Float)this.values.get("menrat7");
	}
	public Float getMenrat3() {
		return (Float)this.values.get("menrat3");
	}
	public Float getMenrat1() {
		return (Float)this.values.get("menrat1");
	}
	public Float getGirlrat1() {
		return (Float)this.values.get("girlrat1");
	}
	public Float getGirlrat3() {
		return (Float)this.values.get("girlrat3");
	}
	public Float getGirlrat7() {
		return (Float)this.values.get("girlrat7");
	}
	public Float getBoyrat7() {
		return (Float)this.values.get("boyrat7");
	}
	public Float getBoyrat3() {
		return (Float)this.values.get("boyrat3");
	}
	public Float getBoyrat1() {
		return (Float)this.values.get("boyrat1");
	}
	public Integer getBehmimp2() {
		return (Integer)this.values.get("behmimp2");
	}
	public Float getBehximp2() {
		return (Float)this.values.get("behximp2");
	}
	public Float getPoundprice() {
		return (Float)this.values.get("poundprice");
	}
	public Integer getTermmth() {
		return (Integer)this.values.get("termmth");
	}
	public Integer getIntrate() {
		return (Integer)this.values.get("intrate");
	}
	public Float getCashpric() {
		return (Float)this.values.get("cashpric");
	}
	public Float getJamcaspr() {
		return (Float)this.values.get("jamcaspr");
	}
	public Float getLocurmnl() {
		return (Float)this.values.get("locurmnl");
	}
	public String getExchrate() {
		return (String)this.values.get("exchrate");
	}
	public Float getFrencpri() {
		return (Float)this.values.get("frencpri");
	}
	public Float getFrnprinl() {
		return (Float)this.values.get("frnprinl");
	}
	public Float getNonpay() {
		return (Float)this.values.get("nonpay");
	}
	public Float getSeneg() {
		return (Float)this.values.get("seneg");
	}
	public Float getSl() {
		return (Float)this.values.get("sl");
	}
	public Float getWwc() {
		return (Float)this.values.get("wwc");
	}
	public Float getSlperton() {
		return (Float)this.values.get("slperton");
	}
	public Float getTonpercr() {
		return (Float)this.values.get("tonpercr");
	}
	public Float getPortugus() {
		return (Float)this.values.get("portugus");
	}
	public Integer getManol() {
		return (Integer)this.values.get("manol");
	}
	public Float getPortug2() {
		return (Float)this.values.get("portug2");
	}
	public Float getDeptamer() {
		return (Float)this.values.get("deptamer");
	}
	public Integer getGirl5() {
		return (Integer)this.values.get("girl5");
	}
	public Integer getBoy5() {
		return (Integer)this.values.get("boy5");
	}
	public Integer getMen5() {
		return (Integer)this.values.get("men5");
	}
	public Integer getWomen5() {
		return (Integer)this.values.get("women5");
	}
	public Integer getChild5() {
		return (Integer)this.values.get("child5");
	}
	public Integer getMale5() {
		return (Integer)this.values.get("male5");
	}
	public Integer getFemale5() {
		return (Integer)this.values.get("female5");
	}
	public Integer getArrport2() {
		return (Integer)this.values.get("arrport2");
	}
	public Integer getNsp() {
		return (Integer)this.values.get("nsp");
	}
	public Integer getNna() {
		return (Integer)this.values.get("nna");
	}
	public Integer getInfant3() {
		return (Integer)this.values.get("infant3");
	}
	public Integer getInfants3() {
		return (Integer)this.values.get("infants3");
	}
	public Integer getInfant1() {
		return (Integer)this.values.get("infant1");
	}
	public Float getKleimerg() {
		return (Float)this.values.get("kleimerg");
	}
	public Float getKlemerg2() {
		return (Float)this.values.get("klemerg2");
	}
	public Integer getAdult5() {
		return (Integer)this.values.get("adult5");
	}
	public Float getBahijelm() {
		return (Float)this.values.get("bahijelm");
	}
	public Integer getAdult2() {
		return (Integer)this.values.get("adult2");
	}
	public Integer getAdult4() {
		return (Integer)this.values.get("adult4");
	}
	public Integer getInfant4() {
		return (Integer)this.values.get("infant4");
	}
	public Float getVoyagid2() {
		return (Float)this.values.get("voyagid2");
	}
	public Integer getNewmina() {
		return (Integer)this.values.get("newmina");
	}
	public Integer getCrew() {
		return (Integer)this.values.get("crew");
	}
	public Integer getGuns2() {
		return (Integer)this.values.get("guns2");
	}
	public Integer getMen7() {
		return (Integer)this.values.get("men7");
	}
	public Integer getMale7() {
		return (Integer)this.values.get("male7");
	}
	public Integer getFemale7() {
		return (Integer)this.values.get("female7");
	}
	public Integer getWomen7() {
		return (Integer)this.values.get("women7");
	}
	public Integer getAdult7() {
		return (Integer)this.values.get("adult7");
	}
	public Integer getBoy7() {
		return (Integer)this.values.get("boy7");
	}
	public Integer getGirl7() {
		return (Integer)this.values.get("girl7");
	}
	public Integer getChild7() {
		return (Integer)this.values.get("child7");
	}
	public Integer getVoy3imp() {
		return (Integer)this.values.get("voy3imp");
	}
	public Float getSlavcapt() {
		return (Float)this.values.get("slavcapt");
	}
	public Float getMajbycon() {
		return (Float)this.values.get("majbycon");
	}
	public Integer getTabexpo() {
		return (Integer)this.values.get("tabexpo");
	}
	public Float getRice() {
		return (Float)this.values.get("rice");
	}
	public Float getSlavemx7() {
		return (Float)this.values.get("slavemx7");
	}
	public Float getSpanishamer() {
		return (Float)this.values.get("spanishamer");
	}
	public Float getYear25a() {
		return (Float)this.values.get("year25a");
	}
	public Float getMenvymrtrat() {
		return (Float)this.values.get("menvymrtrat");
	}
	public Float getWomenvymrtrat() {
		return (Float)this.values.get("womenvymrtrat");
	}
	public Float getBoyvymrtrat() {
		return (Float)this.values.get("boyvymrtrat");
	}
	public Float getGirlvymrtrat() {
		return (Float)this.values.get("girlvymrtrat");
	}
	public Float getCubaperiod() {
		return (Float)this.values.get("cubaperiod");
	}
	public DepDefinition getDatepl() {
		return (DepDefinition)this.values.get("datepl");
	}
	public PurchasePort getEmbport() {
		return (PurchasePort)this.values.get("embport");
	}
	public IntDisembPort getArrport() {
		return (IntDisembPort)this.values.get("arrport");
	}
	public PurchasePort getPlac1tra() {
		return (PurchasePort)this.values.get("plac1tra");
	}
	public RetPort getDestin() {
		return (RetPort)this.values.get("destin");
	}
	public PurchasePort getPlac2tra() {
		return (PurchasePort)this.values.get("plac2tra");
	}
	public PurchasePort getPlac3tra() {
		return (PurchasePort)this.values.get("plac3tra");
	}
	public OldWorldPlace getNpafttra() {
		return (OldWorldPlace)this.values.get("npafttra");
	}
	public FirstDemPort getSla1port() {
		return (FirstDemPort)this.values.get("sla1port");
	}
	public SecondDemPort getAdpsale1() {
		return (SecondDemPort)this.values.get("adpsale1");
	}
	public ThirdDemPort getAdpsale2() {
		return (ThirdDemPort)this.values.get("adpsale2");
	}
	public RetPort getPortret() {
		return (RetPort)this.values.get("portret");
	}
	public TonesType getTontype() {
		return (TonesType)this.values.get("tontype");
	}
	public ShipNationality getNational() {
		return (ShipNationality)this.values.get("national");
	}
	public PurchasePort getEmbport2() {
		return (PurchasePort)this.values.get("embport2");
	}
	public DeparturePort getPortdep() {
		return (DeparturePort)this.values.get("portdep");
	}
	public VesselRig getRig() {
		return (VesselRig)this.values.get("rig");
	}
	public RetPort getPlaccons() {
		return (RetPort)this.values.get("placcons");
	}
	public FirstDemPort getMajselpt() {
		return (FirstDemPort)this.values.get("majselpt");
	}
	public PurchasePort getMajbuypt() {
		return (PurchasePort)this.values.get("majbuypt");
	}
	public ImputedNation getNatinimp() {
		return (ImputedNation)this.values.get("natinimp");
	}
	public DepartureRegion getDeptreg() {
		return (DepartureRegion)this.values.get("deptreg");
	}
	public DepartureRegion getRetrnreg() {
		return (DepartureRegion)this.values.get("retrnreg");
	}
	public DepartureRegion getRegisreg() {
		return (DepartureRegion)this.values.get("regisreg");
	}
	public Year100 getYear100() {
		return (Year100)this.values.get("year100");
	}
	public Year5 getYear5() {
		return (Year5)this.values.get("year5");
	}
	public Year25 getYear25() {
		return (Year25)this.values.get("year25");
	}
	public EmbRegion getRegem1() {
		return (EmbRegion)this.values.get("regem1");
	}
	public EmbRegion getRegem2() {
		return (EmbRegion)this.values.get("regem2");
	}
	public EmbRegion getRegem3() {
		return (EmbRegion)this.values.get("regem3");
	}
	public EmbRegion getEmbreg() {
		return (EmbRegion)this.values.get("embreg");
	}
	public EmbRegion getEmbreg2() {
		return (EmbRegion)this.values.get("embreg2");
	}
	public EmbRegion getMajbuyrg() {
		return (EmbRegion)this.values.get("majbuyrg");
	}
	public EmbRegion getMajbyimp() {
		return (EmbRegion)this.values.get("majbyimp");
	}
	public DepartureRegion getRegdis1() {
		return (DepartureRegion)this.values.get("regdis1");
	}
	public DepartureRegion getRegdis2() {
		return (DepartureRegion)this.values.get("regdis2");
	}
	public DepartureRegion getRegdis3() {
		return (DepartureRegion)this.values.get("regdis3");
	}
	public DepartureRegion getRegarrp() {
		return (DepartureRegion)this.values.get("regarrp");
	}
	public DepartureRegion getMajselrg() {
		return (DepartureRegion)this.values.get("majselrg");
	}
	public Fate2 getFate2() {
		return (Fate2)this.values.get("fate2");
	}
	public Fate3 getFate3() {
		return (Fate3)this.values.get("fate3");
	}
	public Fate4 getFate4() {
		return (Fate4)this.values.get("fate4");
	}
	public ImpPrincipalRegion getMjselimp() {
		return (ImpPrincipalRegion)this.values.get("mjselimp");
	}
	public DepartureRegion getDeptreg1() {
		return (DepartureRegion)this.values.get("deptreg1");
	}
	public DemBroadRegion getMjselrg1() {
		return (DemBroadRegion)this.values.get("mjselrg1");
	}
	public DepartureRegion getConstreg() {
		return (DepartureRegion)this.values.get("constreg");
	}
	public Status getStatus() {
		return (Status)this.values.get("status");
	}
	public MajByIm getMajbyim3() {
		return (MajByIm)this.values.get("majbyim3");
	}
	public Editor getEditor() {
		return (Editor)this.values.get("editor");
	}
	public New getNewf() {
		return (New)this.values.get("newf");
	}
	public Filter getFilter_$() {
		return (Filter)this.values.get("filter_$");
	}
	public Natinim3 getNatinim3() {
		return (Natinim3)this.values.get("natinim3");
	}
	public Mjselrg2 getMjselrg2() {
		return (Mjselrg2)this.values.get("mjselrg2");
	}
	public Newamreg getNewamreg() {
		return (Newamreg)this.values.get("newamreg");
	}
	public Yearches getYearches() {
		return (Yearches)this.values.get("yearches");
	}
	public Gc getGc() {
		return (Gc)this.values.get("gc");
	}
	public Angola getAngola() {
		return (Angola)this.values.get("angola");
	}
	public Seafrica getSeafrica() {
		return (Seafrica)this.values.get("seafrica");
	}
	public DepartureRegion getRegarrp2() {
		return (DepartureRegion)this.values.get("regarrp2");
	}
	public Userid getUserid() {
		return (Userid)this.values.get("userid");
	}
	public XmimpFlag getXmimpflag() {
		return (XmimpFlag)this.values.get("xmimpflag");
	}
	public Temp2 getTemp2() {
		return (Temp2)this.values.get("temp2");
	}
	public Flag2 getFlag2() {
		return (Flag2)this.values.get("flag2");
	}
	public PrimaryLast getPrimarylast() {
		return (PrimaryLast)this.values.get("primarylast");
	}
	public PrimaryLast getPrimarylast1() {
		return (PrimaryLast)this.values.get("primarylast1");
	}
	public Carib getCarib() {
		return (Carib)this.values.get("carib");
	}
	public Natinim4 getNatinim4() {
		return (Natinim4)this.values.get("natinim4");
	}
	public Us getUs() {
		return (Us)this.values.get("us");
	}
	public DepartureRegion getMjselim2() {
		return (DepartureRegion)this.values.get("mjselim2");
	}
	public Temp getTemp() {
		return (Temp)this.values.get("temp");
	}
	/*END AUTOGENERATED*/
}

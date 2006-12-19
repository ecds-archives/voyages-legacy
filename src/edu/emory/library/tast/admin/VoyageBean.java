package edu.emory.library.tast.admin;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.emory.library.tast.dm.Area;
import edu.emory.library.tast.dm.Dictionary;
import edu.emory.library.tast.dm.Fate;
import edu.emory.library.tast.dm.FateOwner;
import edu.emory.library.tast.dm.FateSlaves;
import edu.emory.library.tast.dm.FateVessel;
import edu.emory.library.tast.dm.Nation;
import edu.emory.library.tast.dm.Port;
import edu.emory.library.tast.dm.Region;
import edu.emory.library.tast.dm.Resistance;
import edu.emory.library.tast.dm.VesselRig;
import edu.emory.library.tast.dm.Voyage;
import edu.emory.library.tast.reditor.FieldSchemaCheckbox;
import edu.emory.library.tast.reditor.FieldSchemaDate;
import edu.emory.library.tast.reditor.FieldSchemaDouble;
import edu.emory.library.tast.reditor.FieldSchemaDropdowns;
import edu.emory.library.tast.reditor.FieldSchemaFloat;
import edu.emory.library.tast.reditor.FieldSchemaInteger;
import edu.emory.library.tast.reditor.FieldSchemaLong;
import edu.emory.library.tast.reditor.FieldSchemaTextbox;
import edu.emory.library.tast.reditor.FieldValueCheckbox;
import edu.emory.library.tast.reditor.FieldValueDropdowns;
import edu.emory.library.tast.reditor.FieldValueFloat;
import edu.emory.library.tast.reditor.FieldValueInteger;
import edu.emory.library.tast.reditor.FieldValueLong;
import edu.emory.library.tast.reditor.FieldValueText;
import edu.emory.library.tast.reditor.ListItem;
import edu.emory.library.tast.reditor.Schema;
import edu.emory.library.tast.reditor.Values;
import edu.emory.library.tast.ui.GridOpenRowEvent;
import edu.emory.library.tast.util.HibernateUtil;
import edu.emory.library.tast.util.query.Conditions;
import edu.emory.library.tast.util.query.QueryValue;

public class VoyageBean
{
	
	
	private static final String NOT_KNOWN = "not known";

	private static final String LIST_VESSEL_RIGS_NAME = "vessel-rigs";
	private static final String LIST_RESISTANCE_NAME = "resistance";
	private static final String LIST_FATES_NAME = "fates";
	private static final String LIST_FATES_VESSEL_NAME = "fates-vessel";
	private static final String LIST_FATES_SLAVES_NAME = "fates-slaves";
	private static final String LIST_FATES_OWNER_NAME = "fates-owner";
	private static final String LIST_NATIONS_NAME = "nations";
	private static final String LIST_AREAS_NAME = "areas";
	private static final String LIST_REGIONS_NAME = "regions";
	private static final String LIST_PORTS_NAME = "ports";
	private static final String[] LISTS_LOCATION = {LIST_AREAS_NAME, LIST_REGIONS_NAME, LIST_PORTS_NAME};
	
	private Values shipValues;
	private Values outcomeValues;
	private Values itineraryValues;
	private Values datesValues;
	private Values crewValues;
	private Values slaveNumbersValues;
	private Values slaveCharacteriticsValues;
	private Values sourcesValues;

	private void registerDictionary(String listId, Schema schema, Session sess, List dictionary)
	{
		
		ListItem[] list = new ListItem[dictionary.size() + 1];
		list[0] = new ListItem(null, NOT_KNOWN);
		
		int i = 1;
		for (Iterator iter = dictionary.iterator(); iter.hasNext();)
		{
			Dictionary dictItem = (Dictionary) iter.next();
			list[i++] = new ListItem(
					String.valueOf(dictItem.getId()),
					dictItem.getId() + " - " + dictItem.getName());
		}
		
		schema.registerList(listId, list);
		
	}
	
	private void registerAreas(Schema schema, Session sess, List areas)
	{
		
		ListItem[] list = new ListItem[areas.size() + 1];
		list[0] = new ListItem(null, NOT_KNOWN);
		
		int i = 1;
		for (Iterator iter = areas.iterator(); iter.hasNext();)
		{
			Area area = (Area) iter.next();
			list[i++] = new ListItem(
					String.valueOf(area.getId()),
					area.getId() + " - " + area.getName());
		}
		
		schema.registerList(LIST_AREAS_NAME, list);
		
	}

	private void registerRegions(Schema schema, Session sess, List regions)
	{
		
		ListItem[] list = new ListItem[regions.size() + 1];
		list[0] = new ListItem(null, NOT_KNOWN);
		
		int i = 1;
		for (Iterator iter = regions.iterator(); iter.hasNext();)
		{
			Region region = (Region) iter.next();
			list[i++] = new ListItem(
					String.valueOf(region.getId()),
					String.valueOf(region.getArea().getId()),
					region.getId() + " - " + region.getName());
		}
		
		schema.registerList(LIST_REGIONS_NAME, list);
		
	}

	private void registerPorts(Schema schema, Session sess, List ports)
	{
		
		ListItem[] list = new ListItem[ports.size() + 1];
		list[0] = new ListItem(null, NOT_KNOWN);
		
		int i = 1;
		for (Iterator iter = ports.iterator(); iter.hasNext();)
		{
			Port port = (Port) iter.next();
			list[i++] = new ListItem(
					String.valueOf(port.getId()),
					String.valueOf(port.getRegion().getId()),
					port.getId() + " - " + port.getName());
		}
		
		schema.registerList(LIST_PORTS_NAME, list);
		
	}

	private void registerListOfNations(Schema schema, Session sess)
	{
		registerDictionary(LIST_NATIONS_NAME,
				schema, sess,
				Nation.loadAll(sess, "order"));
	}
	
	private void registerVesselRig(Schema schema, Session sess)
	{
		registerDictionary(LIST_VESSEL_RIGS_NAME,
				schema, sess,
				VesselRig.loadAll(sess, "id"));
	}

	private void registerListOfFates(Schema schema, Session sess)
	{
		registerDictionary(LIST_FATES_NAME,
				schema, sess,
				Fate.loadAll(sess, "id"));
	}
	
	private void registerListOfFatesVessel(Schema schema, Session sess)
	{
		registerDictionary(LIST_FATES_VESSEL_NAME,
				schema, sess,
				FateVessel.loadAll(sess, "id"));
	}

	private void registerListOfFatesSlaves(Schema schema, Session sess)
	{
		registerDictionary(LIST_FATES_SLAVES_NAME,
				schema, sess,
				FateSlaves.loadAll(sess, "id"));
	}

	private void registerListOfFatesOwner(Schema schema, Session sess)
	{
		registerDictionary(LIST_FATES_OWNER_NAME,
				schema, sess,
				FateOwner.loadAll(sess, "id"));
	}

	private void registerListOfResistances(Schema schema, Session sess)
	{
		registerDictionary(LIST_RESISTANCE_NAME,
				schema, sess,
				Resistance.loadAll(sess, "id"));
	}
	
	private String[] createLocationValues(Region region, Port port)
	{
		String[] values = new String[3];
		if (port != null)
		{
			values[0] = String.valueOf(port.getRegion().getArea().getId());
			values[1] = String.valueOf(port.getRegion().getId());
			values[2] = String.valueOf(port.getId());
		}
		else if (region != null)
		{
			values[0] = String.valueOf(region.getArea().getId());
			values[1] = String.valueOf(region.getId());
		}
		return values;
	}

	private String[] createDictionaryValue(Dictionary dictItem)
	{
		String[] values = new String[1];
		if (dictItem != null)
		{
			values[0] = String.valueOf(dictItem.getId());
		}
		return values;
	}

	public Schema getShipSchema()
	{

		Schema schema = new Schema();
		
		Session sess = HibernateUtil.getSession();
		Transaction tran = sess.beginTransaction();
		
		registerListOfNations(schema, sess);
		registerVesselRig(schema, sess);

		registerAreas(schema, sess, Area.loadAll(sess));
		registerRegions(schema, sess, Region.loadAll(sess));
		registerPorts(schema, sess, Port.loadAll(sess));

		tran.commit();
		sess.close();

		schema.addField(new FieldSchemaLong("voyageid", "Voyage ID"));
		schema.addField(new FieldSchemaCheckbox("cd", "CD 1999"));
		schema.addField(new FieldSchemaTextbox("shipname", "Name of ship"));
		schema.addField(new FieldSchemaDropdowns("placcons", "Place of construction", LISTS_LOCATION));
		schema.addField(new FieldSchemaInteger("yrcons", "Year of construction"));
		schema.addField(new FieldSchemaDropdowns("placreg", "Place of registration", LIST_NATIONS_NAME));
		schema.addField(new FieldSchemaInteger("yrreg", "Year of registration"));
		schema.addField(new FieldSchemaDropdowns("natinimp", "Imputed nation", LIST_NATIONS_NAME));
		schema.addField(new FieldSchemaDropdowns("rig", "Vessel rig", LIST_VESSEL_RIGS_NAME));
		schema.addField(new FieldSchemaFloat("tonnage", "Tonnage"));
		schema.addField(new FieldSchemaFloat("tonmod", "Tonnage"));
		schema.addField(new FieldSchemaInteger("guns", "Number of guns"));
		schema.addField(new FieldSchemaTextbox("owners", "Owners", true, 10));

		return schema;
		
	}
	
	private void fillShipValues(Voyage voyage)
	{

		shipValues = new Values();
		shipValues.addValue(new FieldValueLong("voyageid", voyage.getVoyageid()));
		shipValues.addValue(new FieldValueCheckbox("cd", voyage.getCd()));
		shipValues.addValue(new FieldValueText("shipname", voyage.getShipname()));
		shipValues.addValue(new FieldValueDropdowns("placcons", createLocationValues(null, voyage.getPlaccons())));
		shipValues.addValue(new FieldValueInteger("yrcons", voyage.getYrcons()));
		shipValues.addValue(new FieldValueDropdowns("placreg", createLocationValues(null, voyage.getPlacreg())));
		shipValues.addValue(new FieldValueInteger("yrreg", voyage.getYrreg()));
		shipValues.addValue(new FieldValueDropdowns("natinimp", createDictionaryValue(voyage.getNatinimp())));
		shipValues.addValue(new FieldValueDropdowns("rig", createDictionaryValue(voyage.getRig())));
		shipValues.addValue(new FieldValueFloat("tonnage", voyage.getTonmod()));
		shipValues.addValue(new FieldValueFloat("tonmod", voyage.getTonmod()));
		shipValues.addValue(new FieldValueInteger("guns", voyage.getGuns()));

	}
	
	public Schema getOutcomeSchema()
	{

		Schema schema = new Schema();
		
		Session sess = HibernateUtil.getSession();
		Transaction tran = sess.beginTransaction();
		
		registerListOfFates(schema, sess);
		registerListOfFatesOwner(schema, sess);
		registerListOfFatesSlaves(schema, sess);
		registerListOfFatesVessel(schema, sess);
		registerListOfResistances(schema, sess);

		registerAreas(schema, sess, Area.loadAll(sess));
		registerRegions(schema, sess, Region.loadAll(sess));
		registerPorts(schema, sess, Port.loadAll(sess));

		tran.commit();
		sess.close();

		schema.addField(new FieldSchemaDropdowns("fate", "Outcome of voyage", LIST_FATES_NAME));
		schema.addField(new FieldSchemaDropdowns("fate2", "Outcome of voyage for slaves", LIST_FATES_SLAVES_NAME));
		schema.addField(new FieldSchemaDropdowns("fate3", "Outcome of voyage if ship captured", LIST_FATES_VESSEL_NAME));
		schema.addField(new FieldSchemaDropdowns("fate4", "Outcome of voyage for owner", LIST_FATES_OWNER_NAME));
		schema.addField(new FieldSchemaDropdowns("resistance", "Violent incidents", LIST_RESISTANCE_NAME));
		
		return schema;
		
	}

	public Schema getItinerarySchema()
	{

		Schema schema = new Schema();
		
		Session sess = HibernateUtil.getSession();
		Transaction tran = sess.beginTransaction();
		
		registerAreas(schema, sess, Area.loadAll(sess));
		registerRegions(schema, sess, Region.loadAll(sess));
		registerPorts(schema, sess, Port.loadAll(sess));

		tran.commit();
		sess.close();
		
		schema.addField(new FieldSchemaDropdowns("ptdepimp", "Imputed place where voyage began", LISTS_LOCATION));
		schema.addField(new FieldSchemaDropdowns("plac1tra", "First place of slave purchase", LISTS_LOCATION));
		schema.addField(new FieldSchemaDropdowns("plac2tra", "Second place of slave purchase", LISTS_LOCATION));
		schema.addField(new FieldSchemaDropdowns("plac3tra", "Third place of slave purchase", LISTS_LOCATION));
		schema.addField(new FieldSchemaDropdowns("mjbyptimp", "Imputed principal place of slave purchase", LISTS_LOCATION));
		schema.addField(new FieldSchemaTextbox("npafttra", "Places of call before Atlantic crossing"));
		schema.addField(new FieldSchemaDropdowns("sla1port", "First place of landing", LISTS_LOCATION));
		schema.addField(new FieldSchemaDropdowns("adpsale1", "Second place of landing", LISTS_LOCATION));
		schema.addField(new FieldSchemaDropdowns("adpsale2", "Third place of landing", LISTS_LOCATION));
		schema.addField(new FieldSchemaDropdowns("mjslptimp", "Imputed principal place of landing", LISTS_LOCATION));
		schema.addField(new FieldSchemaDropdowns("portret", "Port at which voyage ended", LISTS_LOCATION));
		
		return schema;
		
	}

	public Schema getDatesSchema()
	{

		Schema schema = new Schema();
		
		schema.addField(new FieldSchemaTextbox("yearam", "Imputed year of arrival at first place of landing"));
		schema.addField(new FieldSchemaDate("datedep", "Date that voyage began"));
		schema.addField(new FieldSchemaDate("datebuy", "Date that slave purchase began"));
		schema.addField(new FieldSchemaDate("dateleftafr", "Date that vessel left last slaving port"));
		schema.addField(new FieldSchemaDate("dateland1", "Date that slaves landed at first place"));
		schema.addField(new FieldSchemaDate("dateland2", "Date of arrival at second place of landing"));
		schema.addField(new FieldSchemaDate("dateland3", "Date that slaves landed at third place"));
		schema.addField(new FieldSchemaDate("datedepam", "Date that ship left on return voyage"));
		schema.addField(new FieldSchemaDate("dateend", "Date when voyage completed"));
		schema.addField(new FieldSchemaInteger("voy1imp", "Imputed voyage length from departure to first port of landing"));
		schema.addField(new FieldSchemaInteger("voy2imp", "Imputed length of middle passage"));
		
		return schema;
		
	}

	public Schema getCrewSchema()
	{

		Schema schema = new Schema();
		
		schema.addField(new FieldSchemaTextbox("captains", "Captains", true, 3));
		schema.addField(new FieldSchemaInteger("crew1", "Crew on board at outset of voyage"));
		schema.addField(new FieldSchemaInteger("crew3", "Number of crew when first slaves landed"));
		schema.addField(new FieldSchemaInteger("crewdied", "Number of crew who died over entire voyage"));
		
		return schema;
		
	}

	public Schema getSlaveNumbersSchema()
	{

		Schema schema = new Schema();
		
		schema.addField(new FieldSchemaInteger("slintend", "Number of slaves intended at first place of purchase"));
		schema.addField(new FieldSchemaInteger("ncar13", "Slaves carried from first port of purchase"));
		schema.addField(new FieldSchemaInteger("ncar15", "Slaves carried from second port of purchase"));
		schema.addField(new FieldSchemaInteger("ncar17", "Slaves carried from third port of purchase"));
		schema.addField(new FieldSchemaInteger("tslavesd", "Total slaves carried off from Africa"));
		schema.addField(new FieldSchemaInteger("slaarriv", "Number of slaves who arrived at first place of landing"));
		schema.addField(new FieldSchemaInteger("slas32", "Number of slaves landed at first place"));
		schema.addField(new FieldSchemaInteger("slas36", "Number of slaves landed at second place"));
		schema.addField(new FieldSchemaInteger("slas39", "Number of slaves landed at third place"));
		schema.addField(new FieldSchemaInteger("slaximp", "Imputed total slaves embarked"));
		schema.addField(new FieldSchemaInteger("slamimp", "Imputed total slaves disembarked"));
		
		return schema;
		
	}

	public Schema getSlaveCharacteriticsSchema()
	{

		Schema schema = new Schema();

		schema.addField(new FieldSchemaDouble("menrat7", "Percentage of men at beginning or end of Middle Passage"));
		schema.addField(new FieldSchemaDouble("womrat7", "Percentage of women at beginning or end of Middle Passage"));
		schema.addField(new FieldSchemaDouble("boyrat7", "Percentage of boys at beginning or end of Middle Passage"));
		schema.addField(new FieldSchemaDouble("girlrat7", "Percentage of girls at beginning or end of Middle Passage"));
		schema.addField(new FieldSchemaDouble("malrat7", "Imputed sex ratio of the cargo of slaves"));
		schema.addField(new FieldSchemaDouble("chilrat7", "Imputed ratio of children to adults in the cargo of slaves"));
		schema.addField(new FieldSchemaDouble("jamcaspr", "Imputed sterling price of slaves sold in Jamaica"));
		schema.addField(new FieldSchemaDouble("vymrtimp", "Imputed number of slaves who died during the Middle Passage"));
		schema.addField(new FieldSchemaDouble("vymrtrat", "Imputed mortality rate (slave deaths / embarkations)"));
		
		return schema;
		
	}

	public Schema getSourcesSchema()
	{

		Schema schema = new Schema();

		schema.addField(new FieldSchemaTextbox("sources", "Source", true, 10));
		
		return schema;
		
	}
	
	public void openVoyage(GridOpenRowEvent event)
	{
		
		Long iid = new Long(event.getRowId());
		
		Session sess = HibernateUtil.getSession();
		Transaction tran = sess.beginTransaction();
		
		Conditions cond = new Conditions();
		cond.addCondition(Voyage.getAttribute("iid"), iid, Conditions.OP_EQUALS);

		QueryValue query = new QueryValue("Voyage", cond);
		query.setFirstResult(0);
		query.setLimit(1);
		Voyage voyage = (Voyage) query.executeQueryList(sess).get(0);
		
		fillShipValues(voyage);
		
		tran.commit();
		sess.close();

	}

	public Values getCrewValues()
	{
		return crewValues;
	}

	public void setCrewValues(Values crewValues)
	{
		this.crewValues = crewValues;
	}

	public Values getDatesValues()
	{
		return datesValues;
	}

	public void setDatesValues(Values datesValues)
	{
		this.datesValues = datesValues;
	}

	public Values getItineraryValues()
	{
		return itineraryValues;
	}

	public void setItineraryValues(Values itineraryValues)
	{
		this.itineraryValues = itineraryValues;
	}

	public Values getOutcomeValues()
	{
		return outcomeValues;
	}

	public void setOutcomeValues(Values outcomeValues)
	{
		this.outcomeValues = outcomeValues;
	}

	public Values getShipValues()
	{
		return shipValues;
	}

	public void setShipValues(Values shipValues)
	{
		this.shipValues = shipValues;
	}

	public Values getSlaveCharacteriticsValues()
	{
		return slaveCharacteriticsValues;
	}

	public void setSlaveCharacteriticsValues(Values slaveCharacteriticsValues)
	{
		this.slaveCharacteriticsValues = slaveCharacteriticsValues;
	}

	public Values getSlaveNumbersValues()
	{
		return slaveNumbersValues;
	}

	public void setSlaveNumbersValues(Values slaveNumbersValues)
	{
		this.slaveNumbersValues = slaveNumbersValues;
	}

	public Values getSourcesValues()
	{
		return sourcesValues;
	}

	public void setSourcesValues(Values sourcesValues)
	{
		this.sourcesValues = sourcesValues;
	}

}

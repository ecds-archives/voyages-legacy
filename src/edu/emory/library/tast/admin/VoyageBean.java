package edu.emory.library.tast.admin;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.emory.library.tast.TastResource;
import edu.emory.library.tast.common.GridOpenRowEvent;
import edu.emory.library.tast.common.TabChangedEvent;
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
import edu.emory.library.tast.reditor.FieldSchemaDropdowns;
import edu.emory.library.tast.reditor.FieldSchemaFloat;
import edu.emory.library.tast.reditor.FieldSchemaInteger;
import edu.emory.library.tast.reditor.FieldSchemaLong;
import edu.emory.library.tast.reditor.FieldSchemaTextbox;
import edu.emory.library.tast.reditor.FieldValueCheckbox;
import edu.emory.library.tast.reditor.FieldValueDate;
import edu.emory.library.tast.reditor.FieldValueDropdowns;
import edu.emory.library.tast.reditor.FieldValueFloat;
import edu.emory.library.tast.reditor.FieldValueInteger;
import edu.emory.library.tast.reditor.FieldValueLong;
import edu.emory.library.tast.reditor.FieldValueText;
import edu.emory.library.tast.reditor.ListItem;
import edu.emory.library.tast.reditor.Schema;
import edu.emory.library.tast.reditor.Values;
import edu.emory.library.tast.util.HibernateUtil;
import edu.emory.library.tast.util.StringUtils;
import edu.emory.library.tast.util.query.Conditions;
import edu.emory.library.tast.util.query.QueryValue;

public class VoyageBean
{
	
	
	private static final String NOT_KNOWN = "-";

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
	
	private String selectedTabId = SHIP_TAB_ID;

	private static final String SHIP_TAB_ID = "ship";
	private static final String OUTCOME_TAB_ID = "outcome";
	private static final String ITINERARY_TAB_ID = "itinerary";
	private static final String DATES_TAB_ID = "dates";
	private static final String CREW_TAB_ID = "crew";
	private static final String SLAVE_NUMS_TAB_ID = "slaveNumbers";
	private static final String SLAVE_CHARS_TAB_ID = "slaveChars";
	private static final String SOURCES_TAB_ID = "sources";
	
	private Long voyageIid;
	
	private Values shipValues;
	private Values outcomeValues;
	private Values itineraryValues;
	private Values datesValues;
	private Values crewValues;
	private Values slaveNumsValues;
	private Values slaveCharsValues;
	private Values sourcesValues;
	
	private VoyagesListBean listBean;

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

	private Port getPortValue(Session session, String[] ids)
	{
		if (ids[2] != null && ids[2].length() > 0)
		{
			return Port.loadById(session, Long.parseLong(ids[2])); 
		}
		else if (ids[1] != null && ids[1].length() > 0)
		{
			return Port.loadById(session, Long.parseLong(ids[1])); 
		}
		else if (ids[0] != null && ids[0].length() > 0)
		{
			return Port.loadById(session, Long.parseLong(ids[0])); 
		}
		else
		{
			return null;
		}
	}

	/*
	private Region getRegionValue(Session session, String[] ids)
	{
		if (ids[1] != null)
		{
			return Region.loadById(session, Long.parseLong(ids[1])); 
		}
		else if (ids[0] != null)
		{
			return Region.loadById(session, Long.parseLong(ids[0])); 
		}
		else
		{
			return null;
		}
	}
	*/

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

		schema.addField(new FieldSchemaLong("voyageid", TastResource.getText("database_attributes_voyageid"), "voyageid"));
		schema.addField(new FieldSchemaCheckbox("cd", TastResource.getText("database_attributes_cd")));
		schema.addField(new FieldSchemaTextbox("shipname", TastResource.getText("database_attributes_shipname"), "shipname"));
		schema.addField(new FieldSchemaDropdowns("placcons", TastResource.getText("database_attributes_placcons"), LISTS_LOCATION, "locations"));
		schema.addField(new FieldSchemaInteger("yrcons", TastResource.getText("database_attributes_yrcons"), "year"));
		schema.addField(new FieldSchemaDropdowns("placreg", TastResource.getText("database_attributes_placereg"), LISTS_LOCATION, "locations"));
		schema.addField(new FieldSchemaInteger("yrreg", TastResource.getText("database_attributes_yrreg"), "year"));
		schema.addField(new FieldSchemaDropdowns("natinimp", TastResource.getText("database_attributes_natinimp"), LIST_NATIONS_NAME));
		schema.addField(new FieldSchemaDropdowns("rig", TastResource.getText("database_attributes_rig"), LIST_VESSEL_RIGS_NAME));
		schema.addField(new FieldSchemaInteger("tonnage", TastResource.getText("database_attributes_tonnage")));
		schema.addField(new FieldSchemaFloat("tonmod", TastResource.getText("database_attributes_tonmod")));
		schema.addField(new FieldSchemaInteger("guns", TastResource.getText("database_attributes_guns")));
		schema.addField(new FieldSchemaTextbox("owners", TastResource.getText("database_attributes_owners"), true, 10, "owners"));

		return schema;
		
	}
	
	private void fillShipValues(Voyage voyage)
	{
		
		String owners = StringUtils.joinNonEmpty("\n", new String[] {
				voyage.getOwnera(),
				voyage.getOwnerb(),
				voyage.getOwnerc(),
				voyage.getOwnerd(),
				voyage.getOwnere(),
				voyage.getOwnerf(),
				voyage.getOwnerg(),
				voyage.getOwnerh(),
				voyage.getOwneri(),
				voyage.getOwnerj(),
				voyage.getOwnerk(),
				voyage.getOwnerl(),
				voyage.getOwnerm(),
				voyage.getOwnern(),
				voyage.getOwnero(),
				voyage.getOwnerp()});

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
		shipValues.addValue(new FieldValueInteger("tonnage", voyage.getTonnage()));
		shipValues.addValue(new FieldValueFloat("tonmod", voyage.getTonmod()));
		shipValues.addValue(new FieldValueInteger("guns", voyage.getGuns()));
		shipValues.addValue(new FieldValueText("owners", owners));

	}
	
	private void saveShipValues(Session sess, Voyage voyage)
	{
		
		FieldValueLong voyageIdValue = ((FieldValueLong) shipValues.getValueFor("voyageid"));
		FieldValueCheckbox cdValue = ((FieldValueCheckbox) shipValues.getValueFor("cd"));
		FieldValueText shipNameValue = ((FieldValueText) shipValues.getValueFor("shipname"));
		FieldValueDropdowns placconsValue = ((FieldValueDropdowns) shipValues.getValueFor("placcons"));
		FieldValueInteger yrconsValue = ((FieldValueInteger) shipValues.getValueFor("yrcons"));
		FieldValueDropdowns placregValue = ((FieldValueDropdowns) shipValues.getValueFor("placreg"));
		FieldValueInteger yrregValue = ((FieldValueInteger) shipValues.getValueFor("yrreg"));
		FieldValueDropdowns natinimpValue = ((FieldValueDropdowns) shipValues.getValueFor("natinimp"));
		FieldValueDropdowns rigValue = ((FieldValueDropdowns) shipValues.getValueFor("rig"));
		FieldValueInteger tonnageValue = ((FieldValueInteger) shipValues.getValueFor("tonnage"));
		FieldValueFloat tonmodValue = ((FieldValueFloat) shipValues.getValueFor("tonmod"));
		FieldValueInteger gunsValue = ((FieldValueInteger) shipValues.getValueFor("guns"));
		FieldValueText ownersValue = ((FieldValueText) shipValues.getValueFor("owners"));
		
		voyage.setVoyageid(voyageIdValue.getLong());
		voyage.setCd(cdValue.getBoolean());
		voyage.setShipname(shipNameValue.getValue());
		voyage.setPlaccons(getPortValue(sess, placconsValue.getValues()));
		voyage.setYrcons(yrconsValue.getInteger());
		voyage.setPlacreg(getPortValue(sess, placregValue.getValues()));
		voyage.setYrreg(yrregValue.getInteger());
		voyage.setNatinimp(Nation.loadById(sess, natinimpValue.getValues()[0]));
		voyage.setRig(VesselRig.loadById(sess, rigValue.getValues()[0]));
		voyage.setTonnage(tonnageValue.getInteger());
		voyage.setTonmod(tonmodValue.getFloat());
		voyage.setGuns(gunsValue.getInteger());
		
		String[] owners = StringUtils.removeEmpty(ownersValue.getLines());
		if (owners != null && owners.length > 0) voyage.setOwnera(owners[0]); else voyage.setOwnera(null);
		if (owners != null && owners.length > 1) voyage.setOwnerb(owners[1]); else voyage.setOwnerb(null);
		if (owners != null && owners.length > 2) voyage.setOwnerc(owners[2]); else voyage.setOwnerc(null);
		if (owners != null && owners.length > 3) voyage.setOwnerd(owners[3]); else voyage.setOwnerd(null);
		if (owners != null && owners.length > 4) voyage.setOwnere(owners[4]); else voyage.setOwnere(null);
		if (owners != null && owners.length > 5) voyage.setOwnerf(owners[5]); else voyage.setOwnerf(null);
		if (owners != null && owners.length > 6) voyage.setOwnerg(owners[6]); else voyage.setOwnerg(null);
		if (owners != null && owners.length > 7) voyage.setOwnerh(owners[7]); else voyage.setOwnerh(null);
		if (owners != null && owners.length > 8) voyage.setOwneri(owners[8]); else voyage.setOwneri(null);
		if (owners != null && owners.length > 9) voyage.setOwnerj(owners[9]); else voyage.setOwnerj(null);
		if (owners != null && owners.length > 10) voyage.setOwnerk(owners[10]); else voyage.setOwnerk(null);
		if (owners != null && owners.length > 11) voyage.setOwnerl(owners[11]); else voyage.setOwnerl(null);
		if (owners != null && owners.length > 12) voyage.setOwnerm(owners[12]); else voyage.setOwnerm(null);
		if (owners != null && owners.length > 13) voyage.setOwnern(owners[13]); else voyage.setOwnern(null);
		if (owners != null && owners.length > 14) voyage.setOwnero(owners[14]); else voyage.setOwnero(null);
		if (owners != null && owners.length > 15) voyage.setOwnerp(owners[15]); else voyage.setOwnerp(null); 

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

		schema.addField(new FieldSchemaDropdowns("fate", "Outcome of voyage", LIST_FATES_NAME, "fates"));
		schema.addField(new FieldSchemaDropdowns("fate2", "Outcome of voyage for slaves", LIST_FATES_SLAVES_NAME, "fates"));
		schema.addField(new FieldSchemaDropdowns("fate3", "Outcome of voyage if ship captured", LIST_FATES_VESSEL_NAME, "fates"));
		schema.addField(new FieldSchemaDropdowns("fate4", "Outcome of voyage for owner", LIST_FATES_OWNER_NAME, "fates"));
		schema.addField(new FieldSchemaDropdowns("resistance", "Violent incidents", LIST_RESISTANCE_NAME, "fates"));
		
		return schema;
		
	}

	private void fillOutcomeValues(Voyage voyage)
	{
		
		outcomeValues = new Values();
		outcomeValues.addValue(new FieldValueDropdowns("fate", createDictionaryValue(voyage.getFate())));
		outcomeValues.addValue(new FieldValueDropdowns("fate2", createDictionaryValue(voyage.getFate2())));
		outcomeValues.addValue(new FieldValueDropdowns("fate3", createDictionaryValue(voyage.getFate3())));
		outcomeValues.addValue(new FieldValueDropdowns("fate4", createDictionaryValue(voyage.getFate4())));
		outcomeValues.addValue(new FieldValueDropdowns("resistance", createDictionaryValue(voyage.getResistance())));
		
	}

	private void saveOutcomeValues(Session sess, Voyage voyage)
	{

		FieldValueDropdowns fateValue = ((FieldValueDropdowns) outcomeValues.getValueFor("fate"));
		FieldValueDropdowns fate2Value = ((FieldValueDropdowns) outcomeValues.getValueFor("fate2"));
		FieldValueDropdowns fate3Value = ((FieldValueDropdowns) outcomeValues.getValueFor("fate3"));
		FieldValueDropdowns fate4Value = ((FieldValueDropdowns) outcomeValues.getValueFor("fate4"));
		FieldValueDropdowns resistanceValue = ((FieldValueDropdowns) outcomeValues.getValueFor("resistance"));

		voyage.setFate(Fate.loadById(sess, fateValue.getValues()[0]));
		voyage.setFate2(FateSlaves.loadById(sess, fate2Value.getValues()[0]));
		voyage.setFate3(FateVessel.loadById(sess, fate3Value.getValues()[0]));
		voyage.setFate4(FateOwner.loadById(sess, fate4Value.getValues()[0]));
		voyage.setResistance(Resistance.loadById(sess, resistanceValue.getValues()[0]));
		
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
		
		schema.addField(new FieldSchemaDropdowns("portdep", "Place where voyage began", LISTS_LOCATION, "locations"));
		schema.addField(new FieldSchemaDropdowns("ptdepimp", "Imputed place where voyage began", LISTS_LOCATION, "locations"));
		schema.addField(new FieldSchemaDropdowns("plac1tra", "First place of slave purchase", LISTS_LOCATION, "locations"));
		schema.addField(new FieldSchemaDropdowns("plac2tra", "Second place of slave purchase", LISTS_LOCATION, "locations"));
		schema.addField(new FieldSchemaDropdowns("plac3tra", "Third place of slave purchase", LISTS_LOCATION, "locations"));
		schema.addField(new FieldSchemaDropdowns("mjbyptimp", "Imputed principal place of slave purchase", LISTS_LOCATION, "locations"));
		schema.addField(new FieldSchemaDropdowns("npafttra", "Places of call before Atlantic crossing", LISTS_LOCATION, "locations"));
		schema.addField(new FieldSchemaDropdowns("sla1port", "First place of landing", LISTS_LOCATION, "locations"));
		schema.addField(new FieldSchemaDropdowns("adpsale1", "Second place of landing", LISTS_LOCATION, "locations"));
		schema.addField(new FieldSchemaDropdowns("adpsale2", "Third place of landing", LISTS_LOCATION, "locations"));
		schema.addField(new FieldSchemaDropdowns("mjslptimp", "Imputed principal place of landing", LISTS_LOCATION, "locations"));
		schema.addField(new FieldSchemaDropdowns("portret", "Port at which voyage ended", LISTS_LOCATION, "locations"));
		
		return schema;
		
	}
	
	private void fillItineraryValues(Voyage voyage)
	{
		
		itineraryValues = new Values();
		
		itineraryValues.addValue(new FieldValueDropdowns("portdep", createLocationValues(null, voyage.getPortdep())));
		itineraryValues.addValue(new FieldValueDropdowns("ptdepimp", createLocationValues(voyage.getDeptregimp(), voyage.getPtdepimp())));
		itineraryValues.addValue(new FieldValueDropdowns("plac1tra", createLocationValues(voyage.getRegem1(), voyage.getPlac1tra())));
		itineraryValues.addValue(new FieldValueDropdowns("plac2tra", createLocationValues(voyage.getRegem2(), voyage.getPlac2tra())));
		itineraryValues.addValue(new FieldValueDropdowns("plac3tra", createLocationValues(voyage.getRegem3(), voyage.getPlac3tra())));
		itineraryValues.addValue(new FieldValueDropdowns("mjbyptimp", createLocationValues(voyage.getMajbyimp(), voyage.getMjbyptimp())));
		itineraryValues.addValue(new FieldValueDropdowns("npafttra", createLocationValues(null, voyage.getMjbyptimp())));
		itineraryValues.addValue(new FieldValueDropdowns("sla1port", createLocationValues(voyage.getRegdis1(), voyage.getSla1port())));
		itineraryValues.addValue(new FieldValueDropdowns("adpsale1", createLocationValues(voyage.getRegdis2(), voyage.getAdpsale1())));
		itineraryValues.addValue(new FieldValueDropdowns("adpsale2", createLocationValues(voyage.getRegdis3(), voyage.getAdpsale2())));
		itineraryValues.addValue(new FieldValueDropdowns("portret", createLocationValues(voyage.getRetrnreg(), voyage.getPortret())));

	}
	
	private void saveItineraryValues(Session sess, Voyage voyage)
	{

		FieldValueDropdowns portdepValue = ((FieldValueDropdowns) itineraryValues.getValueFor("portdep"));
		FieldValueDropdowns ptdepimpValue = ((FieldValueDropdowns) itineraryValues.getValueFor("ptdepimp"));
		FieldValueDropdowns plac1traValue = ((FieldValueDropdowns) itineraryValues.getValueFor("plac1tra"));
		FieldValueDropdowns plac2traValue = ((FieldValueDropdowns) itineraryValues.getValueFor("plac2tra"));
		FieldValueDropdowns plac3traValue = ((FieldValueDropdowns) itineraryValues.getValueFor("plac3tra"));
		FieldValueDropdowns mjbyptimpValue = ((FieldValueDropdowns) itineraryValues.getValueFor("mjbyptimp"));
		FieldValueDropdowns npafttraValue = ((FieldValueDropdowns) itineraryValues.getValueFor("npafttra"));
		FieldValueDropdowns sla1portValue = ((FieldValueDropdowns) itineraryValues.getValueFor("sla1port"));
		FieldValueDropdowns adpsale1Value = ((FieldValueDropdowns) itineraryValues.getValueFor("adpsale1"));
		FieldValueDropdowns adpsale2Value = ((FieldValueDropdowns) itineraryValues.getValueFor("adpsale2"));
		FieldValueDropdowns portretValue = ((FieldValueDropdowns) itineraryValues.getValueFor("portret"));

		voyage.setPortdep(getPortValue(sess, portdepValue.getValues()));
		voyage.setPtdepimp(getPortValue(sess, ptdepimpValue.getValues()));
		voyage.setPlac1tra(getPortValue(sess, plac1traValue.getValues()));
		voyage.setPlac2tra(getPortValue(sess, plac2traValue.getValues()));
		voyage.setPlac3tra(getPortValue(sess, plac3traValue.getValues()));
		voyage.setMjbyptimp(getPortValue(sess, mjbyptimpValue.getValues()));
		voyage.setNpafttra(getPortValue(sess, npafttraValue.getValues()));
		voyage.setSla1port(getPortValue(sess, sla1portValue.getValues()));
		voyage.setAdpsale1(getPortValue(sess, adpsale1Value.getValues()));
		voyage.setAdpsale2(getPortValue(sess, adpsale2Value.getValues()));
		voyage.setPortret(getPortValue(sess, portretValue.getValues()));

	}

	public Schema getDatesSchema()
	{

		Schema schema = new Schema();
		
		schema.addField(new FieldSchemaInteger("yearam", "Imputed year of arrival at first place of landing", "year"));
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
	
	private void fillDateValues(Voyage voyage)
	{
		
		datesValues = new Values();
		
		datesValues.addValue(new FieldValueInteger("yearam", voyage.getYearam()));
		datesValues.addValue(new FieldValueDate("datedep", voyage.getDatedep()));
		datesValues.addValue(new FieldValueDate("datebuy", voyage.getDatebuy()));
		datesValues.addValue(new FieldValueDate("dateleftafr", voyage.getDateleftafr()));
		datesValues.addValue(new FieldValueDate("dateland1", voyage.getDateland1()));
		datesValues.addValue(new FieldValueDate("dateland2", voyage.getDateland2()));
		datesValues.addValue(new FieldValueDate("dateland3", voyage.getDateland3()));
		datesValues.addValue(new FieldValueDate("datedepam", voyage.getDatedepam()));
		datesValues.addValue(new FieldValueDate("dateend", voyage.getDateend()));
		datesValues.addValue(new FieldValueInteger("voy1imp", voyage.getVoy1imp()));
		datesValues.addValue(new FieldValueInteger("voy2imp", voyage.getVoy2imp()));

	}
	
	private void saveDateValues(Session sess, Voyage voyage)
	{

		FieldValueInteger yearamValue = ((FieldValueInteger) datesValues.getValueFor("yearam"));
		FieldValueDate datedepValue = ((FieldValueDate) datesValues.getValueFor("datedep"));
		FieldValueDate datebuyValue = ((FieldValueDate) datesValues.getValueFor("datebuy"));
		FieldValueDate dateleftafrValue = ((FieldValueDate) datesValues.getValueFor("dateleftafr"));
		FieldValueDate dateland1Value = ((FieldValueDate) datesValues.getValueFor("dateland1"));
		FieldValueDate dateland2Value = ((FieldValueDate) datesValues.getValueFor("dateland2"));
		FieldValueDate dateland3Value = ((FieldValueDate) datesValues.getValueFor("dateland3"));
		FieldValueDate datedepamValue = ((FieldValueDate) datesValues.getValueFor("datedepam"));
		FieldValueDate dateendValue = ((FieldValueDate) datesValues.getValueFor("dateend"));
		FieldValueInteger voy1impValue = ((FieldValueInteger) datesValues.getValueFor("voy1imp"));
		FieldValueInteger voy2impValue = ((FieldValueInteger) datesValues.getValueFor("voy2imp"));
		
		voyage.setYearam(yearamValue.getInteger());
		voyage.setDatedep(datedepValue.getDate());
		voyage.setDatedep(datebuyValue.getDate());
		voyage.setDateleftafr(dateleftafrValue.getDate());
		voyage.setDateland1(dateland1Value.getDate());
		voyage.setDateland3(dateland2Value.getDate());
		voyage.setDateland3(dateland3Value.getDate());
		voyage.setDatedepam(datedepamValue.getDate());
		voyage.setDateend(dateendValue.getDate());
		voyage.setVoy1imp(voy1impValue.getInteger());
		voyage.setVoy2imp(voy2impValue.getInteger());
	
	}

	public Schema getCrewSchema()
	{

		Schema schema = new Schema();
		
		schema.addField(new FieldSchemaTextbox("captains", "Captains", true, 3, "captains"));
		schema.addField(new FieldSchemaInteger("crew1", "Crew on board at outset of voyage"));
		schema.addField(new FieldSchemaInteger("crew3", "Number of crew when first slaves landed"));
		schema.addField(new FieldSchemaInteger("crewdied", "Number of crew who died over entire voyage"));
		
		return schema;
		
	}
	
	private void fillCrewValues(Voyage voyage)
	{
		
		String captains = StringUtils.joinNonEmpty("\n", new String[] {
				voyage.getCaptaina(),
				voyage.getCaptainb(),
				voyage.getCaptainc()});

		crewValues = new Values();
		
		crewValues.addValue(new FieldValueText("captains", captains));
		crewValues.addValue(new FieldValueInteger("crew1", voyage.getCrew1()));
		crewValues.addValue(new FieldValueInteger("crew3", voyage.getCrew3()));
		crewValues.addValue(new FieldValueInteger("crewdied", voyage.getCrewdied()));
		
	}
	
	private void saveCrewValues(Session sess, Voyage voyage)
	{
		
		FieldValueText captainsValue = ((FieldValueText) crewValues.getValueFor("captains"));
		FieldValueInteger crew1Value = ((FieldValueInteger) crewValues.getValueFor("crew1"));
		FieldValueInteger crew3Value = ((FieldValueInteger) crewValues.getValueFor("crew3"));
		FieldValueInteger crewdiedValue = ((FieldValueInteger) crewValues.getValueFor("crewdied"));
		
		String[] captains = StringUtils.removeEmpty(captainsValue.getLines());
		if (captains != null && captains.length > 0) voyage.setCaptaina(captains[0]); else voyage.setOwnerp(null);
		if (captains != null && captains.length > 1) voyage.setCaptainb(captains[1]); else voyage.setOwnerp(null);
		if (captains != null && captains.length > 2) voyage.setCaptainc(captains[2]); else voyage.setOwnerp(null);

		voyage.setCrew1(crew1Value.getInteger());
		voyage.setCrew3(crew3Value.getInteger());
		voyage.setCrewdied(crewdiedValue.getInteger());
		
	}

	public Schema getSlaveNumsSchema()
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
	
	private void fillSlaveNumsValues(Voyage voyage)
	{
		
		slaveNumsValues = new Values();
		
		slaveNumsValues.addValue(new FieldValueInteger("slintend", voyage.getSlintend()));
		slaveNumsValues.addValue(new FieldValueInteger("ncar13", voyage.getNcar13()));
		slaveNumsValues.addValue(new FieldValueInteger("ncar15", voyage.getNcar15()));
		slaveNumsValues.addValue(new FieldValueInteger("ncar17", voyage.getNcar17()));
		slaveNumsValues.addValue(new FieldValueInteger("tslavesd", voyage.getTslavesd()));
		slaveNumsValues.addValue(new FieldValueInteger("slaarriv", voyage.getSlaarriv()));
		slaveNumsValues.addValue(new FieldValueInteger("slas32", voyage.getSlas32()));
		slaveNumsValues.addValue(new FieldValueInteger("slas36", voyage.getSlas36()));
		slaveNumsValues.addValue(new FieldValueInteger("slas39", voyage.getSlas39()));
		slaveNumsValues.addValue(new FieldValueInteger("slaximp", voyage.getSlaximp()));
		slaveNumsValues.addValue(new FieldValueInteger("slamimp", voyage.getSlamimp()));
		
	}

	private void saveSlaveNumsValues(Session sess, Voyage voyage)
	{
		
		FieldValueInteger slintendValue = (FieldValueInteger) slaveNumsValues.getValueFor("slintend");
		FieldValueInteger ncar13Value = (FieldValueInteger) slaveNumsValues.getValueFor("ncar13");
		FieldValueInteger ncar15Value = (FieldValueInteger) slaveNumsValues.getValueFor("ncar15");
		FieldValueInteger ncar17Value = (FieldValueInteger) slaveNumsValues.getValueFor("ncar17");
		FieldValueInteger tslavesdValue = (FieldValueInteger) slaveNumsValues.getValueFor("tslavesd");
		FieldValueInteger slaarrivValue = (FieldValueInteger) slaveNumsValues.getValueFor("slaarriv");
		FieldValueInteger slas32Value = (FieldValueInteger) slaveNumsValues.getValueFor("slas32");
		FieldValueInteger slas36Value = (FieldValueInteger) slaveNumsValues.getValueFor("slas36");
		FieldValueInteger slas39Value = (FieldValueInteger) slaveNumsValues.getValueFor("slas39");
		FieldValueInteger slaximpValue = (FieldValueInteger) slaveNumsValues.getValueFor("slaximp");
		FieldValueInteger slamimpValue = (FieldValueInteger) slaveNumsValues.getValueFor("slamimp");
		
		voyage.setSlintend(slintendValue.getInteger());
		voyage.setNcar13(ncar13Value.getInteger());
		voyage.setNcar15(ncar15Value.getInteger());
		voyage.setNcar17(ncar17Value.getInteger());
		voyage.setTslavesd(tslavesdValue.getInteger());
		voyage.setSlaarriv(slaarrivValue.getInteger());
		voyage.setSlas32(slas32Value.getInteger());
		voyage.setSlas36(slas36Value.getInteger());
		voyage.setSlas39(slas39Value.getInteger());
		voyage.setSlaximp(slaximpValue.getInteger());
		voyage.setSlamimp(slamimpValue.getInteger());
		
	}

	public Schema getSlaveCharsSchema()
	{

		Schema schema = new Schema();

		schema.addField(new FieldSchemaFloat("menrat7", "Percentage of men at beginning or end of Middle Passage"));
		schema.addField(new FieldSchemaFloat("womrat7", "Percentage of women at beginning or end of Middle Passage"));
		schema.addField(new FieldSchemaFloat("boyrat7", "Percentage of boys at beginning or end of Middle Passage"));
		schema.addField(new FieldSchemaFloat("girlrat7", "Percentage of girls at beginning or end of Middle Passage"));
		schema.addField(new FieldSchemaFloat("malrat7", "Imputed sex ratio of the cargo of slaves"));
		schema.addField(new FieldSchemaFloat("chilrat7", "Imputed ratio of children to adults in the cargo of slaves"));
		schema.addField(new FieldSchemaFloat("jamcaspr", "Imputed sterling price of slaves sold in Jamaica"));
		schema.addField(new FieldSchemaFloat("vymrtimp", "Imputed number of slaves who died during the Middle Passage"));
		schema.addField(new FieldSchemaFloat("vymrtrat", "Imputed mortality rate (slave deaths / embarkations)"));
		
		return schema;
		
	}

	private void fillSlaveCharsValues(Voyage voyage)
	{
		
		slaveCharsValues = new Values();
		
		slaveCharsValues.addValue(new FieldValueFloat("menrat7", voyage.getMalrat7()));
		slaveCharsValues.addValue(new FieldValueFloat("womrat7", voyage.getWomrat7()));
		slaveCharsValues.addValue(new FieldValueFloat("boyrat7", voyage.getBoyrat7()));
		slaveCharsValues.addValue(new FieldValueFloat("girlrat7", voyage.getGirlrat7()));
		slaveCharsValues.addValue(new FieldValueFloat("malrat7", voyage.getMalrat7()));
		slaveCharsValues.addValue(new FieldValueFloat("chilrat7", voyage.getChilrat7()));
		slaveCharsValues.addValue(new FieldValueFloat("jamcaspr", voyage.getJamcaspr()));
		slaveCharsValues.addValue(new FieldValueInteger("vymrtimp", voyage.getVymrtimp()));
		slaveCharsValues.addValue(new FieldValueFloat("vymrtrat", voyage.getVymrtrat()));

	}

	private void saveSlaveCharsValues(Session sess, Voyage voyage)
	{
		
		FieldValueFloat menrat7Value = (FieldValueFloat) slaveCharsValues.getValueFor("menrat7");
		FieldValueFloat womrat7Value = (FieldValueFloat) slaveCharsValues.getValueFor("womrat7");
		FieldValueFloat boyrat7Value = (FieldValueFloat) slaveCharsValues.getValueFor("boyrat7");
		FieldValueFloat girlrat7Value = (FieldValueFloat) slaveCharsValues.getValueFor("girlrat7");
		FieldValueFloat malrat7Value = (FieldValueFloat) slaveCharsValues.getValueFor("malrat7");
		FieldValueFloat chilrat7Value = (FieldValueFloat) slaveCharsValues.getValueFor("chilrat7");
		FieldValueFloat jamcasprValue = (FieldValueFloat) slaveCharsValues.getValueFor("jamcaspr");
		FieldValueInteger vymrtimpValue = (FieldValueInteger) slaveCharsValues.getValueFor("vymrtimp");
		FieldValueFloat vymrtratValue = (FieldValueFloat) slaveCharsValues.getValueFor("vymrtrat");
		
		voyage.setMenrat7(menrat7Value.getFloat());
		voyage.setWomrat7(womrat7Value.getFloat());
		voyage.setBoyrat7(boyrat7Value.getFloat());
		voyage.setGirlrat7(girlrat7Value.getFloat());
		voyage.setMalrat7(malrat7Value.getFloat());
		voyage.setChilrat7(chilrat7Value.getFloat());
		voyage.setJamcaspr(jamcasprValue.getFloat());
		voyage.setVymrtimp(vymrtimpValue.getInteger());
		voyage.setVymrtrat(vymrtratValue.getFloat());

	}

	public Schema getSourcesSchema()
	{

		Schema schema = new Schema();

		schema.addField(new FieldSchemaTextbox("sources", "Source", true, 10, "sources"));
		
		return schema;
		
	}

	private void fillSourcesValues(Voyage voyage)
	{

		String sources = StringUtils.joinNonEmpty("\n", new String[] {
				voyage.getSourcea(),
				voyage.getSourceb(),
				voyage.getSourcec(),
				voyage.getSourced(),
				voyage.getSourcee(),
				voyage.getSourcef(),
				voyage.getSourceg(),
				voyage.getSourceh(),
				voyage.getSourcei(),
				voyage.getSourcej(),
				voyage.getSourcek(),
				voyage.getSourcel(),
				voyage.getSourcem(),
				voyage.getSourcen(),
				voyage.getSourceo(),
				voyage.getSourcep(),
				voyage.getSourceq(),
				voyage.getSourcer()});
		
		sourcesValues = new Values();
		
		sourcesValues.addValue(new FieldValueText("sources", sources));

	}
	
	private void saveSourcesValues(Session sess, Voyage voyage)
	{
		
		FieldValueText sourcesValue = (FieldValueText) sourcesValues.getValueFor("sources"); 

		String[] sources = StringUtils.removeEmpty(sourcesValue.getLines());
		if (sources != null && sources.length > 0) voyage.setSourcea(sources[0]); else voyage.setSourcea(null);
		if (sources != null && sources.length > 1) voyage.setSourceb(sources[1]); else voyage.setSourceb(null);
		if (sources != null && sources.length > 2) voyage.setSourcec(sources[2]); else voyage.setSourcec(null);
		if (sources != null && sources.length > 3) voyage.setSourced(sources[3]); else voyage.setSourced(null);
		if (sources != null && sources.length > 4) voyage.setSourcee(sources[4]); else voyage.setSourcee(null);
		if (sources != null && sources.length > 5) voyage.setSourcef(sources[5]); else voyage.setSourcef(null);
		if (sources != null && sources.length > 6) voyage.setSourceg(sources[6]); else voyage.setSourceg(null);
		if (sources != null && sources.length > 7) voyage.setSourceh(sources[7]); else voyage.setSourceh(null);
		if (sources != null && sources.length > 8) voyage.setSourcei(sources[8]); else voyage.setSourcei(null);
		if (sources != null && sources.length > 9) voyage.setSourcej(sources[9]); else voyage.setSourcej(null);
		if (sources != null && sources.length > 10) voyage.setSourcek(sources[10]); else voyage.setSourcek(null);
		if (sources != null && sources.length > 11) voyage.setSourcel(sources[11]); else voyage.setSourcel(null);
		if (sources != null && sources.length > 12) voyage.setSourcem(sources[12]); else voyage.setSourcem(null);
		if (sources != null && sources.length > 13) voyage.setSourcen(sources[13]); else voyage.setSourcen(null);
		if (sources != null && sources.length > 14) voyage.setSourceo(sources[14]); else voyage.setSourceo(null);
		if (sources != null && sources.length > 15) voyage.setSourcep(sources[15]); else voyage.setSourcep(null);
		if (sources != null && sources.length > 16) voyage.setSourceq(sources[16]); else voyage.setSourceq(null);
		if (sources != null && sources.length > 17) voyage.setSourcer(sources[17]); else voyage.setSourcer(null);
	
	}
	
	private void openOrCreateVoyage(Long voyageIid)
	{

		selectedTabId = SHIP_TAB_ID;
		this.voyageIid = voyageIid;
		
		Session sess = HibernateUtil.getSession();
		Transaction tran = sess.beginTransaction();
		
		Voyage voyage;

		if (voyageIid != null)
		{
		
			Conditions cond = new Conditions();
			cond.addCondition(Voyage.getAttribute("iid"), voyageIid, Conditions.OP_EQUALS);
			
			QueryValue query = new QueryValue("Voyage", cond);
			query.setFirstResult(0);
			query.setLimit(1);
			voyage = (Voyage) query.executeQueryList(sess).get(0);
			
		}
		else
		{
			voyage = new Voyage();
		}
		
		fillShipValues(voyage);
		fillOutcomeValues(voyage);
		fillItineraryValues(voyage);
		fillDateValues(voyage);
		fillCrewValues(voyage);
		fillSlaveNumsValues(voyage);
		fillSlaveCharsValues(voyage);
		fillSourcesValues(voyage);
		
		tran.commit();
		sess.close();
	
	}
	
	public String createVoyage()
	{
		openOrCreateVoyage(null);
		return "edit";
	}
	
	public void openVoyage(GridOpenRowEvent event)
	{
		openOrCreateVoyage(new Long(event.getRowId()));
	}

	public String saveVoyage()
	{

		Session sess = HibernateUtil.getSession();
		Transaction tran = sess.beginTransaction();
		
		Voyage voyage;

		if (voyageIid != null)
		{

			Conditions cond = new Conditions();
			cond.addCondition(Voyage.getAttribute("iid"), voyageIid, Conditions.OP_EQUALS);
			cond.addCondition(Voyage.getAttribute("revision"), new Integer(-1), Conditions.OP_EQUALS);
			
			QueryValue query = new QueryValue("Voyage", cond);
			query.setFirstResult(0);
			query.setLimit(1);
			voyage = (Voyage) query.executeQueryList(sess).get(0);
			
		}
		else
		{
			
			voyage = new Voyage();

		}
		
		saveShipValues(sess, voyage);
		saveOutcomeValues(sess, voyage);
		saveItineraryValues(sess, voyage);
		saveDateValues(sess, voyage);
		saveCrewValues(sess, voyage);
		saveSlaveNumsValues(sess, voyage);
		saveSlaveCharsValues(sess, voyage);
		saveSourcesValues(sess, voyage);
		
		sess.saveOrUpdate(voyage);
		
		tran.commit();
		sess.close();
		
		listBean.invalidateList();

		return "list";

	}
	
	public void onGroupChanged(TabChangedEvent event)
	{
		selectedTabId = event.getTabId();
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

	public Values getSlaveCharsValues()
	{
		return slaveCharsValues;
	}

	public void setSlaveCharsValues(Values slaveCharacteriticsValues)
	{
		this.slaveCharsValues = slaveCharacteriticsValues;
	}

	public Values getSlaveNumsValues()
	{
		return slaveNumsValues;
	}

	public void setSlaveNumsValues(Values slaveNumbersValues)
	{
		this.slaveNumsValues = slaveNumbersValues;
	}

	public Values getSourcesValues()
	{
		return sourcesValues;
	}

	public void setSourcesValues(Values sourcesValues)
	{
		this.sourcesValues = sourcesValues;
	}

	public boolean isGroupShipSelected()
	{
		return SHIP_TAB_ID.equals(selectedTabId);
	}

	public boolean isGroupCrewSelected()
	{
		return CREW_TAB_ID.equals(selectedTabId);
	}

	public boolean isGroupDatesSelected()
	{
		return DATES_TAB_ID.equals(selectedTabId);
	}

	public boolean isGroupItinerarySelected()
	{
		return ITINERARY_TAB_ID.equals(selectedTabId);
	}

	public boolean isGroupOutcomeSelected()
	{
		return OUTCOME_TAB_ID.equals(selectedTabId);
	}

	public boolean isGroupSlaveCharsSelected()
	{
		return SLAVE_CHARS_TAB_ID.equals(selectedTabId);
	}

	public boolean isGroupSlaveNumsSelected()
	{
		return SLAVE_NUMS_TAB_ID.equals(selectedTabId);
	}

	public boolean isGroupSourcesSelected()
	{
		return SOURCES_TAB_ID.equals(selectedTabId);
	}

	public VoyagesListBean getListBean()
	{
		return listBean;
	}

	public void setListBean(VoyagesListBean listBean)
	{
		this.listBean = listBean;
	}

}
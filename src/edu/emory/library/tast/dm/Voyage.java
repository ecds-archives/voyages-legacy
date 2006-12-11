package edu.emory.library.tast.dm;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;

import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.BooleanAttribute;
import edu.emory.library.tast.dm.attributes.DateAttribute;
import edu.emory.library.tast.dm.attributes.EstimatesNationAttribute;
import edu.emory.library.tast.dm.attributes.FateAttribute;
import edu.emory.library.tast.dm.attributes.FateOwnerAttribute;
import edu.emory.library.tast.dm.attributes.FateSlavesAttribute;
import edu.emory.library.tast.dm.attributes.FateVesselAttribute;
import edu.emory.library.tast.dm.attributes.ImportableAttribute;
import edu.emory.library.tast.dm.attributes.ResistanceAttribute;
import edu.emory.library.tast.dm.attributes.NationAttribute;
import edu.emory.library.tast.dm.attributes.NumericAttribute;
import edu.emory.library.tast.dm.attributes.PortAttribute;
import edu.emory.library.tast.dm.attributes.RegionAttribute;
import edu.emory.library.tast.dm.attributes.StringAttribute;
import edu.emory.library.tast.dm.attributes.VesselRigAttribute;
import edu.emory.library.tast.util.HibernateConnector;
import edu.emory.library.tast.util.HibernateUtil;

/**
 * Voyage object.
 * @author Pawel Jurczyk
 *
 */
public class Voyage extends AbstractDescriptiveObject {
	
	/**
	 * ID of voyage.
	 */
	private Long iid;
	
	/**
	 * Object's attributes
	 */
	private static List attributes = new ArrayList();
	static {
		attributes.add(new NumericAttribute("iid", "Voyage", NumericAttribute.TYPE_LONG, null));
		attributes.add(new NumericAttribute("voyageid", "Voyage", NumericAttribute.TYPE_LONG, "voyageid"));
		attributes.add(new BooleanAttribute("cd", "Voyage", "cd"));
		attributes.add(new StringAttribute("shipname", "Voyage", "shipname"));
		attributes.add(new PortAttribute("placcons", "Voyage", "placcons"));
		attributes.add(new NumericAttribute("yrcons", "Voyage", NumericAttribute.TYPE_INTEGER, "yrcons"));
		attributes.add(new PortAttribute("placreg", "Voyage", "placreg"));
		attributes.add(new NumericAttribute("yrreg", "Voyage", NumericAttribute.TYPE_INTEGER, "yrreg"));
		attributes.add(new NationAttribute("natinimp", "Voyage", "natinimp"));
		attributes.add(new VesselRigAttribute("rig", "Voyage", "rig"));
		attributes.add(new NumericAttribute("tonnage", "Voyage", NumericAttribute.TYPE_FLOAT, "tonnage"));
		attributes.add(new NumericAttribute("tonmod", "Voyage", NumericAttribute.TYPE_FLOAT, "tonmod"));
		attributes.add(new NumericAttribute("guns", "Voyage", NumericAttribute.TYPE_INTEGER, "guns"));
		attributes.add(new StringAttribute("ownera", "Voyage", "ownera"));
		attributes.add(new StringAttribute("ownerb", "Voyage", "ownerb"));
		attributes.add(new StringAttribute("ownerc", "Voyage", "ownerc"));
		attributes.add(new StringAttribute("ownerd", "Voyage", "ownerd"));
		attributes.add(new StringAttribute("ownere", "Voyage", "ownere"));
		attributes.add(new StringAttribute("ownerf", "Voyage", "ownerf"));
		attributes.add(new StringAttribute("ownerg", "Voyage", "ownerg"));
		attributes.add(new StringAttribute("ownerh", "Voyage", "ownerh"));
		attributes.add(new StringAttribute("owneri", "Voyage", "owneri"));
		attributes.add(new StringAttribute("ownerj", "Voyage", "ownerj"));
		attributes.add(new StringAttribute("ownerk", "Voyage", "ownerk"));
		attributes.add(new StringAttribute("ownerl", "Voyage", "ownerl"));
		attributes.add(new StringAttribute("ownerm", "Voyage", "ownerm"));
		attributes.add(new StringAttribute("ownern", "Voyage", "ownern"));
		attributes.add(new StringAttribute("ownero", "Voyage", "ownero"));
		attributes.add(new StringAttribute("ownerp", "Voyage", "ownerp"));
		attributes.add(new FateAttribute("fate", "Voyage", "fate"));
		attributes.add(new FateSlavesAttribute("fate2", "Voyage", "fate2"));
		attributes.add(new FateVesselAttribute("fate3", "Voyage", "fate3"));
		attributes.add(new FateOwnerAttribute("fate4", "Voyage", "fate4"));
		attributes.add(new ResistanceAttribute("resistance", "Voyage", "resistance"));
		attributes.add(new PortAttribute("ptdepimp", "Voyage", "ptdepimp"));
		attributes.add(new RegionAttribute("deptregimp", "Voyage", "deptregimp"));
		attributes.add(new PortAttribute("plac1tra", "Voyage", "plac1tra"));
		attributes.add(new PortAttribute("plac2tra", "Voyage", "plac2tra"));
		attributes.add(new PortAttribute("plac3tra", "Voyage", "plac3tra"));
		attributes.add(new PortAttribute("mjbyptimp", "Voyage", "mjbyptimp"));
		attributes.add(new RegionAttribute("regem1", "Voyage", "regem1"));
		attributes.add(new RegionAttribute("regem2", "Voyage", "regem2"));
		attributes.add(new RegionAttribute("regem3", "Voyage", "regem3"));
		attributes.add(new RegionAttribute("majbyimp", "Voyage", "majbyimp"));
		attributes.add(new PortAttribute("npafttra", "Voyage", "npafttra"));
		attributes.add(new PortAttribute("sla1port", "Voyage", "sla1port"));
		attributes.add(new PortAttribute("adpsale1", "Voyage", "adpsale1"));
		attributes.add(new PortAttribute("adpsale2", "Voyage", "adpsale2"));
		attributes.add(new PortAttribute("mjslptimp", "Voyage", "mjslptimp"));
		attributes.add(new RegionAttribute("regdis1", "Voyage", "regdis1"));
		attributes.add(new RegionAttribute("regdis2", "Voyage", "regdis2"));
		attributes.add(new RegionAttribute("regdis3", "Voyage", "regdis3"));
		attributes.add(new RegionAttribute("mjselimp", "Voyage", "mjselimp"));
		attributes.add(new PortAttribute("portret", "Voyage", "portret"));
		attributes.add(new RegionAttribute("retrnreg", "Voyage", "retrnreg"));
		attributes.add(new NumericAttribute("yearam", "Voyage", NumericAttribute.TYPE_INTEGER, "yearam"));
		attributes.add(new DateAttribute("datedep", "Voyage", "date_dep"));
		attributes.add(new DateAttribute("datebuy", "Voyage", "date_buy"));
		attributes.add(new DateAttribute("dateleftafr", "Voyage", "date_leftafr"));
		attributes.add(new DateAttribute("dateland1", "Voyage", "date_land1"));
		attributes.add(new DateAttribute("dateland2", "Voyage", "date_land2"));
		attributes.add(new DateAttribute("dateland3", "Voyage", "date_land3"));
		attributes.add(new DateAttribute("datedepam", "Voyage", "date_depam"));
		attributes.add(new DateAttribute("dateend", "Voyage", "date_end"));
		attributes.add(new NumericAttribute("voy1imp", "Voyage", NumericAttribute.TYPE_INTEGER, "voy1imp"));
		attributes.add(new NumericAttribute("voy2imp", "Voyage", NumericAttribute.TYPE_INTEGER, "voy2imp"));
		attributes.add(new StringAttribute("captaina", "Voyage", "captaina"));
		attributes.add(new StringAttribute("captainb", "Voyage", "captainb"));
		attributes.add(new StringAttribute("captainc", "Voyage", "captainc"));
		attributes.add(new NumericAttribute("crew1", "Voyage", NumericAttribute.TYPE_INTEGER, "crew1"));
		attributes.add(new NumericAttribute("crew3", "Voyage", NumericAttribute.TYPE_INTEGER, "crew3"));
		attributes.add(new NumericAttribute("crewdied", "Voyage", NumericAttribute.TYPE_INTEGER, "crewdied"));
		attributes.add(new NumericAttribute("slintend", "Voyage", NumericAttribute.TYPE_INTEGER, "slintend"));
		attributes.add(new NumericAttribute("ncar13", "Voyage", NumericAttribute.TYPE_INTEGER, "ncar13"));
		attributes.add(new NumericAttribute("ncar15", "Voyage", NumericAttribute.TYPE_INTEGER, "ncar15"));
		attributes.add(new NumericAttribute("ncar17", "Voyage", NumericAttribute.TYPE_INTEGER, "ncar17"));
		attributes.add(new NumericAttribute("tslavesd", "Voyage", NumericAttribute.TYPE_INTEGER, "tslavesd"));
		attributes.add(new NumericAttribute("slaarriv", "Voyage", NumericAttribute.TYPE_INTEGER, "slaarriv"));
		attributes.add(new NumericAttribute("slas32", "Voyage", NumericAttribute.TYPE_INTEGER, "slas32"));
		attributes.add(new NumericAttribute("slas36", "Voyage", NumericAttribute.TYPE_INTEGER, "slas36"));
		attributes.add(new NumericAttribute("slas39", "Voyage", NumericAttribute.TYPE_INTEGER, "slas39"));
		attributes.add(new NumericAttribute("slaximp", "Voyage", NumericAttribute.TYPE_FLOAT, "slaximp"));
		attributes.add(new NumericAttribute("slamimp", "Voyage", NumericAttribute.TYPE_FLOAT, "slamimp"));
		attributes.add(new NumericAttribute("menrat7", "Voyage", NumericAttribute.TYPE_FLOAT, "menrat7"));
		attributes.add(new NumericAttribute("womrat7", "Voyage", NumericAttribute.TYPE_FLOAT, "womrat7"));
		attributes.add(new NumericAttribute("boyrat7", "Voyage", NumericAttribute.TYPE_FLOAT, "boyrat7"));
		attributes.add(new NumericAttribute("girlrat7", "Voyage", NumericAttribute.TYPE_FLOAT, "girlrat7"));
		attributes.add(new NumericAttribute("malrat7", "Voyage", NumericAttribute.TYPE_FLOAT, "malrat7"));
		attributes.add(new NumericAttribute("chilrat7", "Voyage", NumericAttribute.TYPE_FLOAT, "chilrat7"));
		attributes.add(new NumericAttribute("jamcaspr", "Voyage", NumericAttribute.TYPE_FLOAT, "jamcaspr"));
		attributes.add(new NumericAttribute("vymrtimp", "Voyage", NumericAttribute.TYPE_FLOAT, "vymrtimp"));
		attributes.add(new NumericAttribute("vymrtrat", "Voyage", NumericAttribute.TYPE_FLOAT, "vymrtrat"));
		attributes.add(new StringAttribute("sourcea", "Voyage", "sourcea"));
		attributes.add(new StringAttribute("sourceb", "Voyage", "sourceb"));
		attributes.add(new StringAttribute("sourcec", "Voyage", "sourcec"));
		attributes.add(new StringAttribute("sourced", "Voyage", "sourced"));
		attributes.add(new StringAttribute("sourcee", "Voyage", "sourcee"));
		attributes.add(new StringAttribute("sourcef", "Voyage", "sourcef"));
		attributes.add(new StringAttribute("sourceg", "Voyage", "sourceg"));
		attributes.add(new StringAttribute("sourceh", "Voyage", "sourceh"));
		attributes.add(new StringAttribute("sourcei", "Voyage", "sourcei"));
		attributes.add(new StringAttribute("sourcej", "Voyage", "sourcej"));
		attributes.add(new StringAttribute("sourcek", "Voyage", "sourcek"));
		attributes.add(new StringAttribute("sourcel", "Voyage", "sourcel"));
		attributes.add(new StringAttribute("sourcem", "Voyage", "sourcem"));
		attributes.add(new StringAttribute("sourcen", "Voyage", "sourcen"));
		attributes.add(new StringAttribute("sourceo", "Voyage", "sourceo"));
		attributes.add(new StringAttribute("sourcep", "Voyage", "sourcep"));
		attributes.add(new StringAttribute("sourceq", "Voyage", "sourceq"));
		attributes.add(new StringAttribute("sourcer", "Voyage", "sourcer"));
		attributes.add(new EstimatesNationAttribute("e_natinimp", "Voyage", "e_natinimp"));
		attributes.add(new RegionAttribute("e_majbyimp", "Voyage", "e_majbyimp"));
		attributes.add(new RegionAttribute("e_mjselimp1", "Voyage", "e_mjselimp1"));
		attributes.add(new RegionAttribute("e_mjselimp", "Voyage", "e_mjselimp")); 
	}
	
	/**
	 * Gets all attributes of voyage.
	 * @return
	 */
	public static ImportableAttribute[] getAttributes() {
		return (ImportableAttribute[])attributes.toArray(new ImportableAttribute[] {});
	}
	
	/**
	 * Gets attribute with given name.
	 * @param name
	 * @return attribute, null if there is no attribute with given name
	 */
	public static Attribute getAttribute(String name) {
		for (int i = 0; i < attributes.size(); i++) {
			if (((Attribute)attributes.get(i)).getName().equals(name)) {
				return (Attribute)attributes.get(i);
			}
		}
		return null;
	}
	
	/**
	 * Gets names of all attributes in voyage.
	 * @return
	 */
	public static String[] getAllAttrNames() {
		String[] attrsName = new String[attributes.size()];
		for (int i = 0; i < attrsName.length; i++) {
			attrsName[i] = ((Attribute)attributes.get(i)).getName();
		}
		return attrsName;
	}
	
	/**
	 * Creates new Voyage. Object will have new ID.
	 */
	public Voyage() {
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
		}
		session.close();
		
		return localVoyage;
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
		}
		session.close();
		return ret;
	}
	
	/**
	 * Loads a most reecnt voyages.
	 * @param p_firstResult first result
	 * @param p_fetchSize suze of package
	 * @return
	 */
	public static Voyage[] loadAllMostRecent(int p_firstResult, int p_fetchSize) {
		
		Session session = HibernateUtil.getSession();
		//Load voyage from DB
		VoyageIndex[] voyageIndex = HibernateConnector.getConnector()
			.getVoyagesIndexSet(session, p_firstResult, p_fetchSize, HibernateConnector.APPROVED_AND_NOT_APPROVED & HibernateConnector.WITHOUT_HISTORY);
		
		Voyage[] ret = new Voyage[voyageIndex.length];
		//Prepare result
		for (int i = 0; i < voyageIndex.length; i++) {
			ret[i] = voyageIndex[i].getVoyage();
			
		}
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
			list.add(v);
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
		vIndex.setRevisionDate(new Date(System.currentTimeMillis()));
		
		//Save to DB (or update...)
		HibernateConnector.getConnector().createVoyage(vIndex);
	}

	/**
	 * Gets deep copy of object.
	 */
	public Object clone() {
		//Copy voyage itself
		Voyage newVoyage = new Voyage();
		
		newVoyage.values = this.values;
		
		//Return copy object
		return newVoyage;
	}

	public void setIid(Long iid) {
		this.iid = iid;
	}
	
	public Long getIid() {
		return this.iid;
	}
	
	/**
	 * Returns string representation of object.
	 */
	public String toString() {
		return "Voyage: " + values;
	}
	
	public void saveOrUpdate()
	{
		HibernateConnector.getConnector().saveOrUpdateObject(this);
	}

	public void saveOrUpdate(Session sess)
	{
		sess.saveOrUpdate(this);
	}
	
	public void setVoyageId(Long voyageId) {
		this.values.put("voyageId", voyageId);
	}
	public void setCd(Boolean cd) {
		this.values.put("cd", cd);
	}
	public void setSlas32(Integer  slas32) {
		this.values.put("slas32", slas32);
	}
	public void setMjslptimp(Port mjslptimp) {
		this.values.put("mjslptimp", mjslptimp);
	}
	public Port getMjslptimp() {
		return (Port) this.values.get("mjslptimp");
	}
	public void setShipname(String shipname) {
		this.values.put("shipname", shipname);
	}
	public void setCaptaina(String captaina) {
		this.values.put("captaina", captaina);
	}
	public void setCaptainb(String captainb) {
		this.values.put("captainb", captainb);
	}
	public void setCaptainc(String captainc) {
		this.values.put("captainc", captainc);
	}
	public void setDatedep(Date datedep) {
		this.values.put("datedep", datedep);
	}
	public void setTslavesd(Integer tslavesd) {
		this.values.put("tslavesd", tslavesd);
	}
	public void setSlaarriv(Integer slaarriv) {
		this.values.put("slaarriv", slaarriv);
	}
	public void setSlas36(Integer slas36) {
		this.values.put("slas36", slas36);
	}
	public void setSlas39(Integer slas39) {
		this.values.put("slas39", slas39);
	}
	public void setFate(Fate fate) {
		this.values.put("fate", fate);
	}
	public void setSourcea(String sourcea) {
		this.values.put("sourcea", sourcea);
	}
	public void setSourceb(String sourceb) {
		this.values.put("sourceb", sourceb);
	}
	public void setSourcec(String sourcec) {
		this.values.put("sourcec", sourcec);
	}
	public void setSourced(String sourced) {
		this.values.put("sourced", sourced);
	}
	public void setSourcee(String sourcee) {
		this.values.put("sourcee", sourcee);
	}
	public void setSourcef(String sourcef) {
		this.values.put("sourcef", sourcef);
	}
	public void setSourceg(String sourceg) {
		this.values.put("sourceg", sourceg);
	}
	public void setSourceh(String sourceh) {
		this.values.put("sourceh", sourceh);
	}
	public void setSourcei(String sourcei) {
		this.values.put("sourcei", sourcei);
	}
	public void setSourcej(String sourcej) {
		this.values.put("sourcej", sourcej);
	}
	public void setSourcek(String sourcek) {
		this.values.put("sourcek", sourcek);
	}
	public void setSourcel(String sourcel) {
		this.values.put("sourcel", sourcel);
	}
	public void setSourcem(String sourcem) {
		this.values.put("sourcem", sourcem);
	}
	public void setSourcen(String sourcen) {
		this.values.put("sourcen", sourcen);
	}
	public void setSourceo(String sourceo) {
		this.values.put("sourceo", sourceo);
	}
	public void setSourcep(String sourcep) {
		this.values.put("sourcep", sourcep);
	}
	public void setSourceq(String sourceq) {
		this.values.put("sourceq", sourceq);
	}
	public void setSourcer(String sourcer) {
		this.values.put("sourcer", sourcer);
	}
	public void setSlintend(Integer slintend) {
		this.values.put("slintend", slintend);
	}
	public void setTonnage(Float tonnage) {
		this.values.put("tonnage", tonnage);
	}
	public void setCrewdied(Integer crewdied) {
		this.values.put("crewdied", crewdied);
	}
	public void setNcar13(Integer ncar13) {
		this.values.put("ncar13", ncar13);
	}
	public void setNcar15(Integer ncar15) {
		this.values.put("ncar15", ncar15);
	}
	public void setNcar17(Integer ncar17) {
		this.values.put("ncar17", ncar17);
	}
	public void setGuns(Integer guns) {
		this.values.put("guns", guns);
	}
	public void setCrew1(Integer crew1) {
		this.values.put("crew1", crew1);
	}
	public void setYrreg(Integer yrreg) {
		this.values.put("yrreg", yrreg);
	}
	public void setCrew3(Integer crew3) {
		this.values.put("crew3", crew3);
	}
	public void setResistance(Resistance resistance) {
		this.values.put("resistance", resistance);
	}
	public void setPtdepimp(Port ptdepimp) {
		this.values.put("ptdepimp", ptdepimp);
	}
	public void setOwnera(String ownera) {
		this.values.put("ownera", ownera);
	}
	public void setOwnerb(String ownerb) {
		this.values.put("ownerb", ownerb);
	}
	public void setOwnerc(String ownerc) {
		this.values.put("ownerc", ownerc);
	}
	public void setOwnerd(String ownerd) {
		this.values.put("ownerd", ownerd);
	}
	public void setOwnere(String ownere) {
		this.values.put("ownere", ownere);
	}
	public void setOwnerf(String ownerf) {
		this.values.put("ownerf", ownerf);
	}
	public void setOwnerg(String ownerg) {
		this.values.put("ownerg", ownerg);
	}
	public void setOwnerh(String ownerh) {
		this.values.put("ownerh", ownerh);
	}
	public void setOwneri(String owneri) {
		this.values.put("owneri", owneri);
	}
	public void setOwnerj(String ownerj) {
		this.values.put("ownerj", ownerj);
	}
	public void setOwnerk(String ownerk) {
		this.values.put("ownerk", ownerk);
	}
	public void setOwnerl(String ownerl) {
		this.values.put("ownerl", ownerl);
	}
	public void setOwnerm(String ownerm) {
		this.values.put("ownerm", ownerm);
	}
	public void setOwnern(String ownern) {
		this.values.put("ownern", ownern);
	}
	public void setOwnero(String ownero) {
		this.values.put("ownero", ownero);
	}
	public void setOwnerp(String ownerp) {
		this.values.put("ownerp", ownerp);
	}
	public void setYearam(Integer yearam) {
		this.values.put("yearam", yearam);
	}
	public void setTonmod(Float tonmod) {
		this.values.put("tonmod", tonmod);
	}
	public void setVymrtimp(Float vymrtimp) {
		this.values.put("vymrtimp", vymrtimp);
	}
	public void setVymrtrat(Float vymrtrat) {
		this.values.put("vymrtrat", vymrtrat);
	}
	public void setSlaximp(Float slaximp) {
		this.values.put("slaximp", slaximp);
	}
	public void setSlamimp(Float slamimp) {
		this.values.put("slamimp", slamimp);
	}
	public void setVoy2imp(Integer voy2imp) {
		this.values.put("voy2imp", voy2imp);
	}
	public void setVoy1imp(Integer voy1imp) {
		this.values.put("voy1imp", voy1imp);
	}
	public void setMalrat7(Float malrat7) {
		this.values.put("malrat7", malrat7);
	}
	public void setChilrat7(Float chilrat7) {
		this.values.put("chilrat7", chilrat7);
	}
	public void setWomrat7(Float womrat7) {
		this.values.put("womrat7", womrat7);
	}
	public void setMenrat7(Float menrat7) {
		this.values.put("menrat7", menrat7);
	}
	public void setGirlrat7(Float girlrat7) {
		this.values.put("girlrat7", girlrat7);
	}
	public void setBoyrat7(Float boyrat7) {
		this.values.put("boyrat7", boyrat7);
	}
	public void setJamcaspr(Float jamcaspr) {
		this.values.put("jamcaspr", jamcaspr);
	}
	public void setPlac1tra(Port plac1tra) {
		this.values.put("plac1tra", plac1tra);
	}
	public void setPlac2tra(Port plac2tra) {
		this.values.put("plac2tra", plac2tra);
	}
	public void setPlac3tra(Port plac3tra) {
		this.values.put("plac3tra", plac3tra);
	}
	public void setNpafttra(Port npafttra) {
		this.values.put("npafttra", npafttra);
	}
	public void setSla1port(Port sla1port) {
		this.values.put("sla1port", sla1port);
	}
	public void setAdpsale1(Port adpsale1) {
		this.values.put("adpsale1", adpsale1);
	}
	public void setAdpsale2(Port adpsale2) {
		this.values.put("adpsale2", adpsale2);
	}
	public void setPortret(Port portret) {
		this.values.put("portret", portret);
	}
	public void setRig(VesselRig rig) {
		this.values.put("rig", rig);
	}
	public void setPlaccons(Port placcons) {
		this.values.put("placcons", placcons);
	}
	public void setPlacreg(Port placreg) {
		this.values.put("placreg", placreg);
	}
	public void setNatinimp(Nation natinimp) {
		this.values.put("natinimp", natinimp);
	}
	public void setRetrnreg(Region retrnreg) {
		this.values.put("retrnreg", retrnreg);
	}
	public void setRegem1(Region regem1) {
		this.values.put("regem1", regem1);
	}
	public void setRegem2(Region regem2) {
		this.values.put("regem2", regem2);
	}
	public void setRegem3(Region regem3) {
		this.values.put("regem3", regem3);
	}
	public void setMajbyimp(Region majbyimp) {
		this.values.put("majbyimp", majbyimp);
	}
	public void setRegdis1(Region regdis1) {
		this.values.put("regdis1", regdis1);
	}
	public void setRegdis2(Region regdis2) {
		this.values.put("regdis2", regdis2);
	}
	public void setRegdis3(Region regdis3) {
		this.values.put("regdis3", regdis3);
	}
	public void setFate2(FateSlaves fate2) {
		this.values.put("fate2", fate2);
	}
	public void setFate3(FateVessel fate3) {
		this.values.put("fate3", fate3);
	}
	public void setFate4(FateOwner fate4) {
		this.values.put("fate4", fate4);
	}
	public void setMjselimp(Region mjselimp) {
		this.values.put("mjselimp", mjselimp);
	}
	public void setMjbyptimp(Port purchasePort) {
		this.values.put("mjbyptimp", purchasePort);
	}
	public void setYrcons(Integer obj) {
		this.values.put("yrcons", obj);
	}
	public void setDatebuy(Date obj) {
		this.values.put("datebuy", obj);
	}
	public void setDatedepam(Date obj) {
		this.values.put("datedepam", obj);
	}
	public void setDateend(Date obj) {
		this.values.put("dateend", obj);
	}
	public void setDateland1(Date obj) {
		this.values.put("dateland1", obj);
	}
	public void setDateland2(Date obj) {
		this.values.put("dateland2", obj);
	}
	public void setDateland3(Date obj) {
		this.values.put("dateland3", obj);
	}
	public void setDateleftafr(Date obj) {
		this.values.put("dateleftafr", obj);
	}
	public void setDeptregimp(Region obj) {
		this.values.put("deptregimp", obj);
	}
	public void setE_majbyimp(Region obj) {
		this.values.put("e_majbyimp", obj);
	}
	public void setE_mjselimp(Region obj) {
		this.values.put("e_mjselimp", obj);
	}
	public void setE_mjselimp1(Region obj) {
		this.values.put("e_mjselimp1", obj);
	}
	public void setE_natinimp(EstimatesNation obj) {
		this.values.put("e_natinimp", obj);
	}
	
	public Port getSla1port() {
		return (Port) this.values.get("sla1port");
	}
	public Integer getYrcons() {
		return (Integer)this.values.get("yrcons");
	}
	public Date getDatebuy() {
		return (Date)this.values.get("datebuy");
	}
	public Date getDatedepam() {
		return (Date)this.values.get("datedepam");
	}
	public Date getDateend() {
		return (Date)this.values.get("dateend");
	}
	public Date getDateland1() {
		return (Date)this.values.get("dateland1");
	}
	public Date getDateland2() {
		return (Date)this.values.get("dateland2");
	}
	public Date getDateland3() {
		return (Date)this.values.get("dateland3");
	}
	public Date getDateleftafr() {
		return (Date)this.values.get("dateleftafr");
	}
	public Region getDeptregimp() {
		return (Region)this.values.get("deptregimp");
	}
	public Region getE_majbyimp() {
		return (Region)this.values.get("e_majbyimp");
	}
	public Region getE_mjselimp() {
		return (Region)this.values.get("e_mjselimp");
	}
	public Region getE_mjselimp1() {
		return (Region)this.values.get("e_mjselimp1");
	}
	public EstimatesNation getE_natinimp() {
		return (EstimatesNation)this.values.get("e_natinimp");
	}
	
	public Long getVoyageId() {
		return (Long)this.values.get("voyageId");
	}
	public Boolean getCd() {
		return (Boolean)this.values.get("cd");
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
	public Integer getTslavesd() {
		return (Integer)this.values.get("tslavesd");
	}
	public Integer getSlaarriv() {
		return (Integer)this.values.get("slaarriv");
	}
	public Integer getSlas32() {
		return (Integer)this.values.get("slas32");
	}
	public Integer getSlas36() {
		return (Integer)this.values.get("slas36");
	}
	public Integer getSlas39() {
		return (Integer)this.values.get("slas39");
	}
	public Fate getFate() {
		return (Fate)this.values.get("fate");
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
	public Float getTonnage() {
		return (Float)this.values.get("tonnage");
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
	public Integer getGuns() {
		return (Integer)this.values.get("guns");
	}
	public Integer getCrew1() {
		return (Integer)this.values.get("crew1");
	}
	public Integer getYrreg() {
		return (Integer)this.values.get("yrreg");
	}
	public Integer getCrew3() {
		return (Integer)this.values.get("crew3");
	}
	public Resistance getResistance() {
		return (Resistance)this.values.get("resistance");
	}
	public Port getPtdepimp() {
		return (Port)this.values.get("ptdepimp");
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
	public Integer getYearam() {
		return (Integer)this.values.get("yearam");
	}
	public Float getTonmod() {
		return (Float)this.values.get("tonmod");
	}
	public Float getVymrtimp() {
		return (Float)this.values.get("vymrtimp");
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
	public Float getWomrat7() {
		return (Float)this.values.get("womrat7");
	}
	public Float getMenrat7() {
		return (Float)this.values.get("menrat7");
	}
	public Float getGirlrat7() {
		return (Float)this.values.get("girlrat7");
	}
	public Float getBoyrat7() {
		return (Float)this.values.get("boyrat7");
	}
	public Float getJamcaspr() {
		return (Float)this.values.get("jamcaspr");
	}
	public Port getPlac1tra() {
		return (Port)this.values.get("plac1tra");
	}
	public Port getPlac2tra() {
		return (Port)this.values.get("plac2tra");
	}
	public Port getPlac3tra() {
		return (Port)this.values.get("plac3tra");
	}
	public Port getNpafttra() {
		return (Port)this.values.get("npafttra");
	}
	public Port getAdpsale1() {
		return (Port)this.values.get("adpsale1");
	}
	public Port getAdpsale2() {
		return (Port)this.values.get("adpsale2");
	}
	public Port getPortret() {
		return (Port)this.values.get("portret");
	}
	public VesselRig getRig() {
		return (VesselRig)this.values.get("rig");
	}
	public Port getPlaccons() {
		return (Port)this.values.get("placcons");
	}
	public Port getPlacreg() {
		return (Port)this.values.get("placreg");
	}
	public Nation getNatinimp() {
		return (Nation)this.values.get("natinimp");
	}
	public Region getRetrnreg() {
		return (Region)this.values.get("retrnreg");
	}
	public Region getRegem1() {
		return (Region)this.values.get("regem1");
	}
	public Region getRegem2() {
		return (Region)this.values.get("regem2");
	}
	public Region getRegem3() {
		return (Region)this.values.get("regem3");
	}
	public Region getMajbyimp() {
		return (Region)this.values.get("majbyimp");
	}
	public Region getRegdis1() {
		return (Region)this.values.get("regdis1");
	}
	public Region getRegdis2() {
		return (Region)this.values.get("regdis2");
	}
	public Region getRegdis3() {
		return (Region)this.values.get("regdis3");
	}
	public FateSlaves getFate2() {
		return (FateSlaves)this.values.get("fate2");
	}
	public FateVessel getFate3() {
		return (FateVessel)this.values.get("fate3");
	}
	public FateOwner getFate4() {
		return (FateOwner)this.values.get("fate4");
	}
	public Region getMjselimp() {
		return (Region)this.values.get("mjselimp");
	}
	public Port getMjbyptimp() {
		return (Port)this.values.get("mjbyptimp");
	}

}

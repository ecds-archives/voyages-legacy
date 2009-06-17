package edu.emory.library.tast.dm;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import edu.emory.library.tast.db.HibernateConn;
import edu.emory.library.tast.db.TastDbConditions;
import edu.emory.library.tast.db.TastDbQuery;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.BooleanAttribute;
import edu.emory.library.tast.dm.attributes.DateAttribute;
import edu.emory.library.tast.dm.attributes.EstimatesExportRegionAttribute;
import edu.emory.library.tast.dm.attributes.EstimatesImportRegionAttribute;
import edu.emory.library.tast.dm.attributes.EstimatesNationAttribute;
import edu.emory.library.tast.dm.attributes.FateAttribute;
import edu.emory.library.tast.dm.attributes.FateOwnerAttribute;
import edu.emory.library.tast.dm.attributes.FateSlavesAttribute;
import edu.emory.library.tast.dm.attributes.FateVesselAttribute;
import edu.emory.library.tast.dm.attributes.ImportableAttribute;
import edu.emory.library.tast.dm.attributes.NationAttribute;
import edu.emory.library.tast.dm.attributes.NumericAttribute;
import edu.emory.library.tast.dm.attributes.PortAttribute;
import edu.emory.library.tast.dm.attributes.RegionAttribute;
import edu.emory.library.tast.dm.attributes.ResistanceAttribute;
import edu.emory.library.tast.dm.attributes.StringAttribute;
import edu.emory.library.tast.dm.attributes.VesselRigAttribute;
import edu.emory.library.tast.util.StringUtils;

/**
 * Voyage object.
 * 
 */
public class Voyage extends AbstractDescriptiveObject
{

	/**
	 * ID of voyage.
	 */
	private Long iid;

	/**
	 * Object's attributes
	 */
	private static List attributes = new ArrayList();
	private static Map attributesLookup = new HashMap();
	static {
		attributes.add(new NumericAttribute("iid", "Voyage", NumericAttribute.TYPE_LONG, null));
		attributes.add(new NumericAttribute("voyageid", "Voyage", NumericAttribute.TYPE_INTEGER, "voyageid"));
		attributes.add(new BooleanAttribute("suggestion", "Voyage", null));
		attributes.add(new NumericAttribute("revision", "Voyage", NumericAttribute.TYPE_INTEGER, "revision"));
		attributes.add(new NumericAttribute("iid", "Voyage", NumericAttribute.TYPE_LONG, null));
		attributes.add(new StringAttribute("shipname", "Voyage", "shipname"));
		attributes.add(new PortAttribute("placcons", "Voyage", "placcons"));
		attributes.add(new RegionAttribute("constreg", "Voyage", "constreg"));
		attributes.add(new NumericAttribute("yrcons", "Voyage", NumericAttribute.TYPE_INTEGER, "yrcons"));
		attributes.add(new PortAttribute("placreg", "Voyage", "placreg"));
		attributes.add(new RegionAttribute("regisreg", "Voyage", "regisreg"));
		attributes.add(new NumericAttribute("yrreg", "Voyage", NumericAttribute.TYPE_INTEGER, "yrreg"));
		attributes.add(new NationAttribute("natinimp", "Voyage", "natinimp"));
		attributes.add(new VesselRigAttribute("rig", "Voyage", "rig"));
		attributes.add(new NumericAttribute("tonnage", "Voyage", NumericAttribute.TYPE_INTEGER, "tonnage"));
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
		attributes.add(new RegionAttribute("deptregimp1", "Voyage", "deptregimp1"));
		attributes.add(new PortAttribute("plac1tra", "Voyage", "plac1tra"));
		attributes.add(new PortAttribute("plac2tra", "Voyage", "plac2tra"));
		attributes.add(new PortAttribute("plac3tra", "Voyage", "plac3tra"));
		attributes.add(new PortAttribute("mjbyptimp", "Voyage", "mjbyptimp"));
		attributes.add(new RegionAttribute("regem1", "Voyage", "regem1"));
		attributes.add(new RegionAttribute("regem2", "Voyage", "regem2"));
		attributes.add(new RegionAttribute("regem3", "Voyage", "regem3"));
		attributes.add(new RegionAttribute("majbyimp", "Voyage", "majbyimp"));
		attributes.add(new RegionAttribute("majbyimp1", "Voyage", "majbyimp1"));
		attributes.add(new PortAttribute("npafttra", "Voyage", "npafttra"));
		attributes.add(new PortAttribute("sla1port", "Voyage", "sla1port"));
		attributes.add(new PortAttribute("adpsale1", "Voyage", "adpsale1"));
		attributes.add(new PortAttribute("adpsale2", "Voyage", "adpsale2"));
		attributes.add(new PortAttribute("mjslptimp", "Voyage", "mjslptimp"));
		attributes.add(new PortAttribute("portdep", "Voyage", "portdep"));
		attributes.add(new RegionAttribute("regdis1", "Voyage", "regdis1"));
		attributes.add(new RegionAttribute("regdis2", "Voyage", "regdis2"));
		attributes.add(new RegionAttribute("regdis3", "Voyage", "regdis3"));
		attributes.add(new RegionAttribute("mjselimp", "Voyage", "mjselimp"));
		attributes.add(new PortAttribute("portret", "Voyage", "portret"));
		attributes.add(new RegionAttribute("retrnreg", "Voyage", "retrnreg"));
		attributes.add(new RegionAttribute("retrnreg1", "Voyage", "retrnreg1"));
		attributes.add(new NumericAttribute("yearam", "Voyage", NumericAttribute.TYPE_INTEGER, "yearam"));
		attributes.add(new NumericAttribute("yearaf", "Voyage", NumericAttribute.TYPE_INTEGER, "yearaf"));
		attributes.add(new NumericAttribute("yeardep", "Voyage", NumericAttribute.TYPE_INTEGER, "yeardep"));
		
		attributes.add(new DateAttribute("datedep", "Voyage", "datedep"));
		attributes.add(new NumericAttribute("datedepa", "Voyage", NumericAttribute.TYPE_INTEGER, "datedepa"));
		attributes.add(new NumericAttribute("datedepb", "Voyage", NumericAttribute.TYPE_INTEGER, "datedepb"));
		attributes.add(new NumericAttribute("datedepc", "Voyage", NumericAttribute.TYPE_INTEGER, "datedepc"));
		
		attributes.add(new DateAttribute("datebuy", "Voyage", "datebuy"));
		attributes.add(new NumericAttribute("d1slatra", "Voyage", NumericAttribute.TYPE_INTEGER, "d1slatra"));
		attributes.add(new NumericAttribute("d1slatrb", "Voyage", NumericAttribute.TYPE_INTEGER, "d1slatrb"));
		attributes.add(new NumericAttribute("d1slatrc", "Voyage", NumericAttribute.TYPE_INTEGER, "d1slatrc"));
		
		attributes.add(new DateAttribute("dateleftafr", "Voyage", "dateleftafr"));
		attributes.add(new NumericAttribute("dlslatra", "Voyage", NumericAttribute.TYPE_INTEGER, "dlslatra"));
		attributes.add(new NumericAttribute("dlslatrb", "Voyage", NumericAttribute.TYPE_INTEGER, "dlslatrb"));
		attributes.add(new NumericAttribute("dlslatrc", "Voyage", NumericAttribute.TYPE_INTEGER, "dlslatrc"));
		
		attributes.add(new DateAttribute("dateland1", "Voyage", "dateland1"));
		attributes.add(new NumericAttribute("datarr32", "Voyage", NumericAttribute.TYPE_INTEGER, "datarr32"));
		attributes.add(new NumericAttribute("datarr33", "Voyage", NumericAttribute.TYPE_INTEGER, "datarr33"));
		attributes.add(new NumericAttribute("datarr34", "Voyage", NumericAttribute.TYPE_INTEGER, "datarr34"));
				
		attributes.add(new DateAttribute("dateland2", "Voyage", "dateland2"));
		attributes.add(new NumericAttribute("datarr36", "Voyage", NumericAttribute.TYPE_INTEGER, "datarr36"));
		attributes.add(new NumericAttribute("datarr37", "Voyage", NumericAttribute.TYPE_INTEGER, "datarr37"));
		attributes.add(new NumericAttribute("datarr38", "Voyage", NumericAttribute.TYPE_INTEGER, "datarr38"));
		
		attributes.add(new DateAttribute("dateland3", "Voyage", "dateland3"));
		attributes.add(new NumericAttribute("datarr39", "Voyage", NumericAttribute.TYPE_INTEGER, "datarr39"));
		attributes.add(new NumericAttribute("datarr40", "Voyage", NumericAttribute.TYPE_INTEGER, "datarr40"));
		attributes.add(new NumericAttribute("datarr41", "Voyage", NumericAttribute.TYPE_INTEGER, "datarr41"));
		
		attributes.add(new DateAttribute("datedepam", "Voyage", "datedepam"));
		attributes.add(new NumericAttribute("ddepam", "Voyage", NumericAttribute.TYPE_INTEGER, "ddepam"));
		attributes.add(new NumericAttribute("ddepamb", "Voyage", NumericAttribute.TYPE_INTEGER, "ddepamb"));
		attributes.add(new NumericAttribute("ddepamc", "Voyage", NumericAttribute.TYPE_INTEGER, "ddepamc"));
		
		attributes.add(new DateAttribute("dateend", "Voyage", "dateend"));
		attributes.add(new NumericAttribute("datarr43", "Voyage", NumericAttribute.TYPE_INTEGER, "datarr43"));
		attributes.add(new NumericAttribute("datarr44", "Voyage", NumericAttribute.TYPE_INTEGER, "datarr44"));
		attributes.add(new NumericAttribute("datarr45", "Voyage", NumericAttribute.TYPE_INTEGER, "datarr45"));
		
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
		attributes.add(new NumericAttribute("slaximp", "Voyage", NumericAttribute.TYPE_INTEGER, "slaximp"));
		attributes.add(new NumericAttribute("slamimp", "Voyage", NumericAttribute.TYPE_INTEGER, "slamimp"));
		attributes.add(new NumericAttribute("menrat7", "Voyage", NumericAttribute.TYPE_FLOAT, "menrat7"));
		attributes.add(new NumericAttribute("womrat7", "Voyage", NumericAttribute.TYPE_FLOAT, "womrat7"));
		attributes.add(new NumericAttribute("boyrat7", "Voyage", NumericAttribute.TYPE_FLOAT, "boyrat7"));
		attributes.add(new NumericAttribute("girlrat7", "Voyage", NumericAttribute.TYPE_FLOAT, "girlrat7"));
		attributes.add(new NumericAttribute("malrat7", "Voyage", NumericAttribute.TYPE_FLOAT, "malrat7"));
		attributes.add(new NumericAttribute("chilrat7", "Voyage", NumericAttribute.TYPE_FLOAT, "chilrat7"));
		
		attributes.add(new NumericAttribute("menrat3", "Voyage", NumericAttribute.TYPE_FLOAT, "menrat3"));
		attributes.add(new NumericAttribute("womrat3", "Voyage", NumericAttribute.TYPE_FLOAT, "womrat3"));
		attributes.add(new NumericAttribute("boyrat3", "Voyage", NumericAttribute.TYPE_FLOAT, "boyrat3"));
		attributes.add(new NumericAttribute("girlrat3", "Voyage", NumericAttribute.TYPE_FLOAT, "girlrat3"));
		attributes.add(new NumericAttribute("malrat3", "Voyage", NumericAttribute.TYPE_FLOAT, "malrat3"));
		attributes.add(new NumericAttribute("chilrat3", "Voyage", NumericAttribute.TYPE_FLOAT, "chilrat3"));
		
		attributes.add(new NumericAttribute("jamcaspr", "Voyage", NumericAttribute.TYPE_FLOAT, "jamcaspr"));
		attributes.add(new NumericAttribute("vymrtimp", "Voyage", NumericAttribute.TYPE_INTEGER, "vymrtimp"));
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
		attributes.add(new EstimatesExportRegionAttribute("e_majbyimp", "Voyage", "e_majbyimp"));
		attributes.add(new EstimatesImportRegionAttribute("e_mjselimp", "Voyage", "e_mjselimp"));
		attributes.add(new NumericAttribute("tontype", "Voyage", NumericAttribute.TYPE_INTEGER, "tontype"));
		attributes.add(new NumericAttribute("sladamer", "Voyage", NumericAttribute.TYPE_INTEGER, "sladamer"));
		attributes.add(new NumericAttribute("saild1", "Voyage", NumericAttribute.TYPE_INTEGER, "saild1"));
		attributes.add(new NumericAttribute("saild2", "Voyage", NumericAttribute.TYPE_INTEGER, "saild2"));
		attributes.add(new NumericAttribute("saild3", "Voyage", NumericAttribute.TYPE_INTEGER, "saild3"));
		attributes.add(new NumericAttribute("saild4", "Voyage", NumericAttribute.TYPE_INTEGER, "saild4"));
		attributes.add(new NumericAttribute("saild5", "Voyage", NumericAttribute.TYPE_INTEGER, "saild5"));
		attributes.add(new NumericAttribute("voyage", "Voyage", NumericAttribute.TYPE_INTEGER, "voyage"));
		attributes.add(new NumericAttribute("child2", "Voyage", NumericAttribute.TYPE_INTEGER, "child2"));
		attributes.add(new NumericAttribute("child3", "Voyage", NumericAttribute.TYPE_INTEGER, "child3"));
		attributes.add(new NumericAttribute("crew4", "Voyage", NumericAttribute.TYPE_INTEGER, "crew4"));
		attributes.add(new NumericAttribute("crew5", "Voyage", NumericAttribute.TYPE_INTEGER, "crew5"));
		attributes.add(new NumericAttribute("adult1", "Voyage", NumericAttribute.TYPE_INTEGER, "adult1"));
		attributes.add(new NumericAttribute("child1", "Voyage", NumericAttribute.TYPE_INTEGER, "child1"));
		attributes.add(new NumericAttribute("female1", "Voyage", NumericAttribute.TYPE_INTEGER, "female1"));
		attributes.add(new NumericAttribute("male1", "Voyage", NumericAttribute.TYPE_INTEGER, "male1"));
		attributes.add(new NumericAttribute("men1", "Voyage", NumericAttribute.TYPE_INTEGER, "men1"));
		attributes.add(new NumericAttribute("women1", "Voyage", NumericAttribute.TYPE_INTEGER, "women1"));
		attributes.add(new NumericAttribute("boy1", "Voyage", NumericAttribute.TYPE_INTEGER, "boy1"));
		attributes.add(new NumericAttribute("girl1", "Voyage", NumericAttribute.TYPE_INTEGER, "girl1"));
		attributes.add(new NumericAttribute("adult2", "Voyage", NumericAttribute.TYPE_INTEGER, "adult2"));
		attributes.add(new NumericAttribute("female2", "Voyage", NumericAttribute.TYPE_INTEGER, "female2"));
		attributes.add(new NumericAttribute("male2", "Voyage", NumericAttribute.TYPE_INTEGER, "male2"));
		attributes.add(new NumericAttribute("men2", "Voyage", NumericAttribute.TYPE_INTEGER, "men2"));
		attributes.add(new NumericAttribute("women2", "Voyage", NumericAttribute.TYPE_INTEGER, "women2"));
		attributes.add(new NumericAttribute("boy2", "Voyage", NumericAttribute.TYPE_INTEGER, "boy2"));
		attributes.add(new NumericAttribute("girl2", "Voyage", NumericAttribute.TYPE_INTEGER, "girl2"));
		attributes.add(new NumericAttribute("adult3", "Voyage", NumericAttribute.TYPE_INTEGER, "adult3"));
		attributes.add(new NumericAttribute("female3", "Voyage", NumericAttribute.TYPE_INTEGER, "female3"));
		attributes.add(new NumericAttribute("male3", "Voyage", NumericAttribute.TYPE_INTEGER, "male3"));
		attributes.add(new NumericAttribute("men3", "Voyage", NumericAttribute.TYPE_INTEGER, "men3"));
		attributes.add(new NumericAttribute("women3", "Voyage", NumericAttribute.TYPE_INTEGER, "women3"));
		attributes.add(new NumericAttribute("boy3", "Voyage", NumericAttribute.TYPE_INTEGER, "boy3"));
		attributes.add(new NumericAttribute("girl3", "Voyage", NumericAttribute.TYPE_INTEGER, "girl3"));
		attributes.add(new NumericAttribute("child4", "Voyage", NumericAttribute.TYPE_INTEGER, "child4"));
		attributes.add(new NumericAttribute("female4", "Voyage", NumericAttribute.TYPE_INTEGER, "female4"));
		attributes.add(new NumericAttribute("male4", "Voyage", NumericAttribute.TYPE_INTEGER, "male4"));
		attributes.add(new NumericAttribute("men4", "Voyage", NumericAttribute.TYPE_INTEGER, "men4"));
		attributes.add(new NumericAttribute("women4", "Voyage", NumericAttribute.TYPE_INTEGER, "women4"));
		attributes.add(new NumericAttribute("boy4", "Voyage", NumericAttribute.TYPE_INTEGER, "boy4"));
		attributes.add(new NumericAttribute("girl4", "Voyage", NumericAttribute.TYPE_INTEGER, "girl4"));
		attributes.add(new NumericAttribute("child6", "Voyage", NumericAttribute.TYPE_INTEGER, "child6"));
		attributes.add(new NumericAttribute("female6", "Voyage", NumericAttribute.TYPE_INTEGER, "female6"));
		attributes.add(new NumericAttribute("male6", "Voyage", NumericAttribute.TYPE_INTEGER, "male6"));
		attributes.add(new NumericAttribute("men6", "Voyage", NumericAttribute.TYPE_INTEGER, "men6"));
		attributes.add(new NumericAttribute("women6", "Voyage", NumericAttribute.TYPE_INTEGER, "women6"));
		attributes.add(new NumericAttribute("boy6", "Voyage", NumericAttribute.TYPE_INTEGER, "boy6"));
		attributes.add(new NumericAttribute("girl6", "Voyage", NumericAttribute.TYPE_INTEGER, "girl6"));
		attributes.add(new NumericAttribute("adult6", "Voyage", NumericAttribute.TYPE_INTEGER, "adult6"));
		
		attributes.add(new NumericAttribute("adult7", "Voyage", NumericAttribute.TYPE_INTEGER, "adult7"));
		attributes.add(new NumericAttribute("child7", "Voyage", NumericAttribute.TYPE_INTEGER, "child7"));
		attributes.add(new NumericAttribute("female7", "Voyage", NumericAttribute.TYPE_INTEGER, "female7"));
		attributes.add(new NumericAttribute("male7", "Voyage", NumericAttribute.TYPE_INTEGER, "male7"));
		attributes.add(new NumericAttribute("men7", "Voyage", NumericAttribute.TYPE_INTEGER, "men7"));
		attributes.add(new NumericAttribute("women7", "Voyage", NumericAttribute.TYPE_INTEGER, "women7"));
		attributes.add(new NumericAttribute("boy7", "Voyage", NumericAttribute.TYPE_INTEGER, "boy7"));
		attributes.add(new NumericAttribute("girl7", "Voyage", NumericAttribute.TYPE_INTEGER, "girl7"));
		
		attributes.add(new NumericAttribute("adlt1imp", "Voyage", NumericAttribute.TYPE_INTEGER, "adlt1imp"));
		attributes.add(new NumericAttribute("chil1imp", "Voyage", NumericAttribute.TYPE_INTEGER, "chil1imp"));
		attributes.add(new NumericAttribute("feml1imp", "Voyage", NumericAttribute.TYPE_INTEGER, "feml1imp"));
		attributes.add(new NumericAttribute("male1imp", "Voyage", NumericAttribute.TYPE_INTEGER, "male1imp"));		
		attributes.add(new NumericAttribute("adlt2imp", "Voyage", NumericAttribute.TYPE_INTEGER, "adlt2imp"));
		attributes.add(new NumericAttribute("chil2imp", "Voyage", NumericAttribute.TYPE_INTEGER, "chil2imp"));
		attributes.add(new NumericAttribute("feml2imp", "Voyage", NumericAttribute.TYPE_INTEGER, "feml2imp"));
		attributes.add(new NumericAttribute("male2imp", "Voyage", NumericAttribute.TYPE_INTEGER, "male2imp"));
		attributes.add(new NumericAttribute("adlt3imp", "Voyage", NumericAttribute.TYPE_INTEGER, "adlt3imp"));
		attributes.add(new NumericAttribute("chil3imp", "Voyage", NumericAttribute.TYPE_INTEGER, "chil3imp"));
		attributes.add(new NumericAttribute("feml3imp", "Voyage", NumericAttribute.TYPE_INTEGER, "feml3imp"));
		attributes.add(new NumericAttribute("male3imp", "Voyage", NumericAttribute.TYPE_INTEGER, "male3imp"));
		
		attributes.add(new NumericAttribute("slavmax1", "Voyage", NumericAttribute.TYPE_INTEGER, "slavmax1"));
		attributes.add(new NumericAttribute("slavema1", "Voyage", NumericAttribute.TYPE_INTEGER, "slavema1"));
		attributes.add(new NumericAttribute("slavemx1", "Voyage", NumericAttribute.TYPE_INTEGER, "slavemx1"));

		attributes.add(new NumericAttribute("slavmax7", "Voyage", NumericAttribute.TYPE_INTEGER, "slavmax7"));
		attributes.add(new NumericAttribute("slavema7", "Voyage", NumericAttribute.TYPE_INTEGER, "slavema7"));
		attributes.add(new NumericAttribute("slavemx7", "Voyage", NumericAttribute.TYPE_INTEGER, "slavemx7"));
		
		attributes.add(new NumericAttribute("crew2", "Voyage", NumericAttribute.TYPE_INTEGER, "crew2"));
		attributes.add(new NumericAttribute("infantm3", "Voyage", NumericAttribute.TYPE_INTEGER, "infantm3"));
		attributes.add(new NumericAttribute("infantf3", "Voyage", NumericAttribute.TYPE_INTEGER, "infantf3"));
		attributes.add(new NumericAttribute("sladied1", "Voyage", NumericAttribute.TYPE_INTEGER, "sladied1"));
		attributes.add(new NumericAttribute("sladied2", "Voyage", NumericAttribute.TYPE_INTEGER, "sladied2"));
		attributes.add(new NumericAttribute("sladied3", "Voyage", NumericAttribute.TYPE_INTEGER, "sladied3"));
		attributes.add(new NumericAttribute("sladied4", "Voyage", NumericAttribute.TYPE_INTEGER, "sladied4"));
		attributes.add(new NumericAttribute("sladied5", "Voyage", NumericAttribute.TYPE_INTEGER, "sladied5"));
		attributes.add(new NumericAttribute("sladied6", "Voyage", NumericAttribute.TYPE_INTEGER, "sladied6"));
		attributes.add(new NumericAttribute("insurrec", "Voyage", NumericAttribute.TYPE_INTEGER, "insurrec"));
		attributes.add(new BooleanAttribute("evgreen", "Voyage", "evgreen"));
		attributes.add(new NumericAttribute("female5", "Voyage", NumericAttribute.TYPE_INTEGER, "female5"));
		attributes.add(new NumericAttribute("male5", "Voyage", NumericAttribute.TYPE_INTEGER, "male5"));
		attributes.add(new NumericAttribute("child5", "Voyage", NumericAttribute.TYPE_INTEGER, "child5"));
		attributes.add(new NumericAttribute("men5", "Voyage", NumericAttribute.TYPE_INTEGER, "men5"));
		attributes.add(new NumericAttribute("women5", "Voyage", NumericAttribute.TYPE_INTEGER, "women5"));
		attributes.add(new NumericAttribute("boy5", "Voyage", NumericAttribute.TYPE_INTEGER, "boy5"));
		attributes.add(new NumericAttribute("girl5", "Voyage", NumericAttribute.TYPE_INTEGER, "girl5"));
		attributes.add(new NumericAttribute("infant3", "Voyage", NumericAttribute.TYPE_INTEGER, "infant3"));
		attributes.add(new NumericAttribute("infant1", "Voyage", NumericAttribute.TYPE_INTEGER, "infant1"));
		attributes.add(new NumericAttribute("adult5", "Voyage", NumericAttribute.TYPE_INTEGER, "adult5"));
		attributes.add(new NumericAttribute("adult4", "Voyage", NumericAttribute.TYPE_INTEGER, "adult4"));
		attributes.add(new NumericAttribute("infant4", "Voyage", NumericAttribute.TYPE_INTEGER, "infant4"));
		attributes.add(new NumericAttribute("crew", "Voyage", NumericAttribute.TYPE_INTEGER, "crew"));
		attributes.add(new PortAttribute("embport", "Voyage", "embport"));
		attributes.add(new PortAttribute("embport2", "Voyage", "embport2"));
		attributes.add(new PortAttribute("arrport", "Voyage", "arrport"));
		attributes.add(new PortAttribute("arrport2", "Voyage", "arrport2"));
		attributes.add(new NumericAttribute("nppretra", "Voyage", NumericAttribute.TYPE_INTEGER, "nppretra"));
		attributes.add(new NumericAttribute("tslavesp", "Voyage", NumericAttribute.TYPE_INTEGER, "tslavesp"));
		attributes.add(new NumericAttribute("sladvoy", "Voyage", NumericAttribute.TYPE_INTEGER, "sladvoy"));
		attributes.add(new NumericAttribute("npprior", "Voyage", NumericAttribute.TYPE_INTEGER, "npprior"));
		attributes.add(new NationAttribute("national", "Voyage", "national"));
		attributes.add(new NumericAttribute("slinten2", "Voyage", NumericAttribute.TYPE_INTEGER, "slinten2"));
		attributes.add(new NumericAttribute("ndesert", "Voyage", NumericAttribute.TYPE_INTEGER, "ndesert"));
		attributes.add(new NumericAttribute("sladafri", "Voyage", NumericAttribute.TYPE_INTEGER, "sladafri"));
		
		attributes.add(new StringAttribute("shipname_index", "Voyage", "shipname_index"));
		attributes.add(new StringAttribute("owners_index", "Voyage", "owners_index"));
		attributes.add(new StringAttribute("captains_index", "Voyage", "captains_index"));
		attributes.add(new StringAttribute("sources_index", "Voyage", "sources_index"));
	
		attributes.add(new NumericAttribute("placcons_port", "Voyage", NumericAttribute.TYPE_LONG, "placcons_port"));
		attributes.add(new NumericAttribute("placreg_port", "Voyage", NumericAttribute.TYPE_LONG, "placreg_port"));
		attributes.add(new NumericAttribute("ptdepimp_port", "Voyage", NumericAttribute.TYPE_LONG, "ptdepimp_port"));
		attributes.add(new NumericAttribute("plac1tra_port", "Voyage", NumericAttribute.TYPE_LONG, "plac1tra_port"));
		attributes.add(new NumericAttribute("plac2tra_port", "Voyage", NumericAttribute.TYPE_LONG, "plac2tra_port"));
		attributes.add(new NumericAttribute("plac3tra_port", "Voyage", NumericAttribute.TYPE_LONG, "plac3tra_port"));
		attributes.add(new NumericAttribute("mjbyptimp_port", "Voyage", NumericAttribute.TYPE_LONG, "mjbyptimp_port"));
		attributes.add(new NumericAttribute("npafttra_port", "Voyage", NumericAttribute.TYPE_LONG, "npafttra_port"));
		attributes.add(new NumericAttribute("sla1port_port", "Voyage", NumericAttribute.TYPE_LONG, "sla1port_port"));
		attributes.add(new NumericAttribute("adpsale1_port", "Voyage", NumericAttribute.TYPE_LONG, "adpsale1_port"));
		attributes.add(new NumericAttribute("adpsale2_port", "Voyage", NumericAttribute.TYPE_LONG, "adpsale2_port"));
		attributes.add(new NumericAttribute("mjslptimp_port", "Voyage", NumericAttribute.TYPE_LONG, "mjslptimp_port"));
		attributes.add(new NumericAttribute("portret_port", "Voyage", NumericAttribute.TYPE_LONG, "portret_port"));
		attributes.add(new NumericAttribute("portdep_port", "Voyage", NumericAttribute.TYPE_LONG, "portdep_port"));
		attributes.add(new NumericAttribute("embport_port", "Voyage", NumericAttribute.TYPE_LONG, "embport_port"));
		attributes.add(new NumericAttribute("arrport_port", "Voyage", NumericAttribute.TYPE_LONG, "arrport_port"));
		attributes.add(new NumericAttribute("embport2_port", "Voyage", NumericAttribute.TYPE_LONG, "embport2_port"));
		attributes.add(new NumericAttribute("arrport2_port", "Voyage", NumericAttribute.TYPE_LONG, "arrport2_port"));
		
		attributes.add(new NumericAttribute("placcons_region", "Voyage", NumericAttribute.TYPE_LONG, "placcons_region"));
		attributes.add(new NumericAttribute("placreg_region", "Voyage", NumericAttribute.TYPE_LONG, "placreg_region"));
		attributes.add(new NumericAttribute("ptdepimp_region", "Voyage", NumericAttribute.TYPE_LONG, "ptdepimp_region"));
		attributes.add(new NumericAttribute("plac1tra_region", "Voyage", NumericAttribute.TYPE_LONG, "plac1tra_region"));
		attributes.add(new NumericAttribute("plac2tra_region", "Voyage", NumericAttribute.TYPE_LONG, "plac2tra_region"));
		attributes.add(new NumericAttribute("plac3tra_region", "Voyage", NumericAttribute.TYPE_LONG, "plac3tra_region"));
		attributes.add(new NumericAttribute("mjbyptimp_region", "Voyage", NumericAttribute.TYPE_LONG, "mjbyptimp_region"));
		attributes.add(new NumericAttribute("npafttra_region", "Voyage", NumericAttribute.TYPE_LONG, "npafttra_region"));
		attributes.add(new NumericAttribute("sla1port_region", "Voyage", NumericAttribute.TYPE_LONG, "sla1port_region"));
		attributes.add(new NumericAttribute("adpsale1_region", "Voyage", NumericAttribute.TYPE_LONG, "adpsale1_region"));
		attributes.add(new NumericAttribute("adpsale2_region", "Voyage", NumericAttribute.TYPE_LONG, "adpsale2_region"));
		attributes.add(new NumericAttribute("mjslptimp_region", "Voyage", NumericAttribute.TYPE_LONG, "mjslptimp_region"));
		attributes.add(new NumericAttribute("portret_region", "Voyage", NumericAttribute.TYPE_LONG, "portret_region"));
		attributes.add(new NumericAttribute("portdep_region", "Voyage", NumericAttribute.TYPE_LONG, "portdep_region"));
		attributes.add(new NumericAttribute("embport_region", "Voyage", NumericAttribute.TYPE_LONG, "embport_region"));
		attributes.add(new NumericAttribute("arrport_region", "Voyage", NumericAttribute.TYPE_LONG, "arrport_region"));
		attributes.add(new NumericAttribute("embport2_region", "Voyage", NumericAttribute.TYPE_LONG, "embport2_region"));
		attributes.add(new NumericAttribute("arrport2_region", "Voyage", NumericAttribute.TYPE_LONG, "arrport2_region"));
		
		attributes.add(new NumericAttribute("placcons_area", "Voyage", NumericAttribute.TYPE_LONG, "placcons_area"));
		attributes.add(new NumericAttribute("placreg_area", "Voyage", NumericAttribute.TYPE_LONG, "placreg_area"));
		attributes.add(new NumericAttribute("ptdepimp_area", "Voyage", NumericAttribute.TYPE_LONG, "ptdepimp_area"));
		attributes.add(new NumericAttribute("plac1tra_area", "Voyage", NumericAttribute.TYPE_LONG, "plac1tra_area"));
		attributes.add(new NumericAttribute("plac2tra_area", "Voyage", NumericAttribute.TYPE_LONG, "plac2tra_area"));
		attributes.add(new NumericAttribute("plac3tra_area", "Voyage", NumericAttribute.TYPE_LONG, "plac3tra_area"));
		attributes.add(new NumericAttribute("mjbyptimp_area", "Voyage", NumericAttribute.TYPE_LONG, "mjbyptimp_area"));
		attributes.add(new NumericAttribute("npafttra_area", "Voyage", NumericAttribute.TYPE_LONG, "npafttra_area"));
		attributes.add(new NumericAttribute("sla1port_area", "Voyage", NumericAttribute.TYPE_LONG, "sla1port_area"));
		attributes.add(new NumericAttribute("adpsale1_area", "Voyage", NumericAttribute.TYPE_LONG, "adpsale1_area"));
		attributes.add(new NumericAttribute("adpsale2_area", "Voyage", NumericAttribute.TYPE_LONG, "adpsale2_area"));
		attributes.add(new NumericAttribute("mjslptimp_area", "Voyage", NumericAttribute.TYPE_LONG, "mjslptimp_area"));
		attributes.add(new NumericAttribute("portret_area", "Voyage", NumericAttribute.TYPE_LONG, "portret_area"));
		attributes.add(new NumericAttribute("portdep_area", "Voyage", NumericAttribute.TYPE_LONG, "portdep_area"));
		attributes.add(new NumericAttribute("embport_area", "Voyage", NumericAttribute.TYPE_LONG, "embport_area"));
		attributes.add(new NumericAttribute("arrport_area", "Voyage", NumericAttribute.TYPE_LONG, "arrport_area"));
		attributes.add(new NumericAttribute("embport2_area", "Voyage", NumericAttribute.TYPE_LONG, "embport2_area"));
		attributes.add(new NumericAttribute("arrport2_area", "Voyage", NumericAttribute.TYPE_LONG, "arrport2_area"));	
		attributes.add(new NumericAttribute("xmimpflag", "Voyage", NumericAttribute.TYPE_INTEGER, "xmimpflag"));
		attributes.add(new NumericAttribute("year5", "Voyage", NumericAttribute.TYPE_INTEGER, "year5"));
		attributes.add(new NumericAttribute("year10", "Voyage", NumericAttribute.TYPE_INTEGER, "year10"));
		attributes.add(new NumericAttribute("year25", "Voyage", NumericAttribute.TYPE_INTEGER, "year25"));
		attributes.add(new NumericAttribute("year100", "Voyage", NumericAttribute.TYPE_INTEGER, "year100"));
		attributes.add(new PortAttribute("majselpt", "Voyage", "majselpt"));
		attributes.add(new RegionAttribute("majselrg", "Voyage", "majselrg"));
		attributes.add(new RegionAttribute("mjselimp1", "Voyage", "mjselimp1"));
		attributes.add(new RegionAttribute("regarr", "Voyage", "regarr"));
		attributes.add(new RegionAttribute("regarr2", "Voyage", "regarr2"));
		attributes.add(new RegionAttribute("deptreg", "Voyage", "deptreg"));
		attributes.add(new RegionAttribute("deptreg1", "Voyage", "deptreg1"));
		attributes.add(new RegionAttribute("embreg", "Voyage", "embreg"));
		attributes.add(new RegionAttribute("embreg2", "Voyage", "embreg2"));
		attributes.add(new PortAttribute("majbuypt", "Voyage", "majbuypt"));
		attributes.add(new RegionAttribute("majbuyreg", "Voyage", "majbuyreg"));
		
		
		
		
		
		for (Iterator iterator = attributes.iterator(); iterator.hasNext();)
		{
			Attribute attr = (Attribute) iterator.next();
			attributesLookup.put(attr.getName(), attr);
		}
		
	}

	private int revision;
	private boolean suggestion;
	private boolean approved;
	private Calendar cal = Calendar.getInstance();
	
	/**
	 * Gets all attributes of voyage.
	 * 
	 * @return
	 */
	public static ImportableAttribute[] getAttributes() {
		return (ImportableAttribute[]) attributes
				.toArray(new ImportableAttribute[] {});
	}

	/**
	 * Gets attribute with given name.
	 * 
	 * @param name
	 * @return attribute, null if there is no attribute with given name
	 */
	public static Attribute getAttribute(String name)
	{
		Attribute attr = (Attribute) attributesLookup.get(name);
		if (name == null)
		{
			throw new RuntimeException("Attribute " + name + " is not defined in Voyage");
		}
		else
		{
			return attr;
		}
	}

	/**
	 * Gets names of all attributes in voyage.
	 * 
	 * @return
	 */
	public static String[] getAllAttrNames()
	{
		String[] attrsName = new String[attributes.size()];
		for (int i = 0; i < attrsName.length; i++)
		{
			attrsName[i] = ((Attribute) attributes.get(i)).getName();
		}
		return attrsName;
	}

	/**
	 * Creates new Voyage. Object will have new ID.
	 */
	public Voyage() {
	}

	public static int getCurrentRevision()
	{
		return 1;
	}

	public static Voyage loadCurrentRevision(Session sess, Integer voyageId)
	{
		TastDbConditions c = new TastDbConditions();
		c.addCondition(Voyage.getAttribute("voyageid"), voyageId, TastDbConditions.OP_EQUALS);
		c.addCondition(Voyage.getAttribute("suggestion"), new Boolean(false), TastDbConditions.OP_EQUALS);
		c.addCondition(Voyage.getAttribute("revision"), new Integer(Voyage.getCurrentRevision()), TastDbConditions.OP_EQUALS);
		TastDbQuery qValue = new TastDbQuery("Voyage", c);
		List res = qValue.executeQueryList(sess);
		if (res.size() == 0) return null;
		return (Voyage)res.get(0);
	}

	public static Voyage loadCurrentRevision(Session sess, int voyageId)
	{
		return loadCurrentRevision(sess, new Integer(voyageId));
	}
	
	public static Voyage loadFutureRevision(Session sess, int voyageId) {
		return loadFutureRevision(sess, new Integer(voyageId));
	}
	
	public static Voyage loadFutureRevision(Session sess, Integer voyageId) {
		
		TastDbConditions c = new TastDbConditions();
		c.addCondition(Voyage.getAttribute("voyageid"), voyageId, TastDbConditions.OP_EQUALS);
		c.addCondition(Voyage.getAttribute("suggestion"), new Boolean(false), TastDbConditions.OP_EQUALS);
		c.addCondition(Voyage.getAttribute("revision"), new Integer(-1), TastDbConditions.OP_EQUALS);
		TastDbQuery qValue = new TastDbQuery("Voyage", c);
		List res = qValue.executeQueryList(sess);
		if (res.size() == 0) return null;
		return (Voyage)res.get(0);
	}
	
	public static Voyage loadById(Session session, long iid) {
		return loadById(session, new Long(iid));
	}

	public static Voyage loadById(Session session, Long iid) {
		TastDbConditions c = new TastDbConditions();
		c.addCondition(Voyage.getAttribute("iid"), iid, TastDbConditions.OP_EQUALS);
		TastDbQuery qValue = new TastDbQuery("Voyage", c);
		List res = qValue.executeQueryList(session);
		if (res.size() == 0) return null;
		return (Voyage)res.get(0);
	}
	
	public static Voyage loadByVoyageId(Session session, int voyageId, int revisionId)
	{
		TastDbConditions c = new TastDbConditions();
		c.addCondition(Voyage.getAttribute("voyageid"), new Integer(voyageId), TastDbConditions.OP_EQUALS);
		c.addCondition(Voyage.getAttribute("suggestion"), new Boolean(false), TastDbConditions.OP_EQUALS);
		c.addCondition(Voyage.getAttribute("revision"), new Integer(revisionId), TastDbConditions.OP_EQUALS);
		TastDbQuery qValue = new TastDbQuery("Voyage", c);
		List res = qValue.executeQueryList(session);
		if (res.size() == 0) return null;
		return (Voyage)res.get(0);
	}

	public static Voyage loadByVoyageId(Session session, int voyageId)
	{
		return loadByVoyageId(session, voyageId, getCurrentRevision());
	}

	public static List loadByVoyageIds(Session session, Collection voyageIds, int revisionId)
	{
		if (voyageIds == null || voyageIds.size() == 0)
		{
			return null;
		}
		else
		{
			return session.createCriteria(Voyage.class).
				add(Restrictions.in("voyageid", voyageIds)).
				add(Restrictions.eq("revision", new Integer(revisionId))).
				add(Restrictions.eq("suggestion", new Boolean(false))).
				addOrder(Order.asc("voyageid")).list();
		}
	}
	
	public static List loadByVoyageIds(Session session, Collection voyageIds)
	{
		return loadByVoyageIds(session, voyageIds, getCurrentRevision());
	}	

	/**
	 * Saves voyage to DB.
	 * 
	 */
	public void save()
	{
		Session session = HibernateConn.getSession();
		Transaction transaction = session.beginTransaction();
		session.save(this);
		transaction.commit();
	}

	/**
	 * Gets deep copy of object.
	 */
	public Object clone() {
		// Copy voyage itself
		Voyage newVoyage = new Voyage();

		newVoyage.values = this.values;

		// Return copy object
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

	public void saveOrUpdate() {
		Session session = HibernateConn.getSession();
		Transaction transaction = session.beginTransaction();
		session.saveOrUpdate(this);
		transaction.commit();
	}

	public void saveOrUpdate(Session sess) {
		sess.saveOrUpdate(this);
	}

	public void setVoyageid(Integer voyageId) {
		this.values.put("voyageid", voyageId);
	}

	public void setSlas32(Integer slas32) {
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

	public void setTonnage(Integer tonnage) {
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
	
	public void setYearaf(Integer yearaf) {
		this.values.put("yearaf", yearaf);
	}
	
	public void setYeardep(Integer yeardep) {
		this.values.put("yeardep", yeardep);
	}
	
	public void setYear5(Integer year5) {
		this.values.put("year5", year5);
	}
	
	public void setYear10(Integer year10) {
		this.values.put("year10", year10);
	}
	
	public void setYear25(Integer year25) {
		this.values.put("year25", year25);
	}
	
	public void setYear100(Integer year100) {
		this.values.put("year100", year100);
	}

	public void setTonmod(Float tonmod) {
		this.values.put("tonmod", tonmod);
	}

	public void setVymrtimp(Integer vymrtimp) {
		this.values.put("vymrtimp", vymrtimp);
	}

	public void setVymrtrat(Float vymrtrat) {
		this.values.put("vymrtrat", vymrtrat);
	}

	public void setSlaximp(Integer slaximp) {
		this.values.put("slaximp", slaximp);
	}

	public void setSlamimp(Integer slamimp) {
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
	
	public void setConstreg(Region obj) {
		this.values.put("constreg", obj);
	}
	
	public void setRegisreg(Region obj) {
		this.values.put("regisreg", obj);
	}
	
	public void setDeptreg(Region obj) {
		this.values.put("deptreg", obj);
	}
	
	public void setEmbreg(Region obj) {
		this.values.put("embreg", obj);
	}
	
	public void setEmbreg2(Region obj) {
		this.values.put("embreg2", obj);
	}
	
	public void setMajbuyreg(Region obj) {
		this.values.put("majbuyreg", obj);
	}
	
	public void setRegarr(Region obj) {
		this.values.put("regarr", obj);
	}
	
	public void setRegarr2(Region obj) {
		this.values.put("regarr2", obj);
	}
	
	public void setMajselrg(Region obj) {
		this.values.put("majselrg", obj);
	}
	
	public void setDeptreg1(Region obj) {
		this.values.put("deptreg1", obj);
	}
	
	public void setDeptregimp1(Region obj) {
		this.values.put("deptregimp1", obj);
	}
	
	public void setMajbyimp1(Region obj) {
		this.values.put("majbyimp1", obj);
	}
	
	public void setMjselimp1(Region obj) {
		this.values.put("mjselimp1", obj);
	}
	
	public void setRetrnreg1(Region obj) {
		this.values.put("retrnreg1", obj);
	}

	public void setE_majbyimp(EstimatesExportRegion obj) {
		this.values.put("e_majbyimp", obj);
	}

	public void setE_mjselimp(EstimatesImportRegion obj) {
		this.values.put("e_mjselimp", obj);
	}

	public void setE_natinimp(EstimatesNation obj) {
		this.values.put("e_natinimp", obj);
	}

	public void setPortdep(Port obj) {
		this.values.put("portdep", obj);
	}

	public Port getPortdep() {
		return (Port) this.values.get("portdep");
	}

	public Port getSla1port() {
		return (Port) this.values.get("sla1port");
	}

	public Integer getYrcons() {
		return (Integer) this.values.get("yrcons");
	}

	public Date getDatebuy() {
		return (Date) this.values.get("datebuy");
	}

	public Date getDatedepam() {
		return (Date) this.values.get("datedepam");
	}

	public Date getDateend() {
		return (Date) this.values.get("dateend");
	}

	public Date getDateland1() {
		return (Date) this.values.get("dateland1");
	}

	public Date getDateland2() {
		return (Date) this.values.get("dateland2");
	}

	public Date getDateland3() {
		return (Date) this.values.get("dateland3");
	}

	public Date getDateleftafr() {
		return (Date) this.values.get("dateleftafr");
	}

	public Region getDeptregimp() {
		return (Region) this.values.get("deptregimp");
	}
	
	
	public Region getConstreg () {
		return (Region) this.values.get("constreg");
	}
	
	public Region getRegisreg() {
		return (Region) this.values.get("regisreg");
	}
	
	public Region getDeptreg() {
		return (Region) this.values.get("deptreg");
	}
	
	public Region getEmbreg() {
		return (Region) this.values.get("embreg");
	}
	
	public Region getEmbreg2() {
		return (Region) this.values.get("embreg2");
	}
	
	public Region getMajbuyreg() {
		return (Region) this.values.get("majbuyreg");
	}
	
	public Region getRegarr() {
		return (Region) this.values.get("regarr");
	}
	
	public Region getRegarr2() {
		return (Region) this.values.get("regarr2");
	}
	
	public Region getMajselrg() {
		return (Region) this.values.get("majselrg");
	}
	
	public Region getDeptreg1() {
		return (Region) this.values.get("deptreg1");
	}
	
	public Region getDeptregimp1() {
		return (Region) this.values.get("deptregimp1");
	}
	
	public Region getMajbyimp1() {
		return (Region) this.values.get("majbyimp1");
	}
	
	public Region getMjselimp1() {
		return (Region) this.values.get("mjselimp1");
	}
	
	public Region getRetrnreg1() {
		return (Region) this.values.get("retrnreg1");
	}

	public EstimatesExportRegion getE_majbyimp() {
		return (EstimatesExportRegion) this.values.get("e_majbyimp");
	}

	public EstimatesImportRegion getE_mjselimp() {
		return (EstimatesImportRegion) this.values.get("e_mjselimp");
	}

	public EstimatesNation getE_natinimp() {
		return (EstimatesNation) this.values.get("e_natinimp");
	}

	public Integer getVoyageid() {
		return (Integer) this.values.get("voyageid");
	}

	public String getShipname() {
		return (String) this.values.get("shipname");
	}

	public String getCaptaina() {
		return (String) this.values.get("captaina");
	}

	public String getCaptainb() {
		return (String) this.values.get("captainb");
	}

	public String getCaptainc() {
		return (String) this.values.get("captainc");
	}

	public Date getDatedep() {
		return (Date) this.values.get("datedep");
	}

	public Integer getTslavesd() {
		return (Integer) this.values.get("tslavesd");
	}

	public Integer getSlaarriv() {
		return (Integer) this.values.get("slaarriv");
	}

	public Integer getSlas32() {
		return (Integer) this.values.get("slas32");
	}

	public Integer getSlas36() {
		return (Integer) this.values.get("slas36");
	}

	public Integer getSlas39() {
		return (Integer) this.values.get("slas39");
	}

	public Fate getFate() {
		return (Fate) this.values.get("fate");
	}

	public String getSourcea() {
		return (String) this.values.get("sourcea");
	}

	public String getSourceb() {
		return (String) this.values.get("sourceb");
	}

	public String getSourcec() {
		return (String) this.values.get("sourcec");
	}

	public String getSourced() {
		return (String) this.values.get("sourced");
	}

	public String getSourcee() {
		return (String) this.values.get("sourcee");
	}

	public String getSourcef() {
		return (String) this.values.get("sourcef");
	}

	public String getSourceg() {
		return (String) this.values.get("sourceg");
	}

	public String getSourceh() {
		return (String) this.values.get("sourceh");
	}

	public String getSourcei() {
		return (String) this.values.get("sourcei");
	}

	public String getSourcej() {
		return (String) this.values.get("sourcej");
	}

	public String getSourcek() {
		return (String) this.values.get("sourcek");
	}

	public String getSourcel() {
		return (String) this.values.get("sourcel");
	}

	public String getSourcem() {
		return (String) this.values.get("sourcem");
	}

	public String getSourcen() {
		return (String) this.values.get("sourcen");
	}

	public String getSourceo() {
		return (String) this.values.get("sourceo");
	}

	public String getSourcep() {
		return (String) this.values.get("sourcep");
	}

	public String getSourceq() {
		return (String) this.values.get("sourceq");
	}

	public String getSourcer() {
		return (String) this.values.get("sourcer");
	}

	public Integer getSlintend() {
		return (Integer) this.values.get("slintend");
	}

	public Integer getTonnage() {
		return (Integer) this.values.get("tonnage");
	}

	public Integer getCrewdied() {
		return (Integer) this.values.get("crewdied");
	}

	public Integer getNcar13() {
		return (Integer) this.values.get("ncar13");
	}

	public Integer getNcar15() {
		return (Integer) this.values.get("ncar15");
	}

	public Integer getNcar17() {
		return (Integer) this.values.get("ncar17");
	}

	public Integer getGuns() {
		return (Integer) this.values.get("guns");
	}

	public Integer getCrew1() {
		return (Integer) this.values.get("crew1");
	}

	public Integer getYrreg() {
		return (Integer) this.values.get("yrreg");
	}

	public Integer getCrew3() {
		return (Integer) this.values.get("crew3");
	}

	public Resistance getResistance() {
		return (Resistance) this.values.get("resistance");
	}

	public Port getPtdepimp() {
		return (Port) this.values.get("ptdepimp");
	}

	public String getOwnera() {
		return (String) this.values.get("ownera");
	}

	public String getOwnerb() {
		return (String) this.values.get("ownerb");
	}

	public String getOwnerc() {
		return (String) this.values.get("ownerc");
	}

	public String getOwnerd() {
		return (String) this.values.get("ownerd");
	}

	public String getOwnere() {
		return (String) this.values.get("ownere");
	}

	public String getOwnerf() {
		return (String) this.values.get("ownerf");
	}

	public String getOwnerg() {
		return (String) this.values.get("ownerg");
	}

	public String getOwnerh() {
		return (String) this.values.get("ownerh");
	}

	public String getOwneri() {
		return (String) this.values.get("owneri");
	}

	public String getOwnerj() {
		return (String) this.values.get("ownerj");
	}

	public String getOwnerk() {
		return (String) this.values.get("ownerk");
	}

	public String getOwnerl() {
		return (String) this.values.get("ownerl");
	}

	public String getOwnerm() {
		return (String) this.values.get("ownerm");
	}

	public String getOwnern() {
		return (String) this.values.get("ownern");
	}

	public String getOwnero() {
		return (String) this.values.get("ownero");
	}

	public String getOwnerp() {
		return (String) this.values.get("ownerp");
	}

	public Integer getYearam() {
		return (Integer) this.values.get("yearam");
	}
	
	public Integer getYearaf() {
		return (Integer) this.values.get("yearaf");
	}
	
	public Integer getYeardep() {
		return (Integer) this.values.get("yeardep");
	}
	
	public Integer getYear5() {
		return (Integer) this.values.get("year5");
	}
	
	public Integer getYear10() {
		return (Integer) this.values.get("year10");
	}
	
	public Integer getYear25() {
		return (Integer) this.values.get("year25");
	}
	
	public Integer getYear100() {
		return (Integer) this.values.get("year100");
	}

	public Float getTonmod() {
		return (Float) this.values.get("tonmod");
	}

	public Integer getVymrtimp() {
		return (Integer) this.values.get("vymrtimp");
	}

	public Float getVymrtrat() {
		return (Float) this.values.get("vymrtrat");
	}

	public Integer getSlaximp() {
		return (Integer) this.values.get("slaximp");
	}

	public Integer getSlamimp() {
		return (Integer) this.values.get("slamimp");
	}

	public Integer getVoy2imp() {
		return (Integer) this.values.get("voy2imp");
	}

	public Integer getVoy1imp() {
		return (Integer) this.values.get("voy1imp");
	}

	public Float getMalrat7() {
		return (Float) this.values.get("malrat7");
	}

	public Float getChilrat7() {
		return (Float) this.values.get("chilrat7");
	}

	public Float getWomrat7() {
		return (Float) this.values.get("womrat7");
	}

	public Float getMenrat7() {
		return (Float) this.values.get("menrat7");
	}

	public Float getGirlrat7() {
		return (Float) this.values.get("girlrat7");
	}

	public Float getBoyrat7() {
		return (Float) this.values.get("boyrat7");
	}

	public Float getJamcaspr() {
		return (Float) this.values.get("jamcaspr");
	}

	public Port getPlac1tra() {
		return (Port) this.values.get("plac1tra");
	}

	public Port getPlac2tra() {
		return (Port) this.values.get("plac2tra");
	}

	public Port getPlac3tra() {
		return (Port) this.values.get("plac3tra");
	}

	public Port getNpafttra() {
		return (Port) this.values.get("npafttra");
	}

	public Port getAdpsale1() {
		return (Port) this.values.get("adpsale1");
	}

	public Port getAdpsale2() {
		return (Port) this.values.get("adpsale2");
	}

	public Port getPortret() {
		return (Port) this.values.get("portret");
	}

	public VesselRig getRig() {
		return (VesselRig) this.values.get("rig");
	}

	public Port getPlaccons() {
		return (Port) this.values.get("placcons");
	}

	public Port getPlacreg() {
		return (Port) this.values.get("placreg");
	}

	public Nation getNatinimp() {
		return (Nation) this.values.get("natinimp");
	}

	public Region getRetrnreg() {
		return (Region) this.values.get("retrnreg");
	}

	public Region getRegem1() {
		return (Region) this.values.get("regem1");
	}

	public Region getRegem2() {
		return (Region) this.values.get("regem2");
	}

	public Region getRegem3() {
		return (Region) this.values.get("regem3");
	}

	public Region getMajbyimp() {
		return (Region) this.values.get("majbyimp");
	}

	public Region getRegdis1() {
		return (Region) this.values.get("regdis1");
	}

	public Region getRegdis2() {
		return (Region) this.values.get("regdis2");
	}

	public Region getRegdis3() {
		return (Region) this.values.get("regdis3");
	}

	public FateSlaves getFate2() {
		return (FateSlaves) this.values.get("fate2");
	}

	public FateVessel getFate3() {
		return (FateVessel) this.values.get("fate3");
	}

	public FateOwner getFate4() {
		return (FateOwner) this.values.get("fate4");
	}

	public Region getMjselimp() {
		return (Region) this.values.get("mjselimp");
	}

	public Port getMjbyptimp() {
		return (Port) this.values.get("mjbyptimp");
	}

	public Port getEmbport() {
		return (Port) this.values.get("embport");
	}

	public Port getArrport() {
		return (Port) this.values.get("arrport");
	}

	public Port getEmbport2() {
		return (Port) this.values.get("embport2");
	}

	public Port getArrport2() {
		return (Port) this.values.get("arrport2");
	}

	public Integer getTontype() {
		return (Integer) this.values.get("tontype");
	}

	public Integer getSladamer() {
		return (Integer) this.values.get("sladamer");
	}

	public Integer getSaild1() {
		return (Integer) this.values.get("saild1");
	}

	public Integer getSaild2() {
		return (Integer) this.values.get("saild2");
	}

	public Integer getSaild3() {
		return (Integer) this.values.get("saild3");
	}

	public Integer getSaild4() {
		return (Integer) this.values.get("saild4");
	}

	public Integer getSaild5() {
		return (Integer) this.values.get("saild5");
	}

	public Integer getVoyage() {
		return (Integer) this.values.get("voyage");
	}

	public Integer getChild2() {
		return (Integer) this.values.get("child2");
	}

	public Integer getChild3() {
		return (Integer) this.values.get("child3");
	}

	public Integer getCrew4() {
		return (Integer) this.values.get("crew4");
	}

	public Integer getCrew5() {
		return (Integer) this.values.get("crew5");
	}

	public Integer getAdult1() {
		return (Integer) this.values.get("adult1");
	}
	
	public Integer getAdult6() {
		return (Integer) this.values.get("adult6");
	}

	public Integer getChild1() {
		return (Integer) this.values.get("child1");
	}

	public Integer getFemale1() {
		return (Integer) this.values.get("female1");
	}

	public Integer getMale1() {
		return (Integer) this.values.get("male1");
	}

	public Integer getMen1() {
		return (Integer) this.values.get("men1");
	}

	public Integer getWomen1() {
		return (Integer) this.values.get("women1");
	}

	public Integer getBoy1() {
		return (Integer) this.values.get("boy1");
	}

	public Integer getGirl1() {
		return (Integer) this.values.get("girl1");
	}

	public Integer getAdult2() {
		return (Integer) this.values.get("adult2");
	}

	public Integer getFemale2() {
		return (Integer) this.values.get("female2");
	}

	public Integer getMale2() {
		return (Integer) this.values.get("male2");
	}

	public Integer getMen2() {
		return (Integer) this.values.get("men2");
	}

	public Integer getWomen2() {
		return (Integer) this.values.get("women2");
	}

	public Integer getBoy2() {
		return (Integer) this.values.get("boy2");
	}

	public Integer getGirl2() {
		return (Integer) this.values.get("girl2");
	}

	public Integer getAdult3() {
		return (Integer) this.values.get("adult3");
	}

	public Integer getFemale3() {
		return (Integer) this.values.get("female3");
	}

	public Integer getMale3() {
		return (Integer) this.values.get("male3");
	}

	public Integer getMen3() {
		return (Integer) this.values.get("men3");
	}

	public Integer getWomen3() {
		return (Integer) this.values.get("women3");
	}

	public Integer getBoy3() {
		return (Integer) this.values.get("boy3");
	}

	public Integer getGirl3() {
		return (Integer) this.values.get("girl3");
	}

	public Integer getChild4() {
		return (Integer) this.values.get("child4");
	}

	public Integer getFemale4() {
		return (Integer) this.values.get("female4");
	}

	public Integer getMale4() {
		return (Integer) this.values.get("male4");
	}

	public Integer getMen4() {
		return (Integer) this.values.get("men4");
	}

	public Integer getWomen4() {
		return (Integer) this.values.get("women4");
	}

	public Integer getBoy4() {
		return (Integer) this.values.get("boy4");
	}

	public Integer getGirl4() {
		return (Integer) this.values.get("girl4");
	}

	public Integer getChild6() {
		return (Integer) this.values.get("child6");
	}

	public Integer getFemale6() {
		return (Integer) this.values.get("female6");
	}

	public Integer getMale6() {
		return (Integer) this.values.get("male6");
	}

	public Integer getMen6() {
		return (Integer) this.values.get("men6");
	}

	public Integer getWomen6() {
		return (Integer) this.values.get("women6");
	}

	public Integer getBoy6() {
		return (Integer) this.values.get("boy6");
	}

	public Integer getGirl6() {
		return (Integer) this.values.get("girl6");
	}

	public Integer getCrew2() {
		return (Integer) this.values.get("crew2");
	}

	public Integer getInfantm3() {
		return (Integer) this.values.get("infantm3");
	}

	public Integer getInfantf3() {
		return (Integer) this.values.get("infantf3");
	}

	public Integer getSladied1() {
		return (Integer) this.values.get("sladied1");
	}

	public Integer getSladied2() {
		return (Integer) this.values.get("sladied2");
	}

	public Integer getSladied3() {
		return (Integer) this.values.get("sladied3");
	}

	public Integer getSladied4() {
		return (Integer) this.values.get("sladied4");
	}

	public Integer getSladied5() {
		return (Integer) this.values.get("sladied5");
	}

	public Integer getSladied6() {
		return (Integer) this.values.get("sladied6");
	}

	public Integer getInsurrec() {
		return (Integer) this.values.get("insurrec");
	}

	public Boolean getEvgreen() {
		return (Boolean) this.values.get("evgreen");
	}

	public Integer getFemale5() {
		return (Integer) this.values.get("female5");
	}

	public Integer getMale5() {
		return (Integer) this.values.get("male5");
	}

	public Integer getChild5() {
		return (Integer) this.values.get("child5");
	}

	public Integer getMen5() {
		return (Integer) this.values.get("men5");
	}

	public Integer getWomen5() {
		return (Integer) this.values.get("women5");
	}

	public Integer getBoy5() {
		return (Integer) this.values.get("boy5");
	}

	public Integer getGirl5() {
		return (Integer) this.values.get("girl5");
	}

	public Integer getInfant3() {
		return (Integer) this.values.get("infant3");
	}

	public Integer getInfant1() {
		return (Integer) this.values.get("infant1");
	}

	public Integer getAdult5() {
		return (Integer) this.values.get("adult5");
	}

	public Integer getAdult4() {
		return (Integer) this.values.get("adult4");
	}

	public Integer getInfant4() {
		return (Integer) this.values.get("infant4");
	}

	public Integer getCrew() {
		return (Integer) this.values.get("crew");
	}

	public void setEmbport(Port val) {
		this.values.put("embport", val);
	}

	public void setArrport(Port val) {
		this.values.put("arrport", val);
	}

	public void setEmbport2(Port val) {
		this.values.put("embport2", val);
	}

	public void setArrport2(Port val) {
		this.values.put("arrport2", val);
	}

	public void setTontype(Integer val) {
		this.values.put("tontype", val);
	}

	public void setSladamer(Integer val) {
		this.values.put("sladamer", val);
	}

	public void setSaild1(Integer val) {
		this.values.put("saild1", val);
	}

	public void setSaild2(Integer val) {
		this.values.put("saild2", val);
	}

	public void setSaild3(Integer val) {
		this.values.put("saild3", val);
	}

	public void setSaild4(Integer val) {
		this.values.put("saild4", val);
	}

	public void setSaild5(Integer val) {
		this.values.put("saild5", val);
	}

	public void setVoyage(Integer val) {
		this.values.put("voyage", val);
	}

	public void setChild2(Integer val) {
		this.values.put("child2", val);
	}

	public void setChild3(Integer val) {
		this.values.put("child3", val);
	}

	public void setCrew4(Integer val) {
		this.values.put("crew4", val);
	}

	public void setCrew5(Integer val) {
		this.values.put("crew5", val);
	}

	public void setAdult1(Integer val) {
		this.values.put("adult1", val);
	}

	public void setChild1(Integer val) {
		this.values.put("child1", val);
	}

	public void setFemale1(Integer val) {
		this.values.put("female1", val);
	}

	public void setMale1(Integer val) {
		this.values.put("male1", val);
	}

	public void setMen1(Integer val) {
		this.values.put("men1", val);
	}

	public void setWomen1(Integer val) {
		this.values.put("women1", val);
	}

	public void setBoy1(Integer val) {
		this.values.put("boy1", val);
	}

	public void setGirl1(Integer val) {
		this.values.put("girl1", val);
	}

	public void setAdult2(Integer val) {
		this.values.put("adult2", val);
	}
	
	public void setAdult6(Integer val) {
		this.values.put("adult6", val);
	}

	public void setFemale2(Integer val) {
		this.values.put("female2", val);
	}

	public void setMale2(Integer val) {
		this.values.put("male2", val);
	}

	public void setMen2(Integer val) {
		this.values.put("men2", val);
	}

	public void setWomen2(Integer val) {
		this.values.put("women2", val);
	}

	public void setBoy2(Integer val) {
		this.values.put("boy2", val);
	}

	public void setGirl2(Integer val) {
		this.values.put("girl2", val);
	}

	public void setAdult3(Integer val) {
		this.values.put("adult3", val);
	}

	public void setFemale3(Integer val) {
		this.values.put("female3", val);
	}

	public void setMale3(Integer val) {
		this.values.put("male3", val);
	}

	public void setMen3(Integer val) {
		this.values.put("men3", val);
	}

	public void setWomen3(Integer val) {
		this.values.put("women3", val);
	}

	public void setBoy3(Integer val) {
		this.values.put("boy3", val);
	}

	public void setGirl3(Integer val) {
		this.values.put("girl3", val);
	}

	public void setChild4(Integer val) {
		this.values.put("child4", val);
	}
	
	public void setChild5(Integer val) {
		this.values.put("child5", val);
	}

	public void setFemale4(Integer val) {
		this.values.put("female4", val);
	}

	public void setMale4(Integer val) {
		this.values.put("male4", val);
	}

	public void setMen4(Integer val) {
		this.values.put("men4", val);
	}

	public void setWomen4(Integer val) {
		this.values.put("women4", val);
	}

	public void setBoy4(Integer val) {
		this.values.put("boy4", val);
	}

	public void setGirl4(Integer val) {
		this.values.put("girl4", val);
	}

	public void setChild6(Integer val) {
		this.values.put("child6", val);
	}

	public void setFemale6(Integer val) {
		this.values.put("female6", val);
	}

	public void setMale6(Integer val) {
		this.values.put("male6", val);
	}

	public void setMen6(Integer val) {
		this.values.put("men6", val);
	}

	public void setWomen6(Integer val) {
		this.values.put("women6", val);
	}

	public void setBoy6(Integer val) {
		this.values.put("boy6", val);
	}

	public void setGirl6(Integer val) {
		this.values.put("girl6", val);
	}

	public void setCrew2(Integer val) {
		this.values.put("crew2", val);
	}

	public void setinfantm3(Integer val) {
		this.values.put("infantm3", val);
	}

	public void setInfantf3(Integer val) {
		this.values.put("infantf3", val);
	}

	public void setSladied1(Integer val) {
		this.values.put("sladied1", val);
	}

	public void setSladied2(Integer val) {
		this.values.put("sladied2", val);
	}

	public void setSladied3(Integer val) {
		this.values.put("sladied3", val);
	}

	public void setSladied4(Integer val) {
		this.values.put("sladied4", val);
	}

	public void setSladied5(Integer val) {
		this.values.put("sladied5", val);
	}

	public void setSladied6(Integer val) {
		this.values.put("sladied6", val);
	}

	public void setInsurrec(Integer val) {
		this.values.put("insurrec", val);
	}

	public void setEvgreen(Boolean val) {
		this.values.put("evgreen", val);
	}

	public void setFemale5(Integer val) {
		this.values.put("female5", val);
	}

	public void setMale5(Integer val) {
		this.values.put("male5", val);
	}

	public void setSChild5(Integer val) {
		this.values.put("child5", val);
	}

	public void setMen5(Integer val) {
		this.values.put("men5", val);
	}

	public void setWomen5(Integer val) {
		this.values.put("women5", val);
	}

	public void setBoy5(Integer val) {
		this.values.put("boy5", val);
	}

	public void setGirl5(Integer val) {
		this.values.put("girl5", val);
	}

	public void setInfant3(Integer val) {
		this.values.put("infant3", val);
	}

	public void setInfant1(Integer val) {
		this.values.put("infant1", val);
	}

	public void setAdult5(Integer val) {
		this.values.put("adult5", val);
	}

	public void setAdult4(Integer val) {
		this.values.put("adult4", val);
	}

	public void setInfant4(Integer val) {
		this.values.put("infant4", val);
	}

	public void setCrew(Integer val) {
		this.values.put("crew", val);
	}
	
	public boolean isApproved() {
		return approved;
	}

	public void setApproved(boolean approved) {
		this.approved = approved;
	}

	public int getRevision() {
		return revision;
	}

	public void setRevision(int revision) {
		this.revision = revision;
	}

	public boolean isSuggestion() {
		return suggestion;
	}

	public void setSuggestion(boolean suggestion) {
		this.suggestion = suggestion;
	}	
	
	public Integer getNppretra() {
		return (Integer)this.values.get("nppretra");
	}
	
	public void setNppretra(Integer val) {
		this.values.put("nppretra", val);
	}
	
	public Integer getTslavesp() {
		return (Integer)this.values.get("tslavesp");
	}
	
	public void setTslavesp(Integer val) {
		this.values.put("tslavesp", val);
	}
	
	public Integer getSladvoy() {
		return (Integer)this.values.get("sladvoy");
	}
	
	public void setSladvoy(Integer val) {
		this.values.put("sladvoy", val);
	}
	
	public Integer getNpprior() {
		return (Integer)this.values.get("npprior");
	}
	
	public void setNpprior(Integer val) {
		this.values.put("npprior", val);
	}
	
	public Nation getNational() {
		return (Nation)this.values.get("national");
	}
	
	public void setNational(Nation val) {
		this.values.put("national", val);
	}
	
	public Integer getSlinten2() {
		return (Integer)this.values.get("slinten2");
	}
	
	public void setSlinten2(Integer val) {
		this.values.put("slinten2", val);
	}
	
	public void setSladafri(Integer val) {
		this.values.put("sladafri", val);
	}
	
	public Integer getSladafri() {
		return (Integer)this.values.get("sladafri");
	}
	
	public void setNdesert(Integer val) {
		this.values.put("ndesert", val);
	}
	
	public Integer getNdesert() {
		return (Integer)this.values.get("ndesert");
	}
	
	public void setMajbuypt(Port val) {
		this.values.put("majbuypt", val);
	}
	
	public Port getMajbuypt() {
		return (Port)this.values.get("majbuypt");
	}
	
	public void setMajselpt(Port val) {
		this.values.put("majselpt", val);
	}
	
	public Port getMajselpt() {
		return (Port)this.values.get("majselpt");
	}
	
	public void setTslmtimp(Integer tslmtimp) {
		this.values.put("tslmtimp", tslmtimp);
	}
	
	public Integer getTslmtimp() {
		return (Integer) this.values.get("tslmtimp");
	}	
	
	//Some of the new getters and setters
	public Integer getAdlt1imp() {
		return (Integer) this.values.get("adlt1imp");
	}
	
	public Double getChil1imp() {
		return (Double) this.values.get("chil1imp");
	}
	
	public Double getMale1imp() {
		return (Double) this.values.get("male1imp");
	}
	
	public Double getFeml1imp() {
		return (Double) this.values.get("feml1imp");
	}
	
	public Integer getAdlt2imp() {
		return (Integer) this.values.get("adlt2imp");
	}
	
	public Integer getChil2imp() {
		return (Integer) this.values.get("chil2imp");
	}
	
	public Integer getMale2imp() {
		return (Integer) this.values.get("male2imp");
	}
	
	public Integer getFeml2imp() {
		return (Integer) this.values.get("feml2imp");
	}
	
	public Double getSlavema1() {
		return (Double) this.values.get("slavema1");
	}
	
	public Double getSlavemx1() {
		return (Double) this.values.get("slavemx1");
	}
	
	public Integer getSlavmax1() {
		return (Integer) this.values.get("slavmax1");
	}
	
	public Double getChilrat1() {
		return (Double) this.values.get("chilrat1");
	}
	
	public Double getMalrat1() {
		return (Double) this.values.get("malrat1");
	}
	
	public Double getMenrat1() {
		return (Double) this.values.get("menrat1");
	}
	
	public Double getWomrat1() {
		return (Double) this.values.get("womrat1");
	}
	
	public Double getBoyrat1() {
		return (Double) this.values.get("boyrat1");
	}
	
	public Double getGirlrat1() {
		return (Double) this.values.get("girlrat1");
	}
	
	public Double getAdlt3imp() {
		return (Double) this.values.get("adlt3imp");
	}
	
	public Double getMale3imp() {
		return (Double) this.values.get("male3imp");
	}
	
	public Double getChil3imp() {
		return (Double) this.values.get("chil3imp");
	}
	
	public Double getFeml3imp() {
		return (Double) this.values.get("feml3imp");
	}
	
	public Double getSlavema3() {
		return (Double) this.values.get("slavema3");
	}
	
	public Double getSlavemx3() {
		return (Double) this.values.get("slavemx3");
	}
	
	public Integer getSlavmax3() {
		return (Integer) this.values.get("slavmax3");
	}
	
	public Double getChild7() {
		return (Double) this.values.get("child7");
	}
	
	public Double getAdult7() {
		return (Double) this.values.get("adult7");
	}
	
	public Double getGirl7() {
		return (Double) this.values.get("girl7");
	}
	
	public Double getChilrat3() {
		return (Double) this.values.get("chilrat3");
	}
	
	public Double getMalrat3() {
		return (Double) this.values.get("malrat3");
	}
	
	public Double getBoy7() {
		return (Double) this.values.get("boy7");
	}
	
	public Double getWomen7() {
		return (Double) this.values.get("women7");
	}
	
	public Double getMen7() {
		return (Double) this.values.get("men7");
	}
	
	public Integer getSlavmax7() {
		return (Integer) this.values.get("slavmax7");
	}
	
	public Double getMenrat3() {
		return (Double) this.values.get("menrat3");
	}
	
	public Double getWomrat3() {
		return (Double) this.values.get("womrat3");
	}
	
	public Double getBoyrat3() {
		return (Double) this.values.get("boyrat3");
	}
	
	public Double getGirlrat3() {
		return (Double) this.values.get("girlrat3");
	}
	
	public Double getSlavema7() {
		return (Double) this.values.get("slavema7");
	}
	
	public Double getSlavemx7() {
		return (Double) this.values.get("slavemx7");
	}
	
	public Double getMale7() {
		return (Double) this.values.get("male7");
	}
	
	public Double getFemale7() {
		return (Double) this.values.get("female7");
	}
	
	public void setAdlt1imp(Integer obj){
		this.values.put("adlt1imp", obj);
	}
	
	public void setChil1imp(Double obj){
		this.values.put("chil1imp", obj);
	}
	
	public void setMale1imp(Double obj){
		this.values.put("male1imp", obj);
	}
	
	public void setFeml1imp(Double obj){
		this.values.put("feml1imp", obj);
	}
	
	public void setAdlt2imp(Integer obj){
		this.values.put("adlt2imp", obj);
	}
	
	public void setChil2imp(Integer obj){
		this.values.put("chil2imp", obj);
	}
	
	public void setMale2imp(Integer obj){
		this.values.put("male2imp", obj);
	}
	
	public void setFeml2imp(Integer obj){
		this.values.put("feml2imp", obj);
	}
	
	public void setSlavema1(Double obj){
		this.values.put("slavema1", obj);
	}
	
	public void setSlavemx1(Double obj){
		this.values.put("slavemx1", obj);
	}
	
	public void setSlavmax1(Integer obj){
		this.values.put("slavmax1", obj);
	}
	
	public void setChilrat1(Double obj){
		this.values.put("chilrat1", obj);
	}
	
	public void setMalrat1(Double obj){
		this.values.put("malrat1", obj);
	}
	
	public void setMenrat1(Double obj){
		this.values.put("menrat1", obj);
	}
	
	public void setWomrat1(Double obj){
		this.values.put("womrat1", obj);
	}
	
	public void setBoyrat1(Double obj){
		this.values.put("boyrat1", obj);
	}
	
	public void setGirlrat1(Double obj){
		this.values.put("girlrat1", obj);
	}
	
	public void setAdlt3imp(Double obj){
		this.values.put("adlt3imp", obj);
	}
	
	public void setMale3imp(Double obj){
		this.values.put("male3imp", obj);
	}
	
	public void setChil3imp(Double obj){
		this.values.put("chil3imp", obj);
	}
	
	public void setFeml3imp(Double obj){
		this.values.put("feml3imp", obj);
	}
	
	public void setSlavema3(Double obj){
		this.values.put("slavema3", obj);
	}
	
	public void setSlavemx3(Double obj){
		this.values.put("slavemx3", obj);
	}
	
	public void setSlavmax3(Integer obj){
		this.values.put("slavmax3", obj);
	}
	
	public void setChild7(Double obj){
		this.values.put("child7", obj);
	}
	
	public void setAdult7(Double obj){
		this.values.put("adult7", obj);
	}
	
	public void setGirl7(Double obj){
		this.values.put("girl7", obj);
	}
	
	public void setChilrat3(Double obj){
		this.values.put("chilrat3", obj);
	}
	
	public void setMalrat3(Double obj){
		this.values.put("malrat3", obj);
	}
	
	public void setBoy7(Double obj){
		this.values.put("boy7", obj);
	}
	
	public void setWomen7(Double obj){
		this.values.put("women7", obj);
	}
	
	public void setMen7(Double obj){
		this.values.put("men7", obj);
	}
	
	public void setSlavmax7(Integer obj){
		this.values.put("slavmax7", obj);
	}
	
	public void setMenrat3(Double obj){
		this.values.put("menrat3", obj);
	}
	
	public void setWomrat3(Double obj){
		this.values.put("womrat3", obj);
	}
	
	public void setBoyrat3(Double obj){
		this.values.put("boyrat3", obj);
	}
	
	public void setGirlrat3(Double obj){
		this.values.put("girlrat3", obj);
	}
	
	public void setSlavema7(Double obj){
		this.values.put("slavema7", obj);
	}
	
	public void setSlavemx7(Double obj){
		this.values.put("slavemx7", obj);
	}
	
	public void setMale7(Double obj){
		this.values.put("male7", obj);
	}
	
	public void setFemale7(Double obj){
		this.values.put("female7", obj);
	}	
	
	public void setXmimpflag(Double xmimpflag) {
		this.values.put("xmimpflag", xmimpflag);
	}
	
	public Double getXmimpflag() {
		return (Double) this.values.get("xmimpflag");
	}
	
	public void setDatedepa(Integer obj) {		
		this.values.put("datedepa", obj);			
	}
	
	public Integer getDatedepa() {
		return (Integer) this.values.get("datedepa");
	}
	
	public void setDatedepb(Integer obj) {		
		this.values.put("datedepb", obj);			
	}
	
	public Integer getDatedepb() {
		return (Integer) this.values.get("datedepb");
	}
	
	public void setDatedepc(Integer obj) {		
		this.values.put("datedepc", obj);			
	}
	
	public Integer getDatedepc() {
		return (Integer) this.values.get("datedepc");
	}
	
	/*
	 * Day that slave purchase began
	 */
	public void setD1slatra(Integer obj) {				    
		this.values.put("d1slatra", obj);		
	}
	
	public Integer getD1slatra() {
		return (Integer) this.values.get("d1slatra");
	}
	
	/*
	 * Month that slave purchase began
	 */
	public void setD1slatrb(Integer obj) {				    
		this.values.put("d1slatrb", obj);
		
	}
	
	public Integer getD1slatrb() {
		return (Integer) this.values.get("d1slatrb");
	}
	
	/*
	 * Year that slave purchase began
	 */
	public void setD1slatrc(Integer obj) {				    
		this.values.put("d1slatrc", obj);
		
	}
	
	public Integer getD1slatrc() {
		return (Integer) this.values.get("d1slatrc");
	}
	
	/*
	 * Day that vessel left last slaving port - dateleftafr
	 */
	public void setDlslatra(Integer obj) {
		 this.values.put("dlslatra", obj);
	}
	
	public Integer getDlslatra() {
		return (Integer) this.values.get("dlslatra");
	}
	
	/*
	 * Month that vessel left last slaving port - dateleftafr
	 */
	public void setDlslatrb(Integer obj) {
		 this.values.put("dlslatrb", obj);
	}
	
	public Integer getDlslatrb() {
		return (Integer) this.values.get("dlslatrb");
	}
	
	/*
	 * Year that vessel left last slaving port - dateleftafr
	 */
	public void setDlslatrc(Integer obj) {
		 this.values.put("dlslatrc", obj);
	}
	
	public Integer getDlslatrc() {
		return (Integer) this.values.get("dlslatrc");
	}
	
	/*
	 * Day that slaves landed at first place
	 */
	public void setDatarr32(Integer obj) {
	    this.values.put("datarr32", obj);
	}
	
	public Integer getDatarr32() {
		return (Integer) this.values.get("datarr32");
	}
	
	/*
	 * Month that slaves landed at first place
	 */
	public void setDatarr33(Integer obj) {
	    this.values.put("datarr33", obj);
	}
	
	public Integer getDatarr33() {
		return (Integer) this.values.get("datarr33");
	}
		
	/*
	 * Year that slaves landed at first place
	 */
	public void setDatarr34(Integer obj) {
	    this.values.put("datarr34", obj);
	}
	
	public Integer getDatarr34() {
		return (Integer) this.values.get("datarr34");
	}
	
	/*
	 * Day left on return voyage
	 */
	public void setDdepam(Integer obj) {
	    this.values.put("ddepam", obj);
	}
	
	public Integer getDdepam() {
		return (Integer) this.values.get("ddepam");
	}

	/*
	 * Month left on return voyage
	 */
	public void setDdepamb(Integer obj) {
	    this.values.put("ddepamb", obj);
	}
	
	public Integer getDdepamb() {
		return (Integer) this.values.get("ddepamb");
	}
	
	/*
	 * Year left on return voyage
	*/
	public void setDdepamc(Integer obj) {
	    this.values.put("ddepamc", obj);
	}
	
	public Integer getDdepamc() {
		return (Integer) this.values.get("ddepamc");
	}
	
	/*
	 * Day when voyage completed
	 */
	public void setDatarr43(Integer obj) {
		 this.values.put("datarr43", obj);
	}
	
	public Integer getDatarr43() {
		return (Integer) this.values.get("datarr43");
	}
	
	/*
	 * Month when voyage completed
	 */
	public void setDatarr44(Integer obj) {
		 this.values.put("datarr44", obj);
	}
	
	public Integer getDatarr44() {
		return (Integer) this.values.get("datarr44");
	}
		
	/*
	 * Year when voyage completed
	 */
	public void setDatarr45(Integer obj) {
		 this.values.put("datarr45", obj);
	}
	
	public Integer getDatarr45() {
		return (Integer) this.values.get("datarr45");
	}
	
	/*
	 * Day that slaves arrived at second place
	 */
	public void setDatarr36(Integer obj) {
		 this.values.put("datarr36", obj);
	}
	
	public Integer getDatarr36() {
		return (Integer) this.values.get("datarr36");
	}
	
	/*
	 * Month that slaves arrived at second place
	 */
	public void setDatarr37(Integer obj) {
		 this.values.put("datarr37", obj);
	}
	
	public Integer getDatarr37() {
		return (Integer) this.values.get("datarr37");
	}
	
	/*
	 * Year that slaves arrived at second place
	 */
	public void setDatarr38(Integer obj) {
		 this.values.put("datarr38", obj);
	}
	
	public Integer getDatarr38() {
		return (Integer) this.values.get("datarr38");
	}
	

	/*
	 * Day that slaves landed at third place
	 */
	public void setDatarr39(Integer obj) {
		 this.values.put("datarr39", obj);
	}
	
	public Integer getDatarr39() {
		return (Integer) this.values.get("datarr39");
	}
	
	/*
	 * Month that slaves landed at third place
	 */
	public void setDatarr40(Integer obj) {
		 this.values.put("datarr40", obj);
	}
	
	public Integer getDatarr40() {
		return (Integer) this.values.get("datarr40");
	}
	
	/*
	 * Year that slaves landed at third place
	 */
	public void setDatarr41(Integer obj) {
		 this.values.put("datarr41", obj);
	}
	
	public Integer getDatarr41() {
		return (Integer) this.values.get("datarr41");
	}
	
}

package edu.emory.library.tast.submission;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;

import org.hibernate.Session;

import edu.emory.library.tast.dm.Fate;
import edu.emory.library.tast.dm.FateOwner;
import edu.emory.library.tast.dm.FateSlaves;
import edu.emory.library.tast.dm.FateVessel;
import edu.emory.library.tast.dm.Nation;
import edu.emory.library.tast.dm.Port;
import edu.emory.library.tast.dm.Region;
import edu.emory.library.tast.dm.VesselRig;
import edu.emory.library.tast.dm.Voyage;

public class VoyagesCalculation {
	
	Voyage voyage;
	Session session;
	
	public VoyagesCalculation(Voyage voyage, Session session) {
		this.voyage = voyage;
		this.session = session;
	}
	
	/*
	 * Calls all the calculation functions in one shot
	 */
	public Voyage calculateImputedVariables() {
		calculateValueNatinimp();
		calculateValuesYear();
		calculateValuesVoyLengths();
		calculateImputedValueFate2();
		calculateImputedValueFate3();
		calculateImputedValueFate4();
		calculateValuesRegion1();
		calculateValuesRegion2();
		calculateValueMajbuypt();
		calculateValueMajselpt();
		calculateImputedValueTonmod();
		calculateSlavesEmbarkDisembark();
		calculateValuesPeople();
		return this.voyage;
	}
	
	/*
	 * fate2: Outcome of voyage for slaves
	 */
	public void calculateImputedValueFate2() {
		Integer fate = null;		
		HashMap fate2Hash = VoyagesCalcConstants.getfate2Hash();		
		Fate fateObj = voyage.getFate();
		fate = (Integer)fate2Hash.get(fateObj.getId().intValue());
		if (fate != null){
			FateSlaves fateSlave = FateSlaves.loadById(session, fate);
			voyage.setFate2(fateSlave);
		}
	}
	
	/*
	 * fate3: Outcome of voyage If vessel captured
	 */
	public void calculateImputedValueFate3() {
		Integer fate = null;		
		HashMap fate3Hash = VoyagesCalcConstants.getfate3Hash();
		Fate fateObj = voyage.getFate();
		fate = (Integer)fate3Hash.get(fateObj.getId().intValue());
		if (fate != null){
			FateVessel fateVessel = FateVessel.loadById(session, fate);
			voyage.setFate3(fateVessel);		 
		}
	}
	
	/*
	 * fate4: Outcome of voyage for owner
	 */
	public void calculateImputedValueFate4() {
		Integer fate = null;
		HashMap fate4Hash = VoyagesCalcConstants.getfate4Hash();
		Fate fateObj = voyage.getFate();
		fate = (Integer)fate4Hash.get(fateObj.getId().intValue());
		if (fate != null){
			FateOwner fateOwner = FateOwner.loadById(session, fate);
			voyage.setFate4(fateOwner);
		}
	}
	
    /*
     * Calculates value of tonmod variable
     */
	public void calculateImputedValueTonmod() {
		//Create the needed variables for calculations
		Integer tontype=voyage.getTontype(); 
		Integer tonnage=voyage.getTonnage(); 
		Integer yearam=voyage.getYearam(); 
		Integer natinimp=null;
		if(voyage.getNatinimp().getId()!=null) {natinimp=voyage.getNatinimp().getId().intValue();}  //TODO imputed natinimp - should already have a function
		
		Float tonmod=null;
		if(tonnage!=null) {tonmod=tonnage.floatValue();}
		
		//calculate
	    try {
			if (tontype == 13) {tonmod=tonnage.floatValue();}
			else if (tonnage!=null && (tontype < 3 || tontype == 4 || tontype == 5) && yearam > 1773) {tonmod = tonnage.floatValue();}
			else if (tonnage!=null && (tontype < 3 || tontype == 4 || tontype == 5) && yearam < 1774 && tonnage < 151) {tonmod=2.3f + (1.8f * tonnage);}
			else if (tonnage!=null && (tontype < 3 || tontype == 4 || tontype == 5) && yearam < 1774 && tonnage > 150 && tonnage < 251) {tonmod=65.3f + (1.2f * tonnage);}
			else if (tonnage!=null && (tontype < 3 || tontype == 4 || tontype == 5) && yearam < 1774 && tonnage > 250) {tonmod=13.1f + (1.1f * tonnage);}
			else if (tontype == 4 && yearam > 1783 && yearam < 1794) {tonmod=9999f;}
			else if (tontype == 3 || tontype == 6 || tontype == 9 || tontype == 16){tonmod = 71 + (0.86f * tonnage);}
			else if ((tontype == 3 || tontype == 6 || tontype == 9 || tontype == 16) && yearam < 1774 && tonmod < 151) {tonmod=2.3f + (1.8f * tonnage);}
			else if ((tontype == 3 || tontype == 6 || tontype == 9 || tontype == 16) && yearam < 1774 && tonmod > 150 && tonmod < 251) {tonmod=65.3f + (1.2f * tonnage);}
			else if ((tontype == 3 || tontype == 6 || tontype == 9 || tontype == 16) && yearam < 1774 && tonmod > 250) {tonmod=13.1f + (1.1f * tonnage);}
			else if (tontype == 7) {tonmod=tonnage * 2f;}
			else if (tontype == 7 && yearam > 1773 && tonmod < 151) {tonmod=2.3f + (1.8f * tonmod);}
			else if (tontype == 7 && yearam > 1773 && tonmod > 150 && tonmod < 251) {tonmod=65.3f + (1.2f * tonmod);}
			else if (tontype == 7 && yearam > 1773 && tonmod > 250) {tonmod=13.1f + (1.1f * tonmod);}
			else if (tontype == 21) {tonmod= -6.093f + (0.76155f * tonnage);}
			else if (tontype == 21 && yearam > 1773 && tonmod < 151) {tonmod=2.3f + (1.8f * tonmod);}
			else if (tontype == 21 && yearam > 1773 && tonmod > 150 && tonmod < 251) {tonmod=65.3f + (1.2f * tonmod);}
			else if (tontype == 21 && yearam > 1773 && tonmod > 250) {tonmod=13.1f + (1.1f * tonmod);}
			
			if ((tontype==null) && (yearam > 1714 && yearam < 1786) && tonnage > 0 && natinimp==7) {tontype=22;}
			
			if (tontype == 22 && tonnage < 151) {tonmod=2.3f + (1.8f * tonnage);}
			else if (tontype == 22 && tonnage > 150 && tonnage < 251) {tonmod=65.3f + (1.2f * tonnage);}
			else if (tontype == 22 && tonnage > 250) {tonmod=13.1f + (1.1f * tonnage);}
			else if (tontype == 15 || tontype == 14 || tontype == 17) {tonmod = 52.86f + (1.22f * tonnage);}
			else if (tonmod==null) {tonmod=9999f;}
		} catch (Exception e) {
			e.printStackTrace();
		}
	    
        //Store the result in the object 
	    if(tonmod!=null)
	    {
	    	voyage.setTonmod(tonmod);
	    }
	}	
	
	/*
	 * majbuypt: Principal port of slave purchase
	 */
	public void calculateValueMajbuypt() {
		int plac1tra_int = 0;
		int plac2tra_int = 0;
		int plac3tra_int = 0;
		int tslavesd_int = 0;
		int tslavesp_int = 0;
		double rslt_d = 0;
		double rslt_p = 0;
		Port majbuypt = null;
		
		Port plac1tra = voyage.getPlac1tra();
		if (plac1tra != null){
			plac1tra_int = plac1tra.getId().intValue();
		}
		
		Port plac2tra = voyage.getPlac2tra();
		if (plac2tra != null){
			plac2tra_int = plac2tra.getId().intValue();
		}
		
		Port plac3tra = voyage.getPlac3tra();
		if (plac3tra != null){
			plac3tra_int = plac3tra.getId().intValue();
		}
		
		int ncar13_int = 0;
		int ncar15_int = 0;
		int ncar17_int = 0;
		Integer ncar13 = voyage.getNcar13();
		Integer ncar15 = voyage.getNcar15();
		Integer ncar17 = voyage.getNcar17();
		if (ncar13 != null) {
			ncar13_int = ncar13.intValue();
		}
		if (ncar15 != null) {
			ncar15_int = ncar15.intValue();
		}
		if (ncar17 != null) {
			ncar17_int = ncar17.intValue();
		}
		
		Integer tslavesd = voyage.getTslavesd();
		if (tslavesd != null) {
			tslavesd_int = tslavesd.intValue();
		}
		Integer tslavesp = voyage.getTslavesp();
		if (tslavesp != null) {
			tslavesp_int = tslavesp.intValue();
		}
				
		Integer ncartot = defVal(ncar13, 0)+ defVal(ncar15, 0)+ defVal(ncar17, 0);
		/*rslt_d = ncartot.doubleValue()/tslavesd_int;
		rslt_p = ncartot.doubleValue()/tslavesp_int;*/		
		rslt_d = tslavesd_int/ncartot.doubleValue();
		rslt_p = tslavesp_int/ncartot.doubleValue();
		
		if (plac1tra_int >= 1 && plac2tra==null && plac3tra==null) {
			majbuypt = plac1tra;
		} 
		else if (plac2tra_int >= 1 && plac1tra==null && plac3tra==null) {
	    	majbuypt = plac2tra;
	    }
		else if (plac3tra_int >= 1 && plac1tra==null && plac2tra==null) {
	    	majbuypt = plac3tra;
	    }
		else if (plac1tra_int >=1 && plac2tra_int >=1 && plac3tra==null && 
		    		(rslt_d >= 0.5d) && (ncar13_int >= ncar15_int)) {
		    	majbuypt = plac1tra;
		}
		else if (plac1tra_int >=1 && plac2tra_int >=1 && plac3tra==null && 
				(rslt_d >= 0.5d) && (ncar15_int >= ncar13_int)) {
			majbuypt = plac2tra;
		}
		else if (plac1tra_int >=1 && plac2tra_int >=1 && plac3tra==null && 
				tslavesd==null && (rslt_p >= 0.5d) && (ncar13_int >= ncar15_int)) {
			majbuypt = plac1tra;
		}
		else if (plac1tra_int >=1 && plac2tra_int >=1 && plac3tra==null && 
				tslavesd==null && rslt_p >= 0.5d && ncar15_int >= ncar13_int) {
			majbuypt = plac2tra;
		}
		else if (plac1tra_int >=1 && plac2tra_int >=1 && plac3tra==null && rslt_d < 0.5d && ncar13_int < ncar15_int) {
			majbuypt = plac1tra;
		}
		else if (plac1tra_int >= 1 && plac2tra_int >= 1 && plac3tra==null && tslavesd==null && tslavesp==null && ncar13_int >= ncar15_int) {
			majbuypt = plac1tra;
		}
		else if (plac1tra_int >= 1 && plac2tra_int >= 1 && plac3tra==null && tslavesd==null && tslavesp==null && ncar15_int > ncar13_int) {
			majbuypt = plac2tra;
		}
		else if (plac1tra_int >= 1 && plac2tra_int >= 1 && plac3tra==null && tslavesd==null && tslavesp==null && ncartot==null) {
			majbuypt = plac2tra;
		}
		else  if (plac1tra_int >=1 && plac3tra_int >=1 && plac2tra==null) {
			majbuypt = plac1tra;
		}
		else  if (plac2tra_int >=1 && plac3tra_int >=1 && plac1tra==null) {
			majbuypt = plac2tra;
		}
		else if (plac1tra_int >=1 && plac2tra_int >=1 && plac3tra_int >= 1 && rslt_d >= 0.5d && ncar13_int >= ncar15_int && ncar13_int >= ncar17_int) {
			majbuypt = plac1tra;
		}
		else if (plac1tra_int >=1 && plac2tra_int >=1 && plac3tra_int >= 1 && tslavesd==null && rslt_p >= 0.5d && ncar13_int >= ncar15_int && ncar13_int >= ncar17_int) {
			majbuypt = plac1tra;
		}
		else if (plac1tra_int >=1 && plac2tra_int >=1 && plac3tra_int >= 1 && rslt_d >= 0.5d && ncar15_int >= ncar13_int && ncar15_int >= ncar17_int) {
			majbuypt = plac2tra;
		}
		else if (plac1tra_int >=1 && plac2tra_int >=1 && plac3tra_int >= 1 && tslavesd==null && rslt_p >= 0.5d && ncar15_int >= ncar13_int && ncar15_int >= ncar17_int) {
			majbuypt = plac2tra;
		}
		else if (plac1tra_int >=1 && plac2tra_int >=1 && plac3tra_int >= 1 && rslt_d >= 0.5d && ncar17_int >= ncar13_int && ncar17_int >= ncar15_int) {
	    	majbuypt = plac3tra;
	    }
	    else if (plac1tra_int >=1 && plac2tra_int >=1 && plac3tra_int >= 1 && tslavesd==null && rslt_p >= 0.5d && ncar17_int >= ncar13_int && ncar17_int >= ncar15_int) {
	    	majbuypt = plac1tra;
	    }
	    else if (plac1tra_int >=1 && plac2tra_int >=1 && plac3tra_int >= 1 && rslt_d < 0.5d && ncar13_int==0) {
	    	majbuypt = plac1tra;
	    }
	    else if (plac1tra_int >=1 && plac2tra_int >=1 && plac3tra_int >= 1 && rslt_d < 0.5d && ncar13_int>=1 && ncar15_int==0 && ncar17_int==0) {
	    	majbuypt = plac3tra;
	    }
	    else if (plac1tra_int >=1 && plac2tra_int >=1 && plac3tra_int >= 1 && rslt_d < 0.5d && ncar13_int>=1 && ncar15_int>=1 && ncar17_int==0) {
	    	majbuypt = plac3tra;
	    }
	    else if (plac1tra_int >=1 && plac2tra_int >=1 && plac3tra_int >= 1 && tslavesd==null && rslt_p < 0.5d && ncar13_int==0) {
	    	majbuypt = plac1tra;
	    }
	    else if (plac1tra_int >=1 && plac2tra_int >=1 && plac3tra_int >= 1 && tslavesd==null && rslt_p < 0.5d && ncar13_int>=1 && ncar15_int==0 && ncar17_int==0) {
	    	majbuypt = plac3tra;
	    }
	    else if (tslavesd==null && tslavesp==null && ncartot.intValue() >=1 && plac1tra_int >= 1 && plac2tra_int >= 1 && plac3tra_int >= 1) {
	    	majbuypt = plac3tra;
	    }
	    else if (ncartot==null && plac1tra_int >= 1 && plac2tra_int >= 1 && plac3tra_int >= 1) {
	    	majbuypt = plac3tra;
	    }
	    else if (ncartot==null && tslavesd==null && tslavesp==null && plac1tra_int >= 1 && plac2tra_int >= 1 && plac3tra_int >= 1) {
        	majbuypt = plac3tra;
        }
        //if Port is not null, then set the value in voyage 
        if (majbuypt != null){
			voyage.setMajbuypt(majbuypt);
		}
	    
	}
	
	/*
	 * majselpt: Principal port of slave purchase
	 */
	public void calculateValueMajselpt() {
		 Double slastot=0d;
		 
	     int sla1port_int = 0;
	     int adpsale1_int = 0;
		 int adpsale2_int = 0;
		 double slaarriv_doub = 0d;
		 Port majselpt = null;
		 
		 Integer slas32 = voyage.getSlas32();
		 Integer slas36 = voyage.getSlas36();
		 Integer slas39 = voyage.getSlas39();
	      
		 slastot = getSlastot();
		 
		 Integer slaarriv = voyage.getSlaarriv();
		 if (slaarriv != null) {
			 slaarriv_doub = slaarriv.doubleValue();
		 }
		 
		 double rslt_sla = slastot.doubleValue()/slaarriv_doub;
		 
		 Port sla1port = voyage.getSla1port();
		 Port adpsale1 = voyage.getAdpsale1();
		 Port adpsale2 = voyage.getAdpsale2();
		 
		 if (sla1port != null) {
			 sla1port_int = sla1port.getId().intValue();
		 }
		 if (adpsale1 != null){
			 adpsale1_int = adpsale1.getId().intValue();
		 }
		 if (adpsale2 != null) {
		  adpsale2_int = adpsale2.getId().intValue();
		 }		 
		 
	     if (sla1port_int >= 1 && adpsale1_int==0 && adpsale2_int ==0) {
	    	 majselpt = sla1port;
	     }
	     else if (adpsale1_int  >= 1 && sla1port==null && adpsale2==null) {
	    	 majselpt = adpsale1;
	     }
	     else if (adpsale2_int >= 1 && sla1port==null && adpsale1 == null) {
	    	 majselpt = adpsale2;
	     }
	     else if (sla1port_int  >=1 && adpsale1_int  >=1 && adpsale2==null && rslt_sla >= 0.5d && slas32 >= slas36) {
	    	 majselpt = sla1port;
	     }
	     else if (sla1port_int >=1 && adpsale1_int >=1 && adpsale2==null && rslt_sla >= 0.5d && slas36 >= slas32) {
	    	 majselpt = adpsale1;
	     }
	     else if (sla1port_int >=1 && adpsale1_int >=1 && adpsale2==null && rslt_sla < 0.5d && slas32 < slas36) {
	    	 majselpt = sla1port;
	     }
	     else if (sla1port_int >=1 && adpsale1_int >=1 && adpsale2==null && rslt_sla < 0.5d && slas36 < slas32) {
	    	 majselpt = adpsale1;
	     }
	     else if (slastot==null && sla1port_int >= 1 && adpsale1_int >= 1 && adpsale2==null) {
	    	 majselpt = adpsale1;
	     }
	     else if (sla1port_int >= 1 && adpsale1_int >= 1 && adpsale2==null && slaarriv==null && slas32 >= slas36) {
	    	 majselpt = sla1port;
	     }
	     else if (sla1port_int >= 1 && adpsale1_int >= 1 && adpsale2==null && slaarriv==null && slas36 > slas32) {
	    	 majselpt = adpsale1;
	     }
	     else if (sla1port_int >= 1 && adpsale1_int >= 1 && adpsale2==null && slaarriv==null && getNcartot()==null) {
	    	 majselpt = adpsale1;
	     }	     
	     else if (sla1port_int >=1 && adpsale2_int >=1 && adpsale1 == null) {
	    	 majselpt = sla1port;
	     }
	     else if (sla1port_int >=1 && adpsale1_int >=1 && adpsale2_int >= 1 && rslt_sla >= 0.5d && slas32 >= slas36 && slas32 >= slas39) {
	    	 majselpt = sla1port;
	     }
	     else if (sla1port_int >=1 && adpsale1_int >=1 && adpsale2_int >= 1 && rslt_sla >= 0.5d && slas36 >= slas32 && slas36 >= slas39) {
	    	majselpt = adpsale1;
	     }
	     else if (sla1port_int >=1 && adpsale1_int >=1 && adpsale2_int >= 1 && rslt_sla >= 0.5d && slas39 >= slas32 && slas39 >= slas36) {
	    	 majselpt = adpsale2;
	     }
	     else if (sla1port_int >=1 && adpsale1_int >=1 && adpsale2_int >= 1 && rslt_sla < 0.5d && slas32 == 0) {
	    	 majselpt = sla1port;
	     }
	     else if (sla1port_int >=1 && adpsale1_int >=1 && adpsale2_int >= 1 && rslt_sla < 0.5d && slas32 >=1 && slas36==0) {
	    	 majselpt = adpsale1;
	     }
	     else if (sla1port_int >=1 && adpsale1_int >=1 && adpsale2_int >= 1 && rslt_sla < 0.5d && slas32 >=1 && slas39==0) {
	    	 majselpt = adpsale2;
	     }
	     else if (slaarriv==null && slastot.intValue() >=1 && sla1port_int >= 1 && adpsale1_int >= 1 && adpsale2_int >= 1 && slas32 >= slas36 && slas32 >= slas39) {
	    	 majselpt = sla1port;
	     }
	     else if (slaarriv==null && slastot >=1 && sla1port_int >= 1 && adpsale1_int >= 1 && adpsale2_int >= 1 && slas36 >= slas32 && slas36 >= slas39) {
	    	 majselpt = adpsale1;
	     }
	     else if (slaarriv==null && slastot >=1 && sla1port_int >= 1 && adpsale1_int >= 1 && adpsale2_int >= 1 && slas39 >= slas32 && slas39 >= slas36) {
	    	 majselpt = adpsale2;
	     }
	     else if (slaarriv==null && slastot >=1 && sla1port_int >= 1 && adpsale1_int >= 1 && adpsale2==null) {
	    	 majselpt = adpsale1;
	     }
	     else if (slastot==null && sla1port_int >= 1 && adpsale1_int >= 1 && adpsale2_int >= 1) {
	    	 majselpt = adpsale2;
	     }
	     else if (slastot==null && slaarriv==null && sla1port_int >= 1 && adpsale1_int >= 1 && adpsale2_int >= 1) {
	    	 majselpt = sla1port;
	     }
	     
	     voyage.setMajselpt(majselpt);

	}
	
	public Integer getNcartot(){
		Integer ncar13 = voyage.getNcar13();
		Integer ncar15 = voyage.getNcar15();
		Integer ncar17 = voyage.getNcar17();		
				
		Integer ncartot = defVal(ncar13, 0)+ defVal(ncar15, 0)+ defVal(ncar17, 0);
		return ncartot;
	}
	
	
	/*
	 * Calculates slastot
	 * This is not written back to the database 
	 */
	public Double getSlastot(){
		Integer slas32 = voyage.getSlas32();
		 Integer slas36 = voyage.getSlas36();
		 Integer slas39 = voyage.getSlas39();
	      
		 Double slastot = new Double(defVal(slas32, 0)+defVal(slas36, 0)+defVal(slas39,0));
		return slastot;
	}
	
	
	/*
	 * Returns default value if Integer is null
	 */
	public static Integer defVal(Integer orig, Integer def)
	{
	   if(orig==null)
	   {
	        return def;
	   }
	   else
	       return orig;
	}

	/*
	 * Returns default value if Double is null
	 */
	public static Double defVal(Double orig, Double def)
	{
	   if(orig==null)
	   {
	        return def;
	   }
	   else
	       return orig;
	}
	/*
	 * Returns default value if Long is null
	 */
	public static Long defVal(Long orig, Long def)
	{
	   if(orig==null)
	   {
	        return def;
	   }
	   else
	       return orig;
	}
	
	/*
	 * Calculates year5, year10, year25 and year100
	 */
	public void calculateValuesYear() 
	{
		//Create variables for calculations
		Integer yearam=voyage.getYearam();
		Integer year100=-1; //dummy default value because no ranges array
		
		//Assign values and call function to calculate year5
		ArrayList dateInputs;
		ArrayList dateRanges;
		ArrayList impDate;
		
		Integer year5=null;
		try {
			dateInputs = new ArrayList();
			dateInputs.add(yearam);
			dateRanges = VoyagesCalcConstants.getdateRanges1();
			impDate = recode(dateInputs, dateRanges, false);
			year5 = (Integer) impDate.get(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
 
	  Integer year10=null;;
	try {
		//Assign values and call function to calculate year10
		    dateInputs=new ArrayList();
		    dateInputs.add(yearam);
		    dateRanges= VoyagesCalcConstants.getdateRanges2();
		    impDate = recode(dateInputs, dateRanges, false);
		    year10 = (Integer) impDate.get(0);
	} catch (Exception e) {
		e.printStackTrace();
	}

	  Integer year25=null;;
	try {
		//Assign values and call function to calculate year25
		    dateInputs=new ArrayList();
		    dateInputs.add(yearam);
		    dateRanges=VoyagesCalcConstants.getdateRanges3();
		    impDate = recode(dateInputs, dateRanges, false);
		    year25 = (Integer) impDate.get(0);
	} catch (Exception e) {
		e.printStackTrace();
	}

	    //ranges too few for year100 to create a HashMap
	    if (yearam < 1601) {year100=1500;}
	    else if (yearam > 1600 && yearam < 1701) {year100=1600;}
	    else if (yearam > 1700 && yearam < 1801) {year100=1700;}
	    else if (yearam > 1800) {year100=1800;}
	    
	    //Set values in object 
	    //value of -1 means no match from function
	    if(year5!=null && year5!=-1)
	    {
	    	voyage.setYear5(year5);
	    }
	    if(year10!=null && year10!=-1)
	    {
	    	voyage.setYear10(year10);
	    }
	    if(year25!=null && year25!=-1)
	    {
	    	voyage.setYear25(year5);
	    }
	    if(year100!=null && year100!=-1)
	    {
	    	voyage.setYear100(year100);
	    }
	}
	
	/*
	 * Accepts array of inputs and an array of ranges
	 * Returns the associated value for each input based on which range it is in
	 * -1 is returned if the input does not fall into a range 
	 */
	public static ArrayList recode(ArrayList orig, ArrayList ranges, boolean torf)
	{
	  ArrayList ret=new ArrayList(); //Array of return values


	  //Loop over each input value
	  for(int o=0; o < orig.size(); o++)
	  {
	     Integer curr=(Integer)orig.get(o);
	     Integer foundvalue=-1; //value to be returned if no match is found

	     //search each range until a match is found 
	     for(int i=0; i < ranges.size(); i++)
	     {
	         foundvalue=-1;
	         Integer[] range = (Integer[])ranges.get(i);
	         Integer low=range[0];
	         Integer high=range[1];
	         
	         if(torf) //Include low and high values in search
	         {
	             if(curr >=low && curr <= high)
	             {
	                 foundvalue=range[2];
	                 break;
	             }
	         }
	         else //do not include the low and high values in the search
	         {
	             if(curr > low && curr < high)
	             {
	                   foundvalue=range[2];
	                 break;
	             }
	         }
	      }
	     ret.add(foundvalue); //Add the value to the return array
	  }
	  return ret;
	}
	
	/*
	 * Calculates natinimp variable
	 */
	public void calculateValueNatinimp() {
		Integer national=-1;
		
		Integer natinimp=null;
		try {
			//Create variables for calculation
			if(voyage.getNational()!=null) {national = voyage.getNational().getId().intValue();}
			HashMap natHash = VoyagesCalcConstants.getnatHash();
			
			natinimp = (Integer)natHash.get(national);
		} catch (Exception e) {
			e.printStackTrace();
		}
	    
	    //Store the value in a Nation object and update voyages object
	    if (natinimp != null){
	    	Nation nation= Nation.loadById(session, natinimp);
			voyage.setNatinimp(nation);
		}
	}
	
	/*
	 * Calculates voy1imp and voy2imp values
	 */
	public void calculateValuesVoyLengths() {
		//Create variables for calculation
		Date dateland1=voyage.getDateland1();
		Date datedep=voyage.getDatedep(); 
		Date dateleftafr=voyage.getDateleftafr();
		
		//Calculate value
		Integer voy1imp = dateDiff(datedep, dateland1);
	    Integer voy2imp = dateDiff(dateland1, dateleftafr);
	    
	    //update voyages object
	    if (voy1imp != null){
	    	voyage.setVoy1imp(voy1imp);
		}
	    if (voy2imp != null){
	    	voyage.setVoy2imp(voy2imp);
		}
	}
	
	/*
	 * Calculates the number of days between two dates
	 */
	public static Integer dateDiff(Date start, Date end)
	{
	    if(start==null ||  end==null)  //If either are null return null
	    {
	        return null;
	    }

	    //move the time back to the beginning of the days
	    GregorianCalendar greg = new GregorianCalendar();
	    greg.setTime(start);
	    greg.set(Calendar.HOUR_OF_DAY, 0);
	    greg.set(Calendar.MINUTE, 0);
	    greg.set(Calendar.SECOND, 0);
	    greg.set(Calendar.MILLISECOND, 0);
	    start=greg.getTime();
	    
	    greg.setTime(end);
	    greg.set(Calendar.HOUR_OF_DAY, 0);
	    greg.set(Calendar.MINUTE, 0);
	    greg.set(Calendar.SECOND, 0);
	    greg.set(Calendar.MILLISECOND, 0);
	    end=greg.getTime();
	    
	    
	    //convert dates to long, subtract, convert to days
	    Long diff = (end.getTime() - start.getTime())/(1000 * 60 * 60 * 24);
	    diff = round(diff);
	    
	    if(diff < 0)
	    	diff=diff*-1;

	    return diff.intValue();
	}
	
	/*
	 * Rounds a value to the closest double value
	 */
	public static Double round(Double d)
	{
		if(d==null)
			return null; //returns null when input is null
		
	    Double ret=0d;
	    int decimalPlace = 0;
	    BigDecimal bd = new BigDecimal(d);
	    bd = bd.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
	    ret = bd.doubleValue();
	    return ret;
	}

	/*
	 * Rounds a value to the closest long value
	 */
	public static Long round(Long l)
	{
		if(l==null)
			return null; //returns null when input is null
		
	    Long ret=0l;
	    int decimalPlace = 0;
	    BigDecimal bd = new BigDecimal(l);
	    bd = bd.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
	    ret = bd.longValue();
	    return ret;
	}
	
	/*
	 * ptdepimp: Imputed port where voyage began
	 */	
	public void calculatePtDepImp() {
		Port ptdepimp = null;
		Port portdep = voyage.getPortdep(); 
		if (portdep != null){
			ptdepimp = portdep;
		}
		
		//TODO imputed var Majselpt
		if (ptdepimp == null) {
			int ptdepimp_int = 0;
			int majselpt_int = 0;
			Port majselpt = voyage.getMajselpt();
			if (majselpt != null) {
				majselpt_int = majselpt.getId().intValue();
			}
		    if (majselpt_int >= 50200 && majselpt_int < 50300 && portdep==null) {
		    	ptdepimp_int = 50299;
		    }
		    else if (majselpt_int >= 50300 && majselpt_int < 50400 && portdep==null) {
		    	ptdepimp_int = 50399;
		    }
		    else if (majselpt_int >= 50400 && majselpt_int < 50500 && portdep==null) {
		    	ptdepimp_int = 50422;
		    }
		    
		    if (ptdepimp_int != 0) {
		    	ptdepimp = Port.loadById(session, ptdepimp_int);
		    }	    
		}
	    
	    voyage.setPtdepimp(ptdepimp);
	}
	
	/*
	 * tslmtimp:  Imputed total of slaves embarked for mortality calculation
	 * vymrtimp:  Imputed number of slaves died in  middle passage
	 * vymrtrat:  Slaves died on voyage/Slaves embarked	  
	 */	
	public void calculateTslmtimp() {
		try {
			float vymrtrat = 0f;		
			
			Integer sladvoy = voyage.getSladvoy();
			Integer vymrtimp = sladvoy;			
			Integer tslavesd = voyage.getTslavesd();			
			Integer slaarriv = voyage.getSlaarriv();
			
			if (vymrtimp == null && tslavesd!=null && slaarriv!=null) {				
				vymrtimp = tslavesd - slaarriv;
			}			
			Integer tslmtimp = tslavesd;
			
			if ((tslavesd == null) && slaarriv!= null && slaarriv > 0) {
				tslmtimp = slaarriv + vymrtimp;
			}
			if ( (vymrtimp!=null && vymrtimp >= 1) && (tslmtimp != null) ) {
				vymrtrat = vymrtimp / tslmtimp;
			}
			
			voyage.setTslmtimp(tslmtimp);
			
			if (vymrtrat != 0) {
				voyage.setVymrtrat(vymrtrat);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*
	 * Calculates first set of region variables
	 */
	public void calculateValuesRegion1()
	{
		//Create Return values
		Integer constreg=null;
		Integer regisreg=null;
		Integer deptreg=null;
		Integer deptregimp=null;
		Integer embreg=null;
		Integer embreg2=null;
		Integer regem1=null;
		Integer regem2=null;
		Integer regem3=null;
		Integer majbuyreg=null;
		Integer majbyimp=null;
		Integer regarr=null;
		Integer regarr2=null;
		Integer regdis1=null;
		Integer regdis2=null;
		Integer regdis3=null;
		Integer majselrg=null;
		Integer mjselimp=null;
		Integer retrnreg=null;
		
		//Input variables
		Integer placcons = -1;
		Integer placreg = -1;
		Integer portdep = -1; 
		Integer ptdepimp = -1;  //TODO imputed ptdepimp 
		Integer embport = -1; 
		Integer embport2 = -1;
		Integer plac1tra = -1; 
		Integer plac2tra = -1;
		Integer plac3tra = -1;
		Integer majbuypt = -1; //TODO imputed  majbuypt
		Integer mjbyptimp = -1; //TODO imputed mjbyptimp
		Integer arrport = -1;
		Integer arrport2 = -1;
		Integer sla1port = -1;
		Integer adpsale1 = -1;
		Integer adpsale2 = -1;
		Integer majselpt = -1; //TODO imputed majselpt
		Integer mjslptimp = -1; //TODO imputed mjslptimp
		Integer portret = -1;
		
		
		
		try {
			//Get Values for first region calculation
			if(voyage.getPlaccons()!=null) {placcons = voyage.getPlaccons().getId().intValue();}
			if(voyage.getPlacreg()!=null) {placreg = voyage.getPlacreg().getId().intValue();}
			if(voyage.getPortdep()!=null) {portdep = voyage.getPortdep().getId().intValue();} 
			if(voyage.getPtdepimp()!=null) {ptdepimp = voyage.getPtdepimp().getId().intValue();}  
			if(voyage.getEmbport()!=null) {embport = voyage.getEmbport().getId().intValue();} 
			if(voyage.getEmbport2()!=null) {embport2 = voyage.getEmbport2().getId().intValue();}
			if(voyage.getPlac1tra()!=null) {plac1tra = voyage.getPlac1tra().getId().intValue();} 
			if(voyage.getPlac2tra()!=null) {plac2tra = voyage.getPlac2tra().getId().intValue();}
			if(voyage.getPlac3tra()!=null) {plac3tra = voyage.getPlac3tra().getId().intValue();}
			if(voyage.getMajbuypt()!=null) {majbuypt = voyage.getMajbuypt().getId().intValue();} 
			if(voyage.getMjbyptimp()!=null) {mjbyptimp = voyage.getMjbyptimp().getId().intValue();} 
			if(voyage.getArrport()!=null) {arrport = voyage.getArrport().getId().intValue();}
			if(voyage.getArrport2()!=null) {arrport2 = voyage.getArrport2().getId().intValue();}
			if(voyage.getSla1port()!=null) {sla1port = voyage.getSla1port().getId().intValue();}
			if(voyage.getAdpsale1()!=null) {adpsale1 = voyage.getAdpsale1().getId().intValue();}
			if(voyage.getAdpsale2()!=null) {adpsale2 = voyage.getAdpsale2().getId().intValue();}
			if(voyage.getMajselpt()!=null) {majselpt = voyage.getMajselpt().getId().intValue();} 
			if(voyage.getMjslptimp()!=null) {mjslptimp = voyage.getMjslptimp().getId().intValue();}
			if(voyage.getPortret()!=null) {portret = voyage.getPortret().getId().intValue();}
			
			//Add to input array
			ArrayList inputs=new ArrayList();
			inputs.add(placcons);
			inputs.add(placreg);
			inputs.add(portdep);
			inputs.add(ptdepimp);
			inputs.add(embport);
			inputs.add(embport2);
			inputs.add(plac1tra);
			inputs.add(plac2tra);
			inputs.add(plac3tra);
			inputs.add(majbuypt);
			inputs.add(mjbyptimp);
			inputs.add(arrport);
			inputs.add(arrport2);
			inputs.add(sla1port);
			inputs.add(adpsale1);
			inputs.add(adpsale2);
			inputs.add(majselpt);
			inputs.add(mjslptimp);
			inputs.add(portret);

			//Get Ranges for first calculation
			ArrayList ranges= VoyagesCalcConstants.getRegionRanges1();

			//Do the calculation
			ArrayList impVars = recode(inputs, ranges, true);
			
			constreg = (Integer) impVars.get(0);
			regisreg = (Integer) impVars.get(1);
			deptreg = (Integer) impVars.get(2);
			deptregimp = (Integer) impVars.get(3);
			embreg = (Integer) impVars.get(4);
			embreg2 = (Integer) impVars.get(5);
			regem1 = (Integer) impVars.get(6);
			regem2 = (Integer) impVars.get(7);
			regem3 = (Integer) impVars.get(8);
			majbuyreg = (Integer) impVars.get(9);
			majbyimp = (Integer) impVars.get(10);
			regarr = (Integer) impVars.get(11);
			regarr2 = (Integer) impVars.get(12);
			regdis1 = (Integer) impVars.get(13);
			regdis2 = (Integer) impVars.get(14);
			regdis3 = (Integer) impVars.get(15);
			majselrg = (Integer) impVars.get(16);
			mjselimp = (Integer) impVars.get(17);
			retrnreg = (Integer) impVars.get(18);
		} catch (Exception e) {
			e.printStackTrace();
		}
	    
	    //Store the values back to the voyage object
	    if(constreg!=null && constreg!=-1)
	    {
	    	Region region = Region.loadById(session, constreg);
	    	voyage.setConstreg(region);	    
	    }
	    
	    if(regisreg!=null && regisreg!=-1)
	    {
	    	Region region = Region.loadById(session, regisreg);
	    	voyage.setRegisreg(region);	    
	    }
	    if(deptreg!=null && deptreg!=-1)
	    {
	    	Region region = Region.loadById(session, deptreg);
	    	voyage.setDeptreg(region);	    
	    }
	    if(deptregimp!=null && deptregimp!=-1)
	    {
	    	Region region = Region.loadById(session, deptregimp);
	    	voyage.setDeptregimp(region);	    
	    }
	    if(embreg!=null && embreg!=-1)
	    {
	    	Region region = Region.loadById(session, embreg);
	    	voyage.setEmbreg(region);	    
	    }
	    if(embreg2!=null && embreg2!=-1)
	    {
	    	Region region = Region.loadById(session, embreg2);
	    	voyage.setEmbreg2(region);	    
	    }
	    if(regem1!=null && regem1!=-1)
	    {
	    	Region region = Region.loadById(session, regem1);
	    	voyage.setRegem1(region);	    
	    }
	    if(regem2!=null && regem2!=-1)
	    {
	    	Region region = Region.loadById(session, regem2);
	    	voyage.setRegem2(region);	    
	    }
	    if(regem3!=null && regem3!=-1)
	    {
	    	Region region = Region.loadById(session, regem3);
	    	voyage.setRegem3(region);	    
	    }
	    if(majbuyreg!=null && majbuyreg!=-1)
	    {
	    	Region region = Region.loadById(session, majbuyreg);
	    	voyage.setMajbuyreg(region);	    
	    }
	    if(majbyimp!=null && majbyimp!=-1)
	    {
	    	Region region = Region.loadById(session, majbyimp);
	    	voyage.setMajbyimp(region);	    
	    }
	    if(regarr!=null && regarr!=-1)
	    {
	    	Region region = Region.loadById(session, regarr);
	    	voyage.setRegarr(region);	    
	    }
	    if(regarr2!=null && regarr2!=-1)
	    {
	    	Region region = Region.loadById(session, regarr2);
	    	voyage.setRegarr2(region);	    
	    }
	    if(regdis1!=null && regdis1!=-1)
	    {
	    	Region region = Region.loadById(session, regdis1);
	    	voyage.setRegdis1(region);	    
	    }
	    if(regdis2!=null && regdis2!=-1)
	    {
	    	Region region = Region.loadById(session, regdis2);
	    	voyage.setRegdis2(region);	    
	    }
	    if(regdis3!=null && regdis3!=-1)
	    {
	    	Region region = Region.loadById(session, regdis3);
	    	voyage.setRegdis3(region);	    
	    }
	    if(majselrg!=null && majselrg!=-1)
	    {
	    	Region region = Region.loadById(session, majselrg);
	    	voyage.setMajselrg(region);	    
	    }
	    if(mjselimp!=null && mjselimp!=-1)
	    {
	    	Region region = Region.loadById(session, mjselimp);
	    	voyage.setMjselimp(region);	    
	    }
	    if(retrnreg!=null && retrnreg!=-1)
	    {
	    	Region region = Region.loadById(session, retrnreg);
	    	voyage.setRetrnreg(region);	    
	    }
	    	    
	}
	
	/*
	 * Calculates second set of region variables
	 */
	public void calculateValuesRegion2()
	{
		//Create Return values
		Integer deptreg1=null;
		Integer deptregimp1=null;
		Integer majbyimp1=null;
		Integer mjselimp1=null;
		Integer retrnreg1=null;
		
		//Input variables for second region calculation
		Integer portdep = -1;
		Integer ptdepimp = -1; //TODO imputed ptdepimp
		Integer mjbyptimp = -1; //TODO imputed mjbyptimp
		Integer mjslptimp = -1; //TODO imputed mjslptimp
		Integer portret = -1;
		
		try {
			//Input variables for second region calculation
			 if(voyage.getPortdep()!=null) {portdep = voyage.getPortdep().getId().intValue();}
			 if(voyage.getPtdepimp()!=null) {ptdepimp = voyage.getPtdepimp().getId().intValue();} //TODO imputed ptdepimp
			 if(voyage.getMjbyptimp()!=null) {mjbyptimp = voyage.getMjbyptimp().getId().intValue();} //TODO imputed mjbyptimp
			 if(voyage.getMjslptimp()!=null) {mjslptimp = voyage.getMjslptimp().getId().intValue();} //TODO imputed mjslptimp
			 if(voyage.getPortret()!=null) {portret = voyage.getPortret().getId().intValue();}
					
			//Add to input array
			ArrayList inputs=new ArrayList();
			inputs.add(portdep);
			inputs.add(ptdepimp);
			inputs.add(mjbyptimp);
			inputs.add(mjslptimp);
			inputs.add(portret);

			//Get Ranges for second calculation
			ArrayList ranges= VoyagesCalcConstants.getRegionRanges2();

			//Do the calculation
			ArrayList impVars = recode(inputs, ranges, true);
			
			deptreg1 = (Integer) impVars.get(0);
			deptregimp1 = (Integer) impVars.get(1);
			majbyimp1 = (Integer) impVars.get(2);
			mjselimp1 = (Integer) impVars.get(3);
			retrnreg1 = (Integer) impVars.get(4);
		} catch (Exception e) {
			e.printStackTrace();
		}
	    
	    //Store the values back to the voyage object
	    if(deptreg1!=null && deptreg1!=-1)
	    {
	    	Region region = Region.loadById(session, deptreg1);
	    	voyage.setDeptreg1(region);	    
	    }
	    if(deptregimp1!=null && deptregimp1!=-1)
	    {
	    	Region region = Region.loadById(session, deptregimp1);
	    	voyage.setDeptregimp1(region);	    
	    }
	    if(majbyimp1!=null && majbyimp1!=-1)
	    {
	    	Region region = Region.loadById(session, majbyimp1);
	    	voyage.setMajbyimp1(region);	    
	    }
	    if(mjselimp1!=null && mjselimp1!=-1)
	    {
	    	Region region = Region.loadById(session, mjselimp1);
	    	voyage.setMjselimp1(region);	    
	    }
	    if(retrnreg1!=null && retrnreg1!=-1)
	    {
	    	Region region = Region.loadById(session, retrnreg1);
	    	voyage.setRetrnreg1(region);	    
	    }
   	    
	}

	/*
	 * Imputed number of slaves embarked and disembarked
 
	 */
	public void calculateSlavesEmbarkDisembark()
	{
		//Variables to calculate
		Double slaximp=null;
		Double slamimp=null;
		try {
			//Get variables for calculation
			Integer tslavesd = voyage.getTslavesd();
			Integer xmimpflag =0; //TODO imputed  - get value from getter method once it is created
			Integer tslavesp = voyage.getTslavesp();
			Integer ncartot = getNcartot();
			Double slastot = getSlastot();
			Integer slaarriv = voyage.getSlaarriv();
			Long fate2 = voyage.getFate2().getId();
			
			slaximp = 0d;
			slamimp = 0d;
			
			//Do ALL of the calculations
			if (tslavesd.doubleValue() >= 1d) {slaximp = tslavesd.doubleValue();}
			if (tslavesd==null && tslavesp >=1) {slaximp = tslavesp.doubleValue();}
			if (ncartot >= 1 && tslavesd==null && tslavesp==null) {slaximp = ncartot.doubleValue();}
			if (slaarriv >= 1) {slamimp = slaarriv.doubleValue();}
			if (slastot >= 1 && slaarriv==null) {slamimp = slastot;}
			//TODO xmimpflag imputed var
			if (xmimpflag == 127 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.165107561642471);}
			else if (xmimpflag == 127 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null){slaximp = slamimp / (1-0.165107561642471);}
			else if (xmimpflag == 127 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp=163.181286549708;}
			else if (xmimpflag == 127 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null){slaximp =163.181286549708/ (1-0.165107561642471 );}
			
			if (xmimpflag == 128 && slaximp >= 1 && slaarriv==null && slastot==null){slamimp = slaximp - (slaximp*0.230972326367458);}
			else if (xmimpflag == 128 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null){slaximp = slamimp / (1- 0.230972326367458);}
			else if (xmimpflag == 128 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null){slamimp =241.774647887324;}
			else if (xmimpflag == 128 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null){slaximp =241.774647887324/ (1-0.230972326367458 );}
			
			if (xmimpflag == 129 && slaximp >= 1 && slaarriv==null && slastot==null){slamimp = slaximp - (slaximp*0.218216262481124);}
			else if (xmimpflag == 129 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null){slaximp = slamimp / (1- 0.218216262481124);}
			else if (xmimpflag == 129 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null){slamimp =249.141527001862;}
			else if (xmimpflag == 129 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null){slaximp =249.141527001862/ (1-0.218216262481124 );}
			
			if (xmimpflag == 130 && slaximp >= 1 && slaarriv==null && slastot==null){slamimp = slaximp - (slaximp*0.164154067860228);}
			else if (xmimpflag == 130 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null){slaximp = slamimp / (1- 0.164154067860228);}
			else if (xmimpflag == 130 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null){slamimp =227.680034129693;}
			else if (xmimpflag == 130 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null){slaximp =227.680034129693/ (1-0.164154067860228 );}
			
			if (xmimpflag == 131 && slaximp >= 1 && slaarriv==null && slastot==null){slamimp = slaximp - (slaximp*0.153670852602567);}
			else if (xmimpflag == 131 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null){slaximp = slamimp / (1- 0.153670852602567);}
			else if (xmimpflag == 131 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null){slamimp =272.60549132948;}
			else if (xmimpflag == 131 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null){slaximp =272.60549132948/ (1-0.153670852602567 );}
			
			if (xmimpflag == 132 && slaximp >= 1 && slaarriv==null && slastot==null){slamimp = slaximp - (slaximp*0.120410468186061);}
			else if (xmimpflag == 132 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null){slaximp = slamimp / (1- 0.120410468186061);}
			else if (xmimpflag == 132 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null){slamimp =268.071314102564;}
			else if (xmimpflag == 132 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null){slaximp =268.071314102564/ (1-0.120410468186061 );}
			
			if (xmimpflag == 133 && slaximp >= 1 && slaarriv==null && slastot==null){slamimp = slaximp - (slaximp*0.126821090786133);}
			else if (xmimpflag == 133 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null){slaximp = slamimp / (1- 0.126821090786133);}
			else if (xmimpflag == 133 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null){slamimp =290.826654240447;}
			else if (xmimpflag == 133 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null){slaximp =290.826654240447/ (1-0.126821090786133 );}
			
			if (xmimpflag == 134 && slaximp >= 1 && slaarriv==null && slastot==null){slamimp = slaximp - (slaximp*0.105799354866935);}
			else if (xmimpflag == 134 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null){slaximp = slamimp / (1- 0.105799354866935);}
			else if (xmimpflag == 134 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null){slamimp =225.932515337423;}
			else if (xmimpflag == 134 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null){slaximp =225.932515337423/ (1-0.105799354866935 );}
			
			if (xmimpflag == 135 && slaximp >= 1 && slaarriv==null && slastot==null){slamimp = slaximp - (slaximp*0.114160782328086);}
			else if (xmimpflag == 135 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null){slaximp = slamimp / (1- 0.114160782328086);}
			else if (xmimpflag == 135 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null){slamimp =391.452674897119;}
			else if (xmimpflag == 135 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null){slaximp =391.452674897119/ (1-0.114160782328086 );}
			
			if (xmimpflag == 136 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.170755559662484);}
			else if (xmimpflag == 136 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.170755559662484);}
			else if (xmimpflag == 136 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =480.734042553191;}
			else if (xmimpflag == 136 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =480.734042553191/ (1-0.170755559662484 );}
			
			if (xmimpflag == 101 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.142415261804064);}
			else if (xmimpflag == 101 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.142415261804064);}
			else if (xmimpflag == 101 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =163.80243902439;}
			else if (xmimpflag == 101 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =163.80243902439/ (1-0.142415261804064 );}
			
			if (xmimpflag == 102 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.104951847967976);}
			else if (xmimpflag == 102 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.104951847967976);}
			else if (xmimpflag == 102 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =153.265497076023;}
			else if (xmimpflag == 102 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =153.265497076023/ (1-0.104951847967976 );}
			
			if (xmimpflag == 103 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.0794334443169517);}
			else if (xmimpflag == 103 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.0794334443169517);}
			else if (xmimpflag == 103 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =138.094017094017;}
			else if (xmimpflag == 103 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =138.094017094017/ (1-0.0794334443169517 );}
			
			if (xmimpflag == 104 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.125269157905197);}
			else if (xmimpflag == 104 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.125269157905197);}
			else if (xmimpflag == 104 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =107.64;}
			else if (xmimpflag == 104 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =107.64 - (107.64*0.125269157905197 );}
			
			if (xmimpflag == 105 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.0887057111704602);}
			else if (xmimpflag == 105 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.0887057111704602);}
			else if (xmimpflag == 105 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =191.988789237668;}
			else if (xmimpflag == 105 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =191.988789237668/ (1-0.0887057111704602 );}
			
			if (xmimpflag == 106 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.0985396051230542);}
			else if (xmimpflag == 106 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.0985396051230542);}
			else if (xmimpflag == 106 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =188.140969162996;}
			else if (xmimpflag == 106 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =188.140969162996/ (1-0.0985396051230542 );}
			
			if (xmimpflag == 107 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.199714956235816);}
			else if (xmimpflag == 107 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.199714956235816);}
			else if (xmimpflag == 107 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =239.363636363636;}
			else if (xmimpflag == 107 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =239.363636363636/ (1-0.199714956235816 );}
			
			if (xmimpflag == 108 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.116764553914052);}
			else if (xmimpflag == 108 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.116764553914052);}
			else if (xmimpflag == 108 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =241.066480055983;}
			else if (xmimpflag == 108 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =241.066480055983/ (1-0.116764553914052 );}
			
			if (xmimpflag == 110 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.217817105373686);}
			else if (xmimpflag == 110 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.217817105373686);}
			else if (xmimpflag == 110 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =321.139784946236;}
			else if (xmimpflag == 110 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =321.139784946236/ (1-0.217817105373686 );}
			
			if (xmimpflag == 111 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.134584278813695);}
			else if (xmimpflag == 111 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.134584278813695);}
			else if (xmimpflag == 111 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =320.396527777777;}
			else if (xmimpflag == 111 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =320.396527777777/ (1-0.134584278813695 );}
			
			if (xmimpflag == 112 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.0649564900465187);}
			else if (xmimpflag == 112 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.0649564900465187);}
			else if (xmimpflag == 112 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =302.919243986254;}
			else if (xmimpflag == 112 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =302.919243986254/ (1-0.0649564900465187 );}
			
			if (xmimpflag == 113 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.294943293777566);}
			else if (xmimpflag == 113 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.294943293777566);}
			else if (xmimpflag == 113 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =178.191780821918;}
			else if (xmimpflag == 113 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =178.191780821918/ (1-0.294943293777566 );}
			
			if (xmimpflag == 114 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.190466263797331);}
			else if (xmimpflag == 114 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.190466263797331);}
			else if (xmimpflag == 114 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =268.709993468321;}
			else if (xmimpflag == 114 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =268.709993468321/ (1-0.190466263797331 );}
			
			if (xmimpflag == 115 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.165262209695588);}
			else if (xmimpflag == 115 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.165262209695588);}
			else if (xmimpflag == 115 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =265.480215827338;}
			else if (xmimpflag == 115 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =265.480215827338/ (1-0.165262209695588 );}
			
			if (xmimpflag == 116 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.250590294065011);}
			else if (xmimpflag == 116 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.250590294065011);}
			else if (xmimpflag == 116 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =216.026607538803;}
			else if (xmimpflag == 116 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =216.026607538803/ (1-0.250590294065011 );}
			
			if (xmimpflag == 117 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.0862116624182079);}
			else if (xmimpflag == 117 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.0862116624182079);}
			else if (xmimpflag == 117 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =341.979498861048;}
			else if (xmimpflag == 117 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =341.979498861048/ (1-0.0862116624182079 );}
			
			if (xmimpflag == 118 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.0795782666543268);}
			else if (xmimpflag == 118 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.0795782666543268);}
			else if (xmimpflag == 118 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =382.444580777097;}
			else if (xmimpflag == 118 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =382.444580777097/ (1-0.0795782666543268 );}
			
			if (xmimpflag == 120 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.100542298212489);}
			else if (xmimpflag == 120 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.100542298212489);}
			else if (xmimpflag == 120 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =191.62583518931;}
			else if (xmimpflag == 120 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =191.62583518931/ (1-0.100542298212489 );}
			
			if (xmimpflag == 121 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.0690791392436498);}
			else if (xmimpflag == 121 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.0690791392436498);}
			else if (xmimpflag == 121 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =162.041666666667;}
			else if (xmimpflag == 121 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =162.041666666667/ (1-0.0690791392436498 );}
			
			if (xmimpflag == 122 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.274602006426542);}
			else if (xmimpflag == 122 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.274602006426542);}
			else if (xmimpflag == 122 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =173.454545454545;}
			else if (xmimpflag == 122 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =173.454545454545/ (1-0.274602006426542 );}
			
			if (xmimpflag == 123 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.274602006426542);}
			else if (xmimpflag == 123 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.274602006426542);}
			else if (xmimpflag == 123 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =255.028571428571;}
			else if (xmimpflag == 123 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =255.028571428571/ (1-0.274602006426542 );}
			
			if (xmimpflag == 124 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.181330570603409);}
			else if (xmimpflag == 124 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.181330570603409);}
			else if (xmimpflag == 124 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =447.532008830022;}
			else if (xmimpflag == 124 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =447.532008830022/ (1-0.181330570603409 );}
			
			if (xmimpflag == 1 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.255634697158707);}
			else if (xmimpflag == 1 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.255634697158707);}
			else if (xmimpflag == 1 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =166.401374570447;}
			else if (xmimpflag == 1 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =166.401374570447/ (1-0.255634697158707 );}
			
			if (xmimpflag == 2 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.173114449095158);}
			else if (xmimpflag == 2 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.173114449095158);}
			else if (xmimpflag == 2 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =152.863945578231;}
			else if (xmimpflag == 2 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =152.863945578231/ (1-0.173114449095158 );}
			
			if (xmimpflag == 3 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.191426939591589);}
			else if (xmimpflag == 3 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.191426939591589);}
			else if (xmimpflag == 3 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =250.179245283019;}
			else if (xmimpflag == 3 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =250.179245283019/ (1-0.191426939591589 );}
			
			if (xmimpflag == 4 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.143739162059858);}
			else if (xmimpflag == 4 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.143739162059858);}
			else if (xmimpflag == 4 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =273.896226415094;}
			else if (xmimpflag == 4 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =273.896226415094 - (273.896226415094*0.143739162059858 );}
			
			if (xmimpflag == 5 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.0703329947332674);}
			else if (xmimpflag == 5 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.0703329947332674);}
			else if (xmimpflag == 5 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =380.04854368932;}
			else if (xmimpflag == 5 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =380.04854368932 - (380.04854368932*0.0703329947332674 );}
			
			if (xmimpflag == 6 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.117444418143106);}
			else if (xmimpflag == 6 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.117444418143106);}
			else if (xmimpflag == 6 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =305.868020304568;}
			else if (xmimpflag == 6 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =305.868020304568/ (1-0.117444418143106 );}
			
			if (xmimpflag == 7 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.126779394689057);}
			else if (xmimpflag == 7 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.126779394689057);}
			else if (xmimpflag == 7 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =265.88;}
			else if (xmimpflag == 7 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =265.88 - (265.88*0.126779394689057 );}
			
			if (xmimpflag == 8 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.189011301766662);}
			else if (xmimpflag == 8 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.189011301766662);}
			else if (xmimpflag == 8 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =281.325;}
			else if (xmimpflag == 8 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =281.325/ (1-0.189011301766662 );}
			
			if (xmimpflag == 9 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.140365224720275);}
			else if (xmimpflag == 9 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.140365224720275);}
			else if (xmimpflag == 9 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =402.502202643172;}
			else if (xmimpflag == 9 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =402.502202643172/ (1-0.140365224720275 );}
			
			if (xmimpflag == 10 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.107188743129005);}
			else if (xmimpflag == 10 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.107188743129005);}
			else if (xmimpflag == 10 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =277.059842519684;}
			else if (xmimpflag == 10 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =277.059842519684/ (1-0.107188743129005 );}
			
			if (xmimpflag == 11 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.126901348540731);}
			else if (xmimpflag == 11 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.126901348540731);}
			else if (xmimpflag == 11 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =355.810945273632;}
			else if (xmimpflag == 11 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =355.810945273632/ (1-0.126901348540731 );}
			
			if (xmimpflag == 12 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.0655772248600899);}
			else if (xmimpflag == 12 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.0655772248600899);}
			else if (xmimpflag == 12 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =309.533898305085;}
			else if (xmimpflag == 12 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =309.533898305085/ (1-0.0655772248600899 );}
			
			if (xmimpflag == 13 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.0778021073375869);}
			else if (xmimpflag == 13 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.0778021073375869);}
			else if (xmimpflag == 13 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =305.812154696132;}
			else if (xmimpflag == 13 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =305.812154696132/ (1-0.0778021073375869 );}
			
			if (xmimpflag == 14 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.0654921908875572);}
			else if (xmimpflag == 14 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.0654921908875572);}
			else if (xmimpflag == 14 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =285.054112554113;}
			else if (xmimpflag == 14 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =285.054112554113/ (1-0.0654921908875572 );}
			
			if (xmimpflag == 15 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.0671696102131247);}
			else if (xmimpflag == 15 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.0671696102131247);}
			else if (xmimpflag == 15 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =361.638059701493;}
			else if (xmimpflag == 15 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =361.638059701493/ (1-0.0671696102131247 );}
			
			if (xmimpflag == 16 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.371414750110571);}
			else if (xmimpflag == 16 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.371414750110571);}
			else if (xmimpflag == 16 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =239.9;}
			else if (xmimpflag == 16 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =239.9/ (1-0.371414750110571 );}
			
			if (xmimpflag == 157 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.230610260687796);}
			else if (xmimpflag == 157 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.230610260687796);}
			else if (xmimpflag == 157 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =139.029411764706;}
			else if (xmimpflag == 157 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =139.029411764706/ (1-0.230610260687796 );}
			
			if (xmimpflag == 159 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.154487726688789);}
			else if (xmimpflag == 159 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.154487726688789);}
			else if (xmimpflag == 159 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =245.12676056338;}
			else if (xmimpflag == 159 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =245.12676056338/ (1-0.154487726688789 );}
			
			if (xmimpflag == 99 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.166050441674744);}
			else if (xmimpflag == 99 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.166050441674744);}
			else if (xmimpflag == 99 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =125.619750283768;}
			else if (xmimpflag == 99 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =125.619750283768/ (1-0.166050441674744 );}
			
			if (xmimpflag == 100 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.178717812379779);}
			else if (xmimpflag == 100 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.178717812379779);}
			else if (xmimpflag == 100 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =565.645161290322;}
			else if (xmimpflag == 100 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =565.645161290322/ (1-0.178717812379779 );}
			
			if (xmimpflag == 17 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.0557746478873239);}
			else if (xmimpflag == 17 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.0557746478873239);}
			else if (xmimpflag == 17 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =148.882352941176;}
			else if (xmimpflag == 17 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =148.882352941176/ (1-0.0557746478873239 );}
			
			if (xmimpflag == 98 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.126563817175912);}
			else if (xmimpflag == 98 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.126563817175912);}
			else if (xmimpflag == 98 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =132.596685082873;}
			else if (xmimpflag == 98 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =132.596685082873/ (1-0.126563817175912 );}
			
			if (xmimpflag == 18 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.093544030879478);}
			else if (xmimpflag == 18 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.093544030879478);}
			else if (xmimpflag == 18 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =184.486013986014;}
			else if (xmimpflag == 18 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =184.486013986014/ (1-0.093544030879478 );}
			
			if (xmimpflag == 19 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.0985982521761244);}
			else if (xmimpflag == 19 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.0985982521761244);}
			else if (xmimpflag == 19 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =230.298469387755;}
			else if (xmimpflag == 19 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =230.298469387755/ (1-0.0985982521761244 );}
			
			if (xmimpflag == 20 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.0944678720322908);}
			else if (xmimpflag == 20 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.0944678720322908);}
			else if (xmimpflag == 20 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =444.290145985401;}
			else if (xmimpflag == 20 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =444.290145985401/ (1-0.0944678720322908 );}
			
			if (xmimpflag == 21 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.167379623404603);}
			else if (xmimpflag == 21 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.167379623404603);}
			else if (xmimpflag == 21 &&  tslavesd==null&&  tslavesp==null&&  ncartot==null&&  slaarriv==null&& slastot==null) {slamimp =492.946428571429;}
			else if (xmimpflag == 21 &&  tslavesd==null&&  tslavesp==null&&  ncartot==null&&  slaarriv==null&& slastot==null) {slaximp =492.946428571429/ (1-0.167379623404603 );}
			
			if (xmimpflag == 22 && slaximp >= 1 &&  slaarriv==null&& slastot==null) {slamimp = slaximp - (slaximp*0.183801786070534);}
			else if (xmimpflag == 22 && slamimp >= 1 &&  tslavesd==null&&  tslavesp==null&& ncartot==null) {slaximp = slamimp / (1- 0.183801786070534);}
			else if (xmimpflag == 22 &&  tslavesd==null&&  tslavesp==null&&  ncartot==null&&  slaarriv==null&& slastot==null) {slamimp =91.9594594594595;}
			else if (xmimpflag == 22 &&  tslavesd==null&&  tslavesp==null&&  ncartot==null&&  slaarriv==null&& slastot==null) {slaximp =91.9594594594595/ (1-0.183801786070534 );}
			
			if (xmimpflag == 23 && slaximp >= 1 &&  slaarriv==null&& slastot==null) {slamimp = slaximp - (slaximp*0.102358180948044);}
			else if (xmimpflag == 23 && slamimp >= 1 &&  tslavesd==null&&  tslavesp==null&& ncartot==null) {slaximp = slamimp / (1- 0.102358180948044);}
			else if (xmimpflag == 23 &&  tslavesd==null&&  tslavesp==null&&  ncartot==null&&  slaarriv==null&& slastot==null) {slamimp =95.972972972973;}
			else if (xmimpflag == 23 &&  tslavesd==null&&  tslavesp==null&&  ncartot==null&&  slaarriv==null&& slastot==null) {slaximp =95.972972972973/ (1-0.102358180948044 );}
			
			if (xmimpflag == 24 && slaximp >= 1 &&  slaarriv==null&& slastot==null) {slamimp = slaximp - (slaximp*0.122708750828674);}
			else if (xmimpflag == 24 && slamimp >= 1 &&  tslavesd==null&&  tslavesp==null&& ncartot==null) {slaximp = slamimp / (1- 0.122708750828674);}
			else if (xmimpflag == 24 &&  tslavesd==null&&  tslavesp==null&&  ncartot==null&&  slaarriv==null&& slastot==null) {slamimp =146.31;}
			else if (xmimpflag == 24 &&  tslavesd==null&&  tslavesp==null&&  ncartot==null&&  slaarriv==null&& slastot==null) {slaximp =146.31/ (1-0.122708750828674 );}
			
			if (xmimpflag == 25 && slaximp >= 1 &&  slaarriv==null&& slastot==null) {slamimp = slaximp - (slaximp*0.101742168136026);}
			else if (xmimpflag == 25 && slamimp >= 1 &&  tslavesd==null&&  tslavesp==null&& ncartot==null) {slaximp = slamimp / (1- 0.101742168136026);}
			else if (xmimpflag == 25 &&  tslavesd==null&&  tslavesp==null&&  ncartot==null&&  slaarriv==null&& slastot==null) {slamimp =279.357142857143;}
			else if (xmimpflag == 25 &&  tslavesd==null&&  tslavesp==null&&  ncartot==null&&  slaarriv==null&& slastot==null) {slaximp =279.357142857143/ (1-0.101742168136026 );}
			
			if (xmimpflag == 26 && slaximp >= 1 &&  slaarriv==null&& slastot==null) {slamimp = slaximp - (slaximp*0.0830808603000646);}
			else if (xmimpflag == 26 && slamimp >= 1 &&  tslavesd==null&&  tslavesp==null&& ncartot==null) {slaximp = slamimp / (1- 0.0830808603000646);}
			else if (xmimpflag == 26 &&  tslavesd==null&&  tslavesp==null&&  ncartot==null&&  slaarriv==null&& slastot==null) {slamimp =341.5;}
			else if (xmimpflag == 26 &&  tslavesd==null&&  tslavesp==null&&  ncartot==null&&  slaarriv==null&& slastot==null) {slaximp =341.5/ (1-0.0830808603000646 );}
			
			if (xmimpflag == 27 && slaximp >= 1 &&  slaarriv==null&& slastot==null) {slamimp = slaximp - (slaximp*0.0951735364832193);}
			else if (xmimpflag == 27 && slamimp >= 1 &&  tslavesd==null&&  tslavesp==null&& ncartot==null) {slaximp = slamimp / (1- 0.0951735364832193);}
			else if (xmimpflag == 27 &&  tslavesd==null&&  tslavesp==null&&  ncartot==null&&  slaarriv==null&& slastot==null) {slaximp =335.546666666667;}
			else if (xmimpflag == 27 &&  tslavesd==null&&  tslavesp==null&&  ncartot==null&&  slaarriv==null&& slastot==null) {slamimp =335.546666666667 - (335.546666666667*0.0951735364832193 );}
			
			if (xmimpflag == 28 && slaximp >= 1 &&  slaarriv==null&& slastot==null) {slamimp = slaximp - (slaximp*0.0599984615282753);}
			else if (xmimpflag == 28 && slamimp >= 1 &&  tslavesd==null&&  tslavesp==null&& ncartot==null) {slaximp = slamimp / (1- 0.0599984615282753);}
			else if (xmimpflag == 28 &&  tslavesd==null&&  tslavesp==null&&  ncartot==null&&  slaarriv==null&& slastot==null) {slaximp =348.926267281106;}
			else if (xmimpflag == 28 &&  tslavesd==null&&  tslavesp==null&&  ncartot==null&&  slaarriv==null&& slastot==null) {slamimp =348.926267281106 - (348.926267281106*0.0599984615282753 );}
			
			if (xmimpflag == 29 && slaximp >= 1 &&  slaarriv==null&& slastot==null) {slamimp = slaximp - (slaximp*0.0849037398486349);}
			else if (xmimpflag == 29 && slamimp >= 1 &&  tslavesd==null&&  tslavesp==null&& ncartot==null) {slaximp = slamimp / (1- 0.0849037398486349);}
			else if (xmimpflag == 29 &&  tslavesd==null&&  tslavesp==null&&  ncartot==null&&  slaarriv==null&& slastot==null) {slamimp =323.539358600583;}
			else if (xmimpflag == 29 &&  tslavesd==null&&  tslavesp==null&&  ncartot==null&&  slaarriv==null&& slastot==null) {slaximp =323.539358600583/ (1-0.0849037398486349 );}
			
			if (xmimpflag == 30 && slaximp >= 1 &&  slaarriv==null&& slastot==null) {slamimp = slaximp - (slaximp*0.0831292966753462);}
			else if (xmimpflag == 30 && slamimp >= 1 &&  tslavesd==null&&  tslavesp==null&& ncartot==null) {slaximp = slamimp / (1- 0.0831292966753462);}
			else if (xmimpflag == 30 &&  tslavesd==null&&  tslavesp==null&&  ncartot==null&&  slaarriv==null&& slastot==null) {slamimp =435.738461538461;}
			else if (xmimpflag == 30 &&  tslavesd==null&&  tslavesp==null&&  ncartot==null&&  slaarriv==null&& slastot==null) {slaximp =435.738461538461/ (1-0.0831292966753462 );}
			
			if (xmimpflag == 31 && slaximp >= 1 &&  slaarriv==null&& slastot==null) {slamimp = slaximp - (slaximp*0.154603810637904);}
			else if (xmimpflag == 31 && slamimp >= 1 &&  tslavesd==null&&  tslavesp==null&& ncartot==null) {slaximp = slamimp / (1- 0.154603810637904);}
			else if (xmimpflag == 31 &&  tslavesd==null&&  tslavesp==null&&  ncartot==null&&  slaarriv==null&& slastot==null) {slamimp =221.279220779221;}
			else if (xmimpflag == 31 &&  tslavesd==null&&  tslavesp==null&&  ncartot==null&&  slaarriv==null&& slastot==null) {slaximp =221.279220779221/ (1-0.154603810637904 );}
			
			if (xmimpflag == 32 && slaximp >= 1 &&  slaarriv==null&& slastot==null) {slamimp = slaximp - (slaximp*0.169381440464976);}
			else if (xmimpflag == 32 && slamimp >= 1 &&  tslavesd==null&&  tslavesp==null&& ncartot==null) {slaximp = slamimp / (1- 0.169381440464976);}
			else if (xmimpflag == 32 &&  tslavesd==null&&  tslavesp==null&&  ncartot==null&&  slaarriv==null&& slastot==null) {slamimp =296.593103448276;}
			else if (xmimpflag == 32 &&  tslavesd==null&&  tslavesp==null&&  ncartot==null&&  slaarriv==null&& slastot==null) {slaximp =296.593103448276/ (1-0.169381440464976 );}
			
			if (xmimpflag == 33 && slaximp >= 1 &&  slaarriv==null&& slastot==null) {slamimp = slaximp - (slaximp*0.183684529291394);}
			else if (xmimpflag == 33 && slamimp >= 1 &&  tslavesd==null&&  tslavesp==null&& ncartot==null) {slaximp = slamimp / (1- 0.183684529291394);}
			else if (xmimpflag == 33 &&  tslavesd==null&&  tslavesp==null&&  ncartot==null&&  slaarriv==null&& slastot==null) {slamimp =281.452966714906;}
			else if (xmimpflag == 33 &&  tslavesd==null&&  tslavesp==null&&  ncartot==null&&  slaarriv==null&& slastot==null) {slaximp =281.452966714906/ (1-0.183684529291394 );}
			
			if (xmimpflag == 34 && slaximp >= 1 &&  slaarriv==null&& slastot==null) {slamimp = slaximp - (slaximp*0.0864964921326426);}
			else if (xmimpflag == 34 && slamimp >= 1 &&  tslavesd==null&&  tslavesp==null&& ncartot==null) {slaximp = slamimp / (1- 0.0864964921326426);}
			else if (xmimpflag == 34 &&  tslavesd==null&&  tslavesp==null&&  ncartot==null&&  slaarriv==null&& slastot==null) {slamimp =325.652360515021;}
			else if (xmimpflag == 34 &&  tslavesd==null&&  tslavesp==null&&  ncartot==null&&  slaarriv==null&& slastot==null) {slaximp =325.652360515021/ (1-0.0864964921326426 );}
			
			if (xmimpflag ==  35 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.176037224384829);}
			else if (xmimpflag == 35 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.176037224384829);}
			else if (xmimpflag ==  35 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =272.474358974359;}
			else if (xmimpflag ==  35 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =272.474358974359/ (1-0.176037224384829 );}
			
			if (xmimpflag ==  36 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.116937605450612);}
			else if (xmimpflag ==  36 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.116937605450612);}
			else if (xmimpflag ==  36 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =556.677419354839;}
			else if (xmimpflag ==  36 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =556.677419354839/ (1-0.116937605450612 );}
			
			if (xmimpflag ==  37 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.172812495199871);}
			else if (xmimpflag ==  37 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.172812495199871);}
			else if (xmimpflag ==  37 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =890.470588235294;}
			else if (xmimpflag ==  37 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =890.470588235294/ (1-0.172812495199871 );}
			
			if (xmimpflag ==  38 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.105087524949968);}
			else if (xmimpflag ==  38 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.105087524949968);}
			else if (xmimpflag ==  38 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =335.813953488372;}
			else if (xmimpflag ==  38 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =335.813953488372/ (1-0.105087524949968 );}
			
			if (xmimpflag ==  39 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.0856667000685018);}
			else if (xmimpflag ==  39 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.0856667000685018);}
			else if (xmimpflag ==  39 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =257.263157894737;}
			else if (xmimpflag ==  39 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =257.263157894737/ (1-0.0856667000685018 );}
			
			if (xmimpflag ==  40 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.0865650987499053);}
			else if (xmimpflag ==  40 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.0865650987499053);}
			else if (xmimpflag ==  40 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =328.195266272189;}
			else if (xmimpflag ==  40 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =328.195266272189/ (1-0.0865650987499053 );}
			
			if (xmimpflag ==  41 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.171814252005436);}
			else if (xmimpflag ==  41 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.171814252005436);}
			else if (xmimpflag ==  41 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =129.145454545455;}
			else if (xmimpflag ==  41 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =129.145454545455/ (1-0.171814252005436 );}
			
			if (xmimpflag ==  42 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.0610387045813586);}
			else if (xmimpflag ==  42 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.0610387045813586);}
			else if (xmimpflag ==  42 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =158.1;}
			else if (xmimpflag ==  42 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =158.1/ (1-0.0610387045813586 );}
			
			if (xmimpflag ==  43 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.159823459162871);}
			else if (xmimpflag ==  43 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.159823459162871);}
			else if (xmimpflag ==  43 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =247.759689922481;}
			else if (xmimpflag ==  43 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =247.759689922481/ (1-0.159823459162871 );}
			
			if (xmimpflag ==  44 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.0988853555387519);}
			else if (xmimpflag ==  44 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.0988853555387519);}
			else if (xmimpflag ==  44 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =363d;}
			else if (xmimpflag ==  44 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =363/ (1-0.0988853555387519 );}
			
			if (xmimpflag ==  45 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.0904513085721602);}
			else if (xmimpflag ==  45 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.0904513085721602);}
			else if (xmimpflag ==  45 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =466.25641025641;}
			else if (xmimpflag ==  45 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =466.25641025641/ (1-0.0904513085721602 );}
			
			if (xmimpflag ==  46 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.082310278477633);}
			else if (xmimpflag ==  46 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.082310278477633);}
			else if (xmimpflag ==  46 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =159.810810810811;}
			else if (xmimpflag ==  46 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =159.810810810811/ (1-0.082310278477633 );}
			
			if (xmimpflag ==  47 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.104714300552102);}
			else if (xmimpflag ==  47 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.104714300552102);}
			else if (xmimpflag ==  47 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =638.25;}
			else if (xmimpflag ==  47 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =638.25/ (1-0.104714300552102 );}
			
			if (xmimpflag ==  48 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.193439630544956);}
			else if (xmimpflag ==  48 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.193439630544956);}
			else if (xmimpflag ==  48 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =608.392156862745;}
			else if (xmimpflag ==  48 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =608.392156862745/ (1-0.193439630544956 );}
			
			if (xmimpflag ==  49 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.145583038352611);}
			else if (xmimpflag ==  49 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.145583038352611);}
			else if (xmimpflag ==  49 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =428.888888888889;}
			else if (xmimpflag ==  49 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =428.888888888889/ (1-0.145583038352611 );}
			
			if (xmimpflag ==  50 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.233333333333333);}
			else if (xmimpflag ==  50 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.233333333333333);}
			else if (xmimpflag ==  50 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =270.846153846154;}
			else if (xmimpflag ==  50 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =270.846153846154/ (1-0.233333333333333 );}
			
			if (xmimpflag ==  51 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.179223522528989);}
			else if (xmimpflag ==  51 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.179223522528989);}
			else if (xmimpflag ==  51 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =229.64;}
			else if (xmimpflag ==  51 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =229.64/ (1-0.179223522528989 );}
			
			if (xmimpflag ==  52 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.0819156347249732);}
			else if (xmimpflag ==  52 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.0819156347249732);}
			else if (xmimpflag ==  52 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =290.164383561644;}
			else if (xmimpflag ==  52 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =290.164383561644 - (290.164383561644*0.0819156347249732 );}
			
			if (xmimpflag ==  53 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.0540922242825536);}
			else if (xmimpflag ==  53 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.0540922242825536);}
			else if (xmimpflag ==  53 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =256.548387096774;}
			else if (xmimpflag ==  53 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =256.548387096774/ (1-0.0540922242825536 );}
			
			if (xmimpflag ==  54 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.0913651933726713);}
			else if (xmimpflag ==  54 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.0913651933726713);}
			else if (xmimpflag ==  54 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =216.907894736842;}
			else if (xmimpflag ==  54 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =216.907894736842/ (1-0.0913651933726713 );}
			
			if (xmimpflag ==  55 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.0604022380426763);}
			else if (xmimpflag ==  55 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.0604022380426763);}
			else if (xmimpflag ==  55 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =241.461538461538;}
			else if (xmimpflag ==  55 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =241.461538461538/ (1-0.0604022380426763 );}
			
			if (xmimpflag ==  56 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.0542026549646127);}
			else if (xmimpflag ==  56 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.0542026549646127);}
			else if (xmimpflag ==  56 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =340.230769230769;}
			else if (xmimpflag ==  56 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =340.230769230769/ (1-0.0542026549646127 );}
			
			if (xmimpflag ==  57 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.0974564330758702);}
			else if (xmimpflag ==  57 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.0974564330758702);}
			else if (xmimpflag ==  57 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =516.45;}
			else if (xmimpflag ==  57 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =516.45/ (1-0.0974564330758702 );}
			
			if (xmimpflag ==  58 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.162886379968412);}
			else if (xmimpflag ==  58 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.162886379968412);}
			else if (xmimpflag ==  58 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =447.518072289157;}
			else if (xmimpflag ==  58 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =447.518072289157 - (447.518072289157*0.162886379968412 );}
			
			if (xmimpflag ==  59 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.0561646667118922);}
			else if (xmimpflag ==  59 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.0561646667118922);}
			else if (xmimpflag ==  59 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =152.923076923077;}
			else if (xmimpflag ==  59 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =152.923076923077/ (1-0.0561646667118922 );}
			
			if (xmimpflag ==  60 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.133468501803896);}
			else if (xmimpflag ==  60 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.133468501803896);}
			else if (xmimpflag ==  60 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =403.292993630573;}
			else if (xmimpflag ==  60 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =403.292993630573/ (1-0.133468501803896 );}
			
			if (xmimpflag ==  61 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.106708705390018);}
			else if (xmimpflag ==  61 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.106708705390018);}
			else if (xmimpflag ==  61 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =285.644444444444;}
			else if (xmimpflag ==  61 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =285.644444444444/ (1-0.106708705390018 );}
			
			if (xmimpflag ==  62 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.0785278768682708);}
			else if (xmimpflag ==  62 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.0785278768682708);}
			else if (xmimpflag ==  62 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =335.658227848101;}
			else if (xmimpflag ==  62 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =335.658227848101/ (1-0.0785278768682708 );}
			
			if (xmimpflag ==  63 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.107782269167156);}
			else if (xmimpflag ==  63 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.107782269167156);}
			else if (xmimpflag ==  63 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =472.267857142857;}
			else if (xmimpflag ==  63 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =472.267857142857/ (1-0.107782269167156 );}
			
			if (xmimpflag ==  160 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.0779281672325541);}
			else if (xmimpflag ==  160 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.0779281672325541);}
			else if (xmimpflag ==  160 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =536.842857142857;}
			else if (xmimpflag ==  160 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =536.842857142857/ (1-0.0779281672325541 );}
			
			if (xmimpflag ==  65 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.115409873680179);}
			else if (xmimpflag ==  65 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.115409873680179);}
			else if (xmimpflag ==  65 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =103.376146788991;}
			else if (xmimpflag ==  65 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =103.376146788991/ (1-0.115409873680179 );}
			
			if (xmimpflag ==  66 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.207088877726936);}
			else if (xmimpflag ==  66 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.207088877726936);}
			else if (xmimpflag ==  66 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =68.1506849315068;}
			else if (xmimpflag ==  66 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =68.1506849315068/ (1-0.207088877726936 );}
			
			if (xmimpflag ==  67 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.110922605367631);}
			else if (xmimpflag ==  67 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.110922605367631);}
			else if (xmimpflag ==  67 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =80.0491803278688;}
			else if (xmimpflag ==  67 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =80.0491803278688/ (1-0.110922605367631 );}
			
			if (xmimpflag ==  68 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.127935729778166);}
			else if (xmimpflag ==  68 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.127935729778166);}
			else if (xmimpflag ==  68 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =84d;}
			else if (xmimpflag ==  68 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =84 - (84*0.127935729778166 );}
			
			if (xmimpflag ==  69 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.206358225584424);}
			else if (xmimpflag ==  69 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.206358225584424);}
			else if (xmimpflag == 69 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =1004.47058823529;}
			else if (xmimpflag == 69 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =1004.47058823529/ (1-0.206358225584424 );}
			
			if (xmimpflag == 70 && slaximp >= 1 &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.142775407154303);}
			else if (xmimpflag == 70 && slamimp >= 1 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.142775407154303);}
			else if (xmimpflag == 70 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =311.222222222222;}
			else if (xmimpflag == 70 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =311.222222222222/ (1-0.142775407154303 );}
			
			if (xmimpflag == 71 && slaximp >= 1 &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.106323148232566);}
			else if (xmimpflag == 71 && slamimp >= 1 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.106323148232566);}
			else if (xmimpflag == 71 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =310.39837398374;}
			else if (xmimpflag == 71 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =310.39837398374/ (1-0.106323148232566 );}
			
			if (xmimpflag == 97 && slaximp >= 1 &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.138965456634756);}
			else if (xmimpflag == 97 && slamimp >= 1 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.138965456634756);}
			else if (xmimpflag == 97 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =259.21875;}
			else if (xmimpflag == 97 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =259.21875/ (1-0.138965456634756 );}
			
			if (xmimpflag == 72 && slaximp >= 1 &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.169436742362705);}
			else if (xmimpflag == 72 && slamimp >= 1 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.169436742362705);}
			else if (xmimpflag == 72 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =265.325842696629;}
			else if (xmimpflag == 72 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =265.325842696629/ (1-0.169436742362705 );}
			
			if (xmimpflag == 85 && slaximp >= 1 &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.339905284604731);}
			else if (xmimpflag == 85 && slamimp >= 1 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.339905284604731);}
			else if (xmimpflag == 85 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =563.333333333333;}
			else if (xmimpflag == 85 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =563.333333333333/ (1-0.339905284604731 );}
			
			if (xmimpflag == 73 && slaximp >= 1 &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.129605450439467);}
			else if (xmimpflag == 73 && slamimp >= 1 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.129605450439467);}
			else if (xmimpflag == 73 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =407.289473684211;}
			else if (xmimpflag == 73 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =407.289473684211/ (1-0.129605450439467 );}
			
			if (xmimpflag == 74 && slaximp >= 1 &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.0794384325299229);}
			else if (xmimpflag == 74 && slamimp >= 1 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.0794384325299229);}
			else if (xmimpflag == 74 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =117.137931034483;}
			else if (xmimpflag == 74 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =117.137931034483/ (1-0.0794384325299229 );}
			
			if (xmimpflag == 75 && slaximp >= 1 &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.189369734252207);}
			else if (xmimpflag == 75 && slamimp >= 1 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.189369734252207);}
			else if (xmimpflag == 75 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =192.772020725389;}
			else if (xmimpflag == 75 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =192.772020725389/ (1-0.189369734252207 );}
			
			if (xmimpflag == 76 && slaximp >= 1 &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.131187789757565);}
			else if (xmimpflag == 76 && slamimp >= 1 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.131187789757565);}
			else if (xmimpflag == 76 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =199.041666666667;}
			else if (xmimpflag == 76 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =199.041666666667/ (1-0.131187789757565 );}
			
			if (xmimpflag == 77 && slaximp >= 1 &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.136342992788614);}
			else if (xmimpflag == 77 && slamimp >= 1 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.136342992788614);}
			else if (xmimpflag == 77 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =186.407894736842;}
			else if (xmimpflag == 77 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =186.407894736842/ (1-0.136342992788614 );}
			
			if (xmimpflag == 78 && slaximp >= 1 &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.103049659988616);}
			else if (xmimpflag == 78 && slamimp >= 1 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.103049659988616);}
			else if (xmimpflag == 78 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =155.470588235294;}
			else if (xmimpflag == 78 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =155.470588235294/ (1-0.103049659988616 );}
			
			if (xmimpflag == 79 && slaximp >= 1 &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.35);}
			else if (xmimpflag == 79 && slamimp >= 1 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.35);}
			else if (xmimpflag == 79 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =193.74358974359;}
			else if (xmimpflag == 79 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =193.74358974359/ (1-0.35 );}
			
			if (xmimpflag == 80 && slaximp >= 1 &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.0732085200996002);}
			else if (xmimpflag == 80 && slamimp >= 1 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.0732085200996002);}
			else if (xmimpflag == 80 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =249.692307692308;}
			else if (xmimpflag == 80 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =249.692307692308/ (1-0.0732085200996002 );}
			
			if (xmimpflag == 81 && slaximp >= 1 &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.0934359066589073);}
			else if (xmimpflag == 81 && slamimp >= 1 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.0934359066589073);}
			else if (xmimpflag == 81 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =352.952806122449;}
			else if (xmimpflag == 81 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =352.952806122449/ (1-0.0934359066589073 );}
			
			if (xmimpflag == 82 && slaximp >= 1 &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.07182740558555);}
			else if (xmimpflag == 82 && slamimp >= 1 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.07182740558555);}
			else if (xmimpflag == 82 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =419.619047619047;}
			else if (xmimpflag == 82 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =419.619047619047/ (1-0.07182740558555 );}
			
			if (xmimpflag == 83 && slaximp >= 1 &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.0956449943871365);}
			else if (xmimpflag == 83 && slamimp >= 1 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.0956449943871365);}
			else if (xmimpflag == 83 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =304.5625;}
			else if (xmimpflag == 83 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =304.5625 - (304.5625*0.0956449943871365 );}
			
			if (xmimpflag == 84 && slaximp >= 1 &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.163929225997462);}
			else if (xmimpflag == 84 && slamimp >= 1 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.163929225997462);}
			else if (xmimpflag == 84 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =319.285714285714;}
			else if (xmimpflag == 84 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =319.285714285714/ (1-0.163929225997462 );}
			
			if (xmimpflag == 86 && slaximp >= 1 &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.112733293827202);}
			else if (xmimpflag == 86 && slamimp >= 1 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.112733293827202);}
			else if (xmimpflag == 86 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =129.277777777778;}
			else if (xmimpflag == 86 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =129.277777777778/ (1-0.112733293827202 );}
			
			if (xmimpflag == 87 && slaximp >= 1 &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.0655504344628028);}
			else if (xmimpflag == 87 && slamimp >= 1 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.0655504344628028);}
			else if (xmimpflag == 87 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =211d;}
			else if (xmimpflag == 87 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =211/ (1-0.0655504344628028 );}
			
			if (xmimpflag == 88 && slaximp >= 1 &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.198929221794951);}
			else if (xmimpflag == 88 && slamimp >= 1 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.198929221794951);}
			else if (xmimpflag == 88 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =296.473684210526;}
			else if (xmimpflag == 88 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =296.473684210526 - (296.473684210526*0.198929221794951 );}
			
			if (xmimpflag == 89 && slaximp >= 1 &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.107517933823928);}
			else if (xmimpflag == 89 && slamimp >= 1 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.107517933823928);}
			else if (xmimpflag == 89 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =281.958333333333;}
			else if (xmimpflag == 89 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =281.958333333333/ (1-0.107517933823928 );}
			
			if (xmimpflag == 90 && slaximp >= 1 &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.028250184258012);}
			else if (xmimpflag == 90 && slamimp >= 1 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.028250184258012);}
			else if (xmimpflag == 90 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =208.341176470588;}
			else if (xmimpflag == 90 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =208.341176470588/ (1-0.028250184258012 );}
			
			if (xmimpflag == 91 && slaximp >= 1 &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.0487771272192143);}
			else if (xmimpflag == 91 && slamimp >= 1 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.0487771272192143);}
			else if (xmimpflag == 91 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =267.896551724138;}
			else if (xmimpflag == 91 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =267.896551724138/ (1-0.0487771272192143 );}
			
			if (xmimpflag == 92 && slaximp >= 1 &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.111975986975987);}
			else if (xmimpflag == 92 && slamimp >= 1 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.111975986975987);}
			else if (xmimpflag == 92 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =328.555555555556;}
			else if (xmimpflag == 92 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =328.555555555556/ (1-0.111975986975987 );}
			
			if (xmimpflag == 93 && slaximp >= 1 &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.0979648763988006);}
			else if (xmimpflag == 93 && slamimp >= 1 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.0979648763988006);}
			else if (xmimpflag == 93 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =101.111111111111;}
			else if (xmimpflag == 93 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =101.111111111111/ (1-0.0979648763988006 );}
			
			if (xmimpflag == 94 && slaximp >= 1 &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.297737659966491);}
			else if (xmimpflag == 94 && slamimp >= 1 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.297737659966491);}
			else if (xmimpflag == 94 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =319.733333333333;}
			else if (xmimpflag == 94 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =319.733333333333/ (1-0.297737659966491 );}
			
			if (xmimpflag == 95 && slaximp >= 1 &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.0220048899755501);}
			else if (xmimpflag == 95 && slamimp >= 1 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.0220048899755501);}
			else if (xmimpflag == 95 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =220.428571428571;}
			else if (xmimpflag == 95 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =220.428571428571/ (1-0.0220048899755501 );}
			
			if (xmimpflag == 96 && slaximp >= 1 &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0);}
			else if (xmimpflag == 96 && slamimp >= 1 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0);}
			else if (xmimpflag == 96 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =433d;}
			else if (xmimpflag == 96 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =433d/ (1-0 );}
			
			if (xmimpflag == 137 && slaximp >= 1 &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.12659407459354);}
			else if (xmimpflag == 137 && slamimp >= 1 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.12659407459354);}
			else if (xmimpflag == 137 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =104.986301369863;}
			else if (xmimpflag == 137 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =104.986301369863/ (1-0.12659407459354 );}
			
			if (xmimpflag == 138 && slaximp >= 1 &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.179201806454531);}
			else if (xmimpflag == 138 && slamimp >= 1 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.179201806454531);}
			else if (xmimpflag == 138 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =108.37037037037;}
			else if (xmimpflag == 138 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =108.37037037037 - (108.37037037037*0.179201806454531 );}
			
			if (xmimpflag == 139 && slaximp >= 1 &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.162003845923261);}
			else if (xmimpflag == 139 && slamimp >= 1 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.162003845923261);}
			else if (xmimpflag == 139 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =128.438775510204;}
			else if (xmimpflag == 139 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =128.438775510204/ (1-0.162003845923261 );}
			
			if (xmimpflag == 140 && slaximp >= 1 &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.171264386321147);}
			else if (xmimpflag == 140 && slamimp >= 1 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.171264386321147);}
			else if (xmimpflag == 140 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =557.6;}
			else if (xmimpflag == 140 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =557.6/ (1-0.171264386321147 );}
			
			if (xmimpflag == 141 && slaximp >= 1 &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.213152374545978);}
			else if (xmimpflag == 141 && slamimp >= 1 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.213152374545978);}
			else if (xmimpflag == 141 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =74d;}
			else if (xmimpflag == 141 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =74/ (1-0.213152374545978 );}
			
			if (xmimpflag == 142 && slaximp >= 1 &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.190548809128441);}
			else if (xmimpflag == 142 && slamimp >= 1 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.190548809128441);}
			else if (xmimpflag == 142 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =80.5625;}
			else if (xmimpflag == 142 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =80.5625 - (80.5625*0.190548809128441 );}
			
			if (xmimpflag == 145 && slaximp >= 1 &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.0577485174550083);}
			else if (xmimpflag == 145 && slamimp >= 1 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.0577485174550083);}
			else if (xmimpflag == 145 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =376.928571428571;}
			else if (xmimpflag == 145 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =376.928571428571/ (1-0.0577485174550083 );}
			
			if (xmimpflag == 146 && slaximp >= 1 &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.153749295952981);}
			else if (xmimpflag == 146 && slamimp >= 1 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.153749295952981);}
			else if (xmimpflag == 146 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =154.307692307692;}
			else if (xmimpflag == 146 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =154.307692307692/ (1-0.153749295952981 );}
			
			if (xmimpflag == 147 && slaximp >= 1 &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.143606923731731);}
			else if (xmimpflag == 147 && slamimp >= 1 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.143606923731731);}
			else if (xmimpflag == 147 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =165.903225806452;}
			else if (xmimpflag == 147 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =165.903225806452 - (165.903225806452*0.143606923731731 );}
			
			if (xmimpflag == 148 && slaximp >= 1 &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.254317624200109);}
			else if (xmimpflag == 148 && slamimp >= 1 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.254317624200109);}
			else if (xmimpflag == 148 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =199.730769230769;}
			else if (xmimpflag == 148 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =199.730769230769/ (1-0.254317624200109 );}
			
			if (xmimpflag == 149 && slaximp >= 1 &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.136559928551299);}
			else if (xmimpflag == 149 && slamimp >= 1 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.136559928551299);}
			else if (xmimpflag == 149 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =1003d;}
			else if (xmimpflag == 149 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =1003/ (1-0.136559928551299 );}
			
			if (xmimpflag == 150 && slaximp >= 1 &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.182187702624498);}
			else if (xmimpflag == 150 && slamimp >= 1 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.182187702624498);}
			else if (xmimpflag == 150 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =100.090909090909;}
			else if (xmimpflag == 150 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =100.090909090909/ (1-0.182187702624498 );}
			
			if (xmimpflag == 151 && slaximp >= 1 &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.00833333333333333);}
			else if (xmimpflag == 151 && slamimp >= 1 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.00833333333333333);}
			else if (xmimpflag == 151 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =127.103448275862;}
			else if (xmimpflag == 151 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =127.103448275862/ (1-0.00833333333333333 );}
			
			if (xmimpflag == 152 && slaximp >= 1 &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.100333848361108);}
			else if (xmimpflag == 152 && slamimp >= 1 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.100333848361108);}
			else if (xmimpflag == 152 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =436.5;}
			else if (xmimpflag == 152 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =436.5/ (1-0.100333848361108 );}
			
			if (xmimpflag == 154 && slaximp >= 1 &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.235321405225611);}
			else if (xmimpflag == 154 && slamimp >= 1 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.235321405225611);}
			else if (xmimpflag == 154 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =580.060606060606;}
			else if (xmimpflag == 154 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =580.060606060606/ (1-0.235321405225611 );}
			
			if (xmimpflag == 155 && slaximp >= 1 &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.157476046121814);}
			else if (xmimpflag == 155 && slamimp >= 1 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.157476046121814);}
			else if (xmimpflag == 155 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =70.0833333333334;}
			else if (xmimpflag == 155 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =70.0833333333334/ (1-0.157476046121814 );}
			
			if (xmimpflag == 156 && slaximp >= 1 &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.17641709128796);}
			else if (xmimpflag == 156 && slamimp >= 1 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.17641709128796);}
			else if (xmimpflag == 156 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =118.333333333333;}
			else if (xmimpflag == 156 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =118.333333333333/ (1-0.17641709128796 );}


			if (fate2== 2)
			{
			    if(slaximp >= 0) {slamimp=null;}
			    if(slaximp >= 0) {slamimp=null;}
			}
			if (fate2== 4 && slamimp >=0) {slamimp=0d;}

			if (fate2 == 7 && slamimp >= 0)
			{
			   slamimp=null;
			}

			slaximp = round(slaximp);
			slamimp = round(slamimp);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Store in voyages object
		if(slaximp!=null){voyage.setSlaximp(slaximp.intValue());}
		else {voyage.setSlaximp(null);}
		
		if(slamimp!=null)
		{voyage.setSlamimp(slamimp.intValue());}
		else 
		{voyage.setSlamimp(null);}
	}
	
	/*
	 * Calculates people variables
	 */
	public void calculateValuesPeople()
	{
		//Create input variables
		Integer men1=null;
		Integer women1=null;
		Integer boy1=null;
		Integer girl1=null;
		Integer child1=null;
		Integer infant1=null;
		Integer adult1=null;
		Integer men4=null;
		Integer women4=null;
		Integer boy4=null;
		Integer girl4=null;
		Integer child4=null;
		Integer infant4=null;
		Integer adult4=null;
		Integer men5=null;
		Integer women5=null;
		Integer boy5=null;
		Integer girl5=null;
		Integer child5=null;
		Integer adult5=null;
		Integer male1=null;
		Integer female1=null;
		Integer male4=null;
		Integer female4=null;
		Integer male5=null;
		Integer female5=null;
		Integer tslavesd=null;
		Integer tslavesp=null;
		Integer men3=null;
		Integer women3=null;
		Integer boy3=null;
		Integer girl3=null;
		Integer child3=null;
		Integer infant3=null;
		Integer infantm3=null;
		Integer infantf3=null;
		Integer adult3=null;
		Integer men6=null;
		Integer women6=null;
		Integer boy6=null;
		Integer girl6=null;
		Integer child6=null;
		Integer adult6=null;
		Integer male3=null;
		Integer female3=null;
		Integer male6=null;
		Integer female6=null;
		Integer slaarriv=null;
		Integer men2=null;
		Integer women2=null;
		Integer boy2=null;
		Integer girl2=null;
		Integer child2=null;
		Integer adult2=null;
		Integer male2=null;
		Integer female2=null;
		try {
			men1 = voyage.getMen1();
			  women1 = voyage.getWomen1();
			  boy1 = voyage.getBoy1();
			  girl1 = voyage.getGirl1();
			  child1 = voyage.getChild1();
			  infant1 = voyage.getInfant1();
			  adult1 = voyage.getAdult1();
			  men4 = voyage.getMen4();
			  women4 = voyage.getWomen4();
			  boy4 = voyage.getBoy4();
			  girl4 = voyage.getGirl4();
			  child4 = voyage.getChild4();
			  infant4 = voyage.getInfant4();
			  adult4 = voyage.getAdult4();
			  men5 = voyage.getMen5();
			  women5 = voyage.getWomen5();
			  boy5 = voyage.getBoy5();
			  girl5 = voyage.getGirl5();
			  child5 = voyage.getChild5();
			  adult5 = voyage.getAdult5();
			  male1 = voyage.getMale1();
			  female1 = voyage.getFemale1();
			  male4 = voyage.getMale4();
			  female4 = voyage.getFemale4();
			  male5 = voyage.getMale5();
			  female5 = voyage.getFemale5();
			  tslavesd = voyage.getTslavesd();
			  tslavesp = voyage.getTslavesp();
			  men3 = voyage.getMen3();
			  women3 = voyage.getWomen3();
			  boy3 = voyage.getBoy3();
			  girl3 = voyage.getGirl3();
			  child3 = voyage.getChild3();
			  infant3 = voyage.getInfant3();
			  infantm3 = voyage.getInfantm3();
			  infantf3 = voyage.getInfantf3();
			  adult3 = voyage.getAdult3();
			  men6 = voyage.getMen6();
			  women6 = voyage.getWomen6();
			  boy6 = voyage.getBoy6();
			  girl6 = voyage.getGirl6();
			  child6 = voyage.getChild6();
			  adult6 = voyage.getAdult6();
			  male3 = voyage.getMale3();
			  female3 = voyage.getFemale3();
			  male6 = voyage.getMale6();
			  female6 = voyage.getFemale6();
			  slaarriv = voyage.getSlaarriv();
			  men2 = voyage.getMen2();
			  women2 = voyage.getWomen2();
			  boy2 = voyage.getBoy2();
			  girl2 = voyage.getGirl2();
			  child2 = voyage.getChild2();
			  adult2 = voyage.getAdult2();
			  male2 = voyage.getMale2();
			  female2 = voyage.getFemale2();
		} catch (Exception e) {
			e.printStackTrace();
		}
		  
		  
		  //Variables to be imputed
		  Integer adlt1imp = 0;
		  Integer chil1imp = 0;
		  Integer male1imp = 0;
		  Integer feml1imp =0;
		  Integer adlt2imp = 0;
		  Integer chil2imp = 0;
		  Integer male2imp=0;
		  Integer feml2imp=0;
		  Integer slavema1 = 0;
		  Integer slavemx1 = 0;
		  Integer slavmax1 = 0;
		  Double chilrat1=0d;
		  Double malrat1=0d;
		  Double menrat1=0d;
		  Double womrat1=0d;
		  Double boyrat1=0d;
		  Double girlrat1=0d;
		  Double adlt3imp = 0d;
		  Double chil3imp = 0d;
		  Double male3imp = 0d;
		  Double feml3imp = 0d;
		  Double slavema3=0d;
		  Double slavemx3 = 0d;
		  Double slavmax3=0d;
		  Double chilrat3=0d;
	      Double malrat3= 0d;
	      Double menrat3=0d;
	      Double womrat3=0d;
	      Double boyrat3=0d;
	      Double girlrat3=0d;
	      Double slavema7=0d;
	      Double slavemx7=0d;
	      Double slavmax7=0d;
	      Double men7=0d;
	      Double women7=0d;
          Double boy7=0d;
	      Double girl7=0d;
	      Double adult7=0d;
	      Double child7=0d;
	      Double male7=0d;
	      Double female7=0d;
	      Double menrat7=0d;
	      Double womrat7=0d;
	      Double boyrat7=0d;
	      Double girlrat7=0d;
	      Double malrat7=0d;
	      Double chilrat7=0d;
		  
		  
		  //set default values
		  men1=defVal(men1, 0);
		  women1=defVal(women1, 0);
		  boy1=defVal(boy1, 0);
		  girl1=defVal(girl1, 0);
		  child1=defVal(child1, 0);
		  infant1=defVal(infant1, 0);
		  adult1=defVal(adult1, 0);
		  men4=defVal(men4, 0);
		  women4=defVal(women4, 0);
		  boy4=defVal(boy4, 0);
		  girl4=defVal(girl4, 0);
		  child4=defVal(child4, 0);
		  infant4=defVal(infant4, 0);
		  adult4=defVal(adult4, 0);
		  men5=defVal(men5, 0);
		  women5=defVal(women5, 0);
		  boy5=defVal(boy5, 0);
		  girl5=defVal(girl5, 0);
		  child5=defVal(child5, 0);
		  adult5 = defVal(adult5, 0);
		  male1=defVal(male1,0);
		  female1=defVal(female1, 0);
		  male4=defVal(male4, 0);
		  female4=defVal(female4, 0);
		  male5=defVal(male5, 0);
		  female5=defVal(female5, 0);

           
		  try {
			//Do the calculations
			 adlt1imp = men1+women1+adult1+men4+women4+adult4+men5+women5+adult5;
			 chil1imp = boy1+girl1+child1+infant1+boy4+girl4+child4+infant4+boy5+girl5+child5;
			 male1imp = male1+men1+boy1+male4+men4+boy4+male5+men5+boy5;
			 feml1imp = female1+women1+girl1+female4+women4+girl4+female5+women5+girl5;


			 if (tslavesd >=1 && adlt1imp==0) {adlt1imp = tslavesd - chil1imp;}
			 if (tslavesd==null && tslavesp >=1 && adlt1imp==0) {adlt1imp = tslavesp - chil1imp;}
			 if (tslavesd >=1 && chil1imp==0) {chil1imp = tslavesd - adlt1imp;}
			 if (tslavesd==null && tslavesp >=1 && chil1imp==0) {chil1imp = tslavesp - adlt1imp;}

			 slavema1 = adlt1imp+chil1imp;
			 slavemx1 = male1imp+feml1imp;
			 if (slavema1 >=1) {slavmax1=men1+women1+boy1+girl1+men4+women4+boy4+girl4+men5+women5+boy5+girl5;}


			 if(slavema1 >=0 && slavema1 <= 19) {slavema1=null;}
			 if(slavemx1 >=0 && slavemx1 <=19) {slavemx1=null;}
			 if(slavmax1 >=0 && slavmax1 <=19) {slavmax1=null;}

			 if (slavema1==null)
			 {
			     if(adlt1imp >=0) {adlt1imp=null;}
			     if(chil1imp >=0)  {chil1imp=null;}
			 }

			 if(slavemx1==null)
			 {
			  if(feml1imp >=0) {feml1imp=null;}
			  if(male1imp >=0) {male1imp=null;}
			 }
			 if(chil1imp!=null && slavema1!=null){chilrat1=chil1imp.doubleValue()/slavema1.doubleValue();} //Added If to check for null

			 if(male1imp!=null && slavemx1!=null) {malrat1= male1imp.doubleValue()/slavemx1.doubleValue();} //Added If to check for null

			 if (slavmax1!=null && slavmax1 >= 20) {menrat1=(men1.doubleValue()+men4.doubleValue()+men5.doubleValue())/slavmax1.doubleValue();} //Added null check
			 if (slavmax1!=null && slavmax1 >= 20) {womrat1=(women1.doubleValue()+women4.doubleValue()+women5.doubleValue())/slavmax1.doubleValue();} //Added null check
			 if (slavmax1!=null && slavmax1 >= 20) {boyrat1=(boy1.doubleValue()+boy4.doubleValue()+boy5.doubleValue())/slavmax1.doubleValue();} //Added null check
			 if (slavmax1!=null && slavmax1 >= 20) {girlrat1= (girl1.doubleValue()+girl4.doubleValue()+girl5.doubleValue())/slavmax1.doubleValue();} //Added null check
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		 //Set Defaults
		 men3=defVal(men3, 0);
		 women3=defVal(women3, 0);
		 boy3=defVal(boy3, 0);
		 girl3=defVal(girl3, 0);
		 child3=defVal(child3, 0);
		 infant3=defVal(infant3, 0);
		 infantm3=defVal(infantm3, 0);
		 infantf3=defVal(infantf3, 0);
		 adult3=defVal(adult3, 0);
		 men6=defVal(men6, 0);
		 women6=defVal(women6, 0);
		 boy6=defVal(boy6, 0);
		 girl6=defVal(girl6, 0);
		 child6=defVal(child6, 0);
		 adult6=defVal(adult6, 0);
		 male3=defVal(male3, 0);
		 female3=defVal(female3, 0);
		 men3=defVal(men3, 0);
		 women3=defVal(women3, 0);
		 boy3=defVal(boy3, 0);
		 girl3=defVal(girl3, 0);
		 infantm3=defVal(infantm3, 0);
		 infantf3=defVal(infantf3, 0);
		 male6=defVal(male6, 0);
		 female6=defVal(female6, 0);
		 men6=defVal(men6, 0);
		 women6=defVal(women6, 0);
		 boy6=defVal(boy6, 0);
		 girl6=defVal(girl6, 0);


		 try {
			adlt3imp = new Double(men3+women3+adult3+men6+women6+adult6);
			 chil3imp = new Double( boy3+girl3+child3+infant3+boy6+girl6+child6);
			 male3imp = new Double( male3+men3+boy3+infantm3+male6+men6+boy6+male5+men5+boy5);
			 feml3imp = new Double( female3+women3+girl3+infantf3+female6+women6+girl6);

			 if (slaarriv >=1 && adlt3imp==0) {adlt3imp = slaarriv - chil3imp;}
			 if (slaarriv >=1 && chil3imp==0) {chil3imp = slaarriv - adlt3imp;}

			 slavema3 = adlt3imp+chil3imp;
			 slavemx3 = male3imp+feml3imp;
			 if (slavema3 >=1) {slavmax3=new Double(men3+women3+boy3+girl3+men6+women6+boy6+girl6);}


			 if(slavema3 >=0 && slavema3<=19) {slavema3=null;}
			 if(slavemx3 >=0 && slavemx3<=19) {slavemx3=null;}
			 if(slavmax3 >=0 && slavmax3<=19) {slavmax3=null;}


			 if (slavema3==null)
			 {
			  if(adlt3imp >=0) {adlt3imp=null;}
			  if(chil3imp >=0) {chil3imp=null;}
			 }

			 if(slavemx3==null)
			 {
			  if(feml3imp >=0) {feml3imp=null;}
			  if(male3imp >=0) {male3imp=null;}
			 }

			 if(chil3imp!=null && slavema3!=null) {chilrat3=chil3imp/slavema3;} //Added If to check for null
			 if(male3imp!=null && slavemx3!=null) {malrat3= male3imp/slavemx3;} //Added If to check for null
			 if(slavmax3!=null && slavmax3 >= 20) {menrat3=(men3+men6)/slavmax3;}
			 if(slavmax3!=null && slavmax3 >= 20) {womrat3=(women3+women6)/slavmax3;}
			 if(slavmax3!=null && slavmax3 >= 20) {boyrat3=(boy3+boy6)/slavmax3;}
			 if(slavmax3!=null && slavmax3 >= 20) {girlrat3= (girl3+girl6)/slavmax3;}


			 if(slavema3!=null && slavema3 >= 20) {slavema7=slavema3;}
			 if(slavemx3!=null && slavemx3 >= 20) {slavemx7=slavemx3;}
			 if(slavmax3!=null && slavmax3 >= 20) {slavmax7=slavmax3;}
			 if(slavmax7!=null && slavmax7 >= 20) {men7=new Double(men3+men6);}
			 if(slavmax7!=null && slavmax7 >= 20) {women7=new Double(women3+women6);}
			 if(slavmax7!=null && slavmax7 >= 20) {boy7=new Double(boy3+boy6);}
			 if(slavmax7!=null && slavmax7 >= 20) {girl7=new Double(girl3+girl6);}
			 if(slavema7!=null && slavema7 >= 20) {adult7=adlt3imp;}
			 if(slavema7!=null && slavema7 >= 20) {child7=chil3imp;}
			 if(slavemx7!=null && slavemx7 >=20) {male7=male3imp;}
			 if(slavemx7!=null && slavemx7 >= 20) {female7=feml3imp;}
			 if(menrat3!=null && menrat3 >= 0) {menrat7=menrat3;}
			 if(womrat3!=null && womrat3 >= 0) {womrat7=womrat3;}
			 if(boyrat3!=null && boyrat3 >= 0) {boyrat7=boyrat3;}
			 if(girlrat3!=null && girlrat3 >= 0) {girlrat7=girlrat3;}
			 if(malrat3!=null && malrat3 >= 0) {malrat7=malrat3;}
			 if(chilrat3!=null && chilrat3 >= 0) {chilrat7=chilrat3;}

			 if (slavema3==null && slavema1!=null && slavema1 >=20) {slavema7=slavema1.doubleValue();}
			 if (slavemx3==null && slavemx1!=null && slavemx1 >=20) {slavemx7=slavemx1.doubleValue();}
			 if (slavmax3==null && slavmax1!=null && slavmax1 >=20) {slavmax7=slavmax1.doubleValue();}
			 if (slavmax3==null && slavmax1!=null && slavmax1 >= 20) {men7= new Double(men1+men4 +men5);}
			 if (slavmax3==null && slavmax1!=null && slavmax1 >= 20) {women7= new Double(women1+women4+women5);}
			 if (slavmax3==null && slavmax1!=null && slavmax1 >= 20) {boy7= new Double(boy1+boy4+boy5);}
			 if (slavmax3==null && slavmax1!=null && slavmax1 >= 20) {girl7= new Double(girl1+girl4+girl5);}
			 if (slavema3==null && slavema1!=null && slavema1 >= 20) {adult7=adlt1imp.doubleValue();}
			 if (slavema3==null && slavema1!=null && slavema1 >= 20) {child7=chil1imp.doubleValue();}
			 if (slavemx3==null && slavemx1!=null && slavemx1 >=20) {male7=male1imp.doubleValue();}
			 if (slavemx3==null && slavemx1!=null && slavemx1 >= 20) {female7=feml1imp.doubleValue();}
			 if (menrat3==null && menrat1!=null && menrat1 >= 0) {menrat7=menrat1;}
			 if (womrat3==null && womrat1!=null && womrat1 >= 0) {womrat7=womrat1;}
			 if (boyrat3==null && boyrat1!=null && boyrat1 >= 0) {boyrat7=boyrat1;}
			 if (girlrat3==null && girlrat1!=null && girlrat1 >= 0) {girlrat7=girlrat1;}
			 if (malrat3==null && malrat1!=null && malrat1 >= 0) {malrat7=malrat1;}
			 if (chilrat3==null&& chilrat1!=null && chilrat1 >= 0) {chilrat7=chilrat1;}
		} catch (Exception e) {
			e.printStackTrace();
		}

		 //more defaults
		 men2=defVal(men2, 0);
		 women2=defVal(women2, 0);
		 boy2=defVal(boy2, 0);
		 girl2=defVal(girl2, 0);
		 child2=defVal(child2, 0);
		 adult2=defVal(adult2, 0);
		 male2=defVal(male2, 0);
		 female2=defVal(female2, 0);
		 
		 adlt2imp = men2+women2;
		 chil2imp = boy2+girl2+child2;
		 male2imp=men2+boy2;
		 feml2imp=women2+girl2;
		 
		 //Save back to voyage object
		 voyage.setAdlt1imp(adlt1imp); 
		 if(chil1imp !=null) {voyage.setChil1imp(chil1imp.doubleValue());}
		 if(male1imp !=null) {voyage.setMale1imp(male1imp.doubleValue());}
		 if(feml1imp !=null) {voyage.setFeml1imp(feml1imp.doubleValue());}
		 voyage.setAdlt2imp(adlt2imp);
		 voyage.setChil2imp(chil2imp);
		 voyage.setMale2imp(male2imp);
		 voyage.setFeml2imp(feml2imp);
		 if(slavema1!=null) {voyage.setSlavema1(slavema1.doubleValue());}
		 if(slavemx1 !=null) {voyage.setSlavemx1(slavemx1.doubleValue());}
		 voyage.setSlavmax1(slavmax1);
		 voyage.setChilrat1(chilrat1);
		 voyage.setMalrat1(malrat1);  
		 voyage.setMenrat1(menrat1);
		 voyage.setWomrat1(womrat1);
		 voyage.setBoyrat1(boyrat1);
		 voyage.setGirlrat1(girlrat1);
		 voyage.setAdlt3imp(adlt3imp);
		 voyage.setChil3imp(chil3imp);
		 voyage.setMale3imp(male3imp);
		 voyage.setFeml3imp(feml3imp);
		 voyage.setSlavema3(slavema3);
		 voyage.setSlavemx3(slavemx3);
		 if(slavmax3 !=null) {voyage.setSlavmax3(slavmax3.intValue());}
		 voyage.setChilrat3(chilrat3);
	     voyage.setMalrat3(malrat3);
	     voyage.setMenrat3(menrat3);
	     voyage.setWomrat3(womrat3);
	     voyage.setBoyrat3(boyrat3);
	     voyage.setGirlrat3(girlrat3);
	     voyage.setSlavema7(slavema7);
	     voyage.setSlavemx7(slavemx7);
	     if(slavmax7 !=null) {voyage.setSlavmax7(slavmax7.intValue());}
	     voyage.setMen7(men7);
	     voyage.setWomen7(women7);
         voyage.setBoy7(boy7);
	     voyage.setGirl7(girl7);
	     voyage.setAdult7(adult7);
	     voyage.setChild7(child7);
	     voyage.setMale7(male7);
	     voyage.setFemale7(female7);
		 if(menrat7 !=null) {voyage.setMenrat7(menrat7.floatValue());}
		 if(womrat7 !=null) {voyage.setWomrat7(womrat7.floatValue());}
		 if(boyrat7 !=null) {voyage.setBoyrat7(boyrat7.floatValue());}
		 if(girlrat7 !=null) {voyage.setGirlrat7(girlrat7.floatValue());}
		 if(malrat7 !=null) {voyage.setMalrat7(malrat7.floatValue());}
		 if(chilrat7 !=null) {voyage.setChilrat7(chilrat7.floatValue());}
	}
	
	public void calculateXmImpflag(){
		int rig_int = 0;
		int majbyimp_int = 0;
		int natinimp_int = 0;
		int mjselimp_int = 0;
		int mjselimp1_int = 0;
		Double xmimpflag=0d;
		//find vesselRig attribute
		VesselRig rig = voyage.getRig();
		if (rig != null){
			rig_int = rig.getId().intValue();
		}
		
		Region majbyimp = voyage.getMajbyimp();
		if (majbyimp != null){
			majbyimp_int = majbyimp.getId().intValue();
		}
		
		Nation natinimp = voyage.getNatinimp();
		if (natinimp != null){
			natinimp_int = natinimp.getId().intValue();
		}
		
		Integer yearam = voyage.getYearam();
		Region mjselimp = voyage.getMjselimp();
		if (mjselimp != null){
			mjselimp_int = mjselimp.getId().intValue();
		}
		
		Region mjselimp1 = voyage.getMjselimp();
		if (mjselimp1 != null){
			mjselimp1_int = mjselimp1.getId().intValue();
		}
				
	    
		if  ((rig_int==26 || rig_int==29 || rig_int==42 || rig_int==43 || rig_int==54 || rig_int==59 || rig_int==61 || rig_int==65 || rig_int== 80 || rig_int==86 || rig==null)
		    && yearam >= 1626 && yearam < 1651) {xmimpflag = 127d ;}
		else if  ((rig_int==26 || rig_int==29 || rig_int==42 || rig_int==43 || rig_int==54 || rig_int==59 || rig_int==61 || rig_int==65 || rig_int==80 || rig_int==86 || rig==null)
		    && yearam >= 1651 && yearam < 1676) {xmimpflag = 128d ;}
		else if   ((rig_int==26 || rig_int==29 || rig_int==42 || rig_int==43 || rig_int==54 || rig_int==59 || rig_int==61 || rig_int==65 || rig_int== 80 || rig_int==86 || rig==null)
		    && yearam >= 1676 && yearam < 1701) {xmimpflag = 129d ;}
		else if   ((rig_int==26 || rig_int==29 || rig_int==42 || rig_int==43 || rig_int==54 || rig_int==59 || rig_int==61 || rig_int==65 || rig_int==80 || rig_int==86 || rig==null)
		    && yearam >= 1701 && yearam < 1726) {xmimpflag = 130d;}
		else if   ((rig_int==26 || rig_int==29 || rig_int==42 || rig_int==43 || rig_int==54 || rig_int==59 || rig_int==61 || rig_int==65 || rig_int==80 || rig_int==86 || rig==null)
		    && yearam >= 1726 && yearam < 1751) {xmimpflag = 131d;}
		else if   ((rig_int==26 || rig_int==29 || rig_int==42 || rig_int==43 || rig_int==54 || rig_int==59 || rig_int==61 || rig_int==65 || rig_int==80 || rig_int==86 || rig==null)
		   && yearam >= 1751 && yearam < 1776) {xmimpflag = 132d;}
		else if   ((rig_int==26 || rig_int==29 || rig_int==42 || rig_int==43 || rig_int==54 || rig_int==59 || rig_int==61 || rig_int==65 || rig_int==80 || rig_int==86 || rig==null)
		   && yearam >= 1776 && yearam < 1801) {xmimpflag = 133d;}
		else if   ((rig_int==26 || rig_int==29 || rig_int==42 || rig_int==43 || rig_int==54 || rig_int==59 || rig_int==61 || rig_int==65 || rig_int==80 || rig_int==86 || rig==null)
		   && yearam >= 1801 && yearam < 1826) {xmimpflag = 134d;}
		else if   ((rig_int==26 || rig_int==29 || rig_int==42 || rig_int==43 || rig_int==54 || rig_int==59 || rig_int==61 || rig_int==65 || rig_int==80 || rig_int==86 || rig==null)
		   && yearam >= 1826 && yearam < 1851) {xmimpflag = 135d;}
		else if   ((rig_int==26 || rig_int==29 || rig_int==42 || rig_int==43 || rig_int==54 || rig_int==59 || rig_int==61 || rig_int==65 || rig_int==80 || rig_int==86 || rig==null)
		   && yearam >= 1851 && yearam < 1876) {xmimpflag = 136d;}
	
		else if   (yearam < 1700 && majbyimp_int == 60100) {xmimpflag = 101d;}
		else if   (yearam >= 1700 && yearam < 1801 && majbyimp_int == 60100) {xmimpflag = 102d;}
		else if   (yearam >=1800 && majbyimp_int == 60100) {xmimpflag = 103d;}
		else if   (yearam < 1700 && majbyimp_int == 60200) {xmimpflag = 104d;}
		else if   (yearam >= 1700 && yearam < 1801 && majbyimp_int == 60200) {xmimpflag = 105d;}
		else if   (yearam >=1800 && majbyimp_int == 60200) {xmimpflag = 106d;}
		else if  (yearam < 1700 && majbyimp_int == 60400) {xmimpflag = 107d;}
		else if  (yearam >= 1700 && yearam < 1801 && majbyimp_int == 60400){xmimpflag = 108d ;}
		else if  (yearam < 1700 && majbyimp_int == 60500) {xmimpflag = 110d;}
		else if  (yearam >= 1700 && yearam < 1801 && majbyimp_int == 60500) {xmimpflag = 111d;}
		else if  (yearam >=1800 && majbyimp_int == 60500) {xmimpflag = 112d;}
		else if  (yearam < 1700 && majbyimp_int == 60600) {xmimpflag = 113d;}
		else if  (yearam >= 1700 && yearam < 1801 && majbyimp_int == 60600) {xmimpflag = 114d;}
		else if  (yearam >=1800 && majbyimp_int == 60600) {xmimpflag = 115d;}
		else if  (yearam < 1700 && majbyimp_int == 60700) {xmimpflag = 116d;}
		else if  (yearam >= 1700 && yearam < 1801 && majbyimp_int == 60700) {xmimpflag = 117d;}
		else if  (yearam >=1800 && majbyimp_int == 60700) {xmimpflag = 118d;}
		else if  (yearam >= 1700 && yearam < 1801 && majbyimp_int == 60300) {xmimpflag = 120d;}
		else if  (yearam >=1800 && majbyimp_int == 60300) {xmimpflag = 121d;}
		else if  (yearam < 1700 && majbyimp_int == 60800) {xmimpflag = 122d;}
		else if  (yearam >= 1700 && yearam < 1801 && majbyimp_int == 60800) {xmimpflag = 123d;}
		else if  (yearam >=1800 && majbyimp_int == 60800) {xmimpflag = 124d;}
		else if  (yearam < 1627) {xmimpflag = 1d;}
		else if  ((yearam >= 1626 && yearam < 1642) && ((mjselimp_int >= 31100 && mjselimp_int < 32000) ||  mjselimp1_int == 40000 || mjselimp_int == 80400))   {xmimpflag = 2d;}
		else if  (yearam < 1716 && mjselimp_int >= 36100 && mjselimp_int < 37000) {xmimpflag = 3d;}
		else if  (yearam < 1701 && mjselimp_int == 50300)  {xmimpflag = 4d;}
		else if  (yearam >= 1700 && yearam < 1800 && mjselimp_int == 50300)  {xmimpflag = 5d;}
		else if  (yearam > 1799 && mjselimp_int == 50300)  {xmimpflag = 6d;}
		else if  (yearam < 1650 && natinimp_int == 8 )  {xmimpflag = 7d;}
		else if  (yearam >= 1650 && yearam < 1674 && natinimp_int == 8)  {xmimpflag = 8d;}
		else if  (yearam >= 1674 && yearam < 1731 && natinimp_int == 8)  {xmimpflag = 9d;}
		else if  (yearam > 1730 && natinimp_int == 8 )  {xmimpflag = 10d;}
		else if  (yearam < 1751 && mjselimp_int == 50200)  {xmimpflag = 11d;}
		else if  (yearam >= 1751 && yearam < 1776 && mjselimp_int == 50200)  {xmimpflag = 12d;}
		else if  (yearam >= 1776 && yearam < 1801 && mjselimp_int == 50200)  {xmimpflag = 13d;}
		else if  (yearam >= 1801 && yearam < 1826 && mjselimp_int == 50200)  {xmimpflag = 14d;}
		else if  (yearam > 1825 && mjselimp_int == 50200)  {xmimpflag = 15d;}
		else if  (yearam >= 1642 && yearam < 1663 && ((mjselimp_int >= 31100 && mjselimp_int < 32000) ||
		    mjselimp1_int == 40000 || mjselimp_int == 80400))  {xmimpflag = 16d;}
		else if  (yearam >= 1794 && yearam < 1807 && natinimp_int == 15)  {xmimpflag = 157d;}
		else if  (yearam < 1794 && natinimp_int == 15) {xmimpflag = 159d;}
		else if  (yearam < 1851 && natinimp_int == 9 )  {xmimpflag = 99d;}
		else if  (yearam >= 1851 && yearam < 1876 && natinimp_int ==9)  {xmimpflag = 100d;}
		else if  (yearam < 1751 && rig_int == 1) {xmimpflag = 17d;}
		else if  (yearam >= 1751 && yearam < 1776 && rig_int == 1)  {xmimpflag = 98d;}
		else if  (yearam >= 1776 && yearam < 1801 && rig_int == 1)  {xmimpflag = 18d;}
		else if  (yearam >= 1801 && yearam < 1826 && rig_int == 1)  {xmimpflag = 19d;}
		else if  (yearam >= 1826 && yearam < 1851 && rig_int == 1)  {xmimpflag = 20d;}
		else if  (yearam >= 1851 && yearam < 1876 && rig_int == 1)  {xmimpflag = 21d;}
		else if  (yearam < 1776 && rig_int == 2) {xmimpflag = 22d;}
		else if  (yearam >= 1776 && yearam < 1801 && rig_int == 2)  {xmimpflag = 23d;}
		else if  (yearam >= 1801 && yearam < 1826 && rig_int == 2)  {xmimpflag = 24d;}
		else if  (yearam >= 1826 && yearam < 1851 && rig_int == 2)  {xmimpflag = 25d;}
		else if  (yearam >= 1851 && yearam < 1876 && rig_int == 2)  {xmimpflag = 26d;}
		else if  (yearam < 1751 && rig_int == 3) {xmimpflag = 27d;}
		else if  (yearam >= 1751 && yearam < 1776 && rig_int == 3)  {xmimpflag = 28d;}
		else if  (yearam >= 1776 && yearam < 1801 && rig_int == 3)  {xmimpflag = 29d;}
		else if  (yearam >= 1801 && yearam < 1876 && rig_int == 3)  {xmimpflag = 30d;}
		else if  (yearam < 1726 && rig_int == 4) {xmimpflag = 31d;}
		else if  (yearam >= 1726 && yearam < 1751 && rig_int == 4)  {xmimpflag = 32d;}
		else if  (yearam >= 1751 && yearam < 1776 && rig_int == 4)  {xmimpflag = 33d;}
		else if  (yearam >= 1776 && yearam < 1801 && rig_int == 4)  {xmimpflag = 34d;}
		else if  (yearam >= 1801 && yearam < 1826 && rig_int == 4)  {xmimpflag = 35d;}
		else if  (yearam >= 1826 && yearam < 1851 && rig_int == 4)  {xmimpflag = 36d;}
		else if  (yearam >= 1851 && yearam < 1876 && rig_int == 4)  {xmimpflag = 37d;}
		else if  (rig_int==5) {xmimpflag = 38d;}
		else if  (rig_int==6) {xmimpflag = 39d;}
		else if  (rig_int==7) {xmimpflag = 40d;}
		else if  (yearam < 1776 && rig_int == 8) {xmimpflag = 41d;}
		else if  (yearam >= 1776 && yearam < 1801 && rig_int == 8)  {xmimpflag = 42d;}
		else if  (yearam >= 1801 && yearam < 1826 && rig_int == 8)  {xmimpflag = 43d;}
		else if  (yearam >= 1826 && yearam < 1851 && rig_int == 8)  {xmimpflag = 44d;}
		else if  (yearam >= 1851 && yearam < 1876 && rig_int == 8)  {xmimpflag = 45d;}
		else if  (yearam < 1826 && (rig_int == 9 || rig_int == 31)) {xmimpflag = 46d;}
		else if  (yearam >= 1826 && yearam < 1851 && (rig_int == 9 || rig_int == 31))  {xmimpflag = 47d;}
		else if  (yearam >= 1851 && yearam < 1876 && (rig_int == 9 || rig_int == 31))  {xmimpflag = 48d;}
		else if  (rig_int==10||rig_int==24) {xmimpflag = 49d;}
		else if  (rig_int==11 || rig_int==12) {xmimpflag = 50d;}
		else if  (yearam < 1751 && rig_int == 13) {xmimpflag = 51d;}
		else if  (yearam >= 1751 && yearam < 1776 && rig_int == 13)  {xmimpflag = 52d;}
		else if  (yearam >= 1776 && yearam < 1801 && rig_int == 13)  {xmimpflag = 53d;}
		else if  (yearam >= 1801 && yearam < 1826 && rig_int == 13)  {xmimpflag = 54d;}
		else if  (yearam >= 1826 && yearam < 1877 && rig_int == 13)  {xmimpflag = 55d;}
		else if  (rig_int==15) {xmimpflag = 56d;}
		else if  (rig_int==20) {xmimpflag = 57d;}
		else if  (rig_int==21) {xmimpflag = 58d;}
		else if  (rig_int==23) {xmimpflag = 59d;}
		else if  (yearam < 1751 && rig_int == 25) {xmimpflag = 60d;}
		else if  (yearam >= 1751 && yearam < 1776 && rig_int == 25)  {xmimpflag = 61d;}
		else if  (yearam >= 1776 && yearam < 1801 && rig_int == 25)  {xmimpflag = 62d;}
		else if  (yearam >= 1801 && yearam < 1826 && rig_int == 25)  {xmimpflag = 63d;}
		else if  (yearam >= 1826 && yearam < 1851 && rig_int == 25)  {xmimpflag = 160d;}
		else if  (yearam >= 1851 && yearam < 1877 && rig_int == 25)  {xmimpflag = 64d;}
		else if  (yearam < 1751 && rig_int == 27) {xmimpflag = 65d;}
		else if  (yearam >= 1751 && yearam < 1776 && rig_int == 27)  {xmimpflag = 66d;}
		else if  (yearam >= 1776 && yearam < 1801 && rig_int == 27)  {xmimpflag = 67d;}
		else if  (yearam >= 1801 && yearam < 1877 && rig_int == 27)  {xmimpflag = 68d;}
		else if  (rig_int==28) {xmimpflag = 69d;}
		else if  (yearam < 1726 && (rig_int == 30 || rig_int == 45 || rig_int==63)) {xmimpflag = 70d;}
		else if  (yearam >= 1726 && yearam < 1776 && (rig_int == 30 || rig_int == 45 || rig_int==63))  {xmimpflag = 71d;}
		else if  (yearam >= 1776 && yearam < 1801 && (rig_int==30 || rig_int==45 || rig_int==63)) {xmimpflag = 97d;}
		else if  (yearam >= 1801 && yearam < 1826 && (rig_int == 30 || rig_int == 45 || rig_int==63))  {xmimpflag = 72d;}
		else if  (yearam >= 1826 && yearam < 1876 && (rig_int==30 || rig_int==45 || rig_int==63)) {xmimpflag = 85d;}
		else if  (rig_int==32 || rig_int==39) {xmimpflag = 73d;}
		else if  (yearam < 1726 && rig_int == 35) {xmimpflag = 74d;}
		else if  (yearam >= 1726 && yearam < 1751 && rig_int == 35)  {xmimpflag = 75d;}
		else if  (yearam >= 1751 && yearam < 1776 && rig_int == 35)  {xmimpflag = 76d;}
		else if  (yearam >= 1776 && yearam < 1801 && rig_int == 35)  {xmimpflag = 77d;}
		else if  (yearam >= 1801 && yearam < 1877 && rig_int == 35)  {xmimpflag = 78d;}
		else if  (yearam < 1776 && rig_int == 40) {xmimpflag = 79d;}
		else if  (yearam >= 1776 && yearam < 1801 && rig_int == 40)  {xmimpflag = 80d;}
		else if  (yearam >= 1801 && yearam < 1826 && rig_int == 40)  {xmimpflag = 81d;}
		else if  (yearam >= 1826 && yearam < 1876 && rig_int == 40)  {xmimpflag = 82d;}
		else if  (rig_int==41 || rig_int==57) {xmimpflag = 83d;}
		else if  (rig_int==44) {xmimpflag = 84d;}
		else if  (rig_int==47) {xmimpflag = 86d;}
		else if  (rig_int==48) {xmimpflag = 87d;}
		else if  (yearam < 1826 && (rig_int==14 || rig_int == 36 || rig_int==49)) {xmimpflag = 88d;}
		else if  (yearam >= 1826 && yearam < 1876 && ( rig_int == 14 || rig_int==36 || rig_int == 49))  {xmimpflag = 89d;}
		else if  (yearam < 1826 && (rig_int==16 || rig_int == 51)) {xmimpflag = 90d;}
		else if  (yearam >= 1826 && yearam < 1851 && (rig_int==16 || rig_int == 51))  {xmimpflag = 91d;}
		else if  (yearam >= 1851 && yearam < 1876 && (rig_int==16 || rig_int == 51))  {xmimpflag = 92d;}
		else if  (rig_int==17||rig_int==19||rig_int==52||rig_int==53) {xmimpflag = 93d;}
		else if  (yearam < 1726 && rig_int == 60) {xmimpflag = 94d;}
		else if  (yearam >= 1726 && yearam < 1826 && rig_int == 60)  {xmimpflag = 95d;}
		else if  (yearam >= 1826 && yearam < 1876 && rig_int == 60)  {xmimpflag = 96d;}
		else if  (yearam < 1776 && rig_int == 1 && natinimp_int == 9) {xmimpflag = 137d;}
		else if  (yearam >= 1776 && yearam < 1801 && rig_int == 1 && natinimp_int == 9)  {xmimpflag = 138d;}
		else if  (yearam >= 1801 && yearam < 1826 && rig_int == 1 && natinimp_int == 9)  {xmimpflag = 139d;}
		else if  (yearam > 1825 && rig_int == 1 && natinimp_int ==9)  {xmimpflag = 140d;}
		else if  (yearam < 1776 && (rig_int==2 || rig_int == 5) && natinimp_int == 9) {xmimpflag = 141d;}
		else if  (yearam >= 1776 && yearam < 1801 && (rig_int==2 || rig_int == 5) && natinimp_int == 9)  {xmimpflag = 142d;}
		else if  (yearam >= 1801 && yearam < 1826 && rig_int == 5 && natinimp_int == 9)  {xmimpflag = 143d;}
		else if  (yearam > 1825 && (rig_int==2 || rig_int == 5) && natinimp_int ==9)  {xmimpflag = 145d;}
		else if  (yearam < 1776 && rig_int ==4 && natinimp_int == 9) {xmimpflag = 146d;}
		else if  (yearam >= 1776 && yearam < 1801 && rig_int ==4 && natinimp_int == 9)  {xmimpflag = 147d;}
		else if  (yearam >= 1801 && yearam < 1826 && rig_int ==4 && natinimp_int == 9)  {xmimpflag = 148d;}
		else if  (yearam > 1825 && rig_int ==4 && natinimp_int ==9)  {xmimpflag = 149d;}
		else if  (yearam < 1776 && rig_int ==8 && natinimp_int == 9) {xmimpflag = 150d;}
		else if  (yearam >= 1776 && yearam < 1826 && rig_int ==8 && natinimp_int == 9)  {xmimpflag = 151d;}
		else if  (yearam > 1825 && rig_int ==8 && natinimp_int ==9)  {xmimpflag = 152d;}
		else if  (yearam >= 1826 && yearam < 1876 && rig_int == 9 && natinimp_int == 9) {xmimpflag = 154d;}
		else if  (rig_int == 27 && natinimp_int == 9) {xmimpflag = 155d;}
		else if (rig_int == 35 && natinimp_int == 9) {xmimpflag = 156d;}
		
		if (xmimpflag != 0){
			voyage.setXmimpflag(xmimpflag);
		}
	}
}
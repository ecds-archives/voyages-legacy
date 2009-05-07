package edu.emory.library.tast.submission;

import java.math.BigDecimal;
import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;

import org.hibernate.Session;

import edu.emory.library.tast.dm.Fate;
import edu.emory.library.tast.dm.FateOwner;
import edu.emory.library.tast.dm.FateSlaves;
import edu.emory.library.tast.dm.FateVessel;
import edu.emory.library.tast.dm.Nation;
import edu.emory.library.tast.dm.Port;
import edu.emory.library.tast.dm.Region;
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
		Integer natinimp=voyage.getNatinimp().getId().intValue();  //TODO imputed natinimp - should already have a function
		
		Float tonmod=tonnage.floatValue();
		
		//calculate
	    if (tontype == 13) {tonmod=tonnage.floatValue();}
	    if ((tontype < 3 || tontype == 4 || tontype == 5) && yearam > 1773) {tonmod = tonnage.floatValue();}
	    if ((tontype < 3 || tontype == 4 || tontype == 5) && yearam < 1774 && tonnage < 151) {tonmod=2.3f + (1.8f * tonnage);}
	    if ((tontype < 3 || tontype == 4 || tontype == 5) && yearam < 1774 && tonnage > 150 && tonnage < 251) {tonmod=65.3f + (1.2f * tonnage);}
	    if ((tontype < 3 || tontype == 4 || tontype == 5) && yearam < 1774 && tonnage > 250) {tonmod=13.1f + (1.1f * tonnage);}
	    if (tontype == 4 && yearam > 1783 && yearam < 1794) {tonmod=9999f;}
	    if (tontype == 3 || tontype == 6 || tontype == 9 || tontype == 16){tonmod = 71 + (0.86f * tonnage);}
	    if ((tontype == 3 || tontype == 6 || tontype == 9 || tontype == 16) && yearam < 1774 && tonmod < 151) {tonmod=2.3f + (1.8f * tonnage);}
	    if ((tontype == 3 || tontype == 6 || tontype == 9 || tontype == 16) && yearam < 1774 && tonmod > 150 && tonmod < 251) {tonmod=65.3f + (1.2f * tonnage);}
	    if ((tontype == 3 || tontype == 6 || tontype == 9 || tontype == 16) && yearam < 1774 && tonmod > 250) {tonmod=13.1f + (1.1f * tonnage);}
	    if (tontype == 7) {tonmod=tonnage * 2f;}
	    if (tontype == 7 && yearam > 1773 && tonmod < 151) {tonmod=2.3f + (1.8f * tonmod);}
	    if (tontype == 7 && yearam > 1773 && tonmod > 150 && tonmod < 251) {tonmod=65.3f + (1.2f * tonmod);}
	    if (tontype == 7 && yearam > 1773 && tonmod > 250) {tonmod=13.1f + (1.1f * tonmod);}
	    if (tontype == 21) {tonmod= -6.093f + (0.76155f * tonnage);}
	    if (tontype == 21 && yearam > 1773 && tonmod < 151) {tonmod=2.3f + (1.8f * tonmod);}
	    if (tontype == 21 && yearam > 1773 && tonmod > 150 && tonmod < 251) {tonmod=65.3f + (1.2f * tonmod);}
	    if (tontype == 21 && yearam > 1773 && tonmod > 250) {tonmod=13.1f + (1.1f * tonmod);}
	    if ((tontype==null) && (yearam > 1714 && yearam < 1786) && tonnage > 0 && natinimp==7) {tontype=22;}
	    if (tontype == 22 && tonnage < 151) {tonmod=2.3f + (1.8f * tonnage);}
	    if (tontype == 22 && tonnage > 150 && tonnage < 251) {tonmod=65.3f + (1.2f * tonnage);}
	    if (tontype == 22 && tonnage > 250) {tonmod=13.1f + (1.1f * tonnage);}
	    if (tontype == 15 || tontype == 14 || tontype == 17) {tonmod = 52.86f + (1.22f * tonnage);}
	    if (tonmod==null) {tonmod=9999f;}
	    
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
		
		Integer ncar13 = voyage.getNcar13();
		Integer ncar15 = voyage.getNcar15();
		Integer ncar17 = voyage.getNcar17();
		Integer tslavesd = voyage.getTslavesd();
		if (tslavesd != null) {
			tslavesd_int = tslavesd.intValue();
		}
		Integer tslavesp = voyage.getTslavesp();
		if (tslavesp != null) {
			tslavesp_int = tslavesp.intValue();
		}
				
		Integer ncartot = defVal(ncar13, 0)+ defVal(ncar15, 0)+ defVal(ncar17, 0);
		rslt_d = ncartot.doubleValue()/tslavesd_int;
		rslt_p = ncartot.doubleValue()/tslavesp_int;
		
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
		    		(rslt_d >= 0.5d) && (ncar13 >= ncar15)) {
		    	majbuypt = plac1tra;
		}
		else if (plac1tra_int >=1 && plac2tra_int >=1 && plac3tra==null && 
				(rslt_d >= 0.5d) && (ncar15 >= ncar13)) {
			majbuypt = plac2tra;
		}
		else if (plac1tra_int >=1 && plac2tra_int >=1 && plac3tra==null && 
				tslavesd==null && (rslt_p >= 0.5d) && (ncar13 >= ncar15)) {
			majbuypt = plac1tra;
		}
		else if (plac1tra_int >=1 && plac2tra_int >=1 && plac3tra==null && 
				tslavesd==null && rslt_p >= 0.5d && ncar15 >= ncar13) {
			majbuypt = plac2tra;
		}
		else if (plac1tra_int >=1 && plac2tra_int >=1 && plac3tra==null && rslt_d < 0.5d && ncar13 < ncar15) {
			majbuypt = plac1tra;
		}
		else if (plac1tra_int >= 1 && plac2tra_int >= 1 && plac3tra==null && tslavesd==null && tslavesp==null && ncar13 >= ncar15) {
			majbuypt = plac1tra;
		}
		else if (plac1tra_int >= 1 && plac2tra_int >= 1 && plac3tra==null && tslavesd==null && tslavesp==null && ncar15 > ncar13) {
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
		else if (plac1tra_int >=1 && plac2tra_int >=1 && plac3tra_int >= 1 && rslt_d >= 0.5d && ncar13 >= ncar15 && ncar13 >= ncar17) {
			majbuypt = plac1tra;
		}
		else if (plac1tra_int >=1 && plac2tra_int >=1 && plac3tra_int >= 1 && tslavesd==null && rslt_p >= 0.5d && ncar13 >= ncar15 && ncar13 >= ncar17) {
			majbuypt = plac1tra;
		}
		else if (plac1tra_int >=1 && plac2tra_int >=1 && plac3tra_int >= 1 && rslt_d >= 0.5d && ncar15 >= ncar13 && ncar15 >= ncar17) {
			majbuypt = plac2tra;
		}
		else if (plac1tra_int >=1 && plac2tra_int >=1 && plac3tra_int >= 1 && tslavesd==null && rslt_p >= 0.5d && ncar15 >= ncar13 && ncar15 >= ncar17) {
			majbuypt = plac2tra;
		}
	    if (plac1tra_int >=1 && plac2tra_int >=1 && plac3tra_int >= 1 && rslt_d >= 0.5d && ncar17 >= ncar13 && ncar17 >= ncar15) {
	    	majbuypt = plac3tra;
	    }
	    else if (plac1tra_int >=1 && plac2tra_int >=1 && plac3tra_int >= 1 && tslavesd==null && rslt_p >= 0.5d && ncar17 >= ncar13 && ncar17 >= ncar15) {
	    	majbuypt = plac1tra;
	    }
	    else if (plac1tra_int >=1 && plac2tra_int >=1 && plac3tra_int >= 1 && rslt_d < 0.5d && ncar13==0) {
	    	majbuypt = plac1tra;
	    }
	    else if (plac1tra_int >=1 && plac2tra_int >=1 && plac3tra_int >= 1 && rslt_d < 0.5d && ncar13>=1 && ncar15==0 && ncar17==0) {
	    	majbuypt = plac3tra;
	    }
	    else if (plac1tra_int >=1 && plac2tra_int >=1 && plac3tra_int >= 1 && rslt_d < 0.5d && ncar13>=1 && ncar15>=1 && ncar17==0) {
	    	majbuypt = plac3tra;
	    }
	    else if (plac1tra_int >=1 && plac2tra_int >=1 && plac3tra_int >= 1 && tslavesd==null && rslt_p < 0.5d && ncar13==0) {
	    	majbuypt = plac1tra;
	    }
	    else if (plac1tra_int >=1 && plac2tra_int >=1 && plac3tra_int >= 1 && tslavesd==null && rslt_p < 0.5d && ncar13>=1 && ncar15==0 && ncar17==0) {
	    	majbuypt = plac3tra;
	    }
	    else if (tslavesd==null && tslavesp==null && ncartot.intValue() >=1 && plac1tra_int >= 1 && plac2tra_int >= 1 && plac3tra_int >= 1) {
	    	majbuypt = plac3tra;
	    }
	    else if (ncartot==null && plac1tra_int >= 1 && plac2tra_int >= 1 && plac3tra_int >= 1) {
	    	majbuypt = plac3tra;
	    }
        if (ncartot==null && tslavesd==null && tslavesp==null && plac1tra_int >= 1 && plac2tra_int >= 1 && plac3tra_int >= 1) {
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
	 * Calculates year5, year10, year25 and year100
	 */
	public void calculateValuesYear() 
	{
		//Create variables for calculations
		Integer yearam=voyage.getYearam();
		Integer year100=-1; //dummy default value because no ranges array
		
		//Assign values and call function to calculate year5
		ArrayList dateInputs=new ArrayList();
	    dateInputs.add(yearam);
	    ArrayList dateRanges=VoyagesCalcConstants.getdateRanges1();
	    ArrayList impDate = recode(dateInputs, dateRanges, false);
	    Integer year5=(Integer) impDate.get(0);
 
	  //Assign values and call function to calculate year10
        dateInputs=new ArrayList();
	    dateInputs.add(yearam);
	    dateRanges= VoyagesCalcConstants.getdateRanges2();
	    impDate = recode(dateInputs, dateRanges, false);
	    Integer year10=(Integer) impDate.get(0);

	  //Assign values and call function to calculate year25
	    dateInputs=new ArrayList();
	    dateInputs.add(yearam);
	    dateRanges=VoyagesCalcConstants.getdateRanges3();
	    impDate = recode(dateInputs, dateRanges, false);
	    Integer year25=(Integer) impDate.get(0);

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


	  //Loop over each input vlaue
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
		//Create variables for calculation
		Integer national = voyage.getNational().getId().intValue();
		HashMap natHash = VoyagesCalcConstants.getnatHash();
		
	    Integer natinimp= (Integer)natHash.get(national);
	    
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
		Integer voy1imp = dateDiff(dateland1, datedep);
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

	    //convert dates to long, subtract, convert to days
	    Long diff = (end.getTime() - start.getTime())/(1000 * 60 * 60 * 24);
	    diff = round(diff);

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
	    
	    if (ptdepimp == null) {
	    	ptdepimp = Port.loadById(session, ptdepimp_int);
	    }	    
	    voyage.setPtdepimp(ptdepimp);
	}
	
	/*
	 * tslmtimp:  Imputed total of slaves embarked for mortality calculation
	 * vymrtimp:  Imputed number of slaves died in  middle passage
	 * vymrtrat:  Slaves died on voyage/Slaves embarked	 * 
	 */	
	public void calculateTslmtimp() {
		float vymrtrat = 0f;
		int tslavesd_int =  0;
		int slaarriv_int = 0;
		
		Integer sladvoy = voyage.getSladvoy();
		Integer vymrtimp = sladvoy;
		
		Integer tslavesd = voyage.getTslavesd();
		if (tslavesd != null) {
			tslavesd_int = tslavesd.intValue();
		}
		
		Integer slaarriv = voyage.getSlaarriv();
		if (slaarriv != null) {
			 slaarriv_int =  slaarriv.intValue();
		}
		
		Integer tslmtimp = tslavesd;
		
		if (vymrtimp == null) {
			vymrtimp = tslavesd_int - slaarriv_int;
		}
		
		if ((tslavesd == null) && slaarriv_int > 0) {
			tslmtimp = slaarriv_int + vymrtimp;
		}
		if (vymrtimp >= 1) {
			vymrtrat = vymrtimp / tslmtimp;
		}
		
		voyage.setTslmtimp(tslmtimp);
		voyage.setVymrtrat(vymrtrat);
	}
	
	/*
	 * Calculates first set of region variables
	 */
	public void calculateValuesRegion1()
	{
		//Input variables for first region calculation
		Integer placcons = voyage.getPlaccons().getId().intValue();
		Integer placreg = voyage.getPlacreg().getId().intValue();
		Integer portdep = voyage.getPortdep().getId().intValue(); 
		Integer ptdepimp = voyage.getPtdepimp().getId().intValue();  //TODO imputed ptdepimp 
		Integer embport = voyage.getEmbport().getId().intValue(); 
		Integer embport2 = voyage.getEmbport2().getId().intValue();
		Integer plac1tra = voyage.getPlac1tra().getId().intValue(); 
		Integer plac2tra = voyage.getPlac2tra().getId().intValue();
		Integer plac3tra = voyage.getPlac3tra().getId().intValue();
		Integer majbuypt = voyage.getMajbuypt().getId().intValue(); //TODO imputed  majbuypt
		Integer mjbyptimp = voyage.getMjbyptimp().getId().intValue(); //TODO imputed mjbyptimp
		Integer arrport = voyage.getArrport().getId().intValue();
		Integer arrport2 = voyage.getArrport2().getId().intValue();
		Integer sla1port = voyage.getSla1port().getId().intValue();
		Integer adpsale1 = voyage.getAdpsale1().getId().intValue();
		Integer adpsale2 = voyage.getAdpsale2().getId().intValue();
		Integer majselpt = voyage.getMajselpt().getId().intValue(); //TODO imputed majselpt
		Integer mjslptimp = voyage.getMjslptimp().getId().intValue(); //TODO imputed mjslptimp
		Integer portret = voyage.getPortret().getId().intValue();
		
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
	    
	    //Get Return values
	    Integer constreg= (Integer) impVars.get(0);
	    Integer regisreg= (Integer) impVars.get(1);
	    Integer deptreg = (Integer) impVars.get(2);
	    Integer deptregimp = (Integer) impVars.get(3);
	    Integer embreg= (Integer) impVars.get(4);
	    Integer embreg2 = (Integer) impVars.get(5);
	    Integer regem1 = (Integer) impVars.get(6);
	    Integer regem2 = (Integer) impVars.get(7);
	    Integer regem3 = (Integer) impVars.get(8);
	    Integer majbuyreg = (Integer) impVars.get(9);
	    Integer majbyimp = (Integer) impVars.get(10);
	    Integer regarr = (Integer) impVars.get(11);
	    Integer regarr2 = (Integer) impVars.get(12);
	    Integer regdis1 = (Integer) impVars.get(13);
	    Integer regdis2 = (Integer) impVars.get(14);
	    Integer regdis3 = (Integer) impVars.get(15);
	    Integer majselrg = (Integer) impVars.get(16);
	    Integer mjselimp = (Integer) impVars.get(17);
	    Integer retrnreg= (Integer) impVars.get(18);
	    
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
		//Input variables for second region calculation
		Integer portdep = voyage.getPortdep().getId().intValue();
	    Integer ptdepimp = voyage.getPtdepimp().getId().intValue(); //TODO imputed ptdepimp
	    Integer mjbyptimp = voyage.getMjbyptimp().getId().intValue(); //TODO imputed mjbyptimp
	    Integer mjslptimp = voyage.getMjslptimp().getId().intValue(); //TODO imputed mjslptimp
	    Integer portret = voyage.getPortret().getId().intValue();
				
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
	    
	    //Get Return values
	    Integer deptreg1 = (Integer) impVars.get(0);
	    Integer deptregimp1 = (Integer) impVars.get(1);
	    Integer majbyimp1 = (Integer) impVars.get(2);
	    Integer mjselimp1 = (Integer) impVars.get(3);
	    Integer retrnreg1= (Integer) impVars.get(4);
	    
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
		//Get variables for calculation
		Integer tslavesd = voyage.getTslavesd();
		Integer xmimpflag =0; //TODO imputed  - get value from getter method once it is created
		Integer tslavesp = voyage.getTslavesp();
		Integer ncartot = getNcartot();
		Double slastot = getSlastot();
		Integer slaarriv = voyage.getSlaarriv();
		Long fate2 = voyage.getFate2().getId();
		
		//Variables to calculate
		 Double slaximp =0d;
		Double slamimp=0d;
		
		//Do ALL of the calculations
		if (tslavesd.doubleValue() >= 1d) {slaximp = tslavesd.doubleValue();}
		if (tslavesd==null && tslavesp >=1) {slaximp = tslavesp.doubleValue();}
		if (ncartot >= 1 && tslavesd==null && tslavesp==null) {slaximp = ncartot.doubleValue();}
		if (slaarriv >= 1) {slamimp = slaarriv.doubleValue();}
		if (slastot >= 1 && slaarriv==null) {slamimp = slastot;}

		if (xmimpflag == 127 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.165107561642471);}
		if (xmimpflag == 127 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null){slaximp = slamimp / (1-0.165107561642471);}
		if (xmimpflag == 127 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp=163.181286549708;}
		if (xmimpflag == 127 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null){slaximp =163.181286549708/ (1-0.165107561642471 );}
		if (xmimpflag == 128 && slaximp >= 1 && slaarriv==null && slastot==null){slamimp = slaximp - (slaximp*0.230972326367458);}
		if (xmimpflag == 128 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null){slaximp = slamimp / (1- 0.230972326367458);}
		if (xmimpflag == 128 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null){slamimp =241.774647887324;}
		if (xmimpflag == 128 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null){slaximp =241.774647887324/ (1-0.230972326367458 );}
		if (xmimpflag == 129 && slaximp >= 1 && slaarriv==null && slastot==null){slamimp = slaximp - (slaximp*0.218216262481124);}
		if (xmimpflag == 129 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null){slaximp = slamimp / (1- 0.218216262481124);}
		if (xmimpflag == 129 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null){slamimp =249.141527001862;}
		if (xmimpflag == 129 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null){slaximp =249.141527001862/ (1-0.218216262481124 );}
		if (xmimpflag == 130 && slaximp >= 1 && slaarriv==null && slastot==null){slamimp = slaximp - (slaximp*0.164154067860228);}
		if (xmimpflag == 130 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null){slaximp = slamimp / (1- 0.164154067860228);}
		if (xmimpflag == 130 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null){slamimp =227.680034129693;}
		if (xmimpflag == 130 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null){slaximp =227.680034129693/ (1-0.164154067860228 );}
		if (xmimpflag == 131 && slaximp >= 1 && slaarriv==null && slastot==null){slamimp = slaximp - (slaximp*0.153670852602567);}
		if (xmimpflag == 131 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null){slaximp = slamimp / (1- 0.153670852602567);}
		if (xmimpflag == 131 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null){slamimp =272.60549132948;}
		if (xmimpflag == 131 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null){slaximp =272.60549132948/ (1-0.153670852602567 );}
		if (xmimpflag == 132 && slaximp >= 1 && slaarriv==null && slastot==null){slamimp = slaximp - (slaximp*0.120410468186061);}
		if (xmimpflag == 132 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null){slaximp = slamimp / (1- 0.120410468186061);}
		if (xmimpflag == 132 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null){slamimp =268.071314102564;}
		if (xmimpflag == 132 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null){slaximp =268.071314102564/ (1-0.120410468186061 );}
		if (xmimpflag == 133 && slaximp >= 1 && slaarriv==null && slastot==null){slamimp = slaximp - (slaximp*0.126821090786133);}
		if (xmimpflag == 133 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null){slaximp = slamimp / (1- 0.126821090786133);}
		if (xmimpflag == 133 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null){slamimp =290.826654240447;}
		if (xmimpflag == 133 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null){slaximp =290.826654240447/ (1-0.126821090786133 );}
		if (xmimpflag == 134 && slaximp >= 1 && slaarriv==null && slastot==null){slamimp = slaximp - (slaximp*0.105799354866935);}
		if (xmimpflag == 134 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null){slaximp = slamimp / (1- 0.105799354866935);}
		if (xmimpflag == 134 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null){slamimp =225.932515337423;}
		if (xmimpflag == 134 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null){slaximp =225.932515337423/ (1-0.105799354866935 );}
		if (xmimpflag == 135 && slaximp >= 1 && slaarriv==null && slastot==null){slamimp = slaximp - (slaximp*0.114160782328086);}
		if (xmimpflag == 135 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null){slaximp = slamimp / (1- 0.114160782328086);}
		if (xmimpflag == 135 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null){slamimp =391.452674897119;}
		if (xmimpflag == 135 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null){slaximp =391.452674897119/ (1-0.114160782328086 );}
		if (xmimpflag == 136 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.170755559662484);}
		if (xmimpflag == 136 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.170755559662484);}
		if (xmimpflag == 136 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =480.734042553191;}
		if (xmimpflag == 136 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =480.734042553191/ (1-0.170755559662484 );}
		if (xmimpflag == 101 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.142415261804064);}
		if (xmimpflag == 101 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.142415261804064);}
		if (xmimpflag == 101 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =163.80243902439;}
		if (xmimpflag == 101 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =163.80243902439/ (1-0.142415261804064 );}
		if (xmimpflag == 102 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.104951847967976);}
		if (xmimpflag == 102 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.104951847967976);}
		if (xmimpflag == 102 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =153.265497076023;}
		if (xmimpflag == 102 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =153.265497076023/ (1-0.104951847967976 );}
		if (xmimpflag == 103 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.0794334443169517);}
		if (xmimpflag == 103 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.0794334443169517);}
		if (xmimpflag == 103 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =138.094017094017;}
		if (xmimpflag == 103 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =138.094017094017/ (1-0.0794334443169517 );}
		if (xmimpflag == 104 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.125269157905197);}
		if (xmimpflag == 104 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.125269157905197);}
		if (xmimpflag == 104 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =107.64;}
		if (xmimpflag == 104 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =107.64 - (107.64*0.125269157905197 );}
		if (xmimpflag == 105 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.0887057111704602);}
		if (xmimpflag == 105 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.0887057111704602);}
		if (xmimpflag == 105 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =191.988789237668;}
		if (xmimpflag == 105 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =191.988789237668/ (1-0.0887057111704602 );}
		if (xmimpflag == 106 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.0985396051230542);}
		if (xmimpflag == 106 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.0985396051230542);}
		if (xmimpflag == 106 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =188.140969162996;}
		if (xmimpflag == 106 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =188.140969162996/ (1-0.0985396051230542 );}
		if (xmimpflag == 107 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.199714956235816);}
		if (xmimpflag == 107 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.199714956235816);}
		if (xmimpflag == 107 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =239.363636363636;}
		if (xmimpflag == 107 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =239.363636363636/ (1-0.199714956235816 );}
		if (xmimpflag == 108 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.116764553914052);}
		if (xmimpflag == 108 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.116764553914052);}
		if (xmimpflag == 108 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =241.066480055983;}
		if (xmimpflag == 108 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =241.066480055983/ (1-0.116764553914052 );}
		if (xmimpflag == 110 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.217817105373686);}
		if (xmimpflag == 110 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.217817105373686);}
		if (xmimpflag == 110 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =321.139784946236;}
		if (xmimpflag == 110 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =321.139784946236/ (1-0.217817105373686 );}
		if (xmimpflag == 111 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.134584278813695);}
		if (xmimpflag == 111 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.134584278813695);}
		if (xmimpflag == 111 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =320.396527777777;}
		if (xmimpflag == 111 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =320.396527777777/ (1-0.134584278813695 );}
		if (xmimpflag == 112 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.0649564900465187);}
		if (xmimpflag == 112 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.0649564900465187);}
		if (xmimpflag == 112 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =302.919243986254;}
		if (xmimpflag == 112 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =302.919243986254/ (1-0.0649564900465187 );}
		if (xmimpflag == 113 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.294943293777566);}
		if (xmimpflag == 113 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.294943293777566);}
		if (xmimpflag == 113 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =178.191780821918;}
		if (xmimpflag == 113 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =178.191780821918/ (1-0.294943293777566 );}
		if (xmimpflag == 114 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.190466263797331);}
		if (xmimpflag == 114 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.190466263797331);}
		if (xmimpflag == 114 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =268.709993468321;}
		if (xmimpflag == 114 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =268.709993468321/ (1-0.190466263797331 );}
		if (xmimpflag == 115 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.165262209695588);}
		if (xmimpflag == 115 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.165262209695588);}
		if (xmimpflag == 115 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =265.480215827338;}
		if (xmimpflag == 115 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =265.480215827338/ (1-0.165262209695588 );}
		if (xmimpflag == 116 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.250590294065011);}
		if (xmimpflag == 116 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.250590294065011);}
		if (xmimpflag == 116 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =216.026607538803;}
		if (xmimpflag == 116 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =216.026607538803/ (1-0.250590294065011 );}
		if (xmimpflag == 117 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.0862116624182079);}
		if (xmimpflag == 117 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.0862116624182079);}
		if (xmimpflag == 117 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =341.979498861048;}
		if (xmimpflag == 117 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =341.979498861048/ (1-0.0862116624182079 );}
		if (xmimpflag == 118 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.0795782666543268);}
		if (xmimpflag == 118 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.0795782666543268);}
		if (xmimpflag == 118 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =382.444580777097;}
		if (xmimpflag == 118 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =382.444580777097/ (1-0.0795782666543268 );}
		if (xmimpflag == 120 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.100542298212489);}
		if (xmimpflag == 120 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.100542298212489);}
		if (xmimpflag == 120 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =191.62583518931;}
		if (xmimpflag == 120 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =191.62583518931/ (1-0.100542298212489 );}
		if (xmimpflag == 121 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.0690791392436498);}
		if (xmimpflag == 121 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.0690791392436498);}
		if (xmimpflag == 121 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =162.041666666667;}
		if (xmimpflag == 121 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =162.041666666667/ (1-0.0690791392436498 );}
		if (xmimpflag == 122 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.274602006426542);}
		if (xmimpflag == 122 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.274602006426542);}
		if (xmimpflag == 122 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =173.454545454545;}
		if (xmimpflag == 122 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =173.454545454545/ (1-0.274602006426542 );}
		if (xmimpflag == 123 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.274602006426542);}
		if (xmimpflag == 123 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.274602006426542);}
		if (xmimpflag == 123 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =255.028571428571;}
		if (xmimpflag == 123 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =255.028571428571/ (1-0.274602006426542 );}
		if (xmimpflag == 124 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.181330570603409);}
		if (xmimpflag == 124 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.181330570603409);}
		if (xmimpflag == 124 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =447.532008830022;}
		if (xmimpflag == 124 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =447.532008830022/ (1-0.181330570603409 );}
		if (xmimpflag == 1 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.255634697158707);}
		if (xmimpflag == 1 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.255634697158707);}
		if (xmimpflag == 1 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =166.401374570447;}
		if (xmimpflag == 1 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =166.401374570447/ (1-0.255634697158707 );}
		if (xmimpflag == 2 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.173114449095158);}
		if (xmimpflag == 2 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.173114449095158);}
		if (xmimpflag == 2 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =152.863945578231;}
		if (xmimpflag == 2 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =152.863945578231/ (1-0.173114449095158 );}
		if (xmimpflag == 3 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.191426939591589);}
		if (xmimpflag == 3 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.191426939591589);}
		if (xmimpflag == 3 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =250.179245283019;}
		if (xmimpflag == 3 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =250.179245283019/ (1-0.191426939591589 );}
		if (xmimpflag == 4 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.143739162059858);}
		if (xmimpflag == 4 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.143739162059858);}
		if (xmimpflag == 4 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =273.896226415094;}
		if (xmimpflag == 4 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =273.896226415094 - (273.896226415094*0.143739162059858 );}
		if (xmimpflag == 5 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.0703329947332674);}
		if (xmimpflag == 5 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.0703329947332674);}
		if (xmimpflag == 5 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =380.04854368932;}
		if (xmimpflag == 5 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =380.04854368932 - (380.04854368932*0.0703329947332674 );}
		if (xmimpflag == 6 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.117444418143106);}
		if (xmimpflag == 6 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.117444418143106);}
		if (xmimpflag == 6 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =305.868020304568;}
		if (xmimpflag == 6 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =305.868020304568/ (1-0.117444418143106 );}
		if (xmimpflag == 7 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.126779394689057);}
		if (xmimpflag == 7 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.126779394689057);}
		if (xmimpflag == 7 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =265.88;}
		if (xmimpflag == 7 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =265.88 - (265.88*0.126779394689057 );}
		if (xmimpflag == 8 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.189011301766662);}
		if (xmimpflag == 8 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.189011301766662);}
		if (xmimpflag == 8 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =281.325;}
		if (xmimpflag == 8 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =281.325/ (1-0.189011301766662 );}
		if (xmimpflag == 9 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.140365224720275);}
		if (xmimpflag == 9 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.140365224720275);}
		if (xmimpflag == 9 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =402.502202643172;}
		if (xmimpflag == 9 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =402.502202643172/ (1-0.140365224720275 );}
		if (xmimpflag == 10 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.107188743129005);}
		if (xmimpflag == 10 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.107188743129005);}
		if (xmimpflag == 10 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =277.059842519684;}
		if (xmimpflag == 10 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =277.059842519684/ (1-0.107188743129005 );}
		if (xmimpflag == 11 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.126901348540731);}
		if (xmimpflag == 11 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.126901348540731);}
		if (xmimpflag == 11 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =355.810945273632;}
		if (xmimpflag == 11 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =355.810945273632/ (1-0.126901348540731 );}
		if (xmimpflag == 12 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.0655772248600899);}
		if (xmimpflag == 12 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.0655772248600899);}
		if (xmimpflag == 12 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =309.533898305085;}
		if (xmimpflag == 12 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =309.533898305085/ (1-0.0655772248600899 );}
		if (xmimpflag == 13 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.0778021073375869);}
		if (xmimpflag == 13 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.0778021073375869);}
		if (xmimpflag == 13 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =305.812154696132;}
		if (xmimpflag == 13 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =305.812154696132/ (1-0.0778021073375869 );}
		if (xmimpflag == 14 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.0654921908875572);}
		if (xmimpflag == 14 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.0654921908875572);}
		if (xmimpflag == 14 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =285.054112554113;}
		if (xmimpflag == 14 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =285.054112554113/ (1-0.0654921908875572 );}
		if (xmimpflag == 15 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.0671696102131247);}
		if (xmimpflag == 15 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.0671696102131247);}
		if (xmimpflag == 15 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =361.638059701493;}
		if (xmimpflag == 15 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =361.638059701493/ (1-0.0671696102131247 );}
		if (xmimpflag == 16 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.371414750110571);}
		if (xmimpflag == 16 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.371414750110571);}
		if (xmimpflag == 16 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =239.9;}
		if (xmimpflag == 16 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =239.9/ (1-0.371414750110571 );}
		if (xmimpflag == 157 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.230610260687796);}
		if (xmimpflag == 157 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.230610260687796);}
		if (xmimpflag == 157 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =139.029411764706;}
		if (xmimpflag == 157 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =139.029411764706/ (1-0.230610260687796 );}
		if (xmimpflag == 159 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.154487726688789);}
		if (xmimpflag == 159 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.154487726688789);}
		if (xmimpflag == 159 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =245.12676056338;}
		if (xmimpflag == 159 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =245.12676056338/ (1-0.154487726688789 );}
		if (xmimpflag == 99 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.166050441674744);}
		if (xmimpflag == 99 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.166050441674744);}
		if (xmimpflag == 99 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =125.619750283768;}
		if (xmimpflag == 99 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =125.619750283768/ (1-0.166050441674744 );}
		if (xmimpflag == 100 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.178717812379779);}
		if (xmimpflag == 100 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.178717812379779);}
		if (xmimpflag == 100 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =565.645161290322;}
		if (xmimpflag == 100 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =565.645161290322/ (1-0.178717812379779 );}
		if (xmimpflag == 17 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.0557746478873239);}
		if (xmimpflag == 17 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.0557746478873239);}
		if (xmimpflag == 17 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =148.882352941176;}
		if (xmimpflag == 17 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =148.882352941176/ (1-0.0557746478873239 );}
		if (xmimpflag == 98 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.126563817175912);}
		if (xmimpflag == 98 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.126563817175912);}
		if (xmimpflag == 98 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =132.596685082873;}
		if (xmimpflag == 98 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =132.596685082873/ (1-0.126563817175912 );}
		if (xmimpflag == 18 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.093544030879478);}
		if (xmimpflag == 18 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.093544030879478);}
		if (xmimpflag == 18 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =184.486013986014;}
		if (xmimpflag == 18 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =184.486013986014/ (1-0.093544030879478 );}
		if (xmimpflag == 19 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.0985982521761244);}
		if (xmimpflag == 19 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.0985982521761244);}
		if (xmimpflag == 19 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =230.298469387755;}
		if (xmimpflag == 19 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =230.298469387755/ (1-0.0985982521761244 );}
		if (xmimpflag == 20 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.0944678720322908);}
		if (xmimpflag == 20 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.0944678720322908);}
		if (xmimpflag == 20 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =444.290145985401;}
		if (xmimpflag == 20 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =444.290145985401/ (1-0.0944678720322908 );}
		if (xmimpflag == 21 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.167379623404603);}
		if (xmimpflag == 21 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.167379623404603);}
		if (xmimpflag == 21 &&  tslavesd==null&&  tslavesp==null&&  ncartot==null&&  slaarriv==null&& slastot==null) {slamimp =492.946428571429;}
		if (xmimpflag == 21 &&  tslavesd==null&&  tslavesp==null&&  ncartot==null&&  slaarriv==null&& slastot==null) {slaximp =492.946428571429/ (1-0.167379623404603 );}
		if (xmimpflag == 22 && slaximp >= 1 &&  slaarriv==null&& slastot==null) {slamimp = slaximp - (slaximp*0.183801786070534);}
		if (xmimpflag == 22 && slamimp >= 1 &&  tslavesd==null&&  tslavesp==null&& ncartot==null) {slaximp = slamimp / (1- 0.183801786070534);}
		if (xmimpflag == 22 &&  tslavesd==null&&  tslavesp==null&&  ncartot==null&&  slaarriv==null&& slastot==null) {slamimp =91.9594594594595;}
		if (xmimpflag == 22 &&  tslavesd==null&&  tslavesp==null&&  ncartot==null&&  slaarriv==null&& slastot==null) {slaximp =91.9594594594595/ (1-0.183801786070534 );}
		if (xmimpflag == 23 && slaximp >= 1 &&  slaarriv==null&& slastot==null) {slamimp = slaximp - (slaximp*0.102358180948044);}
		if (xmimpflag == 23 && slamimp >= 1 &&  tslavesd==null&&  tslavesp==null&& ncartot==null) {slaximp = slamimp / (1- 0.102358180948044);}
		if (xmimpflag == 23 &&  tslavesd==null&&  tslavesp==null&&  ncartot==null&&  slaarriv==null&& slastot==null) {slamimp =95.972972972973;}
		if (xmimpflag == 23 &&  tslavesd==null&&  tslavesp==null&&  ncartot==null&&  slaarriv==null&& slastot==null) {slaximp =95.972972972973/ (1-0.102358180948044 );}
		if (xmimpflag == 24 && slaximp >= 1 &&  slaarriv==null&& slastot==null) {slamimp = slaximp - (slaximp*0.122708750828674);}
		if (xmimpflag == 24 && slamimp >= 1 &&  tslavesd==null&&  tslavesp==null&& ncartot==null) {slaximp = slamimp / (1- 0.122708750828674);}
		if (xmimpflag == 24 &&  tslavesd==null&&  tslavesp==null&&  ncartot==null&&  slaarriv==null&& slastot==null) {slamimp =146.31;}
		if (xmimpflag == 24 &&  tslavesd==null&&  tslavesp==null&&  ncartot==null&&  slaarriv==null&& slastot==null) {slaximp =146.31/ (1-0.122708750828674 );}
		if (xmimpflag == 25 && slaximp >= 1 &&  slaarriv==null&& slastot==null) {slamimp = slaximp - (slaximp*0.101742168136026);}
		if (xmimpflag == 25 && slamimp >= 1 &&  tslavesd==null&&  tslavesp==null&& ncartot==null) {slaximp = slamimp / (1- 0.101742168136026);}
		if (xmimpflag == 25 &&  tslavesd==null&&  tslavesp==null&&  ncartot==null&&  slaarriv==null&& slastot==null) {slamimp =279.357142857143;}
		if (xmimpflag == 25 &&  tslavesd==null&&  tslavesp==null&&  ncartot==null&&  slaarriv==null&& slastot==null) {slaximp =279.357142857143/ (1-0.101742168136026 );}
		if (xmimpflag == 26 && slaximp >= 1 &&  slaarriv==null&& slastot==null) {slamimp = slaximp - (slaximp*0.0830808603000646);}
		if (xmimpflag == 26 && slamimp >= 1 &&  tslavesd==null&&  tslavesp==null&& ncartot==null) {slaximp = slamimp / (1- 0.0830808603000646);}
		if (xmimpflag == 26 &&  tslavesd==null&&  tslavesp==null&&  ncartot==null&&  slaarriv==null&& slastot==null) {slamimp =341.5;}
		if (xmimpflag == 26 &&  tslavesd==null&&  tslavesp==null&&  ncartot==null&&  slaarriv==null&& slastot==null) {slaximp =341.5/ (1-0.0830808603000646 );}
		if (xmimpflag == 27 && slaximp >= 1 &&  slaarriv==null&& slastot==null) {slamimp = slaximp - (slaximp*0.0951735364832193);}
		if (xmimpflag == 27 && slamimp >= 1 &&  tslavesd==null&&  tslavesp==null&& ncartot==null) {slaximp = slamimp / (1- 0.0951735364832193);}
		if (xmimpflag == 27 &&  tslavesd==null&&  tslavesp==null&&  ncartot==null&&  slaarriv==null&& slastot==null) {slaximp =335.546666666667;}
		if (xmimpflag == 27 &&  tslavesd==null&&  tslavesp==null&&  ncartot==null&&  slaarriv==null&& slastot==null) {slamimp =335.546666666667 - (335.546666666667*0.0951735364832193 );}
		if (xmimpflag == 28 && slaximp >= 1 &&  slaarriv==null&& slastot==null) {slamimp = slaximp - (slaximp*0.0599984615282753);}
		if (xmimpflag == 28 && slamimp >= 1 &&  tslavesd==null&&  tslavesp==null&& ncartot==null) {slaximp = slamimp / (1- 0.0599984615282753);}
		if (xmimpflag == 28 &&  tslavesd==null&&  tslavesp==null&&  ncartot==null&&  slaarriv==null&& slastot==null) {slaximp =348.926267281106;}
		if (xmimpflag == 28 &&  tslavesd==null&&  tslavesp==null&&  ncartot==null&&  slaarriv==null&& slastot==null) {slamimp =348.926267281106 - (348.926267281106*0.0599984615282753 );}
		if (xmimpflag == 29 && slaximp >= 1 &&  slaarriv==null&& slastot==null) {slamimp = slaximp - (slaximp*0.0849037398486349);}
		if (xmimpflag == 29 && slamimp >= 1 &&  tslavesd==null&&  tslavesp==null&& ncartot==null) {slaximp = slamimp / (1- 0.0849037398486349);}
		if (xmimpflag == 29 &&  tslavesd==null&&  tslavesp==null&&  ncartot==null&&  slaarriv==null&& slastot==null) {slamimp =323.539358600583;}
		if (xmimpflag == 29 &&  tslavesd==null&&  tslavesp==null&&  ncartot==null&&  slaarriv==null&& slastot==null) {slaximp =323.539358600583/ (1-0.0849037398486349 );}
		if (xmimpflag == 30 && slaximp >= 1 &&  slaarriv==null&& slastot==null) {slamimp = slaximp - (slaximp*0.0831292966753462);}
		if (xmimpflag == 30 && slamimp >= 1 &&  tslavesd==null&&  tslavesp==null&& ncartot==null) {slaximp = slamimp / (1- 0.0831292966753462);}
		if (xmimpflag == 30 &&  tslavesd==null&&  tslavesp==null&&  ncartot==null&&  slaarriv==null&& slastot==null) {slamimp =435.738461538461;}
		if (xmimpflag == 30 &&  tslavesd==null&&  tslavesp==null&&  ncartot==null&&  slaarriv==null&& slastot==null) {slaximp =435.738461538461/ (1-0.0831292966753462 );}
		if (xmimpflag == 31 && slaximp >= 1 &&  slaarriv==null&& slastot==null) {slamimp = slaximp - (slaximp*0.154603810637904);}
		if (xmimpflag == 31 && slamimp >= 1 &&  tslavesd==null&&  tslavesp==null&& ncartot==null) {slaximp = slamimp / (1- 0.154603810637904);}
		if (xmimpflag == 31 &&  tslavesd==null&&  tslavesp==null&&  ncartot==null&&  slaarriv==null&& slastot==null) {slamimp =221.279220779221;}
		if (xmimpflag == 31 &&  tslavesd==null&&  tslavesp==null&&  ncartot==null&&  slaarriv==null&& slastot==null) {slaximp =221.279220779221/ (1-0.154603810637904 );}
		if (xmimpflag == 32 && slaximp >= 1 &&  slaarriv==null&& slastot==null) {slamimp = slaximp - (slaximp*0.169381440464976);}
		if (xmimpflag == 32 && slamimp >= 1 &&  tslavesd==null&&  tslavesp==null&& ncartot==null) {slaximp = slamimp / (1- 0.169381440464976);}
		if (xmimpflag == 32 &&  tslavesd==null&&  tslavesp==null&&  ncartot==null&&  slaarriv==null&& slastot==null) {slamimp =296.593103448276;}
		if (xmimpflag == 32 &&  tslavesd==null&&  tslavesp==null&&  ncartot==null&&  slaarriv==null&& slastot==null) {slaximp =296.593103448276/ (1-0.169381440464976 );}
		if (xmimpflag == 33 && slaximp >= 1 &&  slaarriv==null&& slastot==null) {slamimp = slaximp - (slaximp*0.183684529291394);}
		if (xmimpflag == 33 && slamimp >= 1 &&  tslavesd==null&&  tslavesp==null&& ncartot==null) {slaximp = slamimp / (1- 0.183684529291394);}
		if (xmimpflag == 33 &&  tslavesd==null&&  tslavesp==null&&  ncartot==null&&  slaarriv==null&& slastot==null) {slamimp =281.452966714906;}
		if (xmimpflag == 33 &&  tslavesd==null&&  tslavesp==null&&  ncartot==null&&  slaarriv==null&& slastot==null) {slaximp =281.452966714906/ (1-0.183684529291394 );}
		if (xmimpflag == 34 && slaximp >= 1 &&  slaarriv==null&& slastot==null) {slamimp = slaximp - (slaximp*0.0864964921326426);}
		if (xmimpflag == 34 && slamimp >= 1 &&  tslavesd==null&&  tslavesp==null&& ncartot==null) {slaximp = slamimp / (1- 0.0864964921326426);}
		if (xmimpflag == 34 &&  tslavesd==null&&  tslavesp==null&&  ncartot==null&&  slaarriv==null&& slastot==null) {slamimp =325.652360515021;}
		if (xmimpflag == 34 &&  tslavesd==null&&  tslavesp==null&&  ncartot==null&&  slaarriv==null&& slastot==null) {slaximp =325.652360515021/ (1-0.0864964921326426 );}
		if (xmimpflag ==  35 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.176037224384829);}
		if (xmimpflag == 35 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.176037224384829);}
		if (xmimpflag ==  35 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =272.474358974359;}
		if (xmimpflag ==  35 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =272.474358974359/ (1-0.176037224384829 );}
		if (xmimpflag ==  36 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.116937605450612);}
		if (xmimpflag ==  36 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.116937605450612);}
		if (xmimpflag ==  36 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =556.677419354839;}
		if (xmimpflag ==  36 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =556.677419354839/ (1-0.116937605450612 );}
		if (xmimpflag ==  37 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.172812495199871);}
		if (xmimpflag ==  37 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.172812495199871);}
		if (xmimpflag ==  37 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =890.470588235294;}
		if (xmimpflag ==  37 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =890.470588235294/ (1-0.172812495199871 );}
		if (xmimpflag ==  38 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.105087524949968);}
		if (xmimpflag ==  38 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.105087524949968);}
		if (xmimpflag ==  38 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =335.813953488372;}
		if (xmimpflag ==  38 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =335.813953488372/ (1-0.105087524949968 );}
		if (xmimpflag ==  39 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.0856667000685018);}
		if (xmimpflag ==  39 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.0856667000685018);}
		if (xmimpflag ==  39 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =257.263157894737;}
		if (xmimpflag ==  39 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =257.263157894737/ (1-0.0856667000685018 );}
		if (xmimpflag ==  40 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.0865650987499053);}
		if (xmimpflag ==  40 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.0865650987499053);}
		if (xmimpflag ==  40 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =328.195266272189;}
		if (xmimpflag ==  40 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =328.195266272189/ (1-0.0865650987499053 );}
		if (xmimpflag ==  41 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.171814252005436);}
		if (xmimpflag ==  41 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.171814252005436);}
		if (xmimpflag ==  41 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =129.145454545455;}
		if (xmimpflag ==  41 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =129.145454545455/ (1-0.171814252005436 );}
		if (xmimpflag ==  42 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.0610387045813586);}
		if (xmimpflag ==  42 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.0610387045813586);}
		if (xmimpflag ==  42 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =158.1;}
		if (xmimpflag ==  42 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =158.1/ (1-0.0610387045813586 );}
		if (xmimpflag ==  43 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.159823459162871);}
		if (xmimpflag ==  43 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.159823459162871);}
		if (xmimpflag ==  43 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =247.759689922481;}
		if (xmimpflag ==  43 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =247.759689922481/ (1-0.159823459162871 );}
		if (xmimpflag ==  44 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.0988853555387519);}
		if (xmimpflag ==  44 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.0988853555387519);}
		if (xmimpflag ==  44 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =363d;}
		if (xmimpflag ==  44 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =363/ (1-0.0988853555387519 );}
		if (xmimpflag ==  45 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.0904513085721602);}
		if (xmimpflag ==  45 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.0904513085721602);}
		if (xmimpflag ==  45 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =466.25641025641;}
		if (xmimpflag ==  45 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =466.25641025641/ (1-0.0904513085721602 );}
		if (xmimpflag ==  46 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.082310278477633);}
		if (xmimpflag ==  46 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.082310278477633);}
		if (xmimpflag ==  46 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =159.810810810811;}
		if (xmimpflag ==  46 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =159.810810810811/ (1-0.082310278477633 );}
		if (xmimpflag ==  47 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.104714300552102);}
		if (xmimpflag ==  47 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.104714300552102);}
		if (xmimpflag ==  47 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =638.25;}
		if (xmimpflag ==  47 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =638.25/ (1-0.104714300552102 );}
		if (xmimpflag ==  48 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.193439630544956);}
		if (xmimpflag ==  48 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.193439630544956);}
		if (xmimpflag ==  48 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =608.392156862745;}
		if (xmimpflag ==  48 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =608.392156862745/ (1-0.193439630544956 );}
		if (xmimpflag ==  49 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.145583038352611);}
		if (xmimpflag ==  49 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.145583038352611);}
		if (xmimpflag ==  49 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =428.888888888889;}
		if (xmimpflag ==  49 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =428.888888888889/ (1-0.145583038352611 );}
		if (xmimpflag ==  50 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.233333333333333);}
		if (xmimpflag ==  50 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.233333333333333);}
		if (xmimpflag ==  50 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =270.846153846154;}
		if (xmimpflag ==  50 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =270.846153846154/ (1-0.233333333333333 );}
		if (xmimpflag ==  51 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.179223522528989);}
		if (xmimpflag ==  51 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.179223522528989);}
		if (xmimpflag ==  51 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =229.64;}
		if (xmimpflag ==  51 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =229.64/ (1-0.179223522528989 );}
		if (xmimpflag ==  52 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.0819156347249732);}
		if (xmimpflag ==  52 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.0819156347249732);}
		if (xmimpflag ==  52 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =290.164383561644;}
		if (xmimpflag ==  52 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =290.164383561644 - (290.164383561644*0.0819156347249732 );}
		if (xmimpflag ==  53 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.0540922242825536);}
		if (xmimpflag ==  53 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.0540922242825536);}
		if (xmimpflag ==  53 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =256.548387096774;}
		if (xmimpflag ==  53 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =256.548387096774/ (1-0.0540922242825536 );}
		if (xmimpflag ==  54 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.0913651933726713);}
		if (xmimpflag ==  54 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.0913651933726713);}
		if (xmimpflag ==  54 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =216.907894736842;}
		if (xmimpflag ==  54 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =216.907894736842/ (1-0.0913651933726713 );}
		if (xmimpflag ==  55 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.0604022380426763);}
		if (xmimpflag ==  55 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.0604022380426763);}
		if (xmimpflag ==  55 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =241.461538461538;}
		if (xmimpflag ==  55 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =241.461538461538/ (1-0.0604022380426763 );}
		if (xmimpflag ==  56 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.0542026549646127);}
		if (xmimpflag ==  56 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.0542026549646127);}
		if (xmimpflag ==  56 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =340.230769230769;}
		if (xmimpflag ==  56 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =340.230769230769/ (1-0.0542026549646127 );}
		if (xmimpflag ==  57 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.0974564330758702);}
		if (xmimpflag ==  57 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.0974564330758702);}
		if (xmimpflag ==  57 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =516.45;}
		if (xmimpflag ==  57 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =516.45/ (1-0.0974564330758702 );}
		if (xmimpflag ==  58 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.162886379968412);}
		if (xmimpflag ==  58 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.162886379968412);}
		if (xmimpflag ==  58 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =447.518072289157;}
		if (xmimpflag ==  58 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =447.518072289157 - (447.518072289157*0.162886379968412 );}
		if (xmimpflag ==  59 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.0561646667118922);}
		if (xmimpflag ==  59 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.0561646667118922);}
		if (xmimpflag ==  59 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =152.923076923077;}
		if (xmimpflag ==  59 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =152.923076923077/ (1-0.0561646667118922 );}
		if (xmimpflag ==  60 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.133468501803896);}
		if (xmimpflag ==  60 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.133468501803896);}
		if (xmimpflag ==  60 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =403.292993630573;}
		if (xmimpflag ==  60 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =403.292993630573/ (1-0.133468501803896 );}
		if (xmimpflag ==  61 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.106708705390018);}
		if (xmimpflag ==  61 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.106708705390018);}
		if (xmimpflag ==  61 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =285.644444444444;}
		if (xmimpflag ==  61 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =285.644444444444/ (1-0.106708705390018 );}
		if (xmimpflag ==  62 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.0785278768682708);}
		if (xmimpflag ==  62 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.0785278768682708);}
		if (xmimpflag ==  62 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =335.658227848101;}
		if (xmimpflag ==  62 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =335.658227848101/ (1-0.0785278768682708 );}
		if (xmimpflag ==  63 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.107782269167156);}
		if (xmimpflag ==  63 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.107782269167156);}
		if (xmimpflag ==  63 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =472.267857142857;}
		if (xmimpflag ==  63 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =472.267857142857/ (1-0.107782269167156 );}
		if (xmimpflag ==  160 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.0779281672325541);}
		if (xmimpflag ==  160 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.0779281672325541);}
		if (xmimpflag ==  160 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =536.842857142857;}
		if (xmimpflag ==  160 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =536.842857142857/ (1-0.0779281672325541 );}
		if (xmimpflag ==  65 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.115409873680179);}
		if (xmimpflag ==  65 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.115409873680179);}
		if (xmimpflag ==  65 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =103.376146788991;}
		if (xmimpflag ==  65 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =103.376146788991/ (1-0.115409873680179 );}
		if (xmimpflag ==  66 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.207088877726936);}
		if (xmimpflag ==  66 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.207088877726936);}
		if (xmimpflag ==  66 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =68.1506849315068;}
		if (xmimpflag ==  66 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =68.1506849315068/ (1-0.207088877726936 );}
		if (xmimpflag ==  67 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.110922605367631);}
		if (xmimpflag ==  67 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.110922605367631);}
		if (xmimpflag ==  67 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =80.0491803278688;}
		if (xmimpflag ==  67 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =80.0491803278688/ (1-0.110922605367631 );}
		if (xmimpflag ==  68 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.127935729778166);}
		if (xmimpflag ==  68 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.127935729778166);}
		if (xmimpflag ==  68 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =84d;}
		if (xmimpflag ==  68 && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =84 - (84*0.127935729778166 );}
		if (xmimpflag ==  69 && slaximp >= 1 && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.206358225584424);}
		if (xmimpflag ==  69 && slamimp >= 1 && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.206358225584424);}
		if (xmimpflag == 69 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =1004.47058823529;}
		if (xmimpflag == 69 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =1004.47058823529/ (1-0.206358225584424 );}
		if (xmimpflag == 70 && slaximp >= 1 &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.142775407154303);}
		if (xmimpflag == 70 && slamimp >= 1 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.142775407154303);}
		if (xmimpflag == 70 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =311.222222222222;}
		if (xmimpflag == 70 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =311.222222222222/ (1-0.142775407154303 );}
		if (xmimpflag == 71 && slaximp >= 1 &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.106323148232566);}
		if (xmimpflag == 71 && slamimp >= 1 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.106323148232566);}
		if (xmimpflag == 71 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =310.39837398374;}
		if (xmimpflag == 71 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =310.39837398374/ (1-0.106323148232566 );}
		if (xmimpflag == 97 && slaximp >= 1 &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.138965456634756);}
		if (xmimpflag == 97 && slamimp >= 1 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.138965456634756);}
		if (xmimpflag == 97 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =259.21875;}
		if (xmimpflag == 97 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =259.21875/ (1-0.138965456634756 );}
		if (xmimpflag == 72 && slaximp >= 1 &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.169436742362705);}
		if (xmimpflag == 72 && slamimp >= 1 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.169436742362705);}
		if (xmimpflag == 72 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =265.325842696629;}
		if (xmimpflag == 72 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =265.325842696629/ (1-0.169436742362705 );}
		if (xmimpflag == 85 && slaximp >= 1 &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.339905284604731);}
		if (xmimpflag == 85 && slamimp >= 1 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.339905284604731);}
		if (xmimpflag == 85 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =563.333333333333;}
		if (xmimpflag == 85 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =563.333333333333/ (1-0.339905284604731 );}
		if (xmimpflag == 73 && slaximp >= 1 &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.129605450439467);}
		if (xmimpflag == 73 && slamimp >= 1 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.129605450439467);}
		if (xmimpflag == 73 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =407.289473684211;}
		if (xmimpflag == 73 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =407.289473684211/ (1-0.129605450439467 );}
		if (xmimpflag == 74 && slaximp >= 1 &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.0794384325299229);}
		if (xmimpflag == 74 && slamimp >= 1 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.0794384325299229);}
		if (xmimpflag == 74 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =117.137931034483;}
		if (xmimpflag == 74 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =117.137931034483/ (1-0.0794384325299229 );}
		if (xmimpflag == 75 && slaximp >= 1 &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.189369734252207);}
		if (xmimpflag == 75 && slamimp >= 1 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.189369734252207);}
		if (xmimpflag == 75 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =192.772020725389;}
		if (xmimpflag == 75 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =192.772020725389/ (1-0.189369734252207 );}
		if (xmimpflag == 76 && slaximp >= 1 &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.131187789757565);}
		if (xmimpflag == 76 && slamimp >= 1 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.131187789757565);}
		if (xmimpflag == 76 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =199.041666666667;}
		if (xmimpflag == 76 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =199.041666666667/ (1-0.131187789757565 );}
		if (xmimpflag == 77 && slaximp >= 1 &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.136342992788614);}
		if (xmimpflag == 77 && slamimp >= 1 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.136342992788614);}
		if (xmimpflag == 77 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =186.407894736842;}
		if (xmimpflag == 77 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =186.407894736842/ (1-0.136342992788614 );}
		if (xmimpflag == 78 && slaximp >= 1 &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.103049659988616);}
		if (xmimpflag == 78 && slamimp >= 1 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.103049659988616);}
		if (xmimpflag == 78 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =155.470588235294;}
		if (xmimpflag == 78 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =155.470588235294/ (1-0.103049659988616 );}
		if (xmimpflag == 79 && slaximp >= 1 &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.35);}
		if (xmimpflag == 79 && slamimp >= 1 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.35);}
		if (xmimpflag == 79 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =193.74358974359;}
		if (xmimpflag == 79 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =193.74358974359/ (1-0.35 );}
		if (xmimpflag == 80 && slaximp >= 1 &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.0732085200996002);}
		if (xmimpflag == 80 && slamimp >= 1 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.0732085200996002);}
		if (xmimpflag == 80 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =249.692307692308;}
		if (xmimpflag == 80 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =249.692307692308/ (1-0.0732085200996002 );}
		if (xmimpflag == 81 && slaximp >= 1 &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.0934359066589073);}
		if (xmimpflag == 81 && slamimp >= 1 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.0934359066589073);}
		if (xmimpflag == 81 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =352.952806122449;}
		if (xmimpflag == 81 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =352.952806122449/ (1-0.0934359066589073 );}
		if (xmimpflag == 82 && slaximp >= 1 &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.07182740558555);}
		if (xmimpflag == 82 && slamimp >= 1 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.07182740558555);}
		if (xmimpflag == 82 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =419.619047619047;}
		if (xmimpflag == 82 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =419.619047619047/ (1-0.07182740558555 );}
		if (xmimpflag == 83 && slaximp >= 1 &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.0956449943871365);}
		if (xmimpflag == 83 && slamimp >= 1 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.0956449943871365);}
		if (xmimpflag == 83 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =304.5625;}
		if (xmimpflag == 83 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =304.5625 - (304.5625*0.0956449943871365 );}
		if (xmimpflag == 84 && slaximp >= 1 &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.163929225997462);}
		if (xmimpflag == 84 && slamimp >= 1 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.163929225997462);}
		if (xmimpflag == 84 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =319.285714285714;}
		if (xmimpflag == 84 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =319.285714285714/ (1-0.163929225997462 );}
		if (xmimpflag == 86 && slaximp >= 1 &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.112733293827202);}
		if (xmimpflag == 86 && slamimp >= 1 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.112733293827202);}
		if (xmimpflag == 86 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =129.277777777778;}
		if (xmimpflag == 86 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =129.277777777778/ (1-0.112733293827202 );}
		if (xmimpflag == 87 && slaximp >= 1 &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.0655504344628028);}
		if (xmimpflag == 87 && slamimp >= 1 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.0655504344628028);}
		if (xmimpflag == 87 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =211d;}
		if (xmimpflag == 87 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =211/ (1-0.0655504344628028 );}
		if (xmimpflag == 88 && slaximp >= 1 &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.198929221794951);}
		if (xmimpflag == 88 && slamimp >= 1 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.198929221794951);}
		if (xmimpflag == 88 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =296.473684210526;}
		if (xmimpflag == 88 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =296.473684210526 - (296.473684210526*0.198929221794951 );}
		if (xmimpflag == 89 && slaximp >= 1 &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.107517933823928);}
		if (xmimpflag == 89 && slamimp >= 1 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.107517933823928);}
		if (xmimpflag == 89 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =281.958333333333;}
		if (xmimpflag == 89 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =281.958333333333/ (1-0.107517933823928 );}
		if (xmimpflag == 90 && slaximp >= 1 &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.028250184258012);}
		if (xmimpflag == 90 && slamimp >= 1 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.028250184258012);}
		if (xmimpflag == 90 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =208.341176470588;}
		if (xmimpflag == 90 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =208.341176470588/ (1-0.028250184258012 );}
		if (xmimpflag == 91 && slaximp >= 1 &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.0487771272192143);}
		if (xmimpflag == 91 && slamimp >= 1 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.0487771272192143);}
		if (xmimpflag == 91 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =267.896551724138;}
		if (xmimpflag == 91 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =267.896551724138/ (1-0.0487771272192143 );}
		if (xmimpflag == 92 && slaximp >= 1 &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.111975986975987);}
		if (xmimpflag == 92 && slamimp >= 1 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.111975986975987);}
		if (xmimpflag == 92 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =328.555555555556;}
		if (xmimpflag == 92 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =328.555555555556/ (1-0.111975986975987 );}
		if (xmimpflag == 93 && slaximp >= 1 &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.0979648763988006);}
		if (xmimpflag == 93 && slamimp >= 1 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.0979648763988006);}
		if (xmimpflag == 93 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =101.111111111111;}
		if (xmimpflag == 93 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =101.111111111111/ (1-0.0979648763988006 );}
		if (xmimpflag == 94 && slaximp >= 1 &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.297737659966491);}
		if (xmimpflag == 94 && slamimp >= 1 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.297737659966491);}
		if (xmimpflag == 94 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =319.733333333333;}
		if (xmimpflag == 94 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =319.733333333333/ (1-0.297737659966491 );}
		if (xmimpflag == 95 && slaximp >= 1 &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.0220048899755501);}
		if (xmimpflag == 95 && slamimp >= 1 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.0220048899755501);}
		if (xmimpflag == 95 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =220.428571428571;}
		if (xmimpflag == 95 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =220.428571428571/ (1-0.0220048899755501 );}
		if (xmimpflag == 96 && slaximp >= 1 &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0);}
		if (xmimpflag == 96 && slamimp >= 1 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0);}
		if (xmimpflag == 96 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =433d;}
		if (xmimpflag == 96 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =433d/ (1-0 );}
		if (xmimpflag == 137 && slaximp >= 1 &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.12659407459354);}
		if (xmimpflag == 137 && slamimp >= 1 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.12659407459354);}
		if (xmimpflag == 137 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =104.986301369863;}
		if (xmimpflag == 137 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =104.986301369863/ (1-0.12659407459354 );}
		if (xmimpflag == 138 && slaximp >= 1 &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.179201806454531);}
		if (xmimpflag == 138 && slamimp >= 1 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.179201806454531);}
		if (xmimpflag == 138 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =108.37037037037;}
		if (xmimpflag == 138 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =108.37037037037 - (108.37037037037*0.179201806454531 );}
		if (xmimpflag == 139 && slaximp >= 1 &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.162003845923261);}
		if (xmimpflag == 139 && slamimp >= 1 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.162003845923261);}
		if (xmimpflag == 139 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =128.438775510204;}
		if (xmimpflag == 139 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =128.438775510204/ (1-0.162003845923261 );}
		if (xmimpflag == 140 && slaximp >= 1 &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.171264386321147);}
		if (xmimpflag == 140 && slamimp >= 1 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.171264386321147);}
		if (xmimpflag == 140 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =557.6;}
		if (xmimpflag == 140 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =557.6/ (1-0.171264386321147 );}
		if (xmimpflag == 141 && slaximp >= 1 &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.213152374545978);}
		if (xmimpflag == 141 && slamimp >= 1 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.213152374545978);}
		if (xmimpflag == 141 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =74d;}
		if (xmimpflag == 141 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =74/ (1-0.213152374545978 );}
		if (xmimpflag == 142 && slaximp >= 1 &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.190548809128441);}
		if (xmimpflag == 142 && slamimp >= 1 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.190548809128441);}
		if (xmimpflag == 142 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =80.5625;}
		if (xmimpflag == 142 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =80.5625 - (80.5625*0.190548809128441 );}
		if (xmimpflag == 145 && slaximp >= 1 &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.0577485174550083);}
		if (xmimpflag == 145 && slamimp >= 1 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.0577485174550083);}
		if (xmimpflag == 145 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =376.928571428571;}
		if (xmimpflag == 145 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =376.928571428571/ (1-0.0577485174550083 );}
		if (xmimpflag == 146 && slaximp >= 1 &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.153749295952981);}
		if (xmimpflag == 146 && slamimp >= 1 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.153749295952981);}
		if (xmimpflag == 146 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =154.307692307692;}
		if (xmimpflag == 146 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =154.307692307692/ (1-0.153749295952981 );}
		if (xmimpflag == 147 && slaximp >= 1 &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.143606923731731);}
		if (xmimpflag == 147 && slamimp >= 1 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.143606923731731);}
		if (xmimpflag == 147 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =165.903225806452;}
		if (xmimpflag == 147 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =165.903225806452 - (165.903225806452*0.143606923731731 );}
		if (xmimpflag == 148 && slaximp >= 1 &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.254317624200109);}
		if (xmimpflag == 148 && slamimp >= 1 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.254317624200109);}
		if (xmimpflag == 148 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =199.730769230769;}
		if (xmimpflag == 148 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =199.730769230769/ (1-0.254317624200109 );}
		if (xmimpflag == 149 && slaximp >= 1 &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.136559928551299);}
		if (xmimpflag == 149 && slamimp >= 1 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.136559928551299);}
		if (xmimpflag == 149 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =1003d;}
		if (xmimpflag == 149 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =1003/ (1-0.136559928551299 );}
		if (xmimpflag == 150 && slaximp >= 1 &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.182187702624498);}
		if (xmimpflag == 150 && slamimp >= 1 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.182187702624498);}
		if (xmimpflag == 150 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =100.090909090909;}
		if (xmimpflag == 150 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =100.090909090909/ (1-0.182187702624498 );}
		if (xmimpflag == 151 && slaximp >= 1 &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.00833333333333333);}
		if (xmimpflag == 151 && slamimp >= 1 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.00833333333333333);}
		if (xmimpflag == 151 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =127.103448275862;}
		if (xmimpflag == 151 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =127.103448275862/ (1-0.00833333333333333 );}
		if (xmimpflag == 152 && slaximp >= 1 &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.100333848361108);}
		if (xmimpflag == 152 && slamimp >= 1 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.100333848361108);}
		if (xmimpflag == 152 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =436.5;}
		if (xmimpflag == 152 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =436.5/ (1-0.100333848361108 );}
		if (xmimpflag == 154 && slaximp >= 1 &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.235321405225611);}
		if (xmimpflag == 154 && slamimp >= 1 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.235321405225611);}
		if (xmimpflag == 154 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =580.060606060606;}
		if (xmimpflag == 154 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =580.060606060606/ (1-0.235321405225611 );}
		if (xmimpflag == 155 && slaximp >= 1 &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.157476046121814);}
		if (xmimpflag == 155 && slamimp >= 1 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.157476046121814);}
		if (xmimpflag == 155 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =70.0833333333334;}
		if (xmimpflag == 155 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =70.0833333333334/ (1-0.157476046121814 );}
		if (xmimpflag == 156 && slaximp >= 1 &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.17641709128796);}
		if (xmimpflag == 156 && slamimp >= 1 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.17641709128796);}
		if (xmimpflag == 156 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =118.333333333333;}
		if (xmimpflag == 156 &&  tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =118.333333333333/ (1-0.17641709128796 );}


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
		
		//Store in voyages object
		voyage.setSlaximp(slaximp.intValue());
		voyage.setSlamimp(slamimp.intValue());
	}
}
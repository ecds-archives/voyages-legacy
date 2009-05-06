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
	
}

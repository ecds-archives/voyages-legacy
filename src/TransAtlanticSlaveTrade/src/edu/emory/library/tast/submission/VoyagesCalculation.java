package edu.emory.library.tast.submission;

import java.util.ArrayList;
import java.util.HashMap;

import org.hibernate.Session;

import edu.emory.library.tast.dm.Fate;
import edu.emory.library.tast.dm.FateOwner;
import edu.emory.library.tast.dm.FateSlaves;
import edu.emory.library.tast.dm.FateVessel;
import edu.emory.library.tast.dm.Nation;
import edu.emory.library.tast.dm.Port;
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
		calculateValueYearN();
		calculateImputedValueFate2();
		calculateImputedValueFate3();
		calculateImputedValueFate4();
		calculateValueMajbuypt();
		calculateValueMajselpt();
		calculateImputedValueTonmod();
		return this.voyage;
	}
	
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
		Integer natinimp=voyage.getNatinimp().getId().intValue();
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
	
	public void calculateValueMajbuypt() {
		int plac1tra_int = 0;
		int plac2tra_int = 0;
		int plac3tra_int = 0;
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
		Integer tslavesp = voyage.getTslavesp();
				
		Integer ncartot = defVal(ncar13, 0)+ defVal(ncar15, 0)+ defVal(ncar17, 0);
		double rslt_d = ncartot.doubleValue()/tslavesd.doubleValue();
		double rslt_p = ncartot.doubleValue()/tslavesp.doubleValue();
		
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
	
	public void calculateValueMajselpt() {
		 Double slastot=0d;
		 
	     int sla1port_int = 0;
	     int adpsale1_int = 0;
		 int adpsale2_int = 0;
		 Port majselpt = null;
		 
		 Integer slas32 = voyage.getSlas32();
		 	 
		 Integer slas36 = voyage.getSlas36();
		 Integer slas39 = voyage.getSlas39();
	      
		 slastot = new Double(defVal(slas32, 0)+defVal(slas36, 0)+defVal(slas39,0));
		 
		 Integer slaarriv = voyage.getSlaarriv();
		 
		 Port sla1port = voyage.getSla1port();
		 Port adpsale1 = voyage.getAdpsale1();
		 Port adpsale2 = voyage.getAdpsale2();
		 
		 sla1port_int = sla1port.getId().intValue();
		 adpsale1_int = adpsale1.getId().intValue();
		 adpsale2_int = adpsale2.getId().intValue();
		 
		 
	     if (sla1port_int >= 1 && adpsale1_int==0 && adpsale2_int ==0) {
	    	 majselpt = sla1port;
	     }
	     else if (adpsale1_int  >= 1 && sla1port==null && adpsale2==null) {
	    	 majselpt = adpsale1;
	     }
	     else if (adpsale2_int >= 1 && sla1port==null && adpsale1 == null) {
	    	 majselpt = adpsale2;
	     }
	     else if (sla1port_int  >=1 && adpsale1_int  >=1 && adpsale2==null && slastot.doubleValue()/slaarriv.doubleValue() >= 0.5d && slas32.intValue() >= slas36) {
	    	 majselpt = sla1port;
	     }
	     else if (sla1port_int >=1 && adpsale1_int >=1 && adpsale2==null && slastot.doubleValue()/slaarriv.doubleValue() >= 0.5d && slas36.intValue() >= slas32) {
	    	 majselpt = adpsale1;
	     }
	     else if (sla1port_int >=1 && adpsale1_int >=1 && adpsale2==null && slastot.doubleValue()/slaarriv.doubleValue() < 0.5d && slas32 < slas36) {
	    	 majselpt = sla1port;
	     }
	     else if (sla1port_int >=1 && adpsale1_int >=1 && adpsale2==null && slastot.doubleValue()/slaarriv.doubleValue() < 0.5d && slas36 < slas32) {
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
	     else if (sla1port_int >=1 && adpsale1_int >=1 && adpsale2_int >= 1 && slastot.doubleValue()/slaarriv.doubleValue() >= 0.5d && slas32 >= slas36 && slas32 >= slas39) {
	    	 majselpt = sla1port;
	     }
	     else if (sla1port_int >=1 && adpsale1_int >=1 && adpsale2_int >= 1 && slastot.doubleValue()/slaarriv.doubleValue() >= 0.5d && slas36 >= slas32 && slas36 >= slas39) {
	    	majselpt = adpsale1;
	     }
	     else if (sla1port_int >=1 && adpsale1_int >=1 && adpsale2_int >= 1 && slastot.doubleValue()/slaarriv.doubleValue() >= 0.5d && slas39 >= slas32 && slas39 >= slas36) {
	    	 majselpt = adpsale2;
	     }
	     else if (sla1port_int >=1 && adpsale1_int >=1 && adpsale2_int >= 1 && slastot.doubleValue()/slaarriv.doubleValue() < 0.5d && slas32 == 0) {
	    	 majselpt = sla1port;
	     }
	     else if (sla1port_int >=1 && adpsale1_int >=1 && adpsale2_int >= 1 && slastot.doubleValue()/slaarriv.doubleValue() < 0.5d && slas32 >=1 && slas36==0) {
	    	 majselpt = adpsale1;
	     }
	     else if (sla1port_int >=1 && adpsale1_int >=1 && adpsale2_int >= 1 && slastot.doubleValue()/slaarriv.doubleValue() < 0.5d && slas32 >=1 && slas39==0) {
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
	public void calculateValueYearN() 
	{
		//Create variables for calculations
		Integer yearam=voyage.getYearam();
		Integer year100=-1; //dummy default value
		
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
	
	
}

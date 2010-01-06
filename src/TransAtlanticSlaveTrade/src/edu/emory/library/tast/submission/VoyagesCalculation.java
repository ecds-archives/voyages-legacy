package edu.emory.library.tast.submission;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Arrays;
import java.util.Date;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;

import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.emory.library.tast.dm.Area;
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
	Transaction trans;
	
	public VoyagesCalculation(Session sess, Voyage voy) {
		voyage = voy;
		session = sess;
		trans = session.beginTransaction();
	}
	
	/*
	 * Calls all the calculation functions in one shot
	 */
	public Voyage calculateImputedVariables() {	
		
		calculateNatinimp();
		calculateDateVariables();
		calculateYearVariables();
		calculateValuesYear();
		calculateVoyLengths();
		calculateTonmod();
		calculateFate2();
		calculateFate3();
		calculateFate4();
		calculateValuesRegion1();
		calculateMjbyptimp();
		calculateMjslptimp();
		calculatePtDepImp();
		calculateValuesRegion1a();
		calculateValuesRegion2();
		calculateXmImpflag();
		calculateSlavesEmbarkDisembark();
		calculateMortality();
		calculateValuesPeople();
		
		
		session.save(voyage);
		trans.commit();
		//session.close();
		
		return voyage;
	}
	
	/*
	 * fate2: Outcome of voyage for slaves
	 */
	public void calculateFate2() {
		HashMap fate2Hash = VoyagesCalcConstants.getfate2Hash();		
		Fate fateObj = voyage.getFate();
		if (fateObj != null) {
			Integer fate = (Integer)fate2Hash.get(fateObj.getId().intValue());
			if (fate != null){
				FateSlaves fateSlave = FateSlaves.loadById(session, fate);
				voyage.setFate2(fateSlave);
			}
		}
	}
	
	/*
	 * fate3: Outcome of voyage If vessel captured
	 */
	public void calculateFate3() {
		HashMap fate3Hash = VoyagesCalcConstants.getfate3Hash();
		Fate fateObj = voyage.getFate();
		if (fateObj != null) {
			Integer fate = (Integer)fate3Hash.get(fateObj.getId().intValue());
			if (fate != null){
				FateVessel fateVessel = FateVessel.loadById(session, fate);
				voyage.setFate3(fateVessel);		 
			}
		}
	}
	
	/*
	 * fate4: Outcome of voyage for owner
	 */
	public void calculateFate4() {
		HashMap fate4Hash = VoyagesCalcConstants.getfate4Hash();
		Fate fateObj = voyage.getFate();
		if (fateObj != null) {
			Integer fate = (Integer)fate4Hash.get(fateObj.getId().intValue());
			if (fate != null){
				FateOwner fateOwner = FateOwner.loadById(session, fate);
				voyage.setFate4(fateOwner);
			}
		}
	}
	
    /*
     * Calculates Standardized tonnage of ship
    */
	public void calculateTonmod() {
		//Create the needed variables for calculations
		Integer tontype = (voyage.getTontype()==null ? null : voyage.getTontype().getId().intValue());
		Integer tonnage = voyage.getTonnage();;
		Integer yearam = voyage.getYearam();
		Integer natinimp = (voyage.getNatinimp() == null ? null : voyage.getNatinimp().getId().intValue());
				
		//TODO imputed natinimp - should already have a function
		
		Float tonmod = null;

		
		//calculate
		tonmod=(tonnage==null ? null : tonnage.floatValue());
		
		if (eq(tontype, 13)) {tonmod=(tonnage==null ? null : tonnage.floatValue());}
		
		else if ((lt(tontype, 3) || eq(tontype, 4) || eq(tontype, 5)))
		{	
			if (gt(yearam, 1773)) {tonmod=(tonnage==null ? null : tonnage.floatValue());}
			if (lt(yearam, 1774) && gt(tonnage, 250)) {tonmod=13.1f + (1.1f * tonnage);}
			if (lt(yearam, 1774) && gt(tonnage, 150) && lt(tonnage, 251)) {tonmod=65.3f + (1.2f * tonnage);}
			if (lt(yearam, 1774) && lt(tonnage, 151)) {tonmod=2.3f + (1.8f * tonnage);}
		}
		
		if (eq(tontype, 4) && gt(yearam, 1783) && lt(yearam, 1794)) {tonmod=null;}
		
		if(eq(tontype, 3) || eq(tontype, 6) || eq(tontype, 9) || eq(tontype, 16))
		{
			tonmod = (tonnage==null ? null : 71f + (0.86f * tonnage));
			if (lt(yearam, 1774) && gt(tonmod, 250)) {tonmod=(tonnage==null ? null : 13.1f + (1.1f * tonnage));}
			if (lt(yearam, 1774) && gt(tonmod, 150) && lt(tonmod, 251)) {tonmod=(tonnage==null ? null : 65.3f + (1.2f * tonnage));}
			if (lt(yearam, 1774) && lt(tonmod, 151)) {tonmod=(tonnage==null ? null: 2.3f + (1.8f * tonnage));}
		}
		
		else if(eq(tontype, 7))
		{
			tonmod=(tonnage==null ? null : tonnage * 2f);
			if (gt(yearam, 1773) && gt(tonmod, 250)) {tonmod=13.1f + (1.1f * tonmod);}
			if (gt(yearam, 1773) && gt(tonmod, 150) && lt(tonmod, 251)) {tonmod=65.3f + (1.2f * tonmod);}
			if (gt(yearam, 1773) && lt(tonmod, 151)) {tonmod=2.3f + (1.8f * tonmod);}
		}
		
		else if(eq(tontype, 21))
		{
			tonmod=(tonnage==null ? null:  -6.093f + (0.76155f * tonnage));
			if (gt(yearam, 1773) && gt(tonmod, 250)) {tonmod=13.1f + (1.1f * tonmod);}
			if (gt(yearam, 1773) && gt(tonmod, 150) && lt(tonmod, 251)) {tonmod=65.3f + (1.2f * tonmod);}
			if (gt(yearam, 1773) && lt(tonmod, 151)) {tonmod=2.3f + (1.8f * tonmod);}
		}
				
		if (tontype==null && (gt(yearam, 1714) && lt(yearam, 1786)) && gt(tonnage, 0) && eq(natinimp, 7)) {tontype=22;}
		
		if(eq(tontype, 22))
		{
			if (gt(tonnage, 250)) {tonmod=13.1f + (1.1f * tonnage);}
			if (gt(tonnage, 150) && lt(tonnage, 251)) {tonmod=65.3f + (1.2f * tonnage);}
			if (lt(tonnage, 151)) {tonmod=2.3f + (1.8f * tonnage);}
		}
		
		else if (eq(tontype, 15) || eq(tontype, 14) || eq(tontype, 17)) {tonmod =(tonnage==null ? null : 52.86f + (1.22f * tonnage));}
			    
        //Store the result in the object
		voyage.setTonmod((round(tonmod,1)));
	    
	}	
	
	/*
	 * mjbyypt: Principal port of slave purchase
	 */
	public void calculateMjbyptimp() {
		Integer plac1tra_int = null;
		Integer plac2tra_int = null;
		Integer plac3tra_int = null;
		Integer embport_int = null;
		Integer embport2_int = null;
		Integer fate2 = (voyage.getFate2()==null ? null : voyage.getFate2().getId().intValue());
				
		Port mjbyptimp = null;
		Double pctemb=null; //temp var	
		Integer regem1 = (voyage.getRegem1() == null ? null : voyage.getRegem1().getId().intValue());
		Integer regem2 = (voyage.getRegem2() == null ? null : voyage.getRegem2().getId().intValue());
		Integer regem3 = (voyage.getRegem3() == null ? null : voyage.getRegem3().getId().intValue());
		
			
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
		
		
		Port embport = voyage.getEmbport();
		if (embport != null){
			embport_int = embport.getId().intValue();
		}
		
		Port embport2 = voyage.getEmbport2();
		if (embport2 != null){
			embport2_int = embport2.getId().intValue();
		}
				
		Integer ncar13 = voyage.getNcar13();
		Integer ncar15 = voyage.getNcar15();
		Integer ncar17 = voyage.getNcar17();
		Integer ncartot = null;
				
		Integer tslavesd = voyage.getTslavesd();
		Integer tslavesp = voyage.getTslavesp();
		
		
		//Begin calculations
		
		
		if (gte(ncar13, 1) || gte(ncar15, 1) || gte(ncar17, 1))
		{
			ncar13=defVal(ncar13, 0);
			ncar15=defVal(ncar15, 0);
			ncar17=defVal(ncar17, 0);
		}	
				
		ncartot = add(new Integer[]{ncar13, ncar15, ncar17});
		
		pctemb = div(ncartot, tslavesd); 
		if(tslavesd==null) {pctemb = div(ncartot, tslavesp);}

		if (gte(plac1tra_int, 1) && plac2tra_int==null && plac3tra_int==null) {mjbyptimp = plac1tra;}
		else if (gte(plac2tra_int, 1) && plac1tra_int==null && plac3tra_int==null) {mjbyptimp = plac2tra;}
		else if (gte(plac3tra_int, 1) && plac1tra_int==null && plac2tra_int==null) {mjbyptimp = plac3tra;}

		
		if (eq(plac1tra_int, plac2tra_int)){mjbyptimp=plac1tra;}
		if (eq(plac1tra_int, plac3tra_int)){mjbyptimp=plac1tra;}
		if (eq(plac2tra_int, plac3tra_int)){mjbyptimp=plac2tra;}
		
		if (gt(ncar13, ncar15) && gt(ncar13, ncar17)) {mjbyptimp = plac1tra;}
		else if (gt(ncar15, ncar13) && gt(ncar15, ncar17)) {mjbyptimp = plac2tra;}
		else if (gt(ncar17, ncar13) && gt(ncar17, ncar15)) {mjbyptimp = plac3tra;}

		if (lt(pctemb,0.5) || (lt(ncartot,50) && tslavesd==null && tslavesp==null) ) //reversed order 
		{
			if (gt(ncar13, 0) && eq(ncar15, 0) && eq(ncar17, 0) && ne(regem2, regem3)) {mjbyptimp = Port.loadById(session, 60999);}
			else if (eq(ncar13, 0) && gt(ncar15, 0) && eq(ncar17, 0) && ne(regem1, regem3)) {mjbyptimp = Port.loadById(session, 60999);}
			else if (eq(ncar13, 0) && eq(ncar15, 0) && gt(ncar17, 0) && ne(regem1, regem2)){mjbyptimp = Port.loadById(session, 60999);}
			else if (gt(ncar13, 0) && eq(ncar15, 0) && eq(ncar17, 0) && eq(regem2, regem3)) {mjbyptimp = Port.loadById(session, regem2+99);}
			else if (eq(ncar13, 0) && gt(ncar15, 0) && eq(ncar17, 0) && eq(regem1, regem3)) {mjbyptimp = Port.loadById(session, regem1+99);}
			else if (eq(ncar13, 0) && eq(ncar15, 0) && gt(ncar17, 0) && eq(regem1, regem2)) {mjbyptimp = Port.loadById(session, regem1+99);}
			else if (gt(ncar13, 0) && eq(ncar15, 0) && eq(ncar17, 0) && gt(plac2tra_int, 0) && plac3tra_int==null) {mjbyptimp = plac2tra;}
			else if (eq(ncar13, 0) && gt(ncar15, 0) && eq(ncar17, 0) && plac3tra_int==null) {mjbyptimp = plac1tra;}
			else if (gt(ncar13, 0) && gt(ncar15, 0) && eq(ncar17, 0)) {mjbyptimp = plac3tra;}
			else if (gt(ncar13, 0) && eq(ncar15, 0) && gt(ncar17, 0)) {mjbyptimp = plac2tra;}
			else if (eq(ncar13, 0) && gt(ncar15, 0) && gt(ncar17, 0)) {mjbyptimp = plac1tra;}

	    }
		
		if (ncartot==null) //reversed orig order
		{
			if(gte(plac1tra_int, 1) && gte(plac2tra_int, 1) && gte(plac3tra_int, 1) && ne(regem1, regem2) && ne(regem2, regem3) && ne(regem1, regem3)) {mjbyptimp = Port.loadById(session, 60999);}
			else if (gte(plac1tra_int, 1) && gte(plac2tra_int, 1) && gte(plac3tra_int, 1) && eq(regem2, regem3)) {mjbyptimp = Port.loadById(session, regem2 + 99);}
			else if (gte(plac1tra_int, 1) && gte(plac2tra_int, 1) && gte(plac3tra_int, 1) && eq(regem1, regem3)) {mjbyptimp = Port.loadById(session, regem1 + 99);}
			else if (gte(plac1tra_int, 1) && gte(plac2tra_int, 1) && gte(plac3tra_int, 1) && eq(regem1, regem2)) {mjbyptimp = Port.loadById(session, regem1 + 99);}
			else if (gte(plac2tra_int, 1) && gte(plac3tra_int, 1) && plac1tra_int==null && ne(regem2, regem3)) {mjbyptimp = Port.loadById(session, 60999);}
			else if (gte(plac1tra_int, 1) && gte(plac3tra_int, 1) && plac2tra_int==null && ne(regem1, regem3)) {mjbyptimp = Port.loadById(session, 60999);}
			else if (gte(plac1tra_int, 1) && gte(plac2tra_int, 1) && plac3tra_int==null && ne(regem1, regem2)) {mjbyptimp = Port.loadById(session, 60999);}
			else if (gte(plac2tra_int, 1) && gte(plac3tra_int, 1) && plac1tra_int==null && eq(regem2, regem3)) {mjbyptimp = Port.loadById(session, regem2 + 99);}
			else if (gte(plac1tra_int, 1) && gte(plac3tra_int, 1) && plac2tra_int==null && eq(regem1, regem3)) {mjbyptimp = Port.loadById(session, regem1 + 99);}
			else if (gte(plac1tra_int, 1) && gte(plac2tra_int, 1) && plac3tra_int==null && eq(regem1, regem2)) {mjbyptimp = Port.loadById(session, regem1 + 99);}

		}

		if (gte(embport_int, 1) && embport2_int==null && plac1tra_int==null && plac2tra_int==null && plac3tra_int==null) {mjbyptimp = embport;}
		if (gte(embport2_int, 1) && plac1tra_int==null && plac2tra_int==null && plac3tra_int==null) {mjbyptimp = embport2;}

		
		
		if 
		(
				mjbyptimp==null && 
				ne(fate2, 2) && 
				(
						gte(embport_int, 1) || 
						gte(embport2_int,1) || 
						gte(plac1tra_int, 1) || 
						gte(plac2tra_int, 1) || 
						gte(plac3tra_int, 1) || 
						gt(ncartot, 0)
				)
		) 
		{mjbyptimp = Port.loadById(session,60999);}
		
		
		//Store in Voyage 
        voyage.setMjbyptimp(mjbyptimp);			
		
        
	}
	
	
	public Integer getNcartot(){
		Integer ncar13 = voyage.getNcar13();
		Integer ncar15 = voyage.getNcar15();
		Integer ncar17 = voyage.getNcar17();
		Integer ncartot=null;
		
		if(gte(ncar13, 1) || gte(ncar15, 1) || gte(ncar17, 1))
		{
			ncar13 = defVal(ncar13, 0);
			ncar15 = defVal(ncar15, 0);
			ncar17=defVal(ncar17, 0);
		}
		
		ncartot = add(new Integer[]{ncar13,ncar15,ncar17});
		
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
		Integer slastot=null;
	      
		 if(gte(slas32, 1) || gte(slas36, 1) || gte(slas39, 1))
		 {
			 slas32  = defVal(slas32, 0);
			 slas36 = defVal(slas36, 0);
			 slas39 = defVal(slas39, 0);
		 }
		 
		 slastot = add(new Integer[]{slas32,slas36,slas39});
		 
		 return (slastot==null ? null : slastot.doubleValue());

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
	    if (yearam !=null && yearam < 1601) {year100=1500;}
	    else if (yearam !=null && yearam > 1600 && yearam < 1701) {year100=1600;}
	    else if (yearam !=null && yearam > 1700 && yearam < 1801) {year100=1700;}
	    else if (yearam !=null && yearam > 1800) {year100=1800;}
	    
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
	    	voyage.setYear25(year25);
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
	  Integer curr=null;


	  //Loop over each input value
	  for(int o=0; o < orig.size(); o++)
	  {
	     if(orig.get(o)!=null) {curr=(Integer)orig.get(o);}
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
	             if(curr!=null && curr >=low && curr <= high)
	             {
	                 foundvalue=range[2];
	                 break;
	             }
	         }
	         else //do not include the low and high values in the search
	         {
	             if(curr!=null && curr > low && curr < high)
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
	 * Calculates natinimp : Imputed country in which ship registered (flag*)
	 */
	public void calculateNatinimp() {
		Integer national=-1;		
		Integer natinimp=null;
		
		try {
			//Create variables for calculation
			if(voyage.getNational()!= null) {
				national = voyage.getNational().getId().intValue();
				HashMap natHash = VoyagesCalcConstants.getnatHash();				
				natinimp = (Integer)natHash.get(national);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	    
	    //Store the value in a Nation object and update voyages object
	    if (natinimp != null){
	    	Nation nation= Nation.loadById(session, natinimp);
	    	if(nation != null) {
	    		voyage.setNatinimp(nation);
	    	}
		}
	}
	
	/*
	 * Calculates voy1imp and voy2imp values
	 */
	public void calculateVoyLengths() {
		//Create variables for calculation
		Date dateland1=voyage.getDateland1();
		Date datedep=voyage.getDatedep(); 
		Date dateleftafr=voyage.getDateleftafr();
		Integer voyMid=voyage.getVoyage();
		Integer voy2imp=null;
		Integer voy1imp=null;
		
		//Calculate values
		voy1imp = dateDiff(datedep, dateland1);
	    
		//Use voyage filed if  voyimp2 missing
		voy2imp = dateDiff(dateland1, dateleftafr);
		if(voy2imp==null || (lt(voy2imp, 20) && gt(sub(voyMid, voy2imp),10)) ){voy2imp = voyMid;}
		
	    //confirm this section is correct
		
		if(lte(voy1imp, 38)) {voy1imp=null;}
		if(lte(voy2imp, 10)) {voy2imp=null;}
			
	    
		
		//update voyages object
	    voyage.setVoy1imp(voy1imp);
		voyage.setVoy2imp(voy2imp);
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
	    diff = round(diff,0);
	    
	    if(diff < 0)
	    	diff=diff*-1;

	    return diff.intValue();
	}
	
	/*
	 * Rounds a value to the closest double value
	 */
	public static Double round(Double d, int pre)
	{
		if(d==null)
			return null; //returns null when input is null
		
	    Double ret=0d;
	    int decimalPlace = pre;
	    BigDecimal bd = new BigDecimal(d);
	    bd = bd.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
	    ret = bd.doubleValue();
	    return ret;
	}

	/*
	 * Rounds a value to the closest long value
	 */
	public static Long round(Long l, int pre)
	{
		if(l==null)
			return null; //returns null when input is null
		
	    Long ret=0l;
	    int decimalPlace = pre;
	    BigDecimal bd = new BigDecimal(l);
	    bd = bd.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
	    ret = bd.longValue();
	    return ret;
	}
	
	
	/*
	 * Rounds a value to the closest float value
	 */
	public static Float round(Float f, int pre)
	{
		if(f==null)
			return null; //returns null when input is null
		
	    Float ret=0f;
	    int decimalPlace = pre;
	    BigDecimal bd = new BigDecimal(f);
	    bd = bd.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
	    ret = bd.floatValue();
	    return ret;
	}
	
	
	/*
	 * ptdepimp: Imputed port where voyage began
	 */	
	public void calculatePtDepImp() {
		
		//Imputed
		Integer ptdepimp = null;
		//TODO imputed var Majselpt
		
		//Input
		Integer portdep = (voyage.getPortdep()==null ? null : voyage.getPortdep().getId().intValue());
		Integer mjslptimp = (voyage.getMjslptimp()==null ? null : voyage.getMjslptimp().getId().intValue());
		
		
		//do calculations
		ptdepimp=portdep;
		if (gte(mjslptimp, 50200) && lt(mjslptimp, 50300) && portdep==null) {ptdepimp=50299;}
		if (gte(mjslptimp, 50300) && lt(mjslptimp, 50400) && portdep==null) {ptdepimp=50399;}
		if (gte(mjslptimp, 50400) && lt(mjslptimp, 50500) && portdep==null) {ptdepimp=50422;}
	    
	    //store in voyage
		voyage.setPtdepimp(Port.loadById(session, defVal(ptdepimp, 0)));  //def val to make null if it is null
	}
	
	/*
	 * tslmtimp:  Imputed total of slaves embarked for mortality calculation
	 * vymrtimp:  Imputed number of slaves died in  middle passage
	 * vymrtrat:  Slaves died on voyage/Slaves embarked	  
	 */	
	public void calculateMortality() {
		try {
			//imputed
			Float vymrtrat = 0f;
			Integer vymrtimp= null;
			Integer tslmtimp = voyage.getTslmtimp();
			
			//Input
			Integer sladvoy = voyage.getSladvoy();
			Integer tslavesd = voyage.getTslavesd();			
			Integer slaarriv = voyage.getSlaarriv();
			
			
			
			
			//do calculations
			vymrtimp=sladvoy;
			if(sladvoy==null && lte(slaarriv, tslavesd)) {vymrtimp=tslavesd-slaarriv;}
			if(gte(vymrtimp, 0)) {tslmtimp=tslavesd;}
			if((tslavesd==null && gte(vymrtimp, 0)) && gte(slaarriv, 1)) {tslmtimp=slaarriv+vymrtimp;}
			vymrtrat=divF(vymrtimp,tslmtimp);
						
			//Save to voyage
			voyage.setTslmtimp(tslmtimp);
			voyage.setVymrtrat(round(vymrtrat,6));
			voyage.setVymrtimp(vymrtimp);
			
		} catch (Exception e) {
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
		Integer embreg=null;
		Integer embreg2=null;
		Integer regem1=null;
		Integer regem2=null;
		Integer regem3=null;
		Integer regarr=null;
		Integer regarr2=null;
		Integer regdis1=null;
		Integer regdis2=null;
		Integer regdis3=null;
		Integer retrnreg=null;
		
		//Input variables
		Integer placcons = -1;
		Integer placreg = -1;
		Integer embport = -1; 
		Integer embport2 = -1;
		Integer plac1tra = -1; 
		Integer plac2tra = -1;
		Integer plac3tra = -1;
		Integer arrport = -1;
		Integer arrport2 = -1;
		Integer sla1port = -1;
		Integer adpsale1 = -1;
		Integer adpsale2 = -1;
		Integer portret = -1;
		
		
		
		try {
			//Get Values for first region calculation
			if(voyage.getPlaccons()!=null) {placcons = voyage.getPlaccons().getId().intValue();}
			if(voyage.getPlacreg()!=null) {placreg = voyage.getPlacreg().getId().intValue();}
			if(voyage.getEmbport()!=null) {embport = voyage.getEmbport().getId().intValue();} 
			if(voyage.getEmbport2()!=null) {embport2 = voyage.getEmbport2().getId().intValue();}
			if(voyage.getPlac1tra()!=null) {plac1tra = voyage.getPlac1tra().getId().intValue();} 
			if(voyage.getPlac2tra()!=null) {plac2tra = voyage.getPlac2tra().getId().intValue();}
			if(voyage.getPlac3tra()!=null) {plac3tra = voyage.getPlac3tra().getId().intValue();}
			if(voyage.getArrport()!=null) {arrport = voyage.getArrport().getId().intValue();}
			if(voyage.getArrport2()!=null) {arrport2 = voyage.getArrport2().getId().intValue();}
			if(voyage.getSla1port()!=null) {sla1port = voyage.getSla1port().getId().intValue();}
			if(voyage.getAdpsale1()!=null) {adpsale1 = voyage.getAdpsale1().getId().intValue();}
			if(voyage.getAdpsale2()!=null) {adpsale2 = voyage.getAdpsale2().getId().intValue();}
			if(voyage.getPortret()!=null) {portret = voyage.getPortret().getId().intValue();}
			
			//Add to input array
			ArrayList inputs=new ArrayList();
			inputs.add(placcons);
			inputs.add(placreg);
			inputs.add(embport);
			inputs.add(embport2);
			inputs.add(plac1tra);
			inputs.add(plac2tra);
			inputs.add(plac3tra);
			inputs.add(arrport);
			inputs.add(arrport2);
			inputs.add(sla1port);
			inputs.add(adpsale1);
			inputs.add(adpsale2);
			inputs.add(portret);

			//Get Ranges for first calculation
			ArrayList ranges= VoyagesCalcConstants.getRegionRanges1();

			//Do the calculation
			ArrayList impVars = recode(inputs, ranges, true);
			
			constreg = (Integer) impVars.get(0);
			regisreg = (Integer) impVars.get(1);
			embreg = (Integer) impVars.get(2);
			embreg2 = (Integer) impVars.get(3);
			regem1 = (Integer) impVars.get(4);
			regem2 = (Integer) impVars.get(5);
			regem3 = (Integer) impVars.get(6);
			regarr = (Integer) impVars.get(7);
			regarr2 = (Integer) impVars.get(8);
			regdis1 = (Integer) impVars.get(9);
			regdis2 = (Integer) impVars.get(10);
			regdis3 = (Integer) impVars.get(11);
			retrnreg = (Integer) impVars.get(12);
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
	    
	    if(retrnreg!=null && retrnreg!=-1)
	    {
	    	Region region = Region.loadById(session, retrnreg);
	    	voyage.setRetrnreg(region);	    
	    }
	    	    
	}
	
	/*
	 * Calculates first set of region variables
	 */
	public void calculateValuesRegion1a()
	{
		//Create Return values
		Integer deptregimp=null;
		Integer majbyimp=null;
		Integer mjselimp=null;
		
		//Input variables
		Integer ptdepimp = -1;
		Integer mjbyptimp = -1; 
		Integer mjslptimp = -1; 
		
		
		
		try {
			//Get Values for first region calculation
			if(voyage.getPtdepimp()!=null) {ptdepimp=voyage.getPtdepimp().getId().intValue();};
			if(voyage.getMjbyptimp()!=null) {mjbyptimp=voyage.getMjbyptimp().getId().intValue();}
			if(voyage.getMjslptimp()!=null) {mjslptimp=voyage.getMjslptimp().getId().intValue();}
			
			
			//Add to input array
			ArrayList inputs=new ArrayList();
			inputs.add(ptdepimp);
			inputs.add(mjbyptimp);
			inputs.add(mjslptimp);

			//Get Ranges for first calculation
			ArrayList ranges= VoyagesCalcConstants.getRegionRanges1();

			//Do the calculation
			ArrayList impVars = recode(inputs, ranges, true);
			
			deptregimp = (Integer) impVars.get(0);
			majbyimp = (Integer) impVars.get(1);
			mjselimp = (Integer) impVars.get(2);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	    
	    //Store the values back to the voyage object
	    if(deptregimp!=null && deptregimp!=-1) {voyage.setDeptregimp(Region.loadById(session, deptregimp));}
	    if(majbyimp!=null && majbyimp!=-1) {voyage.setMajbyimp(Region.loadById(session, majbyimp));}
	    if(mjselimp!=null && mjselimp!=-1) {voyage.setMjselimp(Region.loadById(session, mjselimp));}
   	    
	}
	
	
	
	/*
	 * Calculates second set of region variables
	 */
	public void calculateValuesRegion2()
	{
		//Create Return values
		Integer deptregimp1=null;
		Integer majbyimp1=null;
		Integer mjselimp1=null;
		Integer retrnreg1=null;
		
		//Input variables for second region calculation
		//Integer portdep = -1;
		Integer ptdepimp = -1; //TODO imputed ptdepimp
		Integer mjbyptimp = -1; //TODO imputed mjbyptimp
		Integer mjslptimp = -1; //TODO imputed mjslptimp
		Integer portret = -1;
		
		try {
			//Input variables for second region calculation
			 if(voyage.getPtdepimp()!=null) {ptdepimp = voyage.getPtdepimp().getId().intValue();} //TODO imputed ptdepimp
			 if(voyage.getMjbyptimp()!=null) {mjbyptimp = voyage.getMjbyptimp().getId().intValue();} //TODO imputed mjbyptimp
			 if(voyage.getMjslptimp()!=null) {mjslptimp = voyage.getMjslptimp().getId().intValue();} //TODO imputed mjslptimp
			 if(voyage.getPortret()!=null) {portret = voyage.getPortret().getId().intValue();}
					
			//Add to input array
			ArrayList inputs=new ArrayList();
			inputs.add(ptdepimp);
			inputs.add(mjbyptimp);
			inputs.add(mjslptimp);
			inputs.add(portret);

			//Get Ranges for second calculation
			ArrayList ranges= VoyagesCalcConstants.getRegionRanges2();

			//Do the calculation
			ArrayList impVars = recode(inputs, ranges, true);
			
			deptregimp1 = (Integer) impVars.get(0);
			majbyimp1 = (Integer) impVars.get(1);
			mjselimp1 = (Integer) impVars.get(2);
			retrnreg1 = (Integer) impVars.get(3);
		} catch (Exception e) {
			e.printStackTrace();
		}
	    
	    //Store the values back to the voyage object
	    if(deptregimp1!=null && deptregimp1!=-1)
	    {
	    	Area area = Area.loadById(session, deptregimp1);
	    	voyage.setDeptregimp1(area);	    
	    }
	    if(majbyimp1!=null && majbyimp1!=-1)
	    {
	    	Area area = Area.loadById(session, majbyimp1);
	    	voyage.setMajbyimp1(area);	    
	    }
	    if(mjselimp1!=null && mjselimp1!=-1)
	    {
	    	Area area = Area.loadById(session, mjselimp1);
	    	voyage.setMjselimp1(area);	    
	    }
	    if(retrnreg1!=null && retrnreg1!=-1)
	    {
	    	Area area = Area.loadById(session, retrnreg1);
	    	voyage.setRetrnreg1(area);	    
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
		Long mjbyptimp=null; 
		Long majbyimp=null; 
		Long majbyimp1=null; 
		Long mjslptimp=null;
		Long mjselimp=null; 
		Long mjselimp1=null;
		
		//Input
		Integer xmimpflag=(voyage.getXmimpflag()==null ? null : voyage.getXmimpflag().intValue()); //TODO xmimpflag imputed var
		Integer fate2=(voyage.getFate2()==null ? null : voyage.getFate2().getId().intValue());
		Integer tslavesd = voyage.getTslavesd();
		Integer tslavesp = voyage.getTslavesp();
		Integer ncartot = getNcartot();
		Double slastot = getSlastot();
		Integer slaarriv = voyage.getSlaarriv();
		Integer sladvoy = voyage.getSladvoy();
		
			//Input / output variables
			mjbyptimp = (voyage.getMjbyptimp()== null ? null : voyage.getMjbyptimp().getId()); 
			majbyimp =  (voyage.getMajbyimp() == null ? null : voyage.getMajbyimp().getId());
			majbyimp1 = (voyage.getMajbyimp1() == null ? null : voyage.getMajbyimp1().getId());
			mjslptimp = (voyage.getMjslptimp() == null ? null : voyage.getMjslptimp().getId());
			mjselimp =  (voyage.getMjselimp() == null ? null : voyage.getMjselimp().getId());
			mjselimp1=  (voyage.getMjselimp1() == null ? null : voyage.getMjselimp1().getId());
			
			
	    	//Do the calculations
			
			//reversed
			if (tslavesd==null && tslavesp==null && slaarriv==null && slastot==null && gte(ncartot,50)) {slaximp=ncartot.doubleValue();}
			else if (tslavesd==null && tslavesp==null && slaarriv==null && gt(ncartot,slastot)) {slaximp=ncartot.doubleValue();}
			else if (tslavesd==null && tslavesp==null && gt(ncartot,slaarriv)) {slaximp=ncartot.doubleValue();}
			else if (tslavesd==null && gte(tslavesp,1)) {slaximp = tslavesp.doubleValue();}
			else if (gte(tslavesd,1)) {slaximp = tslavesd.doubleValue();}

			//reversed
			if (slaarriv==null && tslavesd==null && tslavesd==null && ncartot==null && gte(slastot,50)) {slamimp=null;}
			else if (slaarriv==null && tslavesd==null && tslavesp==null && lte(slastot,ncartot)) {slamimp=ncartot.doubleValue();}
			else if (slaarriv==null && tslavesd==null && lte(slastot,tslavesp)) {slamimp=slastot;}
			else if (slaarriv==null && lte(slastot,tslavesd)) {slamimp=slastot;}
			else if (gte(slaarriv,1)) {slamimp = slaarriv.doubleValue();}


			
			
			if(eq(xmimpflag, 127)){
			if(gte(slaximp,1) && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.165107561642471);}
			if(gte(slamimp,1) && tslavesd==null && tslavesp==null && ncartot==null){slaximp = slamimp / (1-0.165107561642471);}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp=163.181286549708;}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null){slaximp =163.181286549708/ (1-0.165107561642471 );}
			}
			else if(eq(xmimpflag, 128)){
			if(gte(slaximp,1) && slaarriv==null && slastot==null){slamimp = slaximp - (slaximp*0.230972326367458);}
			if(gte(slamimp,1) && tslavesd==null && tslavesp==null && ncartot==null){slaximp = slamimp / (1- 0.230972326367458);}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null){slamimp =241.774647887324;}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null){slaximp =241.774647887324/ (1-0.230972326367458 );}
			}
			else if(eq(xmimpflag, 129)){
			if(gte(slaximp,1) && slaarriv==null && slastot==null){slamimp = slaximp - (slaximp*0.218216262481124);}
			if(gte(slamimp,1) && tslavesd==null && tslavesp==null && ncartot==null){slaximp = slamimp / (1- 0.218216262481124);}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null){slamimp =249.141527001862;}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null){slaximp =249.141527001862/ (1-0.218216262481124 );}
			}
			else if(eq(xmimpflag, 130)){
			if(gte(slaximp,1) && slaarriv==null && slastot==null){slamimp = slaximp - (slaximp*0.164154067860228);}
			if(gte(slamimp,1) && tslavesd==null && tslavesp==null && ncartot==null){slaximp = slamimp / (1- 0.164154067860228);}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null){slamimp =227.680034129693;}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null){slaximp =227.680034129693/ (1-0.164154067860228 );}
			}
			else if(eq(xmimpflag, 131)){
			if(gte(slaximp,1) && slaarriv==null && slastot==null){slamimp = slaximp - (slaximp*0.153670852602567);}
			if(gte(slamimp,1) && tslavesd==null && tslavesp==null && ncartot==null){slaximp = slamimp / (1- 0.153670852602567);}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null){slamimp =272.60549132948;}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null){slaximp =272.60549132948/ (1-0.153670852602567 );}
			}
			else if(eq(xmimpflag, 132)){
			if(gte(slaximp,1) && slaarriv==null && slastot==null){slamimp = slaximp - (slaximp*0.120410468186061);}
			if(gte(slamimp,1) && tslavesd==null && tslavesp==null && ncartot==null){slaximp = slamimp / (1- 0.120410468186061);}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null){slamimp =268.071314102564;}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null){slaximp =268.071314102564/ (1-0.120410468186061 );}
			}
			else if(eq(xmimpflag, 133)){
			if(gte(slaximp,1) && slaarriv==null && slastot==null){slamimp = slaximp - (slaximp*0.126821090786133);}
			if(gte(slamimp,1) && tslavesd==null && tslavesp==null && ncartot==null){slaximp = slamimp / (1- 0.126821090786133);}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null){slamimp =290.826654240447;}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null){slaximp =290.826654240447/ (1-0.126821090786133 );}
			}
			else if(eq(xmimpflag, 134)){
			if(gte(slaximp,1) && slaarriv==null && slastot==null){slamimp = slaximp - (slaximp*0.105799354866935);}
			if(gte(slamimp,1) && tslavesd==null && tslavesp==null && ncartot==null){slaximp = slamimp / (1- 0.105799354866935);}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null){slamimp =225.932515337423;}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null){slaximp =225.932515337423/ (1-0.105799354866935 );}
			}
			else if(eq(xmimpflag, 135)){
			if(gte(slaximp,1) && slaarriv==null && slastot==null){slamimp = slaximp - (slaximp*0.114160782328086);}
			if(gte(slamimp,1) && tslavesd==null && tslavesp==null && ncartot==null){slaximp = slamimp / (1- 0.114160782328086);}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null){slamimp =391.452674897119;}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null){slaximp =391.452674897119/ (1-0.114160782328086 );}
			}
			else if(eq(xmimpflag, 136)){
			if(gte(slaximp,1) && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.170755559662484);}
			if(gte(slamimp,1) && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.170755559662484);}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =480.734042553191;}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =480.734042553191/ (1-0.170755559662484 );}
			}
			else if(eq(xmimpflag, 101)){
			if(gte(slaximp,1) && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.142415261804064);}
			if(gte(slamimp,1) && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.142415261804064);}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =163.80243902439;}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =163.80243902439/ (1-0.142415261804064 );}
			}
			else if(eq(xmimpflag, 102)){
			if(gte(slaximp,1) && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.104951847967976);}
			if(gte(slamimp,1) && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.104951847967976);}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =153.265497076023;}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =153.265497076023/ (1-0.104951847967976 );}
			}
			else if(eq(xmimpflag, 103)){
			if(gte(slaximp,1) && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.0794334443169517);}
			if(gte(slamimp,1) && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.0794334443169517);}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =138.094017094017;}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =138.094017094017/ (1-0.0794334443169517 );}
			}
			else if(eq(xmimpflag, 104)){
			if(gte(slaximp,1) && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.125269157905197);}
			if(gte(slamimp,1) && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.125269157905197);}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =107.64;}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =107.64 - (107.64*0.125269157905197 );}
			}
			else if(eq(xmimpflag, 105)){
			if(gte(slaximp,1) && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.0887057111704602);}
			if(gte(slamimp,1) && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.0887057111704602);}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =191.988789237668;}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =191.988789237668/ (1-0.0887057111704602 );}
			}
			else if(eq(xmimpflag, 106)){
			if(gte(slaximp,1) && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.0985396051230542);}
			if(gte(slamimp,1) && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.0985396051230542);}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =188.140969162996;}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =188.140969162996/ (1-0.0985396051230542 );}
			}
			else if(eq(xmimpflag, 107)){
			if(gte(slaximp,1) && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.199714956235816);}
			if(gte(slamimp,1) && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.199714956235816);}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =239.363636363636;}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =239.363636363636/ (1-0.199714956235816 );}
			}
			else if(eq(xmimpflag, 108)){
			if(gte(slaximp,1) && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.116764553914052);}
			if(gte(slamimp,1) && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.116764553914052);}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =241.066480055983;}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =241.066480055983/ (1-0.116764553914052 );}
			}
			else if(eq(xmimpflag, 110)){
			if(gte(slaximp,1) && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.217817105373686);}
			if(gte(slamimp,1) && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.217817105373686);}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =321.139784946236;}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =321.139784946236/ (1-0.217817105373686 );}
			}
			else if(eq(xmimpflag, 111)){
			if(gte(slaximp,1) && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.134584278813695);}
			if(gte(slamimp,1) && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.134584278813695);}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =320.396527777777;}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =320.396527777777/ (1-0.134584278813695 );}
			}
			else if(eq(xmimpflag, 112)){
			if(gte(slaximp,1) && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.0649564900465187);}
			if(gte(slamimp,1) && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.0649564900465187);}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =302.919243986254;}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =302.919243986254/ (1-0.0649564900465187 );}
			}
			else if(eq(xmimpflag, 113)){
			if(gte(slaximp,1) && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.294943293777566);}
			if(gte(slamimp,1) && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.294943293777566);}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =178.191780821918;}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =178.191780821918/ (1-0.294943293777566 );}
			}
			else if(eq(xmimpflag, 114)){
			if(gte(slaximp,1) && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.190466263797331);}
			if(gte(slamimp,1) && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.190466263797331);}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =268.709993468321;}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =268.709993468321/ (1-0.190466263797331 );}
			}
			else if(eq(xmimpflag, 115)){
			if(gte(slaximp,1) && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.165262209695588);}
			if(gte(slamimp,1) && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.165262209695588);}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =265.480215827338;}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =265.480215827338/ (1-0.165262209695588 );}
			}
			else if(eq(xmimpflag, 116)){
			if(gte(slaximp,1) && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.250590294065011);}
			if(gte(slamimp,1) && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.250590294065011);}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =216.026607538803;}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =216.026607538803/ (1-0.250590294065011 );}
			}
			else if(eq(xmimpflag, 117)){
			if(gte(slaximp,1) && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.0862116624182079);}
			if(gte(slamimp,1) && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.0862116624182079);}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =341.979498861048;}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =341.979498861048/ (1-0.0862116624182079 );}
			}
			else if(eq(xmimpflag, 118)){
			if(gte(slaximp,1) && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.0795782666543268);}
			if(gte(slamimp,1) && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.0795782666543268);}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =382.444580777097;}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =382.444580777097/ (1-0.0795782666543268 );}
			}
			else if(eq(xmimpflag, 120)){
			if(gte(slaximp,1) && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.100542298212489);}
			if(gte(slamimp,1) && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.100542298212489);}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =191.62583518931;}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =191.62583518931/ (1-0.100542298212489 );}
			}
			else if(eq(xmimpflag, 121)){
			if(gte(slaximp,1) && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.0690791392436498);}
			if(gte(slamimp,1) && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.0690791392436498);}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =162.041666666667;}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =162.041666666667/ (1-0.0690791392436498 );}
			}
			else if(eq(xmimpflag, 122)){
			if(gte(slaximp,1) && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.274602006426542);}
			if(gte(slamimp,1) && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.274602006426542);}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =173.454545454545;}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =173.454545454545/ (1-0.274602006426542 );}
			}
			else if(eq(xmimpflag, 123)){
			if(gte(slaximp,1) && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.274602006426542);}
			if(gte(slamimp,1) && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.274602006426542);}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =255.028571428571;}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =255.028571428571/ (1-0.274602006426542 );}
			}
			else if(eq(xmimpflag, 124)){
			if(gte(slaximp,1) && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.181330570603409);}
			if(gte(slamimp,1) && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.181330570603409);}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =447.532008830022;}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =447.532008830022/ (1-0.181330570603409 );}
			}
			else if(eq(xmimpflag, 1)){
			if(gte(slaximp,1) && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.255634697158707);}
			if(gte(slamimp,1) && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.255634697158707);}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =166.401374570447;}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =166.401374570447/ (1-0.255634697158707 );}
			}
			else if(eq(xmimpflag, 2)){
			if(gte(slaximp,1) && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.173114449095158);}
			if(gte(slamimp,1) && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.173114449095158);}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =152.863945578231;}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =152.863945578231/ (1-0.173114449095158 );}
			}
			else if(eq(xmimpflag, 3)){
			if(gte(slaximp,1) && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.191426939591589);}
			if(gte(slamimp,1) && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.191426939591589);}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =250.179245283019;}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =250.179245283019/ (1-0.191426939591589 );}
			}
			else if(eq(xmimpflag, 4)){
			if(gte(slaximp,1) && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.143739162059858);}
			if(gte(slamimp,1) && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.143739162059858);}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =273.896226415094;}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =273.896226415094 - (273.896226415094*0.143739162059858 );}
			}
			else if(eq(xmimpflag, 5)){
			if(gte(slaximp,1) && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.0703329947332674);}
			if(gte(slamimp,1) && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.0703329947332674);}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =380.04854368932;}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =380.04854368932 - (380.04854368932*0.0703329947332674 );}
			}
			else if(eq(xmimpflag, 6)){
			if(gte(slaximp,1) && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.117444418143106);}
			if(gte(slamimp,1) && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.117444418143106);}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =305.868020304568;}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =305.868020304568/ (1-0.117444418143106 );}
			}
			else if(eq(xmimpflag, 7)){
			if(gte(slaximp,1) && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.126779394689057);}
			if(gte(slamimp,1) && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.126779394689057);}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =265.88;}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =265.88 - (265.88*0.126779394689057 );}
			}
			else if(eq(xmimpflag, 8)){
			if(gte(slaximp,1) && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.189011301766662);}
			if(gte(slamimp,1) && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.189011301766662);}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =281.325;}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =281.325/ (1-0.189011301766662 );}
			}
			else if(eq(xmimpflag, 9)){
			if(gte(slaximp,1) && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.140365224720275);}
			if(gte(slamimp,1) && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.140365224720275);}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =402.502202643172;}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =402.502202643172/ (1-0.140365224720275 );}
			}
			else if(eq(xmimpflag, 10)){
			if(gte(slaximp,1) && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.107188743129005);}
			if(gte(slamimp,1) && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.107188743129005);}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =277.059842519684;}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =277.059842519684/ (1-0.107188743129005 );}
			}
			else if(eq(xmimpflag, 11)){
			if(gte(slaximp,1) && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.126901348540731);}
			if(gte(slamimp,1) && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.126901348540731);}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =355.810945273632;}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =355.810945273632/ (1-0.126901348540731 );}
			}
			else if(eq(xmimpflag, 12)){
			if(gte(slaximp,1) && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.0655772248600899);}
			if(gte(slamimp,1) && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.0655772248600899);}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =309.533898305085;}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =309.533898305085/ (1-0.0655772248600899 );}
			}
			else if(eq(xmimpflag, 13)){
			if(gte(slaximp,1) && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.0778021073375869);}
			if(gte(slamimp,1) && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.0778021073375869);}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =305.812154696132;}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =305.812154696132/ (1-0.0778021073375869 );}
			}
			else if(eq(xmimpflag, 14)){
			if(gte(slaximp,1) && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.0654921908875572);}
			if(gte(slamimp,1) && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.0654921908875572);}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =285.054112554113;}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =285.054112554113/ (1-0.0654921908875572 );}
			}
			else if(eq(xmimpflag, 15)){
			if(gte(slaximp,1) && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.0671696102131247);}
			if(gte(slamimp,1) && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.0671696102131247);}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =361.638059701493;}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =361.638059701493/ (1-0.0671696102131247 );}
			}
			else if(eq(xmimpflag, 16)){
			if(gte(slaximp,1) && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.371414750110571);}
			if(gte(slamimp,1) && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.371414750110571);}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =239.9;}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =239.9/ (1-0.371414750110571 );}
			}
			else if(eq(xmimpflag, 157)){
			if(gte(slaximp,1) && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.230610260687796);}
			if(gte(slamimp,1) && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.230610260687796);}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =139.029411764706;}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =139.029411764706/ (1-0.230610260687796 );}
			}
			else if(eq(xmimpflag, 159)){
			if(gte(slaximp,1) && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.154487726688789);}
			if(gte(slamimp,1) && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.154487726688789);}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =245.12676056338;}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =245.12676056338/ (1-0.154487726688789 );}
			}
			else if(eq(xmimpflag, 99)){
			if(gte(slaximp,1) && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.166050441674744);}
			if(gte(slamimp,1) && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.166050441674744);}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =125.619750283768;}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =125.619750283768/ (1-0.166050441674744 );}
			}
			else if(eq(xmimpflag, 100)){
			if(gte(slaximp,1) && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.178717812379779);}
			if(gte(slamimp,1) && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.178717812379779);}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =565.645161290322;}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =565.645161290322/ (1-0.178717812379779 );}
			}
			else if(eq(xmimpflag, 17)){
			if(gte(slaximp,1) && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.0557746478873239);}
			if(gte(slamimp,1) && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.0557746478873239);}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =148.882352941176;}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =148.882352941176/ (1-0.0557746478873239 );}
			}
			else if(eq(xmimpflag, 98)){
			if(gte(slaximp,1) && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.126563817175912);}
			if(gte(slamimp,1) && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.126563817175912);}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =132.596685082873;}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =132.596685082873/ (1-0.126563817175912 );}
			}
			else if(eq(xmimpflag, 18)){
			if(gte(slaximp,1) && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.093544030879478);}
			if(gte(slamimp,1) && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.093544030879478);}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =184.486013986014;}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =184.486013986014/ (1-0.093544030879478 );}
			}
			else if(eq(xmimpflag, 19)){
			if(gte(slaximp,1) && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.0985982521761244);}
			if(gte(slamimp,1) && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.0985982521761244);}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =230.298469387755;}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =230.298469387755/ (1-0.0985982521761244 );}
			}
			else if(eq(xmimpflag, 20)){
			if(gte(slaximp,1) && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.0944678720322908);}
			if(gte(slamimp,1) && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.0944678720322908);}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =444.290145985401;}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =444.290145985401/ (1-0.0944678720322908 );}
			}
			else if(eq(xmimpflag, 21)){
			if(gte(slaximp,1) && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.167379623404603);}
			if(gte(slamimp,1) && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.167379623404603);}
			if(tslavesd==null&&  tslavesp==null&&  ncartot==null&&  slaarriv==null&& slastot==null) {slamimp =492.946428571429;}
			if(tslavesd==null&&  tslavesp==null&&  ncartot==null&&  slaarriv==null&& slastot==null) {slaximp =492.946428571429/ (1-0.167379623404603 );}
			}
			else if(eq(xmimpflag, 22)){
			if(gte(slaximp,1) &&  slaarriv==null&& slastot==null) {slamimp = slaximp - (slaximp*0.183801786070534);}
			if(gte(slamimp,1) &&  tslavesd==null&&  tslavesp==null&& ncartot==null) {slaximp = slamimp / (1- 0.183801786070534);}
			if(tslavesd==null&&  tslavesp==null&&  ncartot==null&&  slaarriv==null&& slastot==null) {slamimp =91.9594594594595;}
			if(tslavesd==null&&  tslavesp==null&&  ncartot==null&&  slaarriv==null&& slastot==null) {slaximp =91.9594594594595/ (1-0.183801786070534 );}
			}
			else if(eq(xmimpflag, 23)){
			if(gte(slaximp,1) &&  slaarriv==null&& slastot==null) {slamimp = slaximp - (slaximp*0.102358180948044);}
			if(gte(slamimp,1) &&  tslavesd==null&&  tslavesp==null&& ncartot==null) {slaximp = slamimp / (1- 0.102358180948044);}
			if(tslavesd==null&&  tslavesp==null&&  ncartot==null&&  slaarriv==null&& slastot==null) {slamimp =95.972972972973;}
			if(tslavesd==null&&  tslavesp==null&&  ncartot==null&&  slaarriv==null&& slastot==null) {slaximp =95.972972972973/ (1-0.102358180948044 );}
			}
			else if(eq(xmimpflag, 24)){
			if(gte(slaximp,1) &&  slaarriv==null&& slastot==null) {slamimp = slaximp - (slaximp*0.122708750828674);}
			if(gte(slamimp,1) &&  tslavesd==null&&  tslavesp==null&& ncartot==null) {slaximp = slamimp / (1- 0.122708750828674);}
			if(tslavesd==null&&  tslavesp==null&&  ncartot==null&&  slaarriv==null&& slastot==null) {slamimp =146.31;}
			if(tslavesd==null&&  tslavesp==null&&  ncartot==null&&  slaarriv==null&& slastot==null) {slaximp =146.31/ (1-0.122708750828674 );}
			}
			else if(eq(xmimpflag, 25)){
			if(gte(slaximp,1) &&  slaarriv==null&& slastot==null) {slamimp = slaximp - (slaximp*0.101742168136026);}
			if(gte(slamimp,1) &&  tslavesd==null &&  tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.101742168136026);}
			if(tslavesd==null&&  tslavesp==null&&  ncartot==null&&  slaarriv==null&& slastot==null) {slamimp =279.357142857143;}
			if(tslavesd==null&&  tslavesp==null&&  ncartot==null&&  slaarriv==null&& slastot==null) {slaximp =279.357142857143/ (1-0.101742168136026 );}
			}
			else if(eq(xmimpflag, 26)){
			if(gte(slaximp,1) &&  slaarriv==null&& slastot==null) {slamimp = slaximp - (slaximp*0.0830808603000646);}
			if(gte(slamimp,1) &&  tslavesd==null&&  tslavesp==null&& ncartot==null) {slaximp = slamimp / (1- 0.0830808603000646);}
			if(tslavesd==null&&  tslavesp==null&&  ncartot==null&&  slaarriv==null&& slastot==null) {slamimp =341.5;}
			if(tslavesd==null&&  tslavesp==null&&  ncartot==null&&  slaarriv==null&& slastot==null) {slaximp =341.5/ (1-0.0830808603000646 );}
			}
			else if(eq(xmimpflag, 27)){
			if(gte(slaximp,1) &&  slaarriv==null&& slastot==null) {slamimp = slaximp - (slaximp*0.0951735364832193);}
			if(gte(slamimp,1) &&  tslavesd==null&&  tslavesp==null&& ncartot==null) {slaximp = slamimp / (1- 0.0951735364832193);}
			if(tslavesd==null&&  tslavesp==null&&  ncartot==null&&  slaarriv==null&& slastot==null) {slaximp =335.546666666667;}
			if(tslavesd==null&&  tslavesp==null&&  ncartot==null&&  slaarriv==null&& slastot==null) {slamimp =335.546666666667 - (335.546666666667*0.0951735364832193 );}
			}
			else if(eq(xmimpflag, 28)){
			if(gte(slaximp,1) &&  slaarriv==null&& slastot==null) {slamimp = slaximp - (slaximp*0.0599984615282753);}
			if(gte(slamimp,1) &&  tslavesd==null&&  tslavesp==null&& ncartot==null) {slaximp = slamimp / (1- 0.0599984615282753);}
			if(tslavesd==null&&  tslavesp==null&&  ncartot==null&&  slaarriv==null&& slastot==null) {slaximp =348.926267281106;}
			if(tslavesd==null&&  tslavesp==null&&  ncartot==null&&  slaarriv==null&& slastot==null) {slamimp =348.926267281106 - (348.926267281106*0.0599984615282753 );}
			}
			else if(eq(xmimpflag, 29)){
			if(gte(slaximp,1) &&  slaarriv==null&& slastot==null) {slamimp = slaximp - (slaximp*0.0849037398486349);}
			if(gte(slamimp,1) &&  tslavesd==null&&  tslavesp==null&& ncartot==null) {slaximp = slamimp / (1- 0.0849037398486349);}
			if(tslavesd==null&&  tslavesp==null&&  ncartot==null&&  slaarriv==null&& slastot==null) {slamimp =323.539358600583;}
			if(tslavesd==null&&  tslavesp==null&&  ncartot==null&&  slaarriv==null&& slastot==null) {slaximp =323.539358600583/ (1-0.0849037398486349 );}
			}
			else if(eq(xmimpflag, 30)){
			if(gte(slaximp,1) &&  slaarriv==null&& slastot==null) {slamimp = slaximp - (slaximp*0.0831292966753462);}
			if(gte(slamimp,1) &&  tslavesd==null&&  tslavesp==null&& ncartot==null) {slaximp = slamimp / (1- 0.0831292966753462);}
			if(tslavesd==null&&  tslavesp==null&&  ncartot==null&&  slaarriv==null&& slastot==null) {slamimp =435.738461538461;}
			if(tslavesd==null&&  tslavesp==null&&  ncartot==null&&  slaarriv==null&& slastot==null) {slaximp =435.738461538461/ (1-0.0831292966753462 );}
			}
			else if(eq(xmimpflag, 31)){
			if(gte(slaximp,1) &&  slaarriv==null&& slastot==null) {slamimp = slaximp - (slaximp*0.154603810637904);}
			if(gte(slamimp,1) &&  tslavesd==null&&  tslavesp==null&& ncartot==null) {slaximp = slamimp / (1- 0.154603810637904);}
			if(tslavesd==null&&  tslavesp==null&&  ncartot==null&&  slaarriv==null&& slastot==null) {slamimp =221.279220779221;}
			if(tslavesd==null&&  tslavesp==null&&  ncartot==null&&  slaarriv==null&& slastot==null) {slaximp =221.279220779221/ (1-0.154603810637904 );}
			}
			else if(eq(xmimpflag, 32)){
			if(gte(slaximp,1) &&  slaarriv==null&& slastot==null) {slamimp = slaximp - (slaximp*0.169381440464976);}
			if(gte(slamimp,1) &&  tslavesd==null&&  tslavesp==null&& ncartot==null) {slaximp = slamimp / (1- 0.169381440464976);}
			if(tslavesd==null&&  tslavesp==null&&  ncartot==null&&  slaarriv==null&& slastot==null) {slamimp =296.593103448276;}
			if(tslavesd==null&&  tslavesp==null&&  ncartot==null&&  slaarriv==null&& slastot==null) {slaximp =296.593103448276/ (1-0.169381440464976 );}
			}
			else if(eq(xmimpflag, 33)){
			if(gte(slaximp,1) &&  slaarriv==null&& slastot==null) {slamimp = slaximp - (slaximp*0.183684529291394);}
			if(gte(slamimp,1) &&  tslavesd==null&&  tslavesp==null&& ncartot==null) {slaximp = slamimp / (1- 0.183684529291394);}
			if(tslavesd==null&&  tslavesp==null&&  ncartot==null&&  slaarriv==null&& slastot==null) {slamimp =281.452966714906;}
			if(tslavesd==null&&  tslavesp==null&&  ncartot==null&&  slaarriv==null&& slastot==null) {slaximp =281.452966714906/ (1-0.183684529291394 );}
			}
			else if(eq(xmimpflag, 34)){
			if(gte(slaximp,1) &&  slaarriv==null&& slastot==null) {slamimp = slaximp - (slaximp*0.0864964921326426);}
			if(gte(slamimp,1) &&  tslavesd==null&&  tslavesp==null&& ncartot==null) {slaximp = slamimp / (1- 0.0864964921326426);}
			if(tslavesd==null&&  tslavesp==null&&  ncartot==null&&  slaarriv==null&& slastot==null) {slamimp =325.652360515021;}
			if(tslavesd==null&&  tslavesp==null&&  ncartot==null&&  slaarriv==null&& slastot==null) {slaximp =325.652360515021/ (1-0.0864964921326426 );}
			}
			else if(eq(xmimpflag, 35)){
			if(gte(slaximp,1) && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.176037224384829);}
			if(gte(slamimp,1) && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.176037224384829);}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =272.474358974359;}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =272.474358974359/ (1-0.176037224384829 );}
			}
			else if(eq(xmimpflag, 36)){
			if(gte(slaximp,1) && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.116937605450612);}
			if(gte(slamimp,1) && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.116937605450612);}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =556.677419354839;}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =556.677419354839/ (1-0.116937605450612 );}
			}
			else if(eq(xmimpflag, 37)){
			if(gte(slaximp,1) && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.172812495199871);}
			if(gte(slamimp,1) && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.172812495199871);}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =890.470588235294;}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =890.470588235294/ (1-0.172812495199871 );}
			}
			else if(eq(xmimpflag, 38)){
			if(gte(slaximp,1) && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.105087524949968);}
			if(gte(slamimp,1) && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.105087524949968);}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =335.813953488372;}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =335.813953488372/ (1-0.105087524949968 );}
			}
			else if(eq(xmimpflag, 39)){
			if(gte(slaximp,1) && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.0856667000685018);}
			if(gte(slamimp,1) && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.0856667000685018);}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =257.263157894737;}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =257.263157894737/ (1-0.0856667000685018 );}
			}
			else if(eq(xmimpflag, 40)){
			if(gte(slaximp,1) && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.0865650987499053);}
			if(gte(slamimp,1) && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.0865650987499053);}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =328.195266272189;}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =328.195266272189/ (1-0.0865650987499053 );}
			}
			else if(eq(xmimpflag, 41)){
			if(gte(slaximp,1) && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.171814252005436);}
			if(gte(slamimp,1) && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.171814252005436);}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =129.145454545455;}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =129.145454545455/ (1-0.171814252005436 );}
			}
			else if(eq(xmimpflag, 42)){
			if(gte(slaximp,1) && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.0610387045813586);}
			if(gte(slamimp,1) && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.0610387045813586);}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =158.1;}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =158.1/ (1-0.0610387045813586 );}
			}
			else if(eq(xmimpflag, 43)){
			if(gte(slaximp,1) && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.159823459162871);}
			if(gte(slamimp,1) && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.159823459162871);}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =247.759689922481;}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =247.759689922481/ (1-0.159823459162871 );}
			}
			else if(eq(xmimpflag, 44)){
			if(gte(slaximp,1) && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.0988853555387519);}
			if(gte(slamimp,1) && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.0988853555387519);}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =363d;}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =363/ (1-0.0988853555387519 );}
			}
			else if(eq(xmimpflag, 45)){
			if(gte(slaximp,1) && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.0904513085721602);}
			if(gte(slamimp,1) && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.0904513085721602);}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =466.25641025641;}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =466.25641025641/ (1-0.0904513085721602 );}
			}
			else if(eq(xmimpflag, 46)){
			if(gte(slaximp,1) && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.082310278477633);}
			if(gte(slamimp,1) && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.082310278477633);}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =159.810810810811;}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =159.810810810811/ (1-0.082310278477633 );}
			}
			else if(eq(xmimpflag, 47)){
			if(gte(slaximp,1) && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.104714300552102);}
			if(gte(slamimp,1) && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.104714300552102);}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =638.25;}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =638.25/ (1-0.104714300552102 );}
			}
			else if(eq(xmimpflag, 48)){
			if(gte(slaximp,1) && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.193439630544956);}
			if(gte(slamimp,1) && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.193439630544956);}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =608.392156862745;}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =608.392156862745/ (1-0.193439630544956 );}
			}
			else if(eq(xmimpflag, 49)){
			if(gte(slaximp,1) && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.145583038352611);}
			if(gte(slamimp,1) && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.145583038352611);}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =428.888888888889;}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =428.888888888889/ (1-0.145583038352611 );}
			}
			else if(eq(xmimpflag, 50)){
			if(gte(slaximp,1) && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.233333333333333);}
			if(gte(slamimp,1) && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.233333333333333);}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =270.846153846154;}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =270.846153846154/ (1-0.233333333333333 );}
			}
			else if(eq(xmimpflag, 51)){
			if(gte(slaximp,1) && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.179223522528989);}
			if(gte(slamimp,1) && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.179223522528989);}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =229.64;}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =229.64/ (1-0.179223522528989 );}
			}
			else if(eq(xmimpflag, 52)){
			if(gte(slaximp,1) && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.0819156347249732);}
			if(gte(slamimp,1) && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.0819156347249732);}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =290.164383561644;}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =290.164383561644 - (290.164383561644*0.0819156347249732 );}
			}
			else if(eq(xmimpflag, 53)){
			if(gte(slaximp,1) && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.0540922242825536);}
			if(gte(slamimp,1) && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.0540922242825536);}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =256.548387096774;}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =256.548387096774/ (1-0.0540922242825536 );}
			}
			else if(eq(xmimpflag, 54)){
			if(gte(slaximp,1) && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.0913651933726713);}
			if(gte(slamimp,1) && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.0913651933726713);}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =216.907894736842;}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =216.907894736842/ (1-0.0913651933726713 );}
			}
			else if(eq(xmimpflag, 55)){
			if(gte(slaximp,1) && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.0604022380426763);}
			if(gte(slamimp,1) && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.0604022380426763);}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =241.461538461538;}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =241.461538461538/ (1-0.0604022380426763 );}
			}
			else if(eq(xmimpflag, 56)){
			if(gte(slaximp,1) && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.0542026549646127);}
			if(gte(slamimp,1) && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.0542026549646127);}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =340.230769230769;}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =340.230769230769/ (1-0.0542026549646127 );}
			}
			else if(eq(xmimpflag, 57)){
			if(gte(slaximp,1) && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.0974564330758702);}
			if(gte(slamimp,1) && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.0974564330758702);}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =516.45;}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =516.45/ (1-0.0974564330758702 );}
			}
			else if(eq(xmimpflag, 58)){
			if(gte(slaximp,1) && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.162886379968412);}
			if(gte(slamimp,1) && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.162886379968412);}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =447.518072289157;}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =447.518072289157 - (447.518072289157*0.162886379968412 );}
			}
			else if(eq(xmimpflag, 59)){
			if(gte(slaximp,1) && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.0561646667118922);}
			if(gte(slamimp,1) && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.0561646667118922);}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =152.923076923077;}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =152.923076923077/ (1-0.0561646667118922 );}
			}
			else if(eq(xmimpflag, 60)){
			if(gte(slaximp,1) && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.133468501803896);}
			if(gte(slamimp,1) && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.133468501803896);}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =403.292993630573;}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =403.292993630573/ (1-0.133468501803896 );}
			}
			else if(eq(xmimpflag, 61)){
			if(gte(slaximp,1) && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.106708705390018);}
			if(gte(slamimp,1) && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.106708705390018);}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =285.644444444444;}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =285.644444444444/ (1-0.106708705390018 );}
			}
			else if(eq(xmimpflag, 62)){
			if(gte(slaximp,1) && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.0785278768682708);}
			if(gte(slamimp,1) && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.0785278768682708);}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =335.658227848101;}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =335.658227848101/ (1-0.0785278768682708 );}
			}
			else if(eq(xmimpflag, 63)){
			if(gte(slaximp,1) && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.107782269167156);}
			if(gte(slamimp,1) && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.107782269167156);}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =472.267857142857;}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =472.267857142857/ (1-0.107782269167156 );}
			}
			else if(eq(xmimpflag, 160)){
			if(gte(slaximp,1) && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.0779281672325541);}
			if(gte(slamimp,1) && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.0779281672325541);}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =536.842857142857;}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =536.842857142857/ (1-0.0779281672325541 );}
			}
			else if(eq(xmimpflag, 65)){
			if(gte(slaximp,1) && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.115409873680179);}
			if(gte(slamimp,1) && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.115409873680179);}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =103.376146788991;}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =103.376146788991/ (1-0.115409873680179 );}
			}
			else if(eq(xmimpflag, 66)){
			if(gte(slaximp,1) && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.207088877726936);}
			if(gte(slamimp,1) && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.207088877726936);}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =68.1506849315068;}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =68.1506849315068/ (1-0.207088877726936 );}
			}
			else if(eq(xmimpflag, 67)){
			if(gte(slaximp,1) && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.110922605367631);}
			if(gte(slamimp,1) && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.110922605367631);}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =80.0491803278688;}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =80.0491803278688/ (1-0.110922605367631 );}
			}
			else if(eq(xmimpflag, 68)){
			if(gte(slaximp,1) && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.127935729778166);}
			if(gte(slamimp,1) && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.127935729778166);}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp =84d;}
			if(tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slamimp =84 - (84*0.127935729778166 );}
			}
			else if(eq(xmimpflag, 69)){
			if(gte(slaximp,1) && slaarriv==null && slastot==null) {slamimp = slaximp - (slaximp*0.206358225584424);}
			if(gte(slamimp,1) && tslavesd==null && tslavesp==null && ncartot==null) {slaximp = slamimp / (1- 0.206358225584424);}
			if(tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =1004.47058823529;}
			if(tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =1004.47058823529/ (1-0.206358225584424 );}
			}
			else if(eq(xmimpflag, 70)){
			if(gte(slaximp,1) &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.142775407154303);}
			if(gte(slamimp,1) &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.142775407154303);}
			if(tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =311.222222222222;}
			if(tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =311.222222222222/ (1-0.142775407154303 );}
			}
			else if(eq(xmimpflag, 71)){
			if(gte(slaximp,1) &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.106323148232566);}
			if(gte(slamimp,1) &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.106323148232566);}
			if(tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =310.39837398374;}
			if(tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =310.39837398374/ (1-0.106323148232566 );}
			}
			else if(eq(xmimpflag, 97)){
			if(gte(slaximp,1) &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.138965456634756);}
			if(gte(slamimp,1) &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.138965456634756);}
			if(tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =259.21875;}
			if(tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =259.21875/ (1-0.138965456634756 );}
			}
			else if(eq(xmimpflag, 72)){
			if(gte(slaximp,1) &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.169436742362705);}
			if(gte(slamimp,1) &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.169436742362705);}
			if(tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =265.325842696629;}
			if(tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =265.325842696629/ (1-0.169436742362705 );}
			}
			else if(eq(xmimpflag, 85)){
			if(gte(slaximp,1) &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.339905284604731);}
			if(gte(slamimp,1) &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.339905284604731);}
			if(tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =563.333333333333;}
			if(tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =563.333333333333/ (1-0.339905284604731 );}
			}
			else if(eq(xmimpflag, 73)){
			if(gte(slaximp,1) &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.129605450439467);}
			if(gte(slamimp,1) &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.129605450439467);}
			if(tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =407.289473684211;}
			if(tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =407.289473684211/ (1-0.129605450439467 );}
			}
			else if(eq(xmimpflag, 74)){
			if(gte(slaximp,1) &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.0794384325299229);}
			if(gte(slamimp,1) &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.0794384325299229);}
			if(tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =117.137931034483;}
			if(tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =117.137931034483/ (1-0.0794384325299229 );}
			}
			else if(eq(xmimpflag, 75)){
			if(gte(slaximp,1) &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.189369734252207);}
			if(gte(slamimp,1) &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.189369734252207);}
			if(tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =192.772020725389;}
			if(tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =192.772020725389/ (1-0.189369734252207 );}
			}
			else if(eq(xmimpflag, 76)){
			if(gte(slaximp,1) &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.131187789757565);}
			if(gte(slamimp,1) &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.131187789757565);}
			if(tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =199.041666666667;}
			if(tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =199.041666666667/ (1-0.131187789757565 );}
			}
			else if(eq(xmimpflag, 77)){
			if(gte(slaximp,1) &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.136342992788614);}
			if(gte(slamimp,1) &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.136342992788614);}
			if(tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =186.407894736842;}
			if(tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =186.407894736842/ (1-0.136342992788614 );}
			}
			else if(eq(xmimpflag, 78)){
			if(gte(slaximp,1) &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.103049659988616);}
			if(gte(slamimp,1) &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.103049659988616);}
			if(tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =155.470588235294;}
			if(tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =155.470588235294/ (1-0.103049659988616 );}
			}
			else if(eq(xmimpflag, 79)){
			if(gte(slaximp,1) &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.35);}
			if(gte(slamimp,1) &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.35);}
			if(tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =193.74358974359;}
			if(tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =193.74358974359/ (1-0.35 );}
			}
			else if(eq(xmimpflag, 80)){
			if(gte(slaximp,1) &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.0732085200996002);}
			if(gte(slamimp,1) &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.0732085200996002);}
			if(tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =249.692307692308;}
			if(tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =249.692307692308/ (1-0.0732085200996002 );}
			}
			else if(eq(xmimpflag, 81)){
			if(gte(slaximp,1) &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.0934359066589073);}
			if(gte(slamimp,1) &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.0934359066589073);}
			if(tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =352.952806122449;}
			if(tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =352.952806122449/ (1-0.0934359066589073 );}
			}
			else if(eq(xmimpflag, 82)){
			if(gte(slaximp,1) &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.07182740558555);}
			if(gte(slamimp,1) &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.07182740558555);}
			if(tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =419.619047619047;}
			if(tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =419.619047619047/ (1-0.07182740558555 );}
			}
			else if(eq(xmimpflag, 83)){
			if(gte(slaximp,1) &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.0956449943871365);}
			if(gte(slamimp,1) &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.0956449943871365);}
			if(tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =304.5625;}
			if(tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =304.5625 - (304.5625*0.0956449943871365 );}
			}
			else if(eq(xmimpflag, 84)){
			if(gte(slaximp,1) &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.163929225997462);}
			if(gte(slamimp,1) &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.163929225997462);}
			if(tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =319.285714285714;}
			if(tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =319.285714285714/ (1-0.163929225997462 );}
			}
			else if(eq(xmimpflag, 86)){
			if(gte(slaximp,1) &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.112733293827202);}
			if(gte(slamimp,1) &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.112733293827202);}
			if(tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =129.277777777778;}
			if(tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =129.277777777778/ (1-0.112733293827202 );}
			}
			else if(eq(xmimpflag, 87)){
			if(gte(slaximp,1) &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.0655504344628028);}
			if(gte(slamimp,1) &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.0655504344628028);}
			if(tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =211d;}
			if(tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =211/ (1-0.0655504344628028 );}
			}
			else if(eq(xmimpflag, 88)){
			if(gte(slaximp,1) &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.198929221794951);}
			if(gte(slamimp,1) &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.198929221794951);}
			if(tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =296.473684210526;}
			if(tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =296.473684210526 - (296.473684210526*0.198929221794951 );}
			}
			else if(eq(xmimpflag, 89)){
			if(gte(slaximp,1) &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.107517933823928);}
			if(gte(slamimp,1) &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.107517933823928);}
			if(tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =281.958333333333;}
			if(tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =281.958333333333/ (1-0.107517933823928 );}
			}
			else if(eq(xmimpflag, 90)){
			if(gte(slaximp,1) &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.028250184258012);}
			if(gte(slamimp,1) &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.028250184258012);}
			if(tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =208.341176470588;}
			if(tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =208.341176470588/ (1-0.028250184258012 );}
			}
			else if(eq(xmimpflag, 91)){
			if(gte(slaximp,1) &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.0487771272192143);}
			if(gte(slamimp,1) &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.0487771272192143);}
			if(tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =267.896551724138;}
			if(tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =267.896551724138/ (1-0.0487771272192143 );}
			}
			else if(eq(xmimpflag, 92)){
			if(gte(slaximp,1) &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.111975986975987);}
			if(gte(slamimp,1) &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.111975986975987);}
			if(tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =328.555555555556;}
			if(tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =328.555555555556/ (1-0.111975986975987 );}
			}
			else if(eq(xmimpflag, 93)){
			if(gte(slaximp,1) &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.0979648763988006);}
			if(gte(slamimp,1) &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.0979648763988006);}
			if(tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =101.111111111111;}
			if(tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =101.111111111111/ (1-0.0979648763988006 );}
			}
			else if(eq(xmimpflag, 94)){
			if(gte(slaximp,1) &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.297737659966491);}
			if(gte(slamimp,1) &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.297737659966491);}
			if(tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =319.733333333333;}
			if(tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =319.733333333333/ (1-0.297737659966491 );}
			}
			else if(eq(xmimpflag, 95)){
			if(gte(slaximp,1) &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.0220048899755501);}
			if(gte(slamimp,1) &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.0220048899755501);}
			if(tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =220.428571428571;}
			if(tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =220.428571428571/ (1-0.0220048899755501 );}
			}
			else if(eq(xmimpflag, 96)){
			if(gte(slaximp,1) &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0);}
			if(gte(slamimp,1) &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0);}
			if(tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =433d;}
			if(tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =433d/ (1-0 );}
			}
			else if(eq(xmimpflag, 137)){
			if(gte(slaximp,1) &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.12659407459354);}
			if(gte(slamimp,1) &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.12659407459354);}
			if(tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =104.986301369863;}
			if(tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =104.986301369863/ (1-0.12659407459354 );}
			}
			else if(eq(xmimpflag, 138)){
			if(gte(slaximp,1) &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.179201806454531);}
			if(gte(slamimp,1) &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.179201806454531);}
			if(tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =108.37037037037;}
			if(tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =108.37037037037 - (108.37037037037*0.179201806454531 );}
			}
			else if(eq(xmimpflag, 139)){
			if(gte(slaximp,1) &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.162003845923261);}
			if(gte(slamimp,1) &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.162003845923261);}
			if(tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =128.438775510204;}
			if(tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =128.438775510204/ (1-0.162003845923261 );}
			}
			else if(eq(xmimpflag, 140)){
			if(gte(slaximp,1) &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.171264386321147);}
			if(gte(slamimp,1) &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.171264386321147);}
			if(tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =557.6;}
			if(tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =557.6/ (1-0.171264386321147 );}
			}
			else if(eq(xmimpflag, 141)){
			if(gte(slaximp,1) &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.213152374545978);}
			if(gte(slamimp,1) &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.213152374545978);}
			if(tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =74d;}
			if(tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =74/ (1-0.213152374545978 );}
			}
			else if(eq(xmimpflag, 142)){
			if(gte(slaximp,1) &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.190548809128441);}
			if(gte(slamimp,1) &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.190548809128441);}
			if(tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =80.5625;}
			if(tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =80.5625 - (80.5625*0.190548809128441 );}
			}
			else if(eq(xmimpflag, 145)){
			if(gte(slaximp,1) &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.0577485174550083);}
			if(gte(slamimp,1) &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.0577485174550083);}
			if(tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =376.928571428571;}
			if(tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =376.928571428571/ (1-0.0577485174550083 );}
			}
			else if(eq(xmimpflag, 146)){
			if(gte(slaximp,1) &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.153749295952981);}
			if(gte(slamimp,1) &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.153749295952981);}
			if(tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =154.307692307692;}
			if(tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =154.307692307692/ (1-0.153749295952981 );}
			}
			else if(eq(xmimpflag, 147)){
			if(gte(slaximp,1) &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.143606923731731);}
			if(gte(slamimp,1) &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.143606923731731);}
			if(tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =165.903225806452;}
			if(tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =165.903225806452 - (165.903225806452*0.143606923731731 );}
			}
			else if(eq(xmimpflag, 148)){
			if(gte(slaximp,1) &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.254317624200109);}
			if(gte(slamimp,1) &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.254317624200109);}
			if(tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =199.730769230769;}
			if(tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =199.730769230769/ (1-0.254317624200109 );}
			}
			else if(eq(xmimpflag, 149)){
			if(gte(slaximp,1) &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.136559928551299);}
			if(gte(slamimp,1) &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.136559928551299);}
			if(tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =1003d;}
			if(tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =1003/ (1-0.136559928551299 );}
			}
			else if(eq(xmimpflag, 150)){
			if(gte(slaximp,1) &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.182187702624498);}
			if(gte(slamimp,1) &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.182187702624498);}
			if(tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =100.090909090909;}
			if(tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =100.090909090909/ (1-0.182187702624498 );}
			}
			else if(eq(xmimpflag, 151)){
			if(gte(slaximp,1) &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.00833333333333333);}
			if(gte(slamimp,1) &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.00833333333333333);}
			if(tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =127.103448275862;}
			if(tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =127.103448275862/ (1-0.00833333333333333 );}
			}
			else if(eq(xmimpflag, 152)){
			if(gte(slaximp,1) &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.100333848361108);}
			if(gte(slamimp,1) &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.100333848361108);}
			if(tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =436.5;}
			if(tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =436.5/ (1-0.100333848361108 );}
			}
			else if(eq(xmimpflag, 154)){
			if(gte(slaximp,1) &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.235321405225611);}
			if(gte(slamimp,1) &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.235321405225611);}
			if(tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =580.060606060606;}
			if(tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =580.060606060606/ (1-0.235321405225611 );}
			}
			else if(eq(xmimpflag, 155)){
			if(gte(slaximp,1) &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.157476046121814);}
			if(gte(slamimp,1) &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.157476046121814);}
			if(tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =70.0833333333334;}
			if(tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =70.0833333333334/ (1-0.157476046121814 );}
			}
			else if(eq(xmimpflag, 156)){
			if(gte(slaximp,1) &&  slaarriv==null &&  slastot==null) {slamimp = slaximp - (slaximp*0.17641709128796);}
			if(gte(slamimp,1) &&  tslavesd==null &&  tslavesp==null &&  ncartot==null) {slaximp = slamimp / (1- 0.17641709128796);}
			if(tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slamimp =118.333333333333;}
			if(tslavesd==null &&  tslavesp==null &&  ncartot==null &&  slaarriv==null &&  slastot==null) {slaximp =118.333333333333/ (1-0.17641709128796 );}
			}
				
			if (gt(sladvoy ,0) && slaarriv==null && tslavesd==null && tslavesp==null && ncartot==null && gte(slastot ,50)) {slaximp=add(new Double[]{slastot+sladvoy});}
			if (gt(sladvoy ,0) && tslavesd==null && tslavesp==null && ncartot==null && gt(slaarriv ,1)) {slaximp = add(new Double[]{slaarriv + sladvoy.doubleValue()});}
			if (gt(sladvoy ,0) && tslavesd==null && tslavesp==null && ncartot==null && slaarriv==null && slastot==null) {slaximp = add(new Double[]{slamimp, sladvoy.doubleValue()});}


		slaximp = round(slaximp,0);
		slamimp = round(slamimp,0);
			
			
		
		//Store in voyages object
		if(slaximp!=null){voyage.setSlaximp(slaximp.intValue());}
		else {voyage.setSlaximp(null);}
		
		if(slamimp!=null)
		{voyage.setSlamimp(slamimp.intValue());}
		else 
		{voyage.setSlamimp(null);}
				
		//default value of 0 if null to make it return null
		voyage.setMjbyptimp(Port.loadById(session, defVal(mjbyptimp, 0l))); 
		voyage.setMajbyimp(Region.loadById(session, defVal(majbyimp, 0l)));
		voyage.setMajbyimp1(Area.loadById(session, defVal(majbyimp1, 0l)));
		voyage.setMjslptimp(Port.loadById(session,defVal(mjslptimp, 0l)));
		voyage.setMjselimp(Region.loadById(session, defVal(mjselimp, 0l)));
		voyage.setMjselimp1(Area.loadById(session, defVal(mjselimp1, 0l)));
		
		
	}
	
	
	/*
	 * Calculates people variables
	 */
	public void calculateValuesPeople()  //Only used SPSS functions where changes were made b/c up to this point there were not problems with this function 
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
		Integer sladvoy=null;
		
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
			sladvoy = voyage.getSladvoy();
				  
		  
		  //Variables to be imputed
		  Integer adlt1imp = null;
		  Integer chil1imp = null;
		  Integer male1imp = null;
		  Integer feml1imp =null;
		  Integer adlt2imp = null;
		  Integer chil2imp = null;
		  Integer male2imp=null;
		  Integer feml2imp=null;
		  Double slavema1 = null;
		  Double slavemx1 = null;
		  Double slavmax1 = null;
		  Double chilrat1=null;
		  Double malrat1=null;
		  Double menrat1=null;
		  Double womrat1=null;
		  Double boyrat1=null;
		  Double girlrat1=null;
		  Integer adlt3imp = null;
		  Integer chil3imp = null;
		  Integer male3imp = null;
		  Integer feml3imp = null;
		  Double slavema3=null;
		  Double slavemx3 = null;
		  Double slavmax3=null;
		  Double chilrat3=null;
	      Double malrat3= null;
	      Double menrat3=null;
	      Double womrat3=null;
	      Double boyrat3=null;
	      Double girlrat3=null;
	      Double slavema7=null;
	      Double slavemx7=null;
	      Double slavmax7=null;
	      Integer men7=null;
	      Integer women7=null;
	      Integer boy7=null;
	      Integer girl7=null;
	      Integer adult7=null;
	      Integer child7=null;
	      Integer male7=null;
	      Integer female7=null;
	      Double menrat7=null;
	      Double womrat7=null;
	      Double boyrat7=null;
	      Double girlrat7=null;
	      Double malrat7=null;
	      Double chilrat7=null;
		  
		  
		  //set defaults
	      men1=(eq(men1, 0) ? null : men1);
	      women1=(eq(women1, 0) ? null : women1);
	      boy1=(eq(boy1, 0) ? null : boy1);
	      girl1=(eq(girl1, 0) ? null : girl1);
	      child1=(eq(child1, 0) ? null : child1);
	      infant1=(eq(infant1, 0) ? null : infant1);
	      adult1=(eq(adult1, 0) ? null : adult1);
	      male1=(eq(male1, 0) ? null : male1);
	      female1=(eq(female1, 0) ? null : female1);
	      men2=(eq(men2, 0) ? null : men2);
	      women2=(eq(women2, 0) ? null : women2);
	      boy2=(eq(boy2, 0) ? null : boy2);
	      girl2=(eq(girl2, 0) ? null : girl2);
	      child2=(eq(child2, 0) ? null : child2);
	      adult2=(eq(adult2, 0) ? null : adult2);
	      male2=(eq(male2, 0) ? null : male2);
	      female2=(eq(female2, 0) ? null : female2);
	      men3=(eq(men3, 0) ? null : men3);
	      women3=(eq(women3, 0) ? null : women3);
	      boy3=(eq(boy3, 0) ? null : boy3);
	      girl3=(eq(girl3, 0) ? null : girl3);
	      child3=(eq(child3, 0) ? null : child3);
	      infant3=(eq(infant3, 0) ? null : infant3);
	      adult3=(eq(adult3, 0) ? null : adult3);
	      male3=(eq(male3, 0) ? null : male3);
	      female3=(eq(female3, 0) ? null : female3);
	      men4=(eq(men4, 0) ? null : men4);
	      women4=(eq(women4, 0) ? null : women4);
	      boy4=(eq(boy4, 0) ? null : boy4);
	      girl4=(eq(girl4, 0) ? null : girl4);
	      child4=(eq(child4, 0) ? null : child4);
	      infant4=(eq(infant4, 0) ? null : infant4);
	      adult4=(eq(adult4, 0) ? null : adult4);
	      male4=(eq(male4, 0) ? null : male4);
	      female4=(eq(female4, 0) ? null : female4);
	      men5=(eq(men5, 0) ? null : men5);
	      women5=(eq(women5, 0) ? null : women5);
	      boy5=(eq(boy5, 0) ? null : boy5);
	      girl5=(eq(girl5, 0) ? null : girl5);
	      child5=(eq(child5, 0) ? null : child5);
	      adult5=(eq(adult5, 0) ? null : adult5);
	      male5=(eq(male5, 0) ? null : male5);
	      female5=(eq(female5, 0) ? null : female5);
	      men6=(eq(men6, 0) ? null : men6);
	      women6=(eq(women6, 0) ? null : women6);
	      boy6=(eq(boy6, 0) ? null : boy6);
	      girl6=(eq(girl6, 0) ? null : girl6);
	      child6=(eq(child6, 0) ? null : child6);
	      adult6=(eq(adult6, 0) ? null : adult6);
	      male6=(eq(male6, 0) ? null : male6);
	      female6=(eq(female6, 0) ? null : female6);

          if(
	    		  (men1 != null && men1 >= 1) || 
	    		  (women1 != null && women1 >= 1) || 
	    		  (boy1 != null && boy1 >= 1) || 
	    		  (girl1 != null && girl1 >= 1) || 
	    		  (child1 != null && child1 >= 1) || 
	    		  (infant1 != null && infant1 >= 1) || 
	    		  (adult1 != null && adult1 >= 1) ||  
	    		  (men4 != null && men4 >= 1) || 
	    		  (women4 != null && women4 >= 1) || 
	    		  (boy4 != null && boy4 >= 1) || 
	    		  (girl4 != null && girl4 >= 1) || 
	    		  (child4 != null && child4 >= 1) || 
	    		  (infant4 != null && infant4 >= 1) || 
	    		  (adult4 != null && adult4 >= 1) || 
	    		  (men5 != null && men5 >= 1) || 
	    		  (women5 != null && women5 >= 1) || 
	    		  (boy5 != null && boy5 >= 1) || 
	    		  (girl5 != null && girl5 >= 1) || 
	    		  (child5 != null && child5 >= 1) || 
	    		  (adult5 !=null && adult5 >=1)
	    	)
	      {
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
		    	    
	      }
	      
	      //do calculations
	      adlt1imp = add(new Integer[]{men1, women1, adult1, men4, women4, adult4, men5, women5, adult5});
	      chil1imp = add(new Integer[]{boy1, girl1, child1, infant1, boy4, girl4, child4, infant4, boy5, girl5, child5});
	      

	      adlt1imp=(eq(adlt1imp, 0) ? null : adlt1imp);
	      chil1imp=(eq(chil1imp, 0) ? null : chil1imp);
	      
	      if(gte(tslavesd,1) && adlt1imp==null && eq(chil1imp, tslavesd))  {adlt1imp = 0;}
	      if(gte(tslavesd,1) && chil1imp==null && eq(adlt1imp, tslavesd)) {chil1imp = 0;}
	      if(tslavesd==null && adlt1imp==null && eq(chil1imp, tslavesd)) {adlt1imp = 0;}
	      if(tslavesd==null && chil1imp==null && eq(adlt1imp, tslavesd)) {chil1imp = 0;}

		  
	    //set more default values
	      if(
	    		  (male1 !=null && male1 >= 1) || 
	    		  (female1 !=null && female1 >= 1) || 
	    		  (male4 !=null && male4 >= 1) || 
	    		  (female4 !=null && female4 >= 1) || 
	    		  (male5 !=null && male5 >= 1) || 
	    		  (female5 != null && female5 >= 1)
	    	)
	      {
	        male1=defVal(male1,0);
		    female1=defVal(female1, 0);
		    male4=defVal(male4, 0);
		    female4=defVal(female4, 0);
		    male5=defVal(male5, 0);
		    female5=defVal(female5, 0);
	      }

           
			//Do more calculations
			 male1imp = add(new Integer[]{male1, male4, male5});
			 feml1imp = add(new Integer[]{female1, female4, female5});
			 			  
			 if (male1imp==null) {male1imp = add(new Integer[]{men1,boy1,men4,boy4,men5,boy5});}
			 if (feml1imp==null) {feml1imp = add(new Integer[]{women1,girl1,women4,girl4,women5,girl5});}
				
			 male1imp=(eq(male1imp, 0) ? null : male1imp);
			 feml1imp=(eq(feml1imp, 0) ? null : feml1imp); 

			 if (gte(tslavesd,1) && male1imp==null && eq(feml1imp,tslavesd)) {male1imp = 0;}
			 if (gte(tslavesd,1) && feml1imp==null && eq(male1imp,tslavesd)) {feml1imp = 0;}
			 if (tslavesd==null && male1imp==null && eq(feml1imp,tslavesd)) {male1imp = 0;}
			 if (tslavesd==null && feml1imp==null && eq(male1imp,tslavesd)) {feml1imp = 0;}


			  

 			 slavema1 = addD(new Integer[]{adlt1imp,chil1imp});
			 slavemx1 = addD(new Integer[]{male1imp, feml1imp});
			 slavmax1=addD(new Integer[]{men1,women1,boy1,girl1,men4,women4,boy4,girl4,men5,women5,boy5,girl5});
			

			 if(slavema1!=null && slavema1 >=0 && slavema1 <= 19) {slavema1=null;}
			 if(slavemx1!=null && slavemx1 >=0 && slavemx1 <=19) {slavemx1=null;}
			 if(slavmax1!=null && slavmax1 >=0 && slavmax1 <=19) {slavmax1=null;}
			 
			 
			 if (slavema1==null)
			 {
			     if(adlt1imp!=null && adlt1imp >=0) {adlt1imp=null;}
			     if(chil1imp!=null && chil1imp >=0)  {chil1imp=null;}
			 }

			 if(slavemx1==null)
			 {
			  if(feml1imp!=null && feml1imp >=0) {feml1imp=null;}
			  if(male1imp!=null && male1imp >=0) {male1imp=null;}
			 }
			 
			 if(chil1imp!=null && slavema1!=null && slavema1!=0){chilrat1=chil1imp.doubleValue()/slavema1;} 
			 if(male1imp!=null && slavemx1!=null && slavemx1!=0) {malrat1= male1imp.doubleValue()/slavemx1;}
			 
			 if (slavmax1!=null && slavmax1 >= 20 && slavmax1!=0) 
			 {
				 Integer temp=add(new Integer[]{men1, men4,men5});
				 if(temp !=null) menrat1=temp.doubleValue()/slavmax1;
			 }
			 
			 if (slavmax1!=null && slavmax1 >= 20 && slavmax1 !=0) 
			 {
				 Integer temp = add(new Integer[]{women1, women4,women5});
				 if(temp !=null) {womrat1=temp.doubleValue()/slavmax1;} 
			 }
			 
			 if (slavmax1!=null && slavmax1 >= 20 && slavmax1!=0) 
			 {
				 Integer temp = add(new Integer[]{boy1, boy4, boy5});
				 if (temp!=null) {boyrat1=temp.doubleValue()/slavmax1;}
			 }
			 
			 if (slavmax1!=null && slavmax1 >= 20 && slavmax1!=0) 
			 {
				 Integer temp = add(new Integer[]{girl1, girl4,girl5});
			 	 if(temp!=null) {girlrat1=temp.doubleValue()/slavmax1;}
			 }
		
		 //Set more Defaults
		 if(
				 (men3 != null && men3 >= 1) || 
				 (women3 != null && women3 >= 1) || 
				 (boy3 != null && boy3 >= 1) || 
				 (girl3 != null && girl3 >= 1) || 
				 (child3 != null && child3 >= 1) || 
				 (infant3 != null && infant3 >= 1) || 
				 (adult3 != null && adult3 >= 1) || 
				 (men6 != null && men6 >= 1) || 
				 (women6 != null && women6 >= 1) || 
				 (boy6 != null && boy6 >= 1) || 
				 (girl6 != null && girl6 >= 1) || 
				 (child6 != null && child6 >= 1) || 
				 (adult6 != null && adult6 >= 1)
			)
		 {
			 
			 
		   men3=defVal(men3, 0);
		   women3=defVal(women3, 0);
		   boy3=defVal(boy3, 0);
		   girl3=defVal(girl3, 0);
		   child3=defVal(child3, 0);
		   infant3=defVal(infant3, 0);
		   adult3=defVal(adult3, 0);
		   men6=defVal(men6, 0);
		   women6=defVal(women6, 0);
		   boy6=defVal(boy6, 0);
		   girl6=defVal(girl6, 0);
		   child6=defVal(child6, 0);
		   adult6=defVal(adult6, 0);
		 }
		 
		 adlt3imp = add(new Integer[]{men3,women3,adult3,men6,women6,adult6});
		 chil3imp = add(new Integer[]{boy3,girl3,child3,infant3,boy6,girl6,child6});
		 
		 if(
				 (male3 != null && male3 >= 1) || 
				 (female3 != null && female3 >= 1) || 
				 (male6 != null && male6 >=1) || 
				 (female6!=null && female6 >=1)
			)
		 {
		   male3=defVal(male3, 0);
		   female3=defVal(female3, 0);
		   male6=defVal(male6, 0);
		   female6=defVal(female6, 0);
		 }

			
			 male3imp = add(new Integer[]{male3,male6});
			 feml3imp = add(new Integer[]{female3,female6});
			 
		     if (male3imp==null) {male3imp = add(new Integer[]{men3,boy3,men6,boy6});}
		     if (feml3imp==null) {feml3imp = add(new Integer[]{women3,girl3,women6,girl6});}


		     adlt3imp=(eq(adlt3imp, 0) ? null : adlt3imp);
		     chil3imp=(eq(chil3imp, 0) ? null : chil3imp);
		     male3imp=(eq(male3imp, 0) ? null : male3imp);
		     feml3imp=(eq(feml3imp, 0) ? null : feml3imp);

		     if(gte(slaarriv, 1) && adlt3imp==null && eq(chil3imp, slaarriv)) {adlt3imp = 0;}
		     if(gte(slaarriv, 1) && chil3imp==null && eq(adlt3imp, slaarriv)) {chil3imp = 0;}
		     if(gte(slaarriv, 1) && male3imp==null && eq(feml3imp, slaarriv)) {male3imp = 0;}
		     if(gte(slaarriv, 1) && feml3imp==null && eq(male3imp, slaarriv)) {feml3imp = 0;}



			 
			 

			 slavema3 = addD(new Integer[]{adlt3imp,chil3imp});
			 slavemx3 = addD(new Integer[]{male3imp,feml3imp});
			 slavmax3=addD(new Integer[]{men3,women3,boy3,girl3,men6,women6,boy6,girl6});
			 

             if(slavema3!=null && slavema3 >= 0 && slavema3 <= 19) {slavema3=null;}
             if(slavemx3!=null && slavemx3 >= 0 && slavemx3 <= 19) {slavemx3=null;}
             if(slavmax3!=null && slavmax3 >= 0 && slavmax3 <= 19) {slavmax3=null;}


			 if (slavema3==null)
			 {
			  if(adlt3imp!=null && adlt3imp >=0) {adlt3imp=null;}
			  if(chil3imp!=null && chil3imp >=0) {chil3imp=null;}
			 }

			 if(slavemx3==null)
			 {
			  if(feml3imp!=null && feml3imp >=0) {feml3imp=null;}
			  if(male3imp!=null && male3imp >=0) {male3imp=null;}
			 }

			 if(chil3imp!=null && slavema3!=null && slavema3!=0) {chilrat3=chil3imp/slavema3;} 
			 if(male3imp!=null && slavemx3!=null && slavemx3!=0) {malrat3= male3imp/slavemx3;} 
			 
			 if(slavmax3!=null && slavmax3 >= 20 && slavmax3!=0) 
			 {
				 Integer temp = add(new Integer[]{men3,men6});
				 if(temp != null) {menrat3=temp/slavmax3;}
			 }
			 
			 if(slavmax3!=null && slavmax3 >= 20 && slavmax3!=0) 
			 {
				 Integer temp=add(new Integer[]{women3,women6});
				 if(temp != null) {womrat3=temp/slavmax3;}
			 }
			 
			 if(slavmax3!=null && slavmax3 >= 20 && slavmax3!=0) 
			 {
				 Integer temp = add(new Integer[] {boy3,boy6});
				 if(temp!=null) {boyrat3=temp/slavmax3;}
			 }
			 
			 if(slavmax3!=null && slavmax3 >= 20 && slavmax3!=0) 
			 {
				 Integer temp = add(new Integer[] {girl3,girl6});
				 if(temp!=null) {girlrat3=temp/slavmax3;}
			}

			 if(slavema3!=null && slavema3 >= 20) {slavema7=slavema3;}
			 if(slavemx3!=null && slavemx3 >= 20) {slavemx7=slavemx3;}
			 if(slavmax3!=null && slavmax3 >= 20) {slavmax7=slavmax3;}
			 if(slavmax7!=null && slavmax7 >= 20) {men7=add(new Integer[]{men3,men6});}
			 if(slavmax7!=null && slavmax7 >= 20) {women7=add(new Integer[]{women3,women6});}
			 if(slavmax7!=null && slavmax7 >= 20) {boy7=add(new Integer[]{boy3,boy6});}
			 if(slavmax7!=null && slavmax7 >= 20) {girl7=add(new Integer[]{girl3,girl6});}
			 if(slavema7!=null && slavema7 >= 20) {adult7=(adlt3imp==null ? null : adlt3imp.intValue());}
			 if(slavema7!=null && slavema7 >= 20) {child7=(chil3imp==null ? null : chil3imp.intValue());}
			 if(slavemx7!=null && slavemx7 >=20 ) {male7=(male3imp==null ? null : male3imp.intValue());}
			 if(slavemx7!=null && slavemx7 >= 20) {female7=(feml3imp==null ? null : feml3imp.intValue());}
			 if(menrat3!=null && menrat3 >= 0) {menrat7=menrat3;}
			 if(womrat3!=null && womrat3 >= 0) {womrat7=womrat3;}
			 if(boyrat3!=null && boyrat3 >= 0) {boyrat7=boyrat3;}
			 if(girlrat3!=null && girlrat3 >= 0) {girlrat7=girlrat3;}
			 if(malrat3!=null && malrat3 >= 0) {malrat7=malrat3;}
			 if(chilrat3!=null && chilrat3 >= 0) {chilrat7=chilrat3;}

			 if (slavema3==null && slavema1!=null && slavema1 >=20) {slavema7=slavema1;}
			 if (slavemx3==null && slavemx1!=null && slavemx1 >=20) {slavemx7=slavemx1;}
			 if (slavmax3==null && slavmax1!=null && slavmax1 >=20) {slavmax7=slavmax1;}
			 if (slavmax3==null && slavmax1!=null && slavmax1 >= 20) {men7=add(new Integer[]{men1+men4,men5});}
			 if (slavmax3==null && slavmax1!=null && slavmax1 >= 20) {women7=add(new Integer[]{women1,women4,women5});}
			 if (slavmax3==null && slavmax1!=null && slavmax1 >= 20) {boy7=add(new Integer[]{boy1,boy4,boy5});}
			 if (slavmax3==null && slavmax1!=null && slavmax1 >= 20) {girl7=add(new Integer[]{girl1,girl4,girl5});}
			 if (slavema3==null && slavema1!=null && slavema1 >= 20) {adult7=adlt1imp;}
			 if (slavema3==null && slavema1!=null && slavema1 >= 20) {child7=chil1imp;}
			 if (slavemx3==null && slavemx1!=null && slavemx1 >=20) {male7=male1imp;}
			 if (slavemx3==null && slavemx1!=null && slavemx1 >= 20) {female7=feml1imp;}
			 if (menrat3==null && menrat1!=null && menrat1 >= 0) {menrat7=menrat1;}
			 if (womrat3==null && womrat1!=null && womrat1 >= 0) {womrat7=womrat1;}
			 if (boyrat3==null && boyrat1!=null && boyrat1 >= 0) {boyrat7=boyrat1;}
			 if (girlrat3==null && girlrat1!=null && girlrat1 >= 0) {girlrat7=girlrat1;}
			 if (malrat3==null && malrat1!=null && malrat1 >= 0) {malrat7=malrat1;}
			 if (chilrat3==null && chilrat1!=null && chilrat1 >= 0) {chilrat7=chilrat1;}

		 //more defaults
		 if
		 (
				 (men2 != null && men2 >= 1) || 
				 (women2 != null && women2 >= 1) || 
				 (boy2 != null && boy2 >= 1) || 
				 (girl2 != null && girl2 >= 1) || 
				 (child2 != null && child2 >= 1) ||
				 (adult2 != null && adult2 >= 1)
		 )
		 {	
		  men2=defVal(men2, 0);
		  women2=defVal(women2, 0);
		  boy2=defVal(boy2, 0);
		  girl2=defVal(girl2, 0);
		  child2=defVal(child2, 0);
		  adult2=defVal(adult2, 0);
		}

		 adlt2imp = add(new Integer[]{men2,women2,adult2});
		 chil2imp = add(new Integer[]{boy2,girl2,child2});
		 
		 if(
				 (male2 != null && male2 >= 1) || 
				 (female2 != null && female2 >= 1)
			)
		 {
		   male2=defVal(male2, 0);
		   female2=defVal(female2, 0);
		 }
		 
		 
		 male2imp=male2;
		 feml2imp=female2;
		 
		 if(male2imp==null){male2imp=add(new Integer[]{men2,boy2});}
		 if(feml2imp==null){feml2imp=add(new Integer[]{women2,girl2});}
		 
		 if(gte(sladvoy, 1) && eq(adlt2imp, 0) && gt(sladvoy, chil2imp)) {adlt2imp = sub(sladvoy, chil2imp);}
		 if(gte(sladvoy, 1) && eq(chil2imp, 0) && gt(sladvoy, adlt2imp)) {chil2imp = sub(sladvoy, adlt2imp);}
		 if(gte(sladvoy, 1) && eq(male2imp, 0) && gt(sladvoy, feml2imp)) {male2imp = sub(sladvoy, feml2imp);}
		 if(gte(sladvoy, 1) && eq(feml2imp, 0) && gt(sladvoy, male2imp)) {feml2imp = sub(sladvoy, male2imp);}


		 
		 //Save back to voyage object
		 voyage.setAdlt1imp(adlt1imp); 
		 voyage.setChil1imp((chil1imp==null ? null : chil1imp));
		 voyage.setMale1imp((male1imp==null ? null : male1imp));
		 voyage.setFeml1imp((feml1imp==null ? null : feml1imp));
		 voyage.setAdlt2imp(adlt2imp);
		 voyage.setChil2imp(chil2imp);
		 voyage.setMale2imp(male2imp);
		 voyage.setFeml2imp(feml2imp);
		 voyage.setSlavema1((slavema1==null ? null : round(slavema1, 6)));
		 voyage.setSlavemx1((slavemx1==null ? null : round(slavemx1, 6)));
		 voyage.setSlavmax1(slavmax1);
		 voyage.setChilrat1(round(chilrat1,6));
		 voyage.setMalrat1(round(malrat1,6));  
		 voyage.setMenrat1(round(menrat1,6));
		 voyage.setWomrat1(round(womrat1,6));
		 voyage.setBoyrat1(round(boyrat1,6));
		 voyage.setGirlrat1(round(girlrat1,6));
		 voyage.setAdlt3imp(adlt3imp);
		 voyage.setChil3imp(chil3imp);
		 voyage.setMale3imp(male3imp);
		 voyage.setFeml3imp(feml3imp);
		 voyage.setSlavema3(round(slavema3, 6));
		 voyage.setSlavemx3(round(slavemx3, 6));
		 voyage.setSlavmax3((slavmax3==null ? null : slavmax3.intValue()));
		 voyage.setChilrat3(round(chilrat3,6));
	     voyage.setMalrat3(round(malrat3,6));
	     voyage.setMenrat3(round(menrat3,6));
	     voyage.setWomrat3(round(womrat3,6));
	     voyage.setBoyrat3(round(boyrat3,6));
	     voyage.setGirlrat3(round(girlrat3,6));
	     voyage.setSlavema7(round(slavema7, 6));
	     voyage.setSlavemx7(round(slavemx7, 6));
	     voyage.setSlavmax7((slavmax7==null ? null : slavmax7.intValue()));
	     voyage.setMen7(men7);
	     voyage.setWomen7(women7);
         voyage.setBoy7(boy7);
	     voyage.setGirl7(girl7);
	     voyage.setAdult7(adult7);
	     voyage.setChild7(child7);
	     voyage.setMale7(male7);
	     voyage.setFemale7(female7);
		 voyage.setMenrat7((menrat7==null ? null : round(menrat7.floatValue(),6)));
		 voyage.setWomrat7((womrat7==null ? null : round(womrat7.floatValue(),6)));
		 voyage.setBoyrat7((boyrat7==null ? null : round(boyrat7.floatValue(),6)));
		 voyage.setGirlrat7((girlrat7==null ? null : round(girlrat7.floatValue(),6)));
		 voyage.setMalrat7((malrat7==null ? null : round(malrat7.floatValue(),6)));
		 voyage.setChilrat7((chilrat7==null ? null : round(chilrat7.floatValue(),6)));
	}
	
	public void calculateXmImpflag(){
		
		//Input
		Integer rig = (voyage.getRig()==null ? null : voyage.getRig().getId().intValue());
		Integer majbyimp = (voyage.getMajbyimp()==null ? null : voyage.getMajbyimp().getId().intValue());
		Integer natinimp = (voyage.getNatinimp()==null ? null : voyage.getNatinimp().getId().intValue());
		Integer mjselimp = (voyage.getMjselimp()==null ? null: voyage.getMjselimp().getId().intValue());
		Integer mjselimp1 = (voyage.getMjselimp1()==null ? null: voyage.getMjselimp1().getId().intValue());
		Integer yearam = voyage.getYearam();
		
		//Variable to impute
		Double xmimpflag=null;
			
	    int[] rigArray = {29, 42, 43, 54, 59, 61, 65, 80, 86};
	    //Do Calculations
	    if (eq(rig, 35) && eq(natinimp, 9)) {xmimpflag = 156d;}
		else if  (eq(rig, 27) && eq(natinimp, 9)) {xmimpflag = 155d;}
		else if  (gte(yearam, 1826) && lt(yearam, 1876) && eq(rig, 9) && eq(natinimp, 9)) {xmimpflag = 154d;}
		else if  (gt(yearam, 1825) && eq(rig, 8) && eq(natinimp, 9))  {xmimpflag = 152d;}
		else if  (gte(yearam, 1776) && lt(yearam, 1826) && eq(rig, 8) && eq(natinimp, 9))  {xmimpflag = 151d;}
		else if  (lt(yearam, 1776) && eq(rig, 8) && eq(natinimp, 9)) {xmimpflag = 150d;}
		else if  (gt(yearam, 1825) && eq(rig, 4) && eq(natinimp, 9))  {xmimpflag = 149d;}
		else if  (gte(yearam, 1801) && lt(yearam, 1826) && eq(rig, 4) && eq(natinimp, 9))  {xmimpflag = 148d;}
		else if  (gte(yearam, 1776) && lt(yearam, 1801) && eq(rig, 4) && eq(natinimp, 9))  {xmimpflag = 147d;}
		else if  (lt(yearam, 1776) && eq(rig, 4) && eq(natinimp, 9)) {xmimpflag = 146d;}
		else if  (gt(yearam, 1825) && (eq(rig, 2) || eq(rig, 5)) && eq(natinimp, 9))  {xmimpflag = 145d;}
		else if  (gte(yearam, 1801) && lt(yearam, 1826) && eq(rig, 5) && eq(natinimp, 9))  {xmimpflag = 143d;}
		else if  (gte(yearam, 1776) && lt(yearam, 1801) && (eq(rig, 2) || eq(rig, 5)) && eq(natinimp, 9))  {xmimpflag = 142d;}
		else if  (lt(yearam, 1776) && (eq(rig, 2) || eq(rig, 5)) && eq(natinimp, 9)) {xmimpflag = 141d;}
		else if  (gt(yearam, 1825) && eq(rig, 1) && eq(natinimp, 9))  {xmimpflag = 140d;}
		else if  (gte(yearam, 1801) && lt(yearam, 1826) && eq(rig, 1) && eq(natinimp, 9))  {xmimpflag = 139d;}
		else if  (gte(yearam, 1776) && lt(yearam, 1801) && eq(rig, 1) && eq(natinimp, 9))  {xmimpflag = 138d;}
		else if  (lt(yearam, 1776) && eq(rig, 1) && eq(natinimp, 9)) {xmimpflag = 137d;}
		else if  (gte(yearam, 1826) && lt(yearam, 1876) && eq(rig, 60))  {xmimpflag = 96d;}		
		else if  (gte(yearam, 1726) && lt(yearam, 1826) && eq(rig, 60))  {xmimpflag = 95d;}
		else if  (lt(yearam, 1726) && eq(rig, 60)) {xmimpflag = 94d;}
		else if  (eq(rig, 17)||eq(rig, 19)||eq(rig, 52)||eq(rig, 53)) {xmimpflag = 93d;}
		else if  (gte(yearam, 1851) && lt(yearam, 1876) && (eq(rig, 16) || eq(rig, 51)))  {xmimpflag = 92d;}
		else if  (gte(yearam, 1826) && lt(yearam, 1851) && (eq(rig, 16) || eq(rig, 51)))  {xmimpflag = 91d;}
		else if  (lt(yearam, 1826) && (eq(rig, 16) || eq(rig, 51))) {xmimpflag = 90d;}
		else if  (gte(yearam, 1826) && lt(yearam, 1876) && ( eq(rig, 14) || eq(rig, 36) || eq(rig, 49)))  {xmimpflag = 89d;}
		else if  (lt(yearam, 1826) && (eq(rig, 14) || eq(rig, 36) || eq(rig, 49))) {xmimpflag = 88d;}		
		else if  (eq(rig, 48)) {xmimpflag = 87d;}
		else if  (eq(rig, 47)) {xmimpflag = 86d;}
		else if  (eq(rig, 44)) {xmimpflag = 84d;}
		else if  (eq(rig, 41) || eq(rig, 57)) {xmimpflag = 83d;}
		else if  (gte(yearam, 1826) && lt(yearam, 1876) && eq(rig, 40))  {xmimpflag = 82d;}
		else if  (gte(yearam, 1801) && lt(yearam, 1826) && eq(rig, 40))  {xmimpflag = 81d;}
		else if  (gte(yearam, 1776) && lt(yearam, 1801) && eq(rig, 40))  {xmimpflag = 80d;}
		else if  (lt(yearam, 1776) && eq(rig, 40)) {xmimpflag = 79d;}
		else if  (gte(yearam, 1801) && lt(yearam, 1877) && eq(rig, 35))  {xmimpflag = 78d;}
		else if  (gte(yearam, 1776) && lt(yearam, 1801) && eq(rig, 35))  {xmimpflag = 77d;}
		else if  (gte(yearam, 1751) && lt(yearam, 1776) && eq(rig, 35))  {xmimpflag = 76d;}
		else if  (gte(yearam, 1726) && lt(yearam, 1751) && eq(rig, 35))  {xmimpflag = 75d;}
		else if  (lt(yearam, 1726) && eq(rig, 35)) {xmimpflag = 74d;}
		else if  (eq(rig, 32) || eq(rig, 39)) {xmimpflag = 73d;}
		else if  (gte(yearam, 1826) && lt(yearam, 1876) && (eq(rig, 30) || eq(rig, 45) || eq(rig, 63))) {xmimpflag = 85d;}
		else if  (gte(yearam, 1801) && lt(yearam, 1826) && (eq(rig, 30) || eq(rig, 45) || eq(rig, 63)))  {xmimpflag = 72d;}
		else if  (gte(yearam, 1776) && lt(yearam, 1801) && (eq(rig, 30) || eq(rig, 45) || eq(rig, 63))) {xmimpflag = 97d;}
		else if  (gte(yearam, 1726) && lt(yearam, 1776) && (eq(rig, 30) || eq(rig, 45) || eq(rig, 63)))  {xmimpflag = 71d;}
		else if  (lt(yearam, 1726) && (eq(rig, 30) || eq(rig, 45) || eq(rig, 63))) {xmimpflag = 70d;}
		else if  (eq(rig, 28)) {xmimpflag = 69d;}
		else if  (gte(yearam, 1801) && lt(yearam, 1877) && eq(rig, 27))  {xmimpflag = 68d;}
		else if  (gte(yearam, 1776) && lt(yearam, 1801) && eq(rig, 27))  {xmimpflag = 67d;}
		else if  (gte(yearam, 1751) && lt(yearam, 1776) && eq(rig, 27))  {xmimpflag = 66d;}
		else if  (lt(yearam, 1751) && eq(rig, 27)) {xmimpflag = 65d;}
		else if  (gte(yearam, 1851) && lt(yearam, 1877) && eq(rig, 25))  {xmimpflag = 64d;}
		else if  (gte(yearam, 1826) && lt(yearam, 1851) && eq(rig, 25))  {xmimpflag = 160d;}
		else if  (gte(yearam, 1801) && lt(yearam, 1826) && eq(rig, 25))  {xmimpflag = 63d;}
		else if  (gte(yearam, 1776) && lt(yearam, 1801) && eq(rig, 25))  {xmimpflag = 62d;}
		else if  (gte(yearam, 1751) && lt(yearam, 1776) && eq(rig, 25))  {xmimpflag = 61d;}
		else if  (lt(yearam, 1751) && eq(rig, 25)) {xmimpflag = 60d;}
		else if  (eq(rig, 23)) {xmimpflag = 59d;}
		else if  (eq(rig, 21)) {xmimpflag = 58d;}
		else if  (eq(rig, 20)) {xmimpflag = 57d;}
		else if  (eq(rig, 15)) {xmimpflag = 56d;}
		else if  (gte(yearam, 1826) && lt(yearam, 1877) && eq(rig, 13))  {xmimpflag = 55d;}
		else if  (gte(yearam, 1801) && lt(yearam, 1826) && eq(rig, 13))  {xmimpflag = 54d;}
		else if  (gte(yearam, 1776) && lt(yearam, 1801) && eq(rig, 13))  {xmimpflag = 53d;}
		else if  (gte(yearam, 1751) && lt(yearam, 1776) && eq(rig, 13))  {xmimpflag = 52d;}
		else if  (lt(yearam, 1751) && eq(rig, 13)) {xmimpflag = 51d;}
		else if  (eq(rig, 11) || eq(rig, 12)) {xmimpflag = 50d;}
		else if  (eq(rig, 10)||eq(rig, 24)) {xmimpflag = 49d;}
		else if  (gte(yearam, 1851) && lt(yearam, 1876) && (eq(rig, 9) || eq(rig, 31)))  {xmimpflag = 48d;}
		else if  (gte(yearam, 1826) && lt(yearam, 1851) && (eq(rig, 9) || eq(rig, 31)))  {xmimpflag = 47d;}
		else if  (lt(yearam, 1826) && (eq(rig, 9) || eq(rig, 31))) {xmimpflag = 46d;}
		else if  (gte(yearam, 1851) && lt(yearam, 1876) && eq(rig, 8))  {xmimpflag = 45d;}
		else if  (gte(yearam, 1826) && lt(yearam, 1851) && eq(rig, 8))  {xmimpflag = 44d;}
		else if  (gte(yearam, 1801) && lt(yearam, 1826) && eq(rig, 8))  {xmimpflag = 43d;}
		else if  (gte(yearam, 1776) && lt(yearam, 1801) && eq(rig, 8))  {xmimpflag = 42d;}
		else if  (lt(yearam, 1776) && eq(rig, 8)) {xmimpflag = 41d;}
		else if  (eq(rig, 7)) {xmimpflag = 40d;}		
		else if  (eq(rig, 6)) {xmimpflag = 39d;}
		else if  (eq(rig, 5)) {xmimpflag = 38d;}
		else if  (gte(yearam, 1851) && lt(yearam, 1876) && eq(rig, 4))  {xmimpflag = 37d;}
		else if  (gte(yearam, 1826) && lt(yearam, 1851) && eq(rig, 4))  {xmimpflag = 36d;}
		else if  (gte(yearam, 1801) && lt(yearam, 1826) && eq(rig, 4))  {xmimpflag = 35d;}
		else if  (gte(yearam, 1776) && lt(yearam, 1801) && eq(rig, 4))  {xmimpflag = 34d;}
		else if  (gte(yearam, 1751) && lt(yearam, 1776) && eq(rig, 4))  {xmimpflag = 33d;}
		else if  (gte(yearam, 1726) && lt(yearam, 1751) && eq(rig, 4))  {xmimpflag = 32d;}
		else if  (lt(yearam, 1726) && eq(rig, 4)) {xmimpflag = 31d;}
		else if  (gte(yearam, 1801) && lt(yearam, 1876) && eq(rig, 3))  {xmimpflag = 30d;}
		else if  (gte(yearam, 1776) && lt(yearam, 1801) && eq(rig, 3))  {xmimpflag = 29d;}
		else if  (gte(yearam, 1751) && lt(yearam, 1776) && eq(rig, 3))  {xmimpflag = 28d;}
		else if  (lt(yearam, 1751) && eq(rig, 3)) {xmimpflag = 27d;}
		else if  (gte(yearam, 1851) && lt(yearam, 1876) && eq(rig, 2))  {xmimpflag = 26d;}		
		else if  (gte(yearam, 1826) && lt(yearam, 1851) && eq(rig, 2))  {xmimpflag = 25d;}
		else if  (gte(yearam, 1801) && lt(yearam, 1826) && eq(rig, 2))  {xmimpflag = 24d;}
		else if  (gte(yearam, 1776) && lt(yearam, 1801) && eq(rig, 2))  {xmimpflag = 23d;}
		else if  (lt(yearam, 1776) && eq(rig, 2)) {xmimpflag = 22d;}
		else if  (gte(yearam, 1851) && lt(yearam, 1876) && eq(rig, 1))  {xmimpflag = 21d;}
		else if  (gte(yearam, 1826) && lt(yearam, 1851) && eq(rig, 1))  {xmimpflag = 20d;}
		else if  (gte(yearam, 1801) && lt(yearam, 1826) && eq(rig, 1))  {xmimpflag = 19d;}
		else if  (gte(yearam, 1776) && lt(yearam, 1801) && eq(rig, 1))  {xmimpflag = 18d;}
		else if  (gte(yearam, 1751) && lt(yearam, 1776) && eq(rig, 1))  {xmimpflag = 98d;}
		else if  (lt(yearam, 1751) && eq(rig, 1)) {xmimpflag = 17d;}		
		else if  (gte(yearam, 1851) && lt(yearam, 1876) && eq(natinimp, 9))  {xmimpflag = 100d;}
		else if  (lt(yearam, 1851) && eq(natinimp, 9) )  {xmimpflag = 99d;}
		else if  (lt(yearam, 1794) && eq(natinimp, 15)) {xmimpflag = 159d;}
		else if  (gte(yearam, 1794) && lt(yearam, 1807) && eq(natinimp, 15))  {xmimpflag = 157d;}
		else if  (gte(yearam, 1642) && lt(yearam, 1663) && ((gte(mjselimp, 31100) && lt(mjselimp, 32000)) ||
				    eq(mjselimp1, 40000) || eq(mjselimp, 80400)))  {xmimpflag = 16d;}		
		else if  (gt(yearam, 1730) && eq(natinimp, 8) )  {xmimpflag = 10d;}
		else if  (gte(yearam, 1674) && lt(yearam, 1731) && eq(natinimp, 8))  {xmimpflag = 9d;}
		else if  (gte(yearam, 1650) && lt(yearam, 1674) && eq(natinimp, 8))  {xmimpflag = 8d;}
		else if  (lt(yearam, 1650) && eq(natinimp, 8) )  {xmimpflag = 7d;}
		else if  (gt(yearam, 1825) && eq(mjselimp, 50200))  {xmimpflag = 15d;}
		else if  (gte(yearam, 1801) && lt(yearam, 1826) && eq(mjselimp, 50200))  {xmimpflag = 14d;}
		else if  (gte(yearam, 1776) && lt(yearam, 1801) && eq(mjselimp, 50200))  {xmimpflag = 13d;}
		else if  (gte(yearam, 1751) && lt(yearam, 1776) && eq(mjselimp, 50200))  {xmimpflag = 12d;}
		else if  (lt(yearam, 1751) && eq(mjselimp, 50200))  {xmimpflag = 11d;}
		else if  (gt(yearam, 1799) && eq(mjselimp, 50300))  {xmimpflag = 6d;}	
		else if  (gte(yearam, 1700) && lt(yearam, 1800) && eq(mjselimp, 50300))  {xmimpflag = 5d;}
		else if  (lt(yearam, 1701) && eq(mjselimp, 50300))  {xmimpflag = 4d;}
		else if  (lt(yearam, 1716) && gte(mjselimp, 36100) && lt(mjselimp, 37000)) {xmimpflag = 3d;}
		else if  ((gte(yearam, 1626) && lt(yearam, 1642)) && ((gte(mjselimp, 31100) && lt(mjselimp, 32000)) ||  eq(mjselimp1, 40000) || eq(mjselimp, 80400)))   {xmimpflag = 2d;}
		else if  (lt(yearam, 1627)) {xmimpflag = 1d;}	
		else if  (gte(yearam, 1800) && eq(majbyimp, 60800)) {xmimpflag = 124d;}
		else if  (gte(yearam, 1700) && lt(yearam, 1801) && eq(majbyimp, 60800)) {xmimpflag = 123d;}
		else if  (lt(yearam, 1700) && eq(majbyimp, 60800)) {xmimpflag = 122d;}
		else if  (gte(yearam, 1800) && eq(majbyimp, 60300)) {xmimpflag = 121d;}
		else if  (gte(yearam, 1700) && lt(yearam, 1801) && eq(majbyimp, 60300)) {xmimpflag = 120d;}
		else if  (gte(yearam, 1800) && eq(majbyimp, 60700)) {xmimpflag = 118d;}
		else if  (gte(yearam, 1700) && lt(yearam, 1801) && eq(majbyimp, 60700)) {xmimpflag = 117d;}
		else if  (lt(yearam, 1700) && eq(majbyimp, 60700)) {xmimpflag = 116d;}
		else if  (gte(yearam, 1800) && eq(majbyimp, 60600)) {xmimpflag = 115d;}
		else if  (gte(yearam, 1700) && lt(yearam, 1801) && eq(majbyimp, 60600)) {xmimpflag = 114d;}
		else if  (lt(yearam, 1700) && eq(majbyimp, 60600)) {xmimpflag = 113d;}
		else if  (gte(yearam, 1800) && eq(majbyimp, 60500)) {xmimpflag = 112d;}
		else if  (gte(yearam, 1700) && lt(yearam, 1801) && eq(majbyimp, 60500)) {xmimpflag = 111d;}
		else if  (lt(yearam, 1700) && eq(majbyimp, 60500)) {xmimpflag = 110d;}
		else if  (gte(yearam, 1700) && lt(yearam, 1801) && eq(majbyimp, 60400)){xmimpflag = 108d ;}
		else if  (lt(yearam, 1700) && eq(majbyimp, 60400)) {xmimpflag = 107d;}
		else if  (gte(yearam, 1800) && eq(majbyimp, 60200)) {xmimpflag = 106d;}		
		else if  (gte(yearam, 1700) && lt(yearam, 1801) && eq(majbyimp, 60200)) {xmimpflag = 105d;}
		else if  (lt(yearam, 1700) && eq(majbyimp, 60200)) {xmimpflag = 104d;}
		else if  (gte(yearam, 1800) && eq(majbyimp, 60100)) {xmimpflag = 103d;}
		else if  (gte(yearam, 1700) && lt(yearam, 1801) && eq(majbyimp, 60100)) {xmimpflag = 102d;}
		else if  (lt(yearam, 1700) && eq(majbyimp, 60100)) {xmimpflag = 101d;}
		else if  ((rig == null || (Arrays.binarySearch(rigArray, rig)) >= 0) && (gte(yearam, 1851) && lt(yearam, 1876))) {xmimpflag = 136d;}
		else if  ((rig == null || (Arrays.binarySearch(rigArray, rig)) >= 0) && (gte(yearam, 1826) && lt(yearam, 1851))) {xmimpflag = 135d;}
		else if  ((rig == null || (Arrays.binarySearch(rigArray, rig)) >= 0) && (gte(yearam, 1801) && lt(yearam, 1826))) {xmimpflag = 134d;}		
		else if  ((rig == null || (Arrays.binarySearch(rigArray, rig)) >= 0) && (gte(yearam, 1776) && lt(yearam, 1801))) {xmimpflag = 133d;}
		else if  ((rig == null || (Arrays.binarySearch(rigArray, rig)) >= 0) && (gte(yearam, 1751) && lt(yearam, 1776))) {xmimpflag = 132d;}
		else if  ((rig == null || (Arrays.binarySearch(rigArray, rig)) >= 0) && (gte(yearam, 1726) && lt(yearam, 1751))) {xmimpflag = 131d;}
		else if  ((rig == null || (Arrays.binarySearch(rigArray, rig)) >= 0) && (gte(yearam, 1701) && lt(yearam, 1726))) {xmimpflag = 130d;}
		else if  ((rig == null || (Arrays.binarySearch(rigArray, rig)) >= 0) && (gte(yearam, 1676) && lt(yearam, 1701))) {xmimpflag = 129d ;}
		else if  ((rig == null || (Arrays.binarySearch(rigArray, rig)) >= 0) && (gte(yearam, 1651) && lt(yearam, 1676))) {xmimpflag = 128d ;}
		else if  ((rig == null || (Arrays.binarySearch(rigArray, rig)) >= 0) && (gte(yearam, 1626) && lt(yearam, 1651))) {xmimpflag = 127d ;}		
		else if  ((rig == null || (Arrays.binarySearch(rigArray, rig)) >= 0) && (gte(yearam, 1626) && lt(yearam, 1651))) {xmimpflag = 127d ;}			
		
            //Store in voyage
			voyage.setXmimpflag(round(xmimpflag, 6));
			//System.out.println("xmimpflag:" + xmimpflag);

	}
	

	
	/*
	 * Calculate mjslptimp - Imputed principal place of landing
	*/
	public void calculateMjslptimp() {
		Port mjslptimp = null;
		Integer slas32 = voyage.getSlas32();
		Integer slas36 = voyage.getSlas36();
		Integer slas39 = voyage.getSlas39();
		Integer slaarriv = voyage.getSlaarriv();
		
		Port sla1port = voyage.getSla1port();
		Port adpsale1 = voyage.getAdpsale1();
		Port adpsale2 = voyage.getAdpsale2();
		Integer sla1port_int = (sla1port==null ? null : sla1port.getId().intValue());
		Integer adpsale1_int = (adpsale1==null ? null : adpsale1.getId().intValue()); 
		Integer adpsale2_int = (adpsale2==null ? null : adpsale2.getId().intValue()); 
		Integer regdis1 = (voyage.getRegdis1()==null ? null : voyage.getRegdis1().getId().intValue());
		Integer regdis2 = (voyage.getRegdis2()==null ? null : voyage.getRegdis2().getId().intValue());
		Integer regdis3 = (voyage.getRegdis3()==null ? null : voyage.getRegdis3().getId().intValue());
		Port arrport = voyage.getArrport();
		Integer arrport_int = (arrport==null ? null : arrport.getId().intValue());
		Port arrport2 = voyage.getArrport2();
		Integer arrport2_int = (arrport2==null ? null : arrport2.getId().intValue());
		Integer fate2 = (voyage.getFate2()==null ? null : voyage.getFate2().getId().intValue());
		
		Integer slastot = null;
		Double pctdis = null;
		
        //Begin Calc		
		if( gte(slas32, 1) || gte(slas36, 1) || gte(slas39, 1) )
		{
			slas32=defVal(slas32,0); 
			slas36=defVal(slas36,0); 
			slas39=defVal(slas39,0);
		}
		slastot = add(new Integer[]{slas32,slas36,slas39});

		pctdis = div(slastot, slaarriv);

		if (gte(sla1port_int, 1) && adpsale1_int==null && adpsale2_int==null) {mjslptimp = sla1port; }
		else if (gte(adpsale1_int, 1) && sla1port_int==null && adpsale2_int==null) {mjslptimp = adpsale1;}
		else if (gte(adpsale2_int, 1) && sla1port_int==null && adpsale1_int==null) {mjslptimp = adpsale2;}

		//reversed order
		if (eq(adpsale1_int, adpsale2_int)) {mjslptimp=adpsale1;}
		else if (eq(sla1port_int, adpsale2_int)) {mjslptimp=sla1port;}
		else if (eq(sla1port_int, adpsale1_int)) {mjslptimp=sla1port;}
		
		


	    if ( gt(slas32, slas36) && gt(slas32, slas39)) {mjslptimp = sla1port;}
	    else if (gt(slas36, slas32) && gt(slas36, slas39)) {mjslptimp = adpsale1;}
	    else if (gt(slas39, slas32) && gt(slas39, slas36)) {mjslptimp = adpsale2;}
	    
	    
	    if( (lt(pctdis, 0.5) || (lt(slastot, 50) && slaarriv==null)) && gte(sla1port_int, 1) && gte(adpsale1_int, 1) && adpsale2==null)
	    {
	    	if(eq(slas32, 0) && gte(slas36, 1)) {mjslptimp = sla1port;}
		    if(eq(slas36, 0) && gte(slas32, 1)) {mjslptimp = adpsale1;}
		    if(gte(slas36, 1) && gte(slas32, 1) && eq(regdis1, regdis2)) {mjslptimp = Port.loadById(session, regdis1+99);}
		    if(gte(slas36, 1) && gte(slas32, 1) && ne(regdis1, regdis2)) {mjslptimp = Port.loadById(session, 99801);}
	    }	
	    
	    
	    if
	    
	    ( (lt(pctdis, 0.5) || (lt(slastot, 50) && slaarriv==null)) && gte(sla1port_int, 1) && gte(adpsale1_int, 1) && gte(adpsale2_int, 1) ) {mjslptimp = Port.loadById(session, 99801);}




		
	    if(slastot==null) //may reverse later
	    {	
	    	if (gte(sla1port_int, 1) && gte(adpsale1_int, 1) && adpsale2_int==null && eq(regdis1, regdis2)) {mjslptimp = Port.loadById(session, regdis1+99);}
	    	if (gte(sla1port_int, 1) && gte(adpsale2_int, 1) && adpsale1_int==null && eq(regdis1, regdis3)) {mjslptimp = Port.loadById(session, regdis1+99);}
	    	if (gte(adpsale1_int, 1) && gte(adpsale2_int, 1) && sla1port_int==null && eq(regdis2, regdis3)) {mjslptimp = Port.loadById(session, regdis2+99);}
	    	if (gte(sla1port_int, 1) && gte(adpsale1_int, 1) && adpsale2_int==null && ne(regdis1, regdis2)) {mjslptimp = Port.loadById(session, 99801);}
	    	if (gte(sla1port_int, 1) && gte(adpsale2_int, 1) && adpsale1_int==null && ne(regdis1, regdis3)) {mjslptimp = Port.loadById(session, 99801);}
	    	if (gte(adpsale1_int, 1) && gte(adpsale2_int, 1) && sla1port_int==null && ne(regdis2, regdis3)) {mjslptimp = Port.loadById(session, 99801);}
	    	if (gte(sla1port_int, 1) && gte(adpsale1_int, 1) && gte(adpsale2_int, 1) && eq(regdis1, regdis2)) {mjslptimp = Port.loadById(session, regdis1 + 99);}
	    	if (gte(sla1port_int, 1) && gte(adpsale1_int, 1) && gte(adpsale2_int, 1) && eq(regdis1, regdis3)) {mjslptimp = Port.loadById(session, regdis1 + 99);}
	    	if (gte(sla1port_int, 1) && gte(adpsale1_int, 1) && gte(adpsale2_int, 1) && eq(regdis2, regdis3)) {mjslptimp = Port.loadById(session, regdis2 + 99);}
	    	if (gte(sla1port_int, 1) && gte(adpsale1_int, 1) && gte(adpsale2_int, 1) && ne(regdis1, regdis2) && ne(regdis1, regdis3) && ne(regdis2, regdis3)) {mjslptimp = Port.loadById(session, 99801);}
	    }
	    



	    if (gte(arrport_int, 1) && sla1port==null && adpsale1==null && adpsale2==null) {mjslptimp = arrport;}
        
	    if(
	    		mjslptimp==null && 
	    		(
	    				eq(fate2,1) || 
	    				eq(fate2, 3) || 
	    				eq(fate2, 5)
	    		) && 
	    		(
	    				gte(arrport_int, 1) || 
	    				gte(arrport2_int, 1) || 
	    				gte(sla1port_int, 1) || 
	    				gte(adpsale1_int, 1) || 
	    				gte(adpsale2_int, 1) || 
	    				gt(slastot, 0)
	    		)
	    ) 
	    {
	    	mjslptimp = Port.loadById(session, 99801);
	    }

		


		//Store in voyage
		 voyage.setMjslptimp(mjslptimp);
	    
	}
	
	/*
	 * yeardep - Imputed year in which voyage began
	 * yearaf - Year departed Africa (imputed
	 * yearam - Year of arrival at port of disembarkation (imputed)
	 */
	public void calculateYearVariables() {
		//Get Inputs
		Integer datedepc=voyage.getDatedepc();
		Integer d1slatrc=voyage.getD1slatrc();
		Integer dlslatrc=voyage.getDlslatrc();
		Integer datarr34=voyage.getDatarr34();
		Integer ddepamc=voyage.getDdepamc(); 
		Integer datarr45=voyage.getDatarr45();
		
		Integer yearam=null;		
		yearam = datarr34; 
		if (yearam == null) {yearam = dlslatrc;}
		if (yearam == null) {yearam = d1slatrc;}
		if (yearam == null) {yearam = datedepc;}
		if (yearam == null) {yearam = ddepamc;} 
		if (yearam == null) {yearam = datedepc;}
		if (yearam == null) {yearam = datarr45;}
		voyage.setYearam(yearam);
		
		Integer yearaf = null;		
		yearaf = dlslatrc; 
		if (yearaf == null) yearaf = d1slatrc;
		if (yearaf == null) yearaf = datedepc;
		if (yearaf == null) yearaf = datarr34;
		if (yearaf == null) yearaf = ddepamc;
		if (yearaf == null) yearaf = datarr45;
		voyage.setYearaf(yearaf);
		
		Integer yeardep = null;
		yeardep = datedepc;
		if (yeardep == null) yeardep = d1slatrc;
		if (yeardep == null) yeardep = dlslatrc;
		if (yeardep == null) yeardep = datarr34;
		if (yeardep == null) yeardep = ddepamc;
		if (yeardep == null) yeardep = datarr45;
		voyage.setYeardep(yeardep);				
	}
	
	/* impute variables datedep, datebuy, dateleftAfr, dateland1,
	 * dateland2, dateland3, dateend, datedepam
	 */
	public void calculateDateVariables() {
		Integer day, month, year = null;
		
		day = voyage.getDatedepa();
		month = voyage.getDatedepb();
		year = voyage.getDatedepc();
		Date dt = getValidDate(day, month, year);
		voyage.setDatedep(dt);
		
		
		day = voyage.getD1slatra();
		month = voyage.getD1slatrb();
		year = voyage.getD1slatrc();
		dt = getValidDate(day, month, year);
		voyage.setDatebuy(dt);
	
		day = voyage.getDlslatra();
		month = voyage.getDlslatrb();
		year = voyage.getDlslatrc();
		dt = getValidDate(day, month, year);
		voyage.setDateleftafr(dt);
	
		day = voyage.getDatarr32();
		month = voyage.getDatarr33();
		year = voyage.getDatarr34();
		dt = getValidDate(day, month, year);		
		voyage.setDateland1(dt);
		
		day = voyage.getDatarr36();
		month = voyage.getDatarr37();
		year = voyage.getDatarr38();
		dt = getValidDate(day, month, year);
		voyage.setDateland2(dt);
		
		day = voyage.getDatarr39();
		month = voyage.getDatarr40();
		year = voyage.getDatarr41();
		dt = getValidDate(day, month, year);
		voyage.setDateland3(dt);
				
		day = voyage.getDatarr43();
		month = voyage.getDatarr44();
		year = voyage.getDatarr45();
		dt = getValidDate(day, month, year);
		voyage.setDateend(dt);
		
		day = voyage.getDdepam();
		month = voyage.getDdepamb();
		year = voyage.getDdepamc();
		dt = getValidDate(day, month, year);
		voyage.setDatedepam(dt);				
	}
	
	public Date getValidDate(Integer day, Integer month, Integer year) {
		Date dt = null;
		Calendar cal = Calendar.getInstance();		
		if (year != null && month != null && day != null && year> 0 && month > 0 && day > 0){
			cal.clear();
			cal.set(year, --month, day);
			dt = cal.getTime();
		}
		return dt;
	}
	
	
	//Methods to make Java act like SPSS may want to move to another class
	
	//Add
	public Integer add(Integer[] input)
	{
		Integer ret=0;
				
		for(int i=0; i < input.length; i++)
		{
			if(input[i] != null)
			{
				ret+= input[i];
			}
			else
			{
				ret=null;
				break;
			}
				
		}
		
		return ret;
	}
	
	public Double add(Double[] input)
	{
		Double ret=0d;
				
		for(int i=0; i < input.length; i++)
		{
			if(input[i] != null)
			{
				ret+= input[i];
			}
			else
			{
				ret=null;
				break;
			}
				
		}
		
		return ret;
	}
	
	public Double addD(Integer[] input)
	{
		Double ret=0d;
				
		for(int i=0; i < input.length; i++)
		{
			if(input[i] != null)
			{
				ret+= input[i];
			}
			else
			{
				ret=null;
				break;
			}
				
		}
		
		return ret;
	}
	
	
	
	//Subtract
	public Integer sub(Integer l, Integer r)
	{
		Integer ret=null;
		
		if(l==null || r==null)
		{
			ret=null;
		}
		else
		{
			ret=l-r;
		}

		return ret;
	}
	
	public Double sub(Double l, Double r)
	{
		Double ret=null;
		
		if(l==null || r==null)
		{
			ret=null;
		}
		else
		{
			ret=l-r;
		}

		return ret;
	}
	
	
	//Multiply
	public Integer mult(Integer l, Integer r)
	{
		Integer ret=0;
		
		if(l == null || r == null) {ret=null;}
		else {ret = l*r;}
		
		return ret;
	}
	
	public Double mult(Double l, Double r)
	{
		Double ret=0d;
		
		if(l == null || r == null) {ret=null;}
		else {ret = l*r;}
		
		return ret;
	}
	
	
	//Divide
	public Double div(Integer l, Integer r)
	{
		Double ret=0d;
		Double L=null;
		Double R=null;
		
		if(l == null || r == null  || r==0) {ret=null;}
		else 
		{
			L=l.doubleValue();
			R=r.doubleValue();
			
			ret = L/R;
		}
		
		return ret;
	}
	
	public Double div(Double l, Double r)
	{
		Double ret=0d;
				
		if(l == null || r == null || r==0) {ret=null;}
		else 
		{
			ret = l/r;
		}
		
		return ret;
	}
	
	public Float divF(Integer l, Integer r)
	{
		Float ret=0f;
				
		if(l == null || r == null || r==0) {ret=null;}
		else 
		{
			ret = l.floatValue()/r.floatValue();
		}
		
		return ret;
	}
	
	
	//Equal
	public boolean eq(Integer l, Integer r)
	{
				
		if(l==null || r==null) {return false;}
		else
		{
			return (l.equals(r));
		}
		
	}
	
	public boolean eq(Double l, Double r)
	{
				
		if(l==null || r==null) {return false;}
		else
		{
			return (l.equals(r));
		}
		
	}
	
	
	//Not Equal
	public boolean ne(Integer l, Integer r)
	{
				
		if(l==null || r==null) {return false;}
		else
		{
			return (!l.equals(r));
		}
		
	}
	
	public boolean ne(Double l, Double r)
	{
				
		if(l==null || r==null) {return false;}
		else
		{
			return (!l.equals(r));
		}
		
	}
	
	
	//Less Than
	public boolean lt(Integer l, Integer r)
	{
				
		if(l==null || r==null) {return false;}
		else
		{
			return (l<r);
		}
		
	}
	
	public boolean lt(Double l, Double r)
	{
				
		if(l==null || r==null) {return false;}
		else
		{
			return (l<r);
		}
		
	}
	
	public boolean lt(Float l, Integer r)
	{
				
		if(l==null || r==null) {return false;}
		else
		{
			return (l<r);
		}
		
	}
	
	//Less Than Or Equal
	public boolean lte(Integer l, Integer r)
	{
				
		if(l==null || r==null) {return false;}
		else
		{
			return (l<=r);
		}
		
	}
	
	public boolean lte(Double l, Double r)
	{
				
		if(l==null || r==null) {return false;}
		else
		{
			return (l<=r);
		}
		
	}
	
	public boolean lte(Double l, Integer r)
	{
				
		if(l==null || r==null) {return false;}
		else
		{
			return (l<=r);
		}
		
	}
	
	
	//Greater Than
	public boolean gt(Integer l, Integer r)
	{
				
		if(l==null || r==null) {return false;}
		else
		{
			return (l>r);
		}
		
	}
	
	public boolean gt(Double l, Double r)
	{
				
		if(l==null || r==null) {return false;}
		else
		{
			return (l>r);
		}
		
	}
	
	public boolean gt(Float l, Integer r)
	{
				
		if(l==null || r==null) {return false;}
		else
		{
			return (l>r);
		}
		
	}
	
	public boolean gt(Integer l, Double r)
	{
				
		if(l==null || r==null) {return false;}
		else
		{
			return (l>r);
		}
		
	}
	
	
	
	//Greater Than Or Equal
	public boolean gte(Integer l, Integer r)
	{
				
		if(l==null || r==null) {return false;}
		else
		{
			return (l>=r);
		}
		
	}
	
	public boolean gte(Double l, Double r)
	{
				
		if(l==null || r==null) {return false;}
		else
		{
			return (l>=r);
		}
		
	}
	
	public boolean gte(Double l, Integer r)
	{
				
		if(l==null || r==null) {return false;}
		else
		{
			return (l>=r);
		}
		
	}
	
	public boolean gte(Long l, Integer r)
	{
				
		if(l==null || r==null) {return false;}
		else
		{
			return (l>=r);
		}
		
	}
	
	
}
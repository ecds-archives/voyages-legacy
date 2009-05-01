package edu.emory.library.tast.submission;

import java.util.HashMap;

import org.hibernate.Session;

import edu.emory.library.tast.dm.Fate;
import edu.emory.library.tast.dm.FateOwner;
import edu.emory.library.tast.dm.FateSlaves;
import edu.emory.library.tast.dm.FateVessel;
import edu.emory.library.tast.dm.Voyage;

public class VoyagesCalculation {
	
	Voyage voyage;
	Session session;
	
	public VoyagesCalculation(Voyage voyage, Session session) {
		this.voyage = voyage;
		this.session = session;
	}
	
	public Voyage calculateImputedVariables() { 
		calculateImputedValueFate2();
		calculateImputedValueFate3();
		calculateImputedValueFate4();
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
	
	public void calculateImputedValueTonmod() {
		Integer tontype=voyage.getTontype(); 
		Integer tonnage=voyage.getTonnage(); 
		Integer yearam=voyage.getYearam(); 
		Integer natinimp=voyage.getNatinimp().getId().intValue();
		Float tonmod=tonnage.floatValue();
		
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
	    
	    if(tonmod!=null)
	    {
	    	voyage.setTonmod(tonmod);
	    }
	}	
}

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
}

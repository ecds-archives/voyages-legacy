package edu.emory.library.tast.submissions;

import sun.security.jca.GetInstance;
import edu.emory.library.tast.dm.Voyage;

public class SubmissionAttributes {

	private static SubmissionAttributes instance = null;
	private SubmissionAttribute[] attributes;
	
	public synchronized static SubmissionAttributes getConfiguration() {
		if (instance == null) {
			instance = new SubmissionAttributes();
		}
		return instance;
	}
	
	private SubmissionAttributes() {
		attributes = new SubmissionAttribute[10];
		
		attributes[0] = new SubmissionAttribute(Voyage.getAttribute("shipname"), "Name of ship", null);
		attributes[1] = new SubmissionAttribute(Voyage.getAttribute("datedep"), "Departure date", null);
		attributes[2] = new SubmissionAttribute(Voyage.getAttribute("captaina"), "First captain", null);
		attributes[3] = new SubmissionAttribute(Voyage.getAttribute("captainb"), "Second captain", null);
		attributes[4] = new SubmissionAttribute(Voyage.getAttribute("portdep"), "Port of departure", null);
		attributes[5] = new SubmissionAttribute(Voyage.getAttribute("arrport"), "Port of arrival", null);
		attributes[6] = new SubmissionAttribute(Voyage.getAttribute("crew1"), "Crew 1", null);
		attributes[7] = new SubmissionAttribute(Voyage.getAttribute("crew3"), "Crew 2", null);
		attributes[8] = new SubmissionAttribute(Voyage.getAttribute("tonnage"), "Tinnage of vessel", null);
		attributes[9] = new SubmissionAttribute(Voyage.getAttribute("guns"), "Guns", null);
		
	}

	public SubmissionAttribute[] getSubmissionAttributes() {
		return attributes;
	}
	
	
}

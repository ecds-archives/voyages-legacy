package edu.emory.library.tast.submission;

import sun.security.jca.GetInstance;
import edu.emory.library.tast.common.grideditor.date.DateAdapter;
import edu.emory.library.tast.common.grideditor.textbox.TextboxAdapter;
import edu.emory.library.tast.common.grideditor.textbox.TextboxFloatAdapter;
import edu.emory.library.tast.common.grideditor.textbox.TextboxIntegerAdapter;
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
		
		attributes[0] = new SubmissionAttribute(Voyage.getAttribute("shipname"), "Name of ship", null, TextboxAdapter.TYPE);
		attributes[1] = new SubmissionAttribute(Voyage.getAttribute("datedep"), "Departure date", null, DateAdapter.TYPE);
		attributes[2] = new SubmissionAttribute(Voyage.getAttribute("captaina"), "First captain", null, TextboxAdapter.TYPE);
		attributes[3] = new SubmissionAttribute(Voyage.getAttribute("captainb"), "Second captain", null, TextboxAdapter.TYPE);
		attributes[4] = new SubmissionAttribute(Voyage.getAttribute("portdep"), "Port of departure", null, TextboxAdapter.TYPE);
		attributes[5] = new SubmissionAttribute(Voyage.getAttribute("arrport"), "Port of arrival", null, TextboxAdapter.TYPE);
		attributes[6] = new SubmissionAttribute(Voyage.getAttribute("crew1"), "Crew 1", null, TextboxIntegerAdapter.TYPE);
		attributes[7] = new SubmissionAttribute(Voyage.getAttribute("crew3"), "Crew 2", null, TextboxIntegerAdapter.TYPE);
		attributes[8] = new SubmissionAttribute(Voyage.getAttribute("tonnage"), "Tonnage of vessel", null, TextboxFloatAdapter.TYPE);
		attributes[9] = new SubmissionAttribute(Voyage.getAttribute("guns"), "Guns", null, TextboxIntegerAdapter.TYPE);
		
	}

	public SubmissionAttribute[] getSubmissionAttributes() {
		return attributes;
	}
	
	
}

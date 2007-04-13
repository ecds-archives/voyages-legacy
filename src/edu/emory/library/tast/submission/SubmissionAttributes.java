package edu.emory.library.tast.submission;

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
		
		attributes[0] = new SubmissionAttribute("shipname", Voyage.getAttribute("shipname"), "Name of ship", null, TextboxAdapter.TYPE);
		attributes[1] = new SubmissionAttribute("datedep", Voyage.getAttribute("datedep"), "Departure date", null, DateAdapter.TYPE);
		attributes[2] = new SubmissionAttribute("captaina", Voyage.getAttribute("captaina"), "First captain", null, TextboxAdapter.TYPE);
		attributes[3] = new SubmissionAttribute("captainb", Voyage.getAttribute("captainb"), "Second captain", null, TextboxAdapter.TYPE);
		attributes[4] = new SubmissionAttribute("portdep", Voyage.getAttribute("portdep"), "Port of departure", null, TextboxAdapter.TYPE);
		attributes[5] = new SubmissionAttribute("arrport", Voyage.getAttribute("arrport"), "Port of arrival", null, TextboxAdapter.TYPE);
		attributes[6] = new SubmissionAttribute("crew1", Voyage.getAttribute("crew1"), "Crew 1", null, TextboxIntegerAdapter.TYPE);
		attributes[7] = new SubmissionAttribute("crew3", Voyage.getAttribute("crew3"), "Crew 2", null, TextboxIntegerAdapter.TYPE);
		attributes[8] = new SubmissionAttribute("tonnage", Voyage.getAttribute("tonnage"), "Tonnage of vessel", null, TextboxFloatAdapter.TYPE);
		attributes[9] = new SubmissionAttribute("guns", Voyage.getAttribute("guns"), "Guns", null, TextboxIntegerAdapter.TYPE);
		
	}

	public SubmissionAttribute[] getSubmissionAttributes() {
		return attributes;
	}
	
	
}

package edu.emory.library.tast.dm;

import java.util.Map;

public class EditedVoyage {
	
	private Long id;
	private Voyage voyage;
	private Map attributeNotes;
	
	public EditedVoyage(Voyage voyage, Map notes) {
		this.voyage = voyage;
		this.attributeNotes = notes;
	}
	
	public Map getAttributeNotes() {
		return attributeNotes;
	}
	public void setAttributeNotes(Map attributeNotes) {
		this.attributeNotes = attributeNotes;
	}
	public Voyage getVoyage() {
		return voyage;
	}
	public void setVoyage(Voyage voyage) {
		this.voyage = voyage;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}

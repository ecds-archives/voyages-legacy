package edu.emory.library.tast.dm;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.Session;

import edu.emory.library.tast.db.TastDbConditions;
import edu.emory.library.tast.db.TastDbQuery;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.NumericAttribute;
import edu.emory.library.tast.dm.attributes.VoyageAttribute;

public class EditedVoyage {
	
	private Long id;
	private Voyage voyage;
	private Map attributeNotes;
	
	private static Map attributes = new HashMap();
	static {
		attributes.put("id", new NumericAttribute("id", null, NumericAttribute.TYPE_LONG));
		attributes.put("voyage", new VoyageAttribute("voyage", "EditedVoyage"));
	}
	public static Attribute getAttribute(String name) {
		return (Attribute)attributes.get(name);
	}
	
	
	public EditedVoyage() {
	}
	
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


	public static EditedVoyage loadById(Session session, Long id) {
		TastDbConditions c = new TastDbConditions();
		c.addCondition(getAttribute("id"), id, TastDbConditions.OP_EQUALS);
		TastDbQuery qValue = new TastDbQuery("EditedVoyage", c);
		Object[] ret = qValue.executeQuery(session);
		if (ret.length == 0) {
			return null;
		} else {
			return (EditedVoyage) ret[0];
		}
	}
}

package edu.emory.library.tas;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.emory.library.tas.attrGroups.Attribute;
import edu.emory.library.tas.util.query.Conditions;

public class VoyageIndex {
	
	private Long id;
	private Date revisionDate;
	private Integer flag;
	private Long voyageId;
	private Long revisionId;
	private Long remoteVoyageId;
	
	private Integer latest = null;
	private Integer latest_approved = null;
	
	private Voyage voyage;
	private Set slaves = new HashSet();

	private static Map attributes = new HashMap();
	static {		
		Attribute attr = new Attribute();
		attr.setName("revisionId");
		attr.setType(new Integer(Attribute.TYPE_INTEGER));
		attr.setUserLabel("Revision #");
		attributes.put("revisionId", attr);
		
		attr = new Attribute();
		attr.setName("revisionDate");
		attr.setType(new Integer(Attribute.TYPE_INTEGER));
		attr.setUserLabel("Modification date");
		attributes.put("revisionDate", attr);
		
		attr = new Attribute();
		attr.setName("flag");
		attr.setType(new Integer(Attribute.TYPE_INTEGER));
		attr.setUserLabel("Approved");
		attributes.put("flag", attr);
	}
	
	
	public static Attribute getAttribute(String name) {
		return (Attribute)attributes.get(name);
	}
	
	public VoyageIndex() {}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Set getSlaves() {
		return slaves;
	}

	public void setSlaves(Set slaves) {
		this.slaves = slaves;
	}

	public Voyage getVoyage() {
		return voyage;
	}

	public void setVoyage(Voyage voyage) {
		this.voyage = voyage;
	}

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	public Date getRevisionDate() {
		return revisionDate;
	}

	public void setRevisionDate(Date revisionDate) {
		this.revisionDate = revisionDate;
	}
	
	public String toString() {
		return "VoyageIndex: [" + voyage + "], [" + slaves + "]" + "(" + getVoyageId() + ", " + getRevisionId() + ")";
	}

	public Long getVoyageId() {
		return voyageId;
	}

	public void setVoyageId(Long voyageId) {
		this.voyageId = voyageId;
	}

	public Long getRevisionId() {
		return revisionId;
	}

	public void setRevisionId(Long revisionId) {
		this.revisionId = revisionId;
	}

	public Integer getLatest() {
		return latest;
	}

	public void setLatest(Integer latest) {
		this.latest = latest;
	}

	public Integer getLatest_approved() {
		return latest_approved;
	}

	public void setLatest_approved(Integer latest_approved) {
		this.latest_approved = latest_approved;
	}
	
	public static Conditions getRecentApproved() {
		Conditions cond = new Conditions(Conditions.JOIN_AND);
		cond.addCondition("latest_approved", new Integer(1), Conditions.OP_EQUALS);
		return cond;
	}
	
	public static Conditions getRecent() {
		Conditions cond = new Conditions(Conditions.JOIN_AND);
		cond.addCondition("latest", new Integer(1), Conditions.OP_EQUALS);
		return cond;
	}
	
	public static Conditions getApproved() {
		Conditions cond = new Conditions(Conditions.JOIN_AND);
		cond.addCondition("flag", new Integer(1), Conditions.OP_EQUALS);
		return cond;
	}
	
	public static Conditions getNotApproved() {
		Conditions cond = new Conditions(Conditions.JOIN_AND);
		cond.addCondition("flag", new Integer(0), Conditions.OP_EQUALS);
		return cond;
	}

	public Long getRemoteVoyageId() {
		return remoteVoyageId;
	}

	public void setRemoteVoyageId(Long remoteVoyageId) {
		this.remoteVoyageId = remoteVoyageId;
	}
	
}

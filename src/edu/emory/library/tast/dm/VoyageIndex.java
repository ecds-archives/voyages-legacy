package edu.emory.library.tast.dm;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.ui.search.tabscommon.VisibleAttribute;
import edu.emory.library.tast.util.query.Conditions;

/**
 * Object that helps to keep track of changes in Voyages.
 * It has modification date and revision number as well as set of slaves and
 * voyage attribute.
 * @author Pawel Jurczyk
 *
 */
public class VoyageIndex {
	
	/**
	 * ID of object.
	 */
	private Long id;
	
	/**
	 * Date of revision.
	 */
	private Date revisionDate;
	
	/**
	 * Flag - APPROVED/NOT APPROVED.
	 */
	private Integer flag;
	
	/**
	 * ID of corresponding voyage.
	 */
	private Long voyageId;
	
	/**
	 * Revision number.
	 */
	private Long revisionId;
	
	/**
	 * Internal ID of Voyage.
	 */
	private Long remoteVoyageId;
	
	/**
	 * Indicates if this is the lates revision.
	 * if latest, then 1, otherwise 0
	 */
	private Integer latest = null;
	
	/**
	 * Indicates if this is the lates approved revision.
	 * if latest, then 1, otherwise 0
	 */
	private Integer latest_approved = null;
	
	/**
	 * Voyage object.
	 */
	private Voyage voyage;
	
	/**
	 * Slaves list.
	 */
	private Set slaves = new HashSet();

	/**
	 * Attributes that can be shown on UI.
	 */
	private static Map attributes = new HashMap();
	static {	
		Attribute attr = new Attribute();
		attr.setName("revisionId");
		VisibleAttribute visibleAttr = new VisibleAttribute("revisionId", new int [] {1, 2}, new Attribute[] {attr});
		visibleAttr.setUserLabel("Revision #");
		attributes.put("revisionId", visibleAttr);
		
		attr = new Attribute();
		attr.setName("revisionDate");
		visibleAttr = new VisibleAttribute("revisionDate", new int [] {1, 2}, new Attribute[] {attr});
		visibleAttr.setUserLabel("Modification date");
		attributes.put("revisionDate", visibleAttr);
		
		attr = new Attribute();
		attr.setName("flag");
		visibleAttr = new VisibleAttribute("flag", new int [] {1, 2}, new Attribute[] {attr});
		visibleAttr.setUserLabel("Approved");
		attributes.put("flag", visibleAttr);
	}
	
	/**
	 * Gets attribute of object with given name.
	 * @param name
	 * @return
	 */
	public static VisibleAttribute getVisibleAttribute(String name) {
		return (VisibleAttribute)attributes.get(name);
	}
	
	/**
	 * Default constructor.
	 *
	 */
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

	/**
	 * Gets condition for voyage index that is recent approved object.
	 * @return
	 */
	public static Conditions getRecentApproved() {
		Conditions cond = new Conditions(Conditions.JOIN_AND);
		cond.addCondition("latest_approved", new Integer(1), Conditions.OP_EQUALS);
		return cond;
	}
	
	/**
	 * Gets condition for voyage index that is recent object (not necessary approved).
	 * @return
	 */
	public static Conditions getRecent() {
		Conditions cond = new Conditions(Conditions.JOIN_AND);
		cond.addCondition("latest", new Integer(1), Conditions.OP_EQUALS);
		return cond;
	}
	
	/**
	 * Gets condition for voyage index that is approved object (not necessary recent).
	 * @return
	 */
	public static Conditions getApproved() {
		Conditions cond = new Conditions(Conditions.JOIN_AND);
		cond.addCondition("flag", new Integer(1), Conditions.OP_EQUALS);
		return cond;
	}
	
	/**
	 * Gets condition for voyage index that is not approved object (not necessary recent).
	 * @return
	 */
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

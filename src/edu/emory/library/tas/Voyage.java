package edu.emory.library.tas;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import edu.emory.library.tas.util.HibernateConnector;

public class Voyage extends AbstractDescriptiveObject {
	
	/**
	 * Modified flag.
	 */
	public static final int UPDATED = 1;
	
	/**
	 * Unmodified flag.
	 */
	public static final int NOT_UPDATED = 0;
	
	/**
	 * Approved flag.
	 */
	private boolean approved = false;
	
	/**
	 * Field informing whether object was modified/unmodified.
	 */
	private int modified = Voyage.NOT_UPDATED;
	
	/**
	 * Field informing whether object's slaves were modified/unmodified.
	 */
	private int slavesModified = NOT_UPDATED;
	
	/**
	 * Creation flag. If true - object should be created in DB.
	 */
	private boolean created = false;
	
	/**
	 * Set of slaves.
	 */
	private Set slaves = new HashSet();
	
	/*AUTOGENERATED BLOCK*/
	static {
		/** Static construction **/
		types.put("voyageId", "Long");
		types.put("shipname", "String");
		types.put("iid", "Long");
		types.put("revisionId", "Long");
		types.put("majselpt", "Integer");
		types.put("majbuypt", "Integer");
		types.put("yearaf", "Integer");
		types.put("tslmtimp", "Integer");
		types.put("vymrtimp", "Integer");
		userLabels.put("voyageId", "Voyage ID");
		userLabels.put("shipname", "Ship name");
		userLabels.put("iid", "");
		userLabels.put("revisionId", "");
		userLabels.put("majselpt", "majselpt");
		userLabels.put("majbuypt", "majbuypt");
		userLabels.put("yearaf", "yearaf");
		userLabels.put("tslmtimp", "tslmtimp");
		userLabels.put("vymrtimp", "vymrtimp");
	}
	/*END AUTOGENERATED*/
	
	
	/**
	 * Creates new Voyage. Object will have new ID.
	 */
	public Voyage() {
		this.created = true;
	}
	
	/**
	 * Creates new Voyage.
	 * @param p_created
	 */
	private Voyage(boolean p_created) {
		this.created = p_created;
	}
	
	/**
	 * Creates voyage with given ID.
	 * @param voyageId desired voyage ID.
	 * @return	Created Voyage object
	 */
	public static Voyage createNew(Long voyageId) {
		Voyage voyage = new Voyage(true);
		voyage.setVoyageId(voyageId);
		return voyage;
	}

	/**
	 * 
	 * Loads voyage
	 * @param voyage voyage providing voyage ID
	 * @param option option
	 * @return loaded voyage
	 */
	private static Voyage loadInternal(Voyage voyage, int option) {
		Voyage localVoyage = null;
		
		//Load voyage from DB
		VoyageIndex[] voyageIndex = HibernateConnector.getConnector()
			.getVoyageIndexByVoyage(voyage, option);
		
		//Prepare result
		if (voyageIndex.length != 0) {
			localVoyage = voyageIndex[0].getVoyage();
			localVoyage.setSlaves(voyageIndex[0].getSlaves());
			localVoyage.setRevisionId(voyageIndex[0].getRevisionId());
		}
		
		return localVoyage;
	}
	
	/**
	 * Loads Active (most recent active) voyage with given ID.
	 * @param voyageId	voyuage ID
	 * @return voyage object, null if there is no desired Voyage in DB
	 */
	public static Voyage loadActive(Long voyageId) {
		Voyage localVoyage = new Voyage();
		localVoyage.setVoyageId(voyageId);
		return loadInternal(localVoyage, 
				HibernateConnector.APPROVED & HibernateConnector.WITHOUT_HISTORY);
	}

	/**
	 * Loads most recent (not necessary active) voyage with given ID.
	 * @param voyageId	voyuage ID
	 * @return voyage object, null if there is no desired Voyage in DB
	 */
	public static Voyage loadMostRecent(Long voyageId) {
		Voyage localVoyage = new Voyage();
		localVoyage.setVoyageId(voyageId);
		return loadInternal(localVoyage, 
				HibernateConnector.APPROVED_AND_NOT_APPROVED & HibernateConnector.WITHOUT_HISTORY);
	}

	/**
	 * Loads voyage with given ID and given revision ID.
	 * @param voyageId	voyuage ID
	 * @return voyage object, null if there is no desired Voyage in DB
	 */
	public static Voyage loadByRevision(Long voyageId, Long revisionId) {
		Voyage localVoyage = new Voyage();
		localVoyage.setVoyageId(voyageId);
		localVoyage.setRevisionId(revisionId);
		return loadInternal(localVoyage, 
				HibernateConnector.WITHOUT_HISTORY);
	}

	/**
	 * List all revis
	 * @param voyageId
	 * @param approved
	 * @return
	 */
	public static List loadAllRevisions(Long voyageId, int p_option) {
		int option = p_option & HibernateConnector.WITH_HISTORY;
		Voyage localVoyage = new Voyage();
		localVoyage.setVoyageId(voyageId);
		
		//Load info from DB
		VoyageIndex[] voyageIndex = HibernateConnector.getConnector()
			.getVoyageIndexByVoyage(localVoyage, option);
		List list = new ArrayList();
		//Prepare result
		for (int i = 0; i < voyageIndex.length; i++) {
			Voyage v = voyageIndex[i].getVoyage();
			v.setSlaves(voyageIndex[i].getSlaves());
			v.setRevisionId(voyageIndex[i].getRevisionId());
			list.add(v);
		}
		//Return result
		return list;
	}
	
	/**
	 * Saves voyage to DB.
	 *
	 */
	public void save() {
		//Prepare VoyageIndex
		VoyageIndex vIndex = new VoyageIndex();
		vIndex.setVoyage(this);
		vIndex.setVoyageId(this.getVoyageId());
		vIndex.setSlaves(this.getSlaves());
		vIndex.setRevisionDate(new Date(System.currentTimeMillis()));
		vIndex.setFlag(approved ? new Integer(1): new Integer(0));
		
		//Save to DB (or update...)
		if (this.created) {
			HibernateConnector.getConnector().createVoyage(vIndex);
		} else {
			HibernateConnector.getConnector().updateVoyage(vIndex);
		}
	}

	/**
	 * Gets deep copy of object.
	 */
	public Object clone() {
		//Copy voyage itself
		Voyage newVoyage = new Voyage();
		newVoyage.setShipname(getShipname());
		newVoyage.setVoyageId(getVoyageId());
		newVoyage.setRevisionId(getRevisionId());
		
		//Copy slaves
		Iterator iter = getSlaves().iterator();
		HashSet set = new HashSet();
		while (iter.hasNext()) {
			set.add(((Slave)iter.next()).clone());
		}
		newVoyage.setSlaves(set);
		newVoyage.setModified(getModified());
		
		//Return copy object
		return newVoyage;
	}
	
	/**
	 * Gets set of slaves.
	 * @return Set with Slave objects
	 */
	public Set getSlaves() {
		return this.slaves;
	}
	
	/**
	 * Sets set of slaves.
	 * @param slaves Set with Slave objects
	 */
	public void setSlaves(Set slaves) {
		this.slaves = slaves;
	}
	
	/**
	 * Removes slave from list.
	 * @param slave Slave to remove
	 */
	public void removeSlave(Slave slave) {
		this.slavesModified = UPDATED;
		this.slaves.remove(slave);
	}
	
	/**
	 * Adds slave to set of slaves.
	 * @param slave Slave to add
	 */
	public void addSlave(Slave slave) {
		this.slavesModified = UPDATED;
		this.slaves.add(slave);
	}
	
	/**
	 * Gets Slave with given slaveId.
	 * @param slaveId Slave id
	 * @return Slave object, null if voyage does not have such a slave
	 */
	public Slave getSlave(Long slaveId) {
		Iterator iter = this.slaves.iterator();
		while (iter.hasNext()) {
			Slave slave = (Slave)iter.next();
			if (slave.getSlaveId().equals(slaveId)) {
				return slave;
			}
		}
		return null;
	}
	
	/**
	 * Sets modified flag.
	 * @param p_modified
	 */
	public void setModified(int p_modified) {
		this.modified = p_modified;
	}
	
	/**
	 * Gets modified flag.
	 * @return
	 */
	public int getModified() {
		return this.modified;
	}
	
	public boolean wereSlavesModified() {
		return this.slavesModified == UPDATED;
	}
	
	/**
	 * Returns string representation of object.
	 */
	public String toString() {
		return "Voyage: " + getShipname() + " Slaves: " + getSlaves();
	}
	
	
	
	/*AUTOGENERATED BLOCK*/
	/** Getters/setters **/
	public void setVoyageId(Long voyageId) {
		if (!this.values.get("voyageId").equals(voyageId)) {
			this.modified = UPDATED;
		}
		this.values.put("voyageId", voyageId);
	}
	public void setShipname(String shipname) {
		if (!this.values.get("shipname").equals(shipname)) {
			this.modified = UPDATED;
		}
		this.values.put("shipname", shipname);
	}
	public void setIid(Long iid) {
		if (!this.values.get("iid").equals(iid)) {
			this.modified = UPDATED;
		}
		this.values.put("iid", iid);
	}
	public void setRevisionId(Long revisionId) {
		if (!this.values.get("revisionId").equals(revisionId)) {
			this.modified = UPDATED;
		}
		this.values.put("revisionId", revisionId);
	}
	public void setMajselpt(Integer majselpt) {
		if (!this.values.get("majselpt").equals(majselpt)) {
			this.modified = UPDATED;
		}
		this.values.put("majselpt", majselpt);
	}
	public void setMajbuypt(Integer majbuypt) {
		if (!this.values.get("majbuypt").equals(majbuypt)) {
			this.modified = UPDATED;
		}
		this.values.put("majbuypt", majbuypt);
	}
	public void setYearaf(Integer yearaf) {
		if (!this.values.get("yearaf").equals(yearaf)) {
			this.modified = UPDATED;
		}
		this.values.put("yearaf", yearaf);
	}
	public void setTslmtimp(Integer tslmtimp) {
		if (!this.values.get("tslmtimp").equals(tslmtimp)) {
			this.modified = UPDATED;
		}
		this.values.put("tslmtimp", tslmtimp);
	}
	public void setVymrtimp(Integer vymrtimp) {
		if (!this.values.get("vymrtimp").equals(vymrtimp)) {
			this.modified = UPDATED;
		}
		this.values.put("vymrtimp", vymrtimp);
	}
	public Long getVoyageId() {
		return (Long)this.values.get("voyageId");
	}
	public String getShipname() {
		return (String)this.values.get("shipname");
	}
	public Long getIid() {
		return (Long)this.values.get("iid");
	}
	public Long getRevisionId() {
		return (Long)this.values.get("revisionId");
	}
	public Integer getMajselpt() {
		return (Integer)this.values.get("majselpt");
	}
	public Integer getMajbuypt() {
		return (Integer)this.values.get("majbuypt");
	}
	public Integer getYearaf() {
		return (Integer)this.values.get("yearaf");
	}
	public Integer getTslmtimp() {
		return (Integer)this.values.get("tslmtimp");
	}
	public Integer getVymrtimp() {
		return (Integer)this.values.get("vymrtimp");
	}
	/*END AUTOGENERATED*/
}

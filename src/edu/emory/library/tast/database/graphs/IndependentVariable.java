package edu.emory.library.tast.database.graphs;

import java.text.Format;
import java.text.MessageFormat;

import edu.emory.library.tast.dm.Area;
import edu.emory.library.tast.dm.Fate;
import edu.emory.library.tast.dm.FateOwner;
import edu.emory.library.tast.dm.FateSlaves;
import edu.emory.library.tast.dm.FateVessel;
import edu.emory.library.tast.dm.Nation;
import edu.emory.library.tast.dm.Port;
import edu.emory.library.tast.dm.Region;
import edu.emory.library.tast.dm.VesselRig;
import edu.emory.library.tast.dm.Voyage;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.specific.DirectValueAttribute;
import edu.emory.library.tast.dm.attributes.specific.FunctionAttribute;
import edu.emory.library.tast.dm.attributes.specific.SequenceAttribute;

public class IndependentVariable
{
	
	private String id;
	private String label;
	private Format format;
	private Attribute selectAttribute; 
	private Attribute[] groupByAttributes; 
	private Attribute orderAttribute;
	
	private IndependentVariable(String id, String label, Attribute masterAttribute, Format format)
	{
		this.id = id;
		this.label = label;
		this.format = format;
		this.selectAttribute = masterAttribute;
		this.groupByAttributes = new Attribute[] {masterAttribute};
		this.orderAttribute = masterAttribute;
	}

	public IndependentVariable(String id, String label, Attribute selectAttribute, Attribute groupByAttribute, Attribute orderAttribute, Format format)
	{
		this.id = id;
		this.label = label;
		this.format = format;
		this.selectAttribute = selectAttribute;
		this.groupByAttributes = new Attribute[] {groupByAttribute};
		this.orderAttribute = orderAttribute;
	}
	
	public IndependentVariable(String id, String label, Attribute selectAttribute, Attribute[] groupByAttributes, Attribute orderAttribute, Format format)
	{
		this.id = id;
		this.label = label;
		this.format = format;
		this.selectAttribute = selectAttribute;
		this.groupByAttributes = groupByAttributes;
		this.orderAttribute = orderAttribute;
	}

	public static IndependentVariable createForString(String id, String label, String attrName)
	{
		return new IndependentVariable(
				id,
				label,
				Voyage.getAttribute(attrName),
				new MessageFormat("{0}"));
	}

	public static IndependentVariable createForInteger(String id, String label, String attrName)
	{
		return new IndependentVariable(
				id,
				label,
				Voyage.getAttribute(attrName),
				new MessageFormat("{0,number,#,###,###}"));
	}

	public static IndependentVariable createForPort(String id, String label, String name)
	{
		
		Attribute portAttr = Voyage.getAttribute(name);
		
		SequenceAttribute portNameAttr = new SequenceAttribute(new Attribute[] {
				portAttr,
				Port.getAttribute("name") });
		
		SequenceAttribute portIdAttr = new SequenceAttribute(new Attribute[] {
				portAttr,
				Port.getAttribute("id") });
		
		return new IndependentVariable(
				id,
				label,
				portNameAttr,
				new Attribute[] {portIdAttr, portNameAttr},
				portNameAttr,
				new MessageFormat("{0}"));
	}

	public static IndependentVariable createForRegion(String id, String label, String name)
	{
		
		Attribute portAttr = Voyage.getAttribute(name);
		
		SequenceAttribute regionNameAttr = new SequenceAttribute(new Attribute[] {
				portAttr,
				Port.getAttribute("region"),
				Region.getAttribute("name") });
		
		SequenceAttribute regionIdAttr = new SequenceAttribute(new Attribute[] {
				portAttr,
				Port.getAttribute("region"),
				Region.getAttribute("id") });
		
		return new IndependentVariable(
				id,
				label,
				regionNameAttr,
				new Attribute[] {regionIdAttr, regionNameAttr},
				regionNameAttr,
				new MessageFormat("{0}"));

	}
	
	public static IndependentVariable createForArea(String id, String label, String name)
	{
		
		Attribute portAttr = Voyage.getAttribute(name);
		
		SequenceAttribute areaNameAttr = new SequenceAttribute(new Attribute[]{
				portAttr,
				Port.getAttribute("region"),
				Region.getAttribute("area"),
				Area.getAttribute("name")});
		
		SequenceAttribute areaIdAttr = new SequenceAttribute(new Attribute[]{
				portAttr,
				Port.getAttribute("region"),
				Region.getAttribute("area"),
				Area.getAttribute("name")});
		
		return new IndependentVariable(
				id,
				label,
				areaNameAttr,
				new Attribute[] {areaIdAttr, areaNameAttr},
				areaNameAttr,
				new MessageFormat("{0}"));
		
	}

	public static IndependentVariable createForNation(String id, String label, String name)
	{
		Attribute nationAttr = Voyage.getAttribute(name);
		SequenceAttribute nationNameAttr = new SequenceAttribute(new Attribute[]{nationAttr, Nation.getAttribute("name")});
		SequenceAttribute nationIdAttr = new SequenceAttribute(new Attribute[]{nationAttr, Nation.getAttribute("id")});
		return new IndependentVariable(id, label, nationNameAttr, new Attribute[] {nationIdAttr, nationNameAttr}, nationNameAttr, new MessageFormat("{0}"));
	}

	public static IndependentVariable createForVesselRig(String id, String label, String name)
	{
		Attribute vesselRigAttr = Voyage.getAttribute(name);
		SequenceAttribute vesselRigNameAttr = new SequenceAttribute(new Attribute[]{vesselRigAttr, VesselRig.getAttribute("name")});
		SequenceAttribute vesselRigIdAttr = new SequenceAttribute(new Attribute[]{vesselRigAttr, VesselRig.getAttribute("name")});
		return new IndependentVariable(id, label, vesselRigNameAttr, new Attribute[] {vesselRigIdAttr, vesselRigNameAttr}, vesselRigNameAttr, new MessageFormat("{0}"));
	}

	public static IndependentVariable createForFate(String id, String label, String name)
	{
		Attribute fateAttr = Voyage.getAttribute(name);
		SequenceAttribute fateNameAttr = new SequenceAttribute(new Attribute[]{fateAttr, Fate.getAttribute("name")});
		SequenceAttribute fateIdAttr = new SequenceAttribute(new Attribute[]{fateAttr, Fate.getAttribute("name")});
		return new IndependentVariable(id, label, fateNameAttr, new Attribute[] {fateIdAttr, fateNameAttr}, fateNameAttr, new MessageFormat("{0}"));
	}

	public static IndependentVariable createForFateOwner(String id, String label, String name)
	{
		
		Attribute fateOwnerAttr = Voyage.getAttribute(name);
		
		SequenceAttribute fateOwnerNameAttr = new SequenceAttribute(new Attribute[]{
				fateOwnerAttr,
				FateOwner.getAttribute("name")});
		
		SequenceAttribute fateOwnerIdAttr = new SequenceAttribute(new Attribute[]{
				fateOwnerAttr,
				FateOwner.getAttribute("id")});
		
		return new IndependentVariable(
				id,
				label,
				fateOwnerNameAttr,
				new Attribute[] {fateOwnerIdAttr, fateOwnerNameAttr},
				fateOwnerNameAttr,
				new MessageFormat("{0}"));
	}

	public static IndependentVariable createForFateSlaves(String id, String label, String name)
	{
		Attribute fateSlavesAttr = Voyage.getAttribute(name);
		SequenceAttribute fateSlavesNameAttr = new SequenceAttribute(new Attribute[]{fateSlavesAttr, FateSlaves.getAttribute("name")});
		SequenceAttribute fateSlavesidAttr = new SequenceAttribute(new Attribute[]{fateSlavesAttr, FateSlaves.getAttribute("id")});
		return new IndependentVariable(id, label, fateSlavesNameAttr, new Attribute[] {fateSlavesidAttr, fateSlavesNameAttr}, fateSlavesNameAttr, new MessageFormat("{0}"));
	}

	public static IndependentVariable createForFateVessel(String id, String label, String name)
	{
		Attribute fateVesselAttr = Voyage.getAttribute(name);
		SequenceAttribute fateVesselNameAttr = new SequenceAttribute(new Attribute[]{fateVesselAttr, FateVessel.getAttribute("name")});
		SequenceAttribute fateVesselIdAttr = new SequenceAttribute(new Attribute[]{fateVesselAttr, FateVessel.getAttribute("id")});
		return new IndependentVariable(id, label, fateVesselNameAttr, new Attribute[] {fateVesselIdAttr, fateVesselNameAttr}, fateVesselNameAttr, new MessageFormat("{0}"));
	}

	public static IndependentVariable createForMonth(String id, String label, String dateAttr)
	{
		Attribute monthAttr = new FunctionAttribute(
				"date_part",
				new Attribute[] {
						new DirectValueAttribute("month"),
						Voyage.getAttribute(dateAttr)});
		return new IndependentVariable(id, label, monthAttr, monthAttr, monthAttr, new MessageFormat("{0}"));
	}

	public static IndependentVariable createForYearPeriod(String id, String label, String name, int period)
	{
		Attribute roundedVal = new FunctionAttribute(
				"round_to_multiple",
				new Attribute[] {
						Voyage.getAttribute(name),
						new DirectValueAttribute("period", new Integer(period))});
		return new IndependentVariable(id, label, roundedVal, new MessageFormat("{0}"));
	}

	public String getId()
	{
		return id;
	}
	
	public String getLabel()
	{
		return label;
	}
	
	public Format getFormat()
	{
		return format;
	}
	
	public Attribute getSelectAttribute()
	{
		return selectAttribute;
	}
	
	public Attribute[] getGroupByAttributes()
	{
		return groupByAttributes;
	}
	
	public Attribute getOrderAttribute()
	{
		return orderAttribute;
	}

}

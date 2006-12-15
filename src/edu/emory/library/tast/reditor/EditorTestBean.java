package edu.emory.library.tast.reditor;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.emory.library.tast.dm.Dictionary;
import edu.emory.library.tast.dm.Fate;
import edu.emory.library.tast.dm.FateOwner;
import edu.emory.library.tast.dm.FateSlaves;
import edu.emory.library.tast.dm.FateVessel;
import edu.emory.library.tast.dm.Nation;
import edu.emory.library.tast.dm.Resistance;
import edu.emory.library.tast.dm.VesselRig;
import edu.emory.library.tast.dm.Voyage;
import edu.emory.library.tast.dm.attributes.AreaAttribute;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.BooleanAttribute;
import edu.emory.library.tast.dm.attributes.FateAttribute;
import edu.emory.library.tast.dm.attributes.FateOwnerAttribute;
import edu.emory.library.tast.dm.attributes.FateSlavesAttribute;
import edu.emory.library.tast.dm.attributes.FateVesselAttribute;
import edu.emory.library.tast.dm.attributes.NationAttribute;
import edu.emory.library.tast.dm.attributes.NumericAttribute;
import edu.emory.library.tast.dm.attributes.PortAttribute;
import edu.emory.library.tast.dm.attributes.RegionAttribute;
import edu.emory.library.tast.dm.attributes.ResistanceAttribute;
import edu.emory.library.tast.dm.attributes.StringAttribute;
import edu.emory.library.tast.dm.attributes.VesselRigAttribute;
import edu.emory.library.tast.util.HibernateUtil;

public class EditorTestBean
{
	
	private static final String LIST_VESSEL_RIGS_NAME = "vessel-rigs";
	private static final String LIST_RESISTANCE_NAME = "resistance";
	private static final String LIST_FATES_VESSEL_NAME = "fates-vessel";
	private static final String LIST_FATES_SLAVES_NAME = "fates-slaves";
	private static final String LIST_FATES_OWNER_NAME = "fates-owner";
	private static final String LIST_FATES_NAME = "fates";
	private static final String LIST_NATIONS_NAME = "nations";
	private Values values;
	
	private void registerDictionary(String listId, Schema schema, Session sess, List dictionary)
	{
		
		ListItem[] list = new ListItem[dictionary.size()];
		
		int i = 0;
		for (Iterator iter = dictionary.iterator(); iter.hasNext();)
		{
			Dictionary dictItem = (Dictionary) iter.next();
			list[i++] = new ListItem(
					String.valueOf(dictItem.getId()),
					dictItem.getName());
		}
		
		schema.registerList(listId, list);
		
	}
	
	public Schema getSchema()
	{
		
		Schema schema = new Schema();
		
		Session sess = HibernateUtil.getSession();
		Transaction tran = sess.beginTransaction();
		
		registerDictionary(LIST_NATIONS_NAME, schema, sess, Nation.loadAll(sess));
		registerDictionary(LIST_FATES_NAME, schema, sess, Fate.loadAll(sess));
		registerDictionary(LIST_FATES_OWNER_NAME, schema, sess, FateOwner.loadAll(sess));
		registerDictionary(LIST_FATES_SLAVES_NAME, schema, sess, FateSlaves.loadAll(sess));
		registerDictionary(LIST_FATES_VESSEL_NAME, schema, sess, FateVessel.loadAll(sess));
		registerDictionary(LIST_RESISTANCE_NAME, schema, sess, Resistance.loadAll(sess));
		registerDictionary(LIST_VESSEL_RIGS_NAME, schema, sess, VesselRig.loadAll(sess));
		
		tran.commit();
		sess.close();

		Attribute[] attrs = Voyage.getAttributes();
		for (int i = 0; i < attrs.length; i++)
		{
			Attribute attr = attrs[i];
			if (attr instanceof NumericAttribute || attr instanceof StringAttribute)
			{
				schema.addField(new FieldSchemaTextbox(
						attr.getName(), attr.getName()));
			}
			else if (attr instanceof BooleanAttribute)
			{
				schema.addField(new FieldSchemaCheckbox(
						attr.getName(), attr.getName()));
			}
			else if (attr instanceof PortAttribute)
			{
           	}
			else if (attr instanceof RegionAttribute)
			{
           	}
			else if (attr instanceof AreaAttribute)
			{
           	}
			else if (attr instanceof NationAttribute)
			{
				schema.addField(new FieldSchemaDropdowns(
						attr.getName(), attr.getName(),
						LIST_NATIONS_NAME));
			}
			else if (attr instanceof FateAttribute)
			{
				schema.addField(new FieldSchemaDropdowns(
						attr.getName(), attr.getName(),
						LIST_FATES_NAME));
			}
			else if (attr instanceof FateOwnerAttribute)
			{
				schema.addField(new FieldSchemaDropdowns(
						attr.getName(), attr.getName(),
						LIST_FATES_OWNER_NAME));
			}
			else if (attr instanceof FateSlavesAttribute)
			{
				schema.addField(new FieldSchemaDropdowns(
						attr.getName(), attr.getName(),
						LIST_FATES_SLAVES_NAME));
			}
			else if (attr instanceof FateVesselAttribute)
			{
				schema.addField(new FieldSchemaDropdowns(
						attr.getName(), attr.getName(),
						LIST_FATES_VESSEL_NAME));
			}
			else if (attr instanceof VesselRigAttribute)
			{
				schema.addField(new FieldSchemaDropdowns(
						attr.getName(), attr.getName(),
						LIST_VESSEL_RIGS_NAME));
			}
			else if (attr instanceof ResistanceAttribute)
			{
				schema.addField(new FieldSchemaDropdowns(
						attr.getName(), attr.getName(),
						LIST_RESISTANCE_NAME));
			}
		}
		
		return schema;
	}

	public Values getValues()
	{
		return values;
	}

	public void setValues(Values values)
	{
		this.values = values;
	}

}
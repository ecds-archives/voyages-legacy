package edu.emory.library.tast.reditor;

import edu.emory.library.tast.dm.Voyage;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.NumericAttribute;
import edu.emory.library.tast.dm.attributes.StringAttribute;

public class EditorTestBean
{
	
	private Values values;
	
	public Schema getSchema()
	{
		
		Schema schema = new Schema();

		Attribute[] attrs = Voyage.getAttributes();
		for (int i = 0; i < attrs.length; i++)
		{
			Attribute attr = attrs[i];
			if (attr instanceof NumericAttribute || attr instanceof StringAttribute)
			{
				schema.addField(new FieldSchemaTextbox(
						attr.getName(), attr.getName()));
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
package edu.emory.library.tas.spss;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class STSchemaReader
{
	
	private ArrayList variables = null;
	private Pattern regexVariable = null;
	private Pattern regexLabel = null;
	
	private final static int FIELD_NAME = 1; 
	private final static int FIELD_START_COLUMN = 2; 
	private final static int FIELD_END_COLUMN = 3; 
	private final static int FIELD_TYPE = 4; 
	//private final static int FIELD_MISSING_VALUES = 5;
	private final static int FIELD_LABEL = 6; 
	private final static int FIELD_TAG = 7; 
	private final static int LABEL_KEY = 1; 
	private final static int LABEL_UNQUOTED = 4; 
	private final static int LABEL_SINGLE_QUOTED = 3; 
	private final static int LABEL_DOUBLE_QUOTED = 2; 
	
	public STSchemaReader()
	{
		
		// regex for variable
		regexVariable = Pattern.compile(
			"\\s*" +
			"(\\w+)" + // field name
			"\\s+" +
			"(\\d+)" + // first column
			"-" +
			"(\\d+)" + // last column
			"\\s+" +
			"(?:\\((\\w+)\\))?" + // type
			"\\s*" +
			"(?:\\[(.+)\\])?" + // missing values
			"\\s*" +
			"(?:\\{(.+)\\})?" + // des
			"\\s*" +
			"(?:\\\\(\\w+))?" + // tag
			"\\s*");

		// regex for variable
		regexLabel = Pattern.compile(
			"\\s*" +
			"(\\d+)" + // key
			"\\s+" +
			"(?:" +
				"(?:" +
					"\"(.+)\"" + // label with double quotes
				")|(?:" +
					"'(.+)'" + // label with single quotes
				")|(?:" +
					"(.*)" + // label without quotes
				")" +
			")");
		
		Matcher m = regexLabel.matcher("1 \"slave\"");
		m.matches();
		
		System.out.println(m.group(0));
		System.out.println(m.group(1));
		System.out.println(m.group(2));
		System.out.println(m.group(3));
		System.out.println(m.group(4));
		
	}
	
	private void readSectionVariables(BufferedReader rdr) throws IOException
	{
		String line = null;
		while ((line = rdr.readLine()) != null)
		{
			
			if (line.trim().length() == 0) break;
			
			Matcher matcher = regexVariable.matcher(line);

			if (matcher.matches())
			{

				STSchemaVariable var = new STSchemaVariable();
				variables.add(var);

				var.setName(
					matcher.group(FIELD_NAME));
				
				var.setStartColumn(
					Integer.parseInt(matcher.group(FIELD_START_COLUMN)));
				
				var.setEndColumn(
					Integer.parseInt(matcher.group(FIELD_END_COLUMN)));
				
				String fieldType = matcher.group(FIELD_TYPE); 
				var.setType(
					fieldType != null && fieldType.compareTo("A") == 0 ?
					STSchemaVariable.TYPE_STRING:
					STSchemaVariable.TYPE_NUMERIC);
				
				var.setLabel(
					matcher.group(FIELD_LABEL));
				
				var.setTag(
					matcher.group(FIELD_TAG));

			}
			
		}
	}
	
	private void readVariableLabels(BufferedReader rdr, STSchemaVariable var) throws IOException
	{
		String line = null;
		while ((line = rdr.readLine()) != null)
		{
			
			if (line.trim().length() == 0) break;
			
			Matcher matcher = regexLabel.matcher(line);

			if (matcher.matches())
			{

				STSchemaVariableLabel label = new STSchemaVariableLabel();
				var.getLabels().add(label);
				
				label.setKey(
					Integer.parseInt(matcher.group(LABEL_KEY)));
				
				String labelValue = null;
				labelValue = matcher.group(LABEL_DOUBLE_QUOTED);
				if (labelValue == null) labelValue = matcher.group(LABEL_SINGLE_QUOTED);
				if (labelValue == null) labelValue = matcher.group(LABEL_UNQUOTED);
				label.setLabel(
					labelValue);
				
			}

		}
	}
	
	private void readSectionValueLabels(BufferedReader rdr) throws IOException
	{
		String line = null;
		while ((line = rdr.readLine()) != null)
		{
			line = line.trim();
			if (line.length() > 0 && line.charAt(0) == '\\')
			{
				line = line.substring(1);
				for (int i=0; i<variables.size(); i++)
				{
					STSchemaVariable var = (STSchemaVariable)variables.get(i); 
					if (var.hasTag() && var.getTag().compareTo(line) == 0)
					{
						readVariableLabels(rdr, var);
					}
				}
			}
		}
	}
	
	public ArrayList readSchema(String fileName) throws IOException
	{
		
		// prepare the list of variables
		variables = new ArrayList();
		
		// open file
		BufferedReader rdr = new BufferedReader(new FileReader(fileName));
		String line = null;
		
		// read the stuff in the beginning
		while ((line = rdr.readLine()) != null)
		{
			line = line.trim();
			if (line.compareTo("VARIABLES") == 0)
			{
				readSectionVariables(rdr);
			}
			else if (line.compareTo("VALUE LABELS") == 0)
			{
				readSectionValueLabels(rdr);
			}
		}
		
		// close file
		rdr.close();
		
		// done -> return the list of variables
		return variables;

	}
		
	public static void main(String[] args) throws IOException
	{
		//STSchemaReader sr = new STSchemaReader();
		//ArrayList vars = sr.readSchema("../basecoy56.sts");
	}

}

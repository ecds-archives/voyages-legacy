package edu.emory.library.tast.spss;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class STSchemaReader
{
	
	private Hashtable variables = null;
	private Pattern regexVariable = null;
	private Pattern regexLabel = null;
	private Pattern regexTag = null;
	
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
	
	private final static String STRING_TYPE_INDICATOR = "A"; 
	private final static String DATE_TYPE_INDICATOR = "%d-%m-%Y"; 

	public STSchemaReader()
	{
		
		// regex for variable
		regexVariable = Pattern.compile(
			"\\s*" +
			"(\\w+)" + // field name
			"\\s+" +
			"(\\d+)" + // first column
			"(?:-(\\d+))?" + // last column
			"\\s*" +
			"(?:\\((" + STRING_TYPE_INDICATOR + "|" + DATE_TYPE_INDICATOR + ")\\))?" + // type
			"\\s*" +
			"(?:\\[(.+)\\])?" + // missing values
			"\\s*" +
			"(?:\\{(.+)\\})?" + // des
			"\\s*" +
			"(?:\\\\(\\w+))?" + // tag
			"\\s*");

		// regex for tag
		regexTag = Pattern.compile(
			"\\s*" +
			"\\\\(\\w+)");

		// regex for label
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

	}
	
	private void readSectionVariables(BufferedReader rdr) throws IOException, STSchemaException
	{
		String line = null;
		while ((line = rdr.readLine()) != null)
		{
			
			if (line.trim().length() == 0) break;
			
			Matcher matcher = regexVariable.matcher(line);

			if (matcher.matches())
			{

				STSchemaVariable var = new STSchemaVariable();
				
				var.setName(
					matcher.group(FIELD_NAME));
				
				var.setStartColumn(
					Integer.parseInt(matcher.group(FIELD_START_COLUMN)));
				
				var.setEndColumn(
					matcher.group(FIELD_END_COLUMN) != null ?
					Integer.parseInt(matcher.group(FIELD_END_COLUMN)) :
					var.getStartColumn());
				
				String fieldType = matcher.group(FIELD_TYPE); 
				if (fieldType != null && fieldType.equals(STRING_TYPE_INDICATOR))
				{
					var.setType(STSchemaVariable.TYPE_STRING);
				}
				else if (fieldType != null && fieldType.equals(DATE_TYPE_INDICATOR))
				{
					var.setType(STSchemaVariable.TYPE_DATE);
				}
				else
				{
					var.setType(STSchemaVariable.TYPE_NUMERIC);
				}
				
				var.setLabel(
					matcher.group(FIELD_LABEL));
				
				var.setTag(
					matcher.group(FIELD_TAG));

				variables.put(var.getName(), var);

			}
			else
			{
				throw new STSchemaException("Error parsing line: '" + line + "' while reading variables.");
			}
			
		}
	}
	
	private void readVariableLabels(BufferedReader rdr, STSchemaVariable var) throws IOException, STSchemaException
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
			else
			{
				throw new STSchemaException("Error parsing line: '" + line + "' while reading labels.");
			}

		}
	}
	
	private void readSectionValueLabels(BufferedReader rdr) throws IOException, STSchemaException
	{
		String line = null;
		while ((line = rdr.readLine()) != null)
		{
			Matcher matcher = regexTag.matcher(line);
			if (matcher.lookingAt())
			{
				STSchemaVariable var = (STSchemaVariable) variables.get(matcher.group(1));
				if (var != null)
				{
					readVariableLabels(rdr, var);
				}
			}
		}
	}
	
	public Hashtable readSchema(String fileName) throws IOException, STSchemaException
	{
		
		// prepare the list of variables
		variables = new Hashtable();
		
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
		
	public static void main(String[] args) throws IOException, STSchemaException
	{
//		STSchemaReader sr = new STSchemaReader();
//		ArrayList vars = sr.readSchema("slaves.sts");
//		for (int i=0; i<vars.size(); i++)
//		{
//			STSchemaVariable var = (STSchemaVariable)vars.get(i);
//			System.out.println(var.getName());
//		}
	}

}

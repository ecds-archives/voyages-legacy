package edu.emory.library.tast.spss;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.configuration.Configuration;

import edu.emory.library.tast.AppConfig;
import edu.emory.library.tast.dm.AbstractDescriptiveObject;
import edu.emory.library.tast.dm.AbstractDescriptiveObjectFactory;
import edu.emory.library.tast.dm.Voyage;
import edu.emory.library.tast.dm.VoyageFactory;
import edu.emory.library.tast.dm.attributes.ImportableAttribute;
import edu.emory.library.tast.dm.attributes.exceptions.InvalidDateException;
import edu.emory.library.tast.dm.attributes.exceptions.InvalidNumberException;
import edu.emory.library.tast.dm.attributes.exceptions.StringTooLongException;

public class Import
{

	public static final String DAT_FILE_NAME = "data.dat";
	public static final String STS_FILE_NAME = "data.sts";

	private String importDir;
	private LogWriter log;
	private String dataFileName;
	private STSchemaVariable voyagesVidVar;
	private RecordIOFactory recordIOFactory;
	private String schemaFileName;
	private Map schema;
	private ImportableAttribute[] attributes;
	
	private int totalRecords;
	private int recordsWithWarnings;
	
	private AbstractDescriptiveObjectFactory objectFactory;

	private int calculateSchemaWidth(Map schema)
	{
		int width = 0;
		String[] names = (String[]) schema.keySet().toArray(new String[] {});
		for (int i = 0; i < names.length; i++)
		{
			STSchemaVariable var = (STSchemaVariable) schema.get(names[i]);
			width += var.getLength();
		}
		return width;
	}
	
	private void loadSchemas() throws IOException, STSchemaException
	{

		STSchemaReader rdr = new STSchemaReader();

		log.logInfo("Reading data schema.");

		schema = rdr.readSchema(schemaFileName);

		voyagesVidVar = (STSchemaVariable) schema.get("voyageid");
		int voyagesSchemaWidth = calculateSchemaWidth(schema);
		recordIOFactory = new RecordIOFactory(
				voyagesVidVar.getStartColumn(),
				voyagesVidVar.getEndColumn(),
				voyagesSchemaWidth);

		log.logInfo("Data schema successfully read.");

	}

	private boolean matchAndVerifySchemaV2() throws DataSchemaMissmatchException
	{
		
		boolean ok = true;
		
		for (int i = 0; i < attributes.length; i++)
		{
			ImportableAttribute attr = attributes[i];
			if (!attr.ignoreImport())
			{
				STSchemaVariable var = (STSchemaVariable) schema.get(attr.getImportName());
				if (var == null)
				{
					ok = false;
					log.logError("Missing variable for attribute " +
							attr.getName() + ".");
				}
				else if (var.getType() != attr.getImportType())
				{
					ok = false;
					log.logError("Type missmatch for attribute " +
							attr.getName() + ".");
				}
				else if (attr.isImportLengthLimited() && attr.getMaxImportLength() > var.getLength())
				{
					ok = false;
					log.logError("String too long for database field " + attr.getName() + "." +
							"Expected length = " + attr.getMaxImportLength() + ", " +
							"Variable length = " + var.getLength() + ".");
				}
			}
		}
		
		return ok;

	}

	private boolean updateValues(AbstractDescriptiveObject obj, Record record, int recordNo)
	{

		boolean valid = true;
		for (int i = 0; i < attributes.length; i++)
		{
			
			ImportableAttribute attr = attributes[i];
			STSchemaVariable var = (STSchemaVariable) schema.get(attr.getImportName());

			Object parsedValue = null;
			String columnValue = null;
			
			if (attr.ignoreImport())
				continue;

			try
			{
				
				columnValue = record.getValue(var);
				parsedValue = attr.importParse(columnValue);
				
				obj.setAttrValue(attr.getName(), parsedValue);

			}
			catch (InvalidNumberException ine)
			{
				valid = false;
				log.logWarn("Invalid numeric value '" + columnValue + "' " +
						"in variable " + var.getName() +
						"in record " + recordNo + ".");
			}
			catch (InvalidDateException ide)
			{
				valid = false;
				log.logWarn("Invalid date value '" + columnValue + "' " +
						"in variable " + var.getName() +
						"in record " + recordNo + ".");
			}
			catch (StringTooLongException stle)
			{
				valid = false;
				log.logWarn("String '" + parsedValue + "' too long "
						+ "in variable " + var.getName() +
						" in record " + recordNo + ".");
			}

		}

		return valid;

	}

	private void importData() throws IOException
	{
		
		RecordReader voyagesRdr = recordIOFactory.createReader(dataFileName);
		Record voyageRecord = null;
		
		int recordNo = 0;
		while ((voyageRecord = voyagesRdr.readRecord()) != null)
		{
			AbstractDescriptiveObject obj = objectFactory.newInstance();
			updateValues(obj, voyageRecord, recordNo++);
			obj.saveOrUpdate();
		}

	}

	public void runImport(LogWriter log, String importDir, AbstractDescriptiveObjectFactory objectFactory, ImportableAttribute[] attributes)
	{

		this.attributes = attributes;
		this.objectFactory = objectFactory;
		this.log = log;
		this.importDir = importDir;

		dataFileName = this.importDir + File.separatorChar + DAT_FILE_NAME;
		schemaFileName = this.importDir + File.separatorChar + STS_FILE_NAME;

		try
		{

			log.startStage(LogItem.STAGE_SCHEMA_LOADING);
			loadSchemas();

			log.startStage(LogItem.STAGE_SCHEMA_MATCHING);
			matchAndVerifySchemaV2();

			log.startStage(LogItem.STAGE_IMPORTING_DATA);
			importData();

			log.startStage(LogItem.STAGE_SUMMARY);
			log.logInfo("Import successfully completed.");
			log.logInfo("Total number of records = " + totalRecords);
			log.logInfo("Records with warnings = " + recordsWithWarnings);
			log.doneWithSuccess();

		}
		catch (IOException ioe)
		{
			log.logError("IO error: " + ioe.getMessage());
			log.logInfo("Import terminated.");
			log.doneWithErrors();
		}
		catch (STSchemaException stse)
		{
			log.logError(stse.getMessage());
			log.logInfo("Import terminated.");
			log.doneWithErrors();
		}
		catch (DataSchemaMissmatchException dsme)
		{
			log.logInfo("Import terminated.");
			log.doneWithErrors();
		}

	}

	public static void main(String[] args) throws FileNotFoundException, IOException
	{

		if (args.length != 1) return;
		String importDir = args[0];

		Configuration conf = AppConfig.getConfiguration();
		String fullImportDir = conf.getString(AppConfig.IMPORT_ROOTDIR) + File.separatorChar + importDir;
		LogWriter log = new LogWriter(fullImportDir);

		AbstractDescriptiveObjectFactory objectFactory = new VoyageFactory();
		ImportableAttribute[] attributes = (ImportableAttribute[]) Voyage.getAttributes();
		
		Import imp = new Import();
		imp.runImport(log, fullImportDir, objectFactory, attributes);
		log.close();

		
	}
}
package edu.emory.library.tast.spss;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.configuration.Configuration;

import edu.emory.library.tast.AppConfig;
import edu.emory.library.tast.dm.AbstractDescriptiveObject;
import edu.emory.library.tast.dm.AbstractDescriptiveObjectFactory;
import edu.emory.library.tast.dm.Dictionary;
import edu.emory.library.tast.dm.Port;
import edu.emory.library.tast.dm.Region;
import edu.emory.library.tast.dm.Voyage;
import edu.emory.library.tast.dm.VoyageFactory;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.DateAttribute;
import edu.emory.library.tast.dm.attributes.DictionaryAttribute;
import edu.emory.library.tast.dm.attributes.StringAttribute;
import edu.emory.library.tast.dm.attributes.exceptions.InvalidDateException;
import edu.emory.library.tast.dm.attributes.exceptions.InvalidNumberException;
import edu.emory.library.tast.dm.attributes.exceptions.InvalidNumberOfValuesException;
import edu.emory.library.tast.dm.attributes.exceptions.StringTooLongException;

public class ImportV2
{

	public static final String VOYAGES_SAV = "voyages.sav";
	public static final String DAT_FILE_NAME = "voyages.dat";
	public static final String STS_FILE_NAME = "voyages.sts";
	public static final String VOYAGES_NUMBERED_DAT = "voyages-numbered.dat";
	public static final String VOYAGES_SORTED_DAT = "voyages-sorted.dat";
	private static final int MAX_INT_LENGTH = 10;
	private static final int VOYAGE = 0;

	private String importDir;
	private LogWriter log;
	private String dataFileName;
	private STSchemaVariable voyagesVidVar;
	private RecordIOFactory voyagesRecordIOFactory;
	private String schemaFileName;
	private Map voyageSchema;
	private Attribute voyageAttributes[] = Voyage.getAttributes();
	private STSchemaVariable voyagesLineNoVar = null;
	
	private int totalNoOfVoyages;
	private int noOfVoyagesWithInvalidVid;
	private int totalNoOfSlaves;
	private int noOfSlavesWithInvalidVid;
	private int noOfSlavesWithInvalidSid;
	private int noOfUpdatedVoyages;
	private int noOfCreatedVoyages;
	private int noOfVoyagesWithSlaves;
	private int noOfDeletedSlaves;
	private int noOfUpdatedSlaves;
	private int noOfCreatedSlaves;
	private int noOfInvalidVoyages;
	private int noOfInvalidSlaves;
	
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

		// read schemas
		voyageSchema = rdr.readSchema(schemaFileName);

		// locate vid and create record io factory for voyages
		voyagesVidVar = (STSchemaVariable) voyageSchema.get("voyageid");
		int voyagesSchemaWidth = calculateSchemaWidth(voyageSchema);
		voyagesRecordIOFactory = new RecordIOFactory(
				voyagesVidVar.getStartColumn(),
				voyagesVidVar.getEndColumn(),
				voyagesSchemaWidth);

		log.logInfo("Data schema successfully read.");

	}

	private boolean compareTypes(int colType, int varType)
	{
		return (colType == Attribute.IMPORT_TYPE_NUMERIC && varType == STSchemaVariable.TYPE_NUMERIC)
				|| (colType == Attribute.IMPORT_TYPE_STRING && varType == STSchemaVariable.TYPE_STRING);
	}

	private void matchAndVerifySchema() throws DataSchemaMissmatchException
	{

		Attribute attributes[] = null;
		Map schema = null;
		String errorMsgDataFile = null;

		boolean ok = true;

		attributes = voyageAttributes;
		schema = voyageSchema;
		errorMsgDataFile = "voyages";
		
		for (int i = 0; i < attributes.length; i++)
		{
			Attribute col = attributes[i];
			int type = col.getImportType().intValue();

			// ignoring
			if (type == Attribute.IMPORT_TYPE_IGNORE)
			{
				continue;
			}

			// date
			else if (type == Attribute.IMPORT_TYPE_DATE)
			{
				DateAttribute colDate = (DateAttribute)col;
				STSchemaVariable varDay = (STSchemaVariable) schema.get(colDate.getImportDateDay());
				STSchemaVariable varMonth = (STSchemaVariable) schema.get(colDate.getImportDateMonth());
				STSchemaVariable varYear = (STSchemaVariable) schema.get(colDate.getImportDateYear());
				if (varDay == null || varMonth == null || varYear == null)
				{
					ok = false;
					log.logError("Missing variable for database field "
							+ col.getName() + " " + "of type date " + "in "
							+ errorMsgDataFile + ".");

				}
				else if (varDay.getType() != STSchemaVariable.TYPE_NUMERIC
						|| varMonth.getType() != STSchemaVariable.TYPE_NUMERIC
						|| varYear.getType() != STSchemaVariable.TYPE_NUMERIC)
				{
					ok = false;
					log.logError("Type missmatch for database field "
							+ col.getName() + " " + "of type date " + "in "
							+ errorMsgDataFile + ".");
				}
			}

			// number or string
			else
			{
				STSchemaVariable var = (STSchemaVariable) schema.get(col
						.getImportName());
				if (var == null)
				{
					ok = false;
					log.logError("Missing field for database field "
							+ col.getName() + " " + "in " + errorMsgDataFile
							+ ".");
				}
				else if (!compareTypes(type, var.getType()))
				{
					ok = false;
					log.logError("Type missmatch for database field "
							+ col.getName() + " " + "in " + errorMsgDataFile
							+ ".");
				}
				else if (type == Attribute.IMPORT_TYPE_STRING
						&& var.getLength() > ((StringAttribute)col).getLength().intValue())
				{
					ok = false;
					log.logError("String too long for database field "
							+ col.getName() + " " + "in " + errorMsgDataFile
							+ "." + "Expected length = " + ((StringAttribute)col).getLength()
							+ ", " + "Variable length = " + var.getLength()
							+ ".");
				}
			}

		}

		if (!ok) throw new DataSchemaMissmatchException();

	}

	private boolean updateValues(AbstractDescriptiveObject obj, Record record)
	{

		Attribute attributes[] = null;
		Map schema = null;
		String errorMsgDataFile = null;
		int recordNo = 0;

		attributes = voyageAttributes;
		schema = voyageSchema;
		errorMsgDataFile = "voyages";
		recordNo = Integer.parseInt(record.getValue(voyagesLineNoVar));

		boolean valid = true;
		for (int i = 0; i < attributes.length; i++)
		{
			Attribute attr = attributes[i];

			int importType = attr.getImportType().intValue();

			Object parsedValue = null;
			String columnValue = null;
			String columnDayValue = null;
			String columnMonthValue = null;
			String columnYearValue = null;
			STSchemaVariable var = null;
			STSchemaVariable varDay = null;
			STSchemaVariable varMonth = null;
			STSchemaVariable varYear = null;

			// we are ignoring this field for import
			if (importType == Attribute.IMPORT_TYPE_IGNORE) continue;

			try
			{
				
				// ports
				if (attr instanceof DictionaryAttribute && ((DictionaryAttribute)attr).getDictionary().equals("Port"))
				{
					try
					{
						var = (STSchemaVariable) schema.get(attr.getImportName());
						parsedValue = Port.loadById(Long.parseLong(record.getValue(var)));
					}
					catch (NumberFormatException nfe)
					{
					}
				}

				// regions
				else if (attr instanceof DictionaryAttribute && ((DictionaryAttribute)attr).getDictionary().equals("Region"))
				{
					try
					{
						var = (STSchemaVariable) schema.get(attr.getImportName());
						parsedValue = Region.loadById(Long.parseLong(record.getValue(var)));
					}
					catch (NumberFormatException nfe)
					{
					}
				}
				
				// import numeric or string field
				else if (importType == Attribute.IMPORT_TYPE_NUMERIC || importType == Attribute.IMPORT_TYPE_STRING)
				{
					var = (STSchemaVariable) schema.get(attr.getImportName());
					columnValue = record.getValue(var);
					parsedValue = attr.parse(columnValue);
				}

				/*
				// import date
				else if (importType == Attribute.IMPORT_TYPE_DATE)
				{
					varDay = (STSchemaVariable) schema.get(((DateAttribute)attr).getImportDateDay());
					varMonth = (STSchemaVariable) schema.get(((DateAttribute)attr).getImportDateMonth());
					varYear = (STSchemaVariable) schema.get(((DateAttribute)attr).getImportDateYear());
					columnDayValue = record.getValue(varDay);
					columnMonthValue = record.getValue(varMonth);
					columnYearValue = record.getValue(varYear);
					parsedValue = attr.parse(new String[] { columnDayValue, columnMonthValue, columnYearValue });
				}
				*/

				// nonexisting dictionary value was inserted
				if (attr instanceof DictionaryAttribute && parsedValue != null && ((Dictionary) parsedValue).getId() == null)
					log.logWarn("Nonexistent label '"
								+ ((Dictionary) parsedValue).getName() + "' "
								+ "inserted for variable "
								+ attr.getImportName() + " " + "in "
								+ errorMsgDataFile + " " + "in record "
								+ recordNo + ".");

				// update the object
				obj.setAttrValue(attr.getName(), parsedValue);

			}
			catch (InvalidNumberOfValuesException inve)
			{
				// this cannot happen
				// see the code above
			}
			catch (InvalidNumberException ine)
			{
				valid = false;
				log.logWarn("Invalid numeric value '" + columnValue + "' "
						+ "in variable " + var.getName() + " " + "in "
						+ errorMsgDataFile + " " + "in record " + recordNo
						+ ".");
			}
			catch (InvalidDateException ide)
			{
				valid = false;
				log.logWarn("Invalid date " + "day = '" + columnDayValue
						+ "' in variable " + varDay.getName() + ", "
						+ "month = '" + columnMonthValue + "' in variable "
						+ varMonth.getName() + ", " + "year = '"
						+ columnYearValue + "' in variable "
						+ varYear.getName() + " " + "in " + errorMsgDataFile
						+ " " + "in record " + recordNo + ".");
			}
			catch (StringTooLongException stle)
			{
				valid = false;
				log.logWarn("String '" + parsedValue + "' too long "
						+ "in variable " + var.getName() + " " + "in "
						+ errorMsgDataFile + " " + "in record " + recordNo
						+ ".");
			}

		}

		return valid;

	}

	private void importData() throws IOException
	{
		
		RecordReader voyagesRdr = voyagesRecordIOFactory.createReader(dataFileName);
		Record voyageRecord = null;
		
		while ((voyageRecord = voyagesRdr.readRecord()) != null)
		{
			AbstractDescriptiveObject obj = objectFactory.newInstance();
			updateValues(obj, voyageRecord);
		}

	}

	public void runImport(LogWriter log, String importDir, AbstractDescriptiveObjectFactory objectFactory)
	{
		
		this.objectFactory = objectFactory;

		this.log = log;
		this.importDir = importDir;

		dataFileName = importDir + File.separatorChar + DAT_FILE_NAME;
		schemaFileName = importDir + File.separatorChar + STS_FILE_NAME;

		try
		{

			log.startStage(LogItem.STAGE_SCHEMA_LOADING);
			loadSchemas();

			log.startStage(LogItem.STAGE_SCHEMA_MATCHING);
			matchAndVerifySchema();

			log.startStage(LogItem.STAGE_IMPORTING_DATA);
			importData();

			log.startStage(LogItem.STAGE_SUMMARY);
			log.logInfo("Import successfully completed.");
			log.logInfo("Total number of voyages = " + totalNoOfVoyages);
			log.logInfo("Number voyages with invalid ID = "+ noOfVoyagesWithInvalidVid);
			log.logInfo("Number of created voyages = " + noOfCreatedVoyages);
			log.logInfo("Number of updated voyages = " + noOfUpdatedVoyages);
			log.logInfo("Number of invalid (but imported) voyages = " + noOfInvalidVoyages);
			log.logInfo("Total number of slaves = " + totalNoOfSlaves);
			log.logInfo("Number slaves with invalid ID = " + noOfSlavesWithInvalidSid);
			log.logInfo("Number slaves with invalid VoyageID = " + noOfSlavesWithInvalidVid);
			log.logInfo("Number of deleted slaves = " + noOfDeletedSlaves);
			log.logInfo("Number of created slaves = " + noOfCreatedSlaves);
			log.logInfo("Number of updated slaves = " + noOfUpdatedSlaves);
			log.logInfo("Number of invalid (but imported) slaves = " + noOfInvalidSlaves);
			log.logInfo("Voyages with slaves = " + noOfVoyagesWithSlaves);
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

		// extracts params
		if (args.length != 1) return;
		String importDir = args[0];

		// crate a log
		Configuration conf = AppConfig.getConfiguration();
		String fullImportDir = conf.getString(AppConfig.IMPORT_ROOTDIR) + File.separatorChar + importDir;
		LogWriter log = new LogWriter(fullImportDir);

		// import
		AbstractDescriptiveObjectFactory objectFactory = new VoyageFactory();
		ImportV2 imp = new ImportV2();
		imp.runImport(log, fullImportDir, objectFactory);
		log.close();

		
	}
}
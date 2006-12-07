package edu.emory.library.tast.spss;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.configuration.Configuration;

import edu.emory.library.tast.AppConfig;
import edu.emory.library.tast.dm.AbstractDescriptiveObject;
import edu.emory.library.tast.dm.AbstractDescriptiveObjectFactory;
import edu.emory.library.tast.dm.Dictionary;
import edu.emory.library.tast.dm.Port;
import edu.emory.library.tast.dm.Region;
import edu.emory.library.tast.dm.Slave;
import edu.emory.library.tast.dm.Voyage;
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
	public static final String VOYAGES_DAT = "voyages.dat";
	public static final String VOYAGES_STS = "voyages.sts";
	public static final String VOYAGES_NUMBERED_DAT = "voyages-numbered.dat";
	public static final String VOYAGES_SORTED_DAT = "voyages-sorted.dat";
	public static final String SLAVES_SAV = "slaves.sav";

	/**
	 * The name slaves data file after conversion by StatTransfer. The format of
	 * the file is fixed column widht. This is just the file name. When
	 * importing it is prepended by the import directory.
	 */
	public static final String SLAVES_DAT = "slaves.dat";

	/**
	 * The schema file of voyages provided by StatTransfer. This is just the
	 * file name. When importing it is prepended by the import directory.
	 */
	public static final String SLAVES_STS = "slaves.sts";

	/**
	 * The name of the slaves file after row numbering. This is just the file
	 * name. When importing it is prepended by the import directory.
	 */
	public static final String SLAVES_NUMBERED_DAT = "slaves-numbered.dat";

	/**
	 * The name of the slaves file after sorting. This is just the file name.
	 * When importing it is prepended by the import directory.
	 */
	public static final String SLAVES_SORTED_DAT = "slaves-sorted.dat";

	/**
	 * An integer in the imported files cannot take more than this letters. Use
	 * only by {@link #addLineNumbers(int)}.
	 */
	private static final int MAX_INT_LENGTH = 10;

	/**
	 * The number of preloaded voyages from the database when importing.
	 */
	private static final int VOYAGE_CACHE_SIZE = 100;

	/**
	 * Used as an arguments by methods which work with both voyages and slaves.
	 */
	private static final int VOYAGE = 0;

	/**
	 * Used as an arguments by methods which work with both voyages and slaves.
	 */
	private static final int SLAVE = 1;

	/**
	 * Name of the import directory (not the entire path). It is genarated and
	 * created by {@link ImportServlet} and passed to {@link #main(String[])} as
	 * the only argument.
	 */
	private String importDir;
	
	/**
	 * Import log.
	 */
	private LogWriter log;

	/**
	 * The name under which the uploaded SPSS file with voyages of the current
	 * import is saved. It is obtained from {@link #VOYAGES_SAV} by prepending
	 * the current import directory.
	 */
	private String voyagesSpssFileName;

	/**
	 * The name under which the uploaded SPSS file with slaves of the current
	 * import is saved. It is obtained from {@link #SLAVES_SAV} by prepending
	 * the current import directory.
	 */
	private String slavesSpssFileName;

	/**
	 * The name voyages data file of the current import after conversion by
	 * StatTransfer. It is obtained from {@link #VOYAGES_DAT} by prepending the
	 * current import directory.
	 */
	private String voyagesDataFileName;

	/**
	 * The name of the voyages file after sorting of the current import. It is
	 * obtained from {@link #VOYAGES_SORTED_DAT} by prepending the current import
	 * directory.
	 */
	private String voyagesSortedDataFileName;

	/**
	 * The name of the voyages file after row numbering of the current import. It is
	 * obtained from {@link #VOYAGES_NUMBERED_DAT} by prepending the current import
	 * directory.
	 */
	private String voyagesNumberedDataFileName;

	/**
	 * The name slaves data file of the current import after conversion by
	 * StatTransfer. It is obtained from {@link #SLAVES_DAT} by prepending the
	 * current import directory.
	 */
	private String slavesDataFileName;

	/**
	 * The name of the slaves file after sorting of the current import. It is
	 * obtained from {@link #SLAVES_SORTED_DAT} by prepending the current import
	 * directory.
	 */
	private String slavesSortedDataFileName;

	/**
	 * The name of the slaves file after row numbering of the current import. It is
	 * obtained from {@link #SLAVES_NUMBERED_DAT} by prepending the current import
	 * directory.
	 */
	private String slavesNumberedDataFileName;

	/**
	 * Indicates whether the voyages file is being imported.
	 */
	private boolean voyagesPresent;

	/**
	 * Indicates whether the slaves file is being imported.
	 */
	private boolean slavesPresent;

	/**
	 * For efficiency, a reference to the voyage ID variable of voyages.
	 * It is also contained in {@link #voyageSchema}.
	 */
	private STSchemaVariable voyagesVidVar;

	/**
	 * For efficiency, a reference to the voyage ID variable of slaves.
	 */
	private STSchemaVariable slavesVidVar;

	/**
	 * Factory for creating reader and writers for the imported files. Basically
	 * it contains the information about the length of rows and the position of
	 * the voyage ID (called key by {@link RecordIOFactory}).
	 */
	private RecordIOFactory voyagesRecordIOFactory;

	/**
	 * Factory for creating reader and writers for the imported files. Basically
	 * it contains the information about the length of rows and the position of
	 * the voyage ID (called key by {@link RecordIOFactory}).
	 */
	private RecordIOFactory slavesRecordIOFactory;

	/**
	 * The schema file of voyages of the current import provided by
	 * StatTransfer. It is obtained from {@link #VOYAGES_STS} by prepending the
	 * current import directory.
	 */
	private String voyagesSchemaFileName;

	/**
	 * The schema file of slaves of the current import provided by
	 * StatTransfer. It is obtained from {@link #SLAVES_STS} by prepending the
	 * current import directory.
	 */
	private String slavesSchemaFileName;

	/**
	 * The set of the voyages variables. This is the schema of the imported
	 * file obtaned from SPSS (i.e. not from the database).
	 */
	private Map voyageSchema;

	/**
	 * The set of the slaves variables. This is the schema of the imported
	 * file obtaned from SPSS (i.e. not from the database).
	 */
	private Map slaveSchema;

	/**
	 * The database attributes of a voyage.
	 */
	private Attribute voyageAttributes[] = Voyage.getAttributes();

	/**
	 * The database attributes of a slave.
	 */
	private Attribute slaveAttributes[] = Slave.getAttributes();

	/**
	 * The auxiliary column containing the row number of voyages data file. It
	 * is added before sorting so that we can report errors with the original
	 * row numbers.
	 */
	private STSchemaVariable voyagesLineNoVar = null;

	/**
	 * The auxiliary column containing the row number of slaves data file. It
	 * is added before sorting so that we can report errors with the original
	 * row numbers.
	 */
	private STSchemaVariable slavesLineNoVar = null;
	
	/**
	 * Statistics.
	 */
	private int totalNoOfVoyages;

	/**
	 * Statistics.
	 */
	private int noOfVoyagesWithInvalidVid;

	/**
	 * Statistics.
	 */
	private int totalNoOfSlaves;

	/**
	 * Statistics.
	 */
	private int noOfSlavesWithInvalidVid;

	/**
	 * Statistics.
	 */
	private int noOfSlavesWithInvalidSid;

	/**
	 * Statistics.
	 */
	private int noOfUpdatedVoyages;

	/**
	 * Statistics.
	 */
	private int noOfCreatedVoyages;

	/**
	 * Statistics.
	 */
	private int noOfVoyagesWithSlaves;

	/**
	 * Statistics.
	 */
	private int noOfDeletedSlaves;

	/**
	 * Statistics.
	 */
	private int noOfUpdatedSlaves;

	/**
	 * Statistics.
	 */
	private int noOfCreatedSlaves;

	/**
	 * Statistics.
	 */
	private int noOfInvalidVoyages;

	/**
	 * Statistics.
	 */
	private int noOfInvalidSlaves;
	
	private AbstractDescriptiveObjectFactory objectFactory;

	private class IdIsEmptyException extends Exception
	{
		private static final long serialVersionUID = 4076984197898103476L;
	}

	
	/**
	 * Calculates the total width in characters of a row in an imported file.
	 * 
	 * @param schema
	 *            The imported schema (i.e. a set of columns).
	 * @return The total width.
	 */
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
	
	/**
	 * Load imported schemas from the sts files produced by StatTransfer. Uses
	 * {@link STSchemaReader} to do it.
	 * 
	 * @throws IOException
	 * @throws STSchemaException
	 */
	private void loadSchemas() throws IOException, STSchemaException
	{

		STSchemaReader rdr = new STSchemaReader();

		if (voyagesPresent)
		{
			log.logInfo("Reading data schema of voyages.");

			// read schemas
			voyageSchema = rdr.readSchema(voyagesSchemaFileName);

			// locate vid and create record io factory for voyages
			voyagesVidVar = (STSchemaVariable) voyageSchema.get("voyageid");
			int voyagesSchemaWidth = calculateSchemaWidth(voyageSchema);
			voyagesRecordIOFactory = new RecordIOFactory(
					voyagesVidVar.getStartColumn(),
					voyagesVidVar.getEndColumn(),
					voyagesSchemaWidth);

			log.logInfo("Data schema successfully read.");
		}

		if (slavesPresent)
		{
			log.logInfo("Reading data schema of slaves.");

			// read schemas
			slaveSchema = rdr.readSchema(slavesSchemaFileName);

			// locate vid create record io factory for slaves
			slavesVidVar = (STSchemaVariable) slaveSchema.get("voyageid");
			int slavesSchemaWidth = calculateSchemaWidth(slaveSchema);
			slavesRecordIOFactory = new RecordIOFactory(
					slavesVidVar.getStartColumn(), slavesVidVar.getEndColumn(),
					slavesSchemaWidth);

			log.logInfo("Data schema successfully read.");
		}

	}

	/**
	 * A utility method for {@link #matchAndVerifySchema(int)} which compares a
	 * database attribute type and a imported variable type. The data types are
	 * compared internally by {@link #matchAndVerifySchema(int)} because they
	 * are not mapped 1-1.
	 * 
	 * @param colType
	 *            Database attributes type.
	 * @param varType
	 *            Imported variable type.
	 * @return
	 */
	private boolean compareTypes(int colType, int varType)
	{
		return (colType == Attribute.IMPORT_TYPE_NUMERIC && varType == STSchemaVariable.TYPE_NUMERIC)
				|| (colType == Attribute.IMPORT_TYPE_STRING && varType == STSchemaVariable.TYPE_STRING);
	}

	/**
	 * Compares the database schema of the given file determined by
	 * {@link #VOYAGE} or {@link #SLAVE} with the imported schema. It checks if
	 * the imported schema contains the required columns and whether the columns
	 * have the right types.
	 * 
	 * @throws DataSchemaMissmatchException
	 */
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

	/**
	 * Called by {@link #updateVoyage(Voyage, Record)},
	 * {@link #addNewSlaves(Voyage, Map)},
	 * {@link #removeDeletedSlaves(Voyage, Map)}. It takes a row from an
	 * imported data file, chops it into columns and updates the fiels of the
	 * provided object (i.e. voyage or slaves).
	 * 
	 * @param obj
	 *            Slave of voyage which was loaded from the database or created
	 *            (if it did not exist before).
	 * @param record
	 *            A row from the imported file.
	 * @return
	 */
	private boolean updateValues(AbstractDescriptiveObject obj, Record record)
	{

		Attribute attributes[] = null;
		Map schema = null;
		String errorMsgDataFile = null;
		int recordNo = 0;

		if (obj instanceof Voyage)
		{
			attributes = voyageAttributes;
			schema = voyageSchema;
			errorMsgDataFile = "voyages";
			recordNo = Integer.parseInt(record.getValue(voyagesLineNoVar));
		}
		else if (obj instanceof Slave)
		{
			attributes = slaveAttributes;
			schema = slaveSchema;
			errorMsgDataFile = "slaves";
			recordNo = Integer.parseInt(record.getValue(slavesLineNoVar));
		}
		else
		{
			return false;
		}

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
		
		RecordReader voyagesRdr = voyagesRecordIOFactory.createReader(voyagesSortedDataFileName);
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

		voyagesSpssFileName = importDir + File.separatorChar + VOYAGES_SAV;
		voyagesDataFileName = importDir + File.separatorChar + VOYAGES_DAT;
		voyagesNumberedDataFileName = importDir + File.separatorChar + VOYAGES_NUMBERED_DAT;
		voyagesSortedDataFileName = importDir + File.separatorChar + VOYAGES_SORTED_DAT;
		voyagesSchemaFileName = importDir + File.separatorChar + VOYAGES_STS;
		voyagesPresent = new File(voyagesSpssFileName).exists();

		slavesSpssFileName = importDir + File.separatorChar + SLAVES_SAV;
		slavesDataFileName = importDir + File.separatorChar + SLAVES_DAT;
		slavesSortedDataFileName = importDir + File.separatorChar + SLAVES_SORTED_DAT;
		slavesNumberedDataFileName = importDir + File.separatorChar + SLAVES_NUMBERED_DAT;
		slavesSchemaFileName = importDir + File.separatorChar + SLAVES_STS;
		slavesPresent = new File(slavesSpssFileName).exists();

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

	/**
	 * Called externally by the ImportServlet. args has to have just one
	 * paramater containing the name of the import directory (not the entire
	 * path - just the directory).
	 * 
	 * @param args
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
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
		ImportV2 imp = new ImportV2();
		imp.runImport(log, fullImportDir);
		log.close();

		
	}
}
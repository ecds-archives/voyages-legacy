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
import edu.emory.library.tast.dm.Dictionary;
import edu.emory.library.tast.dm.Slave;
import edu.emory.library.tast.dm.Voyage;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.dm.attributes.exceptions.InvalidDateException;
import edu.emory.library.tast.dm.attributes.exceptions.InvalidNumberException;
import edu.emory.library.tast.dm.attributes.exceptions.InvalidNumberOfValuesException;
import edu.emory.library.tast.dm.attributes.exceptions.StringTooLongException;

/**
 * <p>
 * Stages of the import:
 * </p>
 * <ol>
 * <li>convert files by StatTransfer,</li>
 * <li>parse and load data schemas provided by StatTransfer in sts files,</li>
 * <li>compare the database schama with the data file schema,</li>
 * <li>add extra column to the imported files with line numbers,</li>
 * <li>sort imported files,</li>
 * <li>update dictionaries,</li>
 * <li>import data.</li>
 * </ol>
 * 
 * @author Jan Zich
 * 
 */
public class Import
{

	/**
	 * The name under which the uploaded SPSS file with voyages is saves. Used
	 * also by {@link ImportServletImpo}. This is just the file name. When
	 * importing it is prepended by the import directory.
	 */
	public static final String VOYAGES_SAV = "voyages.sav";

	/**
	 * The name voyages data file after conversion by StatTransfer. The format
	 * of the file is fixed column widht. This is just the file name. When
	 * importing it is prepended by the import directory.
	 */
	public static final String VOYAGES_DAT = "voyages.dat";

	/**
	 * The schema file of voyages provided by StatTransfer. This is just the
	 * file name. When importing it is prepended by the import directory.
	 */
	public static final String VOYAGES_STS = "voyages.sts";

	/**
	 * The name of the voyages file after row numbering. This is just the file
	 * name. When importing it is prepended by the import directory.
	 */
	public static final String VOYAGES_NUMBERED_DAT = "voyages-numbered.dat";

	/**
	 * The name of the voyages file after sorting. This is just the file name.
	 * When importing it is prepended by the import directory.
	 */
	public static final String VOYAGES_SORTED_DAT = "voyages-sorted.dat";

	/**
	 * The name under which the uploaded SPSS file with slaves is saved. User by
	 * {@link ImportServletImpo}. This is just the file name. When importing it
	 * is prepended by the import directory.
	 */
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

	private class IdIsEmptyException extends Exception
	{
		private static final long serialVersionUID = 4076984197898103476L;
	}

	/**
	 * Calls StatTransfer to convert the imported files from the SPPS format to
	 * a fixed column format with a schema description (sts file).
	 * 
	 * @throws IOException
	 * @throws StatTransferException
	 * @throws InterruptedException
	 */
	private void convertSpssFiles() throws IOException, StatTransferException, InterruptedException
	{

		Configuration conf = AppConfig.getConfiguration();
		String statTransferExe = conf.getString(AppConfig.IMPORT_STATTRANSFER);

		// convert voyages
		if (voyagesPresent)
		{
			log.logInfo("Converting voyages by StatTransfer.");
			StatTransfer voyagesST = new StatTransfer(statTransferExe);
			voyagesST.setInputFileType("spss");
			voyagesST.setInputFileName(voyagesSpssFileName);
			voyagesST.setOutputFileType("stfixed");
			voyagesST.setOutputFileName(voyagesSchemaFileName);
			voyagesST.transfer();
			log.logInfo("Voyages successfully converted.");
		}

		// convert slaves
		if (slavesPresent)
		{
			log.logInfo("Converting slaves by StatTransfer.");
			StatTransfer slavesST = new StatTransfer(statTransferExe);
			slavesST.setInputFileType("spss");
			slavesST.setInputFileName(slavesSpssFileName);
			slavesST.setOutputFileType("stfixed");
			slavesST.setOutputFileName(slavesSchemaFileName);
			slavesST.transfer();
			log.logInfo("Slaves successfully converted.");
		}

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
	private boolean matchAndVerifySchema(int recordType)
	{

		Attribute attributes[] = null;
		Map schema = null;
		String errorMsgDataFile = null;

		boolean ok = true;

		if (recordType == VOYAGE)
		{
			attributes = voyageAttributes;
			schema = voyageSchema;
			errorMsgDataFile = "voyages";
		}
		else if (recordType == SLAVE)
		{
			attributes = slaveAttributes;
			schema = slaveSchema;
			errorMsgDataFile = "slaves";
		}
		else
		{
			return false;
		}

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
				STSchemaVariable varDay = (STSchemaVariable) schema.get(col.getImportDateDay());
				STSchemaVariable varMonth = (STSchemaVariable) schema.get(col.getImportDateMonth());
				STSchemaVariable varYear = (STSchemaVariable) schema.get(col.getImportDateYear());
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
						&& var.getLength() > col.getLength().intValue())
				{
					ok = false;
					log.logError("String too long for database field "
							+ col.getName() + " " + "in " + errorMsgDataFile
							+ "." + "Expected length = " + col.getLength()
							+ ", " + "Variable length = " + var.getLength()
							+ ".");
				}
			}

		}

		return ok;

	}

	/**
	 * Compares the database schema of the two imported files with the imported
	 * schema. For each of the two data files calls
	 * {@link #matchAndVerifySchema(int)} with the appropriate value
	 * {@link #VOYAGE} or {@link #SLAVE}.
	 * 
	 * @throws DataSchemaMissmatchException
	 */
	private void matchAndVerifySchema() throws DataSchemaMissmatchException
	{

		if (voyagesPresent)
		{
			log.logInfo("Matching data schema of voyages.");
			if (!matchAndVerifySchema(VOYAGE)) throw new DataSchemaMissmatchException();
			log.logInfo("Data schema is OK.");
		}

		if (slavesPresent)
		{
			log.logInfo("Matching data schema of slaves.");
			if (!matchAndVerifySchema(SLAVE)) throw new DataSchemaMissmatchException();
			log.logInfo("Data schema is OK.");
		}

	}

	/**
	 * Add a column to the given imported file indicating the row number and
	 * saves the resulting file under {@link #voyagesNumberedDataFileName} or
	 * {@link #slavesNumberedDataFileName}.
	 * 
	 * @param type
	 *            The file to sort indicated by {@link #VOYAGE} or
	 *            {@link #SLAVE}.
	 * @throws IOException
	 */
	private void addLineNumbers(int type) throws IOException
	{

		RecordIOFactory recordIO = null;
		String inputFile = null;
		String outputFile = null;

		if (type == VOYAGE)
		{
			recordIO = voyagesRecordIOFactory;
			inputFile = voyagesDataFileName;
			outputFile = voyagesNumberedDataFileName;
		}
		else if (type == SLAVE)
		{
			recordIO = slavesRecordIOFactory;
			inputFile = slavesDataFileName;
			outputFile = slavesNumberedDataFileName;
		}
		else
		{
			return;
		}

		RecordReader rdr = recordIO.createReader(inputFile);
		RecordWriter writer = recordIO.createWriter(outputFile);

		Record record = null;
		int i = 1;
		while ((record = rdr.readRecord()) != null)
		{
			writer.write(record.getLine());
			writer.write(String.valueOf(i), MAX_INT_LENGTH);
			writer.finishRecord();
			i++;
		}

		int currRecordColumns = recordIO.getColumnsCount();
		recordIO.setColumnsCount(currRecordColumns + MAX_INT_LENGTH);

		STSchemaVariable lineNoVar = new STSchemaVariable();
		lineNoVar.setName("lineno");
		lineNoVar.setType(STSchemaVariable.TYPE_NUMERIC);
		lineNoVar.setStartColumn(currRecordColumns + 1);
		lineNoVar.setEndColumn(currRecordColumns + MAX_INT_LENGTH);

		if (type == VOYAGE) voyagesLineNoVar = lineNoVar;
		else if (type == SLAVE) slavesLineNoVar = lineNoVar;

	}

	/**
	 * Add line numbers to the imported files (i.e. the files named by
	 * {@link #voyagesDataFileName} and {@link #slavesDataFileName}). Only
	 * calls {@link #addLineNumbers(int)} with the appropriate value of either
	 * {@link #VOYAGE} or {@link #SLAVE}.
	 * 
	 * @throws IOException
	 */
	private void addLineNumbers() throws IOException
	{

		if (voyagesPresent)
		{
			log.logInfo("Numbering voyages records.");
			addLineNumbers(VOYAGE);
			log.logInfo("Voyages numbered.");
		}

		if (slavesPresent)
		{
			log.logInfo("Numbering slaves records.");
			addLineNumbers(SLAVE);
			log.logInfo("Slaves numbered.");
		}

	}
	
	/**
	 * Sorts the imported data files after row numbering (i.e. files given by
	 * the names {@link #voyagesNumberedDataFileName} and
	 * {@link #slavesNumberedDataFileName}) by the voyage ID. Uses
	 * {@link RecordSorter} to do it. Saves the imported files under
	 * {@link #voyagesSortedDataFileName} and {@link #slavesSortedDataFileName}.
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private void sortFiles() throws FileNotFoundException, IOException
	{

		if (voyagesPresent)
		{
			log.logInfo("Sorting voyages by voyage ID.");
			RecordSorter voyagesSorter = new RecordSorter(
					voyagesNumberedDataFileName,
					voyagesSortedDataFileName,
					voyagesRecordIOFactory);
			voyagesSorter.setTmpFolder(importDir);
			voyagesSorter.setMaxLines(1000);
			voyagesSorter.sort();
			log.logInfo("Voyages sorted.");
		}

		if (slavesPresent)
		{
			log.logInfo("Sorting slaves by voyage ID.");
			RecordSorter slavesSorter = new RecordSorter(
					slavesNumberedDataFileName,
					slavesSortedDataFileName,
					slavesRecordIOFactory);
			slavesSorter.setTmpFolder(importDir);
			slavesSorter.setMaxLines(5000);
			slavesSorter.sort();
			log.logInfo("Slaves sorted.");
		}

	}

	/**
	 * Updates the dictionary of a given attribute by the values in a given
	 * imported variable.
	 * 
	 * @param attr
	 *            Database attribute of a voyage or slave.
	 * @param var
	 *            Imported schema variable containing possibly a new or updates
	 *            list of values.
	 */
	private void updateDictionary(Attribute attr, STSchemaVariable var)
	{
		for (Iterator iterLabel = var.getLabels().iterator(); iterLabel.hasNext();)
		{

			STSchemaVariableLabel label = (STSchemaVariableLabel) iterLabel.next();

			Dictionary[] dicts = Dictionary.loadDictionaryByRemoteId(
					attr.getDictionary(),
					new Integer(label.getKey()));

			Dictionary dict;
			boolean newDict = dicts.length == 0;
			if (newDict)
				dict = Dictionary.createNew(attr.getDictionary());
			else
				dict = dicts[0];

			dict.setName(label.getLabel());
			dict.setRemoteId(new Integer(label.getKey()));

			if (newDict)
				dict.save();
			else
				dict.update();

		}
	}

	/**
	 * Scans the existing attributes of the given object (voyage/slave) and for
	 * those attributes which are dictionaries, finds the correspoding variable
	 * in the imported schema and then calls
	 * {@link #updateDictionary(Attribute, STSchemaVariable)} to really update
	 * the dictionary.
	 * 
	 * @param recordType
	 *            Voyage or slave determined by constants {@link #VOYAGE} and
	 *            {@link #SLAVE}.
	 */
	private void updateDictionaties(int recordType)
	{

		Attribute attributes[] = null;
		Map schema = null;

		if (recordType == VOYAGE)
		{
			attributes = voyageAttributes;
			schema = voyageSchema;
		}
		else if (recordType == SLAVE)
		{
			attributes = slaveAttributes;
			schema = slaveSchema;
		}
		else
		{
			return;
		}

		for (int i = 0; i < attributes.length; i++)
		{
			Attribute attr = attributes[i];
			if (attr.getType().intValue() == Attribute.TYPE_DICT)
			{
				// System.out.println("Updating: " + col.getName() + ", " +
				// col.getDictinaory());
				STSchemaVariable var = (STSchemaVariable) schema.get(attr.getImportName());
				updateDictionary(attr, var);
			}
		}

	}

	/**
	 * Called by {@link #importData()}. Updates the variable labes from the
	 * SPSS files. This method itfelf does not do much - only calles
	 * {@link #updateDictionaties(int)} for voyages and slaves.
	 */
	private void updateDictionaties()
	{

		if (voyagesPresent)
		{
			log.logInfo("Updating labels from voyages data file.");
			updateDictionaties(VOYAGE);
			log.logInfo("Labels updated.");
		}

		if (slavesPresent)
		{
			log.logInfo("Updating labels from slaves data file.");
			updateDictionaties(SLAVE);
			log.logInfo("Labels updated.");
		}

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
			Attribute col = attributes[i];
			int type = col.getType().intValue();
			int importType = col.getImportType().intValue();

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
			if (type == Attribute.IMPORT_TYPE_IGNORE) continue;

			try
			{

				// import numeric or string field
				if (importType == Attribute.IMPORT_TYPE_NUMERIC
						|| importType == Attribute.IMPORT_TYPE_STRING)
				{
					var = (STSchemaVariable) schema.get(col.getImportName());
					columnValue = record.getValue(var);
					parsedValue = col.parse(columnValue);
				}

				// import date
				else if (importType == Attribute.IMPORT_TYPE_DATE)
				{
					varDay = (STSchemaVariable) schema.get(col
							.getImportDateDay());
					varMonth = (STSchemaVariable) schema.get(col
							.getImportDateMonth());
					varYear = (STSchemaVariable) schema.get(col
							.getImportDateYear());
					columnDayValue = record.getValue(varDay);
					columnMonthValue = record.getValue(varMonth);
					columnYearValue = record.getValue(varYear);
					parsedValue = col.parse(new String[] { columnDayValue,
							columnMonthValue, columnYearValue });
				}

				// nonexisting dictionary value was inserted
				if (type == Attribute.TYPE_DICT && parsedValue != null
						&& ((Dictionary) parsedValue).getId() == null) log
						.logWarn("Nonexistent label '"
								+ ((Dictionary) parsedValue).getName() + "' "
								+ "inserted for variable "
								+ col.getImportName() + " " + "in "
								+ errorMsgDataFile + " " + "in record "
								+ recordNo + ".");

				// update the object
				obj.setAttrValue(col.getName(), parsedValue);

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

	/**
	 * Called by {@link #importData()}. Updates the voyage which was loaded
	 * from the database or created (if it did not exist) by the data from
	 * imported file.
	 * 
	 * @param voyage
	 *            The loaded or created voyage.
	 * @param voyageRecord
	 *            The curreny row from the imported file.
	 */
	private void updateVoyage(Voyage voyage, Record voyageRecord)
	{
		boolean valid = updateValues(voyage, voyageRecord);
		if (!valid) noOfInvalidVoyages++;
	}

	/**
	 * Called by {@link #importData()}. Updates the already existing slaves of
	 * the given voyage.
	 * 
	 * @param voyage
	 *            The currently processed voyage.
	 * @param slaves
	 *            The set of slaves from the imported data.
	 */
	private void removeDeletedSlaves(Voyage voyage, Map slaves)
	{
		for (Iterator iterSlave = voyage.getSlaves().iterator(); iterSlave.hasNext();)
		{
			Slave slave = (Slave) iterSlave.next();
			if (!slaves.containsKey(slave.getIid()))
			{
				noOfDeletedSlaves++;
				iterSlave.remove();
			}
		}
	}

	/**
	 * Called by {@link #importData()}. Updates the already existing slaves of
	 * the given voyage.
	 * 
	 * @param voyage
	 *            The currently processed voyage.
	 * @param slaves
	 *            The set of slaves from the imported data.
	 */
	private void updateExistingSlaves(Voyage voyage, Map slaves)
	{
		for (Iterator iterSlave = voyage.getSlaves().iterator(); iterSlave.hasNext();)
		{
			Slave slave = (Slave) iterSlave.next();
			Record slaveRecord = (Record) slaves.get(slave.getIid());
			if (slaveRecord != null)
			{
				noOfUpdatedSlaves++;
				boolean valid = updateValues(slave, slaveRecord);
				if (!valid) noOfInvalidSlaves++;
			}
		}
	}

	/**
	 * Called by {@link #importData()}.. Determines which slaves are new to the given
	 * voyages and adds them to the voyages.
	 * 
	 * @param voyage
	 *            The currently processed voyage.
	 * @param slaves
	 *            The set of slaves from the imported data.
	 */
	private void addNewSlaves(Voyage voyage, Map slaves)
	{
		for (Iterator iterSlave = slaves.entrySet().iterator(); iterSlave.hasNext();)
		{
			Record slaveRecord = (Record) iterSlave.next();
			Long slaveId = new Long(slaveRecord.getKey().trim());
			Slave slave = voyage.getSlave(slaveId);
			if (slave == null)
			{
				noOfCreatedSlaves++;
				slave = Slave.createNew(slaveId);
				voyage.addSlave(slave);
				boolean valid = updateValues(slave, slaveRecord);
				if (!valid) noOfInvalidSlaves++;
			}
		}
	}

	/**
	 * The heart of the import. This is the last stage of the import. This
	 * functions scans simultaneously the two given files (voyages and slaves),
	 * if they are provided, or just one, if only one is provided. At one step
	 * it retrives the next row from the voyages and also the set of next rows
	 * from slaves sharing the same voyage ID. Then, if the voyage ID of the
	 * current voyage row is the same of the voyage ID of the set of slaves, the
	 * current voyages and the corresponding slaves are saved to the database.
	 * If the voyage ID is strictly smaller than the voyage ID of teh set of
	 * slaves, only the voyage is saved and the already existing slaves of the
	 * voyages are not modified. If the voyage ID of the slaves if strictly
	 * smaller than the voyage ID of the current voyage, the voyage is loaded
	 * from the database (or created is it does not exist) and the slaves are
	 * updated.
	 * 
	 * Also, it skips the voyages and slaves which do not contain a valid voyage
	 * ID or slave ID. Typically, this happens when the ID is empty.
	 * 
	 * @throws IOException
	 */
	private void importData() throws IOException
	{

		// open both files
		RecordReader voyagesRdr = voyagesPresent ? voyagesRecordIOFactory
				.createReader(voyagesSortedDataFileName) : null;
		RecordReader slavesRdr = slavesPresent ? slavesRecordIOFactory
				.createReader(slavesSortedDataFileName) : null;

		// variables for the main loop (see below)
		Record voyageRecord = null;
		Record slaveRecord = null;
		Long slaveId = null;
		long voyageVid = 0;
		long slavesVid = 0;
		long mainVid = 0;
		Map slaves = new HashMap();
		boolean saveAndReadVoyage = false;
		boolean saveAndReadSlaves = false;

		// cache
		Voyage[] voyagesCache = null;
		int cachePos = 0;

		// init stats
		totalNoOfVoyages = 0;
		noOfVoyagesWithInvalidVid = 0;
		totalNoOfSlaves = 0;
		noOfSlavesWithInvalidVid = 0;
		noOfSlavesWithInvalidSid = 0;
		noOfVoyagesWithSlaves = 0;
		noOfInvalidVoyages = 0;
		noOfDeletedSlaves = 0;
		noOfInvalidSlaves = 0;
		noOfUpdatedSlaves = 0;
		noOfCreatedSlaves = 0;
		noOfUpdatedVoyages = 0;
		noOfCreatedVoyages = 0;

		// slave id
		STSchemaVariable varSlaveId = slavesPresent ? (STSchemaVariable) slaveSchema
				.get("slaveid")
				: null;

		// we have only voyages -> make sure that in the main loop
		// we always read and save only voyages
		if (voyagesPresent && !slavesPresent)
		{
			saveAndReadVoyage = true;
			saveAndReadSlaves = false;
		}

		// we have only slaves -> make sure that in the main loop
		// we always read and save only slaves
		else if (voyagesPresent && !slavesPresent)
		{
			saveAndReadVoyage = false;
			saveAndReadSlaves = true;
		}

		// we have both files
		else
		{
			saveAndReadVoyage = true;
			saveAndReadSlaves = true;
		}

		// main loop
		while (saveAndReadVoyage || saveAndReadSlaves)
		{

			// read the next voyage
			// outcome: voyageVals
			// if voyageVals == null -> there is no more voyages
			if (saveAndReadVoyage)
			{
				voyageVid = 0;
				while ((voyageRecord = voyagesRdr.readRecord()) != null)
				{
					totalNoOfVoyages++;
					int lineNo = Integer.parseInt(voyageRecord
							.getValue(voyagesLineNoVar));
					try
					{
						String id = voyageRecord.getKey().trim();
						if (id.length() == 0) throw new IdIsEmptyException();
						voyageVid = Long.parseLong(id);
						break;
					}
					catch (IdIsEmptyException iiee)
					{
						noOfVoyagesWithInvalidVid++;
						log.logWarn("Voyage in line " + lineNo + " "
								+ "is missing the voyage ID. Skipping.");
					}
					catch (NumberFormatException nfe)
					{
						noOfVoyagesWithInvalidVid++;
						log.logWarn("Invalid voyage ID " + "("
								+ voyageRecord.getKey().trim() + ") "
								+ "in line " + lineNo + ". Skipping.");
						continue;
					}
				}
			}

			// read the next set of slaves with the same vid
			// outcome: ArrayList slaves;
			// if slaves.count() == 0 -> there is no more slaves
			if (saveAndReadSlaves)
			{
				slaves.clear();
				slavesVid = 0;
				if (slaveRecord != null)
				{
					slavesVid = Long.parseLong(slaveRecord.getKey().trim());
					slaves.put(slaveId, slaveRecord);
				}
				while ((slaveRecord = slavesRdr.readRecord()) != null)
				{
					totalNoOfSlaves++;
					long currSlaveVid = 0;
					long currSlaveSid = 0;
					int lineNo = Integer.parseInt(slaveRecord
							.getValue(slavesLineNoVar));

					try
					{
						String id = voyageRecord.getKey().trim();
						if (id.length() == 0) throw new IdIsEmptyException();
						currSlaveVid = Long.parseLong(id);
					}
					catch (IdIsEmptyException iiee)
					{
						noOfSlavesWithInvalidVid++;
						log.logWarn("Slave in line " + lineNo + " "
								+ "is missing the slave ID. Skipping.");
						continue;
					}
					catch (NumberFormatException nfe)
					{
						noOfSlavesWithInvalidVid++;
						log.logWarn("Invalid slave ID " + "("
								+ slaveRecord.getKey().trim() + ") "
								+ "in line " + lineNo + ". Skipping.");
						continue;
					}

					try
					{
						String id = slaveRecord.getValue(varSlaveId);
						if (id.length() == 0) throw new IdIsEmptyException();
						currSlaveSid = Long.parseLong(id);
					}
					catch (IdIsEmptyException iiee)
					{
						noOfSlavesWithInvalidVid++;
						log.logWarn("Slave in line " + totalNoOfSlaves + " "
								+ "is missing the voyage ID. Skipping.");
						continue;
					}
					catch (NumberFormatException nfe)
					{
						noOfSlavesWithInvalidSid++;
						log.logWarn("Invalid slave ID " + "("
								+ slaveRecord.getValue(varSlaveId) + ") "
								+ "in line " + totalNoOfSlaves + ". Skipping.");
						continue;
					}

					if (slavesVid == 0)
					{
						slavesVid = currSlaveVid;
						slaves.put(new Long(currSlaveSid), slaveRecord);
					}
					else if (slavesVid == currSlaveVid)
					{
						slaves.put(new Long(currSlaveSid), slaveRecord);
					}
					else
					{
						break;
					}

				}
			}

			// ok, we have read a voyage (is there is any)
			// and a sequence of slaves with the same vid
			// (is there were anymore slaves), so for
			// clarity, let's remeber this info in flags
			boolean haveVoyage = voyageVid != 0;
			boolean haveSlaves = slavesVid != 0;

			// end of voyages and slaves
			if (!haveVoyage && !haveSlaves)
			{
				saveAndReadVoyage = false;
				saveAndReadSlaves = false;
			}

			// end of voyages
			else if (!haveVoyage)
			{
				saveAndReadVoyage = false;
			}

			// end of slaves
			else if (!haveSlaves)
			{
				saveAndReadSlaves = false;
			}

			// we have both still
			else
			{
				if (voyageVid == slavesVid)
				{
					saveAndReadVoyage = true;
					saveAndReadSlaves = true;
				}
				else if (voyageVid < slavesVid)
				{
					saveAndReadVoyage = true;
					saveAndReadSlaves = false;
				}
				else
				{
					saveAndReadVoyage = false;
					saveAndReadSlaves = true;
				}
			}

			// save
			if (saveAndReadVoyage || saveAndReadSlaves)
			{

				// determine VID
				mainVid = saveAndReadVoyage ? voyageVid : slavesVid;

				// load voyage from db or create a new one
				Voyage voyage = null;
				while (voyage == null)
				{
					if (voyagesCache == null || cachePos == voyagesCache.length)
					{
						voyagesCache = Voyage.loadMostRecent(new Long(mainVid),
								VOYAGE_CACHE_SIZE);
						cachePos = 0;
					}
					if (voyagesCache.length > 0)
					{
						for (; cachePos < voyagesCache.length; cachePos++)
						{
							if (voyagesCache[cachePos].getVoyageId().intValue() == mainVid)
							{
								voyage = voyagesCache[cachePos];
								break;
							}
							if (voyagesCache[cachePos].getVoyageId().intValue() > mainVid)
							{
								noOfUpdatedVoyages++;
								voyage = Voyage.createNew(new Long(mainVid));
								break;
							}
						}
					}
					else
					{
						noOfCreatedVoyages++;
						voyage = Voyage.createNew(new Long(mainVid));
					}
				}

				// update voyage
				if (saveAndReadVoyage)
				{
					updateVoyage(voyage, voyageRecord);
				}

				// update slaves
				if (saveAndReadSlaves)
				{
					removeDeletedSlaves(voyage, slaves);
					updateExistingSlaves(voyage, slaves);
					addNewSlaves(voyage, slaves);
				}

				// save
				voyage.save();

				// basic stat
				if (saveAndReadVoyage && saveAndReadSlaves) noOfVoyagesWithSlaves++;

				// debug
				// System.out.print("VID = " + mainVid);
				// if (saveVoyage)
				// {
				// System.out.print(", voyage = yes");
				// System.out.print(", shipname = " + new
				// String(voyageRecord.getLine(),200,58));
				// }
				// if (saveSlaves)
				// {
				// System.out.print(", slaves = yes");
				// System.out.print(", number of slaves = " + slaves.size());
				// }
				// System.out.println();

			}

		}

		// close all
		if (voyagesPresent) voyagesRdr.close();
		if (slavesPresent) slavesRdr.close();

	}

	/**
	 * Main method of the import. Does not do much itself. Just initialized the
	 * global parameters for the import (mostly various file names) and then
	 * process each phase of the import one by one.
	 * 
	 * @param log
	 *            The import log. It is passed as a parameter to import in order
	 *            to possibly allow the called to add some things to the log.
	 * @param importDir
	 *            The name of the directory of the import (just the name, not
	 *            the full path).
	 */
	public void runImport(LogWriter log, String importDir)
	{

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

			log.startStage(LogItem.STAGE_CONVERSION);
			convertSpssFiles();

			log.startStage(LogItem.STAGE_SCHEMA_LOADING);
			loadSchemas();

			log.startStage(LogItem.STAGE_SCHEMA_MATCHING);
			matchAndVerifySchema();

			log.startStage(LogItem.STAGE_NUMBERING);
			addLineNumbers();

			log.startStage(LogItem.STAGE_SORTING);
			sortFiles();

			log.startStage(LogItem.STAGE_UPDATING_LABELS);
			updateDictionaties();

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
		catch (StatTransferException ste)
		{
			log.logError("The uploaded file is not proabably a valid SPSS file.");
			log.logError(ste.getMessage());
			log.logInfo("Import terminated.");
			log.doneWithErrors();
		}
		catch (InterruptedException ie)
		{
			log.logError(ie.getMessage());
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
		String fullImportDir = conf.getString(AppConfig.IMPORT_ROOTDIR)
				+ File.separatorChar + importDir;
		LogWriter log = new LogWriter(fullImportDir);

		// import
		Import imp = new Import();
		imp.runImport(log, fullImportDir);
		log.close();

	}

}
package edu.emory.library.tas.spss;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import edu.emory.library.tas.AbstractDescriptiveObject;
import edu.emory.library.tas.Dictionary;
import edu.emory.library.tas.InvalidDateException;
import edu.emory.library.tas.InvalidNumberException;
import edu.emory.library.tas.InvalidNumberOfValuesException;
import edu.emory.library.tas.SchemaColumn;
import edu.emory.library.tas.Slave;
import edu.emory.library.tas.StringTooLongException;
import edu.emory.library.tas.Voyage;
import edu.emory.library.tas.attrGroups.AbstractAttribute;
import edu.emory.library.tas.attrGroups.Attribute;

public class Import
{
	
	private static final int MAX_INT_LENGTH = 10;
	private static final int VOYAGE_CACHE_SIZE = 100;
	private static final int VOYAGE = 0;
	private static final int SLAVE = 1;

	// files, readers and schemas
	private String voyagesSpssFileName;
	private String slavesSpssFileName;
	private String voyagesDataFileName;
	private String voyagesSortedDataFileName;
	private String voyagesNumberedDataFileName;
	private String slavesDataFileName;
	private String slavesSortedDataFileName;
	private String slavesNumberedDataFileName;
	private boolean voyagesPresent;
	private boolean slavesPresent;
	STSchemaVariable voyagesVidVar;
	STSchemaVariable slavesVidVar;
	RecordIOFactory voyagesRecordIOFactory;
	RecordIOFactory slavesRecordIOFactory;
	private String workingDir;
	private LogWriter log;
	private String exeStatTransfer;

	// schemas
	private String voyagesSchemaFileName;
	private String slavesSchemaFileName;
	private Hashtable voyageSchema;
	private Hashtable slaveSchema;
	private Attribute voyageAttributes[] = Voyage.getAttributes();
	private Attribute slaveAttributes[] = Slave.getAttributes();
	private STSchemaVariable voyagesLineNoVar = null;
	private STSchemaVariable slavesLineNoVar = null;
	
	// statistics
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
	
	private class IdIsEmptyException extends Exception
	{
		private static final long serialVersionUID = 4076984197898103476L;
	}

	private void convertSpssFiles() throws IOException, StatTransferException, InterruptedException
	{

		// convert voyages
		if (voyagesSpssFileName != null)
		{
			log.logInfo("Converting voyages by StatTransfer.");
			StatTransfer voyagesST = new StatTransfer(exeStatTransfer);
			voyagesST.setInputFileType("spss");
			voyagesST.setInputFileName(voyagesSpssFileName);
			voyagesST.setOutputFileType("stfixed");
			voyagesST.setOutputFileName(voyagesSchemaFileName);
			voyagesST.transfer();
			log.logInfo("Voyages successfully converted.");
		}
		
		// convert slaves
		if (slavesSpssFileName != null)
		{
			log.logInfo("Converting slaves by StatTransfer.");
			StatTransfer slavesST = new StatTransfer(exeStatTransfer);
			slavesST.setInputFileType("spss");
			slavesST.setInputFileName(slavesSpssFileName);
			slavesST.setOutputFileType("stfixed");
			slavesST.setOutputFileName(slavesSchemaFileName);
			slavesST.transfer();
			log.logInfo("Slaves successfully converted.");
		}

	}
	
	private int calculateSchemaWidth(Hashtable schema)
	{
		int width = 0;
		String[] names = (String[])schema.keySet().toArray(new String[] {});
		for (int i=0; i<names.length; i++)
		{
			STSchemaVariable var = (STSchemaVariable) schema.get(names[i]);
			width += var.getLength();
		}
		return width;
	}

	private void loadSchemas() throws IOException, STSchemaException
	{

		STSchemaReader rdr = new STSchemaReader();

		if (voyagesPresent)
		{
			log.logInfo("Reading data schema of voyages.");
			
			// read schemas
			voyageSchema = rdr.readSchema(voyagesSchemaFileName);
			
			// locate vid and create record io factory for voyages
			voyagesVidVar = (STSchemaVariable)voyageSchema.get("voyageid"); 
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
			slavesVidVar = (STSchemaVariable)slaveSchema.get("voyageid");
			int slavesSchemaWidth = calculateSchemaWidth(slaveSchema); 
			slavesRecordIOFactory = new RecordIOFactory(
				slavesVidVar.getStartColumn(),
				slavesVidVar.getEndColumn(),
				slavesSchemaWidth);
			
			log.logInfo("Data schema successfully read.");
		}

	}
	
	private boolean compareTypes(int colType, int varType)
	{
		return
			(colType == Attribute.IMPORT_TYPE_NUMERIC && varType == STSchemaVariable.TYPE_NUMERIC) ||
			(colType == Attribute.IMPORT_TYPE_STRING && varType == STSchemaVariable.TYPE_STRING);
	}
	
	private boolean matchAndVerifySchemaInt(int recordType)
	{
		
		Attribute attributes[] = null;
		Hashtable schema = null;
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
		
		for (int i=0; i<attributes.length; i++)
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
					log.logError(
							"Missing variable for database field " + col.getName() + " " +
							"of type date " +
							"in " + errorMsgDataFile + ".");

				}
				else if (varDay.getType() != STSchemaVariable.TYPE_NUMERIC || varMonth.getType() != STSchemaVariable.TYPE_NUMERIC || varYear.getType() != STSchemaVariable.TYPE_NUMERIC)
				{
					ok = false;
					log.logError(
							"Type missmatch for database field " + col.getName() + " " +
							"of type date " +
							"in " + errorMsgDataFile + ".");
				}
			}

			// number or string
			else
			{
				STSchemaVariable var = (STSchemaVariable) schema.get(col.getImportName()); 
				if (var == null)
				{
					ok = false;
					log.logError(
							"Missing field for database field " + col.getName() + " " +
							"in " + errorMsgDataFile + ".");
				}
				else if (!compareTypes(type, var.getType()))
				{
					ok = false;
					log.logError(
							"Type missmatch for database field " + col.getName() + " " +
							"in " + errorMsgDataFile + ".");
				}
				else if (type == Attribute.IMPORT_TYPE_STRING && var.getLength() > col.getLength().intValue())
				{
					ok = false;
					log.logError(
							"String too long for database field " + col.getName() + " " +
							"in " + errorMsgDataFile + "." +
							"Expected length = " + col.getLength() + ", " +
							"Variable length = " + var.getLength() + ".");
				}
			}
		
		}
		
		return ok;
		
	}

	private void matchAndVerifySchema() throws DataSchemaMissmatchException
	{

		if (voyagesPresent)
		{
			log.logInfo("Matching data schema of voyages.");
			if (!matchAndVerifySchemaInt(VOYAGE)) throw new DataSchemaMissmatchException();
			log.logInfo("Data schema is OK.");
		}
		
		if (slavesPresent)
		{
			log.logInfo("Matching data schema of slaves.");
			if (!matchAndVerifySchemaInt(SLAVE)) throw new DataSchemaMissmatchException();
			log.logInfo("Data schema is OK.");
		}
		
	}
	
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

		if (type == VOYAGE)
			voyagesLineNoVar = lineNoVar;
		else if (type == SLAVE)
			slavesLineNoVar = lineNoVar;

	}
	
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

	private void sortFiles() throws FileNotFoundException, IOException
	{
		
		if (voyagesPresent)
		{
			log.logInfo("Sorting voyages by voyage ID.");
			RecordSorter voyagesSorter = new RecordSorter(voyagesNumberedDataFileName, voyagesSortedDataFileName, voyagesRecordIOFactory);
			voyagesSorter.setTmpFolder(workingDir);
			voyagesSorter.setMaxLines(1000);
			voyagesSorter.sort();
			log.logInfo("Voyages sorted.");
		}
		
		if (slavesPresent)
		{
			log.logInfo("Sorting slaves by voyage ID.");
			RecordSorter slavesSorter = new RecordSorter(slavesNumberedDataFileName, slavesSortedDataFileName, slavesRecordIOFactory);
			slavesSorter.setTmpFolder(workingDir);
			slavesSorter.setMaxLines(5000);
			slavesSorter.sort();
			log.logInfo("Slaves sorted.");
		}
		
	}
	
	private void updateDictionary(Attribute col, STSchemaVariable var)
	{
		for (Iterator iterLabel = var.getLabels().iterator(); iterLabel.hasNext();)
		{
			
			STSchemaVariableLabel label = (STSchemaVariableLabel) iterLabel.next();
			
			Dictionary[] dicts = Dictionary.loadDictionary(
					col.getDictionary(),
					new Integer(label.getKey()));

			Dictionary dict;
			boolean newDict = dicts.length == 0; 
			if (newDict)
				dict = Dictionary.createNew(col.getDictionary());
			else
				dict = dicts[0];
			
//			if (newDict)
//				System.out.println("Adding value: " + label.getKey() + " (" + label.getLabel() + ")");
//			else
//				System.out.println("Updating value: " + label.getKey() + " (" + label.getLabel() + ")");
			
			dict.setName(label.getLabel());
			dict.setRemoteId(new Integer(label.getKey()));
			
			if (newDict)
				dict.save();
			else
				dict.update();
			
		}
	}

	private void updateDictionaties(int recordType)
	{
		
		Attribute attributes[] = null;
		Hashtable schema = null;		
		
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
		
		for (int i=0; i<attributes.length; i++)
		{
			Attribute attr = attributes[i];
			if (attr.getType().intValue() == SchemaColumn.TYPE_DICT)
			{
				// System.out.println("Updating: " + col.getName() + ", " + col.getDictinaory());
				STSchemaVariable var = (STSchemaVariable) schema.get(attr.getImportName());
				updateDictionary(attr, var);
			}
		}
		
	}
	
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
	
	private boolean updateValues(AbstractDescriptiveObject obj, Record record)
	{
		
		Attribute attributes[] = null;
		Hashtable schema = null;
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
		for (int i=0; i<attributes.length; i++)
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
			if (type == Attribute.IMPORT_TYPE_IGNORE)
				continue;
			
			try
			{

				// import numeric or string field
				if (importType == Attribute.IMPORT_TYPE_NUMERIC || importType == Attribute.IMPORT_TYPE_STRING)
				{
					var = (STSchemaVariable) schema.get(col.getImportName());
					columnValue = record.getValue(var);
					parsedValue = col.parse(columnValue);
				}
				
				// import date
				else if (importType == Attribute.IMPORT_TYPE_DATE)
				{
					varDay = (STSchemaVariable) schema.get(col.getImportDateDay());
					varMonth = (STSchemaVariable) schema.get(col.getImportDateMonth());
					varYear = (STSchemaVariable) schema.get(col.getImportDateYear());
					columnDayValue = record.getValue(varDay);
					columnMonthValue = record.getValue(varMonth);
					columnYearValue = record.getValue(varYear);
					parsedValue = col.parse(new String[]{columnDayValue, columnMonthValue, columnYearValue});
				}
				
				// nonexisting dictionary value was inserted
				if (type == AbstractAttribute.TYPE_DICT && parsedValue != null && ((Dictionary)parsedValue).getId() == null)
					log.logWarn(
							"Nonexistent label '" + ((Dictionary)parsedValue).getName() + "' " +
							"inserted for variable " + col.getImportName() + " " +
							"in " + errorMsgDataFile + " " +
							"in record " + recordNo + ".");
	
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
				log.logWarn(
						"Invalid numeric value '" + columnValue + "' " +
						"in variable " + var.getName() + " " +
						"in " + errorMsgDataFile + " " +
						"in record " + recordNo + ".");
			}
			catch (InvalidDateException ide)
			{
				valid = false;
				log.logWarn(
						"Invalid date " +
						"day = '" + columnDayValue + "' in variable " + varDay.getName() + ", " +
						"month = '" + columnMonthValue + "' in variable " + varMonth.getName() + ", " +
						"year = '" + columnYearValue + "' in variable " + varYear.getName() + " " +
						"in " + errorMsgDataFile + " " +
						"in record " + recordNo + ".");
			}
			catch (StringTooLongException stle)
			{
				valid = false;
				log.logWarn(
						"String '" + parsedValue + "' too long " +
						"in variable " + var.getName() + " " +
						"in " + errorMsgDataFile + " " +
						"in record " + recordNo + ".");
			}
			
		}
		
		return valid;

	}
	
	private void updateVoyage(Voyage voyage, Record voyageRecord)
	{
		boolean valid = updateValues(voyage, voyageRecord);
		if (!valid) noOfInvalidVoyages ++;
	}

	private void removeDeletedSlaves(Voyage voyage, Map slaves)
	{
		for (Iterator iterSlave = voyage.getSlaves().iterator(); iterSlave.hasNext();)
		{
			Slave slave = (Slave) iterSlave.next();
			if (!slaves.containsKey(slave.getIid()))
			{
				noOfDeletedSlaves ++;
				iterSlave.remove();
			}
		}
	}
	
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
				if (!valid) noOfInvalidSlaves ++;
			}
		}
	}

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
				if (!valid) noOfInvalidSlaves ++;
			}
		}
	}
	
	private void importData() throws IOException
	{
		
		// open both files
		RecordReader voyagesRdr = voyagesPresent ? voyagesRecordIOFactory.createReader(voyagesSortedDataFileName) : null;
		RecordReader slavesRdr = slavesPresent ? slavesRecordIOFactory.createReader(slavesSortedDataFileName) : null;
		
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
		STSchemaVariable varSlaveId = slavesPresent ? (STSchemaVariable)slaveSchema.get("slaveid") : null;
		
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
					int lineNo = Integer.parseInt(voyageRecord.getValue(voyagesLineNoVar));
					try
					{
						String id = voyageRecord.getKey().trim();
						if (id.length() == 0) throw new IdIsEmptyException();
						voyageVid = Long.parseLong(id);
						break;
					}
					catch (IdIsEmptyException iiee)
					{
						noOfVoyagesWithInvalidVid ++;
						log.logWarn(
							"Voyage in line " + lineNo + " " +
							"is missing the voyage ID. Skipping.");
					}
					catch (NumberFormatException nfe)
					{
						noOfVoyagesWithInvalidVid ++;
						log.logWarn(
							"Invalid voyage ID " + 
							"(" + voyageRecord.getKey().trim() + ") " +
							"in line " + lineNo + ". Skipping.");
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
					totalNoOfSlaves ++;
					long currSlaveVid = 0;
					long currSlaveSid = 0;
					int lineNo = Integer.parseInt(slaveRecord.getValue(slavesLineNoVar));

					try
					{
						String id = voyageRecord.getKey().trim();
						if (id.length() == 0) throw new IdIsEmptyException();
						currSlaveVid = Long.parseLong(id);
					}
					catch (IdIsEmptyException iiee)
					{
						noOfSlavesWithInvalidVid++;
						log.logWarn(
							"Slave in line " + lineNo + " " +
							"is missing the slave ID. Skipping.");
						continue;
					}
					catch (NumberFormatException nfe)
					{
						noOfSlavesWithInvalidVid++;
						log.logWarn(
								"Invalid slave ID " + 
								"(" + slaveRecord.getKey().trim() + ") " +
								"in line " + lineNo + ". Skipping.");
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
						log.logWarn(
							"Slave in line " + totalNoOfSlaves + " " +
							"is missing the voyage ID. Skipping.");
						continue;
					}
					catch (NumberFormatException nfe)
					{
						noOfSlavesWithInvalidSid++;
						log.logWarn(
								"Invalid slave ID " + 
								"(" + slaveRecord.getValue(varSlaveId) + ") " +
								"in line " + totalNoOfSlaves + ". Skipping.");
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
						voyagesCache = Voyage.loadMostRecent(new Long(mainVid), VOYAGE_CACHE_SIZE);
						cachePos = 0;
					}
					if (voyagesCache.length > 0)
					{
						for (; cachePos<voyagesCache.length; cachePos++)
						{
							if (voyagesCache[cachePos].getVoyageId().intValue() == mainVid)
							{
								voyage = voyagesCache[cachePos];
								break;
							}
							if (voyagesCache[cachePos].getVoyageId().intValue() > mainVid)
							{
								noOfUpdatedVoyages ++;
								voyage = Voyage.createNew(new Long(mainVid));
								break;
							}
						}
					}
					else
					{
						noOfCreatedVoyages ++;
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
				if (saveAndReadVoyage && saveAndReadSlaves)
					noOfVoyagesWithSlaves++;
				
				// debug
//				System.out.print("VID = " + mainVid);
//				if (saveVoyage)
//				{
//					System.out.print(", voyage = yes");
//					System.out.print(", shipname = " + new String(voyageRecord.getLine(),200,58));
//				}
//				if (saveSlaves)
//				{
//					System.out.print(", slaves = yes");
//					System.out.print(", number of slaves = " + slaves.size());
//				}
//				System.out.println();
			
			}
			
		}
		
		// close all
		if (voyagesPresent) voyagesRdr.close();
		if (slavesPresent) slavesRdr.close();

	}

	public void runImport(LogWriter log, String workingDir, String exeStatTransfer, String voyagesSpssFileName, String slavesSpssFileName)
	{
		
		this.log = log;
		this.exeStatTransfer = exeStatTransfer;
		this.workingDir = workingDir;
		this.voyagesSpssFileName = voyagesSpssFileName;
		this.slavesSpssFileName = slavesSpssFileName;
		this.voyagesPresent = voyagesSpssFileName!=null;
		this.slavesPresent = slavesSpssFileName!=null;
		
		voyagesDataFileName = workingDir + File.separatorChar + "voyages.dat";
		voyagesNumberedDataFileName = workingDir + File.separatorChar + "voyages-numbered.dat";
		voyagesSortedDataFileName = workingDir + File.separatorChar + "voyages-sorted.dat";
		voyagesSchemaFileName = workingDir + File.separatorChar + "voyages.sts";
		
		slavesDataFileName = workingDir + File.separatorChar + "slaves.dat";
		slavesSortedDataFileName = workingDir + File.separatorChar + "slaves-sorted.dat";
		slavesNumberedDataFileName = workingDir + File.separatorChar + "slaves-numbered.dat";
		slavesSchemaFileName = workingDir + File.separatorChar + "slaves.sts";
		
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
			log.logInfo("Number voyages with invalid ID = " + noOfVoyagesWithInvalidVid);
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

	public static void main(String[] args) throws FileNotFoundException, IOException
	{
		
		if (args.length != 5) return;
		
		// extracts params
		String workingDir = args[0];
		String exeStatTransfer = args[1];
		String voyagesFileName = args[2].equals("*") ? null : args[3];
		String slavesFileName = args[3].equals("*") ? null : args[4];
		
		// crate a log
		LogWriter log = new LogWriter(workingDir);
		
		// import
		Import imp = new Import();
		imp.runImport(log, workingDir, exeStatTransfer, voyagesFileName, slavesFileName); 
		log.close();
		
	}

}
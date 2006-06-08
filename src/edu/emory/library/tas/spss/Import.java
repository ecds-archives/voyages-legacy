package edu.emory.library.tas.spss;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

import edu.emory.library.tas.AbstractDescriptiveObject;
import edu.emory.library.tas.Dictionary;
import edu.emory.library.tas.InvalidDateException;
import edu.emory.library.tas.InvalidNumberException;
import edu.emory.library.tas.InvalidNumberOfValuesException;
import edu.emory.library.tas.SchemaColumn;
import edu.emory.library.tas.Slave;
import edu.emory.library.tas.StringTooLongException;
import edu.emory.library.tas.Voyage;

public class Import
{
	
	private static final int VOYAGE_CACHE_SIZE = 100;
	private static final int VOYAGE = 0;
	private static final int SLAVE = 1;

	// files, readers and schemas
	private String voyagesSpssFileName;
	private String slavesSpssFileName;
	private String voyagesDataFileName;
	private String voyagesSortedDataFileName;
	private String slavesDataFileName;
	private String slavesSortedDataFileName;
	private boolean voyagesPresent;
	private boolean slavesPresent;
	STSchemaVariable voyagesVidVar;
	STSchemaVariable slavesVidVar;
	RecordIOFactory voyagesRecordIOFactory;
	RecordIOFactory slavesRecordIOFactory;
	private String workingDir;

	// schemas
	private String voyagesSchemaFileName;
	private String slavesSchemaFileName;
	private Hashtable voyageSchema;
	private Hashtable slaveSchema;
	private String voyageDbSchemaNames[] = Voyage.getAllAttrNames();
	private String slaveDbSchemaNames[] = Slave.getAllAttrNames();
	
	// statistics
	private int totalNoOfVoyages;
	private int noOfValidVoyages;
	private int noOfVoyagesWithoutVid;
	private int totalNoOfSlaves;
	private int noOfValidSlaves;
	private int noOfSlavesWithoutVid;
	private int noOfSlavesWithoutSid;
	private int noOfVoyagesWithSlaves;
	
	private void convertSpssFiles() throws IOException
	{

		// convert voyages
		StatTransfer voyagesST = new StatTransfer();
		voyagesST.setInputFileType("spss");
		voyagesST.setInputFileName(voyagesSpssFileName);
		voyagesST.setOutputFileType("stfixed");
		voyagesST.setOutputFileName(voyagesSchemaFileName);
		voyagesST.transfer();
		
		// convert slaves
		StatTransfer slavesST = new StatTransfer();
		slavesST.setInputFileType("spss");
		slavesST.setInputFileName(slavesSpssFileName);
		slavesST.setOutputFileType("stfixed");
		slavesST.setOutputFileName(slavesSchemaFileName);
		slavesST.transfer();

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

		// read schemas
		STSchemaReader rdr = new STSchemaReader();
		voyageSchema = rdr.readSchema(voyagesSchemaFileName);
		slaveSchema = rdr.readSchema(slavesSchemaFileName);
		
		// locate vid and create record io factory for voyages
		voyagesVidVar = (STSchemaVariable)voyageSchema.get("voyageid"); 
		int voyagesSchemaWidth = calculateSchemaWidth(voyageSchema); 
		voyagesRecordIOFactory = new RecordIOFactory(
			voyagesVidVar.getStartColumn(),
			voyagesVidVar.getEndColumn(),
			voyagesSchemaWidth);
	
		// locate vid create record io factory for slaves
		slavesVidVar = (STSchemaVariable)slaveSchema.get("voyageid");
		int slavesSchemaWidth = calculateSchemaWidth(slaveSchema); 
		slavesRecordIOFactory = new RecordIOFactory(
			slavesVidVar.getStartColumn(),
			slavesVidVar.getEndColumn(),
			slavesSchemaWidth);

	}
	
	private SchemaColumn getSchemaColumn(int recordType, String name)
	{
		switch (recordType)
		{
			case VOYAGE: return Voyage.getSchemaColumn(name);
			case SLAVE: return Slave.getSchemaColumn(name);
			default: return null;
		}
	}
	
	private boolean compareTypes(int colType, int varType)
	{
		return
			(colType == SchemaColumn.IMPORT_TYPE_NUMERIC && varType == STSchemaVariable.TYPE_NUMERIC) ||
			(colType == SchemaColumn.IMPORT_TYPE_STRING && varType == STSchemaVariable.TYPE_STRING);
	}
	
	private void matchAndVerifySchemaInt(int recordType)
	{
		
		String dbSchemaNames[] = null;
		Hashtable schema = null;		
		
		if (recordType == VOYAGE)
		{
			dbSchemaNames = voyageDbSchemaNames;
			schema = voyageSchema;
		}
		else if (recordType == SLAVE)
		{
			dbSchemaNames = slaveDbSchemaNames;
			schema = slaveSchema;
		}
		else
		{
			return;
		}
		
		for (int i=0; i<dbSchemaNames.length; i++)
		{

			SchemaColumn col = getSchemaColumn(recordType, dbSchemaNames[i]);

			// ignoring
			if (col.getImportType() == SchemaColumn.IMPORT_TYPE_IGNORE)
			{
				continue;
			}
			
			// date
			else if (col.getImportType() == SchemaColumn.IMPORT_TYPE_DATE)
			{
				STSchemaVariable varDay = (STSchemaVariable) schema.get(col.getImportDateDay()); 
				STSchemaVariable varMonth = (STSchemaVariable) schema.get(col.getImportDateMonth()); 
				STSchemaVariable varYear = (STSchemaVariable) schema.get(col.getImportDateYear()); 
				if (varDay == null || varMonth == null || varYear == null)
				{
					System.out.println("missing field: " + col.getName());
					// error: missing field
				}
				
				else if (varDay.getType() != STSchemaVariable.TYPE_NUMERIC || varMonth.getType() != STSchemaVariable.TYPE_NUMERIC || varYear.getType() != STSchemaVariable.TYPE_NUMERIC)
				{
					System.out.println("type missmatch: " + col.getName());
					// error: type missmatch
				}
				else
				{
					//System.out.println("field " + col.getName() + " ok");
					// seems to be ok
				}
			}

			// number or string
			else
			{
				STSchemaVariable var = (STSchemaVariable) schema.get(col.getImportName()); 
				if (var == null)
				{
					System.out.println("missing field: " + col.getName());
					// error: missing field
				}
				else if (!compareTypes(col.getImportType(), var.getType()))
				{
					System.out.println("type missmatch: " + col.getName());
					// error: type missmatch
				}
				else if (col.getImportType() == SchemaColumn.IMPORT_TYPE_STRING && var.getLength() > col.getImportLength())
				{
					System.out.println("string too long: " + col.getName());
					// error: string too long
				}
				else
				{
					//System.out.println("field " + col.getName() + " ok");
					// seems to be ok
				}
			}
		
		}
		
	}

	private void matchAndVerifySchema()
	{
		matchAndVerifySchemaInt(VOYAGE);
		matchAndVerifySchemaInt(SLAVE);
	}

	private void sortFiles() throws FileNotFoundException, IOException
	{
		
		// sort voyages
		RecordSorter voyagesSorter = new RecordSorter(voyagesDataFileName, voyagesSortedDataFileName, voyagesRecordIOFactory);
		voyagesSorter.setTmpFolder(workingDir);
		voyagesSorter.setMaxLines(1000);
		voyagesSorter.sort();
		
		// sort slaves
		RecordSorter slavesSorter = new RecordSorter(slavesDataFileName, slavesSortedDataFileName, slavesRecordIOFactory);
		voyagesSorter.setTmpFolder(workingDir);
		slavesSorter.setMaxLines(5000);
		slavesSorter.sort();
		
	}
	
	private void updateDictionary(SchemaColumn col, STSchemaVariable var)
	{
		//System.out.println("Inside: " + col.getName() + "(" + var.getLabels().size() + ")");
		for (Iterator iterLabel = var.getLabels().iterator(); iterLabel.hasNext();)
		{
			
			STSchemaVariableLabel label = (STSchemaVariableLabel) iterLabel.next();
			
			Dictionary[] dicts = Dictionary.loadDictionary(
					col.getDictinaory(),
					new Integer(label.getKey()));

			Dictionary dict;
			boolean newDict = dicts.length == 0; 
			if (newDict)
				dict = Dictionary.createNew(col.getDictinaory());
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
		
		String dbSchemaNames[] = null;
		Hashtable schema = null;		
		
		if (recordType == VOYAGE)
		{
			dbSchemaNames = voyageDbSchemaNames;
			schema = voyageSchema;
		}
		else if (recordType == SLAVE)
		{
			dbSchemaNames = slaveDbSchemaNames;
			schema = slaveSchema;
		}
		else
		{
			return;
		}
		
		for (int i=0; i<dbSchemaNames.length; i++)
		{
			SchemaColumn col = getSchemaColumn(recordType, dbSchemaNames[i]);
			if (col.getType() == SchemaColumn.TYPE_DICT)
			{
				System.out.println("Updating: " + col.getName() + ", " + col.getDictinaory());
				STSchemaVariable var = (STSchemaVariable) schema.get(col.getImportName());
				updateDictionary(col, var);
			}
		}
		
	}
	
	private void updateDictionaties()
	{
		updateDictionaties(VOYAGE);
		updateDictionaties(SLAVE);
	}
	
	private boolean updateValues(AbstractDescriptiveObject obj, char[] line)
	{
		
		//System.out.println("Updating row ...");
		
		String dbSchemaNames[] = null;
		Hashtable schema = null;
		
		int recordType;
		if (obj instanceof Voyage)
		{
			recordType = VOYAGE;
			dbSchemaNames = voyageDbSchemaNames;
			schema = voyageSchema;
		}
		else if (obj instanceof Slave)
		{
			recordType = SLAVE;
			dbSchemaNames = slaveDbSchemaNames;
			schema = slaveSchema;
		}
		else
		{
			return false;
		}
		
		boolean valid = true;
		for (int i=0; i<dbSchemaNames.length; i++)
		{

			Object value = null;
			SchemaColumn col = getSchemaColumn(recordType, dbSchemaNames[i]);
			
			// we are ignoring this field for import
			if (col.getImportType() == SchemaColumn.IMPORT_TYPE_IGNORE)
				continue;
			
			try
			{

				// import numeric or string field
				if (col.getImportType() == SchemaColumn.IMPORT_TYPE_NUMERIC || col.getImportType() == SchemaColumn.IMPORT_TYPE_STRING)
				{
					STSchemaVariable var = (STSchemaVariable) schema.get(col.getImportName());
					value = col.parse(
							new String(line,
									var.getStartColumn()-1,
									var.getLength()).trim());
				}
				
				// import date
				else if (col.getImportType() == SchemaColumn.IMPORT_TYPE_DATE)
				{
					STSchemaVariable varDay = (STSchemaVariable) schema.get(col.getImportDateDay());
					STSchemaVariable varMonth = (STSchemaVariable) schema.get(col.getImportDateMonth());
					STSchemaVariable varYear = (STSchemaVariable) schema.get(col.getImportDateYear());
					value = col.parse(new String[]{
							new String(
									line,
									varDay.getStartColumn()-1,
									varDay.getLength()).trim(),
							new String(
									line,
									varMonth.getStartColumn()-1,
									varMonth.getLength()).trim(),
							new String(line,
									varYear.getStartColumn()-1,
									varYear.getLength()).trim()});
				}
				
				// nonexisting dictionary value was inserted
				if (col.getType() == SchemaColumn.TYPE_DICT && value != null && ((Dictionary)value).getId() == null)
					// to log: new dictionary value inserted
					;
	
				// update the object
				obj.setAttrValue(col.getName(), value);
			
			}
			catch (InvalidNumberOfValuesException inve)
			{
				// this cannot happen
				// see the code above
			}
			catch (InvalidNumberException ine)
			{
				// to log: col.getImportName() has an invalid numeric value
			}
			catch (InvalidDateException ide)
			{
				// to log: ... have an invalid date value
			}
			catch (StringTooLongException stle)
			{
				// to log: col.getImportName() containt too long string
			}
			
		}
		
		return valid;

	}
	
	private void updateVoyage(Voyage voyage, Record voyageRecord)
	{
		updateValues(voyage, voyageRecord.getLine());
	}

	private void removeDeletedSlaves(Voyage voyage, ArrayList slaves)
	{
		
		ArrayList toRemove = new ArrayList();
		
		for (Iterator iterSlave = voyage.getSlaves().iterator(); iterSlave.hasNext();)
		{
			Slave slave = (Slave) iterSlave.next();
			boolean slaveFound = false;
			for (int i=0; i<slaves.size(); i++)
			{
				Long slaveId = new Long(((Record)slaves.get(i)).getKey().trim());
				if (slave.getSlaveId().equals(slaveId))
				{
					slaveFound = true;
					break;
				}
			}
			if (!slaveFound)
			{
				toRemove.add(slave);
			}
		}
		
		for (Iterator iterSlave = toRemove.iterator(); iterSlave.hasNext();)
		{
			Slave slave = (Slave) iterSlave.next();
			voyage.removeSlave(slave);
		}
		
	}
	
	private void updateExistingSlaves(Voyage voyage, ArrayList slaves)
	{
		
		for (Iterator iterSlave = slaves.iterator(); iterSlave.hasNext();)
		{
			Record slaveRecord = (Record) iterSlave.next();
			Long slaveId = new Long(slaveRecord.getKey().trim());
			Slave slave = voyage.getSlave(slaveId);
			if (slave != null)
			{
				updateValues(slave, slaveRecord.getLine());
			}
		}
		
	}

	private void addNewSlaves(Voyage voyage, ArrayList slaves)
	{
		
		for (Iterator iterSlave = slaves.iterator(); iterSlave.hasNext();)
		{
			Record slaveRecord = (Record) iterSlave.next();
			Long slaveId = new Long(slaveRecord.getKey().trim());
			Slave slave = voyage.getSlave(slaveId);
			if (slave == null)
			{
				slave = Slave.createNew(slaveId);
				voyage.addSlave(slave);
				updateValues(slave, slaveRecord.getLine());
			}
		}
		
	}
	
	private void importData() throws IOException
	{
		
		// open both files
		RecordReader voyagesRdr = voyagesRecordIOFactory.createReader(voyagesSortedDataFileName);
		RecordReader slavesRdr = slavesRecordIOFactory.createReader(slavesSortedDataFileName);
		
		// variables for the main loop (see below)
		Record voyageRecord = null;
		Record slaveRecord = null;
		int voyageVid = 0;
		int slavesVid = 0;
		int mainVid = 0;
		ArrayList slaves = new ArrayList(); 
		boolean saveVoyage = false;
		boolean saveSlaves = false;
		boolean readVoyage = false;
		boolean readSlaves = false;
		
		// cache
		Voyage[] voyagesCache = null;
		int cachePos = 0;
		
		// init stats
		totalNoOfVoyages = 0;
		noOfVoyagesWithoutVid = 0;
		totalNoOfSlaves = 0;
		noOfSlavesWithoutVid = 0;
		noOfSlavesWithoutSid = 0;
		noOfVoyagesWithSlaves = 0;
		noOfValidVoyages = 0;
		noOfValidSlaves = 0;
		
		// we have only voyages -> make sure that in the main loop 
		// we always read and save only voyages
		if (voyagesPresent && !slavesPresent)
		{
			saveVoyage = true;
			saveSlaves = true;
			readVoyage = false;
			readSlaves = false;
		}

		// we have only slaves -> make sure that in the main loop 
		// we always read and save only slaves
		else if (voyagesPresent && !slavesPresent)
		{
			saveVoyage = false;
			saveSlaves = false;
			readVoyage = true;
			readSlaves = true;
		}
		
		// we have both files
		else
		{
			saveVoyage = true;
			saveSlaves = true;
			readVoyage = true;
			readSlaves = true;
		}
		
		// main loop
		while (readVoyage || readSlaves)
		{

			// read the next voyage
			// outcome: voyageVals
			// if voyageVals == null -> there is no more voyages
			if (readVoyage)
			{
				voyageVid = 0;
				while ((voyageRecord = voyagesRdr.readRecord()) != null)
				{
					totalNoOfVoyages++;
					
					String currVoyageVid = voyageRecord.getKey();

					boolean voyageHasVid = currVoyageVid.trim().length() != 0;
					if (!voyageHasVid) noOfVoyagesWithoutVid++;
					
					boolean validVoyage = voyageHasVid;
					//if (validVoyage) validVoyage = Integer.parseInt(currVoyageVid.trim()) >= 30000;
					if (validVoyage) noOfValidVoyages++;
					
					if (validVoyage)
					{
						voyageVid = Integer.parseInt(currVoyageVid.trim());
						break;
					}
					
				}
			}
	
			// read the next set of slaves with the same vid
			// outcome: ArrayList slaves;
			// if slaves.count() == 0 -> there is no more slaves
			if (readSlaves)
			{
				slaves.clear();
				slavesVid = 0;
				if (slaveRecord != null)
				{
					slavesVid = Integer.parseInt(slaveRecord.getKey().trim());
					slaves.add(slaveRecord);
				}
				while ((slaveRecord = slavesRdr.readRecord()) != null)
				{
					totalNoOfSlaves ++;
					
					String currSlaveVid = slaveRecord.getKey();
					
					boolean slaveHasVid = currSlaveVid.trim().length() > 0;
					boolean slaveHasSid = true;
					if (!slaveHasVid) noOfSlavesWithoutVid++;
					if (!slaveHasSid) noOfSlavesWithoutSid++;

					boolean validSlave = slaveHasVid && slaveHasSid;
					//if (validSlave) validSlave = Integer.parseInt(currSlaveVid.trim()) >= 30000;
					if (validSlave) noOfValidSlaves++;
					
					if (validSlave)
					{
						int currSlaveVidInt = Integer.parseInt(currSlaveVid.trim());
						if (slavesVid == 0)
						{
							slavesVid = currSlaveVidInt;
							slaves.add(slaveRecord);
						}
						else if (slavesVid == currSlaveVidInt)
						{
							slaves.add(slaveRecord);
						}
						else
						{
							break;
						}
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
				saveVoyage = false;
				readVoyage = false;
				saveSlaves = false;
				readSlaves = false;
			}
			
			// end of voyages
			else if (!haveVoyage)
			{
				saveVoyage = false;
				readVoyage = false;
			}
			
			// end of slaves
			else if (!haveSlaves)
			{
				saveSlaves = false;
				readSlaves = false;
			}

			// we have both still
			else
			{
				if (voyageVid == slavesVid)
				{
					saveVoyage = true;
					readVoyage = true;
					saveSlaves = true;
					readSlaves = true;
				}
				else if (voyageVid < slavesVid)
				{
					saveVoyage = true;
					readVoyage = true;
					saveSlaves = false;
					readSlaves = false;
				}
				else
				{
					saveVoyage = false;
					readVoyage = false;
					saveSlaves = true;
					readSlaves = true;
				}
			}
			

			// save 
			if (saveVoyage || saveSlaves)
			{
			
				// determine VID
				mainVid = saveVoyage ? voyageVid : slavesVid;
				
				// load voyage from db or create a new one
				Voyage voyage = null;
				while (voyage == null)
				{
					
					if (voyagesCache == null || cachePos == voyagesCache.length)
					{
						voyagesCache = Voyage.loadMostRecent(new Long(mainVid), VOYAGE_CACHE_SIZE);
						cachePos = 0;
						//HibernateUtil.getSessionFactory().getStatistics().logSummary();
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
								voyage = Voyage.createNew(new Long(mainVid));
								break;
							}
						}
					}
					else
					{
						voyage = Voyage.createNew(new Long(mainVid));
					}
					
				}
				
				// update voyage
				if (saveVoyage)
				{
					updateVoyage(voyage, voyageRecord);
				}
				
				// update slaves
				if (saveSlaves)
				{
					removeDeletedSlaves(voyage, slaves);
					updateExistingSlaves(voyage, slaves);
					addNewSlaves(voyage, slaves);
				}
				
				// save
				voyage.save();

				// basic stat
				if (saveVoyage && saveSlaves)
					noOfVoyagesWithSlaves++;
				
				// debug
				System.out.print("VID = " + mainVid);
				if (saveVoyage)
				{
					System.out.print(", voyage = yes");
					System.out.print(", shipname = " + new String(voyageRecord.getLine(),200,58));
				}
				if (saveSlaves)
				{
					System.out.print(", slaves = yes");
					System.out.print(", number of slaves = " + slaves.size());
				}
				System.out.println();
			
			}
			
		}
		
		// close all
		voyagesRdr.close();
		slavesRdr.close();

	}

	public void runImport(LogWriter log, String workingDir, String voyagesSpssFileName, String slavesSpssFileName)
	{
		
		// remember files
		this.workingDir = workingDir;
		this.voyagesSpssFileName = voyagesSpssFileName;
		this.slavesSpssFileName = slavesSpssFileName;
		this.voyagesPresent = voyagesSpssFileName!=null;
		this.slavesPresent = slavesSpssFileName!=null;
		
		// setup the other file names
		voyagesDataFileName = workingDir + File.separatorChar + "voyages.dat";
		voyagesSortedDataFileName = workingDir + File.separatorChar + "voyages-sorted.dat";
		voyagesSchemaFileName = workingDir + File.separatorChar + "voyages.sts";
		slavesDataFileName = workingDir + File.separatorChar + "slaves.dat";
		slavesSortedDataFileName = workingDir + File.separatorChar + "slaves-sorted.dat";
		slavesSchemaFileName = workingDir + File.separatorChar + "slaves.sts";
		
		// run import
		try
		{
			
			long startTime = System.currentTimeMillis();
			
			log.startStage(LogItem.STAGE_CONVERSION);
			convertSpssFiles();

			log.startStage(LogItem.STAGE_SCHEMA_LOADING);
			loadSchemas();
			
			log.startStage(LogItem.STAGE_SCHEMA_MATCHING);
			matchAndVerifySchema();

			log.startStage(LogItem.STAGE_SORTING);
			sortFiles();

			log.startStage(LogItem.STAGE_IMPORTING);
			updateDictionaties();
			importData();

//			System.out.println("total number of voyages = " + totalNoOfVoyages);
//			System.out.println("number of valid voyages = " + noOfValidVoyages);
//			System.out.println("total number of slaves  = " + totalNoOfSlaves);
//			System.out.println("number of valid slaves  = " + noOfValidSlaves);
//			System.out.println("voyages with slaves     = " + noOfVoyagesWithSlaves);
			
			long endTime = System.currentTimeMillis();
			System.out.println("Total time: " + (endTime - startTime));

			if (false)
			{
				throw new IOException();
			}

			if (false)
			{
				throw new STSchemaException(""); 
			}

		}
		catch (IOException ioe)
		{
			// all problems when reading and writing files
		}
		catch (STSchemaException stse)
		{
		}
		
	}

	public static void main(String[] args) throws FileNotFoundException, IOException
	{
		
		if (args.length != 4) return;
		
		Voyage.initTypes();
		Slave.initTypes();

		// extracts params
		String workingDir = args[0];
		String logFileName = args[1];
		String voyagesFileName = args[2].equals("*") ? null : args[2]; 
		String slavesFileName = args[3].equals("*") ? null : args[3];
		
		// crate a log
		LogWriter log = new LogWriter(logFileName);
		
		// import
		Import imp = new Import();
		imp.runImport(log, workingDir, voyagesFileName, slavesFileName); 
		log.close();
		
	}

}
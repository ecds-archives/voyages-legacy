package edu.emory.library.tas.spss;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

import edu.emory.library.tas.AbstractDescriptiveObject;
import edu.emory.library.tas.Dictionary;
import edu.emory.library.tas.SchemaColumn;
import edu.emory.library.tas.Slave;
import edu.emory.library.tas.Voyage;

public class Import
{
	
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
	AsciiFixedFormatRecordIOFactory voyagesRecordIOFactory;
	AsciiFixedFormatRecordIOFactory slavesRecordIOFactory;

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
			width += var.getEndColumn() - var.getStartColumn() + 1;
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
		voyagesRecordIOFactory = new AsciiFixedFormatRecordIOFactory(
			voyagesVidVar.getStartColumn(),
			voyagesVidVar.getEndColumn(),
			voyagesSchemaWidth);
	
		// locate vid create record io factory for slaves
		slavesVidVar = (STSchemaVariable)slaveSchema.get("voyageid");
		int slavesSchemaWidth = calculateSchemaWidth(slaveSchema); 
		slavesRecordIOFactory = new AsciiFixedFormatRecordIOFactory(
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

			if (col.getImportType() == SchemaColumn.IMPORT_TYPE_IGNORE)
			{
				continue;
			}
			
			else if (col.getType() != SchemaColumn.TYPE_DATE)
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
				else
				{
					//System.out.println("field " + col.getName() + " ok");
					// seems to be ok
				}
			}
			
			else
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
		voyagesSorter.setMaxLines(1000);
		voyagesSorter.sort();
		
		// sort slaves
		RecordSorter slavesSorter = new RecordSorter(slavesDataFileName, slavesSortedDataFileName, slavesRecordIOFactory);
		slavesSorter.setMaxLines(5000);
		slavesSorter.sort();
		
	}
	
	private void updateDictionary(SchemaColumn col, STSchemaVariable var)
	{
		System.out.println("Inside: " + col.getName() + "(" + var.getLabels().size() + ")");
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
			
			if (newDict)
				System.out.println("Adding value: " + label.getKey() + " (" + label.getLabel() + ")");
			else
				System.out.println("Updating value: " + label.getKey() + " (" + label.getLabel() + ")");
			
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
				System.out.println("Updating: " + col.getName() + " (" + col.getDictinaory() + ")");
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
			SchemaColumn col = getSchemaColumn(recordType, dbSchemaNames[i]);
			
			Object value = null;
			
			if (col.getType() == SchemaColumn.IMPORT_TYPE_DATE)
			{
				STSchemaVariable varDay = (STSchemaVariable) schema.get(col.getImportDateDay());
				STSchemaVariable varMonth = (STSchemaVariable) schema.get(col.getImportDateMonth());
				STSchemaVariable varYear = (STSchemaVariable) schema.get(col.getImportDateYear());
				value = col.parse(new String[]{
						new String(
								line,
								varDay.getStartColumn(),
								varDay.getLength()).trim(),
						new String(
								line,
								varMonth.getStartColumn(),
								varMonth.getLength()).trim(),
						new String(line,
								varYear.getStartColumn(),
								varYear.getLength()).trim()});
			}
			
			else
			{
				STSchemaVariable var = (STSchemaVariable) schema.get(col.getImportName());
				value = col.parse(
						new String(line,
								var.getStartColumn(),
								var.getLength()).trim());
			}
			
			if (value == null) valid = false;
			obj.setAttrValue(col.getName(), value);
			
		}
		
		return valid;

	}
	
	private void updateVoyage(Voyage voyage, Record voyageRecord)
	{
		updateValues(voyage, ((AsciiFixedFormatRecord) voyageRecord).getLine());
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
				Long slaveId = new Long(((AsciiFixedFormatRecord)slaves.get(i)).getKey().trim());
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
			AsciiFixedFormatRecord slaveRecord = (AsciiFixedFormatRecord) iterSlave.next();
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
			AsciiFixedFormatRecord slaveRecord = (AsciiFixedFormatRecord) iterSlave.next();
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
		String voyageVid = null;
		String slavesVid = null;
		String mainVid = null;
		ArrayList slaves = new ArrayList(); 
		boolean saveVoyage = false;
		boolean saveSlaves = false;
		boolean readVoyage = false;
		boolean readSlaves = false;
		
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
				voyageVid = null;
				while ((voyageRecord = voyagesRdr.readRecord()) != null)
				{
					totalNoOfVoyages++;
					
					String currVoyageVid = ((AsciiFixedFormatRecord)voyageRecord).getKey().trim();

					boolean voyageHasVid = currVoyageVid.length() != 0;
					if (!voyageHasVid) noOfVoyagesWithoutVid++;
					
					boolean validVoyage = voyageHasVid; 
					if (validVoyage) noOfValidVoyages++;
					
					if (validVoyage)
					{
						voyageVid = currVoyageVid;
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
				slavesVid = null;
				if (slaveRecord != null)
				{
					slavesVid = ((AsciiFixedFormatRecord)slaveRecord).getKey().trim();
					slaves.add(slaveRecord);
				}
				while ((slaveRecord = slavesRdr.readRecord()) != null)
				{
					totalNoOfSlaves ++;
					
					String currSlaveVid = ((AsciiFixedFormatRecord)slaveRecord).getKey().trim();
					
					boolean slaveHasVid = currSlaveVid.trim().length() > 0;
					boolean slaveHasSid = true;
					if (!slaveHasVid) noOfSlavesWithoutVid++;
					if (!slaveHasSid) noOfSlavesWithoutSid++;

					boolean validSlave = slaveHasVid && slaveHasSid;
					if (validSlave) noOfValidSlaves++;

					if (validSlave)
					{
						if (slavesVid == null)
						{
							slavesVid = currSlaveVid;
							slaves.add(slaveRecord);
						}
						else if (slavesVid.compareTo(currSlaveVid) == 0)
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
			boolean haveVoyage = voyageVid != null;
			boolean haveSlaves = slavesVid != null;
			
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
				int cmpVid = voyageVid.compareTo(slavesVid);
				if (cmpVid == 0)
				{
					saveVoyage = true;
					readVoyage = true;
					saveSlaves = true;
					readSlaves = true;
				}
				else if (cmpVid < 0)
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
				Voyage voyage = Voyage.loadMostRecent(new Long(mainVid));
				if (voyage == null) Voyage.createNew(new Long(mainVid));

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
					System.out.print(", shipname = " + new String(((AsciiFixedFormatRecord)voyageRecord).getLine(),200,58));
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

	public void runImport(String voyagesSpssFileName, String slavesSpssFileName)
	{
		
		// remember files
		this.voyagesSpssFileName = voyagesSpssFileName;
		this.slavesSpssFileName = slavesSpssFileName;
		this.voyagesPresent = voyagesSpssFileName!=null;
		this.slavesPresent = slavesSpssFileName!=null;
		
		// setup the other file names
		voyagesDataFileName = "voyages.dat";
		voyagesSortedDataFileName = "voyages-sorted.dat";
		voyagesSchemaFileName = "voyages.sts";
		slavesDataFileName = "slaves.dat";
		slavesSortedDataFileName = "slaves-sorted.dat";
		slavesSchemaFileName = "slaves.sts";

		// run import
		try
		{
			
//			System.out.print("Converting files ...");
//			convertSpssFiles();
//			System.out.println("done");

			System.out.print("Loading schemas ...");
			loadSchemas();
			System.out.println("done");
			
			System.out.print("Matching schemas ...");
			matchAndVerifySchema();
			System.out.println("done");

//			System.out.print("Sorting data ...");
//			sortFiles();
//			System.out.println("done");

			System.out.print("Updating dictionaries ...");
			updateDictionaties();
			System.out.println("done");
			
//			System.out.print("Importing data ...");
//			importData();
//			System.out.println("done");

//			System.out.println("total number of voyages = " + totalNoOfVoyages);
//			System.out.println("number of valid voyages = " + noOfValidVoyages);
//			System.out.println("total number of slaves  = " + totalNoOfSlaves);
//			System.out.println("number of valid slaves  = " + noOfValidSlaves);
//			System.out.println("voyages with slaves     = " + noOfVoyagesWithSlaves);
			
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
		
		Voyage.initTypes();
		Slave.initTypes();
		
		Import imp = new Import();
		imp.runImport(
			"C:\\Documents and Settings\\zich\\My Documents\\Library\\SlaveTrade\\data\\basecoy56.sav",
			"C:\\Documents and Settings\\zich\\My Documents\\Library\\SlaveTrade\\data\\1Masterx18Countries.sav");
		
	}

}
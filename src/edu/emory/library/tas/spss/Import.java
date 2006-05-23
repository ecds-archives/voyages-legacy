package edu.emory.library.tas.spss;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import edu.emory.library.tas.ColumnMetadata;

public class Import
{
	
	private ColumnMetadata[] matchColumns(String csvFileName, ColumnMetadata[] cols) throws IOException
	{
		
		// open
		BufferedReader rdr = new BufferedReader(new FileReader(csvFileName));
		CsvParser csv = new CsvParser(rdr);
		
		// read header
		String[] header = csv.getRow();
		
		// prepare ouput array
		ColumnMetadata csvCols[] = new ColumnMetadata[header.length]; 
		for (int i=0; i<csvCols.length; i++) csvCols[i] = null;

		// match
		for (int i=0; i<header.length; i++)
		{
			for (int j=0; j<cols.length; j++)
			{
				if (cols[j].getName().compareTo(header[i]) == 0)
				{
					csvCols[i] = cols[j];
					break;
				}
			}
		}
		
		// done
		rdr.close();
		return csvCols;
		
	}
	
	public void runImport(String voyagesFileName, String slavesFileName) throws FileNotFoundException, IOException
	{
		
		// temporary, eventualy 350 fields
		ColumnMetadata[] voyageCols = new ColumnMetadata[3]; 
		voyageCols[0] = new ColumnMetadata("voyageid", ColumnMetadata.TYPE_NUMBER);
		voyageCols[1] = new ColumnMetadata("shipname", ColumnMetadata.TYPE_STRING);
		voyageCols[2] = new ColumnMetadata("captaina", ColumnMetadata.TYPE_STRING);

		// temporary, eventualy 62 fields
		ColumnMetadata[] slaveCols = new ColumnMetadata[4];
		slaveCols[0] = new ColumnMetadata("voyageid", ColumnMetadata.TYPE_NUMBER);
		slaveCols[1] = new ColumnMetadata("slaveid", ColumnMetadata.TYPE_NUMBER);
		slaveCols[2] = new ColumnMetadata("sexage", ColumnMetadata.TYPE_NUMBER);
		slaveCols[3] = new ColumnMetadata("age", ColumnMetadata.TYPE_NUMBER);
		
		// do we have both files
		boolean voyagesPresent = voyagesFileName!=null;
		boolean slavesPresent = slavesFileName!=null;

		// sort colums by the order in the CSV files
		ColumnMetadata voyageCsvCols[] = matchColumns(voyagesFileName, voyageCols);
		ColumnMetadata slaveCsvCols[] = matchColumns(slavesFileName, slaveCols);
		
		// find the index of VoyageId
		int voyageVidIdx = ColumnMetadata.indexOf(voyageCsvCols, "voyageid");
		int slaveVidIdx = ColumnMetadata.indexOf(slaveCsvCols, "voyageid");

		// sort voyages
		System.out.print("Sorting voyages ... ");
		String voyagesSortedFileName = voyagesFileName + ".sorted";
		CsvSorter voyagesSorter = new CsvSorter(voyagesFileName, voyagesSortedFileName, "voyageid");
		voyagesSorter.sort();
		System.out.println("done.");
		
		// sort slaves
		System.out.print("Sorting slaves ... ");
		String slavesSortedFileName = slavesFileName + ".sorted";
		CsvSorter slavesSorter = new CsvSorter(slavesFileName, slavesSortedFileName, "voyageid");
		slavesSorter.sort();
		System.out.println("done.");
		
		// open both files
		BufferedReader voyagesRdr = new BufferedReader(new FileReader(voyagesSortedFileName));
		BufferedReader slavesRdr = new BufferedReader(new FileReader(slavesSortedFileName));
		CsvParser voyagesCsv = new CsvParser(voyagesRdr); 
		CsvParser slavesCsv = new CsvParser(slavesRdr);
		
		// variables for the main loop (see below)
		String[] voyageVals = null;
		String[] slaveVals = null;
		String voyageVid = null;
		String slaveVid = null;
		String mainVid = null;
		ArrayList slaves = new ArrayList(); 
		boolean saveVoyage = false;
		boolean saveSlaves = false;
		boolean readVoyage = false;
		boolean readSlave = false;
		
		// we have only voyages -> make sure that in the main loop 
		// we always read and save only voyages
		if (voyagesPresent && !slavesPresent)
		{
			saveVoyage = true;
			saveSlaves = true;
			readVoyage = false;
			readSlave = false;
		}

		// we have only slaves -> make sure that in the main loop 
		// we always read and save only slaves
		else if (voyagesPresent && !slavesPresent)
		{
			saveVoyage = false;
			saveSlaves = false;
			readVoyage = true;
			readSlave = true;
		}
		
		// we have both files
		else
		{
			saveVoyage = true;
			saveSlaves = true;
			readVoyage = true;
			readSlave = true;
		}
		
		// main loop
		while (readVoyage || readSlave)
		{

			// read the next voyage
			// outcome: voyageVals
			// if voyageVals == null -> there is no more voyages
			if (readVoyage)
			{
				while ((voyageVals = voyagesCsv.getRow()) != null)
				{
					voyageVals = voyagesCsv.getRow();
					boolean validVoyage = voyageVals.length == voyageCsvCols.length;
					if (validVoyage)
					{
						voyageVid = voyageVals[voyageVidIdx];
						break;
					}
				}
			}
	
			// read the next set of slaves with the same vid
			// outcome: ArrayList slaves;
			// if slaves.count() == 0 -> there is no more slaves
			if (readSlave)
			{
				slaves.clear();
				if (slaveVals != null)
				{
					slaveVid = slaveVals[slaveVidIdx];
					slaves.add(slaveVals);
				}
				while ((slaveVals = slavesCsv.getRow()) != null)
				{
					boolean validSlave = slaveVals.length == slaveCsvCols.length;
					if (validSlave)
					{
						if (slaveVid == null)
						{
							slaveVid = slaveVals[slaveVidIdx];
							slaves.add(slaveVals);
						}
						else if (slaveVid.compareTo(slaveVals[slaveVidIdx]) == 0)
						{
							slaves.add(slaveVals);
						}
						else
						{
							break;
						}
					}
				}
			}
			
			// end of voyages and slaves
			if (voyageVals == null && slaves.size() == 0)
			{
				saveVoyage = false;
				readVoyage = false;
				saveSlaves = false;
				readSlave = false;
			}
			
			// end of voyages
			else if (voyageVals == null)
			{
				saveVoyage = false;
				readVoyage = false;
			}
			
			// end of slaves
			else if (slaves.size() == 0)
			{
				saveSlaves = false;
				readSlave = false;
			}

			// we have both still
			else
			{
				int cmpVid = voyageVid.compareTo(slaveVid);
				if (cmpVid == 0)
				{
					saveVoyage = true;
					readVoyage = true;
					saveSlaves = true;
					readSlave = true;
				}
				else if (cmpVid < 0)
				{
					saveVoyage = true;
					readVoyage = true;
					saveSlaves = false;
					readSlave = false;
				}
				else
				{
					saveVoyage = false;
					readVoyage = false;
					saveSlaves = true;
					readSlave = true;
				}
			}
			

			// save 
			if (saveVoyage || saveSlaves)
			{
			
				// determine VID
				mainVid = saveVoyage ? voyageVid : slaveVid;   
				
				// load from db ...
				// Voyage v = Voyage.loadFromDb(mainVid);
				System.out.print("VID = " + mainVid);
		
				// we have information about the voyage
				if (saveVoyage)
				{
					// in voyageVals we should have current voyage, so ...
					// v.setParam(); ...
					System.out.print(", voyage = yes");
				}
				
				// we have information about slaves
				if (saveSlaves)
				{
					// in slaves we should have the current slaves
					// v.slaves.add(); ...
					// v.slaves.remove(); ...
					System.out.print(", slaves = yes");
					System.out.print(", number of slaves = " + slaves.size());
				}
				
				// save it to db
				// v.saveToDb();
				System.out.println();
			
			}
			
		}
		
		// close all
		voyagesRdr.close();
		slavesRdr.close();
		
	}
	
	public static void main(String[] args) throws FileNotFoundException, IOException
	{
		
		Import imp = new Import();
		imp.runImport("voyages.csv", "slaves.csv");
		
	}

}
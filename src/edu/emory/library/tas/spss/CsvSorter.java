package edu.emory.library.tas.spss;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class CsvSorter
{
	
	private static final int MAXMERGE = 16;
	private static final int MAXLINES = 1000;
	
	private String inputFileName;
	private String outputFileName;
	private String sortByField;
	private int maxLines = MAXLINES;
	
	private class LineComparator implements Comparator
	{
		
		private int sortByFieldIdx;
		
		public LineComparator(int sortByFieldIdx)
		{
			this.sortByFieldIdx = sortByFieldIdx;
		}
		
		public int compare(Object line0, Object line1)
		{
			String key0 = ((String[])line0)[sortByFieldIdx]; 
			String key1 = ((String[])line1)[sortByFieldIdx]; 
			return (key0.compareTo(key1));
		}
		
	}
	
	public CsvSorter(String inputFileName, String outputFileName, String sortByField)
	{
		this.inputFileName = inputFileName;
		this.outputFileName = outputFileName;
		this.sortByField = sortByField;
	}
	
	public void setMaxLines(int maxLines)
	{
		if (maxLines > 0) this.maxLines = maxLines;
	}

	public int getMaxLines()
	{
		return maxLines;
	}

	public void setSortByField(String sortByField)
	{
		this.sortByField = sortByField;
	}

	public String getSortByField()
	{
		return sortByField;
	}

	public void setInputFileName(String fileName)
	{
		this.inputFileName = fileName;
	}

	public String getInputFileName()
	{
		return inputFileName;
	}

	public void setOutputFileName(String fileName)
	{
		this.outputFileName = fileName;
	}

	public String getOutputFileName()
	{
		return outputFileName;
	}

	public void sort() throws FileNotFoundException, IOException
	{
		
		ArrayList tmpFiles = new ArrayList();
		int nextTmpFileIdx = 0;
		
		// open file
		File inputFile = new File(inputFileName);
		BufferedReader inputRdr = new BufferedReader(new FileReader(inputFile));
		CsvParser cvsParser = new CsvParser(inputRdr);

		// read header
		String[] headerLine = cvsParser.getRow();
		
		// find the position of the header field
		int sortByFieldIdx = -1;
		for (int i=0; i<headerLine.length; i++)
		{
			if (headerLine[i].compareTo(sortByField) == 0)
			{
				sortByFieldIdx = i;
				break;
			}
		}

		// read the input file, sort it by chunks and write
		// the chunks to temporary files
		//String lineText = null;
		String[][] lines = new String[maxLines][];
		int linesCount = 0;
		do
		{
			
			// reset the the list of lines
			linesCount = 0;
			
			// read lines
			do
			{
				String lineValues[] = cvsParser.getRow(true);
				if (!cvsParser.isEOF()) lines[linesCount++] = lineValues;
			}
			while (!cvsParser.isEOF() && linesCount < maxLines);
				
			// in case we really read anything
			if (linesCount > 0)
			{
				
				// sort
				Arrays.sort(lines, new LineComparator(sortByFieldIdx));
				
				// create a temp file
				String tmpFileName = "chunk" + (nextTmpFileIdx++) + ".tmp";
				File tmpFile = new File(inputFile.getParent(), tmpFileName);
				tmpFiles.add(tmpFile);
				
				// write
				PrintWriter tmpFileWriter = new PrintWriter(new BufferedWriter(new FileWriter(tmpFile)));
				for (int i=0; i<linesCount; i++)
				{
					String[] lineValues = lines[i];
					for (int k=0; k<lineValues.length; k++)
					{
						if (k>0) tmpFileWriter.print(",");
						tmpFileWriter.print(lineValues[k]);
					}
					tmpFileWriter.println();
				}
				tmpFileWriter.close();
			
			}
			
		}
		while (linesCount > 0);
		inputRdr.close();

		// main structures for merge: list of 
		// opened files and their first lines
		BufferedReader tmpReaders[] = new BufferedReader[MAXMERGE];
		CsvParser csvParsers[] = new CsvParser[MAXMERGE];
		String currLines[][] = new String[MAXMERGE][];
		
		// merge
		while (tmpFiles.size() > 1)
		{
		
			// reset readers
			for (int i=0; i<MAXMERGE; i++)
			{
				tmpReaders[i] = null;
				currLines[i] = null;
			}
			
			// open first MAXMERGE temp files
			int mergeCount = 0;
			for (int i=0; i<tmpFiles.size() && i<MAXMERGE; i++)
			{
				File tmpFile = (File)(tmpFiles.get(i));
				tmpReaders[i] = new BufferedReader(new FileReader(tmpFile));
				csvParsers[i] = new CsvParser(tmpReaders[i]);
				mergeCount ++;
			}
			
			// create a new temp file and add it to the end
			String mergeToFileName = "chunk" + (nextTmpFileIdx++) + ".tmp";
			File mergeToFile = new File(inputFile.getParent(), mergeToFileName);
			tmpFiles.add(mergeToFile);
			
			// open it
			PrintWriter mergeToWriter = new PrintWriter(new BufferedWriter(new FileWriter(mergeToFileName)));
			
			// read first lines
			for (int i=0; i<mergeCount; i++)
			{
				String lineValues[] = csvParsers[i].getRow(true);
				currLines[i] = lineValues;
			}
			
			// merge
			int tmpLeft = mergeCount;
			while (tmpLeft > 0)
			{
				
				// find min
				String minKey = null;
				int minIdx = -1;
				for (int i=0; i<mergeCount; i++)
				{
					if (tmpReaders[i] != null)
					{
						if (minKey == null || currLines[i][sortByFieldIdx].compareTo(minKey) < 0)
						{
							minKey = currLines[i][sortByFieldIdx];
							minIdx = i;
						}
					}
				}
				
				// write the min line out
				String[] lineValues = currLines[minIdx];
				for (int k=0; k<lineValues.length; k++)
				{
					if (k>0) mergeToWriter.print(",");
					mergeToWriter.print(lineValues[k]);
				}
				mergeToWriter.println();
				
				// read next line
				lineValues = csvParsers[minIdx].getRow(true);
				if (!csvParsers[minIdx].isEOF())
				{
					currLines[minIdx] = lineValues;
				}
				else
				{
					tmpReaders[minIdx].close();					
					tmpReaders[minIdx] = null;					
					currLines[minIdx] = null;
					tmpLeft--;
				}
				
			}
	
			// close
			mergeToWriter.close();
			
			// close and delete the first temp files
			for (int i=0; i<mergeCount; i++)
			{
				File tmpFile = (File)(tmpFiles.get(0));
				tmpFile.delete();
				tmpFiles.remove(0);
			}
		
		}
		
		// rename last temp file to the output file
		File outputFile = new File(inputFile.getParent(), outputFileName);
		((File)tmpFiles.get(0)).renameTo(outputFile);
		
	}

	public static void main(String[] args) throws IOException
	{
		
		CsvSorter s = new CsvSorter("voyages.csv", "voyages-sorted.csv", "voyageid");
		//s.setMaxLines(2000);
		s.sort();
		
	}		
	
}
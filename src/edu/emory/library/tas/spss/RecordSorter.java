package edu.emory.library.tas.spss;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class RecordSorter
{

	private static final int MAXMERGE = 16;
	private static final int MAXLINES = 1000;
	
	private String inputFileName;
	private String outputFileName;
	private String tmpFolder = null;
	private RecordIOFactory recordIO;
	private int maxLines = MAXLINES;
	
	private class RecordComparator implements Comparator
	{
		public int compare(Object recObj0, Object recObj1)
		{
			Record rec0 = (Record)recObj0; 
			Record rec1 = (Record)recObj1; 
			return (rec0.compareTo(rec1));
		}
	}
	
	public RecordSorter(RecordIOFactory recordIO)
	{
		this.recordIO = recordIO;
	}

	public RecordSorter(String inputFileName, String outputFileName, RecordIOFactory recordIO)
	{
		this.inputFileName = inputFileName;
		this.outputFileName = outputFileName;
		this.recordIO = recordIO;
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
	
	public void setTmpFolder(String tmpFolder)
	{
		this.tmpFolder = tmpFolder;
	}

	public String getTmpFolder()
	{
		return tmpFolder;
	}

	public void setMaxLines(int maxLines)
	{
		if (maxLines > 0) this.maxLines = maxLines;
	}

	public int getMaxLines()
	{
		return maxLines;
	}

	public void sort() throws FileNotFoundException, IOException
	{
		
		ArrayList tmpFiles = new ArrayList();
		int nextTmpFileIdx = 0;
		
		// determine temp folder
		if (tmpFolder == null)
			tmpFolder = new File(inputFileName).getParent();
		
		// open input file
		RecordReader reader = recordIO.createReader(new File(inputFileName));

		// generic poiter for small cycles
		Record record;
		
		// read the input file, sort it by chunks and write
		// the chunks to temporary files
		//String lineText = null;
		Record[] records = new Record[maxLines];
		int recordCount = 0;
		do
		{
			
			// reset the the list of lines
			recordCount = 0;
			
			// read lines
			record = null;
			while ((record = reader.readRecord()) != null)
			{
				records[recordCount++] = record;
				if (recordCount == maxLines) break;
			}
				
			// in case we really read anything
			if (recordCount > 0)
			{
				
				// sort
				Arrays.sort(records, new RecordComparator());
				
				// create a temp file
				String tmpFileName = "chunk" + (nextTmpFileIdx++) + ".tmp";
				File tmpFile = new File(getTmpFolder(), tmpFileName);
				tmpFiles.add(tmpFile);
				
				// write
				RecordWriter tmpWriter = recordIO.createWriter(tmpFile);
				tmpWriter.writeRecords(records);
				tmpWriter.close();
			
			}
			
		}
		while (recordCount > 0);

		// main structures for merge: list of 
		// opened files and their first lines
		RecordReader tmpReaders[] = new RecordReader[MAXMERGE];
		Record currLines[] = new Record[MAXMERGE];
		
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
				tmpReaders[i] = recordIO.createReader(tmpFile);
				mergeCount ++;
			}
			
			// create a new temp file and add it to the end
			String mergeToFileName = "chunk" + (nextTmpFileIdx++) + ".tmp";
			File mergeToFile = new File(getTmpFolder(), mergeToFileName);
			tmpFiles.add(mergeToFile);
			
			// open it
			RecordWriter mergeToWriter = recordIO.createWriter(mergeToFile);
			
			// read first lines
			for (int i=0; i<mergeCount; i++)
				currLines[i] = tmpReaders[i].readRecord();
			
			// merge
			int tmpLeft = mergeCount;
			while (tmpLeft > 0)
			{
				
				// find min
				Record minRecord = null;
				int minIdx = -1;
				for (int i=0; i<mergeCount; i++)
				{
					if (tmpReaders[i] != null)
					{
						record = currLines[i];
						if (minRecord == null || record.compareTo(minRecord) < 0)
						{
							minRecord = record;
							minIdx = i;
						}
					}
				}
				
				// write the min line out
				mergeToWriter.writeRecord(minRecord);
				
				// read next line
				record = tmpReaders[minIdx].readRecord();
				if (record != null)
				{
					currLines[minIdx] = record;
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
		File outputFile = new File(getTmpFolder(), outputFileName);
		((File)tmpFiles.get(0)).renameTo(outputFile);
		
	}

	public static void main(String[] args) throws IOException
	{
		
		AsciiFixedFormatRecordIOFactory f = new AsciiFixedFormatRecordIOFactory(193, 198, 2973); 
		RecordSorter s = new RecordSorter("voyages.csv", "voyages-sorted.csv", f);
		//s.setMaxLines(2000);
		s.sort();
		
	}		

}

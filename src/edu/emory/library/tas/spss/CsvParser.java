package edu.emory.library.tas.spss;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.ArrayList;

public class CsvParser {
	
	private Reader rdr;
	private boolean eol;
	private boolean eof;

	private int la = NO_CHAR;
	
	final static private int BEFORE_FIELD = 0;
	final static private int INSIDE_WITH_QUOTES = 1;
	final static private int INSIDE_NO_QUOTES = 2;
	final static private int LAST_WAS_QUOTE = 3;
	final static private int END_FIELD = 4;
	final static private int LOOKING_FOR_COMMA = 5;
	final static private int NO_CHAR = -2;
	
	final private StringBuffer buf = new StringBuffer();
	final private ArrayList linesLst = new ArrayList();

	public CsvParser(Reader rdr)
	{
		this.rdr = rdr;
	}

	private String getNext(boolean read, boolean includeQuotes) throws IOException
	{
		
		eof = false;
		eol = false;
		buf.setLength(0);
		
		int s = BEFORE_FIELD;
		int ch = 0;
		if (la == NO_CHAR) la = rdr.read();
		while (s != END_FIELD)
		{
			
			ch = la;
			la = rdr.read();
			
			if (ch == '\r' && la == '\n')
			{
				ch = '\n';
				la = rdr.read();
			}
			else if (ch == '\r')
			{
				ch = '\n';
			}
			
			switch (s)
			{
			
			case BEFORE_FIELD:
				if (ch == '\n' || ch == -1)
				{
					eol = true;
					s = END_FIELD;
				}
				else if (ch == ',')
				{
					s = END_FIELD;
				}
				else if (ch == '"')
				{
					s = INSIDE_WITH_QUOTES;
					if (includeQuotes && read) buf.append('"');
				}
				else if (!(ch == ' ' || ch == '\t'))
				{
					s = INSIDE_NO_QUOTES;
					if (read) buf.append((char)ch);
				}
				break;
				
			case INSIDE_NO_QUOTES:
				if (ch == ',')
				{
					s = END_FIELD;
				}
				else if (ch == '\n' || ch == -1)
				{
					eol = true;
					s = END_FIELD;
				}
				else
				{
					if (read) buf.append((char)ch);
				}
				break;

			case INSIDE_WITH_QUOTES:
				if (ch == '"')
				{
					s = LAST_WAS_QUOTE;
				}
				else if (ch == -1)
				{
					eol = true;
					s = END_FIELD;
					if (includeQuotes && read) buf.append('"');
				}
				else
				{
					if (read) buf.append((char)ch);
				}
				break;
				
			case LAST_WAS_QUOTE:
				if (ch == '"')
				{
					if (read)
					{
						buf.append('"');
						if (includeQuotes) buf.append('"');
					}
					s = INSIDE_WITH_QUOTES;
				}
				else if (ch == -1 || ch == '\n')
				{
					eol = true;
					s = END_FIELD;
					if (includeQuotes && read) buf.append('"');
				}
				else if (ch == ',')
				{
					s = END_FIELD;
					if (includeQuotes && read) buf.append('"');
				}
				else
				{
					s = LOOKING_FOR_COMMA;
					if (includeQuotes && read) buf.append('"');
				}
				break;
				
			case LOOKING_FOR_COMMA:
				if (ch == ',')
				{
					s = END_FIELD;
				}
				else if (ch == -1 || ch == '\n')
				{
					eol = true;
					s = END_FIELD;
				}
				break;

			}
			
		}
		
		if (ch == -1)
			eof = true;
		
		if (read)
			return buf.toString();
		else
			return null;
		
	}
	
	public boolean isEOL()
	{
		return eol;
	}

	public boolean isEOF()
	{
		return eof;
	}

	public String getNext(boolean includeQuotes) throws IOException
	{
		return getNext(true, includeQuotes);
	}
	
	public String getNext() throws IOException
	{
		return getNext(true, false);
	}

	public String[] getRow(boolean includeQuotes) throws IOException
	{
		
		if (isEOF())
			return null;
		
		linesLst.clear();
		do
		{
			String fld = getNext(true, includeQuotes);
			linesLst.add(fld);
		}
		while (!isEOL());
		
		String []linesArr = new String[linesLst.size()];
		for (int i=0; i<linesLst.size(); i++)
			linesArr[i] = (String)linesLst.get(i);
		
		return linesArr;
		
	}
	
	public String[] getRow() throws IOException
	{
		return getRow(false);
	}
	
	public static void main(String[] args) throws IOException
	{
		
		BufferedReader r = new BufferedReader(new FileReader("voyages.csv"));
		PrintWriter w = new PrintWriter(new BufferedWriter(new FileWriter("voyages-out.csv")));
		
		//StringJoiner j = new StringJoiner();
		CsvParser p = new CsvParser(r);
		
		int i = 0;
		while (!p.isEOF())
		{
			i++;
			String l[] = p.getRow(true);
			if (!p.isEOF())
			{
				for (int k=0; k<l.length; k++)
				{
					if (k>0) w.print(',');
					w.print(l[k]);
				}
				w.println();
				//w.println(j.join(l, ","));
				//System.out.println(j.join(l, ","));
				//System.out.println(l[5]);
			}
		}
		r.close();		
		w.close();		
		
	}

}

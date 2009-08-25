package edu.emory.library.tast.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.ScrollableResults;
import org.hibernate.Session;

import au.com.bytecode.opencsv.CSVWriter;
import edu.emory.library.tast.AppConfig;
import edu.emory.library.tast.db.HibernateConn;
import edu.emory.library.tast.db.TastDbQuery;
import edu.emory.library.tast.dm.Dictionary;
import edu.emory.library.tast.dm.EditedVoyage;
import edu.emory.library.tast.dm.Submission;
import edu.emory.library.tast.dm.SubmissionNew;
import edu.emory.library.tast.dm.attributes.Attribute;

public class CSVUtils {
	
	private static String encoding = "UTF-8";
	private static class DictionaryInfo
	{
		public Class dictionary;
		public List attributes = new ArrayList();
	}

	private static DictionaryInfo[] getAllData(Session sess, TastDbQuery query, boolean useSQL, ZipOutputStream zipStream, boolean codes, String conditions) throws FileNotFoundException, IOException
	{

		SimpleDateFormat dateFormatter = new SimpleDateFormat(AppConfig.getConfiguration().getString(AppConfig.FORMAT_DATE_CVS));

		CSVWriter writer = new CSVWriter(new OutputStreamWriter(zipStream), ',');
		ScrollableResults queryResponse = null;

		Map dictionaries = new HashMap();

		try
		{
			queryResponse = query.executeScrollableQuery(sess, useSQL);

			Attribute[] populatedAttrs = query.getPopulatedAttributes();

			if (conditions != "")
			{
				String[] con = new String[1];
				con[0] = conditions;
				writer.writeNext(con);
			}
			
			String[] row = new String[populatedAttrs.length - 1];
			for (int i = 1; i < populatedAttrs.length; i++)
			{
				row[i - 1] = populatedAttrs[i].getName();
			}

			writer.writeNext(row);

			int cnt = 0;

			while (queryResponse.next())
			{

				cnt++;

				Object[] result = queryResponse.get();

				row = new String[populatedAttrs.length - 1];
				for (int j = 1; j < populatedAttrs.length; j++)
				{
					if (result[j] == null)
					{
						row[j - 1] = "";
					}
					else
					{
						if (!codes)
						{
							if (result[j] instanceof Date)
								row[j - 1] = dateFormatter.format(result[j]);
							else
								row[j - 1] = result[j].toString();
							if (result[j] instanceof Dictionary)
							{
								if (dictionaries.containsKey(populatedAttrs[j].toString()))
								{
									DictionaryInfo info = (DictionaryInfo) dictionaries.get(populatedAttrs[j].toString());
									if (!info.attributes.contains(populatedAttrs[j]))
									{
										info.attributes.add(populatedAttrs[j]);
									}
								}
								else
								{
									DictionaryInfo info = new DictionaryInfo();
									info.attributes.add(populatedAttrs[j]);
									info.dictionary = result[j].getClass();
									dictionaries.put(populatedAttrs[j].toString(), info);
								}
							}
						}
						else
						{
							if (result[j] instanceof Dictionary)
							{
								row[j - 1] = ((Dictionary) result[j]).getId().toString();
								if (dictionaries.containsKey(populatedAttrs[j].toString()))
								{
									DictionaryInfo info = (DictionaryInfo) dictionaries.get(populatedAttrs[j].toString());
									if (!info.attributes.contains(populatedAttrs[j]))
									{
										info.attributes.add(populatedAttrs[j]);
									}
								}
								else
								{
									DictionaryInfo info = new DictionaryInfo();
									info.attributes.add(populatedAttrs[j]);
									info.dictionary = result[j].getClass();
									dictionaries.put(populatedAttrs[j].toString(), info);
								}
							}
							else
							{
								if (result[j] instanceof Date)
									row[j - 1] = dateFormatter.format(result[j]);
								else
									row[j - 1] = result[j].toString();
							}
						}
					}
				}
				writer.writeNext(row);
			}

			writer.writeNext(new String[] { "The number of total records: " + cnt });

			writer.flush();
			return (DictionaryInfo[]) dictionaries.values().toArray(new DictionaryInfo[] {});

		}
		finally
		{
			if (queryResponse != null)
			{
				queryResponse.close();
			}
		}
	}
	
	/*
	 * This method retrieves data according to the query that is passed in and writes it to zipStream
	 * @param   boolean codes - whether you want the codes or labels in the file 	
	 */
	private static void getAllData(Session sess, TastDbQuery query, boolean useSQL, ZipOutputStream zipStream, boolean codes) throws FileNotFoundException, IOException
	{
		SimpleDateFormat dateFormatter = new SimpleDateFormat(AppConfig.getConfiguration().getString(AppConfig.FORMAT_DATE_CVS));
		//insert the bom - byte order marker
		final byte[] bom = new byte[] { (byte)0xEF, (byte)0xBB, (byte)0xBF };	
		zipStream.write(bom);
		CSVWriter writer = new CSVWriter(new OutputStreamWriter(zipStream, encoding), ',');
		
       	//TODO this snippet below is used for testing purposes only 
		/*File file = new File("c:\\tmp\\voyage.csv");
		FileOutputStream fout = new FileOutputStream(file);
		final byte[] bom = new byte[] { (byte)0xEF, (byte)0xBB, (byte)0xBF };	        	
	    fout.write(bom);	    
		CSVWriter writer = new CSVWriter(new OutputStreamWriter(fout, encoding), ',');*/
		
		ScrollableResults queryResponse = null;
		
		Map dictionaries = new HashMap();
		Map users = new HashMap();
		
		try
		{
			//query to retrieve users for the submissions 
			TastDbQuery q = new TastDbQuery(new String("SubmissionNew"));
			Object[] rslt = q.executeQuery(sess);
			if (rslt != null) {
				for (int i=0; i < rslt.length; i++) {
					Submission sub = (Submission)rslt[i];
					EditedVoyage voy= ((SubmissionNew) sub).getNewVoyage();
					String name = ((SubmissionNew) sub) .getUser().getUserName();					
					users.put(voy.getVoyage().getIid(), name);				
				}
			}
			//query for all the voyages
			queryResponse = query.executeScrollableQuery(sess, useSQL);
			
			Attribute[] populatedAttrs = query.getPopulatedAttributes();
			
			String[] row = new String[populatedAttrs.length + 1];
			int i;
			for (i = 0; i < populatedAttrs.length; i++) {
				row[i] = populatedAttrs[i].getName();				
			}
			row[i] = "username";
			writer.writeNext(row);
			
			int cnt = 0;
			String userName = null;
			while (queryResponse.next())
			{
				cnt++;
				Object[] result = queryResponse.get();

				row = new String[populatedAttrs.length + 1];				
				int j;
				for (j = 0;j < populatedAttrs.length; j++)
				{
					if (populatedAttrs[j].getName().equals("iid")) {
						userName = null;
						userName = (String)users.get(result[j]);					
					}
					if (result[j] == null)	{
						row[j] = "";
					}
					else if (result[j] instanceof Date) {	
						row[j] = dateFormatter.format(result[j]);
					}
					else if (codes) {
						if (result[j] instanceof Dictionary) {
							row[j] = ((Dictionary) result[j]).getId().toString();
						}
						else {
							row[j] = result[j].toString();
						}
					}
					else {//labels
						row[j] = result[j].toString();
					}					
				}
				if (userName != null) {
					row[j++] = userName;
				}
				writer.writeNext(row);
			}
			
			writer.flush();			
		}
		catch (IOException io)
		{
			io.printStackTrace();
		}		
		finally
		{
			if (queryResponse != null)
			{
				queryResponse.close();
			}
		}
	}

	public static void writeResponse(Session sess, TastDbQuery query, boolean useSQL, boolean codes, String conditions)
	{		
		ZipOutputStream zipOS = null;
		BufferedReader reader = null;
		
		try
		{			
			FacesContext fc = FacesContext.getCurrentInstance();
			HttpServletResponse response = (HttpServletResponse) fc.getExternalContext().getResponse();
			
			response.setContentType("application/zip");
			response.setHeader("content-disposition", "attachment; filename=data.zip");
			
			zipOS = new ZipOutputStream(response.getOutputStream());
			zipOS.putNextEntry(new ZipEntry("data.csv"));
			
			getAllData(sess, query, useSQL, zipOS,codes, conditions);
			
			// zipOS.putNextEntry(new ZipEntry("codebook.csv"));
			// getDictionaryInfo(zipOS, sess, dicts);
			
			zipOS.close();
			fc.responseComplete();

		}
		catch (IOException io)
		{
			io.printStackTrace();
		}
		catch (OutOfMemoryError e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (reader != null)
			{
				try
				{
					reader.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
			if (zipOS != null)
			{
				try
				{
					zipOS.flush();
					zipOS.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	/*
	 * This method writes response to data.csv and zips the output
	 * It is available to the browser via the download feature
	 */
	public static void writeResponse(Session sess, TastDbQuery query, boolean useSQL, boolean codes)
	{
		ZipOutputStream zipOS = null;
		BufferedReader reader = null;
		try
		{
			FacesContext fc = FacesContext.getCurrentInstance();
			HttpServletResponse response = (HttpServletResponse) fc.getExternalContext().getResponse();
			
			response.setContentType("application/zip");
			response.setHeader("content-disposition", "attachment; filename=data.zip");
			
			zipOS = new ZipOutputStream(response.getOutputStream());
			zipOS.putNextEntry(new ZipEntry("data.csv"));
			getAllData(sess, query, useSQL, zipOS, codes);
			zipOS.close();
			fc.responseComplete();
		}
		catch (IOException io)	{
			io.printStackTrace();
		}
		catch (OutOfMemoryError e) {
			e.printStackTrace();
		}
		finally	{
			if (reader != null)	{
				try	{
					reader.close();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (zipOS != null) {
				try	{
					zipOS.flush();
					zipOS.close();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/*
	private static void getDictionaryInfo(ZipOutputStream zipStream, Session session, DictionaryInfo[] dicts) {
		CSVWriter writer = new CSVWriter(new OutputStreamWriter(zipStream), ',');
		for (int i = 0; i < dicts.length; i++) {
			writer.writeNext(new String[] {"Attribute names:", decodeDictAttrs(dicts[i])});
			writer.writeNext(new String[] {"Code", "Name"});
			List object = Dictionary.loadAll(dicts[i].dictionary, session);
			for (Iterator iter = object.iterator(); iter.hasNext();) {
				Dictionary element = (Dictionary) iter.next();
				writer.writeNext(new String[] {element.getId().toString(), element.getName()});			
			}
			writer.writeNext(new String[] {});
		}
		try {
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	*/

	/*
	private static String decodeDictAttrs(DictionaryInfo info) {
		StringBuffer buffer = new StringBuffer();
		int i = 0;
		for (Iterator iter = info.attributes.iterator(); iter.hasNext();) {
			if (i != 0) {
				buffer.append(", ");
			}
			Attribute attr = (Attribute) iter.next();
			buffer.append(attr.getName());
			i++;
		}
		return buffer.toString();
	}
	*/

	public static void writeResponse(Session session, String[][] data)
	{
		ZipOutputStream zipOS = null;
		BufferedReader reader = null;

		try
		{
			FacesContext fc = FacesContext.getCurrentInstance();
			HttpServletResponse response = (HttpServletResponse) fc.getExternalContext().getResponse();
			response.setContentType("application/zip");
			response.setHeader("content-disposition", "attachment; filename=data.zip");
			zipOS = new ZipOutputStream(response.getOutputStream());
			zipOS.putNextEntry(new ZipEntry("data.csv"));
			CSVWriter writer = new CSVWriter(new OutputStreamWriter(zipOS), ',');
			for (int i = 0; i < data.length; i++)
			{
				writer.writeNext(data[i]);
			}
			writer.close();
			zipOS.close();
			fc.responseComplete();
		}
		catch (IOException io)
		{
			io.printStackTrace();
		}
		finally
		{
			if (reader != null)
			{
				try
				{
					reader.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
			if (zipOS != null)
			{
				try
				{
					zipOS.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
	}
	
}

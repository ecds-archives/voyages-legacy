package edu.emory.library.tast.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
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
import edu.emory.library.tast.db.TastDbQuery;
import edu.emory.library.tast.dm.Dictionary;
import edu.emory.library.tast.dm.attributes.Attribute;

public class CSVUtils {
	
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
		CSVWriter writer = new CSVWriter(new OutputStreamWriter(zipStream), ',');
		ScrollableResults queryResponse = null;
		
		Map dictionaries = new HashMap();

		try
		{
			queryResponse = query.executeScrollableQuery(sess, useSQL);
			
			Attribute[] populatedAttrs = query.getPopulatedAttributes();
			
			String[] str = {"voyageid", "suggestion", "revision", "iid", "adlt1imp*", "adlt2imp*", "adlt3imp*", "adpsale1", "adpsale2", "adult1", "adult2", "adult3", "adult4", "adult5", "adult6", "adult7*", "arrport", "arrport2", "boy1", "boy2", "boy3", "boy4", "boy5", "boy6", "boy7*", "boyrat1*", "boyrat3*", "boyrat7*", "captaina", "captainb", "captainc", "chil1imp*", "chil2imp*", "chil3imp*", "child1", "child2", "child3", "child4", "child5", "child6", "child7*", "chilrat1*", "chilrat3*", "chilrat7*", "constreg*", "crew", "crew1", "crew2", "crew3", "crew4", "crew5", "crewdied", "datedepa", "datedepb", "datedepc", "d1slatra", "d1slatrb", "d1slatrc", "dlslatra", "dlslatrb", "dlslatrc", "ddepam", "ddepamb", "ddepamc", "datarr32", "datarr33", "datarr34", "datarr36", "datarr37", "datarr38", "datarr39", "datarr40", "datarr41", "datarr43", "datarr44", "datarr45", "datedep", "datebuy", "dateleftafr", "dateland1", "dateland2", "dateland3", "datedepam", "dateend", "deptregimp*", "deptregimp1*", "embport", "embport2", "embreg*", "embreg2*", "evgreen", "fate", "fate2*", "fate3*", "fate4*", "female1", "female2", "female3", "female4", "female5", "female6", "female7", "feml1imp*", "feml2imp*", "feml3imp*", "girl1", "girl2", "girl3", "girl4", "girl5", "girl6", "girl7*", "girlrat1*", "girlrat3*", "girlrat7*", "guns", "infant1", "infant3", "infant4", "jamcaspr", "majbuypt", "majbyimp*", "majbyimp1*", "majselpt", "male1", "male1imp*", "male2", "male2imp*", "male3", "male3imp*", "male4", "male5", "male6", "male7*", "malrat1*", "malrat3*", "malrat7*", "men1", "men2", "men3", "men4", "men5", "men6", "men7*", "menrat1*", "menrat3*", "menrat7*", "mjbyptimp*", "mjselimp*", "mjselimp1*", "mjslptimp*", "natinimp*", "national", "ncar13", "ncar15", "ncar17", "ndesert", "npafttra", "nppretra", "npprior", "ownera", "ownerb", "ownerc", "ownerd", "ownere", "ownerf", "ownerg", "ownerh", "owneri", "ownerj", "ownerk", "ownerl", "ownerm", "ownern", "ownero", "ownerp", "plac1tra", "plac2tra", "plac3tra", "placcons", "placreg", "portdep", "portret", "ptdepimp*", "regarr*", "regarr2*", "regdis1*", "regdis2*", "regdis3*", "regem1*", "regem2*", "regem3*", "regisreg*", "resistance", "retrnreg*", "retrnreg1*", "rig", "saild1", "saild2", "saild3", "saild4", "saild5", "shipname", "sla1port", "slaarriv", "sladafri", "sladamer", "sladied1", "sladied2", "sladied3", "sladied4", "sladied5", "sladied6", "sladvoy", "slamimp*", "slas32", "slas36", "slas39", "slavema1*", "slavema3*", "slavema7*", "slavemx1*", "slavemx3*", "slavemx7*", "slavmax1*", "slavmax3*", "slavmax7*", "slaximp*", "slinten2", "slintend", "sourcea", "sourceb", "sourcec", "sourced", "sourcee", "sourcef", "sourceg", "sourceh", "sourcei", "sourcej", "sourcek", "sourcel", "sourcem", "sourcen", "sourceo", "sourcep", "sourceq", "sourcer", "tonmod*", "tonnage", "tontype", "tslavesd", "tslavesp", "tslmtimp*", "voy1imp*", "voy2imp*", "voyage", "vymrtimp*", "vymrtrat*", "women1", "women2", "women3", "women4", "women5", "women6", "women7*", "womrat1*", "womrat3*", "womrat7*", "xmimpflag*", "year10*", "year100*", "year25*", "year5*", "yearaf*", "yearam*", "yeardep*", "yrcons", "yrreg"};
			String[] row = new String[str.length];
			for (int i = 0; i < str.length; i++)
			{
				row[i ] = str[i];
			}

			writer.writeNext(row);
			int cnt = 0;

			while (queryResponse.next())
			{
				cnt++;
				Object[] result = queryResponse.get();

				row = new String[populatedAttrs.length];
				for (int j = 0; j < populatedAttrs.length; j++)
				{
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

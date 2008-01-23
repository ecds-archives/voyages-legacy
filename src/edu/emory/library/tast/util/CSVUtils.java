package edu.emory.library.tast.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.ScrollableResults;
import org.hibernate.Session;

import au.com.bytecode.opencsv.CSVWriter;
import edu.emory.library.tast.dm.Dictionary;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.util.query.QueryValue;

public class CSVUtils {
	
	private static class DictionaryInfo {
		public Class dictionary;
		public List attributes = new ArrayList();
	}
	
	private static DictionaryInfo[] getAllData(Session sess, QueryValue qValue, ZipOutputStream zipStream, boolean codes) throws FileNotFoundException, IOException {
		CSVWriter writer = new CSVWriter(new OutputStreamWriter(zipStream), ',');
		ScrollableResults queryResponse = null;
		
		Map dictionaries = new HashMap();
		
		try {
			queryResponse = qValue.executeScrollableQuery(sess);
			
			
			
			Attribute[] populatedAttrs = qValue.getPopulatedAttributes();
			String[] row = new String[populatedAttrs.length - 1];
			for (int i = 1; i < populatedAttrs.length; i++) {
				row[i - 1] = populatedAttrs[i].getName();
			}
			
			writer.writeNext(row);

			
			int cnt = 0; // cnt is a counter to indicate the row number of the table
			
			while (queryResponse.next()) {
				
				cnt++;
				
				Object[] result = queryResponse.get();
			
				row = new String[populatedAttrs.length - 1];
				for (int j = 1; j < populatedAttrs.length; j++) {
					if (result[j] == null) {
						row[j - 1] = "";
					} else {
						if (!codes) {
							row[j - 1] = result[j].toString();
							if (result[j] instanceof Dictionary) {
								if (dictionaries.containsKey(populatedAttrs[j].toString())) {
									DictionaryInfo info = (DictionaryInfo) dictionaries.get(populatedAttrs[j].toString());
									if (!info.attributes.contains(populatedAttrs[j])) {
										info.attributes.add(populatedAttrs[j]);
									}
								} else {
									DictionaryInfo info = new DictionaryInfo();
									info.attributes.add(populatedAttrs[j]);
									info.dictionary = result[j].getClass();
									dictionaries.put(populatedAttrs[j].toString(), info);
								}
							}
						} else {
							if (result[j] instanceof Dictionary) {
								row[j - 1] = ((Dictionary)result[j]).getId().toString();
								if (dictionaries.containsKey(populatedAttrs[j].toString())) {
									DictionaryInfo info = (DictionaryInfo) dictionaries.get(populatedAttrs[j].toString());
									if (!info.attributes.contains(populatedAttrs[j])) {
										info.attributes.add(populatedAttrs[j]);
									}
								} else {
									DictionaryInfo info = new DictionaryInfo();
									info.attributes.add(populatedAttrs[j]);
									info.dictionary = result[j].getClass();
									dictionaries.put(populatedAttrs[j].toString(), info);
								}
							} else {
								row[j - 1] = result[j].toString();
							}
						}
					}
				}
				writer.writeNext(row);
			}
			
			//this is to show the total number of rows in the downloaded file:
			writer.writeNext(new String[] {"The number of total records: " + cnt});

			writer.flush();
			return (DictionaryInfo[]) dictionaries.values().toArray(new DictionaryInfo[] {});
			
		} finally {
			if (queryResponse != null) {
				queryResponse.close();
			}
		}
	}

	public static void writeResponse(Session sess, QueryValue qValue) {
		writeResponse(sess, qValue, false);
	}
	
	public static void writeResponse(Session sess, QueryValue qValue, boolean codes) {
		
		ZipOutputStream zipOS = null;
		BufferedReader reader = null;
		
		try {
			FacesContext fc = FacesContext.getCurrentInstance();			
			HttpServletResponse response = (HttpServletResponse)fc.getExternalContext().getResponse();
			response.setContentType("application/zip");
			response.setHeader("content-disposition", "attachment; filename=data.zip");
			zipOS = new ZipOutputStream(response.getOutputStream());
			zipOS.putNextEntry(new ZipEntry("data.csv"));
			DictionaryInfo[] dicts = getAllData(sess, qValue, zipOS, codes);
			zipOS.putNextEntry(new ZipEntry("codebook.csv"));
			getDictionaryInfo(zipOS, sess, dicts);
			zipOS.close();
			fc.responseComplete();
		} catch (IOException io) {
			io.printStackTrace();
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (zipOS != null) {
				try {
					zipOS.flush();
					zipOS.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

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

	public static void writeResponse(Session session, String[][] data) {
		ZipOutputStream zipOS = null;
		BufferedReader reader = null;
		
		try {
			FacesContext fc = FacesContext.getCurrentInstance();			
			HttpServletResponse response = (HttpServletResponse)fc.getExternalContext().getResponse();
			response.setContentType("application/zip");
			response.setHeader("content-disposition", "attachment; filename=data.zip");
			zipOS = new ZipOutputStream(response.getOutputStream());
			zipOS.putNextEntry(new ZipEntry("data.csv"));
			CSVWriter writer = new CSVWriter(new OutputStreamWriter(zipOS), ',');
			for (int i = 0; i < data.length; i++) {
				writer.writeNext(data[i]);
			}
			writer.writeNext(new String[] {"The number of total records: "+(data.length-1)});
			writer.close();
			zipOS.close();
			fc.responseComplete();
		} catch (IOException io) {
			io.printStackTrace();
		}finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (zipOS != null) {
				try {
					zipOS.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}

package edu.emory.library.tast.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.ScrollableResults;
import org.hibernate.Session;

import au.com.bytecode.opencsv.CSVWriter;
import edu.emory.library.tast.dm.attributes.Attribute;
import edu.emory.library.tast.util.query.QueryValue;

public class CSVUtils {
	
	private static void getAllData(Session sess, QueryValue qValue, ZipOutputStream zipStream) throws FileNotFoundException, IOException {
		CSVWriter writer = new CSVWriter(new OutputStreamWriter(zipStream));
		ScrollableResults queryResponse = null;
		
		try {
			queryResponse = qValue.executeScrollableQuery(sess);
			Attribute[] populatedAttrs = qValue.getPopulatedAttributes();
			String[] row = new String[populatedAttrs.length - 1];
			for (int i = 1; i < populatedAttrs.length; i++) {
				row[i - 1] = populatedAttrs[i].getName();
			}

			writer.writeNext(row);

			while (queryResponse.next()) {
				Object[] result = queryResponse.get();
				row = new String[populatedAttrs.length - 1];
				for (int j = 1; j < row.length; j++) {
					if (result[j] == null) {
						row[j - 1] = "";
					} else {
						row[j - 1] = result[j].toString();
					}
				}
				writer.writeNext(row);
			}
		} finally {
			if (queryResponse != null) {
				queryResponse.close();
			}
			if (writer != null) {
				writer.close();
			}
		}
	}

	public static void writeResponse(Session sess, QueryValue qValue) {
		
		ZipOutputStream zipOS = null;
		BufferedReader reader = null;
		
		try {
			FacesContext fc = FacesContext.getCurrentInstance();			
			HttpServletResponse response = (HttpServletResponse)fc.getExternalContext().getResponse();
			response.setContentType("application/zip");
			response.setHeader("content-disposition", "attachment; filename=data.zip");
			zipOS = new ZipOutputStream(response.getOutputStream());
			zipOS.putNextEntry(new ZipEntry("data.csv"));
			getAllData(sess, qValue, zipOS);
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
					zipOS.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
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
			CSVWriter writer = new CSVWriter(new OutputStreamWriter(zipOS));
			for (int i = 0; i < data.length; i++) {
				writer.writeNext(data[i]);
			}
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

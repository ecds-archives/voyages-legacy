package edu.emory.library.tast.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.emory.library.tast.dm.SourceInformation;
import edu.emory.library.tast.util.query.QueryValue;

public class SourceInformationUtils {
	
	public char[] stopChars = new char[] {';', ',', ':'};
	
	private SourceInformation[] sources;
	private String[] sourceNames;
	private SourceIndexPosition[] index;
	
	private class SourceIndexPosition implements Comparable {
		
		private String sourceName;
		private int position;
		
		public SourceIndexPosition(String sourceName, int listPosition) {
			this.sourceName = sourceName;
			this.position = listPosition;
		}

		public int getPosition() {
			return position;
		}

		public String getSourceName() {
			return sourceName;
		}

		public int compareTo(Object arg0) {
			return this.sourceName.compareTo(((SourceIndexPosition)arg0).getSourceName());
		}
		
	}
	
	private SourceInformationUtils() {
		QueryValue qValue = new QueryValue("SourceInformation");
		Object[] response = qValue.executeQuery();
		sources = new SourceInformation[response.length];
		index = new SourceIndexPosition[sources.length];
		for (int i = 0; i < response.length; i++) {
			sources[i] = (SourceInformation) response[i];
			index[i] = new SourceIndexPosition(sources[i].getId(), i);
		}
		
		Arrays.sort(index);
		
		sourceNames = new String[response.length];
		for (int i = 0; i < index.length; i++) {
			sourceNames[i] = index[i].getSourceName();
		}
	}
	
	public static SourceInformationUtils createSourceInformationUtils() {
		return new SourceInformationUtils();
	}
	
	public SourceInformation match(String source) {
		if (source == null) {
			return null;
		}
		int bestMatch = Arrays.binarySearch(sourceNames, source);
		if (bestMatch < 0) {
			bestMatch = -(bestMatch) - 2; 
		}
		if (bestMatch >= 0 && source.startsWith(sources[index[bestMatch].getPosition()].getId())) {
			return sources[index[bestMatch].getPosition()];
		} else {
			return searchSmallerMatch(bestMatch, source);
		}
	}
	
	public SourceInformation searchSmallerMatch(int position, String source) {
		if (position > 0) {
			int testPosition = position - 1;
			if (sourceNames[position].startsWith(sourceNames[testPosition].substring(0, 1))) {
				if (source.startsWith(sources[index[testPosition].getPosition()].getId())) {
					return sources[index[testPosition].getPosition()];
				} else {
					return searchSmallerMatch(testPosition, source);
				}
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

//	private int findStopCharMatch(String string) {
//		int matchIndex = string.length();
//		for (int i = 0; i < stopChars.length; i++) {
//			if (string.indexOf(stopChars[i]) < matchIndex && string.indexOf(stopChars[i]) >= 0) {
//				matchIndex = string.indexOf(stopChars[i]);
//			}
//		}
//		return matchIndex;
//	}
	
	public static void main(String[] args) {
		Session session = HibernateUtil.getSession();
		Transaction t = session.beginTransaction();
		try {
			FileOutputStream os = new FileOutputStream("c:\\sources-matched.csv");
			os.write("\"source name\", \"source code (from rollovers file)\", \"source rollover\"\n".getBytes());
			SourceInformationUtils utils = SourceInformationUtils.createSourceInformationUtils();
			List response = session.createSQLQuery("select distinct a.s from (select distinct sourcea as s from voyages where revision=1 union select distinct sourceb as s from voyages where revision=1 union select distinct sourcec as s from voyages where revision=1 union select distinct sourced as s from voyages where revision=1 union select distinct sourcee as s from voyages where revision=1 union select distinct sourcef as s from voyages where revision=1 union select distinct sourceg as s from voyages where revision=1 union select distinct sourceh as s from voyages where revision=1 union select distinct sourcei as s from voyages where revision=1 union select distinct sourcej as s from voyages where revision=1 union select distinct sourcek as s from voyages where revision=1 union select distinct sourcel as s from voyages where revision=1 union select distinct sourcem as s from voyages where revision=1 union select distinct sourcen as s from voyages where revision=1 union select distinct sourceo as s from voyages where revision=1 union select distinct sourcep as s from voyages where revision=1 union select distinct sourceq as s from voyages where revision=1 union select distinct sourcer as s from voyages where revision=1) a order by a.s").list();
			for (Iterator iter = response.iterator(); iter.hasNext();) {
				String source = (String) iter.next();
				SourceInformation info = utils.match(source);
				if (info != null) {
					os.write(("\"" + source + "\",\"" + info.getId() + "\",\"" + info.getInformation() + "\"\n").getBytes());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			t.commit();
			session.close();
		}
	}
	
}

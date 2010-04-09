/*
Copyright 2010 Emory University
	
	    This file is part of Trans-Atlantic Slave Voyages.
	
	    Trans-Atlantic Slave Voyages is free software: you can redistribute it and/or modify
	    it under the terms of the GNU General Public License as published by
	    the Free Software Foundation, either version 3 of the License, or
	    (at your option) any later version.
	
	    Trans-Atlantic Slave Voyages is distributed in the hope that it will be useful,
	    but WITHOUT ANY WARRANTY; without even the implied warranty of
	    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	    GNU General Public License for more details.
	
	    You should have received a copy of the GNU General Public License
	    along with Trans-Atlantic Slave Voyages.  If not, see <http://www.gnu.org/licenses/>. 
*/
package edu.emory.library.tast.database;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import edu.emory.library.tast.dm.Source;

public class SourceInformationLookup {
	
	private static SourceInformationLookup cachedInst = null;
	
	public char[] stopChars = new char[] {';', ',', ':'};
	
	private Source[] sources;
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
	
	private SourceInformationLookup(Session sess)
	{
		List response = sess.createCriteria(Source.class, "s").add(
				Restrictions.not(Restrictions.isNull("s.sourceId"))).list();
		sources = new Source[response.size()];
		index = new SourceIndexPosition[response.size()];
		int i = 0;
		for (Iterator sourceIt = response.iterator(); sourceIt.hasNext();)
		{
			Source source = (Source) sourceIt.next();
			sources[i] = source;
			index[i] = new SourceIndexPosition(source.getSourceId(), i);
			i++;
		}
		
		Arrays.sort(index);
		
		sourceNames = new String[response.size()];
		for (int j = 0; j < index.length; j++) {
			sourceNames[j] = index[j].getSourceName();
		}
	}
	
	public synchronized static SourceInformationLookup createSourceInformationUtils(Session sess)
	{
		if (cachedInst == null) cachedInst = new SourceInformationLookup(sess); 
		return cachedInst;
	}
	
	public Source match(String source) {
		if (source == null) {
			return null;
		}
		int bestMatch = Arrays.binarySearch(sourceNames, source);
		if (bestMatch < 0) {
			bestMatch = -(bestMatch) - 2; 
		}
		if (bestMatch >= 0 && source.startsWith(sources[index[bestMatch].getPosition()].getSourceId())) {
			return sources[index[bestMatch].getPosition()];
		} else {
			return searchSmallerMatch(bestMatch, source);
		}
	}
	
	public Source searchSmallerMatch(int position, String source) {
		if (position > 0) {
			int testPosition = position - 1;
			if (sourceNames[position].startsWith(sourceNames[testPosition].substring(0, 1))) {
				if (source.startsWith(sources[index[testPosition].getPosition()].getSourceId())) {
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
	
//	public static void main(String[] args) {
//		Session session = HibernateUtil.getSession();
//		Transaction t = session.beginTransaction();
//		try {
//			FileOutputStream os = new FileOutputStream("c:\\sources-matched.csv");
//			FileOutputStream osnm = new FileOutputStream("c:\\sources-not-matched.csv");
//			os.write("\"source name\", \"source code (from rollovers file)\", \"source rollover\"\n".getBytes());
//			SourceInformationUtils utils = SourceInformationUtils.createSourceInformationUtils();
//			List response = session.createSQLQuery("select distinct a.s from (select distinct sourcea as s from voyages where revision=1 union select distinct sourceb as s from voyages where revision=1 union select distinct sourcec as s from voyages where revision=1 union select distinct sourced as s from voyages where revision=1 union select distinct sourcee as s from voyages where revision=1 union select distinct sourcef as s from voyages where revision=1 union select distinct sourceg as s from voyages where revision=1 union select distinct sourceh as s from voyages where revision=1 union select distinct sourcei as s from voyages where revision=1 union select distinct sourcej as s from voyages where revision=1 union select distinct sourcek as s from voyages where revision=1 union select distinct sourcel as s from voyages where revision=1 union select distinct sourcem as s from voyages where revision=1 union select distinct sourcen as s from voyages where revision=1 union select distinct sourceo as s from voyages where revision=1 union select distinct sourcep as s from voyages where revision=1 union select distinct sourceq as s from voyages where revision=1 union select distinct sourcer as s from voyages where revision=1) a order by a.s").list();
//			for (Iterator iter = response.iterator(); iter.hasNext();) {
//				String source = (String) iter.next();
//				SourceInformation info = utils.match(source);
//				if (info != null) {
//					os.write(("\"" + source + "\",\"" + info.getId() + "\",\"" + info.getInformation() + "\"\n").getBytes());
//				} else if (source != null) {
//					osnm.write(("\"" + source + "\"\n").getBytes());
//				}
//			}
//			
//			os.flush();
//			osnm.flush();
//			os.close();
//			osnm.close();
//			
//		} catch (IOException e) {
//			e.printStackTrace();
//		} finally {
//			t.commit();
//			session.close();
//		}
//	}
	
}

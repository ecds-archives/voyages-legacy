package edu.emory.library.tast.maps.mapfile;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.emory.library.tast.AppConfig;

public class MapSchemaReader {

//	private static String MAP_FILE_SKELETON = AppConfig.getConfiguration().getString(AppConfig.MAP_FILE_SKELETON);

	private static String PARAM_REGEX = "\\#\\{.*\\}";

	private class ChangeableItem {
		public Integer index;
		public String key;
		public ChangeableItem(Integer index, String key) {
			this.index = index;
			this.key = key;
		}
	}
	
	private StringBuffer file = new StringBuffer();

	private Map markers = new HashMap();
	
	private List revMarkers = new ArrayList();

	private Map modifications = new HashMap();
	
	public MapSchemaReader() {

	}

	public boolean beginReading() {
		BufferedReader reader = null;
		BufferedWriter writer = null;
//		try {
//			reader = new BufferedReader(new FileReader(MAP_FILE_SKELETON));
//			String line = null;
//
//			while ((line = reader.readLine()) != null) {
//				file.append(line).append("\n");
//			}
//
//			Pattern pattern = Pattern.compile(PARAM_REGEX);
//			Matcher matcher = pattern.matcher(file);
//
//			while (matcher.find()) {
//				//System.out.println(matcher.group());
//				int matchIndex = matcher.end();
//				Integer index = new Integer(matchIndex - matcher.group().length());
//				if (!markers.containsKey(matcher.group())) {
//					ArrayList list = new ArrayList();
//					list.add(index);
//					markers.put(matcher.group(), list);
//				} else {
//					ArrayList list = (ArrayList)markers.get(matcher.group());
//					list.add(index);
//				}
//				this.revMarkers.add(new ChangeableItem(index, matcher.group()));
//			}
//
//			return true;
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} finally {
//			try {
//				if (writer != null) {
//					writer.flush();
//					writer.close();
//				}
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			try {
//				if (reader != null) {
//					reader.close();
//				}
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
		return false;
	}
	
	public String[] markerKeys() {
		return (String[])this.markers.keySet().toArray(new String[] {});
	}
	
	public void clearModifications() {
		this.modifications.clear();
	}
	
	public void addBlockModification(String markerBegin, String markerEnd, String substitution) {
		this.modifications.put(markerBegin, new BlockModification(markerBegin, markerEnd, substitution));
	}
	
	public void addSimpleModification(String markerKey, String substitution) {
		this.modifications.put(markerKey, new SimpleModification(markerKey, substitution));
	}
	
	public StringBuffer applyModifications() {
		StringBuffer copy = new StringBuffer(this.file.toString());
		
		int offset = 0;
		for (Iterator iter = this.revMarkers.iterator(); iter.hasNext();) {
			ChangeableItem item = (ChangeableItem)iter.next();
			Modification element = (Modification) this.modifications.get(item.key);
			if (element != null) {
				offset = element.apply(copy, item.index ,this.markers, offset);
			}
		}
		return copy;
	}
}

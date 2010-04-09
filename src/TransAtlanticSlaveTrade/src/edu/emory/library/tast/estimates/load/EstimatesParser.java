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
package edu.emory.library.tast.estimates.load;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EstimatesParser {

	public static EstimatesPosition[] parse(File file) throws IOException {
		if (!file.exists() || !file.canRead()) {
			throw new RuntimeException("File " + file
					+ " does not exist or cannot be opened");
		}
		List estimates = new ArrayList();
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line;
		while ((line = reader.readLine()) != null) {
			line = line.replaceAll("\\(.*\\)", "");
			String[] columns = line.split(";");
			if (columns.length >= 6) {
				try {
					String[] nations = columns[0].replaceAll("\"", "").split(",");
					for (int j = 0; j < nations.length; j++) {

						int from = Integer.parseInt(columns[1].trim());
						int to = Integer.parseInt(columns[2].trim());
						int natimp = Integer.parseInt(nations[j].trim());

						String exp = columns[5].replaceAll("\"", "");
						String imp = columns[6].replaceAll("\"", "");
						String prop = columns[7].replaceAll("\"", "");
						if (columns[3].trim().compareTo("") != 0) {
							String[] portdepts = columns[3]
									.replaceAll("\"", "").split(",");
							int[] ports = resolveNumbers(portdepts);

							for (int i = from; i <= to; i++) {
								estimates.add(new EstimatesPosition(natimp,
										ports, null, i, exp, imp, prop));
							}
						} else if (columns[4].trim().compareTo("") != 0) {
							String[] majselimps = columns[4].replaceAll("\"",
									"").split(",");
							int[] selpts = resolveNumbers(majselimps);

							for (int i = from; i <= to; i++) {
								estimates.add(new EstimatesPosition(natimp,
										null, selpts, i, exp, imp, prop));
							}
						} else {
							for (int i = from; i <= to; i++) {
								estimates.add(new EstimatesPosition(natimp,
										null, null, i, exp, imp, prop));
							}
						}
						System.out.println(" has: " + from + " " + to + "; nation: " + nations[j]);
						System.out.println("    Line: " + line);
					}
				} catch (NumberFormatException e) {
					System.out.println("Skipped line: " + line);
				}
			} else {
				System.out.println("Skipped line: " + line);
			}
		}

		return (EstimatesPosition[]) estimates
				.toArray(new EstimatesPosition[] {});
	}

	private static int[] resolveNumbers(String[] majselimps) {
		ArrayList lens = new ArrayList();
		for (int i = 0; i < majselimps.length; i++) {
			String str = majselimps[i];
			if (str.indexOf(" thru ") != -1) {
				str = str.replaceAll("  ", " ");
				String[] range = str.split(" ");
				int a = Integer.parseInt(range[0].trim());
				int b = Integer.parseInt(range[2].trim());
				for (int j = a; j <= b; j++) {
					lens.add(new Integer(j));
				}
			} else {
				lens.add(new Integer(str.trim()));
			}
		}
		int[] ret = new int[lens.size()];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = ((Integer)lens.get(i)).intValue();
		}
		return ret;
	}

}

package edu.emory.library.tast.estimates.load;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
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
			String[] columns = line.split(";");
			if (columns.length >= 6) {
				try {
					String[] nations = columns[0].replaceAll("\"", "").split(",");
					for (int j = 0; j < nations.length; j++) {

						int from = Integer.parseInt(columns[1]);
						int to = Integer.parseInt(columns[2]);
						int natimp = Integer.parseInt(nations[j]);

						String exp = columns[5].replaceAll("\"", "");
						String imp = columns[6].replaceAll("\"", "");
						String prop = columns[7].replaceAll("\"", "");
						if (columns[3].trim().compareTo("") != 0) {
							String[] portdepts = columns[3]
									.replaceAll("\"", "").split(",");
							int[] ports = new int[portdepts.length];
							for (int n = 0; n < portdepts.length; n++) {
								int portdep = Integer.parseInt(portdepts[n]);
								ports[n] = portdep;
							}
							for (int i = from; i <= to; i++) {
								estimates.add(new EstimatesPosition(natimp,
										ports, null, i, exp, imp, prop));
							}
						} else if (columns[4].trim().compareTo("") != 0) {
							String[] majselimps = columns[4].replaceAll("\"",
									"").split(",");
							int[] selpts = new int[majselimps.length];
							for (int n = 0; n < majselimps.length; n++) {
								int majselimp = Integer.parseInt(majselimps[n]);
								selpts[n] = majselimp;
							}
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

}

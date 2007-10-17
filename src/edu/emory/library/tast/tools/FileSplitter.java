package edu.emory.library.tast.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileSplitter {

	public static String outFile =  "PART_";
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		File file = new File(args[0]);
		int fileName = 0;
		int index = 0;
		BufferedReader reader = new BufferedReader(new FileReader(file));
		BufferedWriter writer = new BufferedWriter(new FileWriter(outFile + fileName + ".txt"));
		String line;
		while ((line = reader.readLine()) != null) {
			
			StringBuffer buffer = new StringBuffer(line);
			while (line.trim().charAt(line.trim().length()-1) != ';') {
				line = reader.readLine();
				buffer.append(line);
			}
			
			if (index == 2000) {
				fileName++;
				index = 0;
				writer.flush();
				writer.close();
				writer = new BufferedWriter(new FileWriter(outFile + fileName + ".txt"));
			}
			writer.write(buffer.toString() + "\n");
			index++;
		}
		
		writer.flush();
		writer.close();

	}

}

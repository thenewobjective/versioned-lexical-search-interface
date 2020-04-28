package edu.uwm.cs.lexical_search.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

public class FileManagement {
	public String openFile(String path) throws IOException {
		//Useless comment
		String textString = "Init";
		if ((path == null) || (path.length() == 0)) return "Failed name";

		File file = new File(path);
		if (!file.exists()) {
			System.out.println("File doesn't exist");
			return "File doesnt exist";
		}

		FileInputStream stream = new FileInputStream(file.getPath());
		Reader in = new BufferedReader(new InputStreamReader(stream));
		char[] readBuffer= new char[2048];
		StringBuffer buffer= new StringBuffer((int) file.length());
		int n;
		while ((n = in.read(readBuffer)) > 0) {
			buffer.append(readBuffer, 0, n);
		}
		textString = buffer.toString();
		in.close();

		return textString;	
	}
}

package edu.brown.cs.voice2text;

import static edu.brown.cs.voice2text.config.RecognitionConfiguration.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.apache.commons.lang3.StringUtils;

public class ConfigReader {
	public ConfigReader() {}
	
	public List<String> readContext() {
		List<String> context = new ArrayList<String>();
		try {
			Scanner scanner = new Scanner(new File(contextLocation));
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				if (StringUtils.isBlank(line)) continue;
				if (line.startsWith("//")) continue;
				context.add(line);
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return context;
	}
	
	public static void main(String[] args) {
		ConfigReader cr = new ConfigReader();
		System.out.println(cr.readContext());
	}
}

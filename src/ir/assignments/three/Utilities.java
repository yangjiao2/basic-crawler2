package ir.assignments.three;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * A collection of utility methods for text processing.
 */
public class Utilities {
	/**
	 * Reads the input text file and splits it into alphanumeric tokens.
	 * Returns an ArrayList of these tokens, ordered according to their
	 * occurrence in the original text file.
	 * 
	 * Non-alphanumeric characters delineate tokens, and are discarded.
	 *
	 * Words are also normalized to lower case. 
	 * 
	 * Example:
	 * 
	 * Given this input string
	 * "An input string, this is! (or is it?)"
	 * 
	 * The output list of strings should be
	 * ["an", "input", "string", "this", "is", "or", "is", "it"]
	 * 
	 * @param input The file to read in and tokenize.
	 * @return The list of tokens (words) from the input file, ordered by occurrence.
	 */
	private static ArrayList<String> token = new ArrayList<String>();
	public static ArrayList<String> tokenizeFile(File input) {
		// TODO Write body!
;
		Scanner scanner;
		try {
			scanner = new Scanner(input);
			scanner.useDelimiter("[^a-z|A-Z|0-9|\'|-]+");
			while(scanner.hasNext()){
				String s = scanner.next().toLowerCase();
				s = s.replaceAll("[^a-z|0-9|\'|-]+", ""); // [^a-z|0-9|\'|-]+", "");^[^a-zA-Z0-9\\s]+|[^a-zA-Z0-9\\s]+$
				token.add(s);
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return token;

	}

	/**
	 * Takes a list of {@link Frequency}s and prints it to standard out. It also
	 * prints out the total number of items, and the total number of unique items.
	 * 
	 * Example one:
	 * 
	 * Given the input list of word frequencies
	 * ["sentence:2", "the:1", "this:1", "repeats:1",  "word:1"]
	 * 
	 * The following should be printed to standard out
	 * 
	 * Total item count: 6
	 * Unique item count: 5
	 * 
	 * sentence	2
	 * the		1
	 * this		1
	 * repeats	1
	 * word		1
	 * 
	 * 
	 * Example two:
	 * 
	 * Given the input list of 2-gram frequencies
	 * ["you think:2", "how you:1", "know how:1", "think you:1", "you know:1"]
	 * 
	 * The following should be printed to standard out
	 * 
	 * Total 2-gram count: 6
	 * Unique 2-gram count: 5
	 * 
	 * you think	2
	 * how you		1
	 * know how		1
	 * think you	1
	 * you know		1
	 * 
	 * @param frequencies A list of frequencies.
	 */
	public static void printFrequencies(List<Frequency> frequencies) {
		
		int total = 0;
		for (Frequency wf : frequencies){
			total += wf.getFrequency();
		}

		int max=0;
		for(int i=0; i<=frequencies.size()-1; i++){
			if(frequencies.get(i).getText().length() > max){
				max=frequencies.get(i).getText().length();
			}
		}
		
		String space = Integer.toString(max + 3);
		
		
		if(frequencies.get(0).getText().contains(" ")){
			System.out.println("Total 2-gram count:"+total);
			System.out.println("Unique 2-gram count:"+frequencies.size()+ "\n");
		}else{
			System.out.println("Total item count:"+total);
			System.out.println("Unique item count:"+frequencies.size() + "\n");
		}
		for (Frequency wf : frequencies){
			System.out.format("%-" + space + "s%d\n", wf.getText(), wf.getFrequency());	
		}

	}
}

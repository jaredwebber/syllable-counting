/*
 * REQUIRES SyllableAlgorithm.java file to run
 * 
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
	
public class SyllablesFromFile {
	
	/**
	 * Main method, decide a file in local directory to read in words one at a time
	 * (words must be on individual lines)
	 * Output is via file designated by PrintWriter, includes the initial word, along
	 * with the calculated number of syllables
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		int numSyllables = 0;
		String word;
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader("../input.txt"));
			PrintWriter writer = new PrintWriter("../output.txt");
			word = reader.readLine();
			while(word != null) {
				numSyllables = 0;
				//convert entered word to lower case
				word = word.toLowerCase();
				
				if(SyllableAlgorithm.isValidWord(word)) {
					//check if word is comprised entirely of vowels
					if(SyllableAlgorithm.allVowels(word)) {
						numSyllables = word.length();
					}
					else {
						if(word.length()>1) {
							numSyllables+=SyllableAlgorithm.checkVowelConsonant(word);
							numSyllables+=SyllableAlgorithm.checkAI(word);
							numSyllables+=SyllableAlgorithm.checkEO(word);
							numSyllables+=SyllableAlgorithm.checkIousIer(word);
							numSyllables-=SyllableAlgorithm.checkESED(word);
							numSyllables+=SyllableAlgorithm.checkFinal(word);
							
							//If deductions brought the count negative, make it 1 
							if(numSyllables<1) {numSyllables = 1;}
						}
						else {
							numSyllables = 1;
						}
					}
					writer.println(SyllableAlgorithm.capitalize(word)+": "+numSyllables);
				}
				word = reader.readLine();
			}
			reader.close();
			writer.close();
		
		}catch(IOException e){
			e.printStackTrace();
			System.out.println("error detected");
		}
		
	}
}

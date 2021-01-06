/*
 * Currently Following the algorithm found here:
 * 		http://jzimba.blogspot.com/2017/07/an-algorithm-for-counting-syllables.html
 * along with personal additions and modifications to improve accuracy
 * 
 * Java implementation of written language rules, many of the titled methods
 * have exceptions and other comparisons taking place within, not necessarily
 * only performing their titled task
 * 
 */

import javax.swing.JOptionPane;
import javax.swing.JFrame;

public class SyllableAlgorithm {
	private final static char[] VOWELS = {'a', 'e', 'i', 'o', 'u', 'y'};
	private final static JFrame frame = new JFrame("Message Title");
	private static boolean active = true;//program has a legitimate word entered, and hasnt quit
	
	/**
	 * The main method.
	 *
	 * @param args the argument
	 */
	public static void main(String[] args) {
		while(active) {
			runAlgorithm();
		}
		System.exit(0);
	}
	
		
	/**
	 * Method responsible for full execution of the program
	 */
	private static void runAlgorithm() {
		int numSyllables = 0;
		String word = getWord();
		
		if(word == null) {//stops program & exits
			active = false;
		}
		else {
			//convert entered word to lower case
			word = word.toLowerCase();
			
			if(isValidWord(word)) {
				//check if word is comprised entirely of vowels
				if(allVowels(word)) {
					numSyllables = word.length();
				}
				else {
					if(word.length()>1) {
						numSyllables+=checkVowelConsonant(word);
						//System.out.println("VC"+checkVowelConsonant(word));
						numSyllables+=checkAI(word);
						//System.out.println("AI"+checkAI(word));
						numSyllables+=checkEO(word);
						//System.out.println("EO"+checkEO(word));
						numSyllables+=checkIousIer(word);
						//System.out.println("ious,ier"+checkIousIer(word));
						numSyllables-=checkESED(word);
						//System.out.println("es-"+checkESED(word));
						numSyllables+=checkFinal(word);
						//System.out.println("final"+checkFinal(word));
						//System.out.println("--------------");
						
						//If deductions brought the count negative, make it 1 
						if(numSyllables<1) {numSyllables = 1;}
					}
					else {
						numSyllables = 1;
					}
				}
				printResults(word, numSyllables);
			}
			
		}
		
	}
	
	/**
	 * Check final word-ending possibilities
	 *
	 * @param the word
	 * @return the number of syllables to be added to total
	 */
	//Method checking 'final' syllables of word
	private static int checkFinal(String word) {
		String pairs[] = {"ea", "ii", "io", "ua", "uo"};//already checked IA and EO
		int value = 1;
		
		if(!isVowel(word.charAt(word.length()-1))) {
			value = 0;
		}
		
		if(!isVowel(word.charAt(word.length()-2)) && word.charAt(word.length()-1) == 'e') {
			value = 0;
		}
		if(word.length()>2) {
			String comp = word.substring(word.length()-2);

			for(int i = 0; i<pairs.length; i++) {
				if(comp.equals(pairs[i])) {
					value = 2;
				}
			}
		}
		//CUSTOM RULES
		if(word.length()>3) {
			String checkITY = "ity";
			String checkBLE = "ble";
			String comp = word.substring(word.length()-3);
			if(comp.equals(checkITY)) {
				value = 2;
			}
			if(comp.equals(checkBLE)) {
				value = 1;
			}
		}
		if(word.length()>4) {
			String checkIAL = "rial";
			String comp = word.substring(word.length()-4);
			if(comp.equals(checkIAL)) {
			//2 syllables, minus the syllable accounted for AL (in vowel-consonant)
				value = 1;
			}
		}
		
		return value;
	}
	
	/**
	 * Check ending of word for ES and ED
	 *
	 * @param the word
	 * @return the number of syllables to be subtracted from total
	 */
	private static int checkESED(String word) {
		int value = 0;
		boolean endsES = false;
		
		//check ES ending
		if(word.length()>2) {
			if(word.charAt(word.length()-2) == 'e' && word.charAt(word.length()-1)== 's') {
				endsES = true;
				value = 1;
			}
			if(endsES && word.length()>=3) {
				char e3 = word.charAt(word.length()-3);//end-3 char
				if(e3 == 'c' || e3 == 'g' || e3 == 'x' || e3 =='s' || e3 == 'z' || e3 == 'i') {//CUSTOM RULE 'i'
					value = 0;
				}
				
				if(endsES && word.length()>=4) {
					if(!isVowel(word.charAt(word.length()-4)) && e3 == 'l') {
						value = 0;
					}
				}
				
			}
			
		}
		
		//Check ED ending
		if(word.length()>3) {
			if(word.charAt(word.length()-2) == 'e' && word.charAt(word.length()-1) == 'd') {
				value = 1;
				if(word.charAt(word.length()-3) == 'd' || word.charAt(word.length()-3) == 't') {
					value = 0;
				}
				else if(word.length()>4) {
					if(word.charAt(word.length()-4) != 'r' && word.charAt(word.length()-3) == 'r') {
						value = 0;
					}
					else if(!isVowel(word.charAt(word.length()-4)) && word.charAt(word.length()-3) == 'l') {
						value = 0;
					}
				}
			}
		}
		
		return value;
	}
	
	/**
	 * Check word for ending IOUS and IER
	 *
	 * @param the word
	 * @return the number of syllables to be added to total
	 */
	private static int checkIousIer(String word) {
		int value = 0;
		
		//Check for ious ending
		if(word.length()>=4) {
			String sub = word.substring(word.length()-4, word.length());
			String check = "ious";
			if(sub.equals(check)) {
				value = 1;
			}
			//CUSTOM RULE ADDED
			if(word.length()>5) {
				if(word.charAt(word.length()-5) == 'c') {
					value = 0;
				}
			}
		}
		//Check for ier ending
		if(word.length()>=3) {
			String sub = word.substring(word.length()-3, word.length());
			String check = "ier";
			if(sub.equals(check)) {
				value = 1;
			}
		}
			
		return value;
	}
	
	/**
	 * Check word for pair of consecutive letters AI 
	 *
	 * @param the word
	 * @return the number of syllables to be added to total
	 */
	private static int checkAI(String word) {
		int value = 0;
		for(int i = 1; i<word.length(); i++) {
			if((word.charAt(i-1) == 'a') && word.charAt(i) == 'i') {
				value ++;
				//CUSTOM RULE - should be d or all consonants?
				if(i<word.length()-1) {
					if(word.charAt(i+1) == 'd') {
						value--;
					}
				}
			}
		}

		return value;
	}
	
	/**
	 * Check word for consecutive letters EO
	 *
	 * @param the word
	 * @return the number of syllables to be added to total
	 */
	private static int checkEO(String word) {
		int value = 0;
		for(int i = 1; i<word.length(); i++) {
			if((word.charAt(i-1) == 'E' || word.charAt(i-1) == 'e') && (word.charAt(i) == 'O' || word.charAt(i) == 'o')) {
				value++;
				System.out.print(word.charAt(i-1)+word.charAt(i));
			}
		}
		
		return value;
	}
	
	/**
	 * Check word for ordered pair (vowel,consonant)
	 *
	 * @param the word
	 * @return the number of syllables to be added to total
	 */
	private static int checkVowelConsonant(String word) {
		int value = 0;
		for(int i = 1; i<word.length(); i++) {
			if(isVowel(word.charAt(i-1)) && !isVowel(word.charAt(i))) {
				value++;
			}
		}
		
		return value;
	}
	
	/**
	 * Checks if word is comprised entirely of letters
	 *
	 * @param the word
	 * @return true, if is valid word
	 */
	//Method verifies each character of input word is a letter
	private static boolean isValidWord(String word) {
		boolean out = true;
		
		if(word.length() ==0) {
			out = false;
		}
		
		for(int i = 0; i<word.length(); i++) {
			if(!Character.isLetter(word.charAt(i))) {
				out = false;
			}
		}
	
		return out;
	}
	
	/**
	 * Prints the results in popup
	 *
	 * @param word the word entered
	 * @param syllables the calculated number of syllables
	 */
	//Method which delivers a pop-up with the calculated number of syllables
	private static void printResults(String word, int syllables) {
		if(!isValidWord(word)) {
			JOptionPane.showMessageDialog(frame, "Invalid Word Received.");
		}
		else {
			JOptionPane.showMessageDialog(frame, "The calculated number of syllables in '"+capitalize(word)+"' is "+syllables+".");	
		}
	}
	
	/**
	 * Gets the word. Asks the user for a word using pop-up JOptionPane.
	 *
	 * @return the word
	 */
	private static String getWord() {
		String word = null;
		word = JOptionPane.showInputDialog(frame, "Enter your word here:\n(cancel to quit)", JOptionPane.QUESTION_MESSAGE);
		
		return word;
	}
	
	/**
	 * Capitalizes the first letter of passed string
	 *
	 * @param word, the string which will be capitalized
	 * @return the newly capitalized string
	 */
	private static String capitalize(String word) {
		return word.substring(0, 1).toUpperCase()+word.substring(1);
	}
	
	/**
	 * Checks if entire string, every char, are all vowels
	 *
	 * @param word, the string which will be checked for vowels
	 * @return true, if all letters in @param are vowels
	 */
	private static boolean allVowels(String word) {
		int numVowels = 0;
		for(int i = 0; i<word.length(); i++) {
			if(isVowel(word.charAt(i))) {
				numVowels++;
			}
		}
		
		if(numVowels == word.length()) {return true;}
		
		return false;
	}

	/**
	 * Checks if passed character is a vowel
	 *
	 * @param c, character which will be verified whether or not its a vowel
	 * @return true, if is vowel
	 */
	private static boolean isVowel(char c) {
		boolean value = false;
		for(int i = 0; i<VOWELS.length; i++) {
			if(c == VOWELS[i]) {
				value = true;
			}
		}
		return value;
	}
}


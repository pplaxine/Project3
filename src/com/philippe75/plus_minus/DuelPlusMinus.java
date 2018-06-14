package com.philippe75.plus_minus;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

import com.philippe75.game.Fish;
import com.philippe75.game.Main;
import com.philippe75.game.Mode;
import com.philippe75.game.PropertiesFile;
import com.philippe75.game.TextEnhencer;
import com.philippe75.generators.SecretNumGenerator;

public class DuelPlusMinus implements Mode{

	private SecretNumGenerator sNG, sNG2;
	private int combiLength; 
	private String hint;
	private String userAnswer = "";
	private List<Integer> tabUserAnswer = new ArrayList<Integer>(); 	
	boolean dev = Main.isDev();
	Scanner clavier = new Scanner(System.in);
	
	//------------------------------------------------
	
	private int tries = 0; 
	private String userCode = "";
	private List<Integer> tabUserCode = new ArrayList<Integer>();
	private List<Integer> tabComputerAnswer2 = new ArrayList<Integer>();
	private Map<Integer, Map<String, Integer>> mapValuesMinMax = new HashMap<>();
	private Map<String, Integer> valuesMinMax; 
	private List<Integer> tabComputerAnswer;
	private String computerAnswerStringFormat;
	private int minValue, maxValue, newValue;
	
	// constructor
	public DuelPlusMinus() {
		setProperties();
		if(setProperties())
			startTheGame();
	}
	
	@Override
	public void startTheGame() {
			sNG = new SecretNumGenerator(combiLength);
			printWelcome();	
			requestUserSecretNum();
			displaySecretNum();
			initGame();
	}
	
	@Override
	public boolean setProperties() {
	
		combiLength = Integer.parseInt(PropertiesFile.getPropertiesFile("CombinationLength"));
		if(new String("true").equals(PropertiesFile.getPropertiesFile("devMode"))) {
			this.dev = true;
		}	
		return true; 
	}
	
	@Override
	public void printWelcome() {
		String 	str = TextEnhencer.ANSI_YELLOW; 
				str += "******************************************\n";
				str += "*******        WELCOME TO          *******\n";
				str += "*******        + or - GAME         *******\n";
				str += "*******         DUEL MODE          *******\n";	
				str += "******************************************\n";
				str += TextEnhencer.ANSI_RESET;
				System.out.println(str); 
	}
	
	protected void displaySecretNum() {
		System.out.println(TextEnhencer.ANSI_CYAN + "\nComputer has generated a secret combination for you to guess ..." + TextEnhencer.ANSI_RESET);
		if(dev) 		
			System.out.println(TextEnhencer.ANSI_CYAN + "*** Secret Num : " + sNG.getRandomNumber() + " *** " + TextEnhencer.ANSI_RESET);
		
	}
	
	//Request the user to enter a secret number and create an ArrayList with the entry. 
	private void requestUserSecretNum() {
		
		
		System.out.printf(TextEnhencer.ANSI_YELLOW + "Please enter the %d digits secret code, that the computer will have to guess, below : \n" + TextEnhencer.ANSI_RESET,combiLength); 
		
		do {
			//Using Regex to make sure user enter the right value type (Integer), as well as,  the correct length of this value type, and not higher range of selection offers.   
			if(userCode !="") {
				if(!userCode.matches("^[./[0-9]]+$")) { 
					System.out.println(TextEnhencer.ANSI_RED + "Please enter a number instead of a characters." + TextEnhencer.ANSI_RESET);					
				}else if (userCode.length() > combiLength || userCode.length() < combiLength){	
					System.out.println((userCode.length() > combiLength )? TextEnhencer.ANSI_RED + "The number of digits is superior to the number of digits required" + TextEnhencer.ANSI_RESET 
							: TextEnhencer.ANSI_RED + "The number of digits is inferior to the number of digits required" + TextEnhencer.ANSI_RESET);
				}
			}
			// store the user answer in a string
			System.out.print(TextEnhencer.ANSI_YELLOW);
			this.userCode = clavier.nextLine();
			System.out.print(TextEnhencer.ANSI_RESET);
		} while ( userCode.length() > combiLength || userCode.length() < combiLength || !userCode.matches("^[./[0-9]]+$") );
		// Transform userAnwser to a ArrayList 
		
		for (int i = 0; i < userCode.length(); i++) {
			tabUserCode.add(Character.getNumericValue(userCode.charAt(i)));  
		}
	}
	
	private void initGame() {
		// Repeat the question while user has enough tries left and hasn't found the answer
		do {
			tries++;
			
			requestUserAnswer();		
			generateUserHint();	
			if(!sNG.getTabNumber().toString().equals(tabUserAnswer.toString())) {
				generateComputerAnswer();
				generateComputerHint();
			}
		} while (!sNG.getTabNumber().toString().equals(tabUserAnswer.toString()) && !tabComputerAnswer.toString().equals(tabUserCode.toString()));
		
		// Print Result once the game is over. 
		if (sNG.getTabNumber().toString().equals(tabUserAnswer.toString())){
			Fish.displayFish();
			System.out.printf(TextEnhencer.ANSI_YELLOW + "\n\t .+*°*+.+> | Congratulation !!! | <+.+*°*+.\n\t You found the computers secret code first !!!  \n" + TextEnhencer.ANSI_RESET);				
		}else {
			System.out.printf(TextEnhencer.ANSI_RED + "\n\t\t\t   .+*°*+.+> | GAME OVER !!!  | <+.+*°*+." + TextEnhencer.ANSI_CYAN + "\nComputer found the answer first !!! the secret number was %s. You'll have more chance next time! \n" + TextEnhencer.ANSI_RESET, sNG.getRandomNumber());
		}
	}

	private void requestUserAnswer() {
		// Repeat while user didn't enter the correct value 
		do {
			//Using Regex to make sure user enter the right value type (Integer) and the correct length of this value type  
			if(userAnswer !="") {
				if(!userAnswer.matches("^[./[0-9]]+$")) { 
					System.out.println(TextEnhencer.ANSI_RED + "Please enter a number instead of a characters." + TextEnhencer.ANSI_RESET);
				}else {
					System.out.println((sNG.getNumberSize() < userAnswer.length())? TextEnhencer.ANSI_RED + "The number of digits is superior to the number of digits required" + TextEnhencer.ANSI_RESET : TextEnhencer.ANSI_RED + "The number of digits is inferior to the number of digits required" + TextEnhencer.ANSI_RESET);
				}
			}
			
			if(tries < 2) 	
				System.out.println(TextEnhencer.ANSI_YELLOW + "\nYour are the first to play!");
			else
				System.out.println(TextEnhencer.ANSI_YELLOW + "\nIt's your turn to play ...");	
			
			System.out.println("Please enter a number of " + sNG.getNumberSize() + (sNG.getNumberSize() > 1 ? " digits." : " digit."));			
			this.userAnswer = clavier.nextLine();
			System.out.print(TextEnhencer.ANSI_RESET);
		} while (!userAnswer.matches("^[./[0-9]]+$") || sNG.getNumberSize() != userAnswer.length());
	}
	
	private void generateUserHint() {
		tabUserAnswer.clear();		 
		// Transform userAnwser to a ArrayList to use the method userAnswerComparator 
		for (int i = 0; i < userAnswer.length(); i++) {
			tabUserAnswer.add(Character.getNumericValue(userAnswer.charAt(i)));  
		}
		
		// Generate the user next move hint 
		this.hint = generateHint(sNG.getTabNumber(), this.tabUserAnswer); 
		
		// Print hint if the answer isn't found or the game finished 
		if(!sNG.getTabNumber().toString().equals(tabUserAnswer.toString())) {
			System.out.println(TextEnhencer.ANSI_GREEN + "\nYour Answer " + userAnswer + " isn't so far, here is the hint ---> " + hint + TextEnhencer.ANSI_RESET);				
		}
		userAnswer = ""; 
	}
	
	// return the hint after comparing each character of two ArrayList.
	private String generateHint(List<Integer> tab1, List<Integer> tab2){
		String answer =""; 	
		for (int i = 0; i < tab1.size(); i++) {
			String str;
			
			if (tab1.get(i) < tab2.get(i)) {
				str = "-";
			}else if (tab1.get(i) > tab2.get(i)) {
				str = "+";
			}else str = "="; 
			
			answer += str;
		}
		return answer; 
	}
	
	private void generateComputerAnswer() {
	
		if(tries < 2) {
			tabComputerAnswer = generateComputerFirstTry();
		}else {
			tabComputerAnswer = generateAnswerWithHint();
		}
	}
	// Generate Computers first random Number 
	private List<Integer> generateComputerFirstTry() {
		// Generates a secreteNumber of the same length as user's answer.  
		sNG2 = new SecretNumGenerator(tabUserCode.size());
		tabComputerAnswer = sNG2.getTabNumber();
		computerAnswerStringFormat = sNG2.getRandomNumber();
		
		return tabComputerAnswer; 
	}
	
	private List<Integer> generateAnswerWithHint() {
		
		tabComputerAnswer2.clear();
		// for each number contained in the answer generate a new number taking in account the hint 
		int i =0; 
		
		
		//tabComputerAnswer.forEach(n -> { " fait ce qu'il y a entre parenhèse" } ); // n nombre d'iteration 
		minValue = 0;
		maxValue = 9;
		
			
		for (Integer b1 : tabComputerAnswer) {
			
			// first we create elements 
			if(mapValuesMinMax.size() < tabComputerAnswer.size()) {
				valuesMinMax = new HashMap<>();
				valuesMinMax.put("minValue", 0);
				valuesMinMax.put("maxValue", 9);
			// then use elements 	
			}else{
				valuesMinMax = mapValuesMinMax.get(i);
			}
	
			int test = tabUserCode.get(i).compareTo(b1);
			
			
			// if (i) digit of the userCode is smaller than (i) digit of the computeranswer.
			if(test < 0) {		
				// if b1 < maxValue, keep b1  
				if (b1 < valuesMinMax.get("maxValue")) {
					maxValue = b1;
				// if b1 > maxValue, keep max value 	
				}else {
					maxValue = valuesMinMax.get("maxValue");
				}	
				
				newValue = ((maxValue)/2);
		 
				tabComputerAnswer2.add(newValue);
				//store the new maxValue 
				valuesMinMax.replace("maxValue", b1-1);
			
			
			// if (i) digit of the userCode is greater than (i) digit of the computeranswer. 		
			}else if(test > 0) {
				// if b1 > minValue, keep b1 
				if (b1 > valuesMinMax.get("minValue")) {
					minValue = b1; 
				// if b1 < minValue, keep min value 	
				}else {
					minValue = valuesMinMax.get("minValue");
				}
							
				newValue = minValue + ((((valuesMinMax.get("maxValue") - minValue) + 1)/2)) ;
				tabComputerAnswer2.add(newValue);
				
				valuesMinMax.replace("minValue", b1+1);
				
			// if (i) digit of the userCode is equal (i) digit of the computeranswer. 	
			}else {
				tabComputerAnswer2.add(b1);
			}
			
			// Hashmap with the values min max for each digit are stored in a map
			mapValuesMinMax.put(i, valuesMinMax);
			i++;
		}
		this.tabComputerAnswer = new ArrayList<Integer>();
		tabComputerAnswer.addAll(tabComputerAnswer2);	
		
		return tabComputerAnswer;  
	}

	private void generateComputerHint() {
		// creates the hint 
		String hint = createHint(tabUserCode, tabComputerAnswer);
		// transforme ComputerAnswer into String via method ArrayListToString 
		computerAnswerStringFormat = ArrayListIntegerToString(tabComputerAnswer);
		
		//print computers answer and show the hint
		System.out.printf(TextEnhencer.ANSI_CYAN + "Computer tries %s ---> %s \n" + TextEnhencer.ANSI_RESET, computerAnswerStringFormat, hint);
	}
	
	// return the hint after comparing each character of two ArrayList.
	private String createHint(List<Integer> tab1, List<Integer> tab2){
		 String answer =""; 	
		for (int i = 0; i < tab1.size(); i++) {
			String str;
			
			if (tab1.get(i) < tab2.get(i)) {
				str = "-";
			}else if (tab1.get(i) > tab2.get(i)) {
				str = "+";
			}else str = "="; 
			
			 answer += str;
		}
		return answer; 
	}
	
	// Convert Arraylist to String
	public String ArrayListIntegerToString(List<Integer> arrayList) {
		String str = ""; 
		for (Integer b1 : arrayList) {
			str +=  b1;
		}
		return str; 
	}

}
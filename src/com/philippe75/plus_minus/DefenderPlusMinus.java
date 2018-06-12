package com.philippe75.plus_minus;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

import com.philippe75.game.Mode;
import com.philippe75.game.TextEnhencer;
import com.philippe75.generators.SecretNumGenerator;

public class DefenderPlusMinus implements Mode{
	private SecretNumGenerator sNG;
	private List<Integer> tabComputerAnswer;
	
	private Map<Integer, Map<String, Integer>> mapValuesMinMax = new HashMap<>(); 
	private Map<String, Integer> valuesMinMax; 
	
	private List<Integer> tabComputerAnswer2 = new ArrayList<Integer>();
	private List<Integer> tabUserCode = new ArrayList<Integer>();
	private String userCode = "";
	
	private String computerAnswerStringFormat;
	private int errorAllowed, minValue, maxValue, newValue; 
	private int score = 0; 

	
	//Constructor
	public DefenderPlusMinus () {	
		setProperties();
	}
	
	public void startTheGame() {
		if(setProperties()) {
			printWelcome();
			requestUserSecretNum();
			initGame();
		}
	}
	
	public boolean setProperties() {
		Properties p = new Properties();
		try(InputStream is = getClass().getResourceAsStream("dataConfig.properties")) {	
	
			p.load(is);
			errorAllowed = Integer.parseInt(p.getProperty("errorAllowed"));
			
		} catch (NullPointerException e) {
			System.err.println("The file dataConfig.properties could not be found.");
			return false;
		} catch (IOException e) {
			System.err.println("Error with the propertiesFiles.");
			return false;
		}
		return true;
	}
	
	//Generate Presentation screen 
	private void printWelcome() {
		String 	str = TextEnhencer.ANSI_YELLOW;
				str += "******************************************\n";
				str += "*******        WELCOME TO          *******\n";
				str += "*******        + or - GAME         *******\n";
				str += "*******       DEFENDER MODE        *******\n";	
				str += "******************************************\n";
				str += TextEnhencer.ANSI_RESET;
		System.out.println(str); 
	}
	
	//Request the user to enter a secret number and create an ArrayList with the entry. 
	private void requestUserSecretNum() {
		Scanner clavier = new Scanner(System.in);
		
		System.out.println(TextEnhencer.ANSI_YELLOW + "Please enter your secret combination below " + TextEnhencer.ANSI_RESET); 
		
		//Using Regex to make sure user enter the right value type (Integer).
		do {
			if(!userCode.equals("")) {
				if(!userCode.matches("^[./[0-9]]+$")) { 
					System.out.println(TextEnhencer.ANSI_RED + "Please enter a number instead of a characters.");
				}
			}
			System.out.print(TextEnhencer.ANSI_YELLOW);
			this.userCode = clavier.nextLine();
			System.out.print(TextEnhencer.ANSI_RESET);
			System.out.println("");
			
		} while (!userCode.matches("^[./[0-9]]+$"));	
		
		// Transform userAnwser to a ArrayList 
		
		for (int i = 0; i < userCode.length(); i++) {
			tabUserCode.add(Character.getNumericValue(userCode.charAt(i)));  
		}
	}
	
	public void initGame() {
		 
		// Repeat the question while Computer has enough tries left and hasn't found the answer
		do {
			// add a try after each question 
			score++;
			
			if(score < 2) {
				tabComputerAnswer = generateComputerFirstTry();
			}else {
				tabComputerAnswer = generateAnswerWithHint();
			}
			
			// creates the hint 
			String hint = createHint(tabUserCode, tabComputerAnswer);
			// transforme ComputerAnswer into String via method ArrayListToString 
			computerAnswerStringFormat = ArrayListIntegerToString(tabComputerAnswer);
			
			//print computers answer and show the hint
			System.out.printf(TextEnhencer.ANSI_CYAN + "Computer tries %s ---> %s \n" + TextEnhencer.ANSI_RESET, computerAnswerStringFormat, hint);
			
		} while (!tabComputerAnswer.toString().equals(tabUserCode.toString()) && score < errorAllowed);
		
		// Print Result once the game is over.
		if(tabComputerAnswer.toString().equals(tabUserCode.toString())) {
			System.out.printf(TextEnhencer.ANSI_RED + "\n\t   .+*°*+.+> | GAME OVER !!! | <+.+*°*+.\n" + TextEnhencer.ANSI_CYAN + "You loose! Computer found your code after %d attempts.\n" + TextEnhencer.ANSI_RESET, score );	
		}else {
			displayFish();
			System.out.printf(TextEnhencer.ANSI_YELLOW + "\n\t   .+*°*+.+> | Congratulation ! | <+.+*°*+.\n\t Computer couldn't find your code after %d attempts" + TextEnhencer.ANSI_RESET, score );	
		}
		
		
	}
	
	
	// Generate Computers first random Number 
	private List<Integer> generateComputerFirstTry() {
		// Generates a secreteNumber of the same length as user's answer.  
		sNG = new SecretNumGenerator(tabUserCode.size());
		tabComputerAnswer = sNG.getTabNumber();
		computerAnswerStringFormat = sNG.getRandomNumber();
		
		return tabComputerAnswer; 
	}
	
	//return a new number taking in account the hint given with the previous try. 
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

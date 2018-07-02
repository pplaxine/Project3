package com.philippe75.plus_minus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.philippe75.extra.TextEnhencer;
import com.philippe75.game.Game;
import com.philippe75.generators.SecretNumGenerator;

/**
 * <b>This abstract class contains all method common to all PlusMinus game modes.</b> 
 * 
 *<p>Challenger mode.</p>
 *<p>Defender mode.</p> 
 *<p>Duel mode.</p> 
 * 
 * @author PPlaxine
 * @version 1.0
 */
public abstract class PlusMinus extends Game{

	
	/**
	 * Secret combination generator.
	 * 
	 * Generates random number. 
	 * 
	 * For more information about the generator, please see SecretNumberGenerator class doc.  
	 * 
	 * @see SecretNumGenerator
	 * @see ChallengerPlusMinus#startTheGame()
	 * @see DuelPlusMinus#startTheGame()
	 */
	protected SecretNumGenerator sNG, sNG2;
	
	/**
	 * Creates a logger to generate log of the class.	
	 */
	private static final Logger log = Logger.getLogger(PlusMinus.class);
	
	//Challenger, Duel ATT-------------------------------------------------------------
	
	/**
	 * Users answer in String format.
	 *  
	 * Store users answer from keyboard entry. The Entry is in form of chain of numbers.
	 * 
	 * The content is transfered to an ArrayList for further processing. 
	 * 
	 * Also used to verify if the users answer complies with the entry requirements  
	 * 
	 * @see PlusMinus#generateUserHint()
	 * @see ChallengerPlusMinus#initGame()
	 * @see DuelPlusMinus#initGame()
	 */
	protected String userAnswer ="";
	
	/**
	 * User answer in List format.
	 * 
	 * Store each digit of users Answer as an element of the List. 
	 * 
	 * The List is then used for comparison with secret combination list. 
	 *  
	 * @see PlusMinus#generateUserHint()
	 * @see ChallengerPlusMinus#initGame()
	 * @see DuelPlusMinus#initGame()
	 */
	protected List<Integer> tabUserAnswer; 

	
	//Defender, Duel ATT-------------------------------------------------------------
	
	/**
	 * Users combination in List format. 
	 * 
	 * Is used to define the length of computer answer. 
	 * 
	 * Is used to be compared with computer try and create the hint.
	 * 
	 * Is also used to verify if the computer has generated the correct answer. 
	 * 
	 * @see PlusMinus#requestUserSecretNum()
	 * @see PlusMinus#generateComputerFirstTry()
	 * @see PlusMinus#generateAnswerWithHint()
	 * @see PlusMinus#generateComputerAnswer()
	 * @see DefenderPlusMinus#initGame()
	 * @see DuelPlusMinus#initGame()
	 */
	protected List<Integer> tabUserCode = new ArrayList<Integer>();
	
	/**
	 * Computer answer in List format.
	 * 
	 * Store each digit of Computers Answer as an element of the List. 
	 * 
	 * The List is then used for comparison with users combination.
	 * Also transfered into a String to be displayed;    
	 *  
	 * @see PlusMinus#requestUserSecretNum()
	 * @see PlusMinus#generateComputerFirstTry()
	 * @see PlusMinus#generateAnswerWithHint()
	 * @see PlusMinus#generateComputerAnswer()
	 * @see DefenderPlusMinus#initGame()
	 * @see DuelPlusMinus#initGame()
	 */
	protected List<Integer> tabComputerAnswer;

	/**
	 * Stores the computer answer in String format to be displayed. 
	 * 
	 * @see PlusMinus#generateComputerFirstTry()
	 * @see PlusMinus#ArrayListIntegerToString(List)
	 */
	private String computerAnswerStringFormat;
	
	/**
	 * Stores for each digit the min value and max value range to get a new digit from.
	 * 
	 * @see PlusMinus#generateAnswerWithHint()
	 */
	private Map<String, Integer> valuesMinMax; 
	
	/**
	 * While computer combination in List format is being compared, stores each new generated digit.  
	 * 
	 * Once comparison is finished, transfers the full new combination into computer reseted combination List.
	 * 
	 * Is then reset, to do it again. 
	 * 
	 *@see PlusMinus#generateAnswerWithHint()
	 *@see PlusMinus#tabComputerAnswer
	 */
	private List<Integer> tabComputerAnswer2 = new ArrayList<Integer>();
	
	/**
	 * Used to define for each digit of computer answer, the minimum value of the range min - max in which the next digit must be taken. 
	 * 
	 * It is set to 0 at the beginning. To generate the new digit, it uses the min value of each digit stored in valuesMinMax. 
	 * After processing, the new min value is replaced in valuesMinMax of the corresponding digit.      
	 * 
	 * @see PlusMinus#generateAnswerWithHint()
	 * @see PlusMinus#valuesMinMax
	 */
	private int minValue;
	
	/**
	 * Used to define for each digit of computer answer, the maximum value of the range min - max in which the next digit must be taken. 
	 * 
	 * It is set to 9 at the beginning. To generate the new digit, it uses the max value of each digit stored in valuesMinMax. 
	 * After processing, the new max value is replaced in valuesMinMax of the corresponding digit.      
	 * 
	 * @see PlusMinus#generateAnswerWithHint()
	 * @see PlusMinus#valuesMinMax
	 */
	private int maxValue;
	
	/**
	 * Stores the minValue and maxValue of each digit of the combination. 
	 * 
	 * @see PlusMinus#generateAnswerWithHint()
	 */
	private Map<Integer, Map<String, Integer>> mapValuesMinMax = new HashMap<>(); 
	
	
	//Common methods -------------------------------------------------------------

	/**
	 * Generates the hint. 
	 * 
	 * For each position, compare numerical elements of two different Lists. return if element of the first List is higher, lower or equal. 
	 * 
	 * Following the outcome, add a sign + / - / = to the hint. 
	 * 
	 * @param tab1
	 * 				List of Integer to be compared. 
	 * @param tab2
	 * 				List of Integer to be compared to the first List of Integer. 
	 * @return The hint in form of a chain of indications "+" or "-".
	 */
	private String generateHint(List<Integer> tab1, List<Integer> tab2){
		String hint =""; 	
		for (int i = 0; i < tab1.size(); i++) {
			String str;
			
			if (tab1.get(i) < tab2.get(i)) {
				str = "-";
			}else if (tab1.get(i) > tab2.get(i)) {
				str = "+";
			}else str = "="; 
			
			 hint += str;
		}
		return hint; 
	}

	
	//Challenger, Duel methods -------------------------------------------------------------
	
	/**
	 *  Request the user to make a keyboard entry.
	 *  
	 *  also verify if the entry complies requirements.
	 *  
	 *  @see PlusMinus#userAnswer
	 *  @see ChallengerPlusMinus#initGame()
	 *  @see DefenderPlusMinus#initGame()
	 *  @see DuelPlusMinus#initGame()
	 */
	protected String requestUserAnswer() {
		String userAnswer="";
		// Repeat while user didn't enter the correct value 
		do {
			//Using Regex to make sure user enter the right value type (Integer) and the correct length of this value type  
			if(userAnswer !="") {
				if(!userAnswer.matches("^[./[0-9]]+$")) { 
					System.out.println(TextEnhencer.ANSI_RED + "Please enter a number instead of a characters." + TextEnhencer.ANSI_RESET);
					log.warn("User entry mismatch the type required");
				}else {
					System.out.println((this.sNG.getNumberSize() < userAnswer.length())? TextEnhencer.ANSI_RED + "The number of digits is superior to the number of digits required" + TextEnhencer.ANSI_RESET : 
						TextEnhencer.ANSI_RED + "The number of digits is inferior to the number of digits required" + TextEnhencer.ANSI_RESET);
					log.warn("User entry mismatch the length of digits required");
				}
			}
			System.out.print(TextEnhencer.ANSI_YELLOW);
			System.out.println("Please enter a number of " + this.sNG.getNumberSize() + (this.sNG.getNumberSize() > 1 ? " digits." : " digit."));
			userAnswer = clavier.nextLine();
			System.out.print(TextEnhencer.ANSI_RESET);
		} while (!userAnswer.matches("^[./[0-9]]+$") || this.sNG.getNumberSize() != userAnswer.length());
		return userAnswer;
	}
	
	/**
	 * Display a hint for players next move.
	 * 
	 * @see PlusMinus#generateHint(List, List)
	 * @see DefenderPlusMinus#initGame()
	 * @see DuelPlusMinus#initGame() 
	 */
	protected void generateUserHint() {
		
		String hint="";
		this.tabUserAnswer.clear();		 
		// Transform userAnwser to a ArrayList to use the method userAnswerComparator 
		for (int i = 0; i < userAnswer.length(); i++) {
			tabUserAnswer.add(Character.getNumericValue(userAnswer.charAt(i)));  
		}
		// Generate the user next move hint 
		hint = this.generateHint(this.sNG.getTabNumber(), this.tabUserAnswer); 
		
		// Print hint if the answer isn't found or the game finished 
		if(!this.sNG.getTabNumber().toString().equals(tabUserAnswer.toString())) {
			System.out.println(TextEnhencer.ANSI_GREEN + "\nYour Answer " + userAnswer + " isn't so far, here is the hint ---> " + hint + TextEnhencer.ANSI_RESET);				
		}
		userAnswer = ""; 
	}
	
	
	//Defender, Duel methods -------------------------------------------------------------
	
	/**
	 * Request the user to enter a secret combination and create an ArrayList with the entry.
	 * 
	 * also verify if the entry is correct. 
	 * 
	 * @see PlusMinus#userCode
	 * @see PlusMinus#tabUserCode
	 * @see DefenderPlusMinus#initGame()
	 * @see DuelPlusMinus#initGame() 
	 */
	protected int requestUserSecretNum() {
		tabUserCode = new ArrayList<>();
		String userCode = "";
		System.out.println(TextEnhencer.ANSI_YELLOW + "Please enter your secret combination "
				+ ((this.getClass().getName() =="com.philippe75.plus_minus.DuelPlusMinus")?"of " + combiLength + " digits ":"") 
				+"below " + TextEnhencer.ANSI_RESET); 
	
		//Using Regex to make sure user enter the right value type (Integer).
		do {
			if(!userCode.equals("")) {
				if(!userCode.matches("^[./[0-9]]+$")) { 
					System.out.println(TextEnhencer.ANSI_RED + "Please enter a number instead of a characters.");
					log.warn("User entry mismatch the type required");
				}
			}
			System.out.print(TextEnhencer.ANSI_YELLOW);
			userCode = clavier.nextLine();
			System.out.print(TextEnhencer.ANSI_RESET);
			System.out.println("");
		} while (!userCode.matches("^[./[0-9]]+$"));	
		
		// Transform userAnwser to a ArrayList 
		for (int i = 0; i < userCode.length(); i++) {
			tabUserCode.add(Character.getNumericValue(userCode.charAt(i)));  
		}
		return userCode.length();
	}

	/**
	 * Generates computers answer 
	 * 
	 * @see PlusMinus#tabComputerAnswer
	 * @see PlusMinus#generateComputerFirstTry()
	 * @see PlusMinus#generateAnswerWithHint()
	 * @see DefenderPlusMinus#initGame()
	 * @see DuelPlusMinus#initGame()  
	 */
	protected void generateComputerAnswer() {
		if(super.tries < 2) {
			tabComputerAnswer = generateComputerFirstTry();
		}else {
			tabComputerAnswer = generateAnswerWithHint();
		}
	}
	
	/**
	 * Display a hint for computers next move. 
	 * 
	 * @see PlusMinus#createHint(List, List)
	 * @see DefenderPlusMinus#initGame()
	 * @see DuelPlusMinus#initGame() 
	 */
	protected void generateComputerHint() {
		// creates the hint 
		String hint = this.generateHint(tabUserCode, tabComputerAnswer);
		// transforme ComputerAnswer into String via method ArrayListToString 
		computerAnswerStringFormat = ArrayListIntegerToString(tabComputerAnswer);
		//print computers answer and show the hint
		System.out.printf(TextEnhencer.ANSI_CYAN + "Computer tries %s ---> %s \n" + TextEnhencer.ANSI_RESET, computerAnswerStringFormat, hint);
	}
	
	/**
	 * Generates Computers first random combination of the length of users entry.
	 * 
	 * Also store the combination in String format. 
	 *  
	 * @return the generated combination in form of a List.  
	 * 
	 * @see SecretNumGenerator
	 * @see SecretNumGenerator#getTabNumber()
	 * @see SecretNumGenerator#getRandomNumber()
	 * @see PlusMinus#computerAnswerStringFormat
	 */
	private List<Integer> generateComputerFirstTry() {
		// Generates a secreteNumber of the same length as user's answer.  
		this.sNG2 = new SecretNumGenerator(tabUserCode.size());
		tabComputerAnswer = this.sNG2.getTabNumber();
		computerAnswerStringFormat = this.sNG2.getRandomNumber();
		
		return tabComputerAnswer; 
	}
	
	/**
	 * Generates computers new answer taking in consideration the previous hint.  
	 * 
	 * If first try, for each digit of the combination a map, containing a min value and a max value, is created. After first try values are re-used. 
	 * 
	 * For each digits the users entry and computer answer are compared. 
	 * 
	 * If both digits match, this digit is added to a new List. 
	 * 
	 * If computers digit is higher than user digit, computers digit value becomes max value( if lower than previous max). After being halved, it is added to the new List.
	 * 
	 * If computers digit is lower than user digit, computer digit value become min value (if higher than previous min). The new digit value will be the middle value between min and max and added to the new List.
	 * 
	 * For each turn and each digit, the maps valuesMinMax are added/replaced in a Map of valuesMinMax.  
	 * 
	 * The List is re-transfered to Computer answer List, and then reset. 
	 * 
	 * @return computers new answer in form of a List. 
	 * @see PlusMinus#valuesMinMax
	 * @see PlusMinus#tabComputerAnswer2
	 * @see PlusMinus#mapValuesMinMax
	 */
	private List<Integer> generateAnswerWithHint() {
		
		tabComputerAnswer2.clear();
		int newValue; 
		int i =0; 
		
		minValue = 0;
		maxValue = 9;
		
		// for each number contained in the answer generate a new number taking in account the hint 
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
				minValue = valuesMinMax.get("minValue");
				
				newValue = (((maxValue)/2) < minValue)? minValue : ((maxValue)/2);
		 
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

	/**
	 * Convert an ArrayList into String
	 * 
	 * @param arrayList 
	 * 					The computers answer in List format. 
	 * @return The computers answer in String format. 
	 */
	private String ArrayListIntegerToString(List<Integer> arrayList) {
		String str = ""; 
		for (Integer b1 : arrayList) {
			str +=  b1;
		}
		return str; 
	}
}

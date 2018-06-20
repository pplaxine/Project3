package com.philippe75.plus_minus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.log4j.Logger;

import com.philippe75.game.Fish;
import com.philippe75.game.Main;
import com.philippe75.game.Mode;
import com.philippe75.game.PropertiesFile;
import com.philippe75.game.TextEnhencer;
import com.philippe75.generators.SecretNumGenerator;

/**
 * <b>DuelPlusMinus is a class that handle the PlusMinus game in Duel Mode.</b>
 * <p>Steps of the game : 
 * <ul>
 * <li>user enters a secret combination for the computer to guess.</li>
 * <li>A random combination is generated by the computer for the user to guess.</li>
 * <li>User tries a combination.</li>
 * <li>for each answer an hint is return. This hint indicates for each digit of the user answer if the secret combination digit is higher or lower.</li>
 * <li>Computer tries a combination.</li>
 * <li>for each answer an hint is also return.</li>
 * <li>Each turn, user and Computer try a combination.</li>
 * <li>If user finds the secret combination he wins, if computer finds first, user looses.</li>
 * </ul>
 * </p>
 * 
 * <p>
 * The random combination is generate via a secret combination generator. 
 * </p>
 * 
 * <p>
 * The secret combination length can be set in a DataConfig.properties file.
 * </p>
 * 
 * @see SecretNumGenerator
 * @see DuelPlusMinus#setProperties()
 * 
 * @author PPlaxine
 * @version 1.0
 */
public class DuelPlusMinus implements Mode{
	
	/**
	 * Secret combination generator. 
	 * 
	 * For more information about the generator, please see SecretNumberGenerator class doc.  
	 * 
	 * @see SecretNumGenerator
	 * @see DuelPlusMinus#startTheGame()
	 */
	private SecretNumGenerator sNG; 
	
	/**
	 * Secret combination generator. 
	 * 
	 * For more information about the generator, please see SecretNumberGenerator class doc.  
	 * 
	 * @see SecretNumGenerator
	 * @see DuelPlusMinus#startTheGame()
	 */
	private SecretNumGenerator sNG2;
	
	/**
	 * combination length for the game. 
	 * 
	 * Defines the length of the secret combination to be generated
	 * It can be modified via the dataConfig.properties file 
	 * 
	 * @see DuelPlusMinus#setProperties()
	 * @see DuelPlusMinus#startTheGame()
	 */
	private int combiLength; 
	
	/**
	 * hint for users next try. 
	 * 
	 * Contains a chain of indications "+" or "-" corresponding to the comparison between users answer digits and secret combination digits.  
	 * 
	 * Is also used to display the hint to the user.
	 * 
	 * @see DuelPlusMinus#generateHint(List, List)
	 * @see DuelPlusMinus#initGame()
	 */
	private String hint;
	
	/**
	 * Users answer in String format.
	 *  
	 * Store users answer from keyboard entry. The Entry is in form of chain of numbers.
	 * 
	 * The content is transfered to an ArrayList for further processing. 
	 * 
	 * Also used to verify if the users answer complies with the entry requirements  
	 * 
	 * @see DuelPlusMinus#initGame()
	 * @see DuelPlusMinus#tabUserAnswer
	 */
	private String userAnswer = "";
	
	/**
	 * User answer in List format.
	 * 
	 * Store each digit of users Answer as an element of the List. 
	 * 
	 * The List is then used for comparison with secret combination list. 
	 *  
	 * @see DuelPlusMinus#initGame()
	 * @see DuelPlusMinus#generateHint(List, List)
	 * @see DuelNumGenerator#getTabNumber() 
	 */
	private List<Integer> tabUserAnswer = new ArrayList<Integer>(); 
	
	/**
	 * Runs the game in developer mode if true is returned.
	 * 
	 * By default, if an argument -dev is passed when starting the program, the boolean will return the value true. 
	 * 
	 * Returns also true, if in dataProperties file the value of devMode is set to true;   
	 *  
	 * @see DuelPlusMinus#displaySecretNum()
	 * @see Main#isDev()
	 * @see Main#dev
	 * @see Main#main(String[])
	 * @see DuelPlusMinus#setProperties()
	 */
	boolean dev = Main.isDev();
	
	/**
	 * Opens the keyboard entry. 
	 * 
	 * @see DuelPlusMinus#requestUserSecretNum()
	 * @see DuelPlusMinus#requestUserAnswer()
	 */
	Scanner clavier = new Scanner(System.in);
	
	/**
	 * Defines number of tries made.
	 * 
	 * @see DuelPlusMinus#initGame()
	 * @see DuelPlusMinus#generateComputerAnswer()
	 */
	private int tries = 0; 
	
	/**
	 * Stores the users combination. 
	 * 
	 * Is used to verify if the answer is a correct entry.  
	 * 
	 * Is then transfered into a List.
	 * 
	 * @see DuelPlusMinus#requestUserSecretNum()
	 * @see DuelPlusMinus#tabUserCode
	 */
	private String userCode = "";
	
	/**
	 * Users combination in List format. 
	 * 
	 * Is used to define the length of computer answer. 
	 * 
	 * Is used to be compared with computer try and create the hint.
	 * 
	 * Is also used to verify if the computer has generated the correct answer. 
	 * 
	 * @see DuelPlusMinus#requestUserSecretNum()
	 * @see DuelPlusMinus#generateComputerFirstTry()
	 * @see DuelPlusMinus#createHint(List, List)
	 * @see DuelPlusMinus#initGame()
	 * @see DuelPlusMinus#generateAnswerWithHint()
	 */
	private List<Integer> tabUserCode = new ArrayList<Integer>();
	
	/**
	 * While computer combination in List format is being compared, stores each new generated digit.  
	 * 
	 * Once comparison is finished, transfers the full new combination into computer reseted combination List.
	 * 
	 * Is then reset, to do it again. 
	 * 
	 *@see DuelPlusMinus#generateAnswerWithHint()
	 *@see DuelPlusMinus#tabComputerAnswer
	 */
	private List<Integer> tabComputerAnswer2 = new ArrayList<Integer>();
	
	/**
	 * stores for each computer try, and for each digit of the combination, the value of the digit and a map of its min value and max value range to get a new digit from. 
	 * 
	 * @see DuelPlusMinus#generateAnswerWithHint()
	 * @see DuelPlusMinus#ValuesMinMax
	 */
	private Map<Integer, Map<String, Integer>> mapValuesMinMax = new HashMap<>();
	
	/**
	 * Stores for each digit the min value and max value range to get a new digit from.
	 * 
	 * @see DuelPlusMinus#generateAnswerWithHint()
	 */
	private Map<String, Integer> valuesMinMax;
	
	/**
	 * Computer answer in List format.
	 * 
	 * Store each digit of Computers Answer as an element of the List. 
	 * 
	 * The List is then used for comparison with users combination.
	 * Also transfered into a String to be displayed;    
	 *  
	 * @see DuelPlusMinus#initGame()
	 * @see DuelPlusMinus#generateComputerFirstTry()
	 * @see SecretNumGenerator#getTabNumber() 
	 * @see DuelPlusMinus#generateAnswerWithHint()
	 * @see DuelPlusMinus#createHint(List, List)
	 * @see DuelPlusMinus#computerAnswerStringFormat
	 */
	private List<Integer> tabComputerAnswer;
	
	/**
	 * Stores the computer answer in String format to be displayed. 
	 * 
	 * @see DuelPlusMinus#generateComputerFirstTry()
	 * @see DuelPlusMinus#ArrayListIntegerToString(List)
	 */
	private String computerAnswerStringFormat;
	
	/**
	 * Used to define for each digit of computer answer, the minimum value of the range min - max in which the next digit must be taken. 
	 * 
	 * It is set to 0 at the beginning. To generate the new digit, it uses the min value of each digit stored in valuesMinMax. 
	 * After processing, the new min value is replaced in valuesMinMax of the corresponding digit.      
	 * 
	 * @see DuelPlusMinus#generateAnswerWithHint()
	 * @see DuelPlusMinus#valuesMinMax
	 */
	private int minValue;
	
	/**
	 * Used to define for each digit of computer answer, the maximum value of the range min - max in which the next digit must be taken. 
	 * 
	 * It is set to 9 at the beginning. To generate the new digit, it uses the max value of each digit stored in valuesMinMax. 
	 * After processing, the new max value is replaced in valuesMinMax of the corresponding digit.      
	 * 
	 * @see DuelPlusMinus#generateAnswerWithHint()
	 * @see DuelPlusMinus#valuesMinMax
	 */
	private int maxValue; 
	
	/**
	 * Creates a logger to generate log of the class.	
	 */
	private static final Logger log = Logger.getLogger(DuelPlusMinus.class);
	
	/**
	 * Constructor of DuelPlusMinus.
	 * 
	 * When the class is instantiated, load properties to be used by the game.
	 * 
	 * @see DuelPlusMinus#setProperties()
	 * @see DuelPlusMinus#errorAllowed
	 */
	public DuelPlusMinus() {
		if(setProperties())
			startTheGame();
	}
	
	/**
	 * Starts the game.  
	 * 
	 * A welcome screen is displayed.
	 * 
	 * The secret combination is displayed if the developer mode is activated.
	 * 
	 * Displays a request for user to make an entry. 
	 * 
	 * Initiate the game. 
	 * 
	 * @see DuelPlusMinus#printWelcome()
	 * @see DuelPlusMinus#requestUserSecretNum()
	 * @see DuelPlusMinus#initGame()
	 */
	@Override
	public void startTheGame() {
		log.info("Start of PlusMinus game in duel mode");
		sNG = new SecretNumGenerator(combiLength);
		printWelcome();	
		requestUserSecretNum();
		displaySecretNum();
		initGame();
		log.info("End of the game");
	}
	
	/**
	 * Returns true if the properties are set.
	 * 
	 *  @see DuelPlusMinus#errorAllowed
	 */
	@Override
	public boolean setProperties() {
	
		combiLength = Integer.parseInt(PropertiesFile.getPropertiesFile("CombinationLength"));
		if(new String("true").equals(PropertiesFile.getPropertiesFile("devMode"))) {
			this.dev = true;
		}
		log.info("Properties set successfully");
		return true; 
	}
	
	/**
	 * Display the welcome screen.
	 * 
	 * @see DuelPlusMinus#startTheGame()
	 */ 
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
	
	/**
	 * Displays the secret combination if mode dev is activated
	 *  
	 * @see DuelPlusMinus#dev
	 * @see DuelPlusMinus#startTheGame()
	 */
	protected void displaySecretNum() {
		System.out.println(TextEnhencer.ANSI_CYAN + "\nComputer has generated a secret combination for you to guess ..." + TextEnhencer.ANSI_RESET);
		if(dev) {
			log.info("Game is running in developer mode");
			System.out.println(TextEnhencer.ANSI_CYAN + "*** Secret Num : " + sNG.getRandomNumber() + " *** " + TextEnhencer.ANSI_RESET);
		}
	}
	
	/**
	 * Request the user to enter a secret combination and create an ArrayList with the entry.
	 * 
	 * also verify if the entry is correct. 
	 * 
	 * @see DuelPlusMinus#userCode 
	 * @see DuelPlusMinus#tabUserCode
	 */ 
	private void requestUserSecretNum() {
		
		System.out.printf(TextEnhencer.ANSI_YELLOW + "Please enter the %d digits secret code, that the computer will have to guess, below : \n" + TextEnhencer.ANSI_RESET,combiLength); 
		
		do {
			//Using Regex to make sure user enter the right value type (Integer), as well as,  the correct length of this value type, and not higher range of selection offers.   
			if(userCode !="") {
				if(!userCode.matches("^[./[0-9]]+$")) { 
					System.out.println(TextEnhencer.ANSI_RED + "Please enter a number instead of a characters." + TextEnhencer.ANSI_RESET);	
					log.warn("User entry mismatch the type required");
				}else if (userCode.length() > combiLength || userCode.length() < combiLength){	
					System.out.println((userCode.length() > combiLength )? TextEnhencer.ANSI_RED + "The number of digits is superior to the number of digits required" + TextEnhencer.ANSI_RESET 
							: TextEnhencer.ANSI_RED + "The number of digits is inferior to the number of digits required" + TextEnhencer.ANSI_RESET);
					log.warn("User entry mismatch the length of digits required");
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
	
	/**
	 * Initiate the game. 
	 * 
	 * User tries a combination.
	 * for each answer an hint is return. This hint indicates for each digit of the user answer if the secret combination digit is higher or lower.
	 * Computer tries a combination.
	 * for each answer an hint is also return.
	 * Each turn, user and Computer try a combination.
	 * If user finds the secret combination he wins, if computer finds first, user looses.
	 * 
	 * @see DuelPlusMinus#requestUserAnswer()
	 * @see DuelPlusMinus#generateAnswerWithHint()
	 * @see DuelPlusMinus#generateComputerAnswer()
	 * @see DuelPlusMinus#generateComputerHint()
	 */
	private void initGame() {
		// Repeat the question while user has enough tries left and hasn't found the answer
		do {
			tries++;
			if(tries < 2) 	
				System.out.println(TextEnhencer.ANSI_YELLOW + "\nYour are the first to play!");
			else
				System.out.println(TextEnhencer.ANSI_YELLOW + "\nIt's your turn to play ...");	
			
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
			System.out.printf(TextEnhencer.ANSI_YELLOW + "\n\t .+*�*+.+> | Congratulation !!! | <+.+*�*+.\n\t You found the computers secret code first !!!  \n" + TextEnhencer.ANSI_RESET);				
		}else {
			System.out.printf(TextEnhencer.ANSI_RED + "\n\t\t\t   .+*�*+.+> | GAME OVER !!!  | <+.+*�*+." + TextEnhencer.ANSI_CYAN + "\nComputer found the answer first !!! the secret number was %s. You'll have more chance next time! \n" + TextEnhencer.ANSI_RESET, sNG.getRandomNumber());
		}
	}

	/**
	 *  Request the user to make a keyboard entry.
	 *  
	 *  also verify if the entry is correct.
	 *  
	 *  @see DuelPlusMinus#userAnswer
	 */
	private void requestUserAnswer() {
		// Repeat while user didn't enter the correct value 
		do {
			//Using Regex to make sure user enter the right value type (Integer) and the correct length of this value type  
			if(userAnswer !="") {
				if(!userAnswer.matches("^[./[0-9]]+$")) { 
					System.out.println(TextEnhencer.ANSI_RED + "Please enter a number instead of a characters." + TextEnhencer.ANSI_RESET);
					log.warn("User entry mismatch the type required");
				}else {
					System.out.println((sNG.getNumberSize() < userAnswer.length())? TextEnhencer.ANSI_RED + "The number of digits is superior to the number of digits required" + TextEnhencer.ANSI_RESET : 
						TextEnhencer.ANSI_RED + "The number of digits is inferior to the number of digits required" + TextEnhencer.ANSI_RESET);
					log.warn("User entry mismatch the length of digits required");
				}
			}
			System.out.println("Please enter a number of " + sNG.getNumberSize() + (sNG.getNumberSize() > 1 ? " digits." : " digit."));			
			this.userAnswer = clavier.nextLine();
			System.out.print(TextEnhencer.ANSI_RESET);
		} while (!userAnswer.matches("^[./[0-9]]+$") || sNG.getNumberSize() != userAnswer.length());
	}
	
	/**
	 * Display a hint for players next move.
	 * 
	 * @see DuelPlusMinus#generateHint(List, List)
	 *  
	 */
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
	
	/**
	 * Generates computers answer 
	 * 
	 * @see DuelPlusMinus#tabComputerAnswer
	 * @see DuelPlusMinus#generateComputerFirstTry()
	 * @see DuelPlusMinus#generateAnswerWithHint()
	 */
	private void generateComputerAnswer() {
	
		if(tries < 2) {
			tabComputerAnswer = generateComputerFirstTry();
		}else {
			tabComputerAnswer = generateAnswerWithHint();
		}
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
	 * @see DuelPlusMinus#computerAnswerStringFormat
	 */
	private List<Integer> generateComputerFirstTry() {
		// Generates a secreteNumber of the same length as user's answer.  
		sNG2 = new SecretNumGenerator(tabUserCode.size());
		tabComputerAnswer = sNG2.getTabNumber();
		computerAnswerStringFormat = sNG2.getRandomNumber();
		
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
	 * @return computer new answer in form of a List. 
	 * @see DuelPlusMinus#valuesMinMax
	 * @see DuelPlusMinus#tabComputerAnswer2
	 * @see DuelPlusMinus#mapValuesMinMax
	 */
	private List<Integer> generateAnswerWithHint() {
		
		int newValue;
		tabComputerAnswer2.clear();
		// for each number contained in the answer generate a new number taking in account the hint 
		int i =0; 
		
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

	/**
	 * Display a hint for computers next move. 
	 * 
	 * @see DuelPlusMinus#createHint(List, List)
	 */
	private void generateComputerHint() {
		// creates the hint 
		String hint = createHint(tabUserCode, tabComputerAnswer);
		// transforme ComputerAnswer into String via method ArrayListToString 
		computerAnswerStringFormat = ArrayListIntegerToString(tabComputerAnswer);
		
		//print computers answer and show the hint
		System.out.printf(TextEnhencer.ANSI_CYAN + "Computer tries %s ---> %s \n" + TextEnhencer.ANSI_RESET, computerAnswerStringFormat, hint);
	}
	
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
	 * @return
	 * 				The hint in form of a chain of indications "+" or "-".
	 */
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
	
	/**
	 * Convert an ArrayList into String
	 * 
	 * @param arrayList 
	 * 					The computers answer in List format. 
	 * @return The computers answer in String format. 
	 */
	public String ArrayListIntegerToString(List<Integer> arrayList) {
		String str = ""; 
		for (Integer b1 : arrayList) {
			str +=  b1;
		}
		return str; 
	}

}
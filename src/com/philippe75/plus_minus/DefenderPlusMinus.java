package com.philippe75.plus_minus;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

import org.apache.log4j.Logger;

import com.philippe75.game.Fish;
import com.philippe75.game.Mode;
import com.philippe75.game.PropertiesFile;
import com.philippe75.game.TextEnhencer;
import com.philippe75.generators.SecretNumGenerator;

/**
 * <b>DefenderPlusMinus is a class that handle the PlusMinus game in Defender Mode.</b>
 * <p>Steps of the game :
 * <ul>
 * <li>User enter a combination.</li>
 * <li>Computer tries a combination.</li>
 * <li>for each answer an hint is return. This hint indicates for each digit of the user answer if the secret combination digit is higher or lower.</li>
 * <li>Computer tries combinations until he finds the answer or the number of tries permitted is reached.</li>
 * <li>If computer finds the secret combination user looses, otherwise he wins.</li>
 * </ul>
 * </p>
 * 
 * <p>
 * The number of errors allowed can be set in a DataConfig.properties file.
 * </p>
 * 
 * <p>
 * The length of the combination to find is dynamically adapted to the length of the user entry. 
 * </p>
 * 
 * @see DefenderPlusMinus#setProperties()
 * @see DefenderPlusMinus#generateComputerFirstTry()
 * 
 * @author PPlaxine
 * @version 1.0
 *
 */
public class DefenderPlusMinus implements Mode{
	
	/**
	 * Secret combination generator. 
	 * 
	 * For more information about the generator, please see SecretNumberGenerator class doc.  
	 * 
	 * Is used to generate a random first answer. 
	 * 
	 * @see SecretNumGenerator
	 * @see DefenderPlusMinus#generateComputerFirstTry()
	 */
	private SecretNumGenerator sNG;
	
	/**
	 * Computer answer in List format.
	 * 
	 * Store each digit of Computers Answer as an element of the List. 
	 * 
	 * The List is then used for comparison with users combination.
	 * Also transfered into a String to be displayed;    
	 *  
	 * @see DefenderPlusMinus#initGame()
	 * @see DefenderPlusMinus#generateComputerFirstTry()
	 * @see SecretNumGenerator#getTabNumber() 
	 * @see DefenderPlusMinus#generateAnswerWithHint()
	 * @see DefenderPlusMinus#createHint(List, List)
	 * @see DefenderPlusMinus#computerAnswerStringFormat
	 */
	private List<Integer> tabComputerAnswer;
	
	/**
	 * stores for each computer try, and for each digit of the combination, the value of the digit and a map of its min value and max value range to get a new digit from. 
	 * 
	 * @see DefenderPlusMinus#generateAnswerWithHint()
	 * @see DefenderPlusMinus#ValuesMinMax
	 */
	private Map<Integer, Map<String, Integer>> mapValuesMinMax = new HashMap<>(); 
	
	/**
	 * Stores for each digit the min value and max value range to get a new digit from.
	 * 
	 * @see DefenderPlusMinus#generateAnswerWithHint()
	 */
	private Map<String, Integer> valuesMinMax; 
	
	/**
	 * While computer combination in List format is being compared, stores each new generated digit.  
	 * 
	 * Once comparison is finished, transfers the full new combination into computer reseted combination List.
	 * 
	 * Is then reset, to do it again. 
	 * 
	 *@see DefenderPlusMinus#generateAnswerWithHint()
	 *@see DefenderPlusMinus#tabComputerAnswer
	 */
	private List<Integer> tabComputerAnswer2 = new ArrayList<Integer>();
	
	/**
	 * Users combination in List format. 
	 * 
	 * Is used to define the length of computer answer. 
	 * 
	 * Is used to be compared with computer try and create the hint.
	 * 
	 * Is also used to verify if the computer has generated the correct answer. 
	 * 
	 * @see DefenderPlusMinus#requestUserSecretNum()
	 * @see DefenderPlusMinus#generateComputerFirstTry()
	 * @see DefenderPlusMinus#createHint(List, List)
	 * @see DefenderPlusMinus#initGame()
	 * @see DefenderPlusMinus#generateAnswerWithHint()
	 */
	private List<Integer> tabUserCode = new ArrayList<Integer>();
	
	/**
	 * Stores the users combination. 
	 * 
	 * Is used to verify if the answer is a correct entry.  
	 * 
	 * Is then transfered into a List.
	 * 
	 * @see DefenderPlusMinus#requestUserSecretNum()
	 * @see DefenderPlusMinus#tabUserCode
	 */
	private String userCode = "";
	
	/**
	 * Stores the computer answer in String format to be displayed. 
	 * 
	 * @see DefenderPlusMinus#generateComputerFirstTry()
	 * @see DefenderPlusMinus#ArrayListIntegerToString(List)
	 */
	private String computerAnswerStringFormat;
	
	/**
	 * errors allowed in the game.
	 * 
	 * Defines the number of tries the computer has.
	 * It can be modified via the dataConfig.properties file 
	 *  
	 * @see DefenderPlusMinus#setProperties()
	 * @see DenferPlusMinus#initGame()
	 */
	private int errorAllowed;
	
	/**
	 * Used to define for each digit of computer answer, the minimum value of the range min - max in which the next digit must be taken. 
	 * 
	 * It is set to 0 at the beginning. To generate the new digit, it uses the min value of each digit stored in valuesMinMax. 
	 * After processing, the new min value is replaced in valuesMinMax of the corresponding digit.      
	 * 
	 * @see DefenderPlusMinus#generateAnswerWithHint()
	 * @see DefenderPlusMinus#valuesMinMax
	 */
	private int minValue;
	
	/**
	 * Used to define for each digit of computer answer, the maximum value of the range min - max in which the next digit must be taken. 
	 * 
	 * It is set to 9 at the beginning. To generate the new digit, it uses the max value of each digit stored in valuesMinMax. 
	 * After processing, the new max value is replaced in valuesMinMax of the corresponding digit.      
	 * 
	 * @see DefenderPlusMinus#generateAnswerWithHint()
	 * @see DefenderPlusMinus#valuesMinMax
	 */
	private int maxValue;
	
	/**
	 * Number of tries made by the Computer.
	 * 
	 * Each try increases by 1 the score.
	 * 
	 * To define if the game has to end, score is compared to number of errors allowed.
	 * 
	 * Used to display the number of tries at the end of the game.
	 * 
	 * @see DefenderPlusMinus#initGame()
	 * @see DefenderPlusMinus#errorAllowed
	 * 
	 */
	private int score = 0; 
	
	private static final Logger log = Logger.getLogger(DefenderPlusMinus.class);
	
	/**
	 * Constructor of Defender PlusMinus.
	 * 
	 * When the class is instantiated, load properties to be used by the game.
	 * 
	 * @see ChallengerPlusMinus#setProperties()
	 * @see ChallengerPlusMinus#errorAllowed
	 */
	public DefenderPlusMinus () {
		if(setProperties())
			startTheGame();
	}
	
	/**
	 * Starts the game.  
	 * 
	 * The content is read only if properties are well set.
	 * 
	 * A welcome screen is displayed.
	 * 
	 * The secret combination is displayed if the developer mode is activated.
	 * 
	 * Displays a request for user to make an entry. 
	 * 
	 * Initiate the game. 
	 * 
	 * @see DefenderPlusMinus#setProperties()
	 * @see DenferPlusMinus#printWelcome()
	 * @see DefenderPlusMinus#requestUserSecretNum()
	 * @see DefenderPlusMinus#initGame()
	 */
	@Override
	public void startTheGame() {
		log.info("Start of PlusMinus game in defender mode");
		printWelcome();
		requestUserSecretNum();
		initGame();
		log.info("End of the game");
	}
	
	/**
	 * Returns true if the properties are set.
	 * 
	 *  @see DefenderPlusMinus#errorAllowed
	 */
	@Override
	public boolean setProperties() {
		errorAllowed = Integer.parseInt(PropertiesFile.getPropertiesFile("errorAllowed"));
		log.info("Properties set successfully");
		return true; 
	}
	
	/**
	 * Display the welcome screen.
	 * 
	 * @see DefenderPlusMinus#startTheGame()
	 */ 
	@Override
	public void printWelcome() {
		String 	str = TextEnhencer.ANSI_YELLOW;
				str += "******************************************\n";
				str += "*******        WELCOME TO          *******\n";
				str += "*******        + or - GAME         *******\n";
				str += "*******       DEFENDER MODE        *******\n";	
				str += "******************************************\n";
				str += TextEnhencer.ANSI_RESET;
		System.out.println(str); 
	}
	
	/**
	 * Request the user to enter a secret combination and create an ArrayList with the entry.
	 * 
	 * also verify if the entry is correct. 
	 * 
	 * @see DefenderPlusMinus#userCode 
	 * @see DefenderPlusMinus#tabUserCode
	 */
	private void requestUserSecretNum() {
		Scanner clavier = new Scanner(System.in);
		
		System.out.println(TextEnhencer.ANSI_YELLOW + "Please enter your secret combination below " + TextEnhencer.ANSI_RESET); 
		
		//Using Regex to make sure user enter the right value type (Integer).
		do {
			if(!userCode.equals("")) {
				if(!userCode.matches("^[./[0-9]]+$")) { 
					System.out.println(TextEnhencer.ANSI_RED + "Please enter a number instead of a characters.");
					log.warn("User entry mismatch the type required");
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
	
	/**
	 * Initiate the game. 
	 * 
	 * Increases the number of tries of the user.
	 * 
	 * Generates a first random answer, then answer taking in consideration the hint.  
	 * 
	 * Computer answer is compared to users entry.
	 * 
	 * The result in form of a chain of indications "+" or "-" is stored in hint. 
	 * 
	 * Computer answer is transformed in String format.
	 * 
	 * Computer answer and hint are then displayed to the user.
	 * 
	 * All those steps are repeated as long as the secret combination is not found or if the value of error allowed isn't reached by score value. 
	 * 
	 * If user find the secret combination, he wins. A drawing and a message are displayed. Otherwise, user loose. a message is displayed.     
	 * 
	 * @see DefenderPlusMinus#score
	 * @see DefenderPlusMinus#generateComputerFirstTry()
	 * @see DefenderPlusMinus#generateAnswerWithHint()
	 * @see DefenderPlusMinus#tabComputerAnswer
	 * @see DefenderPlusMinus#createHint(List, List)
	 * @see DefenderPlusMinus#hint
	 * @see DefenderPlusMinus#ArrayListIntegerToString(List)
	 * @see ChallengerPlusMinus#errorAllowed
	 * @see Mode#displayFish()
	 */
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
			Fish.displayFish();
			System.out.printf(TextEnhencer.ANSI_YELLOW + "\n\t   .+*°*+.+> | Congratulation ! | <+.+*°*+.\n\t Computer couldn't find your code after %d attempts" + TextEnhencer.ANSI_RESET, score );	
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
	 * @see DefenderPlusMinus#computerAnswerStringFormat
	 */
	private List<Integer> generateComputerFirstTry() {
		// Generates a secreteNumber of the same length as user's answer.  
		sNG = new SecretNumGenerator(tabUserCode.size());
		tabComputerAnswer = sNG.getTabNumber();
		computerAnswerStringFormat = sNG.getRandomNumber();
		
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
	 * @see DefenderPlusMinus#valuesMinMax
	 * @see DefenderPlusMinus#tabComputerAnswer2
	 * @see DefenderPlusMinus#mapValuesMinMax
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

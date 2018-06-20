package com.philippe75.plus_minus;

import java.awt.DisplayMode;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

import org.apache.log4j.Logger;

import com.philippe75.game.Fish;
import com.philippe75.game.Game;
import com.philippe75.game.Main;
import com.philippe75.game.IGame;
import com.philippe75.game.PropertiesFile;
import com.philippe75.game.TextEnhencer;
import com.philippe75.generators.SecretNumGenerator;


/**
 * <b>ChallengerPlusMinus is a class that handle the PlusMinus game in Challenger IGame.</b>
 * <p>Steps of the game : 
 * <ul>
 * <li>A random combination is generated.</li>
 * <li>User tries a combination.</li>
 * <li>for each answer an hint is return. This hint indicates for each digit of the user answer if the secret combination digit is higher or lower.</li>
 * <li>User tries combinations until he finds the answer or the number of tries permitted is reached.</li>
 * <li>If user finds the secret combination he wins, otherwise he looses.</li>
 * </ul>
 * </p>
 * 
 * <p>
 * The random combination is generate via a secret combination generator. 
 * </p>
 * 
 * <p>
 * The secret combination length and the number of errors allowed can be set in a DataConfig.properties file.
 * </p>
 * 
 * @see SecretNumGenerator
 * @see ChallengerPlusMinus#setProperties()
 * 
 * @author PPlaxine
 * @version 1.0
 */
public class ChallengerPlusMinus extends Game{
		
	/**
	 * Secret combination generator. 
	 * 
	 * For more information about the generator, please see SecretNumberGenerator class doc.  
	 * 
	 * @see SecretNumGenerator
	 * @see ChallengerPlusMinus#startTheGame()
	 */
	private SecretNumGenerator sNG;
		
	/**
	 * errors allowed in the game.
	 * 
	 * Defines the number of tries the user has.
	 * It can be modified via the dataConfig.properties file 
	 *  
	 * @see ChallengerPlusMinus#setProperties()
	 * @see ChallengerPlusMinus#initGame()
	 */
	private int errorAllowed;
		
	/**
	 * combination length for the game. 
	 * 
	 * Defines the length of the secret combination to be generated
	 * It can be modified via the dataConfig.properties file 
	 * 
	 * @see ChallengerPlusMinus#setProperties()
	 * @see ChallengerPlusMinus#startTheGame()
	 */
	private int combiLength; 
		
	/**
	 * hint for users next try. 
	 * 
	 * Contains a chain of indications "+" or "-" corresponding to the comparison between users answer digits and secret combination digits.  
	 * 
	 * Is also used to display the hint to the user.
	 * 
	 * @see ChallengerPlusMinus#generateHint(List, List)
	 * @see ChallengerPlusMinus#initGame()
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
	 * @see ChallengerPlusMinus#initGame()
	 * @see ChallengerPlusMinus#tabUserAnswer
	 */
	private String userAnswer = "";
		
	/**
	 * User answer in List format.
	 * 
	 * Store each digit of users Answer as an element of the List. 
	 * 
	 * The List is then used for comparison with secret combination list. 
	 *  
	 * @see ChallengerPlusMinus#initGame()
	 * @see ChallengerPlusMinus#generateHint(List, List)
	 * @see SecretNumGenerator#getTabNumber() 
	 */
	private List<Integer> tabUserAnswer; 
		
	/**
	 * Number of tries made by the user.
	 * 
	 * Each try increases by 1 the score.
	 * 
	 * To define if the game has to end, score is compared to number of errors allowed.
	 * 
	 * Used to display the number of tries at the end of the game.
	 * 
	 * @see ChallengerPlusMinus#initGame()
	 * @see ChallengerPlusMinus#errorAllowed
	 * 
	 */
	private int score = 0; 
		
	/**
	 * Runs the game in developer mode if true is returned.
	 * 
	 * By default, if an argument -dev is passed when starting the program, the boolean will return the value true. 
	 * 
	 * Returns also true, if in dataProperties file the value of devMode is set to true;   
	 *  
	 * @see ChallengerPlusMinus#displaySecretNum()
	 * @see Main#isDev()
	 * @see Main#dev
	 * @see Main#main(String[])
	 * @see ChallengerPlusMinus#setProperties()
	 */
	boolean dev = Main.isDev();
	
	/**
	 * Creates a logger to generate log of the class.	
	 */
	private static final Logger log = Logger.getLogger(ChallengerPlusMinus.class);
	
	/**
	 * Constructor of ChallengerPlusMinus.
	 * 
	 * When the class is instantiated, load properties to be used by the game.
	 * 
	 * @see ChallengerPlusMinus#setProperties()
	 * @see ChallengerPlusMinus#combiLength
	 * @see ChallengerPlusMinus#errorAllowed
	 * @see ChallengerPlusMinus#dev
	 */
	public ChallengerPlusMinus() {	
		if (setProperties())
			startTheGame();
	}
	
	/**
	 * Starts the game.  
	 * 
	 * A new secret combination is generated by instancing the secret combination generator.
	 * 
	 * A welcome screen is displayed.
	 * 
	 * The secret combination is displayed if the developer mode is activated.
	 * 
	 * Displays a request for user to make an entry. 
	 * 
	 * Initiate the game. 
	 * 
	 * @see SecretNumGenerator
	 * @see ChallengerPlusMinus#printWelcome()
	 * @see ChallengerPlusMinus#displaySecretNum()
	 * @see ChallengerPlusMinus#initGame()
	 */
	@Override
	public void startTheGame() {
		log.info("Start of PlusMinus game in challenger mode");
		sNG = new SecretNumGenerator(combiLength);
		printWelcome();	
		displaySecretNum(); 
		initGame();
		log.info("End of the game");
	}
	
	/**
	 * Returns true if the properties are set.
	 * 
	 *  @see ChallengerPlusMinus#combiLength
	 *  @see ChallengerPlusMinus#errorAllowed
	 *  @see ChallengerPlusMinus#dev
	 * 	@see ChallengerPlusMinus#startTheGame()
	 */
	@Override
	public boolean setProperties() {
	
		combiLength = Integer.parseInt(PropertiesFile.getPropertiesFile("CombinationLength"));
		errorAllowed = Integer.parseInt(PropertiesFile.getPropertiesFile("errorAllowed"));
		if(new String("true").equals(PropertiesFile.getPropertiesFile("devMode"))) {
			this.dev = true;
		}	
		log.info("Properties set successfully");
		return true;
	}
	
	/**
	 * Display the welcome screen.
	 * 
	 * @see ChallengerPlusMinus#startTheGame()
	 */
	@Override
	public void printWelcome() {
		String 	str = TextEnhencer.ANSI_YELLOW; 
				str += "******************************************\n";
				str += "*******        WELCOME TO          *******\n";
				str += "*******        + or - GAME         *******\n";
				str += "*******      CHALLENGER MODE       *******\n";	
				str += "******************************************";
				str += TextEnhencer.ANSI_RESET;
		System.out.println(str); 
	}
	


	/**
	 * Displays the secret combination if mode dev is activated
	 *  
	 * @see ChallengerPlusMinus#dev
	 * @see ChallengerPlusMinus#startTheGame()
	 */
	protected void displaySecretNum() {
		System.out.println(TextEnhencer.ANSI_CYAN+ "\nComputer has generated a secret combination for you to guess ..." + TextEnhencer.ANSI_RESET);
		if(dev) {
			log.info("Game is running in developer mode");
			System.out.println(TextEnhencer.ANSI_CYAN + "\t*** Secret combination : " + sNG.getRandomNumber() + " ***\n" + TextEnhencer.ANSI_RESET);
		}
	}
	
	/**
	 * Initiate the game. 
	 * 
	 * Increases the number of tries of the user.
	 * 
	 * Store, after checking if entry is correct, users answer. The Entry is in form of chain of numbers.
	 * 
	 * The content of userAnswer is transfered to tabUserAnswer ArrayList in order to make a comparison with the secret combination contained also in a List. 
	 * 
	 * The result in form of a chain of indications "+" or "-" is stored in hint. hint is then displayed to the user.
	 * 
	 * All those steps are repeated as long as the secret combination is not found or if the value of error allowed isn't reached by score value. 
	 * 
	 * If user find the secret combination, he wins. A drawing and a message are displayed. Otherwise, user loose. a message is displayed.     
	 * 
	 * @see ChallengerPlusMinus#score
	 * @see ChallengerPlusMinus#userAnswer
	 * @see ChallengerPlusMinus#tabUserAnswer
	 * @see ChallengerPlusMinus#generateHint(List, List)
	 * @param sNG.getTabNumber()
	 * 								The secret combination contained in a List  
	 * @param tabUserAnswer 
	 * 								The user answer contained in a List
	 * @see ChallengerPlusMinus#hint
	 * @see ChallengerPlusMinus#errorAllowed
	 * @see IGame#displayFish()
	 */
	public void initGame() {
		tabUserAnswer = new ArrayList<Integer>();
		
		// Repeat the question while user has enough tries left and hasn't found the answer
		do {
			// add a try after each question 
			score++;
			requestUserAnswer();		
			generateUserHint();
	
		} while (!sNG.getTabNumber().toString().equals(tabUserAnswer.toString()) && score < errorAllowed);
		
		// Print Result once the game is over. 
		if (sNG.getTabNumber().toString().equals(tabUserAnswer.toString())){
			Fish.displayFish();
			System.out.printf(TextEnhencer.ANSI_YELLOW + "\t   .+*�*+.+> | Congratulation !!! | <+.+*�*+.\n\t   You found the answer after %d trials!!! \n"+ TextEnhencer.ANSI_RESET, score);
		}else {
			System.out.printf(TextEnhencer.ANSI_RED + "\t   .+*�*+.+> | GAME OVER !!!! | <+.+*�*+.\n"+ TextEnhencer.ANSI_CYAN +  "\t\t The secret number was %s \n", sNG.getRandomNumber() + TextEnhencer.ANSI_RESET);
		}
	}
	
	/**
	 *  Request the user to make a keyboard entry.
	 *  
	 *  also verify if the entry is correct.
	 *  
	 *  @see ChallengerPlusMinus#userAnswer
	 */
	private void requestUserAnswer() {
		Scanner clavier = new Scanner(System.in);
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
			System.out.print(TextEnhencer.ANSI_YELLOW);
			System.out.println("Please enter a number of " + sNG.getNumberSize() + (sNG.getNumberSize() > 1 ? " digits." : " digit."));			
			this.userAnswer = clavier.nextLine();
			System.out.print(TextEnhencer.ANSI_RESET);
		} while (!userAnswer.matches("^[./[0-9]]+$") || sNG.getNumberSize() != userAnswer.length());
	}
	
	/**
	 * Display a hint for players next move.
	 * 
	 * @see ChallengerPlusMinus#generateHint(List, List)
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
}

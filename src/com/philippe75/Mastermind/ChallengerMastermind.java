package com.philippe75.mastermind;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.apache.log4j.Logger;

import com.philippe75.game.Fish;
import com.philippe75.game.HowManyColors;
import com.philippe75.game.Main;
import com.philippe75.game.Mode;
import com.philippe75.game.PropertiesFile;
import com.philippe75.game.TextEnhencer;
import com.philippe75.generators.SecretColorCombinationGenerator;

/**
 * <b>ChallengerMastermind is a class that handle the Mastermind game in Challenger Mode.</b>
 * <p>Steps of the game : 
 * <ul>
 * <li>A random colour combination is generated.</li>
 * <li>User tries a combination.</li>
 * <li>for each answer an hint is return. This hint indicates, how many colours are well placed and how many are present but not well placed in the combination.</li>
 * <li>User tries combinations until he finds the answer or the number of tries permitted is reached.</li>
 * <li>If user finds the secret combination he wins, otherwise he looses.</li>
 * </ul>
 * </p>
 * 
 * <p>
 * The random colour combination is generated via a SecretColoCombinationGenerator. 
 * </p>
 * 
 * <p>
 * The secret combination length, the pool of colours, and the number of errors allowed can be set in a DataConfig.properties file.
 * </p>
 * 
 * @see SecretColorCombinationGenerator
 * @see ChallengerMastermind#setProperties()
 * 
 * @author PPlaxine
 * @version 1.0
 */
public class ChallengerMastermind implements Mode{
	
	/**
	 * Secret colour combination generator. 
	 * 
	 * For more information about the generator, please see SecretColorCombinationGenerator class doc.  
	 * 
	 * @see SecretColorCombinationGenerator
	 * @see ChallengerMastermind#startTheGame()
	 */
	private SecretColorCombinationGenerator sCG;
	
	/**
	 * errors allowed in the game.
	 * 
	 * Defines the number of tries the user has.
	 * It can be modified via the dataConfig.properties file 
	 *  
	 * @see ChallengerMastermind#setProperties()
	 * @see ChallengerMastermind#initGame()
	 */
	private int errorAllowed;
	
	/**
	 * combination length for the game. 
	 * 
	 * Defines the length of the secret combination to be generated
	 * It can be modified via the dataConfig.properties file 
	 * 
	 * @see ChallengerMastermind#setProperties()
	 * @see ChallengerMastermind#startTheGame()
	 */
	private int combiLength;
	
	/**
	 * Store the number of colours at the correct position. 
	 * 
	 *  @see ChallengerMastermind#initGame()
	 *  @see ChallengerMastermind#compareAnswer()
	 */
	private int correctPosition;
	
	/**
	 * Pool of colours used for the game. 
	 * 
	 * @see ChallengerMastermind#printQuestion()
	 * @see ChallengerMastermind#compareAnswer()
	 */
	private Map<Integer, String> colorPool = new HashMap<>(); 
	
	/**
	 * Store combination generated by the SecretColorCombinationGenerator
	 * 
	 * @see SecretColorCombinationGenerator
	 * @see ChallengerMastermind#initGame()
	 * @see ChallengerMastermind#compareAnswer()
	 */
	private Map<Integer, String> tabColCombi = new HashMap<>(); 
	
	/**
	 *Users answer in Map format, used for comparison. 
	 *
	 * @see ChallengerMastermind#initGame()
	 * @see ChallengerMastermind#compareAnswer()
	 */
	private Map<Integer, String> tabUserAnswer = new HashMap<>();
	
	/**
	 * Used to compare User answer and the secret colour combination. 
	 * 
	 * @see ChallengerMastermind#compareAnswer()
	 */
	private Map<Integer, String> tabToCompare = new HashMap<>(); 
	
	/**
	 * Enumeration that define the size of the colour pool to request to SecretColorCombinationGenerator. 
	 * 
	 * @see SecretColorCombinationGenerator
	 * @see ChallengerMastermind#setProperties()
	 * @see ChallengerMastermind#startTheGame()
	 */
	private HowManyColors howManyColors; 
	
	/**
	 * User answer in String format with numerical value. 
	 * 
	 * @see ChallengerMastermind#initGame()
	 * @see ChallengerMastermind#getUserAnswer()
	 * @see ChallengerMastermind#compareAnswer()
	 */
	private String userAnswer=""; 
	
	
	Scanner clavier = new Scanner(System.in);
	
	/**
	 * Runs the game in developer mode if true is returned.
	 * 
	 * By default, if an argument -dev is passed when starting the program, the boolean will return the value true. 
	 * 
	 * Returns also true, if in dataProperties file the value of devMode is set to true;   
	 *  
	 * @see ChallengerMastermind#displaySecretNum()
	 * @see Main#isDev()
	 * @see Main#dev
	 * @see Main#main(String[])
	 * @see ChallengerMastermind#setProperties()
	 */
	private boolean dev = Main.isDev();
	
	/**
	 * Creates a logger to generate log of the class.	
	 */
	private static final Logger log = Logger.getLogger(ChallengerMastermind.class);

	/**
	 * Constructor of ChallengerMastermind.
	 * 
	 * When the class is instantiated, load properties to be used by the game.
	 * 
	 * @see ChallengerMastermind#setProperties()
	 * @see ChallengerMastermind#howManyColors
	 * @see ChallengerMastermind#combiLength
	 * @see ChallengerMastermind#errorAllowed
	 * @see ChallengerMastermind#dev
	 */
	public ChallengerMastermind() {
		if(setProperties())
			startTheGame();
	}
	
	/**
	 * Starts the game.  
	 * 
	 * A new secret combination is generated by instancing SecretColorCombinationGenerator.
	 * 
	 * A welcome screen is displayed.
	 * 
	 * The secret combination is displayed if the developer mode is activated.
	 * 
	 * Displays a request for user to make an entry. 
	 * 
	 * Initiate the game. 
	
	 * @see SecretColorCombinationGenerator
	 * @see ChallengerMastermind#printWelcome()
	 * @see ChallengerMastermind#displaySecretNum()
	 * @see ChallengerMastermind#printQuestion()
	 * @see ChallengerMastermind#initGame()
	 */
	@Override
	public void startTheGame() {
		log.info("Start of Mastermind game in challenger mode");
		sCG = new SecretColorCombinationGenerator(combiLength, howManyColors);
		printWelcome();
		displaySecretColorCombi();
		printQuestion();
		initGame();
		log.info("End of the game");
	}
	
	/**
	 * Returns true if the properties are set.
	 * 
	 * 	@see ChallengerMastermind#howManyColors
	 *  @see ChallengerMastermind#combiLength
	 *  @see ChallengerMastermind#errorAllowed
	 *  @see ChallengerMastermind#dev
	 * 	@see ChallengerMastermind#startTheGame()
	 */
	@Override
	public boolean setProperties() {
		
		howManyColors = HowManyColors.valueOf((PropertiesFile.getPropertiesFile("ColorPool")));
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
	 * @see ChallengerMastermind#startTheGame()
	 */
	@Override
	public void printWelcome() {
		String 	str = TextEnhencer.ANSI_YELLOW;
				str += "\n\n******************************************\n";
				str += "*******         WELCOME TO         *******\n";
				str += "*******      MASTERMIND GAME       *******\n";
				str += "*******      CHALLENGER MODE       *******\n";	
				str += "******************************************";
				str += TextEnhencer.ANSI_RESET;
		System.out.println(str); 
	}
	
	/**
	 * Displays the secret combination if mode dev is activated
	 *  
	 * @see ChallengerMastermind#dev
	 * @see ChallengerMastermind#startTheGame()
	 */
	private void displaySecretColorCombi() {
		System.out.println(TextEnhencer.ANSI_CYAN+ "\nComputer has generated a secret combination for you to guess ..." + TextEnhencer.ANSI_RESET);
		if(dev) {
			log.info("Game is running in developer mode");
			System.out.printf(TextEnhencer.ANSI_CYAN + "*** Secret Color Combination : %s ***\n\n" + TextEnhencer.ANSI_RESET, sCG.toString());
		}
	}
	
	/**
	 * Prints the Question with the random generated colours to choose from. 
	 * 
	 *  @see ChallengerMastermind#sCG
	 *  @see ChallengerMastermind#colorPool
	 */
	private void printQuestion() {	
		String str ="";
		
		 
		for (int i = 0; i < sCG.getColorPool().size(); i++) {
			
			str += " ["+ (i) +"]" + sCG.getColorPool().get(i);
			
			// add each colour of a secret combination to colorPool 
			colorPool.put(i, sCG.getColorPool().get(i)); 
		}
		//print the question containing the colorpool choices 
		System.out.printf(TextEnhencer.ANSI_YELLOW + "Please enter a combination of %d choices amongst those colors :%s.\n" + TextEnhencer.ANSI_RESET, combiLength , str);
	}
	
	/**
	 * Initiate the game. 
	 * 
	 * Increases the number of tries of the user.
	 * 
	 * Get the user answer. 
	 * 
	 * Compare the user answer. 
	 * 
	 * All those steps are repeated as long as the secret combination is not found or if the value of error allowed isn't reached by score value. 
	 * 
	 * If user finds the secret combination, he wins. A drawing and a message are displayed. Otherwise, user loose. a message is displayed.     
	 * 
	 * @see ChallengerMastermind#tries
	 * @see ChallengerMastermind#getUserAnswer()
	 * @see ChallengerMastermind#compareAnswer()
	 * @see ChallengerMastermind#errorAllowed
	 * @see Mode#displayFish()
	 */
	private void initGame() {
		// store the combination in tabColComni 
		tabColCombi = sCG.getTabColorCombination(); 
		int tries = 0; 
			
		do {		
				// add a try after each question 
				tries++;
				tabUserAnswer.clear();
				getUserAnswer();
				compareAnswer();
				userAnswer = ""; 
				
			} while (correctPosition != this.combiLength && tries < this.errorAllowed);
		if(correctPosition == this.combiLength) {
			Fish.displayFish();
			System.out.printf( TextEnhencer.ANSI_YELLOW + "\n\t  .+*�*+..+> | Congratulations ! | <+..+*�+.\nYou have found the correct secret color combination after %d " + ((tries < 2)? "trial." : "trials.") + "\n" + TextEnhencer.ANSI_RESET, tries);
		}else {
			System.out.println(TextEnhencer.ANSI_RED + "\n\t\t\t .+*�*+..+> | GAME OVER !!! | <+..+*�+."+ TextEnhencer.ANSI_CYAN + "\n\t\t\t\tYou were almost there.\nThe solution is "+ sCG.toString() +". I am sure you will be more succeful next time!\n"  + TextEnhencer.ANSI_RESET);
		}
	}
	
	/**
	 *  Request the user to make a keyboard entry.
	 *  
	 *  also verify if the entry is correct.
	 *  
	 *  @see ChallengerMastermind#userAnswer
	 */
	private void getUserAnswer() {
		 
		// Repeat while user didn't enter the correct value 
		do {
			//Using Regex to make sure user enter the right value type (Integer) and the correct length of this value type  
			if(userAnswer !="") {
				if(!userAnswer.matches("^[./[0-9]]+$")) { 
					System.out.println(TextEnhencer.ANSI_RED + "Please enter a number instead of a characters." + TextEnhencer.ANSI_RESET);
					log.warn("User entry mismatch the type required");
				}else if (!userAnswer.matches("^[./[0-"+ (sCG.getColorPool().size()-1) + "]]+$")) { 
					System.out.printf(TextEnhencer.ANSI_RED + "You have to enter a number bewteen [0] and [%d]\n" + TextEnhencer.ANSI_RESET, sCG.getColorPool().size()-1);
					log.warn("User entry mismatch the possible answer choices");
				}else {	
					System.out.println((combiLength < userAnswer.length())? TextEnhencer.ANSI_RED + "The number of digits is superior to the number of digits required" + TextEnhencer.ANSI_RESET 
							: TextEnhencer.ANSI_RED + "The number of digits is inferior to the number of digits required" + TextEnhencer.ANSI_RESET);
					log.warn("User entry mismatch the length of digits required");
				}
			}
			// store the user answer in String but with digits
			System.out.print(TextEnhencer.ANSI_YELLOW);
			this.userAnswer = clavier.nextLine();
			System.out.print(TextEnhencer.ANSI_RESET);
				
		} while (!userAnswer.matches("^[./[0-9]]+$") || combiLength != userAnswer.length() 
				|| !userAnswer.matches("^[./[0-"+(sCG.getColorPool().size()-1) +"]]+$") );		
	}
	
	/**
	 * Compare user answer with the secret colours combination. 
	 * 
	 * Indicates how many colours are well placed and how many are present but not well placed in the combination.
	 * 
	 * @see ChallengerMastermind#tabUserAnswer
	 * @see ChallengerMastermind#tabToCompare
	 */
	private void compareAnswer() {
		
			String str=""; 
			int index = 0;
			correctPosition = 0; 
			int exist = 0;
			tabToCompare.putAll(tabColCombi);
					
		for (int i = 0; i < userAnswer.length(); i++) {
			// userAnswer converted in int and stored in index 
			index = Character.getNumericValue((userAnswer.charAt(i)));
			// then converted in colour and stored in MAP 
			tabUserAnswer.put(i,(String)colorPool.get(index));
			// each element is also added to str for print
			str += "|" + tabUserAnswer.get(i) + "|"; 		
		}
		
		
		// check if any colour is at the same position in tabUserAnswer and tabToCompare 
		for (int i = 0; i < tabUserAnswer.size(); i++) {
			//if element at the correct position 
			if(tabUserAnswer.get(i).equals(tabToCompare.get(i))){
				//increase correctPosition 
				correctPosition++;
				// and replace the value in both tabs 
				tabToCompare.replace(i, "Found");
				tabUserAnswer.replace(i, "Found2"); 
			}
		}
		
		// amongst the remaining values check if there is any match 
		for (int i = 0; i < tabUserAnswer.size(); i++) {
			for (int j = 0; j < tabUserAnswer.size(); j++) {
				// if tabUserAnswer contains at any position an element contained in tabCompare
				if(tabUserAnswer.get(i).equals(tabToCompare.get(j))) {
					// replace the value in both tabs 
					tabToCompare.replace(j, "Found3");
					tabUserAnswer.replace(i, "Found4");
					//increase exist 
					exist++;
				}
			}	
		}
		
		System.out.printf(TextEnhencer.ANSI_CYAN + "Your anwser is %s ---> %d well placed, %d exist but not well placed.\n" + TextEnhencer.ANSI_RESET,str, correctPosition, exist);
	}
}

package com.philippe75.mastermind;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import org.apache.log4j.Logger;

import com.philippe75.game.Fish;
import com.philippe75.game.HowManyColors;
import com.philippe75.game.Main;
import com.philippe75.game.IGame;
import com.philippe75.game.PropertiesFile;
import com.philippe75.game.TextEnhencer;
import com.philippe75.generators.SecretColorCombinationGenerator;
import com.philippe75.plus_minus.DefenderPlusMinus;

/**
 * <b>DefenderMastermind is a class that handle the Mastermind game in Defender IGame.</b>
 * <p>Steps of the game : 
 * <ul>
 * <li>A colour pool is created and the choices are displayed to the users.</li>
 * <li>User creates a colour combination, to be guessed by the computer, amongst those colours.</li>
 * <li>Computer tries a combination.</li>
 * <li>for each answer an hint is return. This hint indicates, how many colours are well placed and how many are present but not well placed in the combination.</li>
 * <li>Computer tries combinations until it finds the answer or the number of tries permitted is reached.</li>
 * <li>If Computer finds the secret combination, user looses, otherwise user wins.</li>
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
 * @see DefenderMastermind#setProperties()
 * 
 * @author PPlaxine
 * @version 1.0
 */
public class DefenderMastermind implements IGame{

	/**
	 * combination length for the game. 
	 * 
	 * Defines the length of the secret combination to be generated
	 * It can be modified via the dataConfig.properties file 
	 * 
	 * @see DefenderMastermind#setProperties()
	 * @see DefenderMastermind#startTheGame()
	 */
	private int combiLength; 
	
	/**
	 * errors allowed in the game.
	 * 
	 * Defines the number of tries the computer has.
	 * It can be modified via the dataConfig.properties file 
	 *  
	 * @see DefenderMastermind#setProperties()
	 * @see DefenderMastermind#initGame()
	 */
	private int errorAllowed;
	
	/**
	 * Store the number of colours at the correct position. 
	 * 
	 *  @see DefenderMastermind#initGame()
	 *  @see DefenderMastermind#compareAnswer()
	 */
	private int correctPosition; 
	
	/**
	 * Stores the users combination of numerical value in String format. 
	 * 
	 * Is used to verify if the answer is a correct entry.  
	 * 
	 * @see DefenderMastermind#requestUserSecretCombi()
	 * @see DefenderMastermind#userSelection
	 */
	private String userAnswer="";
	
	/**
	 * Enumeration that define the size of the colour pool to request to SecretColorCombinationGenerator. 
	 * 
	 * @see SecretColorCombinationGenerator
	 * @see DefenderMastermind#setProperties()
	 * @see DefenderMastermind#startTheGame()
	 */
	private HowManyColors howManyColors;
	
	/**
	 * Stores user combination with colours as values in String format. 
	 * 
	 * Also used to compare and generates computer answer.   
	 * 
	 * @see DefenderMastermind#requestUserSecretCombi()
	 * @see DefenderMastermind#compareAnswer()
	 * @see DefenderMastermind#generateComputerAnswer()
	 */
	private Map<Integer, String> userSelection; 
	
	/**
	 * Stores the computer answer in Map format for comparison. 
	 * 
	 * @see DefenderMastermind#compareAnswer()
	 * @see DefenderMastermind#generateComputerAnswer()
	 */
	private Map<Integer, String> tabComputerAnswer = new HashMap<>();
	
	/**
	 * Used to compare computers answer and the users secret colour combination. 
	 * 
	 * @see DefenderMastermind#compareAnswer()
	 */
	private Map<Integer, String> tabToCompare = new HashMap<>();	
	
	/**
	 * Store the colours present but not well placed, to be re-injected in the next computer answer. 
	 * 
	 * @see DefenderMastermind#generateComputerAnswer()
	 * @see DefenderMastermind#compareAnswer()
	 */
	private List<String> tabColorToReinject = new ArrayList<>();
	
	/**
	 * Pool of colours of the size defined by HowManyColors, for the player to choose from. 
	 * 
	 * @see DefenderMastermind#initiateColorChoice()
	 * @see DefenderMastermind#generateQuestion()
	 * @see DefenderMastermind#requestUserSecretCombi()
	 * @see DefenderMastermind#generateComputerAnswer()
	 */
	private List<String> tabColorPool;
	
	/**
	 * Pool containing all the possible colours.
	 * 
	 * @see DefenderMastermind#initiateColorChoice()
	 * @see DefenderMastermind#tabColorPool
	 */
	private List<String> tabColor;
	
	Scanner clavier = new Scanner(System.in);
	
	/**
	 * Runs the game in developer mode if true is returned.
	 * 
	 * By default, if an argument -dev is passed when starting the program, the boolean will return the value true. 
	 * 
	 * Returns also true, if in dataProperties file the value of devMode is set to true;   
	 *  
	 * @see DefenderMastermind#displaySecretNum()
	 * @see Main#isDev()
	 * @see Main#dev
	 * @see Main#main(String[])
	 * @see ChallengerMastermind#setProperties()
	 */
	private boolean dev = Main.isDev();
	
	/**
	 * Creates a logger to generate log of the class.	
	 */
	private static final Logger log = Logger.getLogger(DefenderMastermind.class);
	
	/**
	 * Constructor of DenfenderMastermind.
	 * 
	 * When the class is instantiated, load properties to be used by the game.
	 * 
	 * @see DefenderMastermind#setProperties()
	 * @see DefenderMastermind#howManyColors
	 * @see DefenderMastermind#combiLength
	 * @see DefenderMastermind#errorAllowed
	 * @see DefenderMastermind#dev
	 */
	public DefenderMastermind() {
		if(setProperties())
			startTheGame();
	}
	
	/**
	 * Starts the game.  
	 * 
	 * A welcome screen is displayed.
	 * 
	 * Displays a request for user to make an entry. 
	 * 
	 * Initiate the game.
	 *  
	 * @see DefenderMastermind#printWelcome()
	 * @see DefenderMastermind#initiateColorChoice()
	 * @see DefenderMastermind#generateQuestion()
	 * @see DefenderMastermind#requestUserSecretCombi()
	 * @see DenfenderMastermind#initGame()
	 */
	@Override
	public void startTheGame() {
		log.info("Start of Mastermind game in defender mode");
		if(dev)
			log.info("Game is running in developer mode");
		printWelcome();
		initiateColorChoice();
		generateQuestion();
		requestUserSecretCombi();
		initGame();
		log.info("End of the game");
	}
	
	/**
	 * Returns true if the properties are set.
	 * 
	 * 	@see DefenderMastermind#howManyColors
	 *  @see DefenderMastermind#combiLength
	 *  @see DefenderMastermind#errorAllowed
	 *  @see DefenderMastermind#dev
	 * 	@see DefenderMastermind#startTheGame()
	 */
	@Override
	public boolean setProperties() {
		
		howManyColors = HowManyColors.valueOf((PropertiesFile.getPropertiesFile("ColorPool")));
		combiLength = Integer.parseInt(PropertiesFile.getPropertiesFile("CombinationLength"));
		errorAllowed = Integer.parseInt(PropertiesFile.getPropertiesFile("errorAllowed"));
		if(new String("true").equals(PropertiesFile.getPropertiesFile("devMode"))) {
			this.dev = true;
		}
		log.info("Properties have been sucessfully set");
		return true; 
	}

	/**
	 * Display the welcome screen.
	 * 
	 * @see DefenderMastermind#startTheGame()
	 */
	@Override
	public void printWelcome() {
		String 	str = TextEnhencer.ANSI_YELLOW; 
				str += "\n\n******************************************\n";
				str += "*******         WELCOME TO         *******\n";
				str += "*******      MASTERMIND GAME       *******\n";
				str += "*******       DEFENDER MODE        *******\n";	
				str += "******************************************";
				str += TextEnhencer.ANSI_RESET;
		System.out.println(str); 
	}
	
	/**
	 * Create a random pool of colour.
	 * 
	 *  @see DefenderMastermind#tabColorPool
	 *  @see DefenderMastermind#tabColor
	 *  @see DefenderMastermind#howManyColors
	 */
	private void initiateColorChoice() {
		tabColorPool = new ArrayList<String>();
		tabColor = new ArrayList<String>();
		
		tabColor.add("Red");
		tabColor.add("Blue");
		tabColor.add("Green");
		tabColor.add("Yellow");
		tabColor.add("Orange");
		tabColor.add("Purple");
		tabColor.add("Brown");
		tabColor.add("Pink");
		tabColor.add("Gold");
		tabColor.add("Silver");
		
		Collections.shuffle(tabColor);
		
		//add a defined quantity of colours to the pool 
		tabColorPool.addAll(tabColor.subList(0, howManyColors.getIntValue()));
	}
	
	/**
	 * Creates the question to summit to the user.
	 * 
	 * @see DefenderMastermind#tabColorPool
	 */
	private void generateQuestion() {
		int i = 0;
		String str =""; 
		// add to a String of the colors of the ColorPool 
		for (String couleur : tabColorPool) {
			str += "["+ i +"]" + couleur +" ";
			i++;
		}
		
		System.out.println(TextEnhencer.ANSI_YELLOW + "\n" + str);
		System.out.println("\nPlease compose your secret color combination by choosing " + combiLength + " colors amongst the colors listed." + TextEnhencer.ANSI_RESET);
	}
	
	
	/**
	 *  Request the user to make a keyboard entry.
	 *  
	 *  also verify if the entry is correct.
	 *  
	 *  @see DefenderMastermind#userAnswer
	 */
	private void requestUserSecretCombi() {
		
		//creates new HashMap to store user combination choice 
		userSelection = new HashMap<>(); 
		String str = ""; 
		do {
			//Using Regex to make sure user enter the right value type (Integer), as well as,  the correct length of this value type, and not higher range of selection offers.   
			if(userAnswer !="") {
				if(!userAnswer.matches("^[./[0-9]]+$")) { 
					System.out.println(TextEnhencer.ANSI_RED + "Please enter a number instead of a characters." + TextEnhencer.ANSI_RESET);	
					log.warn("User entry mismatch the type required");
				}else if (userAnswer.length() > combiLength || userAnswer.length() < combiLength){	
					System.out.println((userAnswer.length() > combiLength )? TextEnhencer.ANSI_RED + "The number of digits is superior to the number of digits required" + TextEnhencer.ANSI_RESET
							: TextEnhencer.ANSI_RED + "The number of digits is inferior to the number of digits required" + TextEnhencer.ANSI_RESET);
					log.warn("User entry mismatch the length of digits required");
				}else if (!userAnswer.matches("^[./[0-"+(tabColorPool.size() -1) +"]]+$")) { 
					System.out.printf(TextEnhencer.ANSI_RED + "Your selection has to be composed of number bewteen [0] and [%d]\n" + TextEnhencer.ANSI_RESET, (tabColorPool.size()-1));
					log.warn("User entry mismatch the possible answer choices");
				}
			}
			System.out.print(TextEnhencer.ANSI_YELLOW);
			// store the user answer in a string
			this.userAnswer = clavier.nextLine();
			System.out.print(TextEnhencer.ANSI_RESET);
		} while ( userAnswer.length() > combiLength || userAnswer.length() < combiLength|| !userAnswer.matches("^[./[0-"+ (tabColorPool.size()-1)+"]]+$") || !userAnswer.matches("^[./[0-9]]+$") );
	
		// Converts String userAnswer digits into colours, store it in the HashMap and in a String to display the choice  
		for (int i = 0; i < userAnswer.length(); i++) {
			userSelection.put(i, tabColorPool.get(Character.getNumericValue(userAnswer.charAt(i))));
			
			str += "|" + userSelection.get(i) + "|";
		}
		System.out.println(TextEnhencer.ANSI_YELLOW + "You have selected the folwing secret color combination : \n     *** " + str + " ***\n" + TextEnhencer.ANSI_RESET);
	}
	
	/**
	 * Initiate the game. 
	 * 
	 * Increases the number of tries of the computer.
	 * 
	 * Get the computer answer. 
	 * 
	 * Compare the computer answer. 
	 * 
	 * All those steps are repeated as long as the secret combination is not found or if the value of error allowed isn't reached by score value. 
	 * 
	 * If computer can't find the secret combination, user Wins. Otherwise, user looses.     
	 * 
	 * @see DefenderMastermind#tries
	 * @see DefenderMastermind#generateComputerAnswer()
	 * @see DefenderMastermind#compareAnswer()
	 * @see ChallengerMastermind#errorAllowed
	 * @see IGame#displayFish()
	 */
	private void initGame() {
		
		int tries = 0; 
		// repeat as long as the combination isn't found or the number or error allowed is reached 
		do {		
			// add a try after each question 
			tries++;
			
			generateComputerAnswer();
			compareAnswer();
			
		} while (correctPosition != this.combiLength && tries < this.errorAllowed);
		
		// if the answer isn't found user looses 
		if(correctPosition == this.combiLength) {
			System.out.printf(TextEnhencer.ANSI_RED + "\n\t   .+*°*+.+> | GAME OVER !!! | <+..+*°*+."+ TextEnhencer.ANSI_CYAN + "\nComputer found your secret color combination after %d " + ((tries < 2)? "trial." : "trials.") + "\n" + TextEnhencer.ANSI_RESET, tries);
		// if the answer is found user wins	
		}else {
			Fish.displayFish();
			System.out.printf(TextEnhencer.ANSI_YELLOW + "\n\t     .+*°*+.+> | You Win !!! | <+..+*°*+.\nComputer could not find your secret color combination after %d " + ((tries < 2)? "trial." : "trials.") + "\n" + TextEnhencer.ANSI_RESET, tries);
		}
		
	}
	
	/**
	 * Finds the correct way to handle the result and generate new answer based on the previous. 
	 * 
	 * @see DefenderMastermind#tabComputerAnswer
	 * @see DefenderMastermind#tabColorToReinject
	 */
	private void generateComputerAnswer() {
		
		Random random = new Random(); 
		int index = 0; 
		//if is empty (means first try) then generate a random combination  
		if(tabComputerAnswer.isEmpty()) {	
			for (int i = 0; i <userSelection.size(); i++) {
				index = random.nextInt(tabColorPool.size());
				tabComputerAnswer.put(i, tabColorPool.get(index));
			}
			
		// After the first try 	
		}else {
			for (int i = 0; i < tabComputerAnswer.size(); i++) {
				
				// if value of the element in tabComputerAnswer is "found" 
				if(tabComputerAnswer.get(i).equals("Found2")) {
					//replace "found" by the value of user selection 
					tabComputerAnswer.replace(i,userSelection.get(i));
					//remove the value from tabColorToReinject if the color is found 
					if(tabColorToReinject.contains(userSelection.get(i))) {		// ici 
						if(!tabColorToReinject.isEmpty()) {
							tabColorToReinject.remove(userSelection.get(i));
							if (dev)
								//indicates what colour has been removed from the pool of reinjected colours as it is present in the combination  (only in -dev mode)  
								System.out.println(TextEnhencer.ANSI_RED + userSelection.get(i) + " has been found and therefore removed from tabtoReinject\n" + TextEnhencer.ANSI_RESET);
						}
					}
					
				// if value of element in tabComputerAnswer exist but not at the right place 	
				}else if(tabComputerAnswer.get(i).equals("Found4") && tabColorToReinject.size() > 0) {
					//select a random element amongst those that exist but not at the right place 
					int index2 = random.nextInt(tabColorToReinject.size()); // ICI !!!! 
					// put them in the next ComputerAnswer 
					tabComputerAnswer.replace(i,(tabColorToReinject.get(index2)));
					if(dev)
						//indicates what color from the pool of reinjected colours has been added to the new trial (only in -dev mode)  
						System.out.println(TextEnhencer.ANSI_RED + tabColorToReinject.get(index2) + " has been added to the combination" + TextEnhencer.ANSI_RESET);
					
				//if value does not exist replace by a random value 	
				}else {
						int index3 = random.nextInt(tabColorPool.size());  
						tabComputerAnswer.replace(i, tabColorPool.get(index3));
				}
			}
		}
	}
	
	/**
	 * Compare computer answer with the users secret colours combination. 
	 * 
	 * Indicates how many colours are well placed and how many are present but not well placed in the combination.
	 * 
	 * @see DefenderMastermind#tabComputerAnswer
	 * @see DefenderMastermind#userSelection
	 * @see DefenderMastermind#tabToCompare
	 */
	private void compareAnswer() {
		String str = ""; 
		
		// add values to a String for display 
		for (int i = 0; i < tabComputerAnswer.size(); i++) {
			str += "|" + tabComputerAnswer.get(i) + "|";
		}
		
		correctPosition = 0; 
		int exist = 0;
		tabToCompare.putAll(userSelection);
				
		// check if any colour is at the same position in tabUserAnswer and tabToCompare 
		for (int i = 0; i < tabComputerAnswer.size(); i++) {
			//if element at the correct position 
			if(tabComputerAnswer.get(i).equals(tabToCompare.get(i))){
				//increase correctPosition 
				correctPosition++;
				// and replace the value in both tabs 
				tabToCompare.replace(i, "Found");
				tabComputerAnswer.replace(i, "Found2"); 
			}
		}
	
		// amongst the remaining values check if there is any match 
		for (int i = 0; i < tabComputerAnswer.size(); i++) {
			for (int j = 0; j < tabComputerAnswer.size(); j++) {
				// if tabUserAnswer contains at any position an element contained in tabCompare
				if(tabComputerAnswer.get(i).equals(tabToCompare.get(j))) {
					// replace the value in both tabs
					if(!tabColorToReinject.contains(tabComputerAnswer.get(i)))
						tabColorToReinject.add(tabComputerAnswer.get(i));
					tabToCompare.replace(j, "Found3");
					tabComputerAnswer.replace(i, "Found4");
					//increase exist 
					exist++;
				}
			}	
		}
		System.out.printf(TextEnhencer.ANSI_CYAN + "\nComputer tries %s ---> %d well placed, %d exist but not well placed.\n" + TextEnhencer.ANSI_RESET,str, correctPosition, exist);
		
		if(dev)
			// indicates the pool of colours to be reinjected for next trial  
			System.out.println(TextEnhencer.ANSI_RED + "\n" + tabColorToReinject + "Colors to be reinjected for a new combination\n" + TextEnhencer.ANSI_RESET);
	
	}
}

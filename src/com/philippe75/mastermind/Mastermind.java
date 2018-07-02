package com.philippe75.mastermind;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.log4j.Logger;

import com.philippe75.extra.TextEnhencer;
import com.philippe75.game.Game;
import com.philippe75.generators.SecretColorCombinationGenerator;

/**
 * <b>This abstract class contains all method common to all Mastermind game modes.</b> 
 * 
 *<p>Challenger mode.</p>
 *<p>Defender mode.</p> 
 *<p>Duel mode.</p> 
 * 
 * @author PPlaxine
 * @version 1.0
 */
public abstract class Mastermind extends Game{
	
	/**
	 * Secret colour combination generator. 
	 * 
	 * For more information about the generator, please see SecretColorCombinationGenerator class doc.  
	 * 
	 * @see SecretColorCombinationGenerator
	 * @see ChallengerMastermind#startTheGame()
	 * @see DuelMastermind#startTheGame()
	 */
	protected SecretColorCombinationGenerator sCG;
	
	/**
	 * Creates a logger to generate log of the class.	
	 */
	private static final Logger log = Logger.getLogger(Mastermind.class);
	
	//--------------------------------------------------------------
	
	
	//Challenger, Duel ATT---------------

	/**
	 * Store combination generated by the SecretColorCombinationGenerator
	 * 
	 * @see SecretColorCombinationGenerator
	 * @see Mastermind#compareAnswer()
	 * @see ChallengerMastermind#initGame()
	 * @see DuelMastermind#initGame()
	 */
	protected Map<Integer, String> tabColCombi;
	
	/**
	 * Store the number of colours at the correct position. 
	 * 
	 * @see ChallengerMastermind#compareAnswer()
	 * @see ChallengerMastermind#initGame()
	 * @see DuelMastermind#initGame() 
	 */
	protected int correctPositionUser;
	
	/**
	 * Pool of colours used for the game. 
	 * 
	 * @see Mastermind#compareAnswer()
	 * @see Mastermind#printQuestion()
	 */
	private Map<Integer, String> colorPool = new HashMap<>();
	
	/**
	 * User answer in String format with numerical value. 
	 * 
	 * @see Mastermind#getUserAnswer()
	 * @see Mastermind#compareAnswer()
	 * @see ChallengerMastermind#initGame()
	 * @see DuelMastermind#initGame()
	 */
	private String userAnswer=""; 
	
	
	//Defender, Duel ATT-----------------

	/**
	 * Store the number of colours at the correct position. 
	 * 
	 *  @see Mastermind#compareAnswer()
	 *  @see DefenderMastermind#initGame()
	 *  @see DuelMastermind#initGame()
	 */
	protected int correctPositionComputer; 
	
	/**
	 * Pool of colours of the size defined by HowManyColors, for the player to choose from. 
	 * 
	 * @see Mastermind#initiateColorChoice()
	 * @see Mastermind#generateQuestion()
	 * @see Mastermind#requestUserSecretCombi()
	 * @see Mastermind#generateComputerAnswer()
	 */
	private List<String> tabColorPool;
	
	/**
	 * Stores user combination with colours as values in String format. 
	 * 
	 * Also used to compare and generates computer answer.   
	 * 
	 * @see Mastermind#requestUserSecretCombi()
	 * @see Mastermind#compareAnswer()
	 * @see Mastermind#generateComputerAnswer()
	 */
	private Map<Integer, String> userSelection; 
	
	/**
	 * Stores the computer answer in Map format for comparison. 
	 * 
	 * @see Mastermind#compareAnswer()
	 * @see Mastermind#generateComputerAnswer()
	 */
	private Map<Integer, String> tabComputerAnswer = new HashMap<>();
	
	/**
	 * Store the colours present but not well placed, to be re-injected in the next computer answer. 
	 * 
	 * @see Mastermind#generateComputerAnswer()
	 * @see Mastermind#compareAnswer()
	 */
	private List<String> tabColorToReinject = new ArrayList<>();
	
	/**
	 * Users secret combination in String format.
	 * 
	 * @see Mastermind#requestUserSecretCombi()
	 * @see Mastermind#compareComputerAnswer()
	 */
	private String UsersSecretCombi;
	
	
	//Challenger, Duel method--------------------------
	
	/**
	 * Displays the secret combination if mode dev is activated
	 *  
	 * @see Game#dev
	 * @see ChallengerMastermind#startTheGame()
	 * @see DuelMastermind#startTheGame()
	 */
	protected void displaySecretColorCombi() {
		System.out.println(TextEnhencer.ANSI_CYAN+ "\nComputer has generated a secret combination for you to guess ..." + TextEnhencer.ANSI_RESET);
		if(dev) {
			log.info("Game is running in developer mode");
			System.out.printf(TextEnhencer.ANSI_GREEN + "*** Secret Color Combination : %s ***\n\n" + TextEnhencer.ANSI_RESET, sCG.toString());
		}
	}
	
	/**
	 * Prints the Question with the random generated colours to choose from. 
	 * 
	 * @see Mastermind#sCG
	 * @see Mastermind#colorPool
	 * @see ChallengerMastermind#initGame()
	 * @see DuelMastermind#initGame()
	 */
	void printQuestion() {	
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
	 * Request the user to make a keyboard entry.
	 *  
	 * also verify if the entry complies with the requirements.
	 *  
	 * @see Mastermind#userAnswer
	 * @see ChallengerMastermind#initGame()
	 * @see DuelMastermind#initGame()
	 */
	protected void getUserAnswer() {
		this.userAnswer = "";
	
		
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
	 * @see Mastermind#tabUserAnswer
	 * @see Mastermind#tabToCompare
	 * @see ChallengerMastermind#initGame()
	 * @see DuelMastermind#initGame()
	 */
	protected void compareUserAnswer() {
		
		Map<Integer, String> tabUserAnswer = new HashMap<>();
		Map<Integer, String> tabToCompareUser = new HashMap<>(); 
			
			String str=""; 
			int index = 0;
			correctPositionUser = 0; 
			int exist = 0;
			tabToCompareUser.putAll(tabColCombi);
					
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
			if(tabUserAnswer.get(i).equals(tabToCompareUser.get(i))){
				//increase correctPosition 
				correctPositionUser++;
				// and replace the value in both tabs 
				tabToCompareUser.replace(i, "Found");
				tabUserAnswer.replace(i, "Found2"); 
			}
		}
		
		// amongst the remaining values check if there is any match 
		for (int i = 0; i < tabUserAnswer.size(); i++) {
			for (int j = 0; j < tabUserAnswer.size(); j++) {
				// if tabUserAnswer contains at any position an element contained in tabCompare
				if(tabUserAnswer.get(i).equals(tabToCompareUser.get(j))) {
					// replace the value in both tabs 
					tabToCompareUser.replace(j, "Found3");
					tabUserAnswer.replace(i, "Found4");
					//increase exist 
					exist++;
				}
			}	
		}
		userAnswer = ""; 
		System.out.printf(TextEnhencer.ANSI_GREEN + "\nYour anwser is %s ---> %d well placed, %d exist but not well placed.\n\n" + TextEnhencer.ANSI_RESET,str, correctPositionUser, exist);
	}

	
	//Defender, duel method----------------------------
	
	/**
	 * Create a random pool of colour.
	 * 
	 *  @see Mastermind#tabColorPool
	 *  @see Mastermind#tabColor
	 *  @see Game#howManyColors
	 *  @see DefenderMastermind#initGame()
	 *  @see DuelMastermind#initGame()
	 */
	protected void initiateColorChoice() {
		tabColorPool = new ArrayList<String>();

		
		List<String> tabColor = new ArrayList<String>();
		
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
		tabColorPool.addAll(tabColor.subList(0, super.howManyColors.getIntValue()));
	}

	/**
	 * Creates the question to summit to the user.
	 * 
	 * @see Mastermind#tabColorPool
	 * @see DefenderMastermind#initGame()
	 * @see DuelMastermind#initGame()
	 */
	protected void generateQuestion() {
		int i = 0;
		String str =""; 
		// add to a String all the colours contained in the ColorPool 
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
	 *  also verify if the entry complies with the requirements.
	 *  
	 *  @see Mastermind#userAnswer
	 *  @see DefenderMastermind#initGame()
	 *  @see DuelMastermind#initGame()
	 */
	protected void requestUserSecretCombi() {
		this.userAnswer= "";
		//creates new HashMap to store user combination choice 
		userSelection = new HashMap<>(); 
		UsersSecretCombi = ""; 
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
			
			UsersSecretCombi += "|" + userSelection.get(i) + "|";
		}
		System.out.println(TextEnhencer.ANSI_YELLOW + "\nYou have selected the folwing secret color combination : \n     *** " + UsersSecretCombi + " ***\n" + TextEnhencer.ANSI_RESET);
	}
	
	/**
	 * Finds the correct way to handle the result and generate new answer based on the previous. 
	 * 
	 * @see Mastermind#tabComputerAnswer
	 * @see Mastermind#tabColorToReinject
	 * @see DefenderMastermind#initGame()
	 * @see DuelMastermind#initGame()
	 */
	protected void generateComputerAnswer() {
		
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
					//remove the value from tabColorToReinject if the colour is found 
					if(tabColorToReinject.contains(userSelection.get(i))) {	
						if(!tabColorToReinject.isEmpty()) {
							tabColorToReinject.remove(userSelection.get(i));
							if (dev)
								//indicates what colour has been removed from the pool of re-injected colours as it is present in the combination  (only in -dev mode)  
								System.out.println(TextEnhencer.ANSI_RED + userSelection.get(i) 
								+ " colour has been found and therefore is removed from tabtoReinject (-dev mode)" + TextEnhencer.ANSI_RESET);
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
						System.out.println(TextEnhencer.ANSI_RED + tabColorToReinject.get(index2) + " colour is added to the combination (-dev mode)" + TextEnhencer.ANSI_RESET);
					
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
	 * @see Mastermind#tabComputerAnswer
	 * @see Mastermind#userSelection
	 * @see Mastermind#tabToCompare
	 * @see DefenderMastermind#initGame()
	 * @see DuelMastermind#initGame()
	 */
	protected void compareComputerAnswer() {

		Map<Integer, String> tabToCompareComputer = new HashMap<>();	
		String str = ""; 
		
		// add values to a String for display 
		for (int i = 0; i < tabComputerAnswer.size(); i++) {
			str += "|" + tabComputerAnswer.get(i) + "|";
		}
		
		correctPositionComputer = 0; 
		int exist = 0;
		tabToCompareComputer.putAll(userSelection);
				
		// check if any colour is at the same position in tabUserAnswer and tabToCompare 
		for (int i = 0; i < tabComputerAnswer.size(); i++) {
			//if element at the correct position 
			if(tabComputerAnswer.get(i).equals(tabToCompareComputer.get(i))){
				//increase correctPosition 
				correctPositionComputer++;
				// and replace the value in both tabs 
				tabToCompareComputer.replace(i, "Found");
				tabComputerAnswer.replace(i, "Found2"); 
			}
		}
	
		// amongst the remaining values check if there is any match 
		for (int i = 0; i < tabComputerAnswer.size(); i++) {
			for (int j = 0; j < tabComputerAnswer.size(); j++) {
				// if tabUserAnswer contains at any position an element contained in tabCompare
				if(tabComputerAnswer.get(i).equals(tabToCompareComputer.get(j))) {
					// replace the value in both tabs
					if(!tabColorToReinject.contains(tabComputerAnswer.get(i)))
						tabColorToReinject.add(tabComputerAnswer.get(i));
					tabToCompareComputer.replace(j, "Found3");
					tabComputerAnswer.replace(i, "Found4");
					//increase exist 
					exist++;
				}
			}	
		}
		System.out.print(TextEnhencer.ANSI_CYAN + "Computer has to find the following combination " + UsersSecretCombi );
		System.out.printf(TextEnhencer.ANSI_CYAN + "\nComputer tries %s ---> %d well placed, %d exist but not well placed.\n" + TextEnhencer.ANSI_RESET,str, correctPositionComputer, exist);
		
		if(dev)
			// indicates the pool of colours to be re-injected for next trial  
			System.out.println(TextEnhencer.ANSI_RED + tabColorToReinject + "Colors to be reinjected for a new combination (-dev mode)\n" + TextEnhencer.ANSI_RESET);
		System.out.println("");
	}
}

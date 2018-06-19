package com.philippe75.game;


import java.util.Scanner;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import com.philippe75.mastermind.ChallengerMastermind;
import com.philippe75.mastermind.DefenderMastermind;
import com.philippe75.mastermind.DuelMastermind;
import com.philippe75.mastermind.Mastermind;
import com.philippe75.plus_minus.ChallengerPlusMinus;
import com.philippe75.plus_minus.DefenderPlusMinus;
import com.philippe75.plus_minus.DuelPlusMinus;
import com.philippe75.plus_minus.PlusMinus;
/**
 * <b>Class containing the main method</b>
 * 
 * <p> 
 * Runs the menu, where the user can choose the following :
 * <ul>
 * <li>
 * At the beginning of the game : 
 * <ul>
 * <li>The game to play</li>
 * <li>The mode of the game</li>
 * <li>To quit the game</li>
 * </ul>
 * </li>
 * <li>
 * Once the game is over :
 * <ul>
 * <li>To play again</li>
 * <li>To return to the main menu</li>
 * <li>To quit the game</li>
 * </ul>
 * </li>
 * </ul> 
 * </p>
 * 
 * @author PPlaxine
 * @version 1.0
 */
public class Main {
	
	/**
	 * If true is returned, Same game starts again. 
	 * 
	 * Is modified in the selection menu once the game is over. 
	 * 
	 * @see Main#runMenu()
	 * @see Main#afterGameChoice()
	 */
	private static boolean playagain = false;
	
	/**
	 * returns true if "-dev" is passed as parameter at program launch.
	 * 
	 * Will starts the selected games in developer mode. 
	 * 
	 * @see Main#main(String[])
	 * @see ChallengerPlusMinus
	 * @see DefenderPlusMinus
	 * @see DuelPlusMinus
	 * @see ChallengerMastermind
	 * @see DefenderMastermind
	 * @see DuelMastermind 
	 */
	private static boolean dev; 
	
	
	private static final Logger log = Logger.getLogger(Main.class);
	/**
	 * Method main.
	 * 
	 * @param args  
	 * 				starts selected game in developer mode if "-dev" is passed. 
	 * @see Main#dev
	 * @see ChallengerPlusMinus
	 * @see DefenderPlusMinus
	 * @see DuelPlusMinus
	 * @see ChallengerMastermind
	 * @see DefenderMastermind
	 * @see DuelMastermind 
	 */
	public static void main(String[] args) {
		
		log.info("Program started successfully");
		
		dev = (args.length > 0 && args[0].equals("-dev"))? true : false;
		runMenu();
	}
	
	/**
	 * Runs the game selection menu.
	 * 
	 * Displays the welcome message.  
	 * 
	 * Displays the Game choice menu.
	 * 
	 * After game selection, displays the mode choice menu.
	 * 
	 * Starts game accordingly.
	 * 
	 * Once the game is over displays a after game choice menu. 
	 * 
	 * @see Main#printHeader()
	 * @see Main#printMainMenu()
	 * @see Main#printModeMenu()
	 * @see Game#startGame(GameMode)
	 * @see GameMode
	 * @see Main#afterGameChoice()
	 */
	public static void runMenu() {
		
		int userGameChoice = -1;
		Game cPM = new PlusMinus();
		Game cMM = new Mastermind();
	
		boolean arret = false; 
		printHeader();
		
		while (!arret) {
			log.info("Game and Game modes menu runs");
			printMainMenu();
			userGameChoice = getUserAnswer(3);
			switch (userGameChoice) {
			case 1:
			case 2:{	printModeMenu();	
						switch (getUserAnswer(4)) {
						case 1: if (userGameChoice == 1){
							do {
								cPM.startGame(GameMode.CHALLENGER);
								afterGameChoice();
							}while(playagain);
							
						break; 
						}else {
							do {
								cMM.startGame(GameMode.CHALLENGER);
								afterGameChoice();
							}while(playagain);
						}
								
						break;
						case 2: if (userGameChoice == 1){
							do {
								cPM.startGame(GameMode.DEFENDER);
								afterGameChoice();
							}while(playagain);
						}else {
							do {
								cMM.startGame(GameMode.DEFENDER);
								afterGameChoice();
							}while(playagain);
						}
							
						break;
						case 3: if (userGameChoice == 1){
							do {
								cPM.startGame(GameMode.DUEL);
								afterGameChoice();
							}while(playagain);
						}else {
							do {
								cMM.startGame(GameMode.DUEL);
								afterGameChoice();
							}while(playagain);
						}
							
						break;
						case 4: 
							
						break;
							}
			}
				break; 
			case 3: 
				log.info("Program ended sucessfully");
				System.exit(0);
				break;
			}
		}
	}  

   	/**
   	 * Displays the welcome message.
   	 * 
   	 * @see Main#runMenu()
   	 */
 	private static void printHeader() {
 		String 	str = "\n\n************************************************\n";  
 				str += "***************	    Welcome  to   **************\n";
 				str += "***************	    the GAME !!!  **************\n";
 				str += "***************	     	     	  **************\n";
 				str += "************************************************";
		System.out.println(TextEnhencer.ANSI_GREEN + str + TextEnhencer.ANSI_RESET);
 	}
	
 	/**
 	 * Displays the game choice menu.
 	 * 
 	 * @see Main#runMenu()
 	 */
	private static void printMainMenu() {
		String 	menu = TextEnhencer.ANSI_PURPLE; 
				menu += "\nSelect your game : \n"; 
				menu +=	"\tPlusMoins : ......enter [1]\n";
				menu += "\tMastermind : .....enter [2]\n";
				menu += "\tQuit the game : ..enter [3]\n";
				menu += TextEnhencer.ANSI_RESET;
			
		System.out.println(menu);	
	}
	
	/**
	 * Displays the game mode choice menu.
	 * 
	 * @see Main#runMenu()
	 */
	private static void printModeMenu(){
	   	String 	mode =TextEnhencer.ANSI_PURPLE;  
	   			mode += "Select your mode : \n"; 
	   			mode +=	"\tChallenger : .................enter [1]\n";
	   			mode +=	"\tDefenseur : ..................enter [2]\n";
	   			mode +=	"\tDuel : .......................enter [3]\n";
	   			mode +=	"\tBack to previous screen : ....enter [4]\n";
	   			mode += TextEnhencer.ANSI_RESET;
		System.out.println(mode);
	}
	
	/**
	 * Check if the entry of user is correct.
	 * 
	 * If the entry is higher than the number of questions, or inferior to null, or not a number, user must make a new entry.
	 * 
	 * @param numberofQuestion 
	 * 							number of question in the menu.  
	 * @return the user entry in integer format
	 */
	private static int getUserAnswer(int numberofQuestion) {
		int userAnswer = -1; 
		Scanner clavier = new Scanner(System.in); 
		while (userAnswer > numberofQuestion || userAnswer < 0 ) {
			try {
				System.out.print(TextEnhencer.ANSI_PURPLE + "Enter your choice here below : \n" + TextEnhencer.ANSI_RESET);
				System.out.print(TextEnhencer.ANSI_YELLOW);
				userAnswer = Integer.parseInt(clavier.nextLine()); 
				System.out.print(TextEnhencer.ANSI_RESET);
			} catch (NumberFormatException e) {
				System.out.print(TextEnhencer.ANSI_RED + "Incorrect value. " + TextEnhencer.ANSI_RESET);
				log.error("Users entry mismatch the required entry type");
			}
		}
		return userAnswer;
	}
	
	/**
	 * After game menu. 
	 * 
	 * Once the game is over, user can choose: to play again, to return to the main menu, or quit the game. 
	 * 
	 * @see Main#playagain
	 * @see Main#runMenu()
	 */
	private static void afterGameChoice() {
		playagain = false; 
		
		String 	menu = TextEnhencer.ANSI_GREEN; 
				menu += "\n_____________________________________________________________________\n";
				menu += "_____________________________________________________________________\n";
				menu += "_____________________________________________________________________\n";
				menu += "_____________________________________________________________________\n\n";
				menu +=  "\nWhat would you like to do now ? : \n"; 
				menu +=	"\tPlay again ? : ..................enter [1]\n";
				menu += "\tReturn to the main menu ? : .....enter [2]\n";
				menu += "\tQuit the game : .................enter [3]\n";
				menu += TextEnhencer.ANSI_RESET;
		System.out.println(menu);	
		int userDecision = getUserAnswer(3);
		
		if(userDecision == 1) {
			playagain = true; 
		}else if(userDecision == 2) {
		
		}else {
			log.info("Program ended successfully");
			System.exit(0);	
		}
	}

	/**
	 * Getter 
	 * 
	 * Returns true if the program is launch with -dev as parameter.  
	 * 
	 * @return the value of dev. 
	 * @see Main#dev
	 */
	public static boolean isDev() {
		return dev;
	}
}	
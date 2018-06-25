package com.philippe75.menu;

import java.util.Scanner;

import org.apache.log4j.Logger;

import com.philippe75.game.Game;
import com.philippe75.game.Main;
import com.philippe75.game.TextEnhencer;

public class Menu {
	
	/**
	 * If true is returned, Same game starts again. 
	 * 
	 * Is modified in the selection menu once the game is over. 
	 * 
	 * @see Main#runMenu()
	 * @see Main#afterGameChoice()
	 */
	private boolean playagain = false;
	
	/**
	 * Creates a logger to generate log of the class.
	 * 
	 * @see Main#main(String[])
	 * @see Main#runMenu()
	 * @see Main#getUserAnswer(int)
	 * @see Main#afterGameChoice()
	 */
	private final Logger log = Logger.getLogger(Menu.class);

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
	public Menusettings runMenu() {
		
		Menusettings settings = new Menusettings();
		printHeader();
		chooseGame(settings);
		
		log.info("Game and Game modes menu runs");

		return settings;
	} 
	
	private void chooseGame(Menusettings settings) {
		int userGameChoice = -1;
		printMainMenu();
		userGameChoice = getUserAnswer(3);
			
		switch (userGameChoice) {
		case 1:
			settings.setGameType(GameType.PLUSMINUS);
			chooseMode(settings);
			break;
		case 2:
			settings.setGameType(GameType.MASTERMIND);
			chooseMode(settings);
			break;
		case 3: 
			log.info("Program ended sucessfully");
			System.exit(0);
			break;
		}
	}
	
	private void chooseMode(Menusettings menusettings) {
		
		printModeMenu();	
		switch (getUserAnswer(4)) {
		case 1: 
			menusettings.setGameMode(GameMode.CHALLENGER);
			break;
		case 2: 
			menusettings.setGameMode(GameMode.DEFENDER);
			break;
		case 3: 
			menusettings.setGameMode(GameMode.DUEL);
			break;
		}	
	}

   	/**
   	 * Displays the welcome message.
   	 * 
   	 * @see Main#runMenu()
   	 */
 	private void printHeader() {
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
	private void printMainMenu() {
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
	private void printModeMenu(){
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
	private int getUserAnswer(int numberofQuestion) {
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
	public Boolean afterGameChoice() {
		
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
			return true;
		}else if(userDecision == 2) {
			return false;
		}else {
			log.info("Program ended successfully");
			System.exit(0);	
		}
		return true;
	}
}

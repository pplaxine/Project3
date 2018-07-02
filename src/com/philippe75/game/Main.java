package com.philippe75.game;


import org.apache.log4j.Logger;

import com.philippe75.extra.TextEnhencer;
import com.philippe75.mastermind.ChallengerMastermind;
import com.philippe75.mastermind.DefenderMastermind;
import com.philippe75.mastermind.DuelMastermind;
import com.philippe75.menu.Menu;
import com.philippe75.menu.Menusettings;
import com.philippe75.plus_minus.ChallengerPlusMinus;
import com.philippe75.plus_minus.DefenderPlusMinus;
import com.philippe75.plus_minus.DuelPlusMinus;
/**
 * <b>Class containing the main method.</b>
 * 
 * <p> 
 * Runs the menu, where the user can choose the game and the mode.
 * </p>
 * 
 * @author PPlaxine
 * @version 1.0
 */
public class Main {
	
	/**
	 * How many time the game had been launched. 
	 * 
	 * @see Main#main(String[])
	 * @see Main#PARENTAL_CONTROL
	 */
	private static int gameRuns;
	
	/**
	 * How many time the game can be played before a warning is given.
	 * 
	 * @see Main#main(String[])
	 * @see Main#gameRuns
	 */
	private static final int PARENTAL_CONTROL = 3;
	
	/**
	 * returns true if "-dev" is passed as parameter at program launch.
	 * 
	 * Will starts the selected games in developer mode. 
	 * 
	 * @see Main#main(String[])
 	 * @see Game#dev
	 */
	private static boolean dev; 
	
	/**
	 * Creates a logger to generate log of the class.
	 */
	private static final Logger log = Logger.getLogger(Main.class);
	
	/**
	 * Method main.
	 * 
	 * Starts the menu.
	 * 
	 * @param args  
	 * 				starts selected game in developer mode if "-dev" is passed. 
	 */
	public static void main(String[] args) {
		
		log.info("Program started successfully");
		dev = (args.length > 0 && args[0].equals("-dev"))? true : false;
		 
		do {
			Menu menu = new Menu();
			Menusettings settings = menu.runMenu();
			ModeFactory factory = new ModeFactory();
			
			do {
				IGame mode = factory.createMode(settings);
				gameRuns++;
			}while(menu.afterGameChoice() && gameRuns < PARENTAL_CONTROL);	
			
		}while (true && gameRuns < PARENTAL_CONTROL);
		
		System.out.println(TextEnhencer.ANSI_CYAN + "Enougth video Game for today ... You should go back to your studies Mate !" + TextEnhencer.ANSI_RESET);
		log.info("Program started successfully");
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
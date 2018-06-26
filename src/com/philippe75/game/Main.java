package com.philippe75.game;


import org.apache.log4j.Logger;

import com.philippe75.mastermind.ChallengerMastermind;
import com.philippe75.mastermind.DefenderMastermind;
import com.philippe75.mastermind.DuelMastermind;
import com.philippe75.menu.Menu;
import com.philippe75.menu.Menusettings;
import com.philippe75.plus_minus.ChallengerPlusMinus;
import com.philippe75.plus_minus.DefenderPlusMinus;
import com.philippe75.plus_minus.DuelPlusMinus;
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
	
	private static int gameRuns;
	private static final int PARENTAL_CONTROL = 3;
	
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
	
	/**
	 * Creates a logger to generate log of the class.
	 * 
	 * @see Main#main(String[])
	 * @see Main#runMenu()
	 * @see Main#getUserAnswer(int)
	 * @see Main#afterGameChoice()
	 */
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
package com.philippe75.game;

import java.util.Scanner;

import org.apache.log4j.Logger;

import com.philippe75.extra.Dino;
import com.philippe75.extra.EndOfGameDisplay;
import com.philippe75.extra.Fish;
import com.philippe75.generators.HowManyColors;
import com.philippe75.generators.SecretColorCombinationGenerator;
import com.philippe75.mastermind.ChallengerMastermind;
import com.philippe75.mastermind.DefenderMastermind;
import com.philippe75.mastermind.DuelMastermind;
import com.philippe75.mastermind.Mastermind;
import com.philippe75.plus_minus.ChallengerPlusMinus;
import com.philippe75.plus_minus.DuelPlusMinus;
import com.philippe75.plus_minus.PlusMinus;

/**
 * <b>This abstract class contains all method common to both games.</b> 
 * 
 *<p>PlusMinus Game.</p>
 *<p>Mastermind Game.</p> 
 * 
 * @author PPlaxine
 * @version 1.0
 */
public abstract class Game implements IGame{
	
	
	/**
	 * Used as part of Design Strategy pattern to display pics at the end of the game. 
	 * 
	 * @see Game#displayEndGamePic()
	 */
	// Pattern Strategy design test 
	protected EndOfGameDisplay endOfGameDisplay;

	/**
	 * errors allowed in the game.
	 * 
	 * Defines the number of tries the user has.
	 * It can be modified via the dataConfig.properties file 
	 *  
	 * @see Game#setProperties()
	 * @see ChallengerPlusMinus#initGame()
	 * @see DefenderPlusMinus#initGame()
	 * @see DuelPlusMinus#initGame()
	 * @see ChallengerMastermind#initGame()
	 * @see DefenderMastermind#initGame()
	 * @see DuelMastermind#initGame()
	 */
	protected int errorAllowed;
	
	/**
	 * combination length for the game. 
	 * 
	 * Defines the length of the secret combination to be generated.
	 * It can be modified via the dataConfig.properties file. 
	 * 
	 * @see Game#setProperties()
	 * @see ChallengerPlusMinus#initGame()
	 * @see DefenderPlusMinus#initGame()
	 * @see ChallengerMastermind#initGame()
	 * @see DefenderMastermind#initGame()
	 */
	protected int combiLength;
	
	protected Scanner clavier = new Scanner(System.in);
	
	/**
	 * Enumeration that define the size of the colour pool to request to SecretColorCombinationGenerator. 
	 * 
	 * @see SecretColorCombinationGenerator
	 * @see Game#setProperties()
	 * @see ChallengerMastermind#startTheGame()
	 * @see DefenderMastermind#startTheGame()
	 * @see DuelMastermind#startTheGame()
	 */
	protected HowManyColors howManyColors;
	
	/**
	 * Runs the game in developer mode if true is returned.
	 * 
	 * By default, if an argument -dev is passed when starting the program, the boolean will return the value true. 
	 * 
	 * Returns also true, if in dataProperties file the value of devMode is set to true.   
	 *  
	 * @see PlusMinus#displaySecretNum()
	 * @see Mastermind#displauSecretColorCombi
	 * @see Main#isDev()
	 * @see Main#dev
	 * @see Main#main(String[])
	 * @see Game#setProperties()
	 */
	protected boolean dev = Main.isDev();
	
	/**
	 * Number of tries made by the user, computer. takes +1 each turn.
	 * 
	 * @see ChallengerPlusMinus#initGame()
	 * @see DefenderPlusMinus#initGame()
	 * @see DuelPlusMinus#initGame()
	 * @see ChallengerMastermind#initGame()
	 * @see DefenderMastermind#initGame()
	 * @see DuelMastermind#initGame()
	 */
	protected int tries;
	
	/**
	 * Creates a logger to generate logs of the class.	
	 */
	private static final Logger log = Logger.getLogger(Game.class);
	
	/**
	 * Returns true if the properties are set.
	 * 
	 *  @see Game#combiLength
	 *  @see Game#errorAllowed
	 *  @see Game#howManyColors
	 *  @see Game#dev
	 */
	@Override
	public boolean setProperties() {
		
		this.combiLength = Integer.parseInt(PropertiesFile.getPropertiesFile("CombinationLength"));
		this.errorAllowed = Integer.parseInt(PropertiesFile.getPropertiesFile("errorAllowed"));
		howManyColors = HowManyColors.valueOf((PropertiesFile.getPropertiesFile("ColorPool")));
		if(new String("true").equals(PropertiesFile.getPropertiesFile("devMode"))) {
			this.dev = true;
		}	
		log.info("Properties set successfully");
		return true;
	}
	
	/**
	 * Calls display method of the type of display set. 
	 * 
	 * @see Fish#display()
	 * @see Dino#display()
	 * @see EndOfGameDisplay
	 */
	// Pattern Strategy design test ---------------------------------------------
	protected void displayEndGamePic() {
		endOfGameDisplay.display();
	}
	
	/**
	 * Sets the type of display dynamically in the game.  
	 * 
	 * @param endOfGameDisplay
	 */
	public void setEndOfGameDisplay(EndOfGameDisplay endOfGameDisplay) {
		this.endOfGameDisplay = endOfGameDisplay;
	}
	//---------------------------------------------------------------------------
	
}

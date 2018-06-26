package com.philippe75.game;

import java.util.Scanner;

import org.apache.log4j.Logger;

import com.philippe75.generators.SecretColorCombinationGenerator;
import com.philippe75.mastermind.ChallengerMastermind;
import com.philippe75.plus_minus.ChallengerPlusMinus;

/**
 * <b>This abstract class allows all it's child class to  access the startGame method.</b> 
 * 
 * <p>
 * The gameMode Enumeration passed into parameter is stored, hence reachable by all the child class.  
 * </p>
 * 
 * @author PPlaxine
 * @version 1.0
 */
public abstract class Game implements IGame{

	/**
	 * errors allowed in the game.
	 * 
	 * Defines the number of tries the user has.
	 * It can be modified via the dataConfig.properties file 
	 *  
	 * @see ChallengerPlusMinus#setProperties()
	 * @see ChallengerPlusMinus#initGame()
	 */
	protected int errorAllowed;
	
	/**
	 * combination length for the game. 
	 * 
	 * Defines the length of the secret combination to be generated
	 * It can be modified via the dataConfig.properties file 
	 * 
	 * @see ChallengerPlusMinus#setProperties()
	 * @see ChallengerPlusMinus#startTheGame()
	 */
	protected int combiLength;
	
	protected Scanner clavier = new Scanner(System.in);
	
	/**
	 * Enumeration that define the size of the colour pool to request to SecretColorCombinationGenerator. 
	 * 
	 * @see SecretColorCombinationGenerator
	 * @see ChallengerMastermind#setProperties()
	 * @see ChallengerMastermind#startTheGame()
	 */
	protected HowManyColors howManyColors;
	
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
	protected boolean dev = Main.isDev();
	
	/**
	 * Creates a logger to generate log of the class.	
	 */
	private static final Logger log = Logger.getLogger(Game.class);
	
	protected int tries;
	
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
		
		this.combiLength = Integer.parseInt(PropertiesFile.getPropertiesFile("CombinationLength"));
		this.errorAllowed = Integer.parseInt(PropertiesFile.getPropertiesFile("errorAllowed"));
		howManyColors = HowManyColors.valueOf((PropertiesFile.getPropertiesFile("ColorPool")));
		if(new String("true").equals(PropertiesFile.getPropertiesFile("devMode"))) {
			this.dev = true;
		}	
		log.info("Properties set successfully");
		return true;
	}
	
}

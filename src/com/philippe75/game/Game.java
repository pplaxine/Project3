package com.philippe75.game;

import com.oracle.tools.packager.Log;
import com.philippe75.mastermind.ChallengerMastermind;
import com.philippe75.mastermind.DefenderMastermind;
import com.philippe75.mastermind.DuelMastermind;
import com.philippe75.mastermind.Mastermind;
import com.philippe75.plus_minus.ChallengerPlusMinus;
import com.philippe75.plus_minus.DefenderPlusMinus;
import com.philippe75.plus_minus.DuelPlusMinus;
import com.philippe75.plus_minus.PlusMinus;

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
public abstract class Game {
	
	/**
	 * The mode required to play the game. 
	 * 
	 * @see GameMode
	 * @see Mastermind
	 * @see PlusMinus
	 */
	private GameMode gameMode;
	
	/**
	 * The ModeFactory will create an object of type Mode.
	 * 
	 *  @see ModeFactory
	 *  @see Mode
	 */
	protected ModeFactory modefactory;

	/**
	 * The startGame method of a the child class will be invoked when called in Main.   
	 * 
	 * @param gameMode
	 * 					The mode required to play the game. 
	 * @see ChallengerPlusMinus
	 * @see DefenderPlusMinus
	 * @see DuelPlusMinus
	 * @see ChallengerMastermind
	 * @see DefenderMastermind
	 * @see DuelMastermind
	 */
	public void startGame(GameMode gameMode) {
		this.gameMode = gameMode;
	}
}

package com.philippe75.plus_minus;

import com.philippe75.game.Game;
import com.philippe75.game.GameMode;
import com.philippe75.game.Mode;
import com.philippe75.game.ModeFactory;

/**
 * <b>This class starts the PlusMinus game with the selected mode.</b> 
 * 
 * <p>
 * The three available modes are :
 * <ul>
 * <li>Challenger</li>
 * <li>Defender</li>
 * <li>Duel</li>
 * </ul>
 * </p>

 * @author PPlaxine
 * @version 1.0
 *
 */
public class PlusMinus extends Game {
	
	/**
	 * Constructor of the class. 
	 */
	public PlusMinus() {
		super.modefactory = new ModeFactory();
	}
	
	/**
	 * Starts the selected game with the mode passed in parameter. 
	 * 
	 * Depending on the mode selected, a Mode Factory creates a new instance of the game in that Mode.  
	 * 
	 * @param gameMode 
	 * 					The mode to start the game with. 
	 * @see GameMode
	 * @see Mode#startTheGame()
	 * @see ModeFactory#createMode(String)
	 */
	public void startGame(GameMode gameMode) {
		
		if(gameMode == GameMode.CHALLENGER) {
			modefactory.createMode("ChallengerPlusMinus"); 
			
		}else if (gameMode == GameMode.DEFENDER) {
			modefactory.createMode("DefenderPlusMinus"); 
			
		}else {
			modefactory.createMode("DuelPlusMinus");
		}	
	}
}

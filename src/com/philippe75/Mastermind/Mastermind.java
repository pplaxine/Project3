package com.philippe75.mastermind;

import com.philippe75.game.Game;
import com.philippe75.game.GameMode;
import com.philippe75.game.Mode;

/**
 * <b>This class starts the Mastermind game with the selected mode.</b> 
 * 
 * <p>
 * The three available modes are :
 * <ul>
 * <li>Challenger</li>
 * <li>Defender</li>
 * <li>Duel</li>
 * </ul>
 * </p>
 * 
 * @author PPlaxine
 * @version 1.0
 *
 */
public class Mastermind extends Game{
	
	/**
	 * Starts the selected game with the mode passed in parameter. 
	 * 
	 * Depending on the mode selected, a new instance of the game in that mode is created and started.  
	 * 
	 * @param gameMode 
	 * 					The mode to start the game with. 
	 * @see GameMode
	 * @see Mode#startTheGame()
	 */
	public void startGame(GameMode gameMode) {
		super.startGame(gameMode);
		if(gameMode == GameMode.CHALLENGER) {
			mode = new ChallengerMastermind(); 
			mode.startTheGame();
			
		}else if (gameMode == GameMode.DEFENDER) {
			mode = new DefenderMastermind();
			mode.startTheGame();
		}else {
			mode = new DuelMastermind();
			mode.startTheGame();
		}	
	}
}

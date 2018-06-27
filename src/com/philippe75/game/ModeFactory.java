package com.philippe75.game;

import com.philippe75.mastermind.ChallengerMastermind;
import com.philippe75.mastermind.DefenderMastermind;
import com.philippe75.mastermind.DuelMastermind;
import com.philippe75.menu.GameMode;
import com.philippe75.menu.GameType;
import com.philippe75.menu.Menusettings;
import com.philippe75.plus_minus.ChallengerPlusMinus;
import com.philippe75.plus_minus.DefenderPlusMinus;
import com.philippe75.plus_minus.DuelPlusMinus;

/**
 * 
 * <b>This class is a factory that creates an instance of the Game in required Mode.</b>
 * 
 * <p>
 * The different Modes are : 
 * <ul>
 * <li>ChallengerPlusMinus</li>
 * <li>DefenderPlusMinus</li>
 * <li>DuelPlusMinus</li>
 * <li>ChallengerMastermind</li>
 * <li>DefenderMastermind</li>
 * <li>DuelMastermind</li>
 * </ul>
 * </p>
 * 
 * @author PPlaxine
 * @version 1.0
 */
public class ModeFactory {
		
	/**
	 * Instantiates the Game with mode required that are contained in Menusettings passed into parameter.  
	 * 
	 * @param settings 
	 * 					The type of game and mode of the game.  
	 * @return an instance of the game in the required mode. 
	 * 
	 * @see ChallengerPlusMinus
	 * @see DefenderPlusMinus
	 * @see DuelPlusMinus
	 * @see ChallengerMastermind
	 * @see DefenderMasterminds
	 * @see DuelMastermind
	 */
	public IGame createMode(Menusettings settings) {
		
		if (settings.getGameType() == GameType.PLUSMINUS) {
			if (settings.getGameMode() == GameMode.CHALLENGER)
				return	new ChallengerPlusMinus(); 
			if (settings.getGameMode() == GameMode.DEFENDER)
				return new DefenderPlusMinus();
			else
				return new DuelPlusMinus();
		}else {
			if (settings.getGameMode() == GameMode.CHALLENGER)
				return	new ChallengerMastermind();
			if (settings.getGameMode() == GameMode.DEFENDER)
				return	new DefenderMastermind();
			else
				return new DuelMastermind();
		} 
		
	}
}

package com.philippe75.game;

import com.philippe75.mastermind.ChallengerMastermind;
import com.philippe75.mastermind.DefenderMastermind;
import com.philippe75.mastermind.DuelMastermind;
import com.philippe75.newPack.GameType;
import com.philippe75.newPack.Menusettings;
import com.philippe75.plus_minus.ChallengerPlusMinus;
import com.philippe75.plus_minus.DefenderPlusMinus;
import com.philippe75.plus_minus.DuelPlusMinus;

/**
 * 
 * <b>This class is a factory that creates an instance of the required IGame</b>
 * 
 * <p>
 * The different IGame are : 
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
	 * Instantiate the IGame of the game depending on requested mode passed into parameter.  
	 * 
	 * @param mode
	 * 				The mode to stars the game with. 
	 * @return an instance of the game of the required mode. 
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
				return	 new ChallengerPlusMinus(); 
			if (settings.getGameMode() == GameMode.DEFENDER)
				return 	new DefenderPlusMinus();
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

package com.philippe75.PlusMoins;

import com.philippe.game.Game;
import com.philippe.game.GameMode;
import com.philippe.game.Mode;
import com.philippe.game.SecretNumGenerator;

public class PlusMoins implements Game, Mode {
	 
	// Starts a challengerMode of PlusMoins with code length and number of mistakes allowed in parameter
	
	@Override
	public void startChallengerMode() {
		ChallengerPlusMoins cPM = new ChallengerPlusMoins(new SecretNumGenerator(4),4);
		
	}
	
	// Starts a DefenseurMode of PlusMoins with number of mistakes allowed in parameter 
	@Override
	public void startDefenseurMode() {
		DefenseurPlusMoins dPM = new DefenseurPlusMoins(4);
		
	}
	
	// Starts a DuelMode of PlusMoins
	@Override
	public void startDuelMode() {
		System.out.println("je suis PlusMoins et je lance le mode Duel");
		
	}
	
	// Goes to the menu list
	@Override
	public void startMenu() {
		System.out.println("je suis PM et je lance le menu");
		
	}
	
	// Start the game with the mode selected in parameter
	
	@Override
	public void startGame(GameMode mode) {
		if (mode.equals(GameMode.CHALLENGER)) {
			startChallengerMode();
		}else if (mode.equals(GameMode.DEFENSEUR)) {
			startDefenseurMode();
		}else {
			startDuelMode();
		}
	}




}

package com.philippe75.PlusMoins;

import com.philippe.game.Game;
import com.philippe.game.GameMode;
import com.philippe.game.Mode;
import com.philippe.game.SecretNumGenerator;

public class PlusMoins implements Game, Mode {
	 
	
	@Override
	public void startChallengerMode() {
		ChallengerPlusMoins cPM = new ChallengerPlusMoins(new SecretNumGenerator(3),3);
		
	}

	@Override
	public void startDefenseurMode() {
		System.out.println("je suis PlusMoins et je lance le mode Defenseur");
		
	}

	@Override
	public void startDuelMode() {
		System.out.println("je suis PlusMoins et je lance le mode Duel");
		
	}

	@Override
	public void startMenu() {
		System.out.println("je suis PM et je lance le menu");
		
	}
	
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

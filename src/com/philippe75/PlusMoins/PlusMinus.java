package com.philippe75.PlusMoins;

import com.philippe75.game.Game;
import com.philippe75.game.GameMode;
import com.philippe75.generators.SecretNumGenerator;

public class PlusMinus extends Game {
	
	
	
	public void startGame(GameMode gameMode) {

		super.startGame(gameMode);
		if(gameMode == GameMode.CHALLENGER) {
			ChallengerPlusMinus cPM = new ChallengerPlusMinus();	
		}else if (gameMode == GameMode.DEFENDER) {
			System.out.println("PlusMinus mode Defender");	
		}else {
			System.out.println("PlusMinus mode Duel");
		}	
	}
}

package com.philippe75.Mastermind;

import com.philippe75.game.Game;
import com.philippe75.game.GameMode;

public class Mastermind extends Game{

	
	public void startGame(GameMode gameMode) {
		super.startGame(gameMode);
		if(gameMode == GameMode.CHALLENGER) {
			ChallengerMastermind cM = new ChallengerMastermind();		
		}else if (gameMode == GameMode.DEFENDER) {
			System.out.println("Mastermind mode Defender");	
		}else {
			System.out.println("Mastermind mode Duel");
		}	

	}
}

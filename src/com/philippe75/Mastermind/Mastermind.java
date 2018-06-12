package com.philippe75.mastermind;

import com.philippe75.game.Game;
import com.philippe75.game.GameMode;
import com.philippe75.game.Mode;

public class Mastermind extends Game{
	
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

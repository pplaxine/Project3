package com.philippe75.plus_minus;

import com.philippe75.game.Game;
import com.philippe75.game.GameMode;
import com.philippe75.game.Mode;

public class PlusMinus extends Game {
	
	public void startGame(GameMode gameMode) {
		super.startGame(gameMode);
		
		if(gameMode == GameMode.CHALLENGER) {
			mode = new ChallengerPlusMinus();
			mode.startTheGame();
			
		}else if (gameMode == GameMode.DEFENDER) {
			mode = new DefenderPlusMinus();
			mode.startTheGame();
			
		}else {
			mode = new DuelPlusMinus();
			mode.startTheGame();
		}	
	}
	
	protected void fish() {
		
	}
}

package com.philippe75.game;

import com.philippe75.Mastermind.Mastermind;
import com.philippe75.PlusMoins.PlusMoins;

public interface Game {
	
	// private Mode mode = new Mastermind(); // indiqué par Cesare (cette interface doit être transformée en class également 
	
	
	
	public void startMenu();
	
	
	public void startGame(GameMode gameMode);
	
}

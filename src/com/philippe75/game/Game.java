package com.philippe75.game;

import com.philippe75.Mastermind.Mastermind;
import com.philippe75.PlusMoins.PlusMoins;

public interface Game {
	
	// private Mode mode = new Mastermind(); // indiqu� par Cesare (cette interface doit �tre transform�e en class �galement 
	
	
	
	public void startMenu();
	
	
	public void startGame(GameMode gameMode);
	
}

package com.philippe75.game;

public abstract class Game {
	
	private GameMode gameMode;
	public Mode mode; 
	
	public void startGame(GameMode gameMode) {
		this.gameMode = gameMode;
	}
	

	
	
}

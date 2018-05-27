package com.philippe75.game;

public abstract class Game {
	
	GameMode gameMode;
	
	public void startGame(GameMode gameMode) {
		this.gameMode = gameMode;
	}
	
	
	
	
}

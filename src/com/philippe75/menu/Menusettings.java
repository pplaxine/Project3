package com.philippe75.menu;

/**
 * <b>Class that stored the user game type and game mode selections.</b>
 * 
 * @author PPlaxine
 * @version 1.0
 * 
 * @see GameType
 * @see GameMode
 *
 */
public class Menusettings {
	
	/**
	 * Stores game type selection. 
	 * 
	 * @see Menu#chooseGame()
	 */
	private GameMode gameMode;
	
	/**
	 * Stores game mode selection. 
	 * 
	 * @see Menu#chooseMode()
	 */
	private GameType gameType;
	
	/**
	 * Getter. 
	 * 
	 * @return Game type selected.
	 */
	public GameMode getGameMode() {
		return gameMode;
	}
	
	/**
	 * Setter. 
	 * 
	 * @param gameMode set the type of game selected.
	 */
	public void setGameMode(GameMode gameMode) {
		this.gameMode = gameMode;
	}
	
	/**
	 * Getter. 
	 * 
	 * @return Game mode selected.
	 */
	public GameType getGameType() {
		return gameType;
	}
	
	/**
	 * Setter. 
	 * 
	 * @param gameMode set the mode selected.
	 */
	public void setGameType(GameType gameType) {
		this.gameType = gameType;
	} 
}

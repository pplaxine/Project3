package com.philippe75.game;

/**
 * <b>This interface constrains all the class that implements it to define the following methods : </b>
 * 
 * <p>
 * <li>startTheGame()</li>
 * <li>setProperties()</li>
 * <li>rinWelcome()</li>
 * <li>initGame()</li>
 * </p>
 * 
 * @author PPlaxine
 * @version 1.0
 */
public interface IGame {
	
	public void startTheGame();
	
	public boolean setProperties();
	
	public void printWelcome();
	
	public void initGame();
	
}



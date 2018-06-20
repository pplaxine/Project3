package com.philippe75.game;

/**
 * This interface constrains all the class that implements it to define a startTheGame() method, as well as, a setProperties method. 
 * 
 * A method displayFish is reachable to all the class implementing this interface.It display a drawing of a fish in String format.     
 * 
 * @author PPlaxine
 * @version 1.0
 */
public interface IGame {
	
	public void startTheGame();
	
	public boolean setProperties();
	
	public void printWelcome();
	
	
}



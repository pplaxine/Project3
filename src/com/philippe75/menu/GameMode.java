package com.philippe75.menu;

/**
 * <b>Enumeration of the possible mode for each game</b>  
 * 
 * <p>
 * Is composed of three different mode
 * <ul>
 * <li>Challenger</li>
 * <li>Defender</li>
 * <li>Duel</li>
 * </ul>
 * </p>
 * 
 * <p>
 * Depending on the mode, the game will be started with the selected mode.  
 * </p>
 * 
 * @author PPlaxine
 * @version 1.0
 *
 */
public enum GameMode {
	CHALLENGER(),
	DEFENDER(),
	DUEL();
}

package com.philippe75.game;

import com.philippe75.mastermind.ChallengerMastermind;
import com.philippe75.mastermind.DefenderMastermind;
import com.philippe75.mastermind.DuelMastermind;
import com.philippe75.plus_minus.ChallengerPlusMinus;
import com.philippe75.plus_minus.DefenderPlusMinus;
import com.philippe75.plus_minus.DuelPlusMinus;

/**
 * 
 * <b>This class is a factory that creates an instance of the required Mode</b>
 * 
 * <p>
 * The different Mode are : 
 * <ul>
 * <li>ChallengerPlusMinus</li>
 * <li>DefenderPlusMinus</li>
 * <li>DuelPlusMinus</li>
 * <li>ChallengerMastermind</li>
 * <li>DefenderMastermind</li>
 * <li>DuelMastermind</li>
 * </ul>
 * </p>
 * 
 * @author PPlaxine
 * @version 1.0
 */
public class ModeFactory {

	/**
	 * Instantiate the Mode of the game depending on requested mode passed into parameter.  
	 * 
	 * @param mode
	 * 				The mode to stars the game with. 
	 * @return an instance of the game of the required mode. 
	 * 
	 * @see ChallengerPlusMinus
	 * @see DefenderPlusMinus
	 * @see DuelPlusMinus
	 * @see ChallengerMastermind
	 * @see DefenderMasterminds
	 * @see DuelMastermind
	 */
	public Mode createMode(String mode) {
		 if(mode == null)
			 return null;
		 
		 if(mode.equals("ChallengerPlusMinus")) {
			 return new ChallengerPlusMinus();
		 }else if (mode.equals("DefenderPlusMinus")) {
			 return new DefenderPlusMinus();
		 }else if (mode.equals("DuelPlusMinus")) {
			 return new DuelPlusMinus();
		 }else if (mode.equals("ChallengerMastermind")) {
			 return new ChallengerMastermind();
		 }else if (mode.equals("DefenderMastermind")) {
			 return new DefenderMastermind();
		 }else if (mode.equals("DuelMastermind")) {
			 return new DuelMastermind();
		 }else {
			 return null;
		 }
	}
}

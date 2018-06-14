package com.philippe75.game;

import com.philippe75.mastermind.ChallengerMastermind;
import com.philippe75.mastermind.DefenderMastermind;
import com.philippe75.mastermind.DuelMastermind;
import com.philippe75.plus_minus.ChallengerPlusMinus;
import com.philippe75.plus_minus.DefenderPlusMinus;
import com.philippe75.plus_minus.DuelPlusMinus;

public class ModeFactory {

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

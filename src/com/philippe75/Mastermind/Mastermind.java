package com.philippe75.Mastermind;

import com.philippe75.PlusMoins.ModePlusMoins;
import com.philippe75.jeux.Jeu;

public class Mastermind extends Jeu {
	ModeMastermind mode = new ChallengerMastermind(); 
	
	public Mastermind() {}
	
	public Mastermind(ModeMastermind mode) {
		this.mode = mode; 
	}

	@Override
	public void lancerLeJeu() {
		mode.typeDeMode();
		
	}
	
	public void setMode(ModeMastermind mode) {
		this.mode = mode; 
	}
	
}

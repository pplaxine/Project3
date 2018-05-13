package com.philippe75.PlusMoins;

import com.philippe75.jeux.Jeu;

public class PlusMoins extends Jeu {
	ModePlusMoins mode = new ChallengerPlusMoins(); 
	
	public PlusMoins() {}
	
	public PlusMoins(ModePlusMoins mode) {
		this.mode = mode; 
	}

	public void lancerLeJeu() {
		mode.typeDeMode();
	}
	
	public void setMode(ModePlusMoins mode) {
		this.mode = mode; 
	}
	
	
}

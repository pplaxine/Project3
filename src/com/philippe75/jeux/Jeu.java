package com.philippe75.jeux;

import com.philippe75.modeDeveloppeur.ModeJoueur;
import com.philippe75.modeDeveloppeur.TypeAffichage;

public abstract class Jeu {
	
	TypeAffichage affichage = new ModeJoueur();
	
	public Jeu() {}
	
	public Jeu(TypeAffichage affichage) {
		this.affichage = affichage; 
	}

	public void selectAffichage() {
		affichage.selectAffichage();
	}
	
	public abstract void lancerLeJeu(); 
	
	public void setAffichage(TypeAffichage affichage) {
		this.affichage = affichage; 
	}
	
	public TypeAffichage getAffichage() {
		return this.affichage;
	}
	
	
	
	
}
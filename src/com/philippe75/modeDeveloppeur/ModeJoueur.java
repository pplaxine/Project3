package com.philippe75.modeDeveloppeur;

public class ModeJoueur implements TypeAffichage{
	
	@Override
	public void selectAffichage() {
		System.out.println("J'affiche en mode Joueur donc je cache le chiffre secret.");
		
	}

}

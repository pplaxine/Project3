package com.philippe75.jeux;

import com.philippe75.Mastermind.DefenseurMastermind;
import com.philippe75.Mastermind.DuelMastermind;
import com.philippe75.Mastermind.Mastermind;
import com.philippe75.PlusMoins.DefenseurPlusMoins;
import com.philippe75.PlusMoins.DuelPlusMoins;
import com.philippe75.PlusMoins.PlusMoins;
import com.philippe75.modeDeveloppeur.ModeDeveloppeur;

public class Test {
 public static void main (String[]args) {
	 
//	 PlusMoins pM = new PlusMoins(); 
//	 
//	
//	 pM.selectAffichage();
//	 pM.setAffichage(new ModeDeveloppeur());
//	 pM.selectAffichage();
//	 System.out.println("");
//	 
//	 pM.lancerLeJeu();
	 
//	 pM.setMode(new DuelPlusMoins());
//	 pM.lancerLeJeu();
//	 pM.setMode(new DefenseurPlusMoins());
//	 pM.lancerLeJeu();
//	 System.out.println("\n************************************\n"); 
//	 
	 Mastermind mM = new Mastermind(); 
	 
// 	 mM.selectAffichage();
//	 pM.setAffichage(new ModeDeveloppeur());
//	 pM.selectAffichage();
	 
//	 System.out.println("");
//	 
	 mM.lancerLeJeu();
	 mM.setMode(new DuelMastermind());
	 mM.lancerLeJeu();
	 mM.setMode(new DefenseurMastermind());
	 mM.lancerLeJeu();
	
	 
	 
 }
}

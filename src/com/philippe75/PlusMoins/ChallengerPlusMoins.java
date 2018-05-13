package com.philippe75.PlusMoins;

import java.util.ArrayList;
import java.util.Scanner;


public class ChallengerPlusMoins implements ModePlusMoins{
	@Override
	public void typeDeMode() {

		System.out.println("je lance le mode Challenger PlusMoins");
		lancerJeu(); 
	}
	
	private void lancerJeu() {
		Scanner clavier = new Scanner(System.in);
		
		ArrayList<Integer> tabUserAnswer = new ArrayList(); 	
		ArrayList<Integer> tabNombreAleatoire;
		ChiffreSecret2 cS = new ChiffreSecret2(5); 
		String nombreAleatoire = cS.generer();
	
			System.out.println(nombreAleatoire);

		
		
	
		String nombreUser = clavier.nextLine(); 
		
		
		for (int i = 0; i < nombreUser.length(); i++) {
			tabUserAnswer.add(Character.getNumericValue(nombreUser.charAt(i)));  
		}
 
		tabNombreAleatoire = cS.getTabChiffre(); 
		
		String comparaison = myComparator(tabNombreAleatoire, tabUserAnswer);
		
		System.out.println("Votre résultat ---> " + comparaison);
		
		
		
		
	}

	public static String myComparator(ArrayList<Integer> tabNombreAleatoire, ArrayList<Integer> tabUserAnswer) {
		 String reponse ="/"; 
		
		for (int i = 0; i < tabUserAnswer.size(); i++) {
			String str;
			
			if (tabNombreAleatoire.get(i) < tabUserAnswer.get(i)) {
				str = "-";
			}else if (tabNombreAleatoire.get(i) > tabUserAnswer.get(i)) {
				str = "+";
			}else str = "="; 
			
			reponse += str;
		}
		
		return reponse; 
	}
	
	
}

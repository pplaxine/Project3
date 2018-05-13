package com.philippe75.PlusMoins;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class ChiffreSecret2 {
	
	private int nombre; 
	private ArrayList<Integer> tabChiffre = new ArrayList(); 
	private Random random = new Random();
	
	public ChiffreSecret2(int nombre) {
		this.nombre = nombre;  
	}
	
	public String generer() {
		String nombreAleatoire = ""; 
		
		for (int i = 0; i < this.nombre; i++) {
			tabChiffre.add(random.nextInt(10));	
			nombreAleatoire += tabChiffre.get(i).toString(); 
		} 
		return nombreAleatoire; 
	}

	public ArrayList<Integer> getTabChiffre() {
		return tabChiffre;
	}

	
	
	
	
	
}

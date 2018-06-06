package com.philippe75.generators;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SecretNumGenerator {
	
	private int numberSize; 
	private List<Integer> tabNumber = new ArrayList<Integer>(); 
	private String randomNumber = "";
	
	// generate a random number of a length passed in parameter 
	public SecretNumGenerator(int numberSize) {
		this.numberSize = numberSize; 
		generate();
	}
	
	public String generate() {
		
	
		for (int i = 0; i < this.numberSize; i++) {
			Random random = new Random();
			tabNumber.add(random.nextInt(10));	
			this.randomNumber += tabNumber.get(i).toString(); 
		} 
		return randomNumber; 
	}
	
	
	// Getters  
	
	public List<Integer> getTabNumber() {
		return tabNumber;
	}
	
	public String getRandomNumber() {
		return this.randomNumber;
	}
	
	public int getNumberSize() {
		return numberSize; 
	}

	
	
	
	
	
}

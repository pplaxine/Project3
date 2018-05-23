package com.philippe75.Mastermind;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class SecretColorCombinationGenerator {

	private ColorCombinationLength colorCombinationLength;
	private HowManyColors howManyColors; 
	private ArrayList<String>tabColor; 
	private ArrayList<String> tabColorCombination = new ArrayList<String>();
	private int index;
	private Random random = new Random();

	
	public SecretColorCombinationGenerator(ColorCombinationLength colorCombinaisonLength, HowManyColors howManyColors) {
		this.colorCombinationLength = colorCombinaisonLength;
		this.howManyColors = howManyColors;
		generate(); 
	}
	
	public void generate() {
		initiateColorChoice();		
		for (int i = 0; i < colorCombinationLength.getIntValue() ; i++) {
			index = random.nextInt(howManyColors.getIntValue());		
			tabColorCombination.add(tabColor.get(index)); 
		}	
	}
	
	public void initiateColorChoice() {
		tabColor = new ArrayList<String>();
		
		tabColor.add("Red");
		tabColor.add("Blue");
		tabColor.add("Green");
		tabColor.add("Yellow");
		tabColor.add("Orange");
		tabColor.add("Purple");
		tabColor.add("Brown");
		tabColor.add("Pink");
		tabColor.add("Gold");
		tabColor.add("Silver");
		
		Collections.shuffle(tabColor);
	}
	
	public ArrayList<String> getColorRange(){
		
		ArrayList<String> lol = new ArrayList<>();
		for (int i = 0; i < howManyColors.getIntValue(); i++) {
			lol.add(tabColor.get(i));
		}
		
		return lol;
	}
	
	public ArrayList<String> getTabColorCombination(){
		return this.tabColorCombination;
	}
	
	
	public int getColorCombinationLength() {
		return tabColorCombination.size();
	}
	
	public String toString() {
		String str = "";
		for (String b1 : tabColorCombination) {
			str += "|"+ b1 +"|";
		}
		return str; 
	}
	
}

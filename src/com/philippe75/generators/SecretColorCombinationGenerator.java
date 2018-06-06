package com.philippe75.generators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

import com.philippe75.game.HowManyColors;

public class SecretColorCombinationGenerator {

	private final int COLOR_COMBINATION_LENGTH;
	private HowManyColors howManyColors; 
	private ArrayList<String>tabColor; 
	private HashMap<Integer, String> tabColorCombination = new HashMap<Integer, String>();
	private int index;
	private Random random = new Random();

	
	public SecretColorCombinationGenerator(int colorCombinaisonLength, HowManyColors howManyColors) {
		this.COLOR_COMBINATION_LENGTH = colorCombinaisonLength;
		this.howManyColors = howManyColors;
		generate(); 
	}
	
	public void generate() {
		initiateColorChoice();		
		for (int i = 0; i < COLOR_COMBINATION_LENGTH ; i++) {
			index = random.nextInt(howManyColors.getIntValue());		
			tabColorCombination.put(i, tabColor.get(index)); 
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
	
	public ArrayList<String> getColorPool(){
		
		ArrayList<String> colorPool = new ArrayList<>();
		for (int i = 0; i < howManyColors.getIntValue(); i++) {
			colorPool.add(tabColor.get(i));
		}
		
		return colorPool;
	}
	
	public HashMap<Integer, String> getTabColorCombination(){
		return this.tabColorCombination;
	}
	
	
	public String toString() {
		String str = "";
		for (String b1 : tabColorCombination.values()) {
			str += "|"+ b1 +"|";
		}
		return str; 
	}
	
}

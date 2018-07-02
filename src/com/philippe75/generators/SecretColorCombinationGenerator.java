package com.philippe75.generators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

/**
 * <b>SecretColorCombinationGenerator is a class that generate a random combination of colours.</b>
 * 
 * <p>
 * The random combination can be generate of any length, and from a pool of 4 up to 10 colours. Those value are passed as parameters. 
 * </p>
 * 
 * @see SecretColorCombinationGenerator#SecretColorCombinationGenerator(int, HowManyColors)
 * @see SecretColorCombinationGenerator#COLOR_COMBINATION_LENGTH
 * @see SecretColorCombinationGenerator#howManyColors
 * 
 * @author PPlaxine
 * @version 1.0
 */
public class SecretColorCombinationGenerator {

	/**
	 * Length of the combination. 
	 * 
	 * Define how long must be the combination. 
	 * 
	 * @see SecretColorCombinationGenerator#SecretColorCombinationGenerator(int, HowManyColors)
	 * @see SecretColorCombinationGenerator#generate()
	 */
	private final int COLOR_COMBINATION_LENGTH;
	
	/**
	 * The size of the pool of colours combination is made of.  
	 * 
	 * @see HowManyColors
	 * @see SecretColorCombinationGenerator#SecretColorCombinationGenerator(int, HowManyColors)
	 * @see SecretColorCombinationGenerator#generate()
	 * @see SecretColorCombinationGenerator#getColorPool()
	 */
	private HowManyColors howManyColors; 
	
	
	/**
	 * Contains all the colours to choose from.
	 * 
	 * tabColor is shuffled before the first values are transfered into a List of the size of the number of colours to choose from. 
	 * 
	 * TabColor is used to generate the combination. 
	 * 
	 * @see SecretColorCombinationGenerator#initiateColorChoice()
	 * @see SecretColorCombinationGenerator#getColorPool()
	 * @see SecretColorCombinationGenerator#generate()
	 */
	private ArrayList<String>tabColor; 
	
	/**
	 * Stores the secret colour combination.
	 * 
	 * @see SecretColorCombinationGenerator#generate()
	 */
	private HashMap<Integer, String> tabColorCombination = new HashMap<Integer, String>();
	
	/**
	 * Constructor of SecretColorCombinationGenerator.
	 * 
	 * Starts the process of generating the secret combination.  
	 * 
	 * 
	 * @param colorCombinaisonLength
	 * 						Define the length of the combination 
	 * @param howManyColors
	 * 						Define the size of the pool of colours to choose from. 
	 * @see SecretColorCombinationGenerator#generate()
	 * 	
	 */
	public SecretColorCombinationGenerator(int colorCombinaisonLength, HowManyColors howManyColors) {
		this.COLOR_COMBINATION_LENGTH = colorCombinaisonLength;
		this.howManyColors = howManyColors;
		generate(); 
	}
	
	/**
	 * Generate the secret combination. 
	 * 
	 * Takes in consideration the length required and choose amongst a list of colours predefined. 
	 * 
	 * @see SecretColorCombinationGenerator#tabColorCombination
	 * @see SecretColorCombinationGenerator#COLOR_COMBINATION_LENGTH
	 * @see SecretColorCombinationGenerator#howManyColors
	 */
	private void generate() {
		Random random = new Random();
		int index; 
		initiateColorChoice();	
		
		for (int i = 0; i < COLOR_COMBINATION_LENGTH ; i++) {
			index = random.nextInt(howManyColors.getIntValue());		
			tabColorCombination.put(i, tabColor.get(index)); 
		}	
	}
	
	/**
	 * Generates a Pool of all possible colours.
	 * 
	 * @see SecretColorCombinationGenerator#tabColor
	 */
	private void initiateColorChoice() {
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
	
	/**
	 * Pool of colours to make the combination from. 
	 * 
	 * @return Pool of colours to choose from composed with a number of colours passed in parameter.
	 * @see SecretColorCombinationGenerator#howManyColors
	 */
	public ArrayList<String> getColorPool(){
		ArrayList<String> colorPool = new ArrayList<>();
		for (int i = 0; i < howManyColors.getIntValue(); i++) {
			colorPool.add(tabColor.get(i));
		}
		return colorPool;
	}
	
/**
 * Getter.
 * 	
 * @return the tabColorCombination containing the secret colour combination.  
 * @see SecretColorCombinationGenerator#tabColorCombination
 */
	public HashMap<Integer, String> getTabColorCombination(){
		return this.tabColorCombination;
	}

	/**
	 * Returns a String value of the combination. 
	 * 
	 * @see SecretColorCombinationGenerator#tabColorCombination 
	 */
	public String toString() {
		String str = "";
		for (String b1 : tabColorCombination.values()) {
			str += "|"+ b1 +"|";
		}
		return str; 
	}
}

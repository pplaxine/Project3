package com.philippe75.generators;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * <b>SecretNumGenerator is a class that generate a random combination.</b>
 * 
 * <p>
 * 	The random combination can be of any length passed as parameter.   
 * </p>
 * 
 * <p>
 * 	The combination is stored in a List. 
 * </p> 
 * 
 * @see SecretNumGenerator#SecretNumGenerator(int)
 * @see SecretNumGenerator#tabNumber
 * 
 * @author PPlaxine
 * @version 1.0
 */
public class SecretNumGenerator {
	
	/**
	 * Length of the combination. 
	 * 
	 * Used to define how long must be the combination.   
	 * 
	 * @see SecretNumGenerator#SecretNumGenerator(int)
	 */
	private int numberSize; 
	
	/**
	 * Store the combination generated in List format.
	 *  
	 * @see SecretNumGenerator#generate()
	 */
	private List<Integer> tabNumber = new ArrayList<Integer>(); 
	
	/**
	 * Store the combination generated in String format.
	 * 
	 * @see SecretNumGenerator#generate()
	 */
	private String randomNumber = "";
	
	
	/**
	 * Constructor of SecretNumGenerator.
	 * 
	 * Define how long must be the combination.
	 * 
	 * Starts to generate the combination. 
	 *  
	 * @param numberSize
	 * 						the length of the combination. 
	 * @see SecretNumGenerator#numberSize 
	 * @see SecretNumGenerator#generate()
	 */
	// generate a random number of a length passed in parameter 
	public SecretNumGenerator(int numberSize) {
		this.numberSize = numberSize; 
		generate();
	}
	
	/** 
	 * Generates the combination.
	 * 
	 * Store the random combination in a List. 
	 * 
	 * @return Combination in String format.  
	 * @see SecretNumGenerator#tabNumber
	 */
	public String generate() {
	
		for (int i = 0; i < this.numberSize; i++) {
			Random random = new Random();
			tabNumber.add(random.nextInt(10));	
			this.randomNumber += tabNumber.get(i).toString(); 
		} 
		return randomNumber; 
	}
	
	/**
	 * Getter. 
	 * 
	 * @return The TabNumber value.
	 * @see SecretNumGenerator#tabNumber
	 */
	public List<Integer> getTabNumber() {
		return tabNumber;
	}
	
	/**
	 * Getter. 
	 * 
	 * @return The RandomNumber value.
	 * @See SecretNumGenerator#randomNumber
	 */				 
	public String getRandomNumber() {
		return this.randomNumber;
	}
	
	/**
	 * Getter. 
	 * 
	 * @return The size of the combination.
	 * @See SecretNumGenerator#numberSize
	 */	
	public int getNumberSize() {
		return numberSize; 
	}
}

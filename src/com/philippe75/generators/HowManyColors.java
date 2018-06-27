package com.philippe75.generators;


/**
 * <b>Enumeration to indicate the size of the colour pool.</b>
 * 
 * <p>
 * The size starts from 4 different colours, up to 10 colours.  
 * </p>
 * 
 * @author PPlaxine
 * @version 1.0
 */
public enum HowManyColors {
	
	FOUR(4),
	FIVE(5),
	SIX(6),
	SEVEN(7),
	HEIGH(8),
	NINE(9),
	TEN(10);
	
	/**
	 * Constructor of HowManyColors
	 */
	private int number; 
	HowManyColors(int number) {
		this.number = number;
	}
	
	/**
	 * Getter
	 * 
	 * @return the numerical value of the enum.
	 */
	public int getIntValue() {
		return number;
	}
}

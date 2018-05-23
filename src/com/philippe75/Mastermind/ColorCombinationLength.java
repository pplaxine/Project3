package com.philippe75.Mastermind;

public enum ColorCombinationLength {

	FOUR(4),
	FIVE(5),
	SIX(6),
	SEVEN(7),
	HEIGH(8),
	NINE(9),
	TEN(10);
	
	private int number; 
	ColorCombinationLength(int number) {
		this.number = number;
	}
	
	public int getIntValue() {
		return number;
	}
	
}

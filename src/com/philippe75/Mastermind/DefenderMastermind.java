package com.philippe75.Mastermind;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

public class DefenderMastermind {
	
	private HowManyColors howManyColors;
	private int errorAllowed, score, wellPlaced, exist;
	private String userAnswer="";
	private HashMap<Integer, String> userSelection; 
	private ArrayList<String> tabColorRange;
	private ArrayList<String> tabColor; 
	private ArrayList<String> tabColorNotFound;
	private ArrayList<String> tabColorRemaining;
	private ArrayList<String> ComputerAnswer; 
	
	
	public DefenderMastermind(HowManyColors howManyColors, int errorAllowed) {
		this.errorAllowed = errorAllowed;
		this.howManyColors = howManyColors;
		startTheGame();		
	}
	
	private void startTheGame() {
		printWelcome();
		initiateColorChoice();
		generateQuestion();
		requestUserSecretCombi();
		generateAnswer();
		generateHint();
	}

	private void printWelcome() {
		String 	str  = "******************************************\n";
				str += "*******         WELCOME TO         *******\n";
				str += "*******      MASTERMIND GAME       *******\n";
				str += "*******       DEFENDER MODE        *******\n";	
				str += "******************************************";
		System.out.println(str); 
	}
	
	public void initiateColorChoice() {
		tabColorRange = new ArrayList<String>();
		
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
		
		for (int i = 0; i < howManyColors.getIntValue(); i++) {
			tabColorRange.add(tabColor.get(i)); 
		}
	
	}
	
	public void generateQuestion() {
		int i = 0;
		String str =""; 
		for (String couleur : tabColorRange) {
			str += "["+ i +"]" + couleur +" ";
			i++;
		}
		
		System.out.println("\n" + str);
		System.out.println("\nPlease compose your secret color combination by selecting between 4 and 10 colors listed");
	}
	
	public void requestUserSecretCombi() {
		Scanner clavier = new Scanner(System.in);
		userSelection = new HashMap<>(); 
		String str = ""; 
		do {
			//Using Regex to make sure user enter the right value type (Integer) and the correct length of this value type  
			if(userAnswer !="") {
				if(!userAnswer.matches("^[./[0-9]]+$")) { 
					System.out.println("Please enter a number instead of a characters.");					
				}else if (userAnswer.length() > 10 || userAnswer.length() < 4){	
					System.out.println((userAnswer.length() > 10 )? "The number of digits is superior to the number of digits required" 
							: "The number of digits is inferior to the number of digits required");
				}else if (!userAnswer.matches("^[./[0-"+tabColorRange.size() +"]]+$")) { 
					System.out.printf("Your selection has to be composed of number bewteen [0] and [%d]\n", (tabColorRange.size()-1));
				}
			}
	
			this.userAnswer = clavier.nextLine();
			
		} while (!userAnswer.matches("^[./[0-9]]+$") || userAnswer.length() > 10 || userAnswer.length() < 4 || !userAnswer.matches("^[./[0-"+ tabColorRange.size()+"]]+$") );
	
		for (int i = 0; i < userAnswer.length(); i++) {
			userSelection.put(i, tabColorRange.get(Character.getNumericValue(userAnswer.charAt(i))));
			
			str += "|" + userSelection.get(i) + "|";
		}
		System.out.println("You have selected the folwing secret color combination : \n     *** " + str + " ***\n");
	}
	
	public void generateAnswer() {
		ComputerAnswer = new ArrayList<>(); 
		Random random = new Random(); 
		String str = "";
		for (int i = 0; i < userSelection.size(); i++) {
			int index = random.nextInt(tabColorRange.size());
			
			ComputerAnswer.add(tabColorRange.get(index));
			str += "|" + ComputerAnswer.get(i) + "|";
		}
		System.out.println("Computer tries ............." + str);
	}
	
	public void generateHint() {
		int i = 0; 
		tabColorNotFound = new ArrayList<>();
		tabColorRemaining = new ArrayList<>();
		
		for (String color : ComputerAnswer) {
			tabColorNotFound.add(color); 
		}
		
		for (String color : userSelection.values()) {
			tabColorRemaining.add(color); 
		}
		
		for (String color : userSelection.values()) {
			int j = 0;
			for (String b1 : ComputerAnswer) {
				if(color.equals(b1) && i == j) {
						wellPlaced++;
						tabColorNotFound.set(j, "Found");
						tabColorRemaining.set(j, "");
				}
				j++;
			}
			i++; 
		}
			
		for (String color : tabColorNotFound) {
			int j = 0;
			for (String b1 : tabColorRemaining) {
				if(color.equals(b1) ) {
						exist++;
				}
				j++;
			}
			i++; 
		}
		
		System.out.printf("there is %d wellplace and %d exist" , wellPlaced,exist );
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}

package com.philippe75.Mastermind;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class ChallengerMastermind {

	private SecretColorCombinationGenerator sCG; 
	private int errorAllowed, score;
	
	private ArrayList<String> tabUserAnswer = new ArrayList<String>();
	private HashMap colorHashMap = new HashMap(); 
	private String userAnswer=""; 
	
	
	public ChallengerMastermind(SecretColorCombinationGenerator sCG,int errorAllowed) {
		this.sCG = sCG; 
		this.errorAllowed = errorAllowed;
		startTheGame();
		
		
	}
	
	private void startTheGame() {
		
		printWelcome();
		displaySecretColorCombi();
		printQuestion();
		initGame();
	}
	
	private void printWelcome() {
		String 	str  = "******************************************\n";
				str += "*******         WELCOME TO         *******\n";
				str += "*******      MASTERMIND GAME       *******\n";
				str += "*******      CHALLENGER MODE       *******\n";	
				str += "******************************************";
		System.out.println(str); 
	}
	
	private void displaySecretColorCombi() {
		System.out.printf("*** Secret Color Combination : %s ***\n\n",sCG.toString());
	}
	
	private void printQuestion() {	
		String str ="";
		
		for (int i = 0; i < sCG.getColorRange().size(); i++) {
			
			str += " ["+ (i) +"]" + sCG.getColorRange().get(i);
			colorHashMap.put(i, sCG.getColorRange().get(i)); 
			
		}
		
		System.out.printf("Please enter a combination of %d choices amongst those colors :%s.\n", sCG.getColorCombinationLength() , str);
	}
		
	private void initGame() {
		Scanner clavier = new Scanner(System.in);
			
		do {		
				// add a try after each question 
				score++;
				tabUserAnswer.clear();
				 
				// Repeat while user didn't enter the correct value 
				do {
					//Using Regex to make sure user enter the right value type (Integer) and the correct length of this value type  
					if(userAnswer !="") {
						if(!userAnswer.matches("^[./[0-9]]+$")) { 
							System.out.println("Please enter a number instead of a characters.");
						}else if (!userAnswer.matches("^[./[0-"+ ((sCG.getColorCombinationLength() > 9)? sCG.getColorCombinationLength()-1 :sCG.getColorCombinationLength()) + "]]+$")) { 
							System.out.printf("You have to enter a number bewteen [0] and [%d]\n", sCG.getColorCombinationLength()-1);
						}else {	
							System.out.println((sCG.getColorCombinationLength() < userAnswer.length())? "The number of digits is superior to the number of digits required" 
									: "The number of digits is inferior to the number of digits required");
						}
					}
			
				this.userAnswer = clavier.nextLine();
			
						
				} while (!userAnswer.matches("^[./[0-9]]+$") || sCG.getColorCombinationLength() != userAnswer.length() 
						|| !userAnswer.matches("^[./[0-"+((sCG.getColorCombinationLength() > 9)? sCG.getColorCombinationLength()-1 :sCG.getColorCombinationLength()) +"]]+$") );		
			
				answerComparator();
				userAnswer = ""; 
				
			} while (!sCG.getTabColorCombination().toString().equals(tabUserAnswer.toString()) && score < this.errorAllowed);
		if(sCG.getTabColorCombination().toString().equals(tabUserAnswer.toString())) {
			System.out.println("Congratulations !!! You have found the correct secret color combination.");
		}else {
			System.out.println("\n                    ***** GAME OVER ! You were almost there. *****\n The solution is "+ sCG.toString() +"I am sure you will be more succeful nex time!");
		}
		clavier.close();
	}
	
	
	private void answerComparator() {
			int index = 0;
			int correctPosition = 0; 
			int exist = 0;
		for (int i = 0; i < userAnswer.length(); i++) {
			index = Character.getNumericValue((userAnswer.charAt(i)));
			tabUserAnswer.add((String)colorHashMap.get(index));			
		}
		
		for (int i = 0; i < tabUserAnswer.size(); i++) {
			if(tabUserAnswer.get(i).equals(sCG.getTabColorCombination().get(i))){
				correctPosition++;
			}else if (sCG.getTabColorCombination().contains(tabUserAnswer.get(i))) {
				exist++;
			}
		}
		String stringAnswer = arraylistToString(tabUserAnswer);
		System.out.printf("Your anwser is %s ---> %d well placed, %d exist but not well placed.\n",stringAnswer, correctPosition, exist);
	}
	
	private String arraylistToString(ArrayList<String> arrayList) {
		String str ="";
		for (String b1 : arrayList) {
			str += "|" +b1 +"|";  
		}
		return str;
	}
	
	
	
}


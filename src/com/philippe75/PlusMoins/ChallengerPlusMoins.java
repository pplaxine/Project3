package com.philippe75.PlusMoins;

import java.util.ArrayList;
import java.util.Scanner;

import com.philippe.game.SecretNumGenerator;


public class ChallengerPlusMoins {
		
		private static final String String = null;
		private SecretNumGenerator sNG;
		private String randomNumber, hint;
		private String userAnswer = "";
		private ArrayList<Integer> tabUserAnswer = new ArrayList<Integer>(); 	
		private ArrayList<Integer> tabRandomNum = new ArrayList<Integer>();
		int score = 0; 
		int errorAllowed; 
		
	public ChallengerPlusMoins(SecretNumGenerator sNG, int errorAllowed) {		
		this.sNG = sNG;
		this.errorAllowed = errorAllowed; 
		
		
		randomNumber = this.sNG.getRandomNumber();
		tabRandomNum = this.sNG.getTabNumber();
		Scanner clavier = new Scanner(System.in);
		
		// generate Presentation screen 
		
		System.out.println("************************* ");
		System.out.println("***    + or - GAME    *** ");
		System.out.println("*************************");
		
		System.out.println("\n*** Secret Num : " + randomNumber + " *** ");
		
		// Generate first Question  
		System.out.println("Please enter a number of " + sNG.getNumberSize() + (sNG.getNumberSize() > 1 ? " digits." : " digit."));
		
		// Repeat the question while user has enough tries left and hasn't found the answer
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
					}else {
						System.out.println((sNG.getNumberSize() < userAnswer.length())? "The number of digits is superior to the number of digits required" : "The number of digits is inferior to the number of digits required");
					}
				}
			this.userAnswer = clavier.nextLine();
					
			} while (!userAnswer.matches("^[./[0-9]]+$") || sNG.getNumberSize() != userAnswer.length());		
				
			// Transform userAnwser to a ArrayList to use the method userAnswerComparator 
			for (int i = 0; i < userAnswer.length(); i++) {
				tabUserAnswer.add(Character.getNumericValue(userAnswer.charAt(i)));  
			}
			
			// Generate the user next move hint 
			this.hint = userAnswerComparator(this.tabRandomNum, this.tabUserAnswer); 
			
			// Print hint if the answer isn't found or the game finished 
			if(!tabRandomNum.toString().equals(tabUserAnswer.toString()) && score < errorAllowed) {
				System.out.println("Your Answer " + userAnswer + " isn't so far, here is the hint ----- > " + hint);				
			}
			userAnswer = ""; 
		} while (!tabRandomNum.toString().equals(tabUserAnswer.toString()) && score < errorAllowed);
		
		// Print Result once the game is over. 
		if (tabRandomNum.toString().equals(tabUserAnswer.toString())){
			System.out.printf("Congratulation !!! you found the answer after %d trials!!!", score);			
		}else {
			System.out.printf("GAME OVER !!!! the secret number was %s ", randomNumber );
		}
		
		// close the resource 
		clavier.close();
	}
	
	// return the hint after comparing each character of two ArrayList.
	public String userAnswerComparator(ArrayList<Integer> tab1, ArrayList<Integer> tab2){
		 String answer =""; 	
		for (int i = 0; i < tab1.size(); i++) {
			String str;
			
			if (tab1.get(i) < tab2.get(i)) {
				str = "-";
			}else if (tab1.get(i) > tab2.get(i)) {
				str = "+";
			}else str = "="; 
			
			 answer += str;
		}
		return answer; 
	}
	
}

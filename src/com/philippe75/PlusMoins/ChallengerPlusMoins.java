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
		
		System.out.println("************************* ");
		System.out.println("***    + or - GAME    *** ");
		System.out.println("*************************");
		
		System.out.println("\n*** Secret Num : " + randomNumber + " *** ");
		
		System.out.println("Please enter a number of " + sNG.getNumberSize() + (sNG.getNumberSize() > 1 ? " digits." : " digit."));
		
		do {
			score++;
			
			do {
				if(!userAnswer.equals("")) {
					if(!userAnswer.matches("^[./[0-9]]+$")) { 
						System.out.println("Please enter a number instead of a characters.");
					}else {
						System.out.println((sNG.getNumberSize() < userAnswer.length())? "The number of digits is superior to the number of digits required" : "The number of digits is inferior to the number of digits required");
					}
				}
				
				this.userAnswer = clavier.nextLine();
				
			} while (!userAnswer.matches("^[./[0-9]]+$") || sNG.getNumberSize() != userAnswer.length());		// adding regex instead of try catch:) 
				tabUserAnswer.clear();
			
			for (int i = 0; i < userAnswer.length(); i++) {
				tabUserAnswer.add(Character.getNumericValue(userAnswer.charAt(i)));  
			}
			
			this.hint = userAnswerComparator(this.tabRandomNum, this.tabUserAnswer); 
			
			if(!tabRandomNum.toString().equals(tabUserAnswer.toString()) && score < errorAllowed) {
				System.out.println("Your Answer " + userAnswer + " isn't so far, here is the hint ----- > " + hint);				
			}
			userAnswer = ""; 
		} while (!tabRandomNum.toString().equals(tabUserAnswer.toString()) && score < errorAllowed);
		
		
		if (tabRandomNum.toString().equals(tabUserAnswer.toString())){
			System.out.printf("Congratulation !!! you found the answer after %d trials!!!", score);			
		}else {
			System.out.println("GAME OVER !!!!");
		}
		
		clavier.close();
	}

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

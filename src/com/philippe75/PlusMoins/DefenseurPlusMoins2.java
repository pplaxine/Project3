package com.philippe75.PlusMoins;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;
import java.util.function.UnaryOperator;

import javax.management.StringValueExp;

public class DefenseurPlusMoins2 {
	
	private SecretNumGenerator sNG;
	private ArrayList<Integer> tabComputerAnswer = new ArrayList<Integer>();
	private ArrayList<Integer> tabComputerAnswer2 = new ArrayList<Integer>();
	private ArrayList<Integer> tabUserCode = new ArrayList<Integer>();
	private String userCode = "";
	private String computerAnswer, hint;
	private int errorAllowed, minValue, maxValue, newValue; 
	private int score = 0; 
	boolean stop = false;
	
	
	
	public DefenseurPlusMoins2 (int errorAllowed) {	
	
		this.errorAllowed = errorAllowed; 
		
		
		Scanner clavier = new Scanner(System.in);
		
		// generate Presentation screen 
		
		System.out.println("************************* ");
		System.out.println("***    + or - GAME    *** ");
		System.out.println("*************************");
		
		System.out.println("Please enter your secret code"); 
		
		//Using Regex to make sure user enter the right value type (Integer).
		do {
			if(!userCode.equals("")) {
				if(!userCode.matches("^[./[0-9]]+$")) { 
					System.out.println("Please enter a number instead of a characters.");
				}
			}
			
			this.userCode = clavier.nextLine();
			
		} while (!userCode.matches("^[./[0-9]]+$"));	
		
		// Transform userAnwser to a ArrayList 
		
		for (int i = 0; i < userCode.length(); i++) {
			tabUserCode.add(Character.getNumericValue(userCode.charAt(i)));  
		}
		
		// Generates a secreteNumber of the same length as user's answer.  
		sNG = new SecretNumGenerator(tabUserCode.size());
		tabComputerAnswer = sNG.getTabNumber();
		computerAnswer = sNG.getRandomNumber();
		 
		// Repeat the question while Computer has enough tries left and hasn't found the answer
		do {
			// add a try after each question 
			score++;
			
			// if the first answer is wrong generates a new secretNumber (taking in account the hint)    
			if(stop) {
				
				this.tabComputerAnswer = new ArrayList<Integer>();
				tabComputerAnswer.addAll(tabComputerAnswer2);
				tabComputerAnswer2.clear();
				computerAnswer = ArrayListIntegerToString(tabComputerAnswer);
			}
			
			// generates the hint 
			hint = userAnswerComparator(tabUserCode, tabComputerAnswer);
			System.out.printf("Computer tries %s ---> %s \n", computerAnswer, hint );
			
			
			// for each number contained in the answer generate a new number taking in account the hint 
			int i =0; 
			
			minValue = 0;
			maxValue = 9;
			for (Integer b1 : tabComputerAnswer) {
				int test = tabUserCode.get(i).compareTo(b1);
				
				if(test < 0) {			  
				// if (i) digit of the userCode is smaller than (i) digit of the computeranswer. 	
					maxValue = b1;
					newValue = ((maxValue+2-1)/2);
					tabComputerAnswer2.add(newValue);
				
				
				
				// if (i) digit of the userCode is greater than (i) digit of the computeranswer. 		
				}else if(test > 0) {
					minValue = b1; 
					newValue = minValue + (((maxValue - minValue) + 2 - 1)/2) ;
					
					tabComputerAnswer2.add(newValue);
					
					
				// if (i) digit of the userCode is equal (i) digit of the computeranswer. 	
				}else {
					tabComputerAnswer2.add(b1);
				}
				i++;
			}
			stop = true; 
		} while (!tabComputerAnswer.toString().equals(tabUserCode.toString()) && score < errorAllowed);
		
		// Print Result once the game is over.
		if(tabComputerAnswer.toString().equals(tabUserCode.toString())) {
			System.out.printf("You loose ! Computer found your code after %d attempts", score );	
		}else {
			System.out.printf("Congratulation ! Computer couldn't find your code after %d attempts", score );	
		}
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
	
	// Convert Araylist to String
	public String ArrayListIntegerToString(ArrayList<Integer> arrayList) {
		String str = ""; 
		for (Integer b1 : arrayList) {
			str +=  b1;
		}
		
		return str; 
	}
}

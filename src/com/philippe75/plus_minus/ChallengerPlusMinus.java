package com.philippe75.plus_minus;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

import com.philippe75.game.Main;
import com.philippe75.game.Mode;
import com.philippe75.generators.SecretNumGenerator;


public class ChallengerPlusMinus implements Mode{
		
		private SecretNumGenerator sNG;
		private int errorAllowed, combiLength; 
		private String hint;
		private String userAnswer = "";
		private List<Integer> tabUserAnswer = new ArrayList<Integer>(); 	
		private int score = 0; 
		boolean dev = Main.isDev();
		
		
	public ChallengerPlusMinus() {	
		setProperties();
	}
			
	public void startTheGame() {
		sNG = new SecretNumGenerator(combiLength);
		printWelcome();	
		if(dev)
			displaySecretNum();
		System.out.println("Please enter a number of " + sNG.getNumberSize() + (sNG.getNumberSize() > 1 ? " digits." : " digit."));
		initGame();
	}

	private void setProperties() {
		Properties p = new Properties();
		
		try(InputStream is = new FileInputStream("Ressources/dataConfig.properties")) {	
	
			p.load(is);
			combiLength = Integer.parseInt(p.getProperty("CombinationLength"));
			errorAllowed = Integer.parseInt(p.getProperty("errorAllowed"));
		
			if(new String("true").equals(p.getProperty("devMode"))) {
				this.dev = true; 	
			}

			
		} catch (FileNotFoundException e) {
			System.out.println("The file specified does not exit.");
		} catch (IOException e) {
			System.out.println("Error with the propertiesFiles.");
		}
	}
	

	
	private void printWelcome() {
		String 	str  = "******************************************\n";
				str += "*******        WELCOME TO          *******\n";
				str += "*******        + or - GAME         *******\n";
				str += "*******      CHALLENGER MODE       *******\n";	
				str += "******************************************";
		System.out.println(str); 
	}
	
	protected void displaySecretNum() {
		System.out.println("\n*** Secret Num : " + sNG.getRandomNumber() + " *** ");
	}
	
	private void initGame() {
		Scanner clavier = new Scanner(System.in);
		
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
			this.hint = generateHint(sNG.getTabNumber(), this.tabUserAnswer); 
			
			// Print hint if the answer isn't found or the game finished 
			if(!sNG.getTabNumber().toString().equals(tabUserAnswer.toString()) && score < errorAllowed) {
				System.out.println("Your Answer " + userAnswer + " isn't so far, here is the hint ----- > " + hint);				
			}
			userAnswer = ""; 
		} while (!sNG.getTabNumber().toString().equals(tabUserAnswer.toString()) && score < errorAllowed);
		
		// Print Result once the game is over. 
		if (sNG.getTabNumber().toString().equals(tabUserAnswer.toString())){
			System.out.printf("Congratulation !!! you found the answer after %d trials!!!\n", score);			
		}else {
			System.out.printf("GAME OVER !!!! the secret number was %s \n", sNG.getRandomNumber());
		}
		
	
	
	}
	
	
	// return the hint after comparing each character of two ArrayList.
	private String generateHint(List<Integer> tab1, List<Integer> tab2){
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

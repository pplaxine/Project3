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
import com.philippe75.game.TextEnhencer;
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
		if (setProperties()) {
			sNG = new SecretNumGenerator(combiLength);
			printWelcome();	
			displaySecretNum(); 
			System.out.println(TextEnhencer.ANSI_YELLOW + "Please enter a number of " + sNG.getNumberSize() + (sNG.getNumberSize() > 1 ? " digits." : " digit." + TextEnhencer.ANSI_RESET));
			initGame();
		}
	}

	public boolean setProperties() {
		Properties p = new Properties();
		
		try(InputStream is = getClass().getResourceAsStream("dataConfig.properties")) {	
			p.load(is);
			combiLength = Integer.parseInt(p.getProperty("CombinationLength"));
			errorAllowed = Integer.parseInt(p.getProperty("errorAllowed"));
		
			if(new String("true").equals(p.getProperty("devMode"))) {
				this.dev = true; 	
			}
		} catch (NullPointerException e) {
			System.err.print("The file dataConfig.properties could not be found.");
			return false;
		} catch (IOException e) {
			System.err.println("Error with the propertiesFiles.");
			return false;
		}
		return true;
	}
	

	
	private void printWelcome() {
		String 	str = TextEnhencer.ANSI_YELLOW; 
				str += "******************************************\n";
				str += "*******        WELCOME TO          *******\n";
				str += "*******        + or - GAME         *******\n";
				str += "*******      CHALLENGER MODE       *******\n";	
				str += "******************************************";
				str += TextEnhencer.ANSI_RESET;
		System.out.println(str); 
	}
	
	protected void displaySecretNum() {
		System.out.println(TextEnhencer.ANSI_CYAN+ "\nComputer has generated a secret combination for you to guess ..." + TextEnhencer.ANSI_RESET);
		if(dev)			
			System.out.println(TextEnhencer.ANSI_CYAN + "\t*** Secret combination : " + sNG.getRandomNumber() + " ***\n" + TextEnhencer.ANSI_RESET);
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
						System.out.println(TextEnhencer.ANSI_RED + "Please enter a number instead of a characters." + TextEnhencer.ANSI_RESET);
					}else {
						System.out.println((sNG.getNumberSize() < userAnswer.length())? TextEnhencer.ANSI_RED + "The number of digits is superior to the number of digits required" + TextEnhencer.ANSI_RESET : TextEnhencer.ANSI_RED + "The number of digits is inferior to the number of digits required" + TextEnhencer.ANSI_RESET);
					}
				}
			System.out.print(TextEnhencer.ANSI_YELLOW);	
			this.userAnswer = clavier.nextLine();
			System.out.print(TextEnhencer.ANSI_RESET);	
		
					
			} while (!userAnswer.matches("^[./[0-9]]+$") || sNG.getNumberSize() != userAnswer.length());		
				
			// Transform userAnwser to a ArrayList to use the method userAnswerComparator 
			for (int i = 0; i < userAnswer.length(); i++) {
				tabUserAnswer.add(Character.getNumericValue(userAnswer.charAt(i)));  
			}
			
			// Generate the user next move hint 
			this.hint = generateHint(sNG.getTabNumber(), this.tabUserAnswer); 
			
			// Print hint if the answer isn't found or the game finished 
			if(!sNG.getTabNumber().toString().equals(tabUserAnswer.toString()) && score < errorAllowed) {
				System.out.println(TextEnhencer.ANSI_CYAN + "Your Answer " + userAnswer + " isn't so far, here is the hint ----- > " + hint + TextEnhencer.ANSI_RESET);				
			}
			userAnswer = ""; 
		} while (!sNG.getTabNumber().toString().equals(tabUserAnswer.toString()) && score < errorAllowed);
		
		// Print Result once the game is over. 
		if (sNG.getTabNumber().toString().equals(tabUserAnswer.toString())){
			displayFish();
			System.out.printf(TextEnhencer.ANSI_YELLOW + "\t   .+*°*+.+> | Congratulation !!! | <+.+*°*+.\n\t   You found the answer after %d trials!!! \n"+ TextEnhencer.ANSI_RESET, score);
			
		}else {
			System.out.printf(TextEnhencer.ANSI_RED + "\t   .+*°*+.+> | GAME OVER !!!! | <+.+*°*+.\n"+ TextEnhencer.ANSI_CYAN +  "\t\t The secret number was %s \n", sNG.getRandomNumber() + TextEnhencer.ANSI_RESET);
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

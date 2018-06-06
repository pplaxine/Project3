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

public class DuelPlusMinus implements Mode{
		
	private SecretNumGenerator sNG, sNG2;
	private int combiLength; 
	private int minValue, maxValue, newValue; 
	private String hint;
	private String userAnswer = "", userCode = "";
	private String computerAnswer;
	private List<Integer> tabComputerAnswer2 = new ArrayList<Integer>();
	private List<Integer> tabUserAnswer = new ArrayList<Integer>();
	private List<Integer> tabUserCode = new ArrayList<Integer>();
	private boolean stop = false;
	private boolean dev = Main.isDev();
	private List<Integer> tabComputerAnswer;
	Scanner clavier = new Scanner(System.in);
	private int score =0;
	
	public DuelPlusMinus() {
		setProperties();
	}
	
	public void startTheGame() {
		sNG = new SecretNumGenerator(combiLength);
		printWelcome();
		requestUserSecretNum();
		if(dev)
			displaySecretNum();
		System.out.println("\nYour are the first to play!\nPlease enter a number of " + sNG.getNumberSize() + (sNG.getNumberSize() > 1 ? " digits." : " digit."));
		initGame();
	}

	private void setProperties() {
		Properties p = new Properties();
		
		try(InputStream is = new FileInputStream("Ressources/dataConfig.properties")) {	
	
			p.load(is);
			combiLength = Integer.parseInt(p.getProperty("CombinationLength"));
	
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
				str += "*******         DUEL MODE          *******\n";	
				str += "******************************************\n";
		System.out.println(str); 
	}
	
	private void displaySecretNum() {
		System.out.println("\n --------------------------------");
		System.out.println("|  *** Secret Num : " + sNG.getRandomNumber() + " ***   |");
		System.out.println(" --------------------------------");
	}
	
	
	private void initGame() {
	
		// Repeat the question while user has enough tries left and hasn't found the answer
		do {	
			claimUserAnswer();
			generateUserAnswerHint();	
			//----------------------------------------------------------------------------------------
			// invoque only if the player didn't find the answer
			if(!sNG.getTabNumber().toString().equals(tabUserAnswer.toString()))
				generateComputerAnswer();
			//----------------------------------------------------------------------------------------
			
		}while (!sNG.getTabNumber().toString().equals(tabUserAnswer.toString()) && !tabComputerAnswer.toString().equals(tabUserCode.toString()));
		
		// Print Result once the game is over. 
		if (sNG.getTabNumber().toString().equals(tabUserAnswer.toString())){
			System.out.printf("Congratulation !!! you found the secret Code  !!!\n");			
		}else {
			System.out.printf("GAME OVER !!!! Computer found the answer before you !!! the secret number was %s \n", sNG.getRandomNumber());
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
	
	private void requestUserSecretNum() {
		
		
		System.out.printf("Please enter the %d digits secret code, that the computer will have to guess, below : \n",sNG.getNumberSize()); 
		
		//Using Regex to make sure user enter the right value type (Integer).
		do {
			if(userCode !="") {
				if(!userCode.matches("^[./[0-9]]+$")) { 
					System.out.println("Please enter a number instead of a characters.");
				}else {
					System.out.println((sNG.getNumberSize() < userCode.length())? "The number of digits is superior to the number of digits required" : "The number of digits is inferior to the number of digits required");
				}
			}
			
			this.userCode = clavier.nextLine();
			
		} while (!userCode.matches("^[./[0-9]]+$") || sNG.getNumberSize() != userCode.length());	
		
		
		// Transform userAnwser to a ArrayList 
		
		for (int i = 0; i < userCode.length(); i++) {
			tabUserCode.add(Character.getNumericValue(userCode.charAt(i)));  
		}
		System.out.println("Computer has to guess : " + userCode);
	}
	
	// Generate Computers first random Number 
	private void generateComputerFirstTry() {
		// Generates a secreteNumber of the same length as user's answer.  
		sNG2 = new SecretNumGenerator(tabUserCode.size());
		tabComputerAnswer = sNG.getTabNumber();
		computerAnswer = sNG.getRandomNumber();
	}
	
	// Convert Araylist to String
	public String ArrayListIntegerToString(List<Integer> arrayList) {
		String str = ""; 
		for (Integer b1 : arrayList) {
			str +=  b1;
		}
		
		return str; 
	}
	
	private String createHint(List<Integer> tab1, List<Integer> tab2){
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
	
	//return a new number taking in account the hint given with the previous try. 
	private void generateAnswerWithHint() {
		// for each number contained in the answer generate a new number taking in account the hint 
		int i =0; 
		
		minValue = 0;
		maxValue = 9;
		for (Integer b1 : tabComputerAnswer) {
			int test = tabUserCode.get(i).compareTo(b1);
			
			if(test < 0) {			  
			// if (i) digit of the userCode is smaller than (i) digit of the computeranswer. 	
				maxValue = b1;
				newValue = ((maxValue)/2);
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
	}
	
	public void claimUserAnswer() {
		score++;
		if(score>1 ) 
			System.out.println("\nIt's your turn to play! ");
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
	}
	
	public void generateUserAnswerHint() {
		// Transform userAnwser to a ArrayList to use the method userAnswerComparator 
		for (int i = 0; i < userAnswer.length(); i++) {
			tabUserAnswer.add(Character.getNumericValue(userAnswer.charAt(i)));  
		}
		
		// Generate the user next move hint 
		this.hint = generateHint(sNG.getTabNumber(), this.tabUserAnswer); 
		
		// Print hint if the answer isn't found or the game finished 
		if(!sNG.getTabNumber().toString().equals(tabUserAnswer.toString())) {
			System.out.println("Your Answer " + userAnswer + " isn't so far, here is the hint ----- > " + hint);				
		}
		userAnswer = ""; 
	}
	
	
	
	public void generateComputerAnswer() {
		generateComputerFirstTry();
		   
		if(stop) {
			
			this.tabComputerAnswer = new ArrayList<Integer>();
			
			tabComputerAnswer.addAll(tabComputerAnswer2);
			tabComputerAnswer2.clear();
			computerAnswer = ArrayListIntegerToString(tabComputerAnswer);
		}
		
		// generates the hint 
		hint = createHint(tabUserCode, tabComputerAnswer);
		
		generateAnswerWithHint();
		
		//print computers answer and show the hint
		System.out.printf("\nComputer has to guess : %s\nComputer tries %s ---> %s \n", userCode,computerAnswer, hint );

		stop = true; 
		
	}
	

}

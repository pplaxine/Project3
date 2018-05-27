package com.philippe75.Mastermind;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

import com.philippe75.game.Main;
import com.philippe75.generators.SecretColorCombinationGenerator;

public class ChallengerMastermind {
	
	private SecretColorCombinationGenerator sCG;
	private HowManyColors howManyColors = HowManyColors.FOUR;  
	private int errorAllowed, score, combiLength, correctPosition;
	private HashMap<Integer, String> colorHashMap = new HashMap<>(); 
	private HashMap<Integer, String> tabColCombi = new HashMap<>(); 
	private HashMap<Integer, String> tabUserAnswer = new HashMap<>();
	private HashMap<Integer, String> tabToCompare = new HashMap<>(); 
	private String userAnswer=""; 
	private boolean dev = Main.isDev(); 
	
	public ChallengerMastermind() {

		setProperties();
		startTheGame();
	}
	
	private void startTheGame() {
		
		sCG = new SecretColorCombinationGenerator(combiLength, howManyColors);
		System.out.println(dev);

		printWelcome();
		displaySecretColorCombi();
		printQuestion();
		initGame();
	}
	
	private void setProperties() {
		Properties p = new Properties();
		
	try {
		InputStream is = new FileInputStream("ConfigFile/dataConfig.properties");
		p.load(is);
		
	} catch (FileNotFoundException e) {
		System.out.println("The file specified does not exit.");
	} catch (IOException e) {
		System.out.println("Error with the propertiesFiles.");
	}
	
	combiLength = Integer.parseInt(p.getProperty("CombinationLength"));
	errorAllowed = Integer.parseInt(p.getProperty("errorAllowed"));
	if(p.getProperty("devMode").equals("activated"))
		this.dev = true; 
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
		
		
		if(dev)
			System.out.printf("*** Secret Color Combination : %s ***\n\n",sCG.toString());
	}
	
	private void printQuestion() {	
		String str ="";
		
		for (int i = 0; i < sCG.getColorPool().size(); i++) {
			
			str += " ["+ (i) +"]" + sCG.getColorPool().get(i);
			
			colorHashMap.put(i, sCG.getColorPool().get(i)); 
			
		}
		
		System.out.printf("Please enter a combination of %d choices amongst those colors :%s.\n", combiLength , str);
	}
		
	private void initGame() {
		tabColCombi = sCG.getTabColorCombination(); 
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
						}else if (!userAnswer.matches("^[./[0-"+ ((combiLength > 9)? combiLength -1 : combiLength) + "]]+$")) { 
							System.out.printf("You have to enter a number bewteen [0] and [%d]\n", combiLength -1);
						}else {	
							System.out.println((combiLength < userAnswer.length())? "The number of digits is superior to the number of digits required" 
									: "The number of digits is inferior to the number of digits required");
						}
					}
			
				this.userAnswer = clavier.nextLine();
			
						
				} while (!userAnswer.matches("^[./[0-9]]+$") || combiLength != userAnswer.length() 
						|| !userAnswer.matches("^[./[0-"+((combiLength> 9)? combiLength -1 : combiLength) +"]]+$") );		
			
				answerComparator();
				userAnswer = ""; 
				
			} while (correctPosition != this.combiLength && score < this.errorAllowed);
		if(correctPosition == this.combiLength) {
			System.out.printf("\nCongratulations !!! You have found the correct secret color combination after %d " + ((score < 2)? "trial." : "trials.") + "\n", score);
		}else {
			System.out.println("\n\n\n\t\t ***** GAME OVER ! You were almost there. *****\nThe solution is "+ sCG.toString() +"I am sure you will be more succeful next time!\n");
		}
		
	}
	
	private void answerComparator() {
			String str=""; 
			int index = 0;
			correctPosition = 0; 
			int exist = 0;
		
		// create user answer in HashMap and String  	
		for (int i = 0; i < userAnswer.length(); i++) {
			index = Character.getNumericValue((userAnswer.charAt(i)));
			tabUserAnswer.put(i,(String)colorHashMap.get(index));
			str += "|" + tabUserAnswer.get(i) + "|"; 
			
			tabToCompare.put(i, tabColCombi.get(i)); 		
			
			
			
		}
		
		
		// check if any color is at the right position 
		for (int i = 0; i < tabUserAnswer.size(); i++) {
			if(tabUserAnswer.get(i).equals(tabToCompare.get(i))){
				correctPosition++;
				tabToCompare.replace(i, "Found");
				tabUserAnswer.replace(i, "Found2"); 
			}
		}
		
		for (int i = 0; i < tabUserAnswer.size(); i++) {
			for (int j = 0; j < tabUserAnswer.size(); j++) {
				if(tabUserAnswer.get(i).equals(tabToCompare.get(j))) {
					tabToCompare.replace(j, "Found3");
					tabUserAnswer.replace(i, "Found4");
					exist++;
				}
			}	
		}
		
		System.out.printf("Your anwser is %s ---> %d well placed, %d exist but not well placed.\n",str, correctPosition, exist);
		
	}
}

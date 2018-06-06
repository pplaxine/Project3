package com.philippe75.mastermind;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

import com.philippe75.game.HowManyColors;
import com.philippe75.game.Main;
import com.philippe75.game.Mode;
import com.philippe75.generators.SecretColorCombinationGenerator;

public class ChallengerMastermind implements Mode{
	
	private SecretColorCombinationGenerator sCG;
	private int errorAllowed, score, combiLength, correctPosition;
	private Map<Integer, String> colorPool = new HashMap<>(); 
	private Map<Integer, String> tabColCombi = new HashMap<>(); 
	private Map<Integer, String> tabUserAnswer = new HashMap<>();
	private Map<Integer, String> tabToCompare = new HashMap<>(); 
	private HowManyColors howManyColors; 
	private String userAnswer=""; 
	Scanner clavier = new Scanner(System.in);
	
	//Boolean from Main 
	private boolean dev = Main.isDev(); 
	
	public ChallengerMastermind() {
		setProperties();
	}
	
	public void startTheGame() {
		
		// generate a secret combination 
		sCG = new SecretColorCombinationGenerator(combiLength, howManyColors);
		
		
		printWelcome();
		displaySecretColorCombi();
		printQuestion();
		initGame();
	}
	
	// set properties from ConfigFile 
	
	private void setProperties() {
		Properties p = new Properties();
		
		try {
			InputStream is = new FileInputStream("Ressources/dataConfig.properties");
			p.load(is);
			
			combiLength = Integer.parseInt(p.getProperty("CombinationLength"));
			errorAllowed = Integer.parseInt(p.getProperty("errorAllowed"));
			howManyColors = HowManyColors.valueOf((p.getProperty("ColorPool")));
			
			if(new String("true").equals(p.getProperty("devMode"))) {
				this.dev = true; 	
			}	
				
		} catch (FileNotFoundException e) {
			System.out.println("The file specified does not exit.");
		} catch (IOException e) {
			System.out.println("Error with the propertiesFiles.");
		}
		
	}
	
	
	// print welcome message
	private void printWelcome() {
		String 	str  = "******************************************\n";
				str += "*******         WELCOME TO         *******\n";
				str += "*******      MASTERMIND GAME       *******\n";
				str += "*******      CHALLENGER MODE       *******\n";	
				str += "******************************************";
		System.out.println(str); 
	}
	
	// display the secret combination if mode dev is activated
	private void displaySecretColorCombi() {
		
		
		if(dev)
			System.out.printf("*** Secret Color Combination : %s ***\n\n",sCG.toString());
	}
	
	// print the question 
	private void printQuestion() {	
		String str ="";
		
		 
		for (int i = 0; i < sCG.getColorPool().size(); i++) {
			
			str += " ["+ (i) +"]" + sCG.getColorPool().get(i);
			
			// add each colour of a secret combination to colorPool 
			colorPool.put(i, sCG.getColorPool().get(i)); 
		}
		//print the question containing the colorpool choices 
		System.out.printf("Please enter a combination of %d choices amongst those colors :%s.\n", combiLength , str);
	}
	
	
	private void initGame() {
		// store the combination in tabColComni 
		tabColCombi = sCG.getTabColorCombination(); 
			
		do {		
				// add a try after each question 
				score++;
				tabUserAnswer.clear();
				getUserAnswer();
				compareAnswer();
				userAnswer = ""; 
				
			} while (correctPosition != this.combiLength && score < this.errorAllowed);
		if(correctPosition == this.combiLength) {
			System.out.printf("\nCongratulations !!! You have found the correct secret color combination after %d " + ((score < 2)? "trial." : "trials.") + "\n", score);
		}else {
			System.out.println("\n\n\n\t\t ***** GAME OVER ! You were almost there. *****\nThe solution is "+ sCG.toString() +"I am sure you will be more succeful next time!\n");
		}
	}
	
	private void getUserAnswer() {
		 
		// Repeat while user didn't enter the correct value 
		do {
			//Using Regex to make sure user enter the right value type (Integer) and the correct length of this value type  
			if(userAnswer !="") {
				if(!userAnswer.matches("^[./[0-9]]+$")) { 
					System.out.println("Please enter a number instead of a characters.");
				}else if (!userAnswer.matches("^[./[0-"+ (sCG.getColorPool().size()-1) + "]]+$")) { 
					System.out.printf("You have to enter a number bewteen [0] and [%d]\n", sCG.getColorPool().size()-1);
				}else {	
					System.out.println((combiLength < userAnswer.length())? "The number of digits is superior to the number of digits required" 
							: "The number of digits is inferior to the number of digits required");
				}
			}
			// store the user answer in String but with digits
			this.userAnswer = clavier.nextLine();
				
		} while (!userAnswer.matches("^[./[0-9]]+$") || combiLength != userAnswer.length() 
				|| !userAnswer.matches("^[./[0-"+(sCG.getColorPool().size()-1) +"]]+$") );		
	}
	
	private void compareAnswer() {
			String str=""; 
			int index = 0;
			correctPosition = 0; 
			int exist = 0;
			tabToCompare.putAll(tabColCombi);
					
		for (int i = 0; i < userAnswer.length(); i++) {
			// userAnswer converted in int and stored in index 
			index = Character.getNumericValue((userAnswer.charAt(i)));
			// then converted in colour and stored in MAP 
			tabUserAnswer.put(i,(String)colorPool.get(index));
			// each element is also added to str for print
			str += "|" + tabUserAnswer.get(i) + "|"; 		
		}
		
		
		// check if any colour is at the same position in tabUserAnswer and tabToCompare 
		for (int i = 0; i < tabUserAnswer.size(); i++) {
			//if element at the correct position 
			if(tabUserAnswer.get(i).equals(tabToCompare.get(i))){
				//increase correctPosition 
				correctPosition++;
				// and replace the value in both tabs 
				tabToCompare.replace(i, "Found");
				tabUserAnswer.replace(i, "Found2"); 
			}
		}
		
		// amongst the remaining values check if there is any match 
		for (int i = 0; i < tabUserAnswer.size(); i++) {
			for (int j = 0; j < tabUserAnswer.size(); j++) {
				// if tabUserAnswer contains at any position an element contained in tabCompare
				if(tabUserAnswer.get(i).equals(tabToCompare.get(j))) {
					// replace the value in both tabs 
					tabToCompare.replace(j, "Found3");
					tabUserAnswer.replace(i, "Found4");
					//increase exist 
					exist++;
				}
			}	
		}
		
		System.out.printf("Your anwser is %s ---> %d well placed, %d exist but not well placed.\n",str, correctPosition, exist);
		
	}
	
	
}

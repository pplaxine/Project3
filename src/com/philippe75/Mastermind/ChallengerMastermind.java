package com.philippe75.mastermind;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

import org.apache.log4j.Logger;

import com.philippe75.game.Fish;
import com.philippe75.game.HowManyColors;
import com.philippe75.game.Main;
import com.philippe75.game.Mode;
import com.philippe75.game.PropertiesFile;
import com.philippe75.game.TextEnhencer;
import com.philippe75.generators.SecretColorCombinationGenerator;
import com.philippe75.plus_minus.ChallengerPlusMinus;

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
	
	private static final Logger log = Logger.getLogger(ChallengerMastermind.class);

	
	public ChallengerMastermind() {
		if(setProperties())
			startTheGame();
	}
	@Override
	public void startTheGame() {
		log.info("Start of Mastermind game in challenger mode");
		sCG = new SecretColorCombinationGenerator(combiLength, howManyColors);
		printWelcome();
		displaySecretColorCombi();
		printQuestion();
		initGame();
		log.info("End of the game");
	}
	
	// set properties from ConfigFile 
	@Override
	public boolean setProperties() {
		
		howManyColors = HowManyColors.valueOf((PropertiesFile.getPropertiesFile("ColorPool")));
		combiLength = Integer.parseInt(PropertiesFile.getPropertiesFile("CombinationLength"));
		errorAllowed = Integer.parseInt(PropertiesFile.getPropertiesFile("errorAllowed"));
		if(new String("true").equals(PropertiesFile.getPropertiesFile("devMode"))) {
			this.dev = true;
		}	
		log.info("Properties set successfully");
		return true;
	}
	
	
	// print welcome message
	@Override
	public void printWelcome() {
		String 	str = TextEnhencer.ANSI_YELLOW;
				str += "\n\n******************************************\n";
				str += "*******         WELCOME TO         *******\n";
				str += "*******      MASTERMIND GAME       *******\n";
				str += "*******      CHALLENGER MODE       *******\n";	
				str += "******************************************";
				str += TextEnhencer.ANSI_RESET;
		System.out.println(str); 
	}
	
	// display the secret combination if mode dev is activated
	private void displaySecretColorCombi() {
		System.out.println(TextEnhencer.ANSI_CYAN+ "\nComputer has generated a secret combination for you to guess ..." + TextEnhencer.ANSI_RESET);
		if(dev)
			log.info("Game is running in developer mode");
			System.out.printf(TextEnhencer.ANSI_CYAN + "*** Secret Color Combination : %s ***\n\n" + TextEnhencer.ANSI_RESET, sCG.toString());
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
		System.out.printf(TextEnhencer.ANSI_YELLOW + "Please enter a combination of %d choices amongst those colors :%s.\n" + TextEnhencer.ANSI_RESET, combiLength , str);
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
			Fish.displayFish();
			System.out.printf( TextEnhencer.ANSI_YELLOW + "\n\t  .+*°*+..+> | Congratulations ! | <+..+*°+.\nYou have found the correct secret color combination after %d " + ((score < 2)? "trial." : "trials.") + "\n" + TextEnhencer.ANSI_RESET, score);
		}else {
			System.out.println(TextEnhencer.ANSI_RED + "\n\t\t\t .+*°*+..+> | GAME OVER !!! | <+..+*°+."+ TextEnhencer.ANSI_CYAN + "\n\t\t\t\tYou were almost there.\nThe solution is "+ sCG.toString() +". I am sure you will be more succeful next time!\n"  + TextEnhencer.ANSI_RESET);
		}
	}
	
	private void getUserAnswer() {
		 
		// Repeat while user didn't enter the correct value 
		do {
			//Using Regex to make sure user enter the right value type (Integer) and the correct length of this value type  
			if(userAnswer !="") {
				if(!userAnswer.matches("^[./[0-9]]+$")) { 
					System.out.println(TextEnhencer.ANSI_RED + "Please enter a number instead of a characters." + TextEnhencer.ANSI_RESET);
					log.warn("User entry mismatch the type required");
				}else if (!userAnswer.matches("^[./[0-"+ (sCG.getColorPool().size()-1) + "]]+$")) { 
					System.out.printf(TextEnhencer.ANSI_RED + "You have to enter a number bewteen [0] and [%d]\n" + TextEnhencer.ANSI_RESET, sCG.getColorPool().size()-1);
					log.warn("User entry mismatch the possible answer choices");
				}else {	
					System.out.println((combiLength < userAnswer.length())? TextEnhencer.ANSI_RED + "The number of digits is superior to the number of digits required" + TextEnhencer.ANSI_RESET 
							: TextEnhencer.ANSI_RED + "The number of digits is inferior to the number of digits required" + TextEnhencer.ANSI_RESET);
					log.warn("User entry mismatch the length of digits required");
				}
			}
			// store the user answer in String but with digits
			System.out.print(TextEnhencer.ANSI_YELLOW);
			this.userAnswer = clavier.nextLine();
			System.out.print(TextEnhencer.ANSI_RESET);
				
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
		
		System.out.printf(TextEnhencer.ANSI_CYAN + "Your anwser is %s ---> %d well placed, %d exist but not well placed.\n" + TextEnhencer.ANSI_RESET,str, correctPosition, exist);
	}
}

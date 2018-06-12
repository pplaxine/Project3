package com.philippe75.mastermind;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Scanner;

import com.philippe75.game.HowManyColors;
import com.philippe75.game.Main;
import com.philippe75.game.Mode;
import com.philippe75.game.TextEnhencer;

public class DefenderMastermind implements Mode{

	private int combiLength, errorAllowed, correctPosition, score;
	private String userAnswer="";
	private HowManyColors howManyColors;
	
	private Map<Integer, String> userSelection; 
	private Map<Integer, String> tabComputerAnswer = new HashMap<>();
	private Map<Integer, String> tabToCompare = new HashMap<>();	
	private List<String> tabColorToReinject = new ArrayList<>();
	private List<String> tabColorPool;
	private List<String> tabColor;
	Scanner clavier = new Scanner(System.in);
	//Boolean from Main 
	private boolean dev = Main.isDev();
	
	public DefenderMastermind() {
		setProperties();	
	}
	
	
	public void startTheGame() {
		if(setProperties()) {
			printWelcome();
			initiateColorChoice();
			generateQuestion();
			requestUserSecretCombi();
			initGame();
		}
	}
	
	// charge the dataConfig.properties file 
	public boolean setProperties() {
		Properties p = new Properties();
		
		try(InputStream is = getClass().getResourceAsStream("dataConfig.properties")) {
			p.load(is);
			errorAllowed = Integer.parseInt(p.getProperty("errorAllowed"));
			combiLength = Integer.parseInt(p.getProperty("CombinationLength"));
			howManyColors = HowManyColors.valueOf((p.getProperty("ColorPool")));
			
			if(new String("true").equals(p.getProperty("devMode"))) {
				this.dev = true; 	
			}	
			
		} catch (NullPointerException e) {
			System.err.println("The file dataConfig.properties could not be found.");
			return false;
		} catch (IOException e) {
			System.err.println("Error with the propertiesFiles.");
			return false;
		}
		return true;
	}

	
	private void printWelcome() {
		String 	str = TextEnhencer.ANSI_YELLOW; 
				str += "\n\n******************************************\n";
				str += "*******         WELCOME TO         *******\n";
				str += "*******      MASTERMIND GAME       *******\n";
				str += "*******       DEFENDER MODE        *******\n";	
				str += "******************************************";
				str += TextEnhencer.ANSI_RESET;
		System.out.println(str); 
	}
	
	// create a random pool of color 
	private void initiateColorChoice() {
		tabColorPool = new ArrayList<String>();
		tabColor = new ArrayList<String>();
		
		tabColor.add("Red");
		tabColor.add("Blue");
		tabColor.add("Green");
		tabColor.add("Yellow");
		tabColor.add("Orange");
		tabColor.add("Purple");
		tabColor.add("Brown");
		tabColor.add("Pink");
		tabColor.add("Gold");
		tabColor.add("Silver");
		
		Collections.shuffle(tabColor);
		
		//add a defined quantity of colours to the pool 
		tabColorPool.addAll(tabColor.subList(0, howManyColors.getIntValue()));
	
	}
	
	// creates the question to summit to the user 
	private void generateQuestion() {
		int i = 0;
		String str =""; 
		// add to a String of the colors of the ColorPool 
		for (String couleur : tabColorPool) {
			str += "["+ i +"]" + couleur +" ";
			i++;
		}
		
		System.out.println(TextEnhencer.ANSI_YELLOW + "\n" + str);
		System.out.println("\nPlease compose your secret color combination by choosing " + combiLength + " colors amongst the colors listed." + TextEnhencer.ANSI_RESET);
	}
	
	
	private void requestUserSecretCombi() {
		
		//creates new HashMap to store user combination choice 
		userSelection = new HashMap<>(); 
		String str = ""; 
		do {
			//Using Regex to make sure user enter the right value type (Integer), as well as,  the correct length of this value type, and not higher range of selection offers.   
			if(userAnswer !="") {
				if(!userAnswer.matches("^[./[0-9]]+$")) { 
					System.out.println(TextEnhencer.ANSI_RED + "Please enter a number instead of a characters." + TextEnhencer.ANSI_RESET);					
				}else if (userAnswer.length() > combiLength || userAnswer.length() < combiLength){	
					System.out.println((userAnswer.length() > combiLength )? TextEnhencer.ANSI_RED + "The number of digits is superior to the number of digits required" + TextEnhencer.ANSI_RESET
							: TextEnhencer.ANSI_RED + "The number of digits is inferior to the number of digits required" + TextEnhencer.ANSI_RESET);
				}else if (!userAnswer.matches("^[./[0-"+(tabColorPool.size() -1) +"]]+$")) { 
					System.out.printf(TextEnhencer.ANSI_RED + "Your selection has to be composed of number bewteen [0] and [%d]\n" + TextEnhencer.ANSI_RESET, (tabColorPool.size()-1));
				}
			}
			System.out.print(TextEnhencer.ANSI_YELLOW);
			// store the user answer in a string
			this.userAnswer = clavier.nextLine();
			System.out.print(TextEnhencer.ANSI_RESET);
		} while ( userAnswer.length() > combiLength || userAnswer.length() < combiLength|| !userAnswer.matches("^[./[0-"+ (tabColorPool.size()-1)+"]]+$") || !userAnswer.matches("^[./[0-9]]+$") );
	
		// Converts String userAnswer digits into colours, store it in the HashMap and in a String to display the choice  
		for (int i = 0; i < userAnswer.length(); i++) {
			userSelection.put(i, tabColorPool.get(Character.getNumericValue(userAnswer.charAt(i))));
			
			str += "|" + userSelection.get(i) + "|";
		}
		System.out.println(TextEnhencer.ANSI_YELLOW + "You have selected the folwing secret color combination : \n     *** " + str + " ***\n" + TextEnhencer.ANSI_RESET);
	}
	
	private void initGame() {
		
		// repeat as long as the combination isn't found or the number or error allowed is reached 
		do {		
			// add a try after each question 
			score++;
			
			generateComputerAnswer();
			compareAnswer();
			
		} while (correctPosition != this.combiLength && score < this.errorAllowed);
		
		// if the answer isn't found user looses 
		if(correctPosition == this.combiLength) {
			System.out.printf(TextEnhencer.ANSI_RED + "\n\t   .+*°*+.+> | GAME OVER !!! | <+..+*°*+."+ TextEnhencer.ANSI_CYAN + "\nComputer found your secret color combination after %d " + ((score < 2)? "trial." : "trials.") + "\n" + TextEnhencer.ANSI_RESET, score);
		// if the answer is found user wins	
		}else {
			displayFish();
			System.out.printf(TextEnhencer.ANSI_YELLOW + "\n\t     .+*°*+.+> | You Win !!! | <+..+*°*+.\nComputer could not find your secret color combination after %d " + ((score < 2)? "trial." : "trials.") + "\n" + TextEnhencer.ANSI_RESET, score);
		}
		
	}
	
	// Find the correct way to handle the result and generate new combination 
	private void generateComputerAnswer() {
		
		Random random = new Random(); 
		int index = 0; 
		//if is empty (means first try) then generate a random combination  
		if(tabComputerAnswer.isEmpty()) {	
			for (int i = 0; i <userSelection.size(); i++) {
				index = random.nextInt(tabColorPool.size());
				tabComputerAnswer.put(i, tabColorPool.get(index));
			}
			
		// After the first try 	
		}else {
			for (int i = 0; i < tabComputerAnswer.size(); i++) {
				
				// if value of the element in tabComputerAnswer is "found" 
				if(tabComputerAnswer.get(i).equals("Found2")) {
					//replace "found" by the value of user selection 
					tabComputerAnswer.replace(i,userSelection.get(i));
					//remove the value from tabColorToReinject if the color is found 
					if(tabColorToReinject.contains(userSelection.get(i))) {		// ici 
						if(!tabColorToReinject.isEmpty()) {
							tabColorToReinject.remove(userSelection.get(i));
							if (dev)
								//indicates what colour has been removed from the pool of reinjected colours as it is present in the combination  (only in -dev mode)  
								System.out.println(TextEnhencer.ANSI_RED + userSelection.get(i) + " has been found and therefore removed from tabtoReinject\n" + TextEnhencer.ANSI_RESET);
						}
					}
					
				// if value of element in tabComputerAnswer exist but not at the right place 	
				}else if(tabComputerAnswer.get(i).equals("Found4") && tabColorToReinject.size() > 0) {
					//select a random element amongst those that exist but not at the right place 
					int index2 = random.nextInt(tabColorToReinject.size()); // ICI !!!! 
					// put them in the next ComputerAnswer 
					tabComputerAnswer.replace(i,(tabColorToReinject.get(index2)));
					if(dev)
						//indicates what color from the pool of reinjected colours has been added to the new trial (only in -dev mode)  
						System.out.println(TextEnhencer.ANSI_RED + tabColorToReinject.get(index2) + " has been added to the combination" + TextEnhencer.ANSI_RESET);
					
				//if value does not exist replace by a random value 	
				}else {
						int index3 = random.nextInt(tabColorPool.size());  
						tabComputerAnswer.replace(i, tabColorPool.get(index3));
				}
			}
		}
	}
	
	private void compareAnswer() {
		String str = ""; 
		
		// add values to a String for display 
		for (int i = 0; i < tabComputerAnswer.size(); i++) {
			str += "|" + tabComputerAnswer.get(i) + "|";
		}
		
		correctPosition = 0; 
		int exist = 0;
		tabToCompare.putAll(userSelection);
				
		// check if any colour is at the same position in tabUserAnswer and tabToCompare 
		for (int i = 0; i < tabComputerAnswer.size(); i++) {
			//if element at the correct position 
			if(tabComputerAnswer.get(i).equals(tabToCompare.get(i))){
				//increase correctPosition 
				correctPosition++;
				// and replace the value in both tabs 
				tabToCompare.replace(i, "Found");
				tabComputerAnswer.replace(i, "Found2"); 
			}
		}
	
		// amongst the remaining values check if there is any match 
		for (int i = 0; i < tabComputerAnswer.size(); i++) {
			for (int j = 0; j < tabComputerAnswer.size(); j++) {
				// if tabUserAnswer contains at any position an element contained in tabCompare
				if(tabComputerAnswer.get(i).equals(tabToCompare.get(j))) {
					// replace the value in both tabs
					if(!tabColorToReinject.contains(tabComputerAnswer.get(i)))
						tabColorToReinject.add(tabComputerAnswer.get(i));
					tabToCompare.replace(j, "Found3");
					tabComputerAnswer.replace(i, "Found4");
					//increase exist 
					exist++;
				}
			}	
		}
		System.out.printf(TextEnhencer.ANSI_CYAN + "\nComputer tries %s ---> %d well placed, %d exist but not well placed.\n" + TextEnhencer.ANSI_RESET,str, correctPosition, exist);
		
		if(dev)
			// indicates the pool of colours to be reinjected for next trial  
			System.out.println(TextEnhencer.ANSI_RED + "\n" + tabColorToReinject + "Colors to be reinjected for a new combination\n" + TextEnhencer.ANSI_RESET);
	
	}
}

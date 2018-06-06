package com.philippe75.mastermind;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
import com.philippe75.game.Mode;

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
	
	public DefenderMastermind() {
		setProperties();	
	}
	
	public void startTheGame() {
		
		printWelcome();
		initiateColorChoice();
		generateQuestion();
		requestUserSecretCombi();
		initGame();
	}
	
	private void setProperties() {
		Properties p = new Properties();
		
		try {
			InputStream is = new FileInputStream("Ressources/dataConfig.properties");
			p.load(is);
			
			errorAllowed = Integer.parseInt(p.getProperty("errorAllowed"));
			combiLength = Integer.parseInt(p.getProperty("CombinationLength"));
			howManyColors = HowManyColors.valueOf((p.getProperty("ColorPool")));
			
		} catch (FileNotFoundException e) {
			System.out.println("The file specified does not exit.");
		} catch (IOException e) {
			System.out.println("Error with the propertiesFiles.");
		}
	}

	
	private void printWelcome() {
		String 	str  = "******************************************\n";
				str += "*******         WELCOME TO         *******\n";
				str += "*******      MASTERMIND GAME       *******\n";
				str += "*******       DEFENDER MODE        *******\n";	
				str += "******************************************";
		System.out.println(str); 
	}
	
	
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
		
		tabColorPool.addAll(tabColor.subList(0, howManyColors.getIntValue()));
	
	}
	
	private void generateQuestion() {
		int i = 0;
		String str =""; 
		for (String couleur : tabColorPool) {
			str += "["+ i +"]" + couleur +" ";
			i++;
		}
		
		System.out.println("\n" + str);
		System.out.println("\nPlease compose your secret color combination by choosing " + combiLength + " colors amongst the colors listed.");
	}
	
	private void requestUserSecretCombi() {
		
		userSelection = new HashMap<>(); 
		String str = ""; 
		do {
			//Using Regex to make sure user enter the right value type (Integer), as well as,  the correct length of this value type, and not higher range of selection offers.   
			if(userAnswer !="") {
				if(!userAnswer.matches("^[./[0-9]]+$")) { 
					System.out.println("Please enter a number instead of a characters.");					
				}else if (userAnswer.length() > combiLength || userAnswer.length() < combiLength){	
					System.out.println((userAnswer.length() > combiLength )? "The number of digits is superior to the number of digits required" 
							: "The number of digits is inferior to the number of digits required");
				}else if (!userAnswer.matches("^[./[0-"+(tabColorPool.size() -1) +"]]+$")) { 
					System.out.printf("Your selection has to be composed of number bewteen [0] and [%d]\n", (tabColorPool.size()-1));
				}
			}
			this.userAnswer = clavier.nextLine();
		} while ( userAnswer.length() > combiLength || userAnswer.length() < combiLength|| !userAnswer.matches("^[./[0-"+ (tabColorPool.size()-1)+"]]+$") || !userAnswer.matches("^[./[0-9]]+$") );
	
		// Convert String userAnswer digits into a Map userSelection with colours as value  
		for (int i = 0; i < userAnswer.length(); i++) {
			userSelection.put(i, tabColorPool.get(Character.getNumericValue(userAnswer.charAt(i))));
			
			str += "|" + userSelection.get(i) + "|";
		}
		System.out.println("You have selected the folwing secret color combination : \n     *** " + str + " ***\n");
	}
	
	private void initGame() {
		
		do {		
			// add a try after each question 
			score++;
			
			generateComputerAnswer();
			compareAnswer();
			
		} while (correctPosition != this.combiLength && score < this.errorAllowed);
		
		if(correctPosition == this.combiLength) {
			System.out.printf("\nYou loose !!! Computer found your secret color combination after %d " + ((score < 2)? "trial." : "trials.") + "\n", score);
		}else {
			System.out.printf("\nYou Win !!! Computer could not find your secret color combination after %d " + ((score < 2)? "trial." : "trials.") + "\n", score);
		}
		
	}
	
	// Find the correct way to handle the result and generate new combination 
	private void generateComputerAnswer() {
		
		Random random = new Random(); 
		int index = 0; 
		//First try 
		if(tabComputerAnswer.isEmpty()) {	
			for (int i = 0; i <userSelection.size(); i++) {
				index = random.nextInt(tabColorPool.size());
				tabComputerAnswer.put(i, tabColorPool.get(index));
			}
		// other tries	
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
							System.out.println(userSelection.get(i) + " has been found and therefore removed from tabtoReinject\n");
						}
					}
					
					
				// if value of element in tabComputerAnswer exist but not at the right place 	
				}else if(tabComputerAnswer.get(i).equals("Found4") && tabColorToReinject.size() > 0) {
					//select a random element amongst those that exist but not at the right place 
					int index2 = random.nextInt(tabColorToReinject.size()); // ICI !!!! 
					// put them in the next ComputerAnswer 
					tabComputerAnswer.replace(i,(tabColorToReinject.get(index2)));
					System.out.println(tabColorToReinject.get(index2) + " has been added to the combination");
					
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
		System.out.printf("\nComputer tries %s ---> %d well placed, %d exist but not well placed.\n",str, correctPosition, exist);
		System.out.println("\n" + tabColorToReinject + "Colors to be reinjected for a new combination");
	
	}
}

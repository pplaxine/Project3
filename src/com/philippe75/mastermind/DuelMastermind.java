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
import com.philippe75.game.Main;
import com.philippe75.game.Mode;
import com.philippe75.generators.SecretColorCombinationGenerator;

public class DuelMastermind implements Mode{
	private SecretColorCombinationGenerator sCG;
	private Map<Integer, String> colorPool = new HashMap<>(); 
	private Map<Integer, String> tabColCombi = new HashMap<>(); 
	private Map<Integer, String> tabUserAnswer = new HashMap<>();
	private Map<Integer, String> tabToCompare = new HashMap<>();
	private Map<Integer, String> userSelection;
	private Map<Integer, String> tabComputerAnswer = new HashMap<>();
	private Map<Integer, String> tabToCompare2 = new HashMap<>(); 
	private List<String> tabColor;
	private List<String> tabColorPool;
	private List<String> tabColorToReinject = new ArrayList<>();
	
	private int combiLength, correctPositionUser, correctPositionComputer; 
	private int score = 0;
	private String userAnswer=""; // if prob make userAnswer2 
	private String userSecretCombination;
	private HowManyColors howManyColors;							// à modifier  
	Scanner clavier = new Scanner(System.in);
	
	//Boolean from Main 
	private boolean dev = Main.isDev(); 
	
	public DuelMastermind() {
		setProperties();
	}
	
	public void startTheGame() {
		
		// generate a secret combination 
		sCG = new SecretColorCombinationGenerator(combiLength, howManyColors);
		printWelcome();
		initiateColorChoice();
		generateQuestion();
		requestUserSecretCombi();
		displaySecretColorCombi();
		printQuestion();
		initGame();
	}

	private void setProperties() {
		Properties p = new Properties();
		
		try {
			InputStream is = new FileInputStream("Ressources/dataConfig.properties");
			p.load(is);
		
			combiLength = Integer.parseInt(p.getProperty("CombinationLength"));
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
	
	private void printWelcome() {
		String 	str  = "******************************************\n";
				str += "*******         WELCOME TO         *******\n";
				str += "*******      MASTERMIND GAME       *******\n";
				str += "*******         DUEL MODE          *******\n";	
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
		
		tabColorPool.addAll(tabColor.subList(0, howManyColors.getIntValue()));	// à resoudre 
	
	}
	
	private void generateQuestion() {
		int i = 0;
		String str =""; 
		for (String couleur : tabColorPool) {
			str += "["+ i +"]" + couleur +" ";
			i++;
		}
		
		System.out.println("\n" + str);
		System.out.println("\nPlease compose your secret color combination to be guessed by choosing " + combiLength + " colors amongst the colors listed.");
	}
	
	private void requestUserSecretCombi() {
		
		userSelection = new HashMap<>(); 
		userSecretCombination = ""; 
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
			
			userSecretCombination += "|" + userSelection.get(i) + "|";
		}
		//reset user Anwser 
		userAnswer = ""; 
		System.out.println("You have selected the folwing secret color combination : \n     *** " + userSecretCombination + " ***\n");
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
		System.out.printf("You are the first to play!\n\nPlease enter a combination of %d choices amongst those colors :%s.\n", combiLength , str);
	}
	
	private void initGame() {
		// store the combination in tabColComni 
		tabColCombi = sCG.getTabColorCombination(); 
			
		do {		
				// add a try after each question 
				
				tabUserAnswer.clear();
				getUserAnswer();
				compareAnswerUser();
				if(correctPositionUser != this.combiLength) {
					generateComputerAnswer();
					compareAnswerComputer();
				}
				userAnswer = ""; 
				
			} while (correctPositionUser != this.combiLength && correctPositionComputer != this.combiLength );
		if(correctPositionComputer == this.combiLength) {
			System.out.printf("\n\t\t\t\t\t==== | GAME OVER ! ==== \n\nComputer found your secret combination first !!! The secret combination was %s\n", sCG.toString());
			
		}else {
			System.out.printf("\n\t.+*°*+.+> Congratulations you WIN !!! <+.+*°*+.\n\nYou have found the correct secret color combination first !!!");
		}
	}
	
	private void getUserAnswer() {
		 score++; 
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
			if(score>1)
				System.out.println("\nIt is your turn to play");
			// store the user answer in String but with digits
			this.userAnswer = clavier.nextLine();
				
		} while (!userAnswer.matches("^[./[0-9]]+$") || combiLength != userAnswer.length() 
				|| !userAnswer.matches("^[./[0-"+(sCG.getColorPool().size()-1) +"]]+$") );		
	}
	
	private void compareAnswerUser() {
		String str=""; 
		int index = 0;
		correctPositionUser = 0; 
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
				correctPositionUser++;
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
	
		System.out.printf("Your anwser is %s ---> %d well placed, %d exist but not well placed.\n",str, correctPositionUser, exist);
	
	}
	
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
							
						}
					}
					
					
				// if value of element in tabComputerAnswer exist but not at the right place 	
				}else if(tabComputerAnswer.get(i).equals("Found4") && tabColorToReinject.size() > 0) {
					//select a random element amongst those that exist but not at the right place 
					int index2 = random.nextInt(tabColorToReinject.size()); // ICI !!!! 
					// put them in the next ComputerAnswer 
					tabComputerAnswer.replace(i,(tabColorToReinject.get(index2)));
					
					
				//if value does not exist replace by a random value 	
				}else {
						int index3 = random.nextInt(tabColorPool.size());  
						tabComputerAnswer.replace(i, tabColorPool.get(index3));
				}
			}
		}
	}
	
	private void compareAnswerComputer() {
		String str = ""; 
		
		for (int i = 0; i < tabComputerAnswer.size(); i++) {
			str += "|" + tabComputerAnswer.get(i) + "|";
		}
		
		correctPositionComputer = 0; 
		int exist = 0;
		tabToCompare2.putAll(userSelection);
				
		// check if any colour is at the same position in tabUserAnswer and tabToCompare 
		for (int i = 0; i < tabComputerAnswer.size(); i++) {
			//if element at the correct position 
			if(tabComputerAnswer.get(i).equals(tabToCompare2.get(i))){
				//increase correctPosition 
				correctPositionComputer++;
				// and replace the value in both tabs 
				tabToCompare2.replace(i, "Found");
				tabComputerAnswer.replace(i, "Found2"); 
			}
		}
	
		// amongst the remaining values check if there is any match 
		for (int i = 0; i < tabComputerAnswer.size(); i++) {
			for (int j = 0; j < tabComputerAnswer.size(); j++) {
				// if tabUserAnswer contains at any position an element contained in tabCompare
				if(tabComputerAnswer.get(i).equals(tabToCompare2.get(j))) {
					// replace the value in both tabs
					if(!tabColorToReinject.contains(tabComputerAnswer.get(i)))
						tabColorToReinject.add(tabComputerAnswer.get(i));
					tabToCompare2.replace(j, "Found3");
					tabComputerAnswer.replace(i, "Found4");
					//increase exist 
					exist++;
				}
			}	
		}
		System.out.println("\nComputer has to find the following combination " + userSecretCombination);
		System.out.printf("Computer tries %s ---> %d well placed, %d exist but not well placed.\n\n",str, correctPositionComputer, exist);
	}
	
	

	

}

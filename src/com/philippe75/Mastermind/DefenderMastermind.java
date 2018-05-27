package com.philippe75.Mastermind;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.Scanner;

import com.philippe75.game.Main;

public class DefenderMastermind {
	
	private int combiLength, errorAllowed, score, wellPlaced, exist;
	private String userAnswer="";
	private HowManyColors howManyColors = HowManyColors.FOUR;  
	
	private HashMap<Integer, String> userSelection; 
	private ArrayList<String> tabColorPool;
	private ArrayList<String> tabColor; 
	private ArrayList<String> tabColorRemaining;
	private ArrayList<String> tabColorReInjected = new ArrayList<>();
	
	private HashMap<Integer, String> tabColortoKeep;
	private HashMap<Integer, String> ComputerAnswer = new HashMap<>(); 
	private boolean dev = Main.isDev(); 
	
	public DefenderMastermind() {
		setProperties();
		startTheGame();		
	}
	
	private void startTheGame() {
		
//		printWelcome();
		initiateColorChoice();
		generateQuestion();
		requestUserSecretCombi();
		generateAllPossibilities();
//		generateAnswer();
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

	errorAllowed = Integer.parseInt(p.getProperty("errorAllowed"));
	combiLength = Integer.parseInt(p.getProperty("CombinationLength"));
	if(p.getProperty("devMode").equals("activated"))
		this.dev = true; 
	}

	private void printWelcome() {
		String 	str  = "******************************************\n";
				str += "*******         WELCOME TO         *******\n";
				str += "*******      MASTERMIND GAME       *******\n";
				str += "*******       DEFENDER MODE        *******\n";	
				str += "******************************************";
		System.out.println(str); 
	}
	
	public void initiateColorChoice() {
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
		
		for (int i = 0; i < howManyColors.getIntValue(); i++) {
			tabColorPool.add(tabColor.get(i)); 
		}
	
	}
	
	public void generateQuestion() {
		int i = 0;
		String str =""; 
		for (String couleur : tabColorPool) {
			str += "["+ i +"]" + couleur +" ";
			i++;
		}
		
		System.out.println("\n" + str);
		System.out.println("\nPlease compose your secret color combination amongst the colors listed. Your combination can contain up to " + combiLength+ " ");
	}
	
	public void requestUserSecretCombi() {
		Scanner clavier = new Scanner(System.in);
		userSelection = new HashMap<>(); 
		String str = ""; 
		do {
			//Using Regex to make sure user enter the right value type (Integer), as well as,  the correct length of this value type, and not higher range of selection offers.   
			if(userAnswer !="") {
				if(!userAnswer.matches("^[./[0-9]]+$")) { 
					System.out.println("Please enter a number instead of a characters.");					
				}else if (userAnswer.length() > combiLength || userAnswer.length() < 4){	
					System.out.println((userAnswer.length() > combiLength )? "The number of digits is superior to the number of digits required" 
							: "The number of digits is inferior to the number of digits required");
				}else if (!userAnswer.matches("^[./[0-"+(tabColorPool.size() -1) +"]]+$")) { 
					System.out.printf("Your selection has to be composed of number bewteen [0] and [%d]\n", (tabColorPool.size()-1));
				}
			}
	
			this.userAnswer = clavier.nextLine();
			
		} while ( userAnswer.length() > combiLength || userAnswer.length() < 4 || !userAnswer.matches("^[./[0-"+ (tabColorPool.size()-1)+"]]+$") || !userAnswer.matches("^[./[0-9]]+$") );
	
		for (int i = 0; i < userAnswer.length(); i++) {
			userSelection.put(i, tabColorPool.get(Character.getNumericValue(userAnswer.charAt(i))));
			
			str += "|" + userSelection.get(i) + "|";
		}
		System.out.println("You have selected the folwing secret color combination : \n     *** " + str + " ***\n");
	}
	
	//----------------------------------------------------------------
	
	public void generateAnswer() {
		
		Random random = new Random(); 
		int k = 0; 
		
		do {
			String str = "";
			for (int i = 0; i < userSelection.size(); i++) {
				
				if(userSelection.get(i).equals("") ) {
					ComputerAnswer.replace(i, tabColortoKeep.get(i));
				}else if(!userSelection.get(i).equals("") && !tabColorReInjected.isEmpty()) {
					ComputerAnswer.replace(i, tabColorReInjected.get(i) );
				}else  {
					int index = random.nextInt(tabColorPool.size());
					ComputerAnswer.put( i, (tabColorPool.get(index)));						
				}
				
				str += "|" + ComputerAnswer.get(i) + "|";
			}
			System.out.print("Computer tries ............." + str + ".");
			generateHint();
			tabColorReInjected.clear();
			
			
			k++;
		} while (k<3);
		
		
	}
	
	public void generateHint() {
		int i = 0; 
		tabColortoKeep = new HashMap();
		tabColorRemaining = new ArrayList<>();
		
		for (String color : userSelection.values()) {
			tabColorRemaining.add(color); 
		}
		
		for (String color : userSelection.values()) {
			int j = 0;
			for (String b1 : ComputerAnswer.values()) {
				if(color.equals(b1) && i == j) {
						wellPlaced++;
						ComputerAnswer.replace(j, "Found");
						userSelection.replace(j, "");
						tabColortoKeep.replace(j, b1);
				}
				j++;
			}
			i++; 
		}
			
		for (String color : userSelection.values()) {
			int j = 0;
			for (String b1 : ComputerAnswer.values()) {
				if(color.equals(b1) ) {
						exist++;
	
						tabColorReInjected.add(b1);
				}
				j++;
			}
			i++; 
		}
		
		System.out.printf(" There is %d wellplace and %d exist\n" , wellPlaced,exist );
		
		System.out.println(tabColorReInjected);
	}
	
	public void generateAnswerAfterHint() {
		 
	}
	
	public void generateAllPossibilities() {
		String[][] tabAllPossibilities = new String[(int)Math.pow(tabColorPool.size(), combiLength)][combiLength]; 
		System.out.println(tabAllPossibilities.length);
		
	}

}

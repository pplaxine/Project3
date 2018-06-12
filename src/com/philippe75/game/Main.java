package com.philippe75.game;


import java.util.Scanner;

import com.philippe75.mastermind.Mastermind;
import com.philippe75.plus_minus.PlusMinus;

public class Main {
	
	private static boolean playagain = false; 
	private static boolean dev; 
	
	
	public static void main(String[] args) {
		
		dev = (args.length > 0 && args[0].equals("-dev"))? true : false; 
		runMenu();
	}
	
	
	public static void runMenu() {
		int userGameChoice = -1;
		Game cPM = new PlusMinus();
		Game cMM = new Mastermind();
	
		boolean arret = false; 
		printHeader();
		
		while (!arret) {
			
			printMainMenu();
			userGameChoice = getUserAnswer(3);
			switch (userGameChoice) {
			case 1:
			case 2:{	printModeMenu();	
						switch (getUserAnswer(4)) {
						case 1: if (userGameChoice == 1){
							do {
								cPM.startGame(GameMode.CHALLENGER);
								afterGameChoice();
							}while(playagain);
								
							
						break; 
						}else {
							do {
								cMM.startGame(GameMode.CHALLENGER);
								afterGameChoice();
							}while(playagain);
						}
								
						break;
						case 2: if (userGameChoice == 1){
							do {
								cPM.startGame(GameMode.DEFENDER);
								afterGameChoice();
							}while(playagain);
						}else {
							do {
								cMM.startGame(GameMode.DEFENDER);
								afterGameChoice();
							}while(playagain);
						}
							
						break;
						case 3: if (userGameChoice == 1){
							do {
								cPM.startGame(GameMode.DUEL);
								afterGameChoice();
							}while(playagain);
						}else {
							do {
								cMM.startGame(GameMode.DUEL);
								afterGameChoice();
							}while(playagain);
						}
							
						break;
						case 4: 
							
						break;
							}
			}
				break; 
			case 3: 
				System.exit(0);
				break;
			}
		}
	}  

   	
 	private static void printHeader() {
 		String 	str = "\n\n************************************************\n";  
 				str += "***************	    Welcome  to   **************\n";
 				str += "***************	    the GAME !!!  **************\n";
 				str += "***************	     	     	  **************\n";
 				str += "************************************************";
		System.out.println(TextEnhencer.ANSI_GREEN + str + TextEnhencer.ANSI_RESET);
 	}
	
 	
	private static void printMainMenu() {
		String 	menu = TextEnhencer.ANSI_PURPLE; 
				menu += "\nSelect your game : \n"; 
				menu +=	"\tPlusMoins : ......enter [1]\n";
				menu += "\tMastermind : .....enter [2]\n";
				menu += "\tQuit the game : ..enter [3]\n";
				menu += TextEnhencer.ANSI_RESET;
			
		System.out.println(menu);	
	}
	
	
	private static void printModeMenu(){
	   	String 	mode =TextEnhencer.ANSI_PURPLE;  
	   			mode += "Select your mode : \n"; 
	   			mode +=	"\tChallenger : .................enter [1]\n";
	   			mode +=	"\tDefenseur : ..................enter [2]\n";
	   			mode +=	"\tDuel : .......................enter [3]\n";
	   			mode +=	"\tBack to previous screen : ....enter [4]\n";
	   			mode += TextEnhencer.ANSI_RESET;
		System.out.println(mode);
	}
	
	
	private static int getUserAnswer(int numberofQuestion) {
		int userAnswer = -1; 
		Scanner clavier = new Scanner(System.in); 
		while (userAnswer > numberofQuestion || userAnswer < 0 ) {
			try {
				System.out.print(TextEnhencer.ANSI_PURPLE + "Enter your choice here below : \n" + TextEnhencer.ANSI_RESET);
				System.out.print(TextEnhencer.ANSI_YELLOW);
				userAnswer = Integer.parseInt(clavier.nextLine()); 
				System.out.print(TextEnhencer.ANSI_RESET);
			} catch (NumberFormatException e) {
				System.out.print(TextEnhencer.ANSI_RED + "Incorrect value. " + TextEnhencer.ANSI_RESET);
			}
		}
		return userAnswer;
	}
	
	private static void afterGameChoice() {
		playagain = false; 
		
		String 	menu = TextEnhencer.ANSI_GREEN; 
				menu += "\n_____________________________________________________________________\n";
				menu += "_____________________________________________________________________\n";
				menu += "_____________________________________________________________________\n";
				menu += "_____________________________________________________________________\n\n";
				menu +=  "\nWhat would you like to do now ? : \n"; 
				menu +=	"\tPlay again ? : ..................enter [1]\n";
				menu += "\tReturn to the main menu ? : .....enter [2]\n";
				menu += "\tQuit the game : .................enter [3]\n";
				menu += TextEnhencer.ANSI_RESET;
		System.out.println(menu);	
		int userDecision = getUserAnswer(3);
		
		if(userDecision == 1) {
			playagain = true; 
		}else if(userDecision == 2) {
			
		}else {
			System.exit(0);			
		}
	}

	
	public static boolean isDev() {
		return dev;
	}
}	
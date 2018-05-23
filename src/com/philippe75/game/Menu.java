package com.philippe75.game;

import java.lang.invoke.SwitchPoint;
import java.util.Scanner;

import javax.swing.JTable.PrintMode;

import com.philippe75.Mastermind.Mastermind;
import com.philippe75.PlusMoins.PlusMoins;

public class Menu {
	
	private int userGameChoice;
	private Boolean arret = false; 
	private Game plusMoins = new PlusMoins();
	private Game mastermind = new Mastermind(); 
	
	
	public void runMenu() {
		
		printHeader();
		
		while (!arret) {
			
			printMainMenu();
			userGameChoice = getUserAnswer(3);
			switch (userGameChoice) {
			case 1:
			case 2:{	printModeMenu();	
						switch (getUserAnswer(4)) {
						case 1: if (userGameChoice == 1){
							//plusMoins.startGame(GameMode.CHALLENGER);
							
						break; 
						}else {
							
						}
							
								
						break;
						case 2: if (userGameChoice == 1){
							
						}else {
							
						}
							
						break;
						case 3: if (userGameChoice == 1){
							
						}else {
							
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
//	   	System.out.println(str);
//	   	Scanner clavier2 = new Scanner(System.in); 
//	   	
//	   	do { 
//	   		
//	   		System.out.println(menu);
//	   		
//	   		userGameChoice = clavier2.nextLine();
//	   		switch (userGameChoice) {
//				case "1" :
//				case "2" :{ 
//					System.out.println(mode);
//					userModeChoice = clavier2.nextLine();
//					switch (userModeChoice) {
//						case "1" : if(userGameChoice.equals("1")) {
//							do {
//								plusMoins.startGame(GameMode.CHALLENGER);
//								playAgainMenu();
//								if(playAgainMenu() ==3 )
//									System.exit(0);
//							}while (playAgainMenu()== 1); 
//							
//							break;
//						}else {
//							mastermind.startGame(GameMode.CHALLENGER);
//							break;
//							
//						}	
//						case "2" :if(userGameChoice == "1") {
//							plusMoins.startGame(GameMode.DEFENSEUR);
//							break;
//						}else {
//							mastermind.startGame(GameMode.DEFENSEUR);
//						}
//							break;
//							
//						case "3" : if(userGameChoice == "1") {
//							plusMoins.startGame(GameMode.DUEL);
//						}else {
//							mastermind.startGame(GameMode.DUEL);
//						}	
//							break;
//						case "4" : 			
//							break; 
//								
//						}
//							
//					};
//					break;
//				case "3" : 
//					break; 
//			}
//	   		
//	   	}while(!arret); 
//	   
//	   	clavier2.close();	
//	   	
//	   	System.out.println("bye bye !");	
//	   				
//	}
	

 	public int playAgainMenu(){
 		
 		Scanner clavier = new Scanner(System.in);  
 		int userChoice;  
 		
 		String 	menu2 =  "\nWhat do you want to do next ? : \n"; 
				menu2 += "\tPlay again ? : .............enter [1]\n";
				menu2 += "\tSelect another game? : .....enter [2]\n";
				menu2 += "\tQuit the game : ............enter [3]";
				menu2 += "\nEnter your choice here below : ";
				
	
			System.out.println(menu2);
			userChoice = clavier.nextInt();
			return userChoice; 		
   	}
   	
 	public void printHeader() {
 		String 	str = "************************************************\n";  
		str += "***************	    Welcome  to   **************\n";
		str += "***************	    the GAME !!!  **************\n";
		str += "***************	     	     	  **************\n";
		str += "************************************************";
		System.out.println(str);
 	}
		 
	public void printMainMenu() {
		String 	menu =  "\nSelect your game : \n"; 
			menu +=	"\tPlusMoins : ......enter [1]\n";
			menu += "\tMastermind : .....enter [2]\n";
			menu += "\tQuit the game : ..enter [3]\n";
			
		System.out.println(menu);	
	}
	
	public void printModeMenu(){
	   	String 	mode =  "Select your mode : \n"; 
			mode +=	"\tChallenger : .................enter [1]\n";
			mode +=	"\tDefenseur : ..................enter [2]\n";
			mode +=	"\tDuel : .......................enter [3]\n";
			mode +=	"\tBack to previous screen : ....enter [4]\n";
		System.out.println(mode);
	}
	
	public int getUserAnswer(int numberofQuestion) {
		
		int userAnswer = -1; 
		Scanner clavier = new Scanner(System.in); 
		
		while (userAnswer > numberofQuestion || userAnswer < 0 ) {
			try {
				System.out.print("Enter your choice here below : \n");
				userAnswer = Integer.parseInt(clavier.nextLine()); 
			} catch (NumberFormatException e) {
				System.out.print("Incorrect value. ");
			}
		}
		return userAnswer;
	}
	
	
	
	
}

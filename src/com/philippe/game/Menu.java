package com.philippe.game;

import java.util.Scanner;

import com.philippe75.Mastermind.Mastermind;
import com.philippe75.PlusMoins.PlusMoins;

public class Menu {
	
	
	
	public static void main(String[] args) {
		String userGameChoice, userModeChoice, userNextChoice;
		Boolean arret = false; 
		Game plusMoins = new PlusMoins();
		Game mastermind = new Mastermind(); 
		
		String 	str = "************************************************\n";  
		str += "***************	    Welcome  to   **************\n";
		str += "***************	    the GAME !!!  **************\n";
		str += "***************	     	     	  **************\n";
		str += "************************************************";
		
	   			
	   	String 	menu =  "\nSelect your game : \n"; 
	   			menu +=	"\tPlusMoins : ......enter [1]\n";
	   			menu += "\tMastermind : .....enter [2]\n";
	   			menu += "\tQuit the game : ..enter [3]";
	   			menu += "\nEnter your choice here below : ";
	   			
	   	String 	mode =  "Select your mode : \n"; 
	   			mode +=	"\tChallenger : .................enter [1]\n";
	   			mode +=	"\tDefenseur : ..................enter [2]\n";
	   			mode +=	"\tDuel : .......................enter [3]\n";
	   			mode +=	"\tBack to previous screen : ....enter [4]\n";
	   			mode += "\nEnter your choice here below : ";
	   			
	   	String 	playAgain =  	"\n Play Again ? : \n"; 		
	   			playAgain +=	"\tYes : ................................enter [1]\n";
	   			playAgain +=	"\tI whish to play to another game : ....enter [2]\n";
	   			playAgain +=	"\tQI want to quit : ....................enter [3]\n";
	   			
	   	
	   	System.out.println(str);
	   	Scanner clavier2 = new Scanner(System.in); 
	   	
	   	do { 
	   		
	   		System.out.println(menu);
	   		
	   		userGameChoice = clavier2.nextLine();
	   		switch (userGameChoice) {
				case "1" :
				case "2" :{ 
					System.out.println(mode);
					userModeChoice = clavier2.nextLine();
					switch (userModeChoice) {
						case "1" : if(userGameChoice.equals("1")) {
							plusMoins.startGame(GameMode.CHALLENGER);
							
							break;
						}else {
							mastermind.startGame(GameMode.CHALLENGER);
							break;
							
						}	
						case "2" :if(userGameChoice == "1") {
							plusMoins.startGame(GameMode.DEFENSEUR);
							break;
						}else {
							mastermind.startGame(GameMode.DEFENSEUR);
						}
							break;
							
						case "3" : if(userGameChoice == "1") {
							plusMoins.startGame(GameMode.DUEL);
						}else {
							mastermind.startGame(GameMode.DUEL);
						}	
							break;
						case "4" : 			
							break; 
								
						}
							
					};
					break;
				case "3" : System.exit(0);
					break; 
			}
	   		
	   	}while(!arret); 
	   
	   	clavier2.close();	
	   	
	   		
	   			
	 
		
//		
//		System.out.println();
//		
//		if (userGameChoice == 1 || userGameChoice == 2) {
//			System.out.println(mode);
//			userModeChoice = clavier.nextInt(); 
//			if (userModeChoice == 4) {
//				System.out.println(menu);
//			}
//			
//		}else {
//			System.out.println("Bye bye. See you next time !");
//			
//		}
//		
//		System.out.println("Blabla");
		
	}
	

		   		
		 
	
	
	
	
}

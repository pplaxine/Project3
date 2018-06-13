package com.philippe75.game;

/**
 * This interface constrains all the class that implements it to define a startTheGame() method, as well as, a setProperties method. 
 * 
 * A method displayFish is reachable to all the class implementing this interface.It display a drawing of a fish in String format.     
 * 
 * @author PPlaxine
 * @version 1.0
 */
public interface Mode {
	
	public void startTheGame();
	
	public boolean setProperties();
	
	public void printWelcome();
	
	public default void displayFish() {
		
		String 	str = TextEnhencer.ANSI_GREEN;  
				str +="\t.....................................(_).......\n";
				str +="\t...............................................\n";	
				str +="\t................|L../|................_........\n";
				str +="\t............_...|\\ _| \\--+._/|..........(_)....\n";
				str +="\t.........../ ||\\| Y J  )   / |/| ./............\n";
				str +="\t..........J  |)'( |        ` F`.'/........_....\n";
				str +="\t........-<|  F         __     .-<........(_)...\n";
				str +="\t..........| /       .-'. `.  /-. L___..........\n";
				str +="\t..........J \\      <    \\  | | O\\|.-'.._.......\n";
				str +="\t.........J \\  .-    \\/ O | | \\  |F....(_)......\n";
				str +="\t.......'-F  -<_.     \\   .-'  `-' L__..........\n";
				str +="\t......__J  _   _.     >-'  )._.   |-'..........\n";
				str +="\t......`-|.'   /_.           \\_|   F............\n";
				str +="\t......../.-   .                _.<.............\n";
				str +="\t......./'    /.'             .'  `\\............\n";
				str +="\t......./L  /'   |/      _.-'-\\.................\n";
				str +="\t......./'J       ___.---'\\|....................\n";
				str +="\t.........|\\  .--' V  | `. `....................\n";
				str +="\t.........|/`. `-.     `._).....................\n";
				str +="\t............/ .-.\\.............................\n";
				str +="\t............\\ (  `\\............................\n";
				str +="\t.............`.\\...............................\n";
				str+= TextEnhencer.ANSI_RESET;
		System.out.println("\n" + str);
	}
}



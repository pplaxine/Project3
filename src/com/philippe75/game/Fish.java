package com.philippe75.game;

public class Fish {

	public static void displayFish() {
		
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
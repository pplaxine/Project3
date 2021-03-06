package com.philippe75.mastermind;

import java.util.HashMap;

import org.apache.log4j.Logger;

import com.philippe75.extra.Dino;
import com.philippe75.extra.Fish;
import com.philippe75.extra.TextEnhencer;
import com.philippe75.game.Game;
import com.philippe75.generators.SecretColorCombinationGenerator;

/**
 * <b>DuelMastermind is a class that handle the Mastermind game in duel mode.</b>
 * <p>Steps of the game : 
 * <ul>
 * <li>A colour pool is created and the choices are displayed to the users.</li>
 * <li>User creates a colour combination, to be guessed by the computer, amongst those colours.</li>
 * <li>A random colour combination for user to be guessed is generated.</li>
 * <li>User tries a combination.</li>
 * <li>for each answer an hint is return. This hint indicates, how many colours are well placed and how many are present but not well placed in the combination.</li>
 * <li>Computer tries a combination.</li>
 * <li>An hint is also generated</li>
 * <li>Each turn, user and computer try a combination.</li>
 * <li>If user finds the secret combination first he wins, if computer finds first, user looses.</li>
 * </ul>
 * </p>
 * 
 * <p>
 * The random colour combination is generated via a SecretColoCombinationGenerator. 
 * </p>
 * 
 * <p>
 * The secret combination length and the pool of colours can be set in a DataConfig.properties file.
 * </p>
 * 
 * @see SecretColorCombinationGenerator
 * @see Game#setProperties()
 * 
 * @author PPlaxine
 * @version 1.0
 */
public class DuelMastermind extends Mastermind{

	/**
	 * Creates a logger to generate log of the class.	
	 */
	private static final Logger log = Logger.getLogger(DuelMastermind.class);
	
	/**
	 * Constructor of DuelMastermind.
	 * 
	 * When the class is instantiated, load properties to be used by the game.
	 * 
	 * @see Game#setProperties()
	 * @see Game#howManyColors
	 * @see Game#combiLength
	 * @see Game#dev
	 */
	public DuelMastermind() {
		if(setProperties())
			startTheGame();
	}
	
	/**
	 * Starts the game.  
	 * 
	 * A welcome screen is displayed.
	 * 
	 * Displays a request for user to make an entry. 
	 * 
	 * Initiate the game.
	 *  
	 * @see DuelMastermind#printWelcome()
	 * @see Mastermind#initiateColorChoice()
	 * @see Mastermind#generateQuestion()
	 * @see Mastermind#requestUserSecretCombi()
	 * @see Mastermind#displaySecretColorCombi()
	 * @see DuelMastermind#initGame()
	 */
	@Override
	public void startTheGame() {
		log.info("Start of Mastermind game in duel mode");
		if(dev)
			log.info("Game is running in developer mode");
		sCG = new SecretColorCombinationGenerator(super.combiLength, super.howManyColors);
		printWelcome();
		initiateColorChoice();
		generateQuestion();
		requestUserSecretCombi();
		displaySecretColorCombi();
		initGame();
		log.info("End of the game");
	}
	
	/**
	 * Display the welcome screen.
	 * 
	 * @see DuelMastermind#startTheGame()
	 */
	@Override
	public void printWelcome() {
		String 	str = TextEnhencer.ANSI_YELLOW;
				str += "\n\n******************************************\n";
				str += "*******         WELCOME TO         *******\n";
				str += "*******      MASTERMIND GAME       *******\n";
				str += "*******         DUEL MODE          *******\n";	
				str += "******************************************";
				str += TextEnhencer.ANSI_RESET;
		System.out.println(str); 
	}

	/**
	 * Initiate the game. 
	 * 
	 * Get the user answer. 
	 * 
	 * Compare the user answer. 
	 * 
	 * Get the computer answer. 
	 * 
	 * Compare the computer answer. 
	 * 
	 * All those steps are repeated as long as the secret combination is not found. 
	 * 
	 * If user finds the secret combination first, he Wins. If computer finds the secret combination first, user looses.     
	 * 
	 * @see Mastermind#getUserAnswer()
	 * @see Mastermind#compareAnswerUser()
	 * @see Mastermind#generateComputerAnswer()
	 * @see Mastermind#compareAnswerComputer()
	 */
	public void initGame() {
		// store the combination in tabColComni.
		this.tabColCombi = new HashMap<>(); 
		this.tabColCombi = sCG.getTabColorCombination(); 
		super.tries = 0; 
		
		do {		
			super.tries++;
			if(super.tries < 2) 	
				System.out.println(TextEnhencer.ANSI_YELLOW + "\nYour are the first to play!");
			else
				System.out.println(TextEnhencer.ANSI_YELLOW + "\nIt's your turn to play ...");	
			
			printQuestion();
			getUserAnswer();
			compareUserAnswer();
			if(correctPositionUser != this.combiLength) {
				generateComputerAnswer();
				compareComputerAnswer();
			}
				
		} while (correctPositionUser != this.combiLength && correctPositionComputer != this.combiLength );
		
		//if computer finds the answer first, user looses. 
		if(correctPositionComputer == this.combiLength) {
			//Strategy Pattern 
			this.setEndOfGameDisplay(new Dino());
			this.displayEndGamePic();
			System.out.printf(TextEnhencer.ANSI_RED +  "\n\t\t   .+*�*+..+> | GAME OVER !!! | <.+.+*�*+." + TextEnhencer.ANSI_CYAN + "\n\nComputer found your secret combination first !!! The secret combination was %s\n" + TextEnhencer.ANSI_RESET, sCG.toString());
		//if user finds the answer first, he wins.	
		}else {
			//Strategy Pattern 
			this.setEndOfGameDisplay(new Fish());
			this.displayEndGamePic();
			System.out.printf(TextEnhencer.ANSI_YELLOW + "\n\t.+*�*+.+> | Congratulations you WIN !!! | <+.+*�*+.\n\nYou have found the correct secret color combination first !!!" + TextEnhencer.ANSI_RESET);
		}
	}
}

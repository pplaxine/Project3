package com.philippe75.mastermind;

import org.apache.log4j.Logger;

import com.philippe75.extra.Dino;
import com.philippe75.extra.Fish;
import com.philippe75.extra.TextEnhencer;
import com.philippe75.game.IGame;
import com.philippe75.generators.SecretColorCombinationGenerator;

/**
 * <b>DefenderMastermind is a class that handle the Mastermind game in Defender IGame.</b>
 * <p>Steps of the game : 
 * <ul>
 * <li>A colour pool is created and the choices are displayed to the users.</li>
 * <li>User creates a colour combination, to be guessed by the computer, amongst those colours.</li>
 * <li>Computer tries a combination.</li>
 * <li>for each answer an hint is return. This hint indicates, how many colours are well placed and how many are present but not well placed in the combination.</li>
 * <li>Computer tries combinations until it finds the answer or the number of tries permitted is reached.</li>
 * <li>If Computer finds the secret combination, user looses, otherwise user wins.</li>
 * </ul>
 * </p>
 * 
 * <p>
 * The random colour combination is generated via a SecretColoCombinationGenerator. 
 * </p>
 * 
 * <p>
 * The secret combination length, the pool of colours, and the number of errors allowed can be set in a DataConfig.properties file.
 * </p>
 * 
 * @see SecretColorCombinationGenerator
 * @see DefenderMastermind#setProperties()
 * 
 * @author PPlaxine
 * @version 1.0
 */
public class DefenderMastermind extends Mastermind{

	/**
	 * Creates a logger to generate log of the class.	
	 */
	private static final Logger log = Logger.getLogger(DefenderMastermind.class);
	
	/**
	 * Constructor of DenfenderMastermind.
	 * 
	 * When the class is instantiated, load properties to be used by the game.
	 * 
	 * @see DefenderMastermind#setProperties()
	 * @see DefenderMastermind#howManyColors
	 * @see DefenderMastermind#combiLength
	 * @see DefenderMastermind#errorAllowed
	 * @see DefenderMastermind#dev
	 */
	public DefenderMastermind() {
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
	 * @see DefenderMastermind#printWelcome()
	 * @see DefenderMastermind#initiateColorChoice()
	 * @see DefenderMastermind#generateQuestion()
	 * @see DefenderMastermind#requestUserSecretCombi()
	 * @see DenfenderMastermind#initGame()
	 */
	@Override
	public void startTheGame() {
		log.info("Start of Mastermind game in defender mode");
		if(dev)
			log.info("Game is running in developer mode");
		printWelcome();
		initiateColorChoice();
		generateQuestion();
		requestUserSecretCombi();
		initGame();
		log.info("End of the game");
	}

	/**
	 * Display the welcome screen.
	 * 
	 * @see DefenderMastermind#startTheGame()
	 */
	@Override
	public void printWelcome() {
		String 	str = TextEnhencer.ANSI_YELLOW; 
				str += "\n\n******************************************\n";
				str += "*******         WELCOME TO         *******\n";
				str += "*******      MASTERMIND GAME       *******\n";
				str += "*******       DEFENDER MODE        *******\n";	
				str += "******************************************";
				str += TextEnhencer.ANSI_RESET;
		System.out.println(str); 
	}

	/**
	 * Initiate the game. 
	 * 
	 * Increases the number of tries of the computer.
	 * 
	 * Get the computer answer. 
	 * 
	 * Compare the computer answer. 
	 * 
	 * All those steps are repeated as long as the secret combination is not found or if the value of error allowed isn't reached by score value. 
	 * 
	 * If computer can't find the secret combination, user Wins. Otherwise, user looses.     
	 * 
	 * @see DefenderMastermind#tries
	 * @see DefenderMastermind#generateComputerAnswer()
	 * @see DefenderMastermind#compareAnswer()
	 * @see ChallengerMastermind#errorAllowed
	 * @see IGame#displayFish()
	 */
	public void initGame() {
		super.tries = 0; 
		
		do {		
			tries++;
			generateComputerAnswer();
			compareComputerAnswer();
		} while (correctPositionComputer != this.combiLength && tries < this.errorAllowed);
		
		// if the answer isn't found user looses 
		if(correctPositionComputer == this.combiLength) {
			//Strategy Pattern 
			this.setEndOfGameDisplay(new Dino());
			this.displayEndGamePic();
			System.out.printf(TextEnhencer.ANSI_RED + "\n\t   .+*°*+.+> | GAME OVER !!! | <+..+*°*+."+ TextEnhencer.ANSI_CYAN + "\nComputer found your secret color combination after %d " + ((tries < 2)? "trial." : "trials.") + "\n" + TextEnhencer.ANSI_RESET, tries);
		// if the answer is found user wins	
		}else {
			//Strategy Pattern 
			this.setEndOfGameDisplay(new Fish());
			this.displayEndGamePic();
			System.out.printf(TextEnhencer.ANSI_YELLOW + "\n\t     .+*°*+.+> | You Win !!! | <+..+*°*+.\nComputer could not find your secret color combination after %d " + ((tries < 2)? "trial." : "trials.") + "\n" + TextEnhencer.ANSI_RESET, tries);
		}
	}
}

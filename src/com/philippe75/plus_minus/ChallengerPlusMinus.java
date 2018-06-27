package com.philippe75.plus_minus;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.philippe75.extra.Dino;
import com.philippe75.extra.Fish;
import com.philippe75.extra.TextEnhencer;
import com.philippe75.game.IGame;
import com.philippe75.generators.SecretNumGenerator;


/**
 * <b>ChallengerPlusMinus is a class that handle the PlusMinus game in Challenger IGame.</b>
 * <p>Steps of the game : 
 * <ul>
 * <li>A random combination is generated.</li>
 * <li>User tries a combination.</li>
 * <li>for each answer an hint is return. This hint indicates for each digit of the user answer if the secret combination digit is higher or lower.</li>
 * <li>User tries combinations until he finds the answer or the number of tries permitted is reached.</li>
 * <li>If user finds the secret combination he wins, otherwise he looses.</li>
 * </ul>
 * </p>
 * 
 * <p>
 * The random combination is generate via a secret combination generator. 
 * </p>
 * 
 * <p>
 * The secret combination length and the number of errors allowed can be set in a DataConfig.properties file.
 * </p>
 * 
 * @see SecretNumGenerator
 * @see ChallengerPlusMinus#setProperties()
 * 
 * @author PPlaxine
 * @version 1.0
 */
public class ChallengerPlusMinus extends PlusMinus{

	/**
	 * Creates a logger to generate log of the class.	
	 */
	private static final Logger log = Logger.getLogger(ChallengerPlusMinus.class);
	
	/**
	 * Constructor of ChallengerPlusMinus.
	 * 
	 * When the class is instantiated, load properties to be used by the game.
	 * 
	 * @see ChallengerPlusMinus#setProperties()
	 * @see ChallengerPlusMinus#combiLength
	 * @see ChallengerPlusMinus#errorAllowed
	 * @see ChallengerPlusMinus#dev
	 */
	public ChallengerPlusMinus() {	
		if (this.setProperties())
			startTheGame();
	}
	
	/**
	 * Starts the game.  
	 * 
	 * A new secret combination is generated by instancing the secret combination generator.
	 * 
	 * A welcome screen is displayed.
	 * 
	 * The secret combination is displayed if the developer mode is activated.
	 * 
	 * Displays a request for user to make an entry. 
	 * 
	 * Initiate the game. 
	 * 
	 * @see SecretNumGenerator
	 * @see ChallengerPlusMinus#printWelcome()
	 * @see ChallengerPlusMinus#displaySecretNum()
	 * @see ChallengerPlusMinus#initGame()
	 */
	@Override
	public void startTheGame() {
		log.info("Start of PlusMinus game in challenger mode");
		this.sNG = new SecretNumGenerator(this.combiLength);
		printWelcome();	
		displaySecretNum(); 
		initGame();
		log.info("End of the game");
	}
	
	/**
	 * Display the welcome screen.
	 * 
	 * @see ChallengerPlusMinus#startTheGame()
	 */
	@Override
	public void printWelcome() {
		String 	str = TextEnhencer.ANSI_YELLOW; 
				str += "******************************************\n";
				str += "*******        WELCOME TO          *******\n";
				str += "*******        + or - GAME         *******\n";
				str += "*******      CHALLENGER MODE       *******\n";	
				str += "******************************************";
				str += TextEnhencer.ANSI_RESET;
		System.out.println(str); 
	}

	/**
	 * Displays the secret combination if mode dev is activated
	 *  
	 * @see ChallengerPlusMinus#dev
	 * @see ChallengerPlusMinus#startTheGame()
	 */
	protected void displaySecretNum() {
		System.out.println(TextEnhencer.ANSI_CYAN+ "\nComputer has generated a secret combination for you to guess ..." + TextEnhencer.ANSI_RESET);
		if(dev) {
			log.info("Game is running in developer mode");
			System.out.println(TextEnhencer.ANSI_GREEN + "\t*** Secret combination : " + this.sNG.getRandomNumber() + " ***\n" + TextEnhencer.ANSI_RESET);
		}
	}
	
	/**
	 * Initiate the game. 
	 * 
	 * Increases the number of tries of the user.
	 * 
	 * Store, after checking if entry is correct, users answer. The Entry is in form of chain of numbers.
	 * 
	 * The content of userAnswer is transfered to tabUserAnswer ArrayList in order to make a comparison with the secret combination contained also in a List. 
	 * 
	 * The result in form of a chain of indications "+" or "-" is stored in hint. hint is then displayed to the user.
	 * 
	 * All those steps are repeated as long as the secret combination is not found or if the value of error allowed isn't reached by score value. 
	 * 
	 * If user find the secret combination, he wins. A drawing and a message are displayed. Otherwise, user loose. a message is displayed.     
	 * 
	 * @see ChallengerPlusMinus#score
	 * @see ChallengerPlusMinus#userAnswer
	 * @see ChallengerPlusMinus#tabUserAnswer
	 * @see ChallengerPlusMinus#generateHint(List, List)
	 * @param sNG.getTabNumber()
	 * 								The secret combination contained in a List  
	 * @param tabUserAnswer 
	 * 								The user answer contained in a List
	 * @see ChallengerPlusMinus#hint
	 * @see ChallengerPlusMinus#errorAllowed
	 * @see IGame#displayFish()
	 */
	public void initGame() {
		this.tabUserAnswer = new ArrayList<Integer>();
		
		// Repeat the question while user has enough tries left and hasn't found the answer
		do {
			// add a try after each question 
			tries++;
			this.userAnswer = requestUserAnswer();		
			this.generateUserHint();
	
		} while (!this.sNG.getTabNumber().toString().equals(this.tabUserAnswer.toString()) && tries < this.errorAllowed);
		
		// Print Result once the game is over. 
		if (this.sNG.getTabNumber().toString().equals(this.tabUserAnswer.toString())){
			//Strategy Pattern 
			this.setEndOfGameDisplay(new Fish());
			this.displayEndGamePic();
			System.out.printf(TextEnhencer.ANSI_YELLOW + "\t   .+*�*+.+> | Congratulation !!! | <+.+*�*+.\n\t   You found the answer after %d trials!!! \n"+ TextEnhencer.ANSI_RESET, tries);
		}else {
			//Strategy Pattern 
			this.setEndOfGameDisplay(new Dino());
			this.displayEndGamePic();
			System.out.printf(TextEnhencer.ANSI_RED + "\t   .+*�*+.+> | GAME OVER !!!! | <+.+*�*+.\n"+ TextEnhencer.ANSI_CYAN +  "\t\t The secret number was %s \n", this.sNG.getRandomNumber() + TextEnhencer.ANSI_RESET);
		}
	}
}

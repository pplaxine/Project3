package com.philippe75.plus_minus;

import org.apache.log4j.Logger;

import com.philippe75.extra.Dino;
import com.philippe75.extra.Fish;
import com.philippe75.extra.TextEnhencer;
import com.philippe75.game.Game;

/**
 * <b>DefenderPlusMinus is a class that handle the PlusMinus game in defender mode.</b>
 * <p>Steps of the game :
 * <ul>
 * <li>User enter a combination.</li>
 * <li>Computer tries a combination.</li>
 * <li>for each answer an hint is return. This hint indicates for each digit of the computer answer if the secret combination digit is higher or lower.</li>
 * <li>Computer tries combinations until he finds the answer or the number of tries permitted is reached.</li>
 * <li>If computer finds the secret combination user looses, otherwise he wins.</li>
 * </ul>
 * </p>
 * 
 * <p>
 * The number of errors allowed can be set in a DataConfig.properties file.
 * </p>
 * 
 * <p>
 * The length of the combination to find is dynamically adapted to the length of the user entry. 
 * </p>
 * 
 * @see DefenderPlusMinus#setProperties()
 * @see DefenderPlusMinus#generateComputerFirstTry()
 * 
 * @author PPlaxine
 * @version 1.0
 *
 */
public class DefenderPlusMinus extends PlusMinus{

	/**
	 * Creates a logger to generate log of the class.	
	 */
	private static final Logger log = Logger.getLogger(DefenderPlusMinus.class);
	
	/**
	 * Constructor of DefenderPlusMinus.
	 * 
	 * When the class is instantiated, load properties to be used by the game.
	 * 
	 * @see Game#setProperties()
	 * @see PlusMinus#errorAllowed
	 */
	public DefenderPlusMinus () {
		if(this.setProperties())
			startTheGame();
	}
	
	/**
	 * Starts the game.  
	 * 
	 * A welcome screen is displayed.
	 * 
	 * The secret combination is displayed if the developer mode is activated.
	 * 
	 * Displays a request for user to make an entry. 
	 * 
	 * Initiate the game. 
	 * 
	 * @see DenferPlusMinus#printWelcome()
	 * @see DefenderPlusMinus#requestUserSecretNum()
	 * @see DefenderPlusMinus#initGame()
	 */
	@Override
	public void startTheGame() {
		log.info("Start of PlusMinus game in defender mode");
		printWelcome();
		requestUserSecretNum();
		initGame();
		log.info("End of the game");
	}
	
	/**
	 * Display the welcome screen.
	 * 
	 * @see DefenderPlusMinus#startTheGame()
	 */ 
	@Override
	public void printWelcome() {
		String 	str = TextEnhencer.ANSI_YELLOW;
				str += "******************************************\n";
				str += "*******        WELCOME TO          *******\n";
				str += "*******        + or - GAME         *******\n";
				str += "*******       DEFENDER MODE        *******\n";	
				str += "******************************************\n";
				str += TextEnhencer.ANSI_RESET;
		System.out.println(str); 
	}
	
	/**
	 * Initiate the game. 
	 * 
	 * Increases the number of tries of the user.
	 * 
	 * Generates a first random answer, then answer taking in consideration the hint.  
	 * 
	 * Computer answer is compared to users entry.
	 * 
	 * The result in form of a chain of indications "+" or "-" is stored in hint. 
	 * 
	 * Computer answer is transformed in String format.
	 * 
	 * Computer answer and hint are then displayed to the user.
	 * 
	 * All those steps are repeated as long as the secret combination is not found or if the value of error allowed isn't reached by score value. 
	 * 
	 * If user find the secret combination, he wins. A drawing and a message are displayed. Otherwise, user loose. a message is displayed.     
	 * 
	 * @see PlusMinus#generateComputerAnswer()
	 * @see PlusMinus#generateAnswerWithHint()
	 */
	public void initGame() {
		super.tries = 0; 
		
		// Repeat the question while Computer has enough tries left and hasn't found the answer
		do {
			super.tries++;
			generateComputerAnswer();
			generateComputerHint();
		} while (!tabComputerAnswer.toString().equals(tabUserCode.toString()) && super.tries < this.errorAllowed);
		
		// Print Result once the game is over.
		if(tabComputerAnswer.toString().equals(tabUserCode.toString())) {
			//Strategy Pattern 
			this.setEndOfGameDisplay(new Dino());
			this.displayEndGamePic();
			System.out.printf(TextEnhencer.ANSI_RED + "\n\t   .+*°*+.+> | GAME OVER !!! | <+.+*°*+.\n" + TextEnhencer.ANSI_CYAN + "You loose! Computer found your code after %d attempts.\n" + TextEnhencer.ANSI_RESET, super.tries );	
		}else {
			//Strategy Pattern 
			this.setEndOfGameDisplay(new Fish());
			this.displayEndGamePic();
			System.out.printf(TextEnhencer.ANSI_YELLOW + "\n\t   .+*°*+.+> | Congratulation ! | <+.+*°*+.\n\t Computer couldn't find your code after %d attempts" + TextEnhencer.ANSI_RESET, super.tries );	
		}
	}
}

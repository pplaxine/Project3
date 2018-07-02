package com.philippe75.extra;

/**
 * <b>This class displays picture in String format.</b>
 * 
 * @author PPlaxine
 * @version 1.0
 */
public class Dino implements EndOfGameDisplay{
	
	/**
	 * Displays an hungry Dinosaur picture.
	 */
	@Override
	public void display() {
		
		String 	str = TextEnhencer.ANSI_RED;  
				str +="\t................." + TextEnhencer.ANSI_GREEN +",^.__.>--\"~~'_.--~_)~^"+ TextEnhencer.ANSI_RED +".............\n";  
				str +="\t..............." + TextEnhencer.ANSI_GREEN +"v_L^~   ~    (~ _.-~ \\. |\\"+ TextEnhencer.ANSI_RED +"...........\n";     
				str +="\t............." + TextEnhencer.ANSI_GREEN +",-~    __    __,^\"/\\_A_/ /' \\"+ TextEnhencer.ANSI_RED +"..........\n"; 
				str +="\t..........." + TextEnhencer.ANSI_GREEN +"_/    ,-\"  \"~~\" __) \\  ~_,^   /\\"+ TextEnhencer.ANSI_RED +".........\n";  
				str +="\t.........." + TextEnhencer.ANSI_GREEN +"//    /  ,-~\\ x~\"  \\._\"-~     ~ _Y"+ TextEnhencer.ANSI_RED +"........\n";
				str +="\t.........." + TextEnhencer.ANSI_GREEN +"Y'   Y. (__.//     /  \" , \"\\_r ' ]"+ TextEnhencer.ANSI_RED +"........\n";   
				str +="\t.........." + TextEnhencer.ANSI_GREEN +"J-.__l_>---r{      ~    \\__/ \\_ _/"+ TextEnhencer.ANSI_RED +"........\n";  
				str +="\t........." + TextEnhencer.ANSI_GREEN +"(_ (   (~  (  ~\"---   _.-~ `\\ / \\ !"+ TextEnhencer.ANSI_RED +"........\n";   
				str +="\t.........." + TextEnhencer.ANSI_GREEN +"(_\"~--^----^--------\"  _.-c Y  /Y'"+ TextEnhencer.ANSI_RED +"........\n";  
				str +="\t..........." + TextEnhencer.ANSI_GREEN +"l~---v----.,______.--\"  /  !_/ |"+ TextEnhencer.ANSI_RED +".........\n";   
				str +="\t............" + TextEnhencer.ANSI_GREEN +"\\.__!.____./~-.      _/  /  \\ !"+ TextEnhencer.ANSI_RED +".........\n";  
				str +="\t............." + TextEnhencer.ANSI_GREEN +"`x._\\_____\\__,>---\"~___Y\\__/Y'"+ TextEnhencer.ANSI_RED +".........\n";  
				str +="\t................." + TextEnhencer.ANSI_GREEN +"~     ~(_~~(_)\"~___)/ /\\|"+ TextEnhencer.ANSI_RED +"..........\n";   
				str +="\t........................" + TextEnhencer.ANSI_GREEN +"(_~~   ~~___)  \\_t"+ TextEnhencer.ANSI_RED +"..........\n";  
				str +="\t........................" + TextEnhencer.ANSI_GREEN +"(_~~   ~~___)\\_/ |"+ TextEnhencer.ANSI_RED +"..........\n";  
				str +="\t........................" + TextEnhencer.ANSI_GREEN +"(_~~   ~~___)\\_/ |"+ TextEnhencer.ANSI_RED +"..........\n";   
				str +="\t........................" + TextEnhencer.ANSI_GREEN +"{ ~~   ~~   }/ \\ l"+ TextEnhencer.ANSI_RED +"..........\n";
				str+= TextEnhencer.ANSI_RESET;
		System.out.println("\n" + str);

		
	}

}

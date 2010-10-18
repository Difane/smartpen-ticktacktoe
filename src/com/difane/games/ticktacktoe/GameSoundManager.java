package com.difane.games.ticktacktoe;

import java.util.Hashtable;

import com.livescribe.i18n.AudibleResource;
import com.livescribe.i18n.ResourceBundle;
import com.livescribe.i18n.SoundResource;
import com.livescribe.ui.MediaPlayer;

/**
 * Required sound files:
 * 	- "Tick-Tack-Toe" - Game was selected in the menu
 *  - "Please draw first vertical line." - When first vertical line must be drawn
 *  - "Please draw second vertical line." - When second vertical line must be drawn
 *  - "Please draw first horizontal line." - When first horizontal line must be drawn
 *  - "Please draw second horizontal line." - When second horizontal line must be drawn
 *  - "Crosses is Yours. Make Your turn." - When board is drawn and actual game started
 *  - "Congratulations!!! You WIN!!!" - When human wins
 *  - "Sorry, but Pen wins" - When pen wins
 *  - "The Game ends with a Draw."
 *  - "Start Game" - When start game menu item was highlighted
 *  - "Help" - Help menu item
 *  - "About" - About menu item
 *  - "Rules" - Rules menu item
 *  - "How to draw game board" - menu item
 *  - "How to play the game" - menu item
 *  - "Easy" - menu item
 *  - "Hard" - menu item
 *
 */

public class GameSoundManager {
	/**
	 * DI Container
	 */
	private Container container;
	
	/**
	 * Resources
	 */
	private Hashtable resources;
	
	/**
	 * MediaPlayer
	 */
	private MediaPlayer player;
	
	/**
	 * Constructor
	 * 
	 * @param c
	 *            DI container
	 */
	public GameSoundManager(Container c) {
		this.container = c;
		
		// Initializing MediaPlayer
		this.player = MediaPlayer.newInstance(this.getContainer().getPenletComponent());
		
		// Initializing resources
		this.resources = new Hashtable(17);
		this.resources.put("about", getSoundResourceByName("about"));
		this.resources.put("congratulations-you-win", getSoundResourceByName("congratulations-you-win"));
		this.resources.put("crosses-is-yours-make-your-turn", getSoundResourceByName("crosses-is-yours-make-your-turn"));
		this.resources.put("easy", getSoundResourceByName("easy"));
		this.resources.put("hard", getSoundResourceByName("hard"));
		this.resources.put("help", getSoundResourceByName("help"));
		this.resources.put("how-to-draw-game-board", getSoundResourceByName("how-to-draw-game-board"));
		this.resources.put("how-to-play-the-game", getSoundResourceByName("how-to-play-the-game"));
		this.resources.put("please-draw-first-horizontal-line", getSoundResourceByName("please-draw-first-horizontal-line"));
		this.resources.put("please-draw-first-vertical-line", getSoundResourceByName("please-draw-first-vertical-line"));
		this.resources.put("please-draw-second-horizontal-line", getSoundResourceByName("please-draw-second-horizontal-line"));
		this.resources.put("please-draw-second-vertical-line", getSoundResourceByName("please-draw-second-vertical-line"));
		this.resources.put("rules", getSoundResourceByName("rules"));
		this.resources.put("sorry-but-pen-wins", getSoundResourceByName("sorry-but-pen-wins"));
		this.resources.put("start-game", getSoundResourceByName("start-game"));
		this.resources.put("the-game-ends-with-a-draw", getSoundResourceByName("the-game-ends-with-a-draw"));
		this.resources.put("tick-tack-toe", getSoundResourceByName("tick-tack-toe"));
		
		this.getContainer().getLoggerComponent().debug(
				"[GameSound] Component initialized");
	}
	
	/**
	 * Returns container
	 * 
	 * @return container
	 */
	public Container getContainer() {
		return container;
	}
	
	private SoundResource getSoundResourceByName(String name) {
		return this.container.getPenletComponent().getContext()
				.getResourceBundle().getSoundResource(name);
	}
	
	public void playSoundByName(String name, Boolean blocked) {
		this.player.stop();
		if (this.resources.containsKey(name)) {
			SoundResource res = (SoundResource) this.resources.get(name);
			this.player.play(res.getInputStream(), res.getMimeType(), blocked);
		}
	}
	
	public void playMainMenuStartGame(Boolean blocked) {
		playSoundByName("start-game", blocked);
	}
	
	public void playMainMenuHelp(Boolean blocked) {
		playSoundByName("help", blocked);
	}
	
	public void playMainMenuAbout(boolean blocked) {
		playSoundByName("about", blocked);
	}
	
	public void playHelpMenuRules(Boolean blocked) {
		playSoundByName("rules", blocked);
	}
	
	public void playHelpMenuHowToDrawGameBoard(Boolean blocked) {
		playSoundByName("how-to-draw-game-board", blocked);
	}
	
	public void playHelpMenuHowToPlayTheGame(Boolean blocked) {
		playSoundByName("how-to-play-the-game", blocked);
	}
	
	public void playDrawFirstVerticalLine(Boolean blocked) {
		playSoundByName("please-draw-first-vertical-line", blocked);
	}

	public void playDrawSecondVerticalLine(Boolean blocked) {
		playSoundByName("please-draw-second-vertical-line", blocked);
	}

	public void playDrawFirstHorizontalLine(Boolean blocked) {
		playSoundByName("please-draw-first-horizontal-line", blocked);
	}

	public void playDrawSecondHorizontalLine(Boolean blocked) {
		playSoundByName("please-draw-second-horizontal-line", blocked);
	}
	
	public void playYourTurn(Boolean blocked) {
		playSoundByName("crosses-is-yours-make-your-turn", blocked);
	}
	
	public void playYouWin(Boolean blocked) {
		playSoundByName("congratulations-you-win", blocked);
	}
	
	public void playPenWins(Boolean blocked) {
		playSoundByName("sorry-but-pen-wins", blocked);
	}
	
	public void playDraw(Boolean blocked) {
		playSoundByName("the-game-ends-with-a-draw", blocked);
	}
	
	public void playEasy(Boolean blocked) {
		playSoundByName("easy", blocked);
	}
	
	public void playHard(Boolean blocked) {
		playSoundByName("hard", blocked);
	}
	
	
}

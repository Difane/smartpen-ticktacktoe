package com.difane.games.ticktacktoe;

import java.util.Vector;

import com.difane.games.ticktacktoe.exceptions.GameBoardLineException;
import com.livescribe.display.BrowseList;
import com.livescribe.display.Display;
import com.livescribe.display.Graphics;
import com.livescribe.display.Image;
import com.livescribe.ui.ScrollLabel;
import com.livescribe.util.Timer;
import com.livescribe.util.TimerTask;

public class GameDisplay {

	/**
	 * DI Container
	 */
	private Container container;

	/**
	 * Display, which will show information to the user
	 */
	protected Display display;

	/**
	 * Main scroll label, used to display informational text to the user
	 */
	protected ScrollLabel label;

	/*
	 * Application menus
	 */
	protected Vector menuMainItems;
	protected BrowseList menuMain;
	protected Vector menuHelpItems;
	protected BrowseList menuHelp;
	protected Vector menuLevelSelectItems;
	protected BrowseList menuLevelSelect;

	/*
	 * Variables, required for drawing on the screen
	 */
	Image image;
	Graphics graphics;

	/**
	 * Field coordinates to draw "X" or "O"
	 */
	private int[][] fieldCoords = { { 0, 0 }, { 1, 2 }, { 7, 2 }, { 13, 2 },
			{ 1, 8 }, { 7, 8 }, { 13, 8 }, { 1, 14 }, { 7, 14 }, { 13, 14 } };

	
	/**
	 * Blink task
	 */
	private DrawingBlinkTask blinkTask;
	
	/**
	 * Blink timer
	 */
	private Timer blinkTimer;
	
	/**
	 * Delay in milliseconds between drawing blinking
	 */
	private long blinkDelay = 500;
	
	
	
	/**
	 * Constructor
	 * 
	 * @param c
	 *            DI container
	 */
	public GameDisplay(Container c) {
		this.container = c;

		// Initializing display elements
		this.display = this.getContainer().getPenletComponent().getContext()
				.getDisplay();
		this.label = new ScrollLabel();

		// Initializing menus
		this.menuMainItems = new Vector();
		this.menuMainItems.addElement("Start game");
		this.menuMainItems.addElement("Help");
		this.menuMainItems.addElement("About");
		this.menuMain = new BrowseList(this.menuMainItems);

		this.menuHelpItems = new Vector();
		this.menuHelpItems.addElement("Rules");
		this.menuHelpItems.addElement("How to draw game board");
		this.menuHelpItems.addElement("How to play the game");
		this.menuHelp = new BrowseList(this.menuHelpItems);

		this.menuLevelSelectItems = new Vector();
		this.menuLevelSelectItems.addElement("Easy");
		this.menuLevelSelectItems.addElement("Hard");
		this.menuLevelSelect = new BrowseList(this.menuLevelSelectItems);

		// Initializing graphics
		this.image = Image.createImage(96, 18);
		this.graphics = Graphics.getGraphics(this.image);
		this.graphics.setBrushColor(Display.getWhiteColor());
		this.graphics.setLineStyle(Graphics.LINE_STYLE_SOLID);
				
		this.blinkTimer = new Timer();
		
		this.getContainer().getLoggerComponent().debug("[GameDisplay] Component initialized");
	}

	/**
	 * Returns container
	 * 
	 * @return container
	 */
	public Container getContainer() {
		return container;
	}

	/**
	 * Displayed help menu on the display
	 */
	public void displayHelpMenu() {
		this.display.setCurrent(this.menuHelp);
	}

	/**
	 * Displayed main menu on the display
	 */
	public void displayMainMenu() {
		this.display.setCurrent(this.menuMain);
	}

	/**
	 * Displayed level select menu on the display
	 */
	public void displayLevelSelectMenu() {
		this.display.setCurrent(this.menuLevelSelect);
	}

	/**
	 * Displays a message on the screen
	 * 
	 * @param msg
	 *            Message to display
	 * @param scroll
	 *            Indicates if the text has to be scrolled or not. It does not
	 *            have any effect of the text fits within the display size. true
	 *            if it needs to scroll and false otherwise
	 */
	public void displayMessage(String msg, boolean scroll) {
		this.label.draw(msg, scroll);
		this.display.setCurrent(this.label);
	}

	/**
	 * Displays, that second horizontal line was drawed incorrectly
	 * @param reason 
	 */
	public void displayErrorDrawSecondHorizontalLine(int reason) {
		displayErrorDrawLine(reason);
	}

	private void displayErrorDrawLine(int reason) {
		String message = "";
		
		switch (reason) {
		case GameBoardLineException.REASON_LINE_IS_NOT_HORIZONTAL:
			message = "Line is not horizontal. ";
			break;
		case GameBoardLineException.REASON_LINE_IS_NOT_VERTICAL:
			message = "Line is not vertical. ";
			break;
		case GameBoardLineException.REASON_LINE_TO_SHORT:
			message = "Line is too short. ";
			break;
		case GameBoardLineException.REASON_MUST_BE_AT_THE_RIGHT:
			message = "Line must be at the right of previous one. ";
			break;
		case GameBoardLineException.REASON_MUST_BE_AT_THE_BOTTOM:
			message = "Line must be at the bottom of previous one. ";
			break;
		case GameBoardLineException.REASON_MUST_BE_NEAR_THE_OTHER_LINES:
			message = "Line must be near the other line. ";
			break;
		case GameBoardLineException.REASON_MUST_CROSS_BOTH_VERTICAL_LINES:
			message = "Line must cross both vertical lines. ";
			break;
		default:
			break;
		}
		
		message += "Please try again or read game help.";
		displayMessage(message, true);
	}

	/**
	 * Displays, that first horizontal line was drawed incorrectly
	 * @param reason 
	 */
	public void displayErrorDrawFirstHorizontalLine(int reason) {
		displayErrorDrawLine(reason);
	}

	/**
	 * Displays, that second vertical line was drawed incorrectly
	 * @param reason 
	 */
	public void displayErrorDrawSecondVerticalLine(int reason) {
		displayErrorDrawLine(reason);
	}

	/**
	 * Displays, that first vertical line was drawed incorrectly
	 * @param reason 
	 */
	public void displayErrorDrawFirstVerticalLine(int reason) {
		displayErrorDrawLine(reason);

	}
	
	/**
	 * Displays, that first vertical line was drawed incorrectly
	 */
	public void displayErrorPageChanged() {
		displayMessage(
				"You have changed a page. Please continue on the page, when You started.",
				true);
		
	}

	/**
	 * Displays message about game end
	 */
	public void displayEnd() {
		displayMessage("To start new game start drawing a new board!",
				true);
	}

	/**
	 * Displays message about game draw result
	 */
	public void displayDraw() {
		redrawBoard("DRAW !!!");
	}

	/**
	 * Displays message about pen wins result
	 */
	public void displayPenWins() {
		redrawBoard("YOU LOSE !!!");
	}

	/**
	 * Displays message about human wins result
	 */
	public void displayHumanWins() {
		redrawBoard("YOU WIN !!!");
	}

	/**
	 * Activates menu item by it's index, if this item is not active
	 * 
	 * @param menu
	 *            Menu to activate item in
	 * @param newIndex
	 *            New index of the activated item
	 */
	public void selectMenuItemIfNotSelected(BrowseList menu, int newIndex) {
		if (menu.getFocusIndex() != newIndex) {
			menu.setFocusItem(newIndex);
		}
	}

	/**
	 * Activates Main menu item by it's index, if this item is not active
	 * 
	 * @param newIndex
	 *            New index of the activated item
	 */
	public void selectMainMenuItemIfNotSelected(int newIndex) {
		selectMenuItemIfNotSelected(menuMain, newIndex);
	}

	/**
	 * Activates Help menu item by it's index, if this item is not active
	 * 
	 * @param newIndex
	 *            New index of the activated item
	 */
	public void selectHelpMenuItemIfNotSelected(int newIndex) {
		selectMenuItemIfNotSelected(menuHelp, newIndex);
	}

	/**
	 * Activates LevelSelect menu item by it's index, if this item is not active
	 * 
	 * @param newIndex
	 *            New index of the activated item
	 */
	public void selectLevelSelectMenuItemIfNotSelected(int newIndex) {
		selectMenuItemIfNotSelected(menuLevelSelect, newIndex);
	}

	/**
	 * Draws board on the screen and displays message to the user with
	 * information about next activity
	 * 
	 * @param msg Message to be displayed at the right of the board
	 */
	public void redrawBoard(String msg) {
		// At first drawing empty game field
		this.graphics.clearRect();
		this.drawFirstVerticalLine();
		this.drawSecondVerticalLine();
		this.drawFirstHorizontalLine();
		this.drawSecondHorizontalLine();

		int[] board = this.getContainer().getGameLogicComponent().getFields();

		for (int i = 1; i <= 9; i++) {
			if (board[i] == GameLogic.FIELD_X) {
				this.drawX(i);
			} else if (board[i] == GameLogic.FIELD_O) {
				this.drawO(i);
			} else {
				// Field is empty
			}
		}

		this.graphics.drawString(msg, 25, 2, 0);

		this.displayDrawing(true);
	}

	/**
	 * Redraws game board and displays a message, that next turn is human's
	 */
	public void drawBoardHumansTurn() {
		this.redrawBoard("Your turn!");
	}

	/**
	 * Redraws game board and displays a message, that next turn is pen's
	 */
	public void drawBoardPensTurn() {
		this.redrawBoard("Pens turn!");
	}

	/**
	 * Displays drawing on the screen. Actual data must be drawed on the
	 * "this.penlet.graphics" object
	 * 
	 * @param scroll
	 *            Indicates if the text has to be scrolled or not.
	 */
	public void displayDrawing(boolean scroll) {
		this.label.draw(this.image, null, scroll);
		this.display.setCurrent(this.label);
	}

	/**
	 * Draws first vertical line
	 */
	public void drawFirstVerticalLine() {
		this.graphics.drawLine(5, 1, 5, 17);
	}

	/**
	 * Draws second vertical line
	 */
	public void drawSecondVerticalLine() {
		this.graphics.drawLine(11, 1, 11, 17);
	}

	/**
	 * Draws first horizontal line
	 */
	public void drawFirstHorizontalLine() {
		this.graphics.drawLine(0, 6, 16, 6);
	}

	/**
	 * Draws second vertical line
	 */
	public void drawSecondHorizontalLine() {
		this.graphics.drawLine(0, 12, 16, 12);
	}

	/**
	 * Draws X in the cell, relatinve to the passed cell left-top coords
	 * 
	 * @param x
	 *            X-coord of the top-left cell point
	 * @param y
	 *            Y-coord of the top-left cell point
	 */
	public void drawXInCell(int x, int y) {
		this.graphics.drawLine(x, y, x + 2, y + 2);
		this.graphics.drawLine(x, y + 2, x + 2, y);
	}

	/**
	 * Draws O in the cell, relatinve to the passed cell left-top coords
	 * 
	 * @param x
	 *            X-coord of the top-left cell point
	 * @param y
	 *            Y-coord of the top-left cell point
	 */
	public void drawOInCell(int x, int y) {
		this.graphics.fillRect(x, y, 3, 3);
	}

	/**
	 * Draws X in the given field num
	 * 
	 * @param fieldNum
	 *            Num of the field (1-9)
	 */
	public void drawX(int fieldNum) {
		this.drawXInCell(this.fieldCoords[fieldNum][0],
				this.fieldCoords[fieldNum][1]);
	}

	/**
	 * Draws O in the given field num
	 * 
	 * @param fieldNum
	 *            Num of the field (1-9)
	 */
	public void drawO(int fieldNum) {
		this.drawOInCell(this.fieldCoords[fieldNum][0],
				this.fieldCoords[fieldNum][1]);
	}

	public void doDisplayDrawFirstVerticalLine(boolean lineVisible) {
		this.graphics.clearRect();
		if (lineVisible) {
			this.drawFirstVerticalLine();
		}
		this.graphics.drawString("1st vertical line", 20, 2, 0);
		this.displayDrawing(true);
	}
	
	public void doDisplayDrawSecondVerticalLine(boolean lineVisible) {
		this.graphics.clearRect();
		this.drawFirstVerticalLine();
		if (lineVisible) {
			this.drawSecondVerticalLine();
		}
		this.graphics.drawString("2nd vertical line", 20, 2, 0);
		this.displayDrawing(true);
	}
	
	public void doDisplayDrawFirstHorizontalLine(boolean lineVisible) {
		this.graphics.clearRect();
		this.drawFirstVerticalLine();
		this.drawSecondVerticalLine();
		if (lineVisible) {
			this.drawFirstHorizontalLine();
		}
		this.graphics.drawString("1st horiz. line", 22, 2, 0);
		this.displayDrawing(true);
	}
	
	public void doDisplayDrawSecondHorizontalLine(boolean lineVisible) {
		this.graphics.clearRect();
		this.drawFirstVerticalLine();
		this.drawSecondVerticalLine();
		this.drawFirstHorizontalLine();
		if (lineVisible) {
			this.drawSecondHorizontalLine();
		}
		this.graphics.drawString("2nd horiz. line", 22, 2, 0);
		this.displayDrawing(true);
	}
	
	/**
	 * Cancels current task execution.
	 */
	public void cancelTask() {
		if (null != this.blinkTask) {
			this.blinkTask.cancel();
		}		
	}
	
	/**
	 * Displayed Draw first vertical line on the display
	 * @param lineVisible Is line, that must be displayed, visible or not
	 */
	public void displayDrawFirstVerticalLine(boolean lineVisible) {
		// Preparing blink action
		cancelTask();
		this.blinkTask = new DrawingBlinkTask(this.container, lineVisible, DrawingBlinkTask.DISPLAY_FIRST_VERTICAL_LINE);		
		this.blinkTimer.schedule(this.blinkTask, this.blinkDelay, this.blinkDelay);
	}

	/**
	 * Displayed Draw second vertical line on the display
	 * @param lineVisible Is line, that must be displayed, visible or not
	 */
	public void displayDrawSecondVerticalLine(boolean lineVisible) {
		// Preparing blink action
		cancelTask();
		this.blinkTask = new DrawingBlinkTask(this.container, lineVisible, DrawingBlinkTask.DISPLAY_SECOND_VERTICAL_LINE);
		this.blinkTimer.schedule(this.blinkTask, this.blinkDelay, this.blinkDelay);
	}

	/**
	 * Displayed Draw first horizontal line on the display
	 * @param lineVisible Is line, that must be displayed, visible or not
	 */
	public void displayDrawFirstHorizontalLine(boolean lineVisible) {
		// Preparing blink action
		cancelTask();
		this.blinkTask = new DrawingBlinkTask(this.container, lineVisible, DrawingBlinkTask.DISPLAY_FIRST_HORIZONTAL_LINE);
		this.blinkTimer.schedule(this.blinkTask, this.blinkDelay, this.blinkDelay);
	}

	/**
	 * Displayed Draw second horizontal line on the display
	 * @param lineVisible Is line, that must be displayed, visible or not
	 */
	public void displayDrawSecondHorizontalLine(boolean lineVisible) {
		// Preparing blink action
		cancelTask();
		this.blinkTask = new DrawingBlinkTask(this.container, lineVisible, DrawingBlinkTask.DISPLAY_SECOND_HORIZONTAL_LINE);
		this.blinkTimer.schedule(this.blinkTask, this.blinkDelay, this.blinkDelay);
	}

	/**
	 * Displayed How to play on the display
	 */
	public void displayHowToPlay() {
		displayMessage(
				"At first please select 'Start Game' in the main menu. Next select pen level: easy or hard. Then please draw the board. To learn, how to draw game board, please look at the corresponded help menu item. After drawing the board the game begins. Your turn is first. To make a turn please draw an 'x' in one of the board fields. Then look at the pen screen. If You turn is correct - You will see it on the screen together with the pen's turn. Continue making turns until game ends.",
				true);
	}

	/**
	 * Displayed How to draw board on the display
	 */
	public void displayHowToDrawBoard() {
		displayMessage(
				"Game board is 3x3 grid of squares. To draw it please make following steps. First draw one vertical line, that has minimal lenght of 1 cantimeter. Next draw another vertical line near the first one at the right. Then draw horizontal line, that crosses both vertical lines. Next draw another horizontal line near the first one on the bottom, that also crosses both vertical lines. You board is ready.",
				true);
	}

	/**
	 * Displayed Rules on the display
	 */
	public void displayRules() {
		displayMessage(
				"The object of Tic Tac Toe is to get three in a row. You play on a three by three game board. The first player is known as X and the second is O. X always goes first. Players alternate placing Xs and Os on the game board until either one player has three in a row, horizontally, vertically or diagonally, or all nine squares are filled. If a player is able to draw three Xs or three Os in a row, that player wins. If all nine squares are filled and neither player has three in a row, the game is a draw.",
				true);
	}

	/**
	 * Displayed about on the display
	 */
	public void displayAbout() {
		displayMessage("Tic Tac Toe game. Version 1.0 Copyright Difane Group", true);
	}

	/**
	 * Displayed message, that human starts the game
	 */
	public void displayHumanStartsGame() {
		displayMessage("You playes crosses. You turn is first.", true);
	}

	/**
	 * Displayed message, that pen starts the game
	 */
	public void displayPenStartsGame() {
		displayMessage("You playes noughts. Pen starts the game.", true);
	}

	/**
	 * Focuses main menu to the next item
	 */
	public void focusMainMenuToNext() {
		this.menuMain.focusToNext();
	}

	/**
	 * Focuses main menu to the previous item
	 */
	public void focusMainMenuToPrevious() {
		this.menuMain.focusToPrevious();
	}

	/**
	 * Focuses help menu to the next item
	 */
	public void focusHelpMenuToNext() {
		this.menuHelp.focusToNext();
	}

	/**
	 * Focuses help menu to the previous item
	 */
	public void focusHelpMenuToPrevious() {
		this.menuHelp.focusToPrevious();
	}

	/**
	 * Focuses level select menu to the next item
	 */
	public void focusLevelSelectMenuToNext() {
		this.menuLevelSelect.focusToNext();
	}

	/**
	 * Focuses level select menu to the previous item
	 */
	public void focusLevelSelectMenuToPrevious() {
		this.menuLevelSelect.focusToPrevious();
	}
	
	/**
	 * Timer and task for blinking during game board drawing	 * 
	 */
	
	public class DrawingBlinkTask extends TimerTask {
		
		/**
		 * DI Container
		 */
		private Container container;

		/**
		 * Flag, which determines, must line be shown or not
		 */
		boolean blinkFlag = true;
		
		/**
		 * Possible values for drawing action
		 */
		public static final int DISPLAY_FIRST_VERTICAL_LINE = 0;
		public static final int DISPLAY_SECOND_VERTICAL_LINE = 1;
		public static final int DISPLAY_FIRST_HORIZONTAL_LINE = 2;
		public static final int DISPLAY_SECOND_HORIZONTAL_LINE = 3;
		
		/**
		 * Current drawing action
		 */
		private int drawAction = DISPLAY_FIRST_VERTICAL_LINE;
		
		/**
		 * @param c
		 *            DI container
		 */
		public DrawingBlinkTask(Container c, boolean blinkFlag, int drawAction) {
			super();
			this.container = c;
			this.blinkFlag = blinkFlag;
			this.drawAction = drawAction;
			
			this.container.getLoggerComponent().debug("[GameDisplay][DB] Initialized. Flag: "+blinkFlag+", Action: "+drawAction);
		}
		
		/**
		 * Actual task execution
		 */
		public void run() {
			this.container.getLoggerComponent().debug("[GameDisplay][DB] Executed. Flag: "+this.blinkFlag+", Action: "+this.drawAction);
			
			switch (this.drawAction) {
			case DISPLAY_FIRST_VERTICAL_LINE:
				this.container.getGameDisplayComponent().doDisplayDrawFirstVerticalLine(this.blinkFlag);
				break;
			case DISPLAY_SECOND_VERTICAL_LINE:
				this.container.getGameDisplayComponent().doDisplayDrawSecondVerticalLine(this.blinkFlag);
				break;
			case DISPLAY_FIRST_HORIZONTAL_LINE:
				this.container.getGameDisplayComponent().doDisplayDrawFirstHorizontalLine(this.blinkFlag);
				break;
			case DISPLAY_SECOND_HORIZONTAL_LINE:
				this.container.getGameDisplayComponent().doDisplayDrawSecondHorizontalLine(this.blinkFlag);
				break;
			}
			
			this.blinkFlag = !this.blinkFlag;
		}
	}
}

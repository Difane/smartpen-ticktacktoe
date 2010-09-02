package com.difane.games.ticktacktoe;

import java.util.Vector;

import com.livescribe.display.BrowseList;
import com.livescribe.display.Display;
import com.livescribe.display.Graphics;
import com.livescribe.display.Image;
import com.livescribe.ui.ScrollLabel;

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
	 */
	public void displayErrorDrawSecondHorizontalLine() {
		displayMessage(
				"Second horizontal line is invalid. Please try again or read game help.",
				true);
	}

	/**
	 * Displays, that first horizontal line was drawed incorrectly
	 */
	public void displayErrorDrawFirstHorizontalLine() {
		displayMessage(
				"First horizontal line is invalid. Please try again or read game help.",
				true);
	}

	/**
	 * Displays, that second vertical line was drawed incorrectly
	 */
	public void displayErrorDrawSecondVerticalLine() {
		displayMessage(
				"Second vertical line is invalid. Please try again or read game help.",
				true);
	}

	/**
	 * Displays, that first vertical line was drawed incorrectly
	 */
	public void displayErrorDrawFirstVerticalLine() {
		displayMessage(
				"First vertical line is invalid. Please try again or read game help.",
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

	/**
	 * Displayed Draw first vertical line on the display
	 */
	public void displayDrawFirstVerticalLine() {
		this.graphics.clearRect();
		this.drawFirstVerticalLine();
		this.graphics.drawString("First line", 25, 2, 0);
		this.displayDrawing(true);
	}

	/**
	 * Displayed Draw second vertical line on the display
	 */
	public void displayDrawSecondVerticalLine() {
		this.graphics.clearRect();
		this.drawFirstVerticalLine();
		this.drawSecondVerticalLine();
		this.graphics.drawString("Second line", 25, 2, 0);
		this.displayDrawing(true);
	}

	/**
	 * Displayed Draw first horizontal line on the display
	 */
	public void displayDrawFirstHorizontalLine() {
		this.graphics.clearRect();
		this.drawFirstVerticalLine();
		this.drawSecondVerticalLine();
		this.drawFirstHorizontalLine();
		this.graphics.drawString("Third line", 25, 2, 0);
		this.displayDrawing(true);
	}

	/**
	 * Displayed Draw second horizontal line on the display
	 */
	public void displayDrawSecondHorizontalLine() {
		this.graphics.clearRect();
		this.drawFirstVerticalLine();
		this.drawSecondVerticalLine();
		this.drawFirstHorizontalLine();
		this.drawSecondHorizontalLine();
		this.graphics.drawString("Fourth line", 25, 2, 0);
		this.displayDrawing(true);
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
}

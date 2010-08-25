package com.difane.games.ticktacktoe;

import java.util.Vector;

import com.livescribe.display.BrowseList;
import com.livescribe.display.Display;
import com.livescribe.display.Graphics;
import com.livescribe.display.Image;
import com.livescribe.ui.ScrollLabel;

public class GameDisplay {
	/**
	 * Container
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
	protected Vector		menuMainItems;
	protected BrowseList	menuMain;
	protected Vector		menuHelpItems;
	protected BrowseList	menuHelp;
	protected Vector		menuLevelSelectItems;
	protected BrowseList	menuLevelSelect;
	
	// Variables, required for drawing on the screen
	Image image;
	Graphics graphics;
	
	/**
	 * Field coordinates to draw "X" or "O"
	 */
	private int[][] fieldCoords = { { 0, 0 }, { 1, 2 }, { 7, 2 }, { 13, 2 },
			{ 1, 8 }, { 7, 8 }, { 13, 8 }, { 1, 14 }, { 7, 14 }, { 13, 14 } };


	
	public GameDisplay(Container c)
	{
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
	}
	
	/**
	 * Returns container
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
	
	public void displayErrorDrawSecondHorizontalLine() {
		displayMessage(
				"Second horizontal line is invalid. Please try again or read game help.",
				true);
	}

	public void displayErrorDrawFirstHorizontalLine() {
		displayMessage(
				"First horizontal line is invalid. Please try again or read game help.",
				true);
	}

	public void displayErrorDrawSecondVerticalLine() {
		displayMessage(
				"Second vertical line is invalid. Please try again or read game help.",
				true);
	}

	public void displayErrorDrawFirstVerticalLine() {
		displayMessage(
				"First vertical line is invalid. Please try again or read game help.",
				true);

	}
	
	public void displayEnd() {
		displayMessage("To start new game, please launch application again!",
				true);
	}

	public void displayDraw() {
		displayMessage("Game Draw!", true);
	}

	public void displayPenWins() {
		displayMessage("Sorry, but You Lose!", true);
	}

	public void displayHumanWins() {
		displayMessage("Congratulations! You win!", true);
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
	public void selectMainMenuItemIfNotSelected(int newIndex)
	{
		selectMenuItemIfNotSelected(menuMain, newIndex);
	}
	
	/**
	 * Activates Help menu item by it's index, if this item is not active
	 * 
	 * @param newIndex
	 *            New index of the activated item
	 */
	public void selectHelpMenuItemIfNotSelected(int newIndex)
	{
		selectMenuItemIfNotSelected(menuHelp, newIndex);
	}
	
	/**
	 * Activates LevelSelect menu item by it's index, if this item is not active
	 * 
	 * @param newIndex
	 *            New index of the activated item
	 */
	public void selectLevelSelectMenuItemIfNotSelected(int newIndex)
	{
		selectMenuItemIfNotSelected(menuLevelSelect, newIndex);
	}
	
	/**
	 * Draws board on the screen and displays message to the user with
	 * information about next activity
	 * 
	 * @param humanTurnNext
	 *            True, if next turn must be dome by human, false otherwise
	 */
	public void redrawBoard(boolean humanTurnNext) {
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

		if (humanTurnNext) {
			this.graphics.drawString("Your turn!", 25, 2, 0);
		} else {
			this.graphics.drawString("Pen's turn!", 25, 2, 0);
		}

		this.displayDrawing(true);
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
		displayMessage("This is example HOW TO PLAY string", true);
	}

	/**
	 * Displayed How to draw board on the display
	 */
	public void displayHowToDrawBoard() {
		displayMessage("This is example HOW TO DRAW BOARD string", true);
	}

	/**
	 * Displayed Rules on the display
	 */
	public void displayRules() {
		displayMessage("This is example RULES string", true);
	}

	/**
	 * Displayed about on the display
	 */
	public void displayAbout() {
		displayMessage("This is example about string", true);
	}
	
	/**
	 * Displayed message, that human starts the game
	 */
	public void displayHumanStartsGame() {
		displayMessage("You playes crosses and goes first", true);
	}
	
	/**
	 * Displayed message, that pen starts the game
	 */
	public void displayPenStartsGame() {
		displayMessage("You playes noughts and goes second", true);
	}
	
	public void focusMainMenuToNext(){
		this.menuMain.focusToNext();
	}
	
	public void focusMainMenuToPrevious(){
		this.menuMain.focusToPrevious();
	}
	
	public void focusHelpMenuToNext(){
		this.menuHelp.focusToNext();
	}
	
	public void focusHelpMenuToPrevious(){
		this.menuHelp.focusToPrevious();
	}
	
	public void focusLevelSelectMenuToNext(){
		this.menuLevelSelect.focusToNext();
	}
	
	public void focusLevelSelectMenuToPrevious(){
		this.menuLevelSelect.focusToPrevious();
	}
}

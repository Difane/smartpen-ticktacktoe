package com.difane.games.ticktacktoe;

import com.difane.games.ticktacktoe.exceptions.GameBoardLineLengthException;
import com.difane.games.ticktacktoe.exceptions.GameBoardLinePointsCountException;
import com.difane.games.ticktacktoe.exceptions.GameBoardLinePositionException;
import com.difane.games.ticktacktoe.exceptions.GameBoardLineRequirementsException;
import com.livescribe.afp.PageInstance;
import com.livescribe.display.BrowseList;
import com.livescribe.event.StrokeListener;
import com.livescribe.geom.PolyLine;
import com.livescribe.geom.Stroke;
import com.livescribe.penlet.Region;
import com.livescribe.storage.StrokeStorage;

public class FSM implements StrokeListener {

	// Available states
	static public final int FSM_STATE_UNDEFINED = -1;
	static public final int FSM_STATE_START = 0;
	static public final int FSM_STATE_MAIN_MENU_START_GAME = 1;
	static public final int FSM_STATE_MAIN_MENU_HELP = 2;
	static public final int FSM_STATE_HELP_MENU_RULES = 3;
	static public final int FSM_STATE_HELP_MENU_RULES_DISPLAYED = 4;
	static public final int FSM_STATE_HELP_MENU_HOW_TO_DRAW_BOARD = 5;
	static public final int FSM_STATE_HELP_MENU_HOW_TO_DRAW_BOARD_DISPLAYED = 6;
	static public final int FSM_STATE_HELP_MENU_HOW_TO_PLAY = 7;
	static public final int FSM_STATE_HELP_MENU_HOW_TO_PLAY_DISPLAYED = 8;
	static public final int FSM_STATE_MAIN_MENU_ABOUT = 9;
	static public final int FSM_STATE_MAIN_MENU_ABOUT_DISPLAYED = 10;
	static public final int FSM_STATE_LEVEL_MENU_EASY = 11;
	static public final int FSM_STATE_LEVEL_MENU_HARD = 12;
	static public final int FSM_STATE_DRAW_BOARD_FIRST_VERTICAL_LINE = 13;
	static public final int FSM_STATE_DRAW_BOARD_SECOND_VERTICAL_LINE = 14;
	static public final int FSM_STATE_DRAW_BOARD_FIRST_HORIZONTAL_LINE = 15;
	static public final int FSM_STATE_DRAW_BOARD_SECOND_HORIZONTAL_LINE = 16;
	static public final int FSM_STATE_GAME_SELECT_PLAYER_ORDER = 17;
	static public final int FSM_STATE_GAME_HUMAN_TURN = 18;
	static public final int FSM_STATE_GAME_PEN_TURN = 19;
	static public final int FSM_STATE_GAME_END_HUMAN_WINS = 20;
	static public final int FSM_STATE_GAME_END_PEN_WINS = 21;
	static public final int FSM_STATE_GAME_END_DRAW = 22;
	static public final int FSM_STATE_END = 23;

	private BasePenlet penlet;

	private int currentState = FSM_STATE_UNDEFINED;

	private GameBoard board;
	private GameLogic logic;

	/**
	 * Constructor
	 */
	public FSM(BasePenlet p) {
		this.penlet = p;
		penlet.logger.debug("[FSM] Constructed");
		this.currentState = FSM_STATE_START;

		this.board = new GameBoard();
		this.logic = new GameLogic();
	}

	/**
	 * This event must be called, when application will be started. It will try
	 * to start application by displaying it's main menu
	 */
	public void eventStartApplication() {
		penlet.logger.debug("[FSM] eventStartApplication received");

		transition(currentState, FSM_STATE_MAIN_MENU_START_GAME);
	}

	/**
	 * This event must be called, when DOWN in the menu be pressed
	 */
	public boolean eventMenuDown() {
		penlet.logger.debug("[FSM] eventMenuDown received");
		switch (currentState) {
		case FSM_STATE_MAIN_MENU_START_GAME:
			transition(currentState, FSM_STATE_MAIN_MENU_HELP);
			break;
		case FSM_STATE_MAIN_MENU_HELP:
			transition(currentState, FSM_STATE_MAIN_MENU_ABOUT);
			break;
		case FSM_STATE_HELP_MENU_RULES:
			transition(currentState, FSM_STATE_HELP_MENU_HOW_TO_DRAW_BOARD);
			break;
		case FSM_STATE_HELP_MENU_HOW_TO_DRAW_BOARD:
			transition(currentState, FSM_STATE_HELP_MENU_HOW_TO_PLAY);
			break;
		case FSM_STATE_LEVEL_MENU_EASY:
			transition(currentState, FSM_STATE_LEVEL_MENU_HARD);
			break;
		default:
		}

		// Menu down are always handled
		return true;
	}

	/**
	 * This event must be called, when UP in the menu be pressed
	 */
	public boolean eventMenuUp() {
		penlet.logger.debug("[FSM] eventMenuUp received");
		switch (currentState) {
		case FSM_STATE_MAIN_MENU_HELP:
			transition(currentState, FSM_STATE_MAIN_MENU_START_GAME);
			break;
		case FSM_STATE_MAIN_MENU_ABOUT:
			transition(currentState, FSM_STATE_MAIN_MENU_HELP);
			break;
		case FSM_STATE_HELP_MENU_HOW_TO_DRAW_BOARD:
			transition(currentState, FSM_STATE_HELP_MENU_RULES);
			break;
		case FSM_STATE_HELP_MENU_HOW_TO_PLAY:
			transition(currentState, FSM_STATE_HELP_MENU_HOW_TO_DRAW_BOARD);
			break;
		case FSM_STATE_LEVEL_MENU_HARD:
			transition(currentState, FSM_STATE_LEVEL_MENU_EASY);
			break;
		default:
		}

		// Menu up are always handled
		return true;
	}

	/**
	 * This event must be called, when LEFT in the menu be pressed
	 */
	public boolean eventMenuLeft() {
		penlet.logger.debug("[FSM] eventMenuLeft received");

		boolean result = false;

		switch (currentState) {
		case FSM_STATE_LEVEL_MENU_EASY:
		case FSM_STATE_LEVEL_MENU_HARD:
			transition(currentState, FSM_STATE_MAIN_MENU_START_GAME);
			result = true;
			break;
		case FSM_STATE_HELP_MENU_RULES:
		case FSM_STATE_HELP_MENU_HOW_TO_DRAW_BOARD:
		case FSM_STATE_HELP_MENU_HOW_TO_PLAY:
			transition(currentState, FSM_STATE_MAIN_MENU_HELP);
			result = true;
			break;
		case FSM_STATE_MAIN_MENU_ABOUT_DISPLAYED:
			transition(currentState, FSM_STATE_MAIN_MENU_ABOUT);
			result = true;
			break;

		case FSM_STATE_HELP_MENU_RULES_DISPLAYED:
			transition(currentState, FSM_STATE_HELP_MENU_RULES);
			result = true;
			break;
		case FSM_STATE_HELP_MENU_HOW_TO_DRAW_BOARD_DISPLAYED:
			transition(currentState, FSM_STATE_HELP_MENU_HOW_TO_DRAW_BOARD);
			result = true;
			break;
		case FSM_STATE_HELP_MENU_HOW_TO_PLAY_DISPLAYED:
			transition(currentState, FSM_STATE_HELP_MENU_HOW_TO_PLAY);
			result = true;
			break;
		default:
			result = false;
		}

		return result;
	}

	/**
	 * This event must be called, when RIGHT in the menu be pressed
	 */
	public boolean eventMenuRight() {
		penlet.logger.debug("[FSM] eventMenuRight received");

		switch (currentState) {
		case FSM_STATE_MAIN_MENU_START_GAME:
			transition(currentState, FSM_STATE_LEVEL_MENU_EASY);
			break;
		case FSM_STATE_MAIN_MENU_HELP:
			transition(currentState, FSM_STATE_HELP_MENU_RULES);
			break;
		case FSM_STATE_MAIN_MENU_ABOUT:
			transition(currentState, FSM_STATE_MAIN_MENU_ABOUT_DISPLAYED);
			break;
		case FSM_STATE_HELP_MENU_RULES:
			transition(currentState, FSM_STATE_HELP_MENU_RULES_DISPLAYED);
			break;
		case FSM_STATE_HELP_MENU_HOW_TO_DRAW_BOARD:
			transition(currentState,
					FSM_STATE_HELP_MENU_HOW_TO_DRAW_BOARD_DISPLAYED);
			break;
		case FSM_STATE_HELP_MENU_HOW_TO_PLAY:
			transition(currentState, FSM_STATE_HELP_MENU_HOW_TO_PLAY_DISPLAYED);
			break;
		case FSM_STATE_LEVEL_MENU_EASY:
			this.logic.setAiLevel(GameLogic.AI_LEVEL_EASY);
			transition(currentState, FSM_STATE_DRAW_BOARD_FIRST_VERTICAL_LINE);
			break;
		case FSM_STATE_LEVEL_MENU_HARD:
			this.logic.setAiLevel(GameLogic.AI_LEVEL_HARD);
			transition(currentState, FSM_STATE_DRAW_BOARD_FIRST_VERTICAL_LINE);
			break;
		default:
		}

		// Menu right are always handled
		return true;
	}

	/**
	 * This event must be called, when first board vertical line be ready
	 */
	public void eventFirstVerticalLineReady() {
		penlet.logger.debug("[FSM] eventFirstVerticalLineReady received");
		if (currentState == FSM_STATE_DRAW_BOARD_FIRST_VERTICAL_LINE) {
			transition(currentState, FSM_STATE_DRAW_BOARD_SECOND_VERTICAL_LINE);
		}
	}

	/**
	 * This event must be called, when second board vertical line be ready
	 */
	public void eventSecondVerticalLineReady() {
		penlet.logger.debug("[FSM] eventSecondVerticalLineReady received");
		if (currentState == FSM_STATE_DRAW_BOARD_SECOND_VERTICAL_LINE) {
			transition(currentState, FSM_STATE_DRAW_BOARD_FIRST_HORIZONTAL_LINE);
		}
	}

	/**
	 * This event must be called, when first board horizontal line be ready
	 */
	public void eventFirstHorizontalLineReady() {
		penlet.logger.debug("[FSM] eventFirstHorizontalLineReady received");
		if (currentState == FSM_STATE_DRAW_BOARD_FIRST_HORIZONTAL_LINE) {
			transition(currentState,
					FSM_STATE_DRAW_BOARD_SECOND_HORIZONTAL_LINE);
		}
	}

	/**
	 * This event must be called, when second board horizontal line be ready
	 */
	public void eventSecondHorizontalLineReady() {
		penlet.logger.debug("[FSM] eventSecondHorizontalLineReady received");
		if (currentState == FSM_STATE_DRAW_BOARD_SECOND_HORIZONTAL_LINE) {
			transition(currentState, FSM_STATE_GAME_SELECT_PLAYER_ORDER);
		}
	}

	/**
	 * This event must be called, when player order is selected and next turn is
	 * human's
	 */
	public void eventPlayerSelectedHumanTurnNext() {
		penlet.logger.debug("[FSM] eventPlayerSelectedHumanTurnNext received");
		if (currentState == FSM_STATE_GAME_SELECT_PLAYER_ORDER) {
			this.displayMessage("You playes crosses and goes first", true);
			this.transition(currentState, FSM_STATE_GAME_HUMAN_TURN);
		}
	}

	/**
	 * This event must be called, when player order is selected and next turn is
	 * pen's
	 */
	public void eventPlayerSelectedPenTurnNext() {
		penlet.logger.debug("[FSM] eventPlayerSelectedPenTurnNext received");
		if (currentState == FSM_STATE_GAME_SELECT_PLAYER_ORDER) {
			this.displayMessage("You playes noughts and goes second", true);
			this.transition(currentState, FSM_STATE_GAME_PEN_TURN);
		}
	}

	/**
	 * This event must be called, when player makes his turn
	 */
	public void eventHumanTurnReady() {
		penlet.logger.debug("[FSM] eventHumanTurnReady received");
	}

	/**
	 * This event must be called, when pen makes his turn
	 */
	public void eventPenTurnReady() {
		penlet.logger.debug("[FSM] eventPenTurnReady received");
	}

	/**
	 * This event must be called, when game ends and human wins
	 */
	public void eventHumanWins() {
		penlet.logger.debug("[FSM] eventHumanWins received");
	}

	/**
	 * This event must be called, when game ends and pen wins
	 */
	public void eventPenWins() {
		penlet.logger.debug("[FSM] eventPenWins received");
	}
	
	/**
	 * This event must be called, when game ends and draw appears
	 */
	public void eventDraw() {
		penlet.logger.debug("[FSM] eventDraw received");
	}

	/**
	 * This event ends application
	 */
	public void eventEndApplication() {
		penlet.logger.debug("[FSM] eventEndApplication received");
	}

	/**
	 * Activates menu item by it's index, if this item is not active
	 * 
	 * @param menu
	 *            Menu to activate item in
	 * @param newIndex
	 *            New index of the activated item
	 */
	private void selectMenuItemIfNotSelected(BrowseList menu, int newIndex) {
		if (menu.getFocusIndex() != newIndex) {
			menu.setFocusItem(newIndex);
		}
	}

	private void transition(int currentState, int transitionState) {
		penlet.logger.debug("[FSM] transition started ( " + currentState
				+ " -> " + transitionState + " )");

		try {
			switch (transitionState) {
			case FSM_STATE_MAIN_MENU_START_GAME:
				if (currentState == FSM_STATE_START
						|| currentState == FSM_STATE_LEVEL_MENU_EASY
						|| currentState == FSM_STATE_LEVEL_MENU_HARD) {
					// Select "Help" in the main menu, if not selected
					selectMenuItemIfNotSelected(this.penlet.menuMain, 0);

					// Main menu must be displayed
					displayMainMenu();
					this.penlet.logger
							.debug("[FSM] Main menu was displayed with active item 0");

				} else if (currentState == FSM_STATE_MAIN_MENU_HELP) {
					// Main menu must be focused to the previous item
					this.penlet.menuMain.focusToPrevious();
					this.penlet.logger
							.debug("[FSM] Main menu item 0 was activated");
				}
				break;
			case FSM_STATE_MAIN_MENU_HELP:
				if (currentState == FSM_STATE_MAIN_MENU_START_GAME) {
					// Main menu must be focused to the next item
					this.penlet.menuMain.focusToNext();
					this.penlet.logger
							.debug("[FSM] Main menu item 1 was activated");
				} else if (currentState == FSM_STATE_MAIN_MENU_ABOUT) {
					// Main menu must be focused to the previous item
					this.penlet.menuMain.focusToPrevious();
					this.penlet.logger
							.debug("[FSM] Main menu item 1 was activated");
				} else if (currentState == FSM_STATE_HELP_MENU_RULES
						|| currentState == FSM_STATE_HELP_MENU_HOW_TO_DRAW_BOARD
						|| currentState == FSM_STATE_HELP_MENU_HOW_TO_PLAY) {
					// Select "Help" in the main menu, if not selected
					selectMenuItemIfNotSelected(this.penlet.menuMain, 1);

					// Main menu must be displayed
					displayMainMenu();
					this.penlet.logger
							.debug("[FSM] Main menu was displayed with active item 1");
				}
				break;
			case FSM_STATE_MAIN_MENU_ABOUT:
				if (currentState == FSM_STATE_MAIN_MENU_HELP) {
					// Main menu must be focused to the next item
					this.penlet.menuMain.focusToNext();
					this.penlet.logger
							.debug("[FSM] Main menu item 2 was activated");
				} else if (currentState == FSM_STATE_MAIN_MENU_ABOUT_DISPLAYED) {
					// Select "About" in the main menu, if not selected
					selectMenuItemIfNotSelected(this.penlet.menuMain, 2);

					// Main menu must be displayed
					displayMainMenu();
					this.penlet.logger
							.debug("[FSM] Main menu was displayed with active item 2");
				}
				break;
			case FSM_STATE_MAIN_MENU_ABOUT_DISPLAYED:
				if (currentState == FSM_STATE_MAIN_MENU_ABOUT) {
					displayAbout();
					this.penlet.logger.debug("[FSM] About was displayed");
				}
				break;
			case FSM_STATE_LEVEL_MENU_EASY:
				if (currentState == FSM_STATE_MAIN_MENU_START_GAME) {
					// Select "Easy" in the level select menu, if not selected
					selectMenuItemIfNotSelected(this.penlet.menuLevelSelect, 0);

					// Level select menu must be displayed
					displayLevelSelectMenu();
					this.penlet.logger
							.debug("[FSM] Level select menu was displayed");
				} else if (currentState == FSM_STATE_LEVEL_MENU_HARD) {
					// Main menu must be focused to the next item
					this.penlet.menuLevelSelect.focusToPrevious();
					this.penlet.logger
							.debug("[FSM] Level select menu item 0 was activated");
				}
				break;
			case FSM_STATE_LEVEL_MENU_HARD:
				if (currentState == FSM_STATE_LEVEL_MENU_EASY) {
					this.penlet.menuLevelSelect.focusToNext();
					this.penlet.logger
							.debug("[FSM] Level select menu item 1 was activated");
				}
				break;
			case FSM_STATE_HELP_MENU_RULES:
				if (currentState == FSM_STATE_MAIN_MENU_HELP
						|| currentState == FSM_STATE_HELP_MENU_RULES_DISPLAYED) {
					// Select "Rules" in the help menu, if not selected
					selectMenuItemIfNotSelected(this.penlet.menuHelp, 0);

					// Level select menu must be displayed
					displayHelpMenu();
					this.penlet.logger.debug("[FSM] Help menu was displayed");
				} else if (currentState == FSM_STATE_HELP_MENU_HOW_TO_DRAW_BOARD) {
					// Help menu must be focused to the previous item
					this.penlet.menuHelp.focusToPrevious();
					this.penlet.logger
							.debug("[FSM] Help menu item 0 was activated");
				}
				break;
			case FSM_STATE_HELP_MENU_HOW_TO_DRAW_BOARD:
				if (currentState == FSM_STATE_HELP_MENU_HOW_TO_DRAW_BOARD_DISPLAYED) {
					// Select "How to draw board" in the help menu, if not
					// selected
					selectMenuItemIfNotSelected(this.penlet.menuHelp, 1);

					// Level select menu must be displayed
					displayHelpMenu();
					this.penlet.logger.debug("[FSM] Help menu was displayed");
				} else if (currentState == FSM_STATE_HELP_MENU_RULES) {
					// Help menu must be focused to the next item
					this.penlet.menuHelp.focusToNext();
					this.penlet.logger
							.debug("[FSM] Help menu item 1 was activated");
				} else if (currentState == FSM_STATE_HELP_MENU_HOW_TO_PLAY) {
					// Help menu must be focused to the next item
					this.penlet.menuHelp.focusToPrevious();
					this.penlet.logger
							.debug("[FSM] Help menu item 1 was activated");
				}
				break;
			case FSM_STATE_HELP_MENU_HOW_TO_PLAY:
				if (currentState == FSM_STATE_HELP_MENU_HOW_TO_PLAY_DISPLAYED) {
					// Select "How to play" in the help menu, if not selected
					selectMenuItemIfNotSelected(this.penlet.menuHelp, 2);

					// Level select menu must be displayed
					displayHelpMenu();
					this.penlet.logger.debug("[FSM] Help menu was displayed");
				} else if (currentState == FSM_STATE_HELP_MENU_HOW_TO_DRAW_BOARD) {
					// Help menu must be focused to the next item
					this.penlet.menuHelp.focusToNext();
					this.penlet.logger
							.debug("[FSM] Help menu item 2 was activated");
				}
				break;
			case FSM_STATE_HELP_MENU_RULES_DISPLAYED:
				if (currentState == FSM_STATE_HELP_MENU_RULES) {
					displayRules();
					this.penlet.logger.debug("[FSM] Rules was displayed");
				}
				break;
			case FSM_STATE_HELP_MENU_HOW_TO_DRAW_BOARD_DISPLAYED:
				if (currentState == FSM_STATE_HELP_MENU_HOW_TO_DRAW_BOARD) {
					displayHowToDrawBoard();
					this.penlet.logger
							.debug("[FSM] How to draw board was displayed");
				}
				break;
			case FSM_STATE_HELP_MENU_HOW_TO_PLAY_DISPLAYED:
				if (currentState == FSM_STATE_HELP_MENU_HOW_TO_PLAY) {
					displayHowToPlay();
					this.penlet.logger.debug("[FSM] How to play was displayed");
				}
				break;
			case FSM_STATE_DRAW_BOARD_FIRST_VERTICAL_LINE:
				if (currentState == FSM_STATE_LEVEL_MENU_EASY
						|| currentState == FSM_STATE_LEVEL_MENU_HARD) {
					displayDrawFirstVerticalLine();
					this.penlet.logger
							.debug("[FSM] Draw first vertical line was displayed");
				}
				break;
			case FSM_STATE_DRAW_BOARD_SECOND_VERTICAL_LINE:
				if (currentState == FSM_STATE_DRAW_BOARD_FIRST_VERTICAL_LINE) {
					displayDrawSecondVerticalLine();
					this.penlet.logger
							.debug("[FSM] Draw second vertical line was displayed");
				}
				break;
			case FSM_STATE_DRAW_BOARD_FIRST_HORIZONTAL_LINE:
				if (currentState == FSM_STATE_DRAW_BOARD_SECOND_VERTICAL_LINE) {
					displayDrawFirstHorizontalLine();
					this.penlet.logger
							.debug("[FSM] Draw first horizontal line was displayed");
				}
				break;
			case FSM_STATE_DRAW_BOARD_SECOND_HORIZONTAL_LINE:
				if (currentState == FSM_STATE_DRAW_BOARD_FIRST_HORIZONTAL_LINE) {
					displayDrawSecondHorizontalLine();
					this.penlet.logger
							.debug("[FSM] Draw second horizontal line was displayed");
				}
				break;
			case FSM_STATE_GAME_SELECT_PLAYER_ORDER:
				if(currentState == FSM_STATE_DRAW_BOARD_SECOND_HORIZONTAL_LINE)
				{
					boolean humanFirst = this.logic.selectPlayersOrder();
					if (humanFirst) {
						this.eventPlayerSelectedHumanTurnNext();
					} else {
						this.eventPlayerSelectedPenTurnNext();
					}
				}
			default:
				// Unrecognized target state. Rejecting it
				penlet.logger.warn("[FSM] Unrecognized target state: "
						+ transitionState);
				return;
			} // switch (transitionState)

			this.currentState = transitionState;
		} catch (Exception e) {
			penlet.logger.error("[FSM] Exception appears: " + e);
		}
	}

	/**
	 * Displayed Draw first vertical line on the display
	 */
	private void displayDrawFirstVerticalLine() {
		this.penlet.graphics.clearRect();
		this.drawFirstVerticalLine();
		this.penlet.graphics.drawString("First line", 25, 2, 0);
		this.displayDrawing(true);
	}

	/**
	 * Displayed Draw second vertical line on the display
	 */
	private void displayDrawSecondVerticalLine() {
		this.penlet.graphics.clearRect();
		this.drawFirstVerticalLine();
		this.drawSecondVerticalLine();
		this.penlet.graphics.drawString("Second line", 25, 2, 0);
		this.displayDrawing(true);
	}

	/**
	 * Displayed Draw first horizontal line on the display
	 */
	private void displayDrawFirstHorizontalLine() {
		this.penlet.graphics.clearRect();
		this.drawFirstVerticalLine();
		this.drawSecondVerticalLine();
		this.drawFirstHorizontalLine();
		this.penlet.graphics.drawString("Third line", 25, 2, 0);
		this.displayDrawing(true);
	}

	/**
	 * Displayed Draw second horizontal line on the display
	 */
	private void displayDrawSecondHorizontalLine() {
		this.penlet.graphics.clearRect();
		this.drawFirstVerticalLine();
		this.drawSecondVerticalLine();
		this.drawFirstHorizontalLine();
		this.drawSecondHorizontalLine();
		this.penlet.graphics.drawString("Fourth line", 25, 2, 0);
		this.displayDrawing(true);
	}

	/**
	 * Displayed How to play on the display
	 */
	private void displayHowToPlay() {
		displayMessage("This is example HOW TO PLAY string", true);
	}

	/**
	 * Displayed How to draw board on the display
	 */
	private void displayHowToDrawBoard() {
		displayMessage("This is example HOW TO DRAW BOARD string", true);
	}

	/**
	 * Displayed Rules on the display
	 */
	private void displayRules() {
		displayMessage("This is example RULES string", true);
	}

	/**
	 * Displayed about on the display
	 */
	private void displayAbout() {
		displayMessage("This is example about string", true);
	}

	/**
	 * Displayed main menu on the display
	 */
	private void displayMainMenu() {
		this.penlet.display.setCurrent(this.penlet.menuMain);
	}

	/**
	 * Displayed level select menu on the display
	 */
	private void displayLevelSelectMenu() {
		this.penlet.display.setCurrent(this.penlet.menuLevelSelect);
	}

	/**
	 * Displayed help menu on the display
	 */
	private void displayHelpMenu() {
		this.penlet.display.setCurrent(this.penlet.menuHelp);
	}

	public void strokeCreated(long time, Region region,
			PageInstance pageInstance) {
		this.penlet.logger.debug("[FSM] New stroke was created");

		PolyLine line = null;

		if (currentState == FSM_STATE_DRAW_BOARD_FIRST_VERTICAL_LINE
				|| currentState == FSM_STATE_DRAW_BOARD_SECOND_VERTICAL_LINE
				|| currentState == FSM_STATE_DRAW_BOARD_FIRST_HORIZONTAL_LINE
				|| currentState == FSM_STATE_DRAW_BOARD_SECOND_HORIZONTAL_LINE) {

			this.penlet.logger.debug("[FSM] Trying to get line from stroke");

			StrokeStorage ss = new StrokeStorage(pageInstance);
			Stroke stroke = ss.getStroke(time);

			// Create a line, based on the first and last points of the stroke
			int numPoints = stroke.getNumberofVertices();
			penlet.logger.debug("[FSM] Number of vertices in the stroke is "
					+ numPoints);

			if (numPoints >= 2) {
				this.penlet.logger.debug("[FSM] Creating line from two points");
				line = new PolyLine(2);
				line.setXY(0, stroke.getX(0), stroke.getY(0));
				line.setXY(1, stroke.getX(numPoints - 1), stroke
						.getY(numPoints - 1));
			}
		}

		if (currentState == FSM_STATE_DRAW_BOARD_FIRST_VERTICAL_LINE) {
			try {
				this.penlet.logger
						.debug("[FSM] Trying to use this line as first vertical line");
				board.setFirstVerticalLine(line);
				this.penlet.logger
						.debug("[FSM] First vertical line was successfully created");
				this.eventFirstVerticalLineReady();
			} catch (GameBoardLinePointsCountException e) {
				// TODO Auto-generated catch block
				this.penlet.logger.error("GameBoardLinePointsCountException");
				displayErrorDrawFirstVerticalLine();
			} catch (GameBoardLineLengthException e) {
				// TODO Auto-generated catch block
				this.penlet.logger
						.error("GameBoardLineLengthException. Reason: "
								+ e.getReason());
				displayErrorDrawFirstVerticalLine();
			}
		} else if (currentState == FSM_STATE_DRAW_BOARD_SECOND_VERTICAL_LINE) {
			try {
				this.penlet.logger
						.debug("[FSM] Trying to use this line as second vertical line");
				board.setSecondVerticalLine(line);
				this.penlet.logger
						.debug("[FSM] Second vertical line was successfully created");
				this.eventSecondVerticalLineReady();
			} catch (GameBoardLinePointsCountException e) {
				// TODO Auto-generated catch block
				this.penlet.logger.error("GameBoardLinePointsCountException");
				displayErrorDrawSecondVerticalLine();
			} catch (GameBoardLineRequirementsException e) {
				// TODO Auto-generated catch block
				this.penlet.logger.error("GameBoardLineRequirementsException");
				displayErrorDrawSecondVerticalLine();
			} catch (GameBoardLineLengthException e) {
				// TODO Auto-generated catch block
				this.penlet.logger
						.error("GameBoardLineLengthException. Reason: "
								+ e.getReason());
				displayErrorDrawSecondVerticalLine();
			} catch (GameBoardLinePositionException e) {
				// TODO Auto-generated catch block
				this.penlet.logger
						.error("GameBoardLinePositionException. Reason: "
								+ e.getReason());
				displayErrorDrawSecondVerticalLine();
			}
		} else if (currentState == FSM_STATE_DRAW_BOARD_FIRST_HORIZONTAL_LINE) {
			try {
				this.penlet.logger
						.debug("[FSM] Trying to use this line as first horizontal line");
				board.setFirstHorizontalLine(line);
				this.penlet.logger
						.debug("[FSM] First horizontal line was successfully created");
				this.eventFirstHorizontalLineReady();
			} catch (GameBoardLineRequirementsException e) {
				this.penlet.logger.error("GameBoardLineRequirementsException");
				displayErrorDrawFirstHorizontalLine();
			} catch (GameBoardLinePointsCountException e) {
				this.penlet.logger.error("GameBoardLinePointsCountException");
				displayErrorDrawFirstHorizontalLine();
			} catch (GameBoardLinePositionException e) {
				this.penlet.logger
						.error("GameBoardLinePositionException. Reason: "
								+ e.getReason());
				displayErrorDrawFirstHorizontalLine();
			} catch (GameBoardLineLengthException e) {
				this.penlet.logger
						.error("GameBoardLineLengthException. Reason: "
								+ e.getReason());
				displayErrorDrawFirstHorizontalLine();
			}
		} else if (currentState == FSM_STATE_DRAW_BOARD_SECOND_HORIZONTAL_LINE) {
			try {
				this.penlet.logger
						.debug("[FSM] Trying to use this line as second horizontal line");
				board.setSecondHorizontalLine(line);
				this.penlet.logger
						.debug("[FSM] Second horizontal line was successfully created");
				this.eventSecondHorizontalLineReady();
			} catch (GameBoardLineRequirementsException e) {
				this.penlet.logger.error("GameBoardLineRequirementsException");
				displayErrorDrawSecondHorizontalLine();
			} catch (GameBoardLinePointsCountException e) {
				this.penlet.logger.error("GameBoardLinePointsCountException");
				displayErrorDrawSecondHorizontalLine();
			} catch (GameBoardLinePositionException e) {
				this.penlet.logger
						.error("GameBoardLinePositionException. Reason: "
								+ e.getReason());
				displayErrorDrawSecondHorizontalLine();
			} catch (GameBoardLineLengthException e) {
				this.penlet.logger
						.error("GameBoardLineLengthException. Reason: "
								+ e.getReason());
				displayErrorDrawSecondHorizontalLine();
			}
		}
	}

	private void displayErrorDrawSecondHorizontalLine() {
		displayMessage(
				"Second horizontal line is invalid. Please try again or read game help.",
				true);
	}

	private void displayErrorDrawFirstHorizontalLine() {
		displayMessage(
				"First horizontal line is invalid. Please try again or read game help.",
				true);
	}

	private void displayErrorDrawSecondVerticalLine() {
		displayMessage(
				"Second vertical line is invalid. Please try again or read game help.",
				true);
	}

	private void displayErrorDrawFirstVerticalLine() {
		displayMessage(
				"First vertical line is invalid. Please try again or read game help.",
				true);

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
	private void displayMessage(String msg, boolean scroll) {
		this.penlet.label.draw(msg, scroll);
		this.penlet.display.setCurrent(this.penlet.label);
	}

	/**
	 * Displays drawing on the screen. Actual data must be drawed on the
	 * "this.penlet.graphics" object
	 * 
	 * @param scroll
	 *            Indicates if the text has to be scrolled or not.
	 */
	private void displayDrawing(boolean scroll) {
		this.penlet.label.draw(this.penlet.image, null, scroll);
		this.penlet.display.setCurrent(this.penlet.label);
	}

	/**
	 * Draws first vertical line
	 */
	private void drawFirstVerticalLine() {
		this.penlet.graphics.drawLine(6, 1, 6, 17);
	}

	/**
	 * Draws second vertical line
	 */
	private void drawSecondVerticalLine() {
		this.penlet.graphics.drawLine(12, 1, 12, 17);
	}
	
	/**
	 * Draws first horizontal line
	 */
	private void drawFirstHorizontalLine() {
		this.penlet.graphics.drawLine(1, 6, 17, 6);
	}
	
	/**
	 * Draws second vertical line
	 */
	private void drawSecondHorizontalLine() {
		this.penlet.graphics.drawLine(1, 12, 17, 12);
	}
}

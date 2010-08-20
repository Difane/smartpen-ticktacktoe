package com.difane.games.ticktacktoe;

public class FSM {

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
	static public final int FSM_STATE_DRAW_BOARD_SECOND_HOTIZONTAL_LINE = 16;
	static public final int FSM_STATE_GAME_SELECT_PLAYER_ORDER = 17;
	static public final int FSM_STATE_GAME_HUMAN_TURN = 18;
	static public final int FSM_STATE_GAME_PEN_TURN = 19;
	static public final int FSM_STATE_GAME_END_HUMAN_WINS = 20;
	static public final int FSM_STATE_GAME_END_PEN_WINS = 21;
	static public final int FSM_STATE_END = 22;
	
	private BasePenlet penlet;
	
	/**
	 * Constructor
	 */
	public FSM(BasePenlet p) {
		this.penlet = p;
		penlet.logger.debug("[FSM] Constructed");
	}

	/**
	 * This event must be called, when application will be started. It will try
	 * to start application by displaying it's main menu
	 */
	public void eventStartApplication() {
		penlet.logger.debug("[FSM] eventStartApplication received");
	}

	/**
	 * This event must be called, when DOWN in the menu be pressed
	 */
	public boolean eventMenuDown() {
		penlet.logger.debug("[FSM] eventMenuDown received");
		return false;
	}

	/**
	 * This event must be called, when UP in the menu be pressed
	 */
	public boolean eventMenuUp() {
		penlet.logger.debug("[FSM] eventMenuUp received");
		return false;

	}

	/**
	 * This event must be called, when LEFT in the menu be pressed
	 */
	public boolean eventMenuLeft() {
		penlet.logger.debug("[FSM] eventMenuLeft received");
		return false;
	}

	/**
	 * This event must be called, when RIGHT in the menu be pressed
	 */
	public boolean eventMenuRight() {
		penlet.logger.debug("[FSM] eventMenuRight received");
		return false;
	}

	/**
	 * This event must be called, when first board vertical line be ready
	 */
	public void eventFirstVerticalLineReady() {
		penlet.logger.debug("[FSM] eventFirstVerticalLineReady received");
	}

	/**
	 * This event must be called, when second board vertical line be ready
	 */
	public void eventSecondVerticalLineReady() {
		penlet.logger.debug("[FSM] eventSecondVerticalLineReady received");
	}

	/**
	 * This event must be called, when first board horizontal line be ready
	 */
	public void eventFirstHorizontalLineReady() {
		penlet.logger.debug("[FSM] eventFirstHorizontalLineReady received");
	}

	/**
	 * This event must be called, when second board horizontal line be ready
	 */
	public void eventSecondHorizontalLineReady() {
		penlet.logger.debug("[FSM] eventSecondHorizontalLineReady received");
	}

	/**
	 * This event must be called, when player order is selected and next turn is
	 * human's
	 */
	public void eventPlayerSelectedHumanTurnNext() {
		penlet.logger.debug("[FSM] eventPlayerSelectedHumanTurnNext received");
	}

	/**
	 * This event must be called, when player order is selected and next turn is
	 * pen's
	 */
	public void eventPlayerSelectedPenTurnNext() {
		penlet.logger.debug("[FSM] eventPlayerSelectedPenTurnNext received");
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
	 * This event ends application
	 */
	public void eventEndApplication() {
		penlet.logger.debug("[FSM] eventEndApplication received");
	}
}

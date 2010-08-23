package com.difane.games.ticktacktoe;

import java.util.Vector;

public class GameLogic {

	static public final int AI_LEVEL_EASY = 0;
	static public final int AI_LEVEL_HARD = 1;

	static public final int FIELD_EMPTY = 0;
	static public final int FIELD_X = 1;
	static public final int FIELD_O = 4;

	static public final int GAME_STATUS_NOT_COMPLETED = 0;
	static public final int GAME_STATUS_X_WINS = 1;
	static public final int GAME_STATUS_O_WINS = 4;
	static public final int GAME_STATUS_DRAW = 3;

	private int aiType; // FIELD_X or FIELD_O
	private int humanType; // FIELD_X or FIELD_O
	private int aiLevel;

	private int[] fields = new int[10];
	//private int[] fields = {0, 1, 1, 4, 4, 4, 1, 1, 1, 4};
	private int[] ratings = new int[10];
	
	private int[][] statusHelper = { { 0, 0, 0, 0 }, { 0, 1, 2, 3 },
			{ 0, 4, 5, 6 }, { 0, 7, 8, 9 }, { 0, 1, 4, 7 }, { 0, 2, 5, 8 },
			{ 0, 3, 6, 9 }, { 0, 1, 5, 9 }, { 0, 3, 5, 7 } };

	public GameLogic() {
		aiLevel = AI_LEVEL_EASY;
	}

	/**
	 * @param aiLevel
	 *            the aiLevel to set
	 */
	public void setAiLevel(int aiLevel) {
		this.aiLevel = aiLevel;
	}

	/**
	 * Selects player order.
	 * 
	 * @return true, if Human goes first, false otherwise
	 */
	public boolean selectPlayersOrder() {
		humanType = FIELD_X; // Now Human plays X
		aiType = FIELD_O; // Now AI plays O
		return true;
	}

	/**
	 * Makes human turn
	 * 
	 * @param field
	 *            Field (1 to 9), where turn was made
	 * 
	 */
	public void humanTurn(int field) {
		// TODO Write implementation
	}

	/**
	 * Makes ai turn
	 * 
	 * @return Field (1 to 9), where turn was made
	 */
	public int aiTurn() {
		// TODO Write implementation
		int aiTurnResult = 1;
		return aiTurnResult;
	}

	/**
	 * Returns game status
	 * 
	 * @return one of the GAME_STATUS_* values
	 */
	public int getGameStatus() {

		int result = 0;

		int ni;
		int sa;
		int so;
		int sj;

		ni = 5;

		for (int i = 1; i <= 8; i++) {
			sa = 5;
			so = 0;
			for (int j = 1; j <= 3; j++) {
				sj = fields[statusHelper[i][j]];
				sa = sa & sj;
				so = so | sj;
			}
			result = result | sa;
			ni = ni & so;
		}
		if (ni == 5) {
			result = 3;
		}

		return result;
	}

}

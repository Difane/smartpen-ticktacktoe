package com.difane.games.ticktacktoe;

import java.util.Random;

public class GameLogic {

	/**
	 * DI Container
	 */
	protected Container container;

	/**
	 * AI possible levels
	 */
	static public final int AI_LEVEL_EASY = 0;
	static public final int AI_LEVEL_HARD = 1;

	/**
	 * Game board field possible states. FIELD_X and FIELD_O are used also to
	 * track user and pen type in the game
	 */
	static public final int FIELD_EMPTY = 0;
	static public final int FIELD_X = 1;
	static public final int FIELD_O = 4;

	/**
	 * Possible game statuses
	 */
	static public final int GAME_STATUS_NOT_COMPLETED = 0;
	static public final int GAME_STATUS_X_WINS = 1;
	static public final int GAME_STATUS_O_WINS = 4;
	static public final int GAME_STATUS_DRAW = 3;

	/**
	 * Type of the AI in that game "X" or "O" Must be one of the FIELD_X or
	 * FIELD_O
	 */
	private int aiType;

	/**
	 * Type of the Human in that game "X" or "O" Must be one of the FIELD_X or
	 * FIELD_O
	 */
	private int humanType;

	/**
	 * AI level. Must be one of the AI_LEVEL_*
	 */
	private int aiLevel;

	/**
	 * Game fields during the game
	 */
	private int[] fields = new int[10];

	/**
	 * Game fields ratings. Using by AI to make his turn
	 */
	private int[] ratings = new int[10];

	/**
	 * Helper array for calculate game status
	 */
	private int[][] statusHelper = { { 0, 0, 0, 0 }, { 0, 1, 2, 3 },
			{ 0, 4, 5, 6 }, { 0, 7, 8, 9 }, { 0, 1, 4, 7 }, { 0, 2, 5, 8 },
			{ 0, 3, 6, 9 }, { 0, 1, 5, 9 }, { 0, 3, 5, 7 } };

	/**
	 * Helper array for calculate fields rating
	 */
	private int[][] ratingHelper = { { 0, 0, 0, 0, 0 }, { 0, 1, 4, 7, 0 },
			{ 0, 1, 5, 0, 0 }, { 0, 1, 6, 0, 8 }, { 0, 2, 4, 0, 0 },
			{ 0, 2, 5, 7, 8 }, { 0, 2, 6, 0, 0 }, { 0, 3, 4, 0, 8 },
			{ 0, 3, 5, 0, 0 }, { 0, 3, 6, 7, 0 } };

	/**
	 * Helper array, that used during game status calculation and fields rating
	 * calculation to store some intermediate data to prevent it's multiple
	 * recalculation
	 */
	private int[] runtimeHelper = new int[9];

	/**
	 * Current game status
	 */
	private int gameStatus = GAME_STATUS_NOT_COMPLETED;

	/**
	 * Constructor
	 * 
	 * @param c
	 *            DI Container
	 */
	public GameLogic(Container c) {
		this.container = c;

		// AI level is easy by default
		aiLevel = AI_LEVEL_EASY;
		this.getContainer()
			.getLoggerComponent()
			.debug("[GameLogic] Component initiated. AI level was set to AI_LEVEL_EASY by default");
	}

	/**
	 * Sets new AI level
	 * 
	 * @param aiLevel
	 *            the aiLevel to set
	 */
	public void setAiLevel(int aiLevel) {
		this.aiLevel = aiLevel;
		this.getContainer()
			.getLoggerComponent()
			.debug("[GameLogic] AI level was set to "+aiLevel);
	}

	/**
	 * Selects player order. Currently player always starts and plays X
	 * 
	 * @return true, if Human goes first, false otherwise
	 */
	public boolean selectPlayersOrder() {
		humanType = FIELD_X;
		aiType = FIELD_O;
		
		this.getContainer()
			.getLoggerComponent()
			.debug("[GameLogic] Players order was selected. Human plays X, AI pays O. Human starts the game");
		return true;
	}

	/**
	 * Makes human turn
	 * 
	 * @param field
	 *            Field (1 to 9), where turn was made
	 * @return true if ok, false otherwise
	 */
	public boolean humanTurn(int field) {
		if (field < 1 || field > 9) {
			// Invalid field to make a turn.
			// TODO Rewrite to throw an exception
			this.getContainer()
				.getLoggerComponent()
				.debug("[GameLogic] Human tries to make a turn to the incorrect field: "+field);
			return false;
		}

		/*
		 * If game already completed - do nothing
		 */
		if (gameStatus != GAME_STATUS_NOT_COMPLETED) {
			// TODO Rewrite to throw an exception
			
			this.getContainer()
				.getLoggerComponent()
				.debug("[GameLogic] Human tries to make a turn, but the game is already completed");
			return false;
		}

		/*
		 * If field not empty - do nothing
		 */
		if (fields[field] != FIELD_EMPTY) {
			// TODO Rewrite to throw an exception
			
			this.getContainer()
				.getLoggerComponent()
				.debug("[GameLogic] Human tries to make a turn, but target field is not empty and contains: "+fields[field]);
			return false;
		}

		/*
		 * Making a turn
		 */
		fields[field] = humanType;
		
		this.getContainer()
			.getLoggerComponent()
			.debug("[GameLogic] Human makes a turn to the field "+field);
		return true;
	}

	/**
	 * Makes ai turn
	 * 
	 * @return Field (1 to 9), where turn was made
	 */
	public int aiTurn() {
		// There are no turn possible, if game already completed
		if (gameStatus != GAME_STATUS_NOT_COMPLETED) {
			this.getContainer()
				.getLoggerComponent()
				.debug("[GameLogic] AI tries to make a turn, but the game is already completed");
			return -1;
		}

		this.calculateRating();
		Random rand = new Random();

		int m, r, mr, i;

		m = 0;
		r = 0;

		if (aiLevel == AI_LEVEL_EASY) {
			for (i = 1; i <= 9; i++) {
				r = r + ratings[i];
			}
			mr = rand.nextInt(r) + 1;
			for (i = 1; i <= 9; i++) {
				mr = mr - ratings[i];
				if (mr <= 0) {
					fields[i] = aiType;
					this.getContainer()
						.getLoggerComponent()
						.debug("[GameLogic] AI makes a turn to the field "+i);
					return i;
				}
			}
		} else {
			for (i = 1; i <= 9; i++) {
				if (ratings[i] > r) {
					r = ratings[i];
				}
			}
			for (i = 1; i <= 9; i++) {
				if (ratings[i] == r) {
					m = m + 1;
				}
			}
			mr = rand.nextInt(m) + 1;
			m = 0;

			for (i = 1; i <= 9; i++) {
				if (ratings[i] == r) {
					m = m + 1;
					if (m == mr) {
						fields[i] = aiType;
						this.getContainer()
							.getLoggerComponent()
							.debug("[GameLogic] AI makes a turn to the field "+i);
						return i;
					}
				}
			}
		}
		return -1;
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
			runtimeHelper[i] = 0;
			for (int j = 1; j <= 3; j++) {
				sj = fields[statusHelper[i][j]];
				sa = sa & sj;
				so = so | sj;
				runtimeHelper[i] = runtimeHelper[i] + sj;
			}
			result = result | sa;
			ni = ni & so;
		}
		if (ni == 5) {
			result = 3;
		}

		// Storing game status for internal using
		gameStatus = result;

		this.getContainer()
			.getLoggerComponent()
			.debug("[GameLogic] Game status requested. Current status is"+result);
		
		return result;
	}

	/**
	 * Calculates fields rating to use during making AI turn
	 */
	private void calculateRating() {
		int[] s00 = new int[4];
		int[] s11 = new int[4];
		int[] s44 = new int[4];
		int s0, s1, s4, ssj, ii, j, jj, jjj, sj;

		for (ii = 1; ii <= 9; ii++) {
			ratings[ii] = 0;
		}

		for (ii = 1; ii <= 9; ii++) {
			if (fields[ii] == FIELD_EMPTY) {
				ratings[ii] = ratings[ii] + 1;
				s0 = 0;
				s1 = 0;
				s4 = 0;
				for (j = 1; j <= 4; j++) {
					ssj = ratingHelper[ii][j];
					if (ssj != 0) {
						switch (runtimeHelper[ssj]) {
						case 0:
							s00[s0] = ssj;
							s0 = s0 + 1;
							for (jj = 0; jj <= s4 - 1; jj++) {
								for (jjj = 1; jjj <= 3; jjj++) {
									sj = statusHelper[ssj][jjj];
									if (sj != ii) {
										ratings[sj] = ratings[sj] + 100;
									}
								}
							}

							for (jj = 0; jj <= s1 - 1; jj++) {
								for (jjj = 1; jjj <= 3; jjj++) {
									sj = statusHelper[ssj][jjj];
									ratings[sj] = ratings[sj] + 10;
								}
								for (jjj = 1; jjj <= 3; jjj++) {
									sj = statusHelper[s11[jj]][jjj];
									if (sj != ii && fields[sj] == FIELD_EMPTY) {
										ratings[sj] = ratings[sj] + 10;
									}
								}
							}
							break;
						case 1:
							s11[s1] = ssj;
							s1 = s1 + 1;
							if (s1 > 1) {
								ratings[ii] = ratings[ii] + 1000;
								for (jj = 0; jj <= s1 - 1; jj++) {
									for (jjj = 1; jjj <= 3; jjj++) {
										sj = statusHelper[s11[jj]][jjj];
										if (sj != ii
												&& fields[sj] == FIELD_EMPTY) {
											ratings[sj] = ratings[sj] + 1000;
										}
									}
								}
							}
							for (jj = 0; jj <= s0 - 1; jj++) {
								for (jjj = 1; jjj <= 3; jjj++) {
									sj = statusHelper[ssj][jjj];
									if (fields[sj] == FIELD_EMPTY) {
										ratings[sj] = ratings[sj] + 10;
									}
								}
								for (jjj = 1; jjj <= 3; jjj++) {
									sj = statusHelper[s00[jj]][jjj];
									if (sj != ii && fields[sj] == FIELD_EMPTY) {
										ratings[sj] = ratings[sj] + 10;
									}
								}
							}
							break;
						case 2:
							ratings[ii] = ratings[ii] + 100000;
							break;
						case 4:
							s44[s4] = ssj;
							s4 = s4 + 1;
							if (s4 > 1) {
								ratings[ii] = ratings[ii] + 10000;
							}
							for (jj = 0; jj <= s0 - 1; jj++) {
								for (jjj = 1; jjj <= 3; jjj++) {
									sj = statusHelper[s00[jj]][jjj];
									if (sj != ii) {
										ratings[sj] = ratings[sj] + 100;
									}
								}
							}
							break;
						case 8:
							ratings[ii] = ratings[ii] + 1000000;
							break;
						}
					}
				}
			}
		}

		this.getContainer()
			.getLoggerComponent()
			.debug("[GameLogic] Fields ratings was recalculated");
	}

	/**
	 * Returns human type
	 * 
	 * @return Human type
	 */
	public int getHumanType() {
		return humanType;
	}

	/**
	 * Returns board information
	 * 
	 * @return Board information
	 */
	public int[] getFields() {
		return fields;
	}

	/**
	 * Returns container
	 * 
	 * @return container
	 */
	public Container getContainer() {
		return container;
	}

}

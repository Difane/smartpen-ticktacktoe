package com.difane.games.ticktacktoe.exceptions;

public class GameBoardLineException extends Exception {
	
	// Exception reasons
	static public final int REASON_UNKNOWN = -1;
	static public final int REASON_LINE_IS_NOT_HORIZONTAL = 0;
	static public final int REASON_LINE_IS_NOT_VERTICAL = 1;
	static public final int REASON_LINE_TO_SHORT = 2;
	static public final int REASON_LINE_TO_LONG = 3;
	static public final int REASON_MUST_BE_AT_THE_RIGHT = 4;
	static public final int REASON_MUST_BE_AT_THE_BOTTOM = 5;
	static public final int REASON_MUST_BE_NEAR_THE_OTHER_LINES = 6;
	static public final int REASON_MUST_CROSS_BOTH_VERTICAL_LINES = 7;
	static public final int REASON_MUST_CONTAIN_TWO_POINTS = 8;
	static public final int REASON_INVALID_DRAWING_ORDER = 9;
	
	private int reason = REASON_UNKNOWN;

	/**
	 * Initializes Exception with reason
	 * @param reason
	 */
	public GameBoardLineException(int reason) {
		super();
		this.reason = reason;
	}
	
	/**
	 * @return the reason
	 */
	public int getReason() {
		return this.reason;
	}
}

package com.difane.games.ticktacktoe.exceptions;

public class GameBoardLinePositionException extends Exception {
	static public final int REASON_UNKNOWN = -1;
	static public final int REASON_MUST_BE_AT_THE_LEFT = 0;
	static public final int REASON_MUST_BE_AT_THE_RIGHT = 1;
	static public final int REASON_MUST_BE_AT_THE_BOTTOM = 2;
	static public final int REASON_MUST_BE_NEAR_THE_OTHER_LINES = 3;
	
	private int reason;

	public GameBoardLinePositionException() {
		this.reason = REASON_UNKNOWN;
	}

	public GameBoardLinePositionException(int reason) {
		this.reason = reason;
	}

	/**
	 * @return the reason
	 */
	public int getReason() {
		return this.reason;
	}
}

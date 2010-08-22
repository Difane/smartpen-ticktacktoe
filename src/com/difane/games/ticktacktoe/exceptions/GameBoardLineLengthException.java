package com.difane.games.ticktacktoe.exceptions;

public class GameBoardLineLengthException extends Exception {
	static public final int REASON_UNKNOWN = -1;
	static public final int REASON_LINE_TO_SHORT = 0;
	static public final int REASON_LINE_TO_LONG = 1;

	private int reason;

	public GameBoardLineLengthException() {
		this.reason = REASON_UNKNOWN;
	}

	public GameBoardLineLengthException(int reason) {
		this.reason = reason;
	}

	/**
	 * @return the reason
	 */
	public int getReason() {
		return this.reason;
	}
}

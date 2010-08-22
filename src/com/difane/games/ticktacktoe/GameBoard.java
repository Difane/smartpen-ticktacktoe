package com.difane.games.ticktacktoe;

import com.livescribe.geom.Point;
import com.livescribe.geom.PolyLine;
import com.livescribe.geom.Rectangle;

public class GameBoard {

	private PolyLine firstVerticalLine;
	private PolyLine secondVerticalLine;
	private PolyLine firstHorizontalLine;
	private PolyLine secondHorizontalLine;
	private Rectangle[] board;
	private Rectangle boardBox;

	/**
	 * Constructor
	 */
	public GameBoard() {

	}

	/**
	 * Sets first vertical line (lines are from the left to the right)
	 * 
	 * @param firstVerticalLine
	 *            Polyline, that contains 2 points. First point is the top
	 *            point, second point is the bottom point
	 * @return True, if line is valid, false otherwise
	 */
	public boolean setFirstVerticalLine(PolyLine firstVerticalLine) {
		this.firstVerticalLine = firstVerticalLine;
		return false;
	}

	/**
	 * Sets second vertical line (lines are from the left to the right)
	 * 
	 * @param secondVerticalLine
	 *            Polyline, that contains 2 points. First point is the top
	 *            point, second point is the bottom point
	 * @return True, if line is valid, false otherwise
	 */
	public boolean setSecondVerticalLine(PolyLine secondVerticalLine) {
		this.secondVerticalLine = secondVerticalLine;
		return false;
	}

	/**
	 * Sets first horizontal line (lines are from the top to the bottom)
	 * 
	 * @param firstHorizontalLine
	 *            Polyline, that contains 2 points. First point is the left
	 *            point, second point is the right point
	 * @return True, if line is valid, false otherwise
	 */
	public boolean setFirstHorizontalLine(PolyLine firstHorizontalLine) {
		this.firstHorizontalLine = firstHorizontalLine;
		return false;
	}

	/**
	 * Sets second horizontal line (lines are from the top to the bottom)
	 * 
	 * @param secondHorizontalLine
	 *            Polyline, that contains 2 points. First point is the left
	 *            point, second point is the right point
	 * @return True, if line is valid, false otherwise
	 */
	public boolean setSecondHorizontalLine(PolyLine secondHorizontalLine) {
		this.secondHorizontalLine = secondHorizontalLine;
		return false;
	}

	/**
	 * Calculates board cells coordinates, that then will be used to determine,
	 * where turn was made
	 */
	public void calculateBoard() {

	}

	/**
	 * Return field, where turn was made, by coordinates of the user turn Fields
	 * are counted from the left to the right and from the top to the bottom
	 * 
	 * @param p
	 *            Point, where user has dome his turn
	 */
	public void getTurnField(Point p) {

	}

}

package com.difane.games.ticktacktoe;

import com.difane.games.ticktacktoe.exceptions.GameBoardLineLengthException;
import com.difane.games.ticktacktoe.exceptions.GameBoardLinePointsCountException;
import com.difane.games.ticktacktoe.exceptions.GameBoardLinePositionException;
import com.difane.games.ticktacktoe.exceptions.GameBoardLineRequirementsException;
import com.livescribe.afp.Scale;
import com.livescribe.geom.Point;
import com.livescribe.geom.PolyLine;
import com.livescribe.geom.Rectangle;

public class GameBoard {

	private PolyLine firstVerticalLine;
	private Float firstVerticalLineLength;
	private PolyLine secondVerticalLine;
	private Float secondVerticalLineLength;
	private PolyLine firstHorizontalLine;
	private Float firstHotizontalLineLength;
	private PolyLine secondHorizontalLine;
	private Float secondHorizontalLineLength;
	private Rectangle[] board;
	private Rectangle boardBox;

	/**
	 * Constructor
	 */
	public GameBoard() {
		this.firstVerticalLine = null;
		this.secondVerticalLine = null;
		this.firstHorizontalLine = null;
		this.secondHorizontalLine = null;

		this.firstVerticalLineLength = null;
		this.secondVerticalLineLength = null;
		this.firstHotizontalLineLength = null;
		this.secondHorizontalLineLength = null;
	}

	/**
	 * Sets first vertical line (lines are from the left to the right)
	 * 
	 * @param firstVerticalLine
	 *            Polyline, that contains 2 points. First point is the top
	 *            point, second point is the bottom point
	 * @throws GameBoardLinePointsCountException
	 * @throws GameBoardLineLengthException
	 */
	public void setFirstVerticalLine(PolyLine firstVerticalLine)
			throws GameBoardLinePointsCountException,
			GameBoardLineLengthException {
		/*
		 * If polyline has other count of points, than 2 - it is an error
		 */
		this.validateLinePointsCount(firstVerticalLine);

		/*
		 * At first - we will normalize vertical line and assume, that line is
		 * true vertical and was moved from the top point to the bottom, so we
		 * will use following vertical line coords: Top point - (X of the top
		 * point, Y of the top point) Bottom point - (X of the top point, Y of
		 * the bottom point)
		 */
		firstVerticalLine.setX(1, firstVerticalLine.getX(0));

		/*
		 * Now we must check, that line has required length
		 */
		float length = Scale.auToMM(firstVerticalLine.getY(1)
				- firstVerticalLine.getY(0));

		if (length < 10) { // Less than one Centimeter
			throw new GameBoardLineLengthException();
		}

		this.firstVerticalLine = firstVerticalLine;
		this.firstVerticalLineLength = length;
	}

	/**
	 * Sets second vertical line (lines are from the left to the right)
	 * 
	 * @param secondVerticalLine
	 *            Polyline, that contains 2 points. First point is the top
	 *            point, second point is the bottom point
	 * @throws GameBoardLinePointsCountException
	 * @throws GameBoardLineRequirementsException
	 * @throws GameBoardLineLengthException
	 * @throws GameBoardLinePositionException
	 */
	public void setSecondVerticalLine(PolyLine secondVerticalLine)
			throws GameBoardLinePointsCountException,
			GameBoardLineRequirementsException, GameBoardLineLengthException,
			GameBoardLinePositionException {
		/*
		 * If first vertical line was not set - it's error
		 */
		if (null == this.firstVerticalLine) {
			throw new GameBoardLineRequirementsException();
		}
		/*
		 * If polyline has other count of points, than 2 - it is an error
		 */
		this.validateLinePointsCount(secondVerticalLine);

		/*
		 * At first - we will normalize vertical line and assume, that line is
		 * true vertical and was moved from the top point to the bottom, so we
		 * will use following vertical line coords: Top point - (X of the top
		 * point, Y of the top point) Bottom point - (X of the top point, Y of
		 * the bottom point)
		 */
		secondVerticalLine.setX(1, secondVerticalLine.getX(0));

		/*
		 * Now we must check, that line position is correct.
		 */
		/*
		 * 1. Line must be at the right of the first line
		 */

		if (secondVerticalLine.getX(0) <= firstVerticalLine.getX(0)) {
			throw new GameBoardLinePositionException(
					GameBoardLinePositionException.REASON_MUST_BE_AT_THE_RIGHT);
		}

		/*
		 * 2. Distance between lines must be greater than 1/4 of the first line
		 * length and less than 1/2 of the first line length
		 */
		int lineDistance = secondVerticalLine.getX(0)
				- this.firstVerticalLine.getX(0);

		if ((lineDistance < this.firstVerticalLineLength / 4)
				|| (lineDistance > this.firstVerticalLineLength / 2)) {
			throw new GameBoardLinePositionException(
					GameBoardLinePositionException.REASON_MUST_BE_NEAR_THE_OTHER_LINES);
		}

		/*
		 * 3. If YF is Y of the top point of the first line and YS is Y of the
		 * top point of the second line and YFL is length of the first line,
		 * than YS must be greater, than YF-0.25YFL and YS must be less than
		 * YF+0.25YFL
		 */
		int minY = (int) (this.firstVerticalLine.getY(0) - this.firstVerticalLineLength / 4);
		int maxY = (int) (this.firstVerticalLine.getY(0) + this.firstVerticalLineLength/4);

		if (secondVerticalLine.getY(0) < minY) {
			throw new GameBoardLinePositionException(
					GameBoardLinePositionException.REASON_MUST_BE_NEAR_THE_OTHER_LINES);
		}
		if (secondVerticalLine.getY(0) > maxY) {
			throw new GameBoardLinePositionException(
					GameBoardLinePositionException.REASON_MUST_BE_NEAR_THE_OTHER_LINES);
		}
		
		/*
		 * Now we must check, that line has required length
		 */
		float length = Scale.auToMM(secondVerticalLine.getY(1)
				- secondVerticalLine.getY(0));

		float minLength = (this.firstVerticalLineLength * 2) / 3;
		float maxLength = (this.firstVerticalLineLength * 5) / 4;

		if (length < minLength) {
			throw new GameBoardLineLengthException(
					GameBoardLineLengthException.REASON_LINE_TO_SHORT);
		}
		if (length > maxLength) {
			throw new GameBoardLineLengthException(
					GameBoardLineLengthException.REASON_LINE_TO_LONG);
		}

		this.secondVerticalLine = secondVerticalLine;
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

	/**
	 * Validates count of the points in the line. If it differs from 2 throws an
	 * exception
	 * 
	 * @param l
	 *            Polyline, that must be checked
	 * @throws GameBoardLinePointsCountException
	 */
	private void validateLinePointsCount(PolyLine l)
			throws GameBoardLinePointsCountException {
		if (2 != l.getNumberofVertices()) {
			throw new GameBoardLinePointsCountException();
		}
	}

}

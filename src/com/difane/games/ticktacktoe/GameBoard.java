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
	private int firstVerticalLineLength;
	private PolyLine secondVerticalLine;
	private int secondVerticalLineLength;
	private PolyLine firstHorizontalLine;
	private int firstHorizontalLineLength;
	private PolyLine secondHorizontalLine;
	private int secondHorizontalLineLength;
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

		this.firstVerticalLineLength = 0;
		this.secondVerticalLineLength = 0;
		this.firstHorizontalLineLength = 0;
		this.secondHorizontalLineLength = 0;
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
		int length = firstVerticalLine.getY(1) - firstVerticalLine.getY(0);
		float lengthInMM = Scale.auToMM(length);

		if (lengthInMM < 10) { // Less than one Centimeter
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
		int maxY = (int) (this.firstVerticalLine.getY(0) + this.firstVerticalLineLength / 4);

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
		int length = secondVerticalLine.getY(1) - secondVerticalLine.getY(0);
		float lengthInMM = Scale.auToMM(length);

		if (lengthInMM < 10) { // Less than one Centimeter
			throw new GameBoardLineLengthException();
		}

		int minLength = (this.firstVerticalLineLength * 2) / 3;
		int maxLength = (this.firstVerticalLineLength * 5) / 4;

		if (length < minLength) {
			throw new GameBoardLineLengthException(
					GameBoardLineLengthException.REASON_LINE_TO_SHORT);
		}
		if (length > maxLength) {
			throw new GameBoardLineLengthException(
					GameBoardLineLengthException.REASON_LINE_TO_LONG);
		}

		this.secondVerticalLine = secondVerticalLine;
		this.secondVerticalLineLength = length;
	}

	/**
	 * Sets first horizontal line (lines are from the top to the bottom)
	 * 
	 * @param firstHorizontalLine
	 *            Polyline, that contains 2 points. First point is the left
	 *            point, second point is the right point
	 * @throws GameBoardLineRequirementsException
	 * @throws GameBoardLinePointsCountException
	 * @throws GameBoardLinePositionException
	 * @throws GameBoardLineLengthException
	 */
	public void setFirstHorizontalLine(PolyLine firstHorizontalLine)
			throws GameBoardLineRequirementsException,
			GameBoardLinePointsCountException, GameBoardLinePositionException,
			GameBoardLineLengthException {
		/*
		 * If first and second vertical line was not set - it's error
		 */
		if (null == this.firstVerticalLine || null == this.secondVerticalLine) {
			throw new GameBoardLineRequirementsException();
		}
		/*
		 * If polyline has other count of points, than 2 - it is an error
		 */
		this.validateLinePointsCount(firstHorizontalLine);

		/*
		 * At first - we will normalize horizontal line and assume, that line is
		 * true horizontal and was moved from the left point to the right, so we
		 * will use following horizontal line coords: Left point - (X of the
		 * left point, Y of the left point) Right point - (X of the right point,
		 * Y of the left point)
		 */
		firstHorizontalLine.setY(1, firstHorizontalLine.getY(0));

		/*
		 * Now we must check, that line position is correct.
		 */
		/*
		 * 1. Line must start at the left from first vertical line and end at
		 * the right from second vertical line
		 */
		if (firstHorizontalLine.getX(0) > this.firstVerticalLine.getX(0)) {
			throw new GameBoardLinePositionException(
					GameBoardLinePositionException.REASON_MUST_BE_AT_THE_LEFT);
		}

		if (firstHorizontalLine.getX(1) < this.secondVerticalLine.getX(0)) {
			throw new GameBoardLinePositionException(
					GameBoardLinePositionException.REASON_MUST_BE_AT_THE_RIGHT);
		}

		/*
		 * 2. Distance by X between start of the first horizontal line and first
		 * vertical line must be greater than 1/4 of the first line length and
		 * less than 1/2 of the first line length
		 */

		int distance = this.firstVerticalLine.getX(0)
				- firstHorizontalLine.getX(0);

		int minDistance = (int) (this.firstVerticalLineLength / 4);
		int maxDistance = (int) (this.firstVerticalLineLength / 2);

		if (distance < minDistance || distance > maxDistance) {
			throw new GameBoardLinePositionException(
					GameBoardLinePositionException.REASON_MUST_BE_NEAR_THE_OTHER_LINES);
		}

		/*
		 * Now we must check, that line has required length
		 */
		int length = firstHorizontalLine.getX(1) - firstHorizontalLine.getX(0);
		float lengthInMM = Scale.auToMM(length);

		if (lengthInMM < 10) { // Less than one Centimeter
			throw new GameBoardLineLengthException();
		}

		int minLength = (this.firstVerticalLineLength * 2) / 3;
		int maxLength = (this.firstVerticalLineLength * 5) / 4;

		if (length < minLength) {
			throw new GameBoardLineLengthException(
					GameBoardLineLengthException.REASON_LINE_TO_SHORT);
		}
		if (length > maxLength) {
			throw new GameBoardLineLengthException(
					GameBoardLineLengthException.REASON_LINE_TO_LONG);
		}

		this.firstHorizontalLine = firstHorizontalLine;
		this.firstHorizontalLineLength = length;
	}

	/**
	 * Sets second horizontal line (lines are from the top to the bottom)
	 * 
	 * @param secondHorizontalLine
	 *            Polyline, that contains 2 points. First point is the left
	 *            point, second point is the right point
	 * @throws GameBoardLineRequirementsException
	 * @throws GameBoardLinePointsCountException
	 * @throws GameBoardLinePositionException
	 * @throws GameBoardLineLengthException 
	 */
	public void setSecondHorizontalLine(PolyLine secondHorizontalLine)
			throws GameBoardLineRequirementsException,
			GameBoardLinePointsCountException, GameBoardLinePositionException, GameBoardLineLengthException {

		/*
		 * If first and second vertical and first horizontal lines was not set -
		 * it's error
		 */
		if (null == this.firstVerticalLine || null == this.secondVerticalLine
				|| null == this.firstHorizontalLine) {
			throw new GameBoardLineRequirementsException();
		}
		/*
		 * If polyline has other count of points, than 2 - it is an error
		 */
		this.validateLinePointsCount(secondHorizontalLine);

		/*
		 * At first - we will normalize horizontal line and assume, that line is
		 * true horizontal and was moved from the left point to the right, so we
		 * will use following horizontal line coords: Left point - (X of the
		 * left point, Y of the left point) Right point - (X of the right point,
		 * Y of the left point)
		 */
		secondHorizontalLine.setY(1, secondHorizontalLine.getY(0));

		/*
		 * Now we must check, that line position is correct.
		 */
		/*
		 * 1. Line must start at the left from first vertical line and end at
		 * the right from second vertical line
		 */
		if (secondHorizontalLine.getX(0) > this.firstVerticalLine.getX(0)) {
			throw new GameBoardLinePositionException(
					GameBoardLinePositionException.REASON_MUST_BE_AT_THE_LEFT);
		}

		if (secondHorizontalLine.getX(1) < this.secondVerticalLine.getX(0)) {
			throw new GameBoardLinePositionException(
					GameBoardLinePositionException.REASON_MUST_BE_AT_THE_RIGHT);
		}

		/*
		 * 2. Line must be at the bottom of the first horizontal line
		 */

		if (secondHorizontalLine.getY(0) <= firstHorizontalLine.getY(0)) {
			throw new GameBoardLinePositionException(
					GameBoardLinePositionException.REASON_MUST_BE_AT_THE_BOTTOM);
		}

		/*
		 * 3. Distance between lines must be greater than 1/4 of the first line
		 * length and less than 1/2 of the first line length
		 */
		int lineDistance = secondHorizontalLine.getY(0)
				- this.firstHorizontalLine.getY(0);

		if ((lineDistance < this.firstHorizontalLineLength / 4)
				|| (lineDistance > this.firstHorizontalLineLength / 2)) {
			throw new GameBoardLinePositionException(
					GameBoardLinePositionException.REASON_MUST_BE_NEAR_THE_OTHER_LINES);
		}

		/*
		 * 4. If XF is X of the left point of the first line and XS is X of the
		 * left point of the second line and XFL is length of the first line,
		 * than XS must be greater, than XF-0.25XFL and XS must be less than
		 * XF+0.25XFL
		 */
		int minX = (int) (this.firstHorizontalLine.getX(0) - this.firstHorizontalLineLength / 4);
		int maxX = (int) (this.firstHorizontalLine.getX(0) + this.firstHorizontalLineLength / 4);

		if (secondHorizontalLine.getX(0) < minX) {
			throw new GameBoardLinePositionException(
					GameBoardLinePositionException.REASON_MUST_BE_NEAR_THE_OTHER_LINES);
		}
		if (secondHorizontalLine.getX(0) > maxX) {
			throw new GameBoardLinePositionException(
					GameBoardLinePositionException.REASON_MUST_BE_NEAR_THE_OTHER_LINES);
		}

		/*
		 * Now we must check, that line has required length
		 */
		int length = secondHorizontalLine.getX(1)
				- secondHorizontalLine.getX(0);
		float lengthInMM = Scale.auToMM(length);
		
		if (lengthInMM < 10) { // Less than one Centimeter
			throw new GameBoardLineLengthException();
		}

		int minLength = (this.firstHorizontalLineLength * 2) / 3;
		int maxLength = (this.firstHorizontalLineLength * 5) / 4;

		if (length < minLength) {
			throw new GameBoardLineLengthException(
					GameBoardLineLengthException.REASON_LINE_TO_SHORT);
		}
		if (length > maxLength) {
			throw new GameBoardLineLengthException(
					GameBoardLineLengthException.REASON_LINE_TO_LONG);
		}

		this.secondHorizontalLine = secondHorizontalLine;
		this.secondHorizontalLineLength = length;
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

	/**
	 * Computes the intersection between two lines. The calculated point is
	 * approximate, since integers are used. If you need a more precise result,
	 * use doubles everywhere. (c) 2007 Alexander Hristov. Use Freely (LGPL
	 * license). http://www.ahristov.com
	 * 
	 * @param x1
	 *            Point 1 of Line 1
	 * @param y1
	 *            Point 1 of Line 1
	 * @param x2
	 *            Point 2 of Line 1
	 * @param y2
	 *            Point 2 of Line 1
	 * @param x3
	 *            Point 1 of Line 2
	 * @param y3
	 *            Point 1 of Line 2
	 * @param x4
	 *            Point 2 of Line 2
	 * @param y4
	 *            Point 2 of Line 2
	 * @return Point where the segments intersect, or null if they don't
	 */
	public Point intersection(int x1, int y1, int x2, int y2, int x3, int y3,
			int x4, int y4) {
		int d = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);
		if (d == 0)
			return null;

		int xi = ((x3 - x4) * (x1 * y2 - y1 * x2) - (x1 - x2)
				* (x3 * y4 - y3 * x4))
				/ d;
		int yi = ((y3 - y4) * (x1 * y2 - y1 * x2) - (y1 - y2)
				* (x3 * y4 - y3 * x4))
				/ d;

		return new Point(xi, yi);
	}

}

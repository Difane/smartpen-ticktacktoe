package com.difane.games.ticktacktoe;

import java.util.Vector;

import com.difane.games.ticktacktoe.exceptions.GameBoardImpossibleException;
import com.difane.games.ticktacktoe.exceptions.GameBoardLineIsNotHorizontalException;
import com.difane.games.ticktacktoe.exceptions.GameBoardLineIsNotVerticalException;
import com.difane.games.ticktacktoe.exceptions.GameBoardLineLengthException;
import com.difane.games.ticktacktoe.exceptions.GameBoardLinePointsCountException;
import com.difane.games.ticktacktoe.exceptions.GameBoardLinePositionException;
import com.difane.games.ticktacktoe.exceptions.GameBoardLineRequirementsException;
import com.difane.geom.Line;
import com.livescribe.afp.Scale;
import com.livescribe.geom.Point;
import com.livescribe.geom.PolyLine;
import com.livescribe.geom.Rectangle;

public class GameBoard {

	/*
	 * DI Container
	 */
	private Container container;

	/*
	 * Board lines
	 */
	private PolyLine firstVerticalLine;
	private PolyLine secondVerticalLine;
	private PolyLine firstHorizontalLine;
	private PolyLine secondHorizontalLine;

	/*
	 * Lengths for each board line
	 */
	//private int firstVerticalLineLength;
	//private int secondVerticalLineLength;
	//private int firstHorizontalLineLength;
	//private int secondHorizontalLineLength;

	/*
	 * Vector, that contains board fields rectangles
	 */
	private Vector board;
	
	/*
	 * Line drawing precision in grad
	 */
	private static final double LINE_PRECISION = 10;

	/**
	 * Constructor
	 * 
	 * @param DI
	 *            container
	 */
	public GameBoard(Container c) {
		this.container = c;

		this.firstVerticalLine = null;
		this.secondVerticalLine = null;
		this.firstHorizontalLine = null;
		this.secondHorizontalLine = null;

//		this.firstVerticalLineLength = 0;
//		this.secondVerticalLineLength = 0;
//		this.firstHorizontalLineLength = 0;
//		this.secondHorizontalLineLength = 0;

		this.board = new Vector(10);

		this.getContainer()
			.getLoggerComponent()
			.debug("[GameBoard] Component initialized");
	}

	/**
	 * Sets first vertical line (lines are from the left to the right)
	 * 
	 * @param firstVerticalLine
	 *            Polyline, that contains 2 points. First point is the top
	 *            point, second point is the bottom point
	 * @throws GameBoardLinePointsCountException 
	 * @throws GameBoardLineIsNotVerticalException 
	 * @throws GameBoardLineLengthException 
	 */
	public void setFirstVerticalLine(PolyLine firstVerticalLine) throws GameBoardLinePointsCountException, GameBoardLineIsNotVerticalException, GameBoardLineLengthException {
		/*
		 * If polyline has other count of points, than 2 - it is an error
		 */
		this.validateLinePointsCount(firstVerticalLine);
		
		/*
		 * Line must be vertical
		 */
		this.validateLineVerticality(firstVerticalLine);

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
		validateLineLength(firstVerticalLine);

		this.firstVerticalLine = firstVerticalLine;

		this.getContainer()
			.getLoggerComponent()
			.debug("[GameBoard] FirstVerticalLine: " + this.firstVerticalLine);
	}

	/**
	 * Sets second vertical line (lines are from the left to the right)
	 * 
	 * @param secondVerticalLine
	 *            Polyline, that contains 2 points. First point is the top
	 *            point, second point is the bottom point
	 * @throws GameBoardLineRequirementsException 
	 * @throws GameBoardLinePointsCountException 
	 * @throws GameBoardLineIsNotVerticalException 
	 * @throws GameBoardLineLengthException 
	 * @throws GameBoardLinePositionException 
	 */
	public void setSecondVerticalLine(PolyLine secondVerticalLine) throws GameBoardLineRequirementsException, GameBoardLinePointsCountException, GameBoardLineIsNotVerticalException, GameBoardLineLengthException, GameBoardLinePositionException {
		/*
		 * If first vertical line was not set - it's error
		 */
		if (null == this.firstVerticalLine) {
			this.getContainer()
				.getLoggerComponent()
				.error("[GameBoard] FirstVerticalLine was not set previously");
			throw new GameBoardLineRequirementsException();
		}
		/*
		 * If polyline has other count of points, than 2 - it is an error
		 */
		this.validateLinePointsCount(secondVerticalLine);
		
		/*
		 * Line must be vertical
		 */
		this.validateLineVerticality(secondVerticalLine);

		/*
		 * At first - we will normalize vertical line and assume, that line is
		 * true vertical and was moved from the top point to the bottom, so we
		 * will use following vertical line coords: Top point - (X of the top
		 * point, Y of the top point) Bottom point - (X of the top point, Y of
		 * the bottom point)
		 */
		secondVerticalLine.setX(1, secondVerticalLine.getX(0));
		
		/*
		 * Now we must check, that line has required length
		 */
		validateLineLength(secondVerticalLine);

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
		 * 2. It must be possible to cross both lines with one horizontal line
		 */
		
		if (((secondVerticalLine.getY(0) < firstVerticalLine.getY(0)) 
				&& (secondVerticalLine.getY(1) < firstVerticalLine.getY(0)))
				|| ((secondVerticalLine.getY(0) > firstVerticalLine.getY(1)) 
				&& (secondVerticalLine.getY(1) > firstVerticalLine.getY(1)))) {
			throw new GameBoardLinePositionException(
				GameBoardLinePositionException.REASON_MUST_BE_NEAR_THE_OTHER_LINES);
		}

		this.secondVerticalLine = secondVerticalLine;

		this.getContainer()
			.getLoggerComponent()
			.debug("[GameBoard] secondVerticalLine: " + this.secondVerticalLine);
	}

	/**
	 * Sets first horizontal line (lines are from the top to the bottom)
	 * 
	 * @param firstHorizontalLine
	 *            Polyline, that contains 2 points. First point is the left
	 *            point, second point is the right point
	 * @throws GameBoardLineRequirementsException 
	 * @throws GameBoardLinePointsCountException 
	 * @throws GameBoardLineIsNotHorizontalException 
	 * @throws GameBoardLineLengthException 
	 * @throws GameBoardLinePositionException 
	 */
	public void setFirstHorizontalLine(PolyLine firstHorizontalLine) throws GameBoardLineRequirementsException, GameBoardLinePointsCountException, GameBoardLineIsNotHorizontalException, GameBoardLineLengthException, GameBoardLinePositionException {
		/*
		 * If first and second vertical line was not set - it's error
		 */
		if (null == this.firstVerticalLine || null == this.secondVerticalLine) {
			this.getContainer()
				.getLoggerComponent()
				.error("[GameBoard] FirstVerticalLine and SecondVerticalLine was not set previously");
			throw new GameBoardLineRequirementsException();
		}
		/*
		 * If polyline has other count of points, than 2 - it is an error
		 */
		this.validateLinePointsCount(firstHorizontalLine);
		
		/*
		 * Line must be horizontal
		 */
		this.validateLineHorizontality(firstHorizontalLine);

		/*
		 * At first - we will normalize horizontal line and assume, that line is
		 * true horizontal and was moved from the left point to the right, so we
		 * will use following horizontal line coords: Left point - (X of the
		 * left point, Y of the left point) Right point - (X of the right point,
		 * Y of the left point)
		 */
		firstHorizontalLine.setY(1, firstHorizontalLine.getY(0));

		/*
		 * Now we must check, that line has required length
		 */
		validateLineLength(firstHorizontalLine);
		
		/*
		 * Line must cross both vertical lines
		 */
		Point p1, p2;
		p1 = Line.intersection(firstHorizontalLine.getX(0), firstHorizontalLine.getY(0), 
				firstHorizontalLine.getX(1), firstHorizontalLine.getY(1), 
				firstVerticalLine.getX(0), firstVerticalLine.getY(0), 
				firstVerticalLine.getX(1), firstVerticalLine.getY(1));
		p2 = Line.intersection(firstHorizontalLine.getX(0), firstHorizontalLine.getY(0), 
				firstHorizontalLine.getX(1), firstHorizontalLine.getY(1), 
				secondVerticalLine.getX(0), secondVerticalLine.getY(0), 
				secondVerticalLine.getX(1), secondVerticalLine.getY(1));
		
		if (p1 == null || p2 == null) {
			throw new GameBoardLinePositionException(
					GameBoardLinePositionException.REASON_MUST_CROSS_BOTH_VERTICAL_LINES);
		}
			
		this.firstHorizontalLine = firstHorizontalLine;

		this.getContainer()
			.getLoggerComponent()
			.debug("[GameBoard] firstHorizontalLine: " + this.firstHorizontalLine);
	}

	/**
	 * Sets second horizontal line (lines are from the top to the bottom)
	 * 
	 * @param secondHorizontalLine
	 *            Polyline, that contains 2 points. First point is the left
	 *            point, second point is the right point
	 * @throws GameBoardLineRequirementsException 
	 * @throws GameBoardLinePointsCountException 
	 * @throws GameBoardLineIsNotHorizontalException 
	 * @throws GameBoardLineLengthException 
	 * @throws GameBoardLinePositionException 
	 */
	public void setSecondHorizontalLine(PolyLine secondHorizontalLine) throws GameBoardLineRequirementsException, GameBoardLinePointsCountException, GameBoardLineIsNotHorizontalException, GameBoardLineLengthException, GameBoardLinePositionException {

		/*
		 * If first and second vertical and first horizontal lines was not set -
		 * it's error
		 */
		if (null == this.firstVerticalLine || null == this.secondVerticalLine
				|| null == this.firstHorizontalLine) {
			this.getContainer()
				.getLoggerComponent()
				.error("[GameBoard] FirstVerticalLine, SecondVerticalLine and FirstHorizontalLine was not set previously");
			throw new GameBoardLineRequirementsException();
		}
		/*
		 * If polyline has other count of points, than 2 - it is an error
		 */
		this.validateLinePointsCount(secondHorizontalLine);
		
		/*
		 * Line must be horizontal
		 */
		this.validateLineHorizontality(secondHorizontalLine);

		/*
		 * At first - we will normalize horizontal line and assume, that line is
		 * true horizontal and was moved from the left point to the right, so we
		 * will use following horizontal line coords: Left point - (X of the
		 * left point, Y of the left point) Right point - (X of the right point,
		 * Y of the left point)
		 */
		secondHorizontalLine.setY(1, secondHorizontalLine.getY(0));

		/*
		 * Now we must check, that line has required length
		 */
		validateLineLength(secondHorizontalLine);
		
		/*
		 * Line must cross both vertical lines
		 */
		Point p1, p2;
		p1 = Line.intersection(secondHorizontalLine.getX(0), secondHorizontalLine.getY(0), 
				secondHorizontalLine.getX(1), secondHorizontalLine.getY(1), 
				firstVerticalLine.getX(0), firstVerticalLine.getY(0), 
				firstVerticalLine.getX(1), firstVerticalLine.getY(1));
		p2 = Line.intersection(secondHorizontalLine.getX(0), secondHorizontalLine.getY(0), 
				secondHorizontalLine.getX(1), secondHorizontalLine.getY(1), 
				secondVerticalLine.getX(0), secondVerticalLine.getY(0), 
				secondVerticalLine.getX(1), secondVerticalLine.getY(1));
		
		if (p1 == null || p2 == null) {
			throw new GameBoardLinePositionException(
					GameBoardLinePositionException.REASON_MUST_CROSS_BOTH_VERTICAL_LINES);
		}
		
		
		/*
		 * Line must be at the bottom of the first horizontal line
		 */

		if (secondHorizontalLine.getY(0) <= firstHorizontalLine.getY(0)) {
			throw new GameBoardLinePositionException(
					GameBoardLinePositionException.REASON_MUST_BE_AT_THE_BOTTOM);
		}

		this.secondHorizontalLine = secondHorizontalLine;

		this.getContainer()
			.getLoggerComponent()
			.debug("[GameBoard] secondHorizontalLine: " + this.secondHorizontalLine);
	}

	/**
	 * Calculates board cells coordinates, that then will be used to determine,
	 * where turn was made
	 * 
	 * @throws GameBoardImpossibleException
	 */
	public void calculateBoard() throws GameBoardImpossibleException {
		// At first calculating 4 points, that are points of intersection
		// between lines

		// 1. First point - first vertical line and first horizontal line
		Point tlPoint = Line.intersection(firstVerticalLine.getX(0),
				firstVerticalLine.getY(0), firstVerticalLine.getX(1),
				firstVerticalLine.getY(1), firstHorizontalLine.getX(0),
				firstHorizontalLine.getY(0), firstHorizontalLine.getX(1),
				firstHorizontalLine.getY(1));

		this.getContainer()
			.getLoggerComponent()
			.debug("[GameBoard] Top-left point of intersection: " + tlPoint);

		// 2. Second point - second vertical line and first horizontal line
		Point trPoint = Line.intersection(secondVerticalLine.getX(0),
				secondVerticalLine.getY(0), secondVerticalLine.getX(1),
				secondVerticalLine.getY(1), firstHorizontalLine.getX(0),
				firstHorizontalLine.getY(0), firstHorizontalLine.getX(1),
				firstHorizontalLine.getY(1));

		this.getContainer()
			.getLoggerComponent()
			.debug("[GameBoard] Top-right point of intersection: " + trPoint);

		// 3. Third point - first vertical line and second horizontal line
		Point blPoint = Line.intersection(firstVerticalLine.getX(0),
				firstVerticalLine.getY(0), firstVerticalLine.getX(1),
				firstVerticalLine.getY(1), secondHorizontalLine.getX(0),
				secondHorizontalLine.getY(0), secondHorizontalLine.getX(1),
				secondHorizontalLine.getY(1));

		this.getContainer()
			.getLoggerComponent()
			.debug("[GameBoard] Bottom-left point of intersection: " + blPoint);

		// 4. Fourth point - second vertical line and second horizontal line
		Point brPoint = Line.intersection(secondVerticalLine.getX(0),
				secondVerticalLine.getY(0), secondVerticalLine.getX(1),
				secondVerticalLine.getY(1), secondHorizontalLine.getX(0),
				secondHorizontalLine.getY(0), secondHorizontalLine.getX(1),
				secondHorizontalLine.getY(1));

		this.getContainer()
			.getLoggerComponent()
			.debug("[GameBoard] Bottom-right point of intersection: " + brPoint);

		// If there are no all 4 intersections - board is impossible :)
		if (null == tlPoint || null == trPoint || null == blPoint
				|| null == brPoint) {
			throw new GameBoardImpossibleException();
		}

		// Now constructing 9 rectangles for 9 board fields
		
		// At first calculating width and height of the rectangles
		int rWidth = trPoint.getX() - tlPoint.getX();
		int rHeight = blPoint.getY() - tlPoint.getY();		
		
		// At then getting four helper values for board corners
		int topMax = 0;
		
		// Checking page top
		if (tlPoint.getY() > rHeight) {
			topMax = tlPoint.getY() - rHeight;
		}
		
		int bottomMax = blPoint.getY() + rHeight;
		int leftMax = 0;
		
		if (tlPoint.getX() > rWidth) {
			leftMax = tlPoint.getX() - rWidth;
		}
		
		int rightMax = trPoint.getX() + rWidth;

		this.getContainer()
			.getLoggerComponent()
			.debug("[GameBoard] Top maximal value: " + topMax);
		this.getContainer()
			.getLoggerComponent()
			.debug("[GameBoard] Bottom maximal value: " + bottomMax);
		this.getContainer()
			.getLoggerComponent()
			.debug("[GameBoard] Left maximal value: " + leftMax);
		this.getContainer()
			.getLoggerComponent()
			.debug("[GameBoard] Right maximal value: " + rightMax);

		// Element with zero index (to be arranged with existing game algo
		board.addElement(new Rectangle());

		//TODO Refactoring needed to use only one rectangle
		// Rectangle #1		
		Rectangle r1 = new Rectangle(leftMax, topMax, rWidth, rHeight);
		board.addElement(r1);
		
		this.getContainer()
			.getLoggerComponent()
			.debug("[GameBoard] Rectangle for board field #1: " + r1);

		// Rectangle #2
		Rectangle r2 = new Rectangle(tlPoint.getX(), topMax, rWidth, rHeight);
		board.addElement(r2);
		
		this.getContainer()
			.getLoggerComponent()
			.debug("[GameBoard] Rectangle for board field #2: " + r2);

		// Rectangle #3
		Rectangle r3 = new Rectangle(trPoint.getX(), topMax, rWidth, rHeight);
		board.addElement(r3);
		
		this.getContainer()
			.getLoggerComponent()
			.debug("[GameBoard] Rectangle for board field #3: " + r3);

		// Rectangle #4
		Rectangle r4 = new Rectangle(leftMax, tlPoint.getY(), rWidth, rHeight);
		board.addElement(r4);
		
		this.getContainer()
			.getLoggerComponent()
			.debug("[GameBoard] Rectangle for board field #4: " + r4);

		// Rectangle #5
		Rectangle r5 = new Rectangle(tlPoint.getX(), tlPoint.getY(), rWidth, rHeight);
		board.addElement(r5);
		
		this.getContainer()
			.getLoggerComponent()
			.debug("[GameBoard] Rectangle for board field #5: " + r5);

		// Rectangle #6
		Rectangle r6 = new Rectangle(trPoint.getX(), trPoint.getY(), rWidth, rHeight);
		board.addElement(r6);
		
		this.getContainer()
			.getLoggerComponent()
			.debug("[GameBoard] Rectangle for board field #6: " + r6);

		// Rectangle #7
		Rectangle r7 = new Rectangle(leftMax, blPoint.getY(), rWidth, rHeight);
		board.addElement(r7);
		
		this.getContainer()
			.getLoggerComponent()
			.debug("[GameBoard] Rectangle for board field #7: " + r7);

		// Rectangle #8
		Rectangle r8 = new Rectangle(blPoint.getX(), blPoint.getY(), rWidth, rHeight);
		board.addElement(r8);
		
		this.getContainer()
			.getLoggerComponent()
			.debug("[GameBoard] Rectangle for board field #8: " + r8);

		// Rectangle #9
		Rectangle r9 = new Rectangle(brPoint.getX(), brPoint.getY(), rWidth, rHeight);
		board.addElement(r9);
		
		this.getContainer()
			.getLoggerComponent()
			.debug("[GameBoard] Rectangle for board field #9: " + r9);
	}

	/**
	 * Return field, where turn was made, by coordinates of the user turn Fields
	 * are counted from the left to the right and from the top to the bottom
	 * 
	 * @param p
	 *            Point, where user has dome his turn
	 * @return number from 1 to 9, if turn was made to the one of the cells, -1
	 *         otherwise
	 */
	public int getTurnField(Point p) {
		for (int i = 1; i < board.size(); i++) {
			Rectangle r = (Rectangle) board.elementAt(i);

			this.getContainer().
				getLoggerComponent().
				debug("[GameBoard] Checking field #" + i + ". It's rectangle: " + r
							+ ". Point to check: " + p);
			if (r.contains(p.getX(), p.getY())) {
				this.getContainer().
					getLoggerComponent().
					debug("[GameBoard] Found target field. It's number is "+i);
				return i;
			}
		}
		this.getContainer().
			getLoggerComponent().
			debug("[GameBoard] No one of the fields rectangles contains passed point");
		return -1;
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
	 * Validates, that line is vertical (The angle between line and vertical line,
	 * that are drawed from the line top point, is inside given range
	 * 
	 * @param line
	 *            Line to validate
	 * @return true, if line can be used as vertical, false otherwise
	 * @throws GameBoardLineIsNotVerticalException 
	 */
	private void validateLineVerticality(PolyLine line) throws GameBoardLineIsNotVerticalException
	{
		if(false == Line.isVertical(line.getX(0), line.getY(0), line.getX(1), line
				.getY(1), LINE_PRECISION))
		{
			throw new GameBoardLineIsNotVerticalException();
		}
	}
	
	/**
	 * Validates, that line is horizontal (The angle between line and horizontal line,
	 * that are drawed from the line top point, is inside given range
	 * 
	 * @param line
	 *            Line to validate
	 * @throws GameBoardLineIsNotHorizontalException 
	 */
	private void validateLineHorizontality(PolyLine line) throws GameBoardLineIsNotHorizontalException
	{
		if(false == Line.isHorizontal(line.getX(0), line.getY(0), line.getX(1), line
				.getY(1), LINE_PRECISION))
		{
			throw new GameBoardLineIsNotHorizontalException();
		}
	}
	
	/**
	 * Validates line length
	 * @param line Line to validate
	 * @throws GameBoardLineLengthException
	 */
	private void validateLineLength(PolyLine line) throws GameBoardLineLengthException
	{		
		float lengthInMM = Scale.auToMM((int) Line.length(line.getX(0), line
				.getY(0), line.getX(1), line.getY(1)));

		if (lengthInMM < 10) { // Less than one Centimeter
			throw new GameBoardLineLengthException(
					GameBoardLineLengthException.REASON_LINE_TO_SHORT);
		}
	}
	
	/**
	 * Resets board to the initial state
	 */
	public void reset() {
		this.firstVerticalLine = null;
		this.secondVerticalLine = null;
		this.firstHorizontalLine = null;
		this.secondHorizontalLine = null;
		
		this.board = new Vector(10);

		this.getContainer()
			.getLoggerComponent()
			.debug("[GameBoard] Reset executed");
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

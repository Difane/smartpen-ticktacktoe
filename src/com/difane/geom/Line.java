package com.difane.geom;
import com.livescribe.geom.Point;
import com.livescribe.util.MathFunctions;

public class Line {
	/**
	 * Checks line verticality. The angle of the line can differs from vertical
	 * to the anglePrecision value
	 * 
	 * @param x1
	 *            X-coord of the first line point
	 * @param y1
	 *            Y-coord of the first line point
	 * @param x2
	 *            X-coord of the second line point
	 * @param y2
	 *            Y-coord of the second line point
	 * @param anglePrecision
	 *            Angle precision in GRAD
	 * @return true, if line looks like vertical, false otherwise
	 */
	public static boolean isVertical(int x1, int y1, int x2, int y2,
			double anglePrecision) {

		double dx1 = x1;
		double dy1 = y1;
		double dx2 = x2;
		double dy2 = y2;
		
		double X = Math.abs(dx1-dx2);
		double Y = Math.abs(dy1-dy2);
		
		if (X == 0) {
			// Line is ideally vertical
			return true;
		}

		if (Y == 0) {
			// Line is ideally horizontal
			return false;
		}

		// Retrieving angle value
		double tanAngle = X / Y;
		double angle = MathFunctions.atan(tanAngle);
		
		// Retrieving angle precision in radians
		double anglePrecisionRad = (Math.PI / 180) * anglePrecision;

		if (Math.abs(angle) < Math.abs(anglePrecisionRad)) {
			// Line is vertical
			return true;
		} else {
			// Line is not vertical
			return false;
		}
	}
	
	/**
	 * Checks line horizontality. The angle of the line can differs from horizontal
	 * to the anglePrecision value
	 * 
	 * @param x1
	 *            X-coord of the first line point
	 * @param y1
	 *            Y-coord of the first line point
	 * @param x2
	 *            X-coord of the second line point
	 * @param y2
	 *            Y-coord of the second line point
	 * @param anglePrecision
	 *            Angle precision in GRAD
	 * @return true, if line looks like horizontal, false otherwise
	 */
	public static boolean isHorizontal(int x1, int y1, int x2, int y2,
			double anglePrecision) {

		double dx1 = x1;
		double dy1 = y1;
		double dx2 = x2;
		double dy2 = y2;
		
		double X = Math.abs(dx1-dx2);
		double Y = Math.abs(dy1-dy2);
		
		if (X == 0) {
			// Line is ideally vertical
			return false;
		}

		if (Y == 0) {
			// Line is ideally horizontal
			return true;
		}

		// Retrieving angle value
		double tanAngle = Y / X;
		double angle = MathFunctions.atan(tanAngle);
		
		// Retrieving angle precision in radians
		double anglePrecisionRad = (Math.PI / 180) * anglePrecision;

		if (Math.abs(angle) < Math.abs(anglePrecisionRad)) {
			// Line is horizontal
			return true;
		} else {
			// Line is not horizontal
			return false;
		}
	}
	
	/**
	 * Calculates line length
	 * 
	 * @param x1
	 *            X-coord of the first line point
	 * @param y1
	 *            Y-coord of the first line point
	 * @param x2
	 *            X-coord of the second line point
	 * @param y2
	 *            Y-coord of the second line point
	 * @return line length
	 */
	public static double length(int x1, int y1, int x2, int y2)
	{
		double dx1 = x1;
		double dy1 = y1;
		double dx2 = x2;
		double dy2 = y2;
		
		double length = Math.sqrt(MathFunctions.pow(dx2-dx1, 2)+MathFunctions.pow(dy2-dy1, 2));
		return length;
	}
	
	/**
	 * Computes the intersection between two lines.
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
	public static Point intersection(int x1, int y1, int x2, int y2, int x3, int y3,
			int x4, int y4) {

		double dx1 = x1;
		double dy1 = y1;
		double dx2 = x2;
		double dy2 = y2;
		double dx3 = x3;
		double dy3 = y3;
		double dx4 = x4;
		double dy4 = y4;

		double d = (dx1 - dx2) * (dy3 - dy4) - (dy1 - dy2) * (dx3 - dx4);
		if (d == 0)
			return null;

		double xi = ((dx3 - dx4) * (dx1 * dy2 - dy1 * dx2) - (dx1 - dx2)
				* (dx3 * dy4 - dy3 * dx4))
				/ d;
		double yi = ((dy3 - dy4) * (dx1 * dy2 - dy1 * dx2) - (dy1 - dy2)
				* (dx3 * dy4 - dy3 * dx4))
				/ d;

		return new Point((int) xi, (int) yi);
	}
}

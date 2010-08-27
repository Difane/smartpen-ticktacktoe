package com.difane.geom;
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
}

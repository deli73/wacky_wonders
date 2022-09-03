package xyz.sunrose.wacky_wonders.util;

import it.unimi.dsi.fastutil.floats.Float2FloatFunction;

public class CubicBezier {
	public static Float2FloatFunction cubic_bezier(double x1, double y1, double x2, double y2) {
		SplineInterpolator interpolator = new SplineInterpolator(x1, y1, x2, y2);
		return (t) -> {
			return (float) interpolator.interpolate(t);
		};
	}
}

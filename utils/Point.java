package edu.turtlekit2.warbot.duckingbear.utils;

public class Point {
	private int x;
	private int y;
	
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Point() {
		this(0, 0);
	}
	
	public int x() {
		return x;
	}
	
	public int y() {
		return y;
	}
	
	public int distance(Point p) {
		return (int) Math.sqrt((p.x - x)*(p.x - x) + (p.y - y)*(p.y - y));
	}
	
	/**
	 * Renvoie la direction de 'this' vers 'p'
	 */
	public double heading(Point p) {
		double radian = Math.atan2((p.y - y), (p.x - x));
		return Math.toDegrees(radian);
	}
	
	public String toString() {
		String s = "p(" + x + ", " + y + ")";
		
		return s;
	}
}

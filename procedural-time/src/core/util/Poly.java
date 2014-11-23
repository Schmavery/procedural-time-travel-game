package core.util;

import java.awt.Point;
import java.util.LinkedList;
import java.util.List;

public class Poly {
	LinkedList<Point> pts;
	
	// Note: We're living in Manhattan
	Point centerPt;
	int radius;

	public Poly(){
		pts = new LinkedList<>();
	}
	
	
	
	public void addPt(int x, int y){
		Point newPt = new Point(x, y);
		
		int max = Integer.MIN_VALUE;
		Point p1 = null, p2 = null;
		int tmp;
		
		// Recalc max interpoint distance
		for (Point pt : pts){
			tmp = calcManhattanDist(pt, newPt);
			if (max < tmp){
				max = tmp;
				p1 = pt;
				p2 = newPt;
			}
		}

		pts.add(newPt);
		radius = (max/2) + 1;
		centerPt = new Point(p1.x + (p2.x - p1.x), p1.y + (p2.y - p1.y));
	}
	
	private static int calcManhattanDist(Point p1, Point p2){
		return Math.abs(p2.x - p1.x) + Math.abs(p2.y - p1.y);
	}
	
	private static Point genSubtracted(Point p1, Point p2){
		return new Point(p1.x - p2.x, p1.y - p2.y);
	}

	private List<Point> getNormals(){
		List<Point> normals = new LinkedList<>();
		Point previous = pts.getLast();
		for (Point pt : pts){
			Point tmp = genSubtracted(pt, previous);
			// Get the perp of tmp
			tmp.setLocation(tmp.getY(), -tmp.getX()); 
			normals.add(tmp);
			previous = pt;
		}
		return normals;
	}
	
	private Point getProjection(Point line){
		int min = Integer.MAX_VALUE;
		int max = Integer.MIN_VALUE;
		for (Point pt : pts){
			// Calc dot prod
			int dot = pt.x*line.x + pt.y*line.y;
			min = (dot < min) ? dot : min;
			max = (dot > max) ? dot : max;
		}
		return new Point(min, max);
	}
	
	private static boolean intervalOverlap(Point int1, Point int2){
		return (int1.getX() < int2.getY() && int1.getY() > int2.getX());
	}
	
	// for all normals of this and poly
	// project each poly onto normal.
	// if they overlap, continue
	// otherwise return false
	public boolean overlaps(Poly poly){
		if (calcManhattanDist(centerPt, poly.centerPt) > (radius + poly.radius)){
			return false;
		}
		
		for (Point normal : this.getNormals()){
			if (!Poly.intervalOverlap(getProjection(normal), poly.getProjection(normal)))
				return false;
		}
		return true;
	}
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder("Poly: ");
		for (int i = 0; i < pts.size(); i++){
			if (i != 0){
				sb.append(",");
			}
			sb.append("("+pts.get(i).x+","+pts.get(i).y+")");
		}
		return sb.toString();
	}
}
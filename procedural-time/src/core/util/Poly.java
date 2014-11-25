package core.util;

import org.lwjgl.util.Point;
import java.util.LinkedList;
import java.util.List;

/**
 * General purpose Polygon class (using java.awt.Point)
 * with the ability to test for overlap with another
 * Polygon using the separating axis theor.
 * (Note: This only works for convex polygons)
 */
public class Poly {
	LinkedList<Point> pts;
	
	// Note: We're living in Manhattan
	Point centerPt;
	int radius;

	public Poly(){
		pts = new LinkedList<>();
	}
	
	
	
	public void addPoint(int x, int y){
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
		centerPt = new Point(p1.getX() + (p2.getX() - p1.getX()), p1.getY() + (p2.getY() - p1.getY()));
	}
	
	private static int calcManhattanDist(Point p1, Point p2){
		return Math.abs(p2.getX() - p1.getX()) + Math.abs(p2.getY() - p1.getY());
	}
	
	private static Point genSubtracted(Point p1, Point p2){
		return new Point(p1.getX() - p2.getX(), p1.getY() - p2.getY());
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
			int dot = pt.getX()*line.getX() + pt.getY()*line.getY();
			min = (dot < min) ? dot : min;
			max = (dot > max) ? dot : max;
		}
		return new Point(min, max);
	}
	
	private static boolean intervalOverlap(Point int1, Point int2){
		return (int1.getX() < int2.getY() && int1.getY() > int2.getX());
	}
	
	/**
	 * For all normals of this and poly,
	 * project each poly onto normal.
	 * If they overlap, continue,
	 * otherwise return false.
	 * @param poly
	 * @return true if the polygons overlap.
	 */
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
			sb.append("("+pts.get(i).getX()+","+pts.get(i).getY()+")");
		}
		return sb.toString();
	}
}
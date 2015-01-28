package entities.town;

import java.util.LinkedList;

import org.lwjgl.util.Point;


public class House {
	LinkedList<Point> doors;

	public House(int x, int y, int width, int height){
		doors = new LinkedList<>();
	}
	public void addDoor(Point gridPt){
		doors.add(gridPt);
	}
}

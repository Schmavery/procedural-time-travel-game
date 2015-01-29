package entities.town;

import java.util.LinkedList;

import org.lwjgl.util.Point;
import org.lwjgl.util.Rectangle;


public class House {
	LinkedList<Point> doors;
	Rectangle rect;

	public House(Rectangle rect){
		doors = new LinkedList<>();
		this.rect = rect;
	}
	public void addDoor(Point gridPt){
		doors.add(gridPt);
	}
	public Rectangle getRect(){
		return rect;
	}
}
